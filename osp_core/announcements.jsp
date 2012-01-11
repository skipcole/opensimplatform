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
	
	String showpc = request.getParameter("showpc");
	
	if (showpc != null) {
		if (showpc.equalsIgnoreCase("true")) {
			pso.setShowPhaseChanges(true);
		} else {
			pso.setShowPhaseChanges(false);
		}
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
    <td><p><%= RunningSimulation.getActorAlertText(pso.schema, pso.getRunningSimId(), pso.getActorId(), pso) %></p>
    </td>
  </tr>
</table>
<% if (pso.isShowPhaseChanges()) { %>
<p><a href="announcements.jsp?showpc=false">Hide Phase Changes</a></p>
<% } else { %>
<p><!-- a href="announcements.jsp?showpc=true">Show Phase Changes</a --></p>
<p>
  <% } %>
</p>
<!-- % if (true) { %>
<form name="form1" method="post" action="">
  <input type="submit" name="button" id="button" value="Submit">
</form>
<    % } %  -->
<p>&nbsp;</p>
</body>
</html>