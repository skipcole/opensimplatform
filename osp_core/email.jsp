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
	
	pso.setEmailSection(request);
	
	List emailToList = new ArrayList();
	List sentList = new ArrayList();
	List draftList = new ArrayList();
	
	if (!(pso.preview_mode)) {
		
		if (pso.getEmailSection() == pso.EMAIL_SENT) {
		
		sentList = Email.getDraftsOrSent(pso.schema, pso.getRunningSimId(), pso.getActorId(), true);
		
		} else if (pso.getEmailSection() == pso.EMAIL_DRAFTS) {
			
		draftList = Email.getDraftsOrSent(pso.schema, pso.getRunningSimId(), pso.getActorId(), false);
		
		} else {
			emailToList = Email.getAllTo(pso.schema, pso.getRunningSimId(), pso.getActorId());
		}
	} // End of if in Preview Mode.
		
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
<table  border="0" cellspacing="0" cellpadding="4">
  <tr>
    <td align="center"><a href="write_email.jsp"><img src="images/new_email.png" width="32" height="46" alt="new email" border="0"><br>
      New <br>
      Email</a></td>
    <td align="center"><a href="email.jsp"><img src="images/check_email.png" width="32" height="46" alt="check email" border="0"><br>
      Check <br>
      Email</a></td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="2">
  
  <%
		if (pso.getEmailSection() == pso.EMAIL_SENT) {
		
%>
  
    <tr id="sent_mail_row">
    <td align="left" valign="top" bgcolor="#FFFFFF"><p><a href="email.jsp?email_section=0"><img src="images/folder.png" width="22" height="17" alt="folder" border="0">Inbox</a></p>
    <p><strong><img src="images/folder.png" width="22" height="17" alt="folder" border="0">Sent</strong></p>
    <p><a href="email.jsp?email_section=2"><img src="images/folder.png" width="22" height="17" alt="folder" border="0">Drafts</a></p></td>
    <td width="90%" align="left" valign="top">
      <table width="80%" border="1" cellspacing="0" cellpadding="0">
        <tr>
          <td width="1%" valign="top">&nbsp;</td>
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
          <td valign="top"><a href="view_email.jsp?queue_up=true&comingfrom=em&email_id=<%= email.getId() %>"><%= email.getSubjectLine() %></a></td>
          <td valign="top"><%= email.getToActors() %></td>
          <td valign="top"><a href="view_email.jsp?queue_up=true&comingfrom=em&email_id=<%= email.getId() %>"><%= email.getSendDate() %></a></td>
        </tr>
        <% } %>
      </table>
      <h2>Sent Messages From <%= pso.getActorName() %></h2></td>
  </tr>
<% } else if (pso.getEmailSection() == pso.EMAIL_DRAFTS) {  %>
     <tr>
    <td align="left" valign="top" bgcolor="#FFFFFF"><p><a href="email.jsp?email_section=0" border="0"><img src="images/folder.png" width="22" height="17" alt="folder">Inbox</a></p>
    <p><a href="email.jsp?email_section=1"><img src="images/folder.png" width="22" height="17" alt="folder" border="0">Sent</a></p>
    <p><img src="images/folder.png" width="22" height="17" alt="folder"><strong>Drafts</strong></p></td>
    <td width="90%" align="left" valign="top">
      <table width="80%" border="1" cellpadding="0" cellspacing="0">
        <tr>
          <td width="4%" valign="top">&nbsp;</td>
          <td width="46%" valign="top"><strong>Subject</strong></td>
          <td width="25%" valign="top"><strong>To</strong></td>
          <td width="25%" valign="top"><strong>Date</strong></td>
          <td width="25%" valign="top">&nbsp;</td>
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
          <td valign="top"><input type="submit" name="email_delete_draft" id="email_delete_draft" value="Delete Draft"  onClick="return confirm('Are you sure you want to delete this draft?');"></td>
        </tr>
        <% } %>
      </table>
      <h2>Draft Messages</h2></td>
  </tr>
 
<% } else { %>
  <tr>
    <td align="left" valign="top" bgcolor="#FFFFFF" bordercolor="#000000" ><p><img src="images/folder.png" width="22" height="17" alt="folder"><strong>Inbox</strong></p>
    <p><a href="email.jsp?email_section=1"><img src="images/folder.png" width="22" height="17" alt="folder" border="0">Sent</a></p>
    <p><a href="email.jsp?email_section=2"><img src="images/folder.png" width="22" height="17" alt="folder" border="0">Drafts</a></p></td>
    <td width="90%" align="left" valign="top">
      <table width="80%" border="0" cellspacing="0" cellpadding="0">
      
      
        <tr>
          <td width="1%" valign="top">&nbsp;</td>
          <td width="59%" valign="top"><strong>Subject</strong></td>
          <td width="20%" valign="top"><strong>From</strong></td>
          <td width="20%" valign="top"><strong>Date</strong></td>
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
          <td valign="top"><%= boldStart %><a href="view_email.jsp?queue_up=true&comingfrom=em&email_id=<%= email.getId() %>"><%= email.getSendDate() %></a></td>
        </tr>
        <% } %>
      </table>
    <h2>Inbox for <%= pso.getActorName() %></h2></td>
  </tr>

<% } %>
 
  
</table>
<p>&nbsp;</p>
</body>
</html>
