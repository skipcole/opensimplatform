<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.communications.*,org.usip.osp.baseobjects.*" 
	errorPage="" 
%>
<%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));

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