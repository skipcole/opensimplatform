<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.communications.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	String u_id = request.getParameter("u_id");
	
	Email email = afso.handleEmailUserPassword(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect("create_user_email_sent.jsp");
		return;
	}
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" width="100%" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Created User <%= afso.tempMsg %></h1>
			
              <p>You may now email them to let them know that they have been added to this system, and how they may login.</p>
              <form action="create_user_complete.jsp" method="post"  name="form1" id="form1">
              <table width="100%" border="1" cellspacing="0" cellpadding="2">
        <tr valign="top">
          <td><strong>Email From: </strong></td>
          <td><label>
            <input name="email_from" type="radio" value="noreply@opensimplatform.org" checked="checked" />
          noreply@opensimplatform.org
          </label><br />
		  <label>
		  <input name="email_from" type="radio" value="<%= afso.user_email %>" /><%= afso.user_email %> </label></td>
        </tr>
        <tr valign="top">
          <td><strong>Email To: </strong></td>
          <td><%= afso.tempMsg %><input type="hidden" name="email_to" value="<%= afso.tempMsg %>" /></td>
        </tr>
        <tr valign="top">
          <td><strong>Email Subject Line </strong></td>
          <td><label>
            <input type="text" name="email_subject" value="<%= email.getSubjectLine() %>" />
          </label></td>
        </tr>
        <tr valign="top"> 
          <td width="34%"><strong>Email text:<br /> 
              <br /> 
              </strong></td>
                <td width="66%">
                  <p>
                  <textarea name="email_text" cols="60" rows="5"><%= email.getMsgtext() %></textarea>
                  </p>                  </td>
              </tr>
        <tr valign="top">
          <td>&nbsp;</td>
          <td><label>
		  	<input type="hidden" name="u_id" value="<%= u_id %>" />
            <input type="submit" name="send_email" value="Send" />
          </label></td>
        </tr>
        </table>
		</form>
            </td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
	
	<p>&nbsp;</p>

<p align="center">Back to Assigning Users for this Running Simulation</p>

    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</form>

</body>
</html>