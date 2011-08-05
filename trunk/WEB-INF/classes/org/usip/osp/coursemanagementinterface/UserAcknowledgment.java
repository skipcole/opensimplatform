package org.usip.osp.coursemanagementinterface;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;

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
public class UserAcknowledgment {

	
    /** Unique id. */
	@Id @GeneratedValue
    private Long id;
	
	private Long raId;
	
	private Long userId;
	
	private String userEmail;
	
	private Date acknowledgementDate;
	
	/** Null if the system cannot send out emails. */
	private Date acknowledementSentDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRaId() {
		return raId;
	}

	public void setRaId(Long raId) {
		this.raId = raId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Date getAcknowledgementDate() {
		return acknowledgementDate;
	}

	public void setAcknowledgementDate(Date acknowledgementDate) {
		this.acknowledgementDate = acknowledgementDate;
	}

	public Date getAcknowledementSentDate() {
		return acknowledementSentDate;
	}

	public void setAcknowledementSentDate(Date acknowledementSentDate) {
		this.acknowledementSentDate = acknowledementSentDate;
	}
	
	
	
	
}
