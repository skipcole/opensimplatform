package com.seachangesimulations.osp.gametime;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.sharing.ExportableObject;

/**
 * This class represents instructions on how the game clock should act during a phase
 * of a simulation.
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
public class GameClockPhaseInstructions  implements ExportableObject{

	/** Database id of this object. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transitId;
	
	/** Simulation id. */
    private Long simId;
    
    /** Running simulation id. */
    private Long rsId;
    
    /** Phase id */
    private Long phaseId;
    
    /** Time clock was started */
    private java.util.Date startTime = new java.util.Date();
    
    /** Current time on clock */
    private java.util.Date currentTime = new java.util.Date();
    
    /** Projected end time */
    private java.util.Date endTime = new java.util.Date();
    
    /** Planned duration of timer (in seconds) */
    private long plannedDuration = 0;
    
    /** If time is chunked into things (such as days, months, etc.) how many seconds per chunk. */
    private long timeInterval = 0;
    
    /** It time is chunked into things (such as days, months, etc.) the display name of the chunk. */
    private String timeIntroductoryString = "";
    
    private boolean paused = true;
    
	/**
	 * Pulls the game clock out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param gcId
	 * @return
	 */
	public static GameClockPhaseInstructions getById(String schema, Long gcpiId) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		GameClockPhaseInstructions gcpi = (GameClockPhaseInstructions) MultiSchemaHibernateUtil
				.getSession(schema).get(GameClockPhaseInstructions.class, gcpiId);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return gcpi;
	}
	
	/** Saves object back to the database. */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	public static List<GameClockPhaseInstructions> getAllForSim(Long simId,
			String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<GameClockPhaseInstructions> returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery("from GameClockPhaseInstructions where simId = :simId")
					.setLong("simId", simId).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
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

	public Long getRsId() {
		return rsId;
	}

	public void setRsId(Long rsId) {
		this.rsId = rsId;
	}

	public Long getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}

	public java.util.Date getStartTime() {
		return startTime;
	}

	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}

	public java.util.Date getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(java.util.Date currentTime) {
		this.currentTime = currentTime;
	}

	public java.util.Date getEndTime() {
		return endTime;
	}

	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}

	public long getPlannedDuration() {
		return plannedDuration;
	}

	public void setPlannedDuration(long plannedDuration) {
		this.plannedDuration = plannedDuration;
	}

	public long getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}

	public String getTimeIntroductoryString() {
		return timeIntroductoryString;
	}

	public void setTimeIntroductoryString(String timeIntroductoryString) {
		this.timeIntroductoryString = timeIntroductoryString;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	
}
