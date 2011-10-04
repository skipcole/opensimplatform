package org.usip.osp.baseobjects;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

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
    private String metaPhaseImageFilename = ""; //$NON-NLS-1$
    
    /** Color associated with this phase. */
    private String metaPhaseColor = "FFFFFF"; //$NON-NLS-1$
    
    /** Name of the Meta Phase */
    private String metaPhaseName = "";
    
    @Lob
    private String metaPhaseNotes = "";
	
	/** Id of the simulation that this meta phase belongs in. */
	private Long sim_id;

	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

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
	public static SimulationMetaPhase getById(String schema, Long smp_id) {

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMetaPhaseImageFilename() {
		return metaPhaseImageFilename;
	}

	public void setMetaPhaseImageFilename(String metaPhaseImageFilename) {
		this.metaPhaseImageFilename = metaPhaseImageFilename;
	}

	public String getMetaPhaseColor() {
		return metaPhaseColor;
	}

	public void setMetaPhaseColor(String metaPhaseColor) {
		this.metaPhaseColor = metaPhaseColor;
	}

	public String getMetaPhaseName() {
		return metaPhaseName;
	}

	public void setMetaPhaseName(String metaPhaseName) {
		this.metaPhaseName = metaPhaseName;
	}

	public String getMetaPhaseNotes() {
		return metaPhaseNotes;
	}

	public void setMetaPhaseNotes(String metaPhaseNotes) {
		this.metaPhaseNotes = metaPhaseNotes;
	}

	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getTransit_id() {
		return transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}
}
