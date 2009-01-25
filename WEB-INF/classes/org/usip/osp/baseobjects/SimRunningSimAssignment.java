package org.usip.osp.baseobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents the assignment of an runningsim into a simulation.
 * 
 * @author Ronald "Skip" Cole<br />
 * 
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
@Table(name = "SIM_RUNNINGSIM_ASSIGNMENT")
@Proxy(lazy = false)
public class SimRunningSimAssignment {

	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Database id of this Simulation. */
	@Column(name = "SIM_ID")
	private Long sim_id;
	
    /** Unique id of this runningsim. */
    @Column(name = "RUNNINGSIM_ID")
    private Long runningsim_id;
	
	public SimRunningSimAssignment(){
		
	}
	
	public SimRunningSimAssignment(String schema, Long sim_id, Long runningsim_id){
		this.sim_id = sim_id;
		this.runningsim_id = runningsim_id;
		if (SimRunningSimAssignment.getMe(schema, sim_id, runningsim_id) == null){
			this.saveMe(schema);
		}
	}

	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getRunningSimulation_id() {
		return runningsim_id;
	}

	public void setRunningSimulation_id(Long runningsim_id) {
		this.runningsim_id = runningsim_id;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static SimRunningSimAssignment getMe(String schema, Long sim_id, Long runningsim_id){
		
		String hqlQuery = "from SimRunningSimAssignment where sim_id = " + sim_id + " AND runningsim_id = " + runningsim_id;
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List<SimRunningSimAssignment> returnList = 
			MultiSchemaHibernateUtil.getSession(schema).createQuery(hqlQuery).list();
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		if ((returnList != null) && (returnList.size() > 0)){
			return (SimRunningSimAssignment) returnList.get(0);
		} else {
			return null;
		}
		
	}
	
	public static void removeMe(String schema, Long sim_id, Long runningsim_id){
		
		SimRunningSimAssignment saa = SimRunningSimAssignment.getMe(schema, sim_id, runningsim_id);
		
		if (saa != null){
			MultiSchemaHibernateUtil.beginTransaction(schema);
			MultiSchemaHibernateUtil.getSession(schema).delete(saa);
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		}
	}
	
	public void saveMe(String schema){
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	
	/**
	 * Returns the runningsims associated with a particular sim.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static List<RunningSimulation> getRunningSimulationsForSim(String schema, Long sim_id){
		
		List startList = getRunningSimulationsAssignmentsForSim(schema, sim_id);
		
		ArrayList returnList = new ArrayList();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		for (ListIterator<SimRunningSimAssignment> li = startList.listIterator(); li.hasNext();) {
			SimRunningSimAssignment this_saa = (SimRunningSimAssignment) li.next();
			RunningSimulation act = (RunningSimulation) MultiSchemaHibernateUtil.getSession(schema).get(RunningSimulation.class, this_saa.getRunningSimulation_id());
			returnList.add(act);
		}
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
		
	}
	
	/**
	 * Gets the runningsim simulation assignments for a particular simulation.
	 * 
	 * @param simid
	 * @param schema
	 * @return
	 */
	public static List<SimRunningSimAssignment> getRunningSimulationsAssignmentsForSim(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List<SimRunningSimAssignment> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from SimRunningSimAssignment where sim_id = " + sim_id).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
	
}
