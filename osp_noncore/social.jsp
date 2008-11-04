<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);

	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
%>
<html>
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<p align="center"><img src="../ver1/oscw_noncore/images/z_mmap.gif" width="820" height="898"></p>

<p>&nbsp;</p>
<form name="form1" method="post" action="">
  <p>
    <input name="radiobutton" type="radio" value="radiobutton" checked>
  Ensure population is fed<BR>
    <input name="radiobutton" type="radio" value="radiobutton">
  Ensure population has water<br>
  <input name="radiobutton" type="radio" value="radiobutton">
Ensure population has shelter <br>
<input name="radiobutton" type="radio" value="radiobutton">
Meet basic sanitation needs 
<br>
<input name="radiobutton" type="radio" value="radiobutton"> 
Build effective education system
<br>
<input name="radiobutton" type="radio" value="radiobutton">
Enable displaced persons and refugees to return or relocate
<br>
<input name="radiobutton" type="radio" value="radiobutton">
Address legacy of past abuses (e.g., truth commissions)<br>
<input name="radiobutton" type="radio" value="radiobutton">
Promote peaceful coexistence (e.g., inter-ethnic, interfaith)
<br>
<BR>
  </p>
</form>
</body>
</html>
