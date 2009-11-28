package org.usip.osp.baseobjects;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents a meta phase of a simulation.
 *
 */
/*
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can
 * redistribute it and/or modify it under the terms of the new BSD Style license
 * associated with this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. <BR>
 * 
 */
@Entity
@Proxy(lazy=false)
public class SimulationMetaPhase {

	/** Database id of this Phase. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Image associated with this meta phase.  */
    private String phaseImageFilename = ""; //$NON-NLS-1$
    
    /** Color associated with this phase. */
    private String phaseColor = "FFFFFF"; //$NON-NLS-1$
    
    private String metaPhaseName = "";

	public String getMetaPhaseName() {
		return metaPhaseName;
	}

	public void setMetaPhaseName(String metaPhaseName) {
		this.metaPhaseName = metaPhaseName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/** Id of the simulation that this meta phase belongs in. */
	private Long sim_id;
	
	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransit_id() {
		return this.transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}

	public String getPhaseImageFilename() {
		return phaseImageFilename;
	}

	public void setPhaseImageFilename(String phaseImageFilename) {
		this.phaseImageFilename = phaseImageFilename;
	}

	public String getPhaseColor() {
		return phaseColor;
	}

	public void setPhaseColor(String phaseColor) {
		this.phaseColor = phaseColor;
	}

	/** Saves a meta phase. */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static SimulationMetaPhase getMe(String schema, Long smp_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		SimulationMetaPhase this_smp = (SimulationMetaPhase) MultiSchemaHibernateUtil.getSession(schema).get(
				SimulationMetaPhase.class, smp_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this_smp;

	}
	
	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static boolean simHasMetaPhases(String schema, Long sim_id){
		
		List fullList = getAllForSim(schema, sim_id);
		
		if ((fullList == null) || (fullList.size() == 0)){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Gets all meta phases assigned to a simulation. 
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
    public static List getAllForSim(String schema, Long sim_id){
        
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from SimulationMetaPhase where sim_id = :sim_id order by metaPhaseName").setLong("sim_id", sim_id).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
    }
}
