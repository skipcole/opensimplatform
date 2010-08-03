package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.communications.Conversation;
import org.usip.osp.communications.Event;
import org.usip.osp.communications.SharedDocument;
import org.usip.osp.networking.ObjectPackager;
import org.usip.osp.persistence.*;
import org.apache.log4j.*;

/**
 * This class represents a simulation.
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
 * 
 */
@Entity
@Table(name = "SIMULATIONS")
@Proxy(lazy = false)
public class Simulation implements ExportableObject{

	/** Database id of this Simulation. */
	@Id
	@GeneratedValue
	@Column(name = "SIM_ID")
	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transitId;

	public Long getTransitId() {
		return this.transitId;
	}

	public void setTransitId(Long transitId) {
		this.transitId = transitId;
	}
	
	/** Name of this Simulation. */
	@Column(name = "SIM_NAME")
	private String name = ""; //$NON-NLS-1$

	/** Version of this Simulation. */
	@Column(name = "SIM_VERSION")
	private String version = ""; //$NON-NLS-1$

	/** Version of the software this simulation was made with. */
	private String softwareVersion = ""; //$NON-NLS-1$

	public String getSimulationName() {
		return this.name;
	}

	public void setSimulationName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	
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
	private boolean allowPlayerAutoreg = false;

	public boolean isAllow_player_autoreg() {
		return this.allowPlayerAutoreg;
	}

	public void setAllow_player_autoreg(boolean allow_player_autoreg) {
		this.allowPlayerAutoreg = allow_player_autoreg;
	}

	/** Introduction to this Simulation. */
	@Column(name = "SIM_INTRO")
	@Lob
	private String introduction = ""; //$NON-NLS-1$

	/** Learning Objectives of this Simulation. */
	@Column(name = "SIM_LEARN_OBJVS")
	@Lob
	private String learningObjvs = ""; //$NON-NLS-1$

	/** Planned audience of this Simulation. */
	@Column(name = "SIM_AUDIENCE")
	@Lob
	private String audience = ""; //$NON-NLS-1$

	/** Thoughts on how this sim should be conducted. */
	@Column(name = "SIM_PLAY_IDEAS")
	@Lob
	private String plannedPlayIdeas = ""; //$NON-NLS-1$

	public String getPlannedPlayIdeas() {
		return this.plannedPlayIdeas;
	}

	public void setPlannedPlayIdeas(String planned_play_ideas) {
		this.plannedPlayIdeas = planned_play_ideas;
	}

	/** AAR starter text for this Simulation. */
	@Column(name = "SIM_AAR")
	@Lob
	private String aarStarterText = ""; //$NON-NLS-1$

	/** Creating Organization of this Simulation. */
	@Column(name = "SIM_CREATION_ORG")
	private String creationOrg = ""; //$NON-NLS-1$

	/** Author of this Simulation. */
	@Column(name = "SIM_CREATOR")
	private String creator = ""; //$NON-NLS-1$

	/** Copyright information to be shown on every page footer. */
	@Column(name = "COPYRIGHTSTRING")
	private String copyrightString = ""; //$NON-NLS-1$

	/** Flag to let instructors know it can be used. */
	@Column(name = "READYFORLISTING")
	private boolean isReadyForPublicListing = false;

	@Column(name = "LISTINGKEYWORDS")
	private String listingKeyWords = ""; //$NON-NLS-1$
	
	/** Flag to indicate that simulation has been lastEdited. */
	private Date lastEditDate;

	public Date getLastEditDate() {
		return lastEditDate;
	}

	public void setLastEditDate(Date lastEditDate) {
		this.lastEditDate = lastEditDate;
	}
	
	public void updateLastEditDate(String schema) {
		
		this.lastEditDate = new Date();
		this.saveMe(schema);
	}
	
	/**
	 * Updates the last time a simulation was edited.
	 * 
	 * @param sim_id
	 * @param schema
	 */
	public static void updateSimsLastEditDate(Long sim_id, String schema){
		Simulation sim = Simulation.getById(schema, sim_id);
		sim.updateLastEditDate(schema);
	}
	
	
	/** Flag to indicate that simulation has been published. */
	private Date publishDate;

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getLearning_objvs() {
		return this.learningObjvs;
	}

	public void setLearning_objvs(String learning_objvs) {
		this.learningObjvs = learning_objvs;
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
		if (sim_a.getPlannedPlayIdeas().equals(sim_b.getPlannedPlayIdeas())){
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

		
		// Add the schedule section to the set of universal sections.
		// Maybe should look this up in some other way, but I'll save that for another day.
		BaseSimSection scheduleSection = BaseSimSection.getByRecommendedTagHeading(schema, "Schedule"); //$NON-NLS-1$

		// Add the schedule as the first tab to all players.
		@SuppressWarnings("unused")
		SimulationSectionAssignment ss_sched = new SimulationSectionAssignment(schema, this.getId(), new Long(0), sp_first
				.getId(), scheduleSection.getId(), "Schedule", 2); //$NON-NLS-1$
		
		
		// Add the after action review section to the set of universal sections.
		// Maybe should look this up in some other way, but I'll save that for another day.
		BaseSimSection aarSection = BaseSimSection.getByRecommendedTagHeading(schema, "AAR"); //$NON-NLS-1$

		// Add the introduction as the first tab to all players.
		@SuppressWarnings("unused")
		SimulationSectionAssignment ss_aar = new SimulationSectionAssignment(schema, this.getId(), new Long(0), sp_last
				.getId(), aarSection.getId(), "AAR", 1); //$NON-NLS-1$

		
		// TODO: Not sure this is actually necessary at this point
		SimulationSectionAssignment.applyUniversalSectionsToAllActorsForPhase(schema, this.getId(), sp_first.getId());
		

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
				"from Simulation where READYFORLISTING = '1' and allowPlayerAutoreg = '1'") //$NON-NLS-1$
				.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	/** Saves a simulation. */
	public void saveMe(String schema) {
		
		// Save the last updated date each time this simulaiton is saved.
		this.setLastEditDate(new Date());
		
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
	public static Simulation getById(String schema, Long sim_id) {

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
	
	/** Returns the start date for the simulation phase.
	 * 
	 * @param schema
	 * @param phase_id
	 * @return
	 */
	public Date getPhaseStartTime(String schema, Long phase_id){
		SimulationPhase sp = SimulationPhase.getById(schema, phase_id);
		
		// For now arbitrarily set date to 1/1/2001.
		Calendar cal = new GregorianCalendar();
		
		// Year, month, day, hour, minute
		cal.set(2001, 0, 1, 9, 0);
		
		java.util.Date phaseStartDate = cal.getTime();
		
		return phaseStartDate;
		//return sp.getPhaseStartDate();
		
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

			if (sp.getPhaseName().equalsIgnoreCase("Completed")) { //$NON-NLS-1$
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

		List fullList = Actor.getAllForSimulation(schema, this.getId());

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
		return this.creationOrg;
	}

	public void setCreation_org(String creation_org) {
		this.creationOrg = creation_org;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getAarStarterText() {
		return this.aarStarterText;
	}

	public void setAarStarterText(String aar_starter_text) {
		this.aarStarterText = aar_starter_text;
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
		return this.copyrightString;
	}

	public void setCopyright_string(String copyright_string) {
		this.copyrightString = copyright_string;
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
