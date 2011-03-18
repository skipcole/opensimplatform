package org.usip.osp.sharing;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This is a lookup table to find the components of an EEO, and add them to the transient fields
 * to be exported. They can then be imported somewhere else, and the this lookup table recreated
 * on the importing platform.
 *
 */
/*
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it
 * and/or modify it under the terms of the new BSD Style license associated with
 * this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. <BR>
 */
@Entity
@Proxy(lazy = false)
public class ExperienceExportObjectComponents {

	/** Database id of this Object. */
	@Id
	@GeneratedValue
	private Long id;
	
	private Long eeo_id;
	
	private Long component_id;
	
	private String className;
	
	/** Zero argument constructor */
	public ExperienceExportObjectComponents(){
		
	}
	
	/** Convenience Constructor */
	public ExperienceExportObjectComponents(String schema, Long eeo_id, Long component_id, String className){
		
		this.eeo_id = eeo_id;
		this.component_id = component_id;
		this.className = className;
		
		saveMe(schema);
		
	}
	
	/**
	 * Saves this object back to the database.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	
}
