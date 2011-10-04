package org.usip.osp.coursemanagementinterface;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This object keeps track of an invitation sent from a facilitator to a student to register on
 * their system.
 */
 /*         This file is part of the USIP Open Simulation Platform.<br>
 * 
 *         The USIP Open Simulation Platform is free software; you can
 *         redistribute it and/or modify it under the terms of the new BSD Style
 *         license associated with this distribution.<br>
 * 
 *         The USIP Open Simulation Platform is distributed WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. <BR>
 * 
 * 
 */

@Entity
@Table(name = "USER_REG_INVITE")
@Proxy(lazy = false)
public class UserRegistrationInvite {

	public static void main(String args []){
		System.out.println("Hello World!");
		
		List uriList = getAllSetsForUser("test", new Long(1));
		
		for (ListIterator urli = uriList.listIterator(); urli.hasNext();) {
			String invitationSetName = (String) urli.next();
			System.out.println("x " + invitationSetName);
			
		}
		
		List list2 = getAllInASet("test", "a");
		
	}
	/** Database id of this User. */
	@Id
	@GeneratedValue
	@Column(name = "URI_ID")
	private Long id;
	
	/** ID of the user inviting student's to register. */
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/** Email of instructor who sent invite to register. */
	private String invitingInstructor = ""; //$NON-NLS-1$

	/** Email sent invitation. */
	private String originalInviteEmailAddress = ""; //$NON-NLS-1$

	/** Email addressed used by invited user to register. */
	private String emailAddressRegistered = ""; //$NON-NLS-1$

	/** Date the user registers. */
	@Column(name = "INVITATION_DATE", columnDefinition = "datetime")
	private Date invitationDate;

	/** Date the user registers. */
	@Column(name = "REGISTRATION_DATE", columnDefinition = "datetime")
	private Date registrationDate;

	/** Used to help keep track of a set of students invited all at one time. */
	private String invitationSet;

	private String schemaInvitedTo;

	public UserRegistrationInvite() {

	}
	
	/**
	 * Pulls the UserRegistrationInvite out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param uri_id
	 * @return
	 */
	public static UserRegistrationInvite getById(String schema, Long uri_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		UserRegistrationInvite uri = (UserRegistrationInvite) MultiSchemaHibernateUtil.getSession(schema).get(UserRegistrationInvite.class, uri_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return uri;

	}
	
	/**
	 * Gets a distinct list of invitations from a user.
	 * 
	 * @param schema
	 * @param userId
	 * @return
	 */
	public static List getAllSetsForUser(String schema, Long userId){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"select distinct u.invitationSet from UserRegistrationInvite u " +
				"where userId = :userId")
				.setLong("userId", userId).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
		
		
	}
	
    /**
     * Returns all of the actors found in a schema for a particular simulation
     * 
     * @param schema
     * @return
     */
    public static List getAllInASet(String schema, String set_code){
        
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from UserRegistrationInvite where invitationSet = :invitationSet order by uri_id")
				.setString("invitationSet", set_code).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
    }

	/**
	 * Convenience creation method.
	 * 
	 * @param invitingInstructor
	 * @param originalInviteEmailAddress
	 * @param invitationSet
	 * @param schemaInvitedTo
	 */
	public UserRegistrationInvite(String invitingInstructor,
			String originalInviteEmailAddress, String invitationSet,
			String schemaInvitedTo, Long userId) {

		this.invitingInstructor = invitingInstructor;
		this.originalInviteEmailAddress = originalInviteEmailAddress;
		this.invitationSet = invitationSet;
		this.schemaInvitedTo = schemaInvitedTo;
		this.userId = userId;
		this.invitationDate = new java.util.Date();

	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInvitingInstructor() {
		return this.invitingInstructor;
	}

	public void setInvitingInstructor(String invitingInstructor) {
		this.invitingInstructor = invitingInstructor;
	}

	public String getOriginalInviteEmailAddress() {
		return this.originalInviteEmailAddress;
	}

	public void setOriginalInviteEmailAddress(String originalInviteEmailAddress) {
		this.originalInviteEmailAddress = originalInviteEmailAddress;
	}

	public String getEmailAddressRegistered() {
		return this.emailAddressRegistered;
	}

	public void setEmailAddressRegistered(String emailAddressRegistered) {
		this.emailAddressRegistered = emailAddressRegistered;
	}

	public Date getInvitationDate() {
		return this.invitationDate;
	}

	public void setInvitationDate(Date invitationDate) {
		this.invitationDate = invitationDate;
	}

	public Date getRegistrationDate() {
		return this.registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getInvitationSet() {
		return this.invitationSet;
	}

	public void setInvitationSet(String invitationSet) {
		this.invitationSet = invitationSet;
	}

	public String getSchema() {
		return this.schemaInvitedTo;
	}

	public void setSchema(String schemaInvitedTo) {
		this.schemaInvitedTo = schemaInvitedTo;
	}

	/**
	 * Saves the UserRegistrationInvite into the database.
	 */
	public void saveMe() {

		MultiSchemaHibernateUtil.beginTransaction(schemaInvitedTo);
		MultiSchemaHibernateUtil.getSession(schemaInvitedTo).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schemaInvitedTo);

	}

}
