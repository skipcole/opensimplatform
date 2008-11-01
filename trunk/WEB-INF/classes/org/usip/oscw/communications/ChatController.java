package org.usip.oscw.communications;

import java.util.*;

import javax.servlet.http.*;

import org.usip.oscw.baseobjects.*;
import org.usip.oscw.networking.ParticipantSessionObject;
import org.usip.oscw.persistence.MultiSchemaHibernateUtil;

/**
 * @author Ronald "Skip" Cole
 * 
 *         This file is part of the USIP Online Simulation Platform.<br>
 * 
 *         The USIP Online Simulation Platform is free software; you can
 *         redistribute it and/or modify it under the terms of the new BSD Style
 *         license associated with this distribution.<br>
 * 
 *         The USIP Online Simulation Platform is distributed WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. <BR>
 * 
 */
public class ChatController {

	public static final int NEW_MSG = 1;
	public static final int NO_NEW_MSG = 2;
	
	/** Hashtable to keep track of users on chat page. */
	private static Hashtable onlineUsers = new Hashtable();
	
	/** Keep track of time to check to see if any of the user online flags have expired. */
	private static java.util.Date timeOfLastCheck = new java.util.Date();
	
	/**
	 * This checks to see if an actor is online and returns true or false. In addition it does 2 other 
	 * housekeeping funtions:
	 * 1.) It checks a timer to see if people's time online has elapsed.
	 * 2.) It marks the actor doing the checking as present now.
	 * 
	 * @param schema
	 * @param rsid
	 * @param checking_actor
	 * @param checked_actor
	 * @return
	 */
	public static String checkIfUserOnline (String schema, String rsid, String checking_actor, String checked_actor){
		
		java.util.Date now = new java.util.Date();
		
		System.out.println("timeOfLastCheck is " + timeOfLastCheck.getTime());
		
		if ((timeOfLastCheck.getTime() + (42 * 1000)) > now.getTime()){
			
			timeOfLastCheck = new java.util.Date();
			
			checkUserOnlineFlagsExpired();
			
		}
		
		String checking_actor_key = schema + "_" + rsid + "_" + checking_actor;
		String checked_actor_key = schema + "_" + rsid + "_" + checked_actor;
		
		// Mark the checking actor present
		onlineUsers.put(checking_actor_key, now.getTime() + "");
		
		System.out.println("checked_actor_key: " + checked_actor_key);
		
		for (Enumeration e = onlineUsers.keys(); e.hasMoreElements();){
			String key = (String) e.nextElement();
			
			System.out.println("      checking against key: " + key);
			
			if (key.equalsIgnoreCase(checked_actor_key)){
				return "online";
			}
			
		}
		
		return "offline";
	}
	
	public static void checkUserOnlineFlagsExpired(){
		System.out.println("insert code here");
	}
	
	
	
	
	public static String getConversation(HttpServletRequest request,
			ParticipantSessionObject pso) {

		if ((pso == null) || (pso.running_sim_id == null)) {
			return "";
		}

		// Get user id
		String user_id = request.getParameter("user_id");

		// Get actor id
		String actor_id = request.getParameter("actor_id");

		// Start index is where to start getting messages from
		String start_index = request.getParameter("start_index");

		// Get the new text to add to the conversation
		String newtext = request.getParameter("newtext");

		// Get the key for this conversation
		String conv_id = request.getParameter("conv_id");

		Vector this_conv = getCachedConversation(request, pso, conv_id);
		
		return getConversation(user_id, actor_id, start_index, newtext,
				conv_id, this_conv, pso.running_sim_id, pso.schema);
	}
	
	public static String getConvKey(ParticipantSessionObject pso, String conv_id){
		
		return (pso.schema + "_" + pso.running_sim_id + "_" + conv_id);
		 
	}
	
	public static Vector getCachedConversation(HttpServletRequest request, ParticipantSessionObject pso, String conv_id){
		
		 ///////////////////////////////////////////////////////
		 // The conversation is pulled out of the context Hashtable
		 Hashtable conversation_cache = (Hashtable) request.getSession().getServletContext().getAttribute("conversation_cache");
		 
		 String conversationKey = getConvKey(pso, conv_id);
		 
		 // This conversation is pulled from the set of conversations Vector
		 Vector this_conv = (Vector) conversation_cache.get(conversationKey);

		// At this point, we will try to pull it out of the database
		if (this_conv == null) {
			this_conv = new Vector();

			this_conv = getRunningSimConveration(pso.schema,
					pso.running_sim_id, conv_id);

			conversation_cache.put(conversationKey, this_conv);
		}
		
		return this_conv;
	}

	/**
	 * Looks up all of the lines for the conversation needed and passes them
	 * back. Uses the 'index' to keep track of how much of the conversation the
	 * user needs.
	 * 
	 * @param request
	 * @param pso
	 * @return
	 */
	public static String getConversation(String user_id, String actor_id,
			String start_index, String newtext, String conv_id,
			Vector<ChatLine> this_conv, Long rsid, String schema) {

		if (start_index == null) {
			start_index = "0";
		}

		int start_int = new Integer(start_index).intValue();

		if (newtext != null) {
			ChatLine cl = new ChatLine(user_id, rsid.toString(), actor_id,
					conv_id, newtext);
			cl.saveMe(schema);
			this_conv.add(cl);
		}

		String convLinesToReturn = "";
		for (Enumeration e = this_conv.elements(); e.hasMoreElements();) {
			ChatLine bcl = (ChatLine) e.nextElement();

			// Check to see were are above the start index sent.
			if (bcl.getId().intValue() > start_int) {
				convLinesToReturn += (bcl.packageMe() + "|||||");
			}
		}

		return convLinesToReturn;
	}
	
	public static void insertChatLine(Long user_id,
			Long actor_id, String start_index, String newtext, String conv_id,
			ParticipantSessionObject pso,
			HttpServletRequest request){
		
		 // This conversation is pulled from the set of conversations Vector
		 Vector this_conv = getCachedConversation(request, pso, conv_id);

		// If a line of new text has been passed, tack it on the end.
		if (newtext != null) {
			ChatLine cl = new ChatLine(user_id.toString(), pso.running_sim_id
					.toString(), actor_id.toString(), conv_id, newtext);
			cl.saveMe(pso.schema);
			this_conv.add(cl);
		}
	}
	

	public static String getXMLConversation(Long user_id,
			Long actor_id, String start_index, String conv_id,
			ParticipantSessionObject pso,
			HttpServletRequest request) {
		
		if ((start_index == null) || (start_index.trim().length() == 0)) {
			start_index = "0";
		}
		
		int start_int = new Integer(start_index).intValue();
		
		 // This conversation is pulled from the set of conversations Vector
		 Vector this_conv = getCachedConversation(request, pso, conv_id);

		String convLinesToReturn = "";
		for (Enumeration e = this_conv.elements(); e.hasMoreElements();) {
			ChatLine bcl = (ChatLine) e.nextElement();

			// Check to see were are above the start index sent.
			if (bcl.getId().intValue() > start_int) {
				convLinesToReturn += bcl.packageIntoXML(pso, request);
			}
		}

		return convLinesToReturn;
	}

	/**
	 * 
	 * @param running_sim_id
	 * @param conv_key
	 * @return
	 */
	public static Vector getRunningSimConveration(String schema,
			Long running_sim_id, String conv_id) {

		Vector returnVector = new Vector();

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<ChatLine> returnList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery(
						"from ChatLine where RUNNING_SIM_ID = "
								+ running_sim_id + " AND CONV_ID = '" + conv_id
								+ "'").list();

		for (ListIterator li = returnList.listIterator(); li.hasNext();) {
			ChatLine bcl = (ChatLine) li.next();

			MultiSchemaHibernateUtil.getSession(schema).evict(bcl);
			returnVector.add(bcl);

		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnVector;
	}

	/**
	 * Based on the information passed to it, looks up the conversation and
	 * returns the list of actors assigned to this conversation.
	 * 
	 * @param pso
	 * @param conv_key
	 * @param request
	 * @return
	 */
	public static Vector getActorsForConversation(ParticipantSessionObject pso,
			String conv_id_string, HttpServletRequest request) {

		Vector returnVector = new Vector();

		if ((conv_id_string == null) || (conv_id_string.equalsIgnoreCase(""))) {
			return returnVector;
		}

		Long conv_id = new Long(conv_id_string);

		MultiSchemaHibernateUtil.beginTransaction(pso.schema);

		Conversation conv = (Conversation) MultiSchemaHibernateUtil.getSession(
				pso.schema).get(Conversation.class, conv_id);

		for (ListIterator<ConvActorAssignment> ais = conv
				.getConv_actor_assigns().listIterator(); ais.hasNext();) {

			ConvActorAssignment caa = (ConvActorAssignment) ais.next();

			Long a_id = caa.getActor_id();

			System.out.println("actors id was " + a_id);

			Actor act = (Actor) MultiSchemaHibernateUtil.getSession(pso.schema)
					.get(Actor.class, a_id);
			System.out.println("actor name is " + act.getName());

			ActorGhost ag = new ActorGhost(act);

			returnVector.add(ag);
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);

		return returnVector;
	}

	/**
	 * Returns the list of actors for the broadcast chat - all of the actors
	 * currently assigned to a simulation.
	 * 
	 * @param pso
	 * @param conv_key
	 * @param request
	 * @return
	 */
	public static Vector getActorsForBroadcast(ParticipantSessionObject pso,
			String conv_key, HttpServletRequest request) {

		// The conversation is pulled out of the context
		Hashtable conversation_actors = (Hashtable) request.getSession()
				.getServletContext().getAttribute("conversation_actors");

		Vector<ActorGhost> this_set_of_actors = (Vector<ActorGhost>) conversation_actors
				.get(pso.running_sim_id + "_" + conv_key);

		if (this_set_of_actors == null) {

			System.out.println("doing database hit");

			this_set_of_actors = new Vector();

			MultiSchemaHibernateUtil.beginTransaction(pso.schema);

			Simulation sim = (Simulation) MultiSchemaHibernateUtil.getSession(
					pso.schema).get(Simulation.class, pso.sim_id);

			List daActors = sim.getActors();

			for (ListIterator li = daActors.listIterator(); li.hasNext();) {
				Actor act = (Actor) li.next();

				ActorGhost ag = new ActorGhost();

				ag.setName(act.getName());
				ag.setId(act.getId());
				ag.setDefaultColorChatBubble(act.getDefaultColorChatBubble());

				this_set_of_actors.add(ag);

			}

			conversation_actors.put(pso.running_sim_id + "_" + conv_key,
					this_set_of_actors);

		}

		return this_set_of_actors;

	}

	public static Conversation createConversation(String schema,
			String conversationName, BaseSimSection bss, Long sid) {

		Conversation conv = new Conversation();

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List conv_list = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery(
						"from Conversation where CONV_NAME = '"
								+ conversationName + "' and SIM_ID = " + sid)
				.list();

		if ((conv_list == null) || (conv_list.size() == 0)) {

			conv.setConversation_name(conversationName);
			conv.setSim_id(sid);
			MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(conv);

			Simulation sim = (Simulation) MultiSchemaHibernateUtil.getSession(
					schema).get(Simulation.class, sid);
			sim.getConversations().add(conv);
			MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(sim);
		} else {
			conv = (Conversation) conv_list.get(0);
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return conv;
	}

}
