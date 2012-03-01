package org.usip.osp.coursemanagementinterface;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.Proxy;

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
public class Organization {

	@Id
	@GeneratedValue
	private Long id;
	
	private String organizationName = "";
	
	private String organizationLogo = "";
	
	private String organizationTopBanner = "";
	
	private String organizationWebPage = "";
	
	@Lob
	private String organizationNotes = "";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationLogo() {
		return organizationLogo;
	}

	public void setOrganizationLogo(String organizationLogo) {
		this.organizationLogo = organizationLogo;
	}

	public String getOrganizationTopBanner() {
		return organizationTopBanner;
	}

	public void setOrganizationTopBanner(String organizationTopBanner) {
		this.organizationTopBanner = organizationTopBanner;
	}

	public String getOrganizationWebPage() {
		return organizationWebPage;
	}

	public void setOrganizationWebPage(String organizationWebPage) {
		this.organizationWebPage = organizationWebPage;
	}

	public String getOrganizationNotes() {
		return organizationNotes;
	}

	public void setOrganizationNotes(String organizationNotes) {
		this.organizationNotes = organizationNotes;
	}
	
	
	
}
