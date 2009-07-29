package org.usip.osp.baseobjects;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents an audit item that keeps track of when a player enters and exits the simulated world.
 *
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

	/** Database id of this Simulation. */
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
	
	public UserTrail() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Date getLoggedInDate() {
		return loggedInDate;
	}

	public void setLoggedInDate(Date loggedInDate) {
		this.loggedInDate = loggedInDate;
	}

	public Long getRunning_sim_id() {
		return running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public Long getActor_id() {
		return actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public Date getEndSessionDate() {
		return endSessionDate;
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
	
	/** Saves a simulation. */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
}
