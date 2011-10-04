package com.seachangesimulations.osp.gametime;

import java.util.List;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.networking.PlayerSessionObject;
import org.usip.osp.networking.SessionObjectBase;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.persistence.OSPErrors;
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

	public static final int GCPI_UNDEFINED = 0;
	public static final int GCPI_CONST = 1;
	public static final int GCPI_UP_TIME = 2;
	public static final int GCPI_UP_RUNNING_TIME = 3;
	public static final int GCPI_UP_INTERVAL = 4;
	public static final int GCPI_DOWN_TIME = 5;
	public static final int GCPI_DOWN_RUNNING_TIME = 6;
	public static final int GCPI_DOWN_INTERVAL = 7;
	
	public static final int START_TIME_OVER = 0;
	public static final int START_TIME_CARRIES_OVER = 1;
	
	/** Database id of this object. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transitId;
	
	/** Simulation id. */
    private Long simId;
    
    /** Phase id */
    private Long phaseId;
    
    /** Identifies the type of timer. */
    private int timerType = 0;
    
    /** Time clock was started */
    private java.util.Date startTime = new java.util.Date();
    
    /** Projected end time */
    private java.util.Date endTime = new java.util.Date();
    
    /** Planned duration of timer (in seconds) */
    private long plannedDuration = 0;
    
    /** If time is chunked into things (such as days, months, etc.) how many seconds per chunk. */
    private long currentInterval = 0;
    
    /** It time is chunked into things (such as days, months, etc.) the display name of the chunk. */
    private String intervalName = "";
    
    private String textSynopsis = "";
    
    /** Zero argument constructor required by Hibernate. */
    public GameClockPhaseInstructions() {
    	
    }
    
    public GameClockPhaseInstructions(String schema, Long simId, Long phaseId){
    	this.simId = simId;
    	this.phaseId = phaseId;
    	
    	this.saveMe(schema);
    	
    }
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
	
	/**
	 * Gets all of the phase instructions for a simulation. 
	 * 
	 * @param simId
	 * @param schema
	 * @return
	 */
	public static List<GameClockPhaseInstructions> getAllForSim(String schema, Long simId) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<GameClockPhaseInstructions> returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery("from GameClockPhaseInstructions where simId = :simId")
					.setLong("simId", simId).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	/**
	 * Gets a GameClockPhaseInstructions by phase and sim ids.
	 * @param sob
	 * @param schema
	 * @param phaseId
	 * @param simId
	 * @return
	 */
	public static GameClockPhaseInstructions getByPhaseAndSimId(
			SessionObjectBase sob, Long phaseId, Long simId) {

		System.out.println("GameClockPhaseInstructions.getByPhaseAndSim");
		
		if ((phaseId == null) || (simId == null)){
			System.out.println("GameClockPhaseInstructions.getByPhaseAndSim  nullness");
			return null;
		}
		
		MultiSchemaHibernateUtil.beginTransaction(sob.schema);

		List<GameClockPhaseInstructions> startList = MultiSchemaHibernateUtil
		.getSession(sob.schema)
		.createQuery("from GameClockPhaseInstructions where simId = :simId and phaseId = :phaseId")
			.setLong("simId", simId)
			.setLong("phaseId", phaseId)
			.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(sob.schema);
		
		if ((startList == null) || (startList.size() == 0)){
			return null;
		} else if (startList.size() == 1){
			System.out.println("GameClockPhaseInstructions.getByPhaseAndSim   got one !!!!!!!!!!!1");
			return (GameClockPhaseInstructions) startList.get(0);
		} else {
			OSPErrors.storeInternalWarning("Duplicate GCPI phase: " + phaseId + ", simId " + simId, sob);
			return null;
		}

	}
	
	/**
	 * 
	 * @param request
	 * @param sob
	 * @return
	 */
	public static GameClockPhaseInstructions handleEdit(HttpServletRequest request, 
			SessionObjectBase sob){
		
		GameClockPhaseInstructions gcpi = new GameClockPhaseInstructions();
		
		String sending_page = (String) request.getParameter("sending_page");
		
		if ((USIP_OSP_Util.stringFieldHasValue(sending_page)) && 
			(sending_page.equalsIgnoreCase("edit_gcpi"))) {
			
			String phase_id = (String) request.getParameter("phase_id");
			
			gcpi = GameClockPhaseInstructions.getByPhaseAndSimId(sob, sob.sim_id, new Long(phase_id));
			
			if (gcpi == null){
				gcpi = new GameClockPhaseInstructions(sob.schema, sob.sim_id, new Long(phase_id));
			}
			
			String timer_type = (String) request.getParameter("timer_type");
			
			if (USIP_OSP_Util.stringFieldHasValue(timer_type)){
				gcpi.timerType = new Long(timer_type).intValue();
			}
			
			
			switch (gcpi.timerType){
				case (GCPI_CONST):
					gcpi.setTextSynopsis("constant");
					break;
				case (GCPI_UP_TIME):
					gcpi.setTextSynopsis("up time");
					break;
				case (GCPI_UP_RUNNING_TIME):
					gcpi.setTextSynopsis("up running time");
					break;
				case (GCPI_UP_INTERVAL):
					String seconds_per_interval = (String) request.getParameter("seconds_per_interval");
					String interval_name = (String) request.getParameter("interval_name");
					
					Long secondsPerInterval = new Long(0);
					
					try {
						secondsPerInterval = new Long (seconds_per_interval);
					} catch (Exception e){
						secondsPerInterval = new Long(0);
					}
					gcpi.setCurrentInterval(secondsPerInterval * 1000); // Convert to milliseconds
					gcpi.setIntervalName(interval_name);
					gcpi.setTextSynopsis("Count up in " + interval_name + " of " + secondsPerInterval + " seconds.");
					break;
				case (GCPI_DOWN_TIME):
					gcpi.setTextSynopsis("down time");
					break;
				case (GCPI_DOWN_RUNNING_TIME):
					gcpi.setTextSynopsis("down running time");
					break;
				case (GCPI_DOWN_INTERVAL):
					gcpi.setTextSynopsis("down interval");
					break;
			}
			
			gcpi.saveMe(sob.schema);
			

		}
		
		return gcpi;
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

	public int getTimerType() {
		return timerType;
	}

	public void setTimerType(int timerType) {
		this.timerType = timerType;
	}

	public long getCurrentInterval() {
		return currentInterval;
	}

	public void setCurrentInterval(long currentInterval) {
		this.currentInterval = currentInterval;
	}

	public String getIntervalName() {
		return intervalName;
	}

	public void setIntervalName(String intervalName) {
		this.intervalName = intervalName;
	}

	public java.util.Date getStartTime() {
		return startTime;
	}

	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
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

	public String getTextSynopsis() {
		return textSynopsis;
	}

	public void setTextSynopsis(String textSynopsis) {
		this.textSynopsis = textSynopsis;
	}

	/** When moving into a new phase, these instructions are activated. */
	public static void activatePhaseInstructions(
			HttpServletRequest request,
			PlayerSessionObject pso) {
		
		System.out.println("activatePhaseInstructions");
		
		GameClockPhaseInstructions gcpi = GameClockPhaseInstructions.getByPhaseAndSimId(
				pso, pso.phase_id, pso.sim_id);
		
		if (gcpi == null){	// There are no instructions for this phase.
			return;
		}
		
		// Get highest history, and then pull the snapshot from it.
		GameClockEvent maxGCE = GameClockEvent.getLastEventForPhase(pso.schema, gcpi.getId());
		
		/*
		 * TODO Get this sorted out.
		 
		if (maxGCE == null){
			//maxGCE = GameClockEvent.createEvent(pso.schema, gcpi, pso.getRunningSimId(), "Initial Event Occurred");
			maxGCE = GameClockEvent.createEvent(pso.schema, gcpi, pso.getRunningSimId(), "Initial Event Occurred");
		}
		
		GamePhaseCurrentTime gpct = maxGCE.generateGamePhaseCurrentTime();
		
		// Store it in the cache
		GamePhaseCurrentTime.putGPCTInCache(request, pso.schema, pso.sim_id, pso.getRunningSimId(), pso.phase_id, gpct);
		*/
	}
	
	public GamePhaseCurrentTime generateInitialGamePhaseCurrentTime(){
		
		GamePhaseCurrentTime gpct = new GamePhaseCurrentTime();
		/*
		gpct.setCurrentInterval(this.currentInterval);
		gpct.setCurrentIntervalStartTime(new java.util.Date());
		gpct.setCurrentIntervalStartTimeLong(this.getcu);
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
		*/
		
		return gpct;
	}
	
}
