<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.oscw.baseobjects.*,
		org.usip.oscw.networking.*,
		org.usip.oscw.persistence.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if ( (pso.checkDatabaseCreated()) &&  (!(pso.isLoggedin()))) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String error_msg = pso.handleCreateDB(request);
	
	String db_schema = (String) request.getParameter("db_schema");
    String db_user = (String) request.getParameter("db_user");
    String db_pass = (String) request.getParameter("db_pass");
	
	String root_realname = (String) request.getParameter("root_realname");
	String rootpass1 = (String) request.getParameter("rootpass1");
    String rootpass2 = (String) request.getParameter("rootpass2");
    String rootemail1 = (String) request.getParameter("rootemail1");
    String rootemail2 = (String) request.getParameter("rootemail2");
        
    String email_smtp = (String) request.getParameter("email_smtp");
    String email_user = (String) request.getParameter("email_user");
    String email_pass = (String) request.getParameter("email_pass");
    String email_user_address = (String) request.getParameter("email_user_address");
        


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Online Simulation Platform Control Page</title>
<link href="../usip_oscw.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
</head>
<body onload="">
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td width="80%" valign="top"> <h1>Clean the Database</h1></td>
    <td width="20%" align="right" valign="top"> <a href="../simulation_planning/index.jsp" target="_top">Think</a><br> 
      <a href="../simulation_authoring/creationwebui.jsp" target="_top">Create</a><br> 
      <a href="../simulation_facilitation/facilitateweb.jsp" target="_top">Play</a><br> 
      <a href="../simulation_sharing/index.jsp" target="_top">Share</a></td>
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
    <td colspan="3"> <form action="clean_db.jsp" method="post" name="form1" id="form1">
        <input type="hidden" name="sending_page" value="clean_db" />
        <table width="80%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td valign="top">Warning. Hitting submit will purge the database.</td>
            <td>&nbsp;</td>
          </tr>
        </table>
        <blockquote>
          <p>&nbsp;</p>
          <p>&nbsp; </p>
          <blockquote> 
            <table width="75%" border="0" cellspacing="2" cellpadding="1">
              <tr> 
                <td>Database Login</td>
                <td>&nbsp;</td>
              </tr>
              <tr> 
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr> 
                <td>DB Schema</td>
                <td><input type="text" name="db_schema" /></td>
              </tr>
              <tr> 
                <td>DB User</td>
                <td><input type="text" name="db_user" /></td>
              </tr>
              <tr> 
                <td>DB Password</td>
                <td><input type="text" name="db_password" /></td>
              </tr>
              <tr> 
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr> 
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr> 
                <td>Root login</td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td>root user real name:</td>
                <td><input type="text" name="root_realname" /></td>
              </tr>
              <tr> 
                <td>root username:</td>
                <td>root</td>
              </tr>
              <tr> 
                <td>root password:</td>
                <td><input type="text" name="rootpass1" value="<%= rootpass1 %>"/></td>
              </tr>
              <tr> 
                <td>confirm password:</td>
                <td><input type="text" name="rootpass2" value="<%= rootpass2 %>"/></td>
              </tr>
              <tr> 
                <td>root email:</td>
                <td><input type="text" name="rootemail1" /></td>
              </tr>
              <tr> 
                <td>confirm email</td>
                <td><input type="text" name="rootemail2" /></td>
              </tr>
              <tr> 
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr> 
                <td><strong>Email Settings(?) </strong></td>
                <td>&nbsp;</td>
              </tr>
              <tr> 
                <td>email SMTP </td>
                <td><label> 
                  <input type="text" name="email_smtp" />
                  </label></td>
              </tr>
              <tr> 
                <td>email user </td>
                <td><input type="text" name="email_user" /></td>
              </tr>
              <tr> 
                <td>email password </td>
                <td><input type="text" name="email_pass" /></td>
              </tr>
              <tr> 
                <td>email user address </td>
                <td><input type="text" name="email_user_address" /></td>
              </tr>
            </table>
          </blockquote>
          <p>
            <input name="loadss" type="checkbox" value="true" checked="checked" />
            Load standard sections</p>
          <p> 
            <input name="load_cs" type="checkbox" value="true" checked="checked" />
            Check for and load custom simulation sections.</p>
          </blockquote>
        <table width="80%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td valign="top">Warning. Hitting submit will purge the database.</td>
            <td><input type="submit" name="cleandb" value="Submit" /></td>
          </tr>
        </table>
        <p><font color="#FF0000"><%= error_msg %></font></p>
      </form>
      <p>&nbsp;</p>
      <p>&nbsp;</p></td>
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
    <td align="left" valign="bottom"> <a href="../simulation_authoring/intro.jsp" target="_top">Home 
      </a></td>
    <td align="right" valign="bottom"><a href="../simulation_user_admin/my_profile.jsp">My 
      Profile</a> </td>
  </tr>
  <tr> 
    <td align="left" valign="bottom"><a href="../simulation_authoring/logout.jsp" target="_top">Logout</a></td>
    <td align="right" valign="bottom">&nbsp;</td>
  </tr>
</table>
<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP 
  Open Source Software Project</a>. </p>
</body>
</html>
<%
	
%>
