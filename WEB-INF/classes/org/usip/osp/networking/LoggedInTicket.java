package org.usip.osp.networking;

import java.util.*;

import org.usip.osp.baseobjects.BaseSimSection;
import org.usip.osp.baseobjects.UserTrail;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 *
 * 
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can
 * redistribute it and/or modify it under the terms of the new BSD Style license
 * associated with this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. <BR>
 * 
 */
public class LoggedInTicket {

	/** */
	private Long trail_id;
	
	private Long user_id;
	
	private Long running_sim_id;
	
	private Long actor_id;
	
	private Long tab_position;
	
	private Date firstLoggedIn;
	
	private Date lastHeartBeatPulse;
	
	private int internalCount = 0;
	
	public LoggedInTicket() {
		
	}
	
    public void hearHeartBeat(String schema){
    	
    	Date timeNow = new Date();

    	System.out.println("time: " + timeNow.getTime());
    	
    	if (lastHeartBeatPulse == null){
    		lastHeartBeatPulse = new Date();
    	}
    	
    	System.out.println("lastHeartBeatPulse : " + lastHeartBeatPulse.getTime());
    	
    	if (timeNow.getTime() > ((1000 * 1 * 60) + lastHeartBeatPulse.getTime())){
    		
    		System.out.println("time  saved: " + timeNow.getTime());
    		
    		UserTrail ut = UserTrail.getMe(schema, this.getTrail_id());
    		ut.setEndSessionDate(timeNow);
    		ut.saveMe(schema);

    	}
        
        lastHeartBeatPulse = new Date();
        
    }
	
	public static LoggedInTicket lookupLoggedInTicket(Long trail_id, Vector setOfUsers){
		
		for (Enumeration e = setOfUsers.elements(); e.hasMoreElements();){
			LoggedInTicket lit = (LoggedInTicket) e.nextElement();
			
			if (lit.trail_id.compareTo(trail_id) == 0){
				return lit;
			}
			
		}
		
		return null;
		
		
	}
	
	/**
	 * 
	 * @param schema
	 * @return
	 */
	public Long storeLoginInformationGetTrailID(String schema){
		
		UserTrail ut = new UserTrail();
	
		ut.setUser_id(user_id);

		ut.setLoggedInDate(new java.util.Date());
		ut.setEndSessionDate(ut.getLoggedInDate());
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(ut);
		
		//Get the id of this user trail, to use for updating with heartbeats.
		this.trail_id = ut.getId();
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return this.trail_id;
	}
	

	public Long getRunning_sim_id() {
		return running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getActor_id() {
		return actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public Long getTab_position() {
		return tab_position;
	}

	public void setTab_position(Long tab_position) {
		this.tab_position = tab_position;
	}

	public Date getFirstLoggedIn() {
		return firstLoggedIn;
	}

	public void setFirstLoggedIn(Date firstLoggedIn) {
		this.firstLoggedIn = firstLoggedIn;
	}

	public Date getLastHeartBeatPulse() {
		return lastHeartBeatPulse;
	}

	public void setLastHeartBeatPulse(Date lastHeartBeatPulse) {
		this.lastHeartBeatPulse = lastHeartBeatPulse;
	}

	public Long getTrail_id() {
		return trail_id;
	}

	public void setTrail_id(Long trail_id) {
		this.trail_id = trail_id;
	}
	
}
