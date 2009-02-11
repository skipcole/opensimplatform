<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,
	org.hibernate.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	pso.backPage = "bulk_invite.jsp";
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	
	String sending_page = (String) request.getParameter("sending_page");
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("bulk_invite"))){
		pso.handleBulkInvite(request);
	}
	

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
      <h1>Invite Players to Register </h1>
      <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
<blockquote> 

  <p>Please enter a set of emails below, separated by spaces, commas or carriage returns. Then modify the message text as you see fit, and hit send. The users will 
    receive an email inviting them to autoregister on the system. You will 
    then be able to add them as players in one of your simulations.</p>
  <p>&nbsp;</p>
  <form action="bulk_invite.jsp" method="post" name="form1" id="form1">
    <input type="hidden" name="sending_page" value="bulk_invite" />
          <table width="100%" border="0" cellspacing="2" cellpadding="2">
            <tr valign="top"> 
              <td width="34%">Email Addresses: (?) <br /> <br /> </td>
              <td width="66%"><br /> <p> 
                  <textarea name="setOfUsers" cols="60" rows="5"><%= pso.setOfUsers %></textarea>
                </p>
                <p>&nbsp;</p></td>
            </tr>
            <tr valign="top">
              <td>Message Text: </td>
              <td><textarea name="defaultInviteEmailMsg" cols="60" rows="5"><%= pso.getDefaultInviteMessage() %></textarea></td>
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
        <p><a href="administrate_users.jsp" target="_top">Next 
          Step: Administrate Users </a></p>
        <p align="left"><a href="create_schedule_page.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a></p>
      </div>
</blockquote>
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
