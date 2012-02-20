package org.usip.osp.communications;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.TimeZone;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Actor;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.baseobjects.SimSectionDependentObject;
import org.usip.osp.baseobjects.SimulationPhase;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.baseobjects.core.SimilieTimelineCustomizer;
import org.usip.osp.networking.PlayerSessionObject;
import org.usip.osp.networking.SessionObjectBase;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This object represents a sequence of events that may happen or has happened
 * in a simulation.
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
@Entity
@Proxy(lazy = false)
public class TimeLine implements SimSectionDependentObject {

	/** Zero argument constructors */
	public TimeLine() {

	}

	/**
	 * If this timeline represents a plan of events to happen, it will be of
	 * this category.
	 */
	public static final int CATEGORY_MASTERPLAN = 1;

	/**
	 * If this timeline represents what actually transpired, it will be of this
	 * category.
	 */
	public static final int CATEGORY_ACTUAL_EVENTS = 2;

	/** If this timeline represents a plan, it will be of this category. */
	public static final int CATEGORY_PLAN = 3;

	/** Database id of this TimeLine. */
	@Id
	@GeneratedValue
	private Long id;

	private String name = "";

	private boolean adjustToRunningSimStartTime = false;

	public boolean isAdjustToRunningSimStartTime() {
		return adjustToRunningSimStartTime;
	}

	public void setAdjustToRunningSimStartTime(
			boolean adjustToRunningSimStartTime) {
		this.adjustToRunningSimStartTime = adjustToRunningSimStartTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Long simId;
	private Long runningSimId;
	private Long phaseId;

	private int timeline_category;

	private Date timeline_start_date = new Date();

	public Date getTimeline_start_date() {
		return timeline_start_date;
	}

	public void setTimeline_start_date(Date timeline_start_date) {
		this.timeline_start_date = timeline_start_date;
	}

	public int getTimeline_category() {
		return timeline_category;
	}

	public void setTimeline_category(int timeline_category) {
		this.timeline_category = timeline_category;
	}

	@Lob
	private String description = "";

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	 * Saves this object back to the database.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	/**
	 * Returns a particular Timeline.
	 * 
	 * @param schema
	 * @param Timeline_id
	 * @return
	 */
	public static TimeLine getById(String schema, Long timeline_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		TimeLine act = (TimeLine) MultiSchemaHibernateUtil.getSession(schema)
				.get(TimeLine.class, timeline_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return act;

	}

	/**
	 * Returns all of the base timelines for simulations.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static List getAllBaseForSimulation(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from TimeLine where simId = :sim_id and runningSimId is null")
				.setLong("sim_id", sim_id).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	@Override
	public Long createRunningSimVersion(String schema, Long sim_id, Long rs_id,
			Object templateObject) {

		TimeLine templateTimeLine = (TimeLine) templateObject;

		// Pull it out clean from the database
		templateTimeLine = TimeLine.getById(schema, templateTimeLine.getId());

		TimeLine newTimeLine = new TimeLine();
		newTimeLine.setAdjustToRunningSimStartTime(templateTimeLine
				.isAdjustToRunningSimStartTime());
		newTimeLine.saveMe(schema);

		// to copy events.

		// TODO Auto-generated method stub

		return null;
	}

	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransitId() {
		return this.transit_id;
	}

	public void setTransitId(Long transit_id) {
		this.transit_id = transit_id;
	}

	/**
	 * Packages an event in the format required by similie timeline.
	 * 
	 * @param a
	 * @return
	 */
	public static String packageTimelineInterfaceObject(TimeLineInterface ei) {

		String icon_name = "  icon=\""
				+ USIP_OSP_Properties.getValue("base_sim_url");

		if (ei.getEventType() == 3) {
			icon_name += "/third_party_libraries/timeline_2.3.0/timeline_js/images/red-circle.png\"";
		} else if (ei.getEventType() == 2) {
			icon_name += "/third_party_libraries/timeline_2.3.0/timeline_js/images/green-circle.png\"";
		} else {
			icon_name = "";
		}

		String returnString = "<event start=\""
				+ TimeLine.similie_sdf.format(ei.getEventStartTime())
				+ "\" title=\"" + ei.getEventTitle() + "\" " + icon_name + ">";

		returnString += USIP_OSP_Util.htmlToCode(ei.getEventMsgBody());

		returnString += "</event>";
		return returnString;
	}

	/**
	 * returns an XML string containing the packaged objects.
	 * 
	 * @param setOfEvents
	 * @return
	 */
	public static String packupArray(List<TimeLineInterface> setOfEvents) {

		String returnString = "";

		for (ListIterator<TimeLineInterface> li = setOfEvents.listIterator(); li
				.hasNext();) {
			TimeLineInterface thisEvent = li.next();

			returnString += TimeLine.packageTimelineInterfaceObject(thisEvent)
					+ USIP_OSP_Util.lineTerminator;

		}

		return returnString;
	}

	@Transient
	public String timelineURL = "";

	@Transient
	public String runStart = "";

	@Transient
	public int shortIntervalPixelDistance = 0;

	@Transient
	public int longIntervalPixelDistance = 0;

	public static SimpleDateFormat similie_sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss z");


	{
		TimeLine.similie_sdf.setTimeZone(TimeZone.getDefault());
	}

	public static TimeLine getTimeLineForPresenation(
			HttpServletRequest request, SessionObjectBase sob) {

		TimeLine returnTimeLine = new TimeLine();

		String cs_id = (String) request.getParameter("cs_id");

		String timeline_to_show = "";

		if (cs_id != null) {
			CustomizeableSection cs = CustomizeableSection.getById(sob.schema,
					cs_id);
			timeline_to_show = (String) cs.getContents().get(
					SimilieTimelineCustomizer.KEY_FOR_DISPLAY);

			if (timeline_to_show != null) {
				returnTimeLine = TimeLine.getById(sob.schema, new Long(
						timeline_to_show));
				returnTimeLine.runStart = TimeLine.similie_sdf.format(returnTimeLine
						.getTimeline_start_date());
			}
		}

		returnTimeLine.shortIntervalPixelDistance = 125;
		returnTimeLine.longIntervalPixelDistance = 250;

		returnTimeLine.timelineURL = "similie_timeline_server.jsp?timeline_to_show="
				+ timeline_to_show;

		return returnTimeLine;

	}

	/** Returns a timeline pointed in the right place to review the events that have happened in the current simulation
	 * 
	 * @param request
	 * @param sob
	 * @return
	 */
	public static TimeLine getReviewTimeLine(HttpServletRequest request,
			SessionObjectBase sob) {
		
		String rs_id = (String) request.getParameter("rs_id");
	
		String targetRSID = "";
		if ((rs_id != null) && (rs_id.length() > 0) && (!(rs_id.equalsIgnoreCase("null")))){
			targetRSID = "&rs_id=" + rs_id;
		}
		
		String timelineToGet = "actual" + targetRSID;
		
		String timeline_to_show = (String) request.getParameter("timeline_to_show");
		
		if ((timeline_to_show != null) && (timeline_to_show.equalsIgnoreCase("phases"))){
			timelineToGet = "phases";
		}
		

		TimeLine returnTimeLine = new TimeLine();

		returnTimeLine.runStart = TimeLine.similie_sdf.format(new java.util.Date());
		returnTimeLine.shortIntervalPixelDistance = 125;
		returnTimeLine.longIntervalPixelDistance = 250;
		returnTimeLine.timelineURL = "similie_timeline_server.jsp?timeline_to_show=" + timelineToGet;
		
		System.out.println("tlurl: " + returnTimeLine.timelineURL);

		return returnTimeLine;
	}
	
	/**
	 * Returns a timeline based on what was asked for.
	 * 
	 * @param request
	 * @param response
	 * @param pso
	 * @return
	 */
	public static String serveUpTimeLine(HttpServletRequest request, HttpServletResponse response,
			PlayerSessionObject pso) {
		
		System.out.println("serveUpTimeLine");
		
		String timeline_to_show = (String) request.getParameter("timeline_to_show");
		String rs_id = (String) request.getParameter("rs_id");

		Long RsIdOfTimeLineToShow = pso.getRunningSimId();
		
		if ((rs_id != null) && (rs_id.length() > 0) && (!(rs_id.equalsIgnoreCase("null")))){
			RsIdOfTimeLineToShow = new Long(rs_id);
		}
		
		String textToShow = "";
		
		if ((timeline_to_show != null) && (timeline_to_show.equalsIgnoreCase("actual"))){
			textToShow = PlayerSessionObject.getInjectFiredForTimeline(pso.schema, RsIdOfTimeLineToShow);
		} else if ((timeline_to_show != null) && (timeline_to_show.equalsIgnoreCase("phases"))){
			textToShow = getTimelineOfPhaseChanges(pso.schema, RsIdOfTimeLineToShow);
		} else if (timeline_to_show != null){
			textToShow = PlayerSessionObject.getEventsForTimeline(pso.schema, new Long(timeline_to_show));
		} 
		
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		
		return textToShow;
	}
	
	/**
	 * Returns information for a timeline showing all of the phase changes. 
	 * 
	 * @param schema
	 * @param rs_id
	 * @return
	 */
	public static String getTimelineOfPhaseChanges(String schema, Long rs_id) {

		if (rs_id == null) {
			return "";
		}

		return TimeLine.packupArray(getPhaseChangesForRunningSim(
				schema, rs_id));
	}
	
	/**
	 * Returns the phase change alerts in a form to present a timeline.
	 * 
	 * @param schema
	 * @param running_sim_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<TimeLineInterface> getPhaseChangesForRunningSim(String schema, Long running_sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<TimeLineInterface> returnList = 
			MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Alert where running_sim_id = :running_sim_id and type in ('1', '5')")
				.setLong("running_sim_id", running_sim_id)		
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	
	/**
	 * Takes a list of dates, finds the earliest and the latest and then 
	 * returns the date right in between them.
	 * 
	 * Note, we could (and maybe should) just average all of the times. But I don't know if
	 * adding up a long series of 'longs' might cause it to overflow. This method will
	 * assure an answer, but maybe not the most optimal one.
	 * 
	 * @param theDates
	 * @return
	 */
	public static Date averageDate(List<Date> theDates){
		
		if ((theDates == null) || (theDates.size() == 0)){
			return new Date();
		}
		
		Date earliestDate = null;
		Date latestDate = null;
		
		for (ListIterator li = theDates.listIterator(); li.hasNext();) {
			Date thisDate = (Date) li.next();
			
			if (earliestDate == null){
				earliestDate = thisDate;
			}
			
			if (latestDate == null){
				latestDate = thisDate;
			}
			
			if (thisDate.before(earliestDate)){
				earliestDate = thisDate;
			}
			
			if  (latestDate.after(thisDate)){
				latestDate = thisDate;
			}
		}
		
		long averageOfExtremes = (earliestDate.getTime() + latestDate.getTime() ) /2;
		
		return new Date(averageOfExtremes);
	}
	
	public static void main(String args[]){
		
		java.util.Date now = new java.util.Date();
	
		long thenLong = now.getTime() + (1000 * 60 * 60 * 6);
		
		java.util.Date then = new Date(thenLong);
		
		long halfWay = (now.getTime() + then.getTime()) / 2;
		
		java.util.Date half = new Date(halfWay);
		
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy HH:mm a");
		
		
	}
	
	/**
	 * Handles the creation of timeline events.
	 * 
	 * @param request
	 * @return
	 */
	public static Event handleAddTimeLineEvents(HttpServletRequest request,
			Long timeLineId, SessionObjectBase sob) {

		Event event = new Event();

		if (timeLineId == null) {
			return event;
		}

		TimeLine timeline = TimeLine.getById(sob.schema, timeLineId);

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("timeline_creator"))) {

			String command = (String) request.getParameter("command");

			if (command.equalsIgnoreCase("Update")) {
				String event_id = (String) request.getParameter("event_id");

				event.setId(new Long(event_id));
				sob.draft_event_id = event.getId();

			}

			if (command.equalsIgnoreCase("Clear")) {
				sob.draft_event_id = null;
			} else { // coming here as update or as create.

				String event_type = (String) request.getParameter("event_type");

				int eventTypeInt = 1;

				try {
					eventTypeInt = new Long(event_type).intValue();
				} catch (Exception e) {
					e.printStackTrace();
				}

				event.setEventType(eventTypeInt);
				
				String eventTitle = (String) request.getParameter("event_title");
				
				if (!(USIP_OSP_Util.stringFieldHasValue(eventTitle))){
					eventTitle = "No Title Provided";
				}
				
				event.setEventTitle(eventTitle);
				event.setEventMsgBody((String) request
						.getParameter("event_text"));

				String timeline_event_date = (String) request
						.getParameter("timeline_event_date");

				System.out.println(timeline_event_date);

				SimpleDateFormat sdf_startdate = new SimpleDateFormat(
						"MM/dd/yyyy HH:mm");

				// set this to a safe date (now) in case the date entered does not parse well.
				event.setEventStartTime(new java.util.Date());
				try {
					Date ted = sdf_startdate.parse(timeline_event_date);
					event.setEventStartTime(ted);

				} catch (Exception e) {
					sob.errorMsg = "The date and time that you entered, \"" + timeline_event_date + "\", could not be interpreted. " + 
						"The date was set to your current time.";
				}
				event.setSimId(sob.sim_id);

				event.setPhaseId(sob.phase_id);

				// //////////////////////////////////////////
				event.setTimelineId(timeline.getId());

				event.saveMe(sob.schema);
			}

		}

		String remove_event = (String) request.getParameter("remove_event");
		String edit_event = (String) request.getParameter("edit_event");

		String event_id = (String) request.getParameter("event_id");

		if ((remove_event != null) && (remove_event.equalsIgnoreCase("true"))) {
			Event.removeMe(sob.schema, new Long(event_id));
			sob.draft_event_id = null;
		}

		if ((edit_event != null) && (edit_event.equalsIgnoreCase("true"))) {
			event = Event.getById(sob.schema, new Long(event_id));
			sob.draft_event_id = event.getId();
		}

		return event;
	}
	

	@Override
	public boolean runningSimulationSetLinkedObject() {
		// TODO Auto-generated method stub
		return false;
	}
}
