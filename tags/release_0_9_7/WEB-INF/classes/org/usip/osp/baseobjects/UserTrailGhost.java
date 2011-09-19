package org.usip.osp.baseobjects;

import java.util.*;

import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.apache.log4j.*;

/**
 * The 'UserTrailGhost' and 'UserTrail' work together to keep track of and
 * record when players log onto the system. The 'LoggedInTicket' is not
 * persisted to the database. It recieves The 'UserTrail' object is persisted to
 * the database.
 */ 
 /* This file is part of the USIP Open Simulation Platform.<br>
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
public class UserTrailGhost {

	/** */
	private Long trail_id;

	private Long user_id;

	private Long running_sim_id;

	private Long actor_id;

	private Long tab_position;

	private Date firstLoggedIn;

	private Date lastHeartBeatPulse;

	public UserTrailGhost() {

	}

	/**
	 * Hears heartbeat and if more than a minute has passed, records the time in
	 * the database.
	 * 
	 * @param schema
	 */
	public void hearHeartBeat(String schema) {

		Date timeNow = new Date();

		Logger.getRootLogger().debug("time: " + timeNow.getTime()); //$NON-NLS-1$

		if (this.lastHeartBeatPulse == null) {
			this.lastHeartBeatPulse = new Date();
		}

		Logger.getRootLogger().debug("lastHeartBeatPulse : " + this.lastHeartBeatPulse.getTime()); //$NON-NLS-1$

		if (timeNow.getTime() > ((1000 * 1 * 60) + this.lastHeartBeatPulse.getTime())) {

			if (this.getTrail_id() != null) {
				Logger.getRootLogger().debug("time  saved: " + timeNow.getTime()); //$NON-NLS-1$

				UserTrail ut = UserTrail.getById(schema, this.getTrail_id());
				ut.setEndSessionDate(timeNow);
				ut.saveMe(schema);
			} else {
				// TODO Getting error because of this, and I'm not sure how it is happening - race condition?
				Logger.getRootLogger().warn("UserTrailGhost id is null "); //$NON-NLS-1$
			}

		}

		this.lastHeartBeatPulse = new Date();

	}

	/**
	 * Returns the LoggedInTicket based on its id.
	 * 
	 * @param trail_id
	 * @param setOfUsers
	 * @return
	 */
	public static UserTrailGhost lookupUserTrailGhost(Long trail_id, Vector setOfUsers) {

		for (Enumeration e = setOfUsers.elements(); e.hasMoreElements();) {
			UserTrailGhost lit = (UserTrailGhost) e.nextElement();

			if (lit.trail_id.compareTo(trail_id) == 0) {
				return lit;
			}

		}

		return null;

	}

	/**
	 * Persists the information in this LoggedInTicket to a UserTrail object,
	 * and returns the id of that trail.
	 * 
	 * @param schema
	 * @return
	 */
	public Long storeLoginInformationGetTrailID(String schema) {

		UserTrail ut = new UserTrail();

		ut.setUser_id(this.user_id);

		ut.setLoggedInDate(new java.util.Date());
		ut.setEndSessionDate(ut.getLoggedInDate());

		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(ut);

		// Get the id of this user trail, to use for updating with heartbeats.
		this.trail_id = ut.getId();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this.trail_id;
	}

	public Long getRunning_sim_id() {
		return this.running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public Long getUser_id() {
		return this.user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getActor_id() {
		return this.actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public Long getTab_position() {
		return this.tab_position;
	}

	public void setTab_position(Long tab_position) {
		this.tab_position = tab_position;
	}

	public Date getFirstLoggedIn() {
		return this.firstLoggedIn;
	}

	public void setFirstLoggedIn(Date firstLoggedIn) {
		this.firstLoggedIn = firstLoggedIn;
	}

	public Date getLastHeartBeatPulse() {
		return this.lastHeartBeatPulse;
	}

	public void setLastHeartBeatPulse(Date lastHeartBeatPulse) {
		this.lastHeartBeatPulse = lastHeartBeatPulse;
	}

	public Long getTrail_id() {
		return this.trail_id;
	}

	public void setTrail_id(Long trail_id) {
		this.trail_id = trail_id;
	}

	public void recordLogout(String schema) {
		
		UserTrail ut = UserTrail.getById(schema, this.trail_id);

		java.util.Date today = new java.util.Date();
		ut.setEndSessionDate(today);
		ut.setLoggedOut(today);
		ut.setActuallyLoggedOut(true);
		ut.saveMe(schema);
		
	}

}
