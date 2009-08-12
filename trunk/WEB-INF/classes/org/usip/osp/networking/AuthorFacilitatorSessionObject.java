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
 * This object contains all of the session information for the participant and
 * is the main interface to all of the java objects that the participant will
 * interact with.
 * 
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
 * 
 */
public class AuthorFacilitatorSessionObject {

	/** Determines if actor is logged in. */
	private boolean loggedin = false;

	/** Page to forward the user on to. */
	public boolean forward_on = false;

	/** Schema of the database that the user is working in. */
	public String schema = ""; //$NON-NLS-1$

	/** Organization of the schema that the user is working in. */
	public String schemaOrg = ""; //$NON-NLS-1$

	/** The page to take them back to if needed. */
	public String backPage = "index.jsp"; //$NON-NLS-1$

	/** Id of User that is logged on. */
	public Long user_id;

	/**
	 * Username/ Email address of user that is logged in and using this
	 * ParticipantSessionObject.
	 */
	public String user_name;


	/** Records if user is an admin. */
	private boolean isAdmin = false;

	/** Records if user is authorized to create simulations. */
	private boolean isSimCreator = false;

	/** Records if user is authorized to facilitate simulations. */
	private boolean isFacilitator = false;

	/** Records the display name of this user. */
	public String user_Display_Name = ""; //$NON-NLS-1$

	/** Records the email of this user. */
	public String user_email = ""; //$NON-NLS-1$

	/** Name of simulation being conducted or worked on. */
	public String simulation_name = ""; //$NON-NLS-1$

	/** Version of the simulation be conducted or worked on. */
	public String simulation_version = ""; //$NON-NLS-1$

	/** Organization that created the simulation. */
	public String simulation_org = ""; //$NON-NLS-1$

	/**
	 * Copyright string to display at the bottom of every page in the
	 * simulation.
	 */
	public String sim_copyright_info = ""; //$NON-NLS-1$

	/** ID of Simulation being worked on. */
	public Long sim_id;

	/** ID of the Running Simulation being conducted or worked on. */
	public Long running_sim_id;

	/** Name of the running simulation session. */
	public String run_sim_name = ""; //$NON-NLS-1$

	/** Name of the actor being played or worked on. */
	public String actor_name = ""; //$NON-NLS-1$

	/** ID of Phase being worked on. */
	public Long phase_id;

	/** Indicates if user has selected a phase. */
	public boolean phaseSelected = false;

	/** Name of phase being conducted or worked on. */
	private String phaseName = ""; //$NON-NLS-1$



	/** The Session object. */
	public HttpSession session = null;

	/** Error message to be shown to the user. */
	public String errorMsg = ""; //$NON-NLS-1$

	public List tempSimSecList = new ArrayList();

	/** Login ticket of this user. */
	public LoggedInTicket myLoggedInTicket = new LoggedInTicket();



	/**
	 * Unpacks a simulation from an XML file.
	 * 
	 * @param request
	 */
	public Simulation handleUnpackDetails(HttpServletRequest request) {

		String filename = request.getParameter("filename"); //$NON-NLS-1$

		System.out.println("unpacking " + filename); //$NON-NLS-1$

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

		System.out.println("unpacking " + filename); //$NON-NLS-1$

		ObjectPackager.unpackSim(filename, this.schema, sim_name, sim_version);

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
	public SimulationPhase handleCreateOrUpdatePhase(Simulation sim, HttpServletRequest request) {

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
				SimPhaseAssignment spa = new SimPhaseAssignment(this.schema, sim.getId(), returnSP.getId());

				List<Actor> ctrl_actors = Actor.getControlActors(schema, sim.getId());

				for (ListIterator<Actor> li = ctrl_actors.listIterator(); li.hasNext();) {
					Actor this_act = li.next();
					sim.addControlSectionsToAllPhasesOfControl(this.schema, this_act);
				}

				sim.saveMe(this.schema);
			} else if (command.equalsIgnoreCase("Edit")) { //$NON-NLS-1$
				String sp_id = request.getParameter("sp_id"); //$NON-NLS-1$
				returnSP = SimulationPhase.getMe(this.schema, sp_id);
			} else if (command.equalsIgnoreCase("Update")) { //  //$NON-NLS-1$
				String sp_id = request.getParameter("sp_id"); //$NON-NLS-1$
				returnSP = SimulationPhase.getMe(this.schema, sp_id);
				returnSP.setName(phase_name);
				returnSP.setNotes(phase_notes);
				returnSP.setOrder(string2Int(nominal_order));
				returnSP.saveMe(this.schema);
			} else if (command.equalsIgnoreCase("Clear")) { //  //$NON-NLS-1$
				// returning new simulation phase will clear fields.
			}
		}
		return returnSP;
	}

	public int string2Int(String input) {

		int returnX = 0;

		try {
			returnX = new Integer(input).intValue();
		} catch (Exception e) {
			// Let it slide
		}

		return returnX;
	}

	/** Assigns a user to a simulation. */
	public void handleAssignUser(HttpServletRequest request) {

		String command = request.getParameter("command"); //$NON-NLS-1$

		if (command != null) {
			if ((command.equalsIgnoreCase("Assign User"))) { //$NON-NLS-1$

				String user_id = request.getParameter("user_to_add_to_simulation"); //$NON-NLS-1$
				String actor_id = request.getParameter("actor_to_add_to_simulation"); //$NON-NLS-1$
				String sim_id = request.getParameter("simulation_adding_to"); //$NON-NLS-1$
				String running_sim_id = request.getParameter("running_simulation_adding_to"); //$NON-NLS-1$

				UserAssignment ua = UserAssignment.getUniqueUserAssignment(this.schema, new Long(sim_id), new Long(
						running_sim_id), new Long(actor_id), new Long(user_id));
			}
		}
	}

	public String setOfUsers = ""; //$NON-NLS-1$
	public String invitationCode = ""; //$NON-NLS-1$

	public String getDefaultInviteMessage() {
		String defaultInviteEmailMsg = "Dear Student,\r\n"; //$NON-NLS-1$
		defaultInviteEmailMsg += "Please go to the web site "; //$NON-NLS-1$
		defaultInviteEmailMsg += USIP_OSP_Properties.getValue("simulation_url") //$NON-NLS-1$
				+ "/simulation_user_admin/auto_registration_form.jsp and register yourself.\r\n\r\n"; //$NON-NLS-1$
		defaultInviteEmailMsg += "Thank you,\r\n"; //$NON-NLS-1$
		defaultInviteEmailMsg += this.user_Display_Name;

		return defaultInviteEmailMsg;

	}

	/**
	 * 
	 * @param request
	 */
	public void handleBulkInvite(HttpServletRequest request) {
		this.setOfUsers = request.getParameter("setOfUsers"); //$NON-NLS-1$
		String thisInviteEmailMsg = request.getParameter("defaultInviteEmailMsg"); //$NON-NLS-1$
		this.invitationCode = request.getParameter("invitationCode"); //$NON-NLS-1$

		Long schema_id = SchemaInformationObject.lookUpId(this.schema);

		for (ListIterator<String> li = getSetOfEmails(this.setOfUsers).listIterator(); li.hasNext();) {
			String this_email = li.next();

			if (BaseUser.checkIfUserExists(this_email)) {
				System.out.println("exists:" + this_email);
				// ?? make sure exists in this schema

			} else {

				System.out.println("does not exist:" + this_email);

				// Add entry into system to all them to register.
				UserRegistrationInvite uri = new UserRegistrationInvite(this.user_name, this_email,
						this.invitationCode, this.schema);

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
	public void sendBulkInvitationEmail(String the_email, String subject, String message) {

		Vector cced = null;
		Vector bcced = new Vector();
		bcced.add(this.user_name);

		SchemaInformationObject sio = SchemaInformationObject.lookUpSIOByName(this.schema);

		Emailer.postMail(sio, the_email, subject, message, this.user_name, cced, bcced);
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
		String objectInfo = request.getParameter("object_info");
		String objid = request.getParameter("objid");
		String cancel_action = request.getParameter("cancel_action");
		String phase_sim_id = request.getParameter("phase_sim_id");

		String debug = "";

		if (cancel_action != null) {
			return true;
		}

		String deletion_confirm = request.getParameter("deletion_confirm");
		if ((deletion_confirm != null) && (deletion_confirm.equalsIgnoreCase("Submit"))) {

			Long o_id = new Long(objid);

			if (objectType.equalsIgnoreCase("simulation")) {
				MultiSchemaHibernateUtil.beginTransaction(this.schema);
				Simulation sim = (Simulation) MultiSchemaHibernateUtil.getSession(this.schema).get(Simulation.class,
						o_id);
				MultiSchemaHibernateUtil.getSession(this.schema).delete(sim);
				this.sim_id = null;

				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

			} else if (objectType.equalsIgnoreCase("phase")) {
				MultiSchemaHibernateUtil.beginTransaction(this.schema);
				SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil.getSession(this.schema).get(
						SimulationPhase.class, o_id);
				Long phase_removed_id = sp.getId();
				MultiSchemaHibernateUtil.getSession(this.schema).delete(sp);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

				SimPhaseAssignment.removeMe(this.schema, new Long(phase_sim_id), phase_removed_id);

			} else if (objectType.equalsIgnoreCase("actor")) {
				MultiSchemaHibernateUtil.beginTransaction(this.schema);
				Actor act = (Actor) MultiSchemaHibernateUtil.getSession(this.schema).get(Actor.class, o_id);

				if (this.actor_being_worked_on_id != null) {
					if (this.actor_being_worked_on_id.intValue() == act.getId().intValue()) {
						this.actor_being_worked_on_id = null;
					}
				}

				MultiSchemaHibernateUtil.getSession(this.schema).delete(act);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);
			} else if (objectType.equalsIgnoreCase("inject")) {

				MultiSchemaHibernateUtil.beginTransaction(this.schema);
				Inject inject = (Inject) MultiSchemaHibernateUtil.getSession(this.schema).get(Inject.class, o_id);

				MultiSchemaHibernateUtil.getSession(this.schema).delete(inject);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

			} else if (objectType.equalsIgnoreCase("sim_section")) {
				MultiSchemaHibernateUtil.beginTransaction(this.schema);
				SimulationSectionAssignment ss = (SimulationSectionAssignment) MultiSchemaHibernateUtil.getSession(
						this.schema).get(SimulationSectionAssignment.class, o_id);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

				SimulationSectionAssignment.removeAndReorder(this.schema, ss);

			} else if (objectType.equalsIgnoreCase("user_assignment")) {
				MultiSchemaHibernateUtil.beginTransaction(this.schema);
				UserAssignment ua = (UserAssignment) MultiSchemaHibernateUtil.getSession(this.schema).get(
						UserAssignment.class, o_id);
				MultiSchemaHibernateUtil.getSession(this.schema).delete(ua);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);
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
			if ((command.equalsIgnoreCase("Start Simulation"))) {

				String email_users = request.getParameter("email_users");
				String email_text = request.getParameter("email_text");

				BaseUser bu = BaseUser.getByUserId(this.user_id);

				MultiSchemaHibernateUtil.beginTransaction(this.schema);

				User user = (User) MultiSchemaHibernateUtil.getSession(this.schema).get(User.class, this.user_id);

				RunningSimulation running_sim = (RunningSimulation) MultiSchemaHibernateUtil.getSession(this.schema)
						.get(RunningSimulation.class, this.running_sim_id);

				MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

				running_sim.enableAndPrep(this.schema, this.sim_id.toString(), bu.getUsername(), email_users,
						email_text);

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

		SimulationRatings sr = SimulationRatings.getInstructorRatingsBySimAndUser(this.schema, this.sim_id,
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
				System.out.println("Will be loading file from: " + fullfileloc);
				BaseSimSection.readInXMLFile(this.schema, new File(fullfileloc));

			} else if (command.equalsIgnoreCase("Reload")) {
				System.out.println("Will be reloading file from: " + fullfileloc);
				BaseSimSection.reloadXMLFile(this.schema, new File(fullfileloc), new Long(loaded_id));
				// save
			} else if (command.equalsIgnoreCase("Unload")) {
				System.out.println("Will be unloading bss id: " + loaded_id);
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
				System.out.println("Will be loading file from: " + fullfileloc);
				BaseSimSection.readInXMLFile(this.schema, new File(fullfileloc));

			} else if (command.equalsIgnoreCase("Reload")) {
				System.out.println("Will be reloading file from: " + fullfileloc);
				BaseSimSection.reloadXMLFile(this.schema, new File(fullfileloc), new Long(loaded_id));
				// save
			} else if (command.equalsIgnoreCase("Unload")) {
				System.out.println("Will be unloading bss id: " + loaded_id);
				BaseSimSection.removeBSS(this.schema, loaded_id);
			}
		}
	}







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
	public CustomizeableSection handleCustomizeSection(HttpServletRequest request) {

		return (getMyPSO_SectionMgmt().handleCustomizeSection(request));
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
			this_sd = SharedDocument.getMe(schema, new Long(shared_doc_id));
		}

		// If player just entered this page from a different form, just return
		// the blank document
		// (This will also return the doc queued up for editing, if it was
		// selected.)
		String sending_page = (String) request.getParameter("sending_page");
		if ((sending_page == null) || (!(sending_page.equalsIgnoreCase("make_create_document_page")))) {
			return this_sd;
		}

		// If we got down to here, we must be doing some real work on a
		// document.
		String uniq_doc_title = (String) request.getParameter("uniq_doc_title");
		String doc_display_title = (String) request.getParameter("doc_display_title");
		String doc_starter_text = (String) request.getParameter("doc_starter_text");

		// Do create if called.
		String create_doc = (String) request.getParameter("create_doc");
		if ((create_doc != null)) {
			System.out.println("creating doc of uniq title: " + uniq_doc_title);
			this_sd = new SharedDocument(uniq_doc_title, doc_display_title, sim_id);
			this_sd.setBigString(doc_starter_text);
			this_sd.saveMe(schema);

		}

		// Do update if called.
		String update_doc = (String) request.getParameter("update_doc");
		if ((update_doc != null)) {
			System.out.println("updating doc of uniq title: " + uniq_doc_title);
			this_sd.setUniqueDocTitle(uniq_doc_title);
			this_sd.setDisplayTitle(doc_display_title);
			this_sd.setSim_id(sim_id);
			this_sd.setBigString(doc_starter_text);
			this_sd.saveMe(schema);

		}

		return this_sd;

	}

	/**
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

		String db_org = (String) request.getParameter("db_org");
		String db_user = (String) request.getParameter("db_user");
		String db_pass = (String) request.getParameter("db_pass");
		String db_loc = (String) request.getParameter("db_loc");
		String db_port = (String) request.getParameter("db_port");

		String new_admin_user_cbox = (String) request.getParameter("new_admin_user_cbox");
		String admin_first = (String) request.getParameter("admin_first");
		String admin_middle = (String) request.getParameter("admin_middle");
		String admin_last = (String) request.getParameter("admin_last");
		String admin_full = (String) request.getParameter("admin_full");

		String admin_pass = (String) request.getParameter("admin_pass");
		String admin_email = (String) request.getParameter("admin_email");

		String email_smtp = (String) request.getParameter("email_smtp");
		String email_user = (String) request.getParameter("email_user");
		String email_pass = (String) request.getParameter("email_pass");
		String email_user_address = (String) request.getParameter("email_user_address");

		String error_msg = "";
		String ps = MultiSchemaHibernateUtil.principalschema;

		boolean existingAdminUser = true;

		if ((new_admin_user_cbox != null) && (new_admin_user_cbox.equalsIgnoreCase("new"))) {
			existingAdminUser = false;
		}

		Long admin_user_id;
		BaseUser bu = null;

		if ((sending_page != null) && (cleandb != null) && (sending_page.equalsIgnoreCase("clean_db"))) {

			if ((admin_pass == null) || (admin_pass.length() == 0)) {
				return ("Must enter admin password.");
			} else if ((admin_email == null) || ((admin_email.length() == 0))) {
				return ("Must enter admin email.");
			}
		}

		// Fill SIO
		SchemaInformationObject sio = new SchemaInformationObject();
		sio.setSchema_name(db_schema);
		sio.setSchema_organization(db_org);
		sio.setUsername(db_user);
		sio.setUserpass(db_pass);
		sio.setLocation(db_loc);
		sio.setPort(db_port);
		sio.setEmail_smtp(email_smtp);
		sio.setSmtp_auth_user(email_user);
		sio.setSmtp_auth_password(email_pass);
		sio.setEmail_archive_address(email_user_address);
		System.out.println(sio.toString());

		// Test SIO
		String databaseConn = sio.testConn();

		if (!(databaseConn.equalsIgnoreCase("Database Connection Verified"))) {
			error_msg += "<BR>" + databaseConn;
			return error_msg;
		}

		// Store SIO
		MultiSchemaHibernateUtil.beginTransaction(ps, true);
		MultiSchemaHibernateUtil.getSession(ps, true).saveOrUpdate(sio);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(ps);

		// Put it directly in web cache.
		MultiSchemaHibernateUtil.storeAnSIOInHashtables(sio);

		MultiSchemaHibernateUtil.recreateDatabase(sio);

		// Must create the new user in this schema
		User user = new User(schema, admin_email, admin_pass, admin_first, admin_last, admin_middle, admin_full,
				admin_email, true, true, true);

		String loadss = (String) request.getParameter("loadss");
		String load_cs = (String) request.getParameter("load_cs");

		if ((loadss != null) && (loadss.equalsIgnoreCase("true"))) {
			BaseSimSection.readBaseSimSectionsFromXMLFiles(schema);
		}

		error_msg = "You may now login as the root user with the password that you provided.";

		return error_msg;

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
				&& (wipe_database_key.equals(USIP_OSP_Properties.getValue("wipe_database_key")))) {
			clearedToWipeDB = true;
		}

		if (clearedToWipeDB && (sending_page != null) && (sending_page.equalsIgnoreCase("install_root_db"))) {

			MultiSchemaHibernateUtil.recreateRootDatabase();
			returnMsg = "Root schema should now contain empty tables.";

		} else if ((sending_page != null) && (sending_page.equalsIgnoreCase("install_root_db"))) {
			returnMsg = "Wrong key entered.";
		}

		return returnMsg;

	}

	public void handleCreateSimulationSection(HttpServletRequest request) {
		String sending_page = (String) request.getParameter("sending_page");
		String createsection = (String) request.getParameter("createsection");

		if ((sending_page != null) && (createsection != null) && (sending_page.equalsIgnoreCase("create_section"))) {

			BaseSimSection bss = new BaseSimSection(schema, request.getParameter("url"), request
					.getParameter("directory"), request.getParameter("filename"), request
					.getParameter("rec_tab_heading"), request.getParameter("description"));

			String send_rsid_info = (String) request.getParameter("send_rsid_info");
			String send_actor_info = (String) request.getParameter("send_actor_info");
			String send_user_info = (String) request.getParameter("send_user_info");

			System.out.println("rsid / actor /user: " + send_rsid_info + send_actor_info + send_user_info);

			String sendStringWork = "";

			if ((send_rsid_info != null) && (send_rsid_info.equalsIgnoreCase("true"))) {
				sendStringWork = "1";
			} else {
				sendStringWork = "0";
			}

			if ((send_actor_info != null) && (send_actor_info.equalsIgnoreCase("true"))) {
				sendStringWork += "1";
			} else {
				sendStringWork += "0";
			}

			if ((send_user_info != null) && (send_user_info.equalsIgnoreCase("true"))) {
				sendStringWork += "1";
			} else {
				sendStringWork += "0";
			}

			System.out.println("send string was: " + sendStringWork);
			bss.setSendString(sendStringWork);

			bss.saveMe(schema);

		} // End of if coming from this page and have added simulation section.

		String create_defaults = (String) request.getParameter("create_defaults");
		if ((create_defaults != null) && (create_defaults.equalsIgnoreCase("Create Defaults"))) {
			BaseSimSection.readBaseSimSectionsFromXMLFiles(schema);
		}
	}

	/**
	 * Handles the creation of an inject group.
	 * 
	 * @param request
	 */
	public void handleCreateInjectGroup(HttpServletRequest request) {
		String inject_group_name = (String) request.getParameter("inject_group_name");
		String inject_group_description = (String) request.getParameter("inject_group_description");

		InjectGroup ig = new InjectGroup();
		ig.setName(inject_group_name);
		ig.setDescription(inject_group_description);
		ig.setSim_id(sim_id);

		ig.saveMe(schema);

	}

	/**
	 * Handles the creation of Injects.
	 * 
	 * @param request
	 */
	public void handleCreateInject(HttpServletRequest request) {

		String inject_name = (String) request.getParameter("inject_name");
		String inject_text = (String) request.getParameter("inject_text");
		String inject_notes = (String) request.getParameter("inject_notes");
		String inject_group_id = (String) request.getParameter("inject_group_id");

		String edit = (String) request.getParameter("edit");
		String inj_id = (String) request.getParameter("inj_id");

		if ((edit != null) && (edit.equalsIgnoreCase("true"))) {
			MultiSchemaHibernateUtil.beginTransaction(schema);
			Inject inject = (Inject) MultiSchemaHibernateUtil.getSession(schema).get(Inject.class, new Long(inj_id));
			inject.setInject_name(inject_name);
			inject.setInject_text(inject_text);
			inject.setInject_Notes(inject_notes);
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		} else {
			Inject inject = new Inject();
			inject.setInject_name(inject_name);
			inject.setInject_text(inject_text);
			inject.setInject_Notes(inject_notes);
			inject.setSim_id(sim_id);
			inject.setGroup_id(new Long(inject_group_id));
			inject.saveMe(schema);
		}

	}

	public boolean checkDatabaseCreated() {

		List users = null;

		Connection conn = null;

		try {

			String username = USIP_OSP_Properties.getValue("username");
			String password = USIP_OSP_Properties.getValue("password");
			String loc = USIP_OSP_Properties.getValue("loc");
			String port = USIP_OSP_Properties.getValue("port");
			String url = loc + port + "/" + USIP_OSP_Properties.getValue("principalschema") + "?autoReconnect=true";

			String conn_string = MysqlDatabase.makeConnString(url, username, password);

			conn = MysqlDatabase.getConnection(conn_string);
			Statement stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery("select * from users");

			while (rst.next()) {
				users.add(rst.getString(1));
			}

			conn.close();

		} catch (Exception e) {
			System.out.println("Problem getting users");
			return false;
		} finally {
			try {
				conn.close();
			} catch (Exception e1) {
				System.out.println("Could not close connection in pso.");
			}
		}

		boolean returnValue = false;

		if (users == null) {
			returnValue = false;
		} else if (users.size() > 0) {
			returnValue = true;
		}

		return returnValue;
	}



	/**
	 * Should take this opportunity to mark in the user trail that they have
	 * logged out.
	 * 
	 * @param request
	 */
	public static void logout(HttpServletRequest request) {

		System.out.println("TODO: record the user's logout in their trail.");

	}

	/**
	 * 
	 * @param bu_id
	 * @param schema
	 * @param request
	 * @return
	 */
	public User loginToSchema(Long bu_id, String schema, HttpServletRequest request) {

		User user = User.getInfoOnLogin(bu_id, schema);
		BaseUser bu = BaseUser.getByUserId(bu_id);

		if (user != null) {
			this.user_id = user.getId();
			this.isAdmin = user.isAdmin();
			this.isSimCreator = user.isSim_author();
			this.user_Display_Name = bu.getFull_name();

			// TODO
			this.user_email = bu.getUsername();

			myLoggedInTicket.setTrail_id(user.getTrail_id());
			myLoggedInTicket.setUser_id(this.user_id);

			Hashtable<Long, LoggedInTicket> loggedInUsers = (Hashtable<Long, LoggedInTicket>) request.getSession()
					.getServletContext().getAttribute("loggedInUsers");

			loggedInUsers.put(user.getId(), myLoggedInTicket);

			loggedin = true;
		} else {
			loggedin = false;
		}

		return user;

	}



	/**
	 * Returns the AFSO stored in the session, or creates one. The coder can
	 * indicated if he or she wants to start a transaction.
	 */
	public static AuthorFacilitatorSessionObject getAFSO(HttpSession session, boolean getConn) {

		AuthorFacilitatorSessionObject afso = (AuthorFacilitatorSessionObject) session.getAttribute("afso");

		if (afso == null) {
			System.out.println("afso is new");
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
		String simulation_name = (String) request.getParameter("simulation_name");
		String simulation_version = (String) request.getParameter("simulation_version");

		String creation_org = (String) request.getParameter("creation_org");
		String simcreator = (String) request.getParameter("simcreator");
		String simcopyright = (String) request.getParameter("simcopyright");

		String simblurb = (String) request.getParameter("simblurb");

		if (command != null) {
			if (command.equalsIgnoreCase("Create")) {

				simulation.setName(simulation_name);
				simulation.setVersion(simulation_version);
				simulation.setSoftware_version(USIP_OSP_Properties.getRawValue("release"));
				simulation.setCreation_org(creation_org);
				simulation.setCreator(simcreator);
				simulation.setCopyright_string(simcopyright);
				simulation.setBlurb(simblurb);

				simulation.createDefaultObjects(schema);

				simulation.saveMe(schema);
			} else if (command.equalsIgnoreCase("Update")) { // 
				String sim_id = (String) request.getParameter("sim_id");
				simulation = Simulation.getMe(schema, new Long(sim_id));
				simulation.setName(simulation_name);
				simulation.setVersion(simulation_version);
				simulation.setSoftware_version(USIP_OSP_Properties.getRawValue("release"));
				simulation.setCreation_org(creation_org);
				// simulation.setCreator(simcreator);
				simulation.setCopyright_string(simcopyright);
				simulation.setBlurb(simblurb);

				simulation.saveMe(schema);
			} else if (command.equalsIgnoreCase("Edit")) {
				String sim_id = (String) request.getParameter("sim_id");
				simulation = Simulation.getMe(schema, new Long(sim_id));
			} else if (command.equalsIgnoreCase("Clear")) { // 
				// returning new simulation will clear fields.
			}
		}

		this.sim_id = simulation.getId();

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
		boolean inEditMode = false;

		try {
			MultipartRequest mpr = new MultipartRequest(request, USIP_OSP_Properties.getValue("uploads"));

			String update_actor = (String) mpr.getParameter("update_actor");

			actorid = (String) mpr.getParameter("actorid");

			String clear_button = (String) mpr.getParameter("clear_button");

			String create_actor = (String) mpr.getParameter("create_actor");

			if ((update_actor != null) && (update_actor.equalsIgnoreCase("Update Actor"))) {

				actor_being_worked_on_id = new Long((String) mpr.getParameter("actorid"));

				Actor actorOnScratchPad = Actor.getMe(schema, actor_being_worked_on_id);

				createActor(mpr, actorOnScratchPad);

			} else if ((create_actor != null) && (create_actor.equalsIgnoreCase("Create Actor"))) {
				Actor newActor = new Actor();
				newActor.setImageFilename("no_image_default.jpg");
				createActor(mpr, newActor);

			} else if ((clear_button != null) && (clear_button.equalsIgnoreCase("Clear"))) {
				actor_being_worked_on_id = null;
			} else {
				inEditMode = false;
			}
		} catch (java.io.IOException ioe) {
			System.out.println("error in edit actor:" + ioe.getMessage());

			actorid = (String) request.getParameter("actorid");
			if (actorid != null) {
				actor_being_worked_on_id = new Long(actorid);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
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

			System.out.println("create_actor is " + create_actor);
			System.out.println("update_actor is " + update_actor);

			if ((create_actor != null) && (create_actor.equalsIgnoreCase("Create Actor")) || (update_actor != null)
					&& (update_actor.equalsIgnoreCase("Update Actor"))

			) {
				saveActor = true;
			}

			if (saveActor) {
				System.out.println("saving actor");
				makeUploadDir();
				
				String _sim_id = (String) mpr.getParameter("sim_id");
				actorOnScratchPad.setSim_id(new Long(_sim_id));

				actorOnScratchPad.setPublic_description((String) mpr.getParameter("public_description"));
				actorOnScratchPad.setName((String) mpr.getParameter("actor_name"));
				actorOnScratchPad.setSemi_public_description((String) mpr.getParameter("semi_public_description"));
				actorOnScratchPad.setPrivate_description((String) mpr.getParameter("private_description"));

				String control_actor = (String) mpr.getParameter("control_actor");

				if ((control_actor != null) && (control_actor.equalsIgnoreCase("true"))) {
					
					actorOnScratchPad.setControl_actor(true);
					if (this.sim_id != null){
						
						MultiSchemaHibernateUtil.beginTransaction(schema);
						System.out.println("actors id is" + actorOnScratchPad.getId());
						MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(actorOnScratchPad);
						MultiSchemaHibernateUtil.getSession(schema).flush();
						MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
						
						Simulation sim = Simulation.getMe(schema, this.sim_id);
						sim.addControlSectionsToAllPhasesOfControl(this.schema, actorOnScratchPad);
					}
					
				} else {
					actorOnScratchPad.setControl_actor(false);
				}

				// ////////////////////////////////////////////
				// Image portion of save
				String initFileName = mpr.getOriginalFileName("uploadedfile");

				if ((initFileName != null) && (initFileName.trim().length() > 0)) {

					actorOnScratchPad.setImageFilename(mpr.getOriginalFileName("uploadedfile"));

					File fileData = mpr.getFile("uploadedfile");

					System.out.println("File is " + fileData.length());

					if (fileData.length() <= max_file_longvalue) {
						FileIO.saveImageFile("actorImage", actorOnScratchPad.getImageFilename(), mpr
								.getFile("uploadedfile"));
					} else {
						this.errorMsg = "Selected image file too large.";
						actorOnScratchPad.setImageFilename("no_image_default.jpg");
					}

				}

				// ////////////////////////////////////////////
				// Image portion of save
				String initThumbFileName = mpr.getOriginalFileName("uploaded_thumb_file");

				if ((initThumbFileName != null) && (initThumbFileName.trim().length() > 0)) {

					actorOnScratchPad.setImageThumbFilename(mpr.getOriginalFileName("uploaded_thumb_file"));

					File fileData = mpr.getFile("uploaded_thumb_file");

					System.out.println("File is " + fileData.length());

					if (fileData.length() <= max_file_longvalue) {
						FileIO.saveImageFile("actorImage", actorOnScratchPad.getImageThumbFilename(), mpr
								.getFile("uploaded_thumb_file"));
					} else {
						this.errorMsg += "Selected thumbnail image file too large.";
						actorOnScratchPad.setImageThumbFilename("no_image_default.jpg");
					}

				}

				// ////////////////////////////////////////////

				MultiSchemaHibernateUtil.beginTransaction(schema);

				System.out.println("actors id is" + actorOnScratchPad.getId());
				MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(actorOnScratchPad);
				MultiSchemaHibernateUtil.getSession(schema).flush();

				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
				String add_to_sim = (String) mpr.getParameter("add_to_sim");

				if ((add_to_sim != null) && (add_to_sim.equalsIgnoreCase("true"))) {

					String actors_role = (String) mpr.getParameter("actors_role");
					String chat_color = (String) mpr.getParameter("chat_color");

					SimActorAssignment saa;

					if (!(SimActorAssignment.getActorsForSim(schema, sim_id).contains(actorOnScratchPad))) {
						saa = new SimActorAssignment(schema, sim_id, actorOnScratchPad.getId());
					} else {
						saa = SimActorAssignment.getMe(schema, sim_id, actorOnScratchPad.getId());
					}

					saa.setActors_role(actors_role);
					saa.setActors_chat_color(chat_color);

					saa.saveMe(schema);

					SimulationSectionAssignment.applyAllUniversalSections(schema, sim_id);

				}

				this.actor_name = actorOnScratchPad.getName();
				this.actor_being_worked_on_id = actorOnScratchPad.getId();

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("problem in create actor: " + e.getMessage());

			try {
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
			} catch (Exception e_ignored) {
				Logger.getRootLogger().warn("Difficulty in closing connection.");
				Logger.getRootLogger().warn(e_ignored.getMessage());
			}
		}

	}

	public void makeUploadDir() {

		try {
			new File("uploads").mkdir();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("attempt to make dir: " + e.getMessage());
		}

	}

	/**
	 * If a matches b, return the matchText.
	 * 
	 * @param a
	 * @param b
	 * @param matchText
	 * @return
	 */
	public String matchSelected(String a, String b, String matchText) {
		if ((a == null) || (b == null)) {
			return "";
		}

		if (a.equalsIgnoreCase(b)) {
			return matchText;
		} else {
			return "";
		}
	}

	/** Its a work in progress. */
	public String getEvents() {

		String eventText = "";

		return eventText;
	}



	public String getPhaseNameById(Long phase_id) {
		return "insert code here";
	}

	/**
	 * Returns the phase name stored in the web cache.
	 * 
	 * @return
	 */
	public String getPhaseName() {

		Hashtable<Long, String> phaseNames = (Hashtable<Long, String>) session.getServletContext().getAttribute(
				"phaseNames");

		if (running_sim_id != null) {
			phaseName = phaseNames.get(running_sim_id);

			return phaseName;
		} else {
			return "";
		}
	}



	public Simulation giveMeSim() {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		Simulation simulation = (Simulation) MultiSchemaHibernateUtil.getSession(schema).get(Simulation.class, sim_id);

		MultiSchemaHibernateUtil.getSession(schema).evict(simulation);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		return simulation;

	}




	public Actor giveMeActor(Long a_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Actor actor = (Actor) MultiSchemaHibernateUtil.getSession(schema).get(Actor.class, a_id);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (actor == null) {
			actor = new Actor();
		}

		return actor;
	}

	public SimulationPhase giveMePhase() {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		SimulationPhase phase = (SimulationPhase) MultiSchemaHibernateUtil.getSession(schema).get(
				SimulationPhase.class, phase_id);

		MultiSchemaHibernateUtil.getSession(schema).evict(phase);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return phase;
	}

	/**
	 * 
	 * @return
	 */
	public User giveMeUser() {

		return User.getUser(schema, this.user_id);

	}

	/**
	 * 
	 * @param sim_id
	 * @param actor_id
	 */
	public void addActorToSim(String sim_id, String actor_id) {

		Long s_id = new Long(sim_id);
		Long a_id = new Long(actor_id);

		SimActorAssignment saa = new SimActorAssignment(schema, s_id, a_id);

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

	public boolean isSimCreator() {
		return isSimCreator;
	}

	public boolean isAuthor() {
		return isSimCreator;
	}

	public boolean isFacilitator() {
		return isFacilitator;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleMakeCustomizedSection(HttpServletRequest request) {

		String custom_page = request.getParameter("custom_page");

		MultiSchemaHibernateUtil.beginTransaction(schema);
		CustomizeableSection cs = (CustomizeableSection) MultiSchemaHibernateUtil.getSession(schema).get(
				CustomizeableSection.class, new Long(custom_page));
		// MultiSchemaHibernateUtil.getSession(schema).evict(cs);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		// Check to see if this is already a customized copy
		if (!(cs.isThisIsACustomizedSection())) {
			cs = cs.makeCopy(schema);
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
	public CustomizeableSection handleMakePlayerDiscreteChoice(HttpServletRequest request) {

		return (getMyPSO_SectionMgmt().handleMakePlayerDiscreteChoice(request));

	}

	/**
	 * 
	 * @return A hashtable with all of the actor one on one coversations set in
	 *         the form of 1_2 and 2_1.
	 */
	public Hashtable setOfConversationForASection(Long section_id) {

		Hashtable returnTable = new Hashtable<String, String>();

		List currentChats = Conversation.getAllPrivateChatForASection(schema, sim_id);

		// Loop over all private conversations in this set
		for (ListIterator<Conversation> li = currentChats.listIterator(); li.hasNext();) {
			Conversation con_id = li.next();

			Vector actors = new Vector();

			MultiSchemaHibernateUtil.beginTransaction(schema);
			Conversation conv = (Conversation) MultiSchemaHibernateUtil.getSession(schema).get(Conversation.class,
					con_id.getId());

			// Get the 2 (should be 2) actors in this conversation.
			for (ListIterator<ConvActorAssignment> liiii = conv.getConv_actor_assigns(schema).listIterator(); liiii
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

		List currentChats = Conversation.getAllPrivateChatForSim(schema, sim_id);

		// Loop over all private conversations in this set
		for (ListIterator<Conversation> li = currentChats.listIterator(); li.hasNext();) {
			Conversation con_id = li.next();

			Vector actors = new Vector();

			MultiSchemaHibernateUtil.beginTransaction(schema);
			Conversation conv = (Conversation) MultiSchemaHibernateUtil.getSession(schema).get(Conversation.class,
					con_id.getId());

			// Get the 2 (should be 2) actors in this conversation.
			for (ListIterator<ConvActorAssignment> liiii = conv.getConv_actor_assigns(schema).listIterator(); liiii
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
		 * System.out.println("checking read write on " + act.getName()); List
		 * setOfSections =
		 * SimulationSectionAssignment.getBySimAndActorAndPhase(schema,
		 * this.sim_id, act.getId(), sp .getId());
		 * 
		 * for (ListIterator slist = setOfSections.listIterator();
		 * slist.hasNext();) { SimulationSectionAssignment ss =
		 * (SimulationSectionAssignment) slist.next();
		 * 
		 * CustomizeableSection custSec = CustomizeableSection.getMe(schema,
		 * ss.getBase_section_id() + "");
		 * 
		 * if (custSec != null) { System.out.println("cs id: " +
		 * ss.getBase_section_id()); System.out.println("bss rec tab: " +
		 * custSec.getRec_tab_heading()); System.out.println("can read " +
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
		 * System.out.println("docs were : " + currentActors); }
		 * 
		 * }
		 * 
		 * if (custSec.isConfers_write_ability() == true) {
		 * System.out.println("confers read and write"); Hashtable storedGoodies
		 * = custSec.getContents(); String docs = (String)
		 * storedGoodies.get(SharedDocument.DOCS_IN_HASHTABLE_KEY);
		 * System.out.println("docs were : " + docs); } }
		 * System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		 * 
		 * }
		 * 
		 * } // End of loop over actors }
		 */
	}





	public String getDefaultSimXMLFileName(Simulation simulation) {

		Date saveDate = new java.util.Date();

		SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyyyy");

		String fileName = simulation.getName() + "_" + simulation.getVersion() + "_" + sdf.format(saveDate);

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

		// Simulation simulation = Simulation.getMe(schema, new Long(_sim_id));

		FileIO.saveSimulationXMLFile(ObjectPackager.packageSimulation(schema, new Long(_sim_id)), fileName);

		return fileName;
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
	public void handlePublishing(String command, String sim_key_words, String auto_registration) {

		if (command == null) {
			return;
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Simulation sim = (Simulation) MultiSchemaHibernateUtil.getSession(schema).get(Simulation.class, sim_id);

		if (command.equalsIgnoreCase("Publish It!")) {
			sim.setReadyForPublicListing(true);
			sim.setListingKeyWords(sim_key_words);
		}

		else if (command.equalsIgnoreCase("Un - Publish It!")) {
			sim.setReadyForPublicListing(false);
			sim.setListingKeyWords(sim_key_words);
		}

		if ((auto_registration != null) && (auto_registration.equalsIgnoreCase("true"))) {
			sim.setAllow_player_autoreg(true);
		} else {
			sim.setAllow_player_autoreg(false);
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}

	public static final int AUTHOR_LOGIN = 1;

	public static final int FACILITATOR_LOGIN = 2;

	/**
	 * 
	 * @param request
	 * @return
	 */
	public String validateLoginToOSP(HttpServletRequest request, int login_type) {

		loggedin = false;

		String sendToPage = "index.jsp";

		BaseUser bu = OSPSessionObjectHelper.validate(request);

		if (bu != null) {

			user_id = bu.getId();

			if (bu.getAuthorizedSchemas().size() == 0) {
				errorMsg = "You are not authorized to enter any databases.";
			} else if (bu.getAuthorizedSchemas().size() == 1) {
				// Send them on directly to this schema
				SchemaGhost sg = (SchemaGhost) bu.getAuthorizedSchemas().get(0);
				System.out.println("ghost schema is " + sg.getSchema_name());
				schema = sg.getSchema_name();
				User user = loginToSchema(user_id, sg.getSchema_name(), request);

				if (user.isSim_author() && (login_type == AUTHOR_LOGIN)) {
					loggedin = true;
					this.isSimCreator = true;
					sendToPage = "intro.jsp";
				} else if (user.isSim_instructor() && (login_type == FACILITATOR_LOGIN)) {
					loggedin = true;
					this.isFacilitator = true;
					sendToPage = "facilitateweb.jsp";
				} else {
					errorMsg = "Not authorized.";
				}

				user_name = bu.getUsername();

			} else if (bu.getAuthorizedSchemas().size() > 1) {
				// Send them on to the page where they can select schema.
				loggedin = true;
				sendToPage = "pick_schema.jsp";

				user_name = bu.getUsername();
			}

		} else {
			errorMsg = "Failed Login Attempt";
			System.out.println(errorMsg);
		}

		return sendToPage;
	}

	/**
	 * Sends password to user via email upon request.
	 * 
	 * @param request
	 * @return
	 */
	public boolean handleRetrievePassword(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null) && (sending_page.equalsIgnoreCase("retrieve_password"))) {

			String email = (String) request.getParameter("email");

			BaseUser bu = BaseUser.getByUsername(email);

			if (bu == null) {
				this.errorMsg = "User not found in database";
				return false;
			} else {
				this.errorMsg = "";
			}

			System.out.println("emailing " + email);

			String message = "A request for your password has been received. Your password is " + bu.getPassword();

			String admin_email = USIP_OSP_Properties.getValue("osp_admin_email");
			System.out.println("System.out.println(admin_email); " + admin_email);

			Vector ccs = new Vector();
			Vector bccs = new Vector();
			bccs.add(admin_email);

			try {
				SchemaInformationObject sio = SchemaInformationObject.loadPrincipalSchemaObjectFromPropertiesFile();

				Emailer.postMail(sio, email, "Access to OSP", message, admin_email, ccs, bccs);
			} catch (Exception e) {
				this.errorMsg = "error was: " + e.getMessage();
			}
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public String validateLoginToSim(HttpServletRequest request) {

		loggedin = false;

		String sendToPage = "index.jsp";

		BaseUser bu = OSPSessionObjectHelper.validate(request);

		if (bu != null) {

			user_id = bu.getId();
			user_name = bu.getUsername();

			loggedin = true;
			sendToPage = "select_simulation.jsp";

		} else {
			errorMsg = "Failed Login Attempt";
		}

		return sendToPage;
	}

	/**
	 * Takes a comma separated list of actor ids and turns it into a list of
	 * names.
	 * 
	 * @param request
	 * @param id_list
	 * @return
	 */
	public String stringListToNames(HttpServletRequest request, String id_list, String separator) {

		if (id_list == null) {
			return "";
		}

		StringTokenizer str = new StringTokenizer(id_list, ",");

		String returnList = "";

		while (str.hasMoreTokens()) {
			returnList += (getActorName(request, str.nextToken().trim()) + separator);

		}

		// chop the final comma off
		if (returnList.endsWith(separator)) {
			returnList = returnList.substring(0, returnList.length() - 2);
		}

		return returnList;
	}



	public String getActorName(HttpServletRequest request, String a_id) {

		return getActorName(request, new Long(a_id));
	}

	/**
	 * 
	 * @param request
	 * @param a_id
	 * @return
	 */
	public String getActorName(HttpServletRequest request, Long a_id) {

		ServletContext context = request.getSession().getServletContext();

		Hashtable<String, String> actor_names = (Hashtable<String, String>) context.getAttribute("actor_names");

		if (actor_names == null) {
			actor_names = new Hashtable<String, String>();
			context.setAttribute("actor_names", actor_names);
		}

		String a_name = actor_names.get(schema + "_" + running_sim_id + " " + a_id);
		if (a_name == null) {
			loadActorNamesInHashtable(actor_names);
			a_name = actor_names.get(schema + "_" + running_sim_id + " " + a_id);
			context.setAttribute("actor_names", actor_names);
		}

		return a_name;
	}

	public void loadActorNamesInHashtable(Hashtable actor_names) {

		System.out.println("storing names in hashtable. ");
		Simulation sim = this.giveMeSim();

		for (ListIterator<Actor> li = sim.getActors(schema).listIterator(); li.hasNext();) {
			Actor act = li.next();

			actor_names.put(schema + "_" + running_sim_id + " " + act.getId(), act.getName());

		}
	}

	/**
	 * 
	 * @param request
	 * @param a_id
	 * @return
	 */
	public String getActorThumbImage(HttpServletRequest request, Long a_id) {

		ServletContext context = request.getSession().getServletContext();

		Hashtable<String, String> actor_thumbs = (Hashtable<String, String>) context.getAttribute("actor_thumbs");

		if (actor_thumbs == null) {
			actor_thumbs = new Hashtable<String, String>();
			context.setAttribute("actor_thumbs", actor_thumbs);
		}

		String a_thumb = actor_thumbs.get(schema + "_" + running_sim_id + " " + a_id);
		if (a_thumb == null) {
			loadActorThumbsInHashtable(actor_thumbs);
			a_thumb = actor_thumbs.get(schema + "_" + running_sim_id + " " + a_id);
			context.setAttribute("actor_thumbs", actor_thumbs);
		}

		return a_thumb;
	}

	public Vector myActors = new Vector();

	/**
	 * 
	 * @return
	 */
	public Vector getActorsForConversation(Long ssrsdoa_id, HttpServletRequest request) {

		if ((myActors == null) || (myActors.size() == 0)) {
			myActors = ChatController.getActorsForConversation(this, ssrsdoa_id, request);
		}

		return myActors;

	}

	/**
	 * Takes input from the chat page to change the color in which the actor's
	 * text is being seen.
	 * 
	 * @param actor_id
	 * @param newColor
	 */
	public void changeActorsColor(String actor_id, String newColor) {

		for (Enumeration e = myActors.elements(); e.hasMoreElements();) {
			ActorGhost ag = (ActorGhost) e.nextElement();

			// System.out.println("color was: " +
			// ag.getDefaultColorChatBubble());

			if (ag.getId().toString().equalsIgnoreCase(actor_id)) {
				ag.setDefaultColorChatBubble(newColor);
				// System.out.println("color is: " +
				// ag.getDefaultColorChatBubble());
			}
		}
	}

	public void loadActorThumbsInHashtable(Hashtable actor_thumbs) {

		System.out.println("storing namges actor thumb nail images in hashtable. ");
		Simulation sim = this.giveMeSim();

		for (ListIterator<Actor> li = sim.getActors(schema).listIterator(); li.hasNext();) {
			Actor act = li.next();

			if (act.getImageThumbFilename() != null) {
				actor_thumbs.put(schema + "_" + running_sim_id + " " + act.getId(), act.getImageThumbFilename());
			} else {
				actor_thumbs.put(schema + "_" + running_sim_id + " " + act.getId(), "no_image_default_thumb.jpg");
			}

		}
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

		if ((!this.isAdmin) || (!this.isSimCreator)) {
			errorMsg = "Not authorized to create administrative users.";
			return user;
		} else {
			PSO_UserAdmin pu = new PSO_UserAdmin(this);
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

		System.out.println("pcp - universal was : " + universal);
		System.out.println("tab_heading : " + tab_heading);

		addSectionFromProcessCustomPage(cs.getId(), tab_pos, tab_heading, request, universal);
	}

	public User handleAutoRegistration(HttpServletRequest request) {
		PSO_UserAdmin pu = new PSO_UserAdmin(this);
		return pu.handleAutoRegistration(request);
	}

	public User handleCreateUser(HttpServletRequest request) {
		PSO_UserAdmin pu = new PSO_UserAdmin(this);
		return pu.handleCreateUser(request, schema);
	}




	public void handleMyProfile(HttpServletRequest request) {
		PSO_UserAdmin pu = new PSO_UserAdmin(this);
		pu.handleMyProfile(request, user_id);
	}

	// /////////////////////////////////////////////////////////////////////////

	public Long actor_being_worked_on_id;

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
	public Simulation handleSetUniversalSimSectionsPage(HttpServletRequest request) {

		return (getMyPSO_SectionMgmt().handleSetUniversalSimSectionsPage(request));
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
	public CustomizeableSection handleMakeReadDocumentPage(HttpServletRequest request) {
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
	public CustomizeableSection handleMakeSplitPageVertical(HttpServletRequest request) {
		return (getMyPSO_SectionMgmt().handleMakeSplitPageVertical(request));
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
	public String checkAgainstHash(Long cs_id, int object_index, Long id_of_object_being_checked) {

		Logger.getRootLogger().debug(
				"pso.checkAgainstHash (bss_id/index/object_id): " + cs_id + "/" + object_index + "/"
						+ id_of_object_being_checked);

		Hashtable index_hash = BaseSimSectionDepObjectAssignment.getIndexIdHashtable(schema, cs_id);

		if ((index_hash == null) || (id_of_object_being_checked == null)) {
			Logger.getRootLogger().warn("hash was null");
			return "";
		}

		// Get the value of the object stored at this position.
		Long valueFromHash = (Long) index_hash.get(new Long(object_index));

		if (id_of_object_being_checked.equals(valueFromHash)) {
			System.out.println("returning selcted");
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
	public CustomizeableSection handleMakeWriteDocumentPage(HttpServletRequest request) {

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
	 * 
	 * @param request
	 * @param simulation
	 */
	public void handleAddRunningSimulation(HttpServletRequest request, Simulation simulation) {

		String sending_page = (String) request.getParameter("sending_page");
		String addRunningSimulation = (String) request.getParameter("addRunningSimulation");

		if ((sending_page != null) && (addRunningSimulation != null)
				&& (sending_page.equalsIgnoreCase("create_running_sim"))) {

			String rsn = (String) request.getParameter("running_sim_name");
			RunningSimulation rs = simulation.addNewRunningSimulation(rsn, schema, this.user_id, this.user_Display_Name);

			running_sim_id = rs.getId();

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
	public CustomizeableSection handleMakePrivateChatPage(HttpServletRequest request) {
		return (getMyPSO_SectionMgmt().handleMakePrivateChatPage(request));
	}

	/**
	 * A wrapper that passes the request through to the associated
	 * PSO_SectionMgmt object.
	 * 
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleMakeReflectionPage(HttpServletRequest request) {

		return (getMyPSO_SectionMgmt().handleMakeReflectionPage(request));
	}

	public void addSectionFromProcessCustomPage(Long bss_id, String string_tab_pos, String tab_heading,
			HttpServletRequest request, String universal) {

		getMyPSO_SectionMgmt().addSectionFromProcessCustomPage(bss_id, string_tab_pos, tab_heading, request, universal);
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
			gv = GenericVariable.getGVForRunningSim(schema, varId, this.running_sim_id);
		}

		// Get list of allowable responses
		List allowableResponses = AllowableResponse.pullOutArs(cs, schema);

		for (ListIterator li = allowableResponses.listIterator(); li.hasNext();) {
			AllowableResponse ar = (AllowableResponse) li.next();

			System.out.println("!!!!!!!!!!!!!!!!!!checking " + ar.getId());

			if ((gv != null) && (gv.getCurrentlySelectedResponse() != null)
					&& (gv.getCurrentlySelectedResponse().equals(ar.getId()))) {
				answersSelected.put(ar.getId(), " checked ");
				System.out.println("put in checked for " + ar.getId());
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
	public void takePlayerChoice(HttpServletRequest request, CustomizeableSection cs) {

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null) && (sending_page.equalsIgnoreCase("player_discrete_choice"))) {
			String players_choice = (String) request.getParameter("players_choice");
			Long answer_chosen = new Long(players_choice);

			// Save the answer currently selected in the generic variable
			// itself.
			GenericVariable gv = GenericVariable.pullMeOut(schema, cs, this.running_sim_id);
			gv.setCurrentlySelectedResponse(answer_chosen);

			gv.checkMyTriggers(this, Trigger.FIRE_ON_WHEN_CALLED);

			gv.saveMe(schema);
		}
	}

	public String getBaseSimURL() {
		return USIP_OSP_Properties.getValue("base_sim_url");
	}



	/**
	 * 
	 * @param request
	 * @param sd
	 */
	public void handleWriteDocument(HttpServletRequest request, SharedDocument sd) {

		String sending_page = (String) request.getParameter("sending_page");
		String update_text = (String) request.getParameter("update_text");

		if ((sending_page != null) && (update_text != null) && (sending_page.equalsIgnoreCase("write_document"))) {

			String write_document_text = (String) request.getParameter("write_document_text");

			sd.setBigString(write_document_text);
			sd.saveMe(schema);

		} // End of if coming from this page and have added text

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

		String sending_section = (String) request.getParameter("sending_section");

		if ((sending_section != null) && (sending_section.equalsIgnoreCase("change_color"))) {

			String ss_id = (String) request.getParameter("ss_id");
			String new_color = (String) request.getParameter("new_color");

			SimulationSectionAssignment ssa = SimulationSectionAssignment.getMe(schema, new Long(ss_id));

			if (ssa != null) {
				ssa.setTabColor(new_color);
				ssa.save(schema);
			}

		}

	}
	
	public RunningSimulation giveMeRunningSim() {
		
		if (running_sim_id == null){
			Logger.getRootLogger().warn("Warning RunningSimId is null in pso.giveMeRunningSim");
			
			return null;
		}
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil.getSession(schema).get(
				RunningSimulation.class, running_sim_id);

		MultiSchemaHibernateUtil.getSession(schema).evict(rs);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return rs;
	}

} // End of class
