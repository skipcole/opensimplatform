package org.usip.osp.communications;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;


/**
 * This class represents a tip left by an author or instructor.
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
public class Tips {

	/** Database id of this Tip. */
	@Id
	@GeneratedValue
	private Long id;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Pulls the tips out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static Tips getMe(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Tips tips = (Tips) MultiSchemaHibernateUtil.getSession(schema).get(Tips.class, sim_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return tips;

	}
	
	@SuppressWarnings("unchecked")
	public static List<Tips> getChildren(Long parentId, String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Tips> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Tips where parentTipTextId = :sim_id ")
				.setString("sim_id", parentId.toString())
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	@SuppressWarnings("unchecked")
	public static List<Tips> getAllForSim(Long simid, String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Tips> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Tips where simId = :sim_id ")
				.setString("sim_id", simid.toString())
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	private Long parentTipTextId;
	private Long simId;
	private Long runningSimId;
	private Long actorId;
	private Long phaseId;
	
	private boolean isInstructorAdded;
	
	private String instructorsName;
	private String instructorsId;
	private String instructorsEmail;
	
	private String tipText;
	
	private Date tipLastEditDate;


	public Long getParentTipTextId() {
		return parentTipTextId;
	}

	public void setParentTipTextId(Long parentTipTextId) {
		this.parentTipTextId = parentTipTextId;
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

	public Long getActorId() {
		return actorId;
	}

	public void setActorId(Long actorId) {
		this.actorId = actorId;
	}

	public Long getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}

	public boolean isInstructorAdded() {
		return isInstructorAdded;
	}

	public void setInstructorAdded(boolean isInstructorAdded) {
		this.isInstructorAdded = isInstructorAdded;
	}

	public String getInstructorsName() {
		return instructorsName;
	}

	public void setInstructorsName(String instructorsName) {
		this.instructorsName = instructorsName;
	}

	public String getInstructorsId() {
		return instructorsId;
	}

	public void setInstructorsId(String instructorsId) {
		this.instructorsId = instructorsId;
	}

	public String getInstructorsEmail() {
		return instructorsEmail;
	}

	public void setInstructorsEmail(String instructorsEmail) {
		this.instructorsEmail = instructorsEmail;
	}

	public String getTipText() {
		return tipText;
	}

	public void setTipText(String tipText) {
		this.tipText = tipText;
	}

	public Date getTipLastEditDate() {
		return tipLastEditDate;
	}

	public void setTipLastEditDate(Date tipLastEditDate) {
		this.tipLastEditDate = tipLastEditDate;
	}
	
	
}
