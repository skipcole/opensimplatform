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
 * @author Ronald "Skip" Cole
 * 
 * This file is part of the USIP Online Simulation Platform.<br>
 * 
 * The USIP Online Simulation Platform is free software; you can redistribute it
 * and/or modify it under the terms of the new BSD Style license associated with
 * this distribution.<br>
 * 
 * The USIP Online Simulation Platform is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. <BR>
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

	@OneToMany
	@JoinColumn(name = "RUNNING_SIM_ID")
	private List<UserAssignment> user_assignments = new ArrayList<UserAssignment>();

	@OneToMany
	@JoinColumn(name = "RUNNING_SIM_ID")
	private List<IntVariable> var_int = new ArrayList<IntVariable>();

	/*
	 * @OneToMany @JoinColumn(name = "RUNNING_SIM_ID") private List<Conversation>
	 * conversations = new ArrayList<Conversation>();
	 */

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

	/**
	 * 
	 * @param from
	 * @param email_users
	 * @param emailText
	 * @return
	 */
	public void enableAndPrep(String schema, String sid, String from,
			String email_users, String emailText) {

		Long schema_id = SchemaInformationObject.lookUpId(schema);

		System.out.println("Enabling Sim.");

		// Create objects to hold the data
		Simulation sim = (Simulation) MultiSchemaHibernateUtil.getSession(
				schema).get(Simulation.class, new Long(sid));

		copyInData(sim, MultiSchemaHibernateUtil.getSession(schema));

		doFinalChecksOnSim(sim, MultiSchemaHibernateUtil.getSession(schema));

		// Email if desired
		if ((email_users != null) && (email_users.equalsIgnoreCase("true"))) {
			System.out.println("sending welcome emails");

			System.out.println("sending from " + from);
			sendWelcomeEmail(schema_id, from, emailText,
					MultiSchemaHibernateUtil.getSession(schema));

		}

		// Mark it ready to go
		this.ready_to_begin = true;

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

	}

	public void doFinalChecksOnSim(Simulation sim,
			org.hibernate.Session hibernate_session) {

		// Making sure all actors added to broadcast chat
		for (ListIterator<Conversation> lc = sim.getConversations()
				.listIterator(); lc.hasNext();) {
			Conversation conv = (Conversation) lc.next();

			if ((conv.getConversation_name() != null)
					&& (conv.getConversation_name()
							.equalsIgnoreCase("broadcast"))) {

				conv
						.setConv_actor_assigns(new ArrayList<ConvActorAssignment>());

				// loop over simulation actors
				for (ListIterator<Actor> la = sim.getActors().listIterator(); la
						.hasNext();) {
					Actor act = (Actor) la.next();

					ConvActorAssignment caa = new ConvActorAssignment();
					caa.setActor_id(act.getId());
					caa.setConv_id(conv.getId());

					hibernate_session.save(caa);
					conv.getConv_actor_assigns().add(caa);
				}

				hibernate_session.save(conv);
			}

		}

	}

	public void copyInData(Simulation sim,
			org.hibernate.Session hibernate_session) {

		// Load the starter text.
		this.aar_text = sim.getAar_starter_text();

		this.phase_id = sim.getFirstPhaseId();

		// //////////////////////////////////////////////////////////////////////
		// Copy over conversations
		/*
		 * this.setConversations(new ArrayList<Conversation>()); for
		 * (ListIterator<Conversation> lc =
		 * sim.getConversations().listIterator(); lc.hasNext();){ Conversation
		 * sim_conv = (Conversation) lc.next();
		 * 
		 * Conversation rs_conv = sim_conv.createCopy(this.getId(),
		 * hibernate_session);
		 * 
		 * this.getConversations().add(rs_conv); }
		 */
		// //////////////////////////////////////////////////////////////////////
		// //////////////////////////////////////////////////////////////////////
		// Copy over variables
		this.setVar_int(new ArrayList<IntVariable>());
		for (ListIterator<IntVariable> li = sim.getVar_int().listIterator(); li
				.hasNext();) {
			IntVariable iv = (IntVariable) li.next();

			IntVariable iv_rs = iv.createCopy(this.getId(), hibernate_session);

			this.getVar_int().add(iv_rs);
		}
		// /////////////////////////////////////////////////////////////////////////

		for (ListIterator<SharedDocument> li = SharedDocument.getAllForSim(
				hibernate_session, sim.getId()).listIterator(); li.hasNext();) {
			SharedDocument sd = (SharedDocument) li.next();

			sd.createCopy(this.id, hibernate_session);

		}

		hibernate_session.saveOrUpdate(this);
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
	public void sendWelcomeEmail(Long schema_id, String from, String emailText,
			org.hibernate.Session hibernate_session) {

		emailText = emailText.replace("[web_site_location]",
				Emailer.simulation_url);

		for (ListIterator<UserAssignment> li = user_assignments.listIterator(); li
				.hasNext();) {
			UserAssignment ua = (UserAssignment) li.next();

			String this_guys_emailText = emailText;

			User user = (User) hibernate_session.get(User.class, ua
					.getUser_id());

			MultiSchemaHibernateUtil.beginTransaction(
					MultiSchemaHibernateUtil.principalschema, true);
			BaseUser bu = (BaseUser) MultiSchemaHibernateUtil.getSession(
					MultiSchemaHibernateUtil.principalschema, true).get(
					BaseUser.class, ua.getUser_id());
			MultiSchemaHibernateUtil
					.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

			this_guys_emailText = this_guys_emailText.replace("[username]", bu
					.getUsername());

			this_guys_emailText = this_guys_emailText.replace("[password]", bu
					.getPassword());

			String fullEmail;

			if ((bu.getFull_name() != null)
					&& (bu.getFull_name().trim().length() > 0)) {
				fullEmail = "Dear " + bu.getFull_name() + ",\r\n";
			} else {
				fullEmail = "Dear Player, " + "\r\n";
			}

			fullEmail += this_guys_emailText;

			System.out.println("emailing : " + bu.getUsername());

			String cc = null;
			String bcc = from;
			
			Emailer.postSimReadyMail(schema_id, bu.getUsername(), from, cc,
					bcc, "Simulation Starting", fullEmail);

		}

	}

	public List<RunningSimulation> getAll(
			org.hibernate.Session hibernate_session) {

		return (hibernate_session.createQuery("from RunningSimulation").list());
	}

	public List<RunningSimulation> getAllForSim(String simid,
			org.hibernate.Session hibernate_session) {

		List<RunningSimulation> returnList = hibernate_session.createQuery(
				"from RunningSimulation where sim_id = " + simid).list();

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

	public List<IntVariable> getVar_int() {
		return var_int;
	}

	public void setVar_int(List<IntVariable> var_int) {
		this.var_int = var_int;
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
	public static String getActorAlertText(String schema, Long rs_id,
			Long act_id) {

		String returnString = "";

		MultiSchemaHibernateUtil.beginTransaction(schema);

		RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil
				.getSession(schema).get(RunningSimulation.class, rs_id);

		for (ListIterator<Alert> la = rs.getAlerts().listIterator(); la
				.hasNext();) {
			Alert al = (Alert) la.next();

			al = (Alert) MultiSchemaHibernateUtil.getSession(schema).get(
					Alert.class, al.getId());
			if (al.checkActor(act_id)) {
				returnString += ("<P>" + al.getAlertMessage() + "</P>");
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
