<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.communications.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" 
	errorPage="" %>
<%

/**
 * 
 * @author Ronald Skip Cole
 *

 * This file is part of the Open Simulation Platform.<br>    The Open Simulation Platform is free software; you can redistribute it and/or modify
    it under the terms of the BSD Style license included with this distribution.<br>

    The Open Simulation Platform is distributed WITHOUT ANY WARRANTY; 
    without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  <BR>

    See the license file for more details.<BR>
 */
 
 
 	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (   (pso.running_sim_id == null)  ||  (!(pso.isLoggedin()))  ) {
		return;
	}
	
	String convLinesToReturn = ChatController.getConversation(request, pso);
	
%><%= convLinesToReturn %>