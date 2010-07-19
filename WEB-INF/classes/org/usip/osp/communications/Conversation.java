package org.usip.osp.communications;

import java.util.*;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment;
import org.usip.osp.baseobjects.SimSectionDependentObject;
import org.usip.osp.baseobjects.SimSectionRSDepOjbectAssignment;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.networking.AuthorFacilitatorSessionObject;
import org.usip.osp.networking.USIP_OSP_Cache;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents a particular conversation amongst a group of actors.
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

	/** This is a caucus conversation. */
	public static final int TYPE_CHAT_HELP = 3;
	
	/** This is a caucus conversation. */
	public static final int TYPE_CAUCUS = 4;

	/** This is a chat room that the player can invite people to leave or enter. */
	public static final int TYPE_BASE_USER_CONTROLLED_CAUCUS = 5;

	/** This is a chat room that the player can invite people to leave or enter. */
	public static final int TYPE_RS_USER_CONTROLLED_CAUCUS = 6;
	
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

		Simulation sim = Simulation.getById(schema, sim_id);
		sim.addConversation(schema, this);
		sim.saveMe(schema);

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

	public Long getTransit_id() {
		return this.transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}
	
	/**
	 * Returns a list of all conversations associated with a particular
	 * simulation.
	 */
	public static List getAllForSim(String schema, Long simid) {

		if (simid == null){
			return new ArrayList();
		}
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Conversation> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Conversation where sim_id = :simid and rs_id is null")
				.setLong("simid", simid)
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	/**
	 * Returns a list of all conversations associated with a particular
	 * simulation.
	 */
	public static List getAllForRunningSim(String schema, Long simid, Long rs_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Conversation> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Conversation where sim_id = " + simid + " and rs_id = " + rs_id).list(); //$NON-NLS-1$ //$NON-NLS-2$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	/**
	 * 
	 * @param schema
	 * @return
	 */
	public String getListOfActors(String schema, AuthorFacilitatorSessionObject afso, HttpServletRequest request){
		
		String returnString = ""; //$NON-NLS-1$
		
		List cca_list = getConv_actor_assigns(schema);
		
		for (ListIterator<ConvActorAssignment> bi = cca_list.listIterator(); bi.hasNext();) {
			ConvActorAssignment caa = bi.next();
			
			String a_name = USIP_OSP_Cache.getActorName(afso.schema, afso.sim_id, afso.getRunningSimId(), request, caa.getActor_id());

			returnString += a_name + "-"; //$NON-NLS-1$
		}
		
		
		returnString = returnString.substring(0, returnString.length() -1 );
		
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
	public static List getActorsConversationsForSimSection(String schema, Long aid, Long rs_id, Long section_id) {

		List returnList = new ArrayList();

		List baseList = SimSectionRSDepOjbectAssignment.getAllForRunningSimSection(schema, rs_id, section_id);

		for (ListIterator<SimSectionRSDepOjbectAssignment> li = baseList.listIterator(); li.hasNext();) {
			SimSectionRSDepOjbectAssignment ssrsdoa = li.next();

			List actorsAssignedToThisConversation = ConvActorAssignment.getAllForConversation(schema, ssrsdoa
					.getObjectId());

			for (ListIterator<ConvActorAssignment> bi = actorsAssignedToThisConversation.listIterator(); bi.hasNext();) {
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
	public static List getAllConversationsForSimSection(String schema, Long aid, Long rs_id, Long section_id) {

		List returnList = new ArrayList();

		List baseList = SimSectionRSDepOjbectAssignment.getAllForRunningSimSection(schema, rs_id, section_id);

		for (ListIterator<SimSectionRSDepOjbectAssignment> li = baseList.listIterator(); li.hasNext();) {
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

		for (ListIterator<Conversation> li = baseList.listIterator(); li.hasNext();) {
			Conversation conv_id = li.next();

			MultiSchemaHibernateUtil.beginTransaction(schema);

			Conversation conv = (Conversation) MultiSchemaHibernateUtil.getSession(schema).get(Conversation.class,
					conv_id.getId());

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

		List<Conversation> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getSQL).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	/**
	 * Returns a list of all conversations associated with a particular
	 * simulation.
	 */
	public static List getAllPrivateChatForASection(String schema, Long section_id) {

		// Get set of Base objects for this section
		List conversationsBSSDOAs = BaseSimSectionDepObjectAssignment.getObjectsForSection(schema, section_id);

		List returnList = new ArrayList<Conversation>();

		for (ListIterator<BaseSimSectionDepObjectAssignment> li = conversationsBSSDOAs.listIterator(); li.hasNext();) {

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

		List currentPrivChats = Conversation.getAllPrivateChatForSim(schema, sim_id);

		for (ListIterator<Conversation> li = currentPrivChats.listIterator(); li.hasNext();) {

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

		for (ListIterator<ConvActorAssignment> li = this.getConv_actor_assigns(schema).listIterator(); li.hasNext();) {
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
		if ((this.conv_actor_assigns == null) || (this.conv_actor_assigns.size() == 0)){
			this.conv_actor_assigns = ConvActorAssignment.getAllForConversation(schema, this.getId());
		}
		return this.conv_actor_assigns;
	}

	public void setConv_actor_assigns(List<ConvActorAssignment> conv_actor_assigns) {
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
	public Long createRunningSimVersion(String schema, Long sim_id, Long rs_id, Object templateObject) {

		Conversation templateConv = (Conversation) templateObject;

		// Pull it out clean from the database
		templateConv = Conversation.getById(schema, templateConv.getId());

		// Create the new conversation.
		Conversation new_conv = new Conversation();
		new_conv.setUniqueConvName(templateConv.getUniqueConvName());
		new_conv.setConversationNotes(templateConv.getConversationNotes());
		new_conv.setConversationType(templateConv.getConversationType());
		
		new_conv.saveMe(schema);

		List<ConvActorAssignment> modifiedAssignments = new ArrayList<ConvActorAssignment>();
		// Loop over the assignments gotten, and change the conversation id
		for (ListIterator<ConvActorAssignment> li = ConvActorAssignment.getAllForConversation(schema,
				templateConv.getId()).listIterator(); li.hasNext();) {
			ConvActorAssignment conv_ass = li.next();

			ConvActorAssignment new_conv_ass = new ConvActorAssignment();

			new_conv_ass.setConv_id(new_conv.getId());
			new_conv_ass.setActor_id(conv_ass.getActor_id());
			new_conv_ass.setCan_be_added_removed(conv_ass.isCan_be_added_removed());
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
	 * @param id2
	 * @return
	 */
	public static Conversation getById(String schema, Long id2) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Conversation conv = (Conversation) MultiSchemaHibernateUtil.getSession(schema).get(Conversation.class, id2);

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

}
