package org.usip.osp.baseobjects;

import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents the assignment of a dependent object (such as a
 * conversation, shared document, variable, etc.) to a particular simulation
 * section.
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
@Table(name = "BSSDOA")
@Proxy(lazy = false)
public class BaseSimSectionDepObjectAssignment {

	/** Zero argument constructor needed by Hibernate */
	public BaseSimSectionDepObjectAssignment() {

	}
	
	/**
	 * Creates and saves the object to the database.
	 * 
	 * @param cs_id
	 * @param className
	 * @param do_index
	 * @param objId
	 * @param sim_id
	 * @param schema
	 */
	public BaseSimSectionDepObjectAssignment(Long cs_id, String className, int do_index, Long objId, Long sim_id,
			String schema){
		this.setBss_id(cs_id);
		this.setClassName(className);
		this.setDepObjIndex(do_index);
		this.setObjectId(objId);
		this.setSim_id(sim_id);
		
		this.saveMe(schema);
	}

	public static BaseSimSectionDepObjectAssignment getIfExistsElseCreateIt(String schema, Long bss_id,
			String className, Long objectId, Long sim_id) {

		BaseSimSectionDepObjectAssignment bssdoa = new BaseSimSectionDepObjectAssignment();

		String getString = "from BaseSimSectionDepObjectAssignment where bss_id = '" + bss_id + "' "
				+ " and objectId = " + objectId
				+ " and className = '" + className + "'"
				+ " and sim_id = " + sim_id;

		System.out.println(getString);
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List docList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getString).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		if ((docList != null) && (docList.size() > 0)){
			bssdoa = (BaseSimSectionDepObjectAssignment) docList.get(0);
			return bssdoa;
		} else {
			bssdoa.setBss_id(bss_id);
			bssdoa.setClassName(className);
			bssdoa.setObjectId(objectId);
			bssdoa.setSim_id(sim_id);
			bssdoa.saveMe(schema);
			return bssdoa;
		}
	}

	/**
	 * Creates the hashtable to allow a lookup of ids based on index.
	 * 
	 * @param schema
	 * @param bss_id
	 * @return
	 */
	public static Hashtable getIndexIdHashtable(String schema, Long bss_id) {

		Hashtable returnHash = new Hashtable();

		if (bss_id == null) {
			return returnHash;
		}

		List checkList = getObjectsForSection(schema, bss_id);

		MultiSchemaHibernateUtil.beginTransaction(schema);
		for (ListIterator<BaseSimSectionDepObjectAssignment> li = checkList.listIterator(); li.hasNext();) {
			BaseSimSectionDepObjectAssignment bssdoa = (BaseSimSectionDepObjectAssignment) li.next();

			returnHash.put(new Long(bssdoa.getDepObjIndex()), bssdoa.getId());

		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnHash;

	}

	/**
	 * Removes all of the dependent objects assigned to a particular section.
	 * 
	 * @param schema
	 * @param bss_id
	 */
	public static void removeAllForSection(String schema, Long bss_id) {

		List deleteList = getObjectsForSection(schema, bss_id);

		MultiSchemaHibernateUtil.beginTransaction(schema);
		for (ListIterator<BaseSimSectionDepObjectAssignment> li = deleteList.listIterator(); li.hasNext();) {
			BaseSimSectionDepObjectAssignment bssdoa = (BaseSimSectionDepObjectAssignment) li.next();

			MultiSchemaHibernateUtil.getSession(schema).delete(bssdoa);

		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}

	/**
	 * Return the set of dependencies found for this sim.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static List getSimDependencies(String schema, Long sim_id) {

		String getString = "from BaseSimSectionDepObjectAssignment where sim_id = '" + sim_id + "' order by id";

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getString).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	/**
	 * Gets all of the particular objects associated with a particular base
	 * section.
	 * 
	 * @param schema
	 * @param bss_id
	 * @return
	 */
	public static List getObjectsForSection(String schema, Long bss_id) {

		String getString = "from BaseSimSectionDepObjectAssignment where bss_id = '" + bss_id
				+ "' order by dep_obj_index";

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getString).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;

	}

	/**
	 * Pulls an object out of the database based on information stored in the
	 * bssdoa.
	 * 
	 * @param schema
	 * @param bssdoa
	 * @return
	 */
	public static SimSectionDependentObject pullOutObject(String schema, BaseSimSectionDepObjectAssignment bssdoa) {

		Object obj = null;

		MultiSchemaHibernateUtil.beginTransaction(schema);

		try {
			Class objClass = Class.forName(bssdoa.getClassName());
			obj = MultiSchemaHibernateUtil.getSession(schema).get(objClass, bssdoa.getObjectId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return (SimSectionDependentObject) obj;
	}

	/** Database id of this SSRSDOA. */
	@Id
	@GeneratedValue
	private Long id;

	/** Simulation id of this assignment. */
	private Long sim_id;

	/** Base Sim Section id of this assignment. */
	private Long bss_id;

	/** The id of this object used to pull it out of the database if necessary. */
	private Long objectId;
	
	/**
	 * If the dependent object associated with this section depends upon the actor's id, then this is true.
	 */
	private boolean actor_dependent = false;
	
	/** If the dependent objects associated with this section are associated with a particular actor, this is its id. */
	private Long actor_id;

	/**
	 * The class name of this object used to create it or pull it out of the
	 * database if necessary.
	 */
	private String className;

	/** The index (if needed to sequence objects on the page) of this object */
	private int dep_obj_index;

	/**
	 * Reserving this field in case there is a need to get an object by a tag
	 * name and not an index.
	 */
	private String uniqueTagName;
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransit_id() {
		return transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}

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

	public Long getBss_id() {
		return bss_id;
	}

	public void setBss_id(Long bss_id) {
		this.bss_id = bss_id;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public boolean isActor_dependent() {
		return actor_dependent;
	}

	public void setActor_dependent(boolean actor_dependent) {
		this.actor_dependent = actor_dependent;
	}

	public Long getActor_id() {
		return actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public int getDep_obj_index() {
		return dep_obj_index;
	}

	public void setDep_obj_index(int dep_obj_index) {
		this.dep_obj_index = dep_obj_index;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getDepObjIndex() {
		return dep_obj_index;
	}

	public void setDepObjIndex(int index) {
		this.dep_obj_index = index;
	}

	public String getUniqueTagName() {
		return uniqueTagName;
	}

	public void setUniqueTagName(String uniqueTagName) {
		this.uniqueTagName = uniqueTagName;
	}

	/**
	 * Pulls the simulation out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static BaseSimSectionDepObjectAssignment getMe(String schema, Long bssdoa_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		BaseSimSectionDepObjectAssignment bssdoa = (BaseSimSectionDepObjectAssignment) MultiSchemaHibernateUtil
				.getSession(schema).get(BaseSimSectionDepObjectAssignment.class, bssdoa_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return bssdoa;

	}

	/** Saves the bssdoa. */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}

}
