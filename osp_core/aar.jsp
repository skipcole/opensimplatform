<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	RunningSimulation rs = pso.giveMeRunningSim();
	
%>
<html>
<head>
<title>After Action Report Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>

<body>
<h1>After Action Report</h1>
<p><%= rs.getAar_text() %></p>
<p>&nbsp; </p>
</body>
</html>
<%
	
%>