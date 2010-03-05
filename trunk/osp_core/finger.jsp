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
<title>Finger</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>

<h1>User's Last Login Times</h1>
<p>Click on the user's name to see a past history of their logins.</p>
<p>&nbsp;</p>

<blockquote>

<table width="80%" border="1">
<tr>
      
      <td width="25%"><strong>Actor Name</strong></td>
      <td width="25%"><strong>User </strong></td>
<td><strong>Logged in</strong></td>
</tr>
  <%
  
  		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy HH:mm a");
		
  		for (ListIterator li = simulation.getActors(pso.schema).listIterator(); li.hasNext();) {
			Actor act = (Actor) li.next();
			
			User user = UserAssignment.getUserAssigned(pso.schema, pso.running_sim_id, pso.actor_id);
			String uName = "";
			String uTime = "";
			Long uId = null;
			if (user != null) {
				uName = user.getBu_full_name();
				uTime = sdf.format(user.getLastLogin());
				uId = user.getId();
			}
		%>
  <tr>
    
    <td valign="top"><%= act.getActorName(pso.schema, pso.running_sim_id, request) %></td>
    <td valign="top"><a href="finger_user_details.jsp?user_id=<%= uId %>"><%= uName %></a></td>
    <td valign="top"><%= uTime %></td>
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