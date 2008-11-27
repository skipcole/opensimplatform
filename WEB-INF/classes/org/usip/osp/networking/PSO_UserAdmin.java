package org.usip.osp.networking;

import javax.servlet.http.HttpServletRequest;

import org.usip.osp.baseobjects.User;
import org.usip.osp.persistence.BaseUser;

/**
 * @author Ronald "Skip" Cole<br />
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
public class PSO_UserAdmin {

	private String _full_name = "";
	private String _first_name = "";
	private String _last_name = "";
	private String _middle_name = "";
	
	private boolean _makeAdmin = false;
	private boolean _makeAuthor = false;
	private boolean _makeInstructor = false;
	
	/** Flag to indicate if user is to be admin user. */
	private String _admin = "";

	/** Flag to indicate if user is to be simulation author. */
	private String _author = "";

	/** Email / username of user. */
	private String _email = "";

	/** Flag to indicate if user is to be an instructor. */
	private String _instructor = "";

	/** Password of the user. */
	private String _password = "";
	
	private ParticipantSessionObject pso;
	
	public PSO_UserAdmin(ParticipantSessionObject pso){
		
		this.pso = pso;
	}
	
	
	/**
	 * Gets the basic required parameters from the request.
	 * @param request
	 */
	private void getBaseUserParamters(HttpServletRequest request) {
		
		_full_name = (String) request.getParameter("full_name");
		_admin = (String) request.getParameter("admin");
		_author = (String) request.getParameter("author");
		_email = (String) request.getParameter("email");
		_instructor = (String) request.getParameter("instructor");
		_password = (String) request.getParameter("password");

		if ((_admin != null) && (_admin.equalsIgnoreCase("true"))) {
			_makeAdmin = true;
			_makeAuthor = true;
			_makeInstructor = true;
		} else if ((_author != null)
				&& (_author.equalsIgnoreCase("true"))) {
			_makeAuthor = true;
			_makeInstructor = true;
		} else if ((_instructor != null)
				&& (_instructor.equalsIgnoreCase("true"))) {
			_makeInstructor = true;
		}

	}
	
	/**
	 * Gets more details about the user from the request.
	 * @param request
	 */
	private void getUserDetails(HttpServletRequest request){
		_full_name = (String) request.getParameter("full_name");
		_first_name = (String) request.getParameter("first_name");
		_last_name = (String) request.getParameter("last_name");
		_middle_name = (String) request.getParameter("middle_name");
	}

	/**
	 * 
	 * @param request
	 */
	public User handleCreateAdminUser(HttpServletRequest request, String schema) {

		User user = new User();

		String command = (String) request.getParameter("command");

		if (command != null) {
			
			getBaseUserParamters(request);
			getUserDetails(request);
			
			String u_id = (String) request.getParameter("u_id");
			
			if (command.equalsIgnoreCase("Create")) {

				if (!hasEnoughInfoToCreateUser()) {
					return user;
				} else {

					try {
						user = new User(schema, _email, _password, "", "", "",
								_full_name, _email, _makeAuthor, _makeInstructor,
								_makeAdmin);
						
						user.setBu_password(_password);
						user.setBu_username(_email);
						user.setBu_first_name(_first_name);
						user.setBu_full_name(_full_name);
						user.setBu_last_name(_last_name);
						user.setBu_middle_name(_middle_name);
						
						user.saveMe(schema);

					} catch (Exception e) {
						e.printStackTrace();
						pso.errorMsg = e.getMessage();
					}
				}
			} else if (command.equalsIgnoreCase("Update")) { // 
				user = User.getMe(schema, new Long(u_id));
				user.setAdmin(_makeAdmin);
				user.setBu_first_name(_first_name);
				user.setBu_full_name(_full_name);
				user.setBu_last_name(_last_name);
				user.setBu_middle_name(_middle_name);
				user.setBu_username(_email);
				user.setBu_password(_password);
				user.setSim_author(_makeAuthor);
				user.setSim_instructor(_makeInstructor);
				
				user.saveMe(schema);
				
			} else if (command.equalsIgnoreCase("Edit")) {
				user = User.getMe(schema, new Long(u_id));
			} else if (command.equalsIgnoreCase("Clear")) { // 
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
		
		String command = (String) request.getParameter("command");

		if (command != null) {

			getBaseUserParamters(request);

			// /////////////////////////////////
			if (command.equalsIgnoreCase("Save")) {

				if (!hasEnoughInfoToCreateUser()) {
					return user;
				} else {

					try {

						user = new User(schema, _email, _password, "", "",
								"", _full_name, _email, false, false, false);

					} catch (Exception e) {
						pso.errorMsg = e.getMessage();
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

		System.out.println("p is " + _password);
		System.out.println("f is " + _full_name);
		System.out.println("e is " + _email);
		
		if (_password.trim().equalsIgnoreCase("")) {
			pso.errorMsg += "Must enter password.<br/>";
			return false;
		} else if (_full_name.trim().equalsIgnoreCase("")) {
			pso.errorMsg += "Must enter full name.<br/>";
			return false;
		} else if (_email.trim().equalsIgnoreCase("")) {
			pso.errorMsg += "Must enter email address.<br/>";
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
		
		getUserDetails(request);
		
		BaseUser bu = BaseUser.getByUserId(user_id);
		
		bu.updateMe(_first_name, _full_name, _last_name, _middle_name);
	}

}
