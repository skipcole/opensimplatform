package org.usip.osp.networking;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.baseobjects.User;
import org.usip.osp.baseobjects.UserAssignment;
import org.usip.osp.baseobjects.UserTrailGhost;
import org.usip.osp.communications.Event;
import org.usip.osp.coursemanagementinterface.UserRegistrationInvite;
import org.usip.osp.persistence.BaseUser;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.persistence.SchemaInformationObject;
import org.usip.osp.persistence.UILanguageObject;

/**
 * This object contains all of the methods and data to create users, and to allow
 * users to log in as either an administrator, author, instructor or player.
 *
 */
/*
 * 
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
public class SessionObjectBase {

	public static final int CAPTCHA_WRONG = 1;
	public static final int USERNAME_MISMATCH = 1;
	public static final int PASSWORD_MISMATCH = 1;
	
	public SessionObjectBase() {

	}

	/** Page to forward the user on to. */
	public boolean forward_on = false;

	/**
	 * Returns the simulation based on what sim_id is currently stored in this
	 * Session Object Base.
	 * 
	 * @return
	 */
	public Simulation giveMeSim() {

		if (sim_id == null) {
			return new Simulation();
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Simulation simulation = (Simulation) MultiSchemaHibernateUtil
				.getSession(schema).get(Simulation.class, sim_id);

		MultiSchemaHibernateUtil.getSession(schema).evict(simulation);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		return simulation;

	}

	/** Schema of the database that the user is working in. */
	public String schema = ""; //$NON-NLS-1$

	/** The page to take them back to if needed. */
	public String backPage = "index.jsp"; //$NON-NLS-1$

	/** Code to indicate what kind of error was returned. */
	public int errorCode = 0;

	/** Error message to be shown to the user. */
	public String errorMsg = ""; //$NON-NLS-1$

	/**
	 * We use a language code to indicate what language to show the interface
	 * in. It can be set by the simulation, or over ridden by the player.
	 */
	public int languageCode = UILanguageObject.ENGLISH_LANGUAGE_CODE;

	public int getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(int languageCode) {
		this.languageCode = languageCode;
	}
	
	/** Records if user is an admin. */
	protected boolean isAdmin = false;

	/** Records if user is authorized to create simulations. */
	protected boolean isSimAuthor = false;

	/** Records if user is authorized to facilitate simulations. */
	protected boolean isFacilitator = false;
	
	public boolean isAdmin() {
		return isAdmin;
	}

	public boolean isAuthor() {
		return isSimAuthor;
	}

	public boolean isFacilitator() {
		return isFacilitator;
	}

	/** Records the email of this user. */
	public String user_email = ""; //$NON-NLS-1$

	/** Determines if actor is logged in. */
	private boolean loggedin = false;

	public void setLoggedin(boolean loggedin) {
		this.loggedin = loggedin;
	}

	public boolean isLoggedin() {
		return loggedin;
	}

	/** ID of Simulation being conducted or worked on. */
	public Long sim_id;

	/** Name of simulation being conducted or worked on. */
	public String simulation_name = ""; //$NON-NLS-1$

	/** Version of the simulation be conducted or worked on. */
	public String simulation_version = ""; //$NON-NLS-1$

	/** Organization that created the simulation. */
	public String simulation_org = ""; //$NON-NLS-1$

	/** Id of the actor being developed */
	public Long actor_being_worked_on_id;

	/** ID of Phase being worked on. */
	public Long phase_id;

	/**
	 * Copyright string to display at the bottom of every page in the
	 * simulation.
	 */
	public String sim_copyright_info = ""; //$NON-NLS-1$

	/** ID of the Running Simulation being conducted or worked on. */
	protected Long runningSimId;

	public Long getRunningSimId() {
		return runningSimId;
	}

	public void setRunningSimId(Long runningSimId) {
		this.runningSimId = runningSimId;
	}

	/** Name of the running simulation session. */
	public String run_sim_name = ""; //$NON-NLS-1$

	/** Records the display name of this user. */
	public String userDisplayName = ""; //$NON-NLS-1$

	/** User trail ghost of this user. */
	public UserTrailGhost myUserTrailGhost = new UserTrailGhost();

	/**
	 * Pulls the running sim whose id is being stored out of the database.
	 * 
	 * @return
	 */
	public RunningSimulation giveMeRunningSim() {

		if (runningSimId == null) {
			Logger.getRootLogger().warn(
					"Warning RunningSimId is null in pso.giveMeRunningSim");

			return null;
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);
		RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil
				.getSession(schema).get(RunningSimulation.class, runningSimId);

		MultiSchemaHibernateUtil.getSession(schema).evict(rs);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return rs;
	}

	/**
	 * Returns all of the planned events for a phase.
	 * 
	 * @param schema
	 * @param sim_id
	 * @param phase_id
	 * @return
	 */
	public static String getEventsForPhase(String schema, Long sim_id,
			Long phase_id) {

		if (sim_id == null) {
			return "";
		} else if (phase_id == null) {
			Simulation sim = Simulation.getById(schema, sim_id);
			phase_id = sim.getFirstPhaseId(schema);
		}

		return Event.packupArray(Event.getAllForSimAndPhase(sim_id, phase_id,
				schema));

	}

	public static String getBaseSimURL() {
		return USIP_OSP_Properties.getValue("base_sim_url");
	}

	/** Id of User that is logged on. */
	public Long user_id;

	/**
	 * Username/ Email address of user that is logged in and using this
	 * AuthorFacilitatorSessionObject.
	 */
	public String user_name;

	/**
	 * Returns the user associated with this session.
	 * 
	 * @return
	 */
	public User giveMeUser() {

		if (this.user_id != null) {
			return User.getUser(schema, this.user_id);
		} else {
			return null;
		}
	}
	
	public UserAssignment getBasedOnParameters(HttpServletRequest request) {

		UserAssignment ua = new UserAssignment();

		String uname = request.getParameter("uname"); //$NON-NLS-1$

		ua.setUsername(uname);
		
		String a_id = request.getParameter("a_id"); //$NON-NLS-1$
		String s_id = request.getParameter("s_id"); //$NON-NLS-1$
		String rs_id = request.getParameter("rs_id"); //$NON-NLS-1$

		try {
			if (a_id != null){
				ua.setActor_id(new Long(a_id));
			}
			if (s_id != null){
				ua.setSim_id(new Long(s_id));
			}
			if (rs_id != null){
				ua.setRunning_sim_id(new Long(rs_id));
			}

		} catch (Exception e) {
			return ua;
		}
		
		return ua;
	}

	/**
	 * Handles just assigning a user email to a role in a simulation (and not 
	 * a completely registered student).
	 * 
	 * @param request
	 * @return
	 */
	public UserAssignment handleAssignUserEmail(HttpServletRequest request) {

		UserAssignment ua = getBasedOnParameters(request);

		String sending_page = request.getParameter("sending_page");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("assign_just_email"))) {
			
			String command = request.getParameter("command");
			
			if (command != null) {
				
				this.forward_on = true;
				
				if (command.equalsIgnoreCase("Cancel")){
					backPage = "../simulation_facilitation/assign_user_to_simulation.jsp";
					return ua;
				}
				
				if (command.equalsIgnoreCase("Create")){
					System.out.println("Doing Create in assign user not reg.");
					ua.saveMe(schema);
					backPage = "../simulation_facilitation/create_user.jsp";
					return ua;
				}
				
				if (command.equalsIgnoreCase("Add")){
					System.out.println("Doing Add in assign user not reg.");
					ua.saveMe(schema);
					backPage = "../simulation_facilitation/assign_useremail_to_role.jsp"
						+ ua.getAsParameterString();
					return ua;
				}

			}
		}
		return ua;

	}

	/** Assigns a user to a simulation. */
	public UserAssignment handleAssignUser(HttpServletRequest request) {

		UserAssignment ua = new UserAssignment();

		String command = request.getParameter("command"); //$NON-NLS-1$

		Long a_id = null;
		Long s_id = null;
		Long r_id = null;
		Long ua_id = null;

		if (command != null) {

			String user_assignment_id = request
					.getParameter("user_assignment_id"); //$NON-NLS-1$

			if (command.equalsIgnoreCase("remove_ua")) {
				UserAssignment.removeMe(schema, new Long(user_assignment_id));
				return ua;
			}

			String actor_id = request
					.getParameter("actor_to_add_to_simulation"); //$NON-NLS-1$
			String sim_id = request.getParameter("simulation_adding_to"); //$NON-NLS-1$
			String running_sim_id = request
					.getParameter("running_simulation_adding_to"); //$NON-NLS-1$

			// Email address of user to assign role to
			String user_to_add_to_simulation = request
					.getParameter("user_to_add_to_simulation"); //$NON-NLS-1$

			try {
				a_id = new Long(actor_id);
				s_id = new Long(sim_id);
				r_id = new Long(running_sim_id);

				if ((user_assignment_id != null)
						&& (!(user_assignment_id.equalsIgnoreCase("null")))) {
					ua_id = new Long(user_assignment_id);
				}
			} catch (Exception e) {

				e.printStackTrace();
				return ua;

			}

			if ((command != null) && (command.equalsIgnoreCase("Assign User"))) { //$NON-NLS-1$

				Long user_to_add_id = null;

				if ((user_to_add_to_simulation != null)
						&& (user_to_add_to_simulation
								.equalsIgnoreCase("remove"))) {
					errorMsg = "Removed User Assignment";

					if (ua_id != null) {
						UserAssignment.removeMe(schema, ua_id);
					}

				} else {
					user_to_add_id = USIP_OSP_Cache.getUserIdByName(schema,
							request, user_to_add_to_simulation);
					if (user_to_add_id == null) {

						ua.setUsername(user_to_add_to_simulation);
						ua.setSim_id(s_id);
						ua.setActor_id(a_id);
						ua.setRunning_sim_id(r_id);
						forward_on = true;

						return ua;

					}
				}

				// //////////////////////////////////////////////////////////
				// Add user to an existing userAssignment object
				if (ua_id != null) {
					ua = UserAssignment.getById(schema, ua_id);
				}
				if (ua != null) {
					ua.setSim_id(s_id);
					ua.setRunning_sim_id(r_id);
					ua.setActor_id(a_id);
					ua.setUser_id(user_to_add_id);
					ua.setUsername(user_to_add_to_simulation);

					ua.saveMe(schema);
				}
				// ///////////////////////////////////////////////////////
			} else if (command.equalsIgnoreCase("add_assignment")) {

				// Creating a new blank user assignment
				ua = new UserAssignment(schema, s_id, r_id, a_id, null);

			} // end of adding a ua object
		} // End of if command is not null

		return ua;
	}

	public static final int ALL_GOOD = 0;
	public static final int INSUFFICIENT_INFORMATION = 1;
	public static final int PASSWORDS_MISMATCH = 2;
	public static final int WRONG_OLD_PASSWORD = 3;
	public static final int PASSWORDS_CHANGED = 4;

	/** Assigns a user to a simulation. */
	public int changePassword(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");
		String update = (String) request.getParameter("update");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("change_password"))) {

			if (update != null) {
				String old_password = request.getParameter("old_password");
				String new_password = request.getParameter("new_password");
				String new_password2 = request.getParameter("new_password2");

				if ((old_password == null) || (new_password == null)
						|| (new_password == null)) {
					return INSUFFICIENT_INFORMATION;
				}

				if (!(new_password.equals(new_password2))) {
					return PASSWORDS_MISMATCH;
				}

				BaseUser bu = BaseUser.validateUser(this.user_name,
						old_password);
				if (bu == null) {
					return WRONG_OLD_PASSWORD;
				}

				bu.setPassword(new_password);
				return PASSWORDS_CHANGED;
			}
		}

		return ALL_GOOD;
	}
	
	public void handleMyProfile(HttpServletRequest request) {
		
		String sending_page = (String) request.getParameter("sending_page");
		String update = (String) request.getParameter("update");

		// /////////////////////////////////
		if ((sending_page != null) && (update != null)
				&& (sending_page.equalsIgnoreCase("my_profile"))) {
			OSP_UserAdmin pu = new OSP_UserAdmin(this);
			pu.handleMyProfile(request, user_id);
		
		}
	}
	
	public String captcha_code = "";

	/**
	 * Handles the auto-registration of players.
	 * 
	 * @param request
	 * @return
	 */
	public User handleAutoRegistration(HttpServletRequest request) {

		User user = new User();

		String command = request.getParameter("command"); //$NON-NLS-1$

		if ((command != null) && (command.equalsIgnoreCase("Register"))) {

			String captchacode = USIP_OSP_Util.cleanNulls(request
					.getParameter("captchacode"));

			/* Must have a schema id to now where to put the registered user. */
			String schema_id = request.getParameter("schema_id"); //$NON-NLS-1$

			if (schema_id == null) {
				return user;
			}

			SchemaInformationObject sio = SchemaInformationObject
					.getById(new Long(schema_id));

			String uri_id = (String) request.getParameter("uri");

			UserRegistrationInvite uri = new UserRegistrationInvite();
			boolean recordSaveToURI = false;

			if ((uri_id != null) && (!(uri_id.equalsIgnoreCase("null")))) {
				uri = UserRegistrationInvite.getById(sio.getSchema_name(),
						new Long(uri_id));
				recordSaveToURI = true;
			}

			OSP_UserAdmin osp_ua = new OSP_UserAdmin(this);

			osp_ua.getUserNameDetails(request);

			user.setBu_first_name(osp_ua.get_first_name());
			user.setBu_full_name(osp_ua.get_full_name());
			user.setBu_last_name(osp_ua.get_last_name());
			user.setBu_middle_name(osp_ua.get_middle_name());
			user.setBu_username(osp_ua.get_email());
			user.setUser_name(osp_ua.get_email());

			String confirm_email = request.getParameter("confirm_email"); //$NON-NLS-1$
			String password = request.getParameter("password"); //$NON-NLS-1$
			String confirm_password = request.getParameter("confirm_password"); //$NON-NLS-1$

			boolean returnForLackOfInformation = false;

			if (!(captchacode.equalsIgnoreCase(captcha_code))) {
				errorMsg += "Incorrect Captcha Code<br/>";
				errorCode = CAPTCHA_WRONG;
				returnForLackOfInformation = true;
			}

			if (!(user.getUser_name().equalsIgnoreCase(confirm_email))) {
				errorMsg += "Email Addresses did not match<br/>";
				errorCode = USERNAME_MISMATCH;
				returnForLackOfInformation = true;
			}

			if (!(password.equalsIgnoreCase(confirm_password))) {
				errorMsg += "Passwords did not match<br/>";
				errorCode = PASSWORD_MISMATCH;
				returnForLackOfInformation = true;
			}

			if (returnForLackOfInformation) {
				return user;
			}

			if (User.getByUsername(sio.getSchema_name(), user.getUser_name()) != null) {
				errorMsg += "This username/email already has been registered. <br/>";
				return user;
			}

			if (!(osp_ua.hasEnoughInfoToCreateUser())) {
				return user;
			} else {

				try {

					user = new User(sio.getSchema_name(), user.getUser_name(),
							password, user.getBu_first_name(), user
									.getBu_last_name(), user
									.getBu_middle_name(), user
									.getBu_full_name(), false, false, false);

					if (recordSaveToURI) {
						uri.setEmailAddressRegistered(user.getUser_name());
						uri.setRegistrationDate(new Date());
						uri.saveMe();
					}
				} catch (Exception e) {
					errorMsg = e.getMessage();
				}

				// Set so they forward on to the 'Thank You for registering'
				// page.
				forward_on = true;
			}

		}

		return user;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public User handleCreateUser(HttpServletRequest request) {
		
		String username = request.getParameter("email");
		
		User user = User.getByUsername(schema, username);
		
		if (user != null){
			this.errorMsg = "The user " + username + " already exists.";
			return user; 
		} else {
			OSP_UserAdmin pu = new OSP_UserAdmin(this);
			return pu.handleCreateUser(request, schema);
		}
	}

}
