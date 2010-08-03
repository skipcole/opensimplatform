package org.usip.osp.baseobjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
public class TamperPermission implements ExportableObject{

    /** Unique id of this actor. */
	@Id 
	@GeneratedValue
    private Long id;
	
	/** Id of the simulation that this actor is 'associated' with. They may be associated, but not assigned
	 * to a simulation. */
	private Long sim_id;
	
	private Long running_sim_id;
	
	private Long objectId;
	
	private String objectClass = "";
	
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

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getObjectClass() {
		return objectClass;
	}

	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}

	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	@Override
	public Long getTransitId() {
		return transit_id;
	}

	@Override
	public void setTransitId(Long transit_id) {
		this.transit_id = transit_id;
	}
	
}
