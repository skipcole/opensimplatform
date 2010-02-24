package org.usip.osp.communications;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;

/**
 * This object represents a web page that the players are being directed to look at.
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
 * 
 */
@Entity
@Proxy(lazy = false)
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class WebLinkObjects implements WebObject{


	/** Database id of this TimeLine. */
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "SIM_ID")
	private Long sim_id;

	@Column(name = "RS_ID")
	private Long rs_id;
	
	private String weblinkName = "";
	
	private String weblinkDescription = "";
	
	private String weblinkURL = "";

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

	public String getWeblinkName() {
		return weblinkName;
	}

	public void setWeblinkName(String weblinkName) {
		this.weblinkName = weblinkName;
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
	
}
