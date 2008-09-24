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
	
	String db_schema = pso.getClean(request, "db_schema");
    String db_user = pso.getClean(request, "db_user");
    String db_pass = pso.getClean(request, "db_pass");
	String db_loc = pso.getClean(request, "db_loc");
	String db_port = pso.getClean(request, "db_port");
	
	String admin_first = pso.getClean(request, "admin_first");
	String admin_middle = pso.getClean(request, "admin_middle");
	String admin_last = pso.getClean(request, "admin_last");
	
	String admin_full = pso.getClean(request, "admin_full");
	
	String admin_username = pso.getClean(request, "admin_username");
	
	String root_realname = pso.getClean(request, "root_realname");
	String rootpass1 = pso.getClean(request, "rootpass1");
    String rootpass2 = pso.getClean(request, "rootpass2");
    String rootemail1 = pso.getClean(request, "rootemail1");
    String rootemail2 = pso.getClean(request, "rootemail2");
        
    String email_smtp = pso.getClean(request, "email_smtp");
    String email_user = pso.getClean(request, "email_user");
    String email_pass = pso.getClean(request, "email_pass");
    String email_user_address = pso.getClean(request, "email_user_address");
        

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
		  <blockquote>
          <table width="80%" border="0" cellspacing="2" cellpadding="1">
            <tr> 
              <td>id</td>
              <td>Schema Name</td>
              <td>Organization</td>
            </tr>
            <tr> 
              <td>1</td>
              <td>usiposcw</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>2</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
          </table>
          <p>&nbsp;</p>
        </blockquote>
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

<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP 
  Open Source Software Project</a>. </p>
</body>
</html>
<%
	
%>
