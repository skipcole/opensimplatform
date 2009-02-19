package org.usip.osp.baseobjects;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.communications.ConvActorAssignment;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents the assignment of a dependent object (such as a conversation, shared document, variable, etc.)
 * to a particular simulation section.
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
@Table(name = "SSRSDOA")
@Proxy(lazy = false)
public class SimSectionRSDepOjbectAssignment {

	public SimSectionRSDepOjbectAssignment() {
		
	}
	
	/** Database id of this SSRSDOA. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Simulation id of this assignment. */
	private Long sim_id;
	
	/** Running Simulation id of this assignment: Note, an rs_id = 0 indicates that this is the template
	 * object which may have its default values copied into the values of starter objects.
	 */
	private Long rs_id;
	
	/** The section id of this particular assingment. */
	private Long section_id;
	
	/** The class name of this object used to create it or pullit out of the database if necessary. */
	private String className;
	
	/** The id of this object used to pull it out of the database if necessary. */
	private Long objectId;
	
	/** The index (if needed to sequence objects on the page) of this object */
	private int ssrsdoa_index;
	
	/** Reserving this field in case there is a need to get an object by a tag name and not an index. */
	private String uniqueTagName;

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

	public Long getRs_id() {
		return rs_id;
	}

	public void setRs_id(Long rs_id) {
		this.rs_id = rs_id;
	}

	public Long getSection_id() {
		return section_id;
	}

	public void setSection_id(Long section_id) {
		this.section_id = section_id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
	
	public int getSSRSDOA_Index() {
		return ssrsdoa_index;
	}

	public void setSSRSDOA_Index(int index) {
		this.ssrsdoa_index = index;
	}

	public String getUniqueTagName() {
		return uniqueTagName;
	}

	public void setUniqueTagName(String uniqueTagName) {
		this.uniqueTagName = uniqueTagName;
	}

	/**
	 * Pulls the simulation out of the database base on its id and schema.
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static SimSectionRSDepOjbectAssignment getMe(String schema, Long ssrsdoa_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		SimSectionRSDepOjbectAssignment ssrsdoa = (SimSectionRSDepOjbectAssignment) MultiSchemaHibernateUtil
				.getSession(schema).get(SimSectionRSDepOjbectAssignment.class, ssrsdoa_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return ssrsdoa;

	}
	
	/** Saves the ssrsdoa. */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	/** Returns a list of all dependent objects associated with a particular section. */
	public static List getAllForRunningSimSection(String schema, Long rs_id, Long section_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List<SimSectionRSDepOjbectAssignment> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from SimSectionRSDepOjbectAssignment where rs_id = " + rs_id + " and section_id = " + section_id).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
	public static SimSectionRSDepOjbectAssignment getOneForRunningSimSection(String schema, Long rs_id, Long section_id, int index){
		
		SimSectionRSDepOjbectAssignment ssrsdoa = new SimSectionRSDepOjbectAssignment();
		List starterList = getAllForRunningSimSection(schema, rs_id, section_id);
		
		if ((starterList != null) && (starterList.size() > 0)){
			ssrsdoa = (SimSectionRSDepOjbectAssignment) starterList.get(index);
		}
		
		return ssrsdoa;
	}
	
}
