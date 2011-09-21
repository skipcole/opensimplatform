package com.seachangesimulations.osp.gametime;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.sharing.ExportableObject;

/**
 * This class represents events in the life of a simulation.
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
 */
@Entity
@Proxy(lazy = false)
public class GameClockEvent implements ExportableObject{

	/** Database id of this object. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transitId;
	
	/** Simulation id. */
    private Long simId;
    
    /** Running simulation id. */
    private Long rsId;
    
    /** Time event occurred. */
    private java.util.Date eventDate = new java.util.Date();
    
    /** String explanation of event. */
    private String eventTitle = "";
    
	/**
	 * Pulls the game clock event out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param gcId
	 * @return
	 */
	public static GameClockEvent getById(String schema, Long gceId) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		GameClockEvent gce = (GameClockEvent) MultiSchemaHibernateUtil
				.getSession(schema).get(GameClockEvent.class, gceId);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return gce;
	}
	
	/** Saves object back to the database. */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}

	@Override
	public Long getTransitId() {
		return transitId;
	}

	@Override
	public void setTransitId(Long transitId) {
		this.transitId = transitId;	
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

	public Long getRsId() {
		return rsId;
	}

	public void setRsId(Long rsId) {
		this.rsId = rsId;
	}

	public java.util.Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(java.util.Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
	
	
}
