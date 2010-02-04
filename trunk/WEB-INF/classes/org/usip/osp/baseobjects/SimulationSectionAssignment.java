package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Proxy;
import org.usip.osp.communications.ConvActorAssignment;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.apache.log4j.*;
/**
 * This class represents a section assigned to an actor at a particular phase. A
 * better name for the class might be 'SimulationSectionAssignment.'
 * 
 */
/*
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
@Table(name = "SIM_SEC_ASSIGNMENTS")
@Proxy(lazy = false)
public class SimulationSectionAssignment {

	/**
	 * Position in binary string of bit indicating if running simulation id
	 * should be sent.
	 */
	public static final int POS_RS_ID = 0;

	/**
	 * Position in binary string of bit indicating if running actor id should be
	 * sent.
	 */
	public static final int POS_A_ID = 1;

	/**
	 * Position in binary string of bit indicating if running user id should be
	 * sent.
	 */
	public static final int POS_U_ID = 2;

	@Id
	@GeneratedValue
	@Column(name = "SIMSEC_ID")
	private Long id;

	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransit_id() {
		return this.transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}

	@Column(name = "SIM_ID")
	private Long sim_id;

	/**
	 * Indicates elements of information should be sent in a URL string to an
	 * external page.
	 */
	private String sendString = ""; //$NON-NLS-1$

	@Column(name = "ACTOR_ID")
	private Long actor_id;

	@Column(name = "PHASE_ID")
	private Long phase_id;

	@Column(name = "TAB_POS")
	private int tab_position = 0;
	
	/** Color of this tab heading. */
	@Column(name = "TAB_COLOR")
	private String tabColor = "FFFFFF"; //$NON-NLS-1$

	/** The tab heading of this section. */
	@Column(name = "TAB_HEADING")
	private String tab_heading;

	/** The id of the base section that this section was 'cast' from. */
	@Column(name = "BASE_SEC_ID")
	private Long base_sec_id;
	
	private String baseUniqueName = ""; //$NON-NLS-1$
	
	private String baseVersion = ""; //$NON-NLS-1$

	/** URL of this section */
	@Column(name = "SIMSEC_URL")
	private String url = ""; //$NON-NLS-1$

	/** Directory of this section */
	@Column(name = "SIMSEC_DIR")
	private String directory = ""; //$NON-NLS-1$

	/** Filename of this section */
	@Column(name = "SIMSEC_FILENAME")
	private String page_file_name = ""; //$NON-NLS-1$

	@Column(name = "ADDED_AS_UNIV")
	private boolean addedAsUniversalSection = false;

	/**
	 * Used to indicate if this section has been added as a subsection.
	 * Subsections do not have a tab position, but are called for, by id, by the
	 * page that displays them.
	 */
	private boolean simSubSection = false;

	public boolean isSimSubSection() {
		return this.simSubSection;
	}

	public void setSimSubSection(boolean simSubSection) {
		this.simSubSection = simSubSection;
	}

	public int getSimSubSectionIndex() {
		return this.simSubSectionIndex;
	}

	public void setSimSubSectionIndex(int simSubSectionIndex) {
		this.simSubSectionIndex = simSubSectionIndex;
	}

	public Long getDisplaySectionId() {
		return this.displaySectionId;
	}

	public void setDisplaySectionId(Long displaySectionId) {
		this.displaySectionId = displaySectionId;
	}

	/**
	 * The order of this section on the sub section display section (1 for left,
	 * 2 for right, etc.)
	 */
	private int simSubSectionIndex = 0;

	/** The id of the custom section which is displaying this sub-section. */
	private Long displaySectionId;

	/** Zero argument constructor needed by Hibernate. */
	public SimulationSectionAssignment() {

	}

	/**
	 * Pulls the simulation out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static SimulationSectionAssignment getMe(String schema, Long sec_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		SimulationSectionAssignment simsec = (SimulationSectionAssignment) MultiSchemaHibernateUtil.getSession(schema)
				.get(SimulationSectionAssignment.class, sec_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return simsec;

	}

	/**
	 * Removes a section completely, and re-orders all that are left.
	 * 
	 * @param schema
	 * @param s_gone
	 */
	public static void removeAndReorder(String schema, SimulationSectionAssignment s_gone) {

		Long sid = s_gone.getSim_id();
		Long aid = s_gone.getActor_id();
		Long pid = s_gone.getPhase_id();

		remove(schema, s_gone);

		reorder(schema, sid, aid, pid);

	}

	/**
	 * Sets the tab positions of a list of simulation sections to be equivalent
	 * to their position in the list.
	 */
	public static void reorder(String schema, Long sid, Long aid, Long pid) {

		List survivors = getBySimAndActorAndPhase(schema, sid, aid, pid);

		MultiSchemaHibernateUtil.beginTransaction(schema);
		int ii = 1;

		for (ListIterator li = survivors.listIterator(); li.hasNext();) {
			SimulationSectionAssignment ss = (SimulationSectionAssignment) li.next();

			ss.setTab_position(ii);
			MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(ss);

			++ii;

		}
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}

	/**
	 * Removes this SimulationSectionAssignment from the database.
	 * 
	 * @param schema
	 * @param ss
	 */
	public static void remove(String schema, SimulationSectionAssignment s_gone) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).delete(s_gone);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	/**
	 * Creates a simulation section based on information in a base simulation
	 * section.
	 * 
	 * @param sid
	 * @param aid
	 * @param pid
	 * @param bss_id
	 * @param tab_heading
	 * @param tab_position
	 */
	public SimulationSectionAssignment(String schema, Long sid, Long aid, Long pid, Long bss_id, String tab_heading,
			int tab_position) {

		BaseSimSection bss = null;
		if (bss_id != null) {
			bss = BaseSimSection.getMe(schema, bss_id.toString());
		} else {
			Logger.getRootLogger().debug("Warning bss_id at simulation section creation was null."); //$NON-NLS-1$
			return;
		}

		// These three ids locate the section to a simulation, actor and phase.
		this.setSim_id(sid);
		this.setActor_id(aid);
		this.setPhase_id(pid);

		// These variables are copied straight from the template
		this.setBase_sec_id(bss.getId());
		this.sendString = bss.getSendString();
		this.setUrl(bss.getUrl());
		this.setDirectory(bss.getDirectory());

		this.setPage_file_name(bss.getPage_file_name());
		
		// These variables are not strictly needed, but it is convenient to keep them to help sanity check a 
		// simulation that has been imported. The 'id' of a base simulation section may easily change, but its name and version
		// should be similar.
		this.setBaseUniqueName(bss.getUniqueName());
		this.setBaseVersion(bss.getVersion());

		// Inside a phase, the section can have different tab headings and
		// positions.
		this.setTab_heading(tab_heading);
		this.setTab_position(tab_position);

		this.save(schema);

	}

	/**
	 * Saves this SimulationSectionAssignment to the database.
	 * 
	 * @param schema
	 */
	public void save(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	/**
	 * Copies all of the information from this simulation section into a copy.
	 * 
	 * @return
	 */
	public SimulationSectionAssignment createCopy() {
		SimulationSectionAssignment copy = new SimulationSectionAssignment();

		copy.setActor_id(this.getActor_id());
		copy.setBase_sec_id(this.getBase_sec_id());
		copy.setDirectory(this.getDirectory());
		copy.setDisplaySectionId(this.getDisplaySectionId());
		copy.setPage_file_name(this.getPage_file_name());
		copy.setPhase_id(this.getPhase_id());
		copy.sendString = this.sendString;
		copy.setSimSubSection(this.isSimSubSection());
		copy.setSimSubSectionIndex(this.getSimSubSectionIndex());
		copy.setSim_id(this.getSim_id());
		copy.setTab_heading(this.getTab_heading());
		copy.setTabColor(this.getTabColor());
		copy.setTab_position(this.getTab_position());
		copy.setUrl(this.getUrl());
		

		return copy;
	}

	/**
	 * Uses the information in this simulation section to create the URL to be
	 * used for the bottom frame.
	 * 
	 * @return Returns the entire URL for the bottom frame.
	 */
	public String generateURLforBottomFrame(Long rs_id, Long actor_id, Long user_id) {

		String returnString = this.getUrl() + this.getDirectory() + this.getPage_file_name();

		returnString += "?cs_id=" + this.getBase_sec_id(); //$NON-NLS-1$

		Logger.getRootLogger().debug("sendString is " + this.sendString); //$NON-NLS-1$

		// String firstSep = "?";
		String subsequentSep = "&"; //$NON-NLS-1$
		// String thisSep = firstSep;
		String thisSep = subsequentSep;

		if ((this.sendString != null) && (this.sendString.length() > 0)) {
			if (this.sendString.charAt(POS_RS_ID) == '1') {
				returnString += (thisSep + "running_sim_id=" + rs_id); //$NON-NLS-1$
				thisSep = subsequentSep;
			}

			if (this.sendString.charAt(POS_A_ID) == '1') {
				returnString += (thisSep + "actor_id=" + actor_id); //$NON-NLS-1$
				thisSep = subsequentSep;
			}

			if (this.sendString.charAt(POS_U_ID) == '1') {
				returnString += (thisSep + "user_id=" + user_id); //$NON-NLS-1$
				thisSep = subsequentSep;
			}
		}

		Logger.getRootLogger().debug("returnString: " + returnString); //$NON-NLS-1$

		return returnString;
	}

	/**
	 * Returns a list of the base sim section ids for the simulation whose id
	 * was passed in.
	 * 
	 * @param schema
	 * @param sid
	 * @return
	 */
	public static List<Long> getBaseIdsBySim(String schema, Long sid) {

		if (sid == null) {

			Logger.getRootLogger().warn("sid: " + sid); //$NON-NLS-1$
			return new ArrayList<Long>();
		} else {

			String getHQL = "select DISTINCT ss.base_sec_id from SimulationSectionAssignment ss where SIM_ID = " //$NON-NLS-1$
					+ sid.toString() + " order by ss.base_sec_id"; //$NON-NLS-1$

			Logger.getRootLogger().debug(getHQL);

			MultiSchemaHibernateUtil.beginTransaction(schema);

			List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getHQL).list();

			if (returnList == null) {
				returnList = new ArrayList();
			}

			Logger.getRootLogger().debug("get # ids: " + returnList.size()); //$NON-NLS-1$

			return returnList;
		}

	}

	/**
	 * 
	 * @param schema
	 * @param sid
	 * @return
	 */
	public static List<BaseSimSection> getDistinctSectionsForSim(String schema, Long sid) {

		List<Long> baseIds = getBaseIdsBySim(schema, sid);

		ArrayList returnList = new ArrayList();

		MultiSchemaHibernateUtil.beginTransaction(schema);

		for (ListIterator<Long> bi = baseIds.listIterator(); bi.hasNext();) {
			Long bid = bi.next();

			if (bid != null) {

				BaseSimSection bss = (BaseSimSection) MultiSchemaHibernateUtil.getSession(schema).get(
						BaseSimSection.class, bid);

				returnList.add(bss);
			}
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;

	}

	/**
	 * Returns all of the sections for a particular simulation.
	 * 
	 * @param schema
	 * @param sid
	 * @return
	 */
	public static List<SimulationSectionAssignment> getBySim(String schema, Long sid) {

		if (sid == null) {

			Logger.getRootLogger().debug("sid: " + sid); //$NON-NLS-1$
			return new ArrayList<SimulationSectionAssignment>();
		} else {

			String getHQL = "from SimulationSectionAssignment where SIM_ID = " + sid.toString() + "order by simsec_id"; //$NON-NLS-1$ //$NON-NLS-2$

			MultiSchemaHibernateUtil.beginTransaction(schema);

			List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getHQL).list();

			if (returnList == null) {
				returnList = new ArrayList();
			}

			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			return returnList;
		}

	}

	/**
	 * Wrapper method to call method of same name.
	 * 
	 * @param schema
	 * @param sid
	 * @param aid
	 * @param pid
	 * @return
	 */
	public static List<SimulationSectionAssignment> getBySimAndActorAndPhase(String schema, Long sid, Long aid, Long pid) {

		return getBySimAndActorAndPhase(schema, sid, aid, pid, false);

	}

	public static SimulationSectionAssignment getSubSection(String schema, Long cs_id, int index, Long sid, Long aid,
			Long pid) {

		String getHQL = "from SimulationSectionAssignment where SIM_ID = " + sid + " AND ACTOR_ID = " + aid //$NON-NLS-1$ //$NON-NLS-2$
				+ " AND PHASE_ID = " + pid + " and simSubSection is true " + " and displaySectionId = " + cs_id //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ " and simSubSectionIndex = " + index + " order by TAB_POS"; //$NON-NLS-1$ //$NON-NLS-2$

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getHQL).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if ((returnList == null) || (returnList.size() == 0)) {
			return null;
		} else {
			SimulationSectionAssignment ss = (SimulationSectionAssignment) returnList.get(0);
			return ss;
		}

	}

	/**
	 * Returns a list of simulation sections for the particular actor at a
	 * particular phase. The list is ordered by the tab positions of the various
	 * sections.
	 * 
	 * NB: Actor id is 0 for the standard sections that have been moved into the
	 * default sections for a particular simulation.
	 * 
	 * @param sid
	 *            Simulation ID
	 * @param aid
	 *            Actor ID
	 * @param pid
	 *            Phase ID
	 * @return
	 */
	public static List<SimulationSectionAssignment> getBySimAndActorAndPhase(String schema, Long sid, Long aid,
			Long pid, boolean getSubSections) {

		if ((sid == null) || (aid == null) || (pid == null)) {

			Logger.getRootLogger().debug("sid/aid/pid: " + sid + "/" + aid + "/" + pid); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return new ArrayList<SimulationSectionAssignment>();
		} else {

			String getSub = " and simSubSection is false "; //$NON-NLS-1$

			if (getSubSections) {
				getSub = ""; //$NON-NLS-1$
			}

			String getHQL = "from SimulationSectionAssignment where SIM_ID = :sim_id AND ACTOR_ID = "
					+ ":act_id AND PHASE_ID = :p_id " + getSub + " order by TAB_POS"; //$NON-NLS-1$ //$NON-NLS-2$

			MultiSchemaHibernateUtil.beginTransaction(schema);

			List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getHQL)
				.setString("sim_id", sid.toString())
				.setString("act_id", aid.toString())
				.setString("p_id", pid.toString())
				.list();

			if (returnList == null) {
				returnList = new ArrayList();
			}

			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			return returnList;
		}

	}

	/**
	 * Gets the highest tab position for the set of sections assigned to a
	 * particular actor in a particular phase in a particular simulation.
	 * 
	 * @param schema
	 * @param sid
	 * @param aid
	 * @param pid
	 * @return
	 */
	public static Long getHighestBySimAndActorAndPhase(String schema, Long sid, Long aid, Long pid) {

		List returnList = getBySimAndActorAndPhase(schema, sid, aid, pid);

		if ((returnList == null) || (returnList.size() == 0)) {
			Logger.getRootLogger().debug("returning highest tab = 1"); //$NON-NLS-1$
			return new Long(1);
		} else {
			Logger.getRootLogger().debug("returning highest tab = " + (returnList.size() + 1)); //$NON-NLS-1$
			return new Long(returnList.size() + 1);
		}
	}

	/**
	 * Gets a specific simulation section assignment.
	 * 
	 * @param schema
	 * @param sid
	 * @param aid
	 * @param pid
	 * @param tab_pos
	 * @return
	 */
	public static SimulationSectionAssignment getBySimAndActorAndPhaseAndPos(String schema, Long sid, Long aid,
			Long pid, int tab_pos) {

		if ((sid == null) || (aid == null) || (pid == null)) {

			Logger.getRootLogger().debug("Error: sid/aid/pid: " + sid + "/" + aid + "/" + pid); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return null;
		} else {

			String getHQL = "from SimulationSectionAssignment where SIM_ID = " + sid.toString() + " AND ACTOR_ID = " //$NON-NLS-1$ //$NON-NLS-2$
					+ aid.toString() + " AND PHASE_ID = " + pid.toString() + " AND TAB_POS = " + tab_pos //$NON-NLS-1$ //$NON-NLS-2$
					+ " and simSubSection = false"; //$NON-NLS-1$

			Logger.getRootLogger().debug(getHQL);
			MultiSchemaHibernateUtil.beginTransaction(schema);
			List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getHQL).list();

			// If list has gotten out of order, attempt cleaning it up.
			if ((returnList == null) || (returnList.size() != 1)) {
				Logger.getRootLogger().debug("got wrong number of sections"); //$NON-NLS-1$

				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
				// Reorder and try it again.
				reorder(schema, sid, aid, pid);

				MultiSchemaHibernateUtil.beginTransaction(schema);
				returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getHQL).list();

			}

			if ((returnList == null) || (returnList.size() != 1)) {

				Logger.getRootLogger().debug("Still have got the wrong number of sections after reordering. Returning null."); //$NON-NLS-1$

				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
				return null;
			} else {
				SimulationSectionAssignment sso = (SimulationSectionAssignment) returnList.get(0);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
				return sso;
			}

		}

	}

	/**
	 * 
	 * @param schema
	 * @param sim
	 * @param pid
	 * @param bss_id
	 * @param tab_head
	 * @param cids
	 */
	public static void applySectionsToSomeActors(String schema, Simulation sim, Long pid, Long bss_id, String tab_head,
			List<ConvActorAssignment> cids) {

		// Get, and remove, and occurance of this section for all actors.
		List removeList = SimulationSectionAssignment.getBySimAndPhaseAndBSSid(schema, sim.getId(), pid, bss_id);
		for (ListIterator<SimulationSectionAssignment> lr = removeList.listIterator(); lr.hasNext();) {
			SimulationSectionAssignment s_gone = lr.next();

			SimulationSectionAssignment.remove(schema, s_gone);

		}

		// Apply this section to all applicable actors.
		for (ListIterator lia = cids.listIterator(); lia.hasNext();) {

			ConvActorAssignment caa = (ConvActorAssignment) lia.next();

			Long a_id = caa.getActor_id();

			// If this doesn't work, try the getBySimAndActorAndPhase
			int highestTab = Actor.getHighestTabPosForPhase(schema, sim.getId(), pid, a_id);

			@SuppressWarnings("unused")
			SimulationSectionAssignment ss0 = new SimulationSectionAssignment(schema, sim.getId(), a_id, pid, bss_id,
					tab_head, highestTab);

		}

	}

	/** Applies this section to a list of Actors 
	 * 
	 * @param schema
	 * @param sim_id
	 * @param pid
	 * @param sec_id
	 * @param tab_head
	 * @param a_ids
	 */
	public static void applySectionToSpecificActors(String schema, Long sim_id, Long pid, Long sec_id, String tab_head,
			List<Long> a_ids) {

		// //////////////////////////////////////////////////////////////
		// Get, and remove, and occurance of this section for all actors.
		List removeList = SimulationSectionAssignment.getBySimAndPhaseAndBSSid(schema, sim_id, pid, sec_id);

		for (ListIterator<SimulationSectionAssignment> lr = removeList.listIterator(); lr.hasNext();) {
			SimulationSectionAssignment s_gone = lr.next();

			SimulationSectionAssignment.remove(schema, s_gone);

		}
		// ///////////////////////////////////////////////////////////////

		// ///////////////////////////////////////////////////////////////
		// Apply this section to all applicable actors.
		for (ListIterator lia = a_ids.listIterator(); lia.hasNext();) {

			Long a_id = (Long) lia.next();

			// If this doesn't work, try the getBySimAndActorAndPhase
			int highestTab = Actor.getHighestTabPosForPhase(schema, sim_id, pid, a_id);

			@SuppressWarnings("unused")
			SimulationSectionAssignment ss0 = new SimulationSectionAssignment(schema, sim_id, a_id, pid, sec_id,
					tab_head, highestTab);
		}
		// ///////////////////////////////////////////////////////////////

	}

	/**
	 * 
	 * @param schema
	 * @param sid
	 * @param pid
	 * @param bss_id
	 * @return
	 */
	private static List<SimulationSectionAssignment> getBySimAndPhaseAndBSSid(String schema, Long sid, Long pid,
			Long bss_id) {

		String getHQL = "from SimulationSectionAssignment where SIM_ID = " + sid + " AND BASE_SEC_ID = " + bss_id //$NON-NLS-1$ //$NON-NLS-2$
				+ " AND PHASE_ID = " + pid.toString() + " order by TAB_POS"; //$NON-NLS-1$ //$NON-NLS-2$

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<SimulationSectionAssignment> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getHQL)
				.list();

		if (returnList == null) {
			returnList = new ArrayList<SimulationSectionAssignment>();
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		return returnList;

	}

	/**
	 * Applies the 'universal' sections to all actors for all phases.
	 * 
	 * @param schema
	 * @param sim
	 */
	public static void applyAllUniversalSections(String schema, Long s_id) {

		List phases = SimPhaseAssignment.getPhasesForSim(schema, s_id);

		for (ListIterator lia = phases.listIterator(); lia.hasNext();) {
			SimulationPhase sp = (SimulationPhase) lia.next();

			applyUniversalSectionsToAllActorsForPhase(schema, s_id, sp.getId());
		}
	}

	/**
	 * Applies the universal sections (those with actor_id = 0) to all of the
	 * phases for an actor.
	 * 
	 * @param schema
	 * @param sim_id
	 * @param actor_id
	 */
	public static void applyAllUniversalSectionsToAnActor(String schema, Long sim_id, Long actor_id) {

		List phases = SimPhaseAssignment.getPhasesForSim(schema, sim_id);

		for (ListIterator lia = phases.listIterator(); lia.hasNext();) {
			SimulationPhase sp = (SimulationPhase) lia.next();

			// Get list of universal sections (actor id = 0)
			List<SimulationSectionAssignment> universalList = getBySimAndActorAndPhase(schema, sim_id, new Long(0), sp
					.getId());

			applyUniversalsToActor(schema, sim_id, universalList, actor_id, sp.getId());

		}
	}

	/**
	 * Applies the default sections (those with actor id = 0) to all of the
	 * actors in a particular simulation during a particular phase.
	 * 
	 * For example, if when the game is in the 'in progress' phase, an actor
	 * normally gets an introduction page, a role page and a chat page, then
	 * that is what each actor will get.
	 * 
	 * @param sid
	 * @param pid
	 */
	public static void applyUniversalSectionsToAllActorsForPhase(String schema, Long sid, Long pid) {

		// Get list of universal sections (actor id = 0)
		List<SimulationSectionAssignment> universalList = getBySimAndActorAndPhase(schema, sid, new Long(0), pid);

		// Get the list of actors
		List actorList = SimActorAssignment.getActorsForSim(schema, sid);

		// Assign the defaults to each actor
		for (ListIterator lia = actorList.listIterator(); lia.hasNext();) {
			Actor act = (Actor) lia.next();

			Logger.getRootLogger().debug("checking universals on " + act.getName()); //$NON-NLS-1$

			applyUniversalsToActor(schema, sid, universalList, act.getId(), pid);

		} // End of loop over actors

	}

	/**
	 * 
	 * @param schema
	 * @param s_id
	 * @param universalList
	 * @param a_id
	 * @param p_id
	 */
	public static void applyUniversalsToActor(String schema, Long s_id, List universalList, Long a_id, Long p_id) {
		// Check to see if this section already exists in this actor's set.
		// If not, then add it.
		for (ListIterator lis = universalList.listIterator(); lis.hasNext();) {
			SimulationSectionAssignment ss = (SimulationSectionAssignment) lis.next();

			Logger.getRootLogger().debug("     checking universalList on " + ss.getTab_heading()); //$NON-NLS-1$

			boolean foundThisSection = determineIfActorHasThisSectionAtThisPhase(schema, 
					s_id, a_id, p_id, ss.getBase_sec_id());

			/*
			for (ListIterator listOld = currentActorsList.listIterator(); listOld.hasNext();) {
				SimulationSectionAssignment ss_old = (SimulationSectionAssignment) listOld.next();

				Logger.getRootLogger().debug("             comparing " + ss_old.getBase_section_id() + " and " //$NON-NLS-1$ //$NON-NLS-2$
						+ ss.getBase_section_id());
				if (ss_old.getBase_section_id().equals(ss.getBase_section_id())) {
					Logger.getRootLogger().debug("             found match!"); //$NON-NLS-1$
					foundThisSection = true;
				}

			} */
			
			if (!foundThisSection) {
				
				List currentActorsList = getBySimAndActorAndPhase(schema, s_id, a_id, p_id);
				
				MultiSchemaHibernateUtil.beginTransaction(schema);

				SimulationSectionAssignment ss_new = ss.createCopy();

				ss_new.setActor_id(a_id);
				ss_new.setAddedAsUniversalSection(true);

				ss_new.setTab_position(currentActorsList.size() + 1);

				MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(ss_new);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
			}

		}
	}
	
	public static boolean determineIfActorHasThisSectionAtThisPhase(String schema, 
			Long s_id, Long a_id, Long p_id, Long ssa_base_id){
		
		boolean foundThisSection = false;

		List currentActorsList = getBySimAndActorAndPhase(schema, s_id, a_id, p_id);

		for (ListIterator listOld = currentActorsList.listIterator(); listOld.hasNext();) {
			
			SimulationSectionAssignment ss_old = (SimulationSectionAssignment) listOld.next();

			Logger.getRootLogger().debug("             comparing " + ss_old.getBase_sec_id() + " and " //$NON-NLS-1$ //$NON-NLS-2$
					+ ssa_base_id);
			if (ss_old.getBase_sec_id().equals(ssa_base_id)) {
				Logger.getRootLogger().debug("             found match!"); //$NON-NLS-1$
				foundThisSection = true;
			}

		}
		return foundThisSection;
	}

	/**
	 * Changes positions of two simulation sections.
	 * 
	 * @param sec_id
	 */
	public static void exchangePositions(String first_id, String second_id, org.hibernate.Session hibernate_session) {

		SimulationSectionAssignment first_ss = (SimulationSectionAssignment) hibernate_session.get(
				SimulationSectionAssignment.class, new Long(first_id));

		SimulationSectionAssignment sec_ss = (SimulationSectionAssignment) hibernate_session.get(
				SimulationSectionAssignment.class, new Long(second_id));

		int first_pos = first_ss.getTab_position();
		int sec_pos = sec_ss.getTab_position();

		first_ss.setTab_position(sec_pos);
		sec_ss.setTab_position(first_pos);

		hibernate_session.saveOrUpdate(first_ss);
		hibernate_session.saveOrUpdate(sec_ss);

	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSim_id() {
		return this.sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getActor_id() {
		return this.actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public Long getPhase_id() {
		return this.phase_id;
	}

	public void setPhase_id(Long phase_id) {
		this.phase_id = phase_id;
	}

	public int getTab_position() {
		return this.tab_position;
	}

	public void setTab_position(int tab_position) {
		this.tab_position = tab_position;
	}
	
	public String getTabColor() {
		return this.tabColor;
	}

	public void setTabColor(String tabColor) {
		this.tabColor = tabColor;
	}

	public String getTab_heading() {
		return this.tab_heading;
	}

	public void setTab_heading(String tab_heading) {
		this.tab_heading = tab_heading;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDirectory() {
		return this.directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getPage_file_name() {
		return this.page_file_name;
	}

	public void setPage_file_name(String page_file_name) {
		this.page_file_name = page_file_name;
	}

	public boolean isAddedAsUniversalSection() {
		return this.addedAsUniversalSection;
	}

	public void setAddedAsUniversalSection(boolean addedAsUniversalSection) {
		this.addedAsUniversalSection = addedAsUniversalSection;
	}

	public String getSendString() {
		return sendString;
	}

	public void setSendString(String sendString) {
		this.sendString = sendString;
	}

	public Long getBase_sec_id() {
		return base_sec_id;
	}

	public void setBase_sec_id(Long base_sec_id) {
		this.base_sec_id = base_sec_id;
	}

	public String getBaseUniqueName() {
		return baseUniqueName;
	}

	public void setBaseUniqueName(String base_unique_name) {
		this.baseUniqueName = base_unique_name;
	}

	public String getBaseVersion() {
		return baseVersion;
	}

	public void setBaseVersion(String base_version) {
		this.baseVersion = base_version;
	}

}
