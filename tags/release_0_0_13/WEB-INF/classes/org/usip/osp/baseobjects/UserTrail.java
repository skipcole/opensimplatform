package org.usip.osp.baseobjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents an audit item that keeps track of when a player enters and exits the simulated world.
 */
/*
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
@Table(name = "USER_TRAIL")
@Proxy(lazy = false)
public class UserTrail {

	/** Database id of this UserTrail. */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "USER_ID")
	private Long user_id;
	
	@Column(name = "RUNNING_SIM_ID")
	private Long running_sim_id;
	
	@Column(name = "ACTOR_ID")
	private Long actor_id;
	
	@Column(name="LOGGED_IN_DATE", columnDefinition="datetime") 	
	@GeneratedValue
	private Date loggedInDate;
	
	private Date endSessionDate;
	
	private Date loggedOut;
	
	private boolean actuallyLoggedOut;

	public Date getLoggedOut() {
		return loggedOut;
	}

	public void setLoggedOut(Date loggedOut) {
		this.loggedOut = loggedOut;
	}

	public boolean isActuallyLoggedOut() {
		return actuallyLoggedOut;
	}

	public void setActuallyLoggedOut(boolean actuallyLoggedOut) {
		this.actuallyLoggedOut = actuallyLoggedOut;
	}
	
	public UserTrail() {
		
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUser_id() {
		return this.user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Date getLoggedInDate() {
		return this.loggedInDate;
	}

	public void setLoggedInDate(Date loggedInDate) {
		this.loggedInDate = loggedInDate;
	}

	public Long getRunning_sim_id() {
		return this.running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public Long getActor_id() {
		return this.actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public Date getEndSessionDate() {
		return this.endSessionDate;
	}

	public void setEndSessionDate(Date endSessionDate) {
		this.endSessionDate = endSessionDate;
	}
	
	/**
	 * Pulls the user trail out of the database base on its id and schema.
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static UserTrail getMe(String schema, Long ut_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		UserTrail ut = (UserTrail) MultiSchemaHibernateUtil
				.getSession(schema).get(UserTrail.class, ut_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return ut;

	}
	
	/** Saves a user trail. */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
    /**
     * Returns all of the actors found in a schema for a particular simulation
     * 
     * @param schema
     * @return
     */
    public static List getAllForUser(String schema, Long user_id){
    	
    	if (user_id == null){
    		return new ArrayList();
    	}
        
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from UserTrail where user_id = :user_id order by loggedInDate")
				.setLong("user_id", user_id).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
    }
	
}
