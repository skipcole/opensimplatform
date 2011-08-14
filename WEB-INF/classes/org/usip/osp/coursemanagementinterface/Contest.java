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
public class Contest {

	public static final int CONTEST_INITIATED = 0;
	public static final int CONTEST_IN_REGISTRATION = 1;
	public static final int CONTEST_BEGUN = 2;
	public static final int CONTEST_CLOSED = 3;
	public static final int CONTEST_IN_JUDGEMENT_PHASE = 4;
	public static final int CONTEST_WINNER_ANNOUNCEMENT = 5;
	public static final int CONTEST_OVER = 6;
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String contestName;
	
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
	
}
