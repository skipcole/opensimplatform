<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.baseobjects.*" errorPage="../error.jsp" %>
<%
	
	
	String errorMsg = "";
	
	String results = OSPSessionObjectHelper.handleLoginAttempt(request);
	
	if (results.equalsIgnoreCase("forward_on")){
		response.sendRedirect("select_functionality_and_schema.jsp");
		return;
	} else if (results.equalsIgnoreCase("error")){
		errorMsg = "Incorrect username/password combination.";
	}	
	
%>
<html>
<head>
<title>USIP Open Simulation Platform Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="usip_osp.css" rel="stylesheet" type="text/css">
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
    <td width="80%" valign="middle"  background="Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform Login Page</h1></td>
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
    <td colspan="3" background="Templates/images/page_bg.png" ><P>&nbsp;</P>
      <h1 align="center">&nbsp;&nbsp;&nbsp;USIP Open Simulation Platform <br>
        &nbsp;&nbsp;&nbsp;(Release <%= USIP_OSP_Properties.getRelease() %>)<br> 
        <br>
      </h1>
      <form name="form1" method="post" action="login.jsp" target="_top">
         
        <input type="hidden" name="attempting_login" value="true">
        </font> 
        <table width="58%" border="0" cellspacing="0" cellpadding="0" align="center">
          <tr> 
            <td>user name</font></td>
            <td> <input type="text" name="username"></td>
          </tr>
          <tr> 
            <td>password</font></td>
            <td> <input type="password" name="password"> </td>
          </tr>
          
          <tr> 
            <td>&nbsp;</td>
            <td> <input type="submit" name="Submit" value="Submit"> </td>
          </tr>
          <tr> 
            <td colspan="2"><font color="#FF0000"><%= errorMsg %></font></td>
          </tr>
          <tr>
            <td colspan="2"><div align="right"><span class="style1"><a href="simulation_user_admin/retrieve_password.jsp">Forgot Password? </a></span></div></td>
          </tr>
          <tr>
            <td colspan="2"><div align="right"><a href="simulation_user_admin/auto_registration_form.jsp" class="style1">Register</a></div></td>
          </tr>
        </table>
      </form>
	  <center>
        <table width="50%" border="0" cellspacing="2" cellpadding="1">
           <tr>
            <td>Upcoming Planned Outage:<br /> <%= USIP_OSP_Properties.getNextPlannedDowntime() %></td>
          </tr>
          <tr> 
          <tr> 
            <td><a href="acknowledgements/index.htm">Acknowledgements</a></td>
          </tr>
        </table>
	  </center>
      <p align="center">&nbsp;</p>
    </td>
  </tr>

</table>

<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Creation Software Wizard is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p>
</body>
</html>