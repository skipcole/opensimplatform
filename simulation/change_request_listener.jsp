<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.communications.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" 
%>
<%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
		
	String message = (String) request.getParameter("message");

	String returnMsg = pso.acceptChangeRequest(message);
	
%>
<?xml version="1.0"?>
<%= returnMsg %>