<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.communications.*,org.usip.oscw.baseobjects.*" 
	errorPage="" 
%>
<%

	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);

	String conversation =  (String) request.getParameter("conversation");
	String start_index = (String) request.getParameter("start_index");

	ChatController.checkIfUserOnline(pso.schema, pso.running_sim_id.toString(), checking_actor, checked_actor);
	
%>
<?xml version="1.0"?>
<response>
 <status><%= status_code %></status>
</response>