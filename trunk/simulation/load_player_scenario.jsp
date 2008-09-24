<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	pso.handleLoadPlayerScenario(request);
	
	
	// Send them to the first section
    response.sendRedirect("simwebui.jsp?tabposition=1");
	
	if (true){
    	return;
	}
%>
<html>
<head>
<title>Load Scenario Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>

<p>&nbsp;</p>

<p>This page should not be seen.</p>
</body>
</html>
<%
	
%>