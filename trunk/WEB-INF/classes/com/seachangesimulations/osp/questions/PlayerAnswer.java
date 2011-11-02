package com.seachangesimulations.osp.questions;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.sharing.ExportableObject;

/*
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
@Entity
@Proxy(lazy=false)
public class PlayerAnswer  implements ExportableObject{
	
    /** Unique id. */
	@Id @GeneratedValue
    private Long id;
	
	private Long simId;
	
	private Long runningSimId;
	
	private Long questionId;
	
	private Long userId;
	
	private Long actorId;
	
	private Long transitId;
	
	@Lob
	private String playerAnswer = "";
	
	private boolean submitted = false;
	
	@Override
	public Long getTransitId() {
		return transitId;
	}

	@Override
	public void setTransitId(Long transit_id) {
		transitId = transit_id;
		
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
	
	/** Gets this Player answer by Id. */
	public static PlayerAnswer getById(String schema, Long pAid) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		PlayerAnswer pa = (PlayerAnswer) MultiSchemaHibernateUtil
				.getSession(schema).get(PlayerAnswer.class, pAid);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return pa;

	}
	
	public static PlayerAnswer getByQuestionRunningSimAndUserIds(String schema, Long qId, Long rsId, Long uId){
		
		if ((qId == null) || (rsId == null) || (uId == null)){
			return new PlayerAnswer();
		}
		
		String hQl = "from PlayerAnswer where questionId = :qId and runningSimId = :rsId and userId = :uId";
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List <PlayerAnswer> tempList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				hQl)
				.setLong("qId", qId)
				.setLong("rsId", rsId)
				.setLong("uId", uId)
				.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		if (tempList.size() == 0){
			return new PlayerAnswer();
		} else if (tempList.size() == 1){
			return (PlayerAnswer) tempList.get(0);
		} else {
			// TODO should not be more than one of these, need to flag that if it is found to be so.
			return new PlayerAnswer();
		}
		
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

	public Long getRunningSimId() {
		return runningSimId;
	}

	public void setRunningSimId(Long runningSimId) {
		this.runningSimId = runningSimId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getActorId() {
		return actorId;
	}

	public void setActorId(Long actorId) {
		this.actorId = actorId;
	}

	public String getPlayerAnswer() {
		return playerAnswer;
	}

	public void setPlayerAnswer(String playerAnswer) {
		this.playerAnswer = playerAnswer;
	}

	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}
	
}
