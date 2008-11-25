package org.usip.osp.networking;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.io.File;
import java.io.StreamTokenizer;
import java.sql.*;

import javax.servlet.ServletContext;
import javax.servlet.http.*;

import org.usip.osp.baseobjects.*;
import org.usip.osp.communications.*;
import org.usip.osp.persistence.*;
import org.hibernate.Session;
import org.usip.osp.specialfeatures.*;

import com.oreilly.servlet.MultipartRequest;

/**
 * @author Ronald "Skip" Cole
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
public class ParticipantSessionObject {

	/** Determines if actor is logged in. */
	private boolean loggedin = false;

	public static final int PAGETYPE_OTHER = 0;
	public static final int PAGETYPE_THINK = 1;
	public static final int PAGETYPE_CREATE = 2;
	public static final int PAGETYPE_PLAY = 3;
	public static final int PAGETYPE_SHARE = 4;

	/**
	 * Used to give visual cue that user is in section 1 (think), 2 (create), 3
	 * (play) or 4 (share).
	 */
	public int page_type = PAGETYPE_OTHER;

	public boolean forward_on = false;
	
	/** Schema of the database that the user is working in. */
	public String schema = "";

	/** Organization of the schema that the user is working in. */
	public String schemaOrg = "";

	/** The page to take them back to if needed. */
	public String backPage = "index.jsp";

	/** Id of User that is logged in and using this ParticipantSessionObject. */
	public Long user_id;

	/**
	 * Username/ Email address of user that is logged in and using this
	 * ParticipantSessionObject.
	 */
	public String user_name;
	/**
	 * Once a player has selected a running sim, do not let them back out and
	 * choose another without logging out and logging in.
	 */
	public boolean hasSelectedRunningSim = false;

	/** Records if user is an admin. */
	private boolean isAdmin = false;

	/** Records if user is authorized to create simulations. */
	private boolean isSimCreator = false;

	/** Records if user is authorized to facilitate simulations. */
	private boolean isFacilitator = false;

	/** Records the display name of this user. */
	public String user_Display_Name = "";

	/** Records the email of this user. */
	public String user_email = "";

	/** Name of simulation being conducted or worked on. */
	public String simulation_name = "";

	/** Version of the simulation be conducted or worked on. */
	public String simulation_version = "";

	/** Organization that created the simulation. */
	public String simulation_org = "";

	/**
	 * Copyright string to display at the bottom of every page in the
	 * simulation.
	 */
	public String sim_copyright_info = "";

	/** Name of the running simulation session. */
	public String run_sim_name = "";

	/** ID of Simulation being conducted or worked on. */
	public Long sim_id;

	/** Indicates if user has selected a simulation. */
	public boolean simulationSelected = false;

	/** ID of the Running Simulation being conducted or worked on. */
	public Long running_sim_id;

	/** Indicates if user has selected a running simulation. */
	public boolean runningSimSelected = false;

	/** ID of Actor being played or worked on. */
	public Long actor_id;

	/** Name of the actor being played or worked on. */
	public String actor_name = "";

	/** ID of Phase being conducted or worked on. */
	public Long phase_id;

	/** Indicates if user has selected a phase. */
	public boolean phaseSelected = false;

	/** Name of phase being conducted or worked on. */
	private String phaseName = "";

	/** Round being displayed */
	private String simulation_round = "0";

	public HttpSession session = null;

	/** Error message to be shown to the user. */
	public String errorMsg = "";

	public static String debugStuff = "start here:<br>";

	public List tempSimSecList = new ArrayList();

	/** Text of alert being worked on. */
	public String alertInQueueText = "";

	/** Type of alert being worked on. */
	public int alertInQueueType = 0;

	/** Login ticket of this user. */
	public LoggedInTicket myLoggedInTicket = new LoggedInTicket();

	/** Receives a heartbeat pulse from a user. */
	public void acceptUserHeartbeatPulses(String from_actor, String from_tab,
			HttpServletRequest request) {

		debugStuff = from_actor + " on " + from_tab;
		Hashtable<Long, Hashtable> loggedInPlayers = (Hashtable<Long, Hashtable>) request
				.getSession().getServletContext().getAttribute(
						"loggedInPlayers");

		Hashtable thisSetOfUsers = loggedInPlayers.get(this.running_sim_id);

		if (thisSetOfUsers == null) {
			thisSetOfUsers = new Hashtable();
			request.getSession().getServletContext().setAttribute(
					"loggedInPlayers", loggedInPlayers);
		}

		LoggedInTicket lit = (LoggedInTicket) thisSetOfUsers.get(this.user_id);

		// LoggedInTicket added to cache when player chose simulation. If now
		if (lit == null) {
			return;
		} else {
			lit.hearHeartBeat();
		}
	}


	/**
	 * Unpacks a simulation from an XML file.
	 * 
	 * @param request
	 */
	public void handleUnpackSimulation(HttpServletRequest request) {

		String filename = (String) request.getParameter("filename");

		System.out.println("unpacking " + filename);

		ObjectPackager.unpackSim(filename, schema);

	}

	public void handleWriteAARandEndSim(HttpServletRequest request) {

		String command = (String) request.getParameter("command");

		if (command != null) {
			if (command.equalsIgnoreCase("Save Changes")) {
				System.out.println("Command was save changes! ");
				saveAarText(request);
			} else if (command.equalsIgnoreCase("Save and End Simulation")) {
				// Save text
				saveAarText(request);

				// Mark Completed, change phase
				Simulation sim = this.giveMeSim();
				System.out.println("forwarin on to : "
						+ sim.getLastPhaseId().toString());
				this.changePhase(sim.getLastPhaseId().toString(), request);
				// this.changePhase("Completed", request);

				// Forward them back
				forward_on = true;
			}
		}

	}

	private void saveAarText(HttpServletRequest request) {
		String write_aar_end_sim = (String) request
				.getParameter("write_aar_end_sim");
		System.out.println("saving: " + write_aar_end_sim);

		RunningSimulation rs = giveMeRunningSim();
		rs.setAar_text(write_aar_end_sim);
		rs.saveMe(schema);
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

		String command = (String) request.getParameter("command");
		String phase_name = (String) request.getParameter("phase_name");
		String phase_notes = (String) request.getParameter("phase_notes");
		String nominal_order = (String) request.getParameter("nominal_order");

		if (command != null) {
			if (command.equalsIgnoreCase("Create")) {
				returnSP.setName(phase_name);
				returnSP.setNotes(phase_notes);
				returnSP.setOrder(string2Int(nominal_order));
				returnSP.saveMe(schema);
				sim.getPhases().add(returnSP);
				
				Actor ctrl_act = Actor.getControlActor(schema);
				sim.addControlSectionsToAllPhasesOfControl(schema, ctrl_act);
				
				sim.saveMe(schema);
			} else if (command.equalsIgnoreCase("Edit")) {
				String sp_id = (String) request.getParameter("sp_id");
				returnSP = SimulationPhase.getMe(schema, sp_id);
			} else if (command.equalsIgnoreCase("Update")) { // 
				String sp_id = (String) request.getParameter("sp_id");
				returnSP = SimulationPhase.getMe(schema, sp_id);
				returnSP.setName(phase_name);
				returnSP.setNotes(phase_notes);
				returnSP.setOrder(string2Int(nominal_order));
				returnSP.saveMe(schema);
			} else if (command.equalsIgnoreCase("Clear")) { // 
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

		String command = (String) request.getParameter("command");

		if (command != null) {
			if ((command.equalsIgnoreCase("Assign User"))) {

				String user_id = (String) request
						.getParameter("user_to_add_to_simulation");
				String actor_id = (String) request
						.getParameter("actor_to_add_to_simulation");
				String sim_id = (String) request
						.getParameter("simulation_adding_to");
				String running_sim_id = (String) request
						.getParameter("running_simulation_adding_to");

				UserAssignment ua = UserAssignment.getUniqueUserAssignment(
						schema, new Long(sim_id), new Long(running_sim_id),
						new Long(actor_id), new Long(user_id));
			}
		}
	}

	public String setOfUsers = "";
	public String defaultInviteEmailMsg = "";
	public String invitationCode = "";

	{
		defaultInviteEmailMsg = "Dear Student,<br />\r\n";

	}

	/**
	 * 
	 * @param request
	 */
	public void handleBulkInvite(HttpServletRequest request) {
		setOfUsers = (String) request.getParameter("setOfUsers");
		defaultInviteEmailMsg = (String) request
				.getParameter("defaultInviteEmailMsg");
		invitationCode = (String) request.getParameter("invitationCode");

		Long schema_id = SchemaInformationObject.lookUpId(schema);

		for (ListIterator<String> li = getSetOfEmails(setOfUsers)
				.listIterator(); li.hasNext();) {
			String this_email = (String) li.next();

			if (BaseUser.checkIfUserExists(this_email)) {
				System.out.println("exists:" + this_email);
				// ?? make sure exists in this schema

			} else {

				System.out.println("does not exist:" + this_email);

				// Add entry into system to all them to register.
				UserRegistrationInvite uri = new UserRegistrationInvite(
						user_name, this_email, invitationCode, schema);

				uri.saveMe();

				// Send them email directing them to the page to register

				String subject = "Invitation to register on an OSP System";
				sendBulkInvitationEmail(schema_id, this_email, subject,
						defaultInviteEmailMsg);

			}
		}
	}

	public void sendBulkInvitationEmail(Long schema_id, String the_email,
			String subject, String message) {

		Vector cced = null;
		Vector bcced = new Vector();
		bcced.add(user_name);

		Emailer.postMail(schema_id, the_email, subject, message, user_name,
				cced, bcced);
	}

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

	public boolean handleDeleteObject(HttpServletRequest request) {

		String objectType = request.getParameter("object_type");
		String objectInfo = request.getParameter("object_info");
		String objid = request.getParameter("objid");
		String cancel_action = request.getParameter("cancel_action");

		String debug = "";

		if (cancel_action != null) {
			return true;
		}

		String deletion_confirm = (String) request
				.getParameter("deletion_confirm");
		if ((deletion_confirm != null)
				&& (deletion_confirm.equalsIgnoreCase("Submit"))) {

			Long o_id = new Long(objid);

			if (objectType.equalsIgnoreCase("simulation")) {
				MultiSchemaHibernateUtil.beginTransaction(schema);
				Simulation sim = (Simulation) MultiSchemaHibernateUtil
						.getSession(schema).get(Simulation.class, o_id);
				MultiSchemaHibernateUtil.getSession(schema).delete(sim);
				this.sim_id = null;
				this.simulationSelected = false;

				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			} else if (objectType.equalsIgnoreCase("phase")) {
				MultiSchemaHibernateUtil.beginTransaction(schema);
				SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil
						.getSession(schema).get(SimulationPhase.class, o_id);
				MultiSchemaHibernateUtil.getSession(schema).delete(sp);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			} else if (objectType.equalsIgnoreCase("actor")) {
				MultiSchemaHibernateUtil.beginTransaction(schema);
				Actor act = (Actor) MultiSchemaHibernateUtil.getSession(schema)
						.get(Actor.class, o_id);

				if (actor_id != null) {
					if (actor_id.intValue() == act.getId().intValue()) {
						actor_id = null;
					}
				}

				MultiSchemaHibernateUtil.getSession(schema).delete(act);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
			} else if (objectType.equalsIgnoreCase("inject")) {

				MultiSchemaHibernateUtil.beginTransaction(schema);
				Inject inject = (Inject) MultiSchemaHibernateUtil.getSession(
						schema).get(Inject.class, o_id);

				MultiSchemaHibernateUtil.getSession(schema).delete(inject);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			} else if (objectType.equalsIgnoreCase("sim_section")) {
				MultiSchemaHibernateUtil.beginTransaction(schema);
				SimulationSection ss = (SimulationSection) MultiSchemaHibernateUtil
						.getSession(schema).get(SimulationSection.class, o_id);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

				SimulationSection.removeAndReorder(schema, ss);

			} else if (objectType.equalsIgnoreCase("user_assignment")) {
				MultiSchemaHibernateUtil.beginTransaction(schema);
				UserAssignment ua = (UserAssignment) MultiSchemaHibernateUtil
						.getSession(schema).get(UserAssignment.class, o_id);
				MultiSchemaHibernateUtil.getSession(schema).delete(ua);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
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

		String command = (String) request.getParameter("command");

		if (command != null) {
			if ((command.equalsIgnoreCase("Start Simulation"))) {

				String email_users = (String) request
						.getParameter("email_users");
				String email_text = (String) request.getParameter("email_text");

				BaseUser bu = BaseUser.getByUserId(user_id);

				MultiSchemaHibernateUtil.beginTransaction(schema);

				User user = (User) MultiSchemaHibernateUtil.getSession(schema)
						.get(User.class, user_id);

				RunningSimulation running_sim = (RunningSimulation) MultiSchemaHibernateUtil
						.getSession(schema).get(RunningSimulation.class,
								running_sim_id);

				running_sim.enableAndPrep(schema, sim_id.toString(), bu
						.getUsername(), email_users, email_text);

				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			} // End of if coming from this page and have enabled the sim
			// ////////////////////////////
		}

	}

	/**
	 * 
	 * @param request
	 */
	public void handleLoadPlayerScenario(HttpServletRequest request) {

		schema = (String) request.getParameter("schema");
		schemaOrg = (String) request.getParameter("schema_org");

		session = request.getSession();

		MultiSchemaHibernateUtil.beginTransaction(schema);

		String user_assignment_id = (String) request
				.getParameter("user_assignment_id");

		UserAssignment ua = (UserAssignment) MultiSchemaHibernateUtil
				.getSession(schema).get(UserAssignment.class,
						new Long(user_assignment_id));

		sim_id = ua.getSim_id();
		Simulation simulation = (Simulation) MultiSchemaHibernateUtil
				.getSession(schema).get(Simulation.class, sim_id);

		this.simulation_name = simulation.getName();
		this.sim_copyright_info = simulation.getCopyright_string();
		this.simulation_version = simulation.getVersion();
		this.simulation_org = simulation.getCreation_org();

		running_sim_id = ua.getRunning_sim_id();
		RunningSimulation running_sim = (RunningSimulation) MultiSchemaHibernateUtil
				.getSession(schema)
				.get(RunningSimulation.class, running_sim_id);

		this.run_sim_name = running_sim.getName();

		// ///////////////////////////////////////////////////////////
		this.simulation_round = running_sim.getRound() + "";

		Hashtable<Long, String> roundNames = new Hashtable();

		try {
			roundNames = (Hashtable<Long, String>) session.getServletContext()
					.getAttribute("roundNames");
		} catch (Exception e) {
			e.printStackTrace();
			roundNames = new Hashtable<Long, String>();

			session.getServletContext().setAttribute("roundNames",
					new Hashtable<Long, String>());
		}
		String cachedRoundName = roundNames.get(running_sim_id);
		if (cachedRoundName == null) {
			roundNames.put(running_sim_id, simulation_round);

			request.getSession().getServletContext().setAttribute("roundNames",
					roundNames);

		}
		// ///////////////////////////////////////////////////////////

		actor_id = ua.getActor_id();

		Actor actor = (Actor) MultiSchemaHibernateUtil.getSession(schema).get(
				Actor.class, actor_id);

		this.actor_name = actor.getName();

		this.phase_id = running_sim.getPhase_id();

		SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil
				.getSession(schema).get(SimulationPhase.class, this.phase_id);

		this.phaseName = sp.getName();

		// //////////////////////////////////////////////////////////////////////
		// Store it in the web cache, if this has not been done already
		// by another user.
		Hashtable<Long, String> phaseNames = (Hashtable<Long, String>) session
				.getServletContext().getAttribute("phaseNames");

		String cachedPhaseName = phaseNames.get(running_sim_id);
		if (cachedPhaseName == null) {
			phaseNames.put(running_sim_id, sp.getName());
			request.getSession().getServletContext().setAttribute("phaseNames",
					phaseNames);

		}
		// //////////////////////////////////////////////////////////////////////
		// ///

		// //////////////////////////////////////////////////////////////////////
		// Store it in the web cache, if this has not been done already
		// by another user.
		Hashtable<Long, Long> phaseIds = (Hashtable<Long, Long>) session
				.getServletContext().getAttribute("phaseIds");

		Long cachedPhaseId = phaseIds.get(running_sim_id);
		if (cachedPhaseId == null) {
			phaseIds.put(running_sim_id, phase_id);
			request.getSession().getServletContext().setAttribute("phaseIds",
					phaseIds);

		}
		// //////////////////////////////////////////////////////////////////////
		// ///

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		User user = loginToSchema(user_id, schema, request);

		myLoggedInTicket.setActor_id(actor_id);
		myLoggedInTicket.setRunning_sim_id(running_sim_id);

		// Player starts on tab 1, always.
		myLoggedInTicket.setTab_position(new Long(1));

		storeUserInfoInSessionInformation(request);

		System.out.println("user has selected simulation");
		this.hasSelectedRunningSim = true;

	}

	public void getAndLoad(HttpServletRequest request) {

		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			String pname = (String) e.nextElement();

			String vname = (String) request.getParameter(pname);
			request.getSession().setAttribute(pname, vname);
		}
	}

	/**
	 * Attempts to pull a variable out of the session. If one is not there, then it will return and empty string "".
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

		String new_admin_user_cbox = (String) request
				.getParameter("new_admin_user_cbox");
		String admin_first = (String) request.getParameter("admin_first");
		String admin_middle = (String) request.getParameter("admin_middle");
		String admin_last = (String) request.getParameter("admin_last");
		String admin_full = (String) request.getParameter("admin_full");

		String admin_pass = (String) request.getParameter("admin_pass");
		String admin_email = (String) request.getParameter("admin_email");

		String email_smtp = (String) request.getParameter("email_smtp");
		String email_user = (String) request.getParameter("email_user");
		String email_pass = (String) request.getParameter("email_pass");
		String email_user_address = (String) request
				.getParameter("email_user_address");

		String error_msg = "";
		String ps = MultiSchemaHibernateUtil.principalschema;

		boolean existingAdminUser = true;

		if ((new_admin_user_cbox != null)
				&& (new_admin_user_cbox.equalsIgnoreCase("new"))) {
			existingAdminUser = false;
		}

		Long admin_user_id;
		BaseUser bu = null;

		if ((sending_page != null) && (cleandb != null)
				&& (sending_page.equalsIgnoreCase("clean_db"))) {

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
		User user = new User(schema, admin_email, admin_pass, admin_first,
				admin_last, admin_middle, admin_full, admin_email, true, true,
				true);

		String loadss = (String) request.getParameter("loadss");
		String load_cs = (String) request.getParameter("load_cs");

		if ((loadss != null) && (loadss.equalsIgnoreCase("true"))) {
			BaseSimSection.readBaseSimSectionsFromXMLFiles(schema);
		}

		if ((load_cs != null) && (load_cs.equalsIgnoreCase("true"))) {
			BaseSimSection.readCustomLibSimSectionsFromXMLFiles(schema);
		}

		error_msg = "You may now login as the root user with the password that you provided.";

		return error_msg;

	}

	public void handleCreateRootDB(HttpServletRequest request) {

		System.out.println("creating root db");
		MultiSchemaHibernateUtil.recreateRootDatabase();
	}

	/**
	 * Handles the creation of an inject group.
	 * 
	 * @param request
	 */
	public void handleCreateInjectGroup(HttpServletRequest request) {
		String inject_group_name = (String) request
				.getParameter("inject_group_name");
		String inject_group_description = (String) request
				.getParameter("inject_group_description");

		InjectGroup ig = new InjectGroup();
		ig.setName(inject_group_name);
		ig.setDescription(inject_group_description);
		ig.setSim_id(sim_id);

		ig.saveMe(schema);

	}

	public void handleCreateInject(HttpServletRequest request) {

		String inject_name = (String) request.getParameter("inject_name");
		String inject_text = (String) request.getParameter("inject_text");
		String inject_notes = (String) request.getParameter("inject_notes");
		String inject_group_id = (String) request
				.getParameter("inject_group_id");

		String edit = (String) request.getParameter("edit");
		String inj_id = (String) request.getParameter("inj_id");

		if ((edit != null) && (edit.equalsIgnoreCase("true"))) {
			MultiSchemaHibernateUtil.beginTransaction(schema);
			Inject inject = (Inject) MultiSchemaHibernateUtil
					.getSession(schema).get(Inject.class, new Long(inj_id));
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
			String url = loc + port + "/"
					+ USIP_OSP_Properties.getValue("principalschema")
					+ "?autoReconnect=true";

			String conn_string = MysqlDatabase.makeConnString(url, username,
					password);

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
	 * 
	 * @param request
	 * @return
	 */
	public BaseUser validate(HttpServletRequest request) {

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		System.out.println("validating user: " + username);

		BaseUser bu = BaseUser.validateUser(username, password);

		return bu;

	}

	public User loginToSchema(Long bu_id, String schema,
			HttpServletRequest request) {

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

			Hashtable<Long, LoggedInTicket> loggedInUsers = (Hashtable<Long, LoggedInTicket>) request
					.getSession().getServletContext().getAttribute(
							"loggedInUsers");

			loggedInUsers.put(user.getId(), myLoggedInTicket);

			loggedin = true;
		} else {
			loggedin = false;
		}

		return user;

	}

	/** Gets called when the user has selected a scenario to play. */
	public void storeUserInfoInSessionInformation(HttpServletRequest request) {

		Hashtable<Long, Hashtable> loggedInPlayers = (Hashtable<Long, Hashtable>) request
				.getSession().getServletContext().getAttribute(
						"loggedInPlayers");

		Hashtable thisSetOfPlayers = loggedInPlayers.get(this.running_sim_id);

		if (thisSetOfPlayers == null) {
			thisSetOfPlayers = new Hashtable();
			loggedInPlayers.put(this.running_sim_id, thisSetOfPlayers);
		}

		thisSetOfPlayers.put(this.myLoggedInTicket.getTrail_id(),
				myLoggedInTicket);

		request.getSession().getServletContext().setAttribute(
				"loggedInPlayers", loggedInPlayers);

	}

	/**
	 * Returns the PSO stored in the session, or creates one. The coder can
	 * indicated if he or she wants to start a transaction.
	 */
	public static ParticipantSessionObject getPSO(HttpSession session,
			boolean getConn) {

		ParticipantSessionObject pso = (ParticipantSessionObject) session
				.getAttribute("pso");

		if (pso == null) {
			System.out.println("pso is new");
			pso = new ParticipantSessionObject();
			pso.session = session;
		}

		session.setAttribute("pso", pso);

		return pso;
	}

	/**
	 * Sets the page type based on the directory in which these jsps are
	 * located.
	 */
	public void findPageType(HttpServletRequest request) {

		String url = request.getRequestURI();

		if (url.contains("simulation_planning")) {
			page_type = PAGETYPE_THINK;
		} else if (url.contains("simulation_authoring")) {
			page_type = PAGETYPE_CREATE;
		} else if (url.contains("simulation_facilitation")) {
			page_type = PAGETYPE_PLAY;
		} else if (url.contains("simulation_sharing")) {
			page_type = PAGETYPE_SHARE;
		} else {
			page_type = PAGETYPE_OTHER;
		}
	}

	public List<SimulationSectionGhost> getSimSecList(HttpServletRequest request) {

		session = request.getSession();

		// Get phase id from the cache
		Hashtable<Long, Long> phaseIds = (Hashtable<Long, Long>) session
				.getServletContext().getAttribute("phaseIds");

		Long cachedPhaseId = phaseIds.get(running_sim_id);

		if (cachedPhaseId != null) {
			phase_id = cachedPhaseId;
		}

		String hashKey = sim_id + "_" + actor_id + "_" + phase_id;

		System.out.println("hashKey is " + hashKey);

		Hashtable<String, List<SimulationSectionGhost>> sim_section_info = (Hashtable<String, List<SimulationSectionGhost>>) session
				.getServletContext().getAttribute("sim_section_info");

		if (sim_section_info == null) {
			sim_section_info = new Hashtable<String, List<SimulationSectionGhost>>();
			System.out.println("creating new sim_section_info");
		}

		List<SimulationSectionGhost> returnList = sim_section_info.get(hashKey);

		if (returnList == null) {

			System.out.println("SimSecList was null.");

			returnList = new ArrayList<SimulationSectionGhost>();

			// Get full list from database hit
			List<SimulationSection> fullList = SimulationSection
					.getBySimAndActorAndPhase(schema, sim_id, actor_id,
							phase_id);

			// Copy the needed parts of that list into the ghosts
			for (ListIterator<SimulationSection> li = fullList.listIterator(); li
					.hasNext();) {
				SimulationSection ss = li.next();

				SimulationSectionGhost ssg = new SimulationSectionGhost();

				ssg.setTabHeading(ss.getTab_heading());

				returnList.add(ssg);

			}
			// Store that list into the Context
			sim_section_info.put(hashKey, returnList);

			session.getServletContext().setAttribute("sim_section_info",
					sim_section_info);

		}

		return returnList;
	}

	/**
	 * Advances game round, and propogates values to new round.
	 * 
	 */
	public void advanceRound(HttpServletRequest request) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		RunningSimulation running_sim = (RunningSimulation) MultiSchemaHibernateUtil
				.getSession(schema)
				.get(RunningSimulation.class, running_sim_id);

		running_sim.setRound(running_sim.getRound() + 1);

		System.out.println("Round is " + running_sim.getRound());

		this.simulation_round = running_sim.getRound() + "";

		Hashtable<Long, String> roundNames = (Hashtable<Long, String>) request
				.getSession().getServletContext().getAttribute("roundNames");
		roundNames.put(running_sim_id, this.simulation_round);
		request.getSession().getServletContext().setAttribute("roundNames",
				roundNames);

		propagateValues();

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(running_sim);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		// Let people know that there is a change to catch.
		storeNewHighestChangeNumber(request);

	}

	/**
	 * This is the highest change number that the player has. The change number
	 * for the particular running simulation is kept in the hashtable assigned
	 * to the web application.
	 */
	public Long myHighestChangeNumber = new Long(0);

	/**
	 * 
	 * @param phase_id
	 * @param request
	 */
	public void changePhase(String r_phase_id, HttpServletRequest request) {

		String notify_via_email = (String) request
				.getParameter("notify_via_email");

		String previousPhase = this.phaseName;
		
		try {

			MultiSchemaHibernateUtil.beginTransaction(schema);
			phase_id = new Long(r_phase_id);
			RunningSimulation running_sim = (RunningSimulation) MultiSchemaHibernateUtil
					.getSession(schema).get(RunningSimulation.class,
							running_sim_id);
			running_sim.setPhase_id(phase_id);
			System.out.println("set rs " + running_sim_id + " to " + phase_id);
			MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(
					running_sim);

			SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil
					.getSession(schema).get(SimulationPhase.class, phase_id);

			this.phaseName = sp.getName();

			// Store new phase name in web cache.
			Hashtable<Long, String> phaseNames = (Hashtable<Long, String>) request
					.getSession().getServletContext()
					.getAttribute("phaseNames");

			phaseNames.put(running_sim_id, this.phaseName);
			request.getSession().getServletContext().setAttribute("phaseNames",
					phaseNames);

			System.out.println("setting phase change alert");

			// //////////////////////////////////////////////////////////////////
			// Store new phase id in the web cache
			Hashtable<Long, Long> phaseIds = (Hashtable<Long, Long>) session
					.getServletContext().getAttribute("phaseIds");

			phaseIds.put(running_sim_id, phase_id);

			request.getSession().getServletContext().setAttribute("phaseIds",
					phaseIds);
			// //////////////////////////////////////////////////////////////////
			// ///////

			Alert al = new Alert();
			al.setType(Alert.TYPE_PHASECHANGE);
			
			String phaseChangeNotice = "Phase has changed from '" + previousPhase + "' to '" + this.phaseName + "'.";

			// Will need to add email text, etc.
			al.setAlertMessage(phaseChangeNotice);
			al.setAlertEmailMessage(phaseChangeNotice);

			running_sim.getAlerts().add(al);
			MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(al);
			MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(
					running_sim);

			// Let people know that there is a change to catch.
			storeNewHighestChangeNumber(request);

			if ((notify_via_email != null)
					&& (notify_via_email.equalsIgnoreCase("true"))) {

				Hashtable uniqList = new Hashtable();

				for (ListIterator<UserAssignment> li = running_sim
						.getUser_assignments().listIterator(); li.hasNext();) {
					UserAssignment ua = li.next();
					uniqList.put(ua.getUser_id(), "set");
				}

				Long schema_id = SchemaInformationObject.lookUpId(schema);

				for (Enumeration e = uniqList.keys(); e.hasMoreElements();) {
					Long key = (Long) e.nextElement();
					System.out.println("need to email " + key);

					// Need to get user email address from the key, which is the
					// user id.
					BaseUser bu = BaseUser.getByUserId(key);

					// String actor_name = getActorName(request, u)

					String subject = "Simulation Phase Change";
					String message = "Simulation phase has changed.";

					Vector cced = null;
					Vector bcced = new Vector();
					bcced.add(user_name);

					Emailer.postMail(schema_id, bu.getUsername(), subject,
							message, user_name, cced, bcced);

				}
			}

			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// Someday, I'll find a way to enter this final block in without causing
		// a spurious error.
		/*
		 * finally {
		 * 
		 * try{ MultiSchemaHibernateUtil.commitAndCloseTransaction(schema); }
		 * catch (org.hibernate.TransactionException te){ // Do nothing } catch
		 * (Exception real_e){ System.out.println("Exception of type: " +
		 * real_e.getClass()); } }
		 */

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

		if (command != null) {
			if (command.equalsIgnoreCase("Create")) {

				simulation.setName(simulation_name);
				simulation.setVersion(simulation_version);
				simulation.setCreation_org(creation_org);
				simulation.setCreator(simcreator);
				simulation.setCopyright_string(simcopyright);

				simulation.createDefaultObjects(schema);

				simulation.saveMe(schema);
			} else if (command.equalsIgnoreCase("Update")) { // 
				String sim_id = (String) request.getParameter("sim_id");
				simulation = Simulation.getMe(schema, new Long(sim_id));
				simulation.setName(simulation_name);
				simulation.setVersion(simulation_version);
				simulation.setCreation_org(creation_org);
				// simulation.setCreator(simcreator);
				simulation.setCopyright_string(simcopyright);

				simulation.saveMe(schema);
			} else if (command.equalsIgnoreCase("Edit")) {
				String sim_id = (String) request.getParameter("sim_id");
				simulation = Simulation.getMe(schema, new Long(sim_id));
			} else if (command.equalsIgnoreCase("Clear")) { // 
				// returning new simulation will clear fields.
			}
		}

		this.sim_id = simulation.getId();

		simulationSelected = true;

		return simulation;
	}

	/**
	 * Gets all game variables that change from round to round, and based on
	 * their type and current conditions, sets them to the new values.
	 */
	public void propagateValues() {

		// TODO
		/*
		 * Vector simBoolVarsVector = new BooleanVariable().
		 * getSimVariablesForARunningSimulation(simulation, runningGame.id);
		 * 
		 * debugStuff += "<BR>propagating " + simBoolVarsVector.size() + "
		 * boolean variables in round " + simulation_round + "<BR>"; //
		 * Propagate their values. for (Enumeration e =
		 * simBoolVarsVector.elements(); e.hasMoreElements();){ BooleanVariable
		 * bv = (BooleanVariable) e.nextElement();
		 * 
		 * debugStuff += bv.propagate(simulation, simulation_round,
		 * runningGame.id); debugStuff += "<hr>"; } // Get set of changing
		 * integer variables for this running simulation Vector simIntVarsVector
		 * = new
		 * IntegerVariable().getSimVariablesForARunningSimulation(simulation,
		 * runningGame.id); // Propagate their values. for (Enumeration e =
		 * simIntVarsVector.elements(); e.hasMoreElements();){ IntegerVariable
		 * iv = (IntegerVariable) e.nextElement();
		 * 
		 * iv.propagate(simulation, simulation_round, runningGame.id); }
		 * 
		 * ///////////////////////////////////////////////// // Get set of
		 * changing budget variables for this running simulation Vector
		 * simBudVarsVector = new
		 * BudgetVariable().getSimVariablesForARunningSimulation(simulation,
		 * runningGame.id); // Select past values for each of these for
		 * (Enumeration e = simBudVarsVector.elements(); e.hasMoreElements();){
		 * BudgetVariable bv = (BudgetVariable) e.nextElement(); // Budgets
		 * propagate by consolidating debits and credits and putting final //
		 * amount in to a 'final' record. This amount is also used as a debit
		 * (or credit) // in the next round, if the budget accumulates. (Some $
		 * just gets spent.) bv.propagate(simulation, simulation_round,
		 * runningGame.id); }
		 */

		// //////////////////////////////////////////////
		// Fire the triggers
		// Get Triggers
	}

	public boolean isLoggedin() {
		return loggedin;
	}

	public void handleCreateActor(HttpServletRequest request) {

		String actorid = "";
		boolean inEditMode = false;

		try {
			MultipartRequest mpr = new MultipartRequest(request,
					USIP_OSP_Properties.getValue("uploads"));

			String update_actor = (String) mpr.getParameter("update_actor");

			actorid = (String) mpr.getParameter("actorid");

			String clear_button = (String) mpr.getParameter("clear_button");

			String create_actor = (String) mpr.getParameter("create_actor");

			if ((update_actor != null)
					&& (update_actor.equalsIgnoreCase("Update Actor"))) {

				actor_id = new Long((String) mpr.getParameter("actorid"));

				Actor actorOnScratchPad = new Actor();
				actorOnScratchPad.setId(actor_id);

				createActor(mpr, actorOnScratchPad);

			} else if ((create_actor != null)
					&& (create_actor.equalsIgnoreCase("Create Actor"))) {
				createActor(mpr, new Actor());

			} else if ((clear_button != null)
					&& (clear_button.equalsIgnoreCase("Clear"))) {
				actor_id = null;
			} else {
				inEditMode = false;
			}
		} catch (java.io.IOException ioe) {
			System.out.println("error in edit actor:" + ioe.getMessage());

			actorid = (String) request.getParameter("actorid");
			if (actorid != null) {
				actor_id = new Long(actorid);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void createActor(MultipartRequest mpr, Actor actorOnScratchPad) {

		try {

			boolean saveActor = false;
			String create_actor = (String) mpr.getParameter("create_actor");
			String update_actor = (String) mpr.getParameter("update_actor");

			System.out.println("create_actor is " + create_actor);
			System.out.println("update_actor is " + update_actor);

			if ((create_actor != null)
					&& (create_actor.equalsIgnoreCase("Create Actor"))
					|| (update_actor != null)
					&& (update_actor.equalsIgnoreCase("Update Actor"))

			) {
				saveActor = true;
			}

			if (saveActor) {
				System.out.println("saving actor");
				makeUploadDir();

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

					for (Enumeration e = mpr.getFileNames(); e
							.hasMoreElements();) {
						String fn = (String) e.nextElement();

						FileIO.saveImageFile("actorImage", actorOnScratchPad
								.getImageFilename(), mpr.getFile(fn));

					}

				} else {
					actorOnScratchPad.setImageFilename("no_image_default.jpg");
				}

				// ////////////////////////////////////////////

				MultiSchemaHibernateUtil.beginTransaction(schema);

				System.out.println("actors id is" + actorOnScratchPad.getId());
				MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(
						actorOnScratchPad);
				MultiSchemaHibernateUtil.getSession(schema).flush();

				String add_to_sim = (String) mpr.getParameter("add_to_sim");

				if ((add_to_sim != null)
						&& (add_to_sim.equalsIgnoreCase("true"))) {

					Simulation simulation = (Simulation) MultiSchemaHibernateUtil
							.getSession(schema).get(Simulation.class, sim_id);

					if (!(simulation.getActors().contains(actorOnScratchPad))) {
						simulation.getActors().add(actorOnScratchPad);
					}

					MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(
							simulation);

				}

				this.actor_name = actorOnScratchPad.getName();
				this.actor_id = actorOnScratchPad.getId();

				MultiSchemaHibernateUtil.getSession(schema).evict(
						actorOnScratchPad);

				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("problem in create actor: " + e.getMessage());
		} finally {
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
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

	/** Its a work in progress. */
	public String getEvents() {

		String eventText = "";

		return eventText;
	}

	public Hashtable<String, String> newsAlerts = new Hashtable<String, String>();

	public int myCount = 0;

	/** */
	public Long getHighestChangeNumberForRunningSim(HttpServletRequest request) {

		if (running_sim_id == null) {
			System.out.println("returning null");
			return null;
		}
		// The conversation is pulled out of the context
		Hashtable<Long, Long> highestChangeNumber = getHashtableForThisRunningSim(request);

		Long runningSimHighestChange = (Long) highestChangeNumber
				.get(running_sim_id);

		if (runningSimHighestChange == null) {
			runningSimHighestChange = new Long(1);
			highestChangeNumber.put(running_sim_id, runningSimHighestChange);

			request.getSession().getServletContext().setAttribute(
					"highestChangeNumber", highestChangeNumber);

		}

		return runningSimHighestChange;
	}

	/**
	 * Stores the next highest change number to let people know if they may need
	 * to pop up a message or refresh some of their web pages.
	 * 
	 * @param request
	 */
	public void storeNewHighestChangeNumber(HttpServletRequest request) {

		Long currentHighest = this.getHighestChangeNumberForRunningSim(request);

		System.out.println("current highest: " + currentHighest.intValue());

		currentHighest = new Long(currentHighest.intValue() + 1);

		Hashtable<Long, Long> highestChangeNumber = getHashtableForThisRunningSim(request);

		highestChangeNumber.put(running_sim_id, currentHighest);

		request.getSession().getServletContext().setAttribute(
				"highestChangeNumber", highestChangeNumber);

	}

	public Hashtable getHashtableForThisRunningSim(HttpServletRequest request) {

		// The conversation is pulled out of the context
		Hashtable<Long, Long> highestChangeNumber = (Hashtable<Long, Long>) request
				.getSession().getServletContext().getAttribute(
						"highestChangeNumber");

		if (highestChangeNumber == null) {
			highestChangeNumber = new Hashtable();
			request.getSession().getServletContext().setAttribute(
					"highestChangeNumber", highestChangeNumber);
		}

		return highestChangeNumber;
	}

	public String getAlarmText(HttpServletRequest request,
			HttpServletResponse response) {

		Long runningSimHighestChange = getHighestChangeNumberForRunningSim(request);

		if (runningSimHighestChange == null) {
			return "";
		}

		boolean doDatabaseCheck = false;

		if (runningSimHighestChange.intValue() > myHighestChangeNumber
				.intValue()) {

			myHighestChangeNumber = new Long(runningSimHighestChange.intValue());

			doDatabaseCheck = true;
		}

		// //////////////////////////////////////////////////////
		// Every two minutes do a new database check anyway.
		myCount += 1;
		if (myCount == 120) {
			doDatabaseCheck = true;
			myCount = 0;
		}
		// ////////////////////////////////////////////////////////

		String alarmText = "";

		if ((running_sim_id != null) && (doDatabaseCheck)) {

			MultiSchemaHibernateUtil.beginTransaction(schema);

			RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil
					.getSession(schema).get(RunningSimulation.class,
							running_sim_id);
			alarmText = checkForAlarm(rs, request);
			doDatabaseCheck = false;

			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		}

		return alarmText;
	}

	/**
	 * 
	 * @param rs
	 * @param request
	 * @return
	 */
	public String checkForAlarm(RunningSimulation rs, HttpServletRequest request) {

		boolean throwAnnouncementAlert = false;
		boolean throwNewsAlert = false;
		boolean throwPhaseChangeAlert = false;

		for (ListIterator<Alert> li = rs.getAlerts().listIterator(); li
				.hasNext();) {
			Alert na = li.next();

			if (newsAlerts.get(na.getId().toString()) == null) {
				// storing it in the hashtable, so alert not tripped on this one
				// again.
				newsAlerts.put(na.getId().toString(), "set");

				// Everyone gets phase change alerts.
				if (na.getType() == Alert.TYPE_PHASECHANGE) {
					throwPhaseChangeAlert = true;
				} else {
					boolean thisUserApplicable = false;

					if (!(na.isSpecific_targets())) {
						thisUserApplicable = true;
					} else {
						thisUserApplicable = na.checkActor(this.actor_id);
					}

					if (thisUserApplicable) {
						if (na.getType() == Alert.TYPE_NEWS) {
							throwNewsAlert = true;
						} else if (na.getType() == Alert.TYPE_ANNOUNCEMENT) {
							throwAnnouncementAlert = true;
						}
					} // end of if this alert is applicable to this user
				} // end of if this is not a phase change alert.
			} // End of if this id of this alert is not null (?)
		}

		if (throwPhaseChangeAlert) {
			return "phase_change";
		} else if (throwNewsAlert) {
			return "news";
		} else if (throwAnnouncementAlert) {
			return "announcement";
		} else {
			return "";
		}
	}

	/**
	 * Creates a general announcement and adds it to the set of announcements
	 * for this running simulation.
	 * 
	 * @param news
	 * @param request
	 * @return
	 */
	public RunningSimulation makeGeneralAnnouncement(String news,
			HttpServletRequest request) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil
				.getSession(schema)
				.get(RunningSimulation.class, running_sim_id);

		Alert al = new Alert();
		al.setType(Alert.TYPE_ANNOUNCEMENT);
		al.setAlertMessage(news);

		rs.getAlerts().add(al);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(al);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(rs);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		// Let people know that there is a change to catch.
		storeNewHighestChangeNumber(request);

		return rs;

	}

	public void makeTargettedAnnouncement(HttpServletRequest request) {

		String targets = list2String(getIdsOfCheckBoxes("actor_cb_", request));

		MultiSchemaHibernateUtil.beginTransaction(schema);

		RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil
				.getSession(schema)
				.get(RunningSimulation.class, running_sim_id);

		Alert al = new Alert();
		al.setSpecific_targets(true);
		al.setType(Alert.TYPE_ANNOUNCEMENT);
		al.setAlertMessage(alertInQueueText);
		al.setThe_specific_targets(targets);

		rs.getAlerts().add(al);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(al);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(rs);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		// Let people know that there is a change to catch.
		storeNewHighestChangeNumber(request);

		this.alertInQueueText = "";
		this.alertInQueueType = 0;

	}

	/**
	 * Returns all of the announcements for the running simulation.
	 * 
	 * @return
	 */
	public List getAllAnnouncements() {

		List returnList = new ArrayList();
		MultiSchemaHibernateUtil.beginTransaction(schema);

		RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil
				.getSession(schema)
				.get(RunningSimulation.class, running_sim_id);

		for (ListIterator li = rs.getAlerts().listIterator(); li.hasNext();) {
			Alert al = (Alert) li.next();
			System.out.println(al.getAlertMessage());
			returnList.add(al);
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	/**
	 * Returns the phase name stored in the web cache.
	 * 
	 * @return
	 */
	public String getPhaseName() {

		Hashtable<Long, String> phaseNames = (Hashtable<Long, String>) session
				.getServletContext().getAttribute("phaseNames");

		if (running_sim_id != null) {
			phaseName = phaseNames.get(running_sim_id);

			return phaseName;
		} else {
			return "";
		}
	}

	public String getSimulation_round() {

		Hashtable<Long, String> roundNames = (Hashtable<Long, String>) session
				.getServletContext().getAttribute("roundNames");

		if (running_sim_id != null) {
			simulation_round = roundNames.get(running_sim_id);

			return simulation_round;

		} else {
			return "";
		}

	}

	public Simulation giveMeSim() {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		Simulation simulation = (Simulation) MultiSchemaHibernateUtil
				.getSession(schema).get(Simulation.class, sim_id);

		// ///////////////
		// Stupidly, we must do this. Hiberate requires it odes here
		List pList = simulation.getPhases();

		for (ListIterator<SimulationPhase> li = pList.listIterator(); li
				.hasNext();) {
			SimulationPhase sp = li.next();

			System.out.println(sp.getName());
		}
		// ///////////////

		MultiSchemaHibernateUtil.getSession(schema).evict(simulation);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		return simulation;
		
	}
	

	public RunningSimulation giveMeRunningSim() {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil
				.getSession(schema)
				.get(RunningSimulation.class, running_sim_id);

		MultiSchemaHibernateUtil.getSession(schema).evict(rs);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return rs;
	}

	public Actor giveMeActor() {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		Actor actor = (Actor) MultiSchemaHibernateUtil.getSession(schema).get(
				Actor.class, actor_id);

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
	 * @return
	 */
	public User giveMeUser() {

		return User.getUser(schema, this.user_id);

	}

	public void addActorToSim(String sim_id, String actor_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		Long s_id = new Long(sim_id);
		Simulation sim = (Simulation) MultiSchemaHibernateUtil.getSession(
				schema).get(Simulation.class, s_id);

		Long a_id = new Long(actor_id);
		Actor act = (Actor) MultiSchemaHibernateUtil.getSession(schema).get(
				Actor.class, a_id);

		sim.getActors().add(act);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(sim);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}

	public void removeActorFromSim(String sim_id, String actor_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		Long s_id = new Long(sim_id);
		Simulation sim = (Simulation) MultiSchemaHibernateUtil.getSession(
				schema).get(Simulation.class, s_id);

		Long a_id = new Long(actor_id);
		Actor act = (Actor) MultiSchemaHibernateUtil.getSession(schema).get(
				Actor.class, a_id);

		sim.getActors().remove(act);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(sim);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public boolean isSimCreator() {
		return isSimCreator;
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
			cs = cs.makeCopy(schema);
		}

		cs.setRec_tab_heading(request.getParameter("tab_heading"));

		if (cs.getContents() == null) {
			cs.setContents(new Hashtable());
		}

		for (Enumeration e = cs.getMeta_content().keys(); e.hasMoreElements();) {

			String key = (String) e.nextElement();

			String this_value = request.getParameter(key);

			cs.getContents().put(key, this_value);

		}

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(cs);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return cs;

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
					.getConv_actor_assigns().listIterator(); liiii.hasNext();) {
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

	public void fillReadWriteLists() {

		Simulation sim = this.giveMeSim();

		// Get the list of actors
		List actorList = sim.getActors();

		List phaseList = sim.getPhases();

		// Loop over phases
		for (ListIterator plist = phaseList.listIterator(); plist.hasNext();) {
			SimulationPhase sp = (SimulationPhase) plist.next();
			// Loop over actors
			for (ListIterator alist = actorList.listIterator(); alist.hasNext();) {
				Actor act = (Actor) alist.next();

				System.out.println("checking read write on " + act.getName());
				List setOfSections = SimulationSection
						.getBySimAndActorAndPhase(schema, this.sim_id, act
								.getId(), sp.getId());

				for (ListIterator slist = setOfSections.listIterator(); slist
						.hasNext();) {
					SimulationSection ss = (SimulationSection) slist.next();

					CustomizeableSection custSec = CustomizeableSection.getMe(
							schema, ss.getBase_section_id() + "");

					if (custSec != null) {
						System.out.println("cs id: " + ss.getBase_section_id());
						System.out.println("bss rec tab: "
								+ custSec.getRec_tab_heading());
						System.out.println("can read "
								+ custSec.isConfers_read_ability());

						if (custSec.isConfers_read_ability() == true) {
							Hashtable storedGoodies = custSec.getContents();
							String docs = (String) storedGoodies
									.get(SharedDocument.DOCS_IN_HASHTABLE_KEY);

							String currentActors = (String) ActorsWithReadAccess
									.get(docs);

							if (currentActors == null) {
								currentActors = act.getId().toString();
							} else {
								currentActors += "," + act.getId();
							}

							ActorsWithReadAccess.put(docs, currentActors);

							System.out.println("docs were : " + currentActors);
						}

						if (custSec.isConfers_write_ability() == true) {
							System.out.println("confers read and write");
							Hashtable storedGoodies = custSec.getContents();
							String docs = (String) storedGoodies
									.get(SharedDocument.DOCS_IN_HASHTABLE_KEY);
							System.out.println("docs were : " + docs);
						}
					}
					System.out
							.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

				}

			} // End of loop over actors
		}

	}


	/**
	 * returns a list of strings containing the value ( generally assumed to be
	 * an id) from the checkboxes of a form.
	 */
	public List getIdsOfCheckBoxes(String tagString, HttpServletRequest request) {

		ArrayList returnList = new ArrayList();

		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			String param_name = (String) e.nextElement();

			if (param_name.startsWith(tagString)) {
				if ((request.getParameter(param_name) != null)
						&& (request.getParameter(param_name)
								.equalsIgnoreCase("true"))) {
					String this_a_id = param_name.replaceFirst(tagString, "");

					returnList.add(this_a_id);
				}
			}
		}

		return returnList;
	}

	/** Takes a list and turns it into a comma separated string. */
	public String list2String(List idList) {

		String returnString = "";

		for (ListIterator<String> li = idList.listIterator(); li.hasNext();) {
			String s = (String) li.next();

			returnString += s;
			if (li.hasNext()) {
				returnString += ",";
			}
		}

		return returnString;

	}

	public String getDefaultSimXMLFileName(Simulation simulation) {

		Date saveDate = new java.util.Date();

		SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyyyy");

		String fileName = simulation.getName() + "_" + simulation.getVersion()
				+ "_" + sdf.format(saveDate);

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

		FileIO.saveSimulationXMLFile(ObjectPackager.packageSimulation(schema,
				new Long(_sim_id)), fileName);

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
	public void handlePublishing(String command, String sim_key_words) {

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

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public String validateLoginToSimAuthoringTool(HttpServletRequest request) {

		System.out.println("attemptin validatin");

		loggedin = false;

		String sendToPage = "index.jsp";

		BaseUser bu = validate(request);

		if (bu != null) {

			System.out.println("bu id " + bu.getId());
			user_id = bu.getId();
			System.out.println("pso id " + user_id);

			if (bu.getAuthorizedSchemas().size() == 0) {
				errorMsg = "You are not authorized to enter any databases.";
			} else if (bu.getAuthorizedSchemas().size() == 1) {
				// Send them on directly to this schema
				SchemaGhost sg = (SchemaGhost) bu.getAuthorizedSchemas().get(0);
				System.out.println("ghost schema is " + sg.getSchema_name());
				schema = sg.getSchema_name();
				User user = loginToSchema(user_id, sg.getSchema_name(), request);

				if (user.isSim_author()) {
					loggedin = true;
					request.getSession().setAttribute("author", "true");
					sendToPage = "intro.jsp";
				} else if (user.isSim_instructor()) {
					loggedin = true;
					sendToPage = "../simulation_facilitation/instructor_home.jsp";
				} else {
					errorMsg = "Not authorized to author or facilitate simulations.";
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
	
	public boolean handleRetrievePassword(HttpServletRequest request){
		
		String sending_page = (String) request.getParameter("sending_page");
		
		if ( (sending_page != null) && (sending_page.equalsIgnoreCase("retrieve_password"))){
			
			String email = (String) request.getParameter("email");	
			
			BaseUser bu = BaseUser.getByUsername(email);
			
			if (bu == null){ 
				this.errorMsg = "User not found in database";
				return false;
			} else {
				this.errorMsg = "";
			}
			
			System.out.println("emailing " + email);
			
			String message = "A request for your password has been received. Your password is " +
				bu.getPassword();
			
			String admin_email = USIP_OSP_Properties.getValue("osp_admin_email");
			
			Vector ccs = new Vector();
			Vector bccs = new Vector();
			bccs.add(admin_email);
			
			
			// Still working on this puzzle.
			if (true){
				this.errorMsg = "functionality not implemented";
				return false;
			}
			/*
			 * Still working on this. Do we need to figure what schema they want access to?
			bcc.add(ssytem);
			
			Emailer.postMail(get schema_id, email, "Access to OSP", message, 
					String from, ccs, bccs);
			*/
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

		System.out.println("attemptin validatin");

		loggedin = false;

		String sendToPage = "index.jsp";

		BaseUser bu = validate(request);

		if (bu != null) {

			System.out.println("bu id " + bu.getId());
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
	public String stringListToNames(HttpServletRequest request, String id_list,
			String separator) {

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
	
	/**
	 * 
	 * @param request
	 */
	public void handleMakeAnnouncement(HttpServletRequest request){
		
		RunningSimulation rs = giveMeRunningSim();
		
		String sending_page = (String) request.getParameter("sending_page");
		String add_news = (String) request.getParameter("add_news");
		
		if ( (sending_page != null) && (add_news != null) && (sending_page.equalsIgnoreCase("add_news"))){
	          
			String announcement_text = (String) request.getParameter("announcement_text");
			
			String player_target = (String) request.getParameter("player_target");
			
			if ((player_target != null) && (player_target.equalsIgnoreCase("some"))){
				alertInQueueText = announcement_text;
				alertInQueueType = Alert.TYPE_ANNOUNCEMENT;
				backPage = "make_announcement.jsp";
				this.forward_on = true;
				return;
			} else {
				rs = makeGeneralAnnouncement(announcement_text, request);
			}
			
			   
		} // End of if coming from this page and have added announcement.
		
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

		Hashtable<String, String> actor_names = (Hashtable<String, String>) context
				.getAttribute("actor_names");

		if (actor_names == null) {
			actor_names = new Hashtable<String, String>();
			context.setAttribute("actor_names", actor_names);
		}

		String a_name = actor_names.get(schema + "_" + running_sim_id + " "
				+ a_id);
		if (a_name == null) {
			loadActorNamesInHashtable(actor_names);
			a_name = actor_names
					.get(schema + "_" + running_sim_id + " " + a_id);
			context.setAttribute("actor_names", actor_names);
		}

		return a_name;
	}

	public void loadActorNamesInHashtable(Hashtable actor_names) {

		System.out.println("storing names in hashtable. ");
		Simulation sim = this.giveMeSim();

		for (ListIterator<Actor> li = sim.getActors().listIterator(); li
				.hasNext();) {
			Actor act = li.next();

			actor_names.put(schema + "_" + running_sim_id + " " + act.getId(),
					act.getName());

		}
	}
	
	/** 
	 * Checks for authorization, and then passes the request to the PSO_UserAdmin object.
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
	
	public User handleCreateUser(HttpServletRequest request) {
		PSO_UserAdmin pu = new PSO_UserAdmin(this);
		return pu.handleCreateUser(request, schema);
	}
	
	public void handleMyProfile(HttpServletRequest request){
		PSO_UserAdmin pu = new PSO_UserAdmin(this);
		pu.handleMyProfile(request, user_id);
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public Long actor_being_worked_on_id;

	
	/**
	 * A helper object to contain the work done in creating a section.
	 */
	private PSO_SectionMgmt pso_sm;
	
	/**
	 * Keep just one persistent copy of the PSO_SectionMgmt object.
	 * @return
	 */
	public PSO_SectionMgmt getMyPSO_SectionMgmt(){
		if (pso_sm == null) {
			pso_sm = new PSO_SectionMgmt(this);
		}
		
		return pso_sm;
	}

	/**
	 * A wrapper that passes the request through to the associated PSO_SectionMgmt object.
	 * @param request
	 * @return
	 */
	public Simulation handleSetUniversalSimSectionsPage(
			HttpServletRequest request) {
		
		return (getMyPSO_SectionMgmt().handleSetUniversalSimSectionsPage(request));
	}
	
	/**
	 * A wrapper that passes the request through to the associated PSO_SectionMgmt object.
	 * @param request
	 * @return
	 */
	public String handleSimSectionsRouter(HttpServletRequest request) {
		return (getMyPSO_SectionMgmt().handleSimSectionsRouter(request));
	}
	
	/**
	 * A wrapper that passes the request through to the associated PSO_SectionMgmt object.
	 * @param request
	 * @return
	 */
	public Simulation handleSetSimSectionsPage(HttpServletRequest request) {
		return (getMyPSO_SectionMgmt().handleSetSimSectionsPage(request));
	}
	
	/**
	 * A wrapper that passes the request through to the associated PSO_SectionMgmt object.
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleMekeImagePage(HttpServletRequest request) {
		return (getMyPSO_SectionMgmt().handleMekeImagePage(request));
	}
	
	/**
	 * A wrapper that passes the request through to the associated PSO_SectionMgmt object.
	 * @param request
	 */
	public void handleMakeReadDocumentPage(HttpServletRequest request) {
		getMyPSO_SectionMgmt().handleMakeReadDocumentPage(request);
	}
	
	/**
	 * A wrapper that passes the request through to the associated PSO_SectionMgmt object.
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleMakeWriteDocumentPage(
			HttpServletRequest request) {
	
		return (getMyPSO_SectionMgmt().handleMakeWriteDocumentPage(request));
	}
	
	/**
	 * A wrapper that passes the request through to the associated PSO_SectionMgmt object.
	 * @param request
	 * @return
	 */
	public Simulation handleMakeCaucusPage(HttpServletRequest request) {
		return (getMyPSO_SectionMgmt().handleMakeCaucusPage(request));
	}
	
	/**
	 * A wrapper that passes the request through to the associated PSO_SectionMgmt object.
	 * @param request
	 */
	public void handleMakePrivateChatPage(HttpServletRequest request) {
		getMyPSO_SectionMgmt().handleMakePrivateChatPage(request);
	}
	
	/**
	 * A wrapper that passes the request through to the associated PSO_SectionMgmt object.
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleMakeReflectionPage(
			HttpServletRequest request) {
		
		return (getMyPSO_SectionMgmt().handleMakeReflectionPage(request));
	}
	
}
