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
	
	
	String do_add_col = (String) request.getParameter("do_add_col");
	String do_add_row = (String) request.getParameter("do_add_row");
	
	if (do_add_col != null) {
		//System.out.println("do add col");
		numCols += 1;	
		contents.put("myNumCols_" + pso.getRunningSimId(), numCols + "");
		
		String col_name = (String) request.getParameter("col_name");
		contents.put("colname_" + pso.getRunningSimId() + "_" + numCols, col_name);
		
		cs.saveMe(pso.schema);
	}
	
	if (do_add_row != null) {
		//System.out.println("do add row");
		numRows += 1;	
		contents.put("myNumRows_" + pso.getRunningSimId(), numRows + "");
		
		String row_name = (String) request.getParameter("row_name");
		contents.put("rowname_" + pso.getRunningSimId() + "_" + numRows, row_name);		
		cs.saveMe(pso.schema);
	}
	
	
	String del_col = (String) request.getParameter("del_col");
	if (del_col != null) {
		String col = (String) request.getParameter("col");
		
		numCols -= 1;	
		contents.put("myNumCols_" + pso.getRunningSimId(), numCols + "");
		
		// Loop over keys
		for (Enumeration e = contents.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String value = (String) cs.getContents().get(key);
			
			if (key.startsWith("rowData_" + pso.getRunningSimId() + "_" + col)){
				contents.remove(key);
			}
		}
		
		cs.saveMe(pso.schema);
	
	}
	
	String del_row = (String) request.getParameter("del_row");
	if (del_row != null) {
		String row = (String) request.getParameter("row");
		
		numRows -= 1;	
		contents.put("myNumRows_" + pso.getRunningSimId(), numRows + "");
		
		// Loop over keys
		for (Enumeration e = contents.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String value = (String) cs.getContents().get(key);
			
			if (
			
			(key.startsWith("rowData_" + pso.getRunningSimId() + "_" )) && (key.endsWith("_" + row))
			
			){
				contents.remove(key);
			}
		}
		
		cs.saveMe(pso.schema);
	
	}
	
	
%>
<html>
<head>
<title>Grid Doc</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h1>Needs Assessment Follow Up Chart
  
</h1>
<table width="95%" border="1" cellspacing="2" cellpadding="2">
<tr>
<td valign="top"><strong>Issues</strong></td>
<% for (int ii = 1 ; ii <= numCols ; ++ii) { 

	// loop over cols and get names
	String thisColName = (String) contents.get("colname_" + pso.getRunningSimId() + "_" + ii); %>

<td valign="top"><strong><%= thisColName %></strong>
	<% if (ii == numCols) { %>
	<form name="form2" method="post" action="grid_doc.jsp">
    <input type="hidden" name="cs_id" value="<%= cs_id %>">
    <input type="hidden" name="col" value="<%= ii + "" %>">
    <input type="submit" name="del_col" id="button" value="-"  onclick="return confirm('Are you sure you want to delete this column?');">
  	</form>
    <% } %>
  </td>

<% } %>
</tr>

<% for (int jj = 1 ; jj <= numRows ; ++jj) { 

	String thisRowName = (String) contents.get("rowname_" + pso.getRunningSimId() + "_" + jj); %>

<tr>
<td valign="top"><strong><%= thisRowName %></strong>
<% if (jj == numRows ) { %>
<form name="form2" method="post" action="grid_doc.jsp">
    <input type="hidden" name="cs_id" value="<%= cs_id %>">
    <input type="hidden" name="row" value="<%= jj + "" %>">
    <input type="submit" name="del_row" id="button" value="-"  onclick="return confirm('Are you sure you want to delete this row?');">
  	</form>
<% } %>
</td>
<% for (int ii = 1 ; ii <= numCols ; ++ii) { 

	String rowData = (String) contents.get("rowData_" + pso.getRunningSimId() + "_" + ii + "_ " + jj);
	if (rowData == null) {
		rowData = "";
		contents.put("rowData_" + pso.getRunningSimId() + "_" + ii + "_ " + jj, rowData);
	}
%> 

<td valign="top"><%= rowData %><br /> <a href="edit_grid_data.jsp?cs_id=<%= cs_id %>&col=<%= ii %>&row=<%= jj %>">Edit</a> </td>

<% } %>
</tr>
<% } %>
</table>
<p>&nbsp;</p>
<p>&nbsp;</p>
<form name="form1" method="post" action="grid_doc.jsp">
<input type="hidden" name="cs_id" value="<%= cs_id %>">

<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td>Add Party</td>
    <td>
      <label>
        <input type="text" name="col_name" id="textfield">
        </label>
    
    </td>
    <td><label>
      <input type="submit" name="do_add_col" id="do_add_col" value="Submit">
    </label></td>
  </tr>
  <tr>
    <td>Add Issue</td>
    <td><label>
      <input type="text" name="row_name" id="do_add_col">
    </label></td>
    <td><input type="submit" name="do_add_row" id="button2" value="Submit"></td>
  </tr>
</table>
</form>
<p><a href="grid_doc_linear_print.jsp?cs_id=<%= cs_id %>" target="_blank">Print Out Page</a></p>

</body>
</html>
