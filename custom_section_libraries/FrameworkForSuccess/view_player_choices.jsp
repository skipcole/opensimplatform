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
		
	RunningSimulation rs = pso.giveMeRunningSim();
	
	Hashtable grabBag = rs.getGrabBag();
	
	String selectedCop = (String) grabBag.get("rol_choice");
%>
<html>
<head>
<title>View Player Choices</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<h2>Player Choices</h2>
<p>Security</p>
<blockquote>
  <p>Security Decision:</p>
</blockquote>
<p>Political</p>
<blockquote>
  <p>Political Decision:</p>
</blockquote>
<p>Rule of Law</p>
<blockquote>
  <p>Top Cop: <%= selectedCop %> </p>
</blockquote>
<p>Economic</p>
<blockquote>
  <p>Tax Rate:</p>
</blockquote>
<p>Social Well Being</p>
<blockquote>
  <p>&nbsp;</p>
</blockquote>
<p>&nbsp;</p>
<p>&nbsp;</p>
</body>
</html>
<%
	
%>
