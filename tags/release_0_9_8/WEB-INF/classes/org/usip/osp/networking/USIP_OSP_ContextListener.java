package org.usip.osp.networking;

import java.util.Hashtable;
import java.lang.reflect.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.*;
import org.usip.osp.baseobjects.USIP_OSP_Util;

/*
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
	
	/** Key to get hashtable with localized language strings. */
	public static final String CACHEON_UI_LOCALIZED_LANGUAGE = "local_lang";

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
	public static final String CACHEON_ALERT_NUMBERS = "highestAlertNumber"; //$NON-NLS-1$

	public static final String CACHEON_PHASE_IDS = "phaseIds"; //$NON-NLS-1$
	
	public static final String CACHEON_L_S_PHASE_NAMES_BY_RS_ID = "phaseNames"; //$NON-NLS-1$
	
	public static final String CACHEON_L_S_PHASE_NAMES_BY_ID = "phaseNamesById"; //$NON-NLS-1$
	
	public static final String CACHEON_L_S_METAPHASE_NAMES_BY_ID = "metaPhaseNamesById"; //$NON-NLS-1$
	
	/** The id of the running simulation is used to find the round name. */
	public static final String CACHEON_L_S_ROUND_NAMES = "roundNames"; //$NON-NLS-1$
	
	/** The id of the running simulation is keyed */
	public static final String CACHEON_L_S_PHASE_CHANGE_ALARMS = "phaseChangeAlarm"; //$NON-NLS-1$

	/** Tickets are used to keep track of who is currently logged in. */
	public static final String CACHEON_ACTIVE_TICKETS = "activeTickets"; //$NON-NLS-1$
	
	public static final String CACHEON_LOGGED_IN_PLAYERS = "loggedInPlayers"; //$NON-NLS-1$
	
	public static final String CACHEON_LOGGED_IN_USERS = "loggedInUsers"; //$NON-NLS-1$
	
	public static final String CACHEON_USER_ASSIGNMENTS = "user_assignments"; //$NON-NLS-1$
	
	public static final String CACHEON_USER_NAMES = "user_names"; //$NON-NLS-1$
	
	public static final String CACHEON_USER_IDS = "user_ids"; //$NON-NLS-1$
	
	public static final String CACHEON_BPI_NAMES = "bpi_names"; //$NON-NLS-1$
	
	public static final String CACHEON_AUTOCOMPLETE_USERNAMES = "acu_names"; //$NON-NLS-1$
	
	public static final String CACHEON_AUTOCOMPLETE_PLAYER_USERNAMES = "acup_names"; //$NON-NLS-1$
	
	public static final String CACHEON_SIM_NAMES_BY_ID = "simNamesById"; //$NON-NLS-1$

	/** Used to keep track of which injects have been fired during a game. */
	public static final String CACHEON_INJECTS_FIRED = "injects_fired"; //$NON-NLS-1$
	
	public static final String CACHEON_GAMETIMER = "usip_osp_game_timer";
	
	/**
	 * Utility method that ultimately leads on to resetWebCache(ServletContext)
	 * @param request
	 */
	public static void resetWebCache(HttpServletRequest request, String schema) {
		
		resetWebCache(request.getSession(), schema);
		
	}
	
	/**
	 * Utility method that ultimately leads on to resetWebCache(ServletContext)
	 * @param request
	 */
	public static void resetWebCache(HttpSession session, String schema) {
		
		resetWebCache(session.getServletContext(), schema);
	}
	
	/**
	 * This loops over all of the public static final fields in this class itself and uses their names
	 * to get the stored caches, which it then sets to new Hashtables.
	 * 
	 * @param sce
	 */
	public static void resetWebCache(ServletContext context, String schema) {

		// Moving to cached based on schema name, this now does nothing. It missed the real
		// caches.
		
		Field field[] = USIP_OSP_ContextListener.class.getFields();

		for (int ii = 0; ii < field.length; ++ii) {

			String fieldName = field[ii].getName();
			int mod = field[ii].getModifiers();
			String modifiers = Modifier.toString(mod);
			
			System.out.println(fieldName);

			if (modifiers.equalsIgnoreCase("public static final")) { //$NON-NLS-1$

				try {
					String field_value = (String) field[ii].get(null);
					
					String completeHashKey = USIP_OSP_Cache.makeSchemaSpecificHashKey(schema, field_value);
					System.out.println(" reseting cache: " + completeHashKey);
					
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

	/** Used to keep track of sections that an author can add. */
	public static final String CACHEON_CUSTOMIZED_SECTIONS = "custom_section_info"; //$NON-NLS-1$

	/** Used to keep track of sections that an author can add. */
	public static final String CACHEON_BASE_SECTIONS = "base_section_info"; //$NON-NLS-1$

	public static final String CACHED_TABLE_LONG_HASHTABLE = "long_hashtable";

	public static final String CACHED_TABLE_LONG_STRING = "long_string";

	public static final String CACHED_TABLE_LONG_LONG = "long_long";

	public static final String CACHED_TABLE_STRING_VECTOR = "string_vector";

	public static final String CACHED_TABLE_LIST = "list";

	public static final String CACHED_TABLE_LONG_LIST = "long_list";
	
	public static final String CACHED_TABLE_LONG_GPCT = "long_gpct";

	/**
	 */
	public void contextInitialized(ServletContextEvent sce) {

		// SC - 9/20/2011. 
		// I don't think we need this, and moving to a 'schema based cache' makes it more 
		// difficult and error prone to enable.
		// ServletContext context = sce.getServletContext();
		// USIP_OSP_ContextListener.resetWebCache(context);
		
		// Running into problem hitting the database before tomcat has restarted completely
		// since the logout page leads automatically back to the login page. This may need to change.
		//USIP_OSP_Util.completedStartup = true;

	}

	public void contextDestroyed(ServletContextEvent arg0) {

	}

}