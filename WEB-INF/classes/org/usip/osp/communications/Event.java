package org.usip.osp.communications;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.ExportableObject;
import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.baseobjects.SimPhaseAssignment;
import org.usip.osp.baseobjects.SimSectionDependentObject;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.sharing.ObjectPackager;

/**
 * This class represents an event.
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
@Table(name = "EVENT")
@Proxy(lazy = false)
public class Event implements EventInterface, SimSectionDependentObject, ExportableObject{
	
	public static SimpleDateFormat similie_sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss z");

	public Event() {
		
	}
	
	/** Events of this type will normally happen.  */
	public static final int TYPE_PLANNED = 1;
	
	/** Events of this type may occur depending on how the facilitator decides. */
	public static final int TYPE_POSSIBLE = 2;
	
	/** Events of this type may occur based on the player's choices. */
	public static final int TYPE_CONDITIONAL = 3;
	
	/** Rough categorization of this event: we may have to go to bit mask later to adequately categorize events. */
	private int eventType;
	
	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	/** Database id of this Event. */
	@Id
	@GeneratedValue
	private Long id;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Date eventStartTime;
	private Date eventEndTime;
	
	private String start = "";
	private String end = "";
	private String eventTitle = "";
	
	@Lob
	private String eventMsgBody = "";
	
	private Long simId;
	private Long runningSimId;
	private Long phaseId;

	/** If this event is associated with a particular timeline, record that here. */
	private Long timelineId;
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransitId() {
		return transit_id;
	}

	public void setTransitId(Long transitId) {
		transit_id = transitId;
	}
	
	public Date getEventStartTime() {
		return eventStartTime;
	}

	public void setEventStartTime(Date startTime) {
		this.eventStartTime = startTime;
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

	public String getEventMsgBody() {
		return eventMsgBody;
	}

	public void setEventMsgBody(String text) {
		this.eventMsgBody = text;
	}

	
	/**
	 * returns an XML string containing the packaged objects. 
	 * 
	 * @param setOfEvents
	 * @return
	 */
	public static String packupArray(List setOfEvents){
		
		String returnString = "";
		
		for (ListIterator<EventInterface> li = setOfEvents.listIterator(); li.hasNext();) {
			EventInterface thisEvent = li.next();
			
			returnString += packageEvent(thisEvent) + ObjectPackager.lineTerminator;
			
		}
		
		return returnString;
	}
	
	/**
	 * Packages an event in the format required by similie timeline.
	 * 
	 * @param a
	 * @return
	 */
	public static String packageEvent(EventInterface ei){
		
		String icon_name = "  icon=\"" + USIP_OSP_Properties.getValue("base_sim_url") ;
		
		
		if (ei.getEventType() == 3){
			icon_name += "/third_party_libraries/timeline_2.3.0/timeline_js/images/red-circle.png\"";
		} else if (ei.getEventType() == 2){
			icon_name += "/third_party_libraries/timeline_2.3.0/timeline_js/images/green-circle.png\"";
		} else {
			icon_name = "";
		}
		
		String returnString = "<event start=\"" 
			+ similie_sdf.format(ei.getEventStartTime()) + 
			"\" title=\"" + ei.getEventTitle() +
			"\" " + icon_name 
			+ ">";
		
		returnString += USIP_OSP_Util.htmlToCode(ei.getEventMsgBody());
		
		returnString += "</event>";
		return returnString;
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
	
	public Long getTimelineId() {
		return timelineId;
	}

	public void setTimelineId(Long timelineId) {
		this.timelineId = timelineId;
	}

	/**
	 * Pulls the event out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static Event getById(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Event event = (Event) MultiSchemaHibernateUtil.getSession(schema).get(Event.class, sim_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return event;

	}
	
	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param phase_id
	 */
	public static void removeMe(String schema, Long event_id){
		
		Event event = Event.getById(schema, event_id);
		
		if (event != null){
			MultiSchemaHibernateUtil.beginTransaction(schema);
			MultiSchemaHibernateUtil.getSession(schema).delete(event);
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		}
	}
	
	/**
	 * Returns a list of all events created for a simulation.
	 * 
	 * @param simid
	 * @param schema
	 * @return
	 */
	public static List<Event> getAllForSimAndPhase(Long simid, Long phaseid, String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Event> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Event where simId = :sim_id and phaseId = :phase_id")
				.setString("sim_id", simid.toString())
				.setString("phase_id", phaseid.toString())
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	/**
	 * Returns a list of all events created for a simulation.
	 * 
	 * @param simid
	 * @param schema
	 * @return
	 */
	public static List<Event> getAllBaseForSim(Long simid, String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Event> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Event where simId = :sim_id and runningSimId is null")
				.setString("sim_id", simid.toString())
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	/**
	 * Returns a list of all events created for a simulation.
	 * 
	 * @param simid
	 * @param schema
	 * @return
	 */
	public static List<Event> getAllForTimeLine(Long timelineid, String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Event> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Event where timelineId = :timelineId")
				.setString("timelineId", timelineid.toString())
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	/**
	 * Saves event to the database. 
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	public String getEventStartHour(){
		
		SimpleDateFormat hour_sdf = new SimpleDateFormat("HH");
		
		if (this.getEventStartTime() != null){
			return hour_sdf.format(this.getEventStartTime());
		} else {
			return "";
		}
		
	}
	
	public String getEventStartMinute(){
		
		SimpleDateFormat minute_sdf = new SimpleDateFormat("mm");
		
		if (this.getEventStartTime() != null){
			return minute_sdf.format(this.getEventStartTime());
		} else {
			return "";
		}
		
	}

	@Override
	public String getEventClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getEventId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getEventParentClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getEventParentId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long createRunningSimVersion(String schema, Long simId, Long rsId,
			Object templateObject) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
