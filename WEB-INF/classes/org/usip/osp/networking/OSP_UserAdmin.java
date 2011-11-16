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
	
	/** Confirm password of the user. */
	private String _confirm_password = ""; //$NON-NLS-1$
	
	/** Password of the user. */
	private String _confirm_email = ""; //$NON-NLS-1$
	
	private String _phoneNumber = "";
	
	private String _profileNotes = "";
	
	private String _timeZone = "";
	
	private SessionObjectBase sob;
	
	public OSP_UserAdmin(SessionObjectBase sob){
		
		this.sob = sob;
	}
	
	/**
	 * Pulls permission values from the request if they are found. 
	 * 
	 * @param request
	 */
	public void getPermissionsParameters(HttpServletRequest request){
		
		String perm_level = request.getParameter("perm_level"); //$NON-NLS-1$
		
		if (perm_level == null){
			return;
		}
		
		// Start off by removing all permissions. This essentially makes the user
		// just a player
		this._makeAdmin = false;
		this._makeAuthor = false;
		this._makeInstructor = false;
		
		// Add back permissions based on what was passed in
		if (perm_level.equalsIgnoreCase("admin")) { //$NON-NLS-1$
			this._makeAdmin = true;
			this._makeAuthor = true;
			this._makeInstructor = true;
		} else if (perm_level.equalsIgnoreCase("author")) { //$NON-NLS-1$
			this._makeAuthor = true;
			this._makeInstructor = true;
		} else if ((perm_level.equalsIgnoreCase("instructor"))) { //$NON-NLS-1$
			this._makeInstructor = true;
		}
	}
	
	/**
	 * Gets the basic required parameters from the request.
	 * @param request
	 */
	private void getBaseUserParamters(HttpServletRequest request) {
		
		// Gets the user's name.
		getUserNameDetails(request);

	}
	
	/**
	 * Gets details about the user from the request.
	 * @param request
	 */
	public void getUserNameDetails(HttpServletRequest request){
		
		this._email = request.getParameter("email"); //$NON-NLS-1$
		this._password = request.getParameter("password"); //$NON-NLS-1$
		
		this._confirm_email = request.getParameter("confirm_email"); //$NON-NLS-1$
		this._confirm_password = request.getParameter("confirm_password"); //$NON-NLS-1$
		
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
	public User handlePromoteUser(HttpServletRequest request, String schema) {

		User user = new User();

		String command = request.getParameter("command"); //$NON-NLS-1$

		if (command != null) {
			
			String u_id = request.getParameter("u_id"); //$NON-NLS-1$
			
			if (command.equalsIgnoreCase("Update")) { //  //$NON-NLS-1$
				
				getPermissionsParameters(request);
				
				user = User.getById(schema, new Long(u_id));
				
				user.setAdmin(this._makeAdmin);
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
	 * Handles the creation of a user from the adminstrator interface.
	 * 
	 * @param request
	 */
	public User handleCreateUser(HttpServletRequest request, String schema) {

		User user = new User();
		
		String command = request.getParameter("command"); //$NON-NLS-1$
		
		/////////////////////////////////////////////////
		// This loads the name of the user (student) into the username fields if the 
		// facilitator has selected to create this user.
		String create_for_role = (String) request.getParameter("create_for_role");
		String ua_id = (String) request.getParameter("ua_id");
		
		if ((create_for_role != null) && (create_for_role.equalsIgnoreCase("true"))){
			UserAssignment ua = UserAssignment.getById(schema, new Long(ua_id));
			user.setUser_name(ua.getUsername());
			user.setBu_username(ua.getUsername());
			return user;
		}
		//////////////////////////////////////////////////

		if (command != null) {

			getBaseUserParamters(request);

			// /////////////////////////////////
			if (command.equalsIgnoreCase("Save")) { //$NON-NLS-1$

				if (!hasEnoughInfoToCreateUser(false)) {
					return user;
				} else {

					try {

						getPermissionsParameters(request);
						
						user = new User(schema, this._email, this._password, 
								this._first_name, this._last_name, this._middle_name, 
								this._full_name, this._makeAdmin, this._makeAuthor, this._makeInstructor);
						
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
	protected boolean hasEnoughInfoToCreateUser(boolean verifyConfirmPasswordsEmails) {
		
		boolean hasEnoughInfo = true;
		
		if (this._password.trim().equalsIgnoreCase("")) { //$NON-NLS-1$
			this.sob.errorMsg += "Must enter password.<br/>"; //$NON-NLS-1$
			hasEnoughInfo = false;
		} else if (this._full_name.trim().equalsIgnoreCase("")) { //$NON-NLS-1$
			this.sob.errorMsg += "Must enter name.<br/>"; //$NON-NLS-1$
			hasEnoughInfo = false;
		} else if (this._email.trim().equalsIgnoreCase("")) { //$NON-NLS-1$
			this.sob.errorMsg += "Must enter email address.<br/>"; //$NON-NLS-1$
			hasEnoughInfo = false;
		}
		
		if (verifyConfirmPasswordsEmails){
			if (!(this._email.equalsIgnoreCase(this._confirm_email))) {
				this.sob.errorMsg += "Email Addresses did not match<br/>";
				hasEnoughInfo = false;
			}

			if (!(this._password.equalsIgnoreCase(this._confirm_password))) {
				this.sob.errorMsg += "Passwords did not match<br/>";
				hasEnoughInfo = false;
			}
		}

		return hasEnoughInfo;
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
		user.setTimeZoneOffset(_timeZone);
		
		// Need to do 'just user' or we over write the changes we just made.
		user.saveJustUser(sob.schema);

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

	/**
	 * Loads a user with information found in this object.
	 * @param user
	 */
	public void loadUserWithData(User user) {
		
		user.setBu_first_name(this.get_first_name());
		user.setBu_full_name(this.get_full_name());
		user.setBu_last_name(this.get_last_name());
		user.setBu_middle_name(this.get_middle_name());
		user.setBu_username(this.get_email());
		user.setUser_name(this.get_email());
		
	}
	
	

	
}
