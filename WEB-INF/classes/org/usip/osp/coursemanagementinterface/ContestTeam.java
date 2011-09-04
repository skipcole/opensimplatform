package org.usip.osp.coursemanagementinterface;

import java.util.*;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.persistence.SchemaInformationObject;

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
	
	private Long maxTeamMembers;
	
	private String teamName;
	
	private String teamDescription;
	
	private String teamNotes;
	
	private String teamImageName;
	
	private String teamWebSite;
	
	private String teamAdminRegistrationCode;
	
	private String teamStudentRegistrationCode;
	
	private Date creationDate = new java.util.Date();
	
	
	public ContestTeam() {
		
	}
	
	public ContestTeam(Long cpoId, Long contestId, String teamName){
		
		this.contestParticipatingOrgId = cpoId;
		this.contestId = contestId;
		this.teamName = teamName;
		
		this.saveMe();
		
	}
	
	/**
	 * Returns all of the contests teams for a particular organization.
	 * 
	 * @param cpoId
	 * @return
	 */
	public static List<ContestTeam> getAllForCPO(Long cpoId) {

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		List<ContestTeam> returnList = MultiSchemaHibernateUtil
				.getSession(MultiSchemaHibernateUtil.principalschema, true)
				.createQuery("from ContestTeam where contestParticipatingOrgId = :cpoId")
				.setLong("cpoId", cpoId)
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil
				.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

		return returnList;
	}
	
	public static ContestTeam handleCreateTeam(HttpServletRequest request){
		
		String sending_page = (String) request.getParameter("sending_page");
		
		ContestTeam ct = new ContestTeam();
		
		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("new_contest_team"))){
			
			String cpo_id = (String) request.getParameter("cpo_id");
			String contest_id = (String) request.getParameter("contest_id");
			String team_name = (String) request.getParameter("team_name");
			String max_players = (String) request.getParameter("max_players");
			String team_notes = (String) request.getParameter("team_notes");
			
			ct = new ContestTeam(new Long(cpo_id), new Long (contest_id), team_name);
			
		}
		
		return ct;
		
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

	public Long getMaxTeamMembers() {
		return maxTeamMembers;
	}

	public void setMaxTeamMembers(Long maxTeamMembers) {
		this.maxTeamMembers = maxTeamMembers;
	}

	public String getTeamAdminRegistrationCode() {
		return teamAdminRegistrationCode;
	}

	public void setTeamAdminRegistrationCode(String teamAdminRegistrationCode) {
		this.teamAdminRegistrationCode = teamAdminRegistrationCode;
	}

	public String getTeamStudentRegistrationCode() {
		return teamStudentRegistrationCode;
	}

	public void setTeamStudentRegistrationCode(String teamStudentRegistrationCode) {
		this.teamStudentRegistrationCode = teamStudentRegistrationCode;
	}
	
	/**
	 * Saves this object back to the main database.
	 * 
	 */
	public void saveMe() {
		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);
		MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema, true).saveOrUpdate(
				this);
		MultiSchemaHibernateUtil
				.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
	}
	
}
