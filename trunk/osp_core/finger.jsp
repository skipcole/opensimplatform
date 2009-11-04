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
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
%>
<html>
<head>
<title>Create Actor</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>

<h1>Users Logged in to this Simulation Run</h1>
<p>&nbsp;</p>

<blockquote>

<table width="80%" border="1">
<tr>
      <td width="25%"><strong>User </strong></td>
<td width="25%"><strong>Actor Name</strong></td>
<td><strong>Logged in</strong></td>
</tr>
  <%
  		for (ListIterator li = simulation.getActors().listIterator(); li.hasNext();) {
			Actor aa = (Actor) li.next();
			
			User user = UserAssignment.getUserAssigned(pso.schema, pso.running_sim_id, pso.actor_id);
		%>
  <tr>
    <td valign="top"><%= user.getBu_full_name() %></td>
    <td valign="top"><%= aa.getName() %></td><td valign="top">&nbsp;</td>
  </tr>

  <%
	} // end of loop over actors.
%>
</table>
</blockquote>
<p>&nbsp;</p>
<p></p>
</body>
</html>
<%
	
%>