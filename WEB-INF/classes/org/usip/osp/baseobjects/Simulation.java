package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.communications.Conversation;
import org.usip.osp.persistence.*;
import org.usip.osp.specialfeatures.*;

/**
 * @author Ronald "Skip" Cole
 * 
 *         This file is part of the USIP Online Simulation Platform.<br>
 * 
 *         The USIP Online Simulation Platform is free software; you can
 *         redistribute it and/or modify it under the terms of the new BSD Style
 *         license associated with this distribution.<br>
 * 
 *         The USIP Online Simulation Platform is distributed WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "SIMULATIONS")
@Proxy(lazy = false)
public class Simulation {

	public static void main(String args[]) {

		String schema = "usiposcw";

		System.out.println("begin");

		Simulation s1 = new Simulation();
		s1.setName("sim_one");

		Actor a1 = new Actor();
		a1.setName("billi bob");

		s1.getActors().add(a1);

		SimulationPhase sp = new SimulationPhase();
		sp.setName("in progress");
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(sp);

		s1.getPhases().add(sp);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(a1);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(s1);

		RunningSimulation rs = new RunningSimulation();
		rs.setName("a running sim name");

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(rs);

		s1.getRunning_sims().add(rs);

		/*
		 * User u = new User();
		 * 
		 * MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(u);
		 * 
		 * UserAssignment ua = new UserAssignment();
		 * 
		 * ua.setActor_id(a1.getId()); ua.setRunning_sim_id(rs.getId());
		 * ua.setSim_id(s1.getId()); ua.setUser_id(u.getId());
		 * 
		 * MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(ua);
		 */
		List lua = new ArrayList();

		// new UserAssignment().getAllForUser(u.getId());

		for (ListIterator<UserAssignment> li = lua.listIterator(); li.hasNext();) {
			UserAssignment this_ua = (UserAssignment) li.next();

			System.out.println("sim_id is " + this_ua.getSim_id());
			System.out.println("r_sim_id is " + this_ua.getRunning_sim_id());
			System.out.println("a_id is " + this_ua.getActor_id());

			User uu = UserAssignment.getUserAssigned(schema, new Long(1),
					new Long(1));

		}

		/*
		 * List l = new Simulation().getAll(String schema);
		 * 
		 * for (ListIterator li = l.listIterator(); li.hasNext();) { Simulation
		 * s = (Simulation) li.next(); System.out.println("s name is " +
		 * s.getName());
		 * 
		 * Simulation y = (Simulation) TheHibernator.getMe(Simulation.class,
		 * s.getId());
		 * 
		 * System.out.println("s name now is: " + y.getName());
		 * 
		 * for (ListIterator sActors = s.actors.listIterator();
		 * sActors.hasNext();){ Actor act = (Actor) sActors.next();
		 * System.out.println(" actor name is " + act.getName()); } }
		 */
		// List p = new SimulationPhase().getAllForSim(s1.getId());
	}

	/** A simulation will have actors. */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "SIMULATION_ACTOR", joinColumns = { @JoinColumn(name = "SIM_ID") }, inverseJoinColumns = { @JoinColumn(name = "ACTOR_ID") })
	private List<Actor> actors = new ArrayList<Actor>();

	@OneToMany
	@JoinColumn(name = "SIM_ID")
	private List<SimulationPhase> phases = new ArrayList<SimulationPhase>();

	@OneToMany
	@JoinColumn(name = "SIM_ID")
	private List<RunningSimulation> running_sims = new ArrayList<RunningSimulation>();

	@OneToMany
	@JoinColumn(name = "SIM_ID")
	private List<IntVariable> var_int = new ArrayList<IntVariable>();

	@OneToMany
	@JoinColumn(name = "SIM_ID")
	private List<CustomizeableSection> customized_sections = new ArrayList<CustomizeableSection>();

	@OneToMany
	@JoinColumn(name = "SIM_ID")
	private List<Conversation> conversations = new ArrayList<Conversation>();

	/** Database id of this Simulation. */
	@Id
	@GeneratedValue
	@Column(name = "SIM_ID")
	private Long id;

	/** Name of this Simulation. */
	@Column(name = "SIM_NAME")
	private String name = "";

	/** Version of this Simulation. */
	@Column(name = "SIM_VERSION")
	private String version = "";

	/** Introduction to this Simulation. */
	@Column(name = "SIM_INTRO")
	@Lob
	private String introduction = "";

	/** Learning Objectives of this Simulation. */
	@Column(name = "SIM_LEARN_OBJVS")
	@Lob
	private String learning_objvs = "";

	/** Planned audience of this Simulation. */
	@Column(name = "SIM_AUDIENCE")
	@Lob
	private String audience = "";

	/** Thoughts on how this sim should be conducted. */
	@Column(name = "SIM_PLAY_IDEAS")
	@Lob
	private String planned_play_ideas = "";

	public String getPlanned_play_ideas() {
		return planned_play_ideas;
	}

	public void setPlanned_play_ideas(String planned_play_ideas) {
		this.planned_play_ideas = planned_play_ideas;
	}

	/** AAR starter text for this Simulation. */
	@Column(name = "SIM_AAR")
	@Lob
	private String aar_starter_text = "";

	/** Creating Organization of this Simulation. */
	@Column(name = "SIM_CREATION_ORG")
	private String creation_org = "";

	/** Author of this Simulation. */
	@Column(name = "SIM_CREATOR")
	private String creator = "";

	/** Copyright information to be shown on every page footer. */
	@Column(name = "COPYRIGHTSTRING")
	private String copyright_string = "";

	/** Flag to let instructors know it can be used. */
	@Column(name = "READYFORLISTING")
	private boolean isReadyForPublicListing = false;

	@Column(name = "LISTINGKEYWORDS")
	private String listingKeyWords = "";

	public String getLearning_objvs() {
		return learning_objvs;
	}

	public void setLearning_objvs(String learning_objvs) {
		this.learning_objvs = learning_objvs;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Simulation() {

	}
	
	public void createDefaultObjects(String schema){
		
		// Save simulation to give it an id that will be used by some of the objects 
		// inside of it to refer to it.
		if (this.getId() == null){
			this.saveMe(schema);
		}
		// //////////////////////////////////////////////
		// All new sims start with 2 phases.
		SimulationPhase sp_first = SimulationPhase.getNewFirstPhase(schema);
		SimulationPhase sp_last = SimulationPhase.getNewLastPhase(schema);

		getPhases().add(sp_first);
		getPhases().add(sp_last);
		// /////////////////////////////////////////////////

		Actor ctrl_act = Actor.getControlActor(schema);
		getActors().add(ctrl_act);
		
		addControlSectionsToAllPhasesOfControl(schema, ctrl_act);
		
		this.saveMe(schema);
	}

	public static List getAll(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery("from Simulation").list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	public static List getAllPublished(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery("from Simulation where READYFORLISTING = '1'")
				.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	/** Saves a simulation. */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		/*
		 * Simulation myShadow = (Simulation)
		 * MultiSchemaHibernateUtil.getSession(schema).get( Simulation.class,
		 * this.id);
		 * 
		 * myShadow.setLearning_objvs(this.getLearning_objvs());
		 * myShadow.setAudience(this.getAudience());
		 * myShadow.setIntroduction(this.getIntroduction());
		 * myShadow.setAar_starter_text(this.getAar_starter_text());
		 * myShadow.setPlanned_play_ideas(this.getPlanned_play_ideas());
		 */
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}

	public static Simulation getMe(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Simulation simulation = (Simulation) MultiSchemaHibernateUtil
				.getSession(schema).get(Simulation.class, sim_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return simulation;

	}
	
	public static Simulation getMeFullyLoaded(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Simulation simulation = (Simulation) MultiSchemaHibernateUtil
				.getSession(schema).get(Simulation.class, sim_id);

		for (ListIterator<SimulationPhase> li = simulation.getPhases().listIterator(); li.hasNext();) {
			SimulationPhase this_sp = (SimulationPhase) li.next();
			
			System.out.println(this_sp.getName());
		}
	
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return simulation;

	}

	public void addControlSectionsToAllPhasesOfControl(String schema,
			Actor controlActor) {

		// Loop over phases
		for (ListIterator<SimulationPhase> li = this.phases.listIterator(); li
				.hasNext();) {
			SimulationPhase this_sp = (SimulationPhase) li.next();

			// Loop over control base sim sections
			List controlBaseSimSecs = BaseSimSection.getAllControl(schema);

			List<SimulationSection> simSecs = SimulationSection
					.getBySimAndActorAndPhase(schema, this.id, Actor
							.getControlActor(schema).getId(), this_sp.getId());

			for (ListIterator<BaseSimSection> bs = controlBaseSimSecs
					.listIterator(); bs.hasNext();) {
				BaseSimSection bss = (BaseSimSection) bs.next();

				int sizeOfSimSecs = simSecs.size();
				boolean foundThisControl = false;

				// Loop over sim sections that control has in this phase.
				for (ListIterator<SimulationSection> ls = simSecs
						.listIterator(); ls.hasNext();) {
					SimulationSection ss = (SimulationSection) ls.next();

					if (ss.getBase_section_id().compareTo(bss.getId()) == 0) {
						foundThisControl = true;
					}
				}

				// If control does not have this control section in this phase,
				// then add it.
				if (!foundThisControl) {

					SimulationSection ss0 = new SimulationSection(schema, this
							.getId(), controlActor.getId(), this_sp.getId(),
							bss.getId(), bss.getRec_tab_heading(),
							sizeOfSimSecs + 1);

					simSecs.add(ss0);

					System.out.println("adding " + bss.getRec_tab_heading()
							+ " at " + (sizeOfSimSecs + 1));

				}

			}

		}

	}

	/** Returns the id of the first phase in a simulation. */
	public Long getFirstPhaseId() {

		for (ListIterator li = phases.listIterator(); li.hasNext();) {
			SimulationPhase sp = (SimulationPhase) li.next();

			if (sp.isFirstPhase()) {
				return sp.getId();
			}

		}

		return null;
	}

	/** Returns the id of the last phase in a simulation. */
	public Long getLastPhaseId() {

		for (ListIterator li = phases.listIterator(); li.hasNext();) {
			SimulationPhase sp = (SimulationPhase) li.next();

			if (sp.getName().equalsIgnoreCase("Completed")) {
				return sp.getId();
			}

		}

		return null;
	}

	/**
	 * Creates the new running simulation, sets its phase to be the first phase
	 * of the simulation.
	 * 
	 * @param rs_name
	 */
	public RunningSimulation addNewRunningSimulation(String rs_name,
			org.hibernate.Session hibernate_session) {

		RunningSimulation rs = new RunningSimulation();
		rs.setName(rs_name);

		rs.setPhase_id(this.getFirstPhaseId());

		hibernate_session.saveOrUpdate(rs);

		getRunning_sims().add(rs);

		hibernate_session.saveOrUpdate(this);

		return rs;

	}

	/**
	 * Removes an actor from this simulation.
	 * 
	 * @param actor_id
	 */
	public void removeActor(String actor_id,
			org.hibernate.Session hibernate_session) {

		Actor act = (Actor) hibernate_session.get(Actor.class, new Long(
				actor_id));

		this.actors.remove(act);

		hibernate_session.saveOrUpdate(this);

	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public List<Actor> getActors() {
		return actors;
	}

	public void setActors(List<Actor> actors) {
		this.actors = actors;
	}

	public List<SimulationPhase> getPhases() {
		return phases;
	}

	public void setPhases(List<SimulationPhase> phases) {
		this.phases = phases;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	/** Gets list of actors not previously assigned to this simulation. */
	public List<Actor> getAvailableActors(String schema) {

		List fullList = new Actor().getAll(schema);

		for (ListIterator la = actors.listIterator(); la.hasNext();) {
			Actor act = (Actor) la.next();

			for (int ii = 0; ii < fullList.size(); ++ii) {

				Actor allAct = (Actor) fullList.get(ii);

				if (act.getId().longValue() == allAct.getId().longValue()) {
					fullList.remove(ii);
				}
			}

		}

		return fullList;

	}

	public String getDisplayName() {

		return this.name + " version " + this.version;
	}

	public List<RunningSimulation> getRunning_sims() {
		return running_sims;
	}

	public void setRunning_sims(List<RunningSimulation> running_sims) {
		this.running_sims = running_sims;
	}

	public String getCreation_org() {
		return creation_org;
	}

	public void setCreation_org(String creation_org) {
		this.creation_org = creation_org;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public List<IntVariable> getVar_int() {
		return var_int;
	}

	public void setVar_int(List<IntVariable> var_int) {
		this.var_int = var_int;
	}

	public String getAar_starter_text() {
		return aar_starter_text;
	}

	public void setAar_starter_text(String aar_starter_text) {
		this.aar_starter_text = aar_starter_text;
	}

	public List<CustomizeableSection> getCustomized_sections() {
		return customized_sections;
	}

	public void setCustomized_sections(
			List<CustomizeableSection> customized_sections) {
		this.customized_sections = customized_sections;
	}

	public List<Conversation> getConversations() {
		if (conversations == null) {
			conversations = new ArrayList<Conversation>();
		}
		return conversations;
	}

	public void setConversations(List<Conversation> conversations) {
		this.conversations = conversations;
	}

	/**
	 * Only adds conversation to a simulation if that conversation has not
	 * already been added.
	 * 
	 * @param conv
	 */
	public void addConversation(Conversation conv) {
		if ((!conversations.contains(conv))) {
			conversations.add(conv);
		}
	}

	public String getCopyright_string() {
		return copyright_string;
	}

	public void setCopyright_string(String copyright_string) {
		this.copyright_string = copyright_string;
	}

	public boolean isReadyForPublicListing() {
		return isReadyForPublicListing;
	}

	public void setReadyForPublicListing(boolean isReadyForPublicListing) {
		this.isReadyForPublicListing = isReadyForPublicListing;
	}

	public String getListingKeyWords() {
		return listingKeyWords;
	}

	public void setListingKeyWords(String listingKeyWords) {
		this.listingKeyWords = listingKeyWords;
	}

} // End of Simulation
