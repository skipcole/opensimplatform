package com.seachangesimulations.osp.gametime;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.communications.Alert;
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
public class GameClockEvent implements ExportableObject {

	/** Database id of this object. */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Id of the GameClockPhaseInstruction under control when this event
	 * occurred.
	 */
	private Long gcpiId;

	/** Id used when objects are exported and imported moving across databases. */
	private Long transitId;

	/** Simulation id. */
	private Long simId;

	/** phase id. */
	private Long phaseId;

	/** Running simulation id. */
	private Long rsId;

	private String dateFormat = "MM/dd/yy HH:mm:ss a";

	/** formatting on time output. */
	@Transient
	private java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			dateFormat);

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

	public GameClockEvent() {
		System.out.println("game clock event created");
	}

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

	/**
	 * Pulls the current clock from this historical event recording.
	 * 
	 * @return
	 */
	public static GamePhaseCurrentTime generateGamePhaseCurrentTime(
			GameClockEvent gce) {

		GamePhaseCurrentTime gpct = new GamePhaseCurrentTime();

		gpct.setCurrentInterval(gce.currentInterval);
		gpct.setCurrentIntervalStartTime(gce.currentIntervalStartTime);
		gpct.setCurrentIntervalStartTimeLong(gce.currentIntervalStartTimeLong);
		gpct.setDateTripPoint(gce.dateTripPoint);
		gpct.setInitialized(gce.initialized);
		gpct.setIntervalName(gce.intervalName);
		gpct.setLongTripPoint(gce.longTripPoint);
		gpct.setPhaseId(gce.phaseId);
		gpct.setRsId(gce.rsId);
		gpct.setSdf(gce.sdf);
		gpct.setSimId(gce.simId);
		gpct.setTimeInterval(gce.timeInterval);
		gpct.setTimeOffset(gce.timeOffset);
		gpct.setTimerType(gce.timerType);

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

	public static GameClockEvent getLastEventForPhase(String schema, Long gcpiId) {

		if (gcpiId == null) {
			return null;
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<GameClockEvent> startList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from GameClockEvent where id = (select max(id) from GameClockEvent where gcpiId = :gcpiId)")
				.setLong("gcpiId", gcpiId).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if ((startList == null) || (startList.size() == 0)) {
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
	public static GameClockEvent generateGameClockEvent(String schema,
			GamePhaseCurrentTime gpct, String eventMsg) {

		System.out.println("GameClockEvent createEvent");

		GameClockEvent gce = new GameClockEvent();

		gce.setCurrentInterval(gpct.getCurrentInterval());
		gce.setCurrentIntervalStartTime(gpct.getCurrentIntervalStartTime());
		gce.setCurrentIntervalStartTimeLong(gce.getCurrentIntervalStartTime()
				.getTime());
		gce.setDateFormat(gpct.getDateFormat());
		gce.setDateTripPoint(gpct.getDateTripPoint());
		gce.setEventDate(new java.util.Date());
		gce.setEventTitle(eventMsg);

		gce.setGcpiId(gpct.getGcpiId());

		gce.setIntervalName(gpct.getIntervalName());

		gce.setPhaseId(gpct.getPhaseId());
		gce.setRsId(gpct.getRsId());
		gce.setSimId(gpct.getSimId());

		gce.setTimerType(gpct.getTimerType());
		// TODO Figure out what all we need to copy in here.
		// gce.setInitialized(gpct.getI)

		gce.saveMe(schema);

		return gce;
	}

	public java.text.SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(java.text.SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * 
	 * @param schema
	 * @param gcpi
	 * @return
	 */
	public static void generateInitialGameClockEvents(String schema,
			Long simId, String eventMsg) {

		List simGCPI = GameClockPhaseInstructions.getAllForSim(schema, simId);

		for (ListIterator li = simGCPI.listIterator(); li.hasNext();) {
			GameClockPhaseInstructions gcpi = (GameClockPhaseInstructions) li
					.next();

			System.out.println("GameClockEvent createEvent");

			GameClockEvent gce = new GameClockEvent();

			/*
			 * TODO get this sorted out. use package.html as guide.
			 
			gce.setCurrentInterval(gcpi.getCurrentInterval());
			gce.setCurrentIntervalStartTime(gcpi.getCurrentIntervalStartTime());
			gce.setCurrentIntervalStartTimeLong(gce
					.getCurrentIntervalStartTime().getTime());
			gce.setDateFormat(gcpi.getDateFormat());
			gce.setDateTripPoint(gcpi.getDateTripPoint());
			gce.setEventDate(new java.util.Date());
			gce.setEventTitle(eventMsg);

			gce.setGcpiId(gcpi.getGcpiId());

			gce.setIntervalName(gcpi.getIntervalName());

			gce.setPhaseId(gcpi.getPhaseId());
			gce.setRsId(gcpi.getRsId());
			gce.setSimId(gcpi.getSimId());

			gce.setTimerType(gcpi.getTimerType());
			// TODO Figure out what all we need to copy in here.
			// gce.setInitialized(gpct.getI)

			gce.saveMe(schema);
			*/

		}

	}

}
