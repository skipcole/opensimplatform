package org.usip.oscw.communications;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.oscw.baseobjects.RunningSimulation;
import org.usip.oscw.baseobjects.Simulation;
import org.usip.oscw.baseobjects.UserAssignment;
import org.usip.oscw.persistence.SchemaInformationObject;
import org.usip.oscw.persistence.MultiSchemaHibernateUtil;
import org.usip.oscw.specialfeatures.IntVariable;

/**
 * @author Ronald "Skip" Cole
 *
 * This file is part of the USIP Online Simulation Platform.<br>
 * 
 * The USIP Online Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Online Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "CONVERSATIONS")
@Proxy(lazy = false)
public class Conversation {
	
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
        
        Simulation sim = (Simulation) MultiSchemaHibernateUtil.getSession(schema).get(Simulation.class, sim_id);
        sim.addConversation(this);
        MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(sim);
        
        MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
    }
    
	/*
	 * 
	 * @param rsid
	 * @return
	 
	public Conversation createCopy(Long rsid, org.hibernate.Session hibernate_session) {
		Conversation conv = new Conversation();
		
		ArrayList al = new ArrayList();
		
		for (ListIterator<Long> li = this.getActor_ids().listIterator(); li.hasNext();) {
			Long this_a = (Long) li.next();
			
			al.add(this_a);
		
		}
		conv.setActor_ids(al);
		
		conv.setConversation_name(this.getConversation_name());
		conv.setRunning_sim_id(this.getRunning_sim_id());
		
		System.out.println("saving conv");
		hibernate_session.saveOrUpdate(conv);
		
		return conv;
		
	}
	*/
	
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

	@OneToMany
	@JoinColumn(name = "CONV_ID")
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
			
			if (conv.hasActor(aid)){
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
	public boolean hasActor(Long actor_id){
		
		for (ListIterator<ConvActorAssignment> li = this.getConv_actor_assigns().listIterator(); li.hasNext();) {
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
		
		if (!(hasActor(a_id))){
			ConvActorAssignment caa = new ConvActorAssignment();
			caa.setActor_id(a_id);
			caa.setConv_id(this.id);
			caa.save(schema);
			this.getConv_actor_assigns().add(caa);
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


	public List<ConvActorAssignment> getConv_actor_assigns() {
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
	
}
