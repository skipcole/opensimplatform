<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,
	java.util.*,
	java.text.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.coursemanagementinterface.*,
	org.hibernate.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = "bulk_invite.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	

	String invitationSetName = (String) request.getParameter("invitationSetName");
	invitationSetName = java.net.URLDecoder.decode(invitationSetName);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
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
              <h1>Invited Players to Register on <%= invitationSetName %></h1>
              <br />
      <blockquote> 
	  <table width="100%">
	  <tr>
	  <td><strong>Invite Email</strong></td>
	  <td><strong>Date Registered</strong></td>
	  <td><strong>Email Registered </strong></td>
	  </tr>
	  <% 
        List uriList = UserRegistrationInvite.getAllInASet(afso.schema, invitationSetName);
		
		for (ListIterator urli = uriList.listIterator(); urli.hasNext();) {
				UserRegistrationInvite uri = (UserRegistrationInvite) urli.next(); 
		%>
		<tr>
			<td><%= uri.getOriginalInviteEmailAddress() %></td>
			<td><%= uri.getRegistrationDate() %></td>
		    <td><%= uri.getEmailAddressRegistered() %></td>
		</tr>
		<% } %>
		</table>
        <p>&nbsp;</p>
		</blockquote>
      <blockquote>
        <div align="center">
          <p>&nbsp;</p>
          </div>
        </blockquote>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
<%
	
%>
