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
		response.sendRedirect("../login.jsp");
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
              <h1>Create Login Messages</h1>
              <p>These are messages that the users receive after they have logged in and selected which overall section (admin, author, instructor or player)  </p>
              <blockquote>
                <form action="create_login_notifications.jsp" method="post" name="form1" id="form1">
                  <h2>
        <input type="hidden" name="sending_page" value="notify_players" />
        Instructions</h2>
      <p>Select the players below you wish to notify. Tailor your email as desire, and then hit send. If you would like to send a copy of a previously sent email, select that email from the list at the bottom to put its text in the body of the email to be sent. </p>
      <h2>Step 1. Users to Notify</h2>
      <table width="100%" border="1">
        <tr>
          <td valign="top"><strong>User Category</strong></td>
          <td valign="top"><strong>Yes</strong></td>
          <td valign="top"><strong>No</strong></td>
        </tr>
        <tr>
          <td valign="top">Admin</td>
          <td valign="top"><input type="radio" name="radio" id="radio" value="radio" />
            <label for="radio"></label></td>
          <td valign="top"><input type="radio" name="radio" id="radio2" value="radio" />
            <label for="radio2"></label></td>
        </tr>
        <tr>
          <td valign="top">Author</td>
          <td valign="top"><input type="radio" name="radio" id="radio3" value="radio" />
            <label for="radio3"></label></td>
          <td valign="top"><input type="radio" name="radio" id="radio4" value="radio" />
            <label for="radio4"></label></td>
        </tr>
        <tr>
          <td valign="top">Facilitator</td>
          <td valign="top"><input type="radio" name="radio" id="radio5" value="radio" />
            <label for="radio5"></label></td>
          <td valign="top"><input type="radio" name="radio" id="radio6" value="radio" />
            <label for="radio6"></label></td>
        </tr>
        <tr>
          <td valign="top">Player</td>
          <td valign="top"><input type="radio" name="radio" id="radio7" value="radio" />
            <label for="radio7"></label></td>
          <td valign="top"><input type="radio" name="radio" id="radio8" value="radio" />
            <label for="radio8"></label></td>
        </tr>
		<%
				} // End of loop over StudentDashboardLine
		%>
      </table>
      <h2><br />
        Step 2. Tailor Message Contents  </h2>
      <table width="100%" border="1" cellspacing="0" cellpadding="2">
        <tr valign="top">
          <td>Message Title</td>
          <td><label>
            <input type="text" name="email_subject" value="<%= email.getSubjectLine() %>" />
            </label></td>
        </tr>
        <tr valign="top"> 
          <td width="34%">Message Text:<br /> <br /> </td>
                <td width="66%">
				  <textarea id="email_text" name="email_text" style="height: 120px; width: 480px;"><%= email.getMsgtext() %></textarea>
                <script language="javascript1.2">
					wysiwygWidth = 480;
					wysiwygHeight = 120;
  			generate_wysiwyg('email_text');
		</script></td>
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
<a href="../simulation_facilitation/facilitate_email_notifications.jsp?queue_up=true&amp;e_id=<%= emailPrototype.getId() %>">Email </a></td>
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

                    <a href="../simulation_facilitation/facilitate_panel.jsp">Home</a>
              <p><a href="../simulation_facilitation/facilitate_enable_simulation.jsp">&lt;-- Back </a></p></td>
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
