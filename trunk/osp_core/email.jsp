<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.communications.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	pso.backPage = "email.jsp";
	
	List emailToList = new ArrayList();
	List sentList = new ArrayList();
	List draftList = new ArrayList();
	
	if (!(pso.preview_mode)) {
		emailToList = Email.getAllTo(pso.schema, pso.getRunningSimId(), pso.getActorId());
		sentList = Email.getDraftsOrSent(pso.schema, pso.getRunningSimId(), pso.getActorId(), true);
		draftList = Email.getDraftsOrSent(pso.schema, pso.getRunningSimId(), pso.getActorId(), false);
	}
		
%>
<html>
<head>
<title>OSP Email</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<meta http-equiv="refresh" content="120" />
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<h1>Email</h1>
<p align="center"><a href="write_email.jsp">Compose New Email </a>| <a href="email.jsp">Check for New Email</a></p>
<h2>Inbox for <%= pso.getActorName() %></h2>
<table width="80%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="1%" valign="top">&nbsp;</td>
    <td width="34%" valign="top"><strong>Subject</strong></td>
    <td width="29%" valign="top"><strong>From</strong></td>
    <td width="33%" valign="top"><strong>Date</strong></td>
    <td width="3%" align="center" valign="top">*</td>
  </tr>
<%
	// Get email list
	for (ListIterator li = emailToList.listIterator(); li.hasNext();) {
		Email email = (Email) li.next();
		
		EmailRecipients er = 
		EmailRecipients.getEmailRecipientsLine(pso.schema, email.getId(), pso.getRunningSimId(), pso.getActorId());

		String boldStart = "";
		String boldEnd = "";
		
		if ((!er.isHasBeenRead())) {
			boldStart = "<strong>";
			boldEnd = "</strong>";
		}
		
%>
  <tr>
    <td valign="top">&nbsp;</td>
    <td valign="top"><%= boldStart %><a href="view_email.jsp?queue_up=true&comingfrom=em&email_id=<%= email.getId() %>"><%= email.getSubjectLine() %></a><%= boldEnd %></td>
    <td valign="top"><%= email.getFromActorName() %></td>
    <td valign="top"><%= boldStart %><a href="view_email.jsp?queue_up=true&comingfrom=em&email_id=<%= email.getId() %>"><%= email.getMsgDate() %></a></td>
    <td align="center" valign="top">&nbsp;</td>
  </tr>
<% } %>
</table>
<p>* 'Y' for sent as real world also. N for not sent as real world email. </p>
<hr>
<h2>Sent Messages From <%= pso.getActorName() %></h2>
<table width="80%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="1%" valign="top">&nbsp;</td>
    <td width="46%" valign="top"><strong>Subject</strong></td>
    <td width="25%" valign="top"><strong>To</strong></td>
    <td width="25%" valign="top"><strong>Date</strong></td>
	<td align="center" valign="top">*</td>
  </tr>
  <%
	// Get email list
	for (ListIterator li = sentList.listIterator(); li.hasNext();) {
		Email email = (Email) li.next();
     %>
  <tr>
    <td valign="top">&nbsp;</td>
    <td valign="top"><a href="view_email.jsp?queue_up=true&comingfrom=em&email_id=<%= email.getId() %>"><%= email.getSubjectLine() %></a></td>
    <td valign="top"><%= email.getToActors() %></td>
    <td valign="top"><a href="view_email.jsp?queue_up=true&comingfrom=em&email_id=<%= email.getId() %>"><%= email.getMsgDate() %></a></td>
	<td align="center">x</td>
  </tr>
  <% } %>
</table>
<p>* 'Y' for sent as real world also. N for not sent as real world email. </p>
<hr>
<h2>Draft Messages</h2>
<table width="80%" border="1" cellpadding="0" cellspacing="0">
  <tr>
    <td width="4%" valign="top">&nbsp;</td>
    <td width="46%" valign="top"><strong>Subject</strong></td>
    <td width="25%" valign="top"><strong>To</strong></td>
    <td width="25%" valign="top"><strong>Date</strong></td>
  </tr>
  <%
	// Get email list
	for (ListIterator li = draftList.listIterator(); li.hasNext();) {
		Email email = (Email) li.next();
     %>
  <tr>
    <td valign="top">&nbsp;</td>
    <td valign="top"><a href="write_email.jsp?queue_up=true&comingfrom=em&email_id=<%= email.getId() %>"><%= email.getSubjectLine() %></a></td>
    <td valign="top">&nbsp;</td>
    <td valign="top"><a href="write_email.jsp?queue_up=true&comingfrom=em&email_id=<%= email.getId() %>"><%= email.getMsgDate() %></a></td>
  </tr>
    <% } %>
</table>
<p>&nbsp;</p>
</body>
</html>
