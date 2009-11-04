<%@ page 
	contentType="text/html; charset=UTF-8" 
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
<title>OSP Email</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<meta http-equiv="refresh" content="20" />
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<h2>Email</h2>
<a href="write_email.jsp">Compose</a>
<p>Inbox for <%= pso.actor_name %></p>
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
<p>Sent Messages From <%= pso.actor_name %></p>
<p>Draft Messages</p>
</body>
</html>
<%
	
%>
