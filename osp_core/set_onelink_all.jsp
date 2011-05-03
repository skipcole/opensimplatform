<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*,
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
					
					List theDocs = SharedDocument.getAllDocumentsForRunningSim(pso.schema, pso.sim_id, pso.getRunningSimId());
					//Collections.sort(theDocs);
					for (ListIterator li = theDocs.listIterator(); li.hasNext();) {
						SharedDocument sd = (SharedDocument) li.next();
					%>
              <a href="write_document.jsp?sendingDocId=true&doc_id=<%= sd.getId() %>"><%= sd.getDisplayTitle() %></a><br />
                    <% } %>
            
</p>
<p>&nbsp;</p>
</body>
</html>
