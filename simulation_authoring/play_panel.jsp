<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*" errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Control Page</title>
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(../Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">
<% String canEdit = (String) session.getAttribute("author"); %>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="666" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform </h1></td>
    <td align="right" background="../Templates/images/top_fade.png"> 

	  <div align="center">
	    <table border="0" cellspacing="4" cellpadding="4">
	<%  if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
        <tr>
          <td><a href="intro.jsp" target="_top">Home</a></td>
        </tr>
	<% } else { %>
		<tr>
          <td><a href="../simulation_facilitation/index.jsp" target="_top">Home </a></td>
        </tr>
	<% } %>	
        <tr>
          <td><a href="../simulation_user_admin/my_profile.jsp"> My Profile</a></td>
        </tr>
        <tr>
          <td><a href="logout.jsp" target="_top">Logout</a></td>
        </tr>
      </table>	  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"><% 
		
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		
		<table border="0" cellpadding="0" cellspacing="0" >
		   <tr>
		<td><a href="../simulation_planning/index.jsp" target="_top" class="menu_item">THINK</a></td>
		<td>&nbsp;</td>
	    <td><a href="creationwebui.jsp" target="_top" class="menu_item">CREATE</a></td>
		<td>&nbsp;</td>
		<td><a href="../simulation_facilitation/facilitateweb.jsp" target="_top" class="menu_item">PLAY</a></td>
		<td>&nbsp;</td>
        <td><a href="../simulation_sharing/index.jsp" target="_top" class="menu_item">SHARE</a></td>
		   </tr>
		</table>
	<% } %></td>
  </tr>
  <tr>
    <td colspan="2" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Simulation Logistics Panel</h1>
      <!-- InstanceEndEditable --></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<BR />
<table width="90%" bgcolor="#FFFFFF" align="center" border="1" cellspacing="0" cellpadding="0">
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" --> 
      <table width="100%" border="0" cellspacing="2" cellpadding="1">
        <tr valign="top"> 
          <td>&nbsp;</td>
          <td><h2>Step</h2></td>
          <td><h2>&nbsp;</h2></td>
          <td><h2>Description</h2></td>
        </tr>
        <tr valign="top"> 
          <td>&nbsp;</td>
          <td><a href="../simulation_facilitation/create_running_sim.jsp">Create Running Simulation</a></td>
          <td>&nbsp;</td>
          <td>This creates a playing session, for example &quot;Summer 2007-1.&quot; 
          </td>
        </tr>
        <tr valign="top">
          <td>&nbsp;</td>
          <td>Invite Users</td>
          <td>&nbsp;</td>
          <td>You can enter a list of user emails here to send them an invitation 
            to self register - saving you the process of entering their information.</td>
        </tr>
        <tr valign="top"> 
          <td>&nbsp;</td>
          <td><a href="create_user.jsp">Create Users</a></td>
          <td>&nbsp;</td>
          <td>This is an entry for the real live simulation participant, for example, 
            &quot;John Doe.&quot;</td>
        </tr>
        <tr valign="top"> 
          <td>&nbsp;</td>
          <td><a href="assign_user_to_simulation.jsp">Assigns Users</a></td>
          <td>&nbsp;</td>
          <td>For example assigning &quot;John Doe&quot; to the role of &quot;UN 
            Head of Mission&quot;</td>
        </tr>
        <tr valign="top"> 
          <td>&nbsp;</td>
          <td><a href="enable_simulation.jsp">Begin Simulation</a></td>
          <td>&nbsp;</td>
          <td>Here a Simulation Creator can enable a simulation to begin, suspend 
            a simulation, or mark a simulation as terminated.</td>
        </tr>
        <tr valign="top"> 
          <td>&nbsp;</td>
          <td><a href="../simulation/index.jsp?schema=<%= pso.schema %>" target="_top">Enter Simulation</a></td>
          <td>&nbsp;</td>
          <td>If you are a character (such as control) in a simulation, you can 
            now log in to it to see how it appears.</td>
        </tr>
        <tr valign="top"> 
          <td>&nbsp;</td>
          <td><a href="list_simulation.jsp">List Simulation</a></td>
          <td>&nbsp;</td>
          <td>Mark a simulation as ready for play by instructors.</td>
        </tr>
      </table>
<p>
</p>
<!-- InstanceEndEditable -->
    <p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
