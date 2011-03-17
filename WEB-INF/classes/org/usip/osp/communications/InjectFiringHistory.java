package org.usip.osp.communications;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.sharing.ImportedExperienceObject;

/**
 * History of which injects have been fired, to whom and to when.
 *
 */
/* 
*         This file is part of the USIP Open Simulation Platform.<br>
* 
*         The USIP Open Simulation Platform is free software; you can
*         redistribute it and/or modify it under the terms of the new BSD Style
*         license associated with this distribution.<br>
* 
*         The USIP Open Simulation Platform is distributed WITHOUT ANY
*         WARRANTY; without even the implied warranty of MERCHANTABILITY or
*         FITNESS FOR A PARTICULAR PURPOSE. <BR>
* 
*/
@Entity
@Proxy(lazy = false)
public class InjectFiringHistory implements TimeLineInterface, ImportedExperienceObject {

	public static final int FIRED_TO_ALL = 1;
	
	public static final int FIRED_TO_SOME = 2;
	
	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Running Sim in which this inject was fired. */
	private Long running_sim_id;
	
	/** Id of the actor that fired this inject. */
	private Long actor_id;
	
	/** Id of the inject that has been fired. */
	private Long injectId;
	
	private String targets = "";
	
	/** comma separated list of actors ids this was fired to. */
	private String actorIdsFiredTo;
	
	/** comma separated list of actor Names this was fired to. */
	private String actorNamessFiredTo;
	
	private boolean modifiedByInstructor = false;
	
	public boolean isModifiedByInstructor() {
		return modifiedByInstructor;
	}

	public void setModifiedByInstructor(boolean modifiedByInstructor) {
		this.modifiedByInstructor = modifiedByInstructor;
	}
	
	@Lob
	private String actualFiredText = "";

	public String getActualFiredText() {
		return actualFiredText;
	}

	public void setActualFiredText(String actualFiredText) {
		this.actualFiredText = actualFiredText;
	}

    @Column(name = "inject_name")
	private String injectName = ""; //$NON-NLS-1$
    
	public String getInjectName() {
		return injectName;
	}

	public void setInjectName(String injectName) {
		this.injectName = injectName;
	}

	public InjectFiringHistory(){
		
	}
	
	/**
	 * Records an inject's firing history to the database.
	 * 
	 * @param running_sim_id
	 * @param actor_id
	 * @param injectId
	 * @param targets
	 * @param iText
	 * @param iActors
	 * @param schema
	 */
	public InjectFiringHistory(Long running_sim_id, Long actor_id, Long injectId, 
			String targets, String iTitle, String iText, String iActors, String schema){
		
		this.running_sim_id = running_sim_id;
		this.actor_id = actor_id;
		this.setTargets(targets);
		this.setInjectName(iTitle);
		this.setInjectId(injectId);
		this.setActualFiredText(iText);
		this.setActorIdsFiredTo(iActors);
		this.setFiredDate(new java.util.Date());
		
		this.saveMe(schema);
		
	}
	
	@Column(name = "FIRED_DATE", columnDefinition = "datetime")
	@GeneratedValue
	private Date firedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRunning_sim_id() {
		return running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public Long getActor_id() {
		return actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}
	
	public Long getInjectId() {
		return injectId;
	}

	public void setInjectId(Long injectId) {
		this.injectId = injectId;
	}

	public String getTargets() {
		return targets;
	}

	public void setTargets(String targets) {
		this.targets = targets;
	}

	public String getActorIdsFiredTo() {
		return actorIdsFiredTo;
	}

	public void setActorIdsFiredTo(String actorIdsFiredTo) {
		this.actorIdsFiredTo = actorIdsFiredTo;
	}

	public String getActorNamessFiredTo() {
		return actorNamessFiredTo;
	}

	public void setActorNamessFiredTo(String actorNamessFiredTo) {
		this.actorNamessFiredTo = actorNamessFiredTo;
	}

	public Date getFiredDate() {
		return firedDate;
	}

	public void setFiredDate(Date firedDate) {
		this.firedDate = firedDate;
	}
	
	/**
	 * Returns all of the alerts for this running simulation.
	 * 
	 * @param schema
	 * @param running_sim_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<TimeLineInterface> getAllForRunningSim(String schema, Long running_sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<TimeLineInterface> returnList = 
			MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from InjectFiringHistory where running_sim_id = :running_sim_id")
				.setLong("running_sim_id", running_sim_id)		
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	
	public static List<InjectFiringHistory> getAllForInjectAndRunningSim(String schema, Long running_sim_id, Long injectId) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<InjectFiringHistory> returnList = 
			MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from InjectFiringHistory where running_sim_id = :running_sim_id and injectId = :injectId")
				.setLong("running_sim_id", running_sim_id)		
				.setLong("injectId", injectId)
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	/**
	 * Returns all of the injects fired for a particular running sim/actor combo.
	 * @param schema
	 * @param running_sim_id
	 * @param actor_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<InjectFiringHistory> getAllForRunningSimAndActor
		(String schema, Long running_sim_id, Long actor_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<InjectFiringHistory> returnList = 
			MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from InjectFiringHistory where running_sim_id = :running_sim_id and actor_id = :actor_id")
				.setLong("running_sim_id", running_sim_id)
				.setLong("actor_id", actor_id)
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	/** Saves to database. */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}

	@Override
	public String getEventClass() {
		return this.getClass().toString();
	}

	@Override
	public Date getEventEndTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getEventId() {
		return this.getInjectId();
	}

	@Override
	public String getEventMsgBody() {
		return actualFiredText;
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
	public Date getEventStartTime() {
		return firedDate;
	}

	@Override
	public String getEventTitle() {
		return this.injectName;
	}

	@Override
	public int getEventType() {
		// TODO Auto-generated method stub
		return 0;
	}


	private boolean importedRecord = false;
	
	public boolean isImportedRecord() {
		return importedRecord;
	}

	public void setImportedRecord(boolean importedRecord) {
		this.importedRecord = importedRecord;
		
	}
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public void setTransitId(Long transitId) {
		transit_id = transitId;
		
	}
	
	public Long getTransitId() {
		return transit_id;
	}
	
}
