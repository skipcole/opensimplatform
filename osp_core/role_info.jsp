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
		
	Actor actor = pso.giveMeActor();
	
	

%>
<html>
<head>
<title>Role Information Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<h1>Your Role</h1>
<p align="center"><img src="images/actors/<%= actor.getImageFilename() %>"  ></p>
<table width="95%" border="1" cellspacing="0" cellpadding="2">
  <tr valign="top"> 
    <td><p><strong>Public Information</strong><br>
        <%= actor.getPublic_description() %></p></td>
  </tr>
  <tr valign="top"> 
    <td><p><strong>Information Your Close Friends and Associates Know</strong><br>
        <%= actor.getSemi_public_description() %></p></td>
  </tr>
  <tr valign="top">
    <td><p><strong>Private Information</strong><br>
        <%= actor.getPrivate_description() %></p></td>
  </tr>
</table>
<p>&nbsp;</p>
<p>&nbsp; </p>
</body>
</html>
<%
	
%>
