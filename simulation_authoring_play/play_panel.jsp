<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.baseobjects.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	afso.backPage = "../simulation_authoring_play/play_panel.jsp";

	Simulation simulation = new Simulation();
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
	RunningSimulation running_simulation = new RunningSimulation();
	if (afso.getRunningSimId() != null){
		running_simulation = afso.giveMeRunningSim();
	}
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
</head>
<body onLoad=""  bgcolor="#00FFFF">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1 align="center">Simulation Practice Play Control Panel</h1>
              <p align="left">These pages allow you to kick off a game similar to what an instructor does. But these pages will not be what the facilitators use. (The blue background is a reminder that these are similar but not the same.) On these pages you are able to test out unpublished simulations.</p>
              <blockquote>
              <% if (afso.sim_id != null) { %>
				<strong>Working on Simulation: <%= simulation.getDisplayName() %> </strong> (<a href="../simulation_authoring/select_simulation.jsp">change</a>)<br/>
              <% } %>
			  <% if (afso.getRunningSimId() != null) { %>
				<strong>Working on Running Simulation: <%= running_simulation.getRunningSimulationName() %> </strong> (<a href="select_running_simulation.jsp">change</a>)<br/>
              <% } %>
			  </blockquote>
                
                <h2>Basic Steps </h2>
                <p>Below are the basic steps one will need to go through to initiate a simulation. </p>
                <table width="100%" border="0" cellspacing="2" cellpadding="1">
        
        <tr valign="top"> 
          <td>1. </td>
            <td><a href="create_running_sim.jsp">Create Running Simulation</a> <a href="../simulation_facilitation/helptext/create_running_sim_help.jsp" target="helpinright">(?)</a></td>
            </tr>
        <tr valign="top">
          <td>2.</td>
            <td><a href="create_schedule_page.jsp">Create Schedule Page</a> <a href="../simulation_facilitation/helptext/create_schedule_help.jsp"  target="helpinright">(?)</a></td>
          </tr>
        <tr valign="top"> 
          <td>3.</td>
            <td><a href="assign_user_to_simulation.jsp">Assign Players</a> <a href="../simulation_facilitation/helptext/assign_players_help.jsp" target="helpinright">(?)</a> </td>
            </tr>
        <tr valign="top"> 
          <td>4.</td>
            <td><a href="enable_simulation.jsp">Enable Simulation</a> <a href="../simulation_facilitation/helptext/enable_sim_help.jsp" target="helpinright">(?)</a></td>
            </tr>
        <tr valign="top">
          <td>5.</td>
          <td><a href="email_notifications.jsp">Notify Players via Email</a><a href="../simulation_facilitation/helptext/email_notify_help.jsp" target="helpinright"> (?) </a></td>
        </tr>
      </table>
                <h2>&nbsp;</h2>
                <h2>Basic Instructor Activities</h2>
                <p>Below are listed other activities that instructors occasionally have to do. </p>
      <ul><li><a href="../simulation_user_admin/create_user.jsp">Create Users</a> <a href="../simulation_facilitation/helptext/create_user_help.jsp" target="helpinright">(?)</a></li>
        <li><a href="../simulation_facilitation/inactivate_running_sim.jsp">Inactivate Running Simulations</a></li>
        <li><a href="../simulation_facilitation/bulk_invite.jsp">Invite Users to Register </a> <a href="../simulation_facilitation/helptext/bulk_invite_help.jsp" target="helpinright">(?) </a></li>
        <li><a href="launch_beta_test.jsp">Launch Beta Test</a></li>
        </ul>      
      <p>&nbsp;</p>
      <h2>Advanced Instructor Activities </h2>
      <p>Below are listed instructor activities that are either rare, or in development.</p>
      <ul>
        <li><a href="../simulation_facilitation/create_set_of_running_sims.jsp">Create Set of Running Simulations</a></li>
        <li><a href="../simulation_facilitation/export_import_experience.jsp">Export / Import Simulation Experience</a></li>
      </ul>
      <p>&nbsp; </p>
      <p align="center"></p>			</td>
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
