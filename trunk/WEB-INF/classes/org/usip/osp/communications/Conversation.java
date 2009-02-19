package org.usip.osp.communications;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.baseobjects.SimSectionDependentObject;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.UserAssignment;
import org.usip.osp.persistence.SchemaInformationObject;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.specialfeatures.IntVariable;

/**
 * This class represents a particular conversation amongst a group of actors.
 * 
 * @author Ronald "Skip" Cole<br />
 *
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "CONVERSATIONS")
@Proxy(lazy = false)
public class Conversation  implements SimSectionDependentObject {
	
	public Conversation (){
		
	}
	
	/** This conversation is of an undefined type.*/
	public static final int TYPE_UNDEFINED = 0;
	
	/** This is a broadcast conversation.*/
	public static final int TYPE_BROADCAST = 1;
	
	/** This is a private conversation. */
	public static final int TYPE_PRIVATE = 2;
	
	/** This is a caucus conversation. */
	public static final int TYPE_CAUCUS = 3;
	
	/** This is a chat room that the player can invite people to leave or enter. */
	public static final int TYPE_BASE_USER_CONTROLLED_CAUCUS = 4;
	
	/** This is a chat room that the player can invite people to leave or enter. */
	public static final int TYPE_RS_USER_CONTROLLED_CAUCUS = 5;

	
	/** Saves the conversation and makes sure it is affiliated with the simulation at hand. */
    public void save(String schema, Long sim_id){
        MultiSchemaHibernateUtil.beginTransaction(schema);
        MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
        MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
        
        Simulation sim = Simulation.getMe(schema, sim_id);
        sim.addConversation(schema, this);
        sim.saveMe(schema);
        
    }
    
	
	@Id 
	@GeneratedValue
	@Column(name = "CONV_ID")
    private Long id;
    
    @Column(name = "SIM_ID")
    private Long sim_id;
    
    /** If this is a user controlled conversation, this will contain the id of the 
     * base conversation from which information is originally pulled. */
    @Column(name = "BASE_CONV_ID")
    private Long base_conv_id;
    
    @Column(name = "RS_ID")
    private Long rs_id;
    
    @Column(name = "CONV_NAME")
    private String conversation_name;
    
    @Column(name = "CONV_TYPE")
    private int conversation_type = TYPE_UNDEFINED;

    @Transient
	private List <ConvActorAssignment>  conv_actor_assigns = new ArrayList <ConvActorAssignment>();
	
	/** Returns a list of all conversations associated with a particular simulation. */
	public static List getAllForSim(String schema, Long simid){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List<Conversation> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Conversation where sim_id = " + simid).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
	/**
	 * 
	 * @param schema
	 * @param simid
	 * @param aid
	 * @return
	 */
	public static List getActorsPrivateChats(String schema, Long simid, Long aid){
		
		List baseList = getAllPrivateChatForSim(schema, simid);
		ArrayList returnList = new ArrayList();
		
		for (ListIterator<Conversation> li = baseList.listIterator(); li.hasNext();) {
			Conversation conv_id = (Conversation) li.next();
			
			MultiSchemaHibernateUtil.beginTransaction(schema);
			
			Conversation conv = (Conversation) 
				MultiSchemaHibernateUtil.getSession(schema).get(Conversation.class, conv_id.getId());
			
			if (conv.hasActor(schema, aid)){
				returnList.add(conv);
			}
			
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
			
		}
		
		return returnList;
		
	}
	
	/** Returns a list of all conversations associated with a particular simulation. */
	public static List getAllPrivateChatForSim(String schema, Long simid){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		String getSQL = "from Conversation where sim_id = " + simid + " and conv_type = " +
			TYPE_PRIVATE;
		
		List<Conversation> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				getSQL).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	/**
	 * Delete all private conversations for this simulation
	 * @param schema
	 * @param sim_id
	 */
	public static void deleteAllPrivateChatForSim(String schema, Long sim_id){

		List currentPrivChats = Conversation.getAllPrivateChatForSim(schema, sim_id);
		
		for (ListIterator<Conversation> li = currentPrivChats.listIterator(); li
		.hasNext();) {
			
			Conversation conv = (Conversation) li.next();
			
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
	public boolean hasActor(String schema, Long actor_id){
		
		for (ListIterator<ConvActorAssignment> li = this.getConv_actor_assigns(schema).listIterator(); li.hasNext();) {
			ConvActorAssignment caa = (ConvActorAssignment) li.next();
			
			if (actor_id.equals(caa.getActor_id())){
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
	public void addActor(String a_id, String schema, Long sim_id){
		if (a_id != null){
			addActor(new Long(a_id), schema, sim_id);
		}
	}
	
	/**
	 * 
	 * @param a_id
	 * @param schema
	 * @param sim_id
	 */
	public void addActor(Long a_id, String schema, Long sim_id){
		
		if (!(hasActor(schema, a_id))){
			ConvActorAssignment caa = new ConvActorAssignment();
			caa.setActor_id(a_id);
			caa.setConv_id(this.id);
			caa.save(schema);
			this.getConv_actor_assigns(schema).add(caa);
			this.save(schema, sim_id);
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

    public Long getBase_conv_id() {
		return base_conv_id;
	}

	public void setBase_conv_id(Long base_conv_id) {
		this.base_conv_id = base_conv_id;
	}

	public Long getRs_id() {
		return rs_id;
	}

	public void setRs_id(Long rs_id) {
		this.rs_id = rs_id;
	}

	public String getConversation_name() {
        return conversation_name;
    }

    public void setConversation_name(String conversation_name) {
        this.conversation_name = conversation_name;
    }


	public List<ConvActorAssignment> getConv_actor_assigns(String schema) {
		if (conv_actor_assigns == null){
			conv_actor_assigns = ConvActorAssignment.getAllForConversation(schema, this.getId());
		}
		return conv_actor_assigns;
	}

	public void setConv_actor_assigns(List<ConvActorAssignment> conv_actor_assigns) {
		this.conv_actor_assigns = conv_actor_assigns;
	}

	public int getConversation_type() {
		return conversation_type;
	}

	public void setConversation_type(int conversation_type) {
		this.conversation_type = conversation_type;
	}

	@Override
	public Long createRunningSimVersion(String schema, Long sim_id, Long rs_id, Object templateObject) {
		
		Conversation templateConv = (Conversation) templateObject;
		
		// Pull it out clean from the database
		templateConv = Conversation.getMe(schema, templateConv.getId());
		
		// Create the new conversation.
		Conversation new_conv = new Conversation();
		new_conv.setConversation_name(templateConv.getConversation_name());
		new_conv.save(schema);
		
		List<ConvActorAssignment> modifiedAssignments = new ArrayList<ConvActorAssignment>();
		// Loop over the assignments gotten, and change the conversation id
		for (ListIterator<ConvActorAssignment> li = 
			ConvActorAssignment.getAllForConversation(schema, templateConv.getId()).listIterator(); li.hasNext();) {
			ConvActorAssignment conv_ass = (ConvActorAssignment) li.next();
			
			ConvActorAssignment new_conv_ass = new ConvActorAssignment();
			
			new_conv_ass.setConv_id(new_conv.getId());
			new_conv_ass.setActor_id(conv_ass.getActor_id());
			new_conv_ass.setCan_be_added_removed(conv_ass.isCan_be_added_removed());
			new_conv_ass.setInitially_present(conv_ass.isInitially_present());
			new_conv_ass.setRole(conv_ass.getRole());
			new_conv_ass.setRoom_owner(conv_ass.isRoom_owner());
			
			new_conv_ass.save(schema);
			
			modifiedAssignments.add(new_conv_ass);
		}
		new_conv.setConv_actor_assigns(modifiedAssignments);
		
		new_conv.setConversation_type(templateConv.getConversation_type());

		new_conv.setRs_id(rs_id);
		new_conv.setSim_id(sim_id);

		new_conv.save(schema);

		return new_conv.getId();
	}

	/**
	 * 
	 * @param schema
	 * @param id2
	 * @return
	 */
	private static Conversation getMe(String schema, Long id2) {
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		Conversation conv = (Conversation) MultiSchemaHibernateUtil
				.getSession(schema).get(Conversation.class, id2);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return conv;
	}

	@Override
	public String getObjectClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Saves the object to the database.
	 * 
	 * @param schema
	 */
	public void save(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	
}
