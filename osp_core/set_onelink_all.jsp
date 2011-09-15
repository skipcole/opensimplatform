<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.specialfeatures.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.baseobjects.core.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
		
	
%>
<html>
<head>
<title>Edit Documents Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>

<h2>Select Any Fixed 'One Link' to Edit It
</h2>
<p><%
					
					List theOLs = OneLink.getAllBaseOneLinksForSim(pso.schema, pso.sim_id);
					//Collections.sort(theDocs);
					
					if ((theOLs != null) && (theOLs.size() > 0)) {
%>
<blockquote>
<table border="1" width="100%">
<%
					int ii = 0;
					for (ListIterator li = theOLs.listIterator(); li.hasNext();) {
						OneLink ol = (OneLink) li.next();
						ii += 1;
					%>

<tr>
<td><%= ii %>.</td><td><a href="onelink_set_page.jsp?sendingOLId=true&ol_id=<%= ol.getId() %>"><%= ol.getName() %></a></td><td><%= ol.getNotes() %></td></tr>
                    <% } // End of loop over one links. %>
</table>
</blockquote>
<% } // End of if there are one links. %>
            
</p>
<p>&nbsp;</p>
</body>
</html>
