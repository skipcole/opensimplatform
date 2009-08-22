package org.usip.osp.networking;

import java.util.*;

import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.*;
import org.usip.osp.communications.*;
import org.usip.osp.persistence.*;
import org.usip.osp.specialfeatures.PlayerReflection;

/**
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
public class PlayerSessionObject {

	/** The Session object. */
	public HttpSession session = null;
	
	/**
	 * Returns the PSO stored in the session, or creates one. The coder can
	 * indicated if he or she wants to start a transaction.
	 */
	public static PlayerSessionObject getPSO(HttpSession session, boolean getConn) {

		PlayerSessionObject pso = (PlayerSessionObject) session.getAttribute("pso");

		if (pso == null) {
			Logger.getRootLogger().debug("pso is new");
			pso = new PlayerSessionObject();
			pso.session = session;
		}

		session.setAttribute("pso", pso);

		return pso;
	}
	
	/** Determines if actor is logged in. */
	private boolean loggedin = false;
	
	public boolean isLoggedin() {
		return loggedin;
	}
	
	/** Schema of the database that the user is working in. */
	public String schema = ""; //$NON-NLS-1$
	
	/** Organization of the schema that the user is working in. */
	public String schemaOrg = ""; //$NON-NLS-1$
	
	/** ID of Actor being played. */
	public Long actor_id;
	
	/** Name of the actor being played or worked on. */
	public String actor_name = ""; //$NON-NLS-1$
	
	/** ID of Simulation being conducted or worked on. */
	public Long sim_id;
	
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
	
	/** ID of the Running Simulation being conducted or worked on. */
	public Long running_sim_id;

	/** Name of the running simulation session. */
	public String run_sim_name = ""; //$NON-NLS-1$

	/** ID of Phase being conducted. */
	public Long phase_id;
	
	/** Indicates if user has selected a phase. */
	public boolean phaseSelected = false;

	/** Name of phase being conducted or worked on. */
	private String phaseName = ""; //$NON-NLS-1$
	
	/** Id of User that is logged on. */
	public Long user_id;
	
	/** Records the display name of this user. */
	public String user_Display_Name = ""; //$NON-NLS-1$
	
	/**
	 * Username/ Email address of user that is logged in and using this
	 * PlayerSessionObject.
	 */
	public String user_name;
	
	public String bottomFrame = ""; //$NON-NLS-1$
	
	/**
	 * This is the highest change number that the player has. The change number
	 * for the particular running simulation is kept in the hashtable assigned
	 * to the web application.
	 */
	public Long myHighestChangeNumber = new Long(0);
	
	/** Text of alert being worked on. */
	public String alertInQueueText = ""; //$NON-NLS-1$

	/** Type of alert being worked on. */
	public int alertInQueueType = 0;
	
	/** Page to forward the user on to. */
	public boolean forward_on = false;
	
	public String tabposition = "1"; //$NON-NLS-1$
	
	/** The page to take them back to if needed. */
	public String backPage = "index.jsp"; //$NON-NLS-1$
	
	/**
	 * Once a player has selected a running sim, do not let them back out and
	 * choose another without logging out and logging in.
	 */
	public boolean hasSelectedRunningSim = false;

	/** Login ticket of this user. */
	public LoggedInTicket myLoggedInTicket = new LoggedInTicket();
	
	/**
	 * This is called from the top of the players frame to determine where they
	 * should go.
	 * 
	 * @param request
	 */
	public void handleSimWeb(HttpServletRequest request) {

		this.tabposition = request.getParameter("tabposition"); //$NON-NLS-1$

		this.bottomFrame = "frame_bottom.jsp"; //$NON-NLS-1$

		int tabpos = 1;

		try {
			tabpos = new Integer(this.tabposition).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			List simSecList = SimulationSectionAssignment.getBySimAndActorAndPhase(this.schema, this.sim_id,
					this.actor_id, this.phase_id);

			if (tabpos <= simSecList.size()) {
				SimulationSectionAssignment ss = (SimulationSectionAssignment) simSecList.get(tabpos - 1);
				this.bottomFrame = ss.generateURLforBottomFrame(this.running_sim_id, this.actor_id, this.user_id);
			}

		} catch (Exception e) {
			e.printStackTrace();
			this.forward_on = true;
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
		Hashtable<Long, Long> phaseIds = (Hashtable<Long, Long>) session.getServletContext().getAttribute(
				USIP_OSP_ContextListener.CACHEON_PHASE_IDS);

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
	 * This method is called when the user selects a simulation to play.
	 * (From simulation/select_simulation.jsp)
	 * 
	 * @param request
	 */
	public void handleLoadPlayerScenario(HttpServletRequest request) {

		String sending_page = request.getParameter("sending_page");

		if ((sending_page != null) && (sending_page.equalsIgnoreCase("select_simulation"))) {

			schema = request.getParameter("schema");
			schemaOrg = request.getParameter("schema_org");

			session = request.getSession();

			MultiSchemaHibernateUtil.beginTransaction(schema);

			String user_assignment_id = request.getParameter("user_assignment_id");

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
				roundNames = (Hashtable<Long, String>) session.getServletContext().getAttribute(
						USIP_OSP_ContextListener.CACHEON_L_S_ROUND_NAMES);
			} catch (Exception e) {
				e.printStackTrace();
				roundNames = new Hashtable<Long, String>();
				session.getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_L_S_ROUND_NAMES,
						new Hashtable<Long, String>());
			}
			String cachedRoundName = roundNames.get(running_sim_id);
			if (cachedRoundName == null) {
				roundNames.put(running_sim_id, simulation_round);
				request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_L_S_ROUND_NAMES,
						roundNames);
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
				request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_PHASE_IDS,
						phaseIds);

			}
			// //////////////////////////////////////////////////////////////////////


			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			recordLoginToSchema(user_id, schema, actor_id, running_sim_id, request);

			storeUserInfoInSessionInformation(request);

			UserTrail ut = UserTrail.getMe(schema, myLoggedInTicket.getTrail_id());
			ut.setActor_id(actor_id);
			ut.setRunning_sim_id(running_sim_id);
			ut.saveMe(schema);

			this.hasSelectedRunningSim = true;

			forward_on = true;

		}
	}
	
	/**
	 * Sets values in this PlayerSessionObject to be those stored for the user, and creates
	 * the loggedInTicket to record their presence.
	 * 
	 * @param bu_id
	 * @param schema
	 * @param request
	 * @return
	 */
	public void recordLoginToSchema(Long bu_id, String schema, Long actor_id, Long running_sim_id, HttpServletRequest request) {

		User user = User.getInfoOnLogin(bu_id, schema);
		BaseUser bu = BaseUser.getByUserId(bu_id);

		if (user != null) {
			this.user_id = user.getId();
			this.user_Display_Name = bu.getFull_name();
			
			// Username is also email address
			this.user_name = bu.getUsername();

			myLoggedInTicket.setTrail_id(user.getTrail_id());
			myLoggedInTicket.setUser_id(this.user_id);
			myLoggedInTicket.setActor_id(actor_id);
			myLoggedInTicket.setRunning_sim_id(running_sim_id);
			// Player starts on tab 1, always.
			myLoggedInTicket.setTab_position(new Long(1));

			Hashtable<Long, LoggedInTicket> loggedInUsers = (Hashtable<Long, LoggedInTicket>) request.getSession()
					.getServletContext().getAttribute("loggedInUsers");

			loggedInUsers.put(user.getId(), myLoggedInTicket);

			loggedin = true;
		} else {
			Logger.getRootLogger().warn("Warning: While user selecting simulation, null user detected.");
			loggedin = false;
		}


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
	 * Loads a session.
	 * 
	 * @param request
	 */
	public void handleLoadPlayerAutoAssignedScenario(HttpServletRequest request) {

		MultiSchemaHibernateUtil.beginTransaction(this.schema);

		this.sim_id = new Long(request.getParameter("sim_id"));
		Simulation simulation = (Simulation) MultiSchemaHibernateUtil.getSession(this.schema).get(Simulation.class,
				this.sim_id);

		this.actor_id = new Long(request.getParameter("actor_id"));
		Actor actor = (Actor) MultiSchemaHibernateUtil.getSession(this.schema).get(Actor.class, this.actor_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

		RunningSimulation rs = new RunningSimulation("My Session", this.giveMeSim(), this.schema, null, "Player Self Assigned");
		this.running_sim_id = rs.getId();
		rs.setReady_to_begin(true);

		MultiSchemaHibernateUtil.beginTransaction(this.schema);
		SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil.getSession(this.schema).get(
				SimulationPhase.class, rs.getPhase_id());
		MultiSchemaHibernateUtil.commitAndCloseTransaction(this.schema);

		this.loadSimInfoForDisplay(request, simulation, rs, actor, sp);

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

	
	public static String DEFAULTMEMOTEXT = "To: <BR />From:<BR />Topic:<BR />Message:"; //$NON-NLS-1$

	public String memo_starter_text = DEFAULTMEMOTEXT;

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
	
	public Hashtable getHashtableForThisRunningSim(HttpServletRequest request) {

		// The conversation is pulled out of the context
		Hashtable<Long, Long> highestChangeNumber = (Hashtable<Long, Long>) request.getSession().getServletContext()
				.getAttribute(USIP_OSP_ContextListener.CACHEON_CHANGE_NUMBERS);

		if (highestChangeNumber == null) {
			highestChangeNumber = new Hashtable();
			request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_CHANGE_NUMBERS,
					highestChangeNumber);
		}

		return highestChangeNumber;
	}

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
	
	public Hashtable<String, String> newsAlerts = new Hashtable<String, String>();

	public int myCount = 0;

	/** */
	public Long getHighestChangeNumberForRunningSim(HttpServletRequest request) {

		if (running_sim_id == null) {
			Logger.getRootLogger().debug("returning null");
			return null;
		}
		// The conversation is pulled out of the context
		Hashtable<Long, Long> highestChangeNumber = getHashtableForThisRunningSim(request);

		Long runningSimHighestChange = (Long) highestChangeNumber.get(running_sim_id);

		if (runningSimHighestChange == null) {
			runningSimHighestChange = new Long(1);
			highestChangeNumber.put(running_sim_id, runningSimHighestChange);

			request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_CHANGE_NUMBERS,
					highestChangeNumber);

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

		Logger.getRootLogger().debug("current highest: " + currentHighest.intValue());

		currentHighest = new Long(currentHighest.intValue() + 1);

		Hashtable<Long, Long> highestChangeNumber = getHashtableForThisRunningSim(request);

		highestChangeNumber.put(running_sim_id, currentHighest);

		request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_CHANGE_NUMBERS,
				highestChangeNumber);

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

		Logger.getRootLogger().debug("Round is " + running_sim.getRound());

		this.simulation_round = running_sim.getRound() + "";

		Hashtable<Long, String> roundNames = (Hashtable<Long, String>) request.getSession().getServletContext()
				.getAttribute(USIP_OSP_ContextListener.CACHEON_L_S_ROUND_NAMES);
		roundNames.put(running_sim_id, this.simulation_round);
		request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_L_S_ROUND_NAMES,
				roundNames);

		propagateValues();

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(running_sim);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		// Let people know that there is a change to catch.
		storeNewHighestChangeNumber(request);

	}

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
			Logger.getRootLogger().debug("set rs " + running_sim_id + " to " + phase_id);
			MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(running_sim);

			SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil.getSession(schema).get(
					SimulationPhase.class, phase_id);

			this.phaseName = sp.getName();

			// Store new phase name in web cache.
			Hashtable<Long, String> phaseNames = (Hashtable<Long, String>) request.getSession().getServletContext()
					.getAttribute("phaseNames");

			phaseNames.put(running_sim_id, this.phaseName);
			request.getSession().getServletContext().setAttribute("phaseNames", phaseNames);

			Logger.getRootLogger().debug("setting phase change alert");

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

				for (ListIterator<UserAssignment> li = running_sim.getUser_assignments(schema).listIterator(); li.hasNext();) {
					UserAssignment ua = li.next();
					uniqList.put(ua.getUser_id(), "set");
				}

				SchemaInformationObject sio = SchemaInformationObject.lookUpSIOByName(schema);

				for (Enumeration e = uniqList.keys(); e.hasMoreElements();) {
					Long key = (Long) e.nextElement();
					Logger.getRootLogger().debug("need to email " + key);

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
		 * (Exception real_e){ Logger.getRootLogger().debug("Exception of type: " +
		 * real_e.getClass()); } }
		 */

	}
	
	public Simulation giveMeSim() {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		Simulation simulation = (Simulation) MultiSchemaHibernateUtil.getSession(schema).get(Simulation.class, sim_id);

		MultiSchemaHibernateUtil.getSession(schema).evict(simulation);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		return simulation;

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
					Logger.getRootLogger().debug("forwarin on to : " + sim.getLastPhaseId(this.schema).toString()); //$NON-NLS-1$
					this.changePhase(sim.getLastPhaseId(this.schema).toString(), request);

					// Forward them back
					this.forward_on = true;
					this.backPage = "../simulation/simwebui.jsp?tabposition=1"; //$NON-NLS-1$
				}
			}
		}

	}
	
	public Hashtable pushedInjects = new Hashtable();
	
	public boolean handlePushInject(HttpServletRequest request) {

		String sending_page = request.getParameter("sending_page"); //$NON-NLS-1$

		if ((sending_page != null) && (sending_page.equalsIgnoreCase("push_injects"))) { //$NON-NLS-1$

			String announcement_text = request.getParameter("announcement_text"); //$NON-NLS-1$
			String inject_action = request.getParameter("inject_action"); //$NON-NLS-1$
			String inject_id_string = request.getParameter("inject_id"); //$NON-NLS-1$

			if (inject_id_string != null) {
				Long inject_id = new Long(inject_id_string);
				this.pushedInjects.put(inject_id, "set"); //$NON-NLS-1$
			}

			if ((inject_action != null) && (inject_action.equalsIgnoreCase("2"))) { //$NON-NLS-1$
				announcement_text = announcement_text
						+ "<BR /><strong>Communicate with Control your actions</strong><BR />"; //$NON-NLS-1$
			}

			String player_target = request.getParameter("player_target"); //$NON-NLS-1$

			if ((player_target != null) && (player_target.equalsIgnoreCase("some"))) { //$NON-NLS-1$
				this.alertInQueueText = announcement_text;
				this.alertInQueueType = Alert.TYPE_EVENT;
				return true;
			} else {
				makeGeneralAnnouncement(announcement_text, request);
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
			Logger.getRootLogger().warn("Null CS_ID sent to pso.handlePlayerReflection");
			return new PlayerReflection();
		}

		PlayerReflection playerReflection = PlayerReflection.getPlayerReflection(schema, cs_id, running_sim_id,
				actor_id);

		String sending_page = (String) request.getParameter("sending_page");
		String update_text = (String) request.getParameter("update_text");

		if ((sending_page != null) && (update_text != null) && (sending_page.equalsIgnoreCase("player_reflection"))) {
			String player_reflection_text = (String) request.getParameter("player_reflection_text");

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
	
	public void notifyOfDocChanges(String schema, Long sd_id, Long excluded_actor_id, HttpServletRequest request,
			CustomizeableSection cs) {

		List listToNotify = SharedDocActorNotificAssignObj.getAllAssignmentsForDocument(schema, sd_id);

		for (ListIterator<SharedDocActorNotificAssignObj> li = listToNotify.listIterator(); li.hasNext();) {
			SharedDocActorNotificAssignObj sdanao = (SharedDocActorNotificAssignObj) li.next();

			if (!(sdanao.getActor_id().equals(excluded_actor_id))) {

				Alert al = new Alert();
				al.setSpecific_targets(true);
				al.setType(Alert.TYPE_MEMO);
				al.setThe_specific_targets(sdanao.getActor_id().toString());
				al.setAlertMessage(sdanao.getNotificationText());
				al.setRunning_sim_id(running_sim_id);
				al.saveMe(schema);

				makeTargettedAnnouncement(al, sdanao.getActor_id().toString(), request);
			}
		}

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
	
	public void loadPhaseNameInWebCache(HttpServletRequest request, SimulationPhase sp) {
		// //////////////////////////////////////////////////////////////////////
		// Store it in the web cache, if this has not been done already
		// by another user.
		Hashtable<Long, String> phaseNames = (Hashtable<Long, String>) this.session.getServletContext().getAttribute(
				USIP_OSP_ContextListener.CACHEON_L_S_PHASE_NAMES);

		String cachedPhaseName = phaseNames.get(this.running_sim_id);
		if (cachedPhaseName == null) {
			phaseNames.put(this.running_sim_id, sp.getName());
			request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_L_S_PHASE_NAMES,
					phaseNames);

		}
	}
	
	public String getInjectColor(Long injectId) {

		String unshotColor = "#FFFFFF"; //$NON-NLS-1$
		String shotColor = "#FFCCCC"; //$NON-NLS-1$

		String hashValue = (String) this.pushedInjects.get(injectId);

		if (hashValue == null) {
			return unshotColor;
		} else {
			return shotColor;
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
	
	/** Error message to be shown to the user. */
	public String errorMsg = ""; //$NON-NLS-1$
	
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
	
	/** If user has selected an author, instructor or admin entry point into the system, 
	 * this is called to set their AFSO object.
	 * 
	 * @param request
	 * @param schema_id
	 */
	public static void handleInitialEntry(HttpServletRequest request){
		
		String initial_entry = (String) request.getParameter("initial_entry");
		
		if ((initial_entry != null) && (initial_entry.equalsIgnoreCase("true"))){
			
			PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true), true);
			
			String schema_id = (String) request.getParameter("schema_id");
			
			SchemaInformationObject sio = SchemaInformationObject.getMe(new Long(schema_id));
			
			pso.schema = sio.getSchema_name();
			pso.schemaOrg = sio.getSchema_organization();
			
			OSPSessionObjectHelper osp_soh = (OSPSessionObjectHelper) request.getSession(true).getAttribute("osp_soh");
			
			User user = User.getMe(pso.schema, osp_soh.getUserid());
			BaseUser bu = BaseUser.getByUserId(osp_soh.getUserid());
				
			if (user != null) {
				pso.user_id = user.getId();

				pso.user_Display_Name = bu.getFull_name();
				pso.user_name = bu.getUsername();
				
				pso.loggedin = true;
				
				user.setLastLogin(new Date());
				user.saveMe(pso.schema);
				
			} else {
				pso.loggedin = false;
				Logger.getRootLogger().warn("handling initial entry into simulation and got null user");
			}
		}
	}

}
