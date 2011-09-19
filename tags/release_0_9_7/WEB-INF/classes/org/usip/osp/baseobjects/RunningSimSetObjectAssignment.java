package org.usip.osp.baseobjects;

import java.util.List;
import java.util.ListIterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
/**
 * This class associates an object (like a set of links) with a set of running simulations. 
 *
 */
/* 
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
public class RunningSimSetObjectAssignment {
	
	
	public RunningSimSetObjectAssignment(){
		
	}
	
	/**
	 * 
	 * @param schema
	 * @param rs_id
	 * @param rs_set_id
	 */
	public RunningSimSetObjectAssignment(String schema, Long rs_id, Long rs_set_id){
		
		this.rs_id = rs_id;
		this.rs_set_id = rs_set_id;
		this.saveMe(schema);
		
	}
	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	private Long rs_id;
	
	private Long rs_set_id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRs_id() {
		return rs_id;
	}

	public void setRs_id(Long rs_id) {
		this.rs_id = rs_id;
	}

	public Long getRs_set_id() {
		return rs_set_id;
	}

	public void setRs_set_id(Long rs_set_id) {
		this.rs_set_id = rs_set_id;
	}

	/**
	 * Removes all of the running set assignments for a particular running sim.
	 * @param schema
	 * @param rs_set_id
	 */
	public static void removeAllForRunningSimSet(String schema, Long rs_set_id) {
		
		List returnList = getAllForRunningSimulationSet(schema, rs_set_id);
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		for (ListIterator li = returnList.listIterator(); li.hasNext();) {
			RunningSimSetObjectAssignment rss = (RunningSimSetObjectAssignment) li.next();
			
			MultiSchemaHibernateUtil.getSession(schema).delete(rss);
			
		}
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	/**
	 * Returns all of the actors found in a schema for a particular simulation
	 * 
	 * @param schema
	 * @return
	 */
	public static List <RunningSimSetObjectAssignment> getAllForRunningSimulationSet(String schema, Long rs_set_id){
	    
		MultiSchemaHibernateUtil.beginTransaction(schema);
	
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from RunningSimSetAssignment where rs_set_id = :rs_set_id")
				.setLong("rs_set_id", rs_set_id).list(); //$NON-NLS-1$
	
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	
		return returnList;
	}
	
	/**
	 * Returns all of the running set assignments for a particular running simulation.
	 * 
	 * @param schema
	 * @param rs_id
	 * @return
	 */
	public static List <RunningSimSetObjectAssignment> getAllForRunningSimulation(String schema, Long rs_id){
	    
		MultiSchemaHibernateUtil.beginTransaction(schema);
	
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from RunningSimSetAssignment where rs_id = :rs_id")
				.setLong("rs_id", rs_id).list(); //$NON-NLS-1$
	
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	
		return returnList;
	}
	
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

}
