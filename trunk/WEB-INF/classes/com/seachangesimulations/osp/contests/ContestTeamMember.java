package com.seachangesimulations.osp.contests;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.SimulationPhase;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.baseobjects.User;
import org.usip.osp.persistence.BaseUser;
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

	private java.util.Date creationDate = new java.util.Date();

	private boolean confirmed = false;

	public ContestTeamMember() {

	}

	public ContestTeamMember(Long userId, Long contestId, Long cpoId,
			Long ctId, boolean teamAdmin, boolean confirmed) {

		this.userId = userId;
		this.contestId = contestId;
		this.contestParticipatingOrgId = cpoId;
		this.contestTeamId = ctId;
		this.teamAdministrator = teamAdmin;
		this.creationDate = new java.util.Date();

		this.confirmed = confirmed;

		this.saveMe();
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public static String handleContestTeamAddition(HttpServletRequest request) {

		String returnString = "";
		
		String sending_page = (String) request.getParameter("sending_page");
		String contest_id = (String) request.getParameter("contest_id");
		String cpo_id = (String) request.getParameter("cpo_id");
		String ct_id = (String) request.getParameter("ct_id");
		String team_admin = (String) request.getParameter("team_admin");

		if (USIP_OSP_Util.stringFieldHasValue(ct_id)
				&& USIP_OSP_Util.stringFieldHasValue(cpo_id)
				&& USIP_OSP_Util.stringFieldHasValue(sending_page)
				&& (sending_page.equalsIgnoreCase("new_contest_member"))) {

			String user_email = (String) request.getParameter("user_email");

			BaseUser bu = null;
			Long contestId = null;
			Long cpoId = null;
			Long ctId = null;
			boolean isAdmin = false;

			try {
				bu = BaseUser.getByUsername(user_email);

				if (bu == null) {
					return ("user with user name " + user_email + " not found.");
				}

				contestId = new Long(contest_id);
				cpoId = new Long(cpo_id);
				ctId = new Long(ct_id);
				if ((team_admin != null) && (team_admin.equalsIgnoreCase("true"))) {
					isAdmin = true;
				}
				
				ContestTeam ct = ContestTeam.getById(ctId);
				
				ContestTeamMember existingMember = teamHasUser(ct, bu.getId());
				if (existingMember == null){
					ContestTeamMember ctm = new ContestTeamMember(bu.getId(),
							contestId, cpoId, ctId, isAdmin, true);
					returnString = "added " + user_email + " to team";
					
				} else {
					existingMember.setTeamAdministrator(isAdmin);
					existingMember.saveMe();
					returnString = user_email + " already found. admin set to " + isAdmin;
				}
				
				// Make it so this user can author simulations and conduct them.
				
				User user = User.getById(ct.getTeamSchema(), bu.getId());
				user.setSim_author(true);
				user.setSim_instructor(true);
				user.saveMe(ct.getTeamSchema());

			} catch (Exception e) {
				e.printStackTrace();
				return "error in attempting to add " + user_email;
			}

			return returnString;

		} else {
			return "";
		}
	}
	
	/** Returns true if a member with ID was found. */
	public static ContestTeamMember teamHasUser(ContestTeam ct, Long userId){
		
		List teamMembers = getAllTeamMembers(ct.getId());
		
		for (ListIterator li = teamMembers.listIterator(); li.hasNext();) {
			ContestTeamMember ctm = (ContestTeamMember) li.next();
			
			if (ctm.getUserId().intValue() == userId.intValue()){
				return ctm;
			}
		}
		
		return null;
	}

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

	public java.util.Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(java.util.Date creationDate) {
		this.creationDate = creationDate;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	/**
	 * 
	 * @param ctId
	 * @return
	 */
	public static List<ContestTeamMember> getAllTeamMembers(Long ctId) {

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		List<ContestTeamMember> returnList = MultiSchemaHibernateUtil
				.getSession(MultiSchemaHibernateUtil.principalschema, true)
				.createQuery(
						"from ContestTeamMember where contestTeamId = :ctId")
				.setLong("ctId", ctId).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil
				.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

		return returnList;
	}

	/**
	 * Gets a list of all of the confirmed, or none confirmed, team members.
	 * 
	 * @param ctId
	 * @param confirmed
	 * @return
	 */
	public static List<ContestTeamMember> getAllConfirmedTeamMembers(Long ctId,
			boolean confirmed) {

		List<ContestTeamMember> returnList = new ArrayList();

		List<ContestTeamMember> initialList = getAllTeamMembers(ctId);

		for (ListIterator li = initialList.listIterator(); li.hasNext();) {
			ContestTeamMember ctm = (ContestTeamMember) li.next();

			if (ctm.isConfirmed() == confirmed) {
				returnList.add(ctm);
			}
		}

		return returnList;

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

	/**
	 * Handles the request to mark this user as a sim authoring team member.
	 */
	public static void handleConfirmUser(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");

		if (((USIP_OSP_Util.stringFieldHasValue(sending_page))
				&& sending_page.equalsIgnoreCase("confirm_contest_member"))) {
			
			String ctm_id = (String) request.getParameter("ctm_id");
			ContestTeamMember ctm = ContestTeamMember.getById(new Long(ctm_id));

			ContestTeamMember.confirmContestMember(ctm);

		}

	}

	/**
	 * This confirms user as a sim authoring team member, and gives them
	 * permission to author and instruct simulations.
	 * 
	 * @param ctm
	 */
	public static void confirmContestMember(ContestTeamMember ctm) {
		ContestTeam ct = ContestTeam.getById(ctm.getContestTeamId());

		User user = User.getById(ct.getTeamSchema(), ctm.getUserId());

		user.setSim_author(true);
		user.setSim_instructor(true);

		user.saveMe(ct.getTeamSchema());
		
		ctm.confirmed = true;
		ctm.saveMe();
	}

	/**
	 * Pulls the ContestTeam out of the root database base on its id.
	 * 
	 * @param ctmId
	 * @return
	 */
	public static ContestTeamMember getById(Long ctmId) {

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		ContestTeamMember ctm = (ContestTeamMember) MultiSchemaHibernateUtil
				.getSession(MultiSchemaHibernateUtil.principalschema, true)
				.get(ContestTeamMember.class, ctmId);

		MultiSchemaHibernateUtil
				.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

		return ctm;
	}

}
