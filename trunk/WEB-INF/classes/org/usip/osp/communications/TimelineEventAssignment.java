package org.usip.osp.communications;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.SimSectionDependentObject;

/**
 * This object represents the mapping of an event to a timeline.
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
public class TimelineEventAssignment implements SimSectionDependentObject{
	
	/** Database id of this TimeLine. */
	@Id
	@GeneratedValue
	private Long id;
	
	private Long simId;
	
	private Long runningSimId;
	
	private Long phaseId;
	
	private Long timeLineId;
	
	private Long eventId;
	
	private Long creatingActorId;
	
	private Long transitId;

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

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
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

	@Override
	public Long createRunningSimVersion(String schema, Long simId, Long rsId,
			Object templateObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getTransit_id() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveMe(String schema) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTransit_id(Long transitId) {
		// TODO Auto-generated method stub
		
	}


}
