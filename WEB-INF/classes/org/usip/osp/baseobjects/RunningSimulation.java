package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.communications.CommunicationsHub;
import org.usip.osp.communications.ConvActorAssignment;
import org.usip.osp.communications.Conversation;
import org.usip.osp.communications.Alert;
import org.usip.osp.communications.Emailer;
import org.usip.osp.persistence.BaseUser;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.specialfeatures.InventoryItem;
import org.apache.log4j.*;

/**
 * This class represents a simulation in play.
 */
/*
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it
 * and/or modify it under the terms of the new BSD Style license associated with
 * this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. <BR>
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

	/** Id of the instructor that created this running simulation. */
	private Long creator_id;

	@Column(name = "RS_NAME")
	private String name;

	/** Indicates if running sim can be changed by change packets. */
	private boolean allowExternallySumbittedChanges = false;

	/**
	 * Running simulations that can receive change request packets can be
	 * password protected.
	 */
	private String rs_password;

	/** The id of the current phase. */
	@Column(name = "RS_PHASEID")
	private Long phase_id;

	@Column(name = "RS_READY")
	private boolean ready_to_begin = false;

	@Column(name = "RS_DONE")
	private boolean completed = false;

	private boolean inactivated = false;

	@Column(name = "INACTIVATED_DATE", columnDefinition = "datetime")
	@GeneratedValue
	private Date inactivatedDate;

	public boolean isInactivated() {
		return inactivated;
	}

	public void setInactivated(boolean inactivated) {
		this.inactivated = inactivated;
		this.setInactivatedDate(new Date());
	}

	public Date getInactivatedDate() {
		return inactivatedDate;
	}

	public void setInactivatedDate(Date inactivatedDate) {
		this.inactivatedDate = inactivatedDate;
	}

	@Column(name = "ENABLED_DATE", columnDefinition = "datetime")
	@GeneratedValue
	private Date enabledDate;

	public Date getEnabledDate() {
		return enabledDate;
	}

	public void setEnabledDate(Date enabledDate) {
		this.enabledDate = enabledDate;
	}

	@Column(name = "COMPLETION_DATE", columnDefinition = "datetime")
	@GeneratedValue
	private Date completionDate;

	@Column(name = "RS_ROUND")
	private int round = 0;

	@Lob
	private String schedule = ""; //$NON-NLS-1$

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	@Column(name = "RS_AAR")
	@Lob
	private String aar_text = ""; //$NON-NLS-1$

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
	public RunningSimulation(String name, Simulation sim, String schema,
			Long creator_id, String creator_name) {

		this.name = name;
		this.aar_text = sim.getAarStarterText();
		this.phase_id = sim.getFirstPhaseId(schema);
		this.sim_id = sim.getId();

		this.saveMe(schema);

		// Create the dependent object (shared documents, conversations,
		// variables, etc.)
		createRunningSimObjects(schema, sim);

	}

	/**
	 * 
	 * @param schema
	 * @param rs_id
	 * @return
	 */
	public static RunningSimulation getById(String schema, Long rs_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		RunningSimulation this_rs = (RunningSimulation) MultiSchemaHibernateUtil
				.getSession(schema).get(RunningSimulation.class, rs_id);

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
	public void enableAndPrep(String schema, String sid, String from,
			String email_users, String emailText) {

		Logger.getRootLogger().debug("Enabling Sim."); //$NON-NLS-1$

		Simulation sim = Simulation.getById(schema, new Long(sid));

		// Email if desired
		if ((email_users != null) && (email_users.equalsIgnoreCase("true"))) { //$NON-NLS-1$
			Logger.getRootLogger().debug("sending welcome emails"); //$NON-NLS-1$

			Logger.getRootLogger().debug("sending from " + from); //$NON-NLS-1$
			sendWelcomeEmail(schema, from, emailText);

		}

		// Mark it ready to go
		this.ready_to_begin = true;
		this.setEnabledDate(new java.util.Date());

		Alert startEvent = new Alert();
		startEvent.setSim_id(sim.getId());
		startEvent.setRunning_sim_id(this.getId());
		startEvent.setType(Alert.TYPE_RUN_ENABLED);
		startEvent.setAlertMessage("Simulation Enabled.");
		startEvent.setTimeOfAlert(this.getEnabledDate());
		startEvent.saveMe(schema);
		CommunicationsHub ch = new CommunicationsHub(startEvent, schema);

		this.saveMe(schema);

	}

	/**
	 * Loops over the base objects for the simulation and create running simulation 
	 * versions of them.
	 * 
	 * @param schema
	 * @param sim
	 */
	public void createRunningSimObjects(String schema, Simulation sim) {

		// Get all of the dependent object assignments for this simulation
		List depObjectAssignments = BaseSimSectionDepObjectAssignment
				.getSimDependencies(schema, sim.getId());

		// Create a table to list all of the objects we have created.
		Hashtable uniqueSimObjects = new Hashtable();		

		// Loop over dependent object assignments found for this simulation
		for (ListIterator<BaseSimSectionDepObjectAssignment> lc = depObjectAssignments
				.listIterator(); lc.hasNext();) {
			BaseSimSectionDepObjectAssignment bssdoa = lc.next();

			// The unique key is to prevent us from creating multiple objects
			// for one object.
			String uniqueKey = bssdoa.getClassName()
					+ "_" + bssdoa.getObjectId(); //$NON-NLS-1$

			// Try to get a string from the list using the above key. (If found,
			// we have already
			// created this object.
			Long createdObjId = (Long) uniqueSimObjects.get(uniqueKey);

			Long thisRSVersionsId = null;

			if (createdObjId == null) {

				try {
					Class objClass = Class.forName(bssdoa.getClassName());

					// We start and finish the transaction here since the next
					// step will also involve a transaction.
					MultiSchemaHibernateUtil.beginTransaction(schema);
					SimSectionDependentObject template_obj = (SimSectionDependentObject) MultiSchemaHibernateUtil
							.getSession(schema).get(objClass,
									bssdoa.getObjectId());
					MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

					// Create object - uses its own hibernate transaction to create object.
					thisRSVersionsId = template_obj.createRunningSimVersion(
							schema, sim.getId(), this.id, template_obj);

				} catch (Exception er) {
					er.printStackTrace();
				}

				if (thisRSVersionsId == null) {
					System.out.println("bad add on: " + uniqueKey);
				} else {
					// Add the objects to a hashtable based on the base template object
					// (so template object shared by multiple sections only
					// contribute one new object).
					uniqueSimObjects.put(uniqueKey, thisRSVersionsId);
				}
			} else {
				thisRSVersionsId = createdObjId;
			}

			SimSectionRSDepOjbectAssignment ssrsdoa = new SimSectionRSDepOjbectAssignment();
			ssrsdoa.setClassName(bssdoa.getClassName());
			ssrsdoa.setObjectId(thisRSVersionsId);
			ssrsdoa.setRs_id(this.id);
			ssrsdoa.setSim_id(this.sim_id);
			ssrsdoa.setSection_id(bssdoa.getBss_id());
			ssrsdoa.setSSRSDOA_Index(bssdoa.getDepObjIndex());
			ssrsdoa.setUniqueTagName(bssdoa.getUniqueTagName());

			ssrsdoa.saveMe(schema);

		}
		
		// Get objects that may not have been assigned.
		List iItems = InventoryItem.getAllActorAssignedForSim(schema, this.sim_id);
		for (ListIterator<InventoryItem> iList = iItems.listIterator(); iList.hasNext();) {
			InventoryItem this_item = iList.next();
			
			//Make copy for this running sim.
			this_item.createRunningSimVersion(schema, this.sim_id, this.getId(), this_item);
			
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

		emailText = emailText
				.replace(
						"[web_site_location]", USIP_OSP_Properties.getValue("simulation_url") + "simulation/"); //$NON-NLS-1$

		Hashtable <Long, String> usersEmailed = new Hashtable<Long, String>();

		for (ListIterator<UserAssignment> li = getUser_assignments(schema,
				this.id).listIterator(); li.hasNext();) {
			UserAssignment ua = li.next();

			// If we have not emailed this person yet.
			if (usersEmailed.get(ua.getUser_id()) == null) { 

				String this_guys_emailText = emailText;

				// /////////////////////////////////////
				MultiSchemaHibernateUtil.beginTransaction(
						MultiSchemaHibernateUtil.principalschema, true);
				BaseUser bu = (BaseUser) MultiSchemaHibernateUtil.getSession(
						MultiSchemaHibernateUtil.principalschema, true).get(
						BaseUser.class, ua.getUser_id());
				MultiSchemaHibernateUtil
						.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
				// ///////////////////////////////////////

				this_guys_emailText = this_guys_emailText.replace(
						"[username]", bu.getUsername()); //$NON-NLS-1$

				String fullEmail;

				if ((bu.getFull_name() != null)
						&& (bu.getFull_name().trim().length() > 0)) {
					fullEmail = "Dear " + bu.getFull_name() + ",\r\n"; //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					fullEmail = "Dear Player, " + "\r\n"; //$NON-NLS-1$ //$NON-NLS-2$
				}

				fullEmail += this_guys_emailText;

				Logger.getRootLogger().debug("emailing : " + bu.getUsername()); //$NON-NLS-1$

				String cc = null;
				String bcc = from;

				Emailer.postSimReadyMail(schema, bu.getUsername(), from, cc,
						bcc, "Simulation Starting", fullEmail); //$NON-NLS-1$

				usersEmailed.put(ua.getUser_id(), "set");

			}

		}

	}

	public List<RunningSimulation> getAll(
			org.hibernate.Session hibernate_session) {

		return (hibernate_session.createQuery("from RunningSimulation").list()); //$NON-NLS-1$
	}

	/**
	 * Returns a list of all running sims created for a simulation.
	 * 
	 * @param simid
	 * @param schema
	 * @return
	 */
	public static List<RunningSimulation> getAllForSim(String simid,
			String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<RunningSimulation> returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery("from RunningSimulation where sim_id = :sim_id").setString("sim_id", simid).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(Long creator_id) {
		this.creator_id = creator_id;
	}

	public String getRunningSimulationName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPhase_id() {
		return this.phase_id;
	}

	public void setPhase_id(Long pid) {
		this.phase_id = pid;
	}

	public boolean isAllowExternallySumbittedChanges() {
		return this.allowExternallySumbittedChanges;
	}

	public void setAllowExternallySumbittedChanges(
			boolean allowExternallySumbittedChanges) {
		this.allowExternallySumbittedChanges = allowExternallySumbittedChanges;
	}

	public String getRs_password() {
		return this.rs_password;
	}

	public void setRs_password(String rs_password) {
		this.rs_password = rs_password;
	}

	public List<UserAssignment> getUser_assignments(String schema) {

		return UserAssignment.getAllForRunningSim(schema, this.id);

	}

	public List<UserAssignment> getUser_assignments(String schema, Long rsid) {

		return UserAssignment.getAllForRunningSim(schema, rsid);

	}

	public boolean isReady_to_begin() {
		return this.ready_to_begin;
	}

	public void setReady_to_begin(boolean ready_to_begin) {
		this.ready_to_begin = ready_to_begin;
	}

	public int getRound() {
		return this.round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public String getAar_text() {
		return this.aar_text;
	}

	public void setAar_text(String aar_text) {
		this.aar_text = aar_text;
	}

	private Hashtable grabBag = new Hashtable();

	public Hashtable getGrabBag() {
		return this.grabBag;
	}

	public void setGrabBag(Hashtable grabBag) {
		this.grabBag = grabBag;
	}

	public Long getSim_id() {
		return this.sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public boolean isCompleted() {
		return this.completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Date getCompletionDate() {
		return this.completionDate;
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
	public static String getActorAlertText(String schema, Long rs_id,
			Long act_id) {

		String returnString = ""; //$NON-NLS-1$

		List alerts = Alert.getAllForRunningSim(schema, rs_id);

		// Get list iterator positioned at the end.
		ListIterator<Alert> la = alerts.listIterator(alerts.size());

		MultiSchemaHibernateUtil.beginTransaction(schema);

		while (la.hasPrevious()) {
			Alert al = la.previous();

			al = (Alert) MultiSchemaHibernateUtil.getSession(schema).get(
					Alert.class, al.getId());
			if (al.checkActor(act_id)) {
				returnString += ("<B>" + al.getTimeOfAlert() + "</B><BR>" + al.getAlertMessage() + "<hr>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
