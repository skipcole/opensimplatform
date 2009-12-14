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
<title>Announcements</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<meta http-equiv="refresh" content="20" />
</head>

<body>
<h2>Announcements
</h2>
<table width="95%" border="0" cellspacing="2" cellpadding="2">
  <tr valign="top"> 
    <td><p><%= RunningSimulation.getActorAlertText(pso.schema, pso.running_sim_id, pso.actor_id) %></p>
    </td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>
<%
	
%>
