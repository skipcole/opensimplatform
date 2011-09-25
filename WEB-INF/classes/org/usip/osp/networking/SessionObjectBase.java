package org.usip.osp.networking;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.baseobjects.User;
import org.usip.osp.baseobjects.UserAssignment;
import org.usip.osp.baseobjects.UserTrailGhost;
import org.usip.osp.communications.Emailer;
import org.usip.osp.communications.Event;
import org.usip.osp.communications.InjectFiringHistory;
import org.usip.osp.communications.TimeLine;
import org.usip.osp.coursemanagementinterface.UserRegistrationInvite;
import org.usip.osp.persistence.BaseUser;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.persistence.OSPErrors;
import org.usip.osp.persistence.SchemaInformationObject;
import org.usip.osp.persistence.UILanguageObject;

import com.seachangesimulations.osp.gametime.GamePhaseCurrentTime;

/**
 * This object contains all of the methods and data to create users, and to
 * allow users to log in as either an administrator, author, instructor or
 * player.
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

	public static final int NO_ACTION = -1;
	public static final int ALL_GOOD = 0;
	public static final int CAPTCHA_WRONG = 1;
	public static final int USERNAME_MISMATCH = 2;
	public static final int INSUFFICIENT_INFORMATION = 3;
	public static final int PASSWORDS_MISMATCH = 4;
	public static final int WRONG_OLD_PASSWORD = 5;
	public static final int PASSWORDS_CHANGED = 6;
	public static final int FORCED_PASSWORD_CHANGED = 7;
	public static final int USERNAME_CHANGED = 8;
	public static final int CONFIRM_DEFAULT = 9;
	public static final int USER_FOUND = 10;
	public static final int USER_NOT_FOUND = 11;
	public static final int INSUFFICIENT_PRIVLEGE = 12;

	// //////////////////////////////////////////
	public static final int ADMIN_LOGIN = 0;
	public static final int AUTHOR_LOGIN = 1;
	public static final int FACILITATOR_LOGIN = 2;
	public static final int PLAYER_LOGIN = 3;

	// //////////////////////////////////////////

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

		Simulation simulation = new Simulation();

		if (sim_id == null) {
			return simulation;
		}

		try {
			simulation = Simulation.getById(schema, sim_id);
		} catch (Exception e) {
			OSPErrors.storeInternalWarning(
					"Error encountered trying to get simulation in SOB "
							+ e.getMessage(), this);

			simulation = new Simulation();

		}

		if (simulation == null) {
			simulation = new Simulation();
		}

		return simulation;

	}

	/** Schema of the database that the user is working in. */
	public String schema = ""; //$NON-NLS-1$

	public String schemaDisplayName = ""; //$NON-NLS-1$

	/** The page to take them back to if needed. */
	public String backPage = "index.jsp"; //$NON-NLS-1$

	/**
	 * In the rare cases where the user will need to go back to a point even
	 * further back.
	 */
	public int backBackPageCode = 0; //$NON-NLS-1$

	/** Code to indicate what kind of error was returned. */
	public int errorCode = 0;

	/** Error message to be shown to the user. */
	public String errorMsg = ""; //$NON-NLS-1$

	/** An event that is being worked on. */
	public Long draft_event_id;
	
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
	protected boolean loggedin = false;

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
	public Long runningSimId;

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
	public static String getEventsForTimeline(String schema, Long timeLineId) {

		if (timeLineId == null) {
			return "";
		}

		// TODO - below is what it eventually should be.
		// TimeLine.packupArray(TimeLineObjectAssignment.getAllForTimeline(schema,
		// timeLineId));

		return TimeLine
				.packupArray(Event.getAllForTimeLine(schema, timeLineId));

	}

	public static String getInjectFiredForTimeline(String schema, Long rs_id) {

		if (rs_id == null) {
			return "";
		}

		return TimeLine.packupArray(InjectFiringHistory.getAllTLForRunningSim(
				schema, rs_id));

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

	/**
	 * Creates a user assignment based on parameters passed into it on a web
	 * form.
	 * 
	 * @param request
	 * @return
	 */
	public UserAssignment getUserAssignBasedOnParameters(
			HttpServletRequest request) {

		UserAssignment ua = new UserAssignment();

		String ua_id = request.getParameter("ua_id"); //$NON-NLS-1$

		if ((ua_id != null) && (!(ua_id.equalsIgnoreCase("null")))) {
			ua.setId(new Long(ua_id));
		}

		String uname = request.getParameter("uname"); //$NON-NLS-1$

		ua.setUsername(uname);

		String a_id = request.getParameter("a_id"); //$NON-NLS-1$
		String s_id = request.getParameter("s_id"); //$NON-NLS-1$
		String rs_id = request.getParameter("rs_id"); //$NON-NLS-1$

		try {
			if (a_id != null) {
				ua.setActor_id(new Long(a_id));
			}
			if (s_id != null) {
				ua.setSim_id(new Long(s_id));
			}
			if (rs_id != null) {
				ua.setRunning_sim_id(new Long(rs_id));
			}

		} catch (Exception e) {
			return ua;
		}

		return ua;
	}

	/**
	 * Handles just assigning a user email to a role in a simulation (and not a
	 * completely registered student).
	 * 
	 * @param request
	 * @return
	 */
	public UserAssignment handleAssignUserEmail(HttpServletRequest request) {

		UserAssignment ua = getUserAssignBasedOnParameters(request);

		String sending_page = request.getParameter("sending_page");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("assign_just_email"))) {

			String command = request.getParameter("command");

			if (command != null) {

				this.forward_on = true;

				if (command.equalsIgnoreCase("Cancel")) {
					backPage = "../simulation_facilitation/facilitate_assign_user_to_simulation.jsp";
					return ua;
				}

				if (command.equalsIgnoreCase("Create")) {
					ua.saveMe(schema);
					backPage = "../simulation_user_admin/create_user.jsp?create_for_role=true&ua_id="
							+ ua.getId();

					return ua;
				}

				if (command.equalsIgnoreCase("Add")) {
					ua.saveMe(schema);
					backPage = "../simulation_facilitation/facilitate_assign_user_to_simulation.jsp";
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

		String user_assignment_id = request.getParameter("user_assignment_id"); //$NON-NLS-1$

		Long a_id = null;
		Long s_id = null;
		Long r_id = null;
		Long ua_id = null;
		Long user_to_add_id = null;

		if (command != null) {

			// User selected '-' icon to remove assignment.
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
			String user_id = request.getParameter("user_id"); //$NON-NLS-1$

			try {
				a_id = new Long(actor_id);
				s_id = new Long(sim_id);
				r_id = new Long(running_sim_id);

				if ((user_assignment_id != null)
						&& (!(user_assignment_id.equalsIgnoreCase("null")))) {
					ua_id = new Long(user_assignment_id);
					ua.setId(ua_id);
				}
			} catch (Exception e) {

				e.printStackTrace();
				return ua;

			}

			if ((command != null) && (command.equalsIgnoreCase("Assign User"))) { //$NON-NLS-1$

				if (user_id != null) {
					user_to_add_id = new Long(user_id);
					User thisUser = User.getById(schema, user_to_add_id);
					user_to_add_to_simulation = thisUser.getBu_username();

				} else {

					if ((user_to_add_to_simulation == null)
							|| (user_to_add_to_simulation.trim().length() == 0)) {
						return ua;
					}

					user_to_add_id = USIP_OSP_Cache.getUserIdByName(schema,
							request, user_to_add_to_simulation);
				}

				// User was not found, so must add assignment to just the
				// useremail entered.
				if (user_to_add_id == null) {

					ua.setUsername(user_to_add_to_simulation);
					ua.setSim_id(s_id);
					ua.setActor_id(a_id);
					ua.setRunning_sim_id(r_id);
					forward_on = true;

					return ua;

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

	/** Allows a user to change his or her password. */
	public int changePassword(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");
		String update = (String) request.getParameter("update");

		String forcepasswordchange = request
				.getParameter("forcepasswordchange");

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
				bu.setTempPassword(false);
				bu.setTemppasswordCleartext("");
				bu.saveMe();

				if ((forcepasswordchange != null)
						&& (forcepasswordchange.equalsIgnoreCase("true"))) {
					return FORCED_PASSWORD_CHANGED;
				} else {
					return PASSWORDS_CHANGED;
				}
			}
		}

		return ALL_GOOD;
	}

	/** Allows an admin to change his or her password. */
	public int changeUserPassword(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");
		String update = (String) request.getParameter("update");
		String user_email = (String) request.getParameter("user_email");

		if ((sending_page == null)
				|| (!(sending_page.equalsIgnoreCase("change_userpassword")))) {
			return NO_ACTION;
		}

		BaseUser bu = BaseUser.getByUsername(user_email);

		if (bu == null) {
			return USER_NOT_FOUND;
		}

		String forcepasswordchange = request
				.getParameter("forcepasswordchange");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("change_userpassword"))) {

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

				bu.setPassword(new_password);
				bu.setTempPassword(false);
				bu.setTemppasswordCleartext("");
				bu.saveMe();

				if ((forcepasswordchange != null)
						&& (forcepasswordchange.equalsIgnoreCase("true"))) {
					return FORCED_PASSWORD_CHANGED;
				} else {
					return PASSWORDS_CHANGED;
				}
			}
		}

		return ALL_GOOD;
	}

	/**
	 * Handles a request sent to update the user profile.
	 * 
	 * @param request
	 */
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

	/** The captcha set by the captcha jsp that the user must match. */
	public String sessionCaptchaCode = "";

	/** Indicates if the schema has been specified. */
	public boolean sioSet = false;

	/** Holds the value of the User Assignment Object, if it was passed in. */
	public Long uaId = null;
	
	/** Holds the value of the User Registration Information Object, if it was passed in. */
	public Long uriId = null;
	
	/** Holds the value of the Contest Team Object, if it was passed in. */
	public Long ctId = null;
	
	/**
	 * Handles the auto-registration of players. This method can be called
	 * reached in several ways 1.) When a student responds to a bulk invite. 2.)
	 * When a student responds to an invite to a particular simulation. 3.) When
	 * a student responds to a contest invitation.
	 * 
	 * @param request
	 * @return
	 */
	public User handleAutoRegistration(HttpServletRequest request) {

		// Get clean values to prepare to return.
		User user = new User();
		errorMsg = "";

		// Determine the type of registration (bulk, specific sim, contest) being attempted.
		setRegistrationType(request);

		// Set the schema if it was passed in
		setSchema(request);

		String command = request.getParameter("command"); //$NON-NLS-1$
		
		// Coming here from user action.
		if ((command != null) && (command.equalsIgnoreCase("Register"))) {

			// Create a helper object and use it to load data into the user
			// object
			OSP_UserAdmin osp_ua = new OSP_UserAdmin(this);
			osp_ua.getUserNameDetails(request);
			osp_ua.loadUserWithData(user);

			// Verify that all required information has been submitted, else
			// return
			boolean returnForLackOfInformation = returnUnFinishedUserRegGauntlet(
					user, request.getParameter("captchacode"), osp_ua);

			if (returnForLackOfInformation) {
				return user;
			}

			try {

				user = new User(schema, user.getUserName(),
						request.getParameter("password"),
						user.getBu_first_name(), user.getBu_last_name(),
						user.getBu_middle_name(), user.getBu_full_name(),
						false, false, false);

				// ///////////////////////////////////////
				if (uriId != null) {
					UserRegistrationInvite uri = UserRegistrationInvite
							.getById(schema, uriId);
					uri.setEmailAddressRegistered(user.getUserName());
					uri.setRegistrationDate(new Date());
					uri.saveMe();
				}

				// ///////////////////////////////////////
				if (uaId != null) {
					UserAssignment ua = UserAssignment.getById(schema, uaId);
					//user.setUser_name(ua.getUsername());
					//user.setBu_username(ua.getUsername());

					ua.setUser_id(user.getId());
					ua.advanceStatus(UserAssignment.STATUS_REGISTERED);
					ua.saveMe(schema);
				}

			} catch (Exception e) {
				errorMsg += e.getMessage();
				OSPErrors.storeWebErrors(e, request);
			}

			// Set so they forward on to the 'Thank You for registering' page.
			forward_on = true;

		} // End of if this is a registration attempt.

		handleComingFromBulkInvite(request, user);

		handleComingFromInstructorAssignedRole(user);

		return user;
	}

	private void handleComingFromInstructorAssignedRole(User user) {
		
		if (uaId != null) {
			UserAssignment ua = UserAssignment.getById(schema, uaId);
			user.setUser_name(ua.getUsername());
			user.setBu_username(ua.getUsername());
		}
		
	}

	private void setRegistrationType(HttpServletRequest request) {
		
		this.uaId = null;
		this.uriId = null;
		this.ctId = null;

		// A 'User Assignment ID' indicates this is from an invitation to join a
		// simulation.
		String ua_id = request.getParameter("ua_id");

		// A UserRegistrationInvite ID indicates this is from a bulk invite.
		String uri_id = (String) request.getParameter("uri");

		// A Contest Team ID this is from an invitation to join a contest team.
		String ct_id = (String) request.getParameter("ct_id");
		
		if (USIP_OSP_Util.stringFieldHasValue(ua_id)) {
			this.uaId = new Long(ua_id);
		}
		
		if (USIP_OSP_Util.stringFieldHasValue(uri_id)) {
			this.uriId = new Long(uri_id);
		}
		
		if (USIP_OSP_Util.stringFieldHasValue(ct_id)) {
			this.ctId = new Long(ct_id);
		}
		
	}

	/** Sets the schema selected to be the one that was passed in. */
	private void setSchema(HttpServletRequest request) {

		String schema = (String) request.getParameter("schema");
		sioSet = false;
		if (USIP_OSP_Util.stringFieldHasValue(schema)) {

			try {
				SchemaInformationObject sio = SchemaInformationObject
						.lookUpSIOByName(schema);
				if (sio != null) {
					this.schema = schema;
					this.schemaDisplayName = sio.getSchema_organization();
					this.sioSet = true;
				}
			} catch (Exception e) {
				errorMsg += "Invalid Database Selected.<br/>";
				OSPErrors.storeWebErrors(e, request);
			}

		}

	}

	/**
	 * Checks to see if this has been activated from a bulk invitation email.
	 * 
	 * @param request
	 * @param user
	 */
	private void handleComingFromBulkInvite(HttpServletRequest request,
			User user) {

		String initial_entry = (String) request.getParameter("initial_entry");

		UserRegistrationInvite uri = new UserRegistrationInvite();

		if ((uriId != null) && (USIP_OSP_Util.stringFieldHasValue(initial_entry))
				&& (USIP_OSP_Util.stringFieldHasValue(schema))
		){
			uri = UserRegistrationInvite.getById(schema, uriId);
			user.setBu_username(uri.getOriginalInviteEmailAddress());
		}
	}

	/**
	 * Checks to see if requisit information has been passed in.
	 * 
	 * @param user
	 * @param captchacodeTypedByUser
	 * @param osp_ua
	 * @return
	 */
	public boolean returnUnFinishedUserRegGauntlet(User user,
			String captchacodeTypedByUser, OSP_UserAdmin osp_ua) {

		boolean returnForLackOfInformation = false;

		if (!(captchacodeTypedByUser.equalsIgnoreCase(sessionCaptchaCode))) {
			errorMsg += "Incorrect Captcha Code<br/>";
			errorCode = CAPTCHA_WRONG;
			returnForLackOfInformation = true;
		}

		if (User.getByUsername(schema, user.getUserName()) != null) {
			errorMsg += "This username/email already has been registered. <br/>";
			returnForLackOfInformation = true;
		}

		if (!(osp_ua.hasEnoughInfoToCreateUser(true))) {
			// This loads its own error information.
			returnForLackOfInformation = true;
		}

		if (schema == null) {
			errorMsg += "No database selected.<br/>";
			returnForLackOfInformation = true;
		}

		return returnForLackOfInformation;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public User handleCreateUser(HttpServletRequest request) {

		String username = request.getParameter("email");

		String command = request.getParameter("command");
		if ((command != null) && (command.equalsIgnoreCase("Clear"))) { //$NON-NLS-1$

			return new User();

		}

		User user = User.getByUsername(schema, username);

		if (user != null) {
			this.errorMsg = "The user " + username + " already exists.";
			return user;
		} else {
			OSP_UserAdmin pu = new OSP_UserAdmin(this);
			return pu.handleCreateUser(request, schema);
		}
	}



	/**
	 * Handles the entry of the player to the confirmation page.
	 * 
	 * @param request
	 * @return
	 */
	public int processConfirmation(HttpServletRequest request) {

		System.out.println("Doing confirmation");

		String schema = request.getParameter("schema");
		String er_id = request.getParameter("er_id");
		String ua_id = request.getParameter("ua_id");

		if ((schema != null) && (er_id != null) & (ua_id != null)) {

			this.schema = schema;
			this.sioSet = true;

			this.uaId = new Long(ua_id);
			UserAssignment ua = UserAssignment.getById(schema, uaId);

			if (ua != null) {
				ua.advanceStatus("confirmed");
				ua.saveMe(schema);

				if (ua.getUser_id() != null) {
					return USER_FOUND;
				} else {
					return USER_NOT_FOUND;
				}
			} else {
				OSPErrors.storeInternalWarning("ua null for uaId = " + uaId,
						this);
			}
		}
		return CONFIRM_DEFAULT;
	}

	/**
	 * Called from the head of the login jsp, this attempts to log the user in.
	 * 
	 * @param request
	 * @return
	 */
	public static BaseUser handleLoginAttempt(HttpServletRequest request,
			PlayerSessionObject pso) {

		String attempting_login = (String) request
				.getParameter("attempting_login");

		if ((attempting_login != null)
				&& (attempting_login.equalsIgnoreCase("true"))) {

			BaseUser bu = validate(request);

			if (bu != null) {
				pso.languageCode = bu.getPreferredLanguageCode().intValue();
				pso.user_id = bu.getId();
				pso.user_name = bu.getUsername();
				pso.setLoggedin(true);
			}

			return bu;

		}

		return null;

	}

	/**
	 * Pulls the base user (if found) out of the database.
	 * 
	 * @param request
	 * @return
	 */
	public static BaseUser validate(HttpServletRequest request) {

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		BaseUser bu = BaseUser.validateUser(username, password);

		return bu;

	}

	public static int debugNumber = 1;

	public void debugTag() {
		debugNumber += 1;
		System.out.flush();
	}

	public void debugTag(String marker) {
		debugTag();
	}

	public boolean listOfActorsInvalidated = true;
	private List setOfActors = new ArrayList();

	public List getSetOfActors(Simulation simulation) {

		if (!(listOfActorsInvalidated)) {
			debugTag("get stored set");
			return setOfActors;
		} else {
			debugTag("getting new set");
			setOfActors = simulation.getActors(schema);
			listOfActorsInvalidated = false;
			return setOfActors;
		}
	}

	/**
	 * Handles the changing of a players user name.
	 * 
	 * @param request
	 * @return
	 */
	public int changeUserName(HttpServletRequest request) {

		if (!(this.isAdmin)) {
			return INSUFFICIENT_PRIVLEGE;
		}

		String sending_page = request.getParameter("sending_page");
		String old_username = request.getParameter("old_username");
		String new_username = request.getParameter("new_username");
		String new_username2 = request.getParameter("new_username2");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("change_password"))) {

			// Check that new emails match
			if ((new_username == null) || (new_username2 == null)) {
				return INSUFFICIENT_INFORMATION;
			}

			if (new_username.trim().length() == 0) {
				return INSUFFICIENT_INFORMATION;
			}

			if (!(new_username.equalsIgnoreCase(new_username2))) {
				return USERNAME_MISMATCH;
			}

			BaseUser bu = BaseUser.getByUsername(old_username);

			if (bu == null) {
				return USER_NOT_FOUND;
			} else {
				// change user name in in base u
				bu.setUsername(new_username);
				bu.saveMe();

				// change user name in user table
				User user = User.getById(schema, bu.getId());
				user.setUser_name(new_username);
				user.saveMe(schema);

				// change user name user assignment table
				List ua_s = UserAssignment.getAllByUserName(schema,
						new_username);

				for (ListIterator<UserAssignment> li = ua_s.listIterator(); li
						.hasNext();) {
					UserAssignment ua = li.next();
					ua.setUsername(new_username);
					ua.saveMe(schema);
				}

				String message = "The username '" + old_username
						+ "' has been changed to '" + new_username
						+ "' on the USIP OSP System "
						+ USIP_OSP_Properties.getValue("server_name");

				// send email to user at both email addresses
				Emailer.quickPostMail(schema, new_username,
						"Username Changed on USIP OSP System", message,
						user.getUserName(), user.getUserName());
				Emailer.quickPostMail(schema, old_username,
						"Username Changed on USIP OSP System", message,
						user.getUserName(), user.getUserName());

				return USERNAME_CHANGED;
			} // end if found base user in database.
		} else {
			return ALL_GOOD;
		}
	}
	
	protected boolean simUsesGameClock = false;
	private GamePhaseCurrentTime gpct = null;
	
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public String getGameTime(HttpServletRequest request) {
		
		if (!simUsesGameClock){
			return "";
		}
		
		if (gpct == null){
			gpct = GamePhaseCurrentTime.pullGPCTFromCache(request, schema, sim_id, runningSimId, phase_id);
		}
		return gpct.getGameTime(request, this);
	}

	public GamePhaseCurrentTime getGpct() {
		return gpct;
	}

	public void setGpct(GamePhaseCurrentTime gpct) {
		this.gpct = gpct;
	}
	
	
	
	
} // End of class