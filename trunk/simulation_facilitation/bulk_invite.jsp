<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,
	org.hibernate.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = "bulk_invite.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String results = afso.handleBulkInvite(request);
	
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Invite Players to Register </h1>
              <br />
      <blockquote> 
        
        <p>Please enter a set of emails below, separated by spaces, commas or carriage returns. Then modify the message text as you see fit, and hit send. The users will 
          receive an email inviting them to autoregister on the system. You will 
          then be able to add them as players in one of your simulations.</p>
        <form action="bulk_invite.jsp" method="post" name="form1" id="form1">
      <input type="hidden" name="sending_page" value="bulk_invite" />
      <table width="100%" border="0" cellspacing="2" cellpadding="2">
        <tr valign="top"> 
          <td width="34%">Email Addresses: <a href="helptext/bulk_invite_help.jsp" target="helpinright">(?)</a> <br />
             <br /> </td>
                <td width="66%"><br /> <p> 
                  <textarea name="setOfUsers" cols="60" rows="5"><%= afso.setOfUsers %></textarea>
                  </p>
                  <p>&nbsp;</p></td>
              </tr>
        <tr valign="top">
          <td>Message Text: <a href="helptext/bulk_invite_message_text_help.jsp" target="helpinright">(?)</a></td>
                <td><textarea name="defaultInviteEmailMsg" cols="60" rows="5"><%= afso.getDefaultInviteMessage() %></textarea></td>
              </tr>
        <tr valign="top">
          <td>Look Up Code <a href="helptext/bulk_invite_help_lookupcode.jsp" target="helpinright">(?)</a>:</td>
          <td><label>
            <input type="text" name="invitationCode" id="invitationCode" />
          </label></td>
        </tr>
        <tr valign="top"> 
          <td>&nbsp;</td>
                <td> <input type="submit" name="command" value="Send Invite Email" /> </td>
              </tr>
        </table>
    </form>
    <p align="center"><%= results %></p>
    <p><em><strong>Functionality to Come</strong></em>: Click here to see your previous bulk invitations.</p>
    <p>&nbsp;</p>
      </blockquote>
      <blockquote>
        <div align="center">
          <p><a href="create_user.jsp">Next 
            Step: Create Users </a></p>
          <p align="left"><a href="create_schedule_page.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a></p>
        </div>
        </blockquote>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
<%
	
%>
