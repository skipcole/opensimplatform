<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%
	ParticipantSessionObject pso = new ParticipantSessionObject();
	
	session.setAttribute("pso", pso);
	
	boolean justSentPassword = pso.handleRetrievePassword(request);

	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Control Page</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->
<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>
<!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Retrieve Password</h1>
      <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" -->
<form id="form1" name="form1" method="post" action="retrieve_password.jsp">
<table width="80%" border="1">
  <tr>
    <td width="38%" valign="top">Email Address <br />
      (which is also your username)</td>
    <td width="62%" valign="top">
        <input type="text" name="email" id="textfield" />
    </td>
  </tr>
  <tr>
    <td valign="top">&nbsp;</td>
    <td valign="top"><label>
    	<input type="hidden" name="sending_page" value="retrieve_password" />
      <input type="submit" name="button" id="button" value="Email me my Password" />
    </label></td>
  </tr>
</table>
</form>
<p>&nbsp;</p>
<% if (justSentPassword) { %>
<p>Thank you for your request.</p>
<p>You should soon receive an email containing your password.</p>
<p>Note: If you do not find the  email with your password in it, please check in your junk email folder. If it has gone into there, you may want to register the sender as a 'safe sender.' (The details of doing this depend upon your email service provider, so please direct any email related questions to them.)</p>
<% } // end of if just requested email %> 
<span class="style1">
<%= pso.errorMsg %> </span>
<p>&nbsp;</p>
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
	pso.errorMsg = "";
	
%>
