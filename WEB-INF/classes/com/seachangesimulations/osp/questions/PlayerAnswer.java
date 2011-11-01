package com.seachangesimulations.osp.questions;

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
	
	public static QuestionAndResponse getById(String schema, Long q_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		QuestionAndResponse qAndR = (QuestionAndResponse) MultiSchemaHibernateUtil
				.getSession(schema).get(QuestionAndResponse.class, q_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return qAndR;

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
	
	

}
