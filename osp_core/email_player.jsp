<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
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
	
	SchemaInformationObject sio = SchemaInformationObject.lookUpSIOByName(pso.schema);
	
	pso.handleEmailPlayers(request, sio);
	
		
%>
<html>
<head>
<title>Email Players</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {font-weight: bold}
-->
</style>
</head>

<body>
<h1>Email Players </h1>
<p align="center"><span class="style1"><%= pso.errorMsg %></span>
<% pso.errorMsg = ""; %></p>

<% if (sio.isEmailEnabled()) { %>
<blockquote>
<form action="email_player.jsp" method="post" name="form3" id="form3">
<input type="hidden" name="sending_page" value="email_players" />
<table border="1" width="100%">
        <tr valign="top"> 
          <td ><strong>Actor</strong></td>
            <td ><strong>Username / Email </strong></td>
            <td ><strong>Include</strong></td>
    </tr>
        <%
		
		int ii = 5;
		
			for (ListIterator li = simulation.getActors(pso.schema).listIterator(); li.hasNext();) {
				Actor act = (Actor) li.next();
				
				// For each actor, get all of their user assignments
				List theUsersAssigned = UserAssignment.getUsersAssigned(pso.schema, pso.getRunningSimId(), act.getId());
				
				// Loop over all of the user assignments
				for (ListIterator liua = theUsersAssigned.listIterator(); liua.hasNext();) {
					UserAssignment ua = (UserAssignment) liua.next();
					
					User user_assigned = User.getById(pso.schema, ua.getUser_id());

					%>
        <tr valign="top"> 
          
            <td><%= act.getActorName() %></td>
              <td><%= user_assigned.getBu_username() %></td>
              <td><label>
                <input name="email_player_<%= user_assigned.getBu_username() %>" type="checkbox" value="true" checked>
              </label></td>
    </tr>
        <%
				ii += 2;
				} //End of loop over user assignments
		  	}
			// End of loop over results set of Actors
		%>
  </table>
  <br />
<table width="100%" border="0">
  <tr>
    <td align="left" valign="top">Email Subject Line</td>
    <td align="left" valign="top"><input type="text" name="email_subject"></td>
  </tr>
  <tr>
    <td align="left" valign="top">Email Text</td>
    <td align="left" valign="top"><textarea name="email_text"></textarea></td>
  </tr>
  <tr>
    <td align="left" valign="top">Email From: </td>
    <td align="left" valign="top"><label>
      <input name="email_from" type="radio" value="noreply@opensimplatform.org" checked>
      noreply@opensimplatform.org<br>
    </label>
    <label>
    <input name="email_from" type="radio" value="<%= pso.user_name %>"><%= pso.user_name %>    </label></td>
  </tr>
  <tr>
    <td align="left" valign="top">&nbsp;</td>
    <td align="left" valign="top"><input type="submit" name="Submit" value="Submit"></td>
  </tr>
</table>
</form>
<h2>&nbsp;</h2>
<p>
  <% }  else { // end of if email has been enabled  %>
  Email has not been enabled on this platform. Please contact your administrator.
  <% } %>
</p>
</blockquote>
</body>
</html>