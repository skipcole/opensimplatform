<%@ page contentType="text/html; charset=iso-8859-1" language="java" 
import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" errorPage="../error.jsp" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>USIP Open Simulation Platform</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			  <h1 align="center"> Welcome! </h1>
			  <br />            <table width="720" background="../Templates/images/page_bg.png" align="center" cellpadding="0" cellspacing="0">
              <tr> 
                <td width="720"> 
                  <p><font size="+1">We have created this wizard to help 
                      <a href="#">you, a subject matter expert</a>, create an <a href="#">online</a> <a href="#">training</a> <a href="#">simulation</a> and 
                    then be able to <a href="#">share</a> it with others.</font></p>
          <p><font size="+1">Let's unpack that a bit...</font></p>
        </blockquote>
                  <div align="center"> 
                    <table width="90%" border="0" cellspacing="2" cellpadding="1">
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
            <td width="25%"><h2><a href="../simulation_planning/index.jsp" target="_top">Think</a></h2></td>
            <td width="25%"><h2><a href="creationwebui.jsp" target="_top">Create</a></font></h2></td>
            <td width="25%"><h2><a href="../simulation_facilitation/playweb.jsp" target="_top">Play</a></h2></td>
            <td width="25%"><h2><a href="../simulation_sharing/index.jsp" target="_top">Share</a></h2></td>
          </tr>
          </table>
        <p align="center">  
          <% if (pso.isAdmin()) { %>
          <a href="../simulation_admin/adminwebui.jsp" target="_top">Administrate</a> 
          <% } %>
          <br />
          </font></p>        </td>
    </tr>
            </table>			</td>
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
<%
	
%>