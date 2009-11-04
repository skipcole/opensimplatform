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
	
	Email email = new Email();
	if (pso.draft_email_id != null){
		email = Email.getMe(pso.schema, pso.draft_email_id);
	}
	
	email.setSubjectLine(USIP_OSP_Util.cleanNulls(request.getParameter("email_subject"));
	email.setMsgtext(USIP_OSP_Util.cleanNulls(request.getParameter("email_text"));
	
	String email_subject = request.getParameter("email_subject");
	String sending_page = request.getParameter("sending_page");
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("writing_email"))){
		
		String number_of_recipients = request.getParameter("number_of_recipients");
		
		String add_recipient = request.getParameter("add_recipient");
		if (add_recipient != null) {
		
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
<meta http-equiv="refresh" content="20" />
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>

<body>
<h2>Compose Email</h2>
<form name="form1" method="post" action="write_email.jsp">
  <input type="hidden" name="sending_page" value="write_email" />
  


<br>
<table width="80%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td>Subject: </td>
    <td colspan="2"><label>
<input name="email_subject" type="text" id="email_subject" size="60" value="<%= email.getSubjectLine() %>">
</label></td>
    </tr>
  <tr>
    <td width="6%">To:</td>
    <td width="51%">John Doe 
      <label>
      <input type="submit" name="remove_recipient" id="remove_recipient" value="Remove">
      </label></td>
    <td width="43%"><label>
      <select name="emal_recipient" id="emal_recipient">
        <%
  		for (ListIterator li = simulation.getActors(pso.schema).listIterator(); li.hasNext();) {
			Actor aa = (Actor) li.next();
			
			if (!(aa.getId().equals(pso.actor_id))) {
			%>
        <option value="<%= aa.getId() %>"><%= aa.getName() %></option>
        <% 
			} 
			}
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
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>
    <label></label>
  </p>
</form>
</body>
</html>

