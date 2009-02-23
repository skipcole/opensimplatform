package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.*;

import org.hibernate.Session;
import org.hibernate.annotations.Proxy;
import org.usip.osp.communications.ConvActorAssignment;
import org.usip.osp.communications.Conversation;
import org.usip.osp.communications.Alert;
import org.usip.osp.communications.Emailer;
import org.usip.osp.communications.SharedDocument;
import org.usip.osp.persistence.BaseUser;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.persistence.SchemaInformationObject;
import org.usip.osp.specialfeatures.*;

/**
 * This class represents a simulation in play.
 * 
 * @author Ronald "Skip" Cole<br />
 * 
 *         This file is part of the USIP Open Simulation Platform.<br>
 * 
 *         The USIP Open Simulation Platform is free software; you can
 *         redistribute it and/or modify it under the terms of the new BSD Style
 *         license associated with this distribution.<br>
 * 
 *         The USIP Open Simulation Platform is distributed WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "RUNNING_SIM")
@Proxy(lazy = false)
public class RunningSimulation {

	/** Database id of this Running Simulation. */
	@Id
	@GeneratedValue
	@Column(name = "RUNNING_SIM_ID")
	private Long id;

	@Column(name = "SIM_ID")
	private Long sim_id;

	@Column(name = "RS_NAME")
	private String name;

	/** Indicates if running sim can be changed by change packets. */
	private boolean allowExternallySumbittedChanges = false;

	/**
	 * Running simulations that can receive change request packets can be
	 * password protected.
	 */
	private String rs_password;

	@OneToMany
	@JoinColumn(name = "RUNNING_SIM_ID")
	private List<UserAssignment> user_assignments = new ArrayList<UserAssignment>();

	/** The id of the current phase. */
	@Column(name = "RS_PHASEID")
	private Long phase_id;

	@Column(name = "RS_READY")
	private boolean ready_to_begin = false;

	@Column(name = "RS_DONE")
	private boolean completed = false;

	@Column(name = "COMPLETION_DATE", columnDefinition = "datetime")
	@GeneratedValue
	private Date completionDate;

	@Column(name = "RS_ROUND")
	private int round = 0;

	@OneToMany
	private List<Alert> alerts = new ArrayList<Alert>();

	@Column(name = "RS_AAR")
	@Lob
	private String aar_text = "";

	/** Zero argument constructor required by hibernate. */
	public RunningSimulation() {

	}

	/**
	 * 
	 * @param name
	 * @param phase_id
	 * @param sim
	 * @param schema
	 */
	public RunningSimulation(String name, Simulation sim, String schema) {

		this.name = name;
		this.aar_text = sim.getAar_starter_text();
		this.phase_id = sim.getFirstPhaseId(schema);
		this.sim_id = sim.getId();

		this.saveMe(schema);
		
		// Create the dependent object (shared documents, conversations, variables, etc.)
		createRunningSimObjects(schema, sim);
		this.createMyVariables(schema, sim);

	}

	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static RunningSimulation getMe(String schema, Long rs_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		RunningSimulation this_rs = (RunningSimulation) MultiSchemaHibernateUtil.getSession(schema).get(
				RunningSimulation.class, rs_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this_rs;

	}

	/**
	 * 
	 * @param from
	 * @param email_users
	 * @param emailText
	 * @return
	 */
	public void enableAndPrep(String schema, String sid, String from, String email_users, String emailText) {

		System.out.println("Enabling Sim.");

		Simulation sim = Simulation.getMe(schema, new Long(sid));


		doFinalChecksOnSim(sim, schema);

		// Email if desired
		if ((email_users != null) && (email_users.equalsIgnoreCase("true"))) {
			System.out.println("sending welcome emails");

			System.out.println("sending from " + from);
			sendWelcomeEmail(schema, from, emailText);

		}

		// Mark it ready to go
		this.ready_to_begin = true;

		this.saveMe(schema);

	}

	/**
	 * Loops over the 
	 * @param schema
	 * @param sim
	 */
	public void createRunningSimObjects(String schema, Simulation sim) {

		// Get all of the dependent object assignments for this simulation
		List doAss = BaseSimSectionDepObjectAssignment.getSimDependencies(schema, sim.getId());

		// Create a table to list all of the objects we have created.
		Hashtable uniqueSimObjects = new Hashtable();

		// Loop over dependent object assignments found for this simulation
		for (ListIterator<BaseSimSectionDepObjectAssignment> lc = doAss.listIterator(); lc.hasNext();) {
			BaseSimSectionDepObjectAssignment bssdoa = (BaseSimSectionDepObjectAssignment) lc.next();
			
			// The unique key is to prevent us from creating multiple objects for one object.
			String uniqueKey = bssdoa.getClassName() + "_" + bssdoa.getObjectId();
			
			// Try to get a string from the list using the above key. (If found, we have already 
			// created this object.
			Long createdObjId = (Long) uniqueSimObjects.get(uniqueKey);
			
			Long thisRSVersionsId = null;
			
			if (createdObjId == null){
				
				try {
					Class objClass = Class.forName(bssdoa.getClassName());
					
					MultiSchemaHibernateUtil.beginTransaction(schema);
					SimSectionDependentObject template_obj = (SimSectionDependentObject)
						MultiSchemaHibernateUtil.getSession(schema).get(objClass, bssdoa.getObjectId());
					MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
					
					// Create object
					thisRSVersionsId = template_obj.createRunningSimVersion(schema, 
							bssdoa.getObjectId(), this.id, template_obj);
					
				} catch (Exception er) {
					er.printStackTrace();
				}

				// Add the objects to a hashtable based on the base template object
				// (so template object shared by multiple sections only contribute one new object).
				uniqueSimObjects.put(uniqueKey,thisRSVersionsId);
				
			} else {
				thisRSVersionsId = createdObjId;
			}
			
			SimSectionRSDepOjbectAssignment ssrsdoa = new SimSectionRSDepOjbectAssignment();
			ssrsdoa.setClassName(bssdoa.getClassName());
			ssrsdoa.setObjectId(thisRSVersionsId);
			ssrsdoa.setRs_id(this.id);
			ssrsdoa.setSim_id(sim_id);
			ssrsdoa.setSection_id(bssdoa.getBss_id());
			ssrsdoa.setSSRSDOA_Index(bssdoa.getDepObjIndex());
			ssrsdoa.setUniqueTagName(bssdoa.getUniqueTagName());
			
			ssrsdoa.saveMe(schema);

		}

	}

	public void doFinalChecksOnSim(Simulation sim, String schema) {

		// Making sure all actors added to broadcast chat
		for (ListIterator<Conversation> lc = sim.getConversations(schema).listIterator(); lc.hasNext();) {
			Conversation conv = (Conversation) lc.next();

			if ((conv.getConversation_name() != null) && (conv.getConversation_name().equalsIgnoreCase("broadcast"))) {

				MultiSchemaHibernateUtil.beginTransaction(schema);

				conv.setConv_actor_assigns(new ArrayList<ConvActorAssignment>());

				// loop over simulation actors
				for (ListIterator<Actor> la = sim.getActors(schema).listIterator(); la.hasNext();) {
					Actor act = (Actor) la.next();

					ConvActorAssignment caa = new ConvActorAssignment();
					caa.setActor_id(act.getId());
					caa.setConv_id(conv.getId());

					MultiSchemaHibernateUtil.getSession(schema).save(caa);
					conv.getConv_actor_assigns(schema).add(caa);
				}

				MultiSchemaHibernateUtil.getSession(schema).save(conv);

				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
			}

		}

	}

	/**
	 * Creates copies of all of the variables held by the sim, so one is
	 * available for this running sim.
	 * 
	 * @param schema
	 * @param sim
	 */
	private void createMyVariables(String schema, Simulation sim) {

		for (ListIterator<GenericVariable> li = GenericVariable.getAllBaseGenericVariablesForSim(schema, sim.getId())
				.listIterator(); li.hasNext();) {
			GenericVariable gv = (GenericVariable) li.next();

			GenericVariable gv_copy = gv.createCopy(this.id, schema);

		}

	}

	/**
	 * Email sent from the simulation creator to the player. The simulation
	 * creator is also copied on the email. If the administrator has set a an
	 * email archive address it will also receive a copy.
	 * 
	 * @param from
	 * @param to
	 * @param emailText
	 */
	public void sendWelcomeEmail(String schema, String from, String emailText) {

		emailText = emailText.replace("[web_site_location]", Emailer.simulation_url);

		for (ListIterator<UserAssignment> li = user_assignments.listIterator(); li.hasNext();) {
			UserAssignment ua = (UserAssignment) li.next();

			String this_guys_emailText = emailText;

			// /////////////////////////////////////
			MultiSchemaHibernateUtil.beginTransaction(schema);
			User user = (User) MultiSchemaHibernateUtil.getSession(schema).get(User.class, ua.getUser_id());
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
			// /////////////////////////////////////
			MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema, true);
			BaseUser bu = (BaseUser) MultiSchemaHibernateUtil
					.getSession(MultiSchemaHibernateUtil.principalschema, true).get(BaseUser.class, ua.getUser_id());
			MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
			// ///////////////////////////////////////

			this_guys_emailText = this_guys_emailText.replace("[username]", bu.getUsername());

			this_guys_emailText = this_guys_emailText.replace("[password]", bu.getPassword());

			String fullEmail;

			if ((bu.getFull_name() != null) && (bu.getFull_name().trim().length() > 0)) {
				fullEmail = "Dear " + bu.getFull_name() + ",\r\n";
			} else {
				fullEmail = "Dear Player, " + "\r\n";
			}

			fullEmail += this_guys_emailText;

			System.out.println("emailing : " + bu.getUsername());

			String cc = null;
			String bcc = from;

			Emailer.postSimReadyMail(schema, bu.getUsername(), from, cc, bcc, "Simulation Starting", fullEmail);

		}

	}

	public List<RunningSimulation> getAll(org.hibernate.Session hibernate_session) {

		return (hibernate_session.createQuery("from RunningSimulation").list());
	}

	/**
	 * Returns a list of all running sims created for a simulation.
	 * 
	 * @param simid
	 * @param schema
	 * @return
	 */
	public static List<RunningSimulation> getAllForSim(String simid, String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<RunningSimulation> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from RunningSimulation where sim_id = " + simid).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPhase_id() {
		return phase_id;
	}

	public void setPhase_id(Long pid) {
		this.phase_id = pid;
	}

	public boolean isAllowExternallySumbittedChanges() {
		return allowExternallySumbittedChanges;
	}

	public void setAllowExternallySumbittedChanges(boolean allowExternallySumbittedChanges) {
		this.allowExternallySumbittedChanges = allowExternallySumbittedChanges;
	}

	public String getRs_password() {
		return rs_password;
	}

	public void setRs_password(String rs_password) {
		this.rs_password = rs_password;
	}

	public List<UserAssignment> getUser_assignments() {
		return user_assignments;
	}

	public void setUser_assignments(List<UserAssignment> user_assignments) {
		this.user_assignments = user_assignments;
	}

	public boolean isReady_to_begin() {
		return ready_to_begin;
	}

	public void setReady_to_begin(boolean ready_to_begin) {
		this.ready_to_begin = ready_to_begin;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public String getAar_text() {
		return aar_text;
	}

	public void setAar_text(String aar_text) {
		this.aar_text = aar_text;
	}

	private Hashtable grabBag = new Hashtable();

	public Hashtable getGrabBag() {
		return grabBag;
	}

	public void setGrabBag(Hashtable grabBag) {
		this.grabBag = grabBag;
	}

	public List<Alert> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<Alert> alerts) {
		this.alerts = alerts;
	}

	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	/**
	 * Gets the alert text that is applicable for this actor for this running
	 * sim.
	 */
	public static String getActorAlertText(String schema, Long rs_id, Long act_id) {

		String returnString = "";

		MultiSchemaHibernateUtil.beginTransaction(schema);

		RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil.getSession(schema).get(
				RunningSimulation.class, rs_id);

		// Get list iterator positioned at the end.
		ListIterator<Alert> la = rs.getAlerts().listIterator(rs.getAlerts().size());

		while (la.hasPrevious()) {
			Alert al = (Alert) la.previous();

			al = (Alert) MultiSchemaHibernateUtil.getSession(schema).get(Alert.class, al.getId());
			if (al.checkActor(act_id)) {
				returnString += ("<B>" + al.getTimeOfAlert() + "</B><BR>" + al.getAlertMessage() + "<hr>");
			}
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		return returnString;

	}

	/*
	 * Dont re-add this. it doesn't need it. public List<Conversation>
	 * getConversations() { return conversations; }
	 * 
	 * public void setConversations(List<Conversation> conversations) {
	 * this.conversations = conversations; }
	 */
}
