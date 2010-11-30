<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.baseobjects.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));

	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
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
<title>USIP OSP Text Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<%= bodyText %>
</body>
</html>
