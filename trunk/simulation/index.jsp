<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" errorPage="" %>
<%
	
	ParticipantSessionObject pso;
	
	String attempting_login = (String) request.getParameter("attempting_login");
	if ((attempting_login != null) && (attempting_login.equalsIgnoreCase("true"))){
		session.setAttribute("pso", null);
		pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
		response.sendRedirect(pso.validateLoginToSim(request));
		return;
		
	} // End of if login in.
	
	pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
			
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>USIP Open Simulation Platform</title>

<style type="text/css">
<!--
.style1 {font-size: small}
-->
</style>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
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
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table width="500" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="100%">
            <table width="100%" border="0" align="center" cellpadding="1" cellspacing="2">
              <tr> 
                <td></td>
    </tr>
              <tr>
                <td><h1>Simulation Login<br />
                </h1></td>
              </tr>
              <tr> 
                <td><form action="index.jsp" method="post" name="form1" id="form1">
        <input type="hidden" name="attempting_login" value="true" />
        <table width="80%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td valign="top">email address</td>
              <td valign="top"> <input type="text" name="username" /> </td>
            </tr>
          <tr> 
            <td valign="top">password</td>
              <td valign="top"><input type="password" name="password" /></td>
            </tr>
          <tr> 
            <td valign="top">&nbsp;</td>
              <td valign="top"><input type="submit" name="Submit" value="Submit" /></td>
            </tr>
          </table>
        </form></td>
      </tr>
              <tr> 
                <td><p><font color="#FF0000"><%= pso.errorMsg %></font></p></td>
      </tr>
              <tr>
                <td><div align="right" class="style1"><a href="../simulation_user_admin/retrieve_password.jsp">Forgot Password?   </a></div></td>
      </tr>
              <tr>
                <td><div align="right" class="style1"><a href="../simulation_user_admin/a_reg_frameset.jsp">Register   </a></div></td>
      </tr>
            </table>
            <p>&nbsp;</p>			</td>
		</tr>
	  </table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>


<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
<% pso.errorMsg = "";
%>