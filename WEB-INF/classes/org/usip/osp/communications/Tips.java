package org.usip.osp.communications;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.SimSectionDependentObject;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.sharing.ImportedExperienceObject;


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
public class Tips implements SimSectionDependentObject, ImportedExperienceObject{

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
	public static Tips getById(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Tips tips = (Tips) MultiSchemaHibernateUtil.getSession(schema).get(Tips.class, sim_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return tips;

	}
	
	@SuppressWarnings("unchecked")
	public static List<Tips> getChildren(Long parentId, String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Tips> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Tips where parentTipId = :parentId ")
				.setString("parentId", parentId.toString())
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
	
	@SuppressWarnings("unchecked")
	public static List<Tips> getAllForBaseSim(Long simid, String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Tips> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Tips where simId = :sim_id and parentTipId is null")
				.setString("sim_id", simid.toString())
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	@SuppressWarnings("unchecked")
	public static Tips getBySimActorPhaseSection
		(Long s_id, Long a_id, Long p_id, Long cs_id, String schema, boolean justBase) {

		if (cs_id == null) {
			return new Tips();
		}
		
		String getJustBaseTip = "";
		if (justBase){
			getJustBaseTip = " and baseTip = '1'";
		}
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Tips> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Tips where simId = :s_id and actorId = :a_id and phaseId = :p_id and csId = :cs_id" + getJustBaseTip)
				.setLong("s_id", s_id)
				.setLong("a_id", a_id)
				.setLong("p_id", p_id)
				.setLong("cs_id", cs_id)
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		if ((returnList == null) || (returnList.size() == 0)){
			return new Tips();
		} else {
			return returnList.get(0);
		}

	}
	
	private Long parentTipId;
	private Long simId;
	private Long runningSimId;
	private Long actorId;
	private Long phaseId;
	private Long csId;	
	
	private Long userId;
	private String userName;
	private String userEmail;
	
	private boolean baseTip = true;

	private boolean isInstructorAdded = false;
	private boolean isShared = false;
	
	private boolean isReconciled = false;

	private String instructorsName;
	private String instructorsId;
	private String instructorsEmail;
	
	private String tipName;
	
	private String tipText = "";
	
	private Date tipLastEditDate;
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransitId() {
		return transit_id;
	}

	public void setTransitId(Long transitId) {
		transit_id = transitId;
	}

	public Long getParentTipId() {
		return parentTipId;
	}

	public void setParentTipId(Long parentTipId) {
		this.parentTipId = parentTipId;
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

	public Long getCsId() {
		return csId;
	}

	public void setCsId(Long csId) {
		this.csId = csId;
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

	public boolean isBaseTip() {
		return baseTip;
	}

	public void setBaseTip(boolean baseTip) {
		this.baseTip = baseTip;
	}
	
	public boolean isReconciled() {
		return isReconciled;
	}

	public void setReconciled(boolean isReconciled) {
		this.isReconciled = isReconciled;
	}

	public boolean isShared() {
		return isShared;
	}

	public void setShared(boolean isShared) {
		this.isShared = isShared;
	}

	public String getTipName() {
		return tipName;
	}

	public void setTipName(String tipName) {
		this.tipName = tipName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Override
	public Long createRunningSimVersion(String schema, Long simId, Long rsId,
			Object templateObject) {

		// Tips are handled differently since one base tip is used in all running sims.
		return this.getId();
		
	}

	private boolean importedRecord = false;
	
	public boolean isImportedRecord() {
		return importedRecord;
	}

	public void setImportedRecord(boolean importedRecord) {
		this.importedRecord = importedRecord;
		
	}
	

	@Override
	public boolean runningSimulationSetLinkedObject() {
		// TODO Auto-generated method stub
		return false;
	}
}
