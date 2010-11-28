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
		response.sendRedirect("../login.jsp");
		return;
	}
	
	pso.backPage = "email_master_view.jsp";
	
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
<h1>Email Chronology View</h1>
<h2>&nbsp;</h2>
<p>&nbsp;</p>
<%
			
			List emailToList = new ArrayList();
	
			if (!(pso.preview_mode)) {
				emailToList = Email.getAllForRunningSim(pso.schema, pso.getRunningSimId());
			}
				
%>	

<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
  	<td width="15%" valign="top"><strong>Date</strong></td>
    <td width="15%" valign="top"><strong>From</strong></td>
    <td width="20%" valign="top"><strong>Subject</strong></td>
    <td width="50%" valign="top"><strong>Message</strong></td>
  </tr>
<%
	// Get email list
	for (ListIterator li = emailToList.listIterator(); li.hasNext();) {
		Email email = (Email) li.next();

		String boldStart = "";
		String boldEnd = "";
		
%>
  <tr>
     <td valign="top"><a href="view_email.jsp?queue_up=true&comingfrom=emv&email_id=<%= email.getId() %>"><%= email.getMsgDate() %></a></td>
    <td valign="top"><%= email.getFromActorName() %></td>
    <td valign="top"><a href="view_email.jsp?queue_up=true&comingfrom=emv&email_id=<%= email.getId() %>"><%= email.getSubjectLine() %></a></td>
    <td valign="top"><%= email.getMsgtext() %></td>
  </tr>
<% } %>
</table>
<h2>&nbsp;</h2>
<p>&nbsp;</p>

<p>&nbsp;</p>
</body>
</html>