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

	/**
	 * Creates the hashtable to allow a lookup of ids based on index.
	 * 
	 * @param schema
	 * @param bss_id
	 * @return
	 */
	public static Hashtable getIndexIdHashtable(String schema, Long bss_id){
		
		Hashtable returnHash = new Hashtable();
		
		if (bss_id == null){
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
	public static List getSimDependencies(String schema, Long sim_id){
		
		String getString = "from BaseSimSectionDepObjectAssignment where sim_id = '" + sim_id + "'";

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

		String getString = "from BaseSimSectionDepObjectAssignment where bss_id = '" + bss_id + "' order by dep_obj_index";

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
	public static Object pullOutObject(String schema, BaseSimSectionDepObjectAssignment bssdoa) {

		Object obj = null;

		MultiSchemaHibernateUtil.beginTransaction(schema);

		try {
			Class objClass = Class.forName(bssdoa.className);
			obj = MultiSchemaHibernateUtil.getSession(schema).get(objClass, bssdoa.getObjectId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return obj;
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
