package org.usip.osp.specialfeatures;

import java.util.List;
import java.util.ListIterator;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

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
@Proxy(lazy=false)
public class IndividualLink {
	
    /** Unique id of this actor. */
	@Id @GeneratedValue
    private Long id;
	
	private Long sim_id;
	
	private Long running_sim_id;
	
	private Long set_of_links_id;
	
	private Long actor_id;
	
	private String linkString = "";
	
	private String linkTitle = "";
	
	private String description = "";
	
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

	public Long getSet_of_links_id() {
		return set_of_links_id;
	}

	public void setSet_of_links_id(Long set_of_links_id) {
		this.set_of_links_id = set_of_links_id;
	}

	public Long getActor_id() {
		return actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public String getLinkString() {
		return linkString;
	}

	public void setLinkString(String linkString) {
		this.linkString = linkString;
	}

	public String getLinkTitle() {
		return linkTitle;
	}

	public void setLinkTitle(String linkTitle) {
		this.linkTitle = linkTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public IndividualLink(){
		
	}
	
	/**
	 * Saves object to the database.
	 * @param schema
	 */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	/**
	 * Retrieves object from database.
	 * 
	 * @param schema
	 * @param ac_id
	 * @return
	 */
	public static IndividualLink getMe(String schema, Long il_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		IndividualLink this_ac  = (IndividualLink) 
			MultiSchemaHibernateUtil.getSession(schema).get(IndividualLink.class, il_id);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this_ac;

	}

	
	/** Returns a set of individual links that belong in one set. */
	public static List <IndividualLink> getAllForSetOfLinks(String schema, Long set_of_links_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from IndividualLink where set_of_links_id = :set_of_links_id order by id")
				.setLong("set_of_links_id", set_of_links_id)
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	public static void main(String args[]){
		
		List linkList = IndividualLink.getAllForSetOfLinks("test", new Long(1));
		
		for (ListIterator<IndividualLink> li = linkList.listIterator(); li.hasNext();) {
			IndividualLink this_link = li.next();
			
			this_link.getLinkTitle();
			this_link.getLinkString();
			this_link.getDescription();
		}
		
	}
}
