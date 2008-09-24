<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.communications.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*" 
	errorPage="" %>
<%

/**
 * 
 * @author Ronald Skip Cole
 *
 * Copyright 2007 Ronald "Skip" Cole. <br>

 * This file is part of the Online Simulation Platform.<br>    The Online Simulation Platform is free software; you can redistribute it and/or modify
    it under the terms of the BSD Style license included with this distribution.<br>

    The Online Simulation Platform is distributed WITHOUT ANY WARRANTY; 
    without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  <BR>

    See the license file for more details.<BR>
 */
 
 	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);

	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
		
	// Get conversation id
	String conversation_id = request.getParameter("conversation_id");
	
	// Get from actor id
	String from_actor_id = request.getParameter("actor_id");
	
	// Get to actor id
	String to_actor_id = request.getParameter("actor_id");
	
	// Start index is where to start getting messages from
	String start_index = request.getParameter("start_index");
	
	// Get the new text to add to the conversation
	String newtext = request.getParameter("newtext");
	
	// If no conversatiion id or actor id, then return
	if ((conversation_id == null) || (actor_id == null)) {
		return;
	}
	
	if (start_index == null) {
		start_index = "0";
	}
	int start_int = new Integer(start_index).intValue();
	
	// The conversation is pulled out of the context
	Hashtable broadcast_conversations = (Hashtable) getServletContext().getAttribute("broadcast_conversations");
	
	// This conversation is pulled from the set of conversations 
	Vector this_conf = (Vector) broadcast_conversations.get(conversation_id);
	
	// At this point, we will try to pull it out of the database
	if (this_conf == null) {
		this_conf = new Vector();
		
		//////////////////////////
		try {
		
			String queryId = "select * from comm_prvmsgs where running_sim_id = " + conversation_id;
			
			Connection connection = MysqlDatabase.getConnection();	
			
			Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(queryId);

            while (rs.next()) {
				BroadcastLine bcl = new BroadcastLine();
				bcl.running_sim_id = rs.getInt("running_sim_id");
                bcl.index = rs.getInt("comm_broadcast_id");
				bcl.fromChatter = rs.getInt("from_actor_id");
				bcl.msgtext = rs.getString("text");
				
				this_conf.add(bcl);
            }
			
			stmt.close();
			connection.close();

        } catch (Exception e) {
			e.printStackTrace();
		}
		
		broadcast_conversations.put(conversation_id, this_conf);
	}
	
	
	if (newtext != null) {
		BroadcastLine bcl = new BroadcastLine(pso.running_sim.id, actor_id, newtext);
		
		// Stores the chat line to the database. Also sets the index of it.
		bcl.store();
		///////////////////////////////////////////
		
		this_conf.add(bcl);
	}
	
	String convLinesToReturn = "";
	for (Enumeration e = this_conf.elements(); e.hasMoreElements();){
		BroadcastLine bcl = (BroadcastLine) e.nextElement();
		
		// Check to see were are above the start index sent.
		if (bcl.index > start_int){
			convLinesToReturn += (bcl.packageMe() + "|||||");
		}
	}
	
%><%= convLinesToReturn %>
<%
	
%>