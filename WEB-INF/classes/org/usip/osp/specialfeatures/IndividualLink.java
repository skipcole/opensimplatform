package org.usip.osp.specialfeatures;

import java.util.List;
import java.util.ListIterator;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.networking.PlayerSessionObject;
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
	
    /** Unique id of this link. */
	@Id @GeneratedValue
    private Long id;
	
	private Long sim_id;
	
	private Long cs_id;
	
	private Long running_sim_id;
	
	private Long running_sim_set_id;
	
    private boolean isAssociatedWithSetOfRunningSims = false;
    
	public boolean isAssociatedWithSetOfRunningSims() {
		return isAssociatedWithSetOfRunningSims;
	}

	public void setAssociatedWithSetOfRunningSims(
			boolean isAssociatedWithSetOfRunningSims) {
		this.isAssociatedWithSetOfRunningSims = isAssociatedWithSetOfRunningSims;
	}
	
	public Long getRunning_sim_set_id() {
		return running_sim_set_id;
	}

	public void setRunning_sim_set_id(Long runningSimSetId) {
		running_sim_set_id = runningSimSetId;
	}

	public Long getCs_id() {
		return cs_id;
	}

	public void setCs_id(Long csId) {
		cs_id = csId;
	}
	
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

	/** Returns a set of individual links that belong in one set if running sims on one particular section. */
	public static List <IndividualLink> getAllForSetOfRunningSims(String schema, Long rss_id, Long cs_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from IndividualLink where set_of_links_id = :set_of_links_id " +
				"running_sim_set_id := running_sim_set_id AND " +
				"cs_id := cs_id " + 
				"order by id")
				.setLong("running_sim_set_id", rss_id)
				.setLong("cs_id", cs_id)
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
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
	
	/** Handles the edit of the links. */
	public static IndividualLink handleEdit (HttpServletRequest request, PlayerSessionObject pso, Long sol_id){
		
		IndividualLink individualLink = new IndividualLink();
		
		String queueup = request.getParameter("queueup");
		String il_id = request.getParameter("il_id"); 
		
		if ((queueup != null) && (queueup.equalsIgnoreCase("true")) && (il_id != null) && (il_id.trim().length() > 0)) {		
			individualLink = IndividualLink.getMe(pso.schema, new Long(il_id));
		}
			
		String sending_page = request.getParameter("sending_page"); 
		
		
		if ((sending_page != null) && (sending_page.equalsIgnoreCase("setoflinks_control"))){
			
			String create_il = request.getParameter("create_il");
			String clear_il = request.getParameter("clear_il");
			String update_il = request.getParameter("update_il");
			
			if (clear_il != null) {
				// Do nothing
			} else {

				if (update_il != null) {
					individualLink = IndividualLink.getMe(pso.schema, new Long(il_id));
				}
				
				if 	((update_il != null)  || (create_il != null) ) {	
					String link_title = request.getParameter("link_title"); 
					String link_string = request.getParameter("link_string");
					String link_desc = request.getParameter("link_desc");
					String cs_id = request.getParameter("cs_id");
					String rss_id = request.getParameter("rss_id");
			
					individualLink.setActor_id(pso.actor_id);
					individualLink.setDescription(link_desc);
					individualLink.setLinkString(link_string);
					individualLink.setLinkTitle(link_title);
					individualLink.setRunning_sim_id(pso.running_sim_id);
					individualLink.setCs_id(USIP_OSP_Util.stringToLong(cs_id));
					individualLink.setRunning_sim_set_id(USIP_OSP_Util.stringToLong(rss_id));
					
					individualLink.setSim_id(pso.sim_id);
			
					individualLink.saveMe(pso.schema);
			
				}
			} // end of 'else'
		}

		return individualLink;
	}
	
}
