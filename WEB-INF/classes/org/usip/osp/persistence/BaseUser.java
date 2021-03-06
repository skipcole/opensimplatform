package org.usip.osp.persistence;

import java.security.MessageDigest;
import java.util.*;

import javax.persistence.*;

import org.hibernate.Session;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.User;
import org.apache.log4j.*;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * This class holds all of the personal information (name, email address, etc.) on players.
 */
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
@Table(name = "BASEUSERTABLE")
@Proxy(lazy = false)
public class BaseUser implements Comparable {

	@Id
	@GeneratedValue
	@Column(name = "BU_ID")
	private Long id;

	@Column(name = "USERNAME", unique = true)
	private String username = ""; //$NON-NLS-1$

	@Column(name = "PASSWORD")
	private String password = ""; //$NON-NLS-1$

	@Column(name = "FULLNAME")
	private String full_name = ""; //$NON-NLS-1$

	@Column(name = "FIRSTNAME")
	private String first_name = ""; //$NON-NLS-1$

	@Column(name = "LASTNAME")
	private String last_name = ""; //$NON-NLS-1$

	@Column(name = "MIDDLENAME")
	private String middle_name = ""; //$NON-NLS-1$

	@Column(name = "REGISTERED")
	private boolean registered = false;

	/** 
	 * Indicates if this is a temporarily assigned password that should be changed.
	 * 
	 */
	private boolean tempPassword = false;

	@Column(name = "TEMP_PASSWORD_CT")
	private String tempPasswordCleartext = ""; //$NON-NLS-1$

	public String getTemppasswordCleartext() {
		return tempPasswordCleartext;
	}

	public void setTemppasswordCleartext(String temppasswordCleartext) {
		tempPasswordCleartext = temppasswordCleartext;
	}

	private Long preferredLanguageCode = new Long(
			UILanguageObject.ENGLISH_LANGUAGE_CODE);

	public Long getPreferredLanguageCode() {
		return preferredLanguageCode;
	}

	public void setPreferredLanguageCode(Long preferredLanguageCode) {
		this.preferredLanguageCode = preferredLanguageCode;
	}

	/**
	 * Zero argument constructed needed by hibernate and other programs.
	 *
	 */
	public BaseUser() {

	}

	/** Creates a new base user with the username and password passed in. */
	public BaseUser(String the_username, String the_password) {

		this.setUsername(the_username);
		this.setPassword(the_password);

		saveMe();

	}

	public static void main(String args[]) {

		List mList = searchUserByName("ip");

		for (ListIterator li = mList.listIterator(); li.hasNext();) {
			BaseUser bu = (BaseUser) li.next();
		}
	}

	public static List<BaseUser> getAll() {

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		List returnList = MultiSchemaHibernateUtil
				.getSession(MultiSchemaHibernateUtil.principalschema, true)
				.createQuery("from BaseUser").list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema, true).close();

		return returnList;

	}

	/**
	 * 
	 * @param partialName
	 * @return
	 */
	public static List<BaseUser> searchByNameOrUsername(String partialName) {

		List<BaseUser> listOne = searchUserByName(partialName);
		List<BaseUser> listTwo = searchUserByUserName(partialName);

		Collections.sort(listOne);
		Collections.sort(listTwo);

		List<BaseUser> finalList = new ArrayList();

		// Add everything from list two into the final list
		for (ListIterator li2 = listTwo.listIterator(); li2.hasNext();) {
			BaseUser bu2 = (BaseUser) li2.next();

			finalList.add(bu2);
		}

		for (ListIterator li = listOne.listIterator(); li.hasNext();) {
			BaseUser bu1 = (BaseUser) li.next();

			boolean foundAlready = false;

			for (ListIterator li2 = listTwo.listIterator(); li2.hasNext();) {
				BaseUser bu2 = (BaseUser) li2.next();

				if (bu1.getId().equals(bu2.getId())) {
					foundAlready = true;
				}
			}

			if (!foundAlready) {
				finalList.add(bu1);
			}

		}
		return finalList;
	}

	/**
	 * Searches the database by the name of the user.
	 * 
	 * @param schema
	 * @param partialName
	 * @return
	 */
	public static List<BaseUser> searchUserByName(String partialName) {

		partialName = "%" + partialName + "%";

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		List sList = MultiSchemaHibernateUtil
				.getSession(MultiSchemaHibernateUtil.principalschema, true)
				.createQuery("from BaseUser where FULLNAME like :FULLNAME  ")
				.setString("FULLNAME", partialName).list(); //$NON-NLS-1$ 

		MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema, true).close();

		return sList;

	}

	/**
	 * 
	 * @param partialName
	 * @return
	 */
	public static List<BaseUser> searchUserByUserName(String partialName) {

		partialName = "%" + partialName + "%";

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		List sList = MultiSchemaHibernateUtil
				.getSession(MultiSchemaHibernateUtil.principalschema, true)
				.createQuery("from BaseUser where username like :partialName  ")
				.setString("partialName", partialName).list(); //$NON-NLS-1$ 

		MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema, true).close();

		return sList;

	}

	/** Handles the updating of a base user. */
	public void updateMe(String fi, String fu, String la, String mi) {

		System.out.println("BaseUser.updateMe, first name was " + fi);

		this.first_name = fi;
		this.full_name = fu;
		this.last_name = la;
		this.middle_name = mi;

		saveMe();

	}

	/**
	 * Saves this user back to the main database.
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
	 * Takes a password and hashes it.
	 * 
	 * @param rawPassword
	 * @return
	 */
	public static String hashPassword(String rawPassword) {

		String hash = null;

		try {

			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(rawPassword.getBytes("UTF-8"));
			byte raw[] = md.digest();
			hash = (new BASE64Encoder()).encode(raw);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return hash;
	}

	/**
	 * Returns the list of authorized schemas.
	 * @return
	 */
	public ArrayList getAuthorizedSchemas() {
		return getAuthorizedSchemas(this.id);
	}

	/**
	 * Returns the user if the password entered is correct, otherwise returns null.
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public static BaseUser validateUser(String username, String password) {

		if ((username == null) || (password == null)) {
			Logger.getRootLogger().debug("username or password was null."); //$NON-NLS-1$
			return null;
		}

		password = BaseUser.hashPassword(password);
		username = username.toLowerCase();

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		List sList = null;

		try {
			sList = MultiSchemaHibernateUtil
					.getSession(MultiSchemaHibernateUtil.principalschema, true)
					.createQuery(
							"from BaseUser where lower(USERNAME) = :username AND PASSWORD = :password ")
					.setString("username", username)
					.setString("password", password).list(); //$NON-NLS-1$ 
		} catch (Exception e) {
			// TODO
			// This died here when a user got added with null username (during a
			// csv import)
			// We should find a way to log errors of this nature to a place
			// where the admin can read them.
		}

		if ((sList == null) || (sList.size() == 0)) {
			MultiSchemaHibernateUtil
					.getSession(MultiSchemaHibernateUtil.principalschema, true)
					.getTransaction().commit();
			MultiSchemaHibernateUtil.getSession(
					MultiSchemaHibernateUtil.principalschema, true).close();
			return null;
		} else {
			BaseUser bu = (BaseUser) sList.get(0);

			MultiSchemaHibernateUtil.getSession(
					MultiSchemaHibernateUtil.principalschema, true).evict(bu);
			MultiSchemaHibernateUtil
					.getSession(MultiSchemaHibernateUtil.principalschema, true)
					.getTransaction().commit();
			MultiSchemaHibernateUtil.getSession(
					MultiSchemaHibernateUtil.principalschema, true).close();
			return bu;
		}

	}

	public String getPassword() {
		return this.password;
	}

	/**
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = BaseUser.hashPassword(password);
	}

	public void setPasswordAlreadyHashed(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Given a user_id return all of the schemas to which this user has been
	 * authorized to access.
	 * 
	 * assigned
	 * 
	 * @param user_id
	 * @return
	 */
	public static ArrayList getAuthorizedSchemas(Long user_id) {

		ArrayList<SchemaGhost> returnList = new ArrayList<SchemaGhost>();

		// Loop over known schemas and see if this user exists in them.
		for (ListIterator<SchemaInformationObject> sio_l = SchemaInformationObject
				.getAll().listIterator(); sio_l.hasNext();) {
			SchemaInformationObject sio = sio_l.next();

			if (User.doesUserExistInSchema(user_id, sio.getSchema_name())) {
				SchemaGhost sg = new SchemaGhost();

				sg.setId(sio.getId());
				sg.setSchema_name(sio.getSchema_name());
				sg.setSchema_organization(sio.getSchema_organization());

				Logger.getRootLogger().debug(
						"user authorized for " + sio.getSchema_name()); //$NON-NLS-1$

				returnList.add(sg);
			}

		}

		return returnList;

	}

	/**
	 * 
	 * @param the_username
	 * @return
	 */
	public static BaseUser getByUsername(String the_username) {

		BaseUser bu = null;

		Session s = MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		String hqlString = "from BaseUser where username = :the_username"; //$NON-NLS-1$
		try {
			List returnList = s.createQuery(hqlString)
					.setString("the_username", the_username).list();

			if ((returnList != null) && (returnList.size() > 0)) {
				bu = (BaseUser) returnList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		MultiSchemaHibernateUtil
				.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

		return bu;
	}

	/**
	 * 
	 * @param the_username
	 * @return
	 */
	public static boolean checkIfUserExists(String the_username) {

		BaseUser bu = getByUsername(the_username);

		if (bu == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Ensures that that base user created is not a new version of an existing user.
	 * 
	 * @param the_username
	 * @param the_password
	 * @param schema
	 * @return
	 */
	public static BaseUser getUniqueUser(String the_username,
			String the_password, String schema) {

		Logger.getRootLogger().debug("getting user by name: " + the_username); //$NON-NLS-1$
		BaseUser bu = getByUsername(the_username);

		if (bu != null) {
			return bu;
		} else {
			bu = new BaseUser(the_username, the_password);

			MultiSchemaHibernateUtil
					.beginTransaction(MultiSchemaHibernateUtil.principalschema);

			MultiSchemaHibernateUtil.getSession(
					MultiSchemaHibernateUtil.principalschema).saveOrUpdate(bu);

			MultiSchemaHibernateUtil
					.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

			return bu;
		}
	}

	/**
	 * Returns the base user with this id. This is handled specifically here
	 * (and not just done as a typical hibernate 'get' operation) since we are
	 * coming from the root schema and not just any old schema.
	 * 
	 * @param user_id
	 * @return
	 */
	public static BaseUser getByUserId(Long user_id) {

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		BaseUser bu = (BaseUser) MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema, true).get(
				BaseUser.class, user_id);

		MultiSchemaHibernateUtil
				.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

		return bu;
	}

	public String getInitials() {

		String returnString = "";

		boolean foundInitial = false;

		if ((first_name != null) && (first_name.length() > 0)) {
			returnString += this.first_name.substring(0, 1).toUpperCase();
			foundInitial = true;
		}

		if ((middle_name != null) && (middle_name.length() > 0)) {
			returnString += this.middle_name.substring(0, 1).toUpperCase();
			foundInitial = true;
		}

		if ((last_name != null) && (last_name.length() > 0)) {
			returnString += this.last_name.substring(0, 1).toUpperCase();
			foundInitial = true;
		}

		if (foundInitial) {
			return returnString;
		} else {
			return "unknown";
		}

	}

	/**
	 * @return Returns the real_name.
	 */
	public String getFull_name() {
		return this.full_name;
	}

	/**
	 * @param real_name
	 *            The real_name to set.
	 */
	public void setFull_name(String real_name) {
		this.full_name = real_name;
	}

	/**
	 * @return the first_name
	 */
	public String getFirst_name() {
		return this.first_name;
	}

	/**
	 * @param first_name
	 *            the first_name to set
	 */
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	/**
	 * @return the last_name
	 */
	public String getLast_name() {
		return this.last_name;
	}

	/**
	 * @param last_name
	 *            the last_name to set
	 */
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	/**
	 * @return the middle_name
	 */
	public String getMiddle_name() {
		return this.middle_name;
	}

	/**
	 * @param middle_name
	 *            the middle_name to set
	 */
	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}

	public boolean isRegistered() {
		return this.registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public boolean isTempPassword() {
		return tempPassword;
	}

	public void setTempPassword(boolean tempPassword) {
		this.tempPassword = tempPassword;
	}

	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransit_id() {
		return this.transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}

	@Override
	public int compareTo(Object arg0) {

		if (arg0 == null) {
			return 0;
		}
		BaseUser bu1 = (BaseUser) arg0;

		return this.getLast_name().compareTo(bu1.getLast_name());
	}

}
