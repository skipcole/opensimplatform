<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true), true);
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getMe(pso.schema, cs_id);
	
	RunningSimulation rs = pso.giveMeRunningSim();
	
	System.out.println("blah: " + pso.schema +  " " + cs.getId() + " " + rs.getId());

	List setOfDocs = SharedDocument.getSetOfDocsForSection(pso.schema, cs.getId(), rs.getId());


%>
<html>
<head>
<title>Read Document Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>

<body>
<p><%= cs.getBigString() %></p>

<%		
		for (ListIterator<SharedDocument> li = setOfDocs.listIterator(); li.hasNext();) {

			SharedDocument sd = (SharedDocument) li.next();
			
%>
<hr>
	<h1><%= sd.getDisplayTitle() %></h1>
	<%= sd.getBigString() %>
<p>&nbsp;</p>
<%
	}
%>
</body>
</html>
<%
	
%>
