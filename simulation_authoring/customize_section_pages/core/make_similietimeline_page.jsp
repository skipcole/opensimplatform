<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.communications.*,
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
	
	String selected_display_plan = "";
	String selected_display_actual = "";
	
	String stored_value = (String) cs.getContents().get(SimilieTimelineCustomizer.KEY_FOR_DISPLAY);
	
	if ((stored_value != null) && (stored_value.equalsIgnoreCase("show_plan"))){
		selected_display_plan = "checked";
	} else {
		selected_display_actual = "checked";
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
              <h1>Customize Similie Timeline Page</h1>
              <p>&nbsp;</p>
              <p>You must first create a timeline before you can create a timeline page. <br />
                      </p>
              <form action="make_similietimeline_page.jsp" method="post" name="form2" id="form2">
        <% if (cs.getId() != null) {
	  	System.out.println("cs id was :" + cs.getId());
	   %>
        <input type="hidden" name="cs_id" value="<%= cs.getId() %>" />
        <% } %>
        
        <blockquote>
          <p>Default Tab Heading <a href="../../helptext/default_tab_heading_for_section.jsp" target="helpinright">(?)</a>:  
            <input type="text" name="tab_heading" value="<%= afso.getMyPSO_SectionMgmt().get_tab_heading() %>"/>
            </p>
          <p>Select Timeline: <select name="timeline_id"><%
		List timelineList = TimeLine.getAllBaseForSimulation(afso.schema, afso.sim_id);
		
		for (ListIterator li = timelineList.listIterator(); li.hasNext();) {
			TimeLine tim = (TimeLine) li.next();

		%><option value="<%= tim.getId().toString() %>"><%= tim.getName() %></option><%
	}
%>
            </select>
          (<a href="../../timeline_creator.jsp">Click here to create one</a>.) </p>
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
