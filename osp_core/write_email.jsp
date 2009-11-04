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
	
	List eligibleActors = new ArrayList();
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
		eligibleActors = simulation.getActors(pso.schema);
	}
	
	Email email = new Email();
	List emailRecipients = new ArrayList();
	
	if (pso.draft_email_id != null){
		email = Email.getMe(pso.schema, pso.draft_email_id);
		emailRecipients = Email.getRecipientsOfAnEmail(pso.schema, pso.draft_email_id);
	}
	
	email.setSubjectLine(USIP_OSP_Util.cleanNulls(request.getParameter("email_subject")));
	email.setMsgtext(USIP_OSP_Util.cleanNulls(request.getParameter("email_text")));
	
	String sending_page = request.getParameter("sending_page");
	
	String debug = "";
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("writing_email"))){
	
		email.saveMe(pso.schema);
		pso.draft_email_id = email.getId();
			
		String add_recipient = request.getParameter("add_recipient");
		if (add_recipient != null) {
			debug = request.getParameter("email_recipient");
			EmailRecipients er = 
				new EmailRecipients(pso.schema, pso.draft_email_id, pso.running_sim_id, pso.sim_id, 
				new Long(debug), pso.getActorName(pso.schema, debug), EmailRecipients.RECIPIENT_TO);
		}
		
		
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
  


<br>
<table width="80%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="top">Subject: </td>
    <td colspan="2" valign="top"><label>
<input name="email_subject" type="text" id="email_subject" size="60" value="<%= email.getSubjectLine() %>">
</label></td>
    </tr>
  <tr>
    <td width="6%" valign="top">To:</td>
    <td width="51%" valign="top">
    <%
  		for (ListIterator li =  emailRecipients.listIterator(); li.hasNext();) {
			EmailRecipients er = (EmailRecipients) li.next();
			%>
    <%= er.getActorName() %>
      <label>
      <input type="submit" name="remove_recipient" id="remove_recipient" value="Remove">
      </label><br />
    	<% } %>  
      
      </td>
    <td width="43%" valign="top"><label>
      <select name="email_recipient" id="email_recipient">
        <%
  		for (ListIterator li =  eligibleActors.listIterator(); li.hasNext();) {
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
		  <textarea id="email_text" name="email_text" style="height: 310px; width: 710px;"><%= email.getMsgtext() %>
		  </textarea>
		<script language="javascript1.2">
  			generate_wysiwyg('email_text');
		</script>
		  </p>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="50%"><input type="submit" name="email_save" id="email_save" value="Save Email"></td>
    <td width="50%"><div align="right">
      <label>
      <input type="submit" name="email_clear" id="email_clear" value="Clear">
      </label>
    </div></td>
  </tr>
  <tr>
    <td><input type="submit" name="update_text" value="Send Email"></td>
    <td><div align="right">
      <label>
      <input type="submit" name="email_delete_draft" id="email_delete_draft" value="Delete Draft">
      </label>
    </div></td>
  </tr>
</table>
<p>&nbsp;</p>
<p>
  <label></label>
</p>
<p>debug is: <%= debug %></p>
<p>&nbsp;</p>
<p>
    <label></label>
  </p>
</form>
</body>
</html>

