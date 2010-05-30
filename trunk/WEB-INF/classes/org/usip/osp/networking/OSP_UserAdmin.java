package org.usip.osp.networking;

import javax.servlet.http.HttpServletRequest;

import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.baseobjects.User;
import org.usip.osp.persistence.BaseUser;
import org.apache.log4j.*;

/**
 * Utility class with methods to handle user administration; creation, editing, etc..
 *
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
	
	private AuthorFacilitatorSessionObject pso;
	
	public OSP_UserAdmin(AuthorFacilitatorSessionObject pso){
		
		this.pso = pso;
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
		this._email = request.getParameter("email"); //$NON-NLS-1$
		this._instructor = request.getParameter("instructor"); //$NON-NLS-1$
		this._password = request.getParameter("password"); //$NON-NLS-1$

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
	 * Gets name details about the user from the request.
	 * @param request
	 */
	private void getUserNameDetails(HttpServletRequest request){
		
		this._first_name = request.getParameter("first_name"); //$NON-NLS-1$
		this._last_name = request.getParameter("last_name"); //$NON-NLS-1$
		this._middle_name = request.getParameter("middle_name"); //$NON-NLS-1$
		
		// Construct full name from the piece of name passed in.
		this._full_name = USIP_OSP_Util.constructName(this._first_name, this._middle_name, this._last_name);
		
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
								this._full_name, this._email, this._makeAuthor, this._makeInstructor,
								this._makeAdmin);
						
						user.setBu_password(this._password);
						user.setBu_username(this._email);
						user.setBu_first_name(this._first_name);
						user.setBu_full_name(this._full_name);
						user.setBu_last_name(this._last_name);
						user.setBu_middle_name(this._middle_name);
						
						user.saveMe(schema);

					} catch (Exception e) {
						e.printStackTrace();
						this.pso.errorMsg = e.getMessage();
					}
				}
			} else if (command.equalsIgnoreCase("Update")) { //  //$NON-NLS-1$
				user = User.getMe(schema, new Long(u_id));
				user.setAdmin(this._makeAdmin);
				user.setBu_first_name(this._first_name);
				user.setBu_full_name(this._full_name);
				user.setBu_last_name(this._last_name);
				user.setBu_middle_name(this._middle_name);
				user.setBu_username(this._email);
				user.setBu_password(this._password);
				user.setSim_author(this._makeAuthor);
				user.setSim_instructor(this._makeInstructor);
				
				user.saveMe(schema);
				
			} else if (command.equalsIgnoreCase("Edit")) { //$NON-NLS-1$
				user = User.getMe(schema, new Long(u_id));
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
		
		

		if (command != null) {

			getBaseUserParamters(request);

			// /////////////////////////////////
			if (command.equalsIgnoreCase("Save")) { //$NON-NLS-1$

				if (!hasEnoughInfoToCreateUser()) {
					return user;
				} else {

					try {

						user = new User(schema, this._email, this._password, "", "", //$NON-NLS-1$ //$NON-NLS-2$
								"", this._full_name, this._email, false, false, false); //$NON-NLS-1$
						
						String preferred_language = request.getParameter("preferred_language");
						
						BaseUser bu = BaseUser.getByUserId(user.getId());
						bu.setPreferredLanguageCode(new Long(preferred_language));
						bu.saveMe();
						
					} catch (Exception e) {
						this.pso.errorMsg = e.getMessage();
					}
				}
			}
		}
		
		return user;
	}
	
	/**
	 * 
	 * @param request
	 */
	public User handleAutoRegistration(HttpServletRequest request) {

		User user = new User();
		
		String command = request.getParameter("command"); //$NON-NLS-1$

		if (command != null) {

			getBaseUserParamters(request);
			
			String schema = request.getParameter("selected_schema"); //$NON-NLS-1$

			// /////////////////////////////////
			if (command.equalsIgnoreCase("Register")) { //$NON-NLS-1$

				if (!hasEnoughInfoToCreateUser()) {
					return user;
				} else {

					try {

						user = new User(schema, this._email, this._password, this._first_name, this._last_name,
								this._middle_name, this._full_name, this._email, false, false, false);
						
						this.pso.forward_on = true;
						
					} catch (Exception e) {
						this.pso.errorMsg = e.getMessage();
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
	private boolean hasEnoughInfoToCreateUser() {

		Logger.getRootLogger().debug("p is " + this._password); //$NON-NLS-1$
		Logger.getRootLogger().debug("f is " + this._full_name); //$NON-NLS-1$
		Logger.getRootLogger().debug("e is " + this._email); //$NON-NLS-1$
		
		if (this._password.trim().equalsIgnoreCase("")) { //$NON-NLS-1$
			this.pso.errorMsg += "Must enter password.<br/>"; //$NON-NLS-1$
			return false;
		} else if (this._full_name.trim().equalsIgnoreCase("")) { //$NON-NLS-1$
			this.pso.errorMsg += "Must enter name.<br/>"; //$NON-NLS-1$
			return false;
		} else if (this._email.trim().equalsIgnoreCase("")) { //$NON-NLS-1$
			this.pso.errorMsg += "Must enter email address.<br/>"; //$NON-NLS-1$
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
		
		bu.updateMe(this._first_name, this._full_name, this._last_name, this._middle_name);
	}

}
