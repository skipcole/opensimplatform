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
          <td><a href="../simulation_authoring/intro.jsp" target="_top">Home</a></td>
        </tr>
	<% } else { %>
		<tr>
          <td><a href="../simulation_facilitation/index.jsp" target="_top">Home </a></td>
        </tr>
	<% } %>	
        <tr>
          <td><a href="my_profile.jsp"> My Profile</a></td>
        </tr>
        <tr>
          <td><a href="../simulation_authoring/logout.jsp" target="_top">Logout</a></td>
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
	    <td><a href="../simulation_authoring/creationwebui.jsp" target="_top" class="menu_item">CREATE</a></td>
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
      <h1>Create User</h1>
      <!-- InstanceEndEditable --></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<BR />
<table width="90%" bgcolor="#FFFFFF" align="center" border="1" cellspacing="0" cellpadding="0">
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
      <p>Below are listed alphabetically by username all of the current players 
        listed for your organization.</p>
<blockquote>
        <p>
          <%
		  
		  List userList = User.getAllForSchemaAndLoadDetails(pso.schema);
		
		for (ListIterator uli = userList.listIterator(); uli.hasNext();) {
			User bu = (User) uli.next();  %>
		
          <%= bu.getBu_username() %> <br />
          <% } %>
        </p>
        <p align="center"><a href="../simulation_facilitation/assign_user_to_simulation.jsp">Next 
          Step: Assign User</a></p>
        <p align="left"><a href="../simulation_facilitation/create_running_sim.jsp">&lt;- Back</a></p>
</blockquote>
<!-- InstanceEndEditable -->
    <p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
<%
	pso.errorMsg = "";
	
%>
