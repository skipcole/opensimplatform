package org.usip.osp.coursemanagementinterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.baseobjects.SimulationPhase;
import org.usip.osp.baseobjects.User;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

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
	
	/** Zero argument constructor required by hibernate. */
	public InstructorRunningSimAssignments(){
		
	}
	
	/**
	 * Main creation method.
	 * @param schema
	 * @param rs_id
	 * @param i_id
	 */
	public InstructorRunningSimAssignments(String schema, Long rs_id, Long i_id){
		
		this.runnignSimulationId = rs_id;
		this.instructorId = i_id;
		
		this.saveMe(schema);
	}

	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	private Long instructorId;
	
	@Column(name = "RS_ID")
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
	
	/** Saves an assignment. */
	public void saveMe(String schema) {
			
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	/**
	 * Returns a list of all running sims created for a simulation.
	 * 
	 * @param simid
	 * @param schema
	 * @return
	 */
	public static List<User> getInstructorsForSim(Long rs_id,
			String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<InstructorRunningSimAssignments> tempList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery("from InstructorRunningSimAssignments where rs_id = :rs_id")
				.setLong("rs_id", rs_id).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		ArrayList<User> returnList = new ArrayList<User>();
		
		for (ListIterator li = tempList.listIterator(); li.hasNext();) {
		
			InstructorRunningSimAssignments irsa = (InstructorRunningSimAssignments) li.next();
			if (irsa.getInstructorId() != null){
				User user = User.getById(schema, rs_id);
				returnList.add(user);
			}
		}
		
		return returnList;
	}
	
	/**
	 * Checks to see if the instructor whose id has been passed in is an instructor for the 
	 * running sim (whose id was also passed in)
	 * 
	 * @param i_id
	 * @param schema
	 * @param rs_id
	 * @return
	 */
	public static boolean checkIsInstructor(Long i_id, String schema, Long rs_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<InstructorRunningSimAssignments> tempList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery("from InstructorRunningSimAssignments where rs_id = :rs_id and instructorId = :i_id")
				.setLong("rs_id", rs_id)
				.setLong("i_id", i_id)
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		if ((tempList != null) && (tempList.size() > 0)){
			return true;
		} else {
			return false;
		}
	}
	
}
