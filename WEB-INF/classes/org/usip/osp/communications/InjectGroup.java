package org.usip.osp.communications;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.BaseSimSection;
import org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment;
import org.usip.osp.baseobjects.SimSectionRSDepOjbectAssignment;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents a collection of Injects and contains information about when this group may be used.
 */
 /*
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
@Table(name = "INJECT_GROUPS")
@Proxy(lazy=false)
public class InjectGroup {

    /** Database id of this Inject. */
	@Id
	@GeneratedValue
	@Column(name = "INJECTCLASS_ID")
    private Long id;
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransit_id() {
		return this.transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}
	
	/** Simulation id. */
    @Column(name = "SIM_ID")
    private Long sim_id;
	
	/** Name of this group of injects. */
	private String name = ""; //$NON-NLS-1$
	
	/** Description of this group of injects. */
	@Lob
	private String description = ""; //$NON-NLS-1$

	public static final String PLAYER_CAN_EDIT = "PLAYER_CAN_EDIT";

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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void saveMe(String schema) {
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
	}
	
	public static InjectGroup getById(String schema, String the_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		InjectGroup ig = (InjectGroup) MultiSchemaHibernateUtil.getSession(schema).get(InjectGroup.class,
				new Long(the_id));
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return ig;

	}
	
	public static List getAllForSim(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from InjectGroup where SIM_ID = " + sim_id + " order by injectclass_id").list(); //$NON-NLS-1$ //$NON-NLS-2$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	public static boolean checkIfInUse(String schema, Long sim_id, Long group_id) {
		
		List uses = Inject.getAllForSimAndGroup(schema, sim_id, group_id);
		
		if (uses.size() > 0){
			System.out.println("size is " + uses.size());
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param schema
	 * @param section_id
	 * @param rs_id
	 * @return
	 */
	public static List<InjectGroup> getSetOfInjectGroupsForSection(String schema, Long bss_id) {

		List<InjectGroup> returnList = new ArrayList<InjectGroup>();

		String getString = "from BaseSimSectionDepObjectAssignment where bss_id = :bss_id "
				+ " and className = 'org.usip.osp.communications.InjectGroup' order by dep_obj_index"; //$NON-NLS-1$

		Logger.getRootLogger().debug(getString);

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List injectGroupList = MultiSchemaHibernateUtil.getSession(schema)
			.createQuery(getString)
			.setLong("bss_id", bss_id)
			.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		// Go over list and get items.
		for (ListIterator<BaseSimSectionDepObjectAssignment> li = injectGroupList.listIterator(); li.hasNext();) {

			BaseSimSectionDepObjectAssignment ssrsdoa = li.next();

			MultiSchemaHibernateUtil.beginTransaction(schema);

			try {
				Class objClass = Class.forName(ssrsdoa.getClassName());

				InjectGroup sd = (InjectGroup) MultiSchemaHibernateUtil.getSession(schema).get(objClass,
						ssrsdoa.getObjectId());

				returnList.add(sd);

			} catch (Exception e) {
				e.printStackTrace();
			}

			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		}

		return returnList;

	}
	
}
