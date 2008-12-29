package org.usip.osp.specialfeatures;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;

import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class allows one to set a trigger point such that when the value of one
 * variable switches beyond a limit, a separate action is taken.
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
@Table(name = "TRIGGERS")
@Proxy(lazy = false)
public class Trigger{

    public static final int VAR_TYPE_GENERIC = 0;
    
    public static final int ACT_TYPE_FINAL_VALUE_TO_AAR = 0;
    
    public static final int CHECK_FIRE_ON_END = 0;
    public static final int CHECK_FIRE_ON_PHASE_CHANGE = 1;
    
	/** Database id of this Trigger. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Simulation id. */
    @Column(name = "SIM_ID")
    private Long sim_id;
    
    private int var_type;
    
    private int action_type;
    
    private int check_fire;

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

	public int getVar_type() {
		return var_type;
	}

	public void setVar_type(int var_type) {
		this.var_type = var_type;
	}

	public int getAction_type() {
		return action_type;
	}

	public void setAction_type(int action_type) {
		this.action_type = action_type;
	}

	public int getCheck_fire() {
		return check_fire;
	}

	public void setCheck_fire(int check_fire) {
		this.check_fire = check_fire;
	}
    
	/**
	 * Saves this Trigger to the database.
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
	public static Trigger getMe(String schema, Long t_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Trigger this_t = (Trigger) MultiSchemaHibernateUtil
				.getSession(schema).get(Trigger.class, t_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this_t;

	}

    

}
