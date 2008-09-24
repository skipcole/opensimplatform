package org.usip.oscw.specialfeatures;

import java.util.List;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;
import org.usip.oscw.baseobjects.CustomizeableSection;
import org.usip.oscw.persistence.MultiSchemaHibernateUtil;

/**
 * @author Ronald "Skip" Cole
 *
 * This file is part of the USIP Online Simulation Platform.<br>
 * 
 * The USIP Online Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Online Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "PLAYER_REFLECTIONS")
@Proxy(lazy=false)
public class PlayerReflection implements Comparable{

	/** Database id of this Variable. */
	@Id
	@GeneratedValue
	@Column(name = "PR_ID")
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
    
    /** Custom Simulation section id. */
    @Column(name = "CS_ID")
    private Long cs_id;
    
    /** Actor id. */
    @Column(name = "A_ID")
    private Long a_id;
    
    @Transient
    private String actor_name = "";

	/** Indicates if this document can still be edited. */
	private boolean editable = true;

	/** Contents of this document. */
	@Lob
	private String bigString = "";
	
	/**
	 * 
	 * @param request
	 */
	public CustomizeableSection handleMakeReflectionPage(
			HttpServletRequest request) {
		
		return null;
		
		/*
		String tab_heading = (String) session.getAttribute("tab_heading");
		String tab_pos = (String) session.getAttribute("tab_pos");
		String universal = (String) session.getAttribute("universal");

		String new_tab_heading = request.getParameter("tab_heading");
		if ((new_tab_heading != null) && (new_tab_heading.length() > 0)) {
			tab_heading = new_tab_heading;
		}

		String custom_page = request.getParameter("custom_page");

		MultiSchemaHibernateUtil.beginTransaction(schema);
		customizableSectionOnScratchPad = (CustomizeableSection) MultiSchemaHibernateUtil
				.getSession(schema).get(CustomizeableSection.class,
						new Long(custom_page));
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		// Determine if setting sim to edit.
		String sending_page = (String) request.getParameter("sending_page");

		String save_page = (String) request.getParameter("save_page");
		String save_and_add = (String) request.getParameter("save_and_add");

		if ((sending_page != null)
				&& ((save_page != null) || (save_and_add != null))

				&& (sending_page.equalsIgnoreCase("make_reflection_page"))) {
			// If this is the original custom page, make a new page

			if (!(customizableSectionOnScratchPad.isThisIsACustomizedSection())) {
				System.out.println("making copy");
				customizableSectionOnScratchPad = customizableSectionOnScratchPad
						.makeCopy(schema);
				custom_page = customizableSectionOnScratchPad.getId() + "";
			}

			// Update page values
			String make_reflection_page_text = (String) request
					.getParameter("make_reflection_page_text");
			customizableSectionOnScratchPad
					.setBigString(make_reflection_page_text);
			customizableSectionOnScratchPad.setRec_tab_heading(tab_heading);
			customizableSectionOnScratchPad.save(schema);

			if (save_and_add != null) {
				// add section
				addSectionFromProcessCustomPage(customizableSectionOnScratchPad
						.getId(), tab_pos, tab_heading, request, universal);
				// send them back
				forward_on = true;
				return customizableSectionOnScratchPad;

			}

		} // End of if this is the make_write_news_page
		
		return customizableSectionOnScratchPad;
		
		*/
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBase_id() {
		return base_id;
	}

	public void setBase_id(Long base_id) {
		this.base_id = base_id;
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

	public Long getCs_id() {
		return cs_id;
	}

	public void setCs_id(Long cs_id) {
		this.cs_id = cs_id;
	}
	
	public Long getA_id() {
		return a_id;
	}

	public void setA_id(Long a_id) {
		this.a_id = a_id;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getBigString() {
		return bigString;
	}

	public void setBigString(String bigString) {
		this.bigString = bigString;
	}
	
	public static PlayerReflection getPlayerReflection(String schema, Long cs_id, Long rs_id, Long a_id){
		
		PlayerReflection playerReflection = new PlayerReflection();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		String hql_string = "from PlayerReflection where CS_ID = " + cs_id + " AND RS_ID = " + rs_id
		+ " AND A_ID = " + a_id;
		
		System.out.println("hql_string is " + hql_string);
		
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hql_string).list();
		
		if ((returnList == null) || (returnList.size() == 0)){
			playerReflection.setCs_id(cs_id);
			playerReflection.setRs_id(rs_id);
			playerReflection.setA_id(a_id);
			MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(playerReflection);
			
		} else {
			playerReflection = (PlayerReflection) returnList.get(0);
		}
		
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		
		return playerReflection;
	}
	
	public static List getReflections(String schema, Long rs_id){
		
		PlayerReflection playerReflection = new PlayerReflection();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		String hql_string = "from PlayerReflection where RS_ID = " + rs_id;
		
		System.out.println("hql_string is " + hql_string);
		
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hql_string).list();
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
	
	public void save(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getActor_name() {
		return actor_name;
	}

	public void setActor_name(String actor_name) {
		this.actor_name = actor_name;
	}
}
