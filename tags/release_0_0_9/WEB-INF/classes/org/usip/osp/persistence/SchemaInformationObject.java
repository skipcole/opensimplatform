package org.usip.osp.persistence;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.apache.log4j.*;

/**
 * This class represents a schema database that has been created to hold simulation data.
 * 
 *         This file is part of the USIP Open Simulation Platform.<br>
 * 
 *         The USIP Open Simulation Platform is free software; you can
 *         redistribute it and/or modify it under the terms of the new BSD Style
 *         license associated with this distribution.<br>
 * 
 *         The USIP Open Simulation Platform is distributed WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "SCHEMAINFORMATION")
@Proxy(lazy = false)
public class SchemaInformationObject {

	@Id
	@GeneratedValue
	@Column(name = "SIO_ID")
	private Long id;

	@Column(name = "SCHEMANAME", unique = true)
	private String schema_name;

	/** Organization for which this schema has been created. */
	private String schema_organization;

	/** username to login to database to access schema. */
	private String username;

	/** password to login to database to access schema. */
	private String userpass;

	/** locationt to access database, such as 'jdbc:mysql://localhost:' */
	private String location = "jdbc:mysql://localhost:"; //$NON-NLS-1$

	/** Port to access schema, on MySQL it is generally 3306. */
	private String port = "3306"; //$NON-NLS-1$

	// //////////////////////////////////////////
	/** Email SMTP server */
	private String email_smtp;

	/** Email authorized user */
	private String smtp_auth_user;

	/** Email password for authorized user. */
	private String smtp_auth_password;

	/** Email archive address. */
	private String email_archive_address;
	
	public static final String EMAIL_STATE_VERIFIED = "verified";
	
	public static final String EMAIL_STATE_UNVERIFIED = "unverified";
	
	public static final String EMAIL_STATE_DOWN = "down";
	
	private Date creationDate;
	
	public Date getCreationDate() {
		return creationDate;
	}

	@Lob
	/** notes for this particular installation. */
	private String notes = "";
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	/** Indicates if the email system has been verified and if it is up. */
	private String emailState = "";
	
	public String getEmailState() {
		return emailState;
	}

	public void setEmailState(String emailState) {
		this.emailState = emailState;
	}
	
	private Long emailServerNumber;
	
	public Long getEmailServerNumber() {
		return emailServerNumber;
	}

	public void setEmailServerNumber(Long emailServerNumber) {
		this.emailServerNumber = emailServerNumber;
	}

	/** Keeps track of the last time an author, instructor or admin has logged on. */
	private Date lastLogin;
	
	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	/** Set to indicate that this schema is safe to run unit tests in. */
	private boolean unitTestSchema = false;
	

	public boolean isUnitTestSchema() {
		return unitTestSchema;
	}

	public void setUnitTestSchema(boolean unitTestSchema) {
		this.unitTestSchema = unitTestSchema;
	}

	/**
	 * Your standard zero argument constructor.
	 */
	public SchemaInformationObject() {

		this.creationDate = new Date();
		
	}

	public static void main(String args[]) {

		List x = getAllOrderedByEmailServerNumber();

		Logger.getRootLogger().warn("got all"); //$NON-NLS-1$

		for (ListIterator li = x.listIterator(); li.hasNext();) {
			SchemaInformationObject sio = (SchemaInformationObject) li.next();

			Logger.getRootLogger().warn(sio.getSchema_name());
		}

	}

	/**
	 * Returns a list of all of the SchemaInformationObjects (SIOs) found
	 * 
	 * @return
	 */
	public static List<SchemaInformationObject> getAll() {

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		List<SchemaInformationObject> returnList = MultiSchemaHibernateUtil
				.getSession(MultiSchemaHibernateUtil.principalschema, true)
				.createQuery("from SchemaInformationObject").list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil
				.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

		return returnList;
	}
	
	/**
	 * Returns a list of all of the SchemaInformationObjects (SIOs) found ordered by Email Server Number
	 * 
	 * @return
	 */
	public static List<SchemaInformationObject> getAllOrderedByEmailServerNumber() {

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		List<SchemaInformationObject> returnList = MultiSchemaHibernateUtil
				.getSession(MultiSchemaHibernateUtil.principalschema, true)
				.createQuery("from SchemaInformationObject order by emailServerNumber asc").list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil
				.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

		return returnList;
	}

	/**
	 * Tests to see if a connection can be made to the databse in question. The
	 * string "Database Connection Verified" is returned upon successful
	 * connection.
	 * 
	 * @return
	 */
	public String testConn() {

		Connection conn = MysqlDatabase.getConnection(makeConnString());

		if (conn == null) {
			return "problem creating database connection"; //$NON-NLS-1$
		}

		try {
			conn.close();
		} catch (Exception e) {
			Logger.getRootLogger().debug("Error in closing connection in test conn."); //$NON-NLS-1$
		}

		return "Database Connection Verified"; //$NON-NLS-1$
	}

	/**
	 * Generates the connection string from the url, username and password.
	 * 
	 * @return
	 */
	public String makeConnString() {

		String conn_string = makeURL() + "&user=" + this.username + "&password=" //$NON-NLS-1$ //$NON-NLS-2$
				+ this.userpass;

		System.out.print(conn_string);
		return conn_string;
	}

	/**
	 * Generates the URL based on the location, port and schema_name.
	 * 
	 * @return
	 */
	public String makeURL() {
		String url = this.location + this.port + "/" + this.schema_name //$NON-NLS-1$
				+ "?autoReconnect=true"; //$NON-NLS-1$

		return url;
	}

	public String getSchema_name() {
		return this.schema_name;
	}

	public void setSchema_name(String schema_name) {
		this.schema_name = schema_name;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserpass() {
		return this.userpass;
	}

	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}

	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String toString() {
		return "schema: " + this.schema_name + "\n\r" + "user: " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ this.username + "\n\r" + "loc: " + this.location + "\n\r" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ "port: " + this.port + "\n\r" + "url: " + this.makeURL() //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ "\n\r" + "conn: " + this.makeConnString() //$NON-NLS-1$ //$NON-NLS-2$
				+ "email smtp server: " + this.email_smtp + "\n\r" //$NON-NLS-1$ //$NON-NLS-2$
				+ "email user: " + this.smtp_auth_user + "\n\r" //$NON-NLS-1$ //$NON-NLS-2$
				+ "email archive: " + this.email_archive_address + "\n\r"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String getSchema_organization() {
		return this.schema_organization;
	}

	public void setSchema_organization(String schema_organization) {
		this.schema_organization = schema_organization;
	}

	/**
	 * Returns the id of a schema based on its name.
	 * 
	 * @param schemaName
	 * @return
	 */
	public static Long lookUpId(String schemaName) {

		Long returnId = null;

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		List sList = MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema, true).createQuery(
				"from SchemaInformationObject where SCHEMANAME = :schemaName ")
				.setString("schemaName", schemaName)
				.list(); //$NON-NLS-1$

		if ((sList != null) && (sList.size() == 1)) {
			SchemaInformationObject sio = (SchemaInformationObject) sList
					.get(0);
			returnId = sio.getId();
		}

		MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema, true)
				.getTransaction().commit();
		MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema, true).close();

		return returnId;
	}

	public String getEmail_archive_address() {
		return this.email_archive_address;
	}

	public void setEmail_archive_address(String email_archive_address) {
		this.email_archive_address = email_archive_address;
	}

	public String getEmail_smtp() {
		return this.email_smtp;
	}

	public void setEmail_smtp(String email_smtp) {
		this.email_smtp = email_smtp;
	}

	public String getSmtp_auth_password() {
		return this.smtp_auth_password;
	}

	public void setSmtp_auth_password(String smtp_auth_password) {
		this.smtp_auth_password = smtp_auth_password;
	}

	public String getSmtp_auth_user() {
		return this.smtp_auth_user;
	}

	public void setSmtp_auth_user(String smtp_auth_user) {
		this.smtp_auth_user = smtp_auth_user;
	}

	/**
	 * Looks up a schema by its name and returns it.
	 * 
	 * @param schemaName
	 * @return
	 */
	public static SchemaInformationObject lookUpSIOByName(String schemaName) {

		SchemaInformationObject returnSIO = null;

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		List sList = MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema, true).createQuery(
				"from SchemaInformationObject where SCHEMANAME = :schemaName ")
				.setString("schemaName", schemaName)	
				.list(); //$NON-NLS-1$

		if ((sList != null) && (sList.size() == 1)) {
			SchemaInformationObject sio = (SchemaInformationObject) sList
					.get(0);
			returnSIO = sio;
		}

		MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema, true)
				.getTransaction().commit();
		MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema, true).close();

		return returnSIO;
	}
	
	/**
	 * Pulls the schemainformationobject out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static SchemaInformationObject getMe(Long schema_id) {

		MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema);
		SchemaInformationObject sio = (SchemaInformationObject) 
			MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema).get(SchemaInformationObject.class, schema_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

		return sio;

	}
	
	/** Saves a schemainformation object. */
	public void saveMe() {

		MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema);

		MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

	}

	/**
	 * Gets an email server that seems to be up.
	 * 
	 * @return
	 */
	public static SchemaInformationObject getFirstUpEmailServer() {
		
		List fList = getAllOrderedByEmailServerNumber();
		
		for (ListIterator li = fList.listIterator(); li.hasNext();) {
			SchemaInformationObject sio = (SchemaInformationObject) li.next();

			// TODOD Should probably select a 'verified' ones first, but for now ...
			if (!(sio.emailState.equalsIgnoreCase(EMAIL_STATE_DOWN))){
				return sio;
			}
		}
		
		return null;
	}

}