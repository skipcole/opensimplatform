package org.usip.osp.bishops;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.communications.Conversation;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

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
 * 
 */
@Entity
@Proxy(lazy = false)
public class BishopsPartyInfo {

	/** Database id of this Party Info. */
	@Id
	@GeneratedValue
	private Long id;
	
	private Long sim_id;
	
	private Long running_sim_id;
	
	private String name = "";

	private int partyIndex = 0;
	
	@Lob
	private String needsDoc;
	
	@Lob	
	private String fearsDoc;

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

	public Long getRunning_sim_id() {
		return running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPartyIndex() {
		return partyIndex;
	}

	public void setPartyIndex(int index) {
		this.partyIndex = index;
	}
	
	public String getNeedsDoc() {
		return needsDoc;
	}

	public void setNeedsDoc(String needsDoc) {
		this.needsDoc = needsDoc;
	}

	public String getFearsDoc() {
		return fearsDoc;
	}

	public void setFearsDoc(String fearsDoc) {
		this.fearsDoc = fearsDoc;
	}

	/**
	 * Returns a list of all party info associated with a particular running simulation.
	 */
	public static List getAllForRunningSim(String schema, Long rs_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Conversation> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from BishopsPartyInfo where  running_sim_id = " + rs_id).list(); //$NON-NLS-1$ //$NON-NLS-2$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	/**
	 * Saves the object to the database.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	
	
	public static void main(String args[]){
		BishopsPartyInfo bpi = new BishopsPartyInfo();
		
	}
}
