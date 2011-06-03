<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	afso.backPage = "../simulation_authoring_play/assign_user_to_simulation.jsp";
	
	UserAssignment ua_temp = afso.handleAssignUser(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
		String paramString = ua_temp.getAsParameterString();
		
		response.sendRedirect("assign_user_to_sim_user_not_found.jsp" + paramString);
		
		return;
	}
	
	////////////////////////////////////////////////////
	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	/////////////////////////////////////////////////////
	RunningSimulation running_simulation = new RunningSimulation();
	if (afso.getRunningSimId() != null){
		running_simulation = afso.giveMeRunningSim();
	}
	//////////////////////////////////////////////////////
	
	String user_assignment_id = request.getParameter("user_assignment_id");
	String actor_to_add_to_simulation = request.getParameter("actor_to_add_to_simulation");
	
	System.out.println(user_assignment_id);
	System.out.println(actor_to_add_to_simulation);
	
	UserAssignment ua = new UserAssignment();
	Actor act = new Actor();
	
	if ((user_assignment_id != null) && (!(user_assignment_id.equalsIgnoreCase("null")))) {
		ua = UserAssignment.getById(afso.schema, new Long(user_assignment_id));
	}
	
	if (actor_to_add_to_simulation != null) {
		act = Actor.getById(afso.schema, new Long(actor_to_add_to_simulation));
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>


<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.4.1.js"></script>
<script type="text/javascript" src="../third_party_libraries/jquery/jquery.autocomplete.js"></script>
<link rel="stylesheet" href="../third_party_libraries/jquery/jquery.autocomplete.css" type="text/css" />
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
              <h1>Add Editors  <a href="../simulation_facilitation/helptext/assign_players_help.jsp" target="helpinright"></a></h1>
              <br />
      <% 
			if (afso.sim_id == null) {
		%>
      <p>You must first select the simulation for which you will be adding users.      </p>
      <% } else { %>
      <p>Assigning user to <strong>simulation <%= simulation.getDisplayName() %></strong>. <br />
        <%
			if (afso.getRunningSimId() == null) {
		%>
      <p>&nbsp;</p>
      <% } else { %>
      <p>Assign user  in the <strong>running simulation 
        <%= running_simulation.getRunningSimulationName() %></strong><br/>
      </p>
      <p>Select a player from the roster below to assign them to <strong></strong>the role of <%= act.getActorName() %>.</p>
      <p>&nbsp;</p>
	  <table>
	  <% 
	  	ArrayList usersList = new ArrayList();
		
		 for (ListIterator li = User.getAll(afso.schema, true).listIterator(); li.hasNext();) {
			
			User user = (User) li.next();
			
			usersList.add(user);
			
		}
		
		Collections.sort(usersList);
		
		for (ListIterator li = usersList.listIterator(); li.hasNext();) {
			
			User user = (User) li.next();
		%>
<tr>
<td><%= user.getBu_full_name() %></td>
<td>
		<form action="../simulation_authoring_play/assign_user_to_simulation.jsp" method="post" name="form3" id="form3">
		  <input type="hidden" name="sending_page" value="assign_user_to_simulation" />
          <input type="hidden" name="user_id" value="<%= user.getId() %>" />
          <input type="hidden" name="user_assignment_id" value="<%= ua.getId() %>" />
          <input type="hidden" name="actor_to_add_to_simulation" value="<%= act.getId() %>" />
          <input type="hidden" name="simulation_adding_to" value="<%= simulation.getId() %>" />
          <input type="hidden" name="running_simulation_adding_to" value="<%= running_simulation.getId() %>" />
          <input type="submit" name="command" value="Assign User" />
		</form>
</td></tr>
		<% } %>
        </table>
      <% } // end of if afso.running_sim.id has been set. 
		%>
      <%	
	}// end of if afso.simulation.id has been set.
%>
      </blockquote>
      <p>&nbsp;</p>
      <p align="left"><a href="../simulation_authoring_play/assign_user_to_simulation.jsp">&lt;-- 
        Back</a></p>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>


</body>
</html>