package org.usip.osp.networking;

import javax.servlet.http.HttpServletRequest;

import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.baseobjects.User;
import org.usip.osp.baseobjects.UserAssignment;
import org.usip.osp.persistence.BaseUser;
import org.apache.log4j.*;

/**
 * Utility class with methods to handle user administration; creation, editing, etc..
 */
 /* 
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
public class OSP_UserAdmin {

	private String _full_name = ""; //$NON-NLS-1$
	private String _first_name = ""; //$NON-NLS-1$
	private String _last_name = ""; //$NON-NLS-1$
	private String _middle_name = ""; //$NON-NLS-1$
	
	private boolean _makeAdmin = false;
	private boolean _makeAuthor = false;
	private boolean _makeInstructor = false;
	
	/** Flag to indicate if user is to be admin user. */
	private String _admin = ""; //$NON-NLS-1$

	/** Flag to indicate if user is to be simulation author. */
	private String _author = ""; //$NON-NLS-1$

	/** Email / username of user. */
	private String _email = ""; //$NON-NLS-1$

	/** Flag to indicate if user is to be an instructor. */
	private String _instructor = ""; //$NON-NLS-1$

	/** Password of the user. */
	private String _password = ""; //$NON-NLS-1$
	
	private String _phoneNumber = "";
	
	private String _profileNotes = "";
	
	private String _timeZone = "";
	
	private SessionObjectBase sob;
	
	public OSP_UserAdmin(SessionObjectBase sob){
		
		this.sob = sob;
	}
	
	
	/**
	 * Gets the basic required parameters from the request.
	 * @param request
	 */
	private void getBaseUserParamters(HttpServletRequest request) {
		
		// Gets the user's name.
		getUserNameDetails(request);
		
		this._admin = request.getParameter("admin"); //$NON-NLS-1$
		this._author = request.getParameter("author"); //$NON-NLS-1$
		this._instructor = request.getParameter("instructor"); //$NON-NLS-1$
		

		if ((this._admin != null) && (this._admin.equalsIgnoreCase("true"))) { //$NON-NLS-1$
			this._makeAdmin = true;
			this._makeAuthor = true;
			this._makeInstructor = true;
		} else if ((this._author != null)
				&& (this._author.equalsIgnoreCase("true"))) { //$NON-NLS-1$
			this._makeAuthor = true;
			this._makeInstructor = true;
		} else if ((this._instructor != null)
				&& (this._instructor.equalsIgnoreCase("true"))) { //$NON-NLS-1$
			this._makeInstructor = true;
		}

	}
	
	/**
	 * Gets details about the user from the request.
	 * @param request
	 */
	public void getUserNameDetails(HttpServletRequest request){
		
		this._email = request.getParameter("email"); //$NON-NLS-1$
		this._password = request.getParameter("password"); //$NON-NLS-1$
		
		this._first_name = request.getParameter("first_name"); //$NON-NLS-1$
		this._last_name = request.getParameter("last_name"); //$NON-NLS-1$
		this._middle_name = request.getParameter("middle_name"); //$NON-NLS-1$
		
		// Construct full name from the piece of name passed in.
		this._full_name = USIP_OSP_Util.constructName(this._first_name, this._middle_name, this._last_name);
		
		this._phoneNumber = request.getParameter("phonenumber");
		this._timeZone = request.getParameter("timezone");
		this._profileNotes = request.getParameter("profilenotes");
	}

	/**
	 * 
	 * @param request
	 */
	public User handleCreateAdminUser(HttpServletRequest request, String schema) {

		User user = new User();

		String command = request.getParameter("command"); //$NON-NLS-1$

		if (command != null) {
			
			getBaseUserParamters(request);
			
			String u_id = request.getParameter("u_id"); //$NON-NLS-1$
			
			if (command.equalsIgnoreCase("Create")) { //$NON-NLS-1$

				if (!hasEnoughInfoToCreateUser()) {
					return user;
				} else {

					try {
						user = new User(schema, this._email, this._password, "", "", "", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								this._full_name, this._makeAuthor, this._makeInstructor,
								this._makeAdmin);
						
						user.setBu_username(this._email);
						user.setBu_first_name(this._first_name);
						user.setBu_full_name(this._full_name);
						user.setBu_last_name(this._last_name);
						user.setBu_middle_name(this._middle_name);
						
						user.saveMe(schema);

					} catch (Exception e) {
						e.printStackTrace();
						this.sob.errorMsg = e.getMessage();
					}
				}
			} else if (command.equalsIgnoreCase("Update")) { //  //$NON-NLS-1$
				user = User.getById(schema, new Long(u_id));
				user.setAdmin(this._makeAdmin);
				user.setBu_first_name(this._first_name);
				user.setBu_full_name(this._full_name);
				user.setBu_last_name(this._last_name);
				user.setBu_middle_name(this._middle_name);
				user.setBu_username(this._email);
				user.setSim_author(this._makeAuthor);
				user.setSim_instructor(this._makeInstructor);
				
				user.saveMe(schema);
				
			} else if (command.equalsIgnoreCase("Edit")) { //$NON-NLS-1$
				user = User.getById(schema, new Long(u_id));
			} else if (command.equalsIgnoreCase("Clear")) { //  //$NON-NLS-1$
				// returning new simulation will clear fields.
			}

		} // End of if coming from this page and have added user.

		return user;

	}

	/**
	 * 
	 * @param request
	 */
	public User handleCreateUser(HttpServletRequest request, String schema) {

		User user = new User();
		
		String command = request.getParameter("command"); //$NON-NLS-1$
		
		String create_for_role = (String) request.getParameter("create_for_role");
		String ua_id = (String) request.getParameter("ua_id");
		
		if ((create_for_role != null) && (create_for_role.equalsIgnoreCase("true"))){
			UserAssignment ua = UserAssignment.getById(schema, new Long(ua_id));
			user.setUser_name(ua.getUsername());
			user.setBu_username(ua.getUsername());
			return user;
		}

		if (command != null) {

			getBaseUserParamters(request);

			// /////////////////////////////////
			if (command.equalsIgnoreCase("Save")) { //$NON-NLS-1$

				if (!hasEnoughInfoToCreateUser()) {
					return user;
				} else {

					try {

						user = new User(schema, this._email, this._password, 
								this._first_name, this._last_name, this._middle_name, 
								this._full_name, false, false, false);
						
						String preferred_language = request.getParameter("preferred_language");
						
						BaseUser bu = BaseUser.getByUserId(user.getId());
						bu.setPreferredLanguageCode(new Long(preferred_language));
						bu.setTempPassword(true);
						bu.setTemppasswordCleartext(this._password);
						bu.saveMe();
						
						sob.forward_on = true;
						
					} catch (Exception e) {
						this.sob.errorMsg = e.getMessage();
					}
				}
			}
		}
		
		return user;
	}

	/**
	 * Verifies that the minimal amount of information has been passed in to create a user.
	 * @return
	 */
	protected boolean hasEnoughInfoToCreateUser() {

		Logger.getRootLogger().debug("p is " + this._password); //$NON-NLS-1$
		Logger.getRootLogger().debug("f is " + this._full_name); //$NON-NLS-1$
		Logger.getRootLogger().debug("e is " + this._email); //$NON-NLS-1$
		
		if (this._password.trim().equalsIgnoreCase("")) { //$NON-NLS-1$
			this.sob.errorMsg += "Must enter password.<br/>"; //$NON-NLS-1$
			return false;
		} else if (this._full_name.trim().equalsIgnoreCase("")) { //$NON-NLS-1$
			this.sob.errorMsg += "Must enter name.<br/>"; //$NON-NLS-1$
			return false;
		} else if (this._email.trim().equalsIgnoreCase("")) { //$NON-NLS-1$
			this.sob.errorMsg += "Must enter email address.<br/>"; //$NON-NLS-1$
			return false;
		}

		return true;
	}
	
	/**
	 * Handles updates made on the 'my profile' page.
	 * @param request
	 * @param user_id
	 */
	public void handleMyProfile(HttpServletRequest request, Long user_id){
		
		getUserNameDetails(request);
		
		BaseUser bu = BaseUser.getByUserId(user_id);
		User user = User.getById(sob.schema, user_id);
		
		bu.updateMe(this._first_name, this._full_name, this._last_name, this._middle_name);
	
		user.setPhoneNumber(_phoneNumber);
		
		user.saveMe(sob.schema);

		String language_id = request.getParameter("language_id");
		sob.languageCode = new Long(language_id).intValue();
	
	
	}


	public String get_full_name() {
		return _full_name;
	}


	public void set_full_name(String fullName) {
		_full_name = fullName;
	}


	public String get_first_name() {
		return _first_name;
	}


	public void set_first_name(String firstName) {
		_first_name = firstName;
	}


	public String get_last_name() {
		return _last_name;
	}


	public void set_last_name(String lastName) {
		_last_name = lastName;
	}


	public String get_middle_name() {
		return _middle_name;
	}


	public void set_middle_name(String middleName) {
		_middle_name = middleName;
	}


	public String get_email() {
		return _email;
	}


	public void set_email(String email) {
		_email = email;
	}


	public String get_password() {
		return _password;
	}


	public void set_password(String password) {
		_password = password;
	}


	public String get_phoneNumber() {
		return _phoneNumber;
	}


	public void set_phoneNumber(String phoneNumber) {
		_phoneNumber = phoneNumber;
	}


	public String get_profileNotes() {
		return _profileNotes;
	}


	public void set_profileNotes(String profileNotes) {
		_profileNotes = profileNotes;
	}


	public String get_timeZone() {
		return _timeZone;
	}


	public void set_timeZone(String timeZone) {
		_timeZone = timeZone;
	}
	
	

	
}
