<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String user_id = (String) request.getParameter("user_id");
	
	User user = new User();
	if (user_id != null) {
		user = User.getById(pso.schema, new Long(user_id));
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
      <td width="33%"><strong>Actor Name</strong></td>
</tr>
  <%
  
  	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy HH:mm a");
  
  	List uLogins = UserTrail.getAllForUser(pso.schema, user.getId());
	
  	for (ListIterator li = uLogins.listIterator(); li.hasNext();) {
		UserTrail ut = (UserTrail) li.next();
		
		String aName = USIP_OSP_Cache.getActorName(pso.schema, pso.sim_id, pso.getRunningSimId(), request, ut.getActor_id());

			
		%>
  <tr>
    <td valign="top"><%= sdf.format(ut.getLoggedInDate()) %></td>
    <td valign="top"><%= sdf.format(ut.getEndSessionDate()) %></td>
    <td valign="top"><%= aName %></td>
  </tr>

  <%
	} // end of loop over actors.
%>
</table>
</blockquote>
<p>&nbsp;</p>
<p><a href="finger.jsp">Back</a></p>
</body>
</html>
<%
	
%>