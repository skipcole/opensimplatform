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
	
	pso.backPage = "email_master_view.jsp";
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
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
<title>OSP Email Master View</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<meta http-equiv="refresh" content="120" />
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<h1>Email Master View</h1>
<p align="center"><a href="write_email.jsp?comingfrom=emv">Compose New Email</a> | <a href="email_master_view.jsp">Check for New Email</a></p>

<h2>Inbox for <%= pso.actor_name %></h2>
<table width="80%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="4%" valign="top">&nbsp;</td>
    <td width="46%" valign="top"><strong>Subject</strong></td>
    <td width="25%" valign="top"><strong>From</strong></td>
    <td width="25%" valign="top"><strong>Date</strong></td>
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
    <td valign="top">&nbsp;</td>
    <td valign="top"><%= boldStart %><a href="view_email.jsp?queue_up=true&comingfrom=emv&email_id=<%= email.getId() %>"><%= email.getSubjectLine() %></a><%= boldEnd %></td>
    <td valign="top"><%= email.getFromActorName() %></td>
    <td valign="top"><%= boldStart %><a href="view_email.jsp?queue_up=true&comingfrom=emv&email_id=<%= email.getId() %>"><%= email.getMsgDate() %></a></td>
  </tr>
<% } %>
</table>
<p>&nbsp;</p>
<h2>Sent Messages From <%= pso.actor_name %></h2>
<table width="80%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="4%" valign="top">&nbsp;</td>
    <td width="46%" valign="top"><strong>Subject</strong></td>
    <td width="25%" valign="top"><strong>To</strong></td>
    <td width="25%" valign="top"><strong>Date</strong></td>
  </tr>
  <%
	// Get email list
	for (ListIterator li = sentList.listIterator(); li.hasNext();) {
		Email email = (Email) li.next();
     %>
  <tr>
    <td valign="top">&nbsp;</td>
    <td valign="top"><a href="view_email.jsp?queue_up=true&comingfrom=emv&email_id=<%= email.getId() %>"><%= email.getSubjectLine() %></a></td>
    <td valign="top"><%= email.getToActors() %></td>
    <td valign="top"><a href="view_email.jsp?queue_up=true&comingfrom=emv&email_id=<%= email.getId() %>"><%= email.getMsgDate() %></a></td>
  </tr>
  <% } %>
</table>
<p>&nbsp;</p>
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
    <td valign="top"><a href="write_email.jsp?queue_up=true&comingfrom=emv&email_id=<%= email.getId() %>"><%= email.getSubjectLine() %></a></td>
    <td valign="top">&nbsp;</td>
    <td valign="top"><a href="write_email.jsp?queue_up=true&comingfrom=emv&email_id=<%= email.getId() %>"><%= email.getMsgDate() %></a></td>
  </tr>
    <% } %>
</table>
<p>&nbsp;</p>
<hr>
<h1>Other Actor's Mail Boxes</h1>

<%
  		for (ListIterator lia = simulation.getActors(pso.schema).listIterator(); lia.hasNext();) {
			Actor act = (Actor) lia.next();		
			
			if (!(act.getId().equals(pso.actor_id))) {
			
			emailToList = new ArrayList();
			sentList = new ArrayList();
			draftList = new ArrayList();
	
			if (!(pso.preview_mode)) {
				emailToList = Email.getAllTo(pso.schema, pso.running_sim_id, act.getId());
				sentList = Email.getDraftsOrSent(pso.schema, pso.running_sim_id, act.getId(), true);
				draftList = Email.getDraftsOrSent(pso.schema, pso.running_sim_id, pso.actor_id, false);
			}
				
%>	


<h2>Inbox for <%= act.getActorName(pso.schema, pso.running_sim_id, request) %></h2>
<table width="80%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="4%" valign="top">&nbsp;</td>
    <td width="46%" valign="top"><strong>Subject</strong></td>
    <td width="25%" valign="top"><strong>From</strong></td>
    <td width="25%" valign="top"><strong>Date</strong></td>
  </tr>
<%
	// Get email list
	for (ListIterator li = emailToList.listIterator(); li.hasNext();) {
		Email email = (Email) li.next();
		
		EmailRecipients er = 
		EmailRecipients.getEmailRecipientsLine(pso.schema, email.getId(), pso.running_sim_id, act.getId());

		String boldStart = "";
		String boldEnd = "";
		
		if ((!er.isHasBeenRead())) {
			boldStart = "<strong>";
			boldEnd = "</strong>";
		}
		
%>
  <tr>
    <td valign="top">&nbsp;</td>
    <td valign="top"><%= boldStart %><a href="view_email.jsp?queue_up=true&comingfrom=emv&email_id=<%= email.getId() %>"><%= email.getSubjectLine() %></a><%= boldEnd %></td>
    <td valign="top"><%= email.getFromActorName() %></td>
    <td valign="top"><%= boldStart %><a href="view_email.jsp?queue_up=true&comingfrom=emv&email_id=<%= email.getId() %>"><%= email.getMsgDate() %></a></td>
  </tr>
<% } %>
</table>
<h2>Sent Messages From <%= act.getActorName(pso.schema, pso.running_sim_id, request) %></h2>
<table width="80%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="4%" valign="top">&nbsp;</td>
    <td width="46%" valign="top"><strong>Subject</strong></td>
    <td width="25%" valign="top"><strong>To</strong></td>
    <td width="25%" valign="top"><strong>Date</strong></td>
  </tr>
  <%
	// Get email list
	for (ListIterator li = sentList.listIterator(); li.hasNext();) {
		Email email = (Email) li.next();
     %>
  <tr>
    <td valign="top">&nbsp;</td>
    <td valign="top"><a href="view_email.jsp?queue_up=true&comingfrom=emv&email_id=<%= email.getId() %>"><%= email.getSubjectLine() %></a></td>
    <td valign="top"><%= email.getToActors() %></td>
    <td valign="top"><a href="view_email.jsp?queue_up=true&comingfrom=emv&email_id=<%= email.getId() %>"><%= email.getMsgDate() %></a></td>
  </tr>
  <% } %>
</table>
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
    <td valign="top"><a href="write_email.jsp?queue_up=true&comingfrom=emv&email_id=<%= email.getId() %>"><%= email.getSubjectLine() %></a></td>
    <td valign="top"><%= email.getToActors() %></td>
    <td valign="top"><a href="write_email.jsp?queue_up=true&comingfrom=emv&email_id=<%= email.getId() %>"><%= email.getMsgDate() %></a></td>
  </tr>
    <% } %>
</table>
<hr>
<p>&nbsp;</p>

<% 
		} // End of if this is not the same actor as is logged in.
	}  // End of loop over all actors %>

<p>&nbsp;</p>
</body>
</html>