package org.usip.osp.specialfeatures;

import java.util.List;

import javax.persistence.*;
import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents the reflections of a player regarding a simulation.
 *
 *
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "PLAYER_REFLECTIONS")
@Proxy(lazy=false)
public class PlayerReflection implements Comparable{

	/** Database id of this Variable. */
	@Id
	@GeneratedValue
	@Column(name = "PR_ID")
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
    
    /** Custom Simulation section id. */
    @Column(name = "CS_ID")
    private Long cs_id;
    
    /** Actor id. */
    @Column(name = "A_ID")
    private Long a_id;
    
    /** The phase in which this reflection was made. */
    private Long phase_id;
    
    public Long getPhase_id() {
		return this.phase_id;
	}

	public void setPhase_id(Long phase_id) {
		this.phase_id = phase_id;
	}

	@Transient
    private String actor_name = ""; //$NON-NLS-1$

	/** Indicates if this document can still be edited. */
	private boolean editable = true;

	/** Contents of this document. */
	@Lob
	private String bigString = ""; //$NON-NLS-1$
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBase_id() {
		return this.base_id;
	}

	public void setBase_id(Long base_id) {
		this.base_id = base_id;
	}

	public Long getSim_id() {
		return this.sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getRs_id() {
		return this.rs_id;
	}

	public void setRs_id(Long rs_id) {
		this.rs_id = rs_id;
	}

	public Long getCs_id() {
		return this.cs_id;
	}

	public void setCs_id(Long cs_id) {
		this.cs_id = cs_id;
	}
	
	public Long getA_id() {
		return this.a_id;
	}

	public void setA_id(Long a_id) {
		this.a_id = a_id;
	}

	public boolean isEditable() {
		return this.editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getBigString() {
		return this.bigString;
	}

	public void setBigString(String bigString) {
		this.bigString = bigString;
	}
	
	/**
	 * 
	 * @param schema
	 * @param cs_id
	 * @param rs_id
	 * @param a_id
	 * @return
	 */
	public static PlayerReflection getPlayerReflection(String schema, Long cs_id, Long rs_id, Long a_id){
		
		PlayerReflection playerReflection = new PlayerReflection();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		String hql_string = "from PlayerReflection where CS_ID = " + cs_id + " AND RS_ID = " + rs_id //$NON-NLS-1$ //$NON-NLS-2$
		+ " AND A_ID = " + a_id; //$NON-NLS-1$
		
		System.out.println("hql_string is " + hql_string); //$NON-NLS-1$
		
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hql_string).list();
		
		if ((returnList == null) || (returnList.size() == 0)){
			playerReflection.setCs_id(cs_id);
			playerReflection.setRs_id(rs_id);
			playerReflection.setA_id(a_id);
			MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(playerReflection);
			
		} else {
			playerReflection = (PlayerReflection) returnList.get(0);
		}
		
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		
		return playerReflection;
	}
	
	public static List getReflections(String schema, Long rs_id){
		
		PlayerReflection playerReflection = new PlayerReflection();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		String hql_string = "from PlayerReflection where RS_ID = " + rs_id; //$NON-NLS-1$
		
		System.out.println("hql_string is " + hql_string); //$NON-NLS-1$
		
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hql_string).list();
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
	
	public void save(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getActor_name() {
		return this.actor_name;
	}

	public void setActor_name(String actor_name) {
		this.actor_name = actor_name;
	}
}
