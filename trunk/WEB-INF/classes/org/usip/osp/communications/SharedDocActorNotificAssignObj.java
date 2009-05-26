package org.usip.osp.communications;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents a subscription that a player has to a particular document.
 * When the document changes, players subscribed will receive an alert.
 * 
 * 
 * @author Ronald "Skip" Cole<br />
 * 
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
@Table(name = "SDANAO")
@Proxy(lazy=false)
public class SharedDocActorNotificAssignObj {

	/** Database id of this Shared Document. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Simulation to which this has been assigned. */
	private Long sim_id;
	
	/** The document id for which this notification is set.*/
	private Long sd_id;
	
	/** If this notification is going to be set on an actor by actor basis, use this field
	 * to indicate the actor which will be triggering the notification to go out.*/
	private Long from_actor_id;
	
	private Long from_phase_id;
	
	
	private Long actor_id;
	
	private String notificationText = "";

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

	public Long getSd_id() {
		return sd_id;
	}

	public void setSd_id(Long sd_id) {
		this.sd_id = sd_id;
	}

	public Long getActor_id() {
		return actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public String getNotificationText() {
		return notificationText;
	}

	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}
	
	/**
	 * Gets the notifications for this document. 
	 * 
	 * @param schema
	 * @param doc_id
	 * @return
	 */
	public static List getAllAssignmentsForDocument(String schema, Long doc_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		String hql_string = "from SharedDocActorNotificAssignObj where sd_id = " + doc_id;
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hql_string).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;

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
	
	public SharedDocActorNotificAssignObj createCopy(){
		SharedDocActorNotificAssignObj sdanao = new SharedDocActorNotificAssignObj();
		sdanao.setActor_id(this.getActor_id());
		sdanao.setNotificationText(this.getNotificationText());
		sdanao.setSd_id(this.getSd_id());
		sdanao.setSim_id(this.getSim_id());
		
		
		return sdanao;
	}

}
