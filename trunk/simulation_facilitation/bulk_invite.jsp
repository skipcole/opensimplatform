<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,
	org.hibernate.*,
	org.usip.oscw.baseobjects.*" 
	errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	pso.backPage = "bulk_invite.jsp";
	
	String sending_page = (String) request.getParameter("sending_page");
	String set_of_users = "";
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("bulk_invite"))){
		set_of_users = (String) request.getParameter("email_users");

	}
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Control Page</title>
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" -->
<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>
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
<% String canEdit = (String) session.getAttribute("author"); %>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="666" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform </h1></td>
    <td align="right" background="../Templates/images/top_fade.png"> 

	  <div align="center">
	    <table border="0" cellspacing="4" cellpadding="4">
	<%  if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
        <tr>
          <td><a href="../simulation_authoring/intro.jsp" target="_top">Home</a></td>
        </tr>
	<% } else { %>
		<tr>
          <td><a href="index.jsp" target="_top">Home </a></td>
        </tr>
	<% } %>	
        <tr>
          <td><a href="../simulation_user_admin/my_profile.jsp"> My Profile</a></td>
        </tr>
        <tr>
          <td><a href="../simulation_authoring/logout.jsp" target="_top">Logout</a></td>
        </tr>
      </table>	  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"><% 
		
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		
		<table border="0" cellpadding="0" cellspacing="0" >
		   <tr>
		<td><a href="../simulation_planning/index.jsp" target="_top" class="menu_item">THINK</a></td>
		<td>&nbsp;</td>
	    <td><a href="../simulation_authoring/creationwebui.jsp" target="_top" class="menu_item">CREATE</a></td>
		<td>&nbsp;</td>
		<td><a href="facilitateweb.jsp" target="_top" class="menu_item">PLAY</a></td>
		<td>&nbsp;</td>
        <td><a href="../simulation_sharing/index.jsp" target="_top" class="menu_item">SHARE</a></td>
		   </tr>
		</table>
	<% } %></td>
  </tr>
  <tr>
    <td colspan="2" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Invite Players to Register </h1>
      <!-- InstanceEndEditable --></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<BR />
<table width="90%" bgcolor="#FFFFFF" align="center" border="1" cellspacing="0" cellpadding="0">
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" --> 
<p></p>
<blockquote> 

  <p>Bulk Invite <span class="style1">DOES NOT YET</span> Work!</p>
  <p>Please skip this page.</p>
  <p>Please enter a set of emails below, separated by spaces, commas or carriage returns. Then modify the message text as you see fit, and hit send. The users will 
    receive an email inviting them to autoregister on the system. You will 
    then be able to add them as players in one of your simulations.</p>
  <p>(if a user is already registered, you will be told this.)</p>
  <p>(? send email when user registers, or add a way to check if members of group have registered?)</p>
  <p><%= set_of_users %></p>
  <p>&nbsp;</p>
  <form action="bulk_invite.jsp" method="post" name="form1" id="form1">
    <input type="hidden" name="sending_page" value="bulk_invite" />
          <table width="100%" border="0" cellspacing="2" cellpadding="2">
            <tr valign="top"> 
              <td width="34%">Email Addresses: <br /> <br /> </td>
              <td width="66%"><br /> <p> 
                  <textarea name="email_users" cols="60" rows="5"></textarea>
                </p>
                <p>&nbsp;</p></td>
            </tr>
            <tr valign="top">
              <td>Message Text: </td>
              <td><textarea name="textarea2" cols="60" rows="5"></textarea></td>
            </tr>
            <tr valign="top"> 
              <td>&nbsp;</td>
              <td> <input type="submit" name="command" value="Send Invite Email" /></td>
            </tr>
          </table>
  </form>
  <p>&nbsp;</p>
</blockquote>

<blockquote>
      <div align="center">
        <p><a href="../simulation_user_admin/create_user.jsp" target="_top">Next 
          Step: Create Users </a></p>
        <p align="left"><a href="create_running_sim.jsp">&lt;--</a></p>
      </div>
</blockquote>
      <!-- InstanceEndEditable -->
    <p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
<%
	
%>
