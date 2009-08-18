<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %><%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true), false);
	
	String status_code = "working";
	
	if (!(pso.isLoggedin())) {
		status_code = "logout";
	}
	
	response.setContentType("text/xml");

%><?xml version="1.0"?>
<response>
 <my_status><%= status_code %></my_status>
 <sim_round><%= pso.getSimulation_round() %></sim_round>
 <sim_phase><%= pso.getPhaseName() %></sim_phase>
</response>