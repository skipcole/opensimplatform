<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
	
	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	Actor this_actor = new Actor();
	boolean showControl = false;
	boolean showUnAssigned = false;
	
	String showControlText = "not";
	String showUnAssignedText = "not";
	
	if (!(pso.preview_mode)) {
	
		this_actor = pso.giveMeActor();
    
		String stored_value = (String) cs.getContents().get(CastCustomizer.KEY_FOR_DISPLAY_CONTROL);
	
		if ((stored_value != null) && (stored_value.equalsIgnoreCase("true"))){
			showControl = true;
			showControlText = "";
		}
		
		String stored_value_unassinged = (String) cs.getContents().get(CastCustomizer.KEY_FOR_DISPLAY_UNASSIGNED);
		if ((stored_value_unassinged != null) && (stored_value_unassinged.equalsIgnoreCase("true"))){
			showUnAssigned = true;
			showUnAssignedText = "";
		}
		
		//
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
<%
	if (this_actor.isControl_actor()) {

%><p><strong>Note to Control:</strong> This page has been set up that it will <%= showControlText %> show the control character and will <%= showUnAssignedText %> show all unassigned characters. </p> 
<% } %>
<p><%= cs.getBigString() %></p>
<blockquote>
  <h2>You</h2>
  <table width="80%" border="1">
<tr>
<td width=50><strong>Picture/Name</strong></td>
<td><strong>Description</strong></td>
</tr>

  <tr>
    <td valign="top" width="200"><img src="images/actors/<%= this_actor.getImageFilename() %>" width="200"  >
    <br><%= this_actor.getActorName(pso.schema, pso.getRunningSimId(), request) %></td>
    <td valign="top">
    <% if (this_actor.getRole(pso.schema, pso.sim_id) != null) { %>
    <p><strong>Your Role</strong><br />
    	<%= this_actor.getRole(pso.schema, pso.sim_id) %></p>
    <% } %>
    <p><strong>Public Description</strong><br />
        <%= this_actor.getPublic_description() %></p>
    <p><br />
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
			Actor act = (Actor) li.next();
			
			boolean showRole = false;
			boolean showSemiPublic = false;
			boolean showPrivate = false;
			
			if (this_actor.isControl_actor()) {
				showRole = true;
				showSemiPublic = true;
				showPrivate = true;
			}
			
			if (!(act.getId().equals(pso.getActorId()))) {
			
			String userAssigned = USIP_OSP_Cache.getNumberofUsersAssigned(pso.schema, pso.getRunningSimId(), act.getId(), request);
			
			boolean thisActorAssignedToUser = true;
			if (userAssigned.equalsIgnoreCase("0")) {
				thisActorAssignedToUser = false;
			}
				
			//}
			
			if ((showControl) || (!(act.isControl_actor()))) {
			
			if ((showUnAssigned) || (thisActorAssignedToUser)) {


		%>
  <tr>
    <td valign="top" width="200"><img src="images/actors/<%= act.getImageFilename() %>" width="200"  ><br><%= act.getActorName(pso.schema, pso.getRunningSimId(), request) %></td>
    <td valign="top"><% if (showRole) { %>
        <p><strong>Their Role</strong><br />
    	<%= act.getRole(pso.schema, pso.sim_id) %></p>   <% } %>
        
    	<p><strong>Public Description</strong>
    	<%= act.getPublic_description() %></p>
    
    <% if (showSemiPublic) { %>
    	<p><strong>Semi-public Description</strong><br>
		<%= act.getSemi_public_description() %></p>
	<% } %>
    
    <% if (showPrivate) { %>
		<p><strong>Private Description</strong> <br>
		<%= act.getPrivate_description() %>  
    <% } // end of if showing unassigned.
			} // end of if showing control.
	%>
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