<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*" 
	errorPage="" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if ( (afso.checkDatabaseCreated()) &&  (!(afso.isLoggedin()))) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String error_msg = afso.handleCreateDB(request);
	
	String db_schema = afso.getClean(request, "db_schema");
	String db_org = afso.getClean(request, "db_org");
    String db_user = afso.getClean(request, "db_user");
    String db_pass = afso.getClean(request, "db_pass");
	String db_loc = afso.getClean(request, "db_loc");
	String db_port = afso.getClean(request, "db_port");
	String db_notes = afso.getClean(request, "db_notes");
	
	String admin_first = afso.getClean(request, "admin_first");
	String admin_middle = afso.getClean(request, "admin_middle");
	String admin_last = afso.getClean(request, "admin_last");	
	
	String admin_pass = afso.getClean(request, "admin_pass");
    String admin_email = afso.getClean(request, "admin_email");
	String new_admin_user_cbox = afso.getClean(request, "new_admin_user_cbox");

    //////////////////////////////////////////////////////////////////    
    String email_smtp = afso.getClean(request, "email_smtp");
    String email_user = afso.getClean(request, "email_user");
    String email_pass = afso.getClean(request, "email_pass");
    String email_user_address = afso.getClean(request, "email_user_address");
        

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
	background-image: url(../Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform Installation</h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"></td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top"></td>
    <td colspan="1" valign="top"></td>
    <td width="194" align="right" valign="top"></td>
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
    <td colspan="3"> <form action="install_db.jsp" method="post" name="form1" id="form1">
        <h1>
          <input type="hidden" name="sending_page" value="clean_db" />
          Install Database </h1>
        <blockquote>
          <blockquote> 
            <table width="75%" border="0" cellspacing="2" cellpadding="1">
              <tr> 
                <td><strong>Database Login</strong></td>
                <td>&nbsp;</td>
              </tr>
              <tr> 
                <td valign="top">DB Schema <a href="helptext/db_schema.jsp" target="helpinright">(?)</a></td>
                <td valign="top"><input type="text" name="db_schema" value="<%= db_schema %>" /></td>
              </tr>
              <tr> 
                <td valign="top">Organization</td>
                <td valign="top"><input type="text" name="db_org" value="<%= db_org %>" /></td>
              </tr>
              <tr> 
                <td valign="top">DB User</td>
                <td valign="top"><input type="text" name="db_user" value="<%= db_user %>" /></td>
              </tr>
              <tr> 
                <td valign="top">DB Password</td>
                <td valign="top"><input type="text" name="db_pass" value="<%= db_pass %>" /></td>
              </tr>
              <tr> 
                <td valign="top">DB Location</td>
                <td valign="top"><input type="text" name="db_loc" value="<%= db_loc %>" /></td>
              </tr>
              <tr> 
                <td valign="top">DB Port</td>
                <td valign="top"><input type="text" name="db_port" value="<%= db_port %>" /></td>
              </tr>
              <tr>
                <td valign="top">DB Notes</td>
                <td valign="top"><textarea name="db_notes" cols="40" rows="2"><%= db_notes %></textarea></td>
              </tr>
              <tr> 
                <td valign="top">&nbsp;</td>
                <td valign="top">&nbsp;</td>
              </tr>
              <tr> 
                <td valign="top"><strong>Admin login</strong></td>
                <td valign="top">&nbsp;</td>
              </tr>
              <tr> 
                <td valign="top">admin first name:</td>
                <td valign="top"> 
                  <input type="text" name="admin_first" value="<%= admin_first %>" /></td>
              </tr>
              <tr> 
                <td valign="top">admin middle name:</td>
                <td valign="top"> 
                  <input type="text" name="admin_middle" value="<%= admin_middle %>" /></td>
              </tr>
              <tr> 
                <td valign="top">admin last name:</td>
                <td valign="top"> 
                  <input type="text" name="admin_last" value="<%= admin_last %>" /></td>
              </tr>
              <tr> 
                <td valign="top">admin username/email:</td>
                <td valign="top"> 
                  <input type="text" name="admin_email" value="<%= admin_email %>"/></td>
              </tr>
              <tr> 
                <td valign="top">admin password:</td>
                <td valign="top"> 
                  <input type="text" name="admin_pass" value="<%= admin_pass %>"/></td>
              </tr>
               <tr>
                 <td valign="top">&nbsp;</td>
                 <td valign="top">&nbsp;</td>
               </tr>
              <tr> 
                <td valign="top"><strong>Email Settings</strong></td>
                <td valign="top">&nbsp;</td>
              </tr>
              <tr> 
                <td valign="top">email SMTP </td>
                <td valign="top"><label> 
                  <input type="text" name="email_smtp" value="<%= email_smtp %>" />
                  </label></td>
              </tr>
              <tr> 
                <td valign="top">email user </td>
                <td valign="top"><input type="text" name="email_user" value="<%= email_user %>" /></td>
              </tr>
              <tr> 
                <td valign="top">email password </td>
                <td valign="top"><input type="text" name="email_pass" value="<%= email_pass %>" /></td>
              </tr>
              <tr> 
                <td valign="top">email user address </td>
                <td valign="top"><input type="text" name="email_user_address"  value="<%= email_user_address %>" /></td>
              </tr>
            </table>
          </blockquote>
          <p>
            <input name="loadss" type="checkbox" value="true" checked="checked" />
            Load all section descriptor files found in sections directory</p>
        </blockquote>
        <table width="80%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td valign="top">Warning. Hitting submit will purge the database.</td>
            <td><input type="submit" name="cleandb" value="Submit" /></td>
          </tr>
        </table>
        
        <% if ((error_msg != null) && (error_msg.equalsIgnoreCase("database_created"))){ %>
		<p> You may now<a href="../login.jsp"> login as the root user</a> with the password that you provided.</p>
        <% } else { %>
        <p><font color="#FF0000"><%= error_msg %></font></p>
        <% } %>
        
        <p><a href="steps_2.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a></p>
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
