package org.usip.osp.networking;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/**
 * @author Ronald "Skip" Cole<br />
 *
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
public class USIP_OSP_ContextListener implements ServletContextListener{

    public void contextInitialized(ServletContextEvent sce) {
        
        ServletContext context = sce.getServletContext();
        
        /** Name of the actors stored by schema and rs_id and actor_id */
        Hashtable actor_names = new Hashtable();
        context.setAttribute("actor_names", actor_names);
        
        /**
         */
        Hashtable sim_section_info = new Hashtable();
        context.setAttribute("sim_section_info", sim_section_info);
        
        // Establish the cache of login tickets
        /**
         * Tickets are used to keep track of who is currently logged in.
         */
        Hashtable tickets = new Hashtable();
        context.setAttribute("activeTickets", tickets);
        
        /**
         * Conversations are cached so that the database doesn't have to 
         * be continually hit as players log on and off.
         */
        Hashtable broadcast_conversations = new Hashtable();
        context.setAttribute("broadcast_conversations", broadcast_conversations);
        
        /**
         * Conversations are cached so that the database doesn't have to 
         * be continually hit as players log on and off.
         */
        Hashtable conversation_cache = new Hashtable();
        context.setAttribute("conversation_cache", conversation_cache);
        
        /**
         * The actors available for conversation are cached to help load 
         * chat pages quickly.
         */
        Hashtable conversation_actors = new Hashtable();
        context.setAttribute("conversation_actors", conversation_actors);
        
        /**
         * Chart information can also be cached.
         */
        Hashtable charts = new Hashtable();
        context.setAttribute("charts", charts);
        
        /**
         * When a change is made to the database, 
         * this value is updated for the simulation.
         * Highest change number
         * This alerts the program to check against the database for new values.
         */
        Hashtable<Long, Long> highestChangeNumber = new Hashtable<Long, Long>();
        context.setAttribute("highestChangeNumber", highestChangeNumber);
        
        Hashtable<Long, Long> phaseIds = new Hashtable<Long,Long>();
        context.setAttribute("phaseIds", phaseIds);
        
        Hashtable<Long, String> phaseNames = new Hashtable<Long,String>();
        context.setAttribute("phaseNames", phaseNames);
        
        /** The id of the running simulation is used to find the round name. */
        Hashtable<Long, String> roundNames = new Hashtable<Long,String>();
        context.setAttribute("roundNames", roundNames);
        
        /** The id of the running simulation is keyed */
        Hashtable<Long, String> phaseChangeAlarm = new Hashtable<Long,String>();
        context.setAttribute("phaseChangeAlarm", phaseChangeAlarm);
        
        /** Players who have selected a game are recorded in these */
        Hashtable<Long, Hashtable> loggedInPlayers = new Hashtable<Long,Hashtable>();
        context.setAttribute("loggedInPlayers", loggedInPlayers);
        
        /** All users get an entry in this cache */
        Hashtable<Long, LoggedInTicket> loggedInUsers = new Hashtable<Long,LoggedInTicket>();
        context.setAttribute("loggedInUsers", loggedInUsers);
        
    }

    public void contextDestroyed(ServletContextEvent arg0) {
        
        
    }

}
