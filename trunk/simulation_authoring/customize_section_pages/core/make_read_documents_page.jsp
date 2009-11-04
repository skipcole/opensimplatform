<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	CustomizeableSection cs = afso.handleMakeReadDocumentPage(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.backPage);
		return;
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../../../wysiwyg_files/wysiwyg.js">
</script>
<link href="../../../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
              <tr>
                <td width="120"><img src="../../../Templates/images/white_block_120.png" /></td>
                <td width="100%"><br />
                  <h1>Read Document(s) Page</h1>
                  <br />
                  <form action="make_read_documents_page.jsp" method="post" name="form2" id="form2">
                    <blockquote>
                      <p>Number of Documents to be Shown: </p>
                      <p>Tab Heading:
                        <input type="text" name="tab_heading" value="<%= afso.getMyPSO_SectionMgmt().get_tab_heading() %>"/>
                      </p>
                      <p>To allow access to a player to read a document, you must first have created it. To create a new document associated with this simulation <a href="../../make_create_document_page.jsp">click here</a>. </p>
                      <table width="100%" border="0">
                        <% List docsAvailable = SharedDocument.getAllBaseDocumentsForSim(afso.schema, afso.sim_id); 
							Hashtable index_hash = BaseSimSectionDepObjectAssignment.getIndexIdHashtable(afso.schema, cs.getId());
						%>
                        <% 
							if (cs.getNumDependentObjects() < 1) {
								cs.setNumDependentObjects(1);
							}
							
						for (int ii = 1; ii <= cs.getNumDependentObjects(); ++ii) {
                        
						%>
                        <tr>
                          <td width="75%" valign="top">Document <%= ii %> <a href="helptext/make_read_documents_help.jsp" target="helpInRight">(?)</a></td>
                          <td width="75%" valign="top"><%
		  	if (!((docsAvailable == null) || (docsAvailable.size() == 0))){

		  %>
                            <label>Select Document
                            <select name="doc_<%= ii %>" size="1">
                              <%
					for (ListIterator li = docsAvailable.listIterator(); li.hasNext();) {
					
						SharedDocument sd = (SharedDocument) li.next();
				%>
                              <option value="<%= sd.getId() %>" <%= afso.checkAgainstHash(cs.getId(), ii, sd.getId()) %> ><%= sd.getUniqueDocTitle() %></option>
                              <%
					}
				%>
                                                        </select>
</label>
                            <% } // end of if no documents have been created. %></td>
                          <td><label>
                            <input type="submit" name="button2" id="button2" value="Remove this Document" />
                            </label></td>
                        </tr>
                        <% } %>
                        <tr>
                          <td valign="top"><label>
                          <input type="submit" name="add_document" id="button" value="Add An Additional Document" />
                          </label></td>
                          <td valign="top">&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>
                      </table>
                      <p>Enter the introductory text that will appear on this page.
                        <textarea id="make_read_document_page_text" name="make_read_document_page_text" style="height: 710px; width: 710px;"><%= cs.getBigString() %></textarea>
                        <script language="javascript1.2">
  			generate_wysiwyg('make_read_document_page_text');
		</script>
                      </p>
                      <p>
                        <input type="hidden" name="custom_page" value="<%= afso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
                        <input type="hidden" name="sending_page" value="make_read_document_page" />
                        <input type="submit" name="save_page" value="Save" />
                        <input type="submit" name="save_and_add" value="Save and Add Section" />
                      </p>
                      <p>&nbsp;</p>
                    </blockquote>
                  </form>
                  <a href="<%= afso.backPage %>"><img src="../../../Templates/images/back.gif" alt="Back" border="0"/></a> </td>
              </tr>
            </table></td>
        </tr>
        <tr>
          <td><p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
        </tr>
      </table></td>
  </tr>
</table>
<p>&nbsp;</p>
<p align="center">&nbsp;</p>
</body>
</html>
<%
	
%>
