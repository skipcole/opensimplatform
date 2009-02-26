<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.communications.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" 
%>
<%

	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
		
	String message = (String) request.getParameter("message");

	String returnMsg = pso.acceptChangeRequest(message);
	
%>
<?xml version="1.0"?>
<%= returnMsg %>