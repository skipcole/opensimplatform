<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%
	
	String sending_page = (String) request.getParameter("sending_page");
	
	boolean retrieve_attempt_made = false;
	boolean justSentPassword = false;

	if ((sending_page != null) && (sending_page.equalsIgnoreCase("retrieve_password"))) {
		
		retrieve_attempt_made = true;
		justSentPassword = AuthorFacilitatorSessionObject.handleSendResetPasswordEmail(request);
		
	}

	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>USIP Open Simulation Platform Request Change Password Email</title>


<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>

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
              <h1>Change Password</h1>
              <br />
      <form id="form1" name="form1" method="post" action="retrieve_password.jsp">
  <table width="80%" border="1">
    <tr>
      <td width="38%" valign="top">Email Address <br />
        (which is also your username)</td>
      <td width="62%" valign="top">
        <input type="text" name="email" id="textfield" />        </td>
    </tr>
    <tr>
      <td valign="top">&nbsp;</td>
      <td valign="top"><label>
        <input type="hidden" name="sending_page" value="retrieve_password" />
        <input type="submit" name="button" id="button" value="Change Password" />
        </label></td>
    </tr>
  </table>
        </form>
      <p>&nbsp;</p>
      <% if (retrieve_attempt_made) { %>
      <% if (justSentPassword) { %>
      <p>Thank you for your request.</p>
      <p>You should soon receive an email containing a link and instructions on how to change your password.</p>
      <p>Note: If you do not receive the email, please check in your junk email folder. If it has gone into there, you may want to register the sender as a 'safe sender.' (The details of doing this depend upon your email service provider, so please direct any email related questions to them.)</p>
      <% } // end of if just requested email 
	  		else {
	  %> 
      <span class="style1">
      <font color="#FF0000">The email address you entered was not found in our database.</font>
		 </span>      
	<% } // and of if failed attempt. %>
    <% } // end of if attempted to retrieved attempt. %>
<p>&nbsp;</p>			</td>
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