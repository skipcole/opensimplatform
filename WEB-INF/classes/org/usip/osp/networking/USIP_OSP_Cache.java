package org.usip.osp.networking;

import java.util.Hashtable;
import java.util.ListIterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.Actor;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.SimulationPhase;

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
public class USIP_OSP_Cache {

	/** Returns the phase name (looked up from the cache) by it ID.
	 * 
	 * @param request
	 * @param phase_id
	 * @return
	 */
	public static String getPhaseNameById(HttpServletRequest request, String schema, Long phase_id) {

		// /////////////////////////////////////////////////////
		// The conversation is pulled out of the context Hashtable
		Hashtable <Long, String> phase_name_by_id_cache = (Hashtable)  request.getSession()
				.getServletContext().getAttribute(USIP_OSP_ContextListener.CACHEON_L_S_PHASE_NAMES_BY_ID);

		if (phase_name_by_id_cache == null){
			phase_name_by_id_cache = new Hashtable();
		}
		String phaseName = phase_name_by_id_cache.get(phase_id);
		
		if (phaseName == null){
			
			// Get phase name
			SimulationPhase sp = SimulationPhase.getMe(schema, phase_id);
			
			phaseName = sp.getName();
			
			// Store it in the cache
			phase_name_by_id_cache.put(phase_id, phaseName);
		}
		
		request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_L_S_PHASE_NAMES_BY_ID, phase_name_by_id_cache);
		
		return phaseName;
	}

	public static String getPhaseNameById(HttpServletRequest request, String schema, String phase_id) {

		return getPhaseNameById(request, schema, new Long(phase_id));

	}
	
	/**
	 * 
	 * @param request
	 * @param a_id
	 * @return
	 */
	public static String getActorName(String schema, Long sim_id, Long running_sim_id, HttpServletRequest request, Long a_id) {

		ServletContext context = request.getSession().getServletContext();

		Hashtable<String, String> actor_names = (Hashtable<String, String>) context.getAttribute("actor_names");

		if (actor_names == null) {
			actor_names = new Hashtable<String, String>();
			context.setAttribute("actor_names", actor_names);
		}

		String a_name = actor_names.get(schema + "_" + running_sim_id + " " + a_id);
		if (a_name == null) {
			loadActorNamesInHashtable(schema, sim_id, running_sim_id, actor_names);
			a_name = actor_names.get(schema + "_" + running_sim_id + " " + a_id);
			context.setAttribute("actor_names", actor_names);
		}

		return a_name;
	}

	/**
	 * Stores names in a hashtable so they can be pulled out quickly from the
	 * context.
	 * 
	 * @param actor_names
	 */
	public static void loadActorNamesInHashtable(String schema, Long sim_id, Long running_sim_id, Hashtable actor_names) {

		Logger.getRootLogger().debug("storing names in hashtable. ");
		Simulation sim = Simulation.getMe(schema, sim_id);

		for (ListIterator<Actor> li = sim.getActors(schema).listIterator(); li.hasNext();) {
			Actor act = li.next();

			actor_names.put(schema + "_" + running_sim_id + " " + act.getId(), act.getName());

		}
	}

}
