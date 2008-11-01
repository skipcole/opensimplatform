<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.communications.*,org.usip.oscw.baseobjects.*" 
	errorPage="" 
%>
<%

	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);

	String checking_actor =  (String) request.getParameter("checking_actor");
	String checked_actor = (String) request.getParameter("checked_actor");

	String status = "online";
	
	if ((pso != null) && (pso.running_sim_id != null)){
		status = ChatController.checkIfUserOnline(pso.schema, pso.running_sim_id.toString(), checking_actor, checked_actor);
	}
	
	System.out.println("Hit the actor online checker");
	
%>
<?xml version="1.0"?>
<response>
<status_code><%= status %></status_code>
</response>