<%@ page contentType="text/html; charset=UTF-8" language="java" 
import="java.io.*,java.util.*,java.text.*,
java.sql.*,
org.usip.osp.networking.*,
org.usip.osp.persistence.*,
org.usip.osp.baseobjects.*" %>
<%
	
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	String e_id = request.getParameter("e_id");
	String er_id = request.getParameter("er_id");
	String s_id = request.getParameter("s_id");
	String r_id = request.getParameter("r_id");
	
	//response.sendRedirect("login.jsp");
	//return;
	//This page receives email confirmations and then forwards the player on to the applicable portion of the software.
		
%>
<html>
<head>
<title>USIP Open Simulation Platform Email Confirmation Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="usip_osp.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="third_party_libraries/jquery/jquery-1.4.1.js"></script>
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
    <td width="80%" valign="middle"  background="Templates/images/top_fade.png"><h1 class="header">&nbsp;<%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "USIP_OSP_HEADER") %></h1></td>
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
    <td colspan="3" background="Templates/images/page_bg.png" ><h2 align="center">Thank you for confirming receipt of that email.</h2></td>
  </tr>

</table>

<p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p>
</body>
</html>