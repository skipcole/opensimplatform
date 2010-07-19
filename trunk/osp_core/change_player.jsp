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
		
	Actor this_actor = new Actor();
	
	if (!(pso.preview_mode)) {
	
		this_actor = pso.giveMeActor();
    
	}

	
%>
<html>
<head>
<title>Create Actor</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>

<body>
<h1>Change Players </h1>

<blockquote>
<table border="1">
        <tr valign="top"> 
          <td ><strong>Actor</strong></td>
            <td ><strong>User Currently Assigned</strong></td>
            <td >&nbsp;</td>
            <td ><strong>Enter User's Email</strong></td>
            <td ><strong>Assign User</strong></td>
          </tr>
        <%
		
		int ii = 5;
		
			for (ListIterator li = simulation.getActors(pso.schema).listIterator(); li.hasNext();) {
				Actor act = (Actor) li.next();
				User user_assigned = UserAssignment.getUserAssigned(pso.schema, pso.running_simulation, act.getId());
				
				UserAssignment ua = new UserAssignment();
				
				if (user_assigned == null) {
					user_assigned = new User();
					user_assigned.setBu_username("<font color=\"#FF0000\">Not Assigned</font>");
					
					ua = UserAssignment.getUserAssignment (pso.schema, pso.getRunningSimId(), act.getId());
				}

					%>
        <tr valign="top"> 
          <form action="assign_user_to_simulation.jsp" method="post" name="form3" id="form3">
            <td><%= act.getActorName() %></td>
              <td><%= user_assigned.getBu_username() %></td>
              <td>
                <% String nameToSend = " this user assignment "; %> 
                <% if ((ua != null) && (ua.getId() != null)){ %>
                <a href="delete_object.jsp?object_type=user_assignment&objid=<%= ua.getId() %>&object_info=<%= nameToSend %>">
                  <% } %>
                  <img src="../simulation_authoring/images/delete.png" width="26" height="22" border="0" /></a></td>
              <td>
              <input name="user_to_add_to_simulation" type="text" style="width: 200px;" value="" id="userNameAjax<%= act.getId() %>" class="userNameAjax<%= act.getId() %>" tabindex="<%= ii %>"/>
              </td>
              <td> <input type="hidden" name="sending_page" value="assign_user_to_simulation" /> 
                <input type="hidden" name="actor_to_add_to_simulation" value="<%= act.getId() %>" /> 
                <input type="hidden" name="simulation_adding_to" value="<%= simulation.getId() %>" /> 
                <input type="hidden" name="running_simulation_adding_to" value="<%= running_simulation.getId() %>" /> 
                <input type="submit" name="command" value="Assign User" tabindex="<%= ii + 1 %>" /></td>
            </form>
          </tr>
        <%
				ii += 2;
		  	}
			// End of loop over results set of Actors
		%>
      </table>
</blockquote>
<p>&nbsp;</p>
<p></p>
</body>
</html>