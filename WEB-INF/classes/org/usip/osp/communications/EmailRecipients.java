package org.usip.osp.communications;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.SimActorAssignment;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

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
@Proxy(lazy=false)
public class EmailRecipients {
	
	public static final int RECIPIENT_FROM = 1;
	public static final int RECIPIENT_TO = 2;
	public static final int RECIPIENT_CC = 3;
	public static final int RECIPIENT_BCC = 4;

	/** Zero argument constructor required by hibernate. */
	public EmailRecipients(){
		
	}
	
	public EmailRecipients(String schema, Long email_id, Long rsid, Long sid, Long aid, String a_name, int etype){
		
		this.setActor_id(aid);
		this.setActorName(a_name);
		this.setEmail_id(email_id);
		this.setRecipient_type(etype);
		this.setRunning_sim_id(rsid);
		this.setSim_id(sid);
		
		this.saveMe(schema);
	
	}
	
	
    /** Unique id of this email recipient line. */
	@Id 
	@GeneratedValue
    private Long id;
	
	/** ID in the Emails table that links this object. */
	private Long email_id;
	
	/** ID of the simulation in which this email was sent. */
	private Long sim_id;
	
	/** ID of the running simulation in which this email was sent. */
	private Long running_sim_id;
	
	/** ID of the actor to which this email was sent. */
	private Long actor_id;
	
	/** Name of this actor */
	private String actorName;
	
	/** ID of the player playing the role to which this email was sent. */
	private Long user_id;
	
	/** Type of receipt (from, to, cc, or bcc) */
	private int recipient_type;
	
	private boolean hasBeenRead = false;
	
	/**
	 * Saves this object back to the database.
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
	 * @param er_id
	 */
	public static void removeMe(String schema, Long er_id){
		
		EmailRecipients saa = EmailRecipients.getById(schema, er_id);
		
		if (saa != null){
			MultiSchemaHibernateUtil.beginTransaction(schema);
			MultiSchemaHibernateUtil.getSession(schema).delete(saa);
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		}
	}
	
	/**
	 * Pulls the email recipient out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static EmailRecipients getById(String schema, Long email_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		EmailRecipients email_rep = (EmailRecipients) MultiSchemaHibernateUtil.getSession(schema).get(EmailRecipients.class, email_id);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return email_rep;

	}
	
	/**
	 * Returns the Email Recipient line for a particular email to a particular actor in a particular simulation.
	 * 
	 * @param schema
	 * @param running_sim_id
	 * @param actor_id
	 * @return
	 */
	public static EmailRecipients getEmailRecipientsLine(String schema, Long email_id, Long running_sim_id, Long actor_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		String hqlString = "from EmailRecipients where " +
				"running_sim_id = :rsid and actor_id = :aid and email_id = :eid";

		List returnList = MultiSchemaHibernateUtil.getSession(schema)
			.createQuery(hqlString)
			.setString("rsid", running_sim_id.toString())
			.setString("aid", actor_id.toString())
			.setString("eid", email_id.toString())
			.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		if ((returnList == null) || (returnList.size() == 0)) {
			return null;
		} else if (returnList.size() > 1){
			
			Logger.getRootLogger().warn("Have multiple email recipient lines for email with id of " + email_id); //$NON-NLS-1$
			return (EmailRecipients) returnList.get(0);
			
		} else {
			return (EmailRecipients) returnList.get(0);
		}
	
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmail_id() {
		return email_id;
	}

	public void setEmail_id(Long email_id) {
		this.email_id = email_id;
	}

	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
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

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actor_name) {
		this.actorName = actor_name;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public int getRecipient_type() {
		return recipient_type;
	}

	public void setRecipient_type(int recipient_type) {
		this.recipient_type = recipient_type;
	}

	public boolean isHasBeenRead() {
		return hasBeenRead;
	}

	public void setHasBeenRead(boolean hasBeenRead) {
		this.hasBeenRead = hasBeenRead;
	}
	
}
