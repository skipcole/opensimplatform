<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getMe(pso.schema, cs_id);
	
	String base_doc_id = (String) cs.getContents().get("doc_ids");
	
	RunningSimulation rs = pso.giveMeRunningSim();
	
	
	System.out.println("blah: " + pso.schema +  " " + cs.getId() + " " + rs.getId());
	
	SharedDocument sd = SharedDocument.getDocumentByBaseId(pso.schema, new Long(base_doc_id), rs.getId());
	
	String sending_page = (String) request.getParameter("sending_page");
	String update_text = (String) request.getParameter("update_text");
	
	if ( (sending_page != null) && (update_text != null) && (sending_page.equalsIgnoreCase("write_document"))){
		String write_document_text = (String) request.getParameter("write_document_text");
		
		sd.setBigString(write_document_text);
		sd.save(pso.schema);
		
		   
	} // End of if coming from this page and have added text
	

%>
<html>
<head>
<title>Make Announcement Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>

<body>
<p><%= cs.getBigString() %></p>

<form name="form1" method="post" action="write_document.jsp">

<input type="hidden" name="sending_page" value="write_document" />
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
    <label>
    <input type="submit" name="update_text" value="Submit">
    </label>
  </p>
</form>

<p>&nbsp;</p>
</body>
</html>
<%
	
%>
