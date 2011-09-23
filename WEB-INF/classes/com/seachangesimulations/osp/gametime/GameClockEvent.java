package com.seachangesimulations.osp.gametime;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.sharing.ExportableObject;

/**
 * This class represents events in the life of a simulation.
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
 */
@Entity
@Proxy(lazy = false)
public class GameClockEvent implements ExportableObject{

	/** Database id of this object. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Id of the GameClockPhaseInstruction under control when this event occurred. */
	private Long gcpiId;
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transitId;
	
	/** Simulation id. */
    private Long simId;
    
    /** phase id. */
    private Long phaseId;
    
    /** Running simulation id. */
    private Long rsId;
    
	/** formatting on time output. */
	private java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			"MM/dd/yy HH:mm:ss a");
    
    /** Time event occurred. */
    private java.util.Date eventDate = new java.util.Date();
    
    /** String explanation of event. */
    private String eventTitle = "";
    
	/** If counting toward some deadline, this is it. */
	private Date dateTripPoint = new java.util.Date();

	/** A deadline expressed as a long number. */
	private long longTripPoint = 0;
	
	/** Identifies the type of timer. */
	private int timerType = 0;
	
    /** If time is chunked into things (such as days, months, etc.) how many milliseconds per chunk. */
    private long timeInterval = 0;
    
    /** It time is chunked into things (such as days, months, etc.) the display name of the chunk. */
    private String intervalName = "";
    
	/** The number of the interval (1, 2, 3, etc.) that we are on. */
	private long currentInterval = 0;

	/** Time that the current interval began. */
	private Date currentIntervalStartTime = new Date();

	/** Time that the current interval began, expressed as a long. */
	private long currentIntervalStartTimeLong = 0;

	/** Difference between system time and game time, expressed as a long. */
	private long timeOffset = 0;
	
	/** Indicates if this timer has been initialized. */
	private boolean initialized = false;
    
	/**
	 * Pulls the game clock event out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param gcId
	 * @return
	 */
	public static GameClockEvent getById(String schema, Long gceId) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		GameClockEvent gce = (GameClockEvent) MultiSchemaHibernateUtil
				.getSession(schema).get(GameClockEvent.class, gceId);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return gce;
	}
	
	/** Saves object back to the database. */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	public GamePhaseCurrentTime generateGamePhaseCurrentTime(){
		
		GamePhaseCurrentTime gpct = new GamePhaseCurrentTime();
		
		gpct.setCurrentInterval(this.currentInterval);
		gpct.setCurrentIntervalStartTime(this.currentIntervalStartTime);
		gpct.setCurrentIntervalStartTimeLong(this.currentIntervalStartTimeLong);
		gpct.setDateTripPoint(dateTripPoint);
		gpct.setInitialized(initialized);
		gpct.setIntervalName(intervalName);
		gpct.setLongTripPoint(longTripPoint);
		gpct.setPhaseId(phaseId);
		gpct.setRsId(rsId);
		gpct.setSdf(sdf);
		gpct.setSimId(simId);
		gpct.setTimeInterval(timeInterval);
		gpct.setTimeOffset(timeOffset);
		gpct.setTimerType(timerType);
		
		return gpct;
	}

	@Override
	public Long getTransitId() {
		return transitId;
	}

	@Override
	public void setTransitId(Long transitId) {
		this.transitId = transitId;	
	}

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

	public Long getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}

	public Long getRsId() {
		return rsId;
	}

	public void setRsId(Long rsId) {
		this.rsId = rsId;
	}

	public java.util.Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(java.util.Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public Long getGcpiId() {
		return gcpiId;
	}

	public void setGcpiId(Long gcpiId) {
		this.gcpiId = gcpiId;
	}

	public Date getDateTripPoint() {
		return dateTripPoint;
	}

	public void setDateTripPoint(Date dateTripPoint) {
		this.dateTripPoint = dateTripPoint;
	}

	public long getLongTripPoint() {
		return longTripPoint;
	}

	public void setLongTripPoint(long longTripPoint) {
		this.longTripPoint = longTripPoint;
	}

	public int getTimerType() {
		return timerType;
	}

	public void setTimerType(int timerType) {
		this.timerType = timerType;
	}

	public long getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}

	public String getIntervalName() {
		return intervalName;
	}

	public void setIntervalName(String intervalName) {
		this.intervalName = intervalName;
	}

	public long getCurrentInterval() {
		return currentInterval;
	}

	public void setCurrentInterval(long currentInterval) {
		this.currentInterval = currentInterval;
	}

	public Date getCurrentIntervalStartTime() {
		return currentIntervalStartTime;
	}

	public void setCurrentIntervalStartTime(Date currentIntervalStartTime) {
		this.currentIntervalStartTime = currentIntervalStartTime;
	}

	public long getCurrentIntervalStartTimeLong() {
		return currentIntervalStartTimeLong;
	}

	public void setCurrentIntervalStartTimeLong(long currentIntervalStartTimeLong) {
		this.currentIntervalStartTimeLong = currentIntervalStartTimeLong;
	}

	public long getTimeOffset() {
		return timeOffset;
	}

	public void setTimeOffset(long timeOffset) {
		this.timeOffset = timeOffset;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public static GameClockEvent getLastEvent(String schema, Long gcpiId) {

		if (gcpiId == null){
			return null;
		}
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<GameClockEvent> startList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery("from GameClockEvent where id = (select max(id) from GameClockEvent where gcpiId = :gcpiId)")
					.setLong("gcpiId", gcpiId).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if ((startList == null) || (startList.size() == 0)){
			return null;
		} else {
			return (GameClockEvent) startList.get(0);
		}

	}

	/**
	 * 
	 * @param schema
	 * @param gcpi
	 * @return
	 */
	public static GameClockEvent createEvent(String schema,
			GameClockPhaseInstructions gcpi, Long rsId, String eventMsg) {
		
		GameClockEvent gce = new GameClockEvent();
		
		gce.setGcpiId(gcpi.getId());
		gce.setEventTitle(eventMsg);
		gce.setEventDate(new java.util.Date());
		gce.setSimId(gcpi.getSimId());
		gce.setRsId(rsId);
		gce.setPhaseId(gcpi.getPhaseId());
		gce.saveMe(schema);
		
		return gce;
	}
	
}
