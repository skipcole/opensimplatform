package org.usip.osp.communications;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.ExportableObject;
import org.usip.osp.baseobjects.SimSectionDependentObject;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This object represents the mapping of an object (event, inject, etc.) to a timeline.
 *
 */
/* 
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it
 * and/or modify it under the terms of the new BSD Style license associated with
 * this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. <BR>
 * 
 */
@Entity
@Proxy(lazy = false)
public class TimelineObjectAssignment implements ExportableObject, SimSectionDependentObject{
	
	/** Database id of this TimeLine. */
	@Id
	@GeneratedValue
	private Long id;
	
	private Long simId;
	
	private Long runningSimId;
	
	private Long phaseId;
	
	private Long timeLineId;
	
	private Long objectId;
	
	private String objectClass = "";
	
	private Long creatingActorId;
	
	private Long transitId;
	
	private int assignmentType = 0;
	
	private Date eventStartTime = null;
	private Date eventEndTime = null;
	
	private String start = "";
	private String end = "";
	private String eventTitle = "";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSimId() {
		return simId;
	}

	public void setSimId(Long simId) {
		this.simId = simId;
	}

	public Long getRunningSimId() {
		return runningSimId;
	}

	public void setRunningSimId(Long runningSimId) {
		this.runningSimId = runningSimId;
	}

	public Long getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}

	public Long getTimeLineId() {
		return timeLineId;
	}

	public void setTimeLineId(Long timeLineId) {
		this.timeLineId = timeLineId;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getObjectClass() {
		return objectClass;
	}

	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}

	public Long getCreatingActorId() {
		return creatingActorId;
	}

	public void setCreatingActorId(Long creatingActorId) {
		this.creatingActorId = creatingActorId;
	}

	public Long getTransitId() {
		return transitId;
	}

	public void setTransitId(Long transitId) {
		this.transitId = transitId;
	}

	public int getAssignmentType() {
		return assignmentType;
	}

	public void setAssignmentType(int assignmentType) {
		this.assignmentType = assignmentType;
	}

	public Date getEventStartTime() {
		return eventStartTime;
	}

	public void setEventStartTime(Date eventStartTime) {
		this.eventStartTime = eventStartTime;
	}

	public Date getEventEndTime() {
		return eventEndTime;
	}

	public void setEventEndTime(Date eventEndTime) {
		this.eventEndTime = eventEndTime;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	@Override
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	@Override
	public Long createRunningSimVersion(String schema, Long simId, Long rsId,
			Object templateObject) {
		// TODO Auto-generated method stub
		return null;
	}


}
