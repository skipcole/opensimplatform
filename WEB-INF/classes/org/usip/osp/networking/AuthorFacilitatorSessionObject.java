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
import org.usip.osp.persistence.*;
import org.apache.log4j.Logger;
import org.usip.osp.specialfeatures.*;

import com.oreilly.servlet.MultipartRequest;

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

	/** Page to forward the user on to. */
	public boolean forward_on = false;

	/** Organization of the schema that the user is working in. */
	public String schemaOrg = ""; //$NON-NLS-1$

	/** The page to take them back to if needed. */
	public String backPage = "index.jsp"; //$NON-NLS-1$

	/**
	 * Username/ Email address of user that is logged in and using this
	 * AuthorFacilitatorSessionObject.
	 */
	public String user_name;

	/** Records if user is an admin. */
	private boolean isAdmin = false;

	/** Records if user is authorized to create simulations. */
	private boolean isSimAuthor = false;

	/** Records if user is authorized to facilitate simulations. */
	private boolean isFacilitator = false;

	/** Records the email of this user. */
	public String user_email = ""; //$NON-NLS-1$

	/** Name of the actor being played or worked on. */
	public String actor_name = ""; //$NON-NLS-1$

	/** Indicates if user has selected a phase. */
	public boolean phaseSelected = false;

	/** Name of phase being conducted or worked on. */
	private String phaseName = ""; //$NON-NLS-1$

	/** The Session object. */
	private HttpSession session = null;

	/** Error message to be shown to the user. */
	public String errorMsg = ""; //$NON-NLS-1$

	public List tempSimSecList = new ArrayList();

	/**
	 * Unpacks a simulation from an XML file.
	 * 
	 * @param request
	 */
	public Simulation handleUnpackDetails(HttpServletRequest request) {

		String filename = request.getParameter("filename"); //$NON-NLS-1$

		Logger.getRootLogger().debug("unpacking " + filename); //$NON-NLS-1$

		return ObjectPackager.unpackSimDetails(filename, this.schema);

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

		Logger.getRootLogger().debug("unpacking " + filename); //$NON-NLS-1$

		ObjectPackager.unpackageSim(filename, this.schema, sim_name,
				sim_version);

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

				List<Actor> ctrl_actors = Actor.getControlActors(schema, sim
						.getId());

				for (ListIterator<Actor> li = ctrl_actors.listIterator(); li
						.hasNext();) {
					Actor this_act = li.next();
					sim.addControlSectionsToAllPhasesOfControl(this.schema,
							this_act);
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

	/** Assigns a user to a simulation. */
	public void handleAssignUser(HttpServletRequest request) {

		String command = request.getParameter("command"); //$NON-NLS-1$

		if (command != null) {
			if ((command.equalsIgnoreCase("Assign User"))) { //$NON-NLS-1$

				String user_to_add_to_simulation = request
						.getParameter("user_to_add_to_simulation"); //$NON-NLS-1$

				Long user_to_add_id = USIP_OSP_Cache.getUserIdByName(schema,
						request, user_to_add_to_simulation);

				if (user_to_add_id != null) {
					String actor_id = request
							.getParameter("actor_to_add_to_simulation"); //$NON-NLS-1$
					String sim_id = request
							.getParameter("simulation_adding_to"); //$NON-NLS-1$
					String running_sim_id = request
							.getParameter("running_simulation_adding_to"); //$NON-NLS-1$

					@SuppressWarnings("unused")
					UserAssignment ua = UserAssignment.getUniqueUserAssignment(
							this.schema, new Long(sim_id), new Long(
									running_sim_id), new Long(actor_id),
							new Long(user_to_add_id));
				}
			}
		}
	}

	public String setOfUsers = ""; //$NON-NLS-1$
	public String invitationCode = ""; //$NON-NLS-1$

	public String getDefaultInviteMessage() {

		String defaultInviteEmailMsg = "Dear Student,\r\n"; //$NON-NLS-1$
		defaultInviteEmailMsg += "Please go to the web site "; //$NON-NLS-1$
		defaultInviteEmailMsg += USIP_OSP_Properties.getValue("simulation_url") //$NON-NLS-1$
				+ "/simulation_user_admin/auto_registration_form.jsp";

		Long schema_id = SchemaInformationObject.lookUpId(this.schema);

		defaultInviteEmailMsg += "?schema_id=" + schema_id;

		defaultInviteEmailMsg += " and register yourself.\r\n\r\n"; //$NON-NLS-1$
		defaultInviteEmailMsg += "Thank you,\r\n"; //$NON-NLS-1$
		defaultInviteEmailMsg += this.user_Display_Name;

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

				System.out.println("rsn is " + rsn);
				// Create Running Simulation
				RunningSimulation rs = sim.addNewRunningSimulation(rsn, schema,
						this.user_id, this.user_Display_Name);
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

				rs.enableAndPrep(this.schema, sim.getId().toString(), bu
						.getUsername(), email_users, email_text);

			} else {

				returnString += "<font color=\"red\">Warning: did not find user:"
						+ this_email + "</font><br />";

			}
		}

		return returnString;
	}

	/**
	 * 
	 * @param request
	 */
	public void handleBulkInvite(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page == null)
				|| (!(sending_page.equalsIgnoreCase("bulk_invite")))) {
			return;
		}

		this.setOfUsers = request.getParameter("setOfUsers"); //$NON-NLS-1$
		String thisInviteEmailMsg = request
				.getParameter("defaultInviteEmailMsg"); //$NON-NLS-1$
		this.invitationCode = request.getParameter("invitationCode"); //$NON-NLS-1$

		for (ListIterator<String> li = getSetOfEmails(this.setOfUsers)
				.listIterator(); li.hasNext();) {
			String this_email = li.next();

			if (BaseUser.checkIfUserExists(this_email)) {
				Logger.getRootLogger().debug("exists:" + this_email);
				// ?? make sure exists in this schema

			} else {

				Logger.getRootLogger().debug("does not exist:" + this_email);

				// Add entry into system to all them to register.
				UserRegistrationInvite uri = new UserRegistrationInvite(
						this.user_name, this_email, this.invitationCode,
						this.schema);

				uri.saveMe();

				// Send them email directing them to the page to register

				String subject = "Invitation to register on an OSP System";
				sendBulkInvitationEmail(this_email, subject, thisInviteEmailMsg);

			}
		}
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

		Emailer.postMail(sio, the_email, subject, message, this.user_name,
				cced, bcced);
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
				MultiSchemaHibernateUtil.beginTransaction(this.schema);
				Simulation sim = (Simulation) MultiSchemaHibernateUtil
						.getSession(this.schema).get(Simulation.class, o_id);
				MultiSchemaHibernateUtil.getSession(this.schema).delete(sim);
				this.sim_id = null;

				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

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

				SimulationSectionAssignment.removeAndReorder(this.schema, ss);

				// Clean web cache out
				USIP_OSP_ContextListener.resetWebCache(request);

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

			Simulation.updateSimsLastEditDate(sim_id, schema);

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
			if ((command.equalsIgnoreCase("Start Simulation"))) {

				String email_users = request.getParameter("email_users");
				String email_text = request.getParameter("email_text");

				BaseUser bu = BaseUser.getByUserId(this.user_id);

				MultiSchemaHibernateUtil.beginTransaction(this.schema);

				RunningSimulation running_sim = (RunningSimulation) MultiSchemaHibernateUtil
						.getSession(this.schema).get(RunningSimulation.class,
								this.runningSimId);

				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

				running_sim.enableAndPrep(this.schema, this.sim_id.toString(),
						bu.getUsername(), email_users, email_text);

			} // End of if coming from this page and have enabled the sim
			// ////////////////////////////
		}

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
	public void getAndLoad(HttpServletRequest request) {

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
	 * This handles
	 * 
	 * @param section_tag
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleCustomizeSection(
			HttpServletRequest request) {

		return (getMyPSO_SectionMgmt().handleCustomizeSection(request));
	}

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

			System.out.println("p/v: " + pname + "/" + vname);

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

	/**
	 * 
	 * @param request
	 * @return
	 */
	public TimeLine handleCreateTimeLine(HttpServletRequest request) {

		TimeLine timeline = new TimeLine();

		// If the player cleared the form, return the blank document.
		String clear_button = (String) request.getParameter("clear_button");
		if (clear_button != null) {
			return timeline;
		}

		// If we got passed in a doc id, use it to retrieve the doc we are
		// working on.
		String t_id = (String) request.getParameter("t_id");

		String queueup = (String) request.getParameter("queueup");
		if ((queueup != null) && (queueup.equalsIgnoreCase("true"))
				&& (t_id != null) && (t_id.trim().length() > 0)) {
			timeline = TimeLine.getById(schema, new Long(t_id));
			return timeline;
		}

		// If player just entered this page from a different form, just return
		// the blank object
		String sending_page = (String) request.getParameter("sending_page");
		if ((sending_page == null)
				|| (!(sending_page
						.equalsIgnoreCase("make_create_timeline_page")))) {
			return timeline;
		}

		// If we got down to here, we must be doing some real work on a
		// document.
		String timeline_name = (String) request.getParameter("timeline_name");
		String param_notes = (String) request.getParameter("param_notes");
		String start_value = (String) request.getParameter("start_value");

		// Do create if called.
		String create_timeline = (String) request
				.getParameter("create_timeline");
		if ((create_timeline != null)) {
			Logger.getRootLogger().debug(
					"creating param of uniq name: " + timeline_name);
			// timeline = new TimeLine(uniq_param_name, sim_id);
			timeline.setName(timeline_name);
			// timeline.saveMe(schema);
		}

		// Do update if called.
		String update_timeline = (String) request
				.getParameter("update_timeline");
		if ((update_timeline != null)) {
			Logger.getRootLogger().debug(
					"updating param of uniq title: " + timeline_name);
			timeline = TimeLine.getById(schema, new Long(t_id));
			timeline.setName(timeline_name);
			// timeline.setNotes(param_notes);
			// timeline.setStartingValue(start_value);
			timeline.setSimId(sim_id);
			timeline.saveMe(schema);

		}

		return timeline;

	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public Conversation handleCreateConversation(HttpServletRequest request) {

		Conversation conv = new Conversation();

		// If the player cleared the form, return the blank document.
		String clear_button = (String) request.getParameter("clear_button");
		if (clear_button != null) {
			return conv;
		}

		// If we got passed in a doc id, use it to retrieve the doc we are
		// working on.
		String conv_id = (String) request.getParameter("conv_id");

		String queueup = (String) request.getParameter("queueup");
		if ((queueup != null) && (queueup.equalsIgnoreCase("true"))
				&& (conv_id != null) && (conv_id.trim().length() > 0)) {
			conv = Conversation.getById(schema, new Long(conv_id));
			return conv;
		}

		// If player just entered this page from a different form, just return
		// the blank document
		String sending_page = (String) request.getParameter("sending_page");
		if ((sending_page == null)
				|| (!(sending_page
						.equalsIgnoreCase("make_create_conversation_page")))) {
			return conv;
		}

		// If we got down to here, we must be doing some real work on a
		// document.
		String uniq_conv_name = (String) request.getParameter("uniq_conv_name");
		String conv_notes = (String) request.getParameter("conv_notes");

		// Do create if called.
		String create_conv = (String) request.getParameter("create_conv");
		if ((create_conv != null)) {
			Logger.getRootLogger().debug(
					"creating conv of uniq name: " + uniq_conv_name);
			conv = new Conversation(uniq_conv_name, conv_notes, sim_id);
			conv.saveMe(schema);
		}

		// Do update if called.
		String update_conv = (String) request.getParameter("update_conv");
		if ((update_conv != null)) {
			Logger.getRootLogger().debug(
					"updating conv of uniq title: " + uniq_conv_name);
			conv = Conversation.getById(schema, new Long(conv_id));
			conv.setUniqueConvName(uniq_conv_name);
			conv.setConversationNotes(conv_notes);
			conv.setSim_id(sim_id);
			conv.saveMe(schema);

		}

		// Need to clean out current actor assignments for this conversation
		ConvActorAssignment.removeAllForConversation(schema, conv.getId());

		Hashtable setOfUserRoles = new Hashtable();

		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			String param_name = (String) e.nextElement();

			if (param_name.startsWith("role_")) {
				String this_a_id = param_name.replaceFirst("role_", "");

				setOfUserRoles.put(this_a_id, (String) request
						.getParameter(param_name));

			}
		}

		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			String param_name = (String) e.nextElement();

			if (param_name.startsWith("actor_cb_")) {
				if ((request.getParameter(param_name) != null)
						&& (request.getParameter(param_name)
								.equalsIgnoreCase("true"))) {
					String this_a_id = param_name.replaceFirst("actor_cb_", "");
					Logger.getRootLogger().debug(
							"adding " + this_a_id + " in schema" + schema
									+ " to sim_id " + sim_id);
					conv.addActor(this_a_id, schema, sim_id,
							(String) setOfUserRoles.get(this_a_id));
				}
			}

		}
		return conv;
	}

	/**
	 * Handles the creation of documents to be added to the simulation. This
	 * method is called at the top of the jsp. It can be called for several
	 * reasons. 1.) Player is just entering form. Method should return a new,
	 * 'unsaved' document. 2.) Player hits the create button. Method should
	 * return the shared document created. 3.) Player select one of the existing
	 * docs to queue it up for editing. Method should return the doc selected.
	 * 4.) Player hit the clear button, so method should return a new, 'unsaved'
	 * document. 5.) Player hit the update button, so method should update the
	 * document and then return it.
	 * 
	 * @param request
	 */
	public SharedDocument handleCreateDocument(HttpServletRequest request) {

		SharedDocument this_sd = new SharedDocument();

		// If the player cleared the form, return the blank document.
		String clear_button = (String) request.getParameter("clear_button");
		if (clear_button != null) {
			return this_sd;
		}

		// If we got passed in a doc id, use it to retrieve the doc we are
		// working on.
		String shared_doc_id = (String) request.getParameter("shared_doc_id");
		if ((shared_doc_id != null) && (shared_doc_id.trim().length() > 0)) {
			this_sd = SharedDocument.getById(schema, new Long(shared_doc_id));
			// Probably could return this_sd and exit out at this point
		}

		// If player just entered this page from a different form, just return
		// the blank document
		String sending_page = (String) request.getParameter("sending_page");
		if ((sending_page == null)
				|| (!(sending_page
						.equalsIgnoreCase("make_create_document_page")))) {
			return this_sd;
		}

		// If we got down to here, we must be doing some real work on a
		// document.
		String uniq_doc_title = (String) request.getParameter("uniq_doc_title");
		String doc_display_title = (String) request
				.getParameter("doc_display_title");
		String doc_starter_text = (String) request
				.getParameter("doc_starter_text");

		// Do create if called.
		String create_doc = (String) request.getParameter("create_doc");
		if ((create_doc != null)) {
			Logger.getRootLogger().debug(
					"creating doc of uniq title: " + uniq_doc_title);
			this_sd = new SharedDocument(uniq_doc_title, doc_display_title,
					sim_id);
			this_sd.setBigString(doc_starter_text);
			this_sd.saveMe(schema);

		}

		// Do update if called.
		String update_doc = (String) request.getParameter("update_doc");
		if ((update_doc != null)) {
			Logger.getRootLogger().debug(
					"updating doc of uniq title: " + uniq_doc_title);
			this_sd.setUniqueDocTitle(uniq_doc_title);
			this_sd.setDisplayTitle(doc_display_title);
			this_sd.setSim_id(sim_id);
			this_sd.setBigString(doc_starter_text);
			this_sd.saveMe(schema);

		}

		return this_sd;

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

		String db_org = (String) request.getParameter("db_org");
		String db_notes = (String) request.getParameter("db_notes");

		String admin_first = (String) request.getParameter("admin_first");
		String admin_middle = (String) request.getParameter("admin_middle");
		String admin_last = (String) request.getParameter("admin_last");

		String admin_full = USIP_OSP_Util.constructName(admin_first,
				admin_middle, admin_last);

		String admin_pass = (String) request.getParameter("admin_pass");
		String admin_email = (String) request.getParameter("admin_email");

		String email_smtp = (String) request.getParameter("email_smtp");
		String email_user = (String) request.getParameter("email_user");
		String email_pass = (String) request.getParameter("email_pass");
		String email_user_address = (String) request
				.getParameter("email_user_address");
		String email_server_number = (String) request
				.getParameter("email_server_number");

		String email_status = checkEmailStatus(email_smtp, email_user,
				email_pass, email_user_address);

		String ps = MultiSchemaHibernateUtil.principalschema;

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

		// Fill SIO
		SchemaInformationObject sio = new SchemaInformationObject();
		sio.setSchema_name(db_schema);
		sio.setSchema_organization(db_org);

		sio.setNotes(db_notes);
		sio.setEmail_smtp(email_smtp);
		sio.setSmtp_auth_user(email_user);
		sio.setSmtp_auth_password(email_pass);
		sio.setEmail_archive_address(email_user_address);
		sio.setEmailState(email_status);
		sio.setEmailServerNumber(new Long(email_server_number));
		Logger.getRootLogger().debug(sio.toString());

		if (!(MultiSchemaHibernateUtil.testConn())) {
			return ("<BR> Failed to create database connection to the database "
					+ db_schema + ".");
		}

		// Store SIO if schema object of this name already exist, return
		// warning.

		try {
			MultiSchemaHibernateUtil.beginTransaction(ps, true);
			MultiSchemaHibernateUtil.getSession(ps, true).saveOrUpdate(sio);
			MultiSchemaHibernateUtil.commitAndCloseTransaction(ps);
		} catch (Exception e) {

			e.printStackTrace();

			return ("Warning. Unable to create the database entry for this schema. <br />"
					+ "This may indicate that it already has been created.");
		}

		MultiSchemaHibernateUtil.recreateDatabase(sio);

		// Must create the new user in this schema
		@SuppressWarnings("unused")
		User user = new User(schema, admin_email, admin_pass, admin_first,
				admin_last, admin_middle, admin_full, admin_email, true, true,
				true);

		String loadss = (String) request.getParameter("loadss");

		if ((loadss != null) && (loadss.equalsIgnoreCase("true"))) {
			BaseSimSection.readBaseSimSectionsFromXMLFiles(schema);
		}

		// ///////////////////////////////////////////
		// Test email functionality if SMTP information has been entered.
		String email_msg = "";

		if (sio.checkReqEmailInfoAndMaybeMarkDown()) {

			String message = "This email is coming from your newly installed OSP Installation. Live long and prosper.";

			Vector ccs = new Vector();
			Vector bccs = new Vector();

			if (sio != null) {
				bccs.add(sio.getEmail_archive_address());
				Emailer.postMail(sio, sio.getEmail_archive_address(),
						"USIP OSP Installation Message", message, sio
								.getEmail_archive_address(), ccs, bccs);
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

		return email_msg;

	}

	/**
	 * Creates or updates a database based on the parameters passed in.
	 * 
	 * 
	 * @param request
	 * @return
	 */
	public static String handleCreateOrUpdateDB(HttpServletRequest request,
			Long adminUserId) {

		String error_msg = "";

		String sending_page = (String) request.getParameter("sending_page");
		String command = (String) request.getParameter("command");

		if ((sending_page == null) || (command == null)) {
			return error_msg;
		}

		if (command.equalsIgnoreCase("Clear")) {
			return error_msg;
		}

		SchemaInformationObject sio = new SchemaInformationObject();
		if (command.equalsIgnoreCase("Update")) {
			String sio_id = (String) request.getParameter("sio_id");
			sio = SchemaInformationObject.getById(new Long(sio_id));
		}

		if ((command.equalsIgnoreCase("Update"))
				|| (command.equalsIgnoreCase("Create"))) {
			String db_schema = (String) request.getParameter("db_schema");
			String db_org = (String) request.getParameter("db_org");
			String db_notes = (String) request.getParameter("db_notes");
			String email_smtp = (String) request.getParameter("email_smtp");
			String email_user = (String) request.getParameter("email_user");
			String email_pass = (String) request.getParameter("email_pass");
			String email_user_address = (String) request
					.getParameter("email_user_address");
			String email_server_number = (String) request
					.getParameter("email_server_number");
			String email_status = checkEmailStatus(email_smtp, email_user,
					email_pass, email_user_address);

			// Fill SIO
			sio.setSchema_name(db_schema);
			sio.setSchema_organization(db_org);
			sio.setNotes(db_notes);
			sio.setEmail_smtp(email_smtp);
			sio.setSmtp_auth_user(email_user);
			sio.setSmtp_auth_password(email_pass);
			sio.setEmail_archive_address(email_user_address);
			sio.setEmailState(email_status);
			sio.setEmailServerNumber(new Long(email_server_number));
			Logger.getRootLogger().debug(sio.toString());

			String ps = MultiSchemaHibernateUtil.principalschema;

			if (!(MultiSchemaHibernateUtil.testConn())) {
				error_msg += "<BR> Failed to create database connection";
				return error_msg;
			}

			// Store SIO. If a schema object with the same name already exist,
			// an error will be returned.

			try {
				MultiSchemaHibernateUtil.beginTransaction(ps, true);
				MultiSchemaHibernateUtil.getSession(ps, true).saveOrUpdate(sio);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(ps);
			} catch (Exception e) {

				error_msg = "Warning. Unable to create the database entry for this schema. <br />"
						+ "This may indicate that it already has been created.";

				e.printStackTrace();

				return error_msg;
			}

			// Only if we are creating a new Schema Information Object will we
			// recreate the database.
			if (command.equalsIgnoreCase("Create")) {
				MultiSchemaHibernateUtil.recreateDatabase(sio);

				String loadss = (String) request.getParameter("loadss");

				if ((loadss != null) && (loadss.equalsIgnoreCase("true"))) {
					BaseSimSection.readBaseSimSectionsFromXMLFiles(db_schema);
				}
			}

			BaseUser bu = BaseUser.getByUserId(adminUserId);

			// Create the admin in this schema
			@SuppressWarnings("unused")
			User user = new User(db_schema, bu, true, true, true);

			error_msg = "database_created";

		}
		return error_msg;

	}

	/**
	 * Verify that all required fields have been entered for the email smtp
	 * server.
	 */
	public static String checkEmailStatus(String email_smtp, String email_user,
			String email_pass, String email_user_address) {

		if ((email_smtp == null) || (email_user == null)
				|| (email_pass == null) || (email_user_address == null)) {
			return SchemaInformationObject.EMAIL_STATE_DOWN;
		} else if ((email_smtp.trim().equalsIgnoreCase(""))
				|| (email_user.trim().equalsIgnoreCase(""))
				|| (email_pass.trim().equalsIgnoreCase(""))
				|| (email_user_address.trim().equalsIgnoreCase(""))) {
			return SchemaInformationObject.EMAIL_STATE_DOWN;
		} else {
			return SchemaInformationObject.EMAIL_STATE_UNVERIFIED;
		}
	}

	/**
	 * Recreates the root database that will hold information on the other
	 * schemas and user information.
	 * 
	 * @param request
	 */
	public String handleCreateRootDB(HttpServletRequest request) {

		String returnMsg = "";

		String sending_page = request.getParameter("sending_page");
		String wipe_database_key = request.getParameter("wipe_database_key");

		boolean clearedToWipeDB = false;

		if ((wipe_database_key != null)
				&& (wipe_database_key.equals(USIP_OSP_Properties
						.getValue("wipe_database_key")))) {
			clearedToWipeDB = true;
		}

		if (clearedToWipeDB && (sending_page != null)
				&& (sending_page.equalsIgnoreCase("install_root_db"))) {

			MultiSchemaHibernateUtil.recreateRootDatabase();
			returnMsg = "Root schema should now contain empty tables.";

			// Entering the correct key is equivalent to having logged in.
			this.loggedin = true;

			// If the user/developer has been editing simulations, remove
			// reference to old simulations.
			this.sim_id = null;

			this.forward_on = true;
			this.backPage = "install_db.jsp";

			return returnMsg;

		} else if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("install_root_db"))) {
			returnMsg = "Wrong key entered.";
		}

		return returnMsg;

	}

	public static final int INSTALL_ERROR_NO_PROP = 1;
	public static final int INSTALL_ERROR_NO_CONN = 2;

	public static int checkInstall(HttpServletRequest request) {

		if (!(USIP_OSP_Properties.isFoundPropertiesFile())) {
			return INSTALL_ERROR_NO_PROP;
		}

		if (!MultiSchemaHibernateUtil.testConn()) {
			return INSTALL_ERROR_NO_CONN;
		}

		return 0;
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
		
		ArrayList <Long> targettedPeople = new ArrayList();
		
		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
			String pname = (String) e.nextElement();
			String vname = (String) request.getParameter(pname);
			if (pname.startsWith("target_")){
				pname = pname.replace("target_", "");
				
				if (vname.equalsIgnoreCase("on"));
				
				Long thisTarget = USIP_OSP_Util.stringToLong(pname);
				if (thisTarget != null){
					targettedPeople.add(thisTarget);
				}
			}
		}

		// Do create if called.
		String command = (String) request.getParameter("command");
		if (command != null) {

			if (command.equalsIgnoreCase("Clear")) { //$NON-NLS-1$
				return inject;
			} else if (command.equalsIgnoreCase("Create")) { //$NON-NLS-1$
				inject.setInject_name(inject_name);
				inject.setInject_text(inject_text);
				inject.setInject_Notes(inject_notes);
				inject.setSim_id(sim_id);
				inject.setGroup_id(new Long(ig_id));
				inject.saveMe(schema);
				
				addInjectDefaultRecipients(schema, targettedPeople, inject, false);
				
			} else if (command.equalsIgnoreCase("Update")) {
				inject = Inject.getById(schema, new Long(inj_id));
				inject.setInject_name(inject_name);
				inject.setInject_text(inject_text);
				inject.setInject_Notes(inject_notes);
				inject.setGroup_id(new Long(ig_id));
				inject.saveMe(schema);
				
				addInjectDefaultRecipients(schema, targettedPeople, inject, true);

			}

			Simulation.updateSimsLastEditDate(sim_id, schema);
		}

		return inject;

	}
	
	public static void addInjectDefaultRecipients(String schema, List peopleToAdd, Inject inject, boolean cleanOld){
		
		if (cleanOld){
			InjectActorAssignments.removeAllForInject(schema, inject.getId());
		}
		
		for (ListIterator <Long> li = peopleToAdd.listIterator(); li.hasNext();) {
			
			Long this_iaa = li.next();
			
			InjectActorAssignments iaa = new InjectActorAssignments(schema, this_iaa, inject.getId());
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
	 * starting phase and the completed phase, get added. 3.) The control
	 * character is created (if necessary) and added to the simulation.
	 * 
	 * @param request
	 */
	public Simulation handleCreateOrUpdateNewSim(HttpServletRequest request) {

		Simulation simulation = new Simulation();

		String command = (String) request.getParameter("command");
		String simulation_name = (String) request
				.getParameter("simulation_name");
		String simulation_version = (String) request
				.getParameter("simulation_version");

		String creation_org = (String) request.getParameter("creation_org");
		String simcreator = (String) request.getParameter("simcreator");
		String simcopyright = (String) request.getParameter("simcopyright");

		String simblurb = (String) request.getParameter("simblurb");

		if (command != null) {
			if (command.equalsIgnoreCase("Create")) {

				simulation.setName(simulation_name);
				simulation.setVersion(simulation_version);
				simulation
						.setSoftware_version(USIP_OSP_Properties.getRelease());
				simulation.setCreation_org(creation_org);
				simulation.setCreator(simcreator);
				simulation.setCopyright_string(simcopyright);
				simulation.setBlurb(simblurb);

				simulation.createDefaultObjects(schema);

				simulation.saveMe(schema);
				this.phase_id = simulation.getFirstPhaseId(schema);

			} else if (command.equalsIgnoreCase("Update")) { // 
				String sim_id = (String) request.getParameter("sim_id");
				simulation = Simulation.getById(schema, new Long(sim_id));
				simulation.setName(simulation_name);
				simulation.setVersion(simulation_version);
				simulation
						.setSoftware_version(USIP_OSP_Properties.getRelease());
				simulation.setCreation_org(creation_org);
				// simulation.setCreator(simcreator);
				simulation.setCopyright_string(simcopyright);
				simulation.setBlurb(simblurb);

				simulation.saveMe(schema);
			} else if (command.equalsIgnoreCase("Edit")) {
				String sim_id = (String) request.getParameter("sim_id");
				simulation = Simulation.getById(schema, new Long(sim_id));
				// Clean up any items that could be queued up for editing
				actor_being_worked_on_id = null;

			} else if (command.equalsIgnoreCase("Clear")) { // 
				// returning new simulation will clear fields.
			}
		}

		this.sim_id = simulation.getId();

		saveSimEdited();

		return simulation;
	}

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

		String actorid = "";

		try {
			MultipartRequest mpr = new MultipartRequest(request,
					USIP_OSP_Properties.getValue("uploads"));

			String update_actor = (String) mpr.getParameter("update_actor");

			actorid = (String) mpr.getParameter("actorid");

			String clear_button = (String) mpr.getParameter("clear_button");

			String create_actor = (String) mpr.getParameter("create_actor");

			if ((update_actor != null)
					&& (update_actor.equalsIgnoreCase("Update Actor"))) {

				actor_being_worked_on_id = new Long((String) mpr
						.getParameter("actorid"));

				Actor actorOnScratchPad = Actor.getById(schema,
						actor_being_worked_on_id);

				createActor(mpr, actorOnScratchPad);

			} else if ((create_actor != null)
					&& (create_actor.equalsIgnoreCase("Create Actor"))) {
				Actor newActor = new Actor();
				newActor.setImageFilename("no_image_default.jpg");
				createActor(mpr, newActor);

			} else if ((clear_button != null)
					&& (clear_button.equalsIgnoreCase("Clear"))) {
				actor_being_worked_on_id = null;
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
	 * Creates an actor.
	 * 
	 * @param mpr
	 * @param actorOnScratchPad
	 */
	public void createActor(MultipartRequest mpr, Actor actorOnScratchPad) {

		try {

			boolean saveActor = false;
			String create_actor = (String) mpr.getParameter("create_actor");
			String update_actor = (String) mpr.getParameter("update_actor");

			String MAX_FILE_SIZE = (String) mpr.getParameter("MAX_FILE_SIZE");

			Long max_file_longvalue = new Long(MAX_FILE_SIZE).longValue();

			Logger.getRootLogger().debug("create_actor is " + create_actor);
			Logger.getRootLogger().debug("update_actor is " + update_actor);

			if ((create_actor != null)
					&& (create_actor.equalsIgnoreCase("Create Actor"))
					|| (update_actor != null)
					&& (update_actor.equalsIgnoreCase("Update Actor"))

			) {
				saveActor = true;
			}

			if (saveActor) {
				Logger.getRootLogger().debug("saving actor");
				makeUploadDir();

				String _sim_id = (String) mpr.getParameter("sim_id");
				Simulation sim = Simulation.getById(schema, sim_id);
				sim.updateLastEditDate(schema);

				actorOnScratchPad.setSim_id(new Long(_sim_id));

				actorOnScratchPad.setPublic_description((String) mpr
						.getParameter("public_description"));
				actorOnScratchPad.setName((String) mpr
						.getParameter("actor_name"));
				actorOnScratchPad.setSemi_public_description((String) mpr
						.getParameter("semi_public_description"));
				actorOnScratchPad.setPrivate_description((String) mpr
						.getParameter("private_description"));

				String control_actor = (String) mpr
						.getParameter("control_actor");

				if ((control_actor != null)
						&& (control_actor.equalsIgnoreCase("true"))) {

					actorOnScratchPad.setControl_actor(true);
					if (this.sim_id != null) {

						MultiSchemaHibernateUtil.beginTransaction(schema);
						Logger.getRootLogger().debug(
								"actors id is" + actorOnScratchPad.getId());
						MultiSchemaHibernateUtil.getSession(schema)
								.saveOrUpdate(actorOnScratchPad);
						MultiSchemaHibernateUtil.getSession(schema).flush();
						MultiSchemaHibernateUtil
								.commitAndCloseTransaction(schema);

						sim.addControlSectionsToAllPhasesOfControl(this.schema,
								actorOnScratchPad);
					}

				} else {
					actorOnScratchPad.setControl_actor(false);
				}

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
						FileIO.saveImageFile(OSPSimMedia.ACTOR_IMAGE, actorOnScratchPad
								.getImageFilename(), mpr
								.getFile("uploadedfile"));
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
						FileIO.saveImageFile(OSPSimMedia.ACTOR_IMAGE, actorOnScratchPad
								.getImageThumbFilename(), mpr
								.getFile("uploaded_thumb_file"));
					} else {
						this.errorMsg += "Selected thumbnail image file too large.";
						actorOnScratchPad
								.setImageThumbFilename("no_image_default.jpg");
					}

				}

				// ////////////////////////////////////////////

				MultiSchemaHibernateUtil.beginTransaction(schema);

				Logger.getRootLogger().debug(
						"actors id is" + actorOnScratchPad.getId());
				MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(
						actorOnScratchPad);
				MultiSchemaHibernateUtil.getSession(schema).flush();

				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
				String add_to_sim = (String) mpr.getParameter("add_to_sim");

				if ((add_to_sim != null)
						&& (add_to_sim.equalsIgnoreCase("true"))) {

					String actors_role = (String) mpr
							.getParameter("actors_role");
					String chat_color = (String) mpr.getParameter("chat_color");

					SimActorAssignment saa;

					// Don't add actor to sim, if he or she has already been
					// added.
					boolean simHasActor = false;
					for (ListIterator<Actor> li = SimActorAssignment
							.getActorsForSim(schema, sim_id).listIterator(); li
							.hasNext();) {
						Actor act = li.next();

						if (act.getId().equals(actorOnScratchPad.getId())) {
							simHasActor = true;
						}
					}

					// if (!(SimActorAssignment.getActorsForSim(schema,
					// sim_id).contains(actorOnScratchPad))) {
					if (!(simHasActor)) {
						saa = new SimActorAssignment(schema, sim_id,
								actorOnScratchPad.getId());
					} else {
						saa = SimActorAssignment.getById(schema, sim_id,
								actorOnScratchPad.getId());
					}

					saa.setActors_role(actors_role);
					saa.setActors_chat_color(chat_color);

					saa.saveMe(schema);

					SimulationSectionAssignment.applyAllUniversalSections(
							schema, sim_id);

				}

				this.actor_name = actorOnScratchPad.getActorName();
				this.actor_being_worked_on_id = actorOnScratchPad.getId();

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

	public void makeUploadDir() {

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

	/**
	 * Returns the simulation based on what sim_id is currently stored in this
	 * AuthorFacilitatorSessionObject.
	 * 
	 * @return
	 */
	public Simulation giveMeSim() {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		Simulation simulation = (Simulation) MultiSchemaHibernateUtil
				.getSession(schema).get(Simulation.class, sim_id);

		MultiSchemaHibernateUtil.getSession(schema).evict(simulation);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		return simulation;

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
	public void addActorToSim(String sim_id, String actor_id) {

		Long s_id = new Long(sim_id);
		Long a_id = new Long(actor_id);

		Actor this_act = Actor.getById(schema, a_id);

		if (!(this_act.getSim_id().equals(s_id))) {

			this_act = Actor.cloneMe(schema, a_id);
			this_act.setSim_id(s_id);
			this_act.saveMe(schema);

		}

		@SuppressWarnings("unused")
		SimActorAssignment saa = new SimActorAssignment(schema, s_id, this_act
				.getId());

		SimulationSectionAssignment.applyAllUniversalSections(schema, s_id);

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

	public boolean isAdmin() {
		return isAdmin;
	}

	public boolean isAuthor() {
		return isSimAuthor;
	}

	public boolean isFacilitator() {
		return isFacilitator;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleMakeCustomizedSection(
			HttpServletRequest request) {

		String custom_page = request.getParameter("custom_page");

		MultiSchemaHibernateUtil.beginTransaction(schema);
		CustomizeableSection cs = (CustomizeableSection) MultiSchemaHibernateUtil
				.getSession(schema).get(CustomizeableSection.class,
						new Long(custom_page));
		// MultiSchemaHibernateUtil.getSession(schema).evict(cs);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		// Check to see if this is already a customized copy
		if (!(cs.isThisIsACustomizedSection())) {
			cs = cs.makeCopy(schema, sim_id);
		}

		cs.setRec_tab_heading(request.getParameter("tab_heading"));

		if (cs.getContents() == null) {
			cs.setContents(new Hashtable());
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(cs);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return cs;

	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleMakePlayerDiscreteChoice(
			HttpServletRequest request) {

		return (getMyPSO_SectionMgmt().handleMakePlayerDiscreteChoice(request));

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

	SimpleDateFormat savedFilesDateFormat = new SimpleDateFormat("ddMMMyyyy");

	/**
	 * Puts the name of the sim and the date into a string to use for xml export
	 * file name.
	 */
	public String getDefaultSimXMLFileName(Simulation simulation) {

		Date saveDate = new java.util.Date();

		String fileName = simulation.getSimulationName() + "_"
				+ simulation.getVersion() + "_"
				+ savedFilesDateFormat.format(saveDate);

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

	/**
	 * Turns the simulation into an xml representation.
	 * 
	 * @return
	 */
	public String handlePackageSim(String _sim_id, String fileName) {

		FileIO.saveSimulationXMLFile(ObjectPackager.packageSimulation(schema,
				new Long(_sim_id)), fileName);

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
	public void handlePublishing(String command, String sim_key_words,
			String auto_registration) {

		if (command == null) {
			return;
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Simulation sim = (Simulation) MultiSchemaHibernateUtil.getSession(
				schema).get(Simulation.class, sim_id);

		if (command.equalsIgnoreCase("Publish It!")) {
			sim.setReadyForPublicListing(true);
			sim.setListingKeyWords(sim_key_words);
		}

		else if (command.equalsIgnoreCase("Un - Publish It!")) {
			sim.setReadyForPublicListing(false);
			sim.setListingKeyWords(sim_key_words);
		}

		if ((auto_registration != null)
				&& (auto_registration.equalsIgnoreCase("true"))) {
			sim.setAllow_player_autoreg(true);
		} else {
			sim.setAllow_player_autoreg(false);
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}

	public static final int AUTHOR_LOGIN = 1;

	public static final int FACILITATOR_LOGIN = 2;

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

			String schema_id = (String) request.getParameter("schema_id");

			SchemaInformationObject sio = SchemaInformationObject
					.getById(new Long(schema_id));

			afso.schema = sio.getSchema_name();
			afso.schemaOrg = sio.getSchema_organization();

			OSPSessionObjectHelper osp_soh = (OSPSessionObjectHelper) request
					.getSession(true).getAttribute("osp_soh");

			User user = User.getById(afso.schema, osp_soh.getUserid());
			BaseUser bu = BaseUser.getByUserId(osp_soh.getUserid());

			if (user != null) {
				afso.user_id = user.getId();
				afso.isAdmin = user.isAdmin();
				afso.isSimAuthor = user.isSim_author();
				afso.isFacilitator = user.isSim_instructor();

				afso.user_Display_Name = bu.getFull_name();
				afso.user_email = bu.getUsername();
				afso.user_name = afso.user_email;

				afso.loggedin = true;

				user.setLastLogin(new Date());
				user.saveJustUser(afso.schema);

				sio.setLastLogin(new Date());
				sio.saveMe();

			} else {
				afso.loggedin = false;
				Logger
						.getRootLogger()
						.warn(
								"handling initial entry into simulation and got null user");
			}
		}
	}

	/**
	 * Sends password to user via email upon request.
	 * 
	 * @param request
	 * @return
	 */
	public static boolean handleSendResetPasswordEmail(HttpServletRequest request) {

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
		
		String reset_url = USIP_OSP_Properties.getCachedValue("base_sim_url") + 
		"simulation_user_admin/reset_password.jsp?rpo=" + rpo.getId() + "&rnd=" + rpo.getTextRepresentation();
			
			

		String message = "Dear USIP OSP Friend,<br/><br/>A request to change your password has been submitted.<br />";
		
		message += "Follow this link to the <A HREF=\"" + reset_url + "\"> reset password page </A>, ";
		message += "or just copy and paste this link " + reset_url + " into your web browser.";

		System.out.println(message);
		
		Vector ccs = new Vector();
		Vector bccs = new Vector();


		try {
			SchemaInformationObject sio = SchemaInformationObject
					.getFirstUpEmailServer();

			if (sio != null) {
				bccs.add(sio.getEmail_archive_address());
				Emailer.postMail(sio, email, "Access to OSP", message,
						"noreply@opensimplatform.org", ccs, bccs);
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
	public User handleCreateAdminUser(HttpServletRequest request) {

		User user = new User();

		if ((!this.isAdmin) || (!this.isSimAuthor)) {
			errorMsg = "Not authorized to create administrative users.";
			return user;
		} else {
			OSP_UserAdmin pu = new OSP_UserAdmin(this);
			return pu.handleCreateAdminUser(request, schema);
		}
	}

	/**
	 * Called from process_custom_page.jsp
	 * 
	 * @param request
	 */
	public void handleProcessCustomPage(HttpServletRequest request) {
		// This is what adds it to the base sim section list.
		CustomizeableSection cs = handleMakeCustomizedSection(request);

		String tab_heading = (String) request.getParameter("tab_heading");
		String tab_pos = (String) request.getParameter("tab_pos");
		String universal = (String) request.getParameter("universal");

		Logger.getRootLogger().debug("pcp - universal was : " + universal);
		Logger.getRootLogger().debug("tab_heading : " + tab_heading);

		addSectionFromProcessCustomPage(cs.getId(), tab_pos, tab_heading,
				request, universal);
	}

	public User handleAutoRegistration(HttpServletRequest request) {
		OSP_UserAdmin pu = new OSP_UserAdmin(this);
		return pu.handleAutoRegistration(request);
	}

	public User handleCreateUser(HttpServletRequest request) {
		OSP_UserAdmin pu = new OSP_UserAdmin(this);
		return pu.handleCreateUser(request, schema);
	}

	public void handleMyProfile(HttpServletRequest request) {
		OSP_UserAdmin pu = new OSP_UserAdmin(this);
		pu.handleMyProfile(request, user_id);
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
	 * A wrapper that passes the request through to the associated
	 * PSO_SectionMgmt object.
	 * 
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleMakeImagePage(HttpServletRequest request) {
		return (getMyPSO_SectionMgmt().handleMakeImagePage(request));
	}

	/**
	 * A wrapper that passes the request through to the associated
	 * PSO_SectionMgmt object.
	 * 
	 * @param request
	 */
	public CustomizeableSection handleMakeReadDocumentPage(
			HttpServletRequest request) {
		return (getMyPSO_SectionMgmt().handleMakeReadDocumentPage(request));
	}

	/**
	 * A wrapper that passes the request through to the associated
	 * PSO_SectionMgmt object.
	 * 
	 * @param request
	 */
	public CustomizeableSection handleMakeMemosPage(HttpServletRequest request) {
		return (getMyPSO_SectionMgmt().handleMakeMemosPage(request));
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
	 * A wrapper that passes the request through to the associated
	 * PSO_SectionMgmt object.
	 * 
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleMakeWriteDocumentPage(
			HttpServletRequest request) {

		return (getMyPSO_SectionMgmt().handleMakeWriteDocumentPage(request));
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
	public CustomizeableSection handleMakeSetParameter(
			HttpServletRequest request) {
		return (getMyPSO_SectionMgmt().handleMakeSetParameter(request));
	}

	/**
	 * 
	 * @param request
	 * @param simulation
	 */
	public void handleAddRunningSimulation(HttpServletRequest request,
			Simulation simulation) {

		String sending_page = (String) request.getParameter("sending_page");
		String addRunningSimulation = (String) request
				.getParameter("addRunningSimulation");

		if ((sending_page != null) && (addRunningSimulation != null)
				&& (sending_page.equalsIgnoreCase("create_running_sim"))) {

			String rsn = (String) request.getParameter("running_sim_name");
			RunningSimulation rs = simulation.addNewRunningSimulation(rsn,
					schema, this.user_id, this.user_Display_Name);

			runningSimId = rs.getId();

		} // End of if coming from this page and have added running simulation
	}

	public Simulation handleCreateSchedulePage(HttpServletRequest request) {
		return (getMyPSO_SectionMgmt().handleCreateSchedulePage(request));
	}

	/**
	 * A wrapper that passes the request through to the associated
	 * PSO_SectionMgmt object.
	 * 
	 * @param request
	 */
	public CustomizeableSection handleMakePrivateChatPage(
			HttpServletRequest request) {
		return (getMyPSO_SectionMgmt().handleMakePrivateChatPage(request));
	}

	/**
	 * A wrapper that passes the request through to the associated
	 * PSO_SectionMgmt object.
	 * 
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleMakeReflectionPage(
			HttpServletRequest request) {

		return (getMyPSO_SectionMgmt().handleMakeReflectionPage(request));
	}

	public void addSectionFromProcessCustomPage(Long bss_id,
			String string_tab_pos, String tab_heading,
			HttpServletRequest request, String universal) {

		getMyPSO_SectionMgmt().addSectionFromProcessCustomPage(bss_id,
				string_tab_pos, tab_heading, request, universal);
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

	public boolean handleResetWebCache(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null) && (sending_page.equalsIgnoreCase("reset"))) {
			USIP_OSP_ContextListener.resetWebCache(request);
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

			SimulationSectionAssignment ssa = SimulationSectionAssignment
					.getById(schema, new Long(ss_id));

			if (ssa != null) {
				ssa.setTabColor(new_color);
				ssa.save(schema);
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

			System.out.println(sdanao);
			if (sdanao.equalsIgnoreCase("create_null")) {

				System.out.println("actor_being_worked_on_id"
						+ actor_being_worked_on_id);

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
					
					SharedDocActorNotificAssignObj sdanao_edited = 
						SharedDocActorNotificAssignObj.getById(schema, new Long(sdanao));
					
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

	/** An event that is being worked on. */
	public Long draft_event_id;

	/**
	 * Handles the creation of timeline events.
	 * 
	 * @param request
	 * @return
	 */
	public Event handleTimeLineCreator(HttpServletRequest request) {

		TimeLine timeline = TimeLine.getMasterPlan(schema, sim_id.toString());

		Event event = new Event();

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("timeline_creator"))) {

			String command = (String) request.getParameter("command");

			if (command.equalsIgnoreCase("Update")) {
				String event_id = (String) request.getParameter("event_id");

				event.setId(new Long(event_id));
				draft_event_id = event.getId();

			}

			if (command.equalsIgnoreCase("Clear")) {
				draft_event_id = null;
			} else { // coming here as update or as create.

				String event_type = (String) request.getParameter("event_type");

				int eventTypeInt = 1;

				try {
					eventTypeInt = new Long(event_type).intValue();
				} catch (Exception e) {
					e.printStackTrace();
				}

				event.setEventType(eventTypeInt);
				event.setEventTitle((String) request
						.getParameter("event_title"));
				event.setEventMsgBody((String) request
						.getParameter("event_text"));

				String event_hour = (String) request.getParameter("event_hour");
				String event_minute = (String) request
						.getParameter("event_minute");

				int event_hour_int = 0;
				int event_minute_int = 0;

				try {
					event_hour_int = new Long(event_hour).intValue();
					event_minute_int = new Long(event_minute).intValue();

				} catch (Exception e) {
					e.printStackTrace();
				}

				// For now arbitrarily set date to 1/1/2001.
				Calendar cal = new GregorianCalendar();
				cal.setTimeZone(TimeZone.getDefault());

				// Year, month, day, hour, minute, second
				cal.set(2001, 0, 1, event_hour_int, event_minute_int, 0);

				event.setEventStartTime(cal.getTime());

				event.setSimId(sim_id);

				// TODO add ability to create multiple timelines
				event.setPhaseId(phase_id);
				event.setPhaseId(new Long(1));

				// //////////////////////////////////////////
				event.setTimelineId(timeline.getId());

				event.saveMe(schema);
			}

		}

		String remove_event = (String) request.getParameter("remove_event");
		String edit_event = (String) request.getParameter("edit_event");

		String event_id = (String) request.getParameter("event_id");

		if ((remove_event != null) && (remove_event.equalsIgnoreCase("true"))) {
			Event.removeMe(schema, new Long(event_id));
			draft_event_id = null;
		}

		if ((edit_event != null) && (edit_event.equalsIgnoreCase("true"))) {
			event = Event.getById(schema, new Long(event_id));
			draft_event_id = event.getId();
		}

		return event;
	}

	public String getMetaPhaseName(HttpServletRequest request, Long metaPhaseId) {

		if (metaPhaseId == null) {
			return "";
		} else {
			return USIP_OSP_Cache.getMetaPhaseNameById(request, schema,
					metaPhaseId);
		}

	}

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

			saveSimEdited();

			this.forward_on = true;

		}

	}

	/**
	 * Saves which sim the user last edited to the database.
	 */
	public void saveSimEdited() {
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
	 * Gets the id of the simulation last edited.
	 * 
	 * @return
	 */
	public Long editedBefore() {

		if (user_id != null) {
			User user = User.getById(schema, user_id);
			return user.getLastSimEdited();
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

			RunningSimSetAssignment.removeAllForRunningSimSet(schema, rssQueued
					.getId());

			for (Enumeration<String> e = request.getParameterNames(); e
					.hasMoreElements();) {
				String pname = (String) e.nextElement();
				String vname = (String) request.getParameter(pname);

				if (pname.startsWith("rsid_")) {
					pname = pname.replaceAll("rsid_", "");

					if ((vname != null) && (vname.equalsIgnoreCase("on"))) {
						System.out.println(pname + " will be added to set.");

						@SuppressWarnings("unused")
						RunningSimSetAssignment rssa = new RunningSimSetAssignment(
								schema, new Long(pname), rssQueued.getId());
					}
				}
			}
		}
		return rssQueued;
	}

} // End of class
