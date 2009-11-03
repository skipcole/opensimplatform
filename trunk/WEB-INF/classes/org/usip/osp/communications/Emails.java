package org.usip.osp.communications;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

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
@Table(name = "EMAILS")
@Proxy(lazy=false)
public class Emails {
	
	/**
	 * Zero argument constructor required by hibernate.
	 */
	public Emails(){
		
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
    
    /** Id of the user making this chat line. */
    private Long fromUser;
    
    /** Subject line of this email. */
    private String subjectLine = ""; //$NON-NLS-1$
    
    /** Body of the message text. */
    @Lob
    private String msgtext = ""; //$NON-NLS-1$
    
    private boolean hasBeenRead;
    
    private Long priority;
    
	@Column(name="MSG_DATE", columnDefinition="datetime") 	
	private java.util.Date msgDate;

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

	public boolean isHasBeenRead() {
		return hasBeenRead;
	}

	public void setHasBeenRead(boolean hasBeenRead) {
		this.hasBeenRead = hasBeenRead;
	}

	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}
	
	
}
