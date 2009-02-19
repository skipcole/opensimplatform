package org.usip.osp.communications;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;


/**
 * This class represents the assignment of an actor to a particular conversation. 
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
@Table(name = "CONV_ACTOR_ASSIGNMENT")
@Proxy(lazy = false)
public class ConvActorAssignment {

	@Id 
	@GeneratedValue
	@Column(name = "ID")
    private Long id;
	
	@Column(name = "ACTOR_ID")
	private Long actor_id;
	
	@Column(name = "CONV_ID")
	private Long conv_id;
	
	private String role = "";
	
	private boolean room_owner = false;
	
	private boolean initially_present = false;
	
	private boolean can_be_added_removed = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getActor_id() {
		return actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public Long getConv_id() {
		return conv_id;
	}

	public void setConv_id(Long conv_id) {
		this.conv_id = conv_id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean isRoom_owner() {
		return room_owner;
	}

	public void setRoom_owner(boolean room_owner) {
		this.room_owner = room_owner;
	}

	public boolean isInitially_present() {
		return initially_present;
	}

	public void setInitially_present(boolean initially_present) {
		this.initially_present = initially_present;
	}

	public boolean isCan_be_added_removed() {
		return can_be_added_removed;
	}

	public void setCan_be_added_removed(boolean can_be_added_removed) {
		this.can_be_added_removed = can_be_added_removed;
	}

	public void save(String schema){
        MultiSchemaHibernateUtil.beginTransaction(schema);
        MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);        
        MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	
	/** Returns a list of all conversations associated with a particular simulation. */
	public static List getAllForConversation(String schema, Long conv_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List<ConvActorAssignment> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from ConvActorAssignment where conv_id = " + conv_id).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
}
