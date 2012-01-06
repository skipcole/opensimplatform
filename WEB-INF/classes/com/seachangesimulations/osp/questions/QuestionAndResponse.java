package com.seachangesimulations.osp.questions;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.*;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.sharing.CollectUpForPackaging;
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
public class QuestionAndResponse implements ExportableObject, SimSectionDependentObject, CollectUpForPackaging{
	
	/** Zero element constructor required by Hibernate. */
	public QuestionAndResponse() {
		
	}
	
	public QuestionAndResponse(String schema, Long simId, String qid, String q, String a) {
		
		this.simId = simId;
		this.questionIdentifier = qid;
		this.question = q;
		this.answer = a;
		
		this.saveMe(schema);
		
	}
	
    /** Unique id. */
	@Id @GeneratedValue
    private Long id;
	
	private Long simId;
	
	private Long transitId;
	
	/** Question id for author (Q1, Q2, etc.) */
	private String questionIdentifier = "";
	
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
	
	/** Returns all of the questionAndResponse objects for a section for a sim. */
    public static List <QuestionAndResponse> getAllForSimAndCustomSection(String schema, Long simId, Long csId){
        
    	if ((simId == null) || (csId == null)){
    		return new ArrayList();
    	}
    	
    	List <BaseSimSectionDepObjectAssignment> bsdoa = 
    		BaseSimSectionDepObjectAssignment.getObjectsForSection(schema, csId);
    	
    	ArrayList <QuestionAndResponse> returnList = new ArrayList();
    	
		for (ListIterator li = bsdoa.listIterator(); li.hasNext();) {
			BaseSimSectionDepObjectAssignment this_bsdoa = (BaseSimSectionDepObjectAssignment) li.next();
			
			if (this_bsdoa.getClassName().contains("QuestionAndResponse")){
				QuestionAndResponse qar = QuestionAndResponse.getById(schema, this_bsdoa.getObjectId());
			
				returnList.add(qar);
			}
				
		}

		return returnList;
    }
    
    /** Cleans out all of the questions for this custom section in this sim. */
    public static void deleteAllForSimAndCustomSection(String schema, Long simId, Long csId){
    	
    	List deleteList = getAllForSimAndCustomSection(schema, simId, csId);
    	
    	MultiSchemaHibernateUtil.beginTransaction(schema);
		for (ListIterator li = deleteList.listIterator(); li.hasNext();) {
			QuestionAndResponse qAndR = (QuestionAndResponse) li.next();
			MultiSchemaHibernateUtil.getSession(schema).delete(qAndR);
		}
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
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

	public String getQuestionIdentifier() {
		return questionIdentifier;
	}

	public void setQuestionIdentifier(String questionIdentifier) {
		this.questionIdentifier = questionIdentifier;
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

	public static List<QuestionAndResponse> getAllForSim(String schema, Long simId) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<QuestionAndResponse> returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery("from QuestionAndResponse where simId = :simId")
				.setLong("simId", simId).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	@Override
	public Long createRunningSimVersion(String schema, Long sim_id, Long rs_id,
			Object templateObject) {
		
		return this.getId();
	}

	@Override
	public List getAllForSimulation(String schema, Long simId) {
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from QuestionAndResponse where simId = :simId")
				.setLong("simId", simId).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

}
