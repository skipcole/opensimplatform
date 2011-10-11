<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="/error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	// Create a new pso, in case the look at the history.
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	pso.loadInAFSOInformation(afso);
	
	SharedDocument sd = new SharedDocument();
	
	String sendingDocId = (String) request.getParameter("sendingDocId");
	String doc_id = (String) request.getParameter("doc_id");
	
	if ((sendingDocId != null) && (sendingDocId.equalsIgnoreCase("true"))){
		sd = SharedDocument.getById(afso.schema, new Long(doc_id));
	}
	
	SharedDocument.handleWriteStarterDocument(request, sd, afso);
	
	// If we are proceeding, we need to see if we need to go to another 
	// starter document, or to the the assign players page.
	String starterDocIndex = (String)  request.getParameter("starterDocIndex");
	
	int starter_doc_index = new Long(starterDocIndex).intValue();
	
	String command_save_and_proceed = (String) request.getParameter("command_save_and_proceed");
	
	if (command_save_and_proceed != null) {
		
		String nextURL = "facilitate_assign_user_to_simulation.jsp";
		
		List starterDocs = SharedDocument.getAllStarterBaseDocumentsForSim(afso.schema, afso.sim_id, afso.getRunningSimId());	
		
		if ((starter_doc_index + 1) < starterDocs.size()){
			starter_doc_index += 1;
			SharedDocument sd_next = (SharedDocument) starterDocs.get(starter_doc_index);
			
			nextURL = "facilitate_write_starter_document.jsp?sendingDocId=true&doc_id=" + sd_next.getId()
+ "&starterDocIndex=" + starter_doc_index;
			
		} 
		
		response.sendRedirect(nextURL);
		return;
		
	}

	
%>
<html>
<head>
<title>OSP Write Document Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>

<h2>Edit Starter Document</h2>
  
<p>Below is one of the first documents that your students will see.  Here you can edit it to match the particulars of your run of this simulation. </p>
<form name="form1" method="post" action="facilitate_write_starter_document.jsp">
  <input type="hidden" name="sending_page" value="write_document" />
<input type="hidden" name="sendingDocId" value="<%= sendingDocId %>" />
<input type="hidden" name="starterDocIndex" value="<%= starterDocIndex %>" />
<input type="hidden" name="doc_id" value="<%= doc_id %>" />

<p>
		  <textarea id="write_document_text" name="write_document_text" style="height: 310px; width: 710px;">
		  <%= sd.getBigString() %>
		  </textarea>
		<script language="javascript1.2">
  			generate_wysiwyg('write_document_text');
		</script>
		  </p>

<table width="100%"><tr>
  <td><input type="submit" name="command_save" value="Save"></td>
  <td><input type="submit" name="command_save_and_proceed" value="Save and Proceed"></td>
</tr></table>

</form>
Document Version <%= sd.getVersion() %> To see a previous version, <a href="../osp_core/review_document_version_history.jsp?sd_id=<%= sd.getId() %>">click here</a>.
</body>
</html>