package org.usip.osp.coursemanagementinterface;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Actor;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.persistence.BaseUser;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

import com.oreilly.servlet.MultipartRequest;

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
public class Contest {

	public static final int CONTEST_CREATED = 0;
	public static final int CONTEST_IN_REGISTRATION = 1;
	public static final int CONTEST_BEGUN = 2;
	public static final int CONTEST_CLOSED = 3;
	public static final int CONTEST_IN_JUDGEMENT_PHASE = 4;
	public static final int CONTEST_WINNER_ANNOUNCEMENT = 5;
	public static final int CONTEST_OVER = 6;

	/** Zero argument constructor required by Hibernate */
	public Contest() {

	}

	/**
	 * Utility constructor
	 * 
	 * @param contestName
	 */
	public Contest(String contestName) {
		this.contestName = contestName;
		this.contestState = Contest.CONTEST_CREATED;
		this.saveMe();
	}

	@Id
	@GeneratedValue
	private Long id;

	private String contestName = "";

	private String contestLogo = "";

	@Lob
	private String contestShortDescription = "";
	
	@Lob
	private String contestDescription = "";
	
	@Lob
	private String contestSecondPageInformation = "";

	@Lob
	private String contestNotes = "";

	private int contestState;

	private boolean registrationOpen = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContestName() {
		return contestName;
	}

	public void setContestName(String contestName) {
		this.contestName = contestName;
	}

	public String getContestLogo() {
		return contestLogo;
	}

	public void setContestLogo(String contestLogo) {
		this.contestLogo = contestLogo;
	}

	public String getContestDescription() {
		return contestDescription;
	}

	public void setContestDescription(String contestDescription) {
		this.contestDescription = contestDescription;
	}

	public String getContestNotes() {
		return contestNotes;
	}

	public void setContestNotes(String contestNotes) {
		this.contestNotes = contestNotes;
	}

	public int getContestState() {
		return contestState;
	}

	public void setContestState(int contestState) {
		this.contestState = contestState;
	}

	public boolean isRegistrationOpen() {
		return registrationOpen;
	}

	public void setRegistrationOpen(boolean registrationOpen) {
		this.registrationOpen = registrationOpen;
	}

	/**
	 * Gets a contest based on the contest id passed in, or if there was no contest
	 * id passed in, tries to return the first contest found. If a contest id of "0"
	 * has been passed in, it just returns a new contest.
	 * 
	 * @param request
	 * @return
	 */
	public static Contest getContest(HttpServletRequest request) {

		String contest_id = request.getParameter("contest_id");

		Contest contest = new Contest();
		
		if ((contest_id == null) || (contest_id.equalsIgnoreCase("null"))
				|| (contest_id.length() == 0)) {
			return getFirstContest();
		} else {
			
			if (contest_id.equalsIgnoreCase("0")){
				return new Contest();
			}
			
			try {
				Long cId = new Long(contest_id);
				contest = Contest.getById(cId);
			} catch (Exception e){
				e.printStackTrace();
			}
		}

		return contest;
	}

	/**
	 * Returns the first contest found in the database.
	 * 
	 * @return
	 */
	public static Contest getFirstContest() {
		List contests = getAll();

		if ((contests != null) && (contests.size() > 0)) {
			return (Contest) contests.get(0);
		} else {
			return new Contest();
		}
	}

	/**
	 * Returns all contests found on the platform.
	 * 
	 * @return
	 */
	public static List<Contest> getAll() {

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		List returnList = MultiSchemaHibernateUtil
				.getSession(MultiSchemaHibernateUtil.principalschema, true)
				.createQuery("from Contest").list(); //$NON-NLS-1$ 

		MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema, true).close();

		return returnList;
	}

	/**
	 * Pulls the Contest out of the root database base on its id.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static Contest getById(Long contestId) {

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		Contest contest = (Contest) MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema, true).get(
				Contest.class, contestId);

		MultiSchemaHibernateUtil
				.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

		return contest;
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
	
	public static void setContestLogoImage(MultipartRequest mpr, Contest contestOnScratchPad) {


	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public static Contest handleCreateContest(HttpServletRequest request) {
		
		Contest contest = new Contest();

		String contest_name = (String) request.getParameter("contest_name");
		String sending_page = (String) request.getParameter("sending_page");
		
		String contest_id = (String) request.getParameter("contest_id");
		
		if (USIP_OSP_Util.stringFieldHasValue(contest_id)){
			contest = Contest.getById(new Long(contest_id));
		}
		
		
		boolean saveChanges = false;
		
		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("edit_contest"))){
			saveChanges = true;
			
		}
		
		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("create_contest"))
				&& (contest_name != null) && (contest_name.length() > 0)) {
			contest = new Contest(contest_name);
			
			saveChanges = true;

		}
		
		if (saveChanges){
			
			String short_description = (String) request.getParameter("short_description");
			String description = (String) request.getParameter("description");
			
			contest.setContestShortDescription(short_description);
			contest.setContestDescription(description);
			contest.saveMe();
		}
		
		return contest;

	}

	public String getContestShortDescription() {
		return contestShortDescription;
	}

	public void setContestShortDescription(String contestShortDescription) {
		this.contestShortDescription = contestShortDescription;
	}

	public String getContestSecondPageInformation() {
		return contestSecondPageInformation;
	}

	public void setContestSecondPageInformation(String contestSecondPageInformation) {
		this.contestSecondPageInformation = contestSecondPageInformation;
	}
	
	
	

}
