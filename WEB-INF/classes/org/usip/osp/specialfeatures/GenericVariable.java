package org.usip.osp.specialfeatures;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.communications.SharedDocument;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents a generic variable associated with a simulation.
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
@Table(name = "GENERICVARIABLES")
@Proxy(lazy = false)
public class GenericVariable {

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
    
    private String value = "";
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getBase_id() {
		return base_id;
	}

	public void setBase_id(Long base_id) {
		this.base_id = base_id;
	}

	public Long getRs_id() {
		return rs_id;
	}

	public void setRs_id(Long rs_id) {
		this.rs_id = rs_id;
	}

	/**
	 * Saves this generic variable to the database.
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
	public static GenericVariable getMe(String schema, Long gv_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		GenericVariable this_gv  = (GenericVariable) MultiSchemaHibernateUtil
				.getSession(schema).get(GenericVariable.class, gv_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this_gv;

	}
	
	/**
	 * This returns the 'base' generic variables for a simulation. A 'base' generic variable is the archetypal 
	 * generic variable
	 * that is copied into a version to be edited in a particular running simulation. When the base
	 * generic variable is copied into a copy generic variable, its id is copied into the base_id of the copy. So 
	 * the original 'base' generic variables have their BASE_ID null, and copies have in values in that field.
	 * @param hibernate_session
	 * @param the_sim_id
	 * @return
	 */
	public static List getAllBaseGenericVariablesForSim(  org.hibernate.Session hibernate_session, Long the_sim_id) {
		
		String hql_string = "from GenericVariable where SIM_ID = " + the_sim_id.toString() 
			+ " AND BASE_ID is null";
		List returnList = hibernate_session.createQuery(hql_string).list();
		
		return returnList;
	}
	
	/**
	 * Creates, and saves to the database, a copy of this generic variable. This is used to create a version 
	 * of a 
	 * variable for a particular running simulation session. The copied variable
	 * will have the base_id field filled in with the id of the original.
	 * 
	 * @param rsid Running simulation id.
	 * @param hibernate_session Hibernate session created targetting the appropriate schema for saving into.
	 * @return The generic variable object created.
	 * 
	 */
	public GenericVariable createCopy(Long rsid,  org.hibernate.Session hibernate_session){
		GenericVariable gv = new GenericVariable();
		
		gv.setBase_id(this.getId());
		gv.setRs_id(rsid);
		gv.setSim_id(this.getSim_id());
		gv.setValue(this.getValue());
		
		hibernate_session.saveOrUpdate(gv);
		
		return gv;
	}

	
}
