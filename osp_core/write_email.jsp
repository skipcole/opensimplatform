<%@ page 
	contentType="text/html; charset=utf-8" 
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
	
	String email_save = request.getParameter("email_save");
	
	System.out.println("EEEEEEEEEEEEEEEE:" + email_save);
	
	Email email = Emailer.handleEmailWrite(request, pso);
	
	if (pso.add_recipients){
		if (pso.add_type == PlayerSessionObject.ADD_TO){
			response.sendRedirect("email_recipients.jsp?add_to=to");
			return;
		} else if (pso.add_type == PlayerSessionObject.ADD_CC){
			response.sendRedirect("email_recipients.jsp?add_to=cc");
			return;
		} if (pso.add_type == PlayerSessionObject.ADD_BCC){
			response.sendRedirect("email_recipients.jsp?add_to=bcc");
			return;
		}
	}
	
	// mail has been sent. remove draft id, and return to email page.
	if (pso.forward_on){
		pso.forward_on = false;
		pso.draft_email_id = null;
		response.sendRedirect(pso.backPage);
		return;
	}
	
		String inbox_page = "email.jsp";	
		String comingfrom = request.getParameter("comingfrom");
		
		if ((comingfrom != null) && (comingfrom.equalsIgnoreCase("emv"))){
			inbox_page = "email_master_view.jsp";
		}
		
%>
<html>
<head>
<title>Write Email</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>

<body>
<table align="center" border="0">
<tr><td>

<% if (pso.errorMsg.equalsIgnoreCase("no recipients")) { 
	pso.errorMsg = "";
%>
<script type="text/javascript">
alert("You must add recipients to the 'To' line to send email.");
</script>
<% } %>

<h2>Compose Email</h2>
<form name="form1" method="post" action="write_email.jsp">
  <input type="hidden" name="sending_page" value="writing_email" />
  <input type="hidden" name="draft_email_id" value="<%= pso.draft_email_id %>" />

<br>
<table width="360" border="1" cellspacing="0" cellpadding="0">

  <tr>
    <td colspan="2" valign="top">
    <input type="image"  name="email_save" id="email_save" value="Save" src="images/save.png" alt="save">
    <input type="submit" name="email_save2"  value="Save Email2">
      <input type="submit" name="email_send" value="Send Email">
      <input type="submit" name="email_clear" id="email_clear" value="Clear">
      <input type="image" name="send" id="send" src="images/send_email.png"></td>
    </tr>
  <tr>
    <td valign="top"><input type="submit" name="add_to" id="add_to" value="TO:"></td>
    <td valign="top">&nbsp;
        <%
  		for (ListIterator li =  pso.emailRecipients.listIterator(); li.hasNext();) {
			EmailRecipients er = (EmailRecipients) li.next();
			
			%>
        <%= er.getActorName() %>;
        <% 
			}  // end of loop over email recipients
		%>
</td>
    </tr>
      <tr>
    <td valign="top"><input type="submit" name="add_cc" id="add_cc" value="CC:"></td>
    <td valign="top">&nbsp;
            <%
  		for (ListIterator li =  pso.emailRecipientsCC.listIterator(); li.hasNext();) {
			EmailRecipients er = (EmailRecipients) li.next();
			
			%>
        <%= er.getActorName() %>;
        <% 
			}  // end of loop over email recipients
		%>
    
    </td>
  </tr>
      <tr>
        <td valign="top"><input type="submit" name="add_bcc" id="add_bcc" value="BCC:"></td>
        <td valign="top">&nbsp;
                <%
  		for (ListIterator li =  pso.emailRecipientsBCC.listIterator(); li.hasNext();) {
			EmailRecipients er = (EmailRecipients) li.next();
			
			%>
        <%= er.getActorName() %>;
        <% 
			}  // end of loop over email recipients
		%>
        </td>
      </tr>
      <tr>
    <td valign="top">Subject: </td>
    <td valign="top"><label>
<input name="email_subject" type="text" id="email_subject" size="60" value="<%= email.getSubjectLine() %>">
</label></td>
    </tr>
</table>
<br>
<p>
		  <textarea id="email_text" name="email_text" style="height: 120px; width: 410px;"><%= email.getMsgtext() %>
		  </textarea>
		<script language="javascript1.2">
			wysiwygWidth = 410;
			wysiwygHeight = 120;
  			generate_wysiwyg('email_text');
		</script>
  </p>
<p>
 
</p>
<p><a href="<%= inbox_page %>">Back to Inbox</a></p>

<p>&nbsp;</p>
<p>
    <label></label>
  </p>
</form>
</td></tr></table>
</body>
</html>

