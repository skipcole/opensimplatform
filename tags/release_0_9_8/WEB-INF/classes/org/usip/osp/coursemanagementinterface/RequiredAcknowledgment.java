package org.usip.osp.coursemanagementinterface;

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
public class RequiredAcknowledgment {

	
    /** Unique id. */
	@Id @GeneratedValue
    private Long id;
	
	@Lob
	private String acknowledgementText;
	
	/** Basically true/false if this is required or not. */
	private int requirementLevel;
	
	/** indicates what level of user (Admin, Author, Instructor or Player) is required to 
	 * acknowledge this. */
	private int userLevelRequired;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAcknowledgementText() {
		return acknowledgementText;
	}

	public void setAcknowledgementText(String acknowledgementText) {
		this.acknowledgementText = acknowledgementText;
	}

	public int getRequirementLevel() {
		return requirementLevel;
	}

	public void setRequirementLevel(int requirementLevel) {
		this.requirementLevel = requirementLevel;
	}

	public int getUserLevelRequired() {
		return userLevelRequired;
	}

	public void setUserLevelRequired(int userLevelRequired) {
		this.userLevelRequired = userLevelRequired;
	}

	
}
