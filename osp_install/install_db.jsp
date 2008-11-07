<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if ( (pso.checkDatabaseCreated()) &&  (!(pso.isLoggedin()))) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String error_msg = pso.handleCreateDB(request);
	
	String db_schema = pso.getClean(request, "db_schema");
	String db_org = pso.getClean(request, "db_org");
    String db_user = pso.getClean(request, "db_user");
    String db_pass = pso.getClean(request, "db_pass");
	String db_loc = pso.getClean(request, "db_loc");
	String db_port = pso.getClean(request, "db_port");
	
	String admin_first = pso.getClean(request, "admin_first");
	String admin_middle = pso.getClean(request, "admin_middle");
	String admin_last = pso.getClean(request, "admin_last");	
	String admin_full = pso.getClean(request, "admin_full");
	
	String admin_pass = pso.getClean(request, "admin_pass");
    String admin_email = pso.getClean(request, "admin_email");
	String new_admin_user_cbox = pso.getClean(request, "new_admin_user_cbox");

    //////////////////////////////////////////////////////////////////    
    String email_smtp = pso.getClean(request, "email_smtp");
    String email_user = pso.getClean(request, "email_user");
    String email_pass = pso.getClean(request, "email_pass");
    String email_user_address = pso.getClean(request, "email_user_address");
        

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Open Simulation Platform Control Page</title>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
</head>
<body onLoad="">

<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3"> <form action="install_db.jsp" method="post" name="form1" id="form1">
        <h1>
          <input type="hidden" name="sending_page" value="clean_db" />
          Install Database </h1>
        <table width="80%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td valign="top">Warning. Hitting submit on this page will purge the 
              database.</td>
            <td>&nbsp;</td>
          </tr>
        </table>
        <blockquote>
          <blockquote> 
            <table width="75%" border="0" cellspacing="2" cellpadding="1">
              <tr> 
                <td><strong>Database Login</strong></td>
                <td>&nbsp;</td>
              </tr>
              <tr> 
                <td>DB Schema <a href="helptext/db_schema.jsp" target="helpinright">(?)</a></td>
                <td><input type="text" name="db_schema" value="<%= db_schema %>" /></td>
              </tr>
              <tr> 
                <td>Organization</td>
                <td><input type="text" name="db_org" value="<%= db_org %>" /></td>
              </tr>
              <tr> 
                <td>DB User</td>
                <td><input type="text" name="db_user" value="<%= db_user %>" /></td>
              </tr>
              <tr> 
                <td>DB Password</td>
                <td><input type="text" name="db_pass" value="<%= db_pass %>" /></td>
              </tr>
              <tr> 
                <td>DB Location</td>
                <td><input type="text" name="db_loc" value="<%= db_loc %>" /></td>
              </tr>
              <tr> 
                <td>DB Port</td>
                <td><input type="text" name="db_port" value="<%= db_port %>" /></td>
              </tr>
              <tr> 
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr> 
                <td><strong>Admin login</strong></td>
                <td>&nbsp;</td>
              </tr>
              <tr> 
                <td>admin first name:</td>
                <td> 
                  <input type="text" name="admin_first" value="<%= admin_first %>" /></td>
              </tr>
              <tr> 
                <td>admin middle name:</td>
                <td> 
                  <input type="text" name="admin_middle" value="<%= admin_middle %>" /></td>
              </tr>
              <tr> 
                <td>admin last name:</td>
                <td> 
                  <input type="text" name="admin_last" value="<%= admin_last %>" /></td>
              </tr>
              <tr> 
                <td>admin user full name:</td>
                <td> 
                  <input type="text" name="admin_full" value="<%= admin_full %>" /></td>
              </tr>
              <tr> 
                <td>admin username/email:</td>
                <td> 
                  <input type="text" name="admin_email" value="<%= admin_email %>"/></td>
              </tr>
              <tr> 
                <td><input type="checkbox" name="new_admin_user_cbox" value="new" />
                  New Admin User</td>
                <td>Fill out field in blue below</td>
              </tr>
              <tr> 
                <td bgcolor="#0099FF">admin password:</td>
                <td bgcolor="#0099FF"> 
                  <input type="text" name="admin_pass" value="<%= admin_pass %>"/></td>
              </tr>
              <tr> 
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr> 
                <td><strong>Email Settings</strong></td>
                <td>&nbsp;</td>
              </tr>
              <tr> 
                <td>email SMTP </td>
                <td><label> 
                  <input type="text" name="email_smtp" value="<%= email_smtp %>" />
                  </label></td>
              </tr>
              <tr> 
                <td>email user </td>
                <td><input type="text" name="email_user" value="<%= email_user %>" /></td>
              </tr>
              <tr> 
                <td>email password </td>
                <td><input type="text" name="email_pass" value="<%= email_pass %>" /></td>
              </tr>
              <tr> 
                <td>email user address </td>
                <td><input type="text" name="email_user_address"  value="<%= email_user_address %>" /></td>
              </tr>
            </table>
          </blockquote>
          <p>
            <input name="loadss" type="checkbox" value="true" checked="checked" />
            Load standard sections</p>
          <p> 
<input name="load_cs" type="checkbox" value="true"  />            
Check for and load custom simulation sections.</p>
        </blockquote>
        <table width="80%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td valign="top">Warning. Hitting submit will purge the database.</td>
            <td><input type="submit" name="cleandb" value="Submit" /></td>
          </tr>
        </table>
        <p><font color="#FF0000"><%= error_msg %></font></p>
        <p><a href="steps.jsp">&lt;-- Back</a></p>
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

<p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP 
  Open Source Software Project</a>. </p>
</body>
</html>
<%
	
%>
