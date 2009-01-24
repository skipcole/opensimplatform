package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents the assignment of an actor into a simulation.
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
@Table(name = "SIM_PHASE_ASSIGNMENT")
@Proxy(lazy = false)
public class SimPhaseAssignment {
	
	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Database id of this Simulation. */
	@Column(name = "SIM_ID")
	private Long sim_id;
	
    /** Unique id of this phase. */
    @Column(name = "PHASE_ID")
    private Long phase_id;
	
	public SimPhaseAssignment(){
		
	}
	
	public SimPhaseAssignment(String schema, Long sim_id, Long phase_id){
		this.sim_id = sim_id;
		this.phase_id = phase_id;
		if (SimPhaseAssignment.getMe(schema, sim_id, phase_id) == null){
			this.saveMe(schema);
		}
	}

	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getPhase_id() {
		return phase_id;
	}

	public void setPhase_id(Long phase_id) {
		this.phase_id = phase_id;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static SimPhaseAssignment getMe(String schema, Long sim_id, Long phase_id){
		
		String hqlQuery = "from SimPhaseAssignment where sim_id = " + sim_id + " AND phase_id = " + phase_id;
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List<SimPhaseAssignment> returnList = 
			MultiSchemaHibernateUtil.getSession(schema).createQuery(hqlQuery).list();
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		if ((returnList != null) && (returnList.size() > 0)){
			return (SimPhaseAssignment) returnList.get(0);
		} else {
			return null;
		}
		
	}
	
	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param phase_id
	 */
	public static void removeMe(String schema, Long sim_id, Long phase_id){
		
		SimPhaseAssignment spa = SimPhaseAssignment.getMe(schema, sim_id, phase_id);
		
		if (spa != null){
			MultiSchemaHibernateUtil.beginTransaction(schema);
			MultiSchemaHibernateUtil.getSession(schema).delete(spa);
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		}
	}
	
	/**
	 * 
	 * @param schema
	 */
	public void saveMe(String schema){
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	

	
	/**
	 * Returns the phases associated with a particular sim.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static List<SimulationPhase> getPhasesForSim(String schema, Long sim_id){
		
		List startList = getPhasesAssignmentsForSim(schema, sim_id);
		
		ArrayList returnList = new ArrayList();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		for (ListIterator<SimPhaseAssignment> li = startList.listIterator(); li.hasNext();) {
			SimPhaseAssignment this_saa = (SimPhaseAssignment) li.next();
			SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil.getSession(schema).get(SimulationPhase.class, this_saa.getPhase_id());
			returnList.add(sp);
		}
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
		
	}
	
	/**
	 * Gets the phase simulation assignments for a particular simulation.
	 * 
	 * @param simid
	 * @param schema
	 * @return
	 */
	public static List<SimPhaseAssignment> getPhasesAssignmentsForSim(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List<SimPhaseAssignment> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from SimPhaseAssignment where sim_id = " + sim_id).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}

}
