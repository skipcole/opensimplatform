<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.specialfeatures.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	CustomizeableSection cs = afso.handleCustomizeSection(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.backPage);
		return;
	}

	OneLinkCustomizer olc = new OneLinkCustomizer(request, afso, cs);	
	
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
              <h1>Customize One Link Page</h1>
              <br />
      <form action="make_onelink_page.jsp" method="post" name="form2" id="form2">
        <% if (cs.getId() != null) {
	   %>
        <input type="hidden" name="cs_id" value="<%= cs.getId() %>" />
        <% } %> 
        <blockquote>
          <p>Tab Heading: 
            <input type="text" name="tab_heading" value="<%= afso.getMyPSO_SectionMgmt().get_tab_heading() %>"/>
            </p>
<p><strong>One Link Object that points to web address.</strong></p>
          <blockquote>
            <p>
            <select name="ol_id" id="ol_id">
            <option value="0">None Selected</option>
            <%
					for (ListIterator li = OneLink.getAllBaseOneLinksForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
						OneLink ol_l = (OneLink) li.next();
						
						String selected = USIP_OSP_Util.matchSelected(ol_l.getId(), olc.getOlId(), " selected ");
			%>
            <option value="<%= ol_l.getId() %>" <%= selected %>><%= ol_l.getName() %></option>
        	<% } %>  
            </select>  
            </p>
          </blockquote>

              
          <p><strong>Modify Section Description</strong></p>
          <blockquote>
            <p>
              <textarea name="cs_description" cols="80" rows="3" id="cs_description"><%= cs.getDescription() %></textarea>
            </blockquote>

            <p>&nbsp;</p>
            <%
				
			boolean hasItAlready = SimulationSectionAssignment.determineIfActorHasThisSectionAtThisPhase(afso.schema, 
				afso.sim_id, afso.actor_being_worked_on_id, afso.phase_id, cs.getId());
			
			System.out.println("already has it is " + 	hasItAlready);
			
			String actors_name_string = "fill it in from cache";
			
			if (afso.actor_being_worked_on_id.equals(new Long(0))) {
				actors_name_string = " every actor ";
			} else {
				System.out.println("actor id : " + afso.actor_being_worked_on_id);
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