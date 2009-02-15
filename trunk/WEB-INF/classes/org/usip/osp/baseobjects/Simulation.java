package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.communications.Conversation;
import org.usip.osp.communications.SharedDocument;
import org.usip.osp.persistence.*;
import org.usip.osp.specialfeatures.*;

import org.apache.log4j.*;

/**
 * This class represents a simulation.
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
@Table(name = "SIMULATIONS")
@Proxy(lazy = false)
public class Simulation {
	
	/**
	 * Just used for occasional debugging.
	 * @param args
	 */
	public static void main(String args[]) {

		Logger.getRootLogger().debug("hello world");	
		
	}

	/** Database id of this Simulation. */
	@Id
	@GeneratedValue
	@Column(name = "SIM_ID")
	private Long id;
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransit_id() {
		return transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}

	/** Name of this Simulation. */
	@Column(name = "SIM_NAME")
	private String name = "";

	/** Version of this Simulation. */
	@Column(name = "SIM_VERSION")
	private String version = "";
	
	/** A paragraph introducing what this simulation is all about. */
	@Lob
	private String blurb = "";
	
	public String getBlurb() {
		return blurb;
	}

	public void setBlurb(String blurb) {
		this.blurb = blurb;
	}

	/** Indicates if Players can register themselves to play in the simulation. */
	private boolean allow_player_autoreg = false;

	public boolean isAllow_player_autoreg() {
		return allow_player_autoreg;
	}

	public void setAllow_player_autoreg(boolean allow_player_autoreg) {
		this.allow_player_autoreg = allow_player_autoreg;
	}

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
	
	/**
	 * Creates the initial phases and standard universal sections of a simulation.
	 * 
	 * @param schema The name of the schema to create objects in.
	 */
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

		getPhases(schema).add(sp_first);
		getPhases(schema).add(sp_last);
		
		SimPhaseAssignment spf = new SimPhaseAssignment(schema, this.getId(), sp_first.getId());
		SimPhaseAssignment spl = new SimPhaseAssignment(schema, this.getId(), sp_last.getId());
		// /////////////////////////////////////////////////
		
		// Add the control character
		Actor ctrl_act = Actor.getControlActor(schema);
		SimActorAssignment saa = new SimActorAssignment(schema, this.getId(), ctrl_act.getId());
		
		// Give controller all default sections
		addControlSectionsToAllPhasesOfControl(schema, ctrl_act);
		
		// Maybe should look this up in some other way, but I'll save that for another day.
		BaseSimSection introSection = BaseSimSection.getByRecommendedTagHeading(schema, "Introduction");
		
		// Add the introduction as the first tab to all players.
		SimulationSection ss0 = new SimulationSection(schema, this.getId(),
				new Long(0), sp_first.getId(), introSection.getId(),
				"Introduction", 1);
		
		// Create a schedule page and add it as the second section for all players in the first phase
		CustomizeableSection scheduleSectionBase = (CustomizeableSection) BaseSimSection.getByRecommendedTagHeading(schema, "Read Document");
		// need to get the schedule customized section
		
		CustomizeableSection scheduleSection = scheduleSectionBase.makeCopy(schema);
		
		// Add the schedule page
		SharedDocument sd = new SharedDocument("schedule", "Schedule for this Simulation", this.getId());
		sd.save(schema);
		
		// need to associate with it the schedule document 
		scheduleSection.getContents().put(SharedDocument.DOCS_IN_HASHTABLE_KEY, sd.getId() + ",");
		scheduleSection.save(schema);
		
		// Add the schedule as the second tab to all players.
		SimulationSection ss1 = new SimulationSection(schema, this.getId(),
				new Long(0), sp_first.getId(), scheduleSection.getId(),
				"Schedule", 2);

		SimulationSection.applyUniversalSectionsToAllActorsForPhase(schema, this.getId(), sp_first.getId());
		///////////
		
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
	
	/** 
	 * Returns a set of simulations that a player can register for.
	 * 
	 * @param schema
	 * @return
	 */
	public static List getAllPublishedAutoRegisterable(String schema) {

		List returnList = new ArrayList();
		List firstList =  getAllPublished(schema);

		for (ListIterator<Simulation> li = firstList.listIterator(); li.hasNext();) {
			Simulation this_sim = (Simulation) li.next();
			
			if (this_sim.isAllow_player_autoreg()){
				returnList.add(this_sim);
			}
			
		}

		return returnList;
	}

	/** Saves a simulation. */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}

	/**
	 * Pulls the simulation out of the database base on its id and schema.
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static Simulation getMe(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Simulation simulation = (Simulation) MultiSchemaHibernateUtil
				.getSession(schema).get(Simulation.class, sim_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return simulation;

	}
	
	/**
	 * This method is of dubious value and may go away.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static Simulation getMeFullyLoaded(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Simulation simulation = (Simulation) MultiSchemaHibernateUtil
				.getSession(schema).get(Simulation.class, sim_id);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		for (ListIterator<SimulationPhase> li = simulation.getPhases(schema).listIterator(); li.hasNext();) {
			SimulationPhase this_sp = (SimulationPhase) li.next();
			
			System.out.println(this_sp.getName());
		}
	
		

		return simulation;

	}

	public void addControlSectionsToAllPhasesOfControl(String schema,
			Actor controlActor) {

		// Loop over phases
		for (ListIterator<SimulationPhase> li = this.getPhases(schema).listIterator(); li
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
	public Long getFirstPhaseId(String schema) {

		for (ListIterator li = this.getPhases(schema).listIterator(); li.hasNext();) {
			SimulationPhase sp = (SimulationPhase) li.next();

			if (sp.isFirstPhase()) {
				return sp.getId();
			}

		}

		return null;
	}

	/** Returns the id of the last phase in a simulation. */
	public Long getLastPhaseId(String schema) {

		for (ListIterator li = this.getPhases(schema).listIterator(); li.hasNext();) {
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
			String schema) {

		RunningSimulation rs = new RunningSimulation(rs_name, this, schema);

		SimRunningSimAssignment srsa = new SimRunningSimAssignment(schema, id, rs.getId());

		return rs;

	}

	/**
	 * Removes an actor from this simulation.
	 * 
	 * @param actor_id
	 */
	public void removeActor(String schema, Long actor_id) {

		SimActorAssignment.removeMe(schema, id, actor_id);

	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	public List<Actor> getActors(String schema) {
		return SimActorAssignment.getActorsForSim(schema, id);
	}
	
	public List<SimulationPhase> getPhases(String schema) {
		return SimPhaseAssignment.getPhasesForSim(schema, id);
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

		for (ListIterator la = this.getActors(schema).listIterator(); la.hasNext();) {
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
	
	public List<RunningSimulation> getRunning_sims(String schema) {
		return SimRunningSimAssignment.getRunningSimulationsForSim(schema, id);
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

	public String getAar_starter_text() {
		return aar_starter_text;
	}

	public void setAar_starter_text(String aar_starter_text) {
		this.aar_starter_text = aar_starter_text;
	}

	public List<Conversation> getConversations(String schema) {
		return SimConversationAssignment.getConversationsForSim(schema, id);
	}
	/**
	 * Only adds conversation to a simulation if that conversation has not
	 * already been added.
	 * 
	 * @param conv
	 */
	public void addConversation(String schema, Conversation conv) {

		SimConversationAssignment sca = new SimConversationAssignment(schema, id, conv.getId());
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
