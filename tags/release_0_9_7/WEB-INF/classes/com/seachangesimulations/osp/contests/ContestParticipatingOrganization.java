package com.seachangesimulations.osp.contests;

import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.servlet.http.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;


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
@Proxy(lazy = false)
public class ContestParticipatingOrganization {

	@Id
	@GeneratedValue
	private Long id;

	private Long contestId;
	
	private String registrantFirstName = "";
	
	private String registrantLastName = "";
	
	private String registrantEmailAddress = "";
	
	private String registrantEmailAddress2 = "";
	
	private String phoneNumber = "";

	private String organizationName = "";
	
	private String departmentName = "";
	
	private String divisionName = "";
	
	private String addressLine1 = "";
	
	private String addressLine2 = "";
	
	private String city = "";
	
	private String state = "";
	
	private String postalCode = "";

	@Lob
	private String organizationNotes = "";
	
	private Date registrationDate = new Date();
	
	@Transient
	private String captcha_in_session = "";
	
	@Transient
	private String captcha_from_user = "";
	
	@Transient
	private boolean readyToMoveToNextStep = false;
	
	@Transient
	private String errorMsg = "";
	
	/** The Session object. */
	@Transient
	private HttpSession session = null;
	
	
	/**
	 * Returns the CPO stored in the session, or creates one.
	 */
	public static ContestParticipatingOrganization getCPO(HttpSession session) {

		ContestParticipatingOrganization cpo = (ContestParticipatingOrganization) session
				.getAttribute("cpo");

		if (cpo == null) {
			cpo = new ContestParticipatingOrganization();
			cpo.session = session;
		}

		session.setAttribute("cpo", cpo);

		return cpo;
	}

	/**
	 * Participating organization information will be added first by the
	 * registering agent, later the database will be created.
	 */
	private String organizationSchema;

	/**
	 * Pulls form values from web site and fills in 
	 * @param request
	 * @return
	 */
	public void processInitialRegistration(
			HttpServletRequest request) {

		String sending_page = request.getParameter("sending_page");

		if ((sending_page != null) && (sending_page.equalsIgnoreCase("contest_registration"))) {
			
			String contest_id = request.getParameter("contest_id");
			String first_name = request.getParameter("first_name");
			String last_name = request.getParameter("last_name");
			String email_address = request.getParameter("email_address");
			String email_address2 = request.getParameter("email_address2");
			String phone_number = request.getParameter("phone_number");
			String org_name = request.getParameter("org_name");
			String dept_name = request.getParameter("dept_name");
			String div_name = request.getParameter("div_name");
			String address_line1 = request.getParameter("address_line1");
			String address_line2 = request.getParameter("address_line2");
			String city = request.getParameter("city");
			String state_province = request.getParameter("state_province");
			String postal_code = request.getParameter("postal_code");
			
			String captchacode = request.getParameter("captchacode");
			
			
			try {
				this.setContestId(new Long(contest_id));
			} catch (Exception e){
				this.errorMsg = "No contest selected.";
			}
			
			this.setRegistrantFirstName(first_name);
			this.setRegistrantLastName(last_name);
			this.setRegistrantEmailAddress(email_address);
			this.setRegistrantEmailAddress2(email_address2);
			this.setPhoneNumber(phone_number);
			this.setOrganizationName(org_name);
			this.setDepartmentName(dept_name);
			this.setDivisionName(div_name);
			this.setAddressLine1(address_line1);
			this.setAddressLine2(address_line2);
			this.setCity(city);
			this.setState(state_province);
			this.setPostalCode(postal_code);
			this.setCaptcha_from_user(captchacode);
			
			if (checkComplete(this)){
				this.registrationDate = new Date();
				this.setReadyToMoveToNextStep(true);
				this.saveMe();
			}
		}

	}

	/**
	 * Checks to see if the cpo object created is complete.
	 * 
	 * @param cpo
	 * @return
	 */
	public static boolean checkComplete(ContestParticipatingOrganization cpo){
		
		boolean foundDeficit = false;
		
		if (!(USIP_OSP_Util.stringFieldHasValue(cpo.getRegistrantFirstName()))){
			cpo.errorMsg += "Must enter first name.</br>";
			foundDeficit = true;
		}
		
		if (!(USIP_OSP_Util.stringFieldHasValue(cpo.getRegistrantLastName()))){
			cpo.errorMsg += "Must enter last name.</br>";
			foundDeficit = true;
		}
		
		if (!(USIP_OSP_Util.stringFieldHasValue(cpo.getRegistrantEmailAddress()))){
			cpo.errorMsg += "Must enter email address.</br>";
			foundDeficit = true;
		}
		
		if (!(USIP_OSP_Util.stringFieldHasValue(cpo.getRegistrantEmailAddress2()))){
			cpo.errorMsg += "Must enter confirmation email address.</br>";
			foundDeficit = true;
		}
		
		if ((cpo.getRegistrantEmailAddress() == null) 
				|| (!(cpo.getRegistrantEmailAddress()
						.equalsIgnoreCase(cpo.getRegistrantEmailAddress2())))){
			cpo.errorMsg += "Email addresses must match.</br>";
			foundDeficit = true;
		}
		
		if (!(USIP_OSP_Util.stringFieldHasValue(cpo.getCaptcha_from_user()) )){
			cpo.errorMsg += "Must match captcha value.";
			foundDeficit = true;
		}

		if (!(cpo.getCaptcha_from_user().equalsIgnoreCase(cpo.getCaptcha_in_session())) ){
			
			cpo.errorMsg += "Must match captcha value.";
			foundDeficit = true;
		}
		
		return (!(foundDeficit));
		
	}
	
	public static ContestParticipatingOrganization getById(String cpoId) {
		
		Long id = null;
		try {
			id = new Long (cpoId);
			
		} catch (Exception e){
			return new ContestParticipatingOrganization();
		}
		
		return getById(id);
		
	}

	/**
	 * Pulls the ContestParticipatingOrganization out of the root database base
	 * on its id.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static ContestParticipatingOrganization getById(Long cpoId) {

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		ContestParticipatingOrganization cpo = (ContestParticipatingOrganization) MultiSchemaHibernateUtil
				.getSession(MultiSchemaHibernateUtil.principalschema, true)
				.get(ContestParticipatingOrganization.class, cpoId);

		MultiSchemaHibernateUtil
				.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

		return cpo;
	}
	
	/**
	 * Returns all contests found on the platform.
	 * 
	 * @return
	 */
	public static List<ContestParticipatingOrganization> getAll() {

		MultiSchemaHibernateUtil.beginTransaction(
				MultiSchemaHibernateUtil.principalschema, true);

		List returnList = MultiSchemaHibernateUtil
				.getSession(MultiSchemaHibernateUtil.principalschema, true)
				.createQuery("from ContestParticipatingOrganization").list(); //$NON-NLS-1$ 

		MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema, true).close();

		return returnList;
	}

	/**
	 * Saves this object back to the main database.
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getContestId() {
		return contestId;
	}

	public void setContestId(Long contestId) {
		this.contestId = contestId;
	}

	public String getRegistrantFirstName() {
		return registrantFirstName;
	}

	public void setRegistrantFirstName(String registrantFirstName) {
		this.registrantFirstName = registrantFirstName;
	}

	public String getRegistrantLastName() {
		return registrantLastName;
	}

	public void setRegistrantLastName(String registrantLastName) {
		this.registrantLastName = registrantLastName;
	}

	public String getRegistrantEmailAddress() {
		return registrantEmailAddress;
	}

	public void setRegistrantEmailAddress(String registrantEmailAddress) {
		this.registrantEmailAddress = registrantEmailAddress;
	}

	public String getRegistrantEmailAddress2() {
		return registrantEmailAddress2;
	}

	public void setRegistrantEmailAddress2(String registrantEmailAddress2) {
		this.registrantEmailAddress2 = registrantEmailAddress2;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getOrganizationNotes() {
		return organizationNotes;
	}

	public void setOrganizationNotes(String organizationNotes) {
		this.organizationNotes = organizationNotes;
	}

	public boolean isReadyToMoveToNextStep() {
		return readyToMoveToNextStep;
	}

	public void setReadyToMoveToNextStep(boolean readyToMoveToNextStep) {
		this.readyToMoveToNextStep = readyToMoveToNextStep;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public String getOrganizationSchema() {
		return organizationSchema;
	}

	public void setOrganizationSchema(String organizationSchema) {
		this.organizationSchema = organizationSchema;
	}
	
	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getCaptcha_in_session() {
		return captcha_in_session;
	}

	public void setCaptcha_in_session(String captcha_in_session) {
		this.captcha_in_session = captcha_in_session;
	}

	public String getCaptcha_from_user() {
		return captcha_from_user;
	}

	public void setCaptcha_from_user(String captcha_from_user) {
		this.captcha_from_user = captcha_from_user;
	}

}
