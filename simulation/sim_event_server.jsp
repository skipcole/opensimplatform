<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %><%

	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), false);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String alarmText = pso.getAlarmText(request, response);
	String callnumb = request.getParameter("callnumb");
	String running_sim_id = request.getParameter("running_sim_id");
	
	System.out.println("ing_sim_id was: " + running_sim_id + ", alarm text was: " + alarmText);
	
	response.setContentType("text/xml");

%><?xml version="1.0" encoding="UTF-8"?>
<sim_event_text><%= alarmText %></sim_event_text>