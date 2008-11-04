<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}	
	
	String lineInfo = (String) request.getParameter("lineInfo");
	
	String sending_page = (String) request.getParameter("sending_page");
	
	String debug = "debug: ";
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("shared_document"))){
		String docTextArea = (String) request.getParameter("docTextArea");
		
		debug += SharedDocument.updateText(pso.simulation.db_tablename, lineInfo, docTextArea);
	}
	
	SharedDocument sd = SharedDocument.pullFromSimulationTable(lineInfo,pso.simulation.db_tablename);

%>
<html>
<head>
<title>Shared Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<p><%= debug %></p>
<h1><%= sd.docTitle %></h1>
<p>&nbsp;</p>
<p><%= sd.docDesc %></p>
<p>&nbsp;</p>

<form name="form1" method="post" action="shared_document.jsp?lineInfo=<%= lineInfo %>">
<p>
  <textarea name="docTextArea" cols="80" rows="20"><%= sd.docText %></textarea>
</p>
<input type="hidden" name="sending_page" value="shared_document">
  <input type="submit" name="Submit" value="Submit">
</form>
<p>&nbsp;</p>
</body>
</html>
<%
	
%>