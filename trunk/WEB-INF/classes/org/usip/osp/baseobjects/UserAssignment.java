package org.usip.osp.baseobjects;

import java.util.List;
import java.util.ListIterator;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.*;

/**
 * This class represents the assignment of a user to a particular running 
 * simulation.
 * 
 * @author Ronald "Skip" Cole<br />
 *
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getRunning_sim_id() {
		return running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public Long getActor_id() {
		return actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	
	public List getAllForUser(Long userid, org.hibernate.Session hibernate_session) {
		return (hibernate_session.createQuery("from UserAssignment where user_id = " + 
				userid.toString()).list());
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
		
		String hqlString = "from UserAssignment where RUNNING_SIM_ID = '" + rid +  
			"' and ACTOR_ID = '" + aid + "' AND SIM_ID = '" + sid + "'";
		
		System.out.println(hqlString);
		
		List <UserAssignment> userList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hqlString).list();

		for (ListIterator<UserAssignment> li = userList.listIterator(); li.hasNext();) {
			UserAssignment ua = (UserAssignment) li.next();
			MultiSchemaHibernateUtil.getSession(schema).delete(ua);
			
		}
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	
	
	public static User getUserAssigned (String schema, Long rid, Long aid) {
		
		String hqlString = "from UserAssignment where RUNNING_SIM_ID = " + rid + " and ACTOR_ID = " + aid;
		
		//System.out.println(hqlString);
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List <UserAssignment> userList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hqlString).list();
		
		User returnUser = null;
		if ((userList != null) && (userList.size() > 0)){
			UserAssignment ua = (UserAssignment) userList.get(0);
			returnUser = (User) MultiSchemaHibernateUtil.getSession(schema).get(User.class,ua.getUser_id());
			
			returnUser.loadMyDetails();
			System.out.println(returnUser.getBu_username());
		} else{
			System.out.println("no user assigned found.");
		}
		
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnUser;
		
	}
	
	public static UserAssignment getUserAssignment (String schema, Long rid, Long aid) {
		
		String hqlString = "from UserAssignment where RUNNING_SIM_ID = " + rid + " and ACTOR_ID = " + aid;
		System.out.println(hqlString);
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List <UserAssignment> userList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hqlString).list();
		
		UserAssignment returnUserAssignment = null;
		
		if ((userList != null) && (userList.size() == 1)){
			returnUserAssignment = (UserAssignment) userList.get(0);
			System.out.println("found ua: " + returnUserAssignment.getId());
		} else if (userList.size() > 1){
			System.out.println("Warning more than one user assigned to role.");
			return null;
		} else {
			System.out.println(" returnUserAssignment has not been assigned!" );
		}
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnUserAssignment;
		
	}
    
	
}
