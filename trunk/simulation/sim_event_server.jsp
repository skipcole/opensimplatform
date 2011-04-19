<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %><%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		//response.sendRedirect("index.jsp");
		return;
	}
	
	String alarmXML = pso.alarmXML(request, response);
	
	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");


%><?xml version="1.0" encoding="UTF-8"?>
<%= alarmXML %>