package org.usip.osp.coursemanagementinterface;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/*         This file is part of the USIP Open Simulation Platform.<br>
 * 
 *         The USIP Open Simulation Platform is free software; you can
 *         redistribute it and/or modify it under the terms of the new BSD Style
 *         license associated with this distribution.<br>
 * 
 *         The USIP Open Simulation Platform is distributed WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. <BR>
 * 
 * 
 */

@Entity
@Proxy(lazy = false)
public class InstructorRunningSimAssignments {

	/** Database id of this User. */
	@Id
	@GeneratedValue
	private Long id;
	
	private Long instructorId;
	
	private Long runnignSimulationId;
	
	private String assignmentDescription = "";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(Long instructorId) {
		this.instructorId = instructorId;
	}

	public Long getRunnignSimulationId() {
		return runnignSimulationId;
	}

	public void setRunnignSimulationId(Long runnignSimulationId) {
		this.runnignSimulationId = runnignSimulationId;
	}

	public String getAssignmentDescription() {
		return assignmentDescription;
	}

	public void setAssignmentDescription(String assignmentDescription) {
		this.assignmentDescription = assignmentDescription;
	}
	
	
}
