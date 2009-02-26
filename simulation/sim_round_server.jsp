<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<%

	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), false);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String from_actor = request.getParameter("from_actor");
	String from_tab = request.getParameter("from_tab");
	
	pso.acceptUserHeartbeatPulses(from_actor, from_tab, request);

%><%= pso.getSimulation_round() %>_<%= pso.getPhaseName() %><%
	//
%>