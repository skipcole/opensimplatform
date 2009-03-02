<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" 
	errorPage="../error.jsp" %>
<%

	ParticipantSessionObject.logout(request);
	session.setAttribute("pso", null);
	
	response.sendRedirect("index.jsp?");
	return;
%>