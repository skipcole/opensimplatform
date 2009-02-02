<%@ page contentType="text/html; charset=iso-8859-1" language="java" 
import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>USIP Open Simulation Platform</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->


<link href="../ver1/simulation_authoring/help_oscw.css" rel="stylesheet" type="text/css" />
<!-- InstanceEndEditable -->
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
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">
<%
	String myLogoutPage = pso.getBaseSimURL() + "/simulation/logout.jsp";
	
	if ( (pso.isAuthor())  || (pso.isFacilitator())) {
		myLogoutPage = pso.getBaseSimURL() + "/simulation_authoring/logout.jsp";
	}
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform</h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
	    <table border="0" cellspacing="1" cellpadding="0">
	<%  if (pso.isAuthor()) { %>
        <tr>
          <td><div align="center"><a href="intro.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } else if (pso.isFacilitator()) { %>
		<tr>
          <td><div align="center"><a href="../simulation_facilitation/instructor_home.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } %>	
        <tr>
          <td><div align="center"><a href="../simulation_user_admin/my_profile.jsp" class="menu_item"><img src="../Templates/images/my_profile.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
        <tr>
          <td><div align="center"><a href="<%= myLogoutPage %>" target="_top" class="menu_item"><img src="../Templates/images/logout.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
      </table>	  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"><% 
		
		String bgColor_think = "#475DB0";
		String bgColor_create = "#475DB0";
		String bgColor_play = "#475DB0";
		String bgColor_share = "#475DB0";
		
		pso.findPageType(request);
		
		if (pso.page_type == 1){
			bgColor_think = "#9AABE1";
		} else if (pso.page_type == 2){
			bgColor_create = "#9AABE1";
		} else if (pso.page_type == 3){
			bgColor_play = "#9AABE1";
		} else if (pso.page_type == 4){
			bgColor_share = "#9AABE1";
		}
		
		if (pso.isAuthor()) { %>
		
		<table border="0" cellpadding="0" cellspacing="0" >
		   <tr>
		<td bgcolor="<%= bgColor_think %>"><a href="../simulation_planning/index.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;THINK&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
	    <td bgcolor="<%= bgColor_create %>"><a href="creationwebui.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;CREATE&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
		<td bgcolor="<%= bgColor_play %>"><a href="../simulation_facilitation/facilitateweb.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;PLAY&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
        <td bgcolor="<%= bgColor_share %>"><a href="../simulation_sharing/index.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;SHARE&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		   </tr>
		</table>
	<% } %></td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top"></td>
    <td colspan="1" valign="top"></td>
    <td width="194" align="right" valign="top"></td>
  </tr>
</table>
<BR />
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			<!-- InstanceBeginEditable name="pageTitle" --><h1 align="center"> Welcome! </h1><!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" -->
<table width="720" background="../Templates/images/page_bg.png" align="center" cellpadding="0" cellspacing="0">
  <tr> 
    <td width="720"> 
        <p><font size="+1">We have created this wizard to help 
          you, a subject matter expert, create an online training simulation and 
          then be able to share it with others.</font></p>
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
        </font></p>
      <p align="center"><br />
        </font></p></td>
  </tr>
</table>

<!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
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
<!-- InstanceEnd --></html>
<%
	
%>