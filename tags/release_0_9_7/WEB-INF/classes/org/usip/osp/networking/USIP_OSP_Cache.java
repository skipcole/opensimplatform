package org.usip.osp.networking;

import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.Actor;
import org.usip.osp.baseobjects.ActorAssumedIdentity;
import org.usip.osp.baseobjects.BaseSimSection;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.baseobjects.SimActorAssignment;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.SimulationMetaPhase;
import org.usip.osp.baseobjects.SimulationPhase;
import org.usip.osp.baseobjects.User;
import org.usip.osp.baseobjects.UserAssignment;
import org.usip.osp.bishops.BishopsPartyInfo;
import org.usip.osp.communications.InjectFiringHistory;
import org.usip.osp.persistence.BaseUser;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.persistence.UILanguageObject;

/**
 * This Object maintains the caches uses by the OSP.
 * 
 * @author Skip
 * 
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
public class USIP_OSP_Cache {

	public static String getInterfaceText(HttpServletRequest request,
			int languageCode, String textKey) {

		Hashtable allLangHash = (Hashtable) request.getSession()
				.getServletContext().getAttribute(
						USIP_OSP_ContextListener.CACHEON_UI_LOCALIZED_LANGUAGE);

		if ((allLangHash == null) || (allLangHash.size() == 0)) {
			allLangHash = new Hashtable();
			// load languages
			UILanguageObject.loadLanguages();
			allLangHash.put(new Long(UILanguageObject.ENGLISH_LANGUAGE_CODE),
					UILanguageObject.getEngHash());
			allLangHash.put(new Long(UILanguageObject.SPANISH_LANGUAGE_CODE),
					UILanguageObject.getSpanHash());

			// put it back in
			request.getSession().getServletContext().setAttribute(
					USIP_OSP_ContextListener.CACHEON_UI_LOCALIZED_LANGUAGE,
					allLangHash);

		}

		Hashtable langHash = (Hashtable) allLangHash
				.get(new Long(languageCode));

		if (langHash != null) {
			return (String) langHash.get(textKey);
		} else {
			return "unknown language";
		}

	}

	/**
	 * Returns the phase name (looked up from the cache) by it ID.
	 * 
	 * @param request
	 * @param phase_id
	 * @return
	 */
	public static String getPhaseNameById(HttpServletRequest request,
			String schema, Long phase_id) {

		// /////////////////////////////////////////////////////
		// The conversation is pulled out of the context Hashtable
		Hashtable<Long, String> phase_name_by_id_cache = (Hashtable) request
				.getSession().getServletContext().getAttribute(
						USIP_OSP_ContextListener.CACHEON_L_S_PHASE_NAMES_BY_ID);

		if (phase_name_by_id_cache == null) {
			phase_name_by_id_cache = new Hashtable();
		}
		String phaseName = phase_name_by_id_cache.get(phase_id);

		if (phaseName == null) {

			// Get phase name
			SimulationPhase sp = SimulationPhase.getById(schema, phase_id);

			phaseName = sp.getPhaseName();

			// Store it in the cache
			phase_name_by_id_cache.put(phase_id, phaseName);
		}

		request.getSession().getServletContext().setAttribute(
				USIP_OSP_ContextListener.CACHEON_L_S_PHASE_NAMES_BY_ID,
				phase_name_by_id_cache);

		return phaseName;
	}

	public static String getPhaseNameById(HttpServletRequest request,
			String schema, String phase_id) {

		return USIP_OSP_Cache.getPhaseNameById(request, schema, new Long(
				phase_id));

	}

	/**
	 * Returns the phase name (looked up from the cache) by it ID.
	 * 
	 * @param request
	 * @param phase_id
	 * @return
	 */
	public static String getMetaPhaseNameById(HttpServletRequest request,
			String schema, Long phase_id) {

		// /////////////////////////////////////////////////////
		// The conversation is pulled out of the context Hashtable
		Hashtable<Long, String> meta_phase_name_by_id_cache = (Hashtable) request
				.getSession()
				.getServletContext()
				.getAttribute(
						USIP_OSP_ContextListener.CACHEON_L_S_METAPHASE_NAMES_BY_ID);

		if (meta_phase_name_by_id_cache == null) {
			meta_phase_name_by_id_cache = new Hashtable();
		}
		String metaPhaseName = meta_phase_name_by_id_cache.get(phase_id);

		if (metaPhaseName == null) {

			// Get phase name
			SimulationMetaPhase sp = SimulationMetaPhase.getById(schema,
					phase_id);

			metaPhaseName = sp.getMetaPhaseName();

			// Store it in the cache
			meta_phase_name_by_id_cache.put(phase_id, metaPhaseName);
		}

		request.getSession().getServletContext().setAttribute(
				USIP_OSP_ContextListener.CACHEON_L_S_METAPHASE_NAMES_BY_ID,
				meta_phase_name_by_id_cache);

		return metaPhaseName;
	}

	/**
	 * 
	 * @param request
	 * @param a_id
	 * @return
	 */
	public static String getActorName(String schema, Long sim_id,
			Long running_sim_id, HttpServletRequest request, Long a_id) {

		ServletContext context = request.getSession().getServletContext();

		Hashtable<String, String> actor_names = (Hashtable<String, String>) context
				.getAttribute("actor_names");

		if (actor_names == null) {
			actor_names = new Hashtable<String, String>();
			context.setAttribute("actor_names", actor_names);
		}

		String a_name = actor_names.get(schema + "_" + running_sim_id + " "
				+ a_id);

		// If we did not find a name in the hashtable for this actor, we will
		// load it for all actors.
		if (a_name == null) {
			loadActorNamesInHashtable(schema, sim_id, running_sim_id,
					actor_names);
			a_name = actor_names
					.get(schema + "_" + running_sim_id + " " + a_id);
			context.setAttribute("actor_names", actor_names);
		}

		return a_name;
	}

	/**
	 * 
	 * @param request
	 * @param a_id
	 * @return
	 */
	public static String getActorBaseName(String schema, Long sim_id,
			HttpServletRequest request, Long a_id) {

		ServletContext context = request.getSession().getServletContext();

		Hashtable<String, String> actor_names = (Hashtable<String, String>) context
				.getAttribute("actor_names");

		if (actor_names == null) {
			actor_names = new Hashtable<String, String>();
			context.setAttribute("actor_names", actor_names);
		}

		String a_name = actor_names.get(schema + "_base_" + a_id);

		// If we did not find a name in the hashtable for this actor, we will
		// load it for all actors.
		if (a_name == null) {
			loadActorBaseNamesInHashtable(schema, sim_id, actor_names);
			a_name = actor_names.get(schema + "_base_" + a_id);
			context.setAttribute("actor_names", actor_names);
		}

		return a_name;
	}

	public static void setActorName(String newName, String schema, Long sim_id,
			Long running_sim_id, HttpServletRequest request, Long a_id) {

		ServletContext context = request.getSession().getServletContext();

		Hashtable<String, String> actor_names = (Hashtable<String, String>) context
				.getAttribute("actor_names");

		if (actor_names == null) {
			actor_names = new Hashtable<String, String>();
			context.setAttribute("actor_names", actor_names);
		}

		actor_names.put(schema + "_" + running_sim_id + " " + a_id, newName);

	}

	/**
	 * Stores names in a hashtable so they can be pulled out quickly from the
	 * context.
	 * 
	 * @param actor_names
	 */
	public static void loadActorNamesInHashtable(String schema, Long sim_id,
			Long running_sim_id, Hashtable actor_names) {

		Simulation sim = Simulation.getById(schema, sim_id);

		for (ListIterator<Actor> li = sim.getActors(schema).listIterator(); li
				.hasNext();) {
			Actor act = li.next();

			ActorAssumedIdentity aai = ActorAssumedIdentity.getAssumedIdentity(
					schema, act.getId(), running_sim_id);

			// Check for the existence of an assumed identity
			if (aai != null) {
				actor_names.put(schema + "_" + running_sim_id + " "
						+ act.getId(), aai.getAssumedName());
			} else {
				actor_names.put(schema + "_" + running_sim_id + " "
						+ act.getId(), act.getInitialActorName());
			}
		}

	}

	/**
	 * Places the original actor names in a table for easy lookup. (Actors may
	 * have aliases, the the actual display name to the player may be
	 * different.)
	 * 
	 * @param schema
	 * @param sim_id
	 * @param actor_names
	 */
	public static void loadActorBaseNamesInHashtable(String schema,
			Long sim_id, Hashtable actor_names) {

		Simulation sim = Simulation.getById(schema, sim_id);

		for (ListIterator<Actor> li = sim.getActors(schema).listIterator(); li
				.hasNext();) {
			Actor act = li.next();
			actor_names.put(schema + "_base_" + act.getId(), act
					.getInitialActorName());
		}

	}

	/**
	 * Returns the change number table for a particular running simulation.
	 * 
	 * @param request
	 * @return
	 */
	public static Hashtable getAlertNumberHashtableForRunningSim(
			HttpServletRequest request) {

		// The conversation is pulled out of the context
		Hashtable<Long, Long> highestAlertNumberHashtable = (Hashtable<Long, Long>) request
				.getSession().getServletContext().getAttribute(
						USIP_OSP_ContextListener.CACHEON_ALERT_NUMBERS);

		if (highestAlertNumberHashtable == null) {
			highestAlertNumberHashtable = new Hashtable();
			request.getSession().getServletContext().setAttribute(
					USIP_OSP_ContextListener.CACHEON_ALERT_NUMBERS,
					highestAlertNumberHashtable);
		}

		return highestAlertNumberHashtable;
	}

	/**
	 * TODO: Yes we should be doing this with transactions to avoid race
	 * conditions.
	 * 
	 * @param request
	 * @param running_sim_id
	 * @return
	 */
	public static Long getNextHighestChangeNumber(HttpServletRequest request,
			Long running_sim_id) {

		Hashtable<Long, Long> highestChangeNumberHashtable = getAlertNumberHashtableForRunningSim(request);

		Long changeNumber = highestChangeNumberHashtable.get(running_sim_id);

		Long nextChangeNumber = new Long(changeNumber.intValue() + 1);

		highestChangeNumberHashtable.put(running_sim_id, nextChangeNumber);

		// I don't know if this next step is needed.
		request.getSession().getServletContext().setAttribute(
				USIP_OSP_ContextListener.CACHEON_ALERT_NUMBERS,
				highestChangeNumberHashtable);

		return changeNumber;

	}

	/**
	 * Pulls the name of the image file out of the cache, or loads it if not
	 * found.
	 * 
	 * @param request
	 * @param a_id
	 * @return
	 */
	public static String getActorThumbImage(HttpServletRequest request,
			String schema, Long running_sim_id, Long a_id, Long sim_id) {

		ServletContext context = request.getSession().getServletContext();

		Hashtable<String, String> actor_thumbs = (Hashtable<String, String>) context
				.getAttribute(USIP_OSP_ContextListener.CACHEON_ACTOR_THUMBS);

		if (actor_thumbs == null) {
			actor_thumbs = new Hashtable<String, String>();
			context.setAttribute("actor_thumbs", actor_thumbs);
		}

		String a_thumb = actor_thumbs.get(schema + "_" + running_sim_id + " "
				+ a_id);
		if (a_thumb == null) {
			loadActorThumbsInHashtable(actor_thumbs, schema, running_sim_id,
					sim_id);
			a_thumb = actor_thumbs.get(schema + "_" + running_sim_id + " "
					+ a_id);
			context.setAttribute("actor_thumbs", actor_thumbs);
		}

		return a_thumb;

	}

	/**
	 * Loads all of the actor thumbnail image names into the schema.
	 * 
	 * @param actor_thumbs
	 * @param schema
	 * @param running_sim_id
	 * @param sim_id
	 */
	public static void loadActorThumbsInHashtable(Hashtable actor_thumbs,
			String schema, Long running_sim_id, Long sim_id) {

		Logger.getRootLogger().debug(
				"storing namges actor thumb nail images in hashtable. ");
		Simulation sim = Simulation.getById(schema, sim_id);

		for (ListIterator<Actor> li = sim.getActors(schema).listIterator(); li
				.hasNext();) {
			Actor act = li.next();

			if (act.getImageThumbFilename() != null) {
				actor_thumbs.put(schema + "_" + running_sim_id + " "
						+ act.getId(), act.getImageThumbFilename());
			} else {
				actor_thumbs.put(schema + "_" + running_sim_id + " "
						+ act.getId(), "no_image_default_thumb.jpg");
			}

		}
	}

	/**
	 * 
	 * @param request
	 * @param a_id
	 * @return
	 */
	public static String getUSERName(String schema, HttpServletRequest request,
			Long user_id) {

		if (user_id == null) {
			return "";
		}

		Hashtable<String, String> user_names_hash = getCachedHashtable(request,
				USIP_OSP_ContextListener.CACHEON_USER_NAMES, "string");

		String user_name = user_names_hash.get(schema + "_" + user_id);

		if (user_name == null) {
			BaseUser user = BaseUser.getByUserId(user_id);

			user_name = user.getFull_name();
			user_names_hash.put(schema + "_" + user_id, user_name);

			// context.setAttribute(USIP_OSP_ContextListener.CACHEON_USER_NAMES,
			// user_names_hash);
		}

		return user_name;
	}

	/**
	 * 
	 * @param schema
	 * @param rs_id
	 * @param a_id
	 * @param request
	 * @return
	 */
	public static String getUserAssigned(String schema, Long rs_id, Long a_id,
			HttpServletRequest request) {

		if ((rs_id == null) || (a_id == null)) {
			return UNASSIGNED;
		}

		Hashtable<String, String> user_assignments_hash = getCachedHashtable(
				request, USIP_OSP_ContextListener.CACHEON_USER_ASSIGNMENTS,
				"string");

		String user_id = user_assignments_hash.get(schema + "_" + rs_id + "_"
				+ a_id);

		if (user_id == null) {
			loadRunningSimsUserAssignments_dont_use(schema, rs_id,
					user_assignments_hash);

			user_id = user_assignments_hash.get(schema + "_" + rs_id + "_"
					+ a_id);
		}

		return user_id;

	}

	public static final String UNASSIGNED = "unassigned";

	/*
	 * Loop over the actors for this running simulation and load all. If user is
	 * not assigned to actor, put string 'unassigned' into hashtable We should
	 * Not leave the user_id null, or else we will keep hitting the database.
	 */
	public static void loadRunningSimsUserAssignments_dont_use(String schema,
			Long rs_id, Hashtable user_assignments_hash) {

		Logger.getRootLogger().debug("doing loadRunningSimsUserAssignments");

		RunningSimulation rs = RunningSimulation.getById(schema, rs_id);
		List actors_in_sim = SimActorAssignment.getActorsAssignmentsForSim(
				schema, rs.getSim_id());

		// Loop over all of the actors that should be assigned.
		for (ListIterator<SimActorAssignment> li = actors_in_sim.listIterator(); li
				.hasNext();) {
			SimActorAssignment this_saa = li.next();

			User user = UserAssignment.get_A_UserAssigned_dont_use(schema,
					rs_id, this_saa.getActorId());

			if (user != null) { // If found, enter their user_id into the
				// hashtable
				user_assignments_hash.put(schema + "_" + rs_id + "_"
						+ this_saa.getActorId(), user.getId().toString());
			} else { // If not found, enter 'unassigned'
				user_assignments_hash.put(schema + "_" + rs_id + "_"
						+ this_saa.getActorId(), UNASSIGNED);
			}
		}

	}

	public static final String CACHED_TABLE_LONG_HASHTABLE = "long_hashtable";
	public static final String CACHED_TABLE_LONG_STRING = "long_string";
	public static final String CACHED_TABLE_LONG_LONG = "long_long";
	public static final String CACHED_TABLE_STRING_VECTOR = "string_vector";
	public static final String CACHED_TABLE_LIST = "list";
	public static final String CACHED_TABLE_LONG_LIST = "long_list";

	/**
	 * Pulls the hashtable from the context.
	 * 
	 * @param context
	 * @param hashkey
	 * @return
	 */
	public static Hashtable getCachedHashtable(HttpServletRequest request,
			String hashkey, String dType) {

		ServletContext context = request.getSession().getServletContext();
		Hashtable cacheWeWant = new Hashtable();

		if (dType.equalsIgnoreCase(CACHED_TABLE_STRING_VECTOR)) {
			cacheWeWant = (Hashtable<String, Vector>) context
					.getAttribute(hashkey);

			if (cacheWeWant == null) {
				cacheWeWant = new Hashtable<String, Vector>();
				context.setAttribute(hashkey, cacheWeWant);
			}
		} else if (dType.equalsIgnoreCase("string")) {
			cacheWeWant = (Hashtable<String, String>) context
					.getAttribute(hashkey);

			if (cacheWeWant == null) {
				cacheWeWant = new Hashtable<String, String>();
				context.setAttribute(hashkey, cacheWeWant);
			}
		} else if (dType.equalsIgnoreCase("hashtable")) {
			cacheWeWant = (Hashtable<String, Hashtable>) context
					.getAttribute(hashkey);

			if (cacheWeWant == null) {
				cacheWeWant = new Hashtable<String, Hashtable>();
				context.setAttribute(hashkey, cacheWeWant);
			}
		} else if (dType.equalsIgnoreCase(CACHED_TABLE_LONG_HASHTABLE)) {
			cacheWeWant = (Hashtable<Long, Hashtable>) context
					.getAttribute(hashkey);

			if (cacheWeWant == null) {
				cacheWeWant = new Hashtable<Long, Hashtable>();
				context.setAttribute(hashkey, cacheWeWant);
			}
		} else if (dType.equalsIgnoreCase(CACHED_TABLE_LONG_STRING)) {
			cacheWeWant = (Hashtable<Long, String>) context
					.getAttribute(hashkey);

			if (cacheWeWant == null) {
				cacheWeWant = new Hashtable<Long, String>();
				context.setAttribute(hashkey, cacheWeWant);
			}
		} else if (dType.equalsIgnoreCase(CACHED_TABLE_LONG_LONG)) {
			cacheWeWant = (Hashtable<Long, Long>) context.getAttribute(hashkey);

			if (cacheWeWant == null) {
				cacheWeWant = new Hashtable<Long, Long>();
				context.setAttribute(hashkey, cacheWeWant);
			}
		} else if (dType.equalsIgnoreCase(CACHED_TABLE_LONG_LIST)) {
			cacheWeWant = (Hashtable<String, List>) context
					.getAttribute(hashkey);

			if (cacheWeWant == null) {
				cacheWeWant = new Hashtable<String, List>();
				context.setAttribute(hashkey, cacheWeWant);
			}
		} else {
			Logger.getLogger("root").warn(
					"getCachedHash hashtable type not set");
			cacheWeWant = (Hashtable) context.getAttribute(hashkey);

			if (cacheWeWant == null) {
				cacheWeWant = new Hashtable();
				context.setAttribute(hashkey, cacheWeWant);
			}
		}

		return cacheWeWant;
	}

	/**
	 * 
	 * @param schema
	 * @param request
	 * @param user_name
	 * @return
	 */
	public static Long getUserIdByName(String schema,
			HttpServletRequest request, String user_name) {

		if (user_name == null) {
			return null;
		}

		Hashtable<String, String> user_ids_hash = getCachedHashtable(request,
				USIP_OSP_ContextListener.CACHEON_USER_IDS, "string");

		String userId = user_ids_hash.get(schema + "_" + user_name);

		if (userId == null) {
			BaseUser user = BaseUser.getByUsername(user_name.toString());

			if (user != null) {
				user_ids_hash.put(schema + "_" + user_name, user.getId()
						.toString());
				return user.getId();
			} else {
				return null;
			}
		} else {
			return new Long(userId);
		}

	}

	/**
	 * Returns a list of autocomplete names.
	 * 
	 * @param schema
	 * @param request
	 * @return
	 */
	public static Hashtable getPlayerAutocompleteUserNames(String schema,
			HttpServletRequest request) {

		Hashtable<String, Hashtable> allUserNameTables = getCachedHashtable(
				request,
				USIP_OSP_ContextListener.CACHEON_AUTOCOMPLETE_PLAYER_USERNAMES,
				"hashtable");

		Hashtable thisUserNameTable = (Hashtable) allUserNameTables.get(schema);

		if (thisUserNameTable == null) {
			thisUserNameTable = new Hashtable();
		}

		if (thisUserNameTable.size() == 0) {
			Logger.getLogger("root").debug(
					"getAutocompleteUserNames pulling data from database");
			for (ListIterator<User> li = User.getAllForSchemaAndLoadDetails(
					schema).listIterator(); li.hasNext();) {
				User user = li.next();
				thisUserNameTable.put(user.getUserName(), user.getId());
			}
			allUserNameTables.put(schema, thisUserNameTable);
		}

		return thisUserNameTable;
	}

	public static Hashtable getAutocompleteUserNames(String schema,
			HttpServletRequest request) {

		Hashtable<String, Hashtable> allUserNameTables = getCachedHashtable(
				request,
				USIP_OSP_ContextListener.CACHEON_AUTOCOMPLETE_USERNAMES,
				"hashtable");

		Hashtable thisUserNameTable = (Hashtable) allUserNameTables.get(schema);

		if (thisUserNameTable == null) {
			thisUserNameTable = new Hashtable();
		}

		if (thisUserNameTable.size() == 0) {
			Logger.getLogger("root").debug(
					"getAutocompleteUserNames pulling data from database");
			for (ListIterator<User> li = User.getAllForSchemaAndLoadDetails(
					schema).listIterator(); li.hasNext();) {
				User user = li.next();
				thisUserNameTable.put(user.getUserName() + " | "
						+ user.getBu_full_name(), user.getId());
			}
			allUserNameTables.put(schema, thisUserNameTable);
		}

		return thisUserNameTable;
	}

	public static Hashtable getInjectsFired(String schema,
			HttpServletRequest request, Long rs_id, Long a_id) {

		Hashtable<String, Hashtable> allInjectsFiredTable = getCachedHashtable(
				request, USIP_OSP_ContextListener.CACHEON_INJECTS_FIRED,
				"hashtable");

		Hashtable thisSetOfTables = (Hashtable) allInjectsFiredTable
				.get(schema);

		if (thisSetOfTables == null) {
			thisSetOfTables = new Hashtable();
		}

		String keyForRsAndActor = getRsActorKey(rs_id, a_id);

		Hashtable returnTable = (Hashtable) thisSetOfTables
				.get(keyForRsAndActor);

		if (returnTable == null) {
			returnTable = new Hashtable();
		}

		if (returnTable.size() == 0) {
			loadInjectHistoryCache(returnTable, schema, rs_id, a_id);
		}

		thisSetOfTables.put(keyForRsAndActor, returnTable);

		return returnTable;
	}

	/**
	 * This adds an inject into the cache.
	 * 
	 * @param schema
	 * @param request
	 * @param rs_id
	 * @param a_id
	 * @param inj_id
	 * @param targets
	 * @return
	 */
	public static Hashtable addFiredInjectsToCache(String schema,
			HttpServletRequest request, Long rs_id, Long a_id, Long inj_id,
			String targets) {

		if ((rs_id == null) || (a_id == null)) {
			Logger.getRootLogger().warn(
					"USIP_OSP_Cache.addFiredInjectsToCache (rs/a): " + rs_id
							+ " / " + a_id);
			return new Hashtable();
		}

		Hashtable starterTable = getInjectsFired(schema, request, rs_id, a_id);

		starterTable.put(inj_id, targets);

		return starterTable;

	}

	/**
	 * Returns the Running Sim _ Actor Id Hash Key.
	 * 
	 * @param rs_id
	 * @param a_id
	 * @return
	 */
	public static String getRsActorKey(Long rs_id, Long a_id) {
		String returnString = rs_id.toString() + "_" + a_id.toString();

		return returnString;
	}

	/**
	 * This loads the injects from the database and puts them in the cached
	 * hashtable. It also adds one extra (key/value = 0/set) to indicate that
	 * this has actually been pulled out of the database.
	 * 
	 * @param schema
	 * @param rs_id
	 * @param a_id
	 */
	public static void loadInjectHistoryCache(Hashtable thisTable,
			String schema, Long rs_id, Long a_id) {

		List<InjectFiringHistory> dbList = InjectFiringHistory
				.getAllForRunningSimAndActor(schema, rs_id, a_id);

		// Loop over all of the actors that should be assigned.
		for (ListIterator<InjectFiringHistory> li = dbList.listIterator(); li
				.hasNext();) {
			InjectFiringHistory ifh = li.next();

			thisTable.put(ifh.getInjectId(), ifh.getTargets());

		}

		// Add this entry to signal that cache has been loaded from the
		// database.
		thisTable.put(new Long(0), "all");

	}

	public static String getSimulationNameById(HttpServletRequest request,
			String schema, Long sim_id) {

		// /////////////////////////////////////////////////////
		// The conversation is pulled out of the context Hashtable
		Hashtable<Long, String> simulation_name_by_id_cache = (Hashtable) request
				.getSession().getServletContext().getAttribute(
						USIP_OSP_ContextListener.CACHEON_SIM_NAMES_BY_ID);

		if (simulation_name_by_id_cache == null) {
			simulation_name_by_id_cache = new Hashtable();
		}
		String simulationName = simulation_name_by_id_cache.get(sim_id);

		if (simulationName == null) {

			// Get phase name
			Simulation sim = Simulation.getById(schema, sim_id);

			simulationName = sim.getDisplayName();

			// Store it in the cache
			simulation_name_by_id_cache.put(sim_id, simulationName);
		}

		request.getSession().getServletContext().setAttribute(
				USIP_OSP_ContextListener.CACHEON_SIM_NAMES_BY_ID,
				simulation_name_by_id_cache);

		return simulationName;
	}

	/** Used to keep track of sections that an author can add. */
	public static final String CACHEON_BASE_SECTIONS = "base_section_info"; //$NON-NLS-1$

	/** Used to keep track of sections that an author can add. */
	public static final String CACHEON_CUSTOMIZED_SECTIONS = "custom_section_info"; //$NON-NLS-1$

	/** Flag used to indicate that cache may need updated. */
	public static boolean cacheon_customized_sections_invalidated = true;

	/**
	 * Pulls the list of Base Sections out of the cache.
	 * 
	 * @param schema
	 * @param request
	 * @return
	 */
	public static List getBaseSectionInformation(String schema,
			HttpServletRequest request) {

		ServletContext context = request.getSession().getServletContext();

		List thisListOfBaseSections = (List<BaseSimSection>) context
				.getAttribute(CACHEON_BASE_SECTIONS);

		if ((thisListOfBaseSections == null)
				|| (thisListOfBaseSections.size() == 0)) {
			thisListOfBaseSections = BaseSimSection.getAll(schema);
			context.setAttribute(CACHEON_BASE_SECTIONS, thisListOfBaseSections);
		}

		return thisListOfBaseSections;
	}

	/**
	 * Pulls the list of Custom Sections out of the cache.
	 * 
	 * @param schema
	 * @param request
	 * @return
	 */
	public static List getCustomSectionInformation(String schema,
			HttpServletRequest request) {

		ServletContext context = request.getSession().getServletContext();

		List thisListOfCustomSections = (List<CustomizeableSection>) context
				.getAttribute(CACHEON_CUSTOMIZED_SECTIONS);

		if ((cacheon_customized_sections_invalidated)
				|| (thisListOfCustomSections == null)
				|| (thisListOfCustomSections.size() == 0)) {
			thisListOfCustomSections = CustomizeableSection
					.getAllUncustomized(schema);
			context.setAttribute(CACHEON_CUSTOMIZED_SECTIONS,
					thisListOfCustomSections);
			cacheon_customized_sections_invalidated = false;
		}

		return thisListOfCustomSections;
	}

}
