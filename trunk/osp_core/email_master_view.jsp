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
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
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
<p align="center"><a href="write_email.jsp">Compose New Email</a> | <a href="email.jsp">Check for New Email</a></p>
<%
  		for (ListIterator lia = simulation.getActors(pso.schema).listIterator(); lia.hasNext();) {
			Actor act = (Actor) lia.next();			
%>	
<h2>Inbox for <%= act.getName() %></h2>
<table width="80%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="4%">&nbsp;</td>
    <td width="46%"><strong>Subject</strong></td>
    <td width="25%"><strong>From</strong></td>
    <td width="25%"><strong>Date</strong></td>
  </tr>
<%
	// Get email list
	for (ListIterator li = Email.getAllTo(pso.schema, pso.running_sim_id, act.getId()).listIterator(); li.hasNext();) {
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
    <td>&nbsp;</td>
    <td><%= boldStart %><a href="view_email.jsp?queue_up=true&email_id=<%= email.getId() %>"><%= email.getSubjectLine() %></a><%= boldEnd %></td>
    <td><%= email.getFromActorName() %></td>
    <td><%= email.getMsgDate() %></td>
  </tr>
<% } %>
</table>
<h2>Sent Messages From <%= act.getName() %></h2>
<table width="80%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="4%">&nbsp;</td>
    <td width="46%"><strong>Subject</strong></td>
    <td width="25%"><strong>To</strong></td>
    <td width="25%"><strong>Date</strong></td>
  </tr>
  <%
	// Get email list
	for (ListIterator li = Email.getDraftsOrSent(pso.schema, pso.running_sim_id, act.getId(), true).listIterator(); li.hasNext();) {
		Email email = (Email) li.next();
     %>
  <tr>
    <td>&nbsp;</td>
    <td><a href="view_email.jsp?queue_up=true&email_id=<%= email.getId() %>"><%= email.getSubjectLine() %></a></td>
    <td>&nbsp;</td>
    <td><%= email.getMsgDate() %></td>
  </tr>
  <% } %>
</table>
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
	for (ListIterator li = Email.getDraftsOrSent(pso.schema, pso.running_sim_id, act.getId(), false).listIterator(); li.hasNext();) {
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
<% }  // End of loop over all actors %>

<p>&nbsp;</p>
</body>
</html>
<%
	
%>
