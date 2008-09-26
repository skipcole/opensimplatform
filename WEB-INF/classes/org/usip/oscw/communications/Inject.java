package org.usip.oscw.communications;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.oscw.persistence.MultiSchemaHibernateUtil;

/**
 * @author Ronald "Skip" Cole
 *
 * This file is part of the USIP Online Simulation Platform.<br>
 * 
 * The USIP Online Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Online Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "INJECTS")
@Proxy(lazy=false)
public class Inject {

    /** Database id of this Inject. */
	@Id
	@GeneratedValue
	@Column(name = "INJECT_ID")
    private Long id;
	
	/** Simulation id. */
    @Column(name = "SIM_ID")
    private Long sim_id;
    
	/** Inject Group id. */
    @Column(name = "GROUP_ID")
    private Long group_id;
	
    /** Name of this inject. */
	private String inject_name = "";
	
	/** Name of this group of injects. */
	@Lob
	private String inject_text = "";
	
	/** Description of this group of injects. */
	@Lob
	private String inject_notes = "";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInject_text() {
		return inject_text;
	}

	public void setInject_text(String inject_text) {
		this.inject_text = inject_text;
	}

	public String getInject_Notes() {
		return inject_notes;
	}

	public void setInject_Notes(String inject_notes) {
		this.inject_notes = inject_notes;
	}
	
	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Long group_id) {
		this.group_id = group_id;
	}

	public String getInject_name() {
		return inject_name;
	}

	public void setInject_name(String inject_name) {
		this.inject_name = inject_name;
	}

	public void saveMe(String schema) {
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
	}
	
	public static List getAllForSimAndGroup(String schema, Long sim_id, Long group_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = 
			MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Inject where SIM_ID = " + sim_id + 
				" and GROUP_ID = " + group_id).list();
		

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
}
