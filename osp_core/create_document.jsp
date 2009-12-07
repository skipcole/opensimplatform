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
	
	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getMe(pso.schema, cs_id);
	
	RunningSimulation rs = pso.giveMeRunningSim();
	
	System.out.println("blah: " + pso.schema +  " " + cs.getId() + " " + rs.getId());
	
	SharedDocument sd = new SharedDocument();
	
	List setOfDocs = SharedDocument.getSetOfDocsForSection(pso.schema, cs.getId(), rs.getId());
	
	if ((setOfDocs != null) && (setOfDocs.size() > 0) ){
		System.out.println("im back in here getting 0.");
		sd = (SharedDocument) setOfDocs.get(0);
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
<p><%= cs.getBigString() %></p>

<form name="form1" method="post" action="create_document.jsp">
<input type="hidden" name="sending_page" value="create_document" />
<input type="hidden" name="cs_id" value="<%= cs_id %>" />
  
<p>
		  <textarea id="write_document_text" name="write_document_text" style="height: 310px; width: 710px;">
		  <%= sd.getBigString() %>
		  </textarea>
		<script language="javascript1.2">
  			generate_wysiwyg('write_document_text');
		</script>
		  </p>
  <p>
    <label></label>
    <input type="submit" name="update_text" value="Save">
  Add Update and Delete button.</p>
</form>

<ul>
  <li>Loop over existing documents made from the same template, and display them here.</li>
</ul>
</body>
</html>
<%
	
%>
