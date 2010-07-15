package org.usip.osp.persistence;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;

/* This file is part of the USIP Open Simulation Platform.<br>
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
public class ResetPasswordObject {

    @Id
    @GeneratedValue
    private Long id;
    
	@GeneratedValue
    private Date resetEmailSentTime = new Date();
	
	private double resetRandomNumber;
	
	private Date resetTime = new Date();
	
	private String userEmail;
	
	private boolean resetUsed = false;
	
	public ResetPasswordObject (){
		resetRandomNumber = Math.random(); 
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTextRepresentation(){
		
		String rndString = new Double(resetRandomNumber).toString();
		
		if (rndString.startsWith("1.")){
			rndString = rndString.replaceFirst("1.", "");
		}
		
		if (rndString.startsWith("0.")){
			rndString = rndString.replaceFirst("0.", "");
		}
		
		rndString = rndString.substring(0, 7);
		
		return rndString;
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getResetEmailSentTime() {
		return resetEmailSentTime;
	}

	public void setResetEmailSentTime(Date resetEmailSentTime) {
		this.resetEmailSentTime = resetEmailSentTime;
	}

	public double getResetRandomNumber() {
		return resetRandomNumber;
	}

	public void setResetRandomNumber(double resetRandomNumber) {
		this.resetRandomNumber = resetRandomNumber;
	}

	public Date getResetTime() {
		return resetTime;
	}

	public void setResetTime(Date resetTime) {
		this.resetTime = resetTime;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public boolean isResetUsed() {
		return resetUsed;
	}

	public void setResetUsed(boolean resetUsed) {
		this.resetUsed = resetUsed;
	}
	
	
    public void saveMe(){
        MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema, true);
        MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema, true).saveOrUpdate(this);                        
        MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
    }
	
	public static ResetPasswordObject getMe(Long rpo_id) {

		MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema);
		ResetPasswordObject rpo = (ResetPasswordObject) 
			MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema).get(ResetPasswordObject.class, rpo_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

		return rpo;

	}
	
	public static final int SHOW_EXPIRED_ACCESS_CODE_MODE = 0;
	public static final int SHOW_CHANGE_FORM_MODE = 1;
	public static final int SHOW_PASSWORD_CHANGED_MODE = 2;
	public static final int SHOW_PASSWORD_DONT_MATCH_MODE = 3;
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public static int handleResetPassword(HttpServletRequest request){
		
		ResetPasswordObject this_rpo = null;
		
		String rpo = (String) request.getParameter("rpo");
		String changeSubmitted = (String) request.getParameter("changeSubmitted");

		if (rpo != null) {
			
			this_rpo = ResetPasswordObject.getMe(new Long(rpo));
			
			request.getSession().setAttribute("this_rpo", this_rpo);
			request.getSession().setAttribute("changeMode", new Boolean(true));
			
			if ((this_rpo != null) && (!(this_rpo.isResetUsed()))){
			
				String rnd = (String) request.getParameter("rnd");
				
				if ((rnd != null) && (rnd.equalsIgnoreCase(this_rpo.getTextRepresentation()))) {
					return SHOW_CHANGE_FORM_MODE;
				}
			}
		} else if ((changeSubmitted != null) && (changeSubmitted.equalsIgnoreCase("true"))){
			
			this_rpo = (ResetPasswordObject) request.getSession().getAttribute("this_rpo");
			Boolean changeMode = (Boolean) request.getSession().getAttribute("changeMode");
			
			if (!changeMode.booleanValue()){
				return SHOW_EXPIRED_ACCESS_CODE_MODE;
			}
			
			String pword1 = (String) request.getParameter("pword1");
			String pword2 = (String) request.getParameter("pword2");
			
			if ((pword1 == null) || (pword2 == null)){
				return SHOW_PASSWORD_DONT_MATCH_MODE;
			}
			
			if (!(pword1.equalsIgnoreCase(pword2))){
				return SHOW_PASSWORD_DONT_MATCH_MODE;
			}
			
			BaseUser bu = BaseUser.getByUsername(this_rpo.getUserEmail());
			bu.setPassword(pword1);
			bu.saveMe();
			
			this_rpo.setResetTime(new Date());
			this_rpo.setResetUsed(true);
			this_rpo.saveMe();
			
			request.getSession().setAttribute("this_rpo", null);
			request.getSession().setAttribute("changeMode", new Boolean(false));
			
			return SHOW_PASSWORD_CHANGED_MODE;
		}
		
		
		return SHOW_EXPIRED_ACCESS_CODE_MODE;
	}
	
}
