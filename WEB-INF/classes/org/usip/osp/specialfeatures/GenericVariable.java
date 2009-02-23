package org.usip.osp.specialfeatures;

import java.util.List;
import java.util.ListIterator;

import javax.persistence.*;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.baseobjects.SimSectionDependentObject;
import org.usip.osp.baseobjects.SimulationPhase;
import org.usip.osp.communications.SharedDocument;
import org.usip.osp.networking.ParticipantSessionObject;
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
public class GenericVariable implements SimSectionDependentObject{
	
	/**
	 * Zero argument constructor required by hibernate.
	 */
	public GenericVariable(){
		
	}
	
	/** Key to pull id out if stored in Hashtable */
	public static final String GEN_VAR_KEY = "gen_var_key";

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
    
    @Lob
    private String value = "";
    
    /** If a value has been set for this variable, this is the id allowable response holding the answer. */
    private Long currentlySelectedResponse;
	

	public Long getCurrentlySelectedResponse() {
		return currentlySelectedResponse;
	}

	public void setCurrentlySelectedResponse(Long currentlySelectedResponse) {
		this.currentlySelectedResponse = currentlySelectedResponse;
	}

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
	public static GenericVariable getGVForRunningSim(String schema, Long gv_id, Long rs_id) {

		GenericVariable this_gv = null;
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		String hql_string = "from GenericVariable where RS_ID = " + rs_id 
		+ " AND BASE_ID = '" + gv_id + "'";
		
		System.out.println("----------------------------------");
		System.out.println(hql_string);
		System.out.println("----------------------------------");
		
		List varFound = MultiSchemaHibernateUtil.getSession(schema).createQuery(hql_string).list();
		
		if (varFound == null){
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
			return this_gv;
		}
		
		if (varFound.size() > 1){
			Logger.getRootLogger().warn("More than one generic variable copy found");
		}
		
		
		for (ListIterator<GenericVariable> li = varFound.listIterator(); li.hasNext();) {
			this_gv = (GenericVariable) li.next();
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this_gv;

	}
	
	public static GenericVariable getmE(String schema, Long gv_id) {

		
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
	public static List getAllBaseGenericVariablesForSim(String schema, Long the_sim_id) {
		
		String hql_string = "from GenericVariable where SIM_ID = " + the_sim_id.toString() 
			+ " AND BASE_ID is null";
	
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
	 * @param hibernate_session Hibernate session created targetting the appropriate schema for saving into.
	 * @return The generic variable object created.
	 * 
	 */
	public GenericVariable createCopy(Long rsid,  String schema){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		GenericVariable gv = new GenericVariable();
		
		gv.setBase_id(this.getId());
		gv.setRs_id(rsid);
		gv.setSim_id(this.getSim_id());
		gv.setValue(this.getValue());
		
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(gv);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return gv;
	}
	
	/** Gets the GenericVariable referred to in a custom sections hashtable. */
	public static GenericVariable pullMeOut(String schema, CustomizeableSection cust, Long rs_id){
		
		Long gv_id = (Long) cust.getContents().get(GEN_VAR_KEY);
		
		System.out.println("The gv_id found inside of this custom section is " + gv_id);
		
		return getGVForRunningSim(schema, gv_id, rs_id);
		
	}
	
	/** Gets the GenericVariable referred to in a custom sections hashtable. */
	public static GenericVariable pullOutBaseGV(String schema, CustomizeableSection cust){
		
		Long gv_id = (Long) cust.getContents().get(GEN_VAR_KEY);
		
		System.out.println("The gv_id found inside of this custom section is " + gv_id);
		
		return getmE(schema, gv_id);
		
	}
	
	/**
	 * Checks triggers that are on this generic variable. 
	 * 
	 * @param pso
	 * @param condition
	 */
	public void checkMyTriggers(ParticipantSessionObject pso, int condition){
		
		Logger.getRootLogger().warn("GenericVariable.checkMyTriggers");
		
		List setOfPossibleTriggers = Trigger.
			getTriggersForVariable(pso.schema, Trigger.VAR_TYPE_GENERIC, this.getBase_id());
		
		for (ListIterator<Trigger> li = setOfPossibleTriggers.listIterator(); li.hasNext();) {
			Trigger this_trig = (Trigger) li.next();
			
			Logger.getRootLogger().warn("Found Trigger " + this_trig.getId());
			
			if (this_trig.getFire_on() == condition){
				Logger.getRootLogger().warn("executing on  " + this_trig.getId());
				this_trig.execute(pso);
			}
			
		}
	}

	@Override
	public Long createRunningSimVersion(String schema, Long sim_id, Long rs_id, Object templateObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectClass() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
