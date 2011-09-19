<%@ page contentType="text/html; charset=UTF-8" language="java" 
import="java.io.*,java.util.*,java.text.*,
java.sql.*,
org.usip.osp.networking.*,
org.usip.osp.persistence.*,
org.usip.osp.baseobjects.*" 
errorPage="/error.jsp"
%>
<%
	
	SessionObjectBase sob = USIP_OSP_Util.getSessionObjectBase(request);
	
	int returnCode = sob.processConfirmation(request);

		
%>
<html>
<head>
<title>USIP Open Simulation Platform Email Confirmation Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="usip_osp.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<style type="text/css">
<!--
.style1 {font-size: small}
-->
</style>
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
</head>
<body onLoad="">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="Templates/images/top_fade.png"><h1 class="header">&nbsp;<%= USIP_OSP_Cache.getInterfaceText(request, sob.languageCode, "USIP_OSP_HEADER") %></h1></td>
    <td align="right" background="Templates/images/top_fade.png" width="20%"> 

	  <div align="center"></div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"></td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top">&nbsp;</td>
    <td colspan="1" valign="top"><br /></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<p>&nbsp;</p>
<table width="720" border="0" cellspacing="0" cellpadding="0" align="center" background="Templates/images/page_bg.png">

  <tr> 
    <td colspan="3" background="Templates/images/page_bg.png" >
	<% if (returnCode == SessionObjectBase.CONFIRM_DEFAULT) { %>
		<h2 align="center">You have reached the email confirmation page.</h2>
	<% } else { %>
		<h2 align="center">Thank you for confirming receipt of your invitation email.</h2>
		<% if (returnCode == SessionObjectBase.USER_FOUND) { %>
      	<p align="left">You may now <a href="login.jsp">login to the platform</a>.</p>
	  	<% } else if (returnCode == SessionObjectBase.USER_NOT_FOUND){ %>
      	<p align="left">You must  <a href="simulation_user_admin/auto_registration_page.jsp?schema=<%= sob.schema %>&ua_id=<%= sob.uaId %>">register on this system</a> to join in this simulation. </p>
      	<p align="left">&nbsp;</p>
	 <% } // End of if it looks like a real confirmation request. %>
	  
	  </td>
	<% } %>
  </tr>

</table>

<p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p>
</body>
</html>