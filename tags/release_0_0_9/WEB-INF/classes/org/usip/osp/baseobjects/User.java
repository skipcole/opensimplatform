package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.*;
import org.apache.log4j.*;
/**
 * This class represents a USER in a particular schema. The base user inside of it contains the information
 * in the central table that holds all specific user information. In this particular object are mostly the 
 * permissions to all the user to do things (instruct, author or administrate) in this organizational database (schema).
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
@Entity
@Table(name = "USERS")
@Proxy(lazy = false)
public class User {

	/** Database id of this User. */
	@Id
	@Column(name = "USER_ID")
	private Long id;
	
	@Column(name = "USER_NAME")
	private String user_name;

	@Column(name = "SIMCREATOR")
	private boolean sim_author = false;

	@Column(name = "SIMINSTRUCTOR")
	private boolean sim_instructor = false;

	@Column(name = "ADMIN")
	private boolean admin = false;
	
	private Date lastLogin;
	
	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	/** Used to keep track of time zone of users. */
	private int timeZoneOffset;

	public int getTimeZoneOffset() {
		return this.timeZoneOffset;
	}

	public void setTimeZoneOffset(int timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	@Transient
	private Long trail_id;

	@Transient
	private String bu_full_name = ""; //$NON-NLS-1$

	@Transient
	private String bu_first_name = ""; //$NON-NLS-1$

	@Transient
	private String bu_last_name = ""; //$NON-NLS-1$

	@Transient
	private String bu_middle_name = ""; //$NON-NLS-1$

	@Transient
	private String bu_username = ""; //$NON-NLS-1$
	
	@Transient
	private String bu_password = ""; //$NON-NLS-1$

	public User() {

	}

	/**
	 * Creates a base user and user object in the schema passed in.
	 * @param schema
	 * @param username
	 * @param password
	 * @param first_name
	 * @param last_name
	 * @param middle_name
	 * @param full_name
	 * @param email
	 * @param sim_creator
	 * @param sim_instructor
	 * @param admin
	 */
	public User(String schema, String username, String password,
			String first_name, String last_name, String middle_name,
			String full_name, String email, boolean sim_creator,
			boolean sim_instructor, boolean admin) {

		Logger.getRootLogger().debug("creating user " + username); //$NON-NLS-1$

		BaseUser bu = BaseUser.getUniqueUser(username, password, schema);
		
		bu.setFirst_name(first_name);
		bu.setFull_name(full_name);
		bu.setLast_name(last_name);
		bu.setMiddle_name(middle_name);
		bu.saveMe();

		Logger.getRootLogger().debug("-----------------"); //$NON-NLS-1$
		Logger.getRootLogger().debug("bu id " + bu.getId()); //$NON-NLS-1$
		Logger.getRootLogger().debug("-----------------"); //$NON-NLS-1$

		this.setId(bu.getId());

		this.user_name = username;
		this.sim_author = sim_creator;
		this.sim_instructor = sim_instructor;
		this.admin = admin;

		try {
			MultiSchemaHibernateUtil.beginTransaction(schema);

			MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public static User getInfoOnLogin(Long user_id, String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		User user = (User) MultiSchemaHibernateUtil.getSession(schema).get(
				User.class, user_id);

		MultiSchemaHibernateUtil.getSession(schema).evict(user);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		// Store user logged in information in database
		UserTrailGhost lit = new UserTrailGhost();
		lit.setUser_id(user.getId());

		// trail id gets passed up to the pso, to keep track of this login activities
		user.trail_id = lit.storeLoginInformationGetTrailID(schema);

		return user;
	}
	
	/** 
	 * 
	 * @param u_id
	 * @param schema
	 * @return Returns true if user found in schema.
	 */
	public static boolean doesUserExistInSchema(Long u_id, String schema){
		
		boolean returnValue = false;
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		User user = (User) MultiSchemaHibernateUtil
				.getSession(schema).get(User.class, u_id);
		
		if (user != null){
			returnValue = true;
		}

		MultiSchemaHibernateUtil.getSession(schema).evict(user);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnValue;
		
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	/**
	 * @return Returns the admin.
	 */
	public boolean isAdmin() {
		return this.admin;
	}

	/**
	 * @param admin
	 *            The admin to set.
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	/**
	 * @return Returns the sim_creator.
	 */
	public boolean isSim_author() {
		return this.sim_author;
	}

	/**
	 * @param sim_creator
	 *            The sim_creator to set.
	 */
	public void setSim_author(boolean sim_creator) {
		this.sim_author = sim_creator;
	}

	/**
	 * Returns a list of all of the users in a particular schema.
	 * 
	 * @param schema The schema in which to search.
	 * @return A list of all User objects found in the schema.
	 */
	public static List getAll(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery("from User").list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;

	}

	public static List getAll(String schema, boolean getdetails) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery("from User").list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (getdetails) {
			returnList = getDetails(returnList, schema);
		}

		return returnList;

	}

	/** Returns list of all Admins, creators and instructors. */
	public static List getAllAdminsSCandInstructors(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from User where simcreator = '1' or admin = '1' or siminstructor = '1'") //$NON-NLS-1$
				.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		returnList = getDetails(returnList, schema);
		return returnList;

	}
	
	/**
	 * 
	 * @param schema
	 * @param user_id
	 * @return
	 */
	public static User getUser(String schema, Long user_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		User user = (User) MultiSchemaHibernateUtil
				.getSession(schema).get(User.class, user_id);

		user.loadMyDetails();
		
		MultiSchemaHibernateUtil.getSession(schema).evict(user);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return user;
		
	}
	
	
	/**
	 * Gets the user info from the schema indicated, and then gets the base user info
	 * from the principal schema.
	 * 
	 * Users and BaseUsers share the same id.
	 * 
	 * @param schema
	 * @param u_id
	 * @return
	 */
	public static User getMe(String schema, Long u_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		User user = (User) MultiSchemaHibernateUtil.getSession(schema).get(User.class, u_id);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		
		MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema);
		BaseUser bu = (BaseUser) MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema).get(
				BaseUser.class, u_id);
		user.loadBUInfo(bu);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
		
		return user;
	}
	
	/**
	 * Saves this user to its schema. Copies base user info into the base user object, and saves it in the 
	 * principal schema.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		BaseUser bu = BaseUser.getByUserId(this.getId());
		
		bu.setUsername(this.getBu_username());
		bu.setPassword(this.getBu_password());
		
		bu.setFirst_name(this.getBu_first_name());
		bu.setFull_name(this.getBu_full_name());
		bu.setLast_name(this.getBu_last_name());
		bu.setMiddle_name(this.getBu_middle_name());
		
		bu.saveMe();

	}
	
	/**
	 * Loads info inside the Base user into this user.
	 * @param bu
	 */
	private void loadBUInfo(BaseUser bu){
		
		this.setBu_first_name(bu.getFirst_name());
		this.setBu_full_name(bu.getFull_name());
		this.setBu_last_name(bu.getLast_name());
		this.setBu_middle_name(bu.getMiddle_name());
		
		this.setBu_username(bu.getUsername());
		this.setBu_password(bu.getPassword());
	}

	public Long getTrail_id() {
		return this.trail_id;
	}

	public void setTrail_id(Long trail_id) {
		this.trail_id = trail_id;
	}

	public boolean isSim_instructor() {
		return this.sim_instructor;
	}

	public void setSim_instructor(boolean sim_instructor) {
		this.sim_instructor = sim_instructor;
	}

	/**
	 * Returns all of the Users in the schema provided.
	 * 
	 * @param schema
	 * @return
	 */
	public static List getAllForSchema(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery("from User").list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;

	}

	/**
	 * Returns all of the Users in the schema provided.
	 * 
	 * @param schema
	 * @return
	 */
	public static List getAllForSchemaAndLoadDetails(String schema) {

		List initialList = getAllForSchema(schema);

		// Load details
		getDetails(initialList, schema);

		return initialList;

	}

	/**
	 * Loads the info stored in the base user table into this user object.
	 */
	public void loadMyDetails() {

		MultiSchemaHibernateUtil
				.beginTransaction(MultiSchemaHibernateUtil.principalschema);
		BaseUser bu = (BaseUser) MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema).get(
				org.usip.osp.persistence.BaseUser.class, this.getId());

		if (bu != null) {
			this.setBu_first_name(bu.getFirst_name());
			this.setBu_full_name(bu.getFull_name());
			this.setBu_last_name(bu.getLast_name());
			this.setBu_middle_name(bu.getMiddle_name());

			this.setBu_username(bu.getUsername());

		} else {
			Logger.getRootLogger().debug("warning. user found without base user component."); //$NON-NLS-1$
		}

		MultiSchemaHibernateUtil
				.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
	}

	public static List getDetails(List initialList, String schema) {
		// Load details
		MultiSchemaHibernateUtil.beginTransaction(schema);

		for (ListIterator<User> li = initialList.listIterator(); li.hasNext();) {
			User user = li.next();

			user.loadMyDetails();
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return initialList;
	}

	public String getBu_full_name() {
		return this.bu_full_name;
	}

	public void setBu_full_name(String bu_full_name) {
		this.bu_full_name = bu_full_name;
	}

	public String getBu_first_name() {
		return this.bu_first_name;
	}

	public void setBu_first_name(String bu_first_name) {
		this.bu_first_name = bu_first_name;
	}

	public String getBu_last_name() {
		return this.bu_last_name;
	}

	public void setBu_last_name(String bu_last_name) {
		this.bu_last_name = bu_last_name;
	}

	public String getBu_middle_name() {
		return this.bu_middle_name;
	}

	public void setBu_middle_name(String bu_middle_name) {
		this.bu_middle_name = bu_middle_name;
	}

	public String getBu_username() {
		return this.bu_username;
	}

	public void setBu_username(String bu_username) {
		this.bu_username = bu_username;
	}

	public String getBu_password() {
		return this.bu_password;
	}

	public void setBu_password(String bu_password) {
		this.bu_password = bu_password;
	}
	
	
	/**
	 * If someone is just a player in this schema, return true, else return false.
	 */
	public boolean isJustPlayer(){
		if (
				(!(this.isAdmin())) &&
				(!(this.isSim_author())) &&
				(!(this.isSim_instructor()))
				){
			return true;
		}
		return false;
	}

}