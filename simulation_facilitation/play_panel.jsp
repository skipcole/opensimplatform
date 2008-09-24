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
<link href="../usip_oscw.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="80%" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Simulation Facilitation Panel</h1>
      <!-- InstanceEndEditable --></td>
    <td width="20%" align="right" valign="top"> 
		<% 
		String canEdit = (String) session.getAttribute("author");
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		<a href="../simulation_planning/index.jsp" target="_top">Think</a><br>
	    <a href="../simulation_authoring/creationwebui.jsp" target="_top">Create</a><br>
		<a href="facilitateweb.jsp" target="_top">Play</a><br>
        <a href="../simulation_sharing/index.jsp" target="_top">Share</a>
		<% } %>
		</td>
  </tr>
</table>
<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
</tr>
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
          <td><a href="create_running_sim.jsp">Create Running Simulation</a></td>
          <td>&nbsp;</td>
          <td>This creates a playing session, for example &quot;Summer 2007-1.&quot; 
          </td>
        </tr>
        <tr valign="top">
          <td>&nbsp;</td>
          <td><a href="bulk_invite.jsp">Invite Users</a></td>
          <td>&nbsp;</td>
          <td>You can enter a list of user emails here to send them an invitation 
            to self register - saving you the process of entering their information.</td>
        </tr>
        <tr valign="top"> 
          <td>&nbsp;</td>
          <td><a href="../simulation_user_admin/create_user.jsp">Create Users</a></td>
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
          <td>Here an instructor can enable a simulation to begin.</td>
        </tr>
        <tr valign="top"> 
          <td>&nbsp;</td>
          <td><a href="../simulation/index.jsp?schema=<%= pso.schema %>" target="_top">Enter Simulation</a></td>
          <td>&nbsp;</td>
          <td>If you are a character (such as control) in a simulation, you can 
            now log in to it to see how it appears.</td>
        </tr>
      </table>
<p>
</p>
<!-- InstanceEndEditable --></td>
  </tr>
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
</table>

<p>&nbsp;</p>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td align="left" valign="bottom"> 
	<% 
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
	<a href="../simulation_authoring/intro.jsp" target="_top">Home 
      </a>
	  <% } else { %>
	  <a href="index.jsp" target="_top">Home 
      </a>
	  <% } %>
	  </td>
    <td align="right" valign="bottom"><a href="../simulation_user_admin/my_profile.jsp">My 
      Profile</a> </td>
  </tr>
  <tr>
    <td align="left" valign="bottom"><a href="../simulation_authoring/logout.jsp" target="_top">Logout</a></td>
    <td align="right" valign="bottom">&nbsp;</td>
  </tr>
</table>
<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP Open Source Software Project</a>. </p>
</body>
<!-- InstanceEnd --></html>
