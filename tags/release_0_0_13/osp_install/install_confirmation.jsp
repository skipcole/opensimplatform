<%@ page contentType="text/html; charset=UTF-8" 

language="java" 
import="java.sql.*,java.util.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*" 
errorPage=""

 %>
<%
	String schema = (String) request.getParameter("schema");
	
	int num_bss = BaseSimSection.getAllBaseAndCustomizable(schema).size();
	
	String emailstatus = request.getParameter("emailstatus");
	
	
	int email_msg = 0;
	
	if (emailstatus != null) {
		
		if (emailstatus.equalsIgnoreCase("email_sent")){
			email_msg = 1;	
		} else if (emailstatus.equalsIgnoreCase("email_not_sent")){
			email_msg = 2;
		}
	
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td width="80%" valign="top"><br /> <h1>OSP Installation Confirmed. </h1></td>
    <td width="20%" align="right" valign="top">&nbsp;</td>
  </tr>
</table>
<blockquote>
<p align="left">Schema <%= schema %> installed with an initial catalog of <%= " " + num_bss %> Simulation Sections.</p>
<p align="left">You may now<a href="../login.jsp"> login as the root user</a> with the password that you provided.</p>
<% if (email_msg == 1) { %>
<ul><li>A test email has been sent to your admin email address using the email paramaters that you entered. If you don't find it, please look in your junk email. If you still can't find it you may want to verify the email parameters that you entered in the Admistration section.</li>
</ul>
<% } else if (email_msg == 2) { %>
<ul><li>
Email has not been yet enabled for this installation. 
</li></ul>
<% } %>
<p><br>
</p>
</blockquote>

<p><strong>Any questions? Contact our community at <a href="http://docs.opensimplatform.org">docs.opensimplatform.org</a></strong></p>
<p><a href="mailto:scole@usip.org"></a></p>
<p>&nbsp;</p>

</body>
</html>
