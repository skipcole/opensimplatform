<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" 
	errorPage="" %>
<%
	
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);

	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	pso.handleCreateUser(request);


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
      <h1>Self Registration </h1>
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

<p><font color="#FF0000"><%= pso.errorMsg %></font></p>
<form action="create_user.jsp" method="post" name="form1" id="form1">
        <table width="80%" border="0" cellspacing="0" cellpadding="0">
		  <tr>
            <td>&nbsp;</td>
            <td>username/email<a href="../simulation_authoring/helptext/user_email.jsp" target="helpinright">(?)</a></td>
            <td><input type="text" name="email" tabindex="1" /></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>password<a href="../simulation_authoring/helptext/user_password.jsp" target="helpinright"> 
              (?)</a></td>
            <td><input type="text" name="password" tabindex="2" /></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>real 
              name<a href="../simulation_authoring/helptext/user_real_name.jsp" target="helpinright">(?)</a></td>
            <td><input type="text" name="realname" tabindex="3" /></td>
          </tr>
		  <tr><td>&nbsp;</td>
		  <td>First name:</td>
                <td> 
                  <input type="text" name="admin_first" value="<%= admin_first %>" /></td>
          </tr>
              <tr> 
			  	<td>&nbsp;</td>
                <td>Middle name:</td>
                <td> 
                  <input type="text" name="admin_middle" value="<%= admin_middle %>" /></td>
              </tr>
              <tr> 
			  	<td>&nbsp;</td>
                <td>Last name:</td>
                <td> 
                  <input type="text" name="admin_last" value="<%= admin_last %>" /></td>
		  </tr>
				               <tr> 
			  	<td>&nbsp;</td>
                <td>Full name:</td>
                <td> 
                  <input type="text" name="admin_last" value="<%= admin_last %>" /></td>
				  </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>
				
			  <input type="hidden" name="sending_page" value="create_users" /> 
			
              <input type="submit" name="command" value="Save" tabindex="6" />
			  <%
			  	// Put in switch here to allow the ediiting of users
			  %>
              <label>
              <input type="submit" name="command" value="Clear" />
              </label></td>
          </tr>
        </table>
</form>
<p>&nbsp;</p>
      <p>&nbsp;</p>
<blockquote>
  <p align="center"><a href="../simulation_facilitation/assign_user_to_simulation.jsp">Next 
          Step: Assign User</a></p>
        <p align="left"><a href="../simulation_facilitation/create_running_sim.jsp">&lt;- Back</a></p>
</blockquote>
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
    <td align="right" valign="bottom"><a href="my_profile.jsp">My 
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
<%
	pso.errorMsg = "";
	
%>
