package org.usip.osp.communications;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents a communication, which can be of many different types, sent to
 * a player or players.
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
public class CommunicationsHub {

	/** Database id of this CommunicationsHub item. */
	@Id
	@GeneratedValue
	private Long id;
	
	private Long sim_id;
	
	private Long running_sim_id;
	
	private Long msgId;
	
	private Class msgClass;
	
	private Date timeStamp;
	
	/**
	 * Zero entry constructor required by hibernate.
	 */
	public CommunicationsHub(){
		
	}
	
	public CommunicationsHub(Alert alert, String schema){
		
		this.setMsgClass(Alert.class);
		this.setMsgId(alert.getId());
		this.setRunning_sim_id(alert.getRunning_sim_id());
		this.setSim_id(alert.getSim_id());
		this.setTimeStamp(alert.getTimeOfAlert());
		
		this.saveMe(schema);
	}
	
	/** Saves a hub notice. */
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	public static void main(String args[]){
		System.out.println("hello world");
		
		CommunicationsHub ch = new CommunicationsHub();
		ch.setTimeStamp(new java.util.Date());
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss z");
		
		System.out.println(sdf.format(ch.getTimeStamp()));
		
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getRunning_sim_id() {
		return running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public Class getMsgClass() {
		return msgClass;
	}

	public void setMsgClass(Class msgClass) {
		this.msgClass = msgClass;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
}
