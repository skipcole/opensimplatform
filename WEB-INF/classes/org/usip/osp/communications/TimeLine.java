package org.usip.osp.communications;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Actor;
import org.usip.osp.baseobjects.SimSectionDependentObject;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This object represents a sequence of events that may happen or has happened in a simulation.
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
public class TimeLine  implements SimSectionDependentObject {
	
	/** If this timeline represents a plan of events to happen, it will be of this category. */
	public static final int CATEGORY_MASTERPLAN = 1;
	
	/** If this timeline represents what actually transpired, it will be of this category. */
	public static final int CATEGORY_ACTUAL_EVENTS = 2;
	
	/** If this timeline represents a plan, it will be of this category. */
	public static final int CATEGORY_PLAN = 3;

	/** Database id of this TimeLine. */
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private boolean adjustToRunningSimStartTime = false;
	
	
	public boolean isAdjustToRunningSimStartTime() {
		return adjustToRunningSimStartTime;
	}

	public void setAdjustToRunningSimStartTime(boolean adjustToRunningSimStartTime) {
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
	
	private Date timeline_start_date;
	
	
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
		TimeLine act = (TimeLine) MultiSchemaHibernateUtil
				.getSession(schema).get(TimeLine.class, timeline_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return act;

	}
	
    /**
     * Returns 'master plan' TimeLine.
     * TODO: This currently assumes only one master plan. That will change.
     * 
     * @param schema
     * @return
     */
    public static TimeLine getMasterPlan(String schema, String sim_id){
        
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from TimeLine where simId = :sim_id  and timeline_category = " + CATEGORY_MASTERPLAN)
				.setString("sim_id", sim_id)
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		TimeLine returnTimeLine = new TimeLine();
		
		if ((returnList == null) || (returnList.size() == 0)){
			returnTimeLine.setTimeline_category(CATEGORY_MASTERPLAN);
			returnTimeLine.setSimId(new Long(sim_id));
			returnTimeLine.saveMe(schema);
		} else {
			returnTimeLine = (TimeLine) returnList.get(0);
		}

		return returnTimeLine;
    }
    
    public static List getAllBaseForSimulation(String schema, Long sim_id){
        
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from TimeLine where simId = :sim_id and runningSimId is null")
				.setLong("sim_id", sim_id).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
    }
    
    /**
     * Returns all of the actors found in a schema for a particular simulation
     * 
     * @param schema
     * @return
     */
    public static List getAllForSimulation(String schema, Long sim_id){
        
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from TimeLine where simId = :sim_id ")
				.setLong("sim_id", sim_id).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
    }

	@Override
	public Long createRunningSimVersion(String schema, Long sim_id, Long rs_id, Object templateObject) {
		
		TimeLine templateTimeLine = (TimeLine) templateObject;

		// Pull it out clean from the database
		templateTimeLine = TimeLine.getById(schema, templateTimeLine.getId());
		
		TimeLine newTimeLine = new TimeLine();
		newTimeLine.setAdjustToRunningSimStartTime(templateTimeLine.isAdjustToRunningSimStartTime());
		newTimeLine.saveMe(schema);
		
		// to copy events.
		
		// TODO Auto-generated method stub
		
		return null;
	}

	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransit_id() {
		return this.transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}


}
