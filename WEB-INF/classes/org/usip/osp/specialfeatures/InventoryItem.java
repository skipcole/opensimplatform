package org.usip.osp.specialfeatures;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.SimSectionDependentObject;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.communications.Conversation;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents an inventory Item that a player may have.
 */
/*
 * 
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
public class InventoryItem implements SimSectionDependentObject {

	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;

	/** Indicates if this is a template object for a simulation. */
	private boolean templateObject = false;

	/** Id of the base from which this copy is made. */
	@Column(name = "BASE_ID")
	private Long base_id;

	/** Simulation id. */
	@Column(name = "SIM_ID")
	private Long sim_id;

	/** Running simulation id. */
	@Column(name = "RS_ID")
	private Long rs_id;

	/** Id of the actor who possesses this item */
	private Long owner_id;

	private String itemName = ""; //$NON-NLS-1$

	@Lob
	private String notes = ""; //$NON-NLS-1$

	@Lob
	private String description = ""; //$NON-NLS-1$

	private String imageFile = ""; //$NON-NLS-1$

	@Lob
	private String metaData = ""; //$NON-NLS-1$

	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	/**
	 * Zero argument constructor required by hibernate.
	 */
	public InventoryItem() {

	}

	public InventoryItem(String itemName, String itemDescription,
			String itemNotes, Long simId, boolean templateObject) {

		this.itemName = itemName;
		this.description = itemDescription;
		this.notes = itemNotes;
		this.sim_id = simId;
		this.templateObject = templateObject;
	}

	@Override
	public Long getTransit_id() {
		return this.transit_id;
	}

	@Override
	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}

	@Override
	public Long createRunningSimVersion(String schema, Long simId, Long rsId,
			Object templateObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}

	@Override
	public void setId(Long theId) {
		this.id = theId;

	}

	@Override
	public void setSimId(Long theId) {
		this.sim_id = theId;

	}

	public boolean isTemplateObject() {
		return templateObject;
	}

	public void setTemplateObject(boolean templateObject) {
		this.templateObject = templateObject;
	}

	public Long getBase_id() {
		return base_id;
	}

	public void setBase_id(Long baseId) {
		base_id = baseId;
	}

	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long simId) {
		sim_id = simId;
	}

	public Long getRs_id() {
		return rs_id;
	}

	public void setRs_id(Long rsId) {
		rs_id = rsId;
	}

	public Long getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(Long ownerId) {
		owner_id = ownerId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageFile() {
		return imageFile;
	}

	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}

	public String getMetaData() {
		return metaData;
	}

	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}

	public static void main(String args[]) {
		System.out.println("Hello World!");

		InventoryItem x = new InventoryItem("tool2", "useful2",
				"use sparingly2", new Long(1), true);

		x.saveMe("test");

		for (ListIterator<InventoryItem> li = getAllBaseForSim("test", new Long(1))
				.listIterator(); li.hasNext();) {
			InventoryItem this_sp = li.next();
			System.out.println(this_sp.getItemName());
		}

	}

	/**
	 * Returns all of the template items for a simulation.
	 * 
	 * @param schema
	 * @param simid
	 * @return
	 */
	public static List getAllBaseForSim(String schema, Long simid) {

		if (simid == null) {
			return new ArrayList();
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<InventoryItem> returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from InventoryItem where sim_id = :simid and rs_id is null")
				.setLong("simid", simid).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	/**
	 * Pulls the simulation out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param ii_id
	 * @return
	 */
	public static InventoryItem getById(String schema, Long ii_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		InventoryItem inventoryItem = (InventoryItem) MultiSchemaHibernateUtil
				.getSession(schema).get(InventoryItem.class, ii_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return inventoryItem;

	}

	/**
	 * Returns a hashtable that lists all of the actor ids who own these items,
	 * and the number of these items that they possess.
	 * 
	 * @param schema
	 * @param ii_id
	 * @return
	 */
	public static Hashtable getAllAssignmentForItem(String schema, Long ii_id) {

		Hashtable returnTable = new Hashtable();

		if (ii_id == null) {
			return returnTable;
		}

		List<InventoryItem> tempList = getListOfTemplateAssignments(schema,
				ii_id);

		for (ListIterator<InventoryItem> li = tempList.listIterator(); li
				.hasNext();) {
			InventoryItem this_ii = li.next();

			if (this_ii.getOwner_id() != null) {

				Long total = new Long(0);
				// Check to see if one has been added before.

				Object obj = returnTable.get(this_ii.owner_id);
				if (obj != null) {
					total = (Long) obj;
				}

				// Add one to the number that it already has
				total = new Long(total.intValue() + 1);

				// Store this number into the hashtable
				returnTable.put(this_ii.owner_id, total);

			}
		}

		return returnTable;
	}

	/** Returns the set of intial assignment objects. */
	public static List getListOfTemplateAssignments(String schema, Long ii_id) {
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List<InventoryItem> tempList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from InventoryItem where base_id = :ii_id and templateObject is true")
				.setLong("ii_id", ii_id).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return tempList;

	}

	/**
	 * Removes all of the template assignments for an item.
	 * 
	 * @param schema
	 * @param base_id
	 */
	public static void removeTemplateAssignments(String schema, Long base_id) {
		
		List<InventoryItem> tempList = getListOfTemplateAssignments(schema,
				base_id);

		MultiSchemaHibernateUtil.beginTransaction(schema);
		for (ListIterator<InventoryItem> li = tempList.listIterator(); li
				.hasNext();) {
			InventoryItem this_ii = li.next();
			MultiSchemaHibernateUtil.getSession(schema).delete(this_ii);
			
		}
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

}
