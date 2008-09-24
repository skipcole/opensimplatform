<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page 
	contentType="text/html; charset=ISO-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" 
	errorPage="" %>
<%
	String e_to = "scole@usip.org";
	String e_cc = "scole@usip.org";
	String e_bcc = "scole@usip.org";
	String e_from = "scole@usip.org";
	String e_subject = "sub";
	String e_text = "text";
	
	String debug = "okay";

	String sending_page = (String) request.getParameter("sending_page");
	String send_email = (String) request.getParameter("send_email");
	
	if ( (sending_page != null) && (send_email != null) && (sending_page.equalsIgnoreCase("email_test"))){
	
		e_to = (String) request.getParameter("e_to");
		e_cc = (String) request.getParameter("e_cc");
		e_bcc = (String) request.getParameter("e_bcc");
		e_from = (String) request.getParameter("e_from");
		e_subject = (String) request.getParameter("e_subject");
		e_text = (String) request.getParameter("e_text");
	
		debug = Emailer.postSimReadyMail(e_to, e_from, e_cc, e_subject, e_text);
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Control Page</title>
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../../usip_oscw.css" rel="stylesheet" type="text/css" />
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
      <h1>Test Email Functionality</h1>
      <!-- InstanceEndEditable --></td>
    <td width="20%" align="right" valign="top"> 
		<% 
		String canEdit = (String) session.getAttribute("author");
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		<a href="../../simulation_planning/index.jsp" target="_top">Think</a><br>
	    <a href="../creationwebui.jsp" target="_top">Create</a><br>
		<a href="../../simulation_facilitation/facilitateweb.jsp" target="_top">Play</a><br>
        <a href="../../simulation_sharing/index.jsp" target="_top">Share</a>
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
<form name="form2" id="form2" method="post" action="">
  <table width="80%" border="0" cellspacing="2" cellpadding="1">
    <tr> 
      <td>To:</td>
      <td> <input type="text" name="e_to" value="<%= e_to %>"/> 
      </td>
    </tr>
    <tr> 
      <td>CC:</td>
      <td> <input type="text" name="e_cc"  value="<%= e_cc %>"/> 
      </td>
    </tr>
    <tr> 
      <td>BCC:</td>
      <td> <input type="text" name="e_bcc"  value="<%= e_bcc %>"/> 
      </td>
    </tr>
    <tr> 
      <td>From:</td>
      <td><input type="text" name="e_from"  value="<%= e_from %>"/></td>
    </tr>
    <tr> 
      <td>Subject</td>
      <td><input type="text" name="e_subject"  value="<%= e_subject %>"/></td>
    </tr>
    <tr> 
      <td>Text:</td>
      <td><input type="text" name="e_text"  value="<%= e_text %>"/></td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
      <td><input type="hidden" name="sending_page" value="email_test" />
	  <input type="submit" name="send_email" value="Submit" /></td>
    </tr>

  </table>
</form>
<p><%= Debug.getDebug(debug) %></p>
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
	<a href="../intro.jsp" target="_top">Home 
      </a>
	  <% } else { %>
	  <a href="../../simulation_facilitation/index.jsp" target="_top">Home 
      </a>
	  <% } %>
	  </td>
    <td align="right" valign="bottom"><a href="../../simulation_user_admin/my_profile.jsp">My 
      Profile</a> </td>
  </tr>
  <tr>
    <td align="left" valign="bottom"><a href="../logout.jsp" target="_top">Logout</a></td>
    <td align="right" valign="bottom">&nbsp;</td>
  </tr>
</table>
<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP Open Source Software Project</a>. </p>
</body>
<!-- InstanceEnd --></html>
