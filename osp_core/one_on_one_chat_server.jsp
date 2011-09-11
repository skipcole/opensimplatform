<%@ page 
	contentType="text/xml; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" 
%><%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		return;
	}
	
	ArrayList <String> chat = pso.getChat(request);
	
	//response.setContentType("application/xml");
	response.setHeader("Content-Type", "text/xml");
	response.setHeader("Cache-Control", "no-cache");
	
%><?xml version="1.0" encoding="utf-8"?>
<response><status><%= chat.get(0) %></status><%= chat.get(1) %></response>