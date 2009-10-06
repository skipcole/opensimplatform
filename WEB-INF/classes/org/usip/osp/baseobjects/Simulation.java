package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.communications.Conversation;
import org.usip.osp.communications.SharedDocument;
import org.usip.osp.networking.ObjectPackager;
import org.usip.osp.persistence.*;
import org.apache.log4j.*;

/**
 * This class represents a simulation.
 * 
 * 
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it
 * and/or modify it under the terms of the new BSD Style license associated with
 * this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "SIMULATIONS")
@Proxy(lazy = false)
public class Simulation {


	/** Database id of this Simulation. */
	@Id
	@GeneratedValue
	@Column(name = "SIM_ID")
	private Long id;

	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransit_id() {
		return this.transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}

	/** Name of this Simulation. */
	@Column(name = "SIM_NAME")
	private String name = ""; //$NON-NLS-1$

	/** Version of this Simulation. */
	@Column(name = "SIM_VERSION")
	private String version = ""; //$NON-NLS-1$

	/** Version of the software this simulation was made with. */
	private String software_version = ""; //$NON-NLS-1$

	/** A paragraph introducing what this simulation is all about. */
	@Lob
	private String blurb = ""; //$NON-NLS-1$

	public String getBlurb() {
		return this.blurb;
	}

	public void setBlurb(String blurb) {
		this.blurb = blurb;
	}

	/** Indicates if Players can register themselves to play in the simulation. */
	private boolean allow_player_autoreg = false;

	public boolean isAllow_player_autoreg() {
		return this.allow_player_autoreg;
	}

	public void setAllow_player_autoreg(boolean allow_player_autoreg) {
		this.allow_player_autoreg = allow_player_autoreg;
	}

	/** Introduction to this Simulation. */
	@Column(name = "SIM_INTRO")
	@Lob
	private String introduction = ""; //$NON-NLS-1$

	/** Learning Objectives of this Simulation. */
	@Column(name = "SIM_LEARN_OBJVS")
	@Lob
	private String learning_objvs = ""; //$NON-NLS-1$

	/** Planned audience of this Simulation. */
	@Column(name = "SIM_AUDIENCE")
	@Lob
	private String audience = ""; //$NON-NLS-1$

	/** Thoughts on how this sim should be conducted. */
	@Column(name = "SIM_PLAY_IDEAS")
	@Lob
	private String planned_play_ideas = ""; //$NON-NLS-1$

	public String getPlanned_play_ideas() {
		return this.planned_play_ideas;
	}

	public void setPlanned_play_ideas(String planned_play_ideas) {
		this.planned_play_ideas = planned_play_ideas;
	}

	/** AAR starter text for this Simulation. */
	@Column(name = "SIM_AAR")
	@Lob
	private String aar_starter_text = ""; //$NON-NLS-1$

	/** Creating Organization of this Simulation. */
	@Column(name = "SIM_CREATION_ORG")
	private String creation_org = ""; //$NON-NLS-1$

	/** Author of this Simulation. */
	@Column(name = "SIM_CREATOR")
	private String creator = ""; //$NON-NLS-1$

	/** Copyright information to be shown on every page footer. */
	@Column(name = "COPYRIGHTSTRING")
	private String copyright_string = ""; //$NON-NLS-1$

	/** Flag to let instructors know it can be used. */
	@Column(name = "READYFORLISTING")
	private boolean isReadyForPublicListing = false;

	@Column(name = "LISTINGKEYWORDS")
	private String listingKeyWords = ""; //$NON-NLS-1$
	
	/** Flag to indicate that simulation has been published. */
	private Date publishDate;

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getLearning_objvs() {
		return this.learning_objvs;
	}

	public void setLearning_objvs(String learning_objvs) {
		this.learning_objvs = learning_objvs;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSoftware_version() {
		return this.software_version;
	}

	public void setSoftware_version(String software_version) {
		this.software_version = software_version;
	}

	public Simulation() {

	}

	
	/**
	 * Just used for occasional debugging.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		Logger.getRootLogger().debug("hello world"); //$NON-NLS-1$
		
		Simulation a = new Simulation();
		Simulation b = new Simulation();
		
		a.setLearning_objvs("a");
		b.setLearning_objvs("a");
		
		System.out.println(Simulation.compare(a, b, true));
		

	}
	/**
	 * Compares the simulations and returns an xml string indicating any differences.
	 * 
	 * @param sim_a
	 * @param sim_b
	 * @return
	 */
	public static String compare(Simulation sim_a, Simulation sim_b, boolean exclude_name){
		
		boolean foundDifference = false;
		
		String differenceString = "<SIM_COMPARE>\r\n";
		
		
		// Compare Objectives
		if (sim_a.getLearning_objvs().equals(sim_b.getLearning_objvs())){
			differenceString += ObjectPackager.addResultsToXML("     <OBJECTIVES>", "</OBJECTIVES>\r\n", true);
		} else {
			differenceString += ObjectPackager.addResultsToXML("     <OBJECTIVES>", "</OBJECTIVES>\r\n", false);
			foundDifference = true;
		}
		
		// Compare Audience
		if (sim_a.getAudience().equals(sim_b.getAudience())){
			differenceString += ObjectPackager.addResultsToXML("     <AUDIENCE>", "</AUDIENCE>\r\n", true);
		} else {
			differenceString += ObjectPackager.addResultsToXML("     <AUDIENCE>", "</AUDIENCE>\r\n", false);
			foundDifference = true;
		}
		
		// Compare Planned Play Ideas
		if (sim_a.getPlanned_play_ideas().equals(sim_b.getPlanned_play_ideas())){
			differenceString += ObjectPackager.addResultsToXML("     <PLANNED_PLAY_IDEAS>", "</PLANNED_PLAY_IDEAS>\r\n", true);
		} else {
			differenceString += ObjectPackager.addResultsToXML("     <PLANNED_PLAY_IDEAS>", "</PLANNED_PLAY_IDEAS>\r\n", false);
			foundDifference = true;
		}
		
		// Compare Introductions
		if (sim_a.getIntroduction().equals(sim_b.getIntroduction())){
			differenceString += ObjectPackager.addResultsToXML("     <INTRODUCTION>", "</INTRODUCTION>\r\n", true);
		} else {
			differenceString += ObjectPackager.addResultsToXML("     <INTRODUCTION>", "</INTRODUCTION>\r\n", false);
			foundDifference = true;
		}
		
		// Need to compare phases
		
		// Look at results to see if any of them differed.
		String phaseCompareXML = SimulationPhase.compare(new SimulationPhase(), new SimulationPhase());
		
		if (!(phaseCompareXML.contains("<RESULTS>Same</RESULTS"))){
			foundDifference = true;
		}
		differenceString += phaseCompareXML;
		
		differenceString += ObjectPackager.addResultsToXML("     <RESULTS>", "</RESULTS>\r\n", !(foundDifference));
		
		differenceString += "</SIM_COMPARE>";
		
		return differenceString;
	}
	

	
	

	/**
	 * Creates the initial phases and standard universal sections of a
	 * simulation.
	 * 
	 * @param schema
	 *            The name of the schema to create objects in.
	 */
	public void createDefaultObjects(String schema) {

		// Save simulation to give it an id that will be used by some of the
		// objects
		// inside of it to refer to it.
		if (this.getId() == null) {
			this.saveMe(schema);
		}
		// //////////////////////////////////////////////
		// All new sims start with 2 phases.
		SimulationPhase sp_first = SimulationPhase.getNewFirstPhase(schema);
		SimulationPhase sp_last = SimulationPhase.getNewLastPhase(schema);

		getPhases(schema).add(sp_first);
		getPhases(schema).add(sp_last);

		// Create object in the database
		@SuppressWarnings("unused")
		SimPhaseAssignment spf = new SimPhaseAssignment(schema, this.getId(), sp_first.getId());

		// Create object in the database
		@SuppressWarnings("unused")
		SimPhaseAssignment spl = new SimPhaseAssignment(schema, this.getId(), sp_last.getId());
		// /////////////////////////////////////////////////

		// Add the introduction section to the set of universal sections.
		// Maybe should look this up in some other way, but I'll save that for another day.
		BaseSimSection introSection = BaseSimSection.getByRecommendedTagHeading(schema, "Introduction"); //$NON-NLS-1$

		// Add the introduction as the first tab to all players.
		@SuppressWarnings("unused")
		SimulationSectionAssignment ss_intro = new SimulationSectionAssignment(schema, this.getId(), new Long(0), sp_first
				.getId(), introSection.getId(), "Introduction", 1); //$NON-NLS-1$

		// Add the after action review section to the set of universal sections.
		// Maybe should look this up in some other way, but I'll save that for another day.
		BaseSimSection aarSection = BaseSimSection.getByRecommendedTagHeading(schema, "Introduction"); //$NON-NLS-1$

		// Add the introduction as the first tab to all players.
		@SuppressWarnings("unused")
		SimulationSectionAssignment ss_aar = new SimulationSectionAssignment(schema, this.getId(), new Long(0), sp_last
				.getId(), aarSection.getId(), "AAR", 1); //$NON-NLS-1$
		
		// Create a schedule page and add it as the second section for all
		// players in the first phase
		CustomizeableSection scheduleSectionBase = (CustomizeableSection) BaseSimSection.getByRecommendedTagHeading(
				schema, "Read Document"); //$NON-NLS-1$
		// need to get the schedule customized section

		CustomizeableSection scheduleSection = scheduleSectionBase.makeCopy(schema);

		scheduleSection.setUniqueName("Schedule"); //$NON-NLS-1$
		scheduleSection.setDescription("A place for the players to read the schedule."); //$NON-NLS-1$
		scheduleSection.setRec_tab_heading("Schedule"); //$NON-NLS-1$
		scheduleSection.save(schema);

		// Add the schedule page
		SharedDocument sd = new SharedDocument("schedule", "Schedule for this Simulation", this.getId()); //$NON-NLS-1$ //$NON-NLS-2$
		sd.saveMe(schema);

		// need to associate with it the schedule document
		@SuppressWarnings("unused")
		BaseSimSectionDepObjectAssignment bssdoa = new BaseSimSectionDepObjectAssignment(scheduleSection.getId(),
				"org.usip.osp.communications.SharedDocument", 1, sd.getId(), this.getId(), //$NON-NLS-1$
				schema);

		// Add the schedule as the second tab to all players.
		@SuppressWarnings("unused")
		SimulationSectionAssignment ss1 = new SimulationSectionAssignment(schema, this.getId(), new Long(0), sp_first
				.getId(), scheduleSection.getId(), "Schedule", 2); //$NON-NLS-1$

		SimulationSectionAssignment.applyUniversalSectionsToAllActorsForPhase(schema, this.getId(), sp_first.getId());
		// /////////

		this.saveMe(schema);
	}

	public static List getAll(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery("from Simulation").list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	public static List getAllPublished(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Simulation where READYFORLISTING = '1'") //$NON-NLS-1$
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

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Simulation where READYFORLISTING = '1' and allow_player_autoreg = '1'") //$NON-NLS-1$
				.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

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
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static Simulation getMe(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Simulation simulation = (Simulation) MultiSchemaHibernateUtil.getSession(schema).get(Simulation.class, sim_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return simulation;

	}



	/**
	 * Adds the designated control sections to all control actors.
	 * 
	 * @param schema
	 * @param controlActor
	 */
	public void addControlSectionsToAllPhasesOfControl(String schema, Actor controlActor) {

		// Loop over phases
		for (ListIterator<SimulationPhase> li = this.getPhases(schema).listIterator(); li.hasNext();) {
			SimulationPhase this_sp = li.next();

			// Loop over control base sim sections
			List controlBaseSimSecs = BaseSimSection.getAllControl(schema);

			// Loop over all of the control actors in this simulation
			List control_actors = Actor.getControlActors(schema, this.getId());

			for (ListIterator<Actor> liac = control_actors.listIterator(); liac.hasNext();) {
				Actor this_control_act = liac.next();

				List<SimulationSectionAssignment> simSecs = SimulationSectionAssignment.getBySimAndActorAndPhase(
						schema, this.id, this_control_act.getId(), this_sp.getId());

				for (ListIterator<BaseSimSection> bs = controlBaseSimSecs.listIterator(); bs.hasNext();) {
					BaseSimSection bss = bs.next();

					int sizeOfSimSecs = simSecs.size();
					boolean foundThisControl = false;

					// Loop over sim sections that control has in this phase.
					for (ListIterator<SimulationSectionAssignment> ls = simSecs.listIterator(); ls.hasNext();) {
						SimulationSectionAssignment ss = ls.next();

						if (ss.getBase_sec_id().compareTo(bss.getId()) == 0) {
							foundThisControl = true;
						}
					}

					// If control does not have this control section in this phase, then add it.
					if (!foundThisControl) {

						SimulationSectionAssignment ss0 = new SimulationSectionAssignment(schema, this.getId(),
								controlActor.getId(), this_sp.getId(), bss.getId(), bss.getRec_tab_heading(),
								sizeOfSimSecs + 1);

						simSecs.add(ss0);

						Logger.getRootLogger().debug("adding " + bss.getRec_tab_heading() //$NON-NLS-1$
								+ " at " + (sizeOfSimSecs + 1)); //$NON-NLS-1$

					}

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

			if (sp.getName().equalsIgnoreCase("Completed")) { //$NON-NLS-1$
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
	public RunningSimulation addNewRunningSimulation(String rs_name, String schema, Long _creator_id, String _creator_name) {

		RunningSimulation rs = new RunningSimulation(rs_name, this, schema, _creator_id, _creator_name);

		return rs;

	}

	/**
	 * Removes an actor from this simulation.
	 * 
	 * @param actor_id
	 */
	public void removeActor(String schema, Long actor_id) {

		SimActorAssignment.removeMe(schema, this.id, actor_id);

	}

	public String getIntroduction() {
		return this.introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public List<Actor> getActors(String schema) {
		return SimActorAssignment.getActorsForSim(schema, this.id);
	}

	public List<SimulationPhase> getPhases(String schema) {
		return SimPhaseAssignment.getPhasesForSim(schema, this.id);
	}

	public String getAudience() {
		return this.audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	/** Gets list of actors not previously assigned to this simulation. */
	public List<Actor> getAvailableActorsForSim(String schema) {

		List fullList = Actor.getAllForSim(schema, this.getId());

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

		return this.name + " version " + this.version; //$NON-NLS-1$
	}

	public List<RunningSimulation> getRunning_sims(String schema) {
		
		if (this.id == null){
			Logger.getRootLogger().warn("Simulation with null id called upon to return list of running sims.");
			return new ArrayList<RunningSimulation>();
		}
		
		return RunningSimulation.getAllForSim(this.id.toString(), schema);
	}

	public String getCreation_org() {
		return this.creation_org;
	}

	public void setCreation_org(String creation_org) {
		this.creation_org = creation_org;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getAar_starter_text() {
		return this.aar_starter_text;
	}

	public void setAar_starter_text(String aar_starter_text) {
		this.aar_starter_text = aar_starter_text;
	}

	public List<Conversation> getConversations(String schema) {
		return SimConversationAssignment.getConversationsForSim(schema, this.id);
	}

	/**
	 * Only adds conversation to a simulation if that conversation has not
	 * already been added.
	 * 
	 * @param conv
	 */
	public void addConversation(String schema, Conversation conv) {

		@SuppressWarnings("unused")
		SimConversationAssignment sca = new SimConversationAssignment(schema, this.id, conv.getId());
	}

	public String getCopyright_string() {
		return this.copyright_string;
	}

	public void setCopyright_string(String copyright_string) {
		this.copyright_string = copyright_string;
	}

	public boolean isReadyForPublicListing() {
		return this.isReadyForPublicListing;
	}

	public void setReadyForPublicListing(boolean isReadyForPublicListing) {
		this.isReadyForPublicListing = isReadyForPublicListing;
	}

	public String getListingKeyWords() {
		return this.listingKeyWords;
	}

	public void setListingKeyWords(String listingKeyWords) {
		this.listingKeyWords = listingKeyWords;
	}

} // End of Simulation
