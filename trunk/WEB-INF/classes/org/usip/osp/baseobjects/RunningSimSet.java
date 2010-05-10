package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * Represents a set of simulations (such as a group run simultaneously in a class.
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
public class RunningSimSet {
	
	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "SIM_ID")
	private Long sim_id;
	
	private String RunningSimSetName = "";
	
	/** Value of the user id that created this set. */
	private Long user_id;
	
	/** Name of the user that created this set. */
	private String username;

	@Column(name = "CREATION_DATE", columnDefinition = "datetime")
	@GeneratedValue
	private Date creationDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public String getRunningSimSetName() {
		return RunningSimSetName;
	}

	public void setRunningSimSetName(String runningSimSetName) {
		RunningSimSetName = runningSimSetName;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	/**
	 * Returns a list of all running sims created for a simulation.
	 * 
	 * @param simid
	 * @param schema
	 * @return
	 */
	public static List<RunningSimSet> getAllForSim(String simid, String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<RunningSimSet> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from RunningSimSet where sim_id = :sim_id").setString("sim_id", simid).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	/**
	 * 
	 * @param schema
	 * @param rs_id
	 * @return
	 */
	public static RunningSimSet getMe(String schema, Long rs_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		RunningSimSet this_rs = (RunningSimSet) MultiSchemaHibernateUtil.getSession(schema).get(
				RunningSimSet.class, rs_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this_rs;

	}
	
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	
    /**
     * Returns all of the actors found in a schema for a particular simulation
     * 
     * @param schema
     * @return
     */
    public static List getAllForRunningSimulation(String schema, Long rs_set_id){
        
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from RunningSimSetAssignment where rs_set_id = :rs_set_id")
				.setLong("rs_set_id", rs_set_id).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
    }
    
    /**
     * 
     * @param schema
     * @param rs_set_id
     * @return
     */
    public static Hashtable getHashSetOfRunningSims(String schema, Long rs_set_id){
    	
    	Hashtable returnHashtable = new Hashtable();
    	
    	List returnList = getAllForRunningSimulation(schema, rs_set_id);
    	
    	for (ListIterator li = returnList.listIterator(); li.hasNext();) {
    		RunningSimSetAssignment rss = (RunningSimSetAssignment) li.next();
			
			returnHashtable.put(rss.getRs_id(), "set");
			
    	}
    	
    	return returnHashtable;
    }
	
}
