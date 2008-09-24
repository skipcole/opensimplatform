package org.usip.oscw.communications;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.oscw.baseobjects.Simulation;
import org.usip.oscw.persistence.MultiSchemaHibernateUtil;


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
	
	
	public void save(String schema){
        MultiSchemaHibernateUtil.beginTransaction(schema);
        MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);        
        MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	
}
