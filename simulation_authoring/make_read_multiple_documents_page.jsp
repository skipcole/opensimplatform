<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	pso.handleMakeReadDocumentPage(request);
	
	if (pso.forward_on){
		pso.forward_on = false;
		response.sendRedirect(pso.backPage);
		return;
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
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
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Read Multiple Documents Page</h1>
    <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 

      <form action="make_read_document_page.jsp" method="post" name="form2" id="form2">
        <blockquote>
          <p>Tab Heading: 
            <input type="text" name="tab_heading" value="<%= pso.getMyPSO_SectionMgmt().get_tab_heading() %>"/>
          </p>
          <p><label>Select Document
			
            <select name="doc_id">
				<%
					for (ListIterator li = SharedDocument.getAllBaseDocumentsForSim(pso.schema, pso.sim_id).listIterator(); li.hasNext();) {
						SharedDocument sd = (SharedDocument) li.next();
				%>
              		<option value="<%= sd.getId() %>"><%= sd.getUniqueDocTitle() %></option>
            	<%
					}
				%>
			</select>
            </label>
          </p>
          <p>Enter the introductory text that will appear on this page. 
            <textarea id="make_read_document_page_text" name="make_read_document_page_text" style="height: 710px; width: 710px;"><%= pso.customizableSectionOnScratchPad.getBigString() %></textarea>

		<script language="javascript1.2">
  			generate_wysiwyg('make_read_document_page_text');
		</script>
          </p>
          <p>&nbsp;</p>
          <p> 
            <input type="hidden" name="custom_page" value="<%= pso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
            <input type="hidden" name="sending_page" value="make_read_document_page" />
            <input type="submit" name="save_page" value="Save" />
            <input type="submit" name="save_and_add" value="Save and Add Section" />
          </p>
          <p><input type="submit" name="create_duplicate" value="Create Duplicate" disabled /></p>
        </blockquote>
      </form>
	  <a href="<%= pso.backPage %>"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a><!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
<%
	
%>
