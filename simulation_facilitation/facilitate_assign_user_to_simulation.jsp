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
	
	afso.backPage = "../simulation_facilitation/facilitate_assign_user_to_simulation.jsp";
	
	UserAssignment ua_temp = afso.handleAssignUser(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
		String paramString = ua_temp.getAsParameterString();
		
		response.sendRedirect("assign_user_to_sim_user_not_found.jsp" + paramString);
		
		return;
	}
	

	/////////////////////////////////////////////////////
	RunningSimulation running_simulation = new RunningSimulation();
	if (afso.getRunningSimId() != null){
		running_simulation = afso.giveMeRunningSim();
		afso.sim_id = running_simulation.getSim_id();
		// TODO - Open questions do we change the 'last sim edit field' ?
	}
	////////////////////////////////////////////////////
	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	//////////////////////////////////////////////////////
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>


<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script type="text/javascript" src="../third_party_libraries/jquery/jquery.autocomplete.js"></script>
<link rel="stylesheet" href="../third_party_libraries/jquery/jquery.autocomplete.css" type="text/css" />
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
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
              <h1>Assign User to a Running Simulation <a href="helptext/assign_players_help.jsp" target="helpinright">(?)</a></h1>
              <br />
      <% 
			if (afso.sim_id == null) {
		%>
      <% } else { %>
      <p>Assigning users to <strong>Simulation <%= simulation.getDisplayName() %></strong>. <br />
 
        <%
			if (afso.getRunningSimId() == null) {
		%>
      
      <% } else { %>
      <p>Assign users to play the role of actors in the <strong>Running Simulation 
        <%= running_simulation.getRunningSimulationName() %></strong></p>
      <p>To assign players to a simulation, <strong>you must follow these steps</strong></p>
      <ol>
        <li>Type the user's email address in the User's Email field</li>
        <li>Click on the button 'Assign User' (If the user is not found on this platform, you will then be given several options)</li>
        <li>Repeat until all actors have been assigned users to play them</li>
      </ol>
      <table border="1" width="100%">
        <tr valign="top"> 
          <td ><strong>Actor</strong></td>
            <td ><strong>User Assigned</strong></td>
            <td ><strong>Details</strong></td>
            <td ><strong>Del/Add</strong></td>
            <td ><strong>Enter User's Email</strong></td>
            <td ><strong>Assign User</strong></td>
          </tr>
        <%
		
		int tabIndex = 5;
		
			// Loop over all actors in the simulation
			for (ListIterator li = simulation.getActors(afso.schema).listIterator(); li.hasNext();) {
				Actor act = (Actor) li.next();
				
				// For each actor, get all of their user assignments
				List theUsersAssigned = UserAssignment.getUsersAssigned(afso.schema, running_simulation.getId(), act.getId());
				
				// Even if no actors have been assigned, we still want to display that actor.
				if ((theUsersAssigned == null) || (theUsersAssigned.size() == 0)){
					theUsersAssigned = new ArrayList();
					UserAssignment ua = new UserAssignment();
					ua.setUser_id(new Long(-1));
					theUsersAssigned.add(ua);
				}
				
				User user_assigned = new User();
				
				// Loop over all of the user assignments
				for (ListIterator liua = theUsersAssigned.listIterator(); liua.hasNext();) {
					UserAssignment ua = (UserAssignment) liua.next();
					
					String bgColor = "FFFFFF";
					
					if ((ua.getUser_id() != null) && (ua.getUser_id().intValue() != -1) ){
						user_assigned = User.getById(afso.schema, ua.getUser_id());
					} else if ((ua.getUser_id() != null) && (ua.getUser_id().intValue() == -1) ){
						bgColor = "FFFFFF";
						user_assigned = new User();
						user_assigned.setBu_full_name(ua.getUsername());
					} else {
						bgColor = "FFCCCC";
						user_assigned = new User();
						user_assigned.setBu_full_name("<font color=\"#FF0000\">Not Assigned</font>");
					}

					%>
        <tr valign="top" bgcolor="#<%= bgColor %>"> 
          <form action="facilitate_assign_user_to_simulation.jsp" method="post" name="form3" id="form3">
            <td><%= act.getActorName() %></td>
              <td><%= user_assigned.getBu_full_name() %></td>
              <td><a href="view_sim_actor_assignment_notes.jsp?actor_id=<%= act.getId() %>">details</a></td>
              <td>
                <% if ((ua != null) && (ua.getId() != null)){ %>
                	<a href="facilitate_assign_user_to_simulation.jsp?command=remove_ua&amp;user_assignment_id=<%= ua.getId() %>"><img src="../simulation_authoring/images/delete.png" width="26" height="22" border="0" /></a>
					<a href="facilitate_assign_user_to_simulation.jsp?command=add_assignment&amp;simulation_adding_to=<%= simulation.getId() %>&amp;running_simulation_adding_to=<%= running_simulation.getId() %>&amp;actor_to_add_to_simulation=<%= act.getId() %>"><img src="../simulation_authoring/images/add.png" width="26" height="22" border="0" /></a>
                  <% } %>                  </td>
              <td>
              <input name="user_to_add_to_simulation" type="text" style="width: 200px;" value="" id="userNameAjax<%= act.getId() %>" class="userNameAjax<%= act.getId() %>" tabindex="<%= tabIndex %>"/>              </td>
              <td> <input type="hidden" name="sending_page" value="assign_user_to_simulation" /> 
			    <input type="hidden" name="user_assignment_id" value="<%= ua.getId() %>" />
                <input type="hidden" name="actor_to_add_to_simulation" value="<%= act.getId() %>" /> 
                <input type="hidden" name="simulation_adding_to" value="<%= simulation.getId() %>" /> 
                <input type="hidden" name="running_simulation_adding_to" value="<%= running_simulation.getId() %>" /> 
                <input type="submit" name="command" value="Assign User" tabindex="<%= tabIndex + 1 %>" /></td>
            </form>
          </tr>
        <%
				} // End of loop over user assignments
				
				tabIndex += 2;
		  	}
			// End of loop over results set of Actors
		%>
      </table>
      <% } // end of if afso.running_sim.id has been set. 
		%>
      <%	
	}// end of if afso.simulation.id has been set.
%>
      </blockquote>
      <h6>Note: Rows in pink represent unregistered players that have been assigned a role in the simulation.</h6>
      <p align="center">When you are done assigning users, <a href="facilitate_enable_simulation.jsp">click here to move to the next step.</a></p>     
	   <p align="left">&nbsp;</p>
	   <blockquote>
	     <p align="left"><a href="facilitate_panel.jsp">To Simulation Launch Checklist</a></p>
	     </blockquote>
	   <p align="left">&nbsp;</p></td>
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

<p align="center">&nbsp;</p>
<script type="text/javascript">
function findValue(li) {
	if( li == null ) return alert("No match!");

	// if coming from an AJAX call, let's use the CityId as the value
	if( !!li.extra ) var sValue = li.extra[0];

	// otherwise, display the value in the text box
	else var sValue = li.selectValue;

}

function selectItem(li) {
	findValue(li);
}

function formatItem(row) {
	return row[1] + ", " + row[0];
}

<%
	for (ListIterator li = simulation.getActors(afso.schema).listIterator(); li.hasNext();) {
		Actor act = (Actor) li.next();
		
		/*
function lookupAjax(){
	var oSuggest = $("#userNameAjax< % = act.getId() % > ")[0].autocompleter;
	oSuggest.findValue();
	return false;
}

*/
%>



$("#userNameAjax<%= act.getId() %>").autocomplete(
	"autocomplete.jsp",
	{
delay:3,
minChars:3,
matchSubset:3,
matchContains:3,
cacheLength:10,
onItemSelect:selectItem,
onFindValue:findValue,
formatItem:formatItem,
autoFill:true
	}
);

<% } // End of loop over actor ids %>
</script>
</body>
</html>