<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" 
	errorPage="" %><%

	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), false);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String alarmText = pso.getAlarmText(request, response);
	
%><%= alarmText %><%
	//
%>