<%@ page contentType="text/html; charset=iso-8859-1" language="java" 
import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*" errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Online Simulation Platform Introduction</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../ver1/simulation_authoring/help_oscw.css" rel="stylesheet" type="text/css">
</head>

<body>
<table width="720" bgcolor="#DDDDFF" align="center" cellpadding="0" cellspacing="0">
<tr> 
    <td width="24" height="24" >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3"> <h1 align="center"> Welcome! </h1>
      <blockquote> 
        <p><font size="+1">We have created this wizard to help 
          you, a subject matter expert, create an online training simulation and 
          then be able to share it with others.</font></p>
        <p><font size="+1">Let's unpack that a bit...</font></p>
      </blockquote>
      <div align="center"> 
        <table width="60%" border="0" cellspacing="2" cellpadding="1">
          <tr valign="top"> 
            <td><font size="+1">You:</font></td>
            <td><font size="+1">A subject matter expert - not 
              necessarily a computer programmer</font></td>
          </tr>
          <tr valign="top"> 
            <td><font size="+1">Online:</font></td>
            <td><font size="+1">Played over the Internet</font></td>
          </tr>
          <tr valign="top"> 
            <td><font size="+1">Training:</font></td>
            <td><font size="+1">Created for the specific purpose 
              of giving instruction</font></td>
          </tr>
          <tr valign="top"> 
            <td><font size="+1">Simulation: &nbsp;</font></td>
            <td><font size="+1">An experience that bears some 
              likenesses to reality - it is a process in which the end is not 
              easy to determine</font></td>
          </tr>
          <tr valign="top"> 
            <td><font size="+1">Share:</font></td>
            <td><font size="+1">Once you have created a simulation 
              you will be able to let others build on top of your wisdom</font></td>
          </tr>
        </table>
      </div>
      <blockquote>
        <p>Below are listed the basic steps in the process.</p>

        <ol>
          <ol>
            <li><font size="+1">Think: Some advice on creating online simulations</font></li>
            <li><font size="+1">Create: Use this tool to create online simulations</font></li>
            <li><font size="+1">Play: Expose players to the simulations you have 
              created and gain valuable feedback.</font></li>
            <li><font size="+1">Share: Compact and upload your Simulation to share 
              with others.</font></li>
          </ol>
        </ol>
        </blockquote>
      <table width="100%" border="0" cellspacing="2" cellpadding="1">
        <tr align="center"> 
          <td width="25%"><h2><a href="../simulation_planning/index.jsp">Think</a></h2></td>
          <td width="25%"><h2><a href="creationwebui.jsp">Create</a></font></h2></td>
          <td width="25%"><h2><a href="../simulation_facilitation/facilitateweb.jsp">Play</a></h2></td>
          <td width="25%"><h2><a href="../simulation_sharing/index.jsp">Share</a></h2></td>
        </tr>
      </table>
      <p align="center">  
        <% if (pso.isAdmin()) { %>
        <a href="../simulation_admin/adminwebui.jsp">Administrate</a> 
        <% } %>
        </font><br>
        </font></p></td>
  </tr>
<tr> 
    <td width="24" height="24" >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
  </tr>
</table>
<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Creation Software Wizard is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP Open Source Software Project</a>. </p>
</body>
</html>
<%
	
%>