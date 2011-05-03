<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}

	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	int numCols = 0;
	int numRows = 0;
	
	Hashtable contents = cs.getContents();
	
	// Get num cols/rows from hashtable
	String myNumCols = (String) contents.get("myNumCols_" + pso.getRunningSimId());
	String myNumRows = (String) contents.get("myNumRows_" + pso.getRunningSimId());
	
	if (myNumCols == null){
		//System.out.println("putting in 0");
		contents.put("myNumCols_" + pso.getRunningSimId(), "0");
		cs.saveMe(pso.schema);
	} else {
		numCols = new Long(myNumCols).intValue();
		//System.out.println("nc is " + numCols);
	}
	
	/////////////////////////////
	if (myNumRows == null){
		//System.out.println("putting in 0");
		contents.put("myNumRows_" + pso.getRunningSimId(), "0");
		cs.saveMe(pso.schema);
	} else {
		numRows = new Long(myNumRows).intValue();
		//System.out.println("nr is " + numRows);
	}
	
		
%>
<html>
<head>
<title>Grid Doc</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h1>Needs Assessment Follow Up Chart</h1>
<table width="95%" border="1" cellspacing="2" cellpadding="2">

<% for (int jj = 1 ; jj <= numRows ; ++jj) { 

	String thisRowName = (String) contents.get("rowname_" + pso.getRunningSimId() + "_" + jj); %>

<tr>
<td valign="top"><h1><strong><%= thisRowName %></strong></h1></td>
</tr>
<% for (int ii = 1 ; ii <= numCols ; ++ii) { 

	String rowData = (String) contents.get("rowData_" + pso.getRunningSimId() + "_" + ii + "_ " + jj);
	if (rowData == null) {
		rowData = "";
		contents.put("rowData_" + pso.getRunningSimId() + "_" + ii + "_ " + jj, rowData);
	}
	
	String thisColName = (String) contents.get("colname_" + pso.getRunningSimId() + "_" + ii);
%> 

<tr><td valign="top"><blockquote><h2><%= thisColName %></h2><%= rowData %></blockquote></td></tr>

<% } %>
</tr>
<% } %>
</table>
<p>&nbsp;</p>

<p>&nbsp;</p>

</body>
</html>
