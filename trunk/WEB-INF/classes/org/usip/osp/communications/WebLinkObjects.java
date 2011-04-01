package org.usip.osp.communications;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.networking.PlayerSessionObject;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This object represents a web page that the players are being directed to look
 * at.
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
@Proxy(lazy = false)
public class WebLinkObjects implements WebObject {

	/** Database id of this TimeLine. */
	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "SIM_ID")
	private Long sim_id;

	@Column(name = "RS_ID")
	private Long rs_id;

	@Column(name = "CS_ID")
	private Long cs_id;
	
	@Column(name = "U_ID")
	private Long u_id;
	
	/** This date is automatically generated when the object is posted.  */
	private java.util.Date postingDate;
	
	/** This date can be set by the player. */
	private java.util.Date webObjectDate;

	private String weblinkName = "";
	
	private String weblinkSource = "";

	private String weblinkDescription = "";

	private String weblinkURL = "";

	public WebLinkObjects() {

		this.postingDate = new java.util.Date();
		
	}

	@Transient
	private String wloTopPage = "";

	@Transient
	private String wloBottomPage = "";
	
	@Transient
	private String wloError = "";

	public String getWloError() {
		return wloError;
	}

	public void setWloError(String wloError) {
		this.wloError = wloError;
	}

	public String getWloTopPage() {
		return wloTopPage;
	}

	public void setWloTopPage(String wloTopPage) {
		this.wloTopPage = wloTopPage;
	}

	public String getWloBottomPage() {
		return wloBottomPage;
	}

	public void setWloBottomPage(String wloBottomPage) {
		this.wloBottomPage = wloBottomPage;
	}
	
	public java.util.Date getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(java.util.Date postingDate) {
		this.postingDate = postingDate;
	}

	public java.util.Date getWebObjectDate() {
		return webObjectDate;
	}

	public void setWebObjectDate(java.util.Date webObjectDate) {
		this.webObjectDate = webObjectDate;
	}
	
	public Long getU_id() {
		return u_id;
	}

	public void setU_id(Long uId) {
		u_id = uId;
	}

	/**
	 * Utility constructor.
	 * 
	 * @param schema
	 * @param name
	 * @param desc
	 * @param url
	 */
	public WebLinkObjects(String schema, String name, Date objectDate, String source, String desc, String url,
			Long rsId, Long csId, Long uId) {
		
		this.webObjectDate = objectDate;
		this.postingDate = new java.util.Date();
		this.weblinkName = name;
		this.weblinkSource = source;
		this.weblinkDescription = desc;
		this.weblinkURL = url;
		this.rs_id = rsId;
		this.cs_id = csId;
		this.u_id = uId;

		this.saveMe(schema);
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

	public String getWeblinkName() {
		return weblinkName;
	}

	public void setWeblinkName(String weblinkName) {
		this.weblinkName = weblinkName;
	}

	public String getWeblinkSource() {
		return weblinkSource;
	}

	public void setWeblinkSource(String weblinkSource) {
		this.weblinkSource = weblinkSource;
	}

	public String getWeblinkDescription() {
		return weblinkDescription;
	}

	public void setWeblinkDescription(String weblinkDescription) {
		this.weblinkDescription = weblinkDescription;
	}

	public String getWeblinkURL() {
		return weblinkURL;
	}

	public void setWeblinkURL(String weblinkURL) {
		this.weblinkURL = weblinkURL;
	}

	/**
	 * Indicates elements of information should be sent in a URL string to an
	 * external page.
	 */
	private String sendString = ""; //$NON-NLS-1$

	public String getSendString() {
		return sendString;
	}

	public void setSendString(String sendString) {
		this.sendString = sendString;
	}

	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static WebLinkObjects getById(String schema, Long wlo_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		WebLinkObjects this_wlo = (WebLinkObjects) MultiSchemaHibernateUtil
				.getSession(schema).get(WebLinkObjects.class, wlo_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this_wlo;

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

	/**
	 * Returns all of the actors found in a schema for a particular simulation
	 * 
	 * @param schema
	 * @return
	 */
	public static List getAllForRunningSimulationAndSection(String schema,
			Long rs_id, Long cs_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from WebLinkObjects where rs_id = :rs_id and cs_id = :cs_id order by id")
				.setLong("rs_id", rs_id).setLong("cs_id", cs_id).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	public static void removeMe(String schema, Long wlo_id) {

		WebLinkObjects wlo = WebLinkObjects.getById(schema, wlo_id);

		if (wlo != null) {
			MultiSchemaHibernateUtil.beginTransaction(schema);
			MultiSchemaHibernateUtil.getSession(schema).delete(wlo);
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		}
	}

	/**
	 * Handles the adding, editing, deleting and viewing of web link objects.
	 * 
	 * @param request
	 * @param pso
	 * @return
	 */
	public static void handleEdit(HttpServletRequest request,
			PlayerSessionObject pso) {

		WebLinkObjects wlo = new WebLinkObjects();

		String cs_id = (String) request.getParameter("cs_id");
		String sending_page = (String) request.getParameter("sending_page");
		String command = (String) request.getParameter("command");
		String wlo_id = (String) request.getParameter("wlo_id");

		wlo.setWloTopPage("web_link_page_top.jsp?cs_id=" + cs_id);
		wlo.setWloBottomPage("web_link_page_bottom.jsp?cs_id="
				+ cs_id);
		
		pso.wloOnScratchPad = wlo;
		
		if ((cs_id != null) && (sending_page != null)) {

			if (command != null) {

				Long csId = new Long(cs_id);

				String wlo_name = request.getParameter("wlo_name");
				String wlo_description = request
						.getParameter("wlo_description");
				String wlo_url = request.getParameter("wlo_url");
				String wlo_event_date = request.getParameter("wlo_event_date");
				
				Date wDate = dateStringToDate(wlo_event_date);
				
				String wlo_source = request.getParameter("wlo_source");

				if (command.equalsIgnoreCase("Create")) {
					
					if ((wlo_url != null) && (wlo_url.length() > 0)) {
						wlo = new WebLinkObjects(pso.schema, wlo_name, wDate, wlo_source,
							wlo_description, wlo_url, pso.getRunningSimId(), csId, pso.user_id);
					} else {
						wlo.setWloError("Not enough Information provided");
						wlo.setWeblinkDescription(wlo_description);
						wlo.setWeblinkName(wlo_name);
						wlo.setWeblinkSource(wlo_source);
					}
					
					wlo.setWloTopPage("web_link_page_top.jsp?cs_id=" + cs_id);
					wlo.setWloBottomPage("web_link_page_bottom.jsp?cs_id="
							+ cs_id);
					
					pso.wloOnScratchPad = wlo;
					return;
					
				} else if (command.equalsIgnoreCase("Update")) {
					wlo = WebLinkObjects.getById(pso.schema, new Long(wlo_id));

					if ((wlo_url != null) && (wlo_url.length() > 0)) {
						wlo.setWeblinkDescription(wlo_description);
						wlo.setWeblinkName(wlo_name);
						wlo.setWebObjectDate(wDate);
						wlo.setWeblinkSource(wlo_source);
						wlo.setWeblinkURL(wlo_url);
						wlo.saveMe(pso.schema);
						wlo.setWloTopPage("web_link_page_top.jsp?cs_id="
								+ cs_id);
						wlo.setWloBottomPage("web_link_page_bottom.jsp?cs_id="
								+ cs_id);
						pso.wloOnScratchPad = wlo;
						return;
					}
				} else if (command.equalsIgnoreCase("Delete")) {
					WebLinkObjects.removeMe(pso.schema, new Long(wlo_id));
					wlo.setWloTopPage("web_link_page_top.jsp?cs_id=" + cs_id);
					wlo.setWloBottomPage("web_link_page_bottom.jsp?cs_id="
							+ cs_id);
					pso.wloOnScratchPad = wlo;
					return;
				} else if (command.equalsIgnoreCase("Clear")) {
					wlo.setWloTopPage("web_link_page_top.jsp?cs_id=" + cs_id);
					wlo.setWloBottomPage("web_link_page_bottom.jsp?cs_id="
							+ cs_id);
					pso.wloOnScratchPad = wlo;
					return;
				} else if (command.equalsIgnoreCase("Edit")) {
					System.out.println("i am in edit mode");
					wlo.setWloTopPage("web_link_page_top.jsp?cs_id=" + cs_id);
					wlo.setWloBottomPage("web_link_page_bottom.jsp?cs_id="
							+ cs_id + "&wlo_id=" + wlo_id + "&editMode=true");
					pso.wloOnScratchPad = wlo;
					return;
				}

			}

		} else {
			if (wlo.getId() != null) {
				wlo_id = wlo.getId() + "";
			}
		}

		wlo.setWloTopPage("web_link_page_top.jsp?cs_id=" + cs_id);
		wlo.setWloBottomPage("web_link_page_bottom.jsp?cs_id=" + cs_id);

		if ((wlo_id != null) && (!(wlo_id.equalsIgnoreCase("")))
				&& (!(wlo_id.equalsIgnoreCase("0")))) {
			wlo = WebLinkObjects.getById(pso.schema, new Long(wlo_id));

			wlo.setWloBottomPage(wlo.getWeblinkURL());

			String setTo = "web_link_page_top.jsp?cs_id=" + cs_id + "&wlo_id="
					+ wlo_id;

			wlo.setWloTopPage(setTo);
			pso.wloOnScratchPad = wlo;
			return;
		}

		return;
	}
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
	
	/**
	 * 
	 * @param inputString
	 * @return
	 */
	public static Date dateStringToDate(String inputString){
		
		Date returnDate = new Date();
		
		if ((inputString == null) || (inputString.trim().length() == 0)){
			return returnDate;
		}

		try {
			returnDate = sdf.parse(inputString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return returnDate;
	}
	
	public String getWLODateFormattedForWeb(){
		
		if (this.webObjectDate != null){
			return sdf.format(this.webObjectDate);
		} else {
			return "";
		}
	}

}
