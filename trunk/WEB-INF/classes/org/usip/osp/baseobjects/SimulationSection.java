package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.communications.ChatController;
import org.usip.osp.communications.ConvActorAssignment;
import org.usip.osp.communications.Conversation;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * @author Ronald "Skip" Cole
 * 
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can
 * redistribute it and/or modify it under the terms of the new BSD Style license
 * associated with this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "SIMULATION_SECTIONS")
@Proxy(lazy = false)
public class SimulationSection {

	@Id
	@GeneratedValue
	@Column(name = "SIMSEC_ID")
	private Long id;

	@Column(name = "SIM_ID")
	private Long sim_id;

	@Column(name = "ACTOR_ID")
	private Long actor_id;

	@Column(name = "PHASE_ID")
	private Long phase_id;

	@Column(name = "TAB_POS")
	private int tab_position = 0;

	/** The tab heading of this section. */
	@Column(name = "TAB_HEADING")
	private String tab_heading;

	/** The id of the base section that this section was 'cast' from. */
	@Column(name = "BASE_SEC_ID")
	private Long base_sec_id;

	/** URL of this section */
	@Column(name = "SIMSEC_URL")
	private String url = "";

	/** Directory of this section */
	@Column(name = "SIMSEC_DIR")
	private String directory = "";

	/** Filename of this section */
	@Column(name = "SIMSEC_FILENAME")
	private String page_file_name = "";

	@Column(name = "SIMSEC_OBJ_TAG")
	private String object_tag = "";

	@Column(name = "ADDED_AS_UNIV")
	private boolean addedAsUniversalSection = false;

	/** Zero argument constructor needed by Hibernate. */
	public SimulationSection() {

	}
	
	public static void removeAndReorder(String schema, SimulationSection s_gone){
		
		Long sid = s_gone.getSim_id();
		Long aid = s_gone.getActor_id();
		Long pid = s_gone.getPhase_id();
		
		remove(schema, s_gone);
		
		List survivors = getBySimAndActorAndPhase(schema, sid, aid, pid);
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		reorderSet(survivors, MultiSchemaHibernateUtil.getSession(schema));
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
	}

	/**
	 * 
	 * @param schema
	 * @param ss
	 */
	public static void remove(String schema, SimulationSection s_gone) {
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
	public SimulationSection(String schema, Long sid, Long aid, Long pid,
			Long bss_id, String tab_heading, int tab_position) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		BaseSimSection bss = (BaseSimSection) MultiSchemaHibernateUtil
				.getSession(schema).get(BaseSimSection.class, bss_id);

		MultiSchemaHibernateUtil.getSession(schema).evict(bss);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		// These three ids locate the section to a simulation, actor and phase.
		this.setSim_id(sid);
		this.setActor_id(aid);
		this.setPhase_id(pid);

		// These variables are copied straight from the template
		this.setBase_section_id(bss.getId());
		this.setUrl(bss.getUrl());
		this.setDirectory(bss.getDirectory());

		this.setPage_file_name(bss.getPage_file_name());

		// Give this simulation this conversation, which will then be copied
		// into the running sims
		// This needs to be generalized.
		if ((bss.getPage_file_name() != null)
				&& (bss.getPage_file_name()
						.equalsIgnoreCase("broadcast_screen.jsp"))) {
			Conversation conv = ChatController.createConversation(schema,
					"broadcast", bss, sid);
			this.setPage_file_name("broadcast_screen.jsp?conversation_id="
					+ conv.getId());
			this.setObject_tag("broadcast");
		}

		// Inside a phase, the section can have different tab headings and
		// positions.
		this.setTab_heading(tab_heading);
		this.setTab_position(tab_position);

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).save(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}

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
	public SimulationSection createCopy() {
		SimulationSection copy = new SimulationSection();

		copy.setActor_id(this.getActor_id());
		copy.setBase_section_id(this.getBase_section_id());
		copy.setDirectory(this.getDirectory());
		copy.setPage_file_name(this.getPage_file_name());
		copy.setPhase_id(this.getPhase_id());
		copy.setSim_id(this.getSim_id());
		copy.setTab_heading(this.getTab_heading());
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
	public String generateURLforBottomFrame() {

		String returnString = this.getUrl() + this.getDirectory()
				+ this.getPage_file_name();

		return returnString;
	}
	
	public static List<Long> getBaseIdsBySim(
			String schema, Long sid) {

		if (sid == null) {

			System.out.println("sid: " + sid);
			return new ArrayList<Long>();
		} else {

			String getHQL = "select DISTINCT ss.base_sec_id from SimulationSection ss where SIM_ID = "
					+ sid.toString(); //
			//+ " order by base_sec_id";
			
			System.out.println(getHQL);

			MultiSchemaHibernateUtil.beginTransaction(schema);

			List returnList = MultiSchemaHibernateUtil.getSession(schema)
					.createQuery(getHQL).list();

			if (returnList == null) {
				returnList = new ArrayList();
			}
			
			System.out.println("get # ids: " + returnList.size());

			return returnList;
		}

	}
	
	public static List <BaseSimSection> getDistinctSectionsForSim(String schema, Long  sid){
		
		List<Long> baseIds =  getBaseIdsBySim(schema, sid);
		
		ArrayList returnList = new ArrayList();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		for (ListIterator<Long> bi = baseIds.listIterator(); bi.hasNext();) {
			Long bid = (Long) bi.next();
			
			BaseSimSection bss = (BaseSimSection)  MultiSchemaHibernateUtil.getSession(schema).get(BaseSimSection.class, bid);
		
			returnList.add(bss);
		}
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
		
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
	public static List<SimulationSection> getBySimAndActorAndPhase(
			String schema, Long sid, Long aid, Long pid) {

		if ((sid == null) || (aid == null) || (pid == null)) {

			System.out.println("sid/aid/pid: " + sid + "/" + aid + "/" + pid);
			return new ArrayList<SimulationSection>();
		} else {

			String getHQL = "from SimulationSection where SIM_ID = "
					+ sid.toString() + " AND ACTOR_ID = " + aid.toString()
					+ " AND PHASE_ID = " + pid.toString() + " order by TAB_POS";

			MultiSchemaHibernateUtil.beginTransaction(schema);

			List returnList = MultiSchemaHibernateUtil.getSession(schema)
					.createQuery(getHQL).list();

			if (returnList == null) {
				returnList = new ArrayList();
			}
			
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			return returnList;
		}

	}
	
	/**
	 * Gets the highest tab position for the set of sections assigned to a particular
	 * actor in a particular phase in a particular simulation.
	 * 
	 * @param schema
	 * @param sid
	 * @param aid
	 * @param pid
	 * @return
	 */
	public static Long getHighestBySimAndActorAndPhase(
			String schema, Long sid, Long aid, Long pid) {
		
		List returnList = getBySimAndActorAndPhase(schema, sid, aid, pid);
		
		if ((returnList == null) || (returnList.size() == 0)){
			System.out.println("returning highest tab = 1");
			return new Long(1);
		} else {
			System.out.println("returning highest tab = " + (returnList.size() + 1));
			return new Long(returnList.size() + 1);
		}
	}

	public static SimulationSection getBySimAndActorAndPhaseAndPos(
			String schema, Long sid, Long aid, Long pid, int tab_pos) {

		if ((sid == null) || (aid == null) || (pid == null)) {

			System.out.println("Error: sid/aid/pid: " + sid + "/" + aid + "/"
					+ pid);
			return null;
		} else {

			String getHQL = "from SimulationSection where SIM_ID = "
					+ sid.toString() + " AND ACTOR_ID = " + aid.toString()
					+ " AND PHASE_ID = " + pid.toString() + " AND TAB_POS = "
					+ tab_pos;

			MultiSchemaHibernateUtil.beginTransaction(schema);

			List returnList = MultiSchemaHibernateUtil.getSession(schema)
					.createQuery(getHQL).list();

			if ((returnList == null) || (returnList.size() != 1)) {
				System.out.println("got wrong number of sections");
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
				return null;
			} else {
				SimulationSection sso = (SimulationSection) returnList.get(0);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
				return sso;
			}

		}

	}

	public static void applySectionsToSomeActors(String schema, Simulation sim,
			Long pid, Long bss_id, String tab_head, List<ConvActorAssignment> cids) {

		// Get, and remove, and occurance of this section for all actors.
		List removeList = SimulationSection.getBySimAndPhaseAndBSSid(schema,
				sim.getId(), pid, bss_id);
		for (ListIterator<SimulationSection> lr = removeList.listIterator(); lr
				.hasNext();) {
			SimulationSection s_gone = (SimulationSection) lr.next();

			SimulationSection.remove(schema, s_gone);

		}

		// Apply this section to all applicable actors.
		for (ListIterator lia = cids.listIterator(); lia.hasNext();) {
			
			ConvActorAssignment caa = (ConvActorAssignment) lia.next();
			
			Long a_id = caa.getActor_id();

			// If this doesn't work, try the getBySimAndActorAndPhase
			int highestTab = Actor.getHighestTabPosForPhase(schema, sim.getId(), pid,
					a_id);

			SimulationSection ss0 = new SimulationSection(schema, sim.getId(),
					a_id, pid, bss_id, tab_head, highestTab);

		}

	}
	
	public static void applySectionToSpecificActors(String schema, Simulation sim,
			Long pid, Long sec_id, String tab_head, List<Long> a_ids) {

		////////////////////////////////////////////////////////////////
		// Get, and remove, and occurance of this section for all actors.
		List removeList = SimulationSection.getBySimAndPhaseAndBSSid(schema,
				sim.getId(), pid, sec_id);
		
		for (ListIterator<SimulationSection> lr = removeList.listIterator(); lr
				.hasNext();) {
			SimulationSection s_gone = (SimulationSection) lr.next();

			SimulationSection.remove(schema, s_gone);

		}
		/////////////////////////////////////////////////////////////////

		/////////////////////////////////////////////////////////////////
		// Apply this section to all applicable actors.
		for (ListIterator lia = a_ids.listIterator(); lia.hasNext();) {
			
			Long a_id = (Long) lia.next();

			// If this doesn't work, try the getBySimAndActorAndPhase
			int highestTab = Actor.getHighestTabPosForPhase(schema, sim.getId(), pid,
					a_id);

			SimulationSection ss0 = new SimulationSection(schema, sim.getId(),
					a_id, pid, sec_id, tab_head, highestTab);
		}
		/////////////////////////////////////////////////////////////////

	}

	/**
	 * 
	 * @param schema
	 * @param sid
	 * @param pid
	 * @param bss_id
	 * @return
	 */
	private static List<SimulationSection> getBySimAndPhaseAndBSSid(String schema, Long sid,
			Long pid, Long bss_id) {


		String getHQL = "from SimulationSection where SIM_ID = "
				+ sid + " AND BASE_SEC_ID = " + bss_id
				+ " AND PHASE_ID = " + pid.toString() + " order by TAB_POS";

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<SimulationSection> returnList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery(getHQL).list();

		if (returnList == null) {
			returnList = new ArrayList<SimulationSection>();
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		return returnList;

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
	public static void applyUniversalSectionsToAllActors(String schema,
			Simulation sim, Long pid) {

		// Get list of universal sections (actor id = 0)
		List universalList = getBySimAndActorAndPhase(schema, sim.getId(),
				new Long(0), pid);

		// Get the list of actors
		List actorList = sim.getActors();

		// Assign the defaults to each actor
		for (ListIterator lia = actorList.listIterator(); lia.hasNext();) {
			Actor act = (Actor) lia.next();

			System.out.println("checking universals on " + act.getName());

			// Check to see if this section already exists in this actor's set.
			// If not, then add it.
			for (ListIterator lis = universalList.listIterator(); lis.hasNext();) {
				SimulationSection ss = (SimulationSection) lis.next();

				System.out.println("     checking universalList on "
						+ ss.getTab_heading());

				boolean foundThisSection = false;

				List currentActorsList = getBySimAndActorAndPhase(schema, sim
						.getId(), act.getId(), pid);

				for (ListIterator listOld = currentActorsList.listIterator(); listOld
						.hasNext();) {
					SimulationSection ss_old = (SimulationSection) listOld
							.next();

					System.out.println("             comparing "
							+ ss_old.getBase_section_id() + " and "
							+ ss.getBase_section_id());
					if (ss_old.getBase_section_id().equals(
							ss.getBase_section_id())) {
						System.out.println("             found match!");
						foundThisSection = true;
					}

				}
				if (!foundThisSection) {
					MultiSchemaHibernateUtil.beginTransaction(schema);
					
					SimulationSection ss_new = ss.createCopy();

					ss_new.setActor_id(act.getId());
					ss_new.setAddedAsUniversalSection(true);

					ss_new.setTab_position(currentActorsList.size() + 1);

					MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(
							ss_new);
					MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
				}

			}
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
	public static void _defunctApplyDefaultSectionsToAllActors(String schema,
			Simulation sim, Long pid) {

		// Get list of default sections (actor id = 0)
		List defaultList = getBySimAndActorAndPhase(schema, sim.getId(),
				new Long(0), pid);

		// Get the list of actors
		List actorList = sim.getActors();

		// Assign the defaults to each actor
		for (ListIterator lia = actorList.listIterator(); lia.hasNext();) {
			Actor act = (Actor) lia.next();

			MultiSchemaHibernateUtil.beginTransaction(schema);

			// Remove simulation sections assigned to this simulation and phase
			List removeList = getBySimAndActorAndPhase(schema, sim.getId(), act
					.getId(), pid);
			for (ListIterator lis = removeList.listIterator(); lis.hasNext();) {
				SimulationSection ss = (SimulationSection) lis.next();

				MultiSchemaHibernateUtil.getSession(schema).delete(ss);

			}

			for (ListIterator lis = defaultList.listIterator(); lis.hasNext();) {
				SimulationSection ss = (SimulationSection) lis.next();

				SimulationSection ss_new = ss.createCopy();

				ss_new.setActor_id(act.getId());

				MultiSchemaHibernateUtil.getSession(schema)
						.saveOrUpdate(ss_new);

			}

			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		}

	}

	/**
	 * Changes positions of two simulation sections.
	 * 
	 * @param sec_id
	 */
	public static void exchangePositions(String first_id, String second_id,
			org.hibernate.Session hibernate_session) {

		SimulationSection first_ss = (SimulationSection) hibernate_session.get(
				SimulationSection.class, new Long(first_id));

		SimulationSection sec_ss = (SimulationSection) hibernate_session.get(
				SimulationSection.class, new Long(second_id));

		int first_pos = first_ss.getTab_position();
		int sec_pos = sec_ss.getTab_position();

		first_ss.setTab_position(sec_pos);
		sec_ss.setTab_position(first_pos);

		hibernate_session.saveOrUpdate(first_ss);
		hibernate_session.saveOrUpdate(sec_ss);

	}


	/**
	 * Sets the tab positions of a list of simulation sections to be equivalent
	 * to their position in the list.
	 * 
	 * @param listOfSimSections
	 */
	public static void reorderSet(List<SimulationSection> listOfSimSections,
			org.hibernate.Session hibernate_session) {

		int ii = 1;

		for (ListIterator li = listOfSimSections.listIterator(); li.hasNext();) {
			SimulationSection ss = (SimulationSection) li.next();

			ss.setTab_position(ii);
			hibernate_session.saveOrUpdate(ss);

			++ii;

		}

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getActor_id() {
		return actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public Long getPhase_id() {
		return phase_id;
	}

	public void setPhase_id(Long phase_id) {
		this.phase_id = phase_id;
	}

	public int getTab_position() {
		return tab_position;
	}

	public void setTab_position(int tab_position) {
		this.tab_position = tab_position;
	}

	public String getTab_heading() {
		return tab_heading;
	}

	public void setTab_heading(String tab_heading) {
		this.tab_heading = tab_heading;
	}

	public Long getBase_section_id() {
		return base_sec_id;
	}

	public void setBase_section_id(Long base_section_id) {
		this.base_sec_id = base_section_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getPage_file_name() {
		return page_file_name;
	}

	public void setPage_file_name(String page_file_name) {
		this.page_file_name = page_file_name;
	}

	public String getObject_tag() {
		return object_tag;
	}

	public void setObject_tag(String object_tag) {
		this.object_tag = object_tag;
	}

	public boolean isAddedAsUniversalSection() {
		return addedAsUniversalSection;
	}

	public void setAddedAsUniversalSection(boolean addedAsUniversalSection) {
		this.addedAsUniversalSection = addedAsUniversalSection;
	}

}
