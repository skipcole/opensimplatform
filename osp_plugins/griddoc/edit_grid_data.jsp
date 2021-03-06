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
	String gd_id = (String) request.getParameter("gd_id");
	
	String row_num = (String) request.getParameter("row_num");
	String col_num = (String) request.getParameter("col_num");
	
	GridData gd = new GridData();
	
	if ((gd_id != null) && (!(gd_id.equalsIgnoreCase("null")))) {
		gd = GridData.getById(pso.schema, new Long(gd_id));
	} else {
		
		gd = new GridData(pso.sim_id, pso.getRunningSimId(), new Long(cs_id), row_num, col_num);
	}
	
	String update = (String) request.getParameter("update");
	
	if (update != null) {
		String grid_text = (String) request.getParameter("grid_text");
		gd.setCellData(grid_text);
		gd.saveMe(pso.schema);
		
		response.sendRedirect("grid_doc.jsp?cs_id=" + cs_id);
		return;
	}
	
	
%>
<html>
<head>
<title>Grid Doc</title>
<script language="JavaScript" type="text/javascript" src="../../wysiwyg_files/wysiwyg.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h1>Edit Grid Data</h1>
<p>&nbsp;</p>
<form name="form1" method="post" action="edit_grid_data.jsp">
<input type="hidden" name="row_num" value="<%= row_num %>">
<input type="hidden" name="col_num" value="<%= col_num %>">
<input type="hidden" name="cs_id" value="<%= cs_id %>">
<input type="hidden" name="gd_id" value="<%= gd_id %>">
  <p>
		  <textarea id="grid_text" name="grid_text" style="height: 310px; width: 710px;">
		  <%= gd.getCellData() %>
		  </textarea>
		<script language="javascript1.2">
			newRootDir = "../../wysiwyg_files/";
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
