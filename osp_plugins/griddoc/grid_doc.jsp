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
	
	GridPageData.handleChanges(request, cs, pso.schema, pso.sim_id, pso.getRunningSimId());
	
	GridPageData gpd = GridPageData.loadPage(pso.schema, cs, pso.sim_id, pso.getRunningSimId());
	
	Hashtable contents = cs.getContents();
	
%>
<html>
<head>
<title>Grid Doc</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h1><%=  GridDocCustomizer.getPageStringValue(cs, GridDocCustomizer.KEY_FOR_PAGETITLE) %></h1>
<p><%= cs.getBigString() %></p>
<table width="95%" border="1" cellspacing="2" cellpadding="2">
<tr>
<td valign="top"><%=  GridDocCustomizer.getPageStringValue(cs, GridDocCustomizer.KEY_FOR_NEW_ROW) %></td>
<% for (int ii = 1 ; ii <= gpd.getNumCols() ; ++ii) { 

	GridData gdTop = GridData.getGridData(pso.schema, pso.sim_id, cs.getId(), pso.getRunningSimId(), ii, 0);
			
%>
<td valign="top"><strong><%= gdTop.getCellData() %></strong>
	<% if (ii == gpd.getNumCols()) { %>
	<form name="form2" method="post" action="grid_doc.jsp">
    <input type="hidden" name="cs_id" value="<%= cs_id %>">
    <input type="hidden" name="col" value="<%= ii + "" %>">
    <input type="submit" name="del_col" id="button" value="-"  onclick="return confirm('Are you sure you want to delete this column?');">
  	</form>
    <% } %>
  </td>

<% } %>
</tr>

<% for (int jj = 1 ; jj <= gpd.getNumRows() ; ++jj) { 

GridData gdRow = GridData.getGridData(pso.schema, pso.sim_id, cs.getId(), pso.getRunningSimId(), 0, jj);

%>
<tr>
<td valign="top"><strong><%= gdRow.getCellData() %></strong>
<% if (jj == gpd.getNumRows() ) { %>
<form name="form2" method="post" action="grid_doc.jsp">
    <input type="hidden" name="cs_id" value="<%= cs_id %>">
    <input type="hidden" name="row" value="<%= jj + "" %>">
    <input type="submit" name="del_row" id="button" value="-"  onclick="return confirm('Are you sure you want to delete this row?');">
  	</form>
<% } %>
</td>
<% for (int ii = 1 ; ii <= gpd.getNumCols() ; ++ii) { 

GridData gdCell = GridData.getGridData(pso.schema, pso.sim_id, cs.getId(), pso.getRunningSimId(), ii, jj);

%> 

<td valign="top"><%= gdCell.getCellData() %><br /> <a href="edit_grid_data.jsp?gd_id=<%= gdCell.getId() %>&cs_id=<%= cs_id %>&col_num=<%= ii %>&row_num=<%= jj %>">Edit</a> </td>

<% } %>
</tr>
<% } %>
</table>
<form name="form1" method="post" action="grid_doc.jsp">
<input type="hidden" name="cs_id" value="<%= cs_id %>">

<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td>Add <%= GridDocCustomizer.getPageStringValue(cs, GridDocCustomizer.KEY_FOR_NEW_COLUMN) %></td>
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
    <td>Add <%= GridDocCustomizer.getPageStringValue(cs, GridDocCustomizer.KEY_FOR_NEW_ROW) %></td>
    <td><label>
      <input type="text" name="row_name" id="row_name_textfield">
    </label></td>
    <td><input type="submit" name="do_add_row" id="button2" value="Submit"></td>
  </tr>
</table>
</form>
<p><a href="grid_doc_linear_print.jsp?cs_id=<%= cs_id %>" target="_blank">Print Out Page</a></p>

</body>
</html>