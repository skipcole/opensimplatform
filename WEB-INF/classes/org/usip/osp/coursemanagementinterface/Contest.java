package org.usip.osp.coursemanagementinterface;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Simulation;
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
public class Contest {

	public static final int CONTEST_CREATED = 0;
	public static final int CONTEST_IN_REGISTRATION = 1;
	public static final int CONTEST_BEGUN = 2;
	public static final int CONTEST_CLOSED = 3;
	public static final int CONTEST_IN_JUDGEMENT_PHASE = 4;
	public static final int CONTEST_WINNER_ANNOUNCEMENT = 5;
	public static final int CONTEST_OVER = 6;
	
	/** Zero argument constructor required by Hibernate */
	public Contest(){
		
	}
	
	/** Utility constructor
	 * 
	 * @param contestName
	 */
	public Contest(String contestName){
		this.contestName = contestName;
		this.contestState = Contest.CONTEST_CREATED;
		this.saveMe();
	}
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String contestName;
	
	private String contestLogo;
	
	@Lob
	private String contestDescription;
	
	@Lob
	private String contestNotes;
	
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
	 * Returns all contests found on the platform.
	 * 
	 * @param schema
	 * @return
	 */
	public static List getAll(String schema) {

        MultiSchemaHibernateUtil.beginTransaction(
                MultiSchemaHibernateUtil.principalschema, true);
		
        List returnList = MultiSchemaHibernateUtil.getSession(
                MultiSchemaHibernateUtil.principalschema, true).createQuery(
                "from Contest")
                .list(); //$NON-NLS-1$ 

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
    public void saveMe(){
        MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema, true);
        MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema, true).saveOrUpdate(this);                        
        MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
    }
	
}
