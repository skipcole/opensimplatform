package org.usip.osp.baseobjects;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.usip.osp.communications.SharedDocument;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents the assignment of a dependent object (such as a
 * conversation, shared document, variable, etc.) to a particular simulation
 * section.
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
	public BaseSimSectionDepObjectAssignment(Long cs_id, String className,
			int do_index, Long objId, Long sim_id, String schema) {
		this.setBss_id(cs_id);
		this.setClassName(className);
		this.setDepObjIndex(do_index);
		this.setObjectId(objId);
		this.setSim_id(sim_id);

		this.saveMe(schema);
	}

	public static BaseSimSectionDepObjectAssignment getIfExistsElseCreateIt(
			String schema, Long bss_id, String className, Long objectId,
			Long sim_id) {

		BaseSimSectionDepObjectAssignment bssdoa = new BaseSimSectionDepObjectAssignment();

		String getString = "from BaseSimSectionDepObjectAssignment where bss_id = '" + bss_id + "' " //$NON-NLS-1$ //$NON-NLS-2$
				+ " and objectId = " + objectId //$NON-NLS-1$
				+ " and className = '" + className + "'" //$NON-NLS-1$ //$NON-NLS-2$
				+ " and sim_id = " + sim_id; //$NON-NLS-1$

		Logger.getRootLogger().debug(getString);

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List docList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery(getString).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if ((docList != null) && (docList.size() > 0)) {
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
	public static Hashtable<Long, Long> getIndexIdHashtable(String schema,
			Long bss_id) {

		Hashtable<Long, Long> returnHash = new Hashtable<Long, Long>();

		if (bss_id == null) {
			Logger.getRootLogger().warn(
					"Warning! getIndexIdHashtable get null bss_id"); //$NON-NLS-1$
			return returnHash;
		}

		List checkList = getObjectsForSection(schema, bss_id);

		Logger.getRootLogger().warn(" checklist size: " + checkList.size()); //$NON-NLS-1$

		MultiSchemaHibernateUtil.beginTransaction(schema);
		for (ListIterator<BaseSimSectionDepObjectAssignment> li = checkList
				.listIterator(); li.hasNext();) {
			BaseSimSectionDepObjectAssignment bssdoa = li.next();

			Logger.getRootLogger()
					.warn("index/id:" + bssdoa.getDepObjIndex() + "/" + bssdoa.getId()); //$NON-NLS-1$ //$NON-NLS-2$
			returnHash.put(new Long(bssdoa.getDepObjIndex()),
					bssdoa.getObjectId());

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
		for (ListIterator<BaseSimSectionDepObjectAssignment> li = deleteList
				.listIterator(); li.hasNext();) {
			BaseSimSectionDepObjectAssignment bssdoa = li.next();

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

		String getString = "from BaseSimSectionDepObjectAssignment where sim_id = '" + sim_id + "' order by id"; //$NON-NLS-1$ //$NON-NLS-2$

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery(getString).list();

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
	public static List<BaseSimSectionDepObjectAssignment> getObjectsForSection(
			String schema, Long bss_id) {

		String getString = "from BaseSimSectionDepObjectAssignment where bss_id = '" + bss_id //$NON-NLS-1$
				+ "' order by dep_obj_index"; //$NON-NLS-1$

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BaseSimSectionDepObjectAssignment> returnList = MultiSchemaHibernateUtil
				.getSession(schema).createQuery(getString).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;

	}
	
	/**
	 * 
	 * @param schema
	 * @param bssId
	 * @param className
	 * @return
	 */
	public static List<BaseSimSectionDepObjectAssignment> getObjectsForSection(
			String schema, Long bssId, String className) {

		String getString = "from BaseSimSectionDepObjectAssignment where " +
			"bss_id = :bssId and className = :className order by dep_obj_index"; //$NON-NLS-1$

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BaseSimSectionDepObjectAssignment> returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(getString)
				.setLong("bssId", bssId)
				.setString("className", className)
				.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;

	}

	public static void main(String args[]) {

		boolean b = BaseSimSectionDepObjectAssignment.checkObjectInUse("test",
				new Long(2), "org.usip.osp.communications.SharedDocument");

	}

	public static boolean checkObjectInUse(String schema, Long obj_id,
			String obj_class) {

		List x = BaseSimSectionDepObjectAssignment.getObjectUsages(schema,
				obj_id, obj_class);

		if ((x == null) || (x.size() == 0)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Gets all of the base sim section objects where a particular object has been added.
	 * (Used to find out if an object is in use.
	 * 
	 * @param schema
	 * @param bss_id
	 * @return
	 */
	public static List<BaseSimSectionDepObjectAssignment> getObjectUsages(
			String schema, Long obj_id, String obj_class) {

		String getString = "from BaseSimSectionDepObjectAssignment where objectId = :obj_id and "
				+ "className = :obj_class "; //$NON-NLS-1$

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BaseSimSectionDepObjectAssignment> returnList = MultiSchemaHibernateUtil
				.getSession(schema).createQuery(getString)
				.setLong("obj_id", obj_id).setString("obj_class", obj_class)
				.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;

	}

	/**
	 * Gets a single shared document for a simulation section. 
	 * @param schema
	 * @param bss_id_s
	 * @return
	 */
	public static SharedDocument getSharedDocumentForSection(String schema,
			String bss_id_s) {

		Logger.getRootLogger().debug("bss_id is: " + bss_id_s); //$NON-NLS-1$
		Long bss_id = null;

		try {
			bss_id = new Long(bss_id_s);
		} catch (Exception e) {
			Logger.getRootLogger().debug(e.getMessage());
		}

		if (bss_id == null) {
			return null;
		}

		List startList = getObjectsForSection(schema, bss_id);

		if ((startList == null) || (startList.size() == 0)) {
			return null;
		} else {
			BaseSimSectionDepObjectAssignment bssdoa = (BaseSimSectionDepObjectAssignment) startList
					.get(0);

			SharedDocument sd = SharedDocument.getById(schema,
					bssdoa.getObjectId());

			return sd;
		}

	}

	/**
	 * 
	 * @param schema
	 * @param bss_id
	 * @return
	 */
	public static List<SharedDocument> getSharedDocumentsForSection(
			String schema, Long bss_id) {

		List returnList = new ArrayList();

		if (bss_id == null) {
			return returnList;
		}

		List startList = getObjectsForSection(schema, bss_id);

		if ((startList == null) || (startList.size() == 0)) {
			return returnList;
		} else {

			for (ListIterator<BaseSimSectionDepObjectAssignment> li = startList
					.listIterator(); li.hasNext();) {
				BaseSimSectionDepObjectAssignment bssdoa = li.next();
				
				String sharedDocClassName = SharedDocument.class.toString();
				sharedDocClassName = sharedDocClassName.replaceFirst("class ", "");
				
				if (bssdoa.getClassName().equalsIgnoreCase(sharedDocClassName)) {
					SharedDocument sd = SharedDocument.getById(schema,
							bssdoa.getObjectId());
					returnList.add(sd);
				}
			}
		}

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
	public static SimSectionDependentObject pullOutObject(String schema,
			BaseSimSectionDepObjectAssignment bssdoa) {

		Object obj = null;

		MultiSchemaHibernateUtil.beginTransaction(schema);

		try {
			Class objClass = Class.forName(bssdoa.getClassName());
			obj = MultiSchemaHibernateUtil.getSession(schema).get(objClass,
					bssdoa.getObjectId());
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
		return this.transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSim_id() {
		return this.sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getBss_id() {
		return this.bss_id;
	}

	public void setBss_id(Long bss_id) {
		this.bss_id = bss_id;
	}

	public Long getObjectId() {
		return this.objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public boolean isActor_dependent() {
		return this.actor_dependent;
	}

	public void setActor_dependent(boolean actor_dependent) {
		this.actor_dependent = actor_dependent;
	}

	public Long getActor_id() {
		return this.actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public int getDep_obj_index() {
		return this.dep_obj_index;
	}

	public void setDep_obj_index(int dep_obj_index) {
		this.dep_obj_index = dep_obj_index;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getDepObjIndex() {
		return this.dep_obj_index;
	}

	public void setDepObjIndex(int index) {
		this.dep_obj_index = index;
	}

	public String getUniqueTagName() {
		return this.uniqueTagName;
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
	public static BaseSimSectionDepObjectAssignment getById(String schema,
			Long bssdoa_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		BaseSimSectionDepObjectAssignment bssdoa = (BaseSimSectionDepObjectAssignment) MultiSchemaHibernateUtil
				.getSession(schema).get(
						BaseSimSectionDepObjectAssignment.class, bssdoa_id);

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
