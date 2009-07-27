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
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.usip.osp.specialfeatures.*;

import com.oreilly.servlet.MultipartRequest;

/**
 * This object contains all of the session information for the participant and
 * is the main interface to all of the java objects that the participant will
 * interact with.
 * 
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
public class ParticipantSessionObject {

	/** Determines if actor is logged in. */
	private boolean loggedin = false;

	/** */
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

	/** ID of Simulation being conducted or worked on. */
	public Long sim_id;

	/** ID of the Running Simulation being conducted or worked on. */
	public Long running_sim_id;

	/** Name of the running simulation session. */
	public String run_sim_name = "";

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

	public List tempSimSecList = new ArrayList();

	/** Text of alert being worked on. */
	public String alertInQueueText = "";

	/** Type of alert being worked on. */
	public int alertInQueueType = 0;

	/** Login ticket of this user. */
	public LoggedInTicket myLoggedInTicket = new LoggedInTicket();

	public String tabposition = "1";

	public String bottomFrame = "";

	public static String DEFAULTMEMOTEXT = "To: <BR />From:<BR />Topic:<BR />Message:";

	public String memo_starter_text = DEFAULTMEMOTEXT;

	/**
	 * This is called from the top of the players frame to determine where they
	 * should go.
	 * 
	 * @param request
	 */
	public void handleSimWeb(HttpServletRequest request) {

		tabposition = (String) request.getParameter("tabposition");

		bottomFrame = "frame_bottom.jsp";

		int tabpos = 1;

		try {
			tabpos = new Integer(tabposition).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			List simSecList = SimulationSectionAssignment.getBySimAndActorAndPhase(schema, sim_id, actor_id, phase_id);

			if (tabpos <= simSecList.size()) {
				SimulationSectionAssignment ss = (SimulationSectionAssignment) simSecList.get(tabpos - 1);
				bottomFrame = ss.generateURLforBottomFrame(running_sim_id, actor_id, user_id);
			}

		} catch (Exception e) {
			e.printStackTrace();
			forward_on = true;
		}

	}

	public Hashtable pushedInjects = new Hashtable();

	public String getInjectColor(Long injectId) {

		String unshotColor = "#FFFFFF";
		String shotColor = "#FFCCCC";

		String hashValue = (String) pushedInjects.get(injectId);

		if (hashValue == null) {
			return unshotColor;
		} else {
			return shotColor;
		}

	}

	public boolean handlePushInject(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null) && (sending_page.equalsIgnoreCase("push_injects"))) {

			String announcement_text = (String) request.getParameter("announcement_text");
			String inject_action = (String) request.getParameter("inject_action");
			String inject_id_string = (String) request.getParameter("inject_id");

			if (inject_id_string != null) {
				Long inject_id = new Long(inject_id_string);
				pushedInjects.put(inject_id, "set");
			}

			if ((inject_action != null) && (inject_action.equalsIgnoreCase("2"))) {
				announcement_text = announcement_text
						+ "<BR /><strong>Communicate with Control your actions</strong><BR />";
			}

			String player_target = (String) request.getParameter("player_target");

			if ((player_target != null) && (player_target.equalsIgnoreCase("some"))) {
				alertInQueueText = announcement_text;
				alertInQueueType = Alert.TYPE_EVENT;
				return true;
			} else {
				makeGeneralAnnouncement(announcement_text, request);
				return false;
			}

		}

		return false;

	}

	/**
	 * Unpacks a simulation from an XML file.
	 * 
	 * @param request
	 */
	public Simulation handleUnpackDetails(HttpServletRequest request) {

		String filename = (String) request.getParameter("filename");

		System.out.println("unpacking " + filename);

		return ObjectPackager.unpackSimDetails(filename, schema);

	}

	/**
	 * Unpacks a simulation from an XML file.
	 * 
	 * @param request
	 */
	public void handleUnpackSimulation(HttpServletRequest request) {

		String filename = (String) request.getParameter("filename");
		String sim_name = (String) request.getParameter("sim_name");
		String sim_version = (String) request.getParameter("sim_version");

		System.out.println("unpacking " + filename);

		ObjectPackager.unpackSim(filename, schema, sim_name, sim_version);

	}

	public void handleWriteAAR(HttpServletRequest request) {

		String command = (String) request.getParameter("command");
		String sending_page = (String) request.getParameter("sending_page");

		if (sending_page != null) {
			if (command != null) {
				if (command.equalsIgnoreCase("Save Changes")) {
					saveAarText(request);
				}
			}
		}

	}

	/**
	 * Handles the termination of a simulation.
	 * 
	 * @param request
	 */
	public void handleEndSim(HttpServletRequest request) {

		String command = (String) request.getParameter("command");
		String sending_page = (String) request.getParameter("sending_page");

		if (sending_page != null) {
			if (command != null) {
				if (command.equalsIgnoreCase("End Simulation")) {

					// Mark Completed, change phase
					Simulation sim = this.giveMeSim();
					System.out.println("forwarin on to : " + sim.getLastPhaseId(schema).toString());
					this.changePhase(sim.getLastPhaseId(schema).toString(), request);

					// Forward them back
					forward_on = true;
					backPage = "../simulation/simwebui.jsp?tabposition=1";
				}
			}
		}

	}

	private void saveAarText(HttpServletRequest request) {
		String write_aar_end_sim = (String) request.getParameter("write_aar_end_sim");
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
	public SimulationPhase handleCreateOrUpdatePhase(Simulation sim, HttpServletRequest request) {

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
				SimPhaseAssignment spa = new SimPhaseAssignment(schema, sim.getId(), returnSP.getId());

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

				String user_id = (String) request.getParameter("user_to_add_to_simulation");
				String actor_id = (String) request.getParameter("actor_to_add_to_simulation");
				String sim_id = (String) request.getParameter("simulation_adding_to");
				String running_sim_id = (String) request.getParameter("running_simulation_adding_to");

				UserAssignment ua = UserAssignment.getUniqueUserAssignment(schema, new Long(sim_id), new Long(
						running_sim_id), new Long(actor_id), new Long(user_id));
			}
		}
	}

	public String setOfUsers = "";
	public String invitationCode = "";

	public String getDefaultInviteMessage() {
		String defaultInviteEmailMsg = "Dear Student,\r\n";
		defaultInviteEmailMsg += "Please go to the web site ";
		defaultInviteEmailMsg += USIP_OSP_Properties.getValue("simulation_url")
				+ "/simulation_user_admin/auto_registration_form.jsp and register yourself.\r\n\r\n";
		defaultInviteEmailMsg += "Thank you,\r\n";
		defaultInviteEmailMsg += this.user_Display_Name;

		return defaultInviteEmailMsg;

	}

	/**
	 * 
	 * @param request
	 */
	public void handleBulkInvite(HttpServletRequest request) {
		setOfUsers = (String) request.getParameter("setOfUsers");
		String thisInviteEmailMsg = (String) request.getParameter("defaultInviteEmailMsg");
		invitationCode = (String) request.getParameter("invitationCode");

		Long schema_id = SchemaInformationObject.lookUpId(schema);

		for (ListIterator<String> li = getSetOfEmails(setOfUsers).listIterator(); li.hasNext();) {
			String this_email = (String) li.next();

			if (BaseUser.checkIfUserExists(this_email)) {
				System.out.println("exists:" + this_email);
				// ?? make sure exists in this schema

			} else {

				System.out.println("does not exist:" + this_email);

				// Add entry into system to all them to register.
				UserRegistrationInvite uri = new UserRegistrationInvite(user_name, this_email, invitationCode, schema);

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
		bcced.add(user_name);

		SchemaInformationObject sio = SchemaInformationObject.lookUpSIOByName(schema);

		Emailer.postMail(sio, the_email, subject, message, user_name, cced, bcced);
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

		String deletion_confirm = (String) request.getParameter("deletion_confirm");
		if ((deletion_confirm != null) && (deletion_confirm.equalsIgnoreCase("Submit"))) {

			Long o_id = new Long(objid);

			if (objectType.equalsIgnoreCase("simulation")) {
				MultiSchemaHibernateUtil.beginTransaction(schema);
				Simulation sim = (Simulation) MultiSchemaHibernateUtil.getSession(schema).get(Simulation.class, o_id);
				MultiSchemaHibernateUtil.getSession(schema).delete(sim);
				this.sim_id = null;

				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			} else if (objectType.equalsIgnoreCase("phase")) {
				MultiSchemaHibernateUtil.beginTransaction(schema);
				SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil.getSession(schema).get(
						SimulationPhase.class, o_id);
				Long phase_removed_id = sp.getId();
				MultiSchemaHibernateUtil.getSession(schema).delete(sp);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
				
				SimPhaseAssignment.removeMe(schema, new Long(phase_sim_id), phase_removed_id);

			} else if (objectType.equalsIgnoreCase("actor")) {
				MultiSchemaHibernateUtil.beginTransaction(schema);
				Actor act = (Actor) MultiSchemaHibernateUtil.getSession(schema).get(Actor.class, o_id);

				if (actor_id != null) {
					if (actor_id.intValue() == act.getId().intValue()) {
						actor_id = null;
					}
				}

				MultiSchemaHibernateUtil.getSession(schema).delete(act);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
			} else if (objectType.equalsIgnoreCase("inject")) {

				MultiSchemaHibernateUtil.beginTransaction(schema);
				Inject inject = (Inject) MultiSchemaHibernateUtil.getSession(schema).get(Inject.class, o_id);

				MultiSchemaHibernateUtil.getSession(schema).delete(inject);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			} else if (objectType.equalsIgnoreCase("sim_section")) {
				MultiSchemaHibernateUtil.beginTransaction(schema);
				SimulationSectionAssignment ss = (SimulationSectionAssignment) MultiSchemaHibernateUtil.getSession(
						schema).get(SimulationSectionAssignment.class, o_id);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

				SimulationSectionAssignment.removeAndReorder(schema, ss);

			} else if (objectType.equalsIgnoreCase("user_assignment")) {
				MultiSchemaHibernateUtil.beginTransaction(schema);
				UserAssignment ua = (UserAssignment) MultiSchemaHibernateUtil.getSession(schema).get(
						UserAssignment.class, o_id);
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

				String email_users = (String) request.getParameter("email_users");
				String email_text = (String) request.getParameter("email_text");

				BaseUser bu = BaseUser.getByUserId(user_id);

				MultiSchemaHibernateUtil.beginTransaction(schema);

				User user = (User) MultiSchemaHibernateUtil.getSession(schema).get(User.class, user_id);

				RunningSimulation running_sim = (RunningSimulation) MultiSchemaHibernateUtil.getSession(schema).get(
						RunningSimulation.class, running_sim_id);

				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

				running_sim.enableAndPrep(schema, sim_id.toString(), bu.getUsername(), email_users, email_text);

			} // End of if coming from this page and have enabled the sim
			// ////////////////////////////
		}

	}

	/**
	 * 
	 * @param request
	 */
	public void handleEnterInstructorRatings(HttpServletRequest request) {

		String num_stars = (String) request.getParameter("num_stars");
		String user_comments = (String) request.getParameter("user_comments");
		String user_stated_name = (String) request.getParameter("user_stated_name");

		SimulationRatings sr = SimulationRatings.getInstructorRatingsBySimAndUser(schema, sim_id, user_id);

		sr.setNumberOfStars(new Long(num_stars).intValue());
		sr.setSim_id(sim_id);
		sr.setUser_id(user_id);
		sr.setUsers_stated_name(user_stated_name);
		sr.setUser_comments(user_comments);
		sr.setComment_type(SimulationRatings.INSTRUCTOR_COMMENT);

		sr.saveMe(schema);

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
				BaseSimSection.readInXMLFile(schema, new File(fullfileloc));

			} else if (command.equalsIgnoreCase("Reload")) {
				System.out.println("Will be reloading file from: " + fullfileloc);
				BaseSimSection.reloadXMLFile(schema, new File(fullfileloc), new Long(loaded_id));
				// save
			} else if (command.equalsIgnoreCase("Unload")) {
				System.out.println("Will be unloading bss id: " + loaded_id);
				BaseSimSection.removeBSS(schema, loaded_id);
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
				BaseSimSection.readInXMLFile(schema, new File(fullfileloc));

			} else if (command.equalsIgnoreCase("Reload")) {
				System.out.println("Will be reloading file from: " + fullfileloc);
				BaseSimSection.reloadXMLFile(schema, new File(fullfileloc), new Long(loaded_id));
				// save
			} else if (command.equalsIgnoreCase("Unload")) {
				System.out.println("Will be unloading bss id: " + loaded_id);
				BaseSimSection.removeBSS(schema, loaded_id);
			}
		}
	}

	/**
	 * Loads a session.
	 * 
	 * @param request
	 */
	public void handleLoadPlayerAutoAssignedScenario(HttpServletRequest request) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		this.sim_id = new Long((String) request.getParameter("sim_id"));
		Simulation simulation = (Simulation) MultiSchemaHibernateUtil.getSession(schema).get(Simulation.class, sim_id);

		this.actor_id = new Long((String) request.getParameter("actor_id"));
		Actor actor = (Actor) MultiSchemaHibernateUtil.getSession(schema).get(Actor.class, actor_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		RunningSimulation rs = new RunningSimulation("My Session", this.giveMeSim(), schema);
		this.running_sim_id = rs.getId();
		rs.setReady_to_begin(true);

		MultiSchemaHibernateUtil.beginTransaction(schema);
		SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil.getSession(schema).get(SimulationPhase.class,
				rs.getPhase_id());
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		this.loadSimInfoForDisplay(request, simulation, rs, actor, sp);

	}

	public void loadSimInfoForDisplay(HttpServletRequest request, Simulation simulation, RunningSimulation running_sim,
			Actor actor, SimulationPhase sp) {
		this.simulation_name = simulation.getName();
		this.sim_copyright_info = simulation.getCopyright_string();
		this.simulation_version = simulation.getVersion();
		this.simulation_org = simulation.getCreation_org();

		this.run_sim_name = running_sim.getName();
		this.simulation_round = running_sim.getRound() + "";
		this.phase_id = running_sim.getPhase_id();

		this.actor_name = actor.getName();

		this.phaseName = sp.getName();

		loadPhaseNameInWebCache(request, sp);

	}

	public void loadPhaseNameInWebCache(HttpServletRequest request, SimulationPhase sp) {
		// //////////////////////////////////////////////////////////////////////
		// Store it in the web cache, if this has not been done already
		// by another user.
		Hashtable<Long, String> phaseNames = (Hashtable<Long, String>) session.getServletContext().getAttribute(
				USIP_OSP_ContextListener.CACHEON_L_S_PHASE_NAMES);

		String cachedPhaseName = phaseNames.get(running_sim_id);
		if (cachedPhaseName == null) {
			phaseNames.put(running_sim_id, sp.getName());
			request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_L_S_PHASE_NAMES, phaseNames);

		}
	}

	/**
	 * 
	 * @param request
	 */
	public void handleLoadPlayerScenario(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null) && (sending_page.equalsIgnoreCase("select_simulation"))) {

			schema = (String) request.getParameter("schema");
			schemaOrg = (String) request.getParameter("schema_org");

			session = request.getSession();

			MultiSchemaHibernateUtil.beginTransaction(schema);

			String user_assignment_id = (String) request.getParameter("user_assignment_id");

			UserAssignment ua = (UserAssignment) MultiSchemaHibernateUtil.getSession(schema).get(UserAssignment.class,
					new Long(user_assignment_id));

			sim_id = ua.getSim_id();
			Simulation simulation = (Simulation) MultiSchemaHibernateUtil.getSession(schema).get(Simulation.class,
					sim_id);

			running_sim_id = ua.getRunning_sim_id();
			RunningSimulation running_sim = (RunningSimulation) MultiSchemaHibernateUtil.getSession(schema).get(
					RunningSimulation.class, running_sim_id);

			actor_id = ua.getActor_id();
			Actor actor = (Actor) MultiSchemaHibernateUtil.getSession(schema).get(Actor.class, actor_id);

			SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil.getSession(schema).get(
					SimulationPhase.class, running_sim.getPhase_id());

			// Load information from the pertinent objects to be displayed.
			loadSimInfoForDisplay(request, simulation, running_sim, actor, sp);

			// ////////////////////////////////////////////////////////////////////////
			Hashtable<Long, String> roundNames = new Hashtable();
			try {
				roundNames = (Hashtable<Long, String>) 
					session.getServletContext().getAttribute(USIP_OSP_ContextListener.CACHEON_L_S_ROUND_NAMES);
			} catch (Exception e) {
				e.printStackTrace();
				roundNames = new Hashtable<Long, String>();
				session.getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_L_S_ROUND_NAMES, new Hashtable<Long, String>());
			}
			String cachedRoundName = roundNames.get(running_sim_id);
			if (cachedRoundName == null) {
				roundNames.put(running_sim_id, simulation_round);
				request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_L_S_ROUND_NAMES, roundNames);
			}
			// ///////////////////////////////////////////////////////////

			loadPhaseNameInWebCache(request, sp);


			// //////////////////////////////////////////////////////////////////////
			// Store it in the web cache, if this has not been done already
			// by another user.
			Hashtable<Long, Long> phaseIds = (Hashtable<Long, Long>) session.getServletContext().getAttribute(
					USIP_OSP_ContextListener.CACHEON_PHASE_IDS);

			Long cachedPhaseId = phaseIds.get(running_sim_id);
			if (cachedPhaseId == null) {
				phaseIds.put(running_sim_id, phase_id);
				request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_PHASE_IDS, phaseIds);

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

			UserTrail ut = UserTrail.getMe(schema, myLoggedInTicket.getTrail_id());
			ut.setActor_id(actor_id);
			ut.setRunning_sim_id(running_sim_id);
			ut.saveMe(schema);
			
			this.hasSelectedRunningSim = true;
			
			forward_on = true;
			

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
	 * Handles the creation of documents to be added to the simulation.
	 * 
	 * @param request
	 */
	public SharedDocument handleCreateDocument(HttpServletRequest request) {

		SharedDocument sd = new SharedDocument();

		String shared_doc_id = (String) request.getParameter("shared_doc_id");

		if ((shared_doc_id != null) && (shared_doc_id.trim().length() > 0)) {

			sd = SharedDocument.getMe(schema, new Long(shared_doc_id));
		}

		String sending_page = (String) request.getParameter("sending_page");
		String save_page = (String) request.getParameter("save_page");

		if ((sending_page != null) && (sending_page.equalsIgnoreCase("make_create_document_page"))) {
			String uniq_doc_title = (String) request.getParameter("uniq_doc_title");
			String doc_display_title = (String) request.getParameter("doc_display_title");
			String doc_starter_text = (String) request.getParameter("doc_starter_text");

			System.out.println("creating doc of uniq title: " + uniq_doc_title);
			sd = new SharedDocument(uniq_doc_title, doc_display_title, sim_id);
			sd.setBigString(doc_starter_text);
			sd.saveMe(schema);

		}

		return sd;

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

		if (false && (checkDatabaseCreated()) && (!(isLoggedin()))) {

			this.forward_on = true;
			return "";
		}

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
	 * 
	 * @param request
	 * @return
	 */
	public BaseUser validate(HttpServletRequest request) {

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		BaseUser bu = BaseUser.validateUser(username, password);

		return bu;

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

	/** Gets called when the user has selected a scenario to play. */
	public void storeUserInfoInSessionInformation(HttpServletRequest request) {

		Hashtable<Long, Hashtable> loggedInPlayers = (Hashtable<Long, Hashtable>) request.getSession()
				.getServletContext().getAttribute("loggedInPlayers");

		Hashtable thisSetOfPlayers = loggedInPlayers.get(this.running_sim_id);

		if (thisSetOfPlayers == null) {
			thisSetOfPlayers = new Hashtable();
			loggedInPlayers.put(this.running_sim_id, thisSetOfPlayers);
		}

		thisSetOfPlayers.put(this.myLoggedInTicket.getTrail_id(), myLoggedInTicket);

		request.getSession().getServletContext().setAttribute("loggedInPlayers", loggedInPlayers);

	}

	/**
	 * Returns the PSO stored in the session, or creates one. The coder can
	 * indicated if he or she wants to start a transaction.
	 */
	public static ParticipantSessionObject getPSO(HttpSession session, boolean getConn) {

		ParticipantSessionObject pso = (ParticipantSessionObject) session.getAttribute("pso");

		if (pso == null) {
			System.out.println("pso is new");
			pso = new ParticipantSessionObject();
			pso.session = session;
		}

		session.setAttribute("pso", pso);

		return pso;
	}

	public List<SimulationSectionGhost> getSimSecList(HttpServletRequest request) {

		session = request.getSession();

		// Get phase id from the cache
		Hashtable<Long, Long> phaseIds = (Hashtable<Long, Long>) session.getServletContext().getAttribute(USIP_OSP_ContextListener.CACHEON_PHASE_IDS);

		Long cachedPhaseId = phaseIds.get(running_sim_id);

		if (cachedPhaseId != null) {
			phase_id = cachedPhaseId;
		}

		String hashKey = sim_id + "_" + actor_id + "_" + phase_id;

		Hashtable<String, List<SimulationSectionGhost>> sim_section_info = (Hashtable<String, List<SimulationSectionGhost>>) session
				.getServletContext().getAttribute("sim_section_info");

		if (sim_section_info == null) {
			sim_section_info = new Hashtable<String, List<SimulationSectionGhost>>();
		}

		List<SimulationSectionGhost> returnList = sim_section_info.get(hashKey);

		if (returnList == null) {

			returnList = new ArrayList<SimulationSectionGhost>();

			// Get full list from database hit
			List<SimulationSectionAssignment> fullList = SimulationSectionAssignment.getBySimAndActorAndPhase(schema,
					sim_id, actor_id, phase_id);

			// Copy the needed parts of that list into the ghosts
			for (ListIterator<SimulationSectionAssignment> li = fullList.listIterator(); li.hasNext();) {
				SimulationSectionAssignment ss = li.next();

				SimulationSectionGhost ssg = new SimulationSectionGhost();

				ssg.setTabHeading(ss.getTab_heading());
				ssg.setTabColor(ss.getTabColor());

				returnList.add(ssg);

			}
			// Store that list into the Context
			sim_section_info.put(hashKey, returnList);

			session.getServletContext().setAttribute("sim_section_info", sim_section_info);

		}

		return returnList;
	}

	/**
	 * Advances game round, and propogates values to new round.
	 * 
	 */
	public void advanceRound(HttpServletRequest request) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		RunningSimulation running_sim = (RunningSimulation) MultiSchemaHibernateUtil.getSession(schema).get(
				RunningSimulation.class, running_sim_id);

		running_sim.setRound(running_sim.getRound() + 1);

		System.out.println("Round is " + running_sim.getRound());

		this.simulation_round = running_sim.getRound() + "";

		Hashtable<Long, String> roundNames = (Hashtable<Long, String>) request.getSession().getServletContext()
				.getAttribute(USIP_OSP_ContextListener.CACHEON_L_S_ROUND_NAMES);
		roundNames.put(running_sim_id, this.simulation_round);
		request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_L_S_ROUND_NAMES, roundNames);

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

		String notify_via_email = (String) request.getParameter("notify_via_email");

		String previousPhase = this.phaseName;

		try {

			MultiSchemaHibernateUtil.beginTransaction(schema);
			phase_id = new Long(r_phase_id);
			RunningSimulation running_sim = (RunningSimulation) MultiSchemaHibernateUtil.getSession(schema).get(
					RunningSimulation.class, running_sim_id);
			running_sim.setPhase_id(phase_id);
			System.out.println("set rs " + running_sim_id + " to " + phase_id);
			MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(running_sim);

			SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil.getSession(schema).get(
					SimulationPhase.class, phase_id);

			this.phaseName = sp.getName();

			// Store new phase name in web cache.
			Hashtable<Long, String> phaseNames = (Hashtable<Long, String>) request.getSession().getServletContext()
					.getAttribute("phaseNames");

			phaseNames.put(running_sim_id, this.phaseName);
			request.getSession().getServletContext().setAttribute("phaseNames", phaseNames);

			System.out.println("setting phase change alert");

			// //////////////////////////////////////////////////////////////////
			// Store new phase id in the web cache
			Hashtable<Long, Long> phaseIds = (Hashtable<Long, Long>) session.getServletContext().getAttribute(
					USIP_OSP_ContextListener.CACHEON_PHASE_IDS);

			phaseIds.put(running_sim_id, phase_id);

			request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_PHASE_IDS, phaseIds);
			// //////////////////////////////////////////////////////////////////
			// ///////

			Alert al = new Alert();
			al.setType(Alert.TYPE_PHASECHANGE);

			String phaseChangeNotice = "Phase has changed from '" + previousPhase + "' to '" + this.phaseName + "'.";

			// Will need to add email text, etc.
			al.setAlertMessage(phaseChangeNotice);
			al.setAlertEmailMessage(phaseChangeNotice);

			al.setRunning_sim_id(running_sim_id);
			MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(al);

			// Let people know that there is a change to catch.
			storeNewHighestChangeNumber(request);

			if ((notify_via_email != null) && (notify_via_email.equalsIgnoreCase("true"))) {

				Hashtable uniqList = new Hashtable();

				for (ListIterator<UserAssignment> li = running_sim.getUser_assignments().listIterator(); li.hasNext();) {
					UserAssignment ua = li.next();
					uniqList.put(ua.getUser_id(), "set");
				}

				SchemaInformationObject sio = SchemaInformationObject.lookUpSIOByName(schema);

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

					Emailer.postMail(sio, bu.getUsername(), subject, message, user_name, cced, bcced);

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

				actor_id = new Long((String) mpr.getParameter("actorid"));

				Actor actorOnScratchPad = Actor.getMe(schema, actor_id);

				createActor(mpr, actorOnScratchPad);

			} else if ((create_actor != null) && (create_actor.equalsIgnoreCase("Create Actor"))) {
				Actor newActor = new Actor();
				newActor.setImageFilename("no_image_default.jpg");
				createActor(mpr, newActor);

			} else if ((clear_button != null) && (clear_button.equalsIgnoreCase("Clear"))) {
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

				actorOnScratchPad.setPublic_description((String) mpr.getParameter("public_description"));
				actorOnScratchPad.setName((String) mpr.getParameter("actor_name"));
				actorOnScratchPad.setSemi_public_description((String) mpr.getParameter("semi_public_description"));
				actorOnScratchPad.setPrivate_description((String) mpr.getParameter("private_description"));

				String control_actor = (String) mpr.getParameter("control_actor");

				if ((control_actor != null) && (control_actor.equalsIgnoreCase("true"))) {
					actorOnScratchPad.setControl_actor(true);
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
				this.actor_id = actorOnScratchPad.getId();

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

		Long runningSimHighestChange = (Long) highestChangeNumber.get(running_sim_id);

		if (runningSimHighestChange == null) {
			runningSimHighestChange = new Long(1);
			highestChangeNumber.put(running_sim_id, runningSimHighestChange);

			request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_CHANGE_NUMBERS, highestChangeNumber);

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

		request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_CHANGE_NUMBERS, highestChangeNumber);

	}

	public Hashtable getHashtableForThisRunningSim(HttpServletRequest request) {

		// The conversation is pulled out of the context
		Hashtable<Long, Long> highestChangeNumber = (Hashtable<Long, Long>) request.getSession().getServletContext()
				.getAttribute(USIP_OSP_ContextListener.CACHEON_CHANGE_NUMBERS);

		if (highestChangeNumber == null) {
			highestChangeNumber = new Hashtable();
			request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_CHANGE_NUMBERS, highestChangeNumber);
		}

		return highestChangeNumber;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String alarmXML(HttpServletRequest request, HttpServletResponse response) {

		Long runningSimHighestChange = getHighestChangeNumberForRunningSim(request);

		if (runningSimHighestChange == null) {
			return "";
		}

		boolean doDatabaseCheck = false;

		if (runningSimHighestChange.intValue() > myHighestChangeNumber.intValue()) {

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

		String alarmXML = "<response>";

		if ((running_sim_id != null) && (doDatabaseCheck)) {

			MultiSchemaHibernateUtil.beginTransaction(schema);

			RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil.getSession(schema).get(
					RunningSimulation.class, running_sim_id);

			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			List alarms = checkForAlarm(rs, request);

			if (alarms.size() == 0) {
				alarmXML += "<numAlarms>0</numAlarms>";

			} else if (alarms.size() > 3) { // if too many alerts, just tell
											// them to check their environment.
				alarmXML += "<numAlarms>1</numAlarms>";
				alarmXML += "<sim_event_text>"
						+ "Multiple alerts received. Please check all of the tabs where you are receiving information."
						+ "</sim_event_text>";

			} else {
				alarmXML += "<numAlarms>" + alarms.size() + "</numAlarms>";
				for (ListIterator<Alert> li = alarms.listIterator(); li.hasNext();) {
					Alert this_alert = li.next();

					alarmXML += "<sim_event_text>" + this_alert.getAlertPopupMessage() + "</sim_event_text>";
				}

			}

		} // End of if doing database check.

		alarmXML += "</response>";
		myHighestChangeNumber = new Long(runningSimHighestChange.intValue());
		return alarmXML;

		/*
		 * TODO Move parts from below to where they need to go.
		 * 
		 * if (alarmType.equalsIgnoreCase("phase_change")) { alarmXML +=
		 * "<sim_event_text>" +
		 * "Simulation Phase has changed. You may now have a different set of tabs."
		 * + "</sim_event_text>"; } else if (alarmType.equalsIgnoreCase("news"))
		 * { alarmXML += "<sim_event_text>" +
		 * "There is new news. Please check the news page as soon as possible."
		 * + "</sim_event_text>"; } else if
		 * (alarmType.equalsIgnoreCase("announcement")) { alarmXML +=
		 * "<sim_event_text>" +
		 * "There is a new announcement. Please check the announcements page as soon as possible."
		 * + "</sim_event_text>"; } else if (alarmType.equalsIgnoreCase("memo"))
		 * {
		 * 
		 * }
		 */
	}

	/**
	 * 
	 * @param rs
	 * @param request
	 * @return
	 */
	public List<Alert> checkForAlarm(RunningSimulation rs, HttpServletRequest request) {

		List<Alert> returnList = new ArrayList();
		
		List<Alert> alerts = Alert.getAllForRunningSim(schema, rs.getId());

		for (ListIterator<Alert> li = alerts.listIterator(); li.hasNext();) {
			Alert this_alert = li.next();

			boolean thisUserApplicable = false;

			if (!(this_alert.isSpecific_targets())) {
				thisUserApplicable = true;
			} else {
				thisUserApplicable = this_alert.checkActor(this.actor_id);
			}

			if (thisUserApplicable) {

				returnList.add(this_alert);

			} // end of if this alert is applicable to this user
		} // End of if this id of this alert is not null (?)

		return returnList;
	}

	/**
	 * Creates a general announcement and adds it to the set of announcements
	 * for this running simulation.
	 * 
	 * @param news
	 * @param request
	 * @return
	 */
	public RunningSimulation makeGeneralAnnouncement(String news, HttpServletRequest request) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil.getSession(schema).get(
				RunningSimulation.class, running_sim_id);

		Alert al = new Alert();
		al.setType(Alert.TYPE_ANNOUNCEMENT);
		al.setAlertMessage(news);
		al.setRunning_sim_id(running_sim_id);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(al);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(rs);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		// Let people know that there is a change to catch.
		storeNewHighestChangeNumber(request);

		return rs;

	}

	public void makeTargettedAnnouncement(HttpServletRequest request) {

		String targets = list2String(getIdsOfCheckBoxes("actor_cb_", request));

		Alert al = new Alert();
		al.setSpecific_targets(true);
		al.setType(Alert.TYPE_ANNOUNCEMENT);
		al.setAlertMessage(alertInQueueText);
		al.setThe_specific_targets(targets);
		al.setRunning_sim_id(running_sim_id);
		al.saveMe(schema);

		makeTargettedAnnouncement(al, targets, request);

	}

	/**
	 * Sends out announcements to only the players selected.
	 * 
	 * @param request
	 */
	public void makeTargettedAnnouncement(Alert al, String targets, HttpServletRequest request) {

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

		List<Alert> alerts = Alert.getAllForRunningSim(schema, running_sim_id);
		
		for (ListIterator li = alerts.listIterator(); li.hasNext();) {
			Alert al = (Alert) li.next();
			returnList.add(al);
		}

		Collections.reverse(returnList);

		return returnList;
	}
	
	public String getPhaseNameById(Long phase_id){
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
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public PlayerReflection handlePlayerReflection(HttpServletRequest request){
		
		String cs_id_string = (String) request.getParameter("cs_id");
		
		Long cs_id = null;
		
		if (cs_id_string != null){
			cs_id = new Long(cs_id_string);
		} else {
			Logger.getRootLogger().warn("Null CS_ID sent to pso.handlePlayerReflection");
			return new PlayerReflection();
		}
		
		PlayerReflection playerReflection = PlayerReflection.getPlayerReflection(schema, 
				cs_id, running_sim_id, actor_id);
		
		String sending_page = (String) request.getParameter("sending_page");
		String update_text = (String) request.getParameter("update_text");
		
		if ( (sending_page != null) && (update_text != null) && 
				(sending_page.equalsIgnoreCase("player_reflection"))){
			String player_reflection_text = (String) request.getParameter("player_reflection_text");
			
			playerReflection.setBigString(player_reflection_text);
			playerReflection.save(schema);
			
			   
		} // End of if coming from this page and have added text
		
		return playerReflection;
	}

	public String getSimulation_round() {

		Hashtable<Long, String> roundNames = (Hashtable<Long, String>) session.getServletContext().getAttribute(
				USIP_OSP_ContextListener.CACHEON_L_S_ROUND_NAMES);

		if (running_sim_id != null) {
			simulation_round = roundNames.get(running_sim_id);

			return simulation_round;

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

	public RunningSimulation giveMeRunningSim() {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil.getSession(schema).get(
				RunningSimulation.class, running_sim_id);

		MultiSchemaHibernateUtil.getSession(schema).evict(rs);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return rs;
	}

	public Actor giveMeActor() {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		Actor actor = (Actor) MultiSchemaHibernateUtil.getSession(schema).get(Actor.class, actor_id);

		MultiSchemaHibernateUtil.getSession(schema).evict(actor);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (actor == null) {
			actor = new Actor();
		}

		return actor;
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
						&& (request.getParameter(param_name).equalsIgnoreCase("true"))) {
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

		BaseUser bu = validate(request);

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

		BaseUser bu = validate(request);

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

	/**
	 * 
	 * @param request
	 */
	public void handleMakeAnnouncement(HttpServletRequest request) {

		RunningSimulation rs = giveMeRunningSim();

		String sending_page = (String) request.getParameter("sending_page");
		String add_news = (String) request.getParameter("add_news");

		if ((sending_page != null) && (add_news != null) && (sending_page.equalsIgnoreCase("add_news"))) {

			String announcement_text = (String) request.getParameter("announcement_text");

			String player_target = (String) request.getParameter("player_target");

			if ((player_target != null) && (player_target.equalsIgnoreCase("some"))) {
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

	public void handleMemoPage(SharedDocument sd, HttpServletRequest request, CustomizeableSection cs) {

		// If data has been submitted, tack it at the front, save it and move on
		String sending_page = (String) request.getParameter("sending_page");

		String start_memo = (String) request.getParameter("start_memo");
		String save_draft = (String) request.getParameter("save_draft");
		String submit_memo = (String) request.getParameter("submit_memo");

		String memo_text = (String) request.getParameter("memo_text");

		if (sending_page != null) {
			if (submit_memo != null) {
				if ((memo_text != null) && (memo_text.trim().length() > 0)) {

					java.util.Date today = new java.util.Date();
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy HH:mm a");
					String memo_time = "<em>(Memo Submitted: " + sdf.format(today) + ")</em><br />";

					String fullText = memo_time + memo_text + "<br><hr>" + sd.getBigString();
					sd.setBigString(fullText);
					sd.saveMe(schema);
					memo_starter_text = DEFAULTMEMOTEXT;

					// Find all SDANAO objects for this document, and send
					// notifications to the actors.
					notifyOfDocChanges(schema, sd.getId(), this.actor_id, request, cs);
				}
			}

			if (save_draft != null) {
				memo_starter_text = memo_text;
			}

			if (start_memo != null) {
				memo_starter_text = DEFAULTMEMOTEXT;
			}

		} // End of if coming back from the form on this page.
	}

	public void notifyOfDocChanges(String schema, Long sd_id, Long excluded_actor_id, HttpServletRequest request,
			CustomizeableSection cs) {

		List listToNotify = SharedDocActorNotificAssignObj.getAllAssignmentsForDocument(schema, sd_id);

		for (ListIterator<SharedDocActorNotificAssignObj> li = listToNotify.listIterator(); li.hasNext();) {
			SharedDocActorNotificAssignObj sdanao = (SharedDocActorNotificAssignObj) li.next();

			if (!(sdanao.getActor_id().equals(excluded_actor_id))) {

				Alert al = new Alert();
				al.setSpecific_targets(true);
				al.setType(al.TYPE_MEMO);
				al.setThe_specific_targets(sdanao.getActor_id().toString());
				al.setAlertMessage(sdanao.getNotificationText());
				al.setRunning_sim_id(running_sim_id);
				al.saveMe(schema);

				makeTargettedAnnouncement(al, sdanao.getActor_id().toString(), request);
			}
		}

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
	 * 
	 * @param request
	 * @return
	 */
	public SimulationRatings handleSimFeedback(HttpServletRequest request) {

		System.out.println("handling sim feedback");

		SimulationRatings sr = SimulationRatings.getBySimAndActorAndUser(schema, sim_id, actor_id, user_id);

		String sending_page = (String) request.getParameter("sending_page");
		String sim_feedback_text = (String) request.getParameter("sim_feedback_text");
		String users_stated_name = (String) request.getParameter("users_stated_name");

		if ((sending_page != null) && (sim_feedback_text != null) && (sending_page.equalsIgnoreCase("sim_feedback"))) {

			sr.setActor_id(actor_id);
			sr.setActor_name(actor_name);
			sr.setSim_id(sim_id);
			sr.setUser_id(user_id);
			sr.setUser_comments(sim_feedback_text);
			sr.setComment_type(SimulationRatings.PLAYER_COMMENT);
			sr.setUsers_stated_name(users_stated_name);
			sr.saveMe(schema);

		} // End of if coming from this page and have added text

		return sr;
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
	 * One passes this a section id, a position, and the id of an object, and if that object is assigned
	 * to this section, at that position, the word 'selected' is passed back. If not, an empty string is passed
	 * back.
	 * 
	 * @param index_hash
	 * @param object_index
	 * @param id_of_object_being_checked
	 * @return
	 */
	public String checkAgainstHash(Long cs_id, int object_index, Long id_of_object_being_checked) {

		Logger.getRootLogger().warn("checkAgainstHash (bss_id/index/object_id): " + cs_id + "/" + object_index + "/" + id_of_object_being_checked);
		
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
			RunningSimulation rs = simulation.addNewRunningSimulation(rsn, schema);

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
	 * @param setOfActors
	 * @param section_id
	 * @return
	 */
	public String generatePrivateChatLines(HttpServletRequest request, Hashtable setOfActors, Long section_id) {

		String returnString = "";

		// Loop over the conversations for this Actor
		for (ListIterator<Conversation> li = Conversation.getActorsConversationsForSimSection(schema, actor_id,
				running_sim_id, section_id).listIterator(); li.hasNext();) {
			Conversation conv = (Conversation) li.next();

			returnString += "var start_index" + conv.getId() + " = 0 \r\n";
			returnString += "var new_start_index" + conv.getId() + " = 0 \r\n";

			// Take this opportunity to fill up the hashtable with actors
			// Loop over the conversation actors (should be 2 of them) for this
			// private chat.
			for (ListIterator<ConvActorAssignment> liii = conv.getConv_actor_assigns(schema).listIterator(); liii
					.hasNext();) {
				ConvActorAssignment caa = (ConvActorAssignment) liii.next();

				// Don't do the chat with the actor and his or her self.
				if (!(caa.getActor_id().equals(actor_id))) {
					setOfActors.put(caa.getActor_id().toString(), "set");
				} // end of if this is an applicable actor
			} // End of loop over conversation actors
		} // End of loop over conversations.

		return returnString;

	} // End of method

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
	
	public boolean handleResetWebCache(HttpServletRequest request){
		
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
	public void changeSectionColor(HttpServletRequest request){
		
		String sending_section = (String) request.getParameter("sending_section");
		
		if ((sending_section != null) && (sending_section.equalsIgnoreCase("change_color"))) {
			
			String ss_id = (String) request.getParameter("ss_id");
			String new_color = (String) request.getParameter("new_color");
			
			SimulationSectionAssignment ssa = SimulationSectionAssignment.getMe(schema, new Long(ss_id));
			
			if (ssa != null){
				ssa.setTabColor(new_color);
				ssa.save(schema);
			}
			
			
		}
		
	}

} // End of class
