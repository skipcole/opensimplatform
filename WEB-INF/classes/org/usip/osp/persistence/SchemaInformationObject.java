package org.usip.osp.persistence;

import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.networking.PlayerSessionObject;
import org.apache.log4j.*;

/**
 * This class represents a schema database that has been created to hold
 * simulation data.
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
@Table(name = "SCHEMAINFORMATION")
@Proxy(lazy = false)
public class SchemaInformationObject {

	public static final String EMAIL_STATE_VERIFIED = "verified";
	public static final String EMAIL_STATE_UNVERIFIED = "unverified";
	public static final String EMAIL_STATE_DOWN = "down";
	
	@Id
	@GeneratedValue
	@Column(name = "SIO_ID")
	private Long id;

	@Column(name = "SCHEMANAME", unique = true)
	private String schema_name;

	/** Organization for which this schema has been created. */
	private String schema_organization;
	
	private String schemaOrganizationLogo = "";
	
	private String schemaOrganizationBanner = "";
	
	private String schemaOrganizationWebsite = "";	

	/** Creation date of this schema */
	private Date creationDate;
	
	/** Email SMTP server */
	private String email_smtp;

	/** Email authorized user */
	private String smtp_auth_user;

	/** Email password for authorized user. */
	private String smtp_auth_password;

	/** Email archive address - an email address that should receive a copy of all outgoing emails
	 * from the system to keep a record. */
	private String email_archive_address;
	
	/** A non-monitored email address (generally) from which system emails are sent. */
	private String email_noreply_address = "noreply@opensimplatform.org";
	
	/** Email to send system error messages to */
	private String email_tech_address = "tech@opensimplatform.org";
	
	/** Level above which to send an email to tech support */
	private int systemAlertLevel = 0;
	
	@Lob
	/** notes for this particular installation. */
	private String notes = "";
	
	/** Indicates if the email system has been verified and if it is up. */
	private String emailState = "";

	/** If multiple schema exist, multiple email servers may be declared. 
	 * When email is sent from 'the system' it can try the email servers in the order
	 * in which they are numbered according to their emailServerNumber.
	 */
	private Long emailServerNumber;
	
	/**
	 * Keeps track of the last time an author, instructor or admin has logged
	 * on.
	 */
	private Date lastLogin;
	
	private boolean provideSpecificLicences = false;
	
	private String setOfPlatformSpecificFeaturesAvailable = "";
	

	private Long preferredLanguageCode = new Long(
			UILanguageObject.ENGLISH_LANGUAGE_CODE);

	public Long getPreferredLanguageCode() {
		return preferredLanguageCode;
	}

	public void setPreferredLanguageCode(Long preferredLanguageCode) {
		this.preferredLanguageCode = preferredLanguageCode;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getEmailState() {
		return emailState;
	}

	public void setEmailState(String emailState) {
		this.emailState = emailState;
	}

	public Long getEmailServerNumber() {
		return emailServerNumber;
	}

	public void setEmailServerNumber(Long emailServerNumber) {
		this.emailServerNumber = emailServerNumber;
	}

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

	/**
	 * Just used for debugging purposes.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		List x = getAllOrderedByEmailServerNumber();

		for (ListIterator li = x.listIterator(); li.hasNext();) {
			SchemaInformationObject sio = (SchemaInformationObject) li.next();

			System.out.println(sio.getSchema_name());
		}

		SchemaInformationObject sio = SchemaInformationObject.getById(new Long(
				1));
		System.out.println(sio.getSchema_name());

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
	 * Returns a list of all of the SchemaInformationObjects (SIOs) found
	 * ordered by Email Server Number
	 * 
	 * @return
	 */
	public static List<SchemaInformationObject> getAllOrderedByEmailServerNumber() {

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		List<SchemaInformationObject> returnList = MultiSchemaHibernateUtil
				.getSession(MultiSchemaHibernateUtil.principalschema, true)
				.createQuery(
						"from SchemaInformationObject order by emailServerNumber asc").list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil
				.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

		return returnList;
	}

	public String getSchema_name() {
		return this.schema_name;
	}

	public void setSchema_name(String schema_name) {
		this.schema_name = schema_name;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String toString() {
		return "schema: " + this.schema_name + "\n\r"
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

	public String getSchemaOrganizationLogo() {
		return schemaOrganizationLogo;
	}

	public void setSchemaOrganizationLogo(String schemaOrganizationLogo) {
		this.schemaOrganizationLogo = schemaOrganizationLogo;
	}

	public String getSchemaOrganizationBanner() {
		return schemaOrganizationBanner;
	}

	public void setSchemaOrganizationBanner(String schemaOrganizationBanner) {
		this.schemaOrganizationBanner = schemaOrganizationBanner;
	}

	public String getSchemaOrganizationWebsite() {
		return schemaOrganizationWebsite;
	}

	public void setSchemaOrganizationWebsite(String schemaOrganizationWebsite) {
		this.schemaOrganizationWebsite = schemaOrganizationWebsite;
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
				.setString("schemaName", schemaName).list(); //$NON-NLS-1$

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

	public String getEmailNoreplyAddress() {
		return email_noreply_address;
	}

	public void setEmailNoreplyAddress(String noreplyEmailAddress) {
		email_noreply_address = noreplyEmailAddress;
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

	public String getEmailTechAddress() {
		return email_tech_address;
	}

	public void setEmailTechAddress(String techEmailAddress) {
		email_tech_address = techEmailAddress;
	}

	public int getSystemAlertLevel() {
		return systemAlertLevel;
	}

	public void setSystemAlertLevel(int systemAlertLevel) {
		this.systemAlertLevel = systemAlertLevel;
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
				.setString("schemaName", schemaName).list(); //$NON-NLS-1$

		if ((sList != null) && (sList.size() == 1)) {
			SchemaInformationObject sio = (SchemaInformationObject) sList
					.get(0);
			returnSIO = sio;
		}
		MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema, true).close();

		return returnSIO;
	}

	/**
	 * Pulls the schemainformationobject out of the database base on its id and
	 * schema. 
	 * Note: we have to set the rootFlag as true to get the object from the rootschema.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static SchemaInformationObject getById(Long schema_id) {

		MultiSchemaHibernateUtil
				.beginTransaction(MultiSchemaHibernateUtil.principalschema, true);
		SchemaInformationObject sio = (SchemaInformationObject) MultiSchemaHibernateUtil
				.getSession(MultiSchemaHibernateUtil.principalschema, true).get(
						SchemaInformationObject.class, schema_id);

		MultiSchemaHibernateUtil
				.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

		return sio;

	}

	/** Saves a schemainformation object. */
	public void saveMe() {

		MultiSchemaHibernateUtil
				.beginTransaction(MultiSchemaHibernateUtil.principalschema);

		MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema).saveOrUpdate(this);

		MultiSchemaHibernateUtil
				.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

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

			// TODOD Should probably select a 'verified' ones first, but for now
			// ...
			if (!(sio.emailState.equalsIgnoreCase(EMAIL_STATE_DOWN))) {
				return sio;
			}
		}

		return null;
	}

	/**
	 * Just cleans fields up for presentation on a web form.
	 * 
	 */
	public void cleanForPresentation() {

		if (this.getSchema_name() == null) {
			this.setSchema_name("");
		}

		if (this.getSchema_organization() == null) {
			this.setSchema_organization("");
		}

		if (this.getEmail_archive_address() == null) {
			this.setEmail_archive_address("");
		}

		if (this.getEmail_smtp() == null) {
			this.setEmail_smtp("");
		}

		if (this.getEmailServerNumber() == null) {
			this.setEmailServerNumber(new Long(1));
		}

		if (this.getSmtp_auth_password() == null) {
			this.setSmtp_auth_password("");
		}

		if (this.getSmtp_auth_user() == null) {
			this.setSmtp_auth_user("");
		}
	}

	/**
	 * This simply checks to see if informtion has provided to send emails. (It
	 * doesn't check to see if this information is correct.) One side affect of
	 * this
	 * 
	 * @return
	 */
	public boolean checkReqEmailInfoAndMaybeMarkDown() {

		if (((email_smtp != null) && (email_smtp.trim().length() > 0))
				&& ((smtp_auth_user != null) && (smtp_auth_user.trim().length() > 0))
				&& ((smtp_auth_password != null) && (smtp_auth_password.trim()
						.length() > 0))
				&& ((email_archive_address != null) && (email_archive_address
						.trim().length() > 0))) {
			return true;
		} else {
			this.setEmailState(EMAIL_STATE_DOWN);
			return false;
		}

	}
	
	public boolean isEmailEnabled(){
		if (!(emailState.equalsIgnoreCase(SchemaInformationObject.EMAIL_STATE_DOWN))){
			return true;
		}
		
		return false;
	}

	public void loadInfoIntoSessionObjectBase(PlayerSessionObject pso) {

		pso.schema = this.getSchema_name();
		pso.schemaOrg = this.getSchema_organization();
		
		// Set these away from the default if information has been entered.
		if (USIP_OSP_Util.stringFieldHasValue(this.schemaOrganizationBanner)){
			pso.setSchemaOrgBanner(this.getSchemaOrganizationBanner());
		}
		
		if (USIP_OSP_Util.stringFieldHasValue(this.schemaOrganizationBanner)){
			pso.setSchemaOrgLogo(this.getSchemaOrganizationLogo());
		}
		
		pso.setSchemaOrgName(this.getSchema_organization());
		pso.setSchemaOrgWebsite(this.getSchemaOrganizationBanner());
		
	}

}
