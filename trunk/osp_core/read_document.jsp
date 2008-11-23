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
	
	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getMe(pso.schema, cs_id);
	
	String base_doc_ids = (String) cs.getContents().get(SharedDocument.DOCS_IN_HASHTABLE_KEY);
	
	RunningSimulation rs = pso.giveMeRunningSim();
	
	System.out.println("blah: " + pso.schema +  " " + cs.getId() + " " + rs.getId());

	Vector setOfDocs = new Vector();
	
	StringTokenizer str = new StringTokenizer(base_doc_ids, ",");
	
	while(str.hasMoreTokens()){
		String nextToken = str.nextToken();
		nextToken = nextToken.trim();
		System.out.println("found token for: " + nextToken);
		
		SharedDocument sd = SharedDocument.getDocumentByBaseId(pso.schema, new Long(nextToken), rs.getId());
		setOfDocs.add(sd);
	}


%>
<html>
<head>
<title>Read Document Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>

<body>
<p><%= cs.getBigString() %></p>

<%
		for (Enumeration e = setOfDocs.elements(); e.hasMoreElements();){
			SharedDocument sd = (SharedDocument) e.nextElement();
			
%>
<hr>
	<%= sd.getDisplayTitle() %>
	<%= sd.getBigString() %>
<p>&nbsp;</p>
<%
	}
%>
</body>
</html>
<%
	
%>
