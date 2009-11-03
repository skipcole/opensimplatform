<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
		
%>
<html>
<head>
<title>Announcements</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<meta http-equiv="refresh" content="20" />
</head>

<body>
<h2>Email</h2>
<a href="write_email.jsp">Compose</a>
<p>&nbsp;</p>
<p>Your Inbox</p>
<table width="80%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="4%">&nbsp;</td>
    <td width="46%">Subject</td>
    <td width="25%">From</td>
    <td width="25%">Date</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>
<%
	
%>
