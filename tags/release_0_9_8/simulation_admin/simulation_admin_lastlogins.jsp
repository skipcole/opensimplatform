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
	
%>
<html>
<head>
<title>Finger</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>

<h1>User's Last Login Times</h1>
<p>Click on the user's name to see a past history of their logins.</p>
<p>&nbsp;</p>

<blockquote>

<table width="80%" border="1">
<tr>
      
      <td width="25%"><strong>User </strong></td>
<td><strong>Logged in</strong></td>
</tr>
  <%
  
  		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy HH:mm a");
		
		ArrayList usersList = new ArrayList();
		
		 for (ListIterator li = User.getAll(afso.schema, true).listIterator(); li.hasNext();) {
			
			User user = (User) li.next();
			
			usersList.add(user);
			
		}
		
		Collections.sort(usersList);
		
  		for (ListIterator li = usersList.listIterator(); li.hasNext();) {
			
			User user = (User) li.next();
			
			String uName = "";
			String uTime = " Never ";
			Long uId = null;
			if (user != null) {
				uName = user.getBu_full_name();
				if (user.getLastLogin() != null) {
					uTime = sdf.format(user.getLastLogin());
				}
				uId = user.getId();
			}
		%>
  <tr>
    
    <td valign="top"><a href="simulation_admin_lastlogin_details.jsp?user_id=<%= uId %>"><%= uName %></a></td>
    <td valign="top"><%= uTime %></td>
  </tr>

  <%
	} // end of loop over actors.
%>
</table>
</blockquote>
<p>&nbsp;</p>
<p></p>
</body>
</html>