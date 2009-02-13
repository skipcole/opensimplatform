<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
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
	
	pso.backPage = "../simulation_facilitation/assign_user_to_simulation.jsp";
	
	pso.handleAssignUser(request);
	
	////////////////////////////////////////////////////
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	/////////////////////////////////////////////////////
	RunningSimulation running_simulation = new RunningSimulation();
	if (pso.running_sim_id != null){
		running_simulation = pso.giveMeRunningSim();
	}
	//////////////////////////////////////////////////////
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Control Page</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Assign User to a Running Simulation</h1>
      <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
      <% 
			if (pso.sim_id == null) {
		%>
      <p>You must first select the simulation for which you will be adding users.<br />
        Please <a href="../simulation_authoring/select_simulation.jsp">click here</a> to select it, or 
        <a href="../simulation_authoring/create_simulation.jsp">create a new one</a>.</p>
      <% } else { %>
      <p>Assigning users to <strong>simulation <%= simulation.getDisplayName() %></strong>. <br />
        To select a different simulation, <a href="../simulation_authoring/select_simulation.jsp">click 
        here</a>. 
        <%
			if (!(pso.runningSimSelected)) {
		%>
      <p>You must select the running simulation for which you will be assigning 
        users.<br />
        Please <a href="../simulation_authoring/select_running_simulation.jsp">click here</a> to select 
        it, or <a href="create_running_sim.jsp">create a new one</a>.</p>
      <% } else { %>
      <p>Assign users to play the role of actors in the <strong>running simulation 
        <%= running_simulation.getName() %></strong><br/>
		To select a different running simulation, <a href="../simulation_authoring/select_running_simulation.jsp">click 
        here</a>. 
		</p>
      <table border="1">
        <tr valign="top"> 
          <td ><strong>Actor</strong></td>
          <td ><strong>User Assigned</strong></td>
          <td >&nbsp;</td>
          <td ><strong>Available Users</strong></td>
          <td ><strong>Assign User</strong></td>
        </tr>
        <%
			for (ListIterator li = simulation.getActors(pso.schema).listIterator(); li.hasNext();) {
				Actor act = (Actor) li.next();
				User user_assigned = UserAssignment.getUserAssigned(pso.schema, running_simulation.getId(), act.getId());
				
				UserAssignment ua = new UserAssignment();
				
				if (user_assigned == null) {
					user_assigned = new User();
					user_assigned.setBu_username("<font color=\"#FF0000\">Not Assigned</font>");
					
					ua = UserAssignment.getUserAssignment (pso.schema, pso.running_sim_id, act.getId());
				}

					%>
        <tr valign="top"> 
          <form action="assign_user_to_simulation.jsp" method="post" name="form3" id="form3">
            <td><%= act.getName() %></td>
            <td><%= user_assigned.getBu_username() %></td>
            <td>
			<% String nameToSend = " this user assignment "; %> 
			<% if ((ua != null) && (ua.getId() != null)){ %>
			<a href="delete_object.jsp?object_type=user_assignment&objid=<%= ua.getId() %>&object_info=<%= nameToSend %>">
			<% } %>
			<img src="../simulation_authoring/images/delete.png" width="26" height="22" border="0" /></a></td>
            <td> <select name="user_to_add_to_simulation">
                <% // loop over something potential users for this actor roll.
					
					for (ListIterator lusers = User.getAll(pso.schema, true).listIterator(); lusers.hasNext();) {
					User user = (User) lusers.next();

				  %>
                <option value="<%= user.getId() %>" selected="selected"><%= user.getBu_username() %></option>
                <%
				  	} // End of loop over potentail users.
				  %>
              </select> </td>
            <td> <input type="hidden" name="sending_page" value="assign_user_to_simulation" /> 
              <input type="hidden" name="actor_to_add_to_simulation" value="<%= act.getId() %>" /> 
              <input type="hidden" name="simulation_adding_to" value="<%= simulation.getId() %>" /> 
              <input type="hidden" name="running_simulation_adding_to" value="<%= running_simulation.getId() %>" /> 
              <input type="submit" name="command" value="Assign User" /></td>
          </form>
        </tr>
        <%
		  	}
			// End of loop over results set of Actors
		%>
      </table>
      <% } // end of if pso.running_sim.id has been set. 
		%>
      <%	
	}// end of if pso.simulation.id has been set.
%></blockquote>
      <p>&nbsp;</p>
      <p align="center"><a href="enable_simulation.jsp">Next Step: Enable Simulation</a></p>
      <p align="left"><a href="create_user.jsp">&lt;- 
        Back</a></p>
      <!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
<%
	
%>
