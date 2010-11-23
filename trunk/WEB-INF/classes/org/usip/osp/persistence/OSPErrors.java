package org.usip.osp.persistence;

import java.io.*;

import javax.persistence.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.usip.osp.networking.AuthorFacilitatorSessionObject;
import org.usip.osp.networking.PlayerSessionObject;

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

	public static final int ERROR_MILD = 0;
	public static final int ERROR_ANNOYING = 1;
	public static final int ERROR_SEVERE = 2;
	public static final int ERROR_SHOWSTOPPER = 3;
	
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
	
	/** Email of the user encountering this error */
	private String userEmail;
	
	/** Notes left by the user */
	@Lob
	private String userNotes;
	
	/** Date of this error */
	private java.util.Date errorDate = new java.util.Date();
	
	/** Error message*/
	private String errorMessage = "";
	
	/** Stacktrace of this error */
	@Lob
	private String errorText = "";
	
	/** Severity, as recorded by the player */
	private int errorSeverity = 0;
	
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
    
    public static Long storeWebErrors(Throwable exception, HttpServletRequest request){
    	
    	OSPErrors err = new OSPErrors();
    	
    	err.setErrorMessage(exception.getMessage());
    	
    	try {
    		StringWriter sw = new StringWriter();
    		PrintWriter pw = new PrintWriter(sw);
    		exception.printStackTrace(pw);
    		err.setErrorText(sw.getBuffer().toString());
    		sw.close();
    		pw.close();
    		
    		PlayerSessionObject pso = (PlayerSessionObject) request.getSession().getAttribute("pso");
    		AuthorFacilitatorSessionObject afso = (AuthorFacilitatorSessionObject) request.getSession().getAttribute("afso");
    		
    	} catch (Exception e){
    		Logger.getRootLogger().error("ERROR IN ERROR SYSTEM!");
    		e.printStackTrace();
    	}
    	
    	err.saveMe();
    	
    	return err.getId();
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
