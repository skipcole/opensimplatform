<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%
	
	int displayMode = ResetPasswordObject.handleResetPassword(request);

	ResetPasswordObject this_rpo = null;
	
	if ((displayMode == ResetPasswordObject.SHOW_CHANGE_FORM_MODE) || (displayMode == ResetPasswordObject.SHOW_PASSWORD_DONT_MATCH_MODE) ){
		this_rpo = (ResetPasswordObject) session.getAttribute("this_rpo");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform </h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center"></div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"></td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top">&nbsp;</td>
    <td colspan="1" valign="top"><br /></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<BR />
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
	
<% if ((displayMode == ResetPasswordObject.SHOW_CHANGE_FORM_MODE) || (displayMode == ResetPasswordObject.SHOW_PASSWORD_DONT_MATCH_MODE) ) { 

	String passwordsMatched = "";
	if (displayMode == ResetPasswordObject.SHOW_PASSWORD_DONT_MATCH_MODE) {
		passwordsMatched = "<font color=\"red\">Passwords did not match</font>";
	}

%>
              <h1>Change Password</h1>
              <br />
      <form id="form1" name="form1" method="post" action="reset_password.jsp">
  <table width="80%" border="1">
    <tr>
      <td width="38%" valign="top">Email Address </td>
      <td width="62%" valign="top"><%= this_rpo.getUserEmail() %></td>
    </tr>
    <tr>
      <td valign="top">New Password <%= passwordsMatched %></td>
      <td valign="top"><input type="text" name="pword1" id="textfield" /></td>
    </tr>
    <tr>
      <td valign="top">Confirm Password <%= passwordsMatched %></td>
      <td valign="top"><input type="text" name="pword2" id="pword1" /></td>
    </tr>
    <tr>
      <td valign="top">&nbsp;</td>
      <td valign="top"><label>
        <input type="hidden" name="changeSubmitted" value="true" />
        <input type="submit" name="button" id="button" value="Reset Password" />
        </label></td>
    </tr>
  </table>
        </form>
		<% } else if (displayMode == ResetPasswordObject.SHOW_PASSWORD_CHANGED_MODE) { %>
      <p><strong>Password Successfully Changed. </strong></p>
      <p>You may <a href="../login.jsp">now login</a>.</p>
      <% } else { %>
	  <p><strong>Expired Access Code. </strong></p>
	  <p>If you wish to reset your password, you must submit your email address <a href="retrieve_password.jsp">on this page.</a></p>
	  <p>
	  <% } %>
	  </p></td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>