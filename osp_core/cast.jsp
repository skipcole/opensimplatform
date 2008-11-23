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
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
	Actor this_actor = pso.giveMeActor();
	
%>
<html>
<head>
<title>Create Actor</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>

<h1>Cast</h1>

<p>Below are listed alphabetically all of the current Actors that you can see.</p>
<p>&nbsp;</p>
<blockquote>

<table width="80%" border="1">
<tr>
<td width=50><strong>Picture/Name</strong></td>
<td><strong>Description</strong></td>
</tr>
  <%
  		for (ListIterator li = simulation.getActors().listIterator(); li.hasNext();) {
			Actor aa = (Actor) li.next();
			
		%>
  <tr>
    <td valign="top" width="200"><img src="images/actors/<%= aa.getImageFilename() %>" width="200"  ><br><%= aa.getName() %></td>
    <td valign="top"><p><%= aa.getPublic_description() %>
    <% if (this_actor.isControl_actor()) { %>
    <BR><strong>Semi-public Description</strong><br>
<%= aa.getSemi_public_description() %>
<BR><strong>Private Description</strong> <br>
<%= aa.getPrivate_description() %>  
    <% } %>
    </td>
  </tr>

  <%
	}
%>
</table>
</blockquote>
<p>&nbsp;</p>
<p></p>
</body>
</html>
<%
	
%>