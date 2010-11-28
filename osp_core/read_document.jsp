<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);

	List setOfDocs = new ArrayList();
	
	if (!(pso.preview_mode)) {
		setOfDocs = SharedDocument.getSetOfDocsForSection(pso.schema, cs.getId(), pso.getRunningSimId());
	}


%>
<html>
<head>
<title>Read Document Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
