package org.usip.osp.coursemanagementinterface;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.networking.SessionObjectBase;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

import java.util.*;

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
public class InstructorApplication {

	@Id
	@GeneratedValue
	private Long id;
	
	private String applicantName = "";
	
	private String applicantEmailAddress = "";

	private String adminsEmailAddress = "";
	
	@Lob
	private String applicantBackground = "";
	
	@Lob
	private String applicantDesiredUse = "";
	
	private Date applicationDate = new Date();
	
	private boolean emailSent = false;
	
	public InstructorApplication(){
		applicationDate = new Date();
		
	}
	
	public void saveMe() {

		MultiSchemaHibernateUtil
				.beginTransaction(MultiSchemaHibernateUtil.principalschema);

		MultiSchemaHibernateUtil.getSession(
				MultiSchemaHibernateUtil.principalschema).saveOrUpdate(this);

		MultiSchemaHibernateUtil
				.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getApplicantEmailAddress() {
		return applicantEmailAddress;
	}

	public void setApplicantEmailAddress(String applicantEmailAddress) {
		this.applicantEmailAddress = applicantEmailAddress;
	}

	public String getAdminsEmailAddress() {
		return adminsEmailAddress;
	}

	public void setAdminsEmailAddress(String adminsEmailAddress) {
		this.adminsEmailAddress = adminsEmailAddress;
	}

	public String getApplicantBackground() {
		return applicantBackground;
	}

	public void setApplicantBackground(String applicantBackground) {
		this.applicantBackground = applicantBackground;
	}

	public String getApplicantDesiredUse() {
		return applicantDesiredUse;
	}

	public void setApplicantDesiredUse(String applicantDesiredUse) {
		this.applicantDesiredUse = applicantDesiredUse;
	}

	public Date getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	public boolean isEmailSent() {
		return emailSent;
	}

	public void setEmailSent(boolean emailSent) {
		this.emailSent = emailSent;
	}
	
	public static InstructorApplication sendEmailAndSave(HttpServletRequest request, SessionObjectBase sob){
		
		InstructorApplication iaReturn = new InstructorApplication();
		
		String sending_page = request.getParameter("sending_page");
		
		System.out.println("c1: " + sob.captcha_code);
			
		if ((sending_page != null) && (sending_page.equalsIgnoreCase("instructor_application")) ){
			
			String captchacode = USIP_OSP_Util.cleanNulls(request
					.getParameter("captchacode"));
			
			System.out.println("c2: " + captchacode);
			
			String applicant_name = request.getParameter("applicant_name");
			String applicant_email = request.getParameter("applicant_email");
			String applicant_background = request.getParameter("applicant_background");
			String applicant_desires = request.getParameter("applicant_desires");
			
			iaReturn.setApplicantName(applicant_name);
			iaReturn.setApplicantEmailAddress(applicant_email);
			iaReturn.setApplicantBackground(applicant_background);
			iaReturn.setApplicantDesiredUse(applicant_desires);

			String message = "";
		
			//Emailer.quickPostMail(String schema, String to, "Instructor Application",
			//		message, this.getApplicantEmailAddress(), String replyTo);
		
		}
		
		return iaReturn;
	}
	
}
