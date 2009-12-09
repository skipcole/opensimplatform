package org.usip.osp.communications;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Actor;
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
public class TimeLine {
	
	/** If this timeline represents a plan of events to happen, it will be of this category. */
	public static final int CATEGORY_MASTERPLAN = 1;
	
	/** If this timeline represents what actually transpired, it will be of this category. */
	public static final int CATEGORY_ACTUAL_EVENTS = 2;

	/** Database id of this TimeLine. */
	@Id
	@GeneratedValue
	private Long id;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	private Long simId;
	private Long runningSimId;
	private Long phaseId;
	
	private int timeline_category;
	
	
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
	public static TimeLine getMe(String schema, Long timeline_id) {

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


}
