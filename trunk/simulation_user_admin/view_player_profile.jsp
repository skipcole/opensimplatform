<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	String student_id = (String) request.getParameter("student_id");
	User userOnScratchPad = new User();
	BaseUser bu = new BaseUser();
	
	if (student_id != null) {
		userOnScratchPad = User.getById(afso.schema, new Long (student_id));
		bu = BaseUser.getByUserId(userOnScratchPad.getId());
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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
              <h1>Player Profile </h1>
              <br />
  <table border="0" cellspacing="2" cellpadding="1">
    <tr>
      <td valign="top"><strong>First Name <a href="helptext/first_name.jsp" target="helpinright">(?)</a>:</strong></td>
      <td valign="top">
        <%= userOnScratchPad.getBu_first_name() %></td>
    </tr>
    <tr>
      <td valign="top"><strong>Middle Name <a href="helptext/middle_name.jsp" target="helpinright">(?)</a>:</strong></td>
      <td valign="top">
        <%= userOnScratchPad.getBu_middle_name() %></td>
    </tr>
    <tr>
      <td valign="top"><strong>Last Name <a href="helptext/last_name.jsp" target="helpinright">(?)</a>:</strong></td>
      <td valign="top">
        <%= userOnScratchPad.getBu_last_name() %></td>
    </tr>
    <tr>
      <td valign="top"><strong>Authorization Level:</strong></td>
      <td valign="top">
      		<% if (userOnScratchPad.isAdmin()) { %>Administrator, <% } %>
	  		<% if (userOnScratchPad.isSim_author()) { %>Simulation Author, <% } %> 
			<% if (userOnScratchPad.isSim_instructor()) { %>Simulation Facilitator, <% } %>
Player      </td>
    </tr>
    <tr>
      <td valign="top"><strong>Email Address:</strong></td>
      <td valign="top"><%= userOnScratchPad.getUser_name() %><input type="hidden" name="email" value="<%= userOnScratchPad.getUser_name() %>" /> </td>
    </tr>
    <tr>
      <td valign="top"><strong>Language:</strong></td>
      <td valign="top">English</td>
    </tr>
        <tr>
          <td valign="top"><strong>Time Zone: </strong></td>
          <td valign="top">N/A</td>
        </tr>
        <tr>
          <td valign="top"><strong>Phone Number: </strong></td>
          <td valign="top"><%= userOnScratchPad.getPhoneNumber() %></td>
        </tr>
		<%
			if (bu.isTempPassword()){
		%>
        <tr>
      <td valign="top">&nbsp;</td>
      <td valign="top"><label>
      <a href="email_user_temp_password.jsp?u_id=<%= userOnScratchPad.getId() %>">Email them temporary password. </a></label></td>
    </tr>
		<%
			}
		%>
  </table>
        </form>      
      <p>This Report is for <%= userOnScratchPad.getUser_name() %>, <%= userOnScratchPad.getBu_full_name() %>.</p>			
      <blockquote>
        <table width="80%" border="1">
          <tr>
            <td width="33%"><strong>Logged In</strong></td>
            <td width="33%"><strong>Session Ended</strong></td>
          </tr>
          <%
  
  	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy HH:mm a");
  
  	List uLogins = UserTrail.getAllForUser(afso.schema, userOnScratchPad.getId());
	
  	for (ListIterator li = uLogins.listIterator(); li.hasNext();) {
		UserTrail ut = (UserTrail) li.next();
			
		%>
          <tr>
            <td valign="top"><%= sdf.format(ut.getLoggedInDate()) %></td>
            <td valign="top"><%= sdf.format(ut.getEndSessionDate()) %></td>
          </tr>
          <%
	} // end of loop over actors.
%>
        </table>
      </blockquote>
      <p></p></td>
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