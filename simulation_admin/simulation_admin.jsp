<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*" errorPage="" %>
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
      <h1>Simulation Software Admininstration</h1>
      <!-- InstanceEndEditable --></td>
    <td width="20%" align="right" valign="top"> 
		<% 
		String canEdit = (String) session.getAttribute("author");
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		<a href="../simulation_planning/index.jsp" target="_top">Think</a><br>
	    <a href="../simulation_authoring/creationwebui.jsp" target="_top">Create</a><br>
		<a href="../simulation_facilitation/facilitateweb.jsp" target="_top">Play</a><br>
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
  <!--tr valign="top"> 
    <td>4.</td>
    <td>GC <a href="create_game_phases.jsp">creates Game Phases</a></td>
    <td>For example, &quot;In Progress&quot; or &quot;Done&quot;</td>
  </tr -->
  <tr valign="top"> 
    <td width="2%">&nbsp;</td>
    <td width="24%"><a href="../simulation_user_admin/create_admin_user.jsp">Add Administrative User</a></td>
    <td width="2%">&nbsp;</td>
    <td width="72%">Allows you to add a user who can create simulations. Optionally 
      they can also </td>
  </tr>
  <tr valign="top"> 
    <td>&nbsp;</td>
    <td><a href="../oscw_install/clean_db.jsp">Clean Database</a></td>
    <td>&nbsp;</td>
    <td>Warning! This will clean out all data from the database.</td>
  </tr>
  <tr valign="top"> 
    <td>&nbsp;</td>
          <td><a href="../simulation_authoring/create_simulation_section.jsp">Creates Sections</a></td>
    <td>&nbsp;</td>
    <td>Each section provides a place were the player can see and/or do something. 
      One can think of a section as a tab across the top of the player's screen.</td>
  </tr>
  <tr valign="top"> 
    <td>&nbsp;</td>
    <td><a href="../ver1/oscw_install/install_guide.jsp">Installation Instructions</a></td>
    <td>&nbsp;</td>
    <td>These should already have been used. A link here is included for reference. 
    </td>
  </tr>
  <tr valign="top"> 
    <td>&nbsp;</td>
    <td><a href="software_development_section.jsp">Software Development</a></td>
    <td>&nbsp;</td>
    <td>Section useful to developers only.</td>
  </tr>
  <tr valign="top"> 
    <td>&nbsp;</td>
    <td><a href="../simulation_authoring/test_utilities/email_test.jsp">Test Email</a></td>
    <td>&nbsp;</td>
    <td>Allows you to test the functionality of the emailing system.</td>
  </tr>
</table>
<p>&nbsp;</p>
<p>&nbsp;</p>
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
	  <a href="../simulation_facilitation/index.jsp" target="_top">Home 
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
