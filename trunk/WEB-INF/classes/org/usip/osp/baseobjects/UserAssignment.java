package org.usip.osp.baseobjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.*;
import org.apache.log4j.*;
/**
 * This class represents the assignment of a user to a particular running 
 * simulation.
 *
 */
/*
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "USER_ASSIGNMENTS")
@Proxy(lazy=false)
public class UserAssignment{

	/** Database id of this User Assignment. */
	@Id
	@GeneratedValue
    @Column(name = "USER_ASSIGN_ID")
    private Long id;
	
    /** Simulation id of this user Assignment */
    @Column(name = "SIM_ID")
    private Long sim_id;
    
    /** Running simulation id of this user Assignment */
    @Column(name = "RUNNING_SIM_ID")
    private Long running_sim_id;
    
    /** Actor id of this user Assignment */
    @Column(name = "ACTOR_ID")
    private Long actor_id;
    
    /** User id of this user Assignment */
    @Column(name = "USER_ID")
    private Long user_id;
    
    private Long highestAlertNumberRecieved = new Long(0);
    
    private boolean temporaryAssignment = false;
    
    /** username/email address of this user. */
    private String username = "";
    
    /** If this assignment for an unregistered user, record the name entered for them here. */
    private String tempStudentName = "Player";
    
    /** Indicates if this assignment is to a facilitator. */
    private boolean facilitatorAssignment = false;

    /** Used to record if this player has been invited, has acknowledged the invite
     * or has logged on. */
    private String uaStatus = "";
    
	public Long getHighestAlertNumberRecieved() {
		return highestAlertNumberRecieved;
	}

	public void setHighestAlertNumberRecieved(Long highestAlertNumberRecieved) {
		this.highestAlertNumberRecieved = highestAlertNumberRecieved;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSim_id() {
		return this.sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getRunning_sim_id() {
		return this.running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public Long getActor_id() {
		return this.actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public Long getUser_id() {
		return this.user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	
	public boolean isTemporaryAssignment() {
		return temporaryAssignment;
	}

	public void setTemporaryAssignment(boolean temporaryAssignment) {
		this.temporaryAssignment = temporaryAssignment;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTempStudentName() {
		return tempStudentName;
	}

	public void setTempStudentName(String tempStudentName) {
		this.tempStudentName = tempStudentName;
	}

	public boolean isFacilitatorAssignment() {
		return facilitatorAssignment;
	}

	public void setFacilitatorAssignment(boolean facilitatorAssignment) {
		this.facilitatorAssignment = facilitatorAssignment;
	}

	public Long getAssumedIdentityId() {
		return assumedIdentityId;
	}

	public void setAssumedIdentityId(Long assumedIdentityId) {
		this.assumedIdentityId = assumedIdentityId;
	}
	
	/**
	 * Returns all of the user assignments for a user.
	 * 
	 * @param schema
	 * @param userid
	 * @return
	 */
	public static List getAllForUser(String schema, Long userid) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List returnList = MultiSchemaHibernateUtil.getSession(schema)
			.createQuery("from UserAssignment where user_id = :user_id")
			.setLong("user_id", userid).list();
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;

	}
	
	/**
	 * 
	 * @param schema
	 * @param rsid
	 * @return
	 */
	public static List<UserAssignment> getAllForRunningSim(String schema, Long rsid) {
		
		List returnList = new ArrayList<UserAssignment>();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		returnList =  MultiSchemaHibernateUtil.getSession(schema)
			.createQuery("from UserAssignment where running_sim_id = :rsid")
			.setLong("rsid", rsid)
			.list();
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
	/**
	 * Returns a list of user ids for people in this simulation.
	 * 
	 * @param schema
	 * @param rsid
	 * @return
	 */
	public static List<Long> getUniqueSetOfUsers(String schema, Long rsid){
		
		Hashtable userSet = new Hashtable <Long, String>();
		
		for (ListIterator<UserAssignment> li = getAllForRunningSim(schema, rsid).listIterator(); li.hasNext();) {
			UserAssignment ua = li.next();
			
			if (ua.getUser_id() != null){
				userSet.put(ua.getUser_id(),"set");
			}
		}
		
		ArrayList <Long> returnList = new ArrayList();
		
		for (Enumeration e = userSet.keys(); e.hasMoreElements();) {
			Long key = (Long) e.nextElement();
			returnList.add(key);
		}
		Collections.sort(returnList);
		return returnList;
		
	}
	
	/**
	 * 
	 * @param schema
	 * @param simId
	 * @return
	 */
	public static List getAllForSim(String schema, Long simId) {
		
		List returnList = new ArrayList<UserAssignment>();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		returnList =  MultiSchemaHibernateUtil.getSession(schema)
			.createQuery("from UserAssignment where sim_id = :simId")
			.setLong("simId", simId)	
			.list();
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
	/**
	 * Gets all of the users assignments for people playing an actor in a simulation.
	 * 
	 * @param schema
	 * @param actor_id
	 * @param running_sim_id
	 * @return
	 */
	public static List <UserAssignment> getAllForActorInARunningSim(String schema, Long actor_id, Long running_sim_id) {
		
		List returnList = new ArrayList<UserAssignment>();
		
		String qyrString = "from UserAssignment where running_sim_id = :running_sim_id and actor_id = :actor_id";
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		returnList =  MultiSchemaHibernateUtil.getSession(schema).createQuery(qyrString)
			.setLong("running_sim_id", running_sim_id)
			.setLong("actor_id", actor_id)
			.list();
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
	/**
	 * Zero argument constructor needed by Hibernate
	 */
	public UserAssignment(){
		
	}
	
	
	public UserAssignment(String schema, Long sid, Long rid, Long aid, Long uid){
		
		this.sim_id = sid;
		this.running_sim_id = rid;
		this.actor_id = aid;
		this.user_id = uid;
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
	}
	
	/**
	 * This goes through all of the UserAssignments to see if any of them have an
	 * email assigned, but not a user id. It will then update the user id.
	 * @param schema
	 * @param username
	 * @param user_id
	 */
	public static void checkUserAssignmentsForNameToUpdated(String schema, String username, Long user_id){
		
		for (ListIterator<UserAssignment> li = getAllByUserName(schema, username).listIterator(); li.hasNext();) {
			UserAssignment ua = li.next();
			
			if (ua.getUser_id() == null){
				ua.setUser_id(user_id);
				ua.saveMe(schema);
			} else if (ua.id.intValue() != user_id.intValue()){
				Logger.getRootLogger().warn("Warning user assignment with apparently the wrong id.");
			}
		}
	}
	
	/**
	 * 
	 * @param schema
	 * @param username
	 * @return
	 */
	public static List <UserAssignment> getAllByUserName(String schema, String username) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List returnList = MultiSchemaHibernateUtil.getSession(schema)
			.createQuery("from UserAssignment where username = :username")
			.setString("username", username).list();
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;

	}
	
	public static void removeMe(String schema, Long ua_id){
		
		UserAssignment ua = UserAssignment.getById(schema, ua_id);
		
		if (ua != null){
			MultiSchemaHibernateUtil.beginTransaction(schema);
			MultiSchemaHibernateUtil.getSession(schema).delete(ua);
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		}
	}
	
	/**
	 * If a user has not been assigned to this role, create a new user assignment. Else,
	 * update the current user assignment with a new user id.
	 * @param schema
	 * @param sid
	 * @param rid
	 * @param aid
	 * @param uid
	 * @return
	 */
	public static UserAssignment getUniqueUserAssignment(String schema, Long sid, Long rid, Long aid, Long uid){
		
		UserAssignment ua = new UserAssignment();
		
		String hqlString = "from UserAssignment where RUNNING_SIM_ID = '" + rid +   //$NON-NLS-1$
		"' and ACTOR_ID = '" + aid + "' AND SIM_ID = '" + sid + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		MultiSchemaHibernateUtil.beginTransaction(schema);
			
		List <UserAssignment> userList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hqlString).list();
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if ((userList == null) || (userList.size() == 0)){
			ua = new UserAssignment(schema, sid, rid, aid, uid);
		} else if (userList.size() >= 1){
			ua = (UserAssignment) userList.get(0);
			ua.setUser_id(uid);
			ua.saveMe(schema);
		}
		
		if (userList.size() > 1){
			Logger.getRootLogger().warn("multiple user assignments for s/rs/a: " + sid + "/" + rid + "/" + aid);
		}
		
		return ua;

	}
	
	
	public static List getUsersAssigned (String schema, Long rid, Long aid) {
		
		String hqlString = "from UserAssignment where RUNNING_SIM_ID = " + rid + " and ACTOR_ID = " + aid; //$NON-NLS-1$ //$NON-NLS-2$
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List <UserAssignment> userList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hqlString).list();
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return userList;
		
	}
	
	/**
	 * This is only used for one thing now: to see if a user has been assigned,
	 * so if it is arbitrary if 1 or more have been assigned. This should be simplified and removed.
	 * @param schema
	 * @param rid
	 * @param aid
	 * @return
	 */
	public static User get_A_UserAssigned_dont_use (String schema, Long rid, Long aid) {
		
		String hqlString = "from UserAssignment where RUNNING_SIM_ID = " + rid + " and ACTOR_ID = " + aid; //$NON-NLS-1$ //$NON-NLS-2$
		
		//Logger.getRootLogger().debug(hqlString);
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List <UserAssignment> userList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hqlString).list();
		
		User returnUser = null;
		if ((userList != null) && (userList.size() > 0)){
			UserAssignment ua = userList.get(0);
			
			if (ua.getUser_id() != null){
				returnUser = (User) MultiSchemaHibernateUtil.getSession(schema).get(User.class,ua.getUser_id());
			
				returnUser.loadMyDetails();
				Logger.getRootLogger().debug(returnUser.getBu_username());
			}
		} else{
			Logger.getRootLogger().debug("no user assigned found."); //$NON-NLS-1$
		}
		
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnUser;
		
	}
	
	public static UserAssignment getUserAssignment (String schema, Long rid, Long aid) {
		
		String hqlString = "from UserAssignment where RUNNING_SIM_ID = :running_sim_id and ACTOR_ID = :actor_id"; //$NON-NLS-1$
		Logger.getRootLogger().debug(hqlString);
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List <UserAssignment> userList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hqlString)
			.setLong("running_sim_id", rid)
			.setLong("actor_id", aid)
			.list();
		
		UserAssignment returnUserAssignment = null;
		
		if ((userList != null) && (userList.size() == 1)){
			returnUserAssignment = userList.get(0);
			Logger.getRootLogger().debug("found ua: " + returnUserAssignment.getId()); //$NON-NLS-1$
		} else if (userList.size() > 1){
			Logger.getRootLogger().debug("Warning more than one user assigned to role."); //$NON-NLS-1$
			return null;
		} else {
			Logger.getRootLogger().debug(" returnUserAssignment has not been assigned!" ); //$NON-NLS-1$
		}
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnUserAssignment;
		
	}
	
	/**
	 * Pulls the user assignment out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param ua_id
	 * @return
	 */
	public static UserAssignment getById(String schema, Long ua_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		UserAssignment ua = (UserAssignment) MultiSchemaHibernateUtil.getSession(schema).get(UserAssignment.class, ua_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return ua;

	}
	
	/**
	 * Saves the highestAlertNumber back into the database.
	 * @param schema
	 * @param ua_id
	 * @param myHighestAlertNumber
	 */
	public static void saveHighAlertNumber(String schema, Long ua_id, Long myHighestAlertNumber) {

		UserAssignment ua = UserAssignment.getById(schema, ua_id);
		ua.setHighestAlertNumberRecieved(myHighestAlertNumber);
		ua.saveMe(schema);

	}
	
	
	/**
	 * Saves this object back to the database.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
    
    /** Flag to indicate if this actor's information is temporarily taken */
    private boolean assumedIdentity = false;

	/** Id of the identity that this actor has assumed. */
    private Long assumedIdentityId;

	public boolean isAssumedIdentity() {
		return assumedIdentity;
	}

	public void setAssumedIdentity(boolean assumedIdentity) {
		this.assumedIdentity = assumedIdentity;
	}

	public String getAsParameterString() {
		String returnString = 
			"?uname=" + getUsername() + "&s_id=" + getSim_id() + 
			"&rs_id=" + getRunning_sim_id() + "&a_id=" + getActor_id() +
			"&ua_id=" + this.getId();
		
		return returnString;
	}

	public String getUaStatus() {
		return uaStatus;
	}

	public void setUaStatus(String uaStatus) {
		this.uaStatus = uaStatus;
	}
	
	/**
	 * 
	 * @param schema
	 * @return
	 */
	public Actor giveMeActor(String schema){
		
		Actor act = new Actor();
		act.setName("Unknown in UA ");
		
		if (this.actor_id != null){
			act = Actor.getById(schema, this.actor_id);
		}
		
		return act;
	}
	
	/**
	 * 
	 * @param schema
	 * @return
	 */
	public RunningSimulation giveMeRunningSim(String schema){
		RunningSimulation rs = new RunningSimulation();
		rs.setName("Unknown in UA ");
		
		if (this.running_sim_id != null) {
			rs = RunningSimulation.getById(schema, this.running_sim_id);
		}
		
		return rs;
	}
	
	public static final String STATUS_NONE = "";
	public static final String STATUS_INVITED = "invited";
	public static final String STATUS_CONFIRMED = "confirmed";
	public static final String STATUS_REGISTERED = "registered";
	public static final String STATUS_LOGGED_ON = "logged on";
	public static final String STATUS_ENTERED = "entered simulation";
	
	public static final int STATUS_CODE_NONE = 0;
	public static final int STATUS_CODE_INVITED = 1;
	public static final int STATUS_CODE_CONFIRMED = 2;
	public static final int STATUS_CODE_REGISTERED = 3;
	public static final int STATUS_CODE_LOGGED_ON = 4;
	public static final int STATUS_CODE_ENTERED = 5;
	
	
	/**
	 * Advances the status indicator of this User Assignment if the event is
	 * indeed an advancement. 
	 * @param newStatus
	 */
	public void advanceStatus(String newStatus){
		
		if (prioritizeStatusString(newStatus) > prioritizeStatusString(uaStatus)){
			uaStatus = newStatus;
		}
		
	}
	/**
	 * 
			
	 * @return
	 */
	public String getStatusColor(){
		
		String returnString = "000000";
		
		switch (prioritizeStatusString(uaStatus)) {
		
			case -1: returnString = "C2C2C2"; break;					// Null, Gray
			case STATUS_CODE_NONE: returnString = "FFFFFF"; break;						// Blank, White
			case STATUS_CODE_INVITED: returnString = "FFCCCC"; break;		// Invited, Pink
			case STATUS_CODE_CONFIRMED: returnString = "FFCC66"; break;		// Confirmed, Orange
			case STATUS_CODE_REGISTERED: returnString = "FFFF33"; break;	// Registered, Yellow
			case STATUS_CODE_LOGGED_ON: returnString = "CC99FF"; break;		// Logged on, Purple
			case STATUS_CODE_ENTERED: returnString = "99FF00"; break;		// Entered, Green
		}
		
		return returnString;

	}
	
	/**
	 * Puts the text strings of status information into a hierarchical order.
	 * 
	 * @param inputString
	 * @return
	 */
	public static int prioritizeStatusString(String inputString){
		
		if (inputString == null){
			return -1;
		}
		if (inputString.equalsIgnoreCase("")){
			return STATUS_CODE_NONE;
		}
		if (inputString.equalsIgnoreCase(STATUS_INVITED)){
			return STATUS_CODE_INVITED;
		}		
		if (inputString.equalsIgnoreCase(STATUS_CONFIRMED)){
			return STATUS_CODE_CONFIRMED;
		}	
		if (inputString.equalsIgnoreCase(STATUS_REGISTERED)){
			return STATUS_CODE_REGISTERED;
		}
		if (inputString.equalsIgnoreCase(STATUS_LOGGED_ON)){
			return STATUS_CODE_LOGGED_ON;
		}
		if (inputString.equalsIgnoreCase(STATUS_ENTERED)){
			return STATUS_CODE_ENTERED;
		}

		return 0;
	}
		
	
	
}
