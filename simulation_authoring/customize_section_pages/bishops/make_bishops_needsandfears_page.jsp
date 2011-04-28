<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.specialfeatures.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	CustomizeableSection cs = afso.handleCustomizeSection(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.backPage);
		return;
	}
	
	BishopsCustomizer bc = new BishopsCustomizer(request, afso, cs);
	
	String selected_allow_edit = "";
	String selected_dont_allow_edit = "";
	
	if (bc.isAllowEdit()){
		selected_allow_edit = "checked";
	} else {
		selected_dont_allow_edit = "checked";
	}
	
	String selected_allow_cd_edit = "";
	String selected_dont_allow_cd_edit = "";
	
	if (bc.isAllowConflictDocumentEdit()){
		selected_allow_cd_edit = "checked";
	} else {
		selected_dont_allow_cd_edit = "checked";
	}
	
	
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
              <h1>Customize Needs and Fears Page</h1>
              <br />
      <form action="make_bishops_needsandfears_page.jsp" method="post" name="form2" id="form2">
        <% if (cs.getId() != null) {
	  	System.out.println("cs id was :" + cs.getId());
	   %>
        <input type="hidden" name="cs_id" value="<%= cs.getId() %>" />
        <% } %>
        
        <blockquote>
          <p>Default Tab Heading <a href="../../helptext/default_tab_heading_for_section.jsp" target="helpinright">(?)</a>:  
            <input type="text" name="tab_heading" value="<%= afso.getMyPSO_SectionMgmt().get_tab_heading() %>"/>
            </p>
          <p><strong>Editing</strong></p>
          <blockquote>
            <label>
            <input type="radio" name="selected_allow_edit" id="selected_allow_edit_true" value="true" <%= selected_allow_edit %> />
            Allow Editing</label>
            <br />
            <label>
            <input type="radio" name="selected_allow_edit" id="selected_allow_edit_false" value="false" <%= selected_dont_allow_edit %> />
            Do not allow editing</label>
            </blockquote>
                      <p><strong>Allow Editing of Conflict Document</strong></p>
                      <blockquote>
            <label>
            <input type="radio" name="selected_allow_cd_edit" id="selected_allow_edit_cd_true" value="true" <%= selected_allow_cd_edit %> />
            Allow Conflict Document Editing</label>
            <br />
            <label>
            <input type="radio" name="selected_allow_cd_edit" id="selected_allow_edit_cd_false" value="false" <%= selected_dont_allow_cd_edit %> />
            Do not allow Conflict Document Editing</label>
            </blockquote>
          <p><strong>Parameter Limiting Number of Players</strong></p>
          <blockquote>
            <p>
            <select name="gv_id" id="gv_id">
            <%
					for (ListIterator li = GenericVariable.getAllBaseGenericVariablesForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
						GenericVariable gv_l = (GenericVariable) li.next();
						
						String selected = USIP_OSP_Util.matchSelected(gv_l.getId(), bc.getParameterId(), " selected ");
			%>
            <option value="<%= gv_l.getId() %>" <%= selected %>><%= gv_l.getName() %></option>
        	<% } %>  
            </select>  
            </p>
          </blockquote>
          <p><strong>Conflict Document 1</strong></p>
          <blockquote>
            <p>
            <select name="conflict_doc_1" id="conflict_doc_1">
        	<%
			for (ListIterator li = SharedDocument.getAllBaseDocumentsForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
						SharedDocument sd = (SharedDocument) li.next();
						
						String selected = USIP_OSP_Util.matchSelected(sd.getId(), bc.getDoc1Id(), " selected ");
			%>  
                <option value="<%= sd.getId() %>" <%= selected %>><%= sd.getUniqueDocTitle() %></option>
            <% } %>
            </select>
            </p>
          </blockquote>
          <p><strong>Conflict Document 2</strong></p>
          <blockquote>
            <p>
			<select name="conflict_doc_2" id="conflict_doc_2">
        	<%
			for (ListIterator li = SharedDocument.getAllBaseDocumentsForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
						SharedDocument sd = (SharedDocument) li.next();
						
						String selected = USIP_OSP_Util.matchSelected(sd.getId(), bc.getDoc2Id(), " selected ");
			%>  
                <option value="<%= sd.getId() %>" <%= selected %>><%= sd.getUniqueDocTitle() %></option>
            <% } %>
            </select>
            </p>
            </blockquote>
          <p><strong>Document to Edit</strong></p>
          <blockquote>
            <p>
              <label>
              <input type="radio" name="doc_to_show" id="doc_to_show_1" value="1" />
              </label>
            Document 1</p>
            <p>
              <label>
              <input type="radio" name="doc_to_show" id="doc_to_show_2" value="2" />
              </label>
            Document 2</p>
          </blockquote>
          <p><strong>Modify Section Description</strong></p>
          <blockquote>
            <p>
              <textarea name="cs_description" cols="80" rows="3" id="cs_description"><%= cs.getDescription() %></textarea>
          </blockquote>
          <%
			boolean hasItAlready = SimulationSectionAssignment.determineIfActorHasThisSectionAtThisPhase(afso.schema, 
				afso.sim_id, afso.actor_being_worked_on_id, afso.phase_id, cs.getId());
			
			System.out.println("already has it is " + 	hasItAlready);
			
			String actors_name_string = "fill it in from cache";
			
			if (afso.actor_being_worked_on_id.equals(new Long(0))) {
				actors_name_string = " every actor ";
			} else {
				actors_name_string = USIP_OSP_Cache.getActorName(afso.schema, afso.sim_id, new Long(0), request, afso.actor_being_worked_on_id);
			}
			%>
            <% if (!(hasItAlready)) { %>
		    	<p> 
		    	  <input type="submit" name="save_and_add" value="Save and Add Section" />
		    	  Add this to <%= actors_name_string %> in phase <%= USIP_OSP_Cache.getPhaseNameById(request, afso.schema, afso.phase_id) %>              </p>
            <% } else { %>
            	<p>
            	  <input type="submit" name="save_page" value="Save" />
            	  This section has already been added to <%= actors_name_string %> for phase <%= USIP_OSP_Cache.getPhaseNameById(request, afso.schema, afso.phase_id) %>.</p>
            <% } %>

            <p> 
              <input type="hidden" name="custom_page" value="<%= afso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
              <input type="hidden" name="save_results" value="true" />
              <input type="hidden" name="sending_page" value="make_similietimeline_page" />
            </p>
            <p>&nbsp;</p>
          </blockquote>
      </form>      <a href="<%= afso.backPage %>"><img src="../../../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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
