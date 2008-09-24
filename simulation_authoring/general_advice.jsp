<%@ page contentType="text/html; charset=ISO-8859-1" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*" errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Control Page</title>
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_oscw.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="80%" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Some Advice on Creating Online Simulations</h1>
      <!-- InstanceEndEditable --></td>
    <td width="20%" align="right" valign="top"> 
		<% 
		String canEdit = (String) session.getAttribute("author");
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		<a href="../simulation_planning/index.jsp" target="_top">Think</a><br>
	    <a href="creationwebui.jsp" target="_top">Create</a><br>
		<a href="../simulation_facilitation/facilitateweb.jsp" target="_top">Play</a><br>
        <a href="../simulation_sharing/index.jsp" target="_top">Share</a>
		<% } %>
		</td>
  </tr>
</table>
<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
</tr>
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" --> 
      <blockquote>
        <p>Below is some general advice we  feel is useful. If there is something 
          you would like to add or modify here, please contact us. </p>
        <p>Creating an effective online training simulation 
          is not an easy task. This wizard will help step you through all of the 
          essential mechanical steps, but this is no assurance that the final 
          product will perform correctly and teach the lessons you want to convey. 
          The only way to verify that is to have people try your simulation and 
          listen closely to their feedback. It is quite possible (really probable) 
          that you will need to modify your simulation </p>
        <p>So, expect this to be an iterative process: You 
          create something. You try it out on some players. You get feedback. 
          You modify your simulation. You try that out. And so on.</p>
        <p>This isn't bad. It&#8217;s just the way that 
          things of this complexity have to grow. You will learn much, and your 
          players will learn much, in the act of perfecting the simulation. The 
          more open one's mind is during the process, the more they will be able 
          to learn.</p>
        <p>In a sense, you can think of this program as 
          tool to create an architectural blueprint. The ability to generate a 
          blueprint, and hence a building, is no assurance at all that the resulting 
          building will be functional. The art of architecture has been developed 
          now over centuries, and so there are established patterns to help someone 
          starting out. There are few established patterns in creating online 
          simulations. </p>
        <p>This means that your job will be difficult at 
          times, but it also means that you get to be one of the founders helping 
          to find out what works and what doesn't. As you develop new methods, 
          you will probably reach the limit to what this tool can do, hence this 
          tool has been created open source, so it can always grow, and modular, 
          so it can grow easily. If you have a feature you would like installed, 
          please contact us..</p>
      </blockquote>
      <p>&nbsp; </p>
<!-- InstanceEndEditable --></td>
  </tr>
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
</table>

<p>&nbsp;</p>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td align="left" valign="bottom"> 
	<% 
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
	<a href="intro.jsp" target="_top">Home 
      </a>
	  <% } else { %>
	  <a href="../simulation_facilitation/index.jsp" target="_top">Home 
      </a>
	  <% } %>
	  </td>
    <td align="right" valign="bottom"><a href="../simulation_user_admin/my_profile.jsp">My 
      Profile</a> </td>
  </tr>
  <tr>
    <td align="left" valign="bottom"><a href="logout.jsp" target="_top">Logout</a></td>
    <td align="right" valign="bottom">&nbsp;</td>
  </tr>
</table>
<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP Open Source Software Project</a>. </p>
</body>
<!-- InstanceEnd --></html>
<%
	
%>