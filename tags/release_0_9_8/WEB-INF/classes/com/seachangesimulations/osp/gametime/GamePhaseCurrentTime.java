package com.seachangesimulations.osp.gametime;

import java.util.Date;
import java.util.Hashtable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import org.usip.osp.networking.SessionObjectBase;
import org.usip.osp.networking.USIP_OSP_Cache;
import org.usip.osp.networking.USIP_OSP_ContextListener;

/**
 * This object contains current information about the state of the game.
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
 */
public class GamePhaseCurrentTime {

	public GamePhaseCurrentTime(){
		
	}
	
	/** Simulation id. */
	private Long simId;

	/** phase id. */
	private Long phaseId;

	/** Running simulation id. */
	private Long rsId;
	
	/** Id of the GameClockPhaseInstruction under control at this time. */
	private Long gcpiId;

	private String dateFormat = "MM/dd/yy HH:mm:ss a";
	
	/** formatting on time output. */
	private java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(dateFormat);

	/** If counting toward some deadline, this is it. */
	private Date dateTripPoint = new java.util.Date();

	/** A deadline expressed as a long number. */
	private long longTripPoint = 0;

	/** Identifies the type of timer. */
	private int timerType = 0;

	/**
	 * If time is chunked into things (such as days, months, etc.) how many
	 * milliseconds per chunk.
	 */
	private long timeInterval = 0;

	/**
	 * It time is chunked into things (such as days, months, etc.) the display
	 * name of the chunk.
	 */
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

	private void initialize() {
		currentIntervalStartTime = new Date();
		currentIntervalStartTimeLong = currentIntervalStartTime.getTime();

		currentInterval = 0;
		initialized = true;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	private String calculateGameTime(HttpServletRequest request) {

		if (!initialized) {
			initialize();
		}

		String returnString = "Undefined Time with timerType: " + timerType;
		// /
		switch (timerType) {
		case (GameClockPhaseInstructions.GCPI_CONST):
			returnString = ("constant");
			break;
		case (GameClockPhaseInstructions.GCPI_UP_TIME):
			returnString = ("up time");
			break;
		case (GameClockPhaseInstructions.GCPI_UP_RUNNING_TIME):
			returnString = ("up running time");
			break;
		case (GameClockPhaseInstructions.GCPI_UP_INTERVAL):
			long millisSinceLast = new Date().getTime()
					- currentIntervalStartTimeLong;

			if (millisSinceLast >= currentInterval) {
				++currentInterval;
				System.out.println(currentInterval);
				// Reset clock for next interval.
				millisSinceLast = 0;
				currentIntervalStartTime = new Date();
				currentIntervalStartTimeLong = currentIntervalStartTime
						.getTime();

			}
			returnString = this.intervalName + " " + currentInterval;
			
			break;
		case (GameClockPhaseInstructions.GCPI_DOWN_TIME):
			returnString = ("down time");
			break;
		case (GameClockPhaseInstructions.GCPI_DOWN_RUNNING_TIME):
			returnString = ("down running time");
			break;
		case (GameClockPhaseInstructions.GCPI_DOWN_INTERVAL):
			returnString = ("down interval");
			break;
		}

		return returnString;
	}

	public String getGameTime(HttpServletRequest request, SessionObjectBase sob) {
		return calculateGameTime(request);
	}

	/**
	 * Pulls the GPCT for this phase (if one exists) out of the cache. If one
	 * does not exist, it returns an empty gpct.
	 * 
	 * @param request
	 * @param schema
	 * @param simId
	 * @param rsId
	 * @param phaseId
	 * @return
	 */
	public static GamePhaseCurrentTime pullGPCTFromCache(
			HttpServletRequest request, String schema, Long simId, Long rsId,
			Long phaseId) {

		Hashtable databaseHash = USIP_OSP_Cache.getCachedHashtable(request,
				schema, USIP_OSP_ContextListener.CACHEON_GAMETIMER,
				USIP_OSP_ContextListener.CACHED_TABLE_LONG_GPCT);

		GamePhaseCurrentTime gpct = (GamePhaseCurrentTime) databaseHash
				.get(rsId);

		if (gpct == null) {

			gpct = new GamePhaseCurrentTime();

		}

		return gpct;
	}

	public static void putGPCTInCache(HttpServletRequest request,
			String schema, Long simId, Long rsId, Long phaseId,
			GamePhaseCurrentTime gpct) {
		
		System.out.println("putGPCTInCache " + simId + "/" + rsId + "/" + phaseId + "/" + gpct);

		Hashtable databaseHash = USIP_OSP_Cache.getCachedHashtable(request,
				schema, USIP_OSP_ContextListener.CACHEON_GAMETIMER,
				USIP_OSP_ContextListener.CACHED_TABLE_LONG_GPCT);

		databaseHash.put(rsId, gpct);

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

	public Long getGcpiId() {
		return gcpiId;
	}

	public void setGcpiId(Long gcpiId) {
		this.gcpiId = gcpiId;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public java.text.SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(java.text.SimpleDateFormat sdf) {
		this.sdf = sdf;
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

	public void setCurrentIntervalStartTimeLong(
			long currentIntervalStartTimeLong) {
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

}
