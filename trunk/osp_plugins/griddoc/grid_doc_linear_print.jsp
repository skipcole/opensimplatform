<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	com.seachangesimulations.osp.griddoc.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}

	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	GridPageData gpd = GridPageData.loadPage(pso.schema, cs, pso.sim_id, pso.getRunningSimId());
	
		
%>
<html>
<head>
<title>Grid Doc</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<%=  GridDocCustomizer.getPageStringValue(cs, GridDocCustomizer.KEY_FOR_PAGETITLE) %></h1>
<p><%= cs.getBigString() %></p>

<table width="95%" border="1" cellspacing="2" cellpadding="2">

<% for (int iiLoopOverRows = 1 ; iiLoopOverRows <= gpd.getNumRows() ; ++iiLoopOverRows) { 

GridData gdRow = GridData.getGridData(pso.schema, pso.sim_id, cs.getId(), pso.getRunningSimId(), 0, iiLoopOverRows);

%>
<tr>
<td valign="top"><h1><%= gdRow.getCellData() %></h1>

<% for (int iiSecondLoopOverCols = 1 ; iiSecondLoopOverCols <= gpd.getNumCols() ; ++iiSecondLoopOverCols) { 

GridData gdCellName = GridData.getGridData(pso.schema, pso.sim_id, cs.getId(), pso.getRunningSimId(), iiSecondLoopOverCols, 0);

GridData gdCell = GridData.getGridData(pso.schema, pso.sim_id, cs.getId(), pso.getRunningSimId(), iiSecondLoopOverCols, iiLoopOverRows);

%> 
<blockquote>
<h2><%= gdCellName.getCellData() %></h2>
<blockquote>
<%= gdCell.getCellData() %>
</blockquote>
</blockquote>

<% } // End of loop over cols %>
</td>
</tr>
<% } // End of loop over tows %>
</table>
<br />
<p>&nbsp;</p>
<a href="grid_doc.jsp?cs_id=<%= cs_id %>">&lt;- Back</a>
<p>&nbsp;</p>

</body>
</html>
