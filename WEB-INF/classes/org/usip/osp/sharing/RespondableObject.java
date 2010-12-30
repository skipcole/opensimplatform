package org.usip.osp.sharing;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This object represents something to be put into a list from which the player can select. 
 * Selecting an object will indicate that this is something that player is responding to.
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
@Proxy(lazy=false)
public class RespondableObject {

	/** Zero based constructor. */
	public RespondableObject(){
		dateOfResponseObjectCreation = new Date();
	}
	
    /** Database id. */
	@Id
	@GeneratedValue
    private Long id;
	
	private java.util.Date dateOfResponseObjectCreation;

	/** Running Simulation id. */
	private Long rsId;
	
	/** Simulation id. */
	private Long simId;
	
	/** Actor who created object. */
	private Long creatingActorId;
	
	private String creatingUserName;
	
	private String creatingUserDisplayName;
	
	/** Phase id */
	private Long phaseId;
	
	/** The type of object to respond to that the player sees." */
	private String humanReadableObjectType = "Announcement";
	
	private String responseObjectSynopsis = "";
	
	/** The id of this object used to pull it out of the database if necessary. */
	private Long objectId;
	
	/**
	 * The class name of this object used to create it or pull it out of the
	 * database if necessary.
	 */
	private String className;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDateOfResponseObjectCreation() {
		return dateOfResponseObjectCreation;
	}

	public void setDateOfResponseObjectCreation(Date dateOfResponseObjectCreation) {
		this.dateOfResponseObjectCreation = dateOfResponseObjectCreation;
	}

	public Long getRsId() {
		return rsId;
	}

	public void setRsId(Long rsId) {
		this.rsId = rsId;
	}

	public Long getSimId() {
		return simId;
	}

	public void setSimId(Long simId) {
		this.simId = simId;
	}

	public Long getCreatingActorId() {
		return creatingActorId;
	}

	public void setCreatingActorId(Long creatingActorId) {
		this.creatingActorId = creatingActorId;
	}

	public String getCreatingUserName() {
		return creatingUserName;
	}

	public void setCreatingUserName(String creatingUserName) {
		this.creatingUserName = creatingUserName;
	}

	public String getCreatingUserDisplayName() {
		return creatingUserDisplayName;
	}

	public void setCreatingUserDisplayName(String creatingUserDisplayName) {
		this.creatingUserDisplayName = creatingUserDisplayName;
	}

	public Long getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getHumanReadableObjectType() {
		return humanReadableObjectType;
	}

	public void setHumanReadableObjectType(String humanReadableObjectType) {
		this.humanReadableObjectType = humanReadableObjectType;
	}

	public String getResponseObjectSynopsis() {
		return responseObjectSynopsis;
	}

	public void setResponseObjectSynopsis(String responseObjectSynopsis) {
		this.responseObjectSynopsis = responseObjectSynopsis;
	}
	
	public String getTagLineText(){
		return this.getHumanReadableObjectType() + " : " + this.getResponseObjectSynopsis();
	}
	
	public void saveMe(String schema) {
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
	}
	
	public RespondableObject(String schema, Long simId, Long rsId, Long phase_id, 
			Long objId, String objClassName, String roSynopsis, 
			Long actor_id, String userName, String userDisplayName,
			String recipients) {
		
		dateOfResponseObjectCreation = new Date();
		
		setSimId(simId);
		setRsId(rsId);
		setObjectId(objId);
		setClassName(objClassName);
		
		setCreatingActorId(actor_id);
		setPhaseId(phase_id);
		
		setResponseObjectSynopsis(roSynopsis);
		
		saveMe(schema);
		
		generateRecipients(this, simId, recipients, schema);
		
	}
	
	//TODO generate recipient lines.
	public void generateRecipients(RespondableObject ro, Long simId, String recipients, String schema){
		
	}
	
}
