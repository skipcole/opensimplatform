package org.usip.osp.communications;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents a subscription that a player has to a particular document.
 * When the document changes, players subscribed will receive an alert.
 * 
 *
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
	
	public SharedDocActorNotificAssignObj(){
		
	}
	
	public SharedDocActorNotificAssignObj(String schema, Long sim_id, Long sd_id, Long actor_id, 
			Long from_actor_id, Long from_phase_id, String notificationText){
		
		this.sim_id = sim_id;
		this.sd_id = sd_id;
		this.actor_id = actor_id;
		this.from_actor_id = from_actor_id;
		this.from_phase_id = from_phase_id;
		this.notificationText = notificationText;
		
		this.saveMe(schema);
	}

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
	
	/** If this notification is for only one particular phase, indicate that phase here. */
	private Long from_phase_id;
	
	public Long getFrom_actor_id() {
		return from_actor_id;
	}

	public void setFrom_actor_id(Long from_actor_id) {
		this.from_actor_id = from_actor_id;
	}

	public Long getFrom_phase_id() {
		return from_phase_id;
	}

	public void setFrom_phase_id(Long from_phase_id) {
		this.from_phase_id = from_phase_id;
	}

	/** Id of the actor that will get this notification. */
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
	 * Gets the notifications for this document. 
	 * 
	 * @param schema
	 * @param doc_id
	 * @return
	 */
	public static SharedDocActorNotificAssignObj getAssignmentForDocumentAndActor(String schema, Long doc_id, Long actor_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		String hql_string = "from SharedDocActorNotificAssignObj where sd_id = " + doc_id + " and actor_id = " + actor_id;
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hql_string).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if ((returnList != null) && (returnList.size() > 0)){
			SharedDocActorNotificAssignObj sdanao = (SharedDocActorNotificAssignObj) returnList.get(0);
			return sdanao;
		} else {
			return null;
		}

	}
	
	/**
	 * Pulls the sdanao out of the database base on its id and schema.
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static SharedDocActorNotificAssignObj getMe(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		SharedDocActorNotificAssignObj sdanao = (SharedDocActorNotificAssignObj) MultiSchemaHibernateUtil
				.getSession(schema).get(SharedDocActorNotificAssignObj.class, sim_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return sdanao;

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
	
	/**
	 * 
	 * @param schema
	 * @param remove_id
	 */
	public static void removeSdanao(String schema, String remove_id){
		
		if (remove_id == null){
			Logger.getRootLogger().warn("Warning! Invalid id sent into SDANDAO to remove.");
		}
		
		Long sdanao_id = null;
		
		try {
			sdanao_id = new Long(remove_id);
		} catch (Exception e){
			Logger.getRootLogger().warn("Warning! problem converting id :" + remove_id + " passed in to Long.");
		}
		
		SharedDocActorNotificAssignObj sdanao = SharedDocActorNotificAssignObj.getMe(schema, sdanao_id);
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).delete(sdanao);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public SharedDocActorNotificAssignObj createCopy(){
		SharedDocActorNotificAssignObj sdanao = new SharedDocActorNotificAssignObj();
		sdanao.setActor_id(this.getActor_id());
		//sdanao.set//
		sdanao.setNotificationText(this.getNotificationText());
		sdanao.setSd_id(this.getSd_id());
		sdanao.setSim_id(this.getSim_id());
		
		
		return sdanao;
	}

}
