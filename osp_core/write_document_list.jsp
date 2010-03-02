<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.baseobjects.core.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
		
	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getMe(pso.schema, cs_id);
	
	ChatHelpCustomizer chc = new ChatHelpCustomizer(request, pso, cs);
	
%>
<html>
<head>
<title>Edit Documents Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>

            <p><%
					for (ListIterator li = chc.docs.listIterator(); li.hasNext();) {
						Long this_id = (Long) li.next();
						SharedDocument sd = SharedDocument.getMe(pso.schema, this_id);
					%>
                    <%= sd.getDisplayTitle() %> (<%= sd.getUniqueDocTitle() %>)<br />
                    <% } %>
            
            </p>
<p>&nbsp;</p>
</body>
</html>
