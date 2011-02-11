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
	
	String user_id = (String) request.getParameter("user_id");
	
	User user = new User();
	if (user_id != null) {
		user = User.getById(afso.schema, new Long(user_id));
	}
	
	
%>
<html>
<head>
<title>User Login History</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>

<h1>User Login History</h1>
<p>Click on the user's name to see a past history of their logins.</p>
<p>This Report is for <%= user.getUser_name() %>, <%= user.getBu_full_name() %>.</p>

<blockquote>

<table width="80%" border="1">
<tr>
      
      <td width="33%"><strong>Logged In</strong></td>
      <td width="33%"><strong>Session Ended</strong></td>
</tr>
  <%
  
  	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy HH:mm a");
  
  	List uLogins = UserTrail.getAllForUser(afso.schema, user.getId());
	
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
<p>&nbsp;</p>
</body>
</html>
<%
	
%>