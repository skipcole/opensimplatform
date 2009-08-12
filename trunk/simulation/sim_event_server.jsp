<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %><%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true), false);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String alarmXML = pso.alarmXML(request, response);
	
	response.setContentType("text/xml");

%><?xml version="1.0" encoding="UTF-8"?>
<%= alarmXML %>