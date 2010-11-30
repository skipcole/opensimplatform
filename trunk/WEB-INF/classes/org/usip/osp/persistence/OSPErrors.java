package org.usip.osp.persistence;

import java.io.*;
import java.util.Vector;

import javax.persistence.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.usip.osp.baseobjects.User;
import org.usip.osp.communications.Emailer;
import org.usip.osp.networking.AuthorFacilitatorSessionObject;
import org.usip.osp.networking.PlayerSessionObject;
import org.usip.osp.networking.SessionObjectBase;
import org.usip.osp.sharing.ObjectPackager;

/* 
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
@Proxy(lazy = false)
public class OSPErrors {

	public static final int ERROR_INCONSEQUENTIAL = 0;
	public static final int ERROR_MILD = 1;
	public static final int ERROR_ANNOYING = 2;
	public static final int ERROR_SEVERE = 3;
	public static final int ERROR_SHOWSTOPPER = 4;
	
	public static final int SOURCE_OTHER = 0;
	public static final int SOURCE_JSP = 1;
	public static final int SOURCE_JAVA = 2;
	
	
	public OSPErrors(){
		
	}
	
	/** Database id of this Simulation. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Schema in which this error occurred. */
	private String dBschema;
	
	/** User id of the user who encountered this error. */
	private Long userId;
	
	/** User requested email when this is resolved. */
	private boolean userRequestedEmail = false;
	
	/** Email of the user encountering this error */
	private String userEmail = "";
	
	/** Notes left by the user */
	@Lob
	private String userNotes = "";
	
	/** Date of this error */
	private java.util.Date errorDate = new java.util.Date();
	
	/** Error message*/
	private String errorMessage = "";
	
	/** Stacktrace of this error */
	@Lob
	private String errorText = "";
	
	/** Severity, as recorded by the player */
	private int errorSeverity = 0;
	
	/** Source of error: web, java, or other. */
	private int errorSource = 0;
	
	/** Indicates if this error has been sent */
	private boolean errorSentByEmail;
	
	/** Date email on this error was sent */
	private java.util.Date emailDate = new java.util.Date();
	
	/** Notes left by the admin on this error */
	@Lob
	private String adminNotes;
	
	/** Initials of the admin marking this error as processed */
	private String adminInitials;
	
	/** Number of this error in our issue tracker */
	private String errorNumber;
	
	/** Whether this error has been marked as dealt with. */
	private boolean errorProcessed = false;
	
	/**
	 * Pull this error out of the database.
	 * 
	 * @param error_id
	 * @return
	 */
    public static OSPErrors getById(Long error_id) {

        MultiSchemaHibernateUtil.beginTransaction(
                MultiSchemaHibernateUtil.principalschema, true);

        OSPErrors err = (OSPErrors) MultiSchemaHibernateUtil.getSession(
                MultiSchemaHibernateUtil.principalschema, true).get(
                		OSPErrors.class, error_id);

        MultiSchemaHibernateUtil
                .commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

        return err;
    }
	
    /**
     * Saves this error to the database.
     */
    public void saveMe(){
        MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema, true);
        MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema, true).saveOrUpdate(this);                        
        MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
    }
    
    /**
     * 
     * @param request
     * @return
     */
	public static OSPErrors processForm(HttpServletRequest request){
		
		OSPErrors returnError = new OSPErrors();
		
		SchemaInformationObject sio = null;
		
		try {
			String error_id = request.getParameter("error_id");
			
			if ((error_id != null) && (!(error_id.equalsIgnoreCase("null")))){
				returnError = OSPErrors.getById(new Long(error_id));
				
				String user_notes = request.getParameter("user_notes");
				String user_email = request.getParameter("user_email");
				String user_err_level = request.getParameter("user_err_level");
				
				returnError.setUserNotes(user_notes);
				returnError.setUserEmail(user_email);
				
				if ((user_err_level != null) && (!(user_err_level.equalsIgnoreCase("null")))){
					returnError.setErrorSeverity(new Long(user_err_level).intValue());
				}
				
				returnError.saveMe();
				
				sio = SchemaInformationObject.lookUpSIOByName(returnError.getdBschema());
				
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		returnError.emailErrors(sio, true);
		
		return returnError;
	}
	
	/**
	 * Checks to see if the user has a session object (AFSO or PSO) set. If so, returns the 
	 * SessionObjectBase.
	 * 
	 * @param request
	 * @return
	 */
	public static SessionObjectBase getSessionObjectBaseIfFound(HttpServletRequest request){
		
		SessionObjectBase sob = null;
		PlayerSessionObject pso = (PlayerSessionObject) request.getSession().getAttribute("pso");
		AuthorFacilitatorSessionObject afso = (AuthorFacilitatorSessionObject) request.getSession().getAttribute("afso");
		
		if (pso != null){
			sob = (SessionObjectBase) pso;
		}
		
		if (afso != null){
			sob = (SessionObjectBase) afso;
		}
		
		return sob;
	}
    
    /**
     * Takes the initial error encountered on a jsp and logs it to the database.
     * @param exception
     * @param request
     * @return
     */
    public static OSPErrors storeWebErrors(Throwable exception, HttpServletRequest request){
    	
    	System.out.println("Storing web errors");
    	
    	OSPErrors err = new OSPErrors();
    	
    	err.setErrorSource(SOURCE_JSP);
    	err.setErrorMessage("Error Message: " + exception.toString());
    	
    	SchemaInformationObject sio = null;
    	
    	try {
    		StringWriter sw = new StringWriter();
    		PrintWriter pw = new PrintWriter(sw);
    		exception.printStackTrace(pw);
    		err.setErrorText(sw.getBuffer().toString());
    		sw.close();
    		pw.close();
    		
    		SessionObjectBase sob = getSessionObjectBaseIfFound(request);
    		
    		// Must have logged in, so record additional information if found.
    		if (sob != null){
    			err.setUserId(sob.user_id);
    			err.setdBschema(sob.schema);
    			
    			User user = sob.giveMeUser();
    			if (user != null){
    				err.setUserEmail(user.getUser_name());
    			}
    			
    			sio = SchemaInformationObject.lookUpSIOByName(sob.schema);
    		}
    		
    	} catch (Exception e){
    		Logger.getRootLogger().error("ERROR IN ERROR SYSTEM!");
    		e.printStackTrace();
    	}
    	
    	err.saveMe();
    	Logger.getRootLogger().error("about to save");
    	
    	err.emailErrors(sio, false);
    	
    	return err;
    }
    
    /**
     * Emails the error, if email has been enabled.
     * 
     * @param sio
     * @param fromUser
     * @return
     */
    public boolean emailErrors(SchemaInformationObject sio, boolean fromUser){
    	
    	String subject = "ERROR on " + USIP_OSP_Properties.getValue("server_name");
    	
    	if (sio == null){
			sio = SchemaInformationObject.getFirstUpEmailServer();
			subject += ", 1st email sending";
		} else {
			subject +=  ", error on " + sio.getSchema_name();
		}
    	
    	subject += ", error number " + this.getId();
    	
    	if (fromUser) {
    		subject = "User email on " + subject;
    	}
    	
    	String lt = ObjectPackager.lineTerminator;
    	
    	String message = "Error Date " + this.getErrorDate() + lt;
    	
    	message += this.getErrorMessage() + lt + lt;
    	
    	message += this.getErrorText();
    	
    	if (fromUser){
    		message += "respond to " + this.getUserEmail() + lt;
    		message += "who stated: " + this.getUserNotes() + lt;
    	}
    	
    	System.out.println(message);
    	
    	if (sio.isEmailEnabled()){
    		Emailer.postMail(sio, sio.getTech_email_address(), subject, message, Emailer.NOREPLY_EMAIL, new Vector(), new Vector());
    		return true;
    	} else {
    		System.out.println(message);
    		return false;
    	}
    	
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getdBschema() {
		return dBschema;
	}

	public void setdBschema(String dBschema) {
		this.dBschema = dBschema;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public boolean isUserRequestedEmail() {
		return userRequestedEmail;
	}

	public void setUserRequestedEmail(boolean userRequestedEmail) {
		this.userRequestedEmail = userRequestedEmail;
	}

	public String getUserNotes() {
		return userNotes;
	}

	public void setUserNotes(String userNotes) {
		this.userNotes = userNotes;
	}

	public java.util.Date getErrorDate() {
		return errorDate;
	}

	public void setErrorDate(java.util.Date errorDate) {
		this.errorDate = errorDate;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public int getErrorSeverity() {
		return errorSeverity;
	}

	public void setErrorSeverity(int errorSeverity) {
		this.errorSeverity = errorSeverity;
	}

	public int getErrorSource() {
		return errorSource;
	}

	public void setErrorSource(int errorSource) {
		this.errorSource = errorSource;
	}

	public boolean isErrorSentByEmail() {
		return errorSentByEmail;
	}

	public void setErrorSentByEmail(boolean errorSentByEmail) {
		this.errorSentByEmail = errorSentByEmail;
	}

	public java.util.Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(java.util.Date emailDate) {
		this.emailDate = emailDate;
	}

	public String getAdminNotes() {
		return adminNotes;
	}

	public void setAdminNotes(String adminNotes) {
		this.adminNotes = adminNotes;
	}

	public String getAdminInitials() {
		return adminInitials;
	}

	public void setAdminInitials(String adminInitials) {
		this.adminInitials = adminInitials;
	}

	public String getErrorNumber() {
		return errorNumber;
	}

	public void setErrorNumber(String errorNumber) {
		this.errorNumber = errorNumber;
	}

	public boolean isErrorProcessed() {
		return errorProcessed;
	}

	public void setErrorProcessed(boolean errorProcessed) {
		this.errorProcessed = errorProcessed;
	}
    
}
