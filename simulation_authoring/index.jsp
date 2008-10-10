<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" errorPage="" %>
<%
				
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	String prevErrorMsg = pso.errorMsg;
	
	String attempting_login = (String) request.getParameter("attempting_login");
	
	if ((attempting_login != null) && (attempting_login.equalsIgnoreCase("true"))){
		session.setAttribute("pso", null);
		pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
		response.sendRedirect(pso.validateLoginToSimAuthoringTool(request));
		return;
	} // End of if login in.
	
	pso.errorMsg = prevErrorMsg;
	
%>
<html>
<head>
<title>USIP Online Simulation Platform Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../usip_osp.css" rel="stylesheet" type="text/css">
</head>

<body>
<p>&nbsp;</p>
<p>&nbsp;</p>
<table width="720" border="0" cellspacing="0" cellpadding="0" align="center" background="../Templates/images/page_bg.png">

  <tr> 
    <td colspan="3" background="../Templates/images/page_bg.png" ><P>&nbsp;</P>
      <h1 align="center">&nbsp;&nbsp;&nbsp;USIP Open Simulation Platform<br> 
        <br>
        &nbsp;&nbsp;&nbsp;Authoring / 
        Facilitation Login</h1>
      <p>&nbsp;</p>
      <form name="form1" method="post" action="index.jsp" target="_top">
         
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
            <td colspan="2"><font color="#FF0000"><%= pso.errorMsg %></font></td>
          </tr>
        </table>
      </form>
	  <center>
        <table width="50%" border="0" cellspacing="2" cellpadding="1">
          <tr> 
            <td><a href="../acknowledgements/index.htm">Acknowledgements</a></td>
          </tr>
        </table>
	  </center>
      <p align="center">&nbsp;</p>
    </td>
  </tr>

</table>

<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Creation Software Wizard is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP Open Source Software Project</a>. </p>
</body>
</html>
<%
	pso.errorMsg = "";
%>