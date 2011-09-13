<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	String sd_id = (String) request.getParameter("sd_id");
	String ver_num = (String) request.getParameter("ver_num");
	
	Long sdId = null;
	Long verNum = null;
	SharedDocument sd = new SharedDocument();
	SharedDocumentVersionHistory sdvh = null;
	
	if (sd_id != null){
		sdId = new Long(sd_id);
		
		sd = SharedDocument.getById(pso.schema, sdId);
		
		if (ver_num != null) {
			verNum = new Long(ver_num);
			sdvh = SharedDocumentVersionHistory.getByDocIdAndVerNumber
				(pso, sdId, verNum);
		}
	}

%>
<html>
<head>
<title>Review Document History Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%
	if (sdvh != null) {
%>
Below is document <%= sd.getDisplayTitle() %>, version <%= verNum %>
<p>
<blockquote>
<%= sdvh.getDocText() %>
</blockquote>
</p>
<% } // end of if sd != null
%>

<hr />
<ol>
<%
	List versions = SharedDocumentVersionHistory.getAllVersionsOfDocument(pso.schema, sdId);

	for (ListIterator li = versions.listIterator(); li.hasNext();) {
		SharedDocumentVersionHistory this_sdvh = (SharedDocumentVersionHistory) li.next();
%>
<li><a href="review_document_version_history.jsp?sd_id=<%= sd_id %>&ver_num=<%= this_sdvh.getVersionNum() %>">Version: <%= this_sdvh.getVersionNum() %></a>, Saved: <%= this_sdvh.getSaveDate() %>
<% if (this_sdvh.getActorId() != null) { 

	String actorName = USIP_OSP_Cache.getActorName(pso.schema, pso.sim_id, pso.getRunningSimId(), request, this_sdvh.getActorId());
%>
, by <%= actorName %>
<% } // end of if author found <% } %>

</li>
<%
	}
%>
</ol>
</body>
</html>