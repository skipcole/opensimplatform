package org.usip.osp.communications;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.SimulationPhase;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents an in simulation email.
 * It is in development and is currently not used anywhere.
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
@Proxy(lazy=false)
public class Email {
	
	/**
	 * Zero argument constructor required by hibernate.
	 */
	public Email(){
		
	}
	
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
	 * Pulls the Email out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param email_id
	 * @return
	 */
	public static Email getMe(String schema, Long email_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Email email = (Email) MultiSchemaHibernateUtil.getSession(schema).get(Email.class, email_id);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return email;

	}

    /** Unique id of this email line. Also used for indexing (thus assuming ids only go up). */
	@Id 
	@GeneratedValue
    private Long id;
	
	private Long sim_id;
	
    /** Identifier of the game this chat line is associated with*/
	@Column(name = "RUNNING_SIM_ID")
    private Long running_sim_id;
	
	/** Id of the thread that this email may exist in. */
    private Long thread_id;
    
    /** Id of the actor making this chat line. */
	private Long fromActor;
	
	private String fromActorName = ""; //$NON_NSL-1$
    
    /** Id of the user making this chat line. */
    private Long fromUser;
    
    /** Subject line of this email. */
    private String subjectLine = ""; //$NON-NLS-1$
    
    /** Body of the message text. */
    @Lob
    private String msgtext = ""; //$NON-NLS-1$
    
	@Column(name="MSG_DATE", columnDefinition="datetime") 	
	private java.util.Date msgDate;

	/** Indicates if message is a draft, or if it has been actually sent. */
	private boolean hasBeenSent = false;

	public boolean hasBeenSent() {
		return hasBeenSent;
	}

	public void setHasBeenSent(boolean hasBeenSent) {
		this.hasBeenSent = hasBeenSent;
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

	public Long getRunning_sim_id() {
		return running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public Long getThread_id() {
		return thread_id;
	}

	public void setThread_id(Long thread_id) {
		this.thread_id = thread_id;
	}

	public Long getFromActor() {
		return fromActor;
	}

	public void setFromActor(Long fromActor) {
		this.fromActor = fromActor;
	}

	public String getFromActorName() {
		return fromActorName;
	}

	public void setFromActorName(String fromActorName) {
		this.fromActorName = fromActorName;
	}

	public Long getFromUser() {
		return fromUser;
	}

	public void setFromUser(Long fromUser) {
		this.fromUser = fromUser;
	}

	public String getSubjectLine() {
		return subjectLine;
	}

	public void setSubjectLine(String subjectLine) {
		this.subjectLine = subjectLine;
	}

	public String getMsgtext() {
		return msgtext;
	}

	public void setMsgtext(String msgtext) {
		this.msgtext = msgtext;
	}

	public java.util.Date getMsgDate() {
		return msgDate;
	}

	public void setMsgDate(java.util.Date msgDate) {
		this.msgDate = msgDate;
	}
	
	/**
	 * Returns all of the email directed to an actor during a simulation.
	 * 
	 * @param schema
	 * @param running_sim_id
	 * @param actor_id
	 * @return
	 */
	public static List getAllTo(String schema, Long running_sim_id, Long actor_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		String hqlString = "from EmailRecipients where " +
				"running_sim_id = :rsid and actor_id = :aid";
		
		List tempList = MultiSchemaHibernateUtil.getSession(schema)
			.createQuery(hqlString)
			.setString("rsid", running_sim_id.toString())
			.setString("aid", actor_id.toString())
			.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		ArrayList returnList = new ArrayList();
		
		for (ListIterator<EmailRecipients> li = tempList.listIterator(); li.hasNext();) {
			EmailRecipients this_er = li.next();
			
			Email x = Email.getMe(schema, this_er.getEmail_id());
			
			returnList.add(x);
			
		}
		
		return returnList;
	
	}
	
	/**
	 * Returns all of the email directed to an actor during a simulation.
	 * 
	 * @param schema
	 * @param running_sim_id
	 * @param actor_id
	 * @return
	 */
	public static List getRecipientsOfAnEmail(String schema, Long email_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		String hqlString = "from EmailRecipients where " +
				"email_id = :eid";
		
		List returnList = MultiSchemaHibernateUtil.getSession(schema)
			.createQuery(hqlString)
			.setString("eid", email_id.toString())
			.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		
		return returnList;
	
	}
	
	public List getAllForActor(String schema, Long running_sim_id, Long actor_id){
		return null;
	}
	
}
