package org.usip.osp.networking;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.io.File;
import java.sql.*;

import javax.servlet.ServletContext;
import javax.servlet.http.*;

import org.usip.osp.baseobjects.*;
import org.usip.osp.communications.*;
import org.usip.osp.coursemanagementinterface.UserRegistrationInvite;
import org.usip.osp.persistence.*;
import org.apache.log4j.Logger;
import org.usip.osp.sharing.*;
import org.usip.osp.specialfeatures.*;

import com.oreilly.servlet.MultipartRequest;
import com.seachangesimulations.osp.contests.Contest;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * This object contains all of the session information for the simulation author
 * /facilitator and is the main interface to all of the java objects that they
 * will interact with.
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
public class AuthorFacilitatorSessionObject extends SessionObjectBase {

	/** Determines if actor is logged in. */
	private boolean loggedin = false;

	/** Organization of the schema that the user is working in. */
	public String schemaOrg = ""; //$NON-NLS-1$

	/** The page to move them on to, if needed. */
	public String nextPage = "index.jsp"; //$NON-NLS-1$

	/** Name of the actor being played or worked on. */
	public String actor_name = ""; //$NON-NLS-1$

	/** Indicates if user has selected a phase. */
	public boolean phaseSelected = false;

	/** Name of phase being conducted or worked on. */
	private String phaseName = ""; //$NON-NLS-1$

	/** The Session object. */
	private HttpSession session = null;

	public List tempSimSecList = new ArrayList();

	static {
		makeUploadDir();

	}

	/**
	 * Unpacks a simulation from an XML file.
	 * 
	 * @param request
	 */
	public Simulation handleUnpackSimulationVersion(HttpServletRequest request) {

		Simulation sim = new Simulation();

		String filename = request.getParameter("filename"); //$NON-NLS-1$

		Logger.getRootLogger().debug("unpacking " + filename); //$NON-NLS-1$

		SimulationVersion simBase = ObjectPackager.unpackSimBase(filename,
				this.schema);

		sim.setSimulationName(simBase.getSimulationName());
		sim.setSoftwareVersion(simBase.getSoftwareVersion());
		sim.setVersion(simBase.getVersion());

		return sim;

	}

	/**
	 * Unpacks a simulation from an XML file.
	 * 
	 * @param request
	 */
	public void handleUnpackSimulation(HttpServletRequest request) {

		String filename = request.getParameter("filename"); //$NON-NLS-1$
		String sim_name = request.getParameter("sim_name"); //$NON-NLS-1$
		String sim_version = request.getParameter("sim_version"); //$NON-NLS-1$
		String upgrade_file_name = request.getParameter("upgrade_file_name"); //$NON-NLS-1$

		Logger.getRootLogger().debug("unpacking " + filename); //$NON-NLS-1$

		ObjectPackager.unpackageSim(filename, this.schema, sim_name,
				sim_version, upgrade_file_name, this, user_id,
				this.userDisplayName, this.user_email);

	}

	public IndividualLink handleCreateorUpdateIndividualLink(
			HttpServletRequest request) {
		return null;
	}

	/**
	 * This responds to one of threee commands:
	 * <ol>
	 * <li>Create a new phase.</li>
	 * <li>Queue up a phase for editing.</li>
	 * <li>Update a phase.</li>
	 * </ol>
	 * 
	 * @param sim
	 * @param request
	 * @return
	 */
	public SimulationPhase handleCreateOrUpdatePhase(Simulation sim,
			HttpServletRequest request) {

		SimulationPhase returnSP = new SimulationPhase();

		String command = request.getParameter("command"); //$NON-NLS-1$
		String phase_name = request.getParameter("phase_name"); //$NON-NLS-1$
		String phase_notes = request.getParameter("phase_notes"); //$NON-NLS-1$
		String nominal_order = request.getParameter("nominal_order"); //$NON-NLS-1$

		if (command != null) {
			if (command.equalsIgnoreCase("Create")) { //$NON-NLS-1$
				returnSP.setName(phase_name);
				returnSP.setNotes(phase_notes);
				returnSP.setOrder(string2Int(nominal_order));
				returnSP.saveMe(this.schema);

				@SuppressWarnings("unused")
				SimPhaseAssignment spa = new SimPhaseAssignment(this.schema,
						sim.getId(), returnSP.getId());

				List<Actor> ctrl_actors = Actor.getControlActors(schema,
						sim.getId());

				for (ListIterator<Actor> li = ctrl_actors.listIterator(); li
						.hasNext();) {
					Actor this_act = li.next();

				}

				// I'm not really sure why we are saving the simulation here
				// (this may be a relic),
				// but it does update the last edit time.
				sim.saveMe(this.schema);

			} else if (command.equalsIgnoreCase("Edit")) { //$NON-NLS-1$
				String sp_id = request.getParameter("sp_id"); //$NON-NLS-1$
				returnSP = SimulationPhase.getById(this.schema, sp_id);
			} else if (command.equalsIgnoreCase("Update")) { //  //$NON-NLS-1$
				String sp_id = request.getParameter("sp_id"); //$NON-NLS-1$
				returnSP = SimulationPhase.getById(this.schema, sp_id);
				returnSP.setName(phase_name);
				returnSP.setNotes(phase_notes);
				returnSP.setOrder(string2Int(nominal_order));
				returnSP.saveMe(this.schema);
				Simulation.updateSimsLastEditDate(sim_id, schema);
			} else if (command.equalsIgnoreCase("Clear")) { //  //$NON-NLS-1$
				// returning new simulation phase will clear fields.
			}
		}
		return returnSP;
	}

	/**
	 * 
	 * @param sim
	 * @param request
	 * @return
	 */
	public SimulationMetaPhase handleCreateOrUpdateMetaPhase(
			HttpServletRequest request) {

		SimulationMetaPhase returnMP = new SimulationMetaPhase();

		String command = request.getParameter("command"); //$NON-NLS-1$
		String meta_phase_name = request.getParameter("meta_phase_name"); //$NON-NLS-1$
		String meta_phase_notes = request.getParameter("meta_phase_notes"); //$NON-NLS-1$
		String meta_phase_color = request.getParameter("meta_phase_color"); //$NON-NLS-1$

		String mp_id = request.getParameter("mp_id"); //$NON-NLS-1$

		if (command != null) {
			if (command.equalsIgnoreCase("Create")) { //$NON-NLS-1$
				returnMP.setMetaPhaseName(meta_phase_name);
				returnMP.setMetaPhaseNotes(meta_phase_notes);
				returnMP.setMetaPhaseColor(meta_phase_color);
				returnMP.setSim_id(sim_id);
				returnMP.saveMe(this.schema);
				Simulation.updateSimsLastEditDate(sim_id, schema);
			} else if (command.equalsIgnoreCase("Edit")) { //$NON-NLS-1$
				returnMP = SimulationMetaPhase.getById(this.schema, new Long(
						mp_id));
			} else if (command.equalsIgnoreCase("Update")) { //  //$NON-NLS-1$
				returnMP = SimulationMetaPhase.getById(this.schema, new Long(
						mp_id));
				returnMP.setMetaPhaseName(meta_phase_name);
				returnMP.setMetaPhaseNotes(meta_phase_notes);
				returnMP.setMetaPhaseColor(meta_phase_color);
				returnMP.setSim_id(sim_id);
				returnMP.saveMe(this.schema);
				Simulation.updateSimsLastEditDate(sim_id, schema);
			} else if (command.equalsIgnoreCase("Clear")) { //  //$NON-NLS-1$
				// returning new simulation phase will clear fields.
			}
		}
		return returnMP;
	}

	/** Turns a string into an int. */
	public int string2Int(String input) {

		int returnX = 0;

		try {
			returnX = Integer.parseInt(input);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnX;
	}

	public String setOfUsers = ""; //$NON-NLS-1$
	public String invitationCode = ""; //$NON-NLS-1$

	/**
	 * 
	 * @return
	 */
	public String getDefaultInviteMessage() {

		String defaultInviteEmailMsg = "Dear Student,\r\n"; //$NON-NLS-1$
		defaultInviteEmailMsg += "Please go to the web site "; //$NON-NLS-1$
		defaultInviteEmailMsg += "[website]";
		defaultInviteEmailMsg += " and register yourself.\r\n\r\n"; //$NON-NLS-1$
		defaultInviteEmailMsg += "Thank you,\r\n"; //$NON-NLS-1$
		defaultInviteEmailMsg += this.userDisplayName;

		return defaultInviteEmailMsg;

	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public String handleSetUpBetaTests(HttpServletRequest request) {

		String returnString = "";

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page == null)
				|| (!(sending_page.equalsIgnoreCase("launch_beta")))) {
			return "";
		}

		String sim_id_s = request.getParameter("sim_id"); //$NON-NLS-1$
		if (sim_id_s == null) {
			return "Must select a simulation.<br />";
		}

		Simulation sim = Simulation.getById(schema, new Long(sim_id_s));

		String send_emails = request.getParameter("send_emails"); //$NON-NLS-1$

		String email_users = "false";
		if ((send_emails != null) && (send_emails.equalsIgnoreCase("on"))) {
			email_users = "true";
		}

		String users_emails = request.getParameter("users_emails"); //$NON-NLS-1$
		String email_text = request.getParameter("email_text"); //$NON-NLS-1$

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mma");
		String timeStart = sdf.format(new Date());

		for (ListIterator<String> li = getSetOfEmails(users_emails)
				.listIterator(); li.hasNext();) {
			String this_email = li.next();

			BaseUser bu = BaseUser.getByUsername(this_email);

			if (bu != null) {
				returnString += "found user:" + this_email + "<br />";

				String rsn = "BetaTest " + timeStart + " " + bu.getInitials();

				// Create Running Simulation
				RunningSimulation rs = sim.addNewRunningSimulation(rsn, schema,
						this.user_id, this.userDisplayName, TimeZone
								.getDefault().getDisplayName());
				// Assign this user to all roles in the simulation.
				for (ListIterator<Actor> lia = SimActorAssignment
						.getActorsForSim(schema, sim.getId()).listIterator(); lia
						.hasNext();) {
					Actor act = lia.next();

					@SuppressWarnings("unused")
					UserAssignment ua = UserAssignment.getUniqueUserAssignment(
							this.schema, sim.getId(), rs.getId(), act.getId(),
							bu.getId());

					returnString += "          added user as "
							+ act.getActorName();

				}

				RunningSimulation.enableAndPrep(this.schema, sim.getId(),
						rs.getId());
				// TODO send the beta testers an email.

			} else {

				returnString += "<font color=\"red\">Warning: did not find user:"
						+ this_email + "</font><br />";

			}
		}

		return returnString;
	}

	/**
	 * Returns the URL of the autoregistration site for this installation for
	 * the particular schema which is being used.
	 * 
	 * @return
	 */
	public String getAutoRegistrationBaseLink() {

		String baseURL = USIP_OSP_Properties.getValue("simulation_url") //$NON-NLS-1$
				+ "/simulation_user_admin/auto_registration_page.jsp";

		baseURL += "?schema=" + schema;

		return baseURL;
	}

	/**
	 * 
	 * @return
	 */
	public static String getConfirmEmailBaseLink(String schema) {

		String baseURL = USIP_OSP_Properties.getValue("base_sim_url") //$NON-NLS-1$
				+ "/confirm.jsp";

		baseURL += "?schema=" + schema;

		return baseURL;
	}

	/**
	 * 
	 * @param request
	 */
	public String handleBulkInvite(HttpServletRequest request) {

		String returnString = "";

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page == null)
				|| (!(sending_page.equalsIgnoreCase("bulk_invite")))) {
			return returnString;
		} else {

			this.setOfUsers = request.getParameter("setOfUsers"); //$NON-NLS-1$
			String thisInviteEmailMsg = request
					.getParameter("defaultInviteEmailMsg"); //$NON-NLS-1$
			this.invitationCode = request.getParameter("invitationCode"); //$NON-NLS-1$

			String baseURL = this.getAutoRegistrationBaseLink();

			baseURL += "&initial_entry=true";
			// /

			for (ListIterator<String> li = getSetOfEmails(this.setOfUsers)
					.listIterator(); li.hasNext();) {
				String this_email = li.next();

				if (BaseUser.checkIfUserExists(this_email)) {
					Logger.getRootLogger().debug("exists:" + this_email);
					// make sure exists in this schema
					returnString += "<font color=\"red\">User already registered: "
							+ this_email + "</font><br />";

				} else {

					Logger.getRootLogger()
							.debug("does not exist:" + this_email);

					// Add entry into system to all them to register.
					UserRegistrationInvite uri = new UserRegistrationInvite(
							this.user_name, this_email, this.invitationCode,
							this.schema, this.user_id);

					uri.saveMe();

					// Replace [website] with actual URL
					String fullURL = baseURL + "&uri=" + uri.getId();
					String tayloredInviteEmailMsg = thisInviteEmailMsg.replace(
							"[website]", fullURL);

					// Send them email directing them to the page to register

					String subject = "Invitation to register on a USIP OSP System";
					sendBulkInvitationEmail(this_email, subject,
							tayloredInviteEmailMsg);

					returnString += this_email
							+ " sent invitation email. <br />";

				}
			}

			this.setOfUsers = "";
		}

		return returnString;

	}

	/**
	 * 
	 * @param schema_id
	 * @param the_email
	 * @param subject
	 * @param message
	 */
	public void sendBulkInvitationEmail(String the_email, String subject,
			String message) {

		Vector cced = null;
		Vector bcced = new Vector();
		bcced.add(this.user_name);

		SchemaInformationObject sio = SchemaInformationObject
				.lookUpSIOByName(this.schema);

		Emailer.postMail(sio, the_email, subject, message, message,
				sio.getEmailNoreplyAddress(), this.user_name, cced, bcced);
	}

	/**
	 * 
	 * @param inputSet
	 * @return
	 */
	public List getSetOfEmails(String inputSet) {
		StringTokenizer str = new StringTokenizer(inputSet, ", \r\n");

		ArrayList returnList = new ArrayList();
		while (str.hasMoreTokens()) {
			String name = str.nextToken();
			name = name.trim();
			if (name.length() > 0) {
				returnList.add(name);
			}
		}

		return returnList;
	}

	/**
	 * Handled the removal of a baseobject.
	 * 
	 * @param request
	 * @return
	 */
	public boolean handleDeleteObject(HttpServletRequest request) {

		String objectType = request.getParameter("object_type");
		String objid = request.getParameter("objid");
		String cancel_action = request.getParameter("cancel_action");
		String phase_sim_id = request.getParameter("phase_sim_id");

		if (cancel_action != null) {
			return true;
		}

		String deletion_confirm = request.getParameter("deletion_confirm");
		if ((deletion_confirm != null)
				&& (deletion_confirm.equalsIgnoreCase("Submit"))) {

			Long o_id = new Long(objid);

			if (objectType.equalsIgnoreCase("simulation")) {
				if (Simulation.deleteSimulation(schema, new Long(o_id))) {
					this.sim_id = null;
					User user = this.giveMeUser();
					user.setLastSimEdited(null);
				}

			} else if (objectType.equalsIgnoreCase("phase")) {
				MultiSchemaHibernateUtil.beginTransaction(this.schema);
				SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil
						.getSession(this.schema).get(SimulationPhase.class,
								o_id);
				Long phase_removed_id = sp.getId();
				MultiSchemaHibernateUtil.getSession(this.schema).delete(sp);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

				SimPhaseAssignment.removeMe(this.schema,
						new Long(phase_sim_id), phase_removed_id);

			} else if (objectType.equalsIgnoreCase("actor")) {

				// Removing actor assignments first
				SimActorAssignment.removeActorAssignments(schema, o_id);

				MultiSchemaHibernateUtil.beginTransaction(this.schema);
				Actor act = (Actor) MultiSchemaHibernateUtil.getSession(
						this.schema).get(Actor.class, o_id);

				if (this.actor_being_worked_on_id != null) {
					if (this.actor_being_worked_on_id.intValue() == act.getId()
							.intValue()) {
						this.actor_being_worked_on_id = null;
					}
				}

				MultiSchemaHibernateUtil.getSession(this.schema).delete(act);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

			} else if (objectType.equalsIgnoreCase("inject")) {

				MultiSchemaHibernateUtil.beginTransaction(this.schema);
				Inject inject = (Inject) MultiSchemaHibernateUtil.getSession(
						this.schema).get(Inject.class, o_id);

				MultiSchemaHibernateUtil.getSession(this.schema).delete(inject);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

			} else if (objectType.equalsIgnoreCase("sim_section")) {
				MultiSchemaHibernateUtil.beginTransaction(this.schema);
				SimulationSectionAssignment ss = (SimulationSectionAssignment) MultiSchemaHibernateUtil
						.getSession(this.schema).get(
								SimulationSectionAssignment.class, o_id);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

				SimulationSectionAssignment.removeAndReorder(request,
						this.schema, ss);

			} else if (objectType.equalsIgnoreCase("user_assignment")) {
				MultiSchemaHibernateUtil.beginTransaction(this.schema);
				UserAssignment ua = (UserAssignment) MultiSchemaHibernateUtil
						.getSession(this.schema)
						.get(UserAssignment.class, o_id);
				MultiSchemaHibernateUtil.getSession(this.schema).delete(ua);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

			} else if (objectType.equalsIgnoreCase("section")) {
				MultiSchemaHibernateUtil.beginTransaction(this.schema);
				BaseSimSection bss = (BaseSimSection) MultiSchemaHibernateUtil
						.getSession(this.schema)
						.get(BaseSimSection.class, o_id);
				MultiSchemaHibernateUtil.getSession(this.schema).delete(bss);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);
			} else if (objectType.equalsIgnoreCase("shareddocument")) {
				MultiSchemaHibernateUtil.beginTransaction(this.schema);
				SharedDocument sd = (SharedDocument) MultiSchemaHibernateUtil
						.getSession(this.schema)
						.get(SharedDocument.class, o_id);
				MultiSchemaHibernateUtil.getSession(this.schema).delete(sd);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

			} else if (objectType.equalsIgnoreCase("bss")) {
				MultiSchemaHibernateUtil.beginTransaction(this.schema);
				BaseSimSection bss = (BaseSimSection) MultiSchemaHibernateUtil
						.getSession(this.schema)
						.get(BaseSimSection.class, o_id);
				MultiSchemaHibernateUtil.getSession(this.schema).delete(bss);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);
			} else if (objectType.equalsIgnoreCase("injectgroup")) {
				MultiSchemaHibernateUtil.beginTransaction(this.schema);
				InjectGroup ig = (InjectGroup) MultiSchemaHibernateUtil
						.getSession(this.schema).get(InjectGroup.class, o_id);
				MultiSchemaHibernateUtil.getSession(this.schema).delete(ig);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);
			} else {
				Logger.getRootLogger().warn(
						"Warning: Tried to delete object, but no delete function implemented for: "
								+ objectType);
			}

			if (sim_id != null) {
				Simulation.updateSimsLastEditDate(sim_id, schema);
			}

			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param request
	 */
	public void handleEnableSim(HttpServletRequest request) {

		String command = request.getParameter("command");

		if (command != null) {
			if ((command.equalsIgnoreCase("Enable Simulation"))) {

				RunningSimulation.enableAndPrep(this.schema, this.sim_id,
						this.runningSimId);

				this.forward_on = true;

			} // End of if coming from this page and have enabled the sim
			else if ((command.equalsIgnoreCase("Disable Simulation"))) {

				RunningSimulation rs = this.giveMeRunningSim();
				rs.setReady_to_begin(false);
				rs.saveMe(schema);

			}

			// ////////////////////////////
		}

	}

	/**
	 * Sends an email based on the parameters passed in.
	 * 
	 * @param request
	 * 
	 * TODO: make this static and move it into the Email class
	 */
	public Email handleNotifyPlayers(HttpServletRequest request,
			SchemaInformationObject sio) {

		String command = request.getParameter("command");
		String sending_page = request.getParameter("sending_page");

		String simName = "";
		if (this.sim_id != null) {
			Simulation sim = Simulation.getById(schema, sim_id);
			simName = sim.getDisplayName();
		}

		Email returnEmail = Email.getRawBlankSimInvite(simName);

		// ////////////////////////////
		// If user has selected a previously sent email to send again ...
		String queue_up = request.getParameter("queue_up");
		if ((queue_up != null) && (queue_up.equalsIgnoreCase("true"))) {
			String e_id = request.getParameter("e_id");

			returnEmail = Email.getById(schema, new Long(e_id));
		}

		// ////////////////////////////////////////
		// If user is sending email
		if ((command != null) && (sending_page != null)
				&& (sending_page.equalsIgnoreCase("notify_players"))) {

			String email_text = request.getParameter("email_text");
			String email_from = request.getParameter("email_from");
			String email_subject = request.getParameter("email_subject");

			boolean setReplyTo = true; // If sending from no-reply acct, set
			// reply to instructor's email.
			String send_email_from = sio.getEmailNoreplyAddress();
			if ((email_from != null)
					&& (email_from.equalsIgnoreCase("username"))) {
				send_email_from = this.user_email;
				setReplyTo = false;
			}

			returnEmail = new Email(this.user_id, send_email_from,
					email_subject, email_text, this.sim_id, this.runningSimId);
			returnEmail.setInvitePrototype(true);
			returnEmail.setSimInvitationEmail(true);
			returnEmail.setSendDate(new java.util.Date());
			returnEmail.saveMe(schema);

			for (Enumeration e = request.getParameterNames(); e
					.hasMoreElements();) {
				String pname = (String) e.nextElement();

				String vname = (String) request.getParameter(pname);
				if (pname.startsWith("invite_")) {
					pname = pname.replaceFirst("invite_", "");
					UserAssignment ua = UserAssignment.getById(schema,
							new Long(pname));

					String customizedMessage = email_text;

					if (ua.getUser_id() == null) {
						// Get the user name that the faciliator entered.
						String this_player_name = request.getParameter(ua
								.getId() + "_user_display_name");
						customizedMessage = customizedMessage.replace(
								"[Student Name]", this_player_name);
						// Set the temporary user name of this UserAssignment
						ua.setTempStudentName(this_player_name);
					} else {
						User user = User.getById(schema, ua.getUser_id());
						customizedMessage = customizedMessage.replace(
								"[Student Name]", user.getBu_full_name());
					}

					if (sio.isEmailEnabled()) {
						Email email = new Email(this.user_id, send_email_from,
								email_subject, customizedMessage, this.sim_id,
								this.runningSimId);
						email.setSimInvitationEmail(true);
						email.setToActorEmail(false);

						if (setReplyTo) {
							email.setReplyToName(this.user_email);
						}
						email.saveMe(schema);
						EmailRecipients er = new EmailRecipients(schema,
								email.getId(), this.getRunningSimId(), sim_id,
								ua.getUsername(), EmailRecipients.RECIPIENT_TO);

						String confirmURL = AuthorFacilitatorSessionObject
								.getConfirmEmailBaseLink(sio.getSchema_name());
						confirmURL += "&ua_id=" + ua.getId();
						confirmURL += "&er_id=" + er.getId();

						customizedMessage = customizedMessage.replace(
								"[confirm_receipt]", confirmURL);
						email.setMsgtext(customizedMessage);
						email.setHtmlMsgText(customizedMessage);
						email.saveMe(schema);

						email.sendMe(sio);

						ua.advanceStatus("invited");
						ua.saveMe(schema);
					}
				}

			}
		}

		return returnEmail;
	}

	/**
	 * 
	 * @param request
	 */
	public void handleEnterInstructorRatings(HttpServletRequest request) {

		String num_stars = request.getParameter("num_stars");
		String user_comments = request.getParameter("user_comments");
		String user_stated_name = request.getParameter("user_stated_name");

		SimulationRatings sr = SimulationRatings
				.getInstructorRatingsBySimAndUser(this.schema, this.sim_id,
						this.user_id);

		sr.setNumberOfStars(new Long(num_stars).intValue());
		sr.setSim_id(this.sim_id);
		sr.setUser_id(this.user_id);
		sr.setUsers_stated_name(user_stated_name);
		sr.setUser_comments(user_comments);
		sr.setComment_type(SimulationRatings.INSTRUCTOR_COMMENT);

		sr.saveMe(this.schema);

	}

	/**
	 * Handles commands submitted on the install simulation sections page.
	 * 
	 * @param request
	 */
	public void handleInstallSimulationSections(HttpServletRequest request) {

		String command = request.getParameter("command");
		String fullfileloc = request.getParameter("fullfileloc");
		String loaded_id = request.getParameter("loaded_id");

		if (command != null) {
			if (command.equalsIgnoreCase("Load")) {
				Logger.getRootLogger().debug(
						"Will be loading file from: " + fullfileloc);
				BaseSimSection
						.readInXMLFile(this.schema, new File(fullfileloc));

			} else if (command.equalsIgnoreCase("Reload")) {
				Logger.getRootLogger().debug(
						"Will be reloading file from: " + fullfileloc);
				BaseSimSection.reloadXMLFile(this.schema,
						new File(fullfileloc), new Long(loaded_id));
				// save
			} else if (command.equalsIgnoreCase("Unload")) {
				Logger.getRootLogger().debug(
						"Will be unloading bss id: " + loaded_id);
				BaseSimSection.removeBSS(this.schema, loaded_id);
			}
		}
	}

	/**
	 * Handles commands submitted on the install simulation sections page.
	 * 
	 * @param request
	 */
	public void handleInstallModels(HttpServletRequest request) {

		String command = request.getParameter("command");
		String fullfileloc = request.getParameter("fullfileloc");
		String loaded_id = request.getParameter("loaded_id");

		if (command != null) {
			if (command.equalsIgnoreCase("Load")) {
				Logger.getRootLogger().debug(
						"Will be loading file from: " + fullfileloc);
				BaseSimSection
						.readInXMLFile(this.schema, new File(fullfileloc));

			} else if (command.equalsIgnoreCase("Reload")) {
				Logger.getRootLogger().debug(
						"Will be reloading file from: " + fullfileloc);
				BaseSimSection.reloadXMLFile(this.schema,
						new File(fullfileloc), new Long(loaded_id));
				// save
			} else if (command.equalsIgnoreCase("Unload")) {
				Logger.getRootLogger().debug(
						"Will be unloading bss id: " + loaded_id);
				BaseSimSection.removeBSS(this.schema, loaded_id);
			}
		}
	}

	/**
	 * Takes the request parameters passed them and loads them as session
	 * variables.
	 * 
	 * @param request
	 */
	public static void getAndLoad(HttpServletRequest request) {

		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			String pname = (String) e.nextElement();

			String vname = (String) request.getParameter(pname);
			request.getSession().setAttribute(pname, vname);
		}
	}

	/**
	 * Attempts to pull a variable out of the session. If one is not there, then
	 * it will return and empty string "".
	 * 
	 * @param request
	 * @param keyname
	 * @return
	 */
	public String getClean(HttpServletRequest request, String keyname) {

		String returnString = "";
		if (session != null) {

			returnString = (String) session.getAttribute(keyname);

			if (returnString == null) {
				returnString = "";
			}
		}

		return returnString;
	}

	/**
	 * This takes the sections applied to a 'category' actor and applies them to
	 * all of the other actors in its group.
	 * 
	 * @param request
	 * @return
	 */
	public String applyActorCategory(HttpServletRequest request) {

		String returnString = "";

		// Leave if just entered page from somewhere else.
		String sending_page = (String) request.getParameter("sending_page");
		if ((sending_page == null)
				|| (!(sending_page.equalsIgnoreCase("create_actor_category2")))) {
			return returnString;
		}

		String ac_id = (String) request.getParameter("ac_id");

		ActorCategory actorCategory = new ActorCategory();

		String apply_ac = (String) request.getParameter("apply_ac");
		if ((apply_ac != null)) {
			actorCategory = ActorCategory.getById(schema, new Long(ac_id));
			actorCategory.applySectionsAcrossCategory(schema);
			returnString += "applied ActorCategory "
					+ actorCategory.getCategoryName();
		}

		return returnString;
	}

	/**
	 * Handles CRUD operations on Generic Variables.
	 * 
	 * @param request
	 * @return
	 */
	public ActorCategory handleCreateActorCategory(HttpServletRequest request) {

		ActorCategory actorCategory = new ActorCategory();

		// If the player cleared the form, return the blank document.
		String clear_button = (String) request.getParameter("clear_button");
		if (clear_button != null) {
			return actorCategory;
		}

		// If we got passed in an actor category id, use it to retrieve the ac
		// we are
		// working on.
		String ac_id = (String) request.getParameter("ac_id");

		String queueup = (String) request.getParameter("queueup");
		if ((queueup != null) && (queueup.equalsIgnoreCase("true"))
				&& (ac_id != null) && (ac_id.trim().length() > 0)) {
			actorCategory = ActorCategory.getById(schema, new Long(ac_id));
			return actorCategory;
		}

		// If user just entered this page from a different form, just return the
		// blank object
		String sending_page = (String) request.getParameter("sending_page");
		if ((sending_page == null)
				|| (!(sending_page.equalsIgnoreCase("create_actor_category")))) {
			return actorCategory;
		}

		// If we got down to here, we must be doing some real work on a category
		String ac_name = (String) request.getParameter("ac_name");
		String exemplar_id = (String) request.getParameter("exemplar_id");

		// Do create if called.
		String create_ac = (String) request.getParameter("create_ac");
		if ((create_ac != null)) {
			Logger.getRootLogger().debug("creating ac: " + ac_name);
			actorCategory = new ActorCategory(ac_name, sim_id, new Long(
					exemplar_id), schema);
			setCategoryMembers(actorCategory, request);
		}

		// Do update if called.
		String update_ac = (String) request.getParameter("update_ac");
		if ((update_ac != null)) {
			Logger.getRootLogger().debug("updating ac: " + ac_name);
			actorCategory = ActorCategory.getById(schema, new Long(ac_id));
			actorCategory.setCategoryName(ac_name);
			actorCategory.setExemplar_id(new Long(exemplar_id));
			actorCategory.setSim_id(sim_id);
			actorCategory.saveMe(schema);
			setCategoryMembers(actorCategory, request);
		}

		return actorCategory;

	}

	public void setCategoryMembers(ActorCategory actorCategory,
			HttpServletRequest request) {

		actorCategory.removeMyActorIds(schema);

		for (Enumeration<String> e = request.getParameterNames(); e
				.hasMoreElements();) {
			String pname = (String) e.nextElement();
			String vname = (String) request.getParameter(pname);

			if (pname.startsWith("actor_")) {
				pname = pname.replaceAll("actor_", "");

				if ((vname != null) && (vname.equalsIgnoreCase("on"))) {
					ActorCategoryAssignments aca = new ActorCategoryAssignments();
					aca.setAc_id(actorCategory.getId());
					aca.setActor_id(new Long(pname));
					aca.setSim_id(sim_id);
					aca.saveMe(schema);
				}
			}
		}
	}

	public SetOfLinks handleCreateSetOfLinks(HttpServletRequest request) {

		SetOfLinks returnSetOfLinks = new SetOfLinks();

		// If the player cleared the form, return the blank document.
		String clear_button = (String) request.getParameter("clear_button");
		if (clear_button != null) {
			return returnSetOfLinks;
		}

		// If we got passed in a doc id, use it to retrieve the doc we are
		// working on.
		String sol_id = (String) request.getParameter("ol_id");

		String queueup = (String) request.getParameter("queueup");
		if ((queueup != null) && (queueup.equalsIgnoreCase("true"))
				&& (sol_id != null) && (sol_id.trim().length() > 0)) {
			returnSetOfLinks = SetOfLinks.getById(schema, new Long(sol_id));
			return returnSetOfLinks;
		}

		// If player just entered this page from a different form, just return
		// the blank object
		String sending_page = (String) request.getParameter("sending_page");
		if ((sending_page == null)
				|| (!(sending_page
						.equalsIgnoreCase("make_create_setoflinks_page")))) {
			return returnSetOfLinks;
		}

		// If we got down to here, we must be doing some real work on a
		// document.
		String setoflinks_name = (String) request
				.getParameter("setoflinks_name");
		String setoflinks_notes = (String) request
				.getParameter("setoflinks_notes");
		String start_value = (String) request.getParameter("start_value");

		// Do create if called.
		String create_setoflinks = (String) request
				.getParameter("create_setoflinks");
		if ((create_setoflinks != null)) {
			Logger.getRootLogger().debug(
					"creating setoflinks of uniq name: " + setoflinks_name);
			returnSetOfLinks = new SetOfLinks(setoflinks_name, sim_id);
			returnSetOfLinks.setNotes(setoflinks_notes);
			returnSetOfLinks.saveMe(schema);
		}

		// Do update if called.
		String update_onelink = (String) request.getParameter("update_onelink");
		if ((update_onelink != null)) {
			Logger.getRootLogger().debug(
					"updating onelink of uniq title: " + setoflinks_name);
			returnSetOfLinks = SetOfLinks.getById(schema, new Long(sol_id));
			returnSetOfLinks.setName(setoflinks_name);
			returnSetOfLinks.setNotes(setoflinks_notes);
			returnSetOfLinks.setSim_id(sim_id);
			returnSetOfLinks.saveMe(schema);

		}

		return returnSetOfLinks;

	}

	/**
	 * Handles CRUD operations on OneLink Object
	 * 
	 * @param request
	 * @return
	 */
	public OneLink handleCreateOneLink(HttpServletRequest request) {

		OneLink oneLink = new OneLink();

		// If the player cleared the form, return the blank document.
		String clear_button = (String) request.getParameter("clear_button");
		if (clear_button != null) {
			return oneLink;
		}

		// If we got passed in a doc id, use it to retrieve the doc we are
		// working on.
		String ol_id = (String) request.getParameter("ol_id");

		String queueup = (String) request.getParameter("queueup");
		if ((queueup != null) && (queueup.equalsIgnoreCase("true"))
				&& (ol_id != null) && (ol_id.trim().length() > 0)) {
			oneLink = OneLink.getById(schema, new Long(ol_id));
			return oneLink;
		}

		// If player just entered this page from a different form, just return
		// the blank object
		String sending_page = (String) request.getParameter("sending_page");
		if ((sending_page == null)
				|| (!(sending_page.equalsIgnoreCase("make_create_onelink_page")))) {
			return oneLink;
		}

		// If we got down to here, we must be doing some real work on a
		// document.
		String onelink_name = (String) request.getParameter("onelink_name");
		String onelink_notes = (String) request.getParameter("onelink_notes");
		String start_value = (String) request.getParameter("start_value");

		// Do create if called.
		String create_onelink = (String) request.getParameter("create_onelink");
		if ((create_onelink != null)) {
			Logger.getRootLogger().debug(
					"creating onelink of uniq name: " + onelink_name);
			oneLink = new OneLink(onelink_name, sim_id);
			oneLink.setNotes(onelink_notes);
			oneLink.setStartingValue(start_value);
			oneLink.saveMe(schema);
		}

		// Do update if called.
		String update_onelink = (String) request.getParameter("update_onelink");
		if ((update_onelink != null)) {
			Logger.getRootLogger().debug(
					"updating onelink of uniq title: " + onelink_name);
			oneLink = OneLink.getById(schema, new Long(ol_id));
			oneLink.setName(onelink_name);
			oneLink.setNotes(onelink_notes);
			oneLink.setStartingValue(start_value);
			oneLink.setSim_id(sim_id);
			oneLink.saveMe(schema);

		}

		return oneLink;

	}

	/**
	 * Handles CRUD operations on Generic Variables.
	 * 
	 * @param request
	 * @return
	 */
	public GenericVariable handleCreateGenericVariable(
			HttpServletRequest request) {

		GenericVariable genericVariable = new GenericVariable();

		// If the player cleared the form, return the blank document.
		String clear_button = (String) request.getParameter("clear_button");
		if (clear_button != null) {
			return genericVariable;
		}

		// If we got passed in a doc id, use it to retrieve the doc we are
		// working on.
		String gv_id = (String) request.getParameter("gv_id");

		String queueup = (String) request.getParameter("queueup");
		if ((queueup != null) && (queueup.equalsIgnoreCase("true"))
				&& (gv_id != null) && (gv_id.trim().length() > 0)) {
			genericVariable = GenericVariable.getById(schema, new Long(gv_id));
			return genericVariable;
		}

		// If player just entered this page from a different form, just return
		// the blank object
		String sending_page = (String) request.getParameter("sending_page");
		if ((sending_page == null)
				|| (!(sending_page
						.equalsIgnoreCase("make_create_parameter_page")))) {
			return genericVariable;
		}

		// If we got down to here, we must be doing some real work on a
		// document.
		String uniq_param_name = (String) request
				.getParameter("uniq_param_name");
		String param_notes = (String) request.getParameter("param_notes");
		String start_value = (String) request.getParameter("start_value");

		// TODO - Enable the features below.
		// String has_max_value = (String)
		// request.getParameter("has_max_value");
		// String has_min_value = (String)
		// request.getParameter("has_min_value");
		// String prop_type = (String) request.getParameter("prop_type");

		// Do create if called.
		String create_param = (String) request.getParameter("create_param");
		if ((create_param != null)) {
			Logger.getRootLogger().debug(
					"creating param of uniq name: " + uniq_param_name);
			genericVariable = new GenericVariable(uniq_param_name, sim_id);
			genericVariable.setNotes(param_notes);
			genericVariable.setStartingValue(start_value);
			genericVariable.saveMe(schema);
		}

		// Do update if called.
		String update_param = (String) request.getParameter("update_param");
		if ((update_param != null)) {
			Logger.getRootLogger().debug(
					"updating param of uniq title: " + uniq_param_name);
			genericVariable = GenericVariable.getById(schema, new Long(gv_id));
			genericVariable.setName(uniq_param_name);
			genericVariable.setNotes(param_notes);
			genericVariable.setStartingValue(start_value);
			genericVariable.setSim_id(sim_id);
			genericVariable.saveMe(schema);

		}

		return genericVariable;

	}

	public TimeLine timelineOnScratchPad = new TimeLine();

	/**
	 * Does the CRUD operations on a timeline.
	 * 
	 * @param request
	 * @return
	 */
	public void handleCreateTimeLine(HttpServletRequest request) {

		timelineOnScratchPad = new TimeLine();

		// If the player cleared the form, return the blank document.
		String clear_button = (String) request.getParameter("clear_button");
		if (clear_button != null) {
			return;
		}

		// If we got passed in a doc id, use it to retrieve the doc we are
		// working on.
		String t_id = (String) request.getParameter("t_id");

		String queueup = (String) request.getParameter("queueup");
		if ((queueup != null) && (queueup.equalsIgnoreCase("true"))
				&& (t_id != null) && (t_id.trim().length() > 0)) {
			timelineOnScratchPad = TimeLine.getById(schema, new Long(t_id));
			return;
		}

		// If player just entered this page from a different form, just return
		// the blank object
		String sending_page = (String) request.getParameter("sending_page");
		if ((sending_page == null)
				|| (!(sending_page
						.equalsIgnoreCase("make_create_timeline_page")))) {
			return;
		}

		// If we got down to here, we must be doing some real work on a
		// document.
		String timeline_name = (String) request.getParameter("timeline_name");
		String timeline_start_date = (String) request
				.getParameter("timeline_start_date");
		String timeline_start_hour = (String) request
				.getParameter("timeline_start_hour");

		String timeline_start = timeline_start_date + " " + timeline_start_hour;

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy H");

		Date timeLineStartDate = new Date();

		try {
			timeLineStartDate = sdf.parse(timeline_start);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Do create if called.
		String create_timeline = (String) request
				.getParameter("create_timeline");

		boolean hasName = false;

		if ((timeline_name != null) && (timeline_name.trim().length() > 0)) {
			hasName = true;
		}

		if ((create_timeline != null) && hasName) {
			Logger.getRootLogger().debug(
					"creating param of uniq name: " + timeline_name);
			timelineOnScratchPad.setName(timeline_name);
			timelineOnScratchPad.setTimeline_start_date(timeLineStartDate);
			timelineOnScratchPad.setSimId(sim_id);
			timelineOnScratchPad.saveMe(schema);
		}

		// Do update if called.
		String update_timeline = (String) request
				.getParameter("update_timeline");
		if ((update_timeline != null) && hasName) {
			Logger.getRootLogger().debug(
					"updating param of uniq title: " + timeline_name);
			timelineOnScratchPad = TimeLine.getById(schema, new Long(t_id));
			timelineOnScratchPad.setName(timeline_name);
			timelineOnScratchPad.setTimeline_start_date(timeLineStartDate);
			timelineOnScratchPad.setSimId(sim_id);
			timelineOnScratchPad.saveMe(schema);

		}

	}

	/**
	 * Takes input from the install page and creates the database.
	 * 
	 * We check to see if this admin user already exists. If they do, and the
	 * installer has entered the admin's existing password, then no problem.
	 * Otherwise we indicate to them that the admin user exists and has a
	 * different password.
	 * 
	 * @param request
	 * @return
	 */
	public String handleCreateDB(HttpServletRequest request) {

		getAndLoad(request);

		String sending_page = (String) request.getParameter("sending_page");
		String cleandb = (String) request.getParameter("cleandb");

		if (sending_page == null) {
			return "";
		}

		String db_schema = (String) request.getParameter("db_schema");

		schema = db_schema;

		// ////////////////////////////////////////////////

		DatabaseCreator.loadUpParameters(request);

		String admin_pass = (String) request.getParameter("admin_pass");
		String admin_email = (String) request.getParameter("admin_email");

		if ((sending_page != null) && (cleandb != null)
				&& (sending_page.equalsIgnoreCase("clean_db"))) {

			if ((admin_pass == null) || (admin_pass.length() == 0)) {
				return ("Must enter admin password.");
			} else if ((admin_email == null) || ((admin_email.length() == 0))) {
				return ("Must enter admin email.");
			}

			BaseUser existing_admin = BaseUser.getByUsername(admin_email);

			// If admin already exist, need to make sure that the password
			// passed in is the same.
			if (existing_admin != null) {
				BaseUser bu = BaseUser.validateUser(admin_email, admin_pass);
				if (bu == null) {
					return ("Admin password does not match the existing admin's password.");
				}
			}
		}

		DatabaseCreator.loadUpParameters(request);

		// Fill SIO
		SchemaInformationObject sio = DatabaseCreator.fillSIO();

		if (!(MultiSchemaHibernateUtil.testConn())) {
			return ("<BR> Failed to create database connection to the database "
					+ db_schema + ".");
		}

		// Store SIO if schema object of this name already exist, return
		// warning.

		try {
			sio.saveMe();
		} catch (Exception e) {

			e.printStackTrace();

			return ("Warning. Unable to create the database entry for this schema. <br />"
					+ "This may indicate that it already has been created.");
		}

		MultiSchemaHibernateUtil.recreateDatabase(sio);

		// //////////////////////////////
		String loadss = (String) request.getParameter("loadss");

		if ((loadss != null) && (loadss.equalsIgnoreCase("true"))) {
			BaseSimSection.readBaseSimSectionsFromXMLFiles(db_schema,
					FileIO.getBase_section_web_dir());
		}
		// /////////

		BaseSimSection.readBaseSimSectionsFromXMLFiles(db_schema,
				FileIO.getPlugin_dir());
		MultiSchemaHibernateUtil.createPluginTables(sio);

		String admin_first = (String) request.getParameter("admin_first");
		String admin_middle = (String) request.getParameter("admin_middle");
		String admin_last = (String) request.getParameter("admin_last");

		String admin_full = USIP_OSP_Util.constructName(admin_first,
				admin_middle, admin_last);

		// Must create the new user in this schema
		@SuppressWarnings("unused")
		User user = new User(db_schema, admin_email, admin_pass, admin_first,
				admin_last, admin_middle, admin_full, true, true, true);

		// ///////////////////////////////////////////
		// Test email functionality if SMTP information has been entered.
		String email_msg = "";

		if (sio.checkReqEmailInfoAndMaybeMarkDown()) {

			String message = "This email is coming from your newly installed OSP Installation.";

			Vector ccs = new Vector();
			Vector bccs = new Vector();

			if (sio != null) {
				bccs.add(sio.getEmail_archive_address());
				Emailer.postMail(sio, sio.getEmail_archive_address(),
						"USIP OSP Installation Message", message, message,
						sio.getEmailNoreplyAddress(),
						sio.getEmail_archive_address(), ccs, bccs);
				email_msg = "email_sent";
			} else {
				Logger.getRootLogger().warn("Problem sending test email.");
				email_msg = "sio_null";
			}

		} else {
			email_msg += "email_not_sent";
		}
		// ///////////////////////////////////////////

		this.forward_on = true;
		this.backPage = "install_confirmation.jsp?schema=" + schema
				+ "&emailstatus=" + email_msg;

		// Trying a reset here to get the plugin tables to be recognized at
		// first go.
		MultiSchemaHibernateUtil.resetSessionForSchema();

		return email_msg;

	}

	public static String handleCreateOrUpdateDB(HttpServletRequest request,
			Long adminUserId) {

		return DatabaseCreator.handleCreateOrUpdateDB(request, adminUserId);

	}

	/**
	 * Handles the creation of simulation sections.
	 * 
	 * @param request
	 */
	public BaseSimSection handleCreateSimulationSection(
			HttpServletRequest request) {

		BaseSimSection bss = new BaseSimSection();

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("create_section"))) {

			// ////////////////////////////////////////////////////////
			String command = (String) request.getParameter("command");

			String u = request.getParameter("url");
			String d = request.getParameter("directory");
			String f = request.getParameter("filename");
			String r = request.getParameter("rec_tab_heading");
			String desc = request.getParameter("description");
			String bss_id = request.getParameter("bss_id");
			String send_rsid_info = (String) request
					.getParameter("send_rsid_info");
			String send_actor_info = (String) request
					.getParameter("send_actor_info");
			String send_user_info = (String) request
					.getParameter("send_user_info");

			if (command != null) {
				if (command.equalsIgnoreCase("Create")) {
					bss = new BaseSimSection(schema, u, d, f, r, desc);
					bss.setSendFields(send_rsid_info, send_actor_info,
							send_user_info);
					bss.setAuthorGeneratedSimulationSection(true);
					bss.saveMe(schema);
				} else if (command.equalsIgnoreCase("Update")) { //
					bss = BaseSimSection.getById(schema, bss_id);
					bss.setUrl(u);
					bss.setDirectory(d);
					bss.setPage_file_name(f);
					bss.setRec_tab_heading(r);
					bss.setDescription(desc);
					bss.setSendFields(send_rsid_info, send_actor_info,
							send_user_info);
					bss.setAuthorGeneratedSimulationSection(true);
					bss.saveMe(schema);
				} else if (command.equalsIgnoreCase("Edit")) {
					bss = BaseSimSection.getById(schema, bss_id);
					return bss;
				} else if (command.equalsIgnoreCase("Clear")) { //
					return bss;
					// returning bss will clear field
				}
			}

		}

		return bss;
	}

	/**
	 * Handles the creation of an inject group.
	 * 
	 * @param request
	 */
	public InjectGroup handleCreateInjectGroup(HttpServletRequest request) {

		InjectGroup ig = new InjectGroup();

		// If the player cleared the form, return the blank document.
		String clear_button = (String) request.getParameter("clear_button");
		if (clear_button != null) {
			return ig;
		}

		// If we got passed in a doc id, use it to retrieve the doc we are
		// working on.
		String ig_id = (String) request.getParameter("ig_id");

		String queueup = (String) request.getParameter("queueup");
		if ((queueup != null) && (queueup.equalsIgnoreCase("true"))
				&& (ig_id != null) && (ig_id.trim().length() > 0)) {
			ig = InjectGroup.getById(schema, ig_id);
			return ig;
		}

		// If player just entered this page from a different form, just return
		// the blank object
		String sending_page = (String) request.getParameter("sending_page");
		if ((sending_page == null)
				|| (!(sending_page.equalsIgnoreCase("create_inject_group")))) {
			return ig;
		}

		String inject_group_name = (String) request
				.getParameter("inject_group_name");
		String inject_group_description = (String) request
				.getParameter("inject_group_description");

		// Do create if called.
		String command = (String) request.getParameter("command");
		if (command != null) {

			if (command.equalsIgnoreCase("Clear")) { //$NON-NLS-1$
				return ig;
			} else if (command.equalsIgnoreCase("Create")) { //$NON-NLS-1$
				ig.setName(inject_group_name);
				ig.setDescription(inject_group_description);
				ig.setSim_id(sim_id);

				ig.saveMe(schema);
			} else if (command.equalsIgnoreCase("Update")) {
				ig = InjectGroup.getById(schema, ig_id);
				ig.setName(inject_group_name);
				ig.setDescription(inject_group_description);
				ig.setSim_id(sim_id);

				ig.saveMe(schema);

			}

			Simulation.updateSimsLastEditDate(sim_id, schema);
		}

		return ig;

	}

	/**
	 * Handles the creation of Injects. We only get here if the author has
	 * edited an inject. (Hence we automatically update the simulation last edit
	 * date.)
	 * 
	 * @param request
	 */
	public Inject handleCreateInject(String schema, HttpServletRequest request) {

		Inject inject = new Inject();

		// If the player cleared the form, return the blank document.
		String clear_button = (String) request.getParameter("clear_button");
		if (clear_button != null) {
			return inject;
		}

		// If we got passed in a doc id, use it to retrieve the doc we are
		// working on.
		String inj_id = (String) request.getParameter("inj_id");
		String queueup = (String) request.getParameter("queueup");

		if ((queueup != null) && (queueup.equalsIgnoreCase("true"))
				&& (inj_id != null) && (inj_id.trim().length() > 0)) {
			inject = Inject.getById(schema, new Long(inj_id));
			return inject;
		}

		String inject_name = (String) request.getParameter("inject_name");
		String inject_text = (String) request.getParameter("inject_text");
		String inject_notes = (String) request.getParameter("inject_notes");
		String ig_id = (String) request.getParameter("ig_id");

		ArrayList<Long> targettedPeople = new ArrayList();

		for (Enumeration<String> e = request.getParameterNames(); e
				.hasMoreElements();) {
			String pname = (String) e.nextElement();
			String vname = (String) request.getParameter(pname);
			if (pname.startsWith("target_")) {
				pname = pname.replace("target_", "");

				if (vname.equalsIgnoreCase("on")) {
					Long thisTarget = USIP_OSP_Util.stringToLong(pname);
					if (thisTarget != null) {
						targettedPeople.add(thisTarget);
					}
				}
			}
		}

		// Do create if called.
		String command = (String) request.getParameter("command");
		if (command != null) {

			if (command.equalsIgnoreCase("Create")) { //$NON-NLS-1$
				inject.setInject_name(inject_name);
				inject.setInject_text(inject_text);
				inject.setInject_Notes(inject_notes);
				inject.setSim_id(sim_id);
				inject.setGroup_id(new Long(ig_id));
				inject.saveMe(schema);

				addInjectDefaultRecipients(schema, targettedPeople, inject,
						false);

			} else if (command.equalsIgnoreCase("Update Inject")) {
				inject = Inject.getById(schema, new Long(inj_id));
				inject.setInject_name(inject_name);
				inject.setInject_text(inject_text);
				inject.setInject_Notes(inject_notes);
				inject.setGroup_id(new Long(ig_id));
				inject.saveMe(schema);

				addInjectDefaultRecipients(schema, targettedPeople, inject,
						true);

			}

			Simulation.updateSimsLastEditDate(sim_id, schema);
		}

		return inject;

	}

	public static void addInjectDefaultRecipients(String schema,
			List peopleToAdd, Inject inject, boolean cleanOld) {

		if (cleanOld) {
			InjectActorAssignments.removeAllForInject(schema, inject.getId());
		}

		for (ListIterator<Long> li = peopleToAdd.listIterator(); li.hasNext();) {

			Long this_iaa = li.next();

			InjectActorAssignments iaa = new InjectActorAssignments(schema,
					this_iaa, inject.getId());
		}
	}

	/**
	 * Returns the AFSO stored in the session, or creates one.
	 */
	public static AuthorFacilitatorSessionObject getAFSO(HttpSession session) {

		AuthorFacilitatorSessionObject afso = (AuthorFacilitatorSessionObject) session
				.getAttribute("afso");

		if (afso == null) {
			Logger.getRootLogger().debug("afso is new");
			afso = new AuthorFacilitatorSessionObject();
			afso.session = session;
		}

		session.setAttribute("afso", afso);

		return afso;
	}

	/**
	 * Upon the creation of a new simulation several things happen: 1.) The
	 * values the player entered get stored in the system. 2.) Two phases, the
	 * starting phase and the completed phase, get added.
	 * 
	 * @param request
	 */
	public Simulation handleCreateSim(HttpServletRequest request) {

		Simulation simulation = new Simulation();

		String command = (String) request.getParameter("command");
		String simulation_name = (String) request
				.getParameter("simulation_name");
		String simulation_version = (String) request
				.getParameter("simulation_version");
		String creation_org = (String) request.getParameter("creation_org");

		String clear = (String) request.getParameter("clear");
		if ((clear != null) && (clear.equalsIgnoreCase("true"))) {
			simulation = new Simulation();
			return simulation;
		}

		if (command != null) {
			if (command.equalsIgnoreCase("Create")) {

				simulation.setSimulationName(simulation_name);
				simulation.setVersion(simulation_version);
				simulation.setSoftwareVersion(USIP_OSP_Properties.getRelease());
				simulation.setCreation_org(creation_org);

				simulation.createDefaultObjects(schema);

				simulation.saveMe(schema);
				SimEditors simEditors = new SimEditors(schema,
						simulation.getId(), this.user_id, this.userDisplayName,
						this.user_email);
				simEditors.saveMe(schema);

				this.phase_id = simulation.getFirstPhaseId(schema);

				this.sim_id = simulation.getId();

				saveLastSimEdited();

				this.forward_on = true;

			}
		}

		return simulation;
	}

	/**
	 * Allows for the renaming/re-versioning of a simulation.
	 * 
	 * @param request
	 * @return
	 */
	public Simulation handleRenameSim(HttpServletRequest request) {

		Simulation simulation = new Simulation();

		String sim_id = (String) request.getParameter("sim_id");

		if (sim_id != null) {
			simulation = Simulation.getById(schema, new Long(sim_id));
		}

		String sending_page = (String) request.getParameter("sending_page");
		String simulation_name = (String) request
				.getParameter("simulation_name");
		String simulation_version = (String) request
				.getParameter("simulation_version");
		String creation_org = (String) request.getParameter("creation_org");

		if (sending_page != null) {
			if (sending_page.equalsIgnoreCase("rename")) { //

				simulation.setSimulationName(simulation_name);
				simulation.setVersion(simulation_version);
				simulation.setCreation_org(creation_org);

				simulation.saveMe(schema);

				this.sim_id = simulation.getId();

				saveLastSimEdited();

				this.forward_on = true;
			}
		}

		return simulation;
	}

	/**
	 * This copies a simulation into a new version. Currently it is a 'shallow'
	 * copy. It does not make copies of any objects (such as actors) that also
	 * belong to the simulation.
	 * 
	 * @param request
	 * @return
	 */
	public Simulation handleCopySim(HttpServletRequest request) {

		Simulation new_simulation = new Simulation();
		Simulation old_simulation = new Simulation();

		String sim_id = (String) request.getParameter("sim_id");

		if (sim_id != null) {
			old_simulation = Simulation.getById(schema, new Long(sim_id));
		} else {
			return old_simulation;
		}

		String sending_page = (String) request.getParameter("sending_page");
		String simulation_name = (String) request
				.getParameter("simulation_name");
		String simulation_version = (String) request
				.getParameter("simulation_version");
		String creation_org = (String) request.getParameter("creation_org");

		if (sending_page != null) {
			if (sending_page.equalsIgnoreCase("copy")) { //

				new_simulation.copyIn(old_simulation);

				// Set id to null so when we save we get a new copy
				new_simulation.setId(null);

				new_simulation.setSimulationName(simulation_name);
				new_simulation.setVersion(simulation_version);
				new_simulation.setCreation_org(creation_org);

				new_simulation
						.setSimEditingRestrictions(Simulation.CAN_BE_EDITED_BY_SPECIFIC_USERS);
				new_simulation.saveMe(schema);

				@SuppressWarnings("unused")
				SimEditors se = new SimEditors(schema, new_simulation.getId(),
						this.user_id, userDisplayName, this.userDisplayName);

				this.sim_id = new_simulation.getId();

				saveLastSimEdited();

				this.forward_on = true;
			}
		}

		return new_simulation;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public Simulation handleEditBasicSimParameters(HttpServletRequest request) {

		Simulation simulation = new Simulation();

		String command = (String) request.getParameter("command");
		String simcopyright = (String) request.getParameter("simcopyright");
		String editing_users = (String) request.getParameter("editing_users");

		String simblurb = (String) request.getParameter("simblurb");

		String clear = (String) request.getParameter("clear");
		if ((clear != null) && (clear.equalsIgnoreCase("true"))) {
			simulation = new Simulation();
			sim_id = null;

			return simulation;
		}

		if (command != null) {
			if (command.equalsIgnoreCase("Update")) { //
				String sim_id = (String) request.getParameter("sim_id");
				simulation = Simulation.getById(schema, new Long(sim_id));
				// simulation.setCreator(simcreator);
				simulation.setCopyright_string(simcopyright);
				simulation.setBlurb(simblurb);

				if ((editing_users != null)
						&& (editing_users.equalsIgnoreCase("everyone"))) {
					simulation
							.setSimEditingRestrictions(Simulation.CAN_BE_EDITED_BY_EVERYONE);
				} else {
					simulation
							.setSimEditingRestrictions(Simulation.CAN_BE_EDITED_BY_SPECIFIC_USERS);
				}

				simulation.saveMe(schema);
			} else if (command.equalsIgnoreCase("Clear")) { //
				// returning new simulation will clear fields.
			}

			this.sim_id = simulation.getId();

			saveLastSimEdited();

		} else if (this.sim_id != null) {
			simulation = Simulation.getById(schema, this.sim_id);
		}

		return simulation;
	}

	/**
	 * 
	 * @param request
	 */
	public void addEditor(HttpServletRequest request) {

		String sending_page = request.getParameter("sending_page"); //$NON-NLS-1$
		String user_id = request.getParameter("user_id"); //$NON-NLS-1$
		String userName = request.getParameter("user_name"); //$NON-NLS-1$
		String userEmail = request.getParameter("user_email"); //$NON-NLS-1$

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("add_editor"))) {

			User user = User.getById(schema, new Long(user_id));
			SimEditors se = new SimEditors(schema, sim_id, user.getId(),
					user.getBu_full_name(), user.getUserName());
			se.saveMe(schema);
		}

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("remove_editor"))) {

			SimEditors se = new SimEditors();
			SimEditors.removeAuthorization(schema, sim_id, new Long(user_id));

		}

	}

	/** Returns the logged in value. */
	public boolean isLoggedin() {
		return loggedin;
	}

	/**
	 * Handles the work of creating or updating an actor and is called directly
	 * from the JSP.
	 * 
	 * @param request
	 */
	public void handleCreateActor(HttpServletRequest request) {

		String update_actor = (String) request.getParameter("update_actor");
		String actorid = (String) request.getParameter("actorid");
		String clear_button = (String) request.getParameter("clear_button");
		String create_actor = (String) request.getParameter("create_actor");
		String editmode = (String) request.getParameter("editmode");

		if ((update_actor != null)
				&& (update_actor.equalsIgnoreCase("Update Actor"))) {

			actor_being_worked_on_id = new Long(actorid);

			Actor actorOnScratchPad = Actor.getById(schema,
					actor_being_worked_on_id);

			createOrUpdateActor(request, actorOnScratchPad);

		} else if (create_actor != null) {
			Actor newActor = new Actor();
			newActor.setImageFilename("no_image_default.jpg");
			createOrUpdateActor(request, newActor);

		} else if ((clear_button != null)) {

			actor_being_worked_on_id = null;
		} else if (editmode != null) {
			actorid = (String) request.getParameter("actorid");
			if (actorid != null) {
				actor_being_worked_on_id = new Long(actorid);
			}
		}

	}

	/**
	 * Creates an actor.
	 * 
	 * @param request
	 * @param actorOnScratchPad
	 */
	public void createOrUpdateActor(HttpServletRequest request,
			Actor actorOnScratchPad) {

		boolean saveActor = false;
		String create_actor = (String) request.getParameter("create_actor");
		String update_actor = (String) request.getParameter("update_actor");

		if ((create_actor != null) || (update_actor != null)) {
			saveActor = true;
		}

		if (saveActor) {
			Logger.getRootLogger().debug("saving actor");
			makeUploadDir();

			String _sim_id = (String) request.getParameter("sim_id");
			Simulation sim = Simulation.getById(schema, sim_id);
			sim.updateLastEditDate(schema);

			actorOnScratchPad.setSim_id(new Long(_sim_id));

			String public_description = request
					.getParameter("public_description");
			String actor_name = request.getParameter("actor_name");
			String semi_public_description = request
					.getParameter("semi_public_description");
			String private_description = request
					.getParameter("private_description");
			String control_actor = (String) request
					.getParameter("control_actor");

			if ((actor_name == null) || (actor_name.trim().length() == 0)) {
				actor_name = "Unnamed Actor";
			}

			actorOnScratchPad.setPublic_description(public_description);
			actorOnScratchPad.setName(actor_name);
			actorOnScratchPad
					.setSemi_public_description(semi_public_description);
			actorOnScratchPad.setPrivate_description(private_description);

			if ((control_actor != null)
					&& (control_actor.equalsIgnoreCase("true"))) {
				actorOnScratchPad.setControl_actor(true);
			} else {
				actorOnScratchPad.setControl_actor(false);
			}

			// ////////////////////////////////////////////

			actorOnScratchPad.saveMe(schema);

			// String chat_color = (String) request.getParameter("chat_color");

			SimActorAssignment saa;

			// Don't add actor to sim, if he or she has already been
			// added.
			boolean simHasActor = false;
			for (ListIterator<Actor> li = SimActorAssignment.getActorsForSim(
					schema, sim_id).listIterator(); li.hasNext();) {
				Actor act = li.next();

				if (act.getId().equals(actorOnScratchPad.getId())) {
					simHasActor = true;
				}
			}

			if (!(simHasActor)) {
				saa = new SimActorAssignment(schema, sim_id,
						actorOnScratchPad.getId(),
						SimActorAssignment.TYPE_REQUIRED);
			} else {
				saa = SimActorAssignment.getBySimIdAndActorId(schema, sim_id,
						actorOnScratchPad.getId());
			}

			// saa.setActors_chat_color(chat_color);

			saa.saveMe(schema);

			SimulationSectionAssignment.applyAllUniversalSections(schema,
					sim_id);

			this.actor_name = actorOnScratchPad.getActorName();
			this.actor_being_worked_on_id = actorOnScratchPad.getId();

		}

	}

	/**
	 * 
	 * @param request
	 */
	public void handleCreateActorImages(HttpServletRequest request) {

		String actorid = "";

		try {
			MultipartRequest mpr = new MultipartRequest(request,
					USIP_OSP_Properties.getValue("uploads"));

			String set_images = (String) mpr.getParameter("set_images");

			actorid = (String) mpr.getParameter("actorid");

			if (set_images != null) {

				actor_being_worked_on_id = new Long(
						(String) mpr.getParameter("actorid"));

				Actor actorOnScratchPad = Actor.getById(schema,
						actor_being_worked_on_id);

				setActorImages(mpr, actorOnScratchPad);

			}
		} catch (java.io.IOException ioe) {
			Logger.getRootLogger().debug(
					"error in edit actor:" + ioe.getMessage());

			actorid = (String) request.getParameter("actorid");
			if (actorid != null) {
				actor_being_worked_on_id = new Long(actorid);
			}

		} catch (Exception e) {
			Logger.getRootLogger().debug(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Sets the image for an actor. The image can be uploaded.
	 * 
	 * @param mpr
	 * @param actorOnScratchPad
	 */
	public void setActorImages(MultipartRequest mpr, Actor actorOnScratchPad) {

		try {

			String MAX_FILE_SIZE = (String) mpr.getParameter("MAX_FILE_SIZE");

			Long max_file_longvalue = new Long(MAX_FILE_SIZE).longValue();

			Logger.getRootLogger().debug("saving actor image");
			makeUploadDir();

			String _sim_id = (String) mpr.getParameter("sim_id");
			Simulation sim = Simulation.getById(schema, sim_id);
			sim.updateLastEditDate(schema);

			actorOnScratchPad.setSim_id(new Long(_sim_id));

			// ////////////////////////////////////////////
			// Image portion of save
			String initFileName = mpr.getOriginalFileName("uploadedfile");

			if ((initFileName != null) && (initFileName.trim().length() > 0)) {

				actorOnScratchPad.setImageFilename(mpr
						.getOriginalFileName("uploadedfile"));

				File fileData = mpr.getFile("uploadedfile");

				Logger.getRootLogger().debug("File is " + fileData.length());

				if (fileData.length() <= max_file_longvalue) {
					FileIO.saveImageFile(OSPSimMedia.ACTOR_IMAGE,
							actorOnScratchPad.getImageFilename(),
							mpr.getFile("uploadedfile"));
				} else {
					this.errorMsg = "Selected image file too large.";
					actorOnScratchPad.setImageFilename("no_image_default.jpg");
				}

			}

			// ////////////////////////////////////////////
			// Image portion of save
			String initThumbFileName = mpr
					.getOriginalFileName("uploaded_thumb_file");

			if ((initThumbFileName != null)
					&& (initThumbFileName.trim().length() > 0)) {

				actorOnScratchPad.setImageThumbFilename(mpr
						.getOriginalFileName("uploaded_thumb_file"));

				File fileData = mpr.getFile("uploaded_thumb_file");

				Logger.getRootLogger().debug("File is " + fileData.length());

				if (fileData.length() <= max_file_longvalue) {
					FileIO.saveImageFile(OSPSimMedia.ACTOR_IMAGE,
							actorOnScratchPad.getImageThumbFilename(),
							mpr.getFile("uploaded_thumb_file"));
				} else {
					this.errorMsg += "Selected thumbnail image file too large.";
					actorOnScratchPad
							.setImageThumbFilename("no_image_default.jpg");
				}

			}

			// ////////////////////////////////////////////
			actorOnScratchPad.saveMe(schema);

			this.actor_name = actorOnScratchPad.getActorName();
			this.actor_being_worked_on_id = actorOnScratchPad.getId();

		} catch (Exception e) {
			e.printStackTrace();
			Logger.getRootLogger().debug(
					"problem in create actor: " + e.getMessage());

			try {
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
			} catch (Exception e_ignored) {
				Logger.getRootLogger()
						.warn("Difficulty in closing connection.");
				Logger.getRootLogger().warn(e_ignored.getMessage());
			}
		}

	}

	/**
	 * Creating actor images.
	 * 
	 * @param mpr
	 * @param actorOnScratchPad
	 */
	public void createActorImages(MultipartRequest mpr, Actor actorOnScratchPad) {

		try {

			String update_actor = (String) mpr.getParameter("update_actor");

			String MAX_FILE_SIZE = (String) mpr.getParameter("MAX_FILE_SIZE");

			Long max_file_longvalue = new Long(MAX_FILE_SIZE).longValue();

			if (update_actor != null) {
				Logger.getRootLogger().debug("setting actor images");

				String _sim_id = (String) mpr.getParameter("sim_id");
				Simulation sim = Simulation.getById(schema, sim_id);
				sim.updateLastEditDate(schema);

				actorOnScratchPad.setSim_id(new Long(_sim_id));

				// ////////////////////////////////////////////
				// Image portion of save
				String initFileName = mpr.getOriginalFileName("uploadedfile");

				if ((initFileName != null)
						&& (initFileName.trim().length() > 0)) {

					actorOnScratchPad.setImageFilename(mpr
							.getOriginalFileName("uploadedfile"));

					File fileData = mpr.getFile("uploadedfile");

					Logger.getRootLogger()
							.debug("File is " + fileData.length());

					if (fileData.length() <= max_file_longvalue) {
						FileIO.saveImageFile(OSPSimMedia.ACTOR_IMAGE,
								actorOnScratchPad.getImageFilename(),
								mpr.getFile("uploadedfile"));
					} else {
						this.errorMsg = "Selected image file too large.";
						actorOnScratchPad
								.setImageFilename("no_image_default.jpg");
					}

				}

				// ////////////////////////////////////////////
				// Image portion of save
				String initThumbFileName = mpr
						.getOriginalFileName("uploaded_thumb_file");

				if ((initThumbFileName != null)
						&& (initThumbFileName.trim().length() > 0)) {

					actorOnScratchPad.setImageThumbFilename(mpr
							.getOriginalFileName("uploaded_thumb_file"));

					File fileData = mpr.getFile("uploaded_thumb_file");

					Logger.getRootLogger()
							.debug("File is " + fileData.length());

					if (fileData.length() <= max_file_longvalue) {
						FileIO.saveImageFile(OSPSimMedia.ACTOR_IMAGE,
								actorOnScratchPad.getImageThumbFilename(),
								mpr.getFile("uploaded_thumb_file"));
					} else {
						this.errorMsg += "Selected thumbnail image file too large.";
						actorOnScratchPad
								.setImageThumbFilename("no_image_default.jpg");
					}

				}

				actorOnScratchPad.saveMe(schema);

			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.getRootLogger().debug(
					"problem in create actor: " + e.getMessage());

			try {
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
			} catch (Exception e_ignored) {
				Logger.getRootLogger()
						.warn("Difficulty in closing connection.");
				Logger.getRootLogger().warn(e_ignored.getMessage());
			}
		}
	}

	public static void makeUploadDir() {

		try {
			new File("uploads").mkdir();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.getRootLogger().debug(
					"attempt to make dir: " + e.getMessage());
		}

	}

	/** Its a work in progress. */
	public String getEvents() {

		String eventText = "";

		return eventText;
	}

	/**
	 * Returns the phase name stored in the web cache.
	 * 
	 * @return
	 */
	public String getPhaseName() {

		Hashtable<Long, String> phaseNames = (Hashtable<Long, String>) session
				.getServletContext()
				.getAttribute(
						USIP_OSP_ContextListener.CACHEON_L_S_PHASE_NAMES_BY_RS_ID);

		if (runningSimId != null) {
			phaseName = phaseNames.get(runningSimId);

			return phaseName;
		} else {
			return "";
		}
	}

	public Actor giveMeActor(Long a_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Actor actor = (Actor) MultiSchemaHibernateUtil.getSession(schema).get(
				Actor.class, a_id);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (actor == null) {
			actor = new Actor();
		}

		return actor;
	}

	public Actor giveMeActor() {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		Actor actor = (Actor) MultiSchemaHibernateUtil.getSession(schema).get(
				Actor.class, actor_being_worked_on_id);

		MultiSchemaHibernateUtil.getSession(schema).evict(actor);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (actor == null) {
			actor = new Actor();
		}

		return actor;
	}

	public SimulationPhase giveMePhase() {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		SimulationPhase phase = (SimulationPhase) MultiSchemaHibernateUtil
				.getSession(schema).get(SimulationPhase.class, phase_id);

		MultiSchemaHibernateUtil.getSession(schema).evict(phase);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return phase;
	}

	/**
	 * 
	 * @param sim_id
	 * @param actor_id
	 */
	public SimActorAssignment addActorToSim(HttpServletRequest request) {

		SimActorAssignment saa = new SimActorAssignment();

		String clear_queue = (String) request.getParameter("clear_queue");
		String inactivate = (String) request.getParameter("inactivate");
		String activate = (String) request.getParameter("activate");

		String queue_up = (String) request.getParameter("queue_up");
		String saa_id = (String) request.getParameter("saa_id");

		if ((clear_queue != null) && (clear_queue.equalsIgnoreCase("true"))) {
			return saa;
		} else if ((queue_up != null) && (queue_up.equalsIgnoreCase("true"))) {
			saa = SimActorAssignment.getById(schema, new Long(saa_id));
			return saa;
		}

		/**
		 * We do not just delete assignment for two reasons. First, and more
		 * importantly, the assignment may have some information in it the user
		 * will want to keep. Secondly, if user assignments are deleted, running
		 * simulations that have been created that use them will become
		 * completely broken.
		 */
		else if ((inactivate != null) && (inactivate.equalsIgnoreCase("true"))) {
			SimActorAssignment saa_in = SimActorAssignment.getById(schema,
					new Long(saa_id));
			saa_in.setActive(false);
			saa_in.saveMe(schema);
			return saa;
		}

		else if ((activate != null) && (activate.equalsIgnoreCase("true"))) {
			SimActorAssignment saa_in = SimActorAssignment.getById(schema,
					new Long(saa_id));
			saa_in.setActive(true);
			saa_in.saveMe(schema);
			return saa;
		}

		String actor_being_worked_on_id = (String) request
				.getParameter("actor_being_worked_on_id");
		String sim_id = (String) request.getParameter("sim_id");

		String saa_type = (String) request.getParameter("saa_type");
		String saa_priority = (String) request.getParameter("saa_priority");
		String saa_notes = (String) request.getParameter("saa_notes");
		String saa_role = (String) request.getParameter("saa_role");

		String create_saa = (String) request.getParameter("create_saa");
		String update_saa = (String) request.getParameter("update_saa");

		if ((update_saa != null) && (update_saa.equalsIgnoreCase("true"))) {
			saa = SimActorAssignment.getById(schema, new Long(saa_id));
			saa.storeDetails(saa_type, saa_role, saa_notes, saa_priority,
					schema);

		} else if ((create_saa != null)
				&& (create_saa.equalsIgnoreCase("true"))) {
			if (actor_being_worked_on_id != null) {

				Long s_id = new Long(sim_id);
				Long a_id = new Long(actor_being_worked_on_id);
				saa = new SimActorAssignment(schema, s_id, a_id);

				saa.storeDetails(saa_type, saa_role, saa_notes, saa_priority,
						schema);

				SimulationSectionAssignment.applyAllUniversalSections(schema,
						s_id);
			}
		} // End of if coming from this page and have assigned actor

		// ////

		// ///////////////////////////////////////////
		// Copy in an actor action.
		/*
		 * Actor this_act = Actor.getById(schema, a_id);
		 * 
		 * if (!(this_act.getSim_id().equals(s_id))) {
		 * 
		 * this_act = Actor.cloneMe(schema, a_id); this_act.setSim_id(s_id);
		 * this_act.saveMe(schema); SimActorAssignment saa = new
		 * SimActorAssignment(schema, s_id, this_act.getId());
		 * 
		 * SimulationSectionAssignment.applyAllUniversalSections(schema, s_id);
		 * 
		 * }
		 */
		// //////////////////////////////

		return saa;

	}

	/**
	 * 
	 * @param sim_id
	 * @param actor_id
	 */
	public void removeActorFromSim(String sim_id, String actor_id) {

		Long s_id = new Long(sim_id);
		Long a_id = new Long(actor_id);

		SimActorAssignment.removeMe(schema, s_id, a_id);
	}

	/**
	 * This handles customized sections in which the code for handling the
	 * customization has been put into a separate customizer class.
	 * 
	 * @param section_tag
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleCustomizeSection(
			HttpServletRequest request) {

		return (getMyPSO_SectionMgmt().handleCustomizeSection(request));
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleMakeCustomizedSection(
			HttpServletRequest request, int customSectionType) {

		CustomizeableSection cs = new CustomizeableSection();

		switch (customSectionType) {
		case CS_TYPES.DISCRETE_CHOICE:
			cs = getMyPSO_SectionMgmt().handleMakePlayerDiscreteChoice(request);
			break;
		case CS_TYPES.IMAGE_PAGE:
			cs = getMyPSO_SectionMgmt().handleMakeImagePage(request);
			break;
		case CS_TYPES.MAKE_MEMOS:
			cs = getMyPSO_SectionMgmt().handleMakeMemosPage(request);
			break;
		case CS_TYPES.PRIVATE_CHAT:
			cs = getMyPSO_SectionMgmt().handleMakePrivateChatPage(request);
			break;
		case CS_TYPES.PUSH_INJECTS:
			cs = getMyPSO_SectionMgmt().handleMakePushInjectsPage(request);
			break;
		case CS_TYPES.READ_DOCUMENT:
			cs = getMyPSO_SectionMgmt().handleMakeReadDocumentPage(request);
			break;
		case CS_TYPES.REFLECTIONS:
			cs = getMyPSO_SectionMgmt().handleMakeReflectionPage(request);
			break;
		case CS_TYPES.SET_PARAMETER:
			cs = getMyPSO_SectionMgmt().handleMakeSetParameter(request);
			break;
		case CS_TYPES.WRITE_DOCUMENT:
			cs = getMyPSO_SectionMgmt().handleMakeWriteDocumentPage(request);
			break;
		default:
			Logger.getRootLogger().warn("unknown section type");
		}

		return cs;

	}

	/**
	 * A wrapper that passes the request through to the associated
	 * PSO_SectionMgmt object.
	 * 
	 * @param request
	 * @return
	 */
	public Conversation handleMakeMeetingRoomPage(HttpServletRequest request) {
		return (getMyPSO_SectionMgmt().handleMakeMeetingRoomPage(request));
	}

	/**
	 * A wrapper that passes the request through to the associated
	 * PSO_SectionMgmt object.
	 * 
	 * @param request
	 */
	public Simulation handleCreateSchedulePage(HttpServletRequest request) {
		return (getMyPSO_SectionMgmt().handleCreateSchedulePage(request));
	}

	/**
	 * A wrapper that passes the request through to the associated
	 * PSO_SectionMgmt object.
	 * 
	 * @param request
	 */
	public CustomizeableSection handleMakeSplitPage(HttpServletRequest request,
			int numSections) {
		return (getMyPSO_SectionMgmt()
				.handleMakeSplitPage(request, numSections));
	}

	/**
	 * 
	 * @return A hashtable with all of the actor one on one coversations set in
	 *         the form of 1_2 and 2_1.
	 */
	public Hashtable setOfConversationForASection(Long section_id) {

		Hashtable returnTable = new Hashtable<String, String>();

		List currentChats = Conversation.getAllPrivateChatForASection(schema,
				sim_id);

		// Loop over all private conversations in this set
		for (ListIterator<Conversation> li = currentChats.listIterator(); li
				.hasNext();) {
			Conversation con_id = li.next();

			Vector actors = new Vector();

			MultiSchemaHibernateUtil.beginTransaction(schema);
			Conversation conv = (Conversation) MultiSchemaHibernateUtil
					.getSession(schema).get(Conversation.class, con_id.getId());

			// Get the 2 (should be 2) actors in this conversation.
			for (ListIterator<ConvActorAssignment> liiii = conv
					.getConv_actor_assigns(schema).listIterator(); liiii
					.hasNext();) {
				ConvActorAssignment caa = liiii.next();
				actors.add(caa.getActor_id());
			}

			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			for (Enumeration e1 = actors.elements(); e1.hasMoreElements();) {
				Long a_id_1 = (Long) e1.nextElement();

				for (Enumeration e2 = actors.elements(); e2.hasMoreElements();) {
					Long a_id_2 = (Long) e2.nextElement();
					String key = a_id_1 + "_" + a_id_2;
					returnTable.put(key, "set");
				}
			}
		}

		return returnTable;
	}

	/**
	 * 
	 * @return A hashtable with all of the actor one on one coversations set in
	 *         the form of 1_2 and 2_1.
	 */
	public Hashtable setOfPrivateConversation() {

		Hashtable returnTable = new Hashtable<String, String>();

		List currentChats = Conversation
				.getAllPrivateChatForSim(schema, sim_id);

		// Loop over all private conversations in this set
		for (ListIterator<Conversation> li = currentChats.listIterator(); li
				.hasNext();) {
			Conversation con_id = li.next();

			Vector actors = new Vector();

			MultiSchemaHibernateUtil.beginTransaction(schema);
			Conversation conv = (Conversation) MultiSchemaHibernateUtil
					.getSession(schema).get(Conversation.class, con_id.getId());

			// Get the 2 (should be 2) actors in this conversation.
			for (ListIterator<ConvActorAssignment> liiii = conv
					.getConv_actor_assigns(schema).listIterator(); liiii
					.hasNext();) {
				ConvActorAssignment caa = liiii.next();
				actors.add(caa.getActor_id());
			}

			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			for (Enumeration e1 = actors.elements(); e1.hasMoreElements();) {
				Long a_id_1 = (Long) e1.nextElement();

				for (Enumeration e2 = actors.elements(); e2.hasMoreElements();) {
					Long a_id_2 = (Long) e2.nextElement();

					String key = a_id_1 + "_" + a_id_2;
					returnTable.put(key, "set");
				}
			}
		}

		return returnTable;
	}

	public Hashtable ActorsWithReadAccess = new Hashtable();
	public Hashtable ActorsWithWriteAccess = new Hashtable();

	/**
	 * Fills the hashtables that indicate you can read and write various
	 * documents.
	 * 
	 */
	public void fillReadWriteLists() {
		// TODO this entire thing needs re-written since when to using dependent
		// object assingments instead
		// of keys stored in hashtables.

		/*
		 * Simulation sim = this.giveMeSim();
		 * 
		 * // Get the list of actors List actorList = sim.getActors(schema);
		 * 
		 * List phaseList = sim.getPhases(schema);
		 * 
		 * // Loop over phases for (ListIterator plist =
		 * phaseList.listIterator(); plist.hasNext();) { SimulationPhase sp =
		 * (SimulationPhase) plist.next(); // Loop over actors for (ListIterator
		 * alist = actorList.listIterator(); alist.hasNext();) { Actor act =
		 * (Actor) alist.next();
		 * 
		 * Logger.getRootLogger().debug("checking read write on " +
		 * act.getActorName()); List setOfSections =
		 * SimulationSectionAssignment.getBySimAndActorAndPhase(schema,
		 * this.sim_id, act.getId(), sp .getId());
		 * 
		 * for (ListIterator slist = setOfSections.listIterator();
		 * slist.hasNext();) { SimulationSectionAssignment ss =
		 * (SimulationSectionAssignment) slist.next();
		 * 
		 * CustomizeableSection custSec = CustomizeableSection.getById(schema,
		 * ss.getBase_section_id() + "");
		 * 
		 * if (custSec != null) { Logger.getRootLogger().debug("cs id: " +
		 * ss.getBase_section_id());
		 * Logger.getRootLogger().debug("bss rec tab: " +
		 * custSec.getRec_tab_heading());
		 * Logger.getRootLogger().debug("can read " +
		 * custSec.isConfers_read_ability());
		 * 
		 * if (custSec.isConfers_read_ability() == true) { Hashtable
		 * storedGoodies = custSec.getContents(); String docs = (String)
		 * storedGoodies.get(SharedDocument.DOCS_IN_HASHTABLE_KEY);
		 * 
		 * if (docs != null) { String currentActors = (String)
		 * ActorsWithReadAccess.get(docs);
		 * 
		 * if (currentActors == null) { currentActors = act.getId().toString();
		 * } else { currentActors += "," + act.getId(); }
		 * 
		 * ActorsWithReadAccess.put(docs, currentActors);
		 * 
		 * Logger.getRootLogger().debug("docs were : " + currentActors); }
		 * 
		 * }
		 * 
		 * if (custSec.isConfers_write_ability() == true) {
		 * Logger.getRootLogger().debug("confers read and write"); Hashtable
		 * storedGoodies = custSec.getContents(); String docs = (String)
		 * storedGoodies.get(SharedDocument.DOCS_IN_HASHTABLE_KEY);
		 * Logger.getRootLogger().debug("docs were : " + docs); } }
		 * Logger.getRootLogger
		 * ().debug("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		 * 
		 * }
		 * 
		 * } // End of loop over actors }
		 */
	}

	static SimpleDateFormat savedFilesDateFormat = new SimpleDateFormat(
			"ddMMMyyyy");

	/**
	 * Puts the name of the sim and the date into a string to use for xml export
	 * file name.
	 */
	public static String getDefaultSimXMLFileName(Simulation simulation) {

		Date saveDate = new java.util.Date();

		String fileName = simulation.getSimulationName() + "_"
				+ simulation.getVersion() + "_"
				+ savedFilesDateFormat.format(saveDate);

		fileName = USIP_OSP_Util.cleanStringForFileName(fileName);

		fileName = cleanName(fileName);

		fileName += ".xml";

		return fileName;

	}

	public String getDefaultUserArchiveXMLFileName() {

		Date saveDate = new java.util.Date();

		String fileName = "UserArchive_" + schema + "_"
				+ savedFilesDateFormat.format(saveDate);

		fileName = cleanName(fileName);

		fileName += ".xml";

		return fileName;

	}

	public static String getDefaultExperienceExportXMLFileName(
			Simulation simulation) {

		Date saveDate = new java.util.Date();

		String fileName = simulation.getSimulationName() + "_Ver_"
				+ simulation.getVersion() + "_" + "_Experience_"
				+ savedFilesDateFormat.format(saveDate);

		fileName = USIP_OSP_Util.cleanStringForFileName(fileName);

		fileName = cleanName(fileName);

		fileName += ".xml";

		return fileName;

	}

	/**
	 * Turns the simulation into an xml representation.
	 * 
	 * @return
	 */
	public String handlePackageSim(String _sim_id, String fileName) {

		FileIO.saveSimulationXMLFile(
				ObjectPackager.packageSimulation(schema, new Long(_sim_id)),
				fileName);

		return fileName;
	}

	/**
	 * 
	 * @return
	 */
	public String handlePackageUsersAndSimulations() {

		String returnString = "Packaged<br />";

		String savedFileName = getDefaultUserArchiveXMLFileName();
		FileIO.saveUserArchiveXMLFile(ObjectPackager.packageUsers(schema),
				savedFileName);

		returnString += "     Users to: " + savedFileName + "<br />";

		returnString += handlePackageSimulations();

		return returnString;
	}

	public String handlePackageSimulations() {

		String returnString = "";

		for (ListIterator<Simulation> li = Simulation.getAll(schema)
				.listIterator(); li.hasNext();) {
			Simulation sim = li.next();

			String fileName = getDefaultSimXMLFileName(sim)
					+ ".autoarchive.xml";
			handlePackageSim(sim.getId().toString(), fileName);

			returnString += "     Saved Simulation to: " + fileName + "<br />";

		}

		return returnString;
	}

	/**
	 * Turns the simulation into an xml representation.
	 * 
	 * @return
	 */
	public String handlePackageUsers() {

		String savedFileName = getDefaultUserArchiveXMLFileName();
		FileIO.saveUserArchiveXMLFile(ObjectPackager.packageUsers(schema),
				savedFileName);

		return savedFileName;
	}

	public static String cleanName(String name) {

		String returnName = name;

		returnName = returnName.replace(" ", "_");

		return returnName;
	}

	/**
	 * Handles changing the readyForListing flag, and sets key words.
	 * 
	 * @param command
	 * @param sim_key_words
	 */
	public void handlePublishing(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");
		String command = (String) request.getParameter("command");
		String sim_key_words = (String) request.getParameter("sim_key_words");
		String auto_registration = (String) request
				.getParameter("auto_registration");

		String publish_publicly = (String) request
				.getParameter("publish_publicly");
		String publish_internally = (String) request
				.getParameter("publish_internally");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("publish_sim"))) {

			if ((command == null) || (sim_id == null)) {
				return;
			}

			Simulation sim = Simulation.getById(schema, sim_id);

			if (command.equalsIgnoreCase("Update")) {
				sim.setExternallyPublished(true);
				sim.setListingKeyWords(sim_key_words);
				sim.setPublishDate(new java.util.Date());
			}

			if ((publish_publicly != null)
					&& (publish_publicly.equalsIgnoreCase("true"))) {
				sim.setExternallyPublished(true);
			} else {
				sim.setExternallyPublished(false);
			}

			if ((publish_internally != null)
					&& (publish_internally.equalsIgnoreCase("true"))) {
				sim.setInternallyPublished(true);
			} else {
				sim.setInternallyPublished(false);
			}

			if ((auto_registration != null)
					&& (auto_registration.equalsIgnoreCase("true"))) {
				sim.setAllow_player_autoreg(true);
			} else {
				sim.setAllow_player_autoreg(false);
			}

			sim.saveMe(schema);
		}

	}

	/**
	 * If user has selected an author, instructor or admin entry point into the
	 * system, this is called to set their AFSO object.
	 * 
	 * @param request
	 * @param schema_id
	 */
	public static void handleInitialEntry(HttpServletRequest request) {

		String initial_entry = (String) request.getParameter("initial_entry");

		if ((initial_entry != null) && (initial_entry.equalsIgnoreCase("true"))) {

			AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject
					.getAFSO(request.getSession(true));

			PlayerSessionObject pso = PlayerSessionObject.getPSO(request
					.getSession(true));

			afso.setLanguageCode(pso.getLanguageCode());
			afso.user_id = pso.user_id;

			String schema_id = (String) request.getParameter("schema_id");

			SchemaInformationObject sio = SchemaInformationObject
					.getById(new Long(schema_id));

			afso.schema = sio.getSchema_name();
			afso.schemaOrg = sio.getSchema_organization();

			User user = null;
			BaseUser bu = null;

			if (afso.user_id != null) {
				user = User.getById(afso.schema, afso.user_id);
				bu = BaseUser.getByUserId(afso.user_id);
			}

			if (user != null) {
				afso.user_id = user.getId();
				afso.isAdmin = user.isAdmin();
				afso.isSimAuthor = user.isSim_author();
				afso.isFacilitator = user.isSim_instructor();

				afso.userDisplayName = bu.getFull_name();
				afso.user_email = bu.getUsername();
				afso.user_name = afso.user_email;

				afso.loggedin = true;

				// //////////
				afso.sim_id = afso.getIdOfLastSimEdited();
				afso.setRunningSimId(afso.getIdOfLastRunningSimEdited());
				// /////////

				user.setLastLogin(new Date());
				user.saveJustUser(afso.schema);

				sio.setLastLogin(new Date());
				sio.saveMe();

			} else {
				afso.loggedin = false;
				Logger.getRootLogger()
						.warn("handling initial entry into simulation and got null user");
			}
		}
	}

	/**
	 * Sends password to user via email upon request.
	 * 
	 * @param request
	 * @return
	 */
	public static boolean handleSendResetPasswordEmail(
			HttpServletRequest request) {

		boolean returnValue = true;

		String email = (String) request.getParameter("email");

		BaseUser bu = BaseUser.getByUsername(email);

		if (bu == null) {
			return false;
		}

		Logger.getRootLogger().debug("emailing " + email);

		ResetPasswordObject rpo = new ResetPasswordObject();
		rpo.setUserEmail(email);
		rpo.saveMe();

		String reset_url = USIP_OSP_Properties.getCachedValue("base_sim_url")
				+ "simulation_user_admin/reset_password.jsp?rpo=" + rpo.getId()
				+ "&rnd=" + rpo.getTextRepresentation();

		String message = "Dear USIP OSP Friend,<br/><br/>A request to change your password has been submitted.<br />";

		message += "Follow this link to the <A HREF=\"" + reset_url
				+ "\"> reset password page </A>, ";
		message += "or just copy and paste this link " + reset_url
				+ " into your web browser.";

		Vector ccs = new Vector();
		Vector bccs = new Vector();

		try {
			SchemaInformationObject sio = SchemaInformationObject
					.getFirstUpEmailServer();

			if (sio != null) {
				bccs.add(sio.getEmail_archive_address());
				Emailer.postMail(sio, email, "Access to OSP", message, message,
						sio.getEmailNoreplyAddress(), null, ccs, bccs);
			} else {
				Logger.getRootLogger().warn("Warning no email servers found.");
				returnValue = false;
			}

		} catch (Exception e) {
			Logger.getRootLogger().warn(
					"retreive password error was: " + e.getMessage());
		}

		return returnValue;

	}

	/**
	 * Checks for authorization, and then passes the request to the
	 * PSO_UserAdmin object.
	 * 
	 * @param request
	 * @param schema
	 * @return
	 */
	public User handlePromoteUser(HttpServletRequest request) {

		User user = new User();

		if ((!this.isAdmin) || (!this.isSimAuthor)) {
			errorMsg = "Not authorized to create administrative users.";
			return user;
		} else {
			OSP_UserAdmin pu = new OSP_UserAdmin(this);
			return pu.handlePromoteUser(request, schema);
		}
	}

	// /////////////////////////////////////////////////////////////////////////

	/**
	 * A helper object to contain the work done in creating a section.
	 */
	private PSO_SectionMgmt pso_sm;

	/**
	 * Keep just one persistent copy of the PSO_SectionMgmt object.
	 * 
	 * @return
	 */
	public PSO_SectionMgmt getMyPSO_SectionMgmt() {
		if (pso_sm == null) {
			pso_sm = new PSO_SectionMgmt(this);
		}

		return pso_sm;
	}

	/**
	 * A wrapper that passes the request through to the associated
	 * PSO_SectionMgmt object.
	 * 
	 * @param request
	 * @return
	 */
	public Simulation handleSetUniversalSimSectionsPage(
			HttpServletRequest request) {

		return (getMyPSO_SectionMgmt()
				.handleSetUniversalSimSectionsPage(request));
	}

	/**
	 * A wrapper that passes the request through to the associated
	 * PSO_SectionMgmt object.
	 * 
	 * @param request
	 * @return
	 */
	public String handleSimSectionsRouter(HttpServletRequest request) {
		return (getMyPSO_SectionMgmt().handleSimSectionsRouter(request));
	}

	/**
	 * A wrapper that passes the request through to the associated
	 * PSO_SectionMgmt object.
	 * 
	 * @param request
	 * @return
	 */
	public Simulation handleSetSimSectionsPage(HttpServletRequest request) {
		return (getMyPSO_SectionMgmt().handleSetSimSectionsPage(request));
	}

	/**
	 * One passes this a section id, a position, and the id of an object, and if
	 * that object is assigned to this section, at that position, the word
	 * 'selected' is passed back. If not, an empty string is passed back.
	 * 
	 * @param index_hash
	 * @param object_index
	 * @param id_of_object_being_checked
	 * @return
	 */
	public String checkAgainstHash(Long cs_id, int object_index,
			Long id_of_object_being_checked) {

		Logger.getRootLogger().debug(
				"pso.checkAgainstHash (bss_id/index/object_id): " + cs_id + "/"
						+ object_index + "/" + id_of_object_being_checked);

		Hashtable index_hash = BaseSimSectionDepObjectAssignment
				.getIndexIdHashtable(schema, cs_id);

		if ((index_hash == null) || (id_of_object_being_checked == null)) {
			Logger.getRootLogger().warn("hash was null");
			return "";
		}

		// Get the value of the object stored at this position.
		Long valueFromHash = (Long) index_hash.get(new Long(object_index));

		if (id_of_object_being_checked.equals(valueFromHash)) {
			Logger.getRootLogger().debug("returning selcted");
			return " selected ";
		} else {
			return "";
		}

	}

	/**
	 * Returns a vector indicating which radio box was selected.
	 * 
	 * @param currentVarId
	 * @param allowableResponses
	 * @return
	 */
	public Hashtable selectedChoices(CustomizeableSection cs, boolean baseVar) {

		Hashtable answersSelected = new Hashtable();

		// Get the generic variable associated with this decision
		Long varId = (Long) cs.getContents().get(GenericVariable.GEN_VAR_KEY);

		if (varId == null) {
			return answersSelected;
		}

		GenericVariable gv = null;

		if (baseVar) {
			gv = GenericVariable.pullOutBaseGV(schema, cs);
		} else {
			gv = GenericVariable.getGVForRunningSim(schema, varId,
					this.runningSimId);
		}

		// Get list of allowable responses
		List allowableResponses = AllowableResponse.pullOutArs(cs, schema);

		for (ListIterator li = allowableResponses.listIterator(); li.hasNext();) {
			AllowableResponse ar = (AllowableResponse) li.next();

			Logger.getRootLogger().debug(
					"!!!!!!!!!!!!!!!!!!checking " + ar.getId());

			if ((gv != null) && (gv.getCurrentlySelectedResponse() != null)
					&& (gv.getCurrentlySelectedResponse().equals(ar.getId()))) {
				answersSelected.put(ar.getId(), " checked ");
				Logger.getRootLogger()
						.debug("put in checked for " + ar.getId());
			} else {
				answersSelected.put(ar.getId(), "");
			}
		}

		return answersSelected;
	}

	/**
	 * Accepts players choice and saves it to the database in the generic
	 * variable associated with this custom page.
	 * 
	 * @param request
	 * @param cs
	 */
	public void takePlayerChoice(HttpServletRequest request,
			CustomizeableSection cs) {

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("player_discrete_choice"))) {
			String players_choice = (String) request
					.getParameter("players_choice");
			Long answer_chosen = new Long(players_choice);

			// Save the answer currently selected in the generic variable
			// itself.
			GenericVariable gv = GenericVariable.pullMeOut(schema, cs,
					this.runningSimId);
			gv.setCurrentlySelectedResponse(answer_chosen);

			gv.checkMyTriggers(this, Trigger.FIRE_ON_WHEN_CALLED);

			gv.saveMe(schema);
		}
	}

	/**
	 * Resets the web cache for this schema.
	 * 
	 * @param request
	 * @return
	 */
	public boolean handleResetWebCache(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null) && (sending_page.equalsIgnoreCase("reset"))) {
			USIP_OSP_ContextListener.resetWebCache(request, schema);
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * @param request
	 */
	public void changeSectionColor(HttpServletRequest request) {

		String sending_section = (String) request
				.getParameter("sending_section");

		if ((sending_section != null)
				&& (sending_section.equalsIgnoreCase("change_color"))) {

			String ss_id = (String) request.getParameter("ss_id");
			String new_color = (String) request.getParameter("new_color");
			String universal_color = (String) request
					.getParameter("universal_color");

			SimulationSectionAssignment ssa = SimulationSectionAssignment
					.getById(schema, new Long(ss_id));

			if (ssa != null) {
				ssa.setTabColor(new_color);
				ssa.save(schema);
			}

			if ((universal_color != null)
					&& (universal_color.equalsIgnoreCase("true"))) {

				for (ListIterator lia = SimulationSectionAssignment
						.getUniversals(schema, ssa.getId()).listIterator(); lia
						.hasNext();) {
					SimulationSectionAssignment ssa_child = (SimulationSectionAssignment) lia
							.next();

					ssa_child.setTabColor(new_color);
					ssa_child.save(schema);

				} // End of loop over sections
			}
		}

	}

	/**
	 * 
	 * @param request
	 */
	public void handleSetNextDowntime(HttpServletRequest request) {
		String send_page = request.getParameter("send_page"); //$NON-NLS-1$

		if ((send_page != null)
				&& (send_page.equalsIgnoreCase("change_downtime"))) {

			String new_planned = request.getParameter("new_planned");

			USIP_OSP_Properties.setNextPlannedDowntime(new_planned);
		}

	}

	/**
	 * Put any housekeeping items here.
	 * 
	 * @param request
	 */
	public void logout(HttpServletRequest request) {

		if (loggedin) {
			loggedin = false;
		}
	}

	/**
	 * 
	 * @param request
	 */
	public SharedDocument handleMakeNotifications(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");

		String sd_id = (String) request.getParameter("sd_id");

		SharedDocument sd = new SharedDocument();

		if (sd_id != null) {
			sd = SharedDocument.getById(schema, new Long(sd_id));
		}

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("make_notifications_page"))) {

			String sdanao = (String) request.getParameter("sdanao");
			String actor_being_worked_on_id = (String) request
					.getParameter("actor_being_worked_on_id");
			String sdanao_text = (String) request.getParameter("sdanao_text");

			if (sdanao.equalsIgnoreCase("create_null")) {

				Long from_actor_being_worked_on_id = null;
				Long from_phase_id = null;

				@SuppressWarnings("unused")
				SharedDocActorNotificAssignObj sdanao_new = new SharedDocActorNotificAssignObj(
						schema, sim_id, sd.getId(), new Long(
								actor_being_worked_on_id),
						from_actor_being_worked_on_id, from_phase_id,
						sdanao_text);
			} else if (sdanao.startsWith("create_")) {

				sdanao = sdanao.replaceAll("create_", "");
				if ((sdanao != null) && (!(sdanao.equalsIgnoreCase("null")))) {

					SharedDocActorNotificAssignObj sdanao_edited = SharedDocActorNotificAssignObj
							.getById(schema, new Long(sdanao));

					sdanao_edited.setNotificationText(sdanao_text);
					sdanao_edited.saveMe(schema);

				}
			} else if (sdanao.startsWith("remove_")) {

				sdanao = sdanao.replaceAll("remove_", "");

				if ((sdanao != null) && (!(sdanao.equalsIgnoreCase("null")))) {
					SharedDocActorNotificAssignObj.removeSdanao(schema, sdanao);
				}

			}
		}

		return sd;
	}

	public String getMetaPhaseName(HttpServletRequest request, Long metaPhaseId) {

		if (metaPhaseId == null) {
			return "";
		} else {
			return USIP_OSP_Cache.getMetaPhaseNameById(request, schema,
					metaPhaseId);
		}

	}

	/**
	 * Handles the selection of a simulation by an author.
	 * 
	 * @param request
	 */
	public void handleSelectSimulation(HttpServletRequest request) {

		String select_sim = (String) request.getParameter("select_sim");

		if ((select_sim != null) && (select_sim.equalsIgnoreCase("true"))) {

			// Need to move this to method, and make sure all is done clean when
			// switching between simulations.
			sim_id = new Long((String) request.getParameter("sim_id"));

			// Clean up things that might be on the scratch pad.
			actor_being_worked_on_id = null;
			draft_event_id = null;
			phase_id = null;
			phaseSelected = false;

			Simulation sim = Simulation.getById(schema, sim_id);
			this.simulation_name = sim.getSimulationName();
			this.simulation_org = sim.getCreation_org();
			this.simulation_version = sim.getVersion();

			saveLastSimEdited();

			this.forward_on = true;

		}

	}

	/**
	 * Saves which sim the user last edited to the database.
	 */
	public void saveLastSimEdited() {
		if ((user_id != null) && (sim_id != null)) {
			User user = User.getById(schema, user_id);
			user.setLastSimEdited(sim_id);
			user.saveJustUser(schema);
		} else {
			Logger.getRootLogger().warn(
					"attempted to save non-existant sim or user, user/sim:"
							+ user_id + "/" + sim_id);
		}
	}

	/**
	 * Saves which sim the user last edited to the database.
	 */
	public void saveLastRunningSimEdited() {
		if ((user_id != null) && (this.getRunningSimId() != null)) {
			User user = User.getById(schema, user_id);
			user.setLastRunningSimEdited(this.getRunningSimId());
			user.saveJustUser(schema);
		} else {
			Logger.getRootLogger().warn(
					"attempted to save non-existant sim or user, user/running sim:"
							+ user_id + "/" + sim_id);
		}
	}

	/**
	 * Handles the selection of a running simulation by the author working on
	 * simulations.
	 * 
	 * @param request
	 */
	public void selectRunningSim(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");

		String select_running_sim = (String) request
				.getParameter("select_running_sim");

		if ((select_running_sim != null)
				&& (select_running_sim.equalsIgnoreCase("true"))) {

			Long r_sim_id = new Long((String) request.getParameter("r_sim_id"));

			setRunningSimId(r_sim_id);
			saveLastRunningSimEdited();

			RunningSimulation rs = giveMeRunningSim();

			run_sim_name = rs.getRunningSimulationName();
			forward_on = true;

		}
	}

	/**
	 * Handles the selection of a running simulation by the author working on
	 * simulations.
	 * 
	 * @param request
	 */
	public void selectDashboardSim(HttpServletRequest request) {

		String select_running_sim = (String) request
				.getParameter("select_running_sim");

		if ((select_running_sim != null)
				&& (select_running_sim.equalsIgnoreCase("true"))) {

			Long r_sim_id = new Long((String) request.getParameter("r_sim_id"));
			setRunningSimId(r_sim_id);

			RunningSimulation rs = giveMeRunningSim();
			run_sim_name = rs.getRunningSimulationName();

			// Changed Running Sim, may have changed Sim being worked on as
			// well.
			this.sim_id = rs.getSim_id();
			Simulation sim = this.giveMeSim();

			simulation_name = sim.getSimulationName();
			simulation_org = sim.getCreation_org();
			simulation_version = sim.getVersion();

			// Save information for next time player logs in.
			saveLastRunningSimEdited();
			saveLastSimEdited();

			forward_on = true;

		}
	}

	/**
	 * Gets the id of the simulation last edited.
	 * 
	 * @return
	 */
	public Long getIdOfLastSimEdited() {

		if (user_id != null) {
			User user = User.getById(schema, user_id);
			return user.getLastSimEdited();
		} else {
			return null;
		}
	}

	/**
	 * Gets the id of the simulation last edited.
	 * 
	 * @return
	 */
	public Long getIdOfLastRunningSimEdited() {

		if (user_id != null) {
			User user = User.getById(schema, user_id);
			return user.getLastRunningSimEdited();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param request
	 */
	public void handleRestore(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("restore"))) {

			String restore_filename = (String) request
					.getParameter("restore_filename");

			ObjectPackager.unpackageUsers(restore_filename, schema);

		}
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public RunningSimSet handleRunningSimSet(HttpServletRequest request) {

		RunningSimSet rssQueued = new RunningSimSet();
		// /////////////
		String create_set = (String) request.getParameter("create_set");

		if (create_set != null) {
			String set_name = (String) request.getParameter("set_name");
			RunningSimSet rss = new RunningSimSet();
			rss.setRunningSimSetName(set_name);
			rss.setSim_id(sim_id);
			rss.saveMe(schema);
		}

		String display_rss = (String) request.getParameter("display_rss");

		if ((display_rss != null) && (display_rss.equalsIgnoreCase("true"))) {
			String rss_id = (String) request.getParameter("rss_id");
			rssQueued = RunningSimSet.getById(schema, new Long(rss_id));
		}

		String sending_page = (String) request.getParameter("sending_page");
		String rss_id = (String) request.getParameter("rss_id");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("set_of_running_sims"))) {
			rssQueued = RunningSimSet.getById(schema, new Long(rss_id));
		}

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("edit_set"))) {

			rssQueued = RunningSimSet.getById(schema, new Long(rss_id));

			String set_name = (String) request.getParameter("set_name");
			rssQueued.setRunningSimSetName(set_name);
			rssQueued.saveMe(schema);

			RunningSimSetAssignment.removeAllForRunningSimSet(schema,
					rssQueued.getId());

			for (Enumeration<String> e = request.getParameterNames(); e
					.hasMoreElements();) {
				String pname = (String) e.nextElement();
				String vname = (String) request.getParameter(pname);

				if (pname.startsWith("rsid_")) {
					pname = pname.replaceAll("rsid_", "");

					if ((vname != null) && (vname.equalsIgnoreCase("on"))) {

						@SuppressWarnings("unused")
						RunningSimSetAssignment rssa = new RunningSimSetAssignment(
								schema, new Long(pname), rssQueued.getId());
					}
				}
			}
		}
		return rssQueued;
	}

	public boolean foundUpgradeFile(String fileName) {

		String fileLocation = FileIO.upgrade_files_dir + File.separator
				+ fileName;

		File upgradeFile = new File(fileLocation);

		if (upgradeFile.exists() && upgradeFile.canRead()) {
			return true;
		} else {
			return false;
		}

	}

	private static final String CTRL_PANEL = "control_panel.jsp";
	public static final int CREATE_SIM = 1;
	public static final int ENTER_SIM_BASIC_INFO = 2;
	public static final int SIM_OBJECTIVES = 3;
	public static final int SIM_AUDIENCE = 4;
	public static final int SIM_INTRO = 5;
	public static final int SIM_PLANNED_PLAY_IDEAS = 6;
	public static final int SIM_AAR_TEXT = 7;

	/**
	 * This method handles the call from a wizard page of the wizard.
	 * 
	 * @param request
	 * @return
	 */
	public Simulation handleWizardPage(HttpServletRequest request, int saveType) {

		Simulation simulation = new Simulation();

		String cancel = (String) request.getParameter("cancel");
		// Clean nulls on sending page to avoid null object error.
		String sending_page = USIP_OSP_Util.cleanNulls((String) request
				.getParameter("sending_page"));
		String sim_text = (String) request.getParameter("sim_text");
		String save = (String) request.getParameter("save");
		String save_and_proceed = (String) request
				.getParameter("save_and_proceed");

		if (cancel != null) {
			this.forward_on = true;
			this.nextPage = CTRL_PANEL;
			return simulation;
		}

		if (sim_id != null) {
			simulation = giveMeSim();
		} else {
			return simulation;
		}

		// If doing a save.
		if (((save != null) || (save_and_proceed != null))
				&& (sending_page.equalsIgnoreCase("authoring_wizard_page"))) {

			switch (saveType) {
			case SIM_OBJECTIVES:
				simulation.setLearning_objvs(sim_text);
				String sim_hidden_objs = request
						.getParameter("sim_hidden_objs");
				simulation.setHiddenLearningObjectives(sim_hidden_objs);
				nextPage = "create_simulation_audience.jsp";
				break;
			case SIM_AUDIENCE:
				simulation.setAudience(sim_text);
				nextPage = "create_simulation_introduction.jsp";
				break;
			case SIM_INTRO:
				simulation.setIntroduction(sim_text);
				nextPage = "create_simulation_planned_play_ideas.jsp";
				break;
			case SIM_PLANNED_PLAY_IDEAS:
				String min_num_players = USIP_OSP_Util
						.cleanNulls((String) request
								.getParameter("min_num_players"));
				String max_num_players = USIP_OSP_Util
						.cleanNulls((String) request
								.getParameter("max_num_players"));
				String min_play_time = USIP_OSP_Util
						.cleanNulls((String) request
								.getParameter("min_play_time"));
				String rec_play_time = USIP_OSP_Util
						.cleanNulls((String) request
								.getParameter("rec_play_time"));

				PlannedPlaySessionParameters ppsp = simulation.getPPSP(schema);
				ppsp.setMinNumPlayers(min_num_players);
				ppsp.setMaxNumPlayers(max_num_players);
				ppsp.setMinPlayTime(min_play_time);
				ppsp.setRecommendedPlayTime(rec_play_time);
				ppsp.setPlannedPlayIdeas(sim_text);
				ppsp.saveMe(schema);

				simulation.setPlannedPlayIdeas(sim_text);
				nextPage = "create_simulation_phases.jsp";
				break;
			case SIM_AAR_TEXT:
				simulation.setAarStarterText(sim_text);
				nextPage = "review_sim.jsp";
				break;

			default:
				Logger.getRootLogger().warn("Unknown wizard save case");
				break;
			}

			simulation.saveMe(schema);

			if ((save_and_proceed != null)) {
				this.forward_on = true;
			}
		}

		return simulation;

	}

	public String getBaseList(HttpServletRequest request) {

		List baseList = BaseSimSection.getAll(schema);

		// USIP_OSP_Cache.getBaseSectionInformation(schema,request);

		String returnString = "";

		for (ListIterator li = new BaseSimSection().getAll(schema)
				.listIterator(); li.hasNext();) {
			BaseSimSection bss = (BaseSimSection) li.next();

			returnString += "<option value=\"" + bss.getId() + "\">"
					+ bss.getRec_tab_heading() + "</option>"
					+ USIP_OSP_Util.lineTerminator;
		}

		return returnString;
	}

	/**
	 * Pulls the list out of cache.
	 * 
	 * @param request
	 * @return
	 */
	public List getUncustomizedSections(HttpServletRequest request) {
		// List uc = USIP_OSP_Cache.getCustomSectionInformation(schema,
		// request);

		List uc = CustomizeableSection.getAllUncustomized(schema);

		if (uc == null) {
			uc = new ArrayList();
		}

		Collections.sort(uc);
		return uc;
	}

	/**
	 * Returns the HTML containing all of the sections.
	 * 
	 * @return
	 */
	public String getSectionsList(HttpServletRequest request) {

		String returnString = getBaseList(request);

		List uncustomizedList = getUncustomizedSections(request);

		String rawCust = "";
		String custCust = "";

		for (ListIterator li = uncustomizedList.listIterator(); li.hasNext();) {
			CustomizeableSection cs = (CustomizeableSection) li.next();

			// ////////////////////////////////////////////////////////
			// Don't list sections the actor already has at this phase.
			// TODO this was slowing things down terribly. Better to just add it
			// later if needed.
			boolean hasItAlready = false;

			/*
			 * SimulationSectionAssignment
			 * .determineIfActorHasThisSectionAtThisPhase(schema, sim_id,
			 * actor_being_worked_on_id, phase_id, cs.getId());
			 */
			boolean forThisSimulation = false;

			if (cs.getSimId() == null) {
				forThisSimulation = true;
			} else if (cs.getSimId().intValue() == sim_id.intValue()) {
				forThisSimulation = true;
			}

			if ((!(hasItAlready)) && (forThisSimulation)) {

				if (cs.isThisIsACustomizedSection()) {
					custCust += "<option value=\"" + cs.getId().toString()
							+ "\" class=\"player_customized_section\" >"
							+ cs.getRec_tab_heading() + "</option>"
							+ USIP_OSP_Util.lineTerminator;
				} else {
					rawCust += "<option value=\"" + cs.getId().toString()
							+ "\" class=\"customized_section\" >"
							+ cs.getRec_tab_heading() + "</option>"
							+ USIP_OSP_Util.lineTerminator;
				}

			} // End of if they don't have this section already at this
				// phase
		} // End of loop over customizeable sections

		returnString = returnString + rawCust + custCust;

		return returnString;
	}

	public static void main(String args[]) {

		Calendar y = new GregorianCalendar();

		y.set(1900, 10, 10, 10, 10);

		java.util.Date x = y.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy H");

		try {
			Date g = sdf.parse("10/18/2010 9");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List getImageFiles() {

		ArrayList returnList = new ArrayList();

		// The set of base simulation sections are read out of
		// XML files stored in the simulation_section_information directory.

		String fileLocation = FileIO.getActor_image_dir();

		File locDir = new File(fileLocation);

		if (locDir == null) {
			Logger.getRootLogger().debug(
					"Problem finding files at " + fileLocation); //$NON-NLS-1$
			return returnList;
		} else {

			File files[] = locDir.listFiles();

			if (files == null) {
				Logger.getRootLogger().debug(
						"Problem finding files at " + fileLocation); //$NON-NLS-1$
				return returnList;
			} else {
				for (int ii = 0; ii < files.length; ii++) {

					String fName = files[ii].getName();
					if ((fName.endsWith(".png")) || ((fName.endsWith(".gif")))
							|| ((fName.endsWith(".jpg")))
							|| (fName.endsWith(".PNG"))
							|| ((fName.endsWith(".GIF")))
							|| ((fName.endsWith(".JPG")))) { //$NON-NLS-1$
						try {
							Vector thisImageVector = new Vector();
							String fullFileLoc = fileLocation + fName;
							String relativePath = "../osp_core/images/actors/"
									+ fName;

							thisImageVector.add(relativePath);
							thisImageVector.add(fName);

							returnList.add(thisImageVector);

						} catch (Exception e) {
							Logger.getRootLogger().debug(
									"problem reading in file " + fName); //$NON-NLS-1$
							Logger.getRootLogger().debug(e.getMessage());
						}
					}

				}
			}

			return returnList;
		} // end of if found files.
	} // end of method

	/**
	 * Handles the CRUD on creating items.
	 * 
	 * @param request
	 * @return
	 */
	public InventoryItem handleCreateItems(HttpServletRequest request) {

		InventoryItem inventoryItem = new InventoryItem();

		// If the player cleared the form, return the blank document.
		String clear_button = (String) request.getParameter("clear_button");
		if (clear_button != null) {
			return inventoryItem;
		}

		// If we got passed in a doc id, use it to retrieve the doc we are
		// working on.
		String ii_id = (String) request.getParameter("ii_id");

		String queueup = (String) request.getParameter("queueup");
		if ((queueup != null) && (queueup.equalsIgnoreCase("true"))
				&& (ii_id != null) && (ii_id.trim().length() > 0)) {
			inventoryItem = InventoryItem.getById(schema, new Long(ii_id));
			return inventoryItem;
		}

		// If player just entered this page from a different form, just return
		// the blank document
		String sending_page = (String) request.getParameter("sending_page");
		if ((sending_page == null)
				|| (!(sending_page.equalsIgnoreCase("make_create_items_page")))) {
			return inventoryItem;
		}

		// If we got down to here, we must be doing some real work on a
		// document.
		String item_name = (String) request.getParameter("item_name");
		String item_description = (String) request
				.getParameter("item_description");
		String item_notes = (String) request.getParameter("item_notes");

		// Do create if called.
		String create_item = (String) request.getParameter("create_item");
		if ((create_item != null)) {
			Logger.getRootLogger().debug(
					"creating item of uniq name: " + item_name);
			inventoryItem = new InventoryItem(item_name, item_description,
					item_notes, sim_id, true);
			inventoryItem.saveMe(schema);
		}

		// Do update if called.
		String update_item = (String) request.getParameter("update_item");
		if ((update_item != null)) {
			Logger.getRootLogger().debug(
					"updating item of uniq title: " + item_name);
			inventoryItem = InventoryItem.getById(schema, new Long(ii_id));
			inventoryItem.setItemName(item_name);
			inventoryItem.setDescription(item_description);
			inventoryItem.setNotes(item_notes);
			inventoryItem.setSim_id(sim_id);
			inventoryItem.saveMe(schema);

		}

		return inventoryItem;
	}

	/**
	 * 
	 * @param request
	 */
	public void handleDistributeItems(HttpServletRequest request) {

		Hashtable itemDistribution = new Hashtable();

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("distribute_items"))) {

			String ii_id = (String) request.getParameter("ii_id");

			if (ii_id != null) {

				InventoryItem iiTemplate = InventoryItem.getById(schema,
						new Long(ii_id));

				// Remove all previous distributions
				InventoryItem.removeTemplateAssignments(schema,
						new Long(ii_id), sim_id);

				// Loop over all of the actor ids found
				for (Enumeration<String> e = request.getParameterNames(); e
						.hasMoreElements();) {
					String pname = (String) e.nextElement();

					String vname = (String) request.getParameter(pname);
					Logger.getRootLogger().debug(pname + " " + vname);

					if (pname.startsWith("ii_assign_")) {
						pname = pname.replaceAll("ii_assign_", "");

						if ((vname == null) || (vname.equalsIgnoreCase(""))
								|| (vname.equalsIgnoreCase("null"))) {
							vname = "0";
						}
						// turn vname into an int
						int numItems = 0;
						try {
							numItems = new Long(vname).intValue();
						} catch (Exception er) {
							Logger.getRootLogger().warn(
									"Trouble converting vname: " + vname);
						}
						// Create one item record for each item an actor has in
						// their possession
						for (int ii = 1; ii <= numItems; ++ii) {
							InventoryItem iItem = new InventoryItem(
									iiTemplate.getItemName(),
									iiTemplate.getDescription(),
									iiTemplate.getNotes(), sim_id, false);

							iItem.setBase_id(iiTemplate.getId());
							iItem.setOwner_id(new Long(pname));
							iItem.saveMe(schema);

						}
					}

				}

			}
		}
	}

	/**
	 * if a message, not an error, has to get reported back to the user, you can
	 * use this.
	 */
	public String tempMsg = "";

	/**
	 * 
	 * @param request
	 * @return
	 */
	public Email handleEmailUserPassword(HttpServletRequest request) {

		Email email = new Email();

		// Handle what happens when they want to send an email.
		String send_email = request.getParameter("send_email");
		String u_id = request.getParameter("u_id");

		if (send_email != null) {
			String email_subject = request.getParameter("email_subject");
			String email_text = request.getParameter("email_text");
			String email_from = request.getParameter("email_from");
			String email_to = request.getParameter("email_to");

			email.setSubjectLine(email_subject);
			email.setMsgtext(email_text);
			email.setHtmlMsgText(email_text);
			email.setMsgDate(new java.util.Date());
			email.setFromUserName(email_from);
			email.setFromUser(user_id);
			email.setToActorEmail(false);
			email.saveMe(schema);

			// Set to
			EmailRecipients er = new EmailRecipients(schema, email.getId(),
					this.getRunningSimId(), sim_id, email_to,
					EmailRecipients.RECIPIENT_TO);
			EmailRecipients er_cc = new EmailRecipients(schema, email.getId(),
					this.getRunningSimId(), sim_id, email_to,
					EmailRecipients.RECIPIENT_CC);

			SchemaInformationObject sio = SchemaInformationObject
					.lookUpSIOByName(schema);
			email.sendMe(sio);

			this.forward_on = true;
		}

		// Handle what happens when they get here after creating a user.

		if (u_id != null) {
			User user = new User();
			user = User.getById(schema, new Long(u_id));

			tempMsg = user.getBu_username();

			email = prepResponseEmail(request, user);
		}

		return email;
	}

	/**
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	public Email prepResponseEmail(HttpServletRequest request, User user) {

		Email email = new Email();

		BaseUser bu = BaseUser.getByUserId(user.getId());

		email.setSubjectLine("USIP OSP Registration Complete");

		String responseText = "";
		responseText += "<p>Dear " + user.getBu_full_name() + ", </p>"
				+ USIP_OSP_Util.lineTerminator;

		responseText += "<p>You have been registered on a USIP OSP system, and may now login.</p>"
				+ USIP_OSP_Util.lineTerminator;

		responseText += "<p></p>" + USIP_OSP_Util.lineTerminator;

		responseText += "<p>Site: <a href=\""
				+ USIP_OSP_Properties.getValue("simulation_url") + "\">"
				+ USIP_OSP_Properties.getValue("simulation_url") + "</a></p>"
				+ USIP_OSP_Util.lineTerminator;
		responseText += "<p>Username: " + user.getBu_username() + "</p>"
				+ USIP_OSP_Util.lineTerminator;
		responseText += "<p>Password: " + user.getBu_password() + "</p>"
				+ USIP_OSP_Util.lineTerminator;

		if (bu.isTempPassword()) {
			responseText += "<p>This is temporary password. You will need to change it after you "
					+ "login to the system.</p>" + USIP_OSP_Util.lineTerminator;
		}

		responseText += "<p>Thank You</p>" + USIP_OSP_Util.lineTerminator;

		email.setMsgtext(responseText);

		email.setHtmlMsgText(responseText);

		return email;
	}

	public static ArrayList getBaseResponseEmailText() {
		return null;
	}

	public static ArrayList htmlIfyAString(ArrayList<String> inputString) {

		for (ListIterator<String> li = inputString.listIterator(); li.hasNext();) {
			String this_act = li.next();
		}
		return null;
	}

	/**
	 * Used to change a user name (email address) of a player.
	 * 
	 * @param u_id
	 * @param newUserName
	 * @return
	 */
	public static boolean changePlayerUsername(Long u_id, String newUserName) {

		// Change name in base user table

		// Change name in any user table in any schema

		// Change name in any user assignments in any schema
		return false;
	}

	/**
	 * 
	 */
	public RunningSimulation editRunningSimulation(HttpServletRequest request) {
		String rs_id = (String) request.getParameter("rs_id");

		RunningSimulation rs = new RunningSimulation();

		if ((rs_id != null) && (!(rs_id.equalsIgnoreCase("null")))) {
			rs = RunningSimulation.getById(schema, new Long(rs_id));

			String sending_page = (String) request.getParameter("sending_page");

			if ((sending_page != null)
					&& ((sending_page.equalsIgnoreCase("rs_changename")))) {

				String rs_new_name = (String) request
						.getParameter("rs_new_name");
				String timezone = (String) request.getParameter("timezone");

				rs.setName(rs_new_name);
				rs.setTimeZone(timezone);
				rs.saveMe(schema);

			}
		}
		return rs;
	}

	/**
	 * Gets an actor with id of 0, and with name of 'Every One,' and sets the id
	 * of the actor being worked on to 0.
	 * 
	 * @return
	 */
	public Actor getAndSetUniversalActor() {

		Actor actor = new Actor();

		actor_being_worked_on_id = new Long(0);

		actor.setId(actor_being_worked_on_id);
		actor.setName("Every One");

		return actor;
	}

	/** Effectively logs in the user based on their having entered the correct wipe database key.
	 * 
	 * @param afso
	 * @param io
	 */
	public static void setFromInstallObject(
			AuthorFacilitatorSessionObject afso, InstallationObject io) {

		afso.loggedin = io.isInstallationLogin();

	}

	public void moveFromEditStarterPage(HttpServletRequest request) {

		// If we are proceeding, we need to see if we need to go to another
		// starter document, or to the the assign players page.
		String starterDocIndex = (String) request
				.getParameter("starterDocIndex");

		int starter_doc_index = new Long(starterDocIndex).intValue();

		String command_save_and_proceed = (String) request
				.getParameter("command_save_and_proceed");

		if (command_save_and_proceed != null) {

			forward_on = true;

			backPage = "facilitate_assign_user_to_simulation.jsp";

			List starterDocs = SharedDocument.getAllStarterBaseDocumentsForSim(
					schema, sim_id, getRunningSimId());

			if ((starter_doc_index + 1) < starterDocs.size()) {
				starter_doc_index += 1;
				SharedDocument sd_next = (SharedDocument) starterDocs
						.get(starter_doc_index);

				backPage = "facilitate_write_starter_document.jsp?sendingDocId=true&doc_id="
						+ sd_next.getId()
						+ "&starterDocIndex="
						+ starter_doc_index;

			}
		}
	}

} // End of class
