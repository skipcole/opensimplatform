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

	
	Email email = new Email();
	
	String queue_up = request.getParameter("queue_up");
	
	String inbox_page = "email.jsp";
	
	if ((queue_up != null) && (queue_up.equalsIgnoreCase("true"))){
		
		String email_id = request.getParameter("email_id");
		email = Email.getById(pso.schema, new Long(email_id));
		
		String comingfrom = request.getParameter("comingfrom");
		
		if ((comingfrom != null) && (comingfrom.equalsIgnoreCase("emv"))){
			inbox_page = "email_master_view.jsp";
		}
	}
	
	EmailRecipients er = 
		EmailRecipients.getEmailRecipientsLine(pso.schema, email.getId(), pso.getRunningSimId(), pso.getActorId());
	
	System.out.println(pso.schema + ", " +  email.getId() + ", " +   pso.getRunningSimId() + ", " +   pso.getActorId());
	
	if (er != null) {
		er.setHasBeenRead(true);
		er.saveMe(pso.schema);
	} else {
		System.out.println("warning er is null");
	}
	
	
%>
<html>
<head>
<title>Announcements</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>
</head>

<body>
<h2>Email</h2>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="50%">From: <%= email.getFromActorName() %></td>
    <td width="50%">To: <%= email.getToActors() %></td>
  </tr>
  <% if (false) { %>
  <tr>
    <td>&nbsp;</td>
    <td>CC: </td>
  </tr>
  
  <tr>
    <td>&nbsp;</td>
    <td><span class="style1">You were bcc'ed.</span></td>
  </tr>
  <% } %>
  <tr>
    <td colspan="2">Subject: <%= email.getSubjectLine() %></td>
  </tr>
  <tr>
    <td colspan="2"><%= email.getMsgtext() %>
    <br></td>
  </tr>
  <tr>
    <td><form name="form1" method="post" action="write_email.jsp">
    <input type="hidden" name="reply_to" value="true">
    <input type="hidden" name="reply_id" value="<%= email.getId() %>">
    <input type="hidden" name="reply_to_actor_id" value="<%= email.getFromActor() %>">
      <label>
        <div align="center">
          <input type="submit" name="email_reply" id="email_reply" value="Reply">
        </div>
      </label>
    </form>    </td>
    <td><form name="form2" method="post" action="write_email.jsp">
        <input type="hidden" name="forward_to" value="true">
    <input type="hidden" name="forward_id" value="<%= email.getId() %>">
      <label>
        <div align="center">
          <input type="submit" name="email_forward" id="email_forward" value="Forward">
        </div>
      </label>
    </form>    </td>
  </tr>
</table>
<p><a href="<%= inbox_page %>">Back to Inbox</a></p>
</body>
</html>
