<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.baseobjects.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);

	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String bodyText = "";
	
	String cs_id = (String) request.getParameter("cs_id");
	
	MultiSchemaHibernateUtil.beginTransaction(pso.schema);

	CustomizeableSection cs = (CustomizeableSection) MultiSchemaHibernateUtil.getSession(pso.schema).get(CustomizeableSection.class, new Long(cs_id));
    
	bodyText = (String) cs.getBigString();
	MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
	
%>
<html>
<head>
<title>USIP OSCW Text Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<%= bodyText %>
</body>
</html>