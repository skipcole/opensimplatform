<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,
	java.util.*,
	java.text.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.coursemanagementinterface.*,
	org.hibernate.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = "invite_user_to_role.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	List uaList = new ArrayList();
	//String results = afso.handleBulkInvite(request);
	String ua_id = (String) request.getParameter("ua_id");
	
	if ((ua_id != null) && (ua_id.equalsIgnoreCase("all"))){
		uaList = UserAssignment.getAllForRunningSim(afso.schema, afso.getRunningSimId());
	} else if (ua_id != null) {   // have got sent in an id.
		UserAssignment ua = UserAssignment.getById(afso.schema, new Long(ua_id));
		uaList.add(ua);
	}


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
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
              <h1>Invite Players to Register <a href="helptext/bulk_invite_help.jsp" target="helpinright">(?)</a></h1>
              <br />
      <blockquote> 
        
        <p>Check the 'send' box for the players you want to send invitations to register</p>
        <p>
          <input type="hidden" name="sending_page" value="bulk_invite" />
          </p>
        <form action="bulk_invite.jsp" method="post" name="form1" id="form1">
          <table width="100%" border="0">
            <tr>
              <td><strong>Actor</strong></td>
              <td><strong>Username</strong></td>
              <td><strong>Player's Name </strong></td>
              <td><strong>Status</strong></td>
              <td><strong>Send</strong></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td><label>
                <input type="checkbox" name="checkbox" value="checkbox" />
              </label></td>
            </tr>
          </table>
          <p>&nbsp;    </p>
          <table width="100%" border="0" cellspacing="2" cellpadding="2">
        <tr valign="top">
          <td>Message Text: <a href="helptext/bulk_invite_message_text_help.jsp" target="helpinright">(?)</a></td>
                <td><textarea name="defaultInviteEmailMsg" cols="60" rows="5"><%= afso.getDefaultInviteMessage() %></textarea></td>
              </tr>
        <tr valign="top">
          <td>Look Up Code <a href="helptext/bulk_invite_help_lookupcode.jsp" target="helpinright">(?)</a>:</td>
          <td>
		  <%
		  
		  	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy hh:mm:ss a");
			sdf.setTimeZone(TimeZone.getDefault());
	
			String default_lookup_code = sdf.format(new java.util.Date());
		  
		  %>
		  <label>
            <input type="text" name="invitationCode" id="invitationCode" value="<%= default_lookup_code %>" />
          </label>
		  
		  </td>
        </tr>
        <tr valign="top"> 
          <td>&nbsp;</td>
                <td> <input type="submit" name="command" value="Send Invite Email" /> </td>
              </tr>
        </table>
    </form>
        <h2>Your Previous Invitations </h2>
        <%
			List uriList = UserRegistrationInvite.getAllSetsForUser(afso.schema, afso.user_id);
			
			if ((uriList == null) || (uriList.size() == 0)){
			%>
			<blockquote>
			None
			</blockquote>
			<%
			} else {
		
				for (ListIterator urli = uriList.listIterator(); urli.hasNext();) {
					String invitationSetName = (String) urli.next();  
					String encodedSetName = java.net.URLEncoder.encode(invitationSetName);
					%>
        			<p><a href="bulk_invite_show_users.jsp?invitationSetName=<%= encodedSetName %>"><%= invitationSetName %></a></p>
				<% } // End of loop over User registration Invites
			} // End of if there were some invites to display
			%>
      </blockquote>
      <blockquote>
        
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
