package org.usip.osp.specialfeatures;

import java.util.List;
import java.util.ListIterator;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.baseobjects.SimSectionDependentObject;
import org.usip.osp.baseobjects.core.OneLinkCustomizer;
import org.usip.osp.networking.AuthorFacilitatorSessionObject;
import org.usip.osp.networking.PlayerSessionObject;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents a 'one link' web address associated with a simulation.
 */
/*
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
@Proxy(lazy = false)
public class OneLink implements SimSectionDependentObject{
	
	/**
	 * Zero argument constructor required by hibernate.
	 */
	public OneLink(){
		
	}
	
	public OneLink(String name, Long sim_id){
		this.name = name;
		this.sim_id = sim_id;
		
	}
	


	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Id of the base from which this copy is made. */
    @Column(name = "BASE_ID")
    private Long base_id;
	
	/** Simulation id. */
    @Column(name = "SIM_ID")
    private Long sim_id;
    
    /** Running simulation id. */
    @Column(name = "RS_ID")
    private Long rs_id;
    
    private String name = ""; //$NON-NLS-1$
    
    private String notes = ""; //$NON-NLS-1$
	
	public static final String PLACEHOLDER_LOCATION = "../osp_core/onelink_placeholder.jsp";
    
    private String startingValue = PLACEHOLDER_LOCATION; //$NON-NLS-1$
    

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getStartingValue() {
		return startingValue;
	}

	public void setStartingValue(String startingValue) {
		this.startingValue = startingValue;
	}

	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransitId() {
		return this.transit_id;
	}

	public void setTransitId(Long transit_id) {
		this.transit_id = transit_id;
	}
    
    /** If a value has been set for this variable, this is the id allowable response holding the answer. */
    private Long currentlySelectedResponse;
	

	public Long getCurrentlySelectedResponse() {
		return this.currentlySelectedResponse;
	}

	public void setCurrentlySelectedResponse(Long currentlySelectedResponse) {
		this.currentlySelectedResponse = currentlySelectedResponse;
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

	public Long getBase_id() {
		return this.base_id;
	}

	public void setBase_id(Long base_id) {
		this.base_id = base_id;
	}

	public Long getRs_id() {
		return this.rs_id;
	}

	public void setRs_id(Long rs_id) {
		this.rs_id = rs_id;
	}

	/**
	 * Saves this object to the database.
	 * @param schema
	 */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static OneLink getOneLinkForRunningSim(String schema, Long olId, Long rsId) {

		OneLink this_gv = null;
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		String hql_string = "from OneLink where RS_ID = :rsId AND BASE_ID = :olId"; //$NON-NLS-1$ //$NON-NLS-2$
		
		List varFound = MultiSchemaHibernateUtil.getSession(schema)
		.createQuery(hql_string)
		.setLong("rsId", rsId)
		.setLong("olId", olId)
		.list();
		
		if (varFound == null){
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
			return this_gv;
		}
		
		if (varFound.size() > 1){
			Logger.getRootLogger().warn("More than one generic variable copy found"); //$NON-NLS-1$
		}
		
		
		for (ListIterator<OneLink> li = varFound.listIterator(); li.hasNext();) {
			this_gv = li.next();
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this_gv;

	}
	
	/**
	 * Checks to see if the OneLink has been modified, and returns the original or modified version.
	 * 
	 * @param request
	 * @param schema
	 * @param ol
	 * @return
	 */
	public static OneLink checkForRunningSimOneLinkUpdate(HttpServletRequest request, String schema, OneLink ol){
		
		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null) && (  sending_page.equalsIgnoreCase("set_one_link")  ) ) {
			String newValue = (String) request.getParameter("new_value");
		
			if (newValue != null) {
				ol.setStartingValue(newValue);
				ol.saveMe(schema);
			}
		}
		
		return ol;
	}
	
	public String generateForwardOnTag(){
		
		String tagString = "";
		
		if (!(this.startingValue.equalsIgnoreCase(PLACEHOLDER_LOCATION))){
			tagString = "<META http-equiv=\"refresh\" content=\"0;URL=" + startingValue + "\"> ";
		}
		
		return tagString;
	}
	
	/**
	 * Retrieves object from database.
	 * 
	 * @param schema
	 * @param gv_id
	 * @return
	 */
	public static OneLink getById(String schema, Long gv_id) {

		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		OneLink this_gv  = (OneLink) MultiSchemaHibernateUtil
				.getSession(schema).get(OneLink.class, gv_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this_gv;

	}
		
		
	
	/**
	 * This returns the 'base' one links for a simulation. A 'base' generic variable is the archetypal 
	 * generic variable
	 * that is copied into a version to be edited in a particular running simulation. When the base
	 * generic variable is copied into a copy generic variable, its id is copied into the base_id of the copy. So 
	 * the original 'base' generic variables have their BASE_ID null, and copies have in values in that field.
	 * @param hibernate_session
	 * @param the_sim_id
	 * @return
	 */
	public static List getAllBaseOneLinksForSim(String schema, Long simId) {
		
		String hql_string = "from OneLink where SIM_ID = :simId AND RS_ID is null"; //$NON-NLS-1$
	
		MultiSchemaHibernateUtil.beginTransaction(schema);
		List returnList = MultiSchemaHibernateUtil.getSession(schema)
			.createQuery(hql_string)
			.setLong("simId", simId)
			.list();
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
	/**
	 * This returns the 'base' one links for a simulation. A 'base' generic variable is the archetypal 
	 * generic variable
	 * that is copied into a version to be edited in a particular running simulation. When the base
	 * generic variable is copied into a copy generic variable, its id is copied into the base_id of the copy. So 
	 * the original 'base' generic variables have their BASE_ID null, and copies have in values in that field.
	 * @param hibernate_session
	 * @param the_sim_id
	 * @return
	 */
	public static List getAllForSim(String schema, Long the_sim_id) {
		
		String hql_string = "from OneLink where SIM_ID = " + the_sim_id.toString();  //$NON-NLS-1$
	
		MultiSchemaHibernateUtil.beginTransaction(schema);
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hql_string).list();
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
	/**
	 * Creates, and saves to the database, a copy of this one link object. This is used to create a version 
	 * of a variable for a particular running simulation session. The copied variable
	 * will have the base_id field filled in with the id of the original.
	 * 
	 * @param rsid Running simulation id.
	 * @param hibernate_session Hibernate session created targetting the appropriate schema for saving into.
	 * @return The generic variable object created.
	 * 
	 */
	public OneLink createCopy(Long rsid,  String schema){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		OneLink gv = new OneLink();
		
		gv.setBase_id(this.getId());
		gv.setRs_id(rsid);
		gv.setSim_id(this.getSim_id());
		gv.setStartingValue(this.getStartingValue());
		
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(gv);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return gv;
	}
	

	@Override
	public Long createRunningSimVersion(String schema, Long sim_id, Long rs_id, Object templateObject) {

		Logger.getRootLogger().warn("Creating generic variable for running sim : " + rs_id); //$NON-NLS-1$

		OneLink templateOL = (OneLink) templateObject;

		OneLink ol = new OneLink();
		
		ol.setName(templateOL.getName());
		ol.setNotes(templateOL.getNotes());
		ol.setSim_id(templateOL.getSim_id());
		ol.setBase_id(templateOL.getId());
		
		// Set value to starting value
		ol.setStartingValue(this.getStartingValue());
		
		ol.setRs_id(rs_id);
		
		ol.saveMe(schema);
		
		return ol.getId();
	}

	@Override
	public void setSimId(Long theId) {
		this.sim_id = theId;
		
	}
	
	/**
	 * Handles the creation of a one link item.
	 * 
	 * @param request
	 * @return
	 */
	public static String handleOneLink(HttpServletRequest request, PlayerSessionObject pso) {

		String cs_id = (String) request.getParameter("cs_id");

		OneLinkCustomizer olc = new OneLinkCustomizer();

		CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
		olc = new OneLinkCustomizer(request, pso, cs);

		String forwardOnString = "";

		OneLink ol = OneLink.getById(pso.schema, olc.getOlId());

		if (!(pso.preview_mode)) {
			ol = OneLink.getOneLinkForRunningSim(pso.schema, olc.getOlId(),
					pso.getRunningSimId());
			forwardOnString = ol.generateForwardOnTag();
		}

		return forwardOnString;
	}

	
}
