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
	private static final String CACHEON_UI_LOCALIZED_LANGUAGE = "local_lang";

	/** This key is not made schema specific, since all schemas on an installation will pull from the
	 * same set of language properties files.
	 * 
	 * @return
	 */
	public static String getCacheonUILocalizedLanguage() {
		return CACHEON_UI_LOCALIZED_LANGUAGE;
	}

	/** Simulation section Information is continually being accessed. */
	private static final String CACHEON_SIM_SEC_INFO = "sim_section_info"; //$NON-NLS-1$

	public static String getCacheonSimSecInfo(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_SIM_SEC_INFO);
	}

	private static final String CACHEON_PHASE_IDS = "phaseIds"; //$NON-NLS-1$

	public static String getCacheonPhaseIds(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_PHASE_IDS);
	}

	private static final String CACHEON_PHASE_NAMES_BY_RS_ID = "phaseNames"; //$NON-NLS-1$

	public static String getCacheonPhaseNamesByRsId(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_PHASE_NAMES_BY_RS_ID);
	}

	/**
	 * Name of the actor thumb nail images stored by schema and rs_id and
	 * actor_id
	 */
	private static final String CACHEON_ACTOR_THUMBS = "actor_thumbs"; //$NON-NLS-1$

	public static String getCacheonActorThumbs(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_ACTOR_THUMBS);
	}

	/**
	 * Conversations are cached so that the database doesn't have to be
	 * continually hit as players log on and off.
	 */
	private static final String CACHEON_CONVERSATIONS = "conversation_cache"; //$NON-NLS-1$

	public static String getCacheonConversations(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_CONVERSATIONS);
	}

	/**
	 * When a change is made to the database, this value is updated for the
	 * simulation. Highest change number This alerts the program to check
	 * against the database for new values.
	 */
	private static final String CACHEON_ALERT_NUMBERS = "highestAlertNumber"; //$NON-NLS-1$

	public static String getCacheonAlertNumbers(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_ALERT_NUMBERS);
	}

	private static final String CACHEON_PHASE_NAMES_BY_ID = "phaseNamesById"; //$NON-NLS-1$

	public static String getCacheonPhaseNamesById(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_PHASE_NAMES_BY_ID);
	}

	private static final String CACHEON_METAPHASE_NAMES_BY_ID = "metaPhaseNamesById"; //$NON-NLS-1$

	public static String getCacheonMetaphaseNamesById(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_METAPHASE_NAMES_BY_ID);
	}

	/** The id of the running simulation is used to find the round name. */
	private static final String CACHEON_ROUND_NAMES = "roundNames"; //$NON-NLS-1$

	public static String getCacheonRoundNames(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_ROUND_NAMES);
	}

	private static final String CACHEON_LOGGED_IN_PLAYERS = "loggedInPlayers"; //$NON-NLS-1$

	public static String getCacheonLoggedInPlayers(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_LOGGED_IN_PLAYERS);
	}

	private static final String CACHEON_LOGGED_IN_USERS = "loggedInUsers"; //$NON-NLS-1$

	public static String getCacheonLoggedInUsers(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_LOGGED_IN_USERS);
	}

	private static final String CACHEON_USER_ASSIGNMENTS = "user_assignments"; //$NON-NLS-1$

	public static String getCacheonUserAssignments(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_USER_ASSIGNMENTS);
	}

	private static final String CACHEON_USER_NAMES = "user_names"; //$NON-NLS-1$

	public static String getCacheonUserNames(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_USER_NAMES);
	}

	private static final String CACHEON_USER_IDS = "user_ids"; //$NON-NLS-1$

	public static String getCacheonUserIds(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_USER_IDS);
	}

	private static final String CACHEON_BPI_NAMES = "bpi_names"; //$NON-NLS-1$

	public static String getCacheonBpiNames(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_BPI_NAMES);
	}
	
	private static final String CACHEON_AUTOCOMPLETE_USERNAMES = "acu_names"; //$NON-NLS-1$

	public static String getCacheonAutocompleteUsernames(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_AUTOCOMPLETE_USERNAMES);
	}
	
	private static final String CACHEON_AUTOCOMPLETE_PLAYER_USERNAMES = "acup_names"; //$NON-NLS-1$

	public static String getCacheonAutocompletePlayerUsernames(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_AUTOCOMPLETE_PLAYER_USERNAMES);
	}
	
	private static final String CACHEON_SIM_NAMES_BY_ID = "simNamesById"; //$NON-NLS-1$

	public static String getCacheonSimNamesById(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_SIM_NAMES_BY_ID);
	}
	
	/** Used to keep track of which injects have been fired during a game. */
	private static final String CACHEON_INJECTS_FIRED = "injects_fired"; //$NON-NLS-1$

	public static String getCacheonInjectsFired(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_INJECTS_FIRED);
	}
	
	private static final String CACHEON_GAMETIMER = "usip_osp_game_timer";

	public static String getCacheonGametimer(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_GAMETIMER);
	}
	
	/** Used to keep track of sections that an author can add. */
	private static final String CACHEON_BASE_SECTIONS = "base_section_info"; //$NON-NLS-1$

	public static String getCacheonBaseSections(String schema) {
		return USIP_OSP_Cache.makeSchemaSpecificHashKey(schema,
				CACHEON_BASE_SECTIONS);
	}
	
	// /////////////////////////////////////////////////////////////////////////////////////////////

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
	 * @param sce		The context for which the cache should be reset.
	 * @param schema	The database schema in which the user is working.
	 */
	public static void resetWebCache(ServletContext context, String schema) {

		Field field[] = USIP_OSP_ContextListener.class.getFields();

		for (int ii = 0; ii < field.length; ++ii) {

			String fieldName = field[ii].getName();
			int mod = field[ii].getModifiers();
			String modifiers = Modifier.toString(mod);

			if (modifiers.contains("static final")) { //$NON-NLS-1$

				try {
					String field_value = (String) field[ii].get(null);

					String completeHashKey = USIP_OSP_Cache
							.makeSchemaSpecificHashKey(schema, field_value);

					if (fieldName.contains("_L_S_")) { //$NON-NLS-1$
						context.setAttribute(field_value,
								new Hashtable<Long, String>());
					} else {
						context.setAttribute(field_value, new Hashtable());
					}

				} catch (Exception e) {
					Logger.getRootLogger()
							.warn("Exception in USIP_OSP_ContextListener.resetWebCache"); //$NON-NLS-1$
					Logger.getRootLogger().warn("Error was: " + e.getMessage()); //$NON-NLS-1$
					Logger.getRootLogger().warn(
							"While trying to set field: " + fieldName); //$NON-NLS-1$
				}
			}
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// Nothing to do here yet

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// Nothing to do here yet

	}

}
