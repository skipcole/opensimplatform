<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.communications.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	// Get from cs the values
	PlayerDeclarationDocumentCustomizer pdc = new PlayerDeclarationDocumentCustomizer(request, pso, cs);

	List setOfDocs = new ArrayList();
	
	SharedDocument templateDoc = new SharedDocument();
	
	if (!(pso.preview_mode)) {
		setOfDocs = SharedDocument.getSetOfDocsForSection(pso.schema, cs.getId(), pso.getRunningSimId());
		if (setOfDocs != null) {
			templateDoc = (SharedDocument) setOfDocs.get(0);
		}
	}
	
	String a_id = (String) request.getParameter("a_id");
	String b_id = (String) request.getParameter("b_id");
	
	boolean showDoc = false;
	
	if (a_id != null) {
		showDoc = true;
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
<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="24%">view/hide list of player docs</td>
    <td width="76%">&nbsp;</td>
  </tr>
  <%
	for (ListIterator la = SimActorAssignment.getActorsForSim(pso.schema, pso.sim_id).listIterator(); la.hasNext();) {
		Actor act = (Actor) la.next();
		String checked = "";
					
		if (pdc.thisActorHasDocument(act.getId())) {  %>
  <tr>
    <td>&nbsp;</td>
    <td><a href="player_declaration_document.jsp?a_id=<%= act.getId() %>&b_id=<%= templateDoc.getId() %>&cs_id=<%= cs_id %>"><%= act.getActorName() %></a></td>
  </tr>
            
        <%  }
		
	}
%>

</table>
<%		
	if (showDoc) {
	
		SharedDocument sd = SharedDocument.getPlayerDocument(pso.schema, new Long (b_id), pso.sim_id, pso.getRunningSimId(), new Long (a_id));
			
%>
<hr>
	<h1><%= sd.getDisplayTitle() %></h1>
	<%= sd.getBigString() %>
<p>&nbsp;</p>
<% if ( sd.getPrimaryAuthorId().intValue() == pso.getActorId().intValue() ) { %>
you can edit this.
<% } %>
<%
	} // if showDoc
%>


ok
</body>
</html>
<%
	
%>
