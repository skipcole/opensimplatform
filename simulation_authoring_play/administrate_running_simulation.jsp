<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.coursemanagementinterface.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	RunningSimulation running_simulation = new RunningSimulation();
	
	String rs_id = request.getParameter("rs_id");
	
	afso.setRunningSimId(new Long(rs_id));
	
	running_simulation = RunningSimulation.getById(afso.schema, afso.getRunningSimId());
	//////////////////////////////////////////////////////
	
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
<body onLoad="" bgcolor="#00FFFF">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Administrate Running Simulation </h1>
              <p>&nbsp;</p>
              <h2><a name="admintasks" id="admintasks"></a>Administration Tasks </h2>
              <table width="100%" border="0" cellspacing="2" cellpadding="1">
                <tr valign="top">
                  <td>1. </td>
                  <td><a href="create_running_sim.jsp">Edit Running Simulation</a> Name <a href="../simulation_facilitation/helptext/create_running_sim_help.jsp" target="helpinright">(?)</a></td>
                </tr>
                <tr valign="top">
                  <td>2.</td>
                  <td><a href="create_schedule_page.jsp">Edit  Schedule Page</a> <a href="../simulation_facilitation/helptext/create_schedule_help.jsp"  target="helpinright">(?)</a></td>
                </tr>
                <tr valign="top">
                  <td>3.</td>
                  <td><a href="assign_user_to_simulation.jsp">Assign Players</a> <a href="../simulation_facilitation/helptext/assign_players_help.jsp" target="helpinright">(?)</a> </td>
                </tr>
                <tr valign="top">
                  <td>4.</td>
                  <td><a href="enable_simulation.jsp">Enable Simulation</a> 
				  <% if (running_simulation.isReady_to_begin()) { %>
				  	(Simulation is Enabled) 
				  <% } %>
				  <a href="../simulation_facilitation/helptext/enable_sim_help.jsp" target="helpinright">(?)</a></td>
                </tr>
                <tr valign="top">
                  <td>5.</td>
                  <td><a href="email_notifications.jsp">Notify Players via Email</a><a href="../simulation_facilitation/helptext/email_notify_help.jsp" target="helpinright"> (?) </a></td>
                </tr>
                <tr valign="top">
                  <td>7.</td>
                  <td>Inactivate Running Simulation (?) </td>
                </tr>
              </table>
              <p>&nbsp;</p></td>
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
