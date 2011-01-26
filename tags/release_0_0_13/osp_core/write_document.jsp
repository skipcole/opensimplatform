<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	SharedDocument sd = new SharedDocument();
	String editInstructions = "";
	
	String cs_id = (String) request.getParameter("cs_id");
	String sendingDocId = (String) request.getParameter("sendingDocId");
	String doc_id = (String) request.getParameter("doc_id");
	
	if ((sendingDocId != null) && (sendingDocId.equalsIgnoreCase("true"))){
		sd = SharedDocument.getMe(pso.schema, new Long(doc_id));
	} else {
		
		CustomizeableSection cs = CustomizeableSection.getMe(pso.schema, cs_id);
		editInstructions = cs.getBigString();
	
		List setOfDocs = SharedDocument.getSetOfDocsForSection(pso.schema, cs.getId(), pso.running_sim_id);
	
		if ((setOfDocs != null) && (setOfDocs.size() > 0) ){
			sd = (SharedDocument) setOfDocs.get(0);
		}
	}
	
	pso.handleWriteDocument(request, sd);
	

%>
<html>
<head>
<title>OSP Write Document Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<p><%= editInstructions %></p>

<form name="form1" method="post" action="write_document.jsp">
<input type="submit" name="update_text" value="Submit">
<input type="hidden" name="sending_page" value="write_document" />
<input type="hidden" name="cs_id" value="<%= cs_id %>" />
<input type="hidden" name="sendingDocId" value="<%= sendingDocId %>" />
<input type="hidden" name="doc_id" value="<%= doc_id %>" />

<p>
		  <textarea id="write_document_text" name="write_document_text" style="height: 310px; width: 710px;">
		  <%= sd.getBigString() %>
		  </textarea>
		<script language="javascript1.2">
  			generate_wysiwyg('write_document_text');
		</script>
		  </p>
</form>

</body>
</html>