package org.usip.osp.persistence;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;

/**
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
@Table(name = "USER_REG_INVITE")
@Proxy(lazy = false)
public class UserRegistrationInvite {

	/** Database id of this User. */
	@Id
	@GeneratedValue
	@Column(name = "URI_ID")
	private Long id;

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
	 * Convenience creation method.
	 * 
	 * @param invitingInstructor
	 * @param originalInviteEmailAddress
	 * @param invitationSet
	 * @param schemaInvitedTo
	 */
	public UserRegistrationInvite(String invitingInstructor,
			String originalInviteEmailAddress, String invitationSet,
			String schemaInvitedTo) {

		this.invitingInstructor = invitingInstructor;
		this.originalInviteEmailAddress = originalInviteEmailAddress;
		this.invitationSet = invitationSet;
		this.schemaInvitedTo = schemaInvitedTo;
		
		this.registrationDate = new java.util.Date();

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

	public String getSchemaInvitedTo() {
		return this.schemaInvitedTo;
	}

	public void setSchemaInvitedTo(String schemaInvitedTo) {
		this.schemaInvitedTo = schemaInvitedTo;
	}

	public void saveMe() {

		MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema);
		MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

	}

}
