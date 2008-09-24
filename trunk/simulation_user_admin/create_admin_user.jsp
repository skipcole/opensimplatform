<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" 
	errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin()) || (!(pso.isAdmin()))) {
		response.sendRedirect("index.jsp");
		return;
	}

	String sending_page = (String) request.getParameter("sending_page");
	String adduser = (String) request.getParameter("adduser");

	///////////////////////////////////	
	if ( (sending_page != null) && (adduser != null) && (sending_page.equalsIgnoreCase("create_users"))){

		System.out.println("creating user");
		pso.handleCreateAdminUser(request);
		            
	} // End of if coming from this page and have added user.

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
      <h1>Create Admin User</h1>
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
      <p>On this page you can create simulation authors, instructors and administrative 
        users.</p>
<form action="create_admin_user.jsp" method="post" name="form1" id="form1">
        <table width="80%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td>username/email<a href="../simulation_authoring/helptext/user_name.jsp" target="helpinright">(?)</a>:</td>
            <td> <input type="text" name="email" tabindex="1" /> </td>
          </tr>
          <tr> 
            <td>password<a href="../simulation_authoring/helptext/user_password.jsp" target="helpinright">(?)</a></td>
            <td><input type="text" name="password" tabindex="2" /></td>
          </tr>
          <tr> 
            <td>real name<a href="../simulation_authoring/helptext/user_real_name.jsp" target="helpinright">(?)</a></td>
            <td><input type="text" name="realname" tabindex="3" /></td>
          </tr>
          <tr> 
            <td>administrator</td>
            <td><input type="checkbox" name="admin" value="true" tabindex="4" /></td>
          </tr>
          <tr> 
            <td>simulation author</td>
            <td><input name="author" type="checkbox" value="true" tabindex="5" /></td>
          </tr>
          <tr> 
            <td>simulation Instructor</td>
            <td><input name="instructor" type="checkbox" value="true" tabindex="6" /></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td><input type="hidden" name="sending_page" value="create_users" /> 
              <input type="submit" name="adduser" value="Submit" /></td>
          </tr>
        </table>
</form>
<p>&nbsp;</p>      <p>Below are listed alphabetically by username all of the current sim creators 
        and administrators.</p>
      <blockquote> 
        <table width="80%" border="1" cellspacing="1" cellpadding="2">
          <tr> 
            <td><strong>Name</strong></td>
            <td><strong>Admin</strong></td>
            <td><strong>Author</strong></td>
            <td><strong>Facilitator</strong></td>
          </tr>
          <% for (ListIterator li = User.getAllAdminsSCandInstructors(pso.schema).listIterator(); li.hasNext();) {
			User user = (User) li.next(); %>
          <tr> 
            <td><%= user.getBu_username() %></td>
            <td><%= user.isAdmin() %></td>
            <td><%= user.isSim_author() %></td>
            <td><%= user.isSim_instructor() %></td>
          </tr>
          <% } %>
        </table>
        <p>&nbsp;</p>
        
        
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