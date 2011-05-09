<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if ((pso == null) || (!(pso.isLoggedin()))) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	int returnCode = afso.changeUserName(request);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>


<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			<% if (returnCode == SessionObjectBase.USERNAME_CHANGED) { %>
			<h1>Username Changed</h1>
			<% } else if (returnCode == SessionObjectBase.USERNAME_MISMATCH) { %>
			<h1 class="style1">New Usernames Did Not Match</h1>
			<% } else { %>
			<h1>Change Username</h1>

			  	<p>&nbsp;</p>
			  	<form id="form1" name="form1" method="post" action="change_username.jsp">
  <table border="0" cellspacing="2" cellpadding="1" width="100%">
    <tr>
      <td>Old Username (email address): </td>
      <td>
        <label>
        <input type="password" name="old_username" />
        </label></td>
    </tr>
    <tr>
      <td>New Username  (email address): </td>
      <td>
        <label>
        <input type="password" name="new_username" />
        </label></td>
    </tr>
    <tr>
      <td>Confirm New Username (email address): </td>
      <td>
        <label>
        <input type="password" name="new_username2" />
        </label></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td><label>
        <input type="hidden" name="sending_page" value="change_password" /> 
        <input type="submit" name="update" id="update" value="Update" disabled="disabled" />
        </label></td>
    </tr>
  </table>
        </form>      
<% } // end of if change password %>
		<p>&nbsp;</p>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>