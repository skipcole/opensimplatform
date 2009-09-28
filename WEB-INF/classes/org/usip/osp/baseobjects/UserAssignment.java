package org.usip.osp.baseobjects;

import java.util.ArrayList;
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
	
	public List getAllForUser(Long userid, org.hibernate.Session hibernate_session) {
		return (hibernate_session.createQuery("from UserAssignment where user_id = " +  //$NON-NLS-1$
				userid.toString()).list());
	}
	
	/**
	 * 
	 * @param schema
	 * @param rsid
	 * @return
	 */
	public static List getAllForRunningSim(String schema, Long rsid) {
		
		List returnList = new ArrayList<UserAssignment>();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		returnList =  MultiSchemaHibernateUtil.getSession(schema).createQuery("from UserAssignment where running_sim_id = " +  //$NON-NLS-1$
				rsid.toString()).list();
		
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
		
		removePreviousAssignments(schema, sid, rid, aid);
		
		UserAssignment ua = new UserAssignment(schema, sid, rid, aid, uid);
		
		return ua;

	}
	
	public static void removePreviousAssignments(String schema, Long sid, Long rid, Long aid){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		String hqlString = "from UserAssignment where RUNNING_SIM_ID = '" + rid +   //$NON-NLS-1$
			"' and ACTOR_ID = '" + aid + "' AND SIM_ID = '" + sid + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		Logger.getRootLogger().debug(hqlString);
		
		List <UserAssignment> userList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hqlString).list();

		for (ListIterator<UserAssignment> li = userList.listIterator(); li.hasNext();) {
			UserAssignment ua = li.next();
			MultiSchemaHibernateUtil.getSession(schema).delete(ua);
			
		}
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	
	
	public static User getUserAssigned (String schema, Long rid, Long aid) {
		
		String hqlString = "from UserAssignment where RUNNING_SIM_ID = " + rid + " and ACTOR_ID = " + aid; //$NON-NLS-1$ //$NON-NLS-2$
		
		//Logger.getRootLogger().debug(hqlString);
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List <UserAssignment> userList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hqlString).list();
		
		User returnUser = null;
		if ((userList != null) && (userList.size() > 0)){
			UserAssignment ua = userList.get(0);
			returnUser = (User) MultiSchemaHibernateUtil.getSession(schema).get(User.class,ua.getUser_id());
			
			returnUser.loadMyDetails();
			Logger.getRootLogger().debug(returnUser.getBu_username());
		} else{
			Logger.getRootLogger().debug("no user assigned found."); //$NON-NLS-1$
		}
		
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnUser;
		
	}
	
	public static UserAssignment getUserAssignment (String schema, Long rid, Long aid) {
		
		String hqlString = "from UserAssignment where RUNNING_SIM_ID = " + rid + " and ACTOR_ID = " + aid; //$NON-NLS-1$ //$NON-NLS-2$
		Logger.getRootLogger().debug(hqlString);
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List <UserAssignment> userList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hqlString).list();
		
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
	public static UserAssignment getMe(String schema, Long ua_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		UserAssignment ua = (UserAssignment) MultiSchemaHibernateUtil.getSession(schema).get(UserAssignment.class, ua_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return ua;

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
    
	
}
