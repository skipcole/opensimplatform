<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}

	String sd_id = (String) request.getParameter("sd_id");
	SharedDocument docOnBlock = SharedDocument.getById(pso.schema, new Long(sd_id));
	
	String update = (String) request.getParameter("update");
	
	if (update != null) {
		String grid_text = (String) request.getParameter("grid_text");

		docOnBlock.setBigString(grid_text);
		docOnBlock.saveMe(pso.schema);
		
		response.sendRedirect("brainstorm.jsp");
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
<h1>Edit Brainstorm Document</h1>
<p>&nbsp;</p>
<form name="form1" method="post" action="brainstorm_data.jsp">
<input type="hidden" name="sd_id" value="<%= sd_id %>">
  <p>
		  <textarea id="grid_text" name="grid_text" style="height: 310px; width: 710px;">
		  <%= docOnBlock.getBigString() %>
		  </textarea>
		<script language="javascript1.2">
  			generate_wysiwyg('grid_text');
		</script>
  </p>
  <label>
  <input type="submit" name="update" id="button" value="Submit">
  </label>
</form>
<p><a href="brainstorm.jsp">&lt;- Back</a></p>
</body>
</html>
