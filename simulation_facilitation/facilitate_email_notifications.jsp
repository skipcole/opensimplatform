<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.persistence.*,
	org.usip.osp.coursemanagementinterface.*,
	org.hibernate.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	afso.backPage = "email_notifications.jsp";
	
	// Used to detect if email has been enabled on this server, and to send email if needed.
	SchemaInformationObject sio = SchemaInformationObject.lookUpSIOByName(afso.schema);
	
	Email email = afso.handleNotifyPlayers(request, sio);

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

<title>Open Simulation Platform Email Notification Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>


<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {
	color: #FF0000;
	font-weight: bold;
}
-->
</style>
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
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
        <% } else { %>
        <%
			if (afso.getRunningSimId() == null) {
		%>
        <p>You must select the running simulation.        </p>
		  
		<% } else if (!(running_sim.isReady_to_begin())) { %>
        <p><strong>Running simulation <%= running_sim.getRunningSimulationName() %> </strong> <span class="style1">has  not been enabled. You must first enable a simulation before sending invitation emails.</span><br />

		  <% } else { %>
        <p>Emailing players in  <strong>running simulation <%= running_sim.getRunningSimulationName() %></strong></p>
  
    <form action="facilitate_email_notifications.jsp" method="post" name="form1" id="form1">
      <h2>
        <input type="hidden" name="sending_page" value="notify_players" />
        Instructions</h2>
      <p>Select the players below you wish to notify. Tailor your email as desire, and then hit send. If you would like to send a copy of a previously sent email, select that email from the list at the bottom to put its text in the body of the email to be sent. </p>
      <h2>Step 1. Players to Notify</h2>
      <table width="100%" border="1">
        <tr>
          <td valign="top"><strong>Actor</strong></td>
          <td valign="top"><strong>Student Name </strong></td>
          <td valign="top"><strong>Username</strong></td>
          <td valign="top"><strong>Status</strong></td>
          <td valign="top"><strong>Send</strong></td>
        </tr>
		<% 
		
			List dashboardLines = StudentDashboardLine.getDashboardLines(afso.sim_id, afso.schema, afso.runningSimId);
			
			// Loop over all actors in the simulation
			for (ListIterator li = dashboardLines.listIterator(); li.hasNext();) {
				StudentDashboardLine sdl = (StudentDashboardLine) li.next();

					%>
        <tr>
          <td valign="top"><%= sdl.getStudentRole() %></td>
          <td valign="top">
		  	<% if (sdl.isStudentRegistered()) { %>
				<%= sdl.getStudentName() %>
			<% } else { %>
				<input name="<%= sdl.getUserAssignmentId() %>_user_display_name" type="text" value="<%= sdl.getStudentName() %>" size="30" maxlength="80" />
			<% } %>
		  </td>
          <td valign="top"><%= sdl.getStudentEmail() %></td>
          <td valign="top"><%= sdl.getStudentStatus() %></td>
          <td valign="top">
		  	<%
				String checked_value = "checked=\"checked\"";
				
				if (sdl.isStudentInvited()){
					checked_value = "";
				}
				
			%>
		  <label>
            <input type="checkbox" name="invite_<%= sdl.getUserAssignmentId() %>" value="true" <%= checked_value %> />
          </label></td>
        </tr>
		<%
				} // End of loop over StudentDashboardLine
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
                <td width="66%">
				  <textarea id="email_text" name="email_text" style="height: 120px; width: 480px;"><%= email.getMsgtext() %></textarea>
                <script language="javascript1.2">
					wysiwygWidth = 480;
					wysiwygHeight = 120;
  			generate_wysiwyg('email_text');
		</script>
                </p>
                  <p><strong><font color="#CC9900">Note:</font> You should not edit the text inside of brackets []. This text will automatically be replaced with the correct 
                    information for your system. </strong></p></td>
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
              <h2>Previously Sent Invitations for this Running Simulation </h2>
<%
	List previousSentList = Email.getPrototypeInvites(afso.schema, afso.sim_id, afso.getRunningSimId());
	
	if ((previousSentList == null) || (previousSentList.size() == 0)) {
	
	%>
	None
	<% } else { %>
	<table border="1">
	<TR>
	<td valign="top"><strong>Click to Queue it Up</strong></td>
	<td valign="top"><strong>Sent</strong></td>
	<td valign="top"><strong>Subject Line</strong></td>
	</TR>
	<%
		// Get email list
	for (ListIterator li = Email.getPrototypeInvites(afso.schema, afso.sim_id, afso.getRunningSimId()).listIterator(); li.hasNext();) {
		Email emailPrototype = (Email) li.next();
%>
<tr>
<td valign="top">
<a href="facilitate_email_notifications.jsp?queue_up=true&amp;e_id=<%= emailPrototype.getId() %>">Email </a></td>
<td valign="top"><%= emailPrototype.getSendDate() %></td>
<td valign="top"><%= emailPrototype.getSubjectLine() %></td>
</tr>
<% } // end of loop over invite emails. %>
</table>
<% } // end of if there were previous invits %>
              </blockquote>
              <p align="center">
                <% } // end of if running_sim.id has been set. %>
                    <%
		
	}// end of if afso.simulation.id has been set.
	
} // End of if email has been enabled.

%> 		

                    <a href="facilitate_panel.jsp">Home</a>
              <p><a href="facilitate_enable_simulation.jsp">&lt;-- Back </a></p></td>
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
