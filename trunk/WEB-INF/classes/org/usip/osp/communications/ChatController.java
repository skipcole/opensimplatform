package org.usip.osp.communications;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.*;

import org.usip.osp.baseobjects.*;
import org.usip.osp.networking.AuthorFacilitatorSessionObject;
import org.usip.osp.networking.PlayerSessionObject;
import org.usip.osp.networking.USIP_OSP_Cache;
import org.usip.osp.networking.USIP_OSP_ContextListener;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.apache.log4j.*;
/**
 * This class provides utility functions to manage a chat session.
 *
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
public class ChatController {

	public static final int NEW_MSG = 1;
	public static final int NO_NEW_MSG = 2;

	/** Hashtable to keep track of users on chat page. */
	private static Hashtable onlineUsers = new Hashtable();

	/**
	 * Keep track of time to check to see if any of the user online flags have
	 * expired.
	 */
	private static java.util.Date timeOfLastCheck = new java.util.Date();

	/**
	 * This checks to see if an actor is online and returns true or false. In
	 * addition it does 2 other housekeeping funtions: 1.) It checks a timer to
	 * see if people's time online has elapsed. 2.) It marks the actor doing the
	 * checking as present now.
	 * 
	 * @param schema
	 * @param rsid
	 * @param checking_actor
	 * @param checked_actor
	 * @return
	 */
	public static String checkIfUserOnline(String schema, String rsid,
			String checking_actor, String checked_actor) {

		java.util.Date now = new java.util.Date();

		Logger.getRootLogger().debug("timeOfLastCheck is " + timeOfLastCheck.getTime()); //$NON-NLS-1$

		if ((timeOfLastCheck.getTime() + (42 * 1000)) > now.getTime()) {

			timeOfLastCheck = new java.util.Date();

			checkUserOnlineFlagsExpired();

		}

		String checking_actor_key = schema + "_" + rsid + "_" + checking_actor; //$NON-NLS-1$ //$NON-NLS-2$
		String checked_actor_key = schema + "_" + rsid + "_" + checked_actor; //$NON-NLS-1$ //$NON-NLS-2$

		// Mark the checking actor present
		onlineUsers.put(checking_actor_key, now.getTime() + ""); //$NON-NLS-1$

		Logger.getRootLogger().debug("checked_actor_key: " + checked_actor_key); //$NON-NLS-1$

		for (Enumeration e = onlineUsers.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();

			Logger.getRootLogger().debug("      checking against key: " + key); //$NON-NLS-1$

			if (key.equalsIgnoreCase(checked_actor_key)) {
				return "online"; //$NON-NLS-1$
			}

		}

		return "offline"; //$NON-NLS-1$
	}

	/**
	 * Checks to see if a user online ticket has expired.
	 * 
	 */
	public static void checkUserOnlineFlagsExpired() {

		for (Enumeration e = onlineUsers.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();

			java.util.Date now = new java.util.Date();

			String usersLastTouchTime = (String) onlineUsers.get(key);
			
			Logger.getRootLogger().debug("usersLastTouchTime: " + usersLastTouchTime); //$NON-NLS-1$

			long longULTT = new Long(usersLastTouchTime).longValue();

			if (((longULTT + (60 * 1000)) < now.getTime())) {
				Logger.getRootLogger().debug("removing: " + key); //$NON-NLS-1$
				onlineUsers.remove(key);
			}

		}
	}


	/** Returns a key for the conversation based on schema, running sim id and conversation id. */
	public static String getConvKey(PlayerSessionObject pso, String conv_id) {

		return (pso.schema + "_" + pso.getRunningSimId() + "_" + conv_id); //$NON-NLS-1$ //$NON-NLS-2$

	}

	public static Vector getCachedConversation(HttpServletRequest request,
			PlayerSessionObject pso, String conv_id) {

		// /////////////////////////////////////////////////////
		// The conversation is pulled out of the context Hashtable
		Hashtable<String, Vector> conversation_cache = USIP_OSP_Cache.
			getCachedHashtable(
					request,
					USIP_OSP_ContextListener.CACHEON_CONVERSATIONS,
					USIP_OSP_Cache.CACHED_TABLE_STRING_VECTOR);

		String conversationKey = getConvKey(pso, conv_id);

		// This conversation is pulled from the set of conversations Vector
		Vector this_conv = (Vector) conversation_cache.get(conversationKey);

		// At this point, we will try to pull it out of the database
		if (this_conv == null) {
			this_conv = new Vector();

			this_conv = getRunningSimConveration(pso.schema,
					pso.getRunningSimId(), conv_id);

			conversation_cache.put(conversationKey, this_conv);
		}

		return this_conv;
	}
	
	/**
	 * 
	 * @param request
	 * @param pso
	 * @param conv_id
	 * @return
	 */
	public static String getHTMLConv(HttpServletRequest request,
			PlayerSessionObject pso, String conv_id){

		String convLinesToReturn = ""; //$NON-NLS-1$
		
		Vector this_conv = getCachedConversation(request, pso, conv_id);
		
		for (Enumeration e = this_conv.elements(); e.hasMoreElements();) {
			ChatLine bcl = (ChatLine) e.nextElement();
			
			String fromAName = USIP_OSP_Cache.getActorName(pso.schema, pso.sim_id, pso.getRunningSimId(), request, bcl.getFromActor());
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm a"); //$NON-NLS-1$

			// Check to see were are above the start index sent.
			if (bcl.getId().intValue() > 0) {
				convLinesToReturn += (sdf.format(bcl.getMsgDate()) + " : <B>" +  fromAName + "</B> says &quot;" + bcl.getMsgtext() + "&quot;<br />"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}

		return convLinesToReturn;
	}


	public static void insertChatLine(Long user_id, Long actor_id,
			String start_index, String newtext, String conv_id,
			PlayerSessionObject pso, HttpServletRequest request) {

		// This conversation is pulled from the set of conversations Vector
		Vector this_conv = getCachedConversation(request, pso, conv_id);

		// If a line of new text has been passed, tack it on the end.
		if (newtext != null) {
			ChatLine cl = new ChatLine(user_id.toString(), pso.getRunningSimId()
					.toString(), actor_id.toString(), conv_id, newtext);
			cl.saveMe(pso.schema);
			this_conv.add(cl);
		}
	}

	public static String getXMLConversation(String start_index, String conv_id, PlayerSessionObject pso,
			HttpServletRequest request) {

		if ((start_index == null) || (start_index.trim().length() == 0)) {
			start_index = "0"; //$NON-NLS-1$
		}

		int start_int = new Integer(start_index).intValue();

		// This conversation is pulled from the set of conversations Vector
		Vector this_conv = getCachedConversation(request, pso, conv_id);

		String convLinesToReturn = ""; //$NON-NLS-1$
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
						"from ChatLine where RUNNING_SIM_ID = " //$NON-NLS-1$
								+ running_sim_id + " AND CONV_ID = '" + conv_id //$NON-NLS-1$
								+ "'").list(); //$NON-NLS-1$

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
	public static Vector getActorsForConversation(PlayerSessionObject pso,
			Long conv_id, HttpServletRequest request) {

		Vector returnVector = new Vector();

		if (conv_id == null) {
			Logger.getRootLogger().debug("waring empty conversation id passe in"); //$NON-NLS-1$
			return returnVector;
		}

		Conversation conv = new Conversation();
		conv.setId(conv_id);

		for (ListIterator<ConvActorAssignment> ais = conv
				.getConv_actor_assigns(pso.schema).listIterator(); ais.hasNext();) {

			ConvActorAssignment caa = ais.next();

			Long a_id = caa.getActor_id();

			Logger.getRootLogger().debug("actors id was " + a_id); //$NON-NLS-1$
			MultiSchemaHibernateUtil.beginTransaction(pso.schema);
			Actor act = (Actor) MultiSchemaHibernateUtil.getSession(pso.schema)
					.get(Actor.class, a_id);
			Logger.getRootLogger().debug("actor name is " + act.getActorName()); //$NON-NLS-1$
			MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
			
			ActorGhost ag = new ActorGhost(act);
			
			ag.setName(act.getActorName(pso.schema, pso.getRunningSimId(), request));

			returnVector.add(ag);
		}

		return returnVector;
	}

}
