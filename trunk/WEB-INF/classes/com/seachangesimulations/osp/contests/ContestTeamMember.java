package com.seachangesimulations.osp.contests;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;
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
 */
@Entity
@Proxy(lazy = false)
public class ContestTeamMember {

	@Id
	@GeneratedValue
	private Long id;
	
	private Long userId;
	
	private Long contestTeamId;
	
	private Long contestId;
	
	private Long contestParticipatingOrgId;
	
	private boolean teamAdministrator;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getContestTeamId() {
		return contestTeamId;
	}

	public void setContestTeamId(Long contestTeamId) {
		this.contestTeamId = contestTeamId;
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

	public boolean isTeamAdministrator() {
		return teamAdministrator;
	}

	public void setTeamAdministrator(boolean teamAdministrator) {
		this.teamAdministrator = teamAdministrator;
	}
	
	public static List<ContestTeamMember> getAllTeam(Long ctId) {

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		List<ContestTeamMember> returnList = MultiSchemaHibernateUtil
				.getSession(MultiSchemaHibernateUtil.principalschema, true)
				.createQuery("from ContestTeamMember where x = :cpoId")
				.setLong("cpoId", ctId)
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil
				.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

		return returnList;
	}
	
	
}
