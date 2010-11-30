<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String doc_id = (String) request.getParameter("doc_id");
	
	SharedDocument conflictDoc = SharedDocument.getById(pso.schema, new Long(doc_id));
	
	String sending_page = (String) request.getParameter("sending_page");
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("write_conflict_document"))){
		conflictDoc.setBigString((String) request.getParameter("write_conflict_document_text"));
		conflictDoc.saveMe(pso.schema);
	}
	
%>
<html>
<head>
<title>OSP Write Document Page</title>
<script language="JavaScript" type="text/javascript" src="../../wysiwyg_files/wysiwyg.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<p>Edit the Conflict Document Below</p>

<form name="form1" method="post" action="write_conflict_document.jsp">
<input type="submit" name="update_text" value="Submit">
<input type="hidden" name="sending_page" value="write_conflict_document" />
<input type="hidden" name="doc_id" value="<%= doc_id %>" />
  
<p>
		  <textarea id="write_conflict_document_text" name="write_conflict_document_text" style="height: 310px; width: 710px;">
		  <%= conflictDoc.getBigString() %>
		  </textarea>
		<script language="javascript1.2">
  			generate_wysiwyg('write_conflict_document_text');
		</script>
		  </p>
  <p>
    <label></label>
  </p>
</form>

<p>&nbsp;</p>
</body>
</html>