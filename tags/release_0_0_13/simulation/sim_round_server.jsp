<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %><%

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
 <sim_round><%= pso.getSimulation_round() %></sim_round>
 <sim_phase><%= pso.getPhaseName() %></sim_phase>
</response>