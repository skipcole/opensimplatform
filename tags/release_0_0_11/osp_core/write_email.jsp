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
	
	Email email = pso.handleEmailWrite(request);
	
	// mail has been sent. remove draft id, and return to email page.
	if (pso.forward_on){
		pso.forward_on = false;
		pso.draft_email_id = null;
		response.sendRedirect(pso.backPage);
		return;
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
<h2>Compose Email</h2>
<form name="form1" method="post" action="write_email.jsp">
  <input type="hidden" name="sending_page" value="writing_email" />
  <input type="hidden" name="draft_email_id" value="<%= pso.draft_email_id %>" />

<br>
<table width="360" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="top">Subject: </td>
    <td colspan="2" valign="top"><label>
<input name="email_subject" type="text" id="email_subject" size="60" value="<%= email.getSubjectLine() %>">
</label></td>
    </tr>
  <tr>
    <td width="6%" valign="top">To:</td>
    <td width="51%" valign="top">
          <select name="removed_email" id="removed_email">
        <%
  		for (ListIterator li =  pso.emailRecipients.listIterator(); li.hasNext();) {
			EmailRecipients er = (EmailRecipients) li.next();
			
			%>
        <option value="<%= er.getId() %>"><%= er.getActorName() %></option>
        <% 
			}  // end of loop over email recipients
		%>
      </select>

      <label>
      <input type="submit" name="remove_recipient" id="remove_recipient" value="Remove">
      </label>
      
      </td>
    <td width="43%" valign="top"><label>
      <select name="email_recipient" id="email_recipient">
        <%
  		for (ListIterator li =  pso.eligibleActors.listIterator(); li.hasNext();) {
			Actor act = (Actor) li.next();
			
			if (!(act.getId().equals(pso.actor_id))) {
			%>
        <option value="<%= act.getId() %>"><%= act.getName() %></option>
        <% 
			} // end of if this is not the same actor
			}  // end of loop over emails
		%>
      </select>
      <input type="submit" name="add_recipient" id="add_recipient" value="Add Selected">
    </label></td>
  </tr>
</table>
<br>
<p>
		  <textarea id="email_text" name="email_text" style="height: 240px; width: 360px;"><%= email.getMsgtext() %>
		  </textarea>
		<script language="javascript1.2">
			wysiwygWidth = 360;
			wysiwygHeight = 240;
  			generate_wysiwyg('email_text');
		</script>
		  </p>
<table width="360" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="50%"><input type="submit" name="email_save" id="email_save" value="Save Email"></td>
    <td width="50%"><div align="right">
      <label>
      <input type="submit" name="email_clear" id="email_clear" value="Clear">
      </label>
    </div></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td><input type="submit" name="email_send" value="Send Email"></td>
    <td><div align="right">
      <label>
      <input type="submit" name="email_delete_draft" id="email_delete_draft" value="Delete Draft"  onClick="return confirm('Are you sure you want to delete this draft?');">
      </label>
    </div></td>
  </tr>
</table>
<p>&nbsp;</p>
<p>
  <label></label>
</p>
<p>&nbsp;</p>
<p>
    <label></label>
  </p>
</form>
</body>
</html>

