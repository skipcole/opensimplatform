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
import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.baseobjects.SimPhaseAssignment;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.networking.ObjectPackager;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

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
public class Event implements EventInterface{
	
	public static SimpleDateFormat similie_sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss z");

	
	/** Database id of this Simulation. */
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

	public static void main(String args[]){
		Event e = new Event();
		
		e.eventTitle = "a title";
		System.out.println(ObjectPackager.getObjectXML(e));
		//similie_sdf.setTimeZone(TimeZone.getDefault());
		
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
			
			returnString += packageEvent(thisEvent);
			
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
		
		
		String returnString = "<event start=\"" 
			+ similie_sdf.format(ei.getEventStartTime()) + "\" title=\"" + ei.getEventTitle() 
			+ "\">";
		
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

	/**
	 * Pulls the event out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static Event getMe(String schema, Long sim_id) {

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
		
		Event event = Event.getMe(schema, event_id);
		
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
	public static List<Event> getAllForSim(Long simid, Long phaseid, String schema) {

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
	 * Saves event to the database. 
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	/*
	  <event start="Nov 02 2009  07:00:00 GMT"
	         end="Nov 02 2009  09:00:00 GMT"
	         isDuration="true"
	         title="Writing Timeline documentation"
	         image="http://simile.mit.edu/images/csail-logo.gif">
	    A few days to write some documentation for 
	    &lt;a href="http://simile.mit.edu/timeline/"&gt;Timeline&lt;/a&gt;.
	  </event>
	  <event start="Oct 15 2009  00:00:00 GMT"
	         end="Oct 15 2009  00:00:00 GMT"
	         title="Friend's wedding">
	     I'm not sure precisely when my friend's wedding is.
	  </event>
	    <event start="Nov 02 2009 17:08:02 EST"
	         title="Text of an inject"
	         link="http://travel.yahoo.com/">
	To: Uzzdwaadi JRTF &lt;br/&gt; 
	From: IC Staff           
	Re: Letter from TRIBAL Leaders     

	We have received a letter from a group of 15 tribal leaders in the Uzzdwaadi region. The letter reads in part: "When the rest of our community is suffering deprivation and hardship, it is unacceptable that any resources should be given to those that left our community and created havoc in our country." It appears that the letter has also been released to the media.
	  </event>
	<event start="Nov 02 2009 17:08:02 EST" end="Nov 02 2009 17:08:02 EST" title="bicyle recycle  ...">
	bicyle recycle&lt;br&gt;
	</event>
	<event start="Nov 02 2009 17:09:54 EST" title="There is a new announcement: the city  ...">the city&lt;br&gt;</event>
*/
}
