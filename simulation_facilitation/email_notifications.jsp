<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.communications.*,org.usip.osp.persistence.*,
	org.hibernate.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	afso.backPage = "email_notifications.jsp";
	
	SchemaInformationObject sio = SchemaInformationObject.lookUpSIOByName(afso.schema);
	
	Email email = afso.handleNotifyPlayers(request);

	////////////////////////////////////////////////////////
	Simulation simulation = new Simulation();	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
		/////////////////////////////////////////////////////
	RunningSimulation running_sim = new RunningSimulation();
	if (afso.getRunningSimId() != null){
		running_sim = (RunningSimulation) afso.giveMeRunningSim();
	}
	
	//////////////////////////////////////////////////////
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {
	color: #FF0000;
	font-weight: bold;
}
-->
</style>
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
              <h1>Notify Players By Email <a href="helptext/enable_sim_help.jsp" target="helpinright"></a></h1>
			  
<%
	if(!(sio.isEmailEnabled())){
%>
Email has not been enabled on this server. Please contact your administrator if you desire these features.
<%
	} else {
%>
              <blockquote> 
        <% 
			if (afso.sim_id == null) {
		%>
        <p>You must first select a simulation.<br />
          
          Please <a href="../simulation_authoring/select_simulation.jsp">click here</a> to select it, or <a href="../simulation_authoring/create_simulation.jsp">create a new one</a>.</p>
		  
		<% } else { %>
        <p>Emailing players in <strong>simulation: <%= simulation.getDisplayName() %></strong>. <br />
          To select a different simulation, <a href="../simulation_authoring/select_simulation.jsp">click here</a>.</p>
          <%
			if (afso.getRunningSimId() == null) {
		%>
        <p>You must select the running simulation.<br />
          
          Please <a href="select_running_simulation.jsp">click here</a> to select it, or <a href="create_running_sim.jsp">create a new one</a>.</p>
		  
		<% } else if (!(running_sim.isReady_to_begin())) { %>
        <p><strong>Running simulation <%= running_sim.getRunningSimulationName() %> </strong> <span class="style1">has  not been enabled. You must first enable a simulation before sending invitation emails.</span><br />

		  <% } else { %>
        <p>Emailing players in  <strong>running simulation <%= running_sim.getRunningSimulationName() %></strong><br />
          To select a different running simulation to enable, <a href="select_running_simulation.jsp">click here</a>.</p>
  
  <p>&nbsp;</p>
    <form action="email_notifications.jsp" method="post" name="form1" id="form1">
      <h2>
        <input type="hidden" name="sending_page" value="notify_players" />
        Instructions</h2>
      <p>Select the players below you wish to notify. Tailor your email as desire, and then hit send. If you would like to send a copy of a previously sent email, select that email from the list at the bottom to put its text in the body of the email to be sent. </p>
      <h2>Step 1. Players to Notify</h2>
      <table width="100%" border="1">
        <tr>
          <td valign="top"><strong>Actor</strong></td>
          <td valign="top"><strong>User</strong></td>
          <td valign="top"><strong>Username</strong></td>
          <td valign="top"><strong>Previous<br />
            Invites</strong></td>
          <td valign="top"><strong>Send</strong></td>
        </tr>
		<% 
			// Loop over all actors in the simulation
			for (ListIterator li = simulation.getActors(afso.schema).listIterator(); li.hasNext();) {
				Actor act = (Actor) li.next();
				
				// For each actor, get all of their user assignments
				List theUsersAssigned = UserAssignment.getUsersAssigned(afso.schema, running_sim.getId(), act.getId());
				
				User user_assigned = new User();
				
				// Loop over all of the user assignments
				for (ListIterator liua = theUsersAssigned.listIterator(); liua.hasNext();) {
					UserAssignment ua = (UserAssignment) liua.next();
					
					if (ua.getUser_id() != null){
						user_assigned = User.getById(afso.schema, ua.getUser_id());
					} else {
						user_assigned = new User();
						user_assigned.setBu_username("<font color=\"#FF0000\">Not Assigned</font>");
					}

					%>
        <tr>
          <td valign="top"><%= act.getActorName() %></td>
          <td valign="top">John Doe</td>
          <td valign="top"><%= user_assigned.getBu_username() %></td>
          <td valign="top">&nbsp;</td>
          <td valign="top"><label>
            <input type="checkbox" name="invite_<%= ua.getId() %>" value="true" />
          </label></td>
        </tr>
		<%
				} // End of loop over user assignments
		  	} // End of loop over results set of Actors
		%>
      </table>
      <h2><br />
        Step 2. Tailor Email Contents  </h2>
      <table width="100%" border="1" cellspacing="0" cellpadding="2">
        <tr valign="top">
          <td>Email from: </td>
          <td><label>
            <input name="email_from" type="radio" value="noreply" checked="checked" />
          noreply@opensimplatform.org
          </label><br />
		  <label>
		  <input name="email_from" type="radio" value="username" /><%= afso.user_email %> </label></td>
        </tr>
        <tr valign="top">
          <td>Email Subject Line </td>
          <td><label>
            <input type="text" name="email_subject" value="<%= email.getSubjectLine() %>" />
          </label></td>
        </tr>
        <tr valign="top"> 
          <td width="34%">Email text:<br /> <br /> </td>
                <td width="66%"> Dear &lt;Players Name&gt;,<br /> <p>
                  <textarea name="email_text" cols="60" rows="5"><%= email.getMsgtext() %></textarea>
                </p>
                  <p><font color="#CC9900">Note:</font> You should not need to replace 
                    the text inside of brackets []. If your system is configured 
                    correctly, these will automatically be replaced with the correct 
                    information in the emails sent out.</p></td>
              </tr>
        </table>
    
      <h2><br />
        Step 3. Send Email</h2>
      <p>
        <label>
        <input type="submit" name="command" value="Send Email" />
        </label>
</p>
	</form>
              <h2>Previously Sent Invitations </h2>
              <p>Functionalty in progress. </p>
<%
		// Get email list
	for (ListIterator li = Email.getPrototypeInvites(afso.schema, afso.sim_id, afso.getRunningSimId()).listIterator(); li.hasNext();) {
		Email emailPrototype = (Email) li.next();
%>
Email <%= emailPrototype.getId() %><br/>
<% } // end of loop over invite emails. %>

              </blockquote>
              <% } // end of if running_sim.id has been set. %>
        <%
		
	}// end of if afso.simulation.id has been set.
	
} // End of if email has been enabled.

%> 		</td>
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
</body>
</html>
<%
	
%>
