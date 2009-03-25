<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	//pso.handleMakeReadDocumentPage(request);
	
	String sending_page = (String) request.getParameter("sending_page");
	String save_page = (String) request.getParameter("save_page");

	if ((sending_page != null) && (sending_page.equalsIgnoreCase("make_create_document_page"))){
		String uniq_doc_title = (String) request.getParameter("uniq_doc_title");
		String doc_display_title = (String) request.getParameter("doc_display_title");
		
		System.out.println("creating doc of uniq title: " + uniq_doc_title);
		SharedDocument sd = new SharedDocument(uniq_doc_title, doc_display_title, pso.sim_id);
		sd.save(pso.schema);
		
	}
	
	if (pso.forward_on){
		pso.forward_on = false;
		response.sendRedirect(pso.backPage);
		return;
	}
	
	pso.fillReadWriteLists();
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../../../wysiwyg_files/wysiwyg.js">
</script>


<link href="../../../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../../../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Create/Edit Documents Page</h1>
              <br />
    <p>Documents associated with a simulation can be read and written to by the players. One must first create a document here, and then add it to specific 'Read' or 'Write' sections to give the players this functionality.
    <form action="../../make_create_document_page.jsp" method="post" name="form2" id="form2">
      
      <h2>Create New Document</h2>
            <table>
              <tr>
                <td>Unique Internal Document Title  <a href="../../helptext/uniq_doc_identifer_help.jsp" target="helpinright">(?)</a>:</td>
              <td><input type="text" name="uniq_doc_title" /></td></tr>
              <tr><td>Document Display Title <a href="../../helptext/document_display_title_help.jsp" target="helpinright">(?)</a>:</td>
            <td><input name="doc_display_title" type="text" size="60" /></td></tr>
              <tr><td>&nbsp;</td><td><input type="submit" name="create_doc" value="Create" /></td></tr>
              </table>
              <input type="hidden" name="sending_page" value="make_create_document_page" />
      </p>
    </form>
      <p>&nbsp;</p>
      <p>Below are listed all of the documents currently associated with this simulation. </p>
      <table border="1">
  <tr><td><strong>Uniq Identifier</strong></td>
  <td><strong>Display Title Seen to Players</strong></td>
  <td><strong>Update</strong></td>
  <td><strong>Delete</strong></td>
  <td><strong>Read*</strong></td>
  <td><strong>Write*</strong></td>
  </tr>
        <%
			  		int ii = 0;
					for (ListIterator li = SharedDocument.getAllBaseDocumentsForSim(pso.schema, pso.sim_id).listIterator(); li.hasNext();) {
						SharedDocument sd = (SharedDocument) li.next();
				%>
        <form action="../../make_create_document_page.jsp" method="post" name="form_edit_<%= ii + "" %>">
          <tr><td><%= sd.getUniqueDocTitle() %></td>
                <td><%= sd.getDisplayTitle() %></td>
                <td><input name="Update" type="button" value="Update" /></td>
                <td>delete</td>
                <td><%= pso.stringListToNames(request, (String) pso.ActorsWithReadAccess.get(sd.getId().toString()), "<br />") %></td>
                <td>&nbsp;</td>
                </tr>
          </form>
                <%
					}
				%>
      </table>
      <p>* Read/Write access simply means that at some point in the simulation (at some phase) this actor has access to read or write to this document.</p>      <p><a href="<%= pso.backPage %>"><img src="../../../Templates/images/back.gif" alt="Back" border="0"/></a></p>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
<%
	
%>
