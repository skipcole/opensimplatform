package org.usip.osp.communications;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.SimSectionDependentObject;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.apache.log4j.*;

/**
 * This class represents the assignment of an actor to a particular conversation. 
 */
 /*
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
@Table(name = "CONV_ACTOR_ASSIGNMENT")
@Proxy(lazy = false)
public class ConvActorAssignment implements SimSectionDependentObject {

	public ConvActorAssignment() {
		
	}
	
	@Id 
	@GeneratedValue
	@Column(name = "ID")
    private Long id;
	
	@Column(name = "SIM_ID")
	private Long sim_id;
	
	public Long getSimId() {
		return sim_id;
	}

	public void setSimId(Long simId) {
		sim_id = simId;
	}
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransitId() {
		return this.transit_id;
	}

	public void setTransitId(Long transit_id) {
		this.transit_id = transit_id;
	}
	
	private Long running_sim_id;

	public Long getRunning_sim_id() {
		return this.running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	@Column(name = "ACTOR_ID")
	private Long actor_id;
	
	@Column(name = "CONV_ID")
	private Long conv_id;
	
	private String role = ""; //$NON-NLS-1$
	
	private boolean room_owner = false;
	
	private boolean initially_present = false;
	
	private boolean can_be_added_removed = false;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getActor_id() {
		return this.actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public Long getConv_id() {
		return this.conv_id;
	}

	public void setConv_id(Long conv_id) {
		this.conv_id = conv_id;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean isRoom_owner() {
		return this.room_owner;
	}

	public void setRoom_owner(boolean room_owner) {
		this.room_owner = room_owner;
	}

	public boolean isInitially_present() {
		return this.initially_present;
	}

	public void setInitially_present(boolean initially_present) {
		this.initially_present = initially_present;
	}

	public boolean isCan_be_added_removed() {
		return this.can_be_added_removed;
	}

	public void setCan_be_added_removed(boolean can_be_added_removed) {
		this.can_be_added_removed = can_be_added_removed;
	}

	/**
	 * Saves this object to the database.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	
	
	public static void removeCAA(String schema, ConvActorAssignment caa){
        MultiSchemaHibernateUtil.beginTransaction(schema);
        MultiSchemaHibernateUtil.getSession(schema).delete(caa);      
        MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	
	/**
	 * Removes all of the Conversation Actor Assignments associated with a particular conversation.
	 * @param schema
	 * @param conv_id
	 */
	public static void removeAllForConversation(String schema, Long conv_id){
		
		List removeList = getAllForConversation(schema, conv_id);
		
		for (ListIterator<ConvActorAssignment> bi = removeList.listIterator(); bi.hasNext();) {
			ConvActorAssignment caa = bi.next();
			
			removeCAA(schema, caa);
			
		}
	}
	
	/** Returns a list of all conversations associated with a particular simulation. */
	public static List getAllBaseForSim(String schema, Long sim_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List<ConvActorAssignment> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from ConvActorAssignment where sim_id = :sim_id and running_sim_id is null")
				.setLong("sim_id", sim_id)
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
	/** Returns a list of all conversations associated with a particular simulation. */
	public static List getAllForConversation(String schema, Long conv_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List<ConvActorAssignment> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from ConvActorAssignment where conv_id = " + conv_id).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
	/** Returns a list of all conversations associated with a particular simulation. */
	public static ConvActorAssignment getSpecificCAA(String schema, Long actor_id, Long conv_id){
		
		ConvActorAssignment returnCAA = null;
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List<ConvActorAssignment> getList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from ConvActorAssignment where conv_id = " + conv_id + " and actor_id = " + actor_id).list(); //$NON-NLS-1$ //$NON-NLS-2$

		if ((getList != null) && (getList.size() > 0)){
			returnCAA = getList.get(0);
			Logger.getRootLogger().debug("returnCAA role was: " + returnCAA.getRole()); //$NON-NLS-1$
		}
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnCAA;
	}

	@Override
	public Long createRunningSimVersion(String schema, Long simId, Long rsId,
			Object templateObject) {

		// Do Nothing here
		// When a new conversation is created for a running sim, that conversation creates all
		// of its Conversation Actor Assignments (ConvActorAssignment) so they all point to the 
		// correct conversation id.
		return null;
	}
	
	@Override
	public boolean runningSimulationSetLinkedObject() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
