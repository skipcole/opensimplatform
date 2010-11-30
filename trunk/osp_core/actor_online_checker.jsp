<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.communications.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" 
%>
<%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));

	String checking_actor =  (String) request.getParameter("checking_actor");
	String checked_actor = (String) request.getParameter("checked_actor");

	String status = "online";
	
	if ((pso != null) && (pso.getRunningSimId() != null)){
		status = ChatController.checkIfUserOnline(pso.schema, pso.getRunningSimId().toString(), checking_actor, checked_actor);
	}
	
	
%>
<?xml version="1.0"?>
<response>
<status_code><%= status %></status_code>
</response>