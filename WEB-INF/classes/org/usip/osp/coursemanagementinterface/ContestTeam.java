package org.usip.osp.coursemanagementinterface;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;

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
public class ContestTeam {

	@Id
	@GeneratedValue
	private Long id;
	
	private Long contestId;
	
	private Long contestParticipatingOrgId;
	
	private String teamName;
	
	private String teamDescription;
	
	private String teamNotes;
	
	private String teamImageName;
	
	private String teamWebSite;
	
	private String teamRegistrationCode;
	
	
	public ContestTeam() {
		
	}
	
	public static ContestTeam handleCreateTeam(HttpServletRequest request){
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getContestId() {
		return contestId;
	}

	public void setContestId(Long contestId) {
		this.contestId = contestId;
	}

	public Long getContestParticipatingOrgId() {
		return contestParticipatingOrgId;
	}

	public void setContestParticipatingOrgId(Long contestParticipatingOrgId) {
		this.contestParticipatingOrgId = contestParticipatingOrgId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamDescription() {
		return teamDescription;
	}

	public void setTeamDescription(String teamDescription) {
		this.teamDescription = teamDescription;
	}

	public String getTeamNotes() {
		return teamNotes;
	}

	public void setTeamNotes(String teamNotes) {
		this.teamNotes = teamNotes;
	}

	public String getTeamImageName() {
		return teamImageName;
	}

	public void setTeamImageName(String teamImageName) {
		this.teamImageName = teamImageName;
	}

	public String getTeamWebSite() {
		return teamWebSite;
	}

	public void setTeamWebSite(String teamWebSite) {
		this.teamWebSite = teamWebSite;
	}

	public String getTeamRegistrationCode() {
		return teamRegistrationCode;
	}

	public void setTeamRegistrationCode(String teamRegistrationCode) {
		this.teamRegistrationCode = teamRegistrationCode;
	}
	
	
}
