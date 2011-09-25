<%@ page 
	contentType="text/xml; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %><%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	String status_code = "working";
	
	if (!(pso.isLoggedin())) {
		status_code = "logout";
	}
	
	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");

%><?xml version="1.0"?>
<response>
 <my_status><%= status_code %></my_status>
 <sim_time><%= pso.getGameTime(request) %></sim_time>
 <sim_phase><%= pso.getPhaseName() %></sim_phase>
</response>