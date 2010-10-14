package org.usip.osp.communications;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.sharing.*;

/**
 * This class represents a piece of information (an inject) thrust upon the players.
 */
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
@Table(name = "INJECTS")
@Proxy(lazy=false)
public class Inject implements CreatesRespondableObjects{

    /** Database id of this Inject. */
	@Id
	@GeneratedValue
	@Column(name = "INJECT_ID")
    private Long id;
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransit_id() {
		return this.transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}
	
	/** Simulation id. */
    @Column(name = "SIM_ID")
    private Long sim_id;
    
	/** Inject Group id. */
    @Column(name = "GROUP_ID")
    private Long group_id;
	
    /** Name of this inject. */
    @Column(name = "inject_name")
	private String injectName = ""; //$NON-NLS-1$
	
	/** Name of this group of injects. */
	@Lob
	private String inject_text = ""; //$NON-NLS-1$
	
	/** Notes regarding this injects. */
	@Lob
	private String inject_notes = ""; //$NON-NLS-1$

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInject_text() {
		return this.inject_text;
	}

	public void setInject_text(String inject_text) {
		this.inject_text = inject_text;
	}

	public String getInject_Notes() {
		return this.inject_notes;
	}

	public void setInject_Notes(String inject_notes) {
		this.inject_notes = inject_notes;
	}
	
	public Long getSim_id() {
		return this.sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getGroup_id() {
		return this.group_id;
	}

	public void setGroup_id(Long group_id) {
		this.group_id = group_id;
	}

	public String getInject_name() {
		return this.injectName;
	}

	public void setInject_name(String inject_name) {
		this.injectName = inject_name;
	}
	
	public static Inject getById(String schema, Long id) {
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		Inject inj = (Inject)
		MultiSchemaHibernateUtil.getSession(schema).get(Inject.class, id);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return inj;
	}

	public void saveMe(String schema) {
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
	}
	
	public static List getAllForSimAndGroup(String schema, Long sim_id, Long group_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = 
			MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Inject where SIM_ID = " + sim_id +  //$NON-NLS-1$
				" and GROUP_ID = " + group_id).list(); //$NON-NLS-1$
		

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	@Lob
	private String learningObjectives = ""; //$NON-NLS-1$
	
	@Lob
	private String typicalQuestionsAndResponses = ""; //$NON-NLS-1$

	public String getLearningObjectives() {
		return learningObjectives;
	}

	public void setLearningObjectives(String learningObjectives) {
		this.learningObjectives = learningObjectives;
	}

	public String getTypicalQuestionsAndResponses() {
		return typicalQuestionsAndResponses;
	}

	public void setTypicalQuestionsAndResponses(String typicalQuestionsAndResponses) {
		this.typicalQuestionsAndResponses = typicalQuestionsAndResponses;
	}

	@Override
	public void createRespondableObject(String schema, Long simId, Long rsId, Long phase_id, 
			Long actor_id, String userName, String userDisplayName) {
		
		RespondableObject ro = new RespondableObject();
		
		ro.setSimId(simId);
		ro.setRsId(rsId);
		ro.setObjectId(this.getId());
		ro.setClassName(Inject.class.toString().replaceFirst("class ", ""));
		
		ro.setCreatingActorId(actor_id);
		ro.setPhaseId(phase_id);
		
		ro.setResponseObjectSynopsis(this.getInject_text());
		
		ro.saveMe(schema);
		
	}
	
	
}
