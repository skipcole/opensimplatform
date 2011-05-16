<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}

	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	
	
%>
<html>
<head>
<title>Grid Doc</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h1><%= cs.getContents().get(GridDocCustomizer.KEY_FOR_PAGETITLE) %></h1>
<table width="95%" border="1" cellspacing="2" cellpadding="2">
<tr>
<td valign="top"><%= cs.getContents().get(GridDocCustomizer.KEY_FOR_NEW_COLUMN) %></td>
<% for (int ii = 1 ; ii <= numCols ; ++ii) { 

	// loop over cols and get names
	String thisColName = (String) contents.get("colname_" + pso.getRunningSimId() + "_" + ii); %>

<td valign="top"><strong><%= thisColName %></strong>
	<% if (ii == numCols) { %>
	<form name="form2" method="post" action="../../osp_core/grid_doc.jsp">
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
<form name="form2" method="post" action="../../osp_core/grid_doc.jsp">
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

<td valign="top"><%= rowData %><br /> <a href="../../osp_core/edit_grid_data.jsp?cs_id=<%= cs_id %>&col=<%= ii %>&row=<%= jj %>">Edit</a> </td>

<% } %>
</tr>
<% } %>
</table>
<form name="form1" method="post" action="../../osp_core/grid_doc.jsp">
<input type="hidden" name="cs_id" value="<%= cs_id %>">

<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td>Add <%= cs.getContents().get(GridDocCustomizer.KEY_FOR_NEW_COLUMN) %></td>
    <td>
      <label>
        <input type="text" name="col_name" id="col_name_textfield">
        </label>
    
    </td>
    <td><label>
      <input type="submit" name="do_add_col" id="do_add_col" value="Submit">
    </label></td>
  </tr>
  <tr>
    <td>Add <%= cs.getContents().get(GridDocCustomizer.KEY_FOR_NEW_ROW) %></td>
    <td><label>
      <input type="text" name="row_name" id="row_name_textfield">
    </label></td>
    <td><input type="submit" name="do_add_row" id="button2" value="Submit"></td>
  </tr>
</table>
</form>
<p><a href="../../osp_core/grid_doc_linear_print.jsp?cs_id=<%= cs_id %>" target="_blank">Print Out Page</a></p>

</body>
</html>
