package org.usip.osp.baseobjects;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.Proxy;

/**
 * Represents a set of simulations (such as a group run simultaneously in a class.
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
public class RunningSimSet {
	
	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	private String RunningSimSetName = "";
	
	/** Value of the user id that created this set. */
	private Long user_id;
	
	/** Name of the user that created this set. */
	private String username;

	@Column(name = "CREATION_DATE", columnDefinition = "datetime")
	@GeneratedValue
	private Date creationDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRunningSimSetName() {
		return RunningSimSetName;
	}

	public void setRunningSimSetName(String runningSimSetName) {
		RunningSimSetName = runningSimSetName;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
	
	
}
