<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}

	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	Hashtable contents = cs.getContents();
	
	String col = (String) request.getParameter("col");
	String row = (String) request.getParameter("row");
	
	String update = (String) request.getParameter("update");
	
	if (update != null) {
		String grid_text = (String) request.getParameter("grid_text");
		contents.put("rowData_" + pso.getRunningSimId() + "_" + col + "_ " + row, grid_text);
		cs.saveMe(pso.schema);
		
		response.sendRedirect("grid_doc.jsp?cs_id=" + cs_id);
		return;
	}
	
	
%>
<html>
<head>
<title>Grid Doc</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h1>Edit Grid Data</h1>
<p>&nbsp;</p>
<form name="form1" method="post" action="edit_grid_data.jsp">
<input type="hidden" name="cs_id" value="<%= cs_id %>">
<input type="hidden" name="col" value="<%= col %>">
<input type="hidden" name="row" value="<%= row %>">
<%
	String gridText = (String) contents.get("rowData_" + pso.getRunningSimId() + "_" + col + "_ " + row);
	
	if (gridText == null) {
		gridText = "";
	}
%>
  <p>
		  <textarea id="grid_text" name="grid_text" style="height: 310px; width: 710px;">
		  <%= gridText %>
		  </textarea>
		<script language="javascript1.2">
  			generate_wysiwyg('grid_text');
		</script>
  </p>
  <label>
  <input type="submit" name="update" id="button" value="Submit">
  </label>
</form>
<p><a href="grid_doc.jsp?cs_id=<%= cs_id %>">&lt;- Back</a></p>
</body>
</html>
