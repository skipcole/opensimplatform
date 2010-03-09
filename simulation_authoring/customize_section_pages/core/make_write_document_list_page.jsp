<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.communications.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	CustomizeableSection cs = afso.handleCustomizeSection(request);
	
	WriteDocumentListCustomizer wdl = new WriteDocumentListCustomizer(request, afso, cs);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.backPage);
		return;
	}
	
	// Load the values
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

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
              <h1>Write Document List Page</h1>
              <br />
			 
      <form action="make_write_document_list_page.jsp" method="post" name="form2" id="form2">
        <blockquote>
          <p>Tab Heading: 
            <input type="text" name="tab_heading" value="<%= afso.getMyPSO_SectionMgmt().get_tab_heading() %>"/>
            </p>
          <p>To allow access to a player to write a document, you must first have created it. To create a new document associated with this simulation <a href="../../make_create_document_page.jsp">click here</a>.          </p>
          
          <p>Documents to Add</p>
          <table width="100%" border="0">
              <tr>
                <td width="75%" valign="top">
                  <%
		  	List docsAvailable = SharedDocument.getAllBaseDocumentsForSim(afso.schema, afso.sim_id);
			
			Hashtable selectedHash = wdl.getDocsOnListHashtable(docsAvailable, cs, afso.schema);

			int numSelect = docsAvailable.size();
			
		  	if (!((docsAvailable == null) || (docsAvailable.size() == 0))){

				for (ListIterator li = docsAvailable.listIterator(); li.hasNext();) {
					
					SharedDocument sd = (SharedDocument) li.next(); %>

                  <select name="doc_id_<%= sd.getId() %>" id="doc_id_<%= sd.getId() %>">
                  	<option value="0" >Not Present</option>
                   <% for (int ii = 1; ii <= numSelect; ++ii){ 
						
						String selected = "";
						String selectedRaw = (String) selectedHash.get(sd.getId() + "_" + ii);
						if (selectedRaw != null){
							selected = selectedRaw;
						}
						
				   %>
                   	<option value="<%= ii %>" <%= selected %>><%= ii %></option>
                   <% } %>
                  </select>

                  <%= sd.getDisplayTitle() %> (<%= sd.getUniqueDocTitle() %>)<br />
           <%   }
			} // end of if no documents have been created. %></td>
              </tr>
              </table>

              <p>                </p>
            <p><strong>Enter the introductory text that will appear on this page.            </strong></p>
            <p>
              <textarea id="make_write_document_page_text" name="make_write_document_page_text" style="height: 120px; width: 480px;"><%= cs.getBigString() %></textarea>
              
              <script language="javascript1.2">
			  	wysiwygWidth = 480;
				wysiwygHeight = 120;
  				generate_wysiwyg('make_write_document_page_text');
			</script>
              </p>
            <p> 
              <input type="hidden" name="custom_page" value="<%= afso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
              <input type="hidden" name="sending_page" value="make_write_document_list_page" />
<%
				
			boolean hasItAlready = SimulationSectionAssignment.determineIfActorHasThisSectionAtThisPhase(afso.schema, 
				afso.sim_id, afso.actor_being_worked_on_id, afso.phase_id, cs.getId());
			
			String actors_name_string = "fill it in from cache";
			
			if (afso.actor_being_worked_on_id.equals(new Long(0))) {
				actors_name_string = " every actor ";
			} else {
				System.out.println("actor id : " + afso.actor_being_worked_on_id);
				actors_name_string = USIP_OSP_Cache.getActorName(afso.schema, afso.sim_id, new Long(0), request, afso.actor_being_worked_on_id);
			}
			
			
			if (!(hasItAlready)) { %>
		    	<p> 
		    	  <input type="submit" name="save_and_add" value="Save and Add Section" />
		    	  Add this to <%= actors_name_string %> in phase <%= USIP_OSP_Cache.getPhaseNameById(request, afso.schema, afso.phase_id) %>              </p>
            <% } else { %>
            	<p>
            	  <input type="submit" name="save_page" value="Save" />
            	  This section has already been added to <%= actors_name_string %> for phase <%= USIP_OSP_Cache.getPhaseNameById(request, afso.schema, afso.phase_id) %>.</p>
            <% } %>

            
            <p>&nbsp;</p>
          </blockquote>
      </form>      <a href="<%= afso.backPage %>"><img src="../../../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
		</tr>
		</table>	</td>
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
</html>
<%
	
%>
