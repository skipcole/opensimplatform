<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.communications.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	afso.backPage = "make_create_document_page.jsp";
	
	SharedDocument this_sd = SharedDocument.handleCreateDocument(request, afso);
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Create/Edit Documents Page</h1>
              <br />
    <p>Documents associated with a simulation can be read and written to by the players. One must first create a document here, and then add it to specific 'Read' or 'Write' sections to give the players this functionality.
    <form action="make_create_document_page.jsp" method="post" name="form2" id="form2">
      
      <h2>Create New Document</h2>
            <table>
              <tr>
                <td valign="top">Unique Internal Document Title  <a href="helptext/uniq_doc_identifer_help.jsp" target="helpinright">(?)</a>:</td>
              <td valign="top"><input type="text" name="uniq_doc_title" value="<%= this_sd.getUniqueDocTitle() %>" tabindex="1" /></td></tr>
              <tr><td valign="top">Document Display Title <a href="helptext/document_display_title_help.jsp" target="helpinright">(?)</a>:</td>
            <td valign="top"><input name="doc_display_title" type="text" size="60" value="<%= this_sd.getDisplayTitle() %>" tabindex="2" /></td></tr>
              <tr>
                <td valign="top">Document Starter Text (?):</td>
                <td valign="top">
                <textarea id="doc_starter_text" name="doc_starter_text" style="height: 300px; width: 400px;" tabindex="3"><%= this_sd.getBigString() %></textarea>
                          <script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
		<script language="javascript1.2">
			wysiwygWidth = 300;
			wysiwygHeight = 300;
  			generate_wysiwyg('doc_starter_text');
		</script>
                
                
                </td>
              </tr>
              <tr>
                <td valign="top">Starter Document</td>
                <td valign="top">
                <%
				
					String notStarterDoc = " checked=\"checked\" ";
					String isStarterDoc = "";
					
					if (this_sd.isStarterDoc()){
						notStarterDoc = "";
						isStarterDoc = " checked=\"checked\" ";
					}
				%>
                
                <label for="starter_doc_no">No 
                  <input name="starter_doc" type="radio" id="starter_doc_no" value="false" <%= notStarterDoc %>  tabindex="4" /></label>
                  <label for="starter_doc_yes">/ Yes 
                    <input type="radio" name="starter_doc" id="starter_doc_yes" value="true" <%= isStarterDoc %>  tabindex="5" />
                  </label></td>
              </tr>
              <tr><td>&nbsp;</td><td>
              
              <% if (this_sd.getId() == null) { %>
              
              <input type="submit" name="create_doc" value="Create" tabindex="6" />
              
              <%
				} else {
				%>
                <input type="hidden" name="shared_doc_id" value="<%= this_sd.getId() %>" tabindex="7" />
                <input type="submit" name="clear_button" value="Clear" tabindex="6" />
                <input type="submit"  name="update_doc" value="Update Document" tabindex="8" /> 
                <%
					}
				%>
              
              </td></tr>
              </table>
              <input type="hidden" name="sending_page" value="make_create_document_page" />
      </p>
    </form>
      <p>&nbsp;</p>
      <p>Below are listed all of the documents currently associated with this simulation. </p>
      <table border="1" width="100%">
  <tr><td><strong>Uniq Identifier</strong></td>
  <td><strong>Display Title Seen to Players</strong></td>
  <td><strong>Delete</strong></td>
  </tr>
        <%
			  		int ii = 0;
					for (ListIterator li = SharedDocument.getAllBaseDocumentsForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
						SharedDocument sd = (SharedDocument) li.next();
						String nameToSend = java.net.URLEncoder.encode(sd.getDisplayTitle());
						boolean checkObjectInUse = BaseSimSectionDepObjectAssignment.checkObjectInUse(afso.schema, sd.getId(), "org.usip.osp.communications.SharedDocument");
				
						
				%>
        
          <tr><td><a href="make_create_document_page.jsp?shared_doc_id=<%= sd.getId() %>"><%= sd.getUniqueDocTitle() %></a></td>
                <td><%= sd.getDisplayTitle() %></td>
                <td>
                <% 
				if (checkObjectInUse) { %> <a href="view_sections_using_object.jsp?obj_class=org.usip.osp.communications.SharedDocument&obj_id=<%= sd.getId().toString() %>">object in use</a> 
                <% } else { %> 
                	<a href="delete_object.jsp?object_type=shareddocument&objid=<%= sd.getId().toString() %>&object_info=<%= nameToSend %>">delete</a>
                <% } %>                </td>
                </tr>
          
                <%
					}
				%>
      </table>
      <p>&nbsp;</p>
      <p><a href="add_objects.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a></p>			</td>
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
