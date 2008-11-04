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
	
	String imageName = "";
	String title = "";
	String page_description = "";
	String fullfilename = "";
	
	String cs_id = (String) request.getParameter("cs_id");
	
	MultiSchemaHibernateUtil.beginTransaction(pso.schema);

	CustomizeableSection cs = (CustomizeableSection) MultiSchemaHibernateUtil.getSession(pso.schema).get(CustomizeableSection.class, new Long(cs_id));
    
	imageName = (String) cs.getContents().get("image_file_name");
	fullfilename = "../simulation/images/" + imageName;

	title = (String) cs.getContents().get("page_title");
	page_description = cs.getDescription();
	MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
	
%>
<html>
<head>
<title>USIP OSCW Image Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<h1 align="left"><%= title %></h1>
<p><%= page_description %></p>
<p align="center"><img src="<%= fullfilename %>"></p>

</body>
</html>
