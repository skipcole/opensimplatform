<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
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
              <h1 align="center">Simulation Facilitation Panel</h1>
              <br />
      <table width="100%" border="0" cellspacing="2" cellpadding="1">
        <tr valign="top"> 
          <td colspan="2"><h2>Step</h2></td>
            </tr>
        <tr valign="top"> 
          <td>1. </td>
            <td><a href="create_running_sim.jsp">Create Running Simulation</a> <a href="helptext/create_running_sim_help.jsp" target="helpinright">(?)</a></td>
            </tr>
        <tr valign="top">
          <td>2.</td>
            <td><a href="create_schedule_page.jsp">Create Schedule Page</a> (?) (optional)</td>
          </tr>
        <tr valign="top">
          <td>3. </td>
            <td><a href="bulk_invite.jsp">Invite Users</a> <a href="helptext/bulk_invite_help.jsp" target="helpinright">(?) </a></td>
            </tr>
        <tr valign="top">
          <td>4.</td>
            <td><a href="administrate_users.jsp">Administrate Users</a> <a href="helptext/administrate_users_help.jsp" target="helpinright">(?) </a></td>
            </tr>
        <tr valign="top"> 
          <td>5.</td>
            <td><a href="create_user.jsp">Create Users</a> <a href="helptext/create_user_help.jsp" target="helpinright">(?)</a> </td>
            </tr>
        <tr valign="top"> 
          <td>6.</td>
            <td><a href="assign_user_to_simulation.jsp">Assigns Players</a> <a href="helptext/assign_players_help.jsp" target="helpinright">(?)</a> </td>
            </tr>
        <tr valign="top"> 
          <td>7.</td>
            <td><a href="enable_simulation.jsp">Enable Simulation</a> <a href="helptext/enable_sim_help.jsp" target="helpinright">(?)</a></td>
            </tr>
        <tr valign="top"> 
          <td>8.</td>
            <td><a href="../simulation/index.jsp?schema=<%= pso.schema %>" target="_top">Enter Simulation</a> <a href="helptext/enter_sim_help.jsp" target="helpinright">(?)</a> </td>
            </tr>
      </table>
      <p></p>
      <p align="center"><a href="library.jsp">Go to the Library</a> </p>      <p align="center"></p>			</td>
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
