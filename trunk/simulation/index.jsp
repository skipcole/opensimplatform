<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" errorPage="" %>
<%
	
	ParticipantSessionObject pso;
	
	String attempting_login = (String) request.getParameter("attempting_login");
	if ((attempting_login != null) && (attempting_login.equalsIgnoreCase("true"))){
		session.setAttribute("pso", null);
		pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
		response.sendRedirect(pso.validateLoginToSim(request));
		
	} // End of if login in.
	
	pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
			
%>
<html>
<head>
<title>Online Simulation Platform Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style type="text/css">
<!--
.style1 {font-size: small}
-->
</style>
</head>

<body>

<p>&nbsp;</p>
<table width="100%" border="0" cellspacing="2" cellpadding="1">
  <tr> 
    <td colspan="3"><h1>Online Simulation</h1></td>
  </tr>
  <tr> 
    <td width="25%">&nbsp;</td>
    <td><form name="form1" method="post" action="index.jsp">
        <input type="hidden" name="attempting_login" value="true">
        <table width="80%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td>email address</td>
            <td> <input type="text" name="username"> </td>
          </tr>
          <tr> 
            <td>password</td>
            <td><input type="password" name="password"></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td><input type="submit" name="Submit" value="Submit"></td>
          </tr>
        </table>
      </form></td>
    <td width="25%">&nbsp;</td>
  </tr>
  <tr> 
    <td width="25%">&nbsp;</td>
    <td><p><font color="#FF0000"><%= pso.errorMsg %></font></p></td>
    <td width="25%">&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><div align="right" class="style1"><a href="../simulation_facilitation/retrieve_password.jsp">Forgot Password?   </a></div></td>
    <td>&nbsp;</td>
  </tr>
</table>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>Simulations accessed here were generated the <a href="http://www.usip.org">USIP</a> Open Simulation Platform which is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project. </a></p>
<p>&nbsp;</p>

</body>
</html>
<% pso.errorMsg = "";
%>