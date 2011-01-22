<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

<%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
		
	String textToShow = "";
	
	if (!(pso.preview_mode)) {
		RunningSimulation rs = pso.giveMeRunningSim();
		textToShow = rs.getAar_text();
	} else {
		Simulation sim = pso.giveMeSim();
		textToShow = sim.getAarStarterText();
	}
	
%>
<html>
<head>
<title>After Action Report Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>

<body>
<h1>After Action Report</h1>
<p><%= textToShow %></p>
<p>&nbsp; </p>
</body>
</html>