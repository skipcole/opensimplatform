<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
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
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../../../wysiwyg_files/wysiwyg.js">
</script>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
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
              <h1>Customize Cast Page</h1>
              <br />
      <form action="make_cast_page.jsp" method="post" name="form2" id="form2">
        <% if ((cs != null) && (cs.getId() != null)) {
	   %>
        <input type="hidden" name="cs_id" value="<%= cs.getId() %>" />
        <% } %>
        <blockquote> 
          <p>Cast page will display control characters? <a href="../../helptext/cast_display_control.jsp" target="helpinright">(?)</a>
            <label>
              <input type="radio" name="display_control" id="display_control_yes" value="true" <%= Customizer.storedValueTrueToChecked(cs, CastCustomizer.KEY_FOR_DISPLAY_CONTROL, true) %> />
              Yes</label>
            / 
            <input type="radio" name="display_control" id="display_control_no" value="false" <%= Customizer.storedValueTrueToChecked(cs, CastCustomizer.KEY_FOR_DISPLAY_CONTROL, false) %> />
            No          </p>
          <p>Cast page will display unassigned actors? <a href="../../helptext/cast_display_unassigned.jsp" target="helpinright">(?):</a>
            <label>
              <input type="radio" name="display_unassigned" id="display_unassigned_yes" value="true" 
			  <%= Customizer.storedValueTrueToChecked(cs, CastCustomizer.KEY_FOR_DISPLAY_UNASSIGNED, true) %> />
              Yes</label>
            / 
            <input type="radio" name="display_unassigned" id="display_unassigned_no" value="false" 
			<%= Customizer.storedValueTrueToChecked(cs, CastCustomizer.KEY_FOR_DISPLAY_UNASSIGNED, false) %> />
            No          </p>
        </blockquote>
        
        <blockquote>
          <p>Default Tab Heading <a href="../../helptext/default_tab_heading_for_section.jsp" target="helpinright">(?)</a>: 
            <input type="text" name="tab_heading" value="<%= afso.getMyPSO_SectionMgmt().get_tab_heading() %>"/>
            </p>
            <table width="100%" border="1" cellspacing="0">
              <tr>
                <td valign="top">&nbsp;</td>
                <td valign="top">&nbsp;</td>
              </tr>
              <tr>
                <td valign="top">Page Introduction</td>
                <td valign="top"><label>
                  <textarea name="cs_bigstring" id="textarea" cols="45" rows="5"><%= cs.getBigString() %></textarea>
                  </label></td>
              </tr>
              </table>
              
          <p><strong>Modify Section Description</strong></p>
          <blockquote>
            <p>
              <textarea name="cs_description" cols="80" rows="3" id="cs_description"><%= cs.getDescription() %></textarea>
          </blockquote>

            <%
				
			boolean hasItAlready = SimulationSectionAssignment.determineIfActorHasThisSectionAtThisPhase(afso.schema, 
				afso.sim_id, afso.actor_being_worked_on_id, afso.phase_id, cs.getId());
			
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
              <input type="hidden" name="sending_page" value="make_cast_page" />
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
