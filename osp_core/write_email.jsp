<%@ page 
	contentType="text/html; charset=iso-8859-1" 
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
	
	String email_text = request.getParameter("email_text");
	
	String sending_page = request.getParameter("sending_page");
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("writing_email"))){
		Emails email = new Emails();
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
</head>

<body>
<h2>Compose Email</h2>
<form name="form1" method="post" action="write_email.jsp">
  <input type="hidden" name="sending_page" value="write_email" />
  
Subject: 
<label>
<input name="email_subject" type="text" id="email_subject" size="60">
</label>
<br>
<table width="80%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="6%">To:</td>
    <td width="51%">John Doe 
      <label>
      <input type="submit" name="remove_recipient" id="remove_recipient" value="Remove">
      </label></td>
    <td width="43%"><label>
      <select name="emal_recipient" id="emal_recipient">
        <option value="a">a</option>
      </select>
      <input type="submit" name="add_recipient" id="add_recipient" value="Add Selected">
    </label></td>
  </tr>
</table>
<br>
<p>
		  <textarea id="email_text" name="email_text" style="height: 310px; width: 710px;"><%= email_text %>
		  </textarea>
		<script language="javascript1.2">
  			generate_wysiwyg('email_text');
		</script>
		  </p>
<p>
  <input type="submit" name="update_text" value="Send Email">
</p>
<p>&nbsp;</p>
<p>
    <label></label>
  </p>
</form>
</body>
</html>

