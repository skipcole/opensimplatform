package com.seachangesimulations.osp.questions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Actor;
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
public class QuestionAndResponse implements ExportableObject{
	
    /** Unique id. */
	@Id @GeneratedValue
    private Long id;
	
	private Long simId;
	
	private Long transitId;
	
	private Long customSectionId;
	
	@Lob
	private String question = "";
	
	@Lob
	private String answer = "";

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

	public Long getCustomSectionId() {
		return customSectionId;
	}

	public void setCustomSectionId(Long customSectionId) {
		this.customSectionId = customSectionId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	

}
