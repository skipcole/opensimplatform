<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
		
	RunningSimulation rs = pso.giveMeRunningSim();
	
%>
<html>
<head>
<title>News Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<h2>Political Decisions</h2>
<p>&nbsp;</p>
<form name="form1" method="post" action="">
  <p>
    <input type="radio" name="radiobutton" value="radiobutton">
    All elections as soon as possible 
  </p>
  <p> 
    <input type="radio" name="radiobutton" value="radiobutton">
    Local elections followed by national elections 
  </p>
  <p>
    <input type="radio" name="radiobutton" value="radiobutton">
    National elections followed by local elections  </p>
  <p>
    <input type="radio" name="radiobutton" value="radiobutton"> 
    Postpone all elections
</p>
</form>
<p>&nbsp;</p>
</body>
</html>
<%
	
%>
