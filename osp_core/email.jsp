<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.communications.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	pso.backPage = "email.jsp";
	
	List emailToList = new ArrayList();
	List sentList = new ArrayList();
	List draftList = new ArrayList();
	
	if (!(pso.preview_mode)) {
		emailToList = Email.getAllTo(pso.schema, pso.running_sim_id, pso.actor_id);
		sentList = Email.getDraftsOrSent(pso.schema, pso.running_sim_id, pso.actor_id, true);
		draftList = Email.getDraftsOrSent(pso.schema, pso.running_sim_id, pso.actor_id, false);
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
<p align="center"><a href="write_email.jsp">Compose New Email</a> | <a href="email.jsp">Check for New Email</a></p>
<h2>Inbox for <%= pso.actor_name %></h2>
<table width="80%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="4%">&nbsp;</td>
    <td width="46%"><strong>Subject</strong></td>
    <td width="25%"><strong>From</strong></td>
    <td width="25%"><strong>Date</strong></td>
  </tr>
<%
	// Get email list
	for (ListIterator li = emailToList.listIterator(); li.hasNext();) {
		Email email = (Email) li.next();
		
		EmailRecipients er = 
		EmailRecipients.getEmailRecipientsLine(pso.schema, email.getId(), pso.running_sim_id, pso.actor_id);

		String boldStart = "";
		String boldEnd = "";
		
		if ((!er.isHasBeenRead())) {
			boldStart = "<strong>";
			boldEnd = "</strong>";
		}
		
%>
  <tr>
    <td>&nbsp;</td>
    <td><%= boldStart %><a href="view_email.jsp?queue_up=true&email_id=<%= email.getId() %>"><%= email.getSubjectLine() %></a><%= boldEnd %></td>
    <td><%= email.getFromActorName() %></td>
    <td><%= email.getMsgDate() %></td>
  </tr>
<% } %>
</table>
<hr>
<h2>Sent Messages From <%= pso.actor_name %></h2>
<table width="80%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="4%">&nbsp;</td>
    <td width="46%"><strong>Subject</strong></td>
    <td width="25%"><strong>To</strong></td>
    <td width="25%"><strong>Date</strong></td>
  </tr>
  <%
	// Get email list
	for (ListIterator li = sentList.listIterator(); li.hasNext();) {
		Email email = (Email) li.next();
     %>
  <tr>
    <td>&nbsp;</td>
    <td><a href="view_email.jsp?queue_up=true&email_id=<%= email.getId() %>"><%= email.getSubjectLine() %></a></td>
    <td><%= email.getToActors() %></td>
    <td><%= email.getMsgDate() %></td>
  </tr>
  <% } %>
</table>
<hr>
<h2>Draft Messages</h2>
<table width="80%" border="1" cellpadding="0" cellspacing="0">
  <tr>
    <td width="4%">&nbsp;</td>
    <td width="46%"><strong>Subject</strong></td>
    <td width="25%"><strong>To</strong></td>
    <td width="25%"><strong>Date</strong></td>
  </tr>
  <%
	// Get email list
	for (ListIterator li = draftList.listIterator(); li.hasNext();) {
		Email email = (Email) li.next();
     %>
  <tr>
    <td>&nbsp;</td>
    <td><a href="write_email.jsp?queue_up=true&email_id=<%= email.getId() %>"><%= email.getSubjectLine() %></a></td>
    <td>&nbsp;</td>
    <td><%= email.getMsgDate() %></td>
  </tr>
    <% } %>
</table>
<p>&nbsp;</p>
</body>
</html>
