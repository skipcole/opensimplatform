<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.networking.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));

	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	

	CustomizeableSection cs = TextPageCustomizer.getTextPageCS(request, pso);
	
%>
<html>
<head>
<title>USIP OSP Text Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<%= cs.getBigString() %>
</body>
</html>