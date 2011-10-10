package org.usip.osp.networking;

import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.*;
import org.usip.osp.baseobjects.core.*;
import org.usip.osp.bishops.*;
import org.usip.osp.communications.*;
import org.usip.osp.persistence.*;
import org.usip.osp.sharing.*;
import org.usip.osp.specialfeatures.*;

import com.seachangesimulations.osp.gametime.GameClockEvent;
import com.seachangesimulations.osp.gametime.GameClockPhaseInstructions;

/**
 * This object contains all of the session information for the participant and
 * is the main interface to all of the java objects that the participant will
 * interact with.
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
public class PlayerSessionObject extends SessionObjectBase {

	/** The Session object. */
	public HttpSession session = null;

	/**
	 * Returns the PSO stored in the session, or creates one. The coder can
	 * indicated if he or she wants to start a transaction.
	 */
	public static PlayerSessionObject getPSO(HttpSession session) {

		PlayerSessionObject pso = (PlayerSessionObject) session
				.getAttribute("pso");

		if (pso == null) {
			Logger.getRootLogger().debug("pso is new");
			pso = new PlayerSessionObject();
			pso.session = session;
		}

		session.setAttribute("pso", pso);

		return pso;
	}

	private boolean controlCharacter = false;

	public boolean isControlCharacter() {
		return controlCharacter;
	}

	public boolean preview_mode = false;

	/** Organization of the schema that the user is working in. */
	public String schemaOrg = ""; //$NON-NLS-1$

	/** Name of the actor being played or worked on. */
	private String actorName = ""; //$NON-NLS-1$

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	/** Indicates if user has selected a phase. */
	public boolean phaseSelected = false;

	/** Name of phase being conducted or worked on. */
	private String phaseName = ""; //$NON-NLS-1$

	public Long draft_email_id;

	public int topFrameHeight = 200;
	public int bottomFrameHeight = 40;

	private String bottomFrame = ""; //$NON-NLS-1$

	public String getBottomFrame() {
		return bottomFrame;
	}

	public void setBottomFrame(String bottomFrame) {
		this.bottomFrame = bottomFrame;
	}

	/**
	 * This is the highest change number that the player has. The change number
	 * for the particular running simulation is kept in the hashtable assigned
	 * to the web application.
	 */
	public Long myHighestAlertNumber = new Long(0);

	private Long myUserAssignmentId;

	/** Text of alert being worked on. */
	public String alertInQueueText = ""; //$NON-NLS-1$

	/** Type of alert being worked on. */
	public int alertInQueueType = 0;

	public String tabposition = "1"; //$NON-NLS-1$

	/**
	 * Once a player has selected a running sim, do not let them back out and
	 * choose another without logging out and logging in.
	 */
	// TODO I don't think this was working as planned. Need to forward to login page if necessary
	//public boolean hasSelectedRunningSim = false;

	/**
	 * This is called from the top of the players frame to determine where they
	 * should go.
	 * 
	 * @param request
	 */
	public void handleSimWeb(HttpServletRequest request) {

		if (request.getParameter("tabposition") != null) {
			this.tabposition = request.getParameter("tabposition"); //$NON-NLS-1$

			this.bottomFrame = "frame_bottom.jsp"; //$NON-NLS-1$

			int tabpos = 1;

			try {
				tabpos = new Integer(this.tabposition).intValue();
			} catch (Exception e) {
				Logger.getRootLogger().warn(
						"Return to sim page with tab position not an integer.");
			}

			List simSecList = getSimSecList(request);

			try {
				// List simSecList = SimulationSectionAssignment
				// .getBySimAndActorAndPhase(this.schema, this.sim_id,
				// this.actorId, this.phase_id);

				if (tabpos <= simSecList.size()) {
					SimulationSectionGhost ss = (SimulationSectionGhost) simSecList
							.get(tabpos - 1);
					this.bottomFrame = ss.getTabURL();
				}

			} catch (Exception e) {
				e.printStackTrace();
				this.forward_on = true;
			}
		}

	}

	/**
	 * Returns the list of simulation sections for the actor being played.
	 * 
	 * @param request
	 * @return
	 */
	public List<SimulationSectionGhost> getSimSecList(HttpServletRequest request) {

		session = request.getSession();

		// Get phase id from the cache
		Hashtable<Long, Long> phaseIds = (Hashtable<Long, Long>) session
				.getServletContext().getAttribute(
						USIP_OSP_ContextListener.CACHEON_PHASE_IDS);

		Long cachedPhaseId = phaseIds.get(runningSimId);

		if (cachedPhaseId != null) {
			phase_id = cachedPhaseId;
		}

		String hashKey = sim_id + "_" + actorId + "_" + phase_id;

		// Control players get the additional section which has to be cached
		// with them.
		if (this.isControlCharacter()) {
			hashKey += "_control";
		}

		Hashtable<String, List<SimulationSectionGhost>> sim_section_info = (Hashtable<String, List<SimulationSectionGhost>>) session
				.getServletContext().getAttribute(
						USIP_OSP_ContextListener.CACHEON_SIM_SEC_INFO);

		if (sim_section_info == null) {
			sim_section_info = new Hashtable<String, List<SimulationSectionGhost>>();
		}

		List<SimulationSectionGhost> returnList = sim_section_info.get(hashKey);

		if (returnList == null) {

			returnList = new ArrayList<SimulationSectionGhost>();

			// Get full list from database hit
			List<SimulationSectionAssignment> fullList = SimulationSectionAssignment
					.getBySimAndActorAndPhase(schema, sim_id, actorId, phase_id);

			// Copy the needed parts of that list into the ghosts
			for (ListIterator<SimulationSectionAssignment> li = fullList
					.listIterator(); li.hasNext();) {
				SimulationSectionAssignment ss = li.next();

				SimulationSectionGhost ssg = new SimulationSectionGhost();

				ssg.setTabHeading(ss.getTab_heading());
				ssg.setTabColor(ss.getTabColor());
				ssg.setTabURL(ss.generateURLforBottomFrame(actorId));

				returnList.add(ssg);

			}
			// Store that list into the Context
			sim_section_info.put(hashKey, returnList);

			session.getServletContext().setAttribute(
					USIP_OSP_ContextListener.CACHEON_SIM_SEC_INFO,
					sim_section_info);

			if (this.isControlCharacter()) {
				SimulationSectionGhost ssg = new SimulationSectionGhost();
				ssg.setTabHeading("Control");
				ssg.setTabColor("FFCCCC");
				ssg.setTabURL("../osp_core/control.jsp");

				returnList.add(ssg);
			}
		}

		return returnList;
	}

	/**
	 * When an author is previewing how a section will look for a player, this
	 * method is called to load up a temporary PlayerSessionObject with
	 * information for the Simulation Section being previewed to use.
	 * 
	 * @param afso
	 */
	public void loadInAFSOInformation(AuthorFacilitatorSessionObject afso) {

		this.loggedin = afso.isLoggedin();

		this.schema = afso.schema;
		this.sim_id = afso.sim_id;
		this.actorId = afso.actor_being_worked_on_id;
		this.phase_id = afso.phase_id;

		this.preview_mode = true;

	}

	/**
	 * This method is called when the user selects a simulation to play. (From
	 * simulation/select_simulation.jsp)
	 * 
	 * @param request
	 */
	public void handleLoadPlayerScenario(HttpServletRequest request) {

		String sending_page = request.getParameter("sending_page");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("select_simulation"))) {

			schema = request.getParameter("schema");
			schemaOrg = request.getParameter("schema_org");

			session = request.getSession();

			String user_assignment_id = request
					.getParameter("user_assignment_id");

			UserAssignment ua = UserAssignment.getById(schema, new Long(
					user_assignment_id));
			myUserAssignmentId = ua.getId();
			ua.advanceStatus(UserAssignment.STATUS_ENTERED);
			ua.saveMe(schema);

			this.myHighestAlertNumber = ua.getHighestAlertNumberRecieved();

			sim_id = ua.getSim_id();

			Simulation simulation = Simulation.getById(schema, sim_id);
			
			if (simulation.isUsesGameClock()){
				this.simUsesGameClock = true;
			}

			runningSimId = ua.getRunning_sim_id();
			RunningSimulation running_sim = RunningSimulation.getById(schema,
					runningSimId);

			// Set the date to be the date in which the Running Sim will be run.
			this.dateOffset = this.getDateOffset(running_sim);

			actorId = ua.getActor_id();
			Actor actor = Actor.getById(schema, actorId);

			// If player logs in as a control character, their 'control-ness'
			// will follow them.
			if (actor.isControl_actor()) {
				this.controlCharacter = true;
			}

			SimulationPhase sp = SimulationPhase.getById(schema, running_sim
					.getPhase_id());

			// Load information from the pertinent objects to be displayed.
			loadSimInfoForDisplay(request, simulation, running_sim, actor, sp);

			System.out.println("xxxxxxxxxxxxxxxx");
			// Turn on instructions if necessary
			if (simulation.isUsesGameClock()){
				System.out.println("xxxxxxxxxxxxxxxx activate");
				GameClockPhaseInstructions.activatePhaseInstructions(request, this);
			}
			
			// ////////////////////////////////////////////////////////////////////////
			Hashtable<Long, String> roundNames = USIP_OSP_Cache
					.getCachedHashtable(request, schema,
							USIP_OSP_ContextListener.CACHEON_L_S_ROUND_NAMES,
							USIP_OSP_ContextListener.CACHED_TABLE_LONG_STRING);

			String cachedRoundName = roundNames.get(runningSimId);

			if (cachedRoundName == null) {
				roundNames.put(runningSimId, simulation_round);
				request.getSession().getServletContext().setAttribute(
						USIP_OSP_ContextListener.CACHEON_L_S_ROUND_NAMES,
						roundNames);
			}

			loadPhaseNameInWebCache(request, sp);

			// //////////////////////////////////////////////////////////////////////
			// Store it in the web cache, if this has not been done already
			// by another user.
			Hashtable<Long, Long> phaseIds = USIP_OSP_Cache.getCachedHashtable(
					request, schema, USIP_OSP_ContextListener.CACHEON_PHASE_IDS,
					USIP_OSP_ContextListener.CACHED_TABLE_LONG_LONG);

			Long cachedPhaseId = phaseIds.get(runningSimId);
			if (cachedPhaseId == null) {
				phaseIds.put(runningSimId, phase_id);
				request.getSession().getServletContext().setAttribute(
						USIP_OSP_ContextListener.CACHEON_PHASE_IDS, phaseIds);

			}
			// //////////////////////////////////////////////////////////////////////

			recordLoginToSchema(user_id, schema, actorId, runningSimId, request);

			storeUserInfoInSessionInformation(request);

			UserTrail ut = UserTrail.getById(schema, myUserTrailGhost
					.getTrail_id());
			ut.setActor_id(actorId);
			ut.setRunning_sim_id(runningSimId);
			ut.saveMe(schema);

			forward_on = true;

		} // End of if coming from select simulation page.
	}

	/**
	 * Sets values in this PlayerSessionObject to be those stored for the user,
	 * and creates the loggedInTicket to record their presence.
	 * 
	 * @param bu_id
	 * @param schema
	 * @param request
	 * @return
	 */
	public void recordLoginToSchema(Long bu_id, String schema, Long actor_id,
			Long running_sim_id, HttpServletRequest request) {

		User user = User.getInfoOnLogin(bu_id, schema);
		BaseUser bu = BaseUser.getByUserId(bu_id);

		if (user != null) {
			this.user_id = user.getId();
			this.userDisplayName = bu.getFull_name();

			// Username is also email address
			this.user_name = bu.getUsername();

			myUserTrailGhost.setTrail_id(user.getTrail_id());
			myUserTrailGhost.setUser_id(this.user_id);
			myUserTrailGhost.setActor_id(actor_id);
			myUserTrailGhost.setRunning_sim_id(running_sim_id);
			// Player starts on tab 1, always.
			myUserTrailGhost.setTab_position(new Long(1));

			Hashtable<Long, UserTrailGhost> loggedInUsers = (Hashtable<Long, UserTrailGhost>) request
					.getSession().getServletContext().getAttribute(
							USIP_OSP_ContextListener.CACHEON_LOGGED_IN_USERS);

			if (loggedInUsers == null) {
				loggedInUsers = new Hashtable();
				request.getSession().getServletContext().setAttribute(
						USIP_OSP_ContextListener.CACHEON_LOGGED_IN_USERS,
						loggedInUsers);
			}

			loggedInUsers.put(user.getId(), myUserTrailGhost);

			loggedin = true;
		} else {
			Logger
					.getRootLogger()
					.warn(
							"Warning: While user selecting simulation, null user detected.");
			loggedin = false;
		}

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

	/** Gets called when the user has selected a scenario to play. */
	public void storeUserInfoInSessionInformation(HttpServletRequest request) {

		Hashtable<Long, Hashtable> loggedInPlayers = USIP_OSP_Cache
				.getCachedHashtable(request, schema,
						USIP_OSP_ContextListener.CACHEON_LOGGED_IN_PLAYERS,
						USIP_OSP_ContextListener.CACHED_TABLE_LONG_HASHTABLE);

		Hashtable thisSetOfPlayers = loggedInPlayers.get(this.runningSimId);

		if (thisSetOfPlayers == null) {
			thisSetOfPlayers = new Hashtable();
			loggedInPlayers.put(this.runningSimId, thisSetOfPlayers);
		}

		thisSetOfPlayers.put(this.myUserTrailGhost.getTrail_id(),
				myUserTrailGhost);

		request.getSession().getServletContext().setAttribute(
				"loggedInPlayers", loggedInPlayers);

	}

	/**
	 * Loads a session.
	 * 
	 * @param request
	 */
	public void handleLoadPlayerAutoAssignedScenario(HttpServletRequest request) {

		MultiSchemaHibernateUtil.beginTransaction(this.schema);

		this.sim_id = new Long(request.getParameter("sim_id"));
		Simulation simulation = (Simulation) MultiSchemaHibernateUtil
				.getSession(this.schema).get(Simulation.class, this.sim_id);

		this.actorId = new Long(request.getParameter("actor_id"));
		Actor actor = (Actor) MultiSchemaHibernateUtil.getSession(this.schema)
				.get(Actor.class, this.actorId);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

		RunningSimulation rs = new RunningSimulation("My Session", this
				.giveMeSim(), this.schema, null, "Player Self Assigned",
				TimeZone.getDefault().getDisplayName());
		this.runningSimId = rs.getId();
		rs.setReady_to_begin(true);

		MultiSchemaHibernateUtil.beginTransaction(this.schema);
		SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil
				.getSession(this.schema).get(SimulationPhase.class,
						rs.getPhase_id());
		MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

		this.loadSimInfoForDisplay(request, simulation, rs, actor, sp);

	}

	/** Keeps track of the previously recorded high alert number. */
	private Long prevMyHighestAlertNumber = new Long(0);

	/**
	 * This method does the following: 1.) Gets from the cache the highest
	 * change number for this simulation. 2.) It compares this with what this
	 * user has as the highest change number they have seen. 3.)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String alarmXML(HttpServletRequest request,
			HttpServletResponse response) {

		// Get from the cache the highest change number for this simulation.
		Long runningSimHighestChange = getHighestAlertNumberForRunningSim(request);

		boolean doDatabaseCheck = false;

		// Compare the highest running sim change number with what this user has
		// seen.
		if (runningSimHighestChange.intValue() > myHighestAlertNumber
				.intValue()) {
			doDatabaseCheck = true;
		}

		String alarmXML = "<response>";

		if ((runningSimId != null) && (doDatabaseCheck)) {

			// Get a list of alarms
			List<Alert> alerts_raw = Alert.getAllForRunningSimAboveNumber(
					schema, runningSimId, myHighestAlertNumber);

			List<Alert> my_alerts = filterForUserAlerts(alerts_raw);

			if (my_alerts.size() == 0) {
				alarmXML += "<numAlarms>0</numAlarms>";

			} else if (my_alerts.size() > 2) {
				alarmXML += "<numAlarms>1</numAlarms>";
				alarmXML += "<sim_event_type>"
						+ Alert.getTypeText(Alert.TYPE_MULTIPLE)
						+ "</sim_event_type>";
				alarmXML += "<sim_event_text>" + Alert.getMultipleAlertText()
						+ "</sim_event_text>";
				myHighestAlertNumber = runningSimHighestChange;
			} else {

				Alert this_alert = my_alerts.get(0);
				alarmXML += "<numAlarms>1</numAlarms>";
				alarmXML += "<sim_event_type>" + this_alert.getTypeText()
						+ "</sim_event_type>";
				alarmXML += "<sim_event_text>"
						+ this_alert.getAlertPopupMessage()
						+ "</sim_event_text>";

				// Either way, mark this one as checked by setting the highest
				// alert number
				myHighestAlertNumber = new Long(this_alert.getId());
			}

		} // End of if doing database check.

		alarmXML += "</response>";

		return alarmXML;

	}

	/**
	 * Takes a list of alerts and only returns the ones applicable to this user.
	 * 
	 * @param alerts_raw
	 * @return
	 */
	public List filterForUserAlerts(List<Alert> alerts_raw) {

		ArrayList returnList = new ArrayList();

		for (ListIterator li = alerts_raw.listIterator(); li.hasNext();) {
			Alert alert = (Alert) li.next();

			boolean thisUserApplicable = false;

			if (!(alert.isSpecific_targets())) {
				thisUserApplicable = true;
			} else {
				thisUserApplicable = alert.checkActor(this.actorId);
			}

			// Check to see if its applicable, if so, add it to output.
			if (thisUserApplicable) {
				returnList.add(alert);
			}
		}

		return returnList;
	}

	public static String DEFAULTMEMOTEXT = "To: <BR />From:<BR />Topic:<BR />Message:"; //$NON-NLS-1$

	public String memo_starter_text = DEFAULTMEMOTEXT;

	public void handleMemoPage(SharedDocument sd, HttpServletRequest request,
			CustomizeableSection cs) {

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
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
							"MM/dd/yy HH:mm a");
					String memo_time = "<em>(Memo Submitted: "
							+ sdf.format(today) + ")</em><br />";

					String fullText = memo_time + memo_text + "<br><hr>"
							+ sd.getBigString();
					sd.setBigString(fullText);
					sd.saveMe(schema);
					memo_starter_text = DEFAULTMEMOTEXT;

					// Find all SDANAO objects for this document, and send
					// notifications to the actors.
					notifyOfDocChanges(schema, sd.getId(), this.actorId,
							request, cs);
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

	/**
	 * 
	 * @param request
	 * @param setOfActors
	 * @param section_id
	 * @return
	 */
	public String generateChatHelpLines(HttpServletRequest request,
			ArrayList setOfConversations, Long section_id) {

		String returnString = "";

		// Loop over the conversations for this Actor
		for (ListIterator<Conversation> li = setOfConversations.listIterator(); li
				.hasNext();) {
			Conversation conv = (Conversation) li.next();

			returnString += "var start_index" + conv.getId() + " = 0 \r\n";
			returnString += "var new_start_index" + conv.getId() + " = 0 \r\n";

			// Take this opportunity to fill up the hashtable with actors
			// Loop over the conversation actors (should be 2 of them) for this
			// private chat.
			for (ListIterator<ConvActorAssignment> liii = conv
					.getConv_actor_assigns(schema).listIterator(); liii
					.hasNext();) {
				ConvActorAssignment caa = (ConvActorAssignment) liii.next();

				// Don't do the chat with the actor and his or her self.
				if (!(caa.getActor_id().equals(actorId))) {
					// setOfActors.put(caa.getActor_id().toString(), "set");
				} // end of if this is an applicable actor
			} // End of loop over conversation actors
		} // End of loop over conversations.

		return returnString;

	} // End of method

	/**
	 * 
	 * @param request
	 * @param setOfActors
	 * @param section_id
	 * @return
	 */
	public String generateConferenceRoomChatLines(HttpServletRequest request,
			Hashtable setOfActors, Long conv_id) {

		String returnString = "";

		Conversation conv = Conversation.getById(schema, conv_id);

		returnString += "var start_index" + conv.getId() + " = 0 \r\n";
		returnString += "var new_start_index" + conv.getId() + " = 0 \r\n";

		// Take this opportunity to fill up the hashtable with actors
		// Loop over the conversation actors (should be 2 of them) for this
		// private chat.
		for (ListIterator<ConvActorAssignment> liii = conv
				.getConv_actor_assigns(schema).listIterator(); liii.hasNext();) {
			ConvActorAssignment caa = (ConvActorAssignment) liii.next();

			// xxxxx Don't do the chat with the actor and his or her self.
			// xxxxif (!(caa.getActor_id().equals(actorId))) {
			setOfActors.put(caa.getActor_id().toString(), "set");
			// xxxxx} // end of if this is an applicable actor
		} // End of loop over conversation actors

		return returnString;

	} // End of method

	/**
	 * 
	 * @param request
	 * @param setOfActors
	 * @param section_id
	 * @return
	 */
	public String generatePrivateChatLines(HttpServletRequest request,
			Hashtable setOfActors, Long section_id) {

		String returnString = "";

		// Loop over the conversations for this Actor
		for (ListIterator<Conversation> li = Conversation
				.getActorsConversationsForSimSection(schema, actorId,
						runningSimId, section_id).listIterator(); li.hasNext();) {
			Conversation conv = (Conversation) li.next();

			returnString += "var start_index" + conv.getId() + " = 0 \r\n";
			returnString += "var new_start_index" + conv.getId() + " = 0 \r\n";

			// Take this opportunity to fill up the hashtable with actors
			// Loop over the conversation actors (should be 2 of them) for this
			// private chat.
			for (ListIterator<ConvActorAssignment> liii = conv
					.getConv_actor_assigns(schema).listIterator(); liii
					.hasNext();) {
				ConvActorAssignment caa = (ConvActorAssignment) liii.next();

				// Don't do the chat with the actor and his or her self.
				if (!(caa.getActor_id().equals(actorId))) {
					setOfActors.put(caa.getActor_id().toString(), "set");
				} // end of if this is an applicable actor
			} // End of loop over conversation actors
		} // End of loop over conversations.

		return returnString;

	} // End of method

	/**
	 * Creates a general announcement and adds it to the set of announcements
	 * for this running simulation.
	 * 
	 * @param news
	 * @param request
	 * @return
	 */
	public void makeGeneralAnnouncement(String news, HttpServletRequest request) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil
				.getSession(schema).get(RunningSimulation.class, runningSimId);

		Alert al = new Alert();
		al.setType(Alert.TYPE_ANNOUNCEMENT);
		al.setAlertMessage(news);
		al.setRunning_sim_id(runningSimId);
		al.setSim_id(sim_id);

		String shortIntro = USIP_OSP_Util.cleanAndShorten(news, 20) + " ...";

		al.setAlertPopupMessage("There is a new announcement: " + shortIntro);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(al);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(rs);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		// Let people know that there is a change to catch.
		storeNewHighestChangeNumber(request, al.getId());

		@SuppressWarnings("unused")
		CommunicationsHub ch = new CommunicationsHub(al, schema);

	}

	/**
	 * Takes information out of the request to create the targeted announcement.
	 * 
	 * @param request
	 */
	public void makeTargettedAnnouncement(HttpServletRequest request,
			String inject_id) {

		String targets = list2String(USIP_OSP_Util.getIdsOfCheckBoxes(
				"actor_cb_", request));

		Alert al = new Alert();
		al.setSpecific_targets(true);
		
		al.setType(alertInQueueType);
		
		// TODO - this seemed to matter.
		al.setType(Alert.TYPE_ANNOUNCEMENT);
				
		al.setAlertMessage(alertInQueueText);

		String shortIntro = USIP_OSP_Util.cleanAndShorten(alertInQueueText, 20)
				+ " ...";

		al.setAlertPopupMessage("There is a new announcement: " + shortIntro);
		al.setThe_specific_targets(targets);
		al.setRunning_sim_id(runningSimId);
		al.saveMe(schema);

		// Let people know that there is a change to catch.
		storeNewHighestChangeNumber(request, al.getId());

		if ((inject_id != null) && (!(inject_id.equalsIgnoreCase("null")))
				&& (inject_id.length() > 0)) {

			Inject inj = Inject.getById(schema, new Long(inject_id));

			// Record this in the firing history
			InjectFiringHistory ifh = new InjectFiringHistory(
					this.runningSimId, this.actorId, new Long(inject_id),
					"some", inj.getInject_name(), alertInQueueText, targets,
					schema);

			USIP_OSP_Cache
					.addFiredInjectsToCache(schema, request, this.runningSimId,
							this.actorId, new Long(inject_id), "all");
			// TODO come back here and add inject name instead of short Intro
			RespondableObject ro = new RespondableObject(schema, this.sim_id,
					this.runningSimId, phase_id, new Long(inject_id),
					Inject.class.toString().replaceFirst("class ", ""),
					shortIntro, this.actorId, this.user_name,
					this.userDisplayName, "all");
		}

	}

	/** Takes a list and turns it into a comma separated string. */
	public static String list2String(List <String> idList) {

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

	/**
	 * Sends out announcements to only the players selected.
	 * 
	 * @param request
	 */
	public void makeTargettedAnnouncement(Alert al, String targets,
			HttpServletRequest request) {

		// Let people know that there is a change to catch.
		storeNewHighestChangeNumber(request, al.getId());

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

		List<Alert> alerts = Alert.getAllForRunningSim(schema, runningSimId);

		for (ListIterator li = alerts.listIterator(); li.hasNext();) {
			Alert al = (Alert) li.next();
			returnList.add(al);
		}

		Collections.reverse(returnList);

		return returnList;
	}

	public Hashtable<String, String> newsAlerts = new Hashtable<String, String>();

	public int myCount = 0;

	/**
	 * This method 1.) gets the cache of alert numbers, 2.) get the highest
	 * Alert number for this simulation 3.) (If the highest Alert number is
	 * null, then set it to '0' in the cache.) 4.) returns the highest Alert
	 * number for this simulation run.
	 * 
	 * */
	public Long getHighestAlertNumberForRunningSim(HttpServletRequest request) {

		if (runningSimId == null) {
			Logger
					.getRootLogger()
					.warn(
							"PSO: Running sim id is 0. Returning 0 for highest change number. ");
			return new Long(0);
		}

		// Get cache of alert numbers
		Hashtable<Long, Long> highestAlertNumber = USIP_OSP_Cache
				.getAlertNumberHashtableForRunningSim(request);

		// Get the highest change number for this simulation
		Long runningSimHighestAlert = (Long) highestAlertNumber
				.get(runningSimId);

		// If the highest change number is null, then set it to '0' in the
		// cache.
		if (runningSimHighestAlert == null) {
			// Try to get it from database. If that fails, set it to 0.
			runningSimHighestAlert = Alert.getHighestAlertNumber(schema,
					runningSimId);

			if (runningSimHighestAlert != null) {
				highestAlertNumber.put(runningSimId, runningSimHighestAlert);

				request.getSession().getServletContext().setAttribute(
						USIP_OSP_ContextListener.CACHEON_ALERT_NUMBERS,
						highestAlertNumber);
			} else {
				runningSimHighestAlert = new Long(0);
			}
		}

		return runningSimHighestAlert;
	}

	/**
	 * Stores the next highest change number to let people know if they may need
	 * to pop up a message or refresh some of their web pages.
	 * 
	 * @param request
	 */
	public void storeNewHighestChangeNumber(HttpServletRequest request,
			Long newHighestAlertNumber) {

		// Get the hashtable to store it in from the cache
		Hashtable<Long, Long> highestChangeNumber = USIP_OSP_Cache
				.getAlertNumberHashtableForRunningSim(request);

		// put it back into the hashtable
		highestChangeNumber.put(runningSimId, newHighestAlertNumber);

		// Make sure that this hashtable is stored back in the context.
		request.getSession().getServletContext().setAttribute(
				USIP_OSP_ContextListener.CACHEON_ALERT_NUMBERS,
				highestChangeNumber);

	}

	/**
	 * 
	 * @param phase_id
	 * @param request
	 */
	public static void changePhase(HttpServletRequest request, PlayerSessionObject pso) {

		String sending_page = (String) request.getParameter("sending_page");
		
		if (!(USIP_OSP_Util.stringFieldMatches(sending_page, "change_phase"))){
			
			return;
			
		}
		
		String notify_via_email = (String) request
				.getParameter("notify_via_email");

		String previousPhase = pso.phaseName;

		try {

			// Save exit event in game history.
			if (pso.simUsesGameClock){
				/*
				GameClockPhaseInstructions gcpi = GameClockPhaseInstructions.getByPhaseAndSimId(
						pso, pso.phase_id, pso.sim_id);
				
				if (gcpi != null){
					// Create event on leaving last phase
					GameClockEvent.createEvent(pso.schema, gcpi, pso.getRunningSimId(), "Exited Phase");
					
					// Create event on entering new phase
					GameClockEvent.createEvent(pso.schema, gcpi, pso.getRunningSimId(), "Entered Phase");
				}
				*/
			}
			
			pso.phase_id = new Long(request.getParameter("phase_id"));
			
			RunningSimulation rs = RunningSimulation.getById(pso.schema, pso.runningSimId);
			rs.setPhase_id(pso.phase_id);
			rs.saveMe(pso.schema);
			
			// Activate the clock on this phase.
			GameClockPhaseInstructions.activatePhaseInstructions(request, pso);

			SimulationPhase sp = SimulationPhase.getById(pso.schema, pso.phase_id);

			pso.phaseName = sp.getPhaseName();

			// Store new phase name in web cache.
			Hashtable<Long, String> phaseNames = (Hashtable<Long, String>) request
					.getSession()
					.getServletContext()
					.getAttribute(
							USIP_OSP_ContextListener.CACHEON_L_S_PHASE_NAMES_BY_RS_ID);

			phaseNames.put(pso.runningSimId, pso.phaseName);
			request.getSession().getServletContext().setAttribute(
					USIP_OSP_ContextListener.CACHEON_L_S_PHASE_NAMES_BY_RS_ID,
					phaseNames);

			// //////////////////////////////////////////////////////////////////
			// Store new phase id in the web cache
			Hashtable<Long, Long> phaseIds = (Hashtable<Long, Long>) pso.session
					.getServletContext().getAttribute(
							USIP_OSP_ContextListener.CACHEON_PHASE_IDS);

			phaseIds.put(pso.runningSimId, pso.phase_id);

			request.getSession().getServletContext().setAttribute(
					USIP_OSP_ContextListener.CACHEON_PHASE_IDS, phaseIds);
			// //////////////////////////////////////////////////////////////////
			// ///////

			Alert al = new Alert();

			al.setType(Alert.TYPE_PHASECHANGE);

			String phaseChangeNotice = "Phase has changed from '"
					+ previousPhase + "' to '" + pso.phaseName + "'.";

			// Will need to add email text, etc.
			al.setAlertMessage(phaseChangeNotice);
			al.setAlertEmailMessage(phaseChangeNotice);
			al.setAlertPopupMessage(phaseChangeNotice);
			al.setSim_id(pso.sim_id);
			al.setRunning_sim_id(pso.runningSimId);
			al.saveMe(pso.schema);

			// Let people know that there is a change to catch.
			pso.storeNewHighestChangeNumber(request, al.getId());

			if ((notify_via_email != null)
					&& (notify_via_email.equalsIgnoreCase("true"))) {

				Hashtable uniqList = new Hashtable();

				for (ListIterator<UserAssignment> li = rs
						.getUser_assignments(pso.schema).listIterator(); li
						.hasNext();) {
					UserAssignment ua = li.next();
					uniqList.put(ua.getUser_id(), "set");
				}

				SchemaInformationObject sio = SchemaInformationObject
						.lookUpSIOByName(pso.schema);

				for (Enumeration e = uniqList.keys(); e.hasMoreElements();) {
					Long key = (Long) e.nextElement();
					Logger.getRootLogger().debug("need to email " + key);

					// Need to get user email address from the key, which is the
					// user id.
					BaseUser bu = BaseUser.getByUserId(key);

					String subject = "Simulation Phase Change";
					String message = "Simulation phase has changed.";

					Vector cced = null;
					Vector bcced = new Vector();
					bcced.add(pso.user_name);

					Emailer.postMail(sio, bu.getUsername(), subject, message,
							message, sio.getEmailNoreplyAddress(), pso.user_name,
							cced, bcced);

				}
			}


			if (sp.isCopyInObjects()) {
				// Should move this to work via an interface.
				List objectToCopy = BishopsPartyInfo.getAllForRunningSim(
						pso.schema, pso.runningSimId, false);

				for (ListIterator<BishopsPartyInfo> li = objectToCopy
						.listIterator(); li.hasNext();) {
					BishopsPartyInfo bpi = li.next();

					bpi.copyToNewVersion(pso.schema);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List eligibleActors = new ArrayList();
	public List emailRecipients = new ArrayList();


	/**
	 * 
	 */
	public void setUpEligibleActors() {
		if (draft_email_id != null) {
			emailRecipients = Email.getRecipientsOfAnEmail(schema,
					draft_email_id, EmailRecipients.RECIPIENT_TO);
		} else {
			emailRecipients = new ArrayList();
		}

		Simulation simulation = new Simulation();

		if (sim_id != null) {
			simulation = giveMeSim();
			eligibleActors = new ArrayList();

			for (ListIterator<Actor> lia = simulation.getActors(schema)
					.listIterator(); lia.hasNext();) {
				Actor act = lia.next();

				// Add to the eligible actor lists actors that are not already
				// marked as recipients
				boolean foundName = false;
				for (ListIterator<EmailRecipients> lie = emailRecipients
						.listIterator(); lie.hasNext();) {
					EmailRecipients er = lie.next();

					if (act.getActorName().equalsIgnoreCase(er.getActorName())) {
						foundName = true;
					}

				}

				if (!(foundName)) {
					eligibleActors.add(act);
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

		String command = request.getParameter("command"); //$NON-NLS-1$
		String sending_page = request.getParameter("sending_page"); //$NON-NLS-1$

		if (sending_page != null) {
			if (command != null) {
				if (command.equalsIgnoreCase("End Simulation")) { //$NON-NLS-1$

					// Mark Completed, change phase
					Simulation sim = this.giveMeSim();
					Logger
							.getRootLogger()
							.debug(
									"forwarin on to : " + sim.getLastPhaseId(this.schema).toString()); //$NON-NLS-1$
					changePhase(request, this);

					// Forward them back
					this.forward_on = true;
					this.backPage = "../simulation/simwebui.jsp?tabposition=1"; //$NON-NLS-1$
				}
			}
		}

	}

	/**
	 * Handles the pushing of the inject out to the players.
	 * 
	 * @param request
	 * @return
	 */
	public boolean handlePushInject(HttpServletRequest request) {

		String sending_page = request.getParameter("sending_page"); //$NON-NLS-1$

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("push_injects"))) { //$NON-NLS-1$

			String announcement_text = request
					.getParameter("announcement_text"); //$NON-NLS-1$
			String inject_action = request.getParameter("inject_action"); //$NON-NLS-1$

			if ((inject_action != null)
					&& (inject_action.equalsIgnoreCase("2"))) { //$NON-NLS-1$
				announcement_text = announcement_text
						+ "<BR /><strong>Communicate with Control your actions</strong><BR />"; //$NON-NLS-1$
			}

			String player_target = request.getParameter("player_target"); //$NON-NLS-1$

			String inject_id = request.getParameter("inject_id"); //$NON-NLS-1$

			if ((player_target != null) // Sending Inject to some players
					&& (player_target.equalsIgnoreCase("some"))) { //$NON-NLS-1$
				this.alertInQueueText = announcement_text;
				this.alertInQueueType = Alert.TYPE_EVENT;
				return true;

			} else { // Sending inject to all players
				makeGeneralAnnouncement(announcement_text, request);

				if (inject_id != null) {

					Inject theInject = Inject.getById(schema, new Long(
							inject_id));

					// Record this in the firing history
					InjectFiringHistory ifh = new InjectFiringHistory(
							this.runningSimId, this.actorId, theInject.getId(),
							"all", theInject.getInject_name(),
							announcement_text, "all", schema);

					USIP_OSP_Cache.addFiredInjectsToCache(schema, request,
							this.runningSimId, this.actorId, theInject.getId(),
							"all");

					RespondableObject ro = new RespondableObject(schema,
							this.sim_id, this.runningSimId, phase_id, theInject
									.getId(), Inject.class.toString()
									.replaceFirst("class ", ""), theInject
									.getInject_name(), this.actorId,
							this.user_name, this.userDisplayName, "all");

				}

				return false;
			}

		}

		return false;

	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public PlayerReflection handlePlayerReflection(HttpServletRequest request) {

		String cs_id_string = (String) request.getParameter("cs_id");

		Long cs_id = null;

		if (cs_id_string != null) {
			cs_id = new Long(cs_id_string);
		} else {
			Logger.getRootLogger().warn(
					"Null CS_ID sent to pso.handlePlayerReflection");
			return new PlayerReflection();
		}

		PlayerReflection playerReflection = PlayerReflection
				.getPlayerReflection(schema, cs_id, runningSimId, actorId,
						user_id);

		String sending_page = (String) request.getParameter("sending_page");
		String update_text = (String) request.getParameter("update_text");

		if ((sending_page != null) && (update_text != null)
				&& (sending_page.equalsIgnoreCase("player_reflection"))) {
			String player_reflection_text = (String) request
					.getParameter("player_reflection_text");

			playerReflection.setU_id(this.user_id);
			playerReflection.setBigString(player_reflection_text);
			playerReflection.save(schema);

		} // End of if coming from this page and have added text

		return playerReflection;
	}

	/**
	 * 
	 * @param request
	 */
	public void handleMakeAnnouncement(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");
		String add_news = (String) request.getParameter("add_news");

		if ((sending_page != null) && (add_news != null)
				&& (sending_page.equalsIgnoreCase("add_news"))) {

			String announcement_text = (String) request
					.getParameter("announcement_text");

			String player_target = (String) request
					.getParameter("player_target");

			if ((player_target != null)
					&& (player_target.equalsIgnoreCase("some"))) {
				alertInQueueText = announcement_text;
				alertInQueueType = Alert.TYPE_ANNOUNCEMENT;
				backPage = "make_announcement.jsp";
				this.forward_on = true;
				return;
			} else {
				makeGeneralAnnouncement(announcement_text, request);
			}

		} // End of if coming from this page and have added announcement.

	}

	/**
	 * 
	 * @param request
	 */
	public void handleMakeRatingAnnouncement(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");
		String add_news = (String) request.getParameter("add_news");

		if ((sending_page != null) && (add_news != null)
				&& (sending_page.equalsIgnoreCase("make_rating_announcement"))) {

			String announcement_text = (String) request
					.getParameter("announcement_text");

			String player_target = (String) request
					.getParameter("player_target");

			String points_awarded = (String) request
					.getParameter("points_awarded");

			Long pointsAwarded = new Long(points_awarded);

			String awardImage = "";

			for (int ii = 0; ii < pointsAwarded.intValue(); ++ii) {
				awardImage += "<img src=\"" + getBaseSimURL()
						+ "/simulation/images/dove_30by30.png\" >";
			}

			announcement_text = awardImage + announcement_text;

			if ((player_target != null)
					&& (player_target.equalsIgnoreCase("some"))) {
				alertInQueueText = announcement_text;
				alertInQueueType = Alert.TYPE_RATING_ANNOUNCEMENT;
				backPage = "make_rating_announcement.jsp";
				this.forward_on = true;
				return;
			} else {
				makeGeneralAnnouncement(announcement_text, request);
			}

		} // End of if coming from this page and have added announcement.

	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public SimulationRatings handleSimFeedback(HttpServletRequest request) {

		Logger.getRootLogger().debug("handling sim feedback");

		SimulationRatings sr = SimulationRatings.getBySimAndActorAndUser(
				schema, sim_id, actorId, user_id);

		String sending_page = (String) request.getParameter("sending_page");
		String sim_feedback_text = (String) request
				.getParameter("sim_feedback_text");
		String users_stated_name = (String) request
				.getParameter("users_stated_name");

		if ((sending_page != null) && (sim_feedback_text != null)
				&& (sending_page.equalsIgnoreCase("sim_feedback"))) {

			sr.setActor_id(actorId);
			sr.setActor_name(actorName);
			sr.setSim_id(sim_id);
			sr.setUser_id(user_id);
			sr.setUser_comments(sim_feedback_text);
			sr.setComment_type(SimulationRatings.PLAYER_COMMENT);
			sr.setUsers_stated_name(users_stated_name);
			sr.saveMe(schema);

		} // End of if coming from this page and have added text

		return sr;
	}

	public void notifyOfDocChanges(String schema, Long sd_id,
			Long excluded_actor_id, HttpServletRequest request,
			CustomizeableSection cs) {

		List listToNotify = SharedDocActorNotificAssignObj
				.getAllAssignmentsForDocument(schema, sd_id);

		for (ListIterator<SharedDocActorNotificAssignObj> li = listToNotify
				.listIterator(); li.hasNext();) {
			SharedDocActorNotificAssignObj sdanao = (SharedDocActorNotificAssignObj) li
					.next();

			if (!(sdanao.getActor_id().equals(excluded_actor_id))) {

				Alert al = new Alert();
				al.setSpecific_targets(true);
				al.setType(Alert.TYPE_MEMO);
				al.setThe_specific_targets(sdanao.getActor_id().toString());
				al.setAlertMessage(sdanao.getNotificationText());
				al.setAlertPopupMessage(sdanao.getNotificationText());
				al.setRunning_sim_id(runningSimId);
				al.saveMe(schema);

				makeTargettedAnnouncement(al, sdanao.getActor_id().toString(),
						request);

			}
		}

	}

	public Actor giveMeActor() {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		Actor actor = (Actor) MultiSchemaHibernateUtil.getSession(schema).get(
				Actor.class, actorId);

		MultiSchemaHibernateUtil.getSession(schema).evict(actor);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (actor == null) {
			actor = new Actor();
		}

		return actor;
	}

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

	public void loadSimInfoForDisplay(HttpServletRequest request,
			Simulation simulation, RunningSimulation running_sim, Actor actor,
			SimulationPhase sp) {
		this.simulation_name = simulation.getSimulationName();
		this.sim_copyright_info = simulation.getCopyright_string();
		this.simulation_version = simulation.getVersion();
		this.simulation_org = simulation.getCreation_org();

		this.run_sim_name = running_sim.getRunningSimulationName();
		this.simulation_round = running_sim.getRound() + "";
		this.phase_id = running_sim.getPhase_id();

		this.actorName = actor.getActorName(schema, running_sim.getId(),
				request);

		this.phaseName = sp.getPhaseName();

		loadPhaseNameInWebCache(request, sp);

	}

	private void saveAarText(HttpServletRequest request) {
		String write_aar_end_sim = request.getParameter("write_aar_end_sim"); //$NON-NLS-1$
		Logger.getRootLogger().debug("saving: " + write_aar_end_sim); //$NON-NLS-1$

		RunningSimulation rs = giveMeRunningSim();
		rs.setAar_text(write_aar_end_sim);
		rs.saveMe(this.schema);
	}

	public void handleWriteAAR(HttpServletRequest request) {

		String command = request.getParameter("command"); //$NON-NLS-1$
		String sending_page = request.getParameter("sending_page"); //$NON-NLS-1$

		if (sending_page != null) {
			if (command != null) {
				if (command.equalsIgnoreCase("Save Changes")) { //$NON-NLS-1$
					saveAarText(request);
				}
			}
		}

	}



	/** Round being displayed */
	private String simulation_round = "0"; //$NON-NLS-1$

	/**
	 * Store it in the web cache, if this has not been done already by another
	 * user.
	 * 
	 * @param request
	 * @param sp
	 */
	public void loadPhaseNameInWebCache(HttpServletRequest request,
			SimulationPhase sp) {

		Hashtable<Long, String> phaseNames = USIP_OSP_Cache.getCachedHashtable(
				request, schema,
				USIP_OSP_ContextListener.CACHEON_L_S_PHASE_NAMES_BY_RS_ID,
				USIP_OSP_ContextListener.CACHED_TABLE_LONG_STRING);

		String cachedPhaseName = phaseNames.get(this.runningSimId);

		if (cachedPhaseName == null) {
			phaseNames.put(this.runningSimId, sp.getPhaseName());
			request.getSession().getServletContext().setAttribute(
					USIP_OSP_ContextListener.CACHEON_L_S_PHASE_NAMES_BY_RS_ID,
					phaseNames);

			System.out.println("cachedPhaseName is " + sp.getPhaseName());

		}
	}

	/**
	 * Returns the color of an inject, which are colored if they have already
	 * been used during this play session.
	 * 
	 * @param injectId
	 * @return
	 */
	public String getInjectColor(HttpServletRequest request, Long injectId) {

		String unshotColor = "#FFFFFF"; //$NON-NLS-1$
		String shotColor = "#FFCCCC"; //$NON-NLS-1$
		String partialShotColor = "#CCFFCC"; //$NON-NLS-1$
		
		if (this.runningSimId == null) {
			return unshotColor;
		}

		// Get hashtable for this rsid/actorid combo
		Hashtable cachedInjectInfo = USIP_OSP_Cache.getInjectsFired(schema,
				request, this.runningSimId, this.actorId);

		String hashValue = (String) cachedInjectInfo.get(injectId);

		if (hashValue == null) {
			return unshotColor;
		} else {
			if (hashValue.equalsIgnoreCase("all")) {
				return shotColor;
			} else {
				return partialShotColor;
			}
		}
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

	/**
	 * 
	 * @param request
	 * @return
	 */
	public String validateLoginToSim(HttpServletRequest request) {

		loggedin = false;

		String sendToPage = "index.jsp";

		BaseUser bu = SessionObjectBase.validate(request);

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
	 * Should take this opportunity to mark in the user trail that they have
	 * logged out.
	 * 
	 * @param request
	 */
	public void logout(HttpServletRequest request) {

		if (loggedin) {

			if (myUserAssignmentId != null) {
				UserAssignment.saveHighAlertNumber(schema, myUserAssignmentId,
						myHighestAlertNumber);
			}

			if ((myUserTrailGhost != null)
					&& (myUserTrailGhost.getTrail_id() != null)) {
				myUserTrailGhost.recordLogout(schema);
			}

			loggedin = false;
		}
	}

	/**
	 * Pulls the name of the image file out of the cache, or loads it if not
	 * found.
	 * 
	 * @param request
	 * @param a_id
	 * @return
	 */
	public String getActorThumbImage(HttpServletRequest request, Long a_id) {

		String a_thumb = USIP_OSP_Cache.getActorThumbImage(request, schema,
				runningSimId, a_id, sim_id);

		return a_thumb;
	}

	public Vector myActors = new Vector();

	/**
	 * 
	 * @return
	 */
	public Vector getActorsForConversation(Long ssrsdoa_id,
			HttpServletRequest request) {

		if ((myActors == null) || (myActors.size() == 0)) {
			myActors = ChatController.getActorsForConversation(this,
					ssrsdoa_id, request);
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

			if (ag.getId().toString().equalsIgnoreCase(actor_id)) {
				ag.setDefaultColorChatBubble(newColor);
			}
		}
	}

	private ArrayList<TimeLineInterface> setOfEvents = new ArrayList();

	{

		Event e1 = new Event();

		e1.setEventTitle("bring it on");
		e1.setEventMsgBody("here are the words");
		e1.setEventStartTime(new java.util.Date());

		setOfEvents.add(e1);
	}

	public ArrayList<TimeLineInterface> getSetOfEvents() {
		return setOfEvents;
	}

	public void setSetOfEvents(ArrayList<TimeLineInterface> setOfEvents) {
		this.setOfEvents = setOfEvents;
	}

	public String getSimilieEvents() {
		return TimeLine.packupArray(setOfEvents);
	}

	public String checkDatesOnSim(Simulation sim, RunningSimulation rs) {

		if (rs.getEnabledDate().before(sim.getLastEditDate())) {
			return "Warning. This Running Simulation may be invalid.";
		} else {
			return "";
		}
	}

	public WebLinkObjects wloOnScratchPad = new WebLinkObjects();

	/**
	 * Called when a player 'becomes' another player.
	 * 
	 * @param request
	 */
	public void handleBecomePlayer(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("become_player"))) {
			String actor_id = (String) request.getParameter("actor_id");
			String actor_name = (String) request.getParameter("actor_name");
			if (actor_id != null) {
				setActorId(new Long(actor_id));
				setActorName(actor_name);
				tabposition = "1";
			}
		}
	}

	public SharedDocument bneeds = new SharedDocument();

	public void handleTempBrainstorm() {
		// /////////////////////////////////////////////////////////
		MultiSchemaHibernateUtil.beginTransaction(schema);

		String hql_string = "from SharedDocument where SIM_ID = :sim_id AND RS_ID = :rs_id " + //$NON-NLS-1$ //$NON-NLS-2$
				" AND base_id = -1"; //$NON-NLS-1$

		List returnList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery(hql_string).setLong("sim_id", sim_id).setLong(
						"rs_id", runningSimId).list();

		if ((returnList == null) || (returnList.size() == 0)) {

			bneeds.setBase_id(new Long(-1));
			bneeds.setRs_id(runningSimId);
			bneeds.setSim_id(sim_id);
			bneeds.saveMe(schema);

		} else {
			bneeds = (SharedDocument) returnList.get(0);
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		// //////////////////////////////////////

		SharedDocument bsolutions = new SharedDocument();

		MultiSchemaHibernateUtil.beginTransaction(schema);

		hql_string = "from SharedDocument where SIM_ID = :sim_id AND RS_ID = :rs_id " + //$NON-NLS-1$ //$NON-NLS-2$
				" AND base_id = -2"; //$NON-NLS-1$

		returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				hql_string).setLong("sim_id", sim_id).setLong("rs_id",
				runningSimId).list();

		if ((returnList == null) || (returnList.size() == 0)) {

			bsolutions.setBase_id(new Long(-2));
			bsolutions.setRs_id(runningSimId);
			bsolutions.setSim_id(sim_id);
			bsolutions.saveMe(schema);

		} else {
			bsolutions = (SharedDocument) returnList.get(0);
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		// ////////////////////////////////
	}

	/**
	 * 
	 * @param request
	 */
	public void addPlayerTip(CustomizeableSection cs, TipsCustomizer tc,
			HttpServletRequest request) {
		String sending_page = (String) request.getParameter("sending_page");
		if ((sending_page != null) && (sending_page.equalsIgnoreCase("tips"))) {

			System.out.println("adding tip");

			Tips tip = new Tips();

			String tip_id = (String) request.getParameter("tip_id");
			if ((tip_id != null) && (!(tip_id.equalsIgnoreCase("null")))) {
				tip.setId(new Long(tip_id));
			}
			String tip_page_text = request.getParameter("tip_page_text");
			tip.setTipText(tip_page_text);
			tip.setActorId(getActorId());
			tip.setPhaseId(phase_id);
			tip.setCsId(cs.getId());
			tip.setSimId(sim_id);
			tip.setTipLastEditDate(new java.util.Date());
			tip.setBaseTip(false);
			tip.setParentTipId(tc.getTip().getId());
			tip.setUserId(user_id);
			tip.setUserName(userDisplayName);
			tip.setUserEmail(user_name);

			tip.saveMe(schema);

		}
	}

	public void handleEmailPlayers(HttpServletRequest request,
			SchemaInformationObject sio) {

		String sending_page = request.getParameter("sending_page"); //$NON-NLS-1$

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("email_players"))) {

			String email_text = request.getParameter("email_text"); //$NON-NLS-1$
			String email_subject = request.getParameter("email_subject"); //$NON-NLS-1$
			String email_from = request.getParameter("email_from"); //$NON-NLS-1$

			email_text = "!!!   This is a simulation email. This is not reality   !!!!   <br/>"
					+ email_text;
			email_text = email_text
					+ "<br/>!!!   This is a simulation email. This is not reality   !!!!   <br/>";

			Hashtable<String, String> playersToEmail = new Hashtable();

			for (Enumeration e = request.getParameterNames(); e
					.hasMoreElements();) {
				String param_name = (String) e.nextElement();

				if (param_name.startsWith("email_player_")) {
					if ((request.getParameter(param_name) != null)
							&& (request.getParameter(param_name)
									.equalsIgnoreCase("true"))) {

						String user_email = param_name.replaceFirst(
								"email_player_", "");

						playersToEmail.put(user_email, "set");

					}

				}
			}

			for (Enumeration e = playersToEmail.keys(); e.hasMoreElements();) {
				String email_address = (String) e.nextElement();

				Emailer.postMail(sio, email_address, email_subject, email_text,
						email_text, sio.getEmailNoreplyAddress(), email_from,
						new Vector(), new Vector());

			}

		}
	}

	/**
	 * Takes the language code current selected, compares it to the selection
	 * item and if they match, passes back the HTML code to indicate this is the
	 * currently selected language.
	 * 
	 * @param thisSelection
	 * @return
	 */
	public String getLanuageCodeSelected(int thisSelection) {

		if (this.languageCode == thisSelection) {
			return " selected=\"selected\" ";
		} else {
			return "";
		}
	}

	/**
	 * Detects if a lanugage change has come in.
	 * 
	 * @param request
	 */
	public void detectLanguageChange(HttpServletRequest request) {

		String select_language = request.getParameter("select_language");

		if (select_language != null) {
			languageCode = new Long(select_language).intValue();
		}
	}

	/**
	 * Sets the time, in milliseconds, between the server and the running
	 * simulation.
	 * 
	 * @param rs
	 * @return
	 */
	public long getDateOffset(RunningSimulation rs) {

		TimeZone tz_server = TimeZone.getDefault();

		TimeZone tz_rs = TimeZone.getTimeZone(rs.getTimeZone());

		long offsetServer = tz_server.getOffset(new Date().getTime());
		long offsetRunningSim = tz_rs.getOffset(new Date().getTime());

		long relativeOffset = offsetServer - offsetRunningSim;

		return relativeOffset;

	}

	public long dateOffset = 0;

	public String insertChatLine(HttpServletRequest request) {
		String status_code = ChatController.NO_NEW_MSG + "";

		String message = (String) request.getParameter("message");
		String name = (String) request.getParameter("name");
		String conversation = (String) request.getParameter("conversation");
		String start_index = (String) request.getParameter("start_index");

		if ((message != null) && (message.trim().length() > 0)) {
			ChatController.insertChatLine(user_id, getActorId(), start_index,
					message, conversation, this, request, new Date());
		}
		return status_code;
	}

	public String getLocalTimeForDisplay() {

		java.util.Date nowLocalTime = new Date();
		long nowLong = nowLocalTime.getTime() - dateOffset;
		nowLocalTime = new Date(nowLong);

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"MM/dd/yy HH:mm a");
		String time_string = sdf.format(nowLocalTime);

		return time_string;

	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public ArrayList<String> getChat(HttpServletRequest request) {
		String status_code = ChatController.NO_NEW_MSG + "";

		String show_all = (String) request.getParameter("show_all");

		String conv_id = (String) request.getParameter("conversation");
		String start_index = (String) request.getParameter("start_index");

		String xml_msgs = "";

		if (conv_id != null) {
			if (show_all != null) {
				xml_msgs = ChatController.getXMLConversation(start_index,
						conv_id, this, request, 0);
			} else {
				xml_msgs = ChatController.getXMLConversation(start_index,
						conv_id, this, request, 100);
			}
		}

		if ((xml_msgs != null) && (xml_msgs.trim().length() > 0)) {
			status_code = ChatController.NEW_MSG + "";
		}

		ArrayList<String> returnList = new ArrayList<String>();
		returnList.add(status_code);
		returnList.add(xml_msgs);

		return returnList;
	}
	
	/**
	 * Returns the link to the message page.
	 * 
	 * @param sg
	 * @param loginType
	 * @return
	 */
	public String getLinkToMessages(SchemaGhost sg, String loginType){
		return "login_messages.jsp?entry_type=" + loginType + sg.getSchemaIdHTML();
	}
	
	public List getListOfMessages(){
		return null;
	}
	
	/**
	 * Returns the url to forward the user to after they have read any messages that 
	 * they needed to see.
	 * 
	 * @param request
	 * @return
	 */
	public static String getURLofSectionToEnter(HttpServletRequest request){
		
		String entry_type = (String) request.getParameter("entry_type");
		String schema_id = (String) request.getParameter("schema_id");
		
		String urlTrailer = "?initial_entry=true&schema_id=" + schema_id;
		
		if (entry_type.equalsIgnoreCase("ADMIN")){
			return "simulation_admin/adminwebui.jsp" + urlTrailer;
		} else if (entry_type.equalsIgnoreCase("AUTHOR")) {
			return "simulation_authoring/creationwebui.jsp" + urlTrailer + "&show_intro=true";
		} else if (entry_type.equalsIgnoreCase("INSTRUCTOR")){
			return "simulation_facilitation/facilitateweb.jsp" + urlTrailer + "&ftab=home";
		} else if (entry_type.equalsIgnoreCase("PLAYER")){
			return "simulation/select_simulation_to_play.jsp" + urlTrailer;
		} else {
			//TODO Log error
			return "login.jsp";
		}
		
	}
	
}
