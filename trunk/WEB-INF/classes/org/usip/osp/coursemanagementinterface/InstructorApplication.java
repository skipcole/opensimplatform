package org.usip.osp.coursemanagementinterface;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.*;
import org.usip.osp.communications.*;
import org.usip.osp.networking.*;
import org.usip.osp.persistence.*;

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
	
	/** Flag to indicate that admin has processed this application. */
	private boolean applicationProcessed = false;
	
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
	
	public boolean isApplicationProcessed() {
		return applicationProcessed;
	}

	public void setApplicationProcessed(boolean applicationProcessed) {
		this.applicationProcessed = applicationProcessed;
	}

	/**
	 * Checks to see if we should send this application and save it.
	 * @param request
	 * @param sob
	 * @return
	 */
	public static InstructorApplication sendEmailAndSave(HttpServletRequest request, SessionObjectBase sob){
		
		InstructorApplication iaReturn = new InstructorApplication();
		
		String sending_page = request.getParameter("sending_page");
		
		System.out.println("c1: " + sob.captcha_code);
			
		if ((sending_page != null) && (sending_page.equalsIgnoreCase("instructor_application")) ){
			
			String applicant_name = request.getParameter("applicant_name");
			String applicant_email = request.getParameter("applicant_email");
			String applicant_background = request.getParameter("applicant_background");
			String applicant_desires = request.getParameter("applicant_desires");
			
			iaReturn.setApplicantName(applicant_name);
			iaReturn.setApplicantEmailAddress(applicant_email);
			iaReturn.setApplicantBackground(applicant_background);
			iaReturn.setApplicantDesiredUse(applicant_desires);
			
			if (!(iaReturn.hasEnoughData())){
				sob.errorMsg += "All fields are required.<br/>";
				sob.errorCode = SessionObjectBase.INSUFFICIENT_INFORMATION;
				return iaReturn;
			}
			
			
			String captchacode = USIP_OSP_Util.cleanNulls(request
					.getParameter("captchacode"));
			
			if ((captchacode == null) || (!(captchacode.equalsIgnoreCase(sob.captcha_code)))){
				sob.errorMsg += "Incorrect Captcha Code<br/>";
				sob.errorCode = SessionObjectBase.CAPTCHA_WRONG;

			} else {
				String message = "Dear Administrator, " + USIP_OSP_Util.lineTerminator;
				message += "I would like to conduct simulations on your platform. " + USIP_OSP_Util.lineTerminator;
				message += "A little more about me: " + iaReturn.getApplicantBackground()  + USIP_OSP_Util.lineTerminator;
				message += "I would like to use this: " + iaReturn.getApplicantDesiredUse() + USIP_OSP_Util.lineTerminator;
				message += "Sincerely," + USIP_OSP_Util.lineTerminator;
				message += iaReturn.getApplicantName() + USIP_OSP_Util.lineTerminator;
				message += iaReturn.getAdminsEmailAddress() + USIP_OSP_Util.lineTerminator;
				
				SchemaInformationObject sio = SchemaInformationObject.getFirstUpEmailServer();
				
				String to = sio.getEmailTechAddress();
				
				Emailer.quickDirectPostMailToAdmin(sio, to, "Instructor Application",
						message, iaReturn.getApplicantEmailAddress(), iaReturn.getApplicantEmailAddress());
				iaReturn.setEmailSent(true);
				iaReturn.setAdminsEmailAddress(to);
				
				iaReturn.saveMe();
			}
		
		}
		
		return iaReturn;
	}
	
	/**
	 * Looks at an application and verifies that all fields have been filled in.
	 * 
	 * @return
	 */
	public boolean hasEnoughData(){
		
		if ((this.getApplicantBackground() == null) || (this.getApplicantBackground().trim().length() == 0)){
			return false;
		}
		if ((this.getApplicantDesiredUse() == null) || (this.getApplicantDesiredUse().trim().length() == 0)){
			return false;
		}
		if ((this.getApplicantEmailAddress() == null) || (this.getApplicantEmailAddress().trim().length() == 0)){
			return false;
		}
		if ((this.getApplicantName() == null) || (this.getApplicantName().trim().length() == 0)){
			return false;
		}
		
		return true;
	}
	
}
