<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" errorPage="error.jsp" %>
<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
		
	if (pso.getListOfMessages() == null){
		
		response.sendRedirect(PlayerSessionObject.getURLofSectionToEnter(request));
		
		return;
	}
	
	response.setHeader("Cache-Control", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
    response.setHeader("Pragma", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
    response.setHeader("Expires", "-1"); //$NON-NLS-1$ //$NON-NLS-2$
	
	String entry_type = (String) request.getParameter("entry_type");
	String schema_id = (String) request.getParameter("schema_id");
		
%>
<html>
<head>
<title>USIP Open Simulation Platform Login</title>
<link href="usip_osp.css" rel="stylesheet" type="text/css">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
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
    <td width="80%" valign="middle"  background="Templates/images/top_fade.png"><h1 class="header">&nbsp;<%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "USIP_OSP_HEADER") %>    </h1></td>
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
<table width="720" border="0" cellspacing="0" cellpadding="0" align="center" bgcolor="#DDDDFF">
<tr> 
    <td width="24" height="24" >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3">
      <blockquote> 
          <h2><span class="header"><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "select_region_of_program_to_enter") %></span></h2>
        <table align="center"><tr><td><h3> Login Message</h3></td></tr>
          <tr><td>
Message Text
          </td></tr>
          <tr>
            <td align="center"><form name="form1" method="post" action="">
              <input type="submit" name="button" id="button" value="Submit">
            </form></td>
          </tr>
          <tr>
            <td align="right"><form name="form2" method="post" action="">
              <input type="submit" name="button2" id="button2" value="Cancel">
            </form></td>
          </tr>
          </table>
          <p>&nbsp;</p>
      </blockquote>

</td>
  </tr>
  <tr> 
    <td width="24" height="24" >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
  </tr>
</table>

<p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p>
</body>
</html>