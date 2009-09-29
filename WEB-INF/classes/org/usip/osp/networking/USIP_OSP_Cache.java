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

		return USIP_OSP_Cache.getPhaseNameById(request, schema, new Long(phase_id));

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
	
	/**
	 * Returns the change number table for a particular running simulation.
	 * 
	 * @param request
	 * @return
	 */
	public static Hashtable getAlertNumberHashtableForRunningSim(HttpServletRequest request) {

		// The conversation is pulled out of the context
		Hashtable<Long, Long> highestAlertNumberHashtable = (Hashtable<Long, Long>) request.getSession().getServletContext()
				.getAttribute(USIP_OSP_ContextListener.CACHEON_ALERT_NUMBERS);

		if (highestAlertNumberHashtable == null) {
			highestAlertNumberHashtable = new Hashtable();
			request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_ALERT_NUMBERS,
					highestAlertNumberHashtable);
		}

		return highestAlertNumberHashtable;
	}
	
	/**
	 * TODO: Yes we should be doing this with transactions to avoid race conditions.
	 * 
	 * @param request
	 * @param running_sim_id
	 * @return
	 */
	public static Long getNextHighestChangeNumber(HttpServletRequest request, Long running_sim_id){
		
		Hashtable<Long, Long> highestChangeNumberHashtable = getAlertNumberHashtableForRunningSim(request);
		
		Long changeNumber = highestChangeNumberHashtable.get(running_sim_id);
		
		Long nextChangeNumber = new Long(changeNumber.intValue() + 1);
		
		highestChangeNumberHashtable.put(running_sim_id, nextChangeNumber);
		
		// I don't know if this next step is needed.
		request.getSession().getServletContext().setAttribute(USIP_OSP_ContextListener.CACHEON_ALERT_NUMBERS,
				highestChangeNumberHashtable);
		
		return changeNumber;
		
	}
	
	/**
	 * Pulls the name of the image file out of the cache, or loads it if not found.
	 * 
	 * @param request
	 * @param a_id
	 * @return
	 */
	public static String getActorThumbImage(HttpServletRequest request, String schema, 
			Long running_sim_id, Long a_id, Long sim_id) {
				
			ServletContext context = request.getSession().getServletContext();

			Hashtable<String, String> actor_thumbs = (Hashtable<String, String>) context.getAttribute(USIP_OSP_ContextListener.CACHEON_ACTOR_THUMBS);

			if (actor_thumbs == null) {
				actor_thumbs = new Hashtable<String, String>();
				context.setAttribute("actor_thumbs", actor_thumbs);
			}

			String a_thumb = actor_thumbs.get(schema + "_" + running_sim_id + " " + a_id);
			if (a_thumb == null) {
				loadActorThumbsInHashtable(actor_thumbs, schema, running_sim_id, sim_id);
				a_thumb = actor_thumbs.get(schema + "_" + running_sim_id + " " + a_id);
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
	public static void loadActorThumbsInHashtable(Hashtable actor_thumbs, String schema, Long running_sim_id, Long sim_id) {

		Logger.getRootLogger().debug("storing namges actor thumb nail images in hashtable. ");
		Simulation sim = Simulation.getMe(schema, sim_id);

		for (ListIterator<Actor> li = sim.getActors(schema).listIterator(); li.hasNext();) {
			Actor act = li.next();

			if (act.getImageThumbFilename() != null) {
				actor_thumbs.put(schema + "_" + running_sim_id + " " + act.getId(), act.getImageThumbFilename());
			} else {
				actor_thumbs.put(schema + "_" + running_sim_id + " " + act.getId(), "no_image_default_thumb.jpg");
			}

		}
	}

}
