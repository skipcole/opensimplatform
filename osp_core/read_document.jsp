<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
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
	
	Long base_doc_id = (Long) cs.getContents().get("doc_id");
	
	MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
	
	RunningSimulation rs = pso.giveMeRunningSim();
	
	System.out.println("blah: " + pso.schema +  " " + cs.getId() + " " + rs.getId());
	
	SharedDocument sd = SharedDocument.getDocumentByBaseId(pso.schema, base_doc_id, rs.getId());
	
	String sending_page = (String) request.getParameter("sending_page");
	String update_text = (String) request.getParameter("update_text");
	
	

%>
<html>
<head>
<title>Read Document Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>

<body>
<p><%= bodyText %></p>
<hr>  
		  <%= sd.getBigString() %>

<p>&nbsp;</p>
</body>
</html>
<%
	
%>
