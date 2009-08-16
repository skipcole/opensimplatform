package org.usip.osp.networking;

import java.util.Hashtable;
import java.lang.reflect.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

/**
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
public class USIP_OSP_ContextListener implements ServletContextListener {

	/** Name of the actors stored by schema and rs_id and actor_id */
	public static final String CACHEON_ACTOR_NAMES = "actor_names"; //$NON-NLS-1$

	/**
	 * Name of the actor thumb nail images stored by schema and rs_id and
	 * actor_id
	 */
	public static final String CACHEON_ACTOR_THUMBS = "actor_thumbs"; //$NON-NLS-1$

	/** Simulation section Information is continually being accessed. */
	public static final String CACHEON_SIM_SEC_INFO = "sim_section_info"; //$NON-NLS-1$
	
	/**
	 * Conversations are cached so that the database doesn't have to be
	 * continually hit as players log on and off.
	 */
	public static final String CACHEON_BROADCAST_CONV = "broadcast_conversations"; //$NON-NLS-1$
	
	/**
	 * Conversations are cached so that the database doesn't have to be
	 * continually hit as players log on and off.
	 */
	public static final String CACHEON_CONVERSATIONS = "conversation_cache"; //$NON-NLS-1$
	
	/**
	 * The actors available for conversation are cached to help load chat
	 * pages quickly.
	 */
	public static final String CACHEON_CONV_ACTORS = "conversation_actors"; //$NON-NLS-1$
	
	/**
	 * Chart information can also be cached.
	 */
	public static final String CACHEON_CHARTS = "charts"; //$NON-NLS-1$
	
	/**
	 * When a change is made to the database, this value is updated for the
	 * simulation. Highest change number This alerts the program to check
	 * against the database for new values.
	 */
	public static final String CACHEON_CHANGE_NUMBERS = "highestChangeNumber"; //$NON-NLS-1$

	public static final String CACHEON_PHASE_IDS = "phaseIds"; //$NON-NLS-1$
	public static final String CACHEON_L_S_PHASE_NAMES = "phaseNames"; //$NON-NLS-1$
	
	/** The id of the running simulation is used to find the round name. */
	public static final String CACHEON_L_S_ROUND_NAMES = "roundNames"; //$NON-NLS-1$
	
	/** The id of the running simulation is keyed */
	public static final String CACHEON_L_S_PHASE_CHANGE_ALARMS = "phaseChangeAlarm"; //$NON-NLS-1$

	/** Tickets are used to keep track of who is currently logged in. */
	public static final String CACHEON_ACTIVE_TICKETS = "activeTickets"; //$NON-NLS-1$
	
	
	public static final String CACHEON_LOGGED_IN_PLAYERS = "loggedInPlayers"; //$NON-NLS-1$
	
	
	public static final String CACHEON_LOGGED_IN_USERS = "loggedInUsers"; //$NON-NLS-1$

	public static void main(String args[]) {
		Logger.getRootLogger().debug("Hello World"); //$NON-NLS-1$

	}

	public static void resetWebCache(HttpServletRequest request) {
		
		resetWebCache(request.getSession());
		
	}
	
	public static void resetWebCache(HttpSession session) {
		
		resetWebCache(session.getServletContext());
	}
	
	/**
	 * This loops over all of the public static final fi
	 * 
	 * @param sce
	 */
	public static void resetWebCache(ServletContext context) {

		Field field[] = USIP_OSP_ContextListener.class.getFields();

		for (int ii = 0; ii < field.length; ++ii) {

			String fieldName = field[ii].getName();
			int mod = field[ii].getModifiers();
			String modifiers = Modifier.toString(mod);

			if (modifiers.equalsIgnoreCase("public static final")) { //$NON-NLS-1$

				try {
					String field_value = (String) field[ii].get(null);
					if (fieldName.contains("_L_S_")){ //$NON-NLS-1$
						context.setAttribute(field_value, new Hashtable<Long, String>());
					} else {
						context.setAttribute(field_value, new Hashtable());
					}

				} catch (Exception e) {
					Logger.getRootLogger().warn("Exception in USIP_OSP_ContextListener.resetWebCache"); //$NON-NLS-1$
					Logger.getRootLogger().warn("Error was: " + e.getMessage()); //$NON-NLS-1$
					Logger.getRootLogger().warn("While trying to set field: " + fieldName); //$NON-NLS-1$
				}
			}

		}

	}

	public void contextInitialized(ServletContextEvent sce) {

		ServletContext context = sce.getServletContext();
		
		USIP_OSP_ContextListener.resetWebCache(context);

		/*

		Hashtable<Long, Long> highestChangeNumber = new Hashtable<Long, Long>();
		context.setAttribute(CACHEON_CHANGE_NUMBERS, highestChangeNumber);

		Hashtable<Long, Long> phaseIds = new Hashtable<Long, Long>();
		context.setAttribute(CACHEON_PHASE_IDS, phaseIds);

		/** Players who have selected a game are recorded in these 
		Hashtable<Long, Hashtable> loggedInPlayers = new Hashtable<Long, Hashtable>();
		context.setAttribute(CACHEON_LOGGED_IN_PLAYERS, loggedInPlayers);

		/** All users get an entry in this cache 
		Hashtable<Long, LoggedInTicket> loggedInUsers = new Hashtable<Long, LoggedInTicket>();
		context.setAttribute(CACHEON_LOGGED_IN_USERS, loggedInUsers);
		*/

	}

	public void contextDestroyed(ServletContextEvent arg0) {

	}

}
