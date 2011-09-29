<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %><%
	
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	String textToShow = TimeLine.serveUpTimeLine(request, response, pso);
	
	System.out.println(" in s timeline server");
		
	System.out.println(textToShow);
	
%><?xml version="1.0" encoding="utf-8"?>
<data>
<%= textToShow %>
</data>