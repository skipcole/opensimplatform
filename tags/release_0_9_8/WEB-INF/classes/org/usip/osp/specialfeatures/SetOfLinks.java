package org.usip.osp.specialfeatures;

import java.util.List;
import java.util.ListIterator;

import javax.persistence.*;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.baseobjects.SimSectionDependentObject;
import org.usip.osp.networking.AuthorFacilitatorSessionObject;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents a 'one link' web address associated with a simulation.
 */
/*
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
@Proxy(lazy = false)
public class SetOfLinks implements SimSectionDependentObject{
	
	/**
	 * Zero argument constructor required by hibernate.
	 */
	public SetOfLinks(){
		
	}
	
	public SetOfLinks(String name, Long sim_id){
		this.name = name;
		this.sim_id = sim_id;
		
	}
	


	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Id of the base from which this copy is made. */
    @Column(name = "BASE_ID")
    private Long base_id;
	
	/** Simulation id. */
    @Column(name = "SIM_ID")
    private Long sim_id;
    
    /** Running simulation id. */
    @Column(name = "RS_ID")
    private Long rs_id;
    
    
    /** Running simulation id. */
    @Column(name = "RS_SET_ID")
    private Long rs_set_id;
    
    private String name = ""; //$NON-NLS-1$
    
    private String notes = ""; //$NON-NLS-1$  

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransitId() {
		return this.transit_id;
	}

	public void setTransitId(Long transit_id) {
		this.transit_id = transit_id;
	}
    
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getSim_id() {
		return this.sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getBase_id() {
		return this.base_id;
	}

	public void setBase_id(Long base_id) {
		this.base_id = base_id;
	}

	public Long getRs_id() {
		return this.rs_id;
	}

	public void setRs_id(Long rs_id) {
		this.rs_id = rs_id;
	}

	public Long getRs_set_id() {
		return rs_set_id;
	}

	public void setRs_set_id(Long rsSetId) {
		rs_set_id = rsSetId;
	}

	/**
	 * Saves this object to the database.
	 * @param schema
	 */
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
	public static SetOfLinks getSetOfLinksForRunningSim(String schema, Long gv_id, Long rs_id) {

		SetOfLinks this_gv = null;
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		String hql_string = "from SetOfLinks where RS_ID = " + rs_id  //$NON-NLS-1$
		+ " AND BASE_ID = '" + gv_id + "'"; //$NON-NLS-1$ //$NON-NLS-2$
		
		Logger.getRootLogger().debug("----------------------------------"); //$NON-NLS-1$
		Logger.getRootLogger().debug(hql_string);
		Logger.getRootLogger().debug("----------------------------------"); //$NON-NLS-1$
		
		List varFound = MultiSchemaHibernateUtil.getSession(schema).createQuery(hql_string).list();
		
		if (varFound == null){
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
			return this_gv;
		}
		
		if (varFound.size() > 1){
			Logger.getRootLogger().warn("More than one generic variable copy found"); //$NON-NLS-1$
		}
		
		
		for (ListIterator<SetOfLinks> li = varFound.listIterator(); li.hasNext();) {
			this_gv = li.next();
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this_gv;

	}
	
	/**
	 * Retrieves object from database.
	 * 
	 * @param schema
	 * @param gv_id
	 * @return
	 */
	public static SetOfLinks getById(String schema, Long gv_id) {

		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		SetOfLinks this_gv  = (SetOfLinks) MultiSchemaHibernateUtil
				.getSession(schema).get(SetOfLinks.class, gv_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this_gv;

	}
		
		
	
	/**
	 * This returns the 'base' one links for a simulation. A 'base' generic variable is the archetypal 
	 * generic variable
	 * that is copied into a version to be edited in a particular running simulation. When the base
	 * generic variable is copied into a copy generic variable, its id is copied into the base_id of the copy. So 
	 * the original 'base' generic variables have their BASE_ID null, and copies have in values in that field.
	 * @param hibernate_session
	 * @param the_sim_id
	 * @return
	 */
	public static List getAllBaseSetOfLinksForSim(String schema, Long the_sim_id) {
		
		String hql_string = "from SetOfLinks where SIM_ID = " + the_sim_id.toString()  //$NON-NLS-1$
			+ " AND RS_ID is null"; //$NON-NLS-1$
	
		MultiSchemaHibernateUtil.beginTransaction(schema);
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hql_string).list();
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
	/**
	 * Creates, and saves to the database, a copy of this generic variable. This is used to create a version 
	 * of a 
	 * variable for a particular running simulation session. The copied variable
	 * will have the base_id field filled in with the id of the original.
	 * 
	 * @param rsid Running simulation id.
	 * @return The generic variable object created.
	 * 
	 */
	public SetOfLinks createCopy(Long rsid,  String schema){
			
		SetOfLinks sol = new SetOfLinks();
		
		sol.setBase_id(this.getId());
		sol.setRs_id(rsid);
		sol.setSim_id(this.getSim_id());
		sol.setName(this.getName());
		sol.setNotes(this.getNotes());
		sol.saveMe(schema);
		
		return sol;
	}
	

	@Override
	public Long createRunningSimVersion(String schema, Long sim_id, Long rs_id, Object templateObject) {

		Logger.getRootLogger().warn("Creating generic variable for running sim : " + rs_id); //$NON-NLS-1$

		SetOfLinks templateOL = (SetOfLinks) templateObject;

		SetOfLinks sol = new SetOfLinks();
		
		sol.setName(templateOL.getName());
		sol.setNotes(templateOL.getNotes());
		sol.setSim_id(templateOL.getSim_id());
		sol.setBase_id(templateOL.getId());
				
		sol.setRs_id(rs_id);
		
		sol.saveMe(schema);
		
		return sol.getId();
	}

	@Override
	public void setSimId(Long theId) {
		this.sim_id = theId;
		
	}

	
}
