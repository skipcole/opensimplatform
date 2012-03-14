package org.usip.osp.communications;

import java.util.*;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Actor;
import org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment;
import org.usip.osp.baseobjects.SimSectionDependentObject;
import org.usip.osp.baseobjects.SimSectionRSDepOjbectAssignment;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.networking.AuthorFacilitatorSessionObject;
import org.usip.osp.networking.SessionObjectBase;
import org.usip.osp.networking.USIP_OSP_Cache;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents a particular conversation amongst a group of actors.
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
@Table(name = "CONVERSATIONS")
@Proxy(lazy = false)
public class Conversation implements SimSectionDependentObject {

	public Conversation() {

	}

	public Conversation(String uniqName, String docNotes, Long _sim_id) {
		this.uniqueConvName = uniqName;
		this.conversationNotes = docNotes;
		this.sim_id = _sim_id;
	}

	/** This conversation is of an undefined type. */
	public static final int TYPE_UNDEFINED = 0;

	/** This is a broadcast conversation. */
	public static final int TYPE_BROADCAST = 1;

	/** This is a private conversation. */
	public static final int TYPE_PRIVATE = 2;

	/** This is a chat help conversation. */
	public static final int TYPE_CHAT_HELP = 3;

	/** This is a caucus conversation. */
	public static final int TYPE_CAUCUS = 4;

	/** This is a chat room that the player can invite people to leave or enter. */
	public static final int TYPE_BASE_USER_CONTROLLED_CAUCUS = 5;

	/** This is a chat room that the player can invite people to leave or enter. */
	public static final int TYPE_RS_USER_CONTROLLED_CAUCUS = 6;

	/** This is a chat room that the player can invite people to leave or enter. */
	public static final int TYPE_STUDENT_CHAT = 7;

	/** Unique identifier of this name. */
	private String uniqueConvName = ""; //$NON-NLS-1$

	/**
	 * Saves the conversation and makes sure it is affiliated with the
	 * simulation at hand.
	 */
	public void save(String schema, Long sim_id) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}

	@Id
	@GeneratedValue
	@Column(name = "CONV_ID")
	private Long id;

	@Column(name = "SIM_ID")
	private Long sim_id;

	/**
	 * If this is a user controlled conversation, this will contain the id of
	 * the base conversation from which information is originally pulled.
	 */
	@Column(name = "BASE_CONV_ID")
	private Long base_conv_id;

	@Column(name = "RS_ID")
	private Long rs_id;

	@Column(name = "CONV_TYPE")
	private int conversationType = TYPE_UNDEFINED;

	@Lob
	private String conversationNotes = "";

	@Transient
	private List<ConvActorAssignment> conv_actor_assigns = null;

	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransitId() {
		return this.transit_id;
	}

	public void setTransitId(Long transit_id) {
		this.transit_id = transit_id;
	}

	/**
	 * Returns a list of all conversations associated with a particular
	 * simulation.
	 */
	public static List getAllBaseForSim(String schema, Long simid) {

		if (simid == null) {
			return new ArrayList();
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Conversation> returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from Conversation where sim_id = :simid and rs_id is null")
				.setLong("simid", simid).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	/**
	 * Returns a list of all conversations associated with a particular
	 * simulation.
	 */
	public static List getAllForRunningSim(String schema, Long simid, Long rs_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Conversation> returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from Conversation where sim_id = " + simid + " and rs_id = " + rs_id).list(); //$NON-NLS-1$ //$NON-NLS-2$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	/**
	 * 
	 * @param schema
	 * @return
	 */
	public String getListOfActors(String schema, SessionObjectBase sob,
			HttpServletRequest request) {

		String returnString = ""; //$NON-NLS-1$

		List cca_list = getConv_actor_assigns(schema);

		for (ListIterator<ConvActorAssignment> bi = cca_list.listIterator(); bi
				.hasNext();) {
			ConvActorAssignment caa = bi.next();

			String a_name = USIP_OSP_Cache.getActorName(sob.schema, sob.sim_id,
					sob.getRunningSimId(), request, caa.getActor_id());

			returnString += a_name + "-"; //$NON-NLS-1$
		}

		if (returnString.length() > 0) {
			returnString = returnString.substring(0, returnString.length() - 1);
		}

		return returnString;
	}

	/**
	 * 
	 * @param schema
	 * @param simid
	 * @param aid
	 * @param rs_id
	 * @param section_id
	 * @return
	 */
	public static List getActorsConversationsForSimSection(String schema,
			Long aid, Long rs_id, Long section_id) {

		List returnList = new ArrayList();

		List baseList = SimSectionRSDepOjbectAssignment
				.getAllForRunningSimSection(schema, rs_id, section_id);

		for (ListIterator<SimSectionRSDepOjbectAssignment> li = baseList
				.listIterator(); li.hasNext();) {
			SimSectionRSDepOjbectAssignment ssrsdoa = li.next();

			List actorsAssignedToThisConversation = ConvActorAssignment
					.getAllForConversation(schema, ssrsdoa.getObjectId());

			for (ListIterator<ConvActorAssignment> bi = actorsAssignedToThisConversation
					.listIterator(); bi.hasNext();) {
				ConvActorAssignment caa = bi.next();

				if (caa.getActor_id().equals(aid)) {
					Conversation conv = new Conversation();
					conv.setId(ssrsdoa.getObjectId());
					returnList.add(conv);
				}
			}
		}

		return returnList;
	}

	/**
	 * 
	 * @param schema
	 * @param simid
	 * @param aid
	 * @param rs_id
	 * @param section_id
	 * @return
	 */
	public static List getAllConversationsForSimSection(String schema,
			Long aid, Long rs_id, Long section_id) {

		List returnList = new ArrayList();

		List baseList = SimSectionRSDepOjbectAssignment
				.getAllForRunningSimSection(schema, rs_id, section_id);

		for (ListIterator<SimSectionRSDepOjbectAssignment> li = baseList
				.listIterator(); li.hasNext();) {
			SimSectionRSDepOjbectAssignment bssdoa = li.next();

			Conversation conv = new Conversation();
			conv.setId(bssdoa.getObjectId());
			returnList.add(conv);

		}

		return returnList;
	}

	/**
	 * 
	 * @param schema
	 * @param simid
	 * @param aid
	 * @return
	 */
	public static List getActorsPrivateChats(String schema, Long simid, Long aid) {

		List baseList = getAllPrivateChatForSim(schema, simid);
		ArrayList returnList = new ArrayList();

		for (ListIterator<Conversation> li = baseList.listIterator(); li
				.hasNext();) {
			Conversation conv_id = li.next();

			MultiSchemaHibernateUtil.beginTransaction(schema);

			Conversation conv = (Conversation) MultiSchemaHibernateUtil
					.getSession(schema)
					.get(Conversation.class, conv_id.getId());

			if (conv.hasActor(schema, aid)) {
				returnList.add(conv);
			}

			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		}

		return returnList;

	}

	/**
	 * Returns a list of all conversations associated with a particular
	 * simulation.
	 */
	public static List getAllPrivateChatForSim(String schema, Long simid) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		String getSQL = "from Conversation where sim_id = " + simid + " and conv_type = " + TYPE_PRIVATE; //$NON-NLS-1$ //$NON-NLS-2$

		List<Conversation> returnList = MultiSchemaHibernateUtil
				.getSession(schema).createQuery(getSQL).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param section_id
	 * @param rs_id
	 * @param actor1
	 * @return
	 */
	public static List getAllChatHelpConversationsForHelper(String schema,
			Long sim_id, Long section_id, Long rs_id, Long actor1) {

		ArrayList returnList = new ArrayList<Conversation>();

		List<Actor> actList = Actor.getAllForSimulation(schema, sim_id);

		for (ListIterator<Actor> li = actList.listIterator(); li.hasNext();) {

			Actor act = li.next();

			Conversation conv = Conversation.getChatHelpConversation(schema,
					sim_id, section_id, rs_id, act.getId(), actor1);

			if (conv != null) {
				returnList.add(conv);
			}
		}

		return returnList;
	}

	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param section_id
	 * @param rs_id
	 * @param actor1
	 * @param actor2
	 * @return
	 */
	public static Conversation getChatHelpConversation(String schema,
			Long sim_id, Long section_id, Long rs_id, Long actor1, Long actor2) {

		// Look for conversation named like, "Chat Help, S1, SEC99, RS2, A7, A9"

		String actorIdPiece = "";
		if (actor1.intValue() < actor2.intValue()) {
			actorIdPiece = ", A" + actor1 + ", A" + actor2;
		} else {
			actorIdPiece = ", A" + actor2 + ", A" + actor1;
		}

		String uniqName = "Chat Help, S" + sim_id + ", SEC" + section_id
				+ ", RS" + rs_id + actorIdPiece;

		Conversation conv = Conversation.getByUniqueIdentifier(schema,
				uniqName, TYPE_CHAT_HELP);

		if (conv == null) {
			conv = new Conversation();
			conv.setSim_id(sim_id);
			conv.setSimId(sim_id);
			conv.setConversationType(TYPE_CHAT_HELP);
			conv.setRs_id(rs_id);
			conv.setUniqueConvName(uniqName);
			conv.saveMe(schema);

			ConvActorAssignment caa1 = new ConvActorAssignment();
			caa1.setActor_id(actor1);
			caa1.setConv_id(conv.getId());
			caa1.setRunning_sim_id(rs_id);
			caa1.setSimId(sim_id);
			caa1.saveMe(schema);

			ConvActorAssignment caa2 = new ConvActorAssignment();
			caa2.setActor_id(actor2);
			caa2.setConv_id(conv.getId());
			caa2.setRunning_sim_id(rs_id);
			caa2.setSimId(sim_id);
			caa2.saveMe(schema);

		}

		return conv;

	}

	/**
	 * 
	 * @param schema
	 * @param uniqueConvName
	 * @param conv_type
	 * @return
	 */
	public static Conversation getByUniqueIdentifier(String schema,
			String uniqueConvName, int conv_type) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		String getSQL = "from Conversation where uniqueConvName = :uniqueConvName and conv_type = :conv_type"; //$NON-NLS-1$

		List<Conversation> returnList = MultiSchemaHibernateUtil
				.getSession(schema).createQuery(getSQL)
				.setString("uniqueConvName", uniqueConvName)
				.setInteger("conv_type", conv_type).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if ((returnList == null) || (returnList.size() == 0)) {
			return null;
		} else if (returnList.size() == 1) {
			return returnList.get(0);
		} else {
			Logger.getRootLogger().warn(
					"multiple conversations with same unique id found");
			return returnList.get(0);
		}
	}

	/**
	 * Returns a list of all conversations associated with a particular
	 * simulation.
	 * 
	 */
	public static List getAllPrivateChatForASection(String schema,
			Long section_id) {

		// Get set of Base objects for this section
		List conversationsBSSDOAs = BaseSimSectionDepObjectAssignment
				.getObjectsForSection(schema, section_id);

		List returnList = new ArrayList<Conversation>();

		for (ListIterator<BaseSimSectionDepObjectAssignment> li = conversationsBSSDOAs
				.listIterator(); li.hasNext();) {

			BaseSimSectionDepObjectAssignment bssdoa = li.next();
			Conversation conv = new Conversation();
			conv.setId(bssdoa.getObjectId());

			returnList.add(conv);
		}

		return returnList;
	}

	/**
	 * Delete all private conversations for this simulation
	 * 
	 * @param schema
	 * @param sim_id
	 */
	public static void deleteAllPrivateChatForSim(String schema, Long sim_id) {

		List currentPrivChats = Conversation.getAllPrivateChatForSim(schema,
				sim_id);

		for (ListIterator<Conversation> li = currentPrivChats.listIterator(); li
				.hasNext();) {

			Conversation conv = li.next();

			MultiSchemaHibernateUtil.beginTransaction(schema);
			MultiSchemaHibernateUtil.getSession(schema).delete(conv);
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		}
	}

	/**
	 * Checks a conversation for the occurance of a particular actor.
	 * 
	 * @param actor_id
	 * @return
	 */
	public boolean hasActor(String schema, Long actor_id) {

		for (ListIterator<ConvActorAssignment> li = this.getConv_actor_assigns(
				schema).listIterator(); li.hasNext();) {
			ConvActorAssignment caa = li.next();

			if (actor_id.equals(caa.getActor_id())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param a_id
	 * @param schema
	 * @param sim_id
	 */
	public void addActor(String a_id, String schema, Long sim_id, String role) {
		if (a_id != null) {
			addActor(new Long(a_id), schema, sim_id, role);
		}
	}

	/**
	 * 
	 * @param a_id
	 * @param schema
	 * @param sim_id
	 */
	public void addActor(Long a_id, String schema, Long sim_id, String role) {

		if (!(hasActor(schema, a_id))) {
			ConvActorAssignment caa = new ConvActorAssignment();
			caa.setSimId(sim_id);
			caa.setActor_id(a_id);
			caa.setRole(role);
			caa.setConv_id(this.id);
			caa.saveMe(schema);
			this.getConv_actor_assigns(schema).add(caa);
			this.save(schema, sim_id);
		}
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

	public Long getBase_conv_id() {
		return this.base_conv_id;
	}

	public void setBase_conv_id(Long base_conv_id) {
		this.base_conv_id = base_conv_id;
	}

	public Long getRs_id() {
		return this.rs_id;
	}

	public void setRs_id(Long rs_id) {
		this.rs_id = rs_id;
	}

	public List<ConvActorAssignment> getConv_actor_assigns(String schema) {
		if ((this.conv_actor_assigns == null)
				|| (this.conv_actor_assigns.size() == 0)) {
			this.conv_actor_assigns = ConvActorAssignment
					.getAllForConversation(schema, this.getId());
		}
		return this.conv_actor_assigns;
	}

	public void setConv_actor_assigns(
			List<ConvActorAssignment> conv_actor_assigns) {
		this.conv_actor_assigns = conv_actor_assigns;
	}

	public int getConversationType() {
		return this.conversationType;
	}

	public void setConversationType(int conversation_type) {
		this.conversationType = conversation_type;
	}

	public String getConversationNotes() {
		return conversationNotes;
	}

	public void setConversationNotes(String conversationNotes) {
		this.conversationNotes = conversationNotes;
	}

	@Override
	public Long createRunningSimVersion(String schema, Long sim_id, Long rs_id,
			Object templateObject) {

		Conversation templateConv = (Conversation) templateObject;

		// Pull it out clean from the database
		templateConv = Conversation.getById(schema, templateConv.getId());

		// Create the new conversation.
		Conversation new_conv = new Conversation();
		new_conv.setUniqueConvName(templateConv.getUniqueConvName());
		new_conv.setConversationNotes(templateConv.getConversationNotes());
		new_conv.setConversationType(templateConv.getConversationType());
		new_conv.setBase_conv_id(templateConv.getBase_conv_id());

		new_conv.saveMe(schema);

		List<ConvActorAssignment> modifiedAssignments = new ArrayList<ConvActorAssignment>();
		// Loop over the assignments gotten, and change the conversation id
		for (ListIterator<ConvActorAssignment> li = ConvActorAssignment
				.getAllForConversation(schema, templateConv.getId())
				.listIterator(); li.hasNext();) {
			ConvActorAssignment conv_ass = li.next();

			ConvActorAssignment new_conv_ass = new ConvActorAssignment();

			new_conv_ass.setSimId(sim_id);
			new_conv_ass.setRunning_sim_id(rs_id);
			new_conv_ass.setConv_id(new_conv.getId());
			new_conv_ass.setActor_id(conv_ass.getActor_id());
			new_conv_ass.setCan_be_added_removed(conv_ass
					.isCan_be_added_removed());
			new_conv_ass.setInitially_present(conv_ass.isInitially_present());
			new_conv_ass.setRole(conv_ass.getRole());
			new_conv_ass.setRoom_owner(conv_ass.isRoom_owner());

			new_conv_ass.saveMe(schema);

			modifiedAssignments.add(new_conv_ass);
		}
		new_conv.setConv_actor_assigns(modifiedAssignments);

		new_conv.setConversationType(templateConv.getConversationType());

		new_conv.setRs_id(rs_id);
		new_conv.setSim_id(sim_id);

		new_conv.saveMe(schema);

		return new_conv.getId();
	}

	/**
	 * 
	 * @param schema
	 * @param convId
	 * @return
	 */
	public static Conversation getById(String schema, Long convId) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Conversation conv = (Conversation) MultiSchemaHibernateUtil.getSession(
				schema).get(Conversation.class, convId);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return conv;
	}

	/**
	 * Saves the object to the database.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	@Override
	public void setSimId(Long theId) {
		setSim_id(theId);

	}

	public String getUniqueConvName() {
		return uniqueConvName;
	}

	public void setUniqueConvName(String uniqueConvName) {
		this.uniqueConvName = uniqueConvName;
	}

	/**
	 * Responds to items selected on the create conversation page.
	 * 
	 * @param request
	 * @return
	 */
	public static Conversation handleCreateConversation(HttpServletRequest request,
			SessionObjectBase sob) {
	
		Conversation conv = new Conversation();
		conv.setConversationType(TYPE_CAUCUS);
	
		// If the player cleared the form, return the blank document.
		String clear_button = (String) request.getParameter("clear_button");
		if (clear_button != null) {
			return conv;
		}
	
		// If we got passed in a doc id, use it to retrieve the doc we are
		// working on.
		String conv_id = (String) request.getParameter("conv_id");
	
		String queueup = (String) request.getParameter("queueup");
		if ((queueup != null) && (queueup.equalsIgnoreCase("true"))
				&& (conv_id != null) && (conv_id.trim().length() > 0)) {
			conv = getById(sob.schema, new Long(conv_id));
			return conv;
		}
	
		// If player just entered this page from a different form, just return
		// the blank document
		String sending_page = (String) request.getParameter("sending_page");
		if ((sending_page == null)
				|| (!(sending_page
						.equalsIgnoreCase("make_create_conversation_page")))) {
			return conv;
		}
	
		// If we got down to here, we must be doing some real work on a
		// document.
		String uniq_conv_name = (String) request.getParameter("uniq_conv_name");
		String conv_notes = (String) request.getParameter("conv_notes");
		String conv_type = (String) request.getParameter("conv_type");
		
		int conv_type_int = Conversation.TYPE_CAUCUS;
		
		try {
			conv_type_int = new Long(conv_type).intValue();
		} catch (Exception e){
			e.printStackTrace();
			conv_type_int = Conversation.TYPE_CAUCUS;
		}
	
		// Do create if called.
		String create_conv = (String) request.getParameter("create_conv");
		if ((create_conv != null)) {
			Logger.getRootLogger().debug(
					"creating conv of uniq name: " + uniq_conv_name);
			conv = new Conversation(uniq_conv_name, conv_notes, sob.sim_id);
			conv.setConversationType(conv_type_int);
			conv.saveMe(sob.schema);
		}
	
		// Do update if called.
		String update_conv = (String) request.getParameter("update_conv");
		if ((update_conv != null)) {
			Logger.getRootLogger().debug(
					"updating conv of uniq title: " + uniq_conv_name);
			conv = getById(sob.schema, new Long(conv_id));
			conv.setUniqueConvName(uniq_conv_name);
			conv.setConversationNotes(conv_notes);
			conv.setSim_id(sob.sim_id);
			conv.setConversationType(conv_type_int);
			conv.saveMe(sob.schema);
	
		}
	
		// Need to clean out current actor assignments for this conversation
		ConvActorAssignment.removeAllForConversation(sob.schema, conv.getId());
	
		Hashtable setOfUserRoles = new Hashtable();
	
		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			String param_name = (String) e.nextElement();
	
			if (param_name.startsWith("role_")) {
				String this_a_id = param_name.replaceFirst("role_", "");
	
				setOfUserRoles.put(this_a_id,
						(String) request.getParameter(param_name));
	
			}
		}
	
		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			String param_name = (String) e.nextElement();
	
			if (param_name.startsWith("actor_cb_")) {
				if ((request.getParameter(param_name) != null)
						&& (request.getParameter(param_name)
								.equalsIgnoreCase("true"))) {
					String this_a_id = param_name.replaceFirst("actor_cb_", "");
					Logger.getRootLogger().debug(
							"adding " + this_a_id + " in schema" + sob.schema
									+ " to sim_id " + sob.sim_id);
					conv.addActor(this_a_id, sob.schema, sob.sim_id,
							(String) setOfUserRoles.get(this_a_id));
				}
			}
	
		}
		return conv;
	}

	public static Conversation getStudentChatForSim(String schema, Long simId,
			Long rsId) {

		Conversation conv = new Conversation();

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Conversation> returnList = null;

		if (rsId == null) {
			String getSQL = "from Conversation where sim_id = :simid and rs_id is null and conv_type = "
					+ TYPE_STUDENT_CHAT; //$NON-NLS-1$

			returnList = MultiSchemaHibernateUtil.getSession(schema)
					.createQuery(getSQL)
					.setLong("simId", simId)
					.list();
		} else {
			String getSQL = "from Conversation where sim_id = :simid and rs_id = :rsId and conv_type = "
					+ TYPE_STUDENT_CHAT; //$NON-NLS-1$

			returnList = MultiSchemaHibernateUtil.getSession(schema)
					.createQuery(getSQL)
					.setLong("simId", simId)
					.setLong("rsId", rsId)
					.list();
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			conv = new Conversation("student_conv",
					"Student Converation for Simulation", simId);
			conv.setConversationType(TYPE_STUDENT_CHAT);
			conv.setRs_id(rsId);
			conv.saveMe(schema);
		} else {
			conv = (Conversation) returnList.get(0);
		}

		return conv;
	}
	
	@Override
	public boolean runningSimulationSetLinkedObject() {
		// TODO Auto-generated method stub
		return false;
	}

}
