<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.specialfeatures.*,
	org.usip.osp.bishops.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
	
	BishopsCustomizer bc = new BishopsCustomizer();
	List partyList = new ArrayList();
	List inactivePartyList = new ArrayList();
	
	List setOfDocs = new ArrayList();
	
	SharedDocument conflictDoc = new SharedDocument();
	SharedDocument conflictDoc1 = new SharedDocument();
	SharedDocument conflictDoc2 = new SharedDocument();
	
	if (!(pso.preview_mode)) {	
		CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
		bc = new BishopsCustomizer(request, pso, cs);
		partyList = BishopsPartyInfo.getAllForRunningSim(pso.schema, pso.getRunningSimId(), false);
		inactivePartyList = BishopsPartyInfo.getAllForRunningSim(pso.schema, pso.getRunningSimId(), true);
		
		setOfDocs = SharedDocument.getSetOfDocsForSection(pso.schema, cs.getId(), pso.getRunningSimId());
		conflictDoc1 = (SharedDocument) setOfDocs.get(0);
		conflictDoc2 = (SharedDocument) setOfDocs.get(1);
		
		if(bc.getDocumentToShow() == 2) { 
			conflictDoc = conflictDoc2;
		} else {
			conflictDoc = conflictDoc1;
		}
		
	}
	
	
%>
<html>
<head>
<title>Linear Print Needs and Fears Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h1 align="center">Linear Print Needs and Fears Page </h1>
<table width="100%" border="1" cellspacing="0" cellpadding="2">
  <tr valign="top">
<% if ((partyList != null) && (partyList.size() >= 4)) { 
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(3);
  %>
    <% } %>
    <td width="33%" bgcolor="#FFEEEE"><div style="width:100%; overflow: auto"><h2>Conflict info
    <% if (bc.isAllowConflictDocumentEdit()) { %>
    (<a href="write_conflict_document.jsp?doc_id=<%= conflictDoc.getId() %>">edit</a>)
    <% } %>
    </h2>
    <p><%= conflictDoc.getBigString() %></p>
    <p>&nbsp;</p>
    
   	<% if(bc.getDocumentToShow() == 2) { %> 
    <p><a href="../compare_document.jsp?doc_1=<%= conflictDoc1.getId() %>&doc_2=<%= conflictDoc2.getId() %>">compare with previous version</a></p>
    <% } %>
    </div></td>
<% if ((partyList != null) && (partyList.size() >= 5)) { 
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(4);
  %>
    <% } %>
  </tr>
</table>
<p>&nbsp;</p>
<% if ((partyList != null) && (partyList.size() > 0)) { %>
<h2>Linear View </h2>
<%
	for (int ii = 0; ii <  partyList.size(); ++ii) {
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(ii);
  %>
<blockquote>
  <h2>Party <%= ii+1 %>: <%= bpi.getBPIName() %> (<a href="add_party.jsp?queueu_up=true&bpi_id=<%= bpi.getId() %>">edit</a>)<br>
  </h2>
      <h2>Needs</h2>
      <p><%= bpi.getNeedsDoc() %>      </p>
      <p>&nbsp;</p>
      <h2>Fears</h2>
      <p><%= bpi.getFearsDoc() %>      </p>
  <p>&nbsp;</p>
      <hr>
</blockquote>
  <%	} %>
  <hr>  
    <%	}  // End of if there are additional actors. %>

<h2>Inactive Actors</h2>
<% if ((inactivePartyList != null) && (inactivePartyList.size() > 0)) {

	for (int ii = 0; ii <  inactivePartyList.size(); ++ii) {
  		BishopsPartyInfo bpi = (BishopsPartyInfo) inactivePartyList.get(ii);
  %>
<blockquote>
  <h2>Party: <%= bpi.getBPIName() %> (<a href="add_party.jsp?queueu_up=true&bpi_id=<%= bpi.getId() %>">edit</a>)<br>
  </h2>
      <h2>Needs</h2>
      <p><%= bpi.getNeedsDoc() %>      </p>
      <p>&nbsp;</p>
      <h2>Fears</h2>
      <p><%= bpi.getFearsDoc() %>      </p>
  <p>&nbsp;</p>
      <hr>
</blockquote>
  <%	}   // End of loop over inactive Party List
  	}  // End of if inactive Party list not null
  %>
<p>&nbsp;</p>

</body>
</html>
