<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*" 
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
	
	Actor this_actor = pso.giveMeActor();
	
	String cs_id = (String) request.getParameter("cs_id");
	
	CustomizeableSection cs = CustomizeableSection.getMe(pso.schema, cs_id);
    
	String stored_value = (String) cs.getContents().get(CastCustomizer.KEY_FOR_DISPLAY_CONTROL);
	
	boolean showControl = false;
	if ((stored_value != null) && (stored_value.equalsIgnoreCase("true"))){
		showControl = true;
	}
	
%>
<html>
<head>
<title>Create Actor</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>

<body>
<h1>Cast</h1>

<p><%= cs.getBigString() %></p>
<blockquote>
  <h2>You</h2>
  <table width="80%" border="1">
<tr>
<td width=50><strong>Picture/Name</strong></td>
<td><strong>Description</strong></td>
</tr>

  <tr>
    <td valign="top" width="200"><img src="images/actors/<%= this_actor.getImageFilename() %>" width="200"  ><br><%= this_actor.getName() %></td>
    <td valign="top">
    <p><strong>Your Role</strong><br />
    	<%= this_actor.getRole(pso.schema, pso.sim_id) %></p>
    <p><strong>Public Description</strong><br />
        <%= this_actor.getPublic_description() %></p>
    <p><strong>Semi-public Description</strong><br />
	<%= this_actor.getSemi_public_description() %></p>

	<p><strong>Private Description</strong> <br>
	<%= this_actor.getPrivate_description() %>  </p>    </td>
  </tr>
  </table>
  
<p>&nbsp;</p>
<h2>Others</h2>
<table width="90%" border="1">
<tr>
<td width=50><strong>Picture/Name</strong></td>
<td><strong>Description</strong></td>
</tr>
  <%
  		for (ListIterator li = simulation.getActors(pso.schema).listIterator(); li.hasNext();) {
			Actor aa = (Actor) li.next();
			
			boolean showRole = false;
			boolean showSemiPublic = false;
			boolean showPrivate = false;
			
			if (this_actor.isControl_actor()) {
				showRole = true;
				showSemiPublic = true;
				showPrivate = true;
			}
			
			if (!(aa.getId().equals(pso.actor_id))) {
			
			if ((showControl) || (!(aa.isControl_actor()))) {
		%>
  <tr>
    <td valign="top" width="200"><img src="images/actors/<%= aa.getImageFilename() %>" width="200"  ><br><%= aa.getName() %></td>
    <td valign="top"><% if (showRole) { %>
        <p><strong>Their Role</strong><br />
    	<%= aa.getRole(pso.schema, pso.sim_id) %></p>   <% } %>
        
    	<p><strong>Public Description</strong>
    	<%= aa.getPublic_description() %></p>
    
    <% if (showSemiPublic) { %>
    	<p><strong>Semi-public Description</strong><br>
		<%= aa.getSemi_public_description() %></p>
	<% } %>
    
    <% if (showPrivate) { %>
		<p><strong>Private Description</strong> <br>
		<%= aa.getPrivate_description() %>  
    <% } %>
    </td>
  </tr>

  <%
		} // End of don't show control if it is not desired.
	} // End of don't show the same actor as this player is playing.
	
	} // End of loop over actors
%>
</table>
</blockquote>
<p>&nbsp;</p>
<p></p>
</body>
</html>
<%
	
%>