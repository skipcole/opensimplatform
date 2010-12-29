package org.usip.osp.communications;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.SimSectionDependentObject;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents a subscription that a player has to a particular document.
 * When the document changes, players subscribed will receive an alert.
 * 
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
@Table(name = "SDANAO")
@Proxy(lazy=false)
public class SharedDocActorNotificAssignObj implements SimSectionDependentObject{
	
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
	
	/** Running Sim to which this has been assigned. */
	private Long runningSimId;
	
	/** Simulation to which this has been assigned. */
	private Long sim_id;
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransitId() {
		return transit_id;
	}

	public void setTransitId(Long transitId) {
		transit_id = transitId;
	}
	
	/** The document id for which this notification is set.*/
	private Long sd_id;
	
	/** If this notification is going to be set on an actor by actor basis, use this field
	 * to indicate the actor which will be triggering the notification to go out.*/
	private Long from_actor_id;
	
	/** If this notification is for only one particular phase, indicate that phase here. */
	private Long from_phase_id;
	
	public Long getFrom_actor_id() {
		return this.from_actor_id;
	}

	public void setFrom_actor_id(Long from_actor_id) {
		this.from_actor_id = from_actor_id;
	}

	public Long getFrom_phase_id() {
		return this.from_phase_id;
	}

	public void setFrom_phase_id(Long from_phase_id) {
		this.from_phase_id = from_phase_id;
	}

	/** Id of the actor that will get this notification. */
	private Long actor_id;
	
	private String notificationText = ""; //$NON-NLS-1$

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getRunningSimId() {
		return runningSimId;
	}

	public void setRunningSimId(Long runningSimId) {
		this.runningSimId = runningSimId;
	}

	public Long getSimId() {
		return this.sim_id;
	}

	public void setSimId(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getSd_id() {
		return this.sd_id;
	}

	public void setSd_id(Long sd_id) {
		this.sd_id = sd_id;
	}

	public Long getActor_id() {
		return this.actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public String getNotificationText() {
		return this.notificationText;
	}

	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}
	
	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static List getAllBaseForSim(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		String hql_string = "from SharedDocActorNotificAssignObj where sim_id = :sim_id and runningSimId is null"; //$NON-NLS-1$
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hql_string)
		.setLong("sim_id", sim_id)
		.list();

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
	public static List getAllAssignmentsForDocument(String schema, Long doc_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		String hql_string = "from SharedDocActorNotificAssignObj where sd_id = " + doc_id; //$NON-NLS-1$
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
		String hql_string = "from SharedDocActorNotificAssignObj where sd_id = " + doc_id + " and actor_id = " + actor_id; //$NON-NLS-1$ //$NON-NLS-2$
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
	public static SharedDocActorNotificAssignObj getById(String schema, Long sim_id) {

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
			Logger.getRootLogger().warn("Warning! Invalid id sent into SDANDAO to remove."); //$NON-NLS-1$
		}
		
		Long sdanao_id = null;
		
		try {
			sdanao_id = new Long(remove_id);
		} catch (Exception e){
			Logger.getRootLogger().warn("Warning! problem converting id :" + remove_id + " passed in to Long."); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		SharedDocActorNotificAssignObj sdanao = SharedDocActorNotificAssignObj.getById(schema, sdanao_id);
		
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
		sdanao.setSimId(this.getSimId());
		sdanao.setRunningSimId(this.getRunningSimId());
		
		return sdanao;
	}

	@Override
	public Long createRunningSimVersion(String schema, Long simId, Long rsId,
			Object templateObject) {
		// Do Nothing here
		// When a new shared document is created for a running sim, that shared document creates all
		// of its Conversation Actor Assignments (ConvActorAssignment) so they all point to the 
		// correct conversation id.
		return null;
	}

}
