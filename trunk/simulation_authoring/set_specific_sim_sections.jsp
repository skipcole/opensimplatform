<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = afso.getBaseSimURL() + "/simulation_authoring/set_specific_sim_sections.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	afso.changeSectionColor(request);
	
	Simulation simulation = new Simulation();
	Actor actor = new Actor();
	SimulationPhase spp = new SimulationPhase();
	List thisSimsActors = new ArrayList();
	
	if (afso.sim_id != null) {
		simulation = afso.handleSetSimSectionsPage(request);	
	
		if ((afso.actor_being_worked_on_id != null) && (afso.actor_being_worked_on_id.intValue() != 0 )){
			actor = afso.giveMeActor(afso.actor_being_worked_on_id);
		}
	
		if (afso.phase_id != null) {
			spp = afso.giveMePhase();
		}
		
		thisSimsActors = simulation.getActors(afso.schema);
	
	} // end of if afso.selected
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>


<script language="JavaScript" type="text/JavaScript">
<!--
var tab_headings = new Array();
	var sec_descs = new Array();
	var the_sample_images = new Array();
	
	<%	

	for (ListIterator li = new BaseSimSection().getAllAndChildren(afso.schema).listIterator(); li.hasNext();) {
			BaseSimSection bss = (BaseSimSection) li.next(); %>
	tab_headings["<%= bss.getId() %>"] = "<%= bss.getRec_tab_heading() %>";
	sec_descs["<%= bss.getId() %>"] = "<%= bss.getDescription() %>";
	the_sample_images["<%= bss.getId() %>"] = "<%= bss.getSample_image() %>";
	<% 

	   } 
	%>	
	
function loadFirstInfo(){

	window.document.section_form.tab_heading.value = tab_headings["1"];
	document.getElementById('sec_desc').innerHTML = sec_descs["1"];
	window.document.getElementById('sample_image').src = "../simulation_section_information/images/" + the_sample_images["1"];
	
}

function loadInfo(dropdownlist){

	var myindex  = dropdownlist.selectedIndex;
    
	var passedvalue = dropdownlist.options[myindex].value;
	
	window.document.section_form.tab_heading.value = tab_headings[passedvalue];
	document.getElementById('sec_desc').innerHTML = sec_descs[passedvalue];
	window.document.getElementById('sample_image').src = "../simulation_section_information/images/" + the_sample_images[passedvalue];
	
	return true;

}

//-->
</script>
<!-- TemplateParam name="theBodyInfo" type="text" value="" -->
<style type="text/css">
<!--
.customized_section {background-color:#CCFFCC}
.player_customized_section {background-color:#99FFFF}

.style_cs {color: #FF0000}
-->
</style>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="loadFirstInfo();">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Set Specific Simulation Sections</h1>
              <br />
      <blockquote> 
        <% 
			if (afso.sim_id != null) {
		%>
        <p>Set the sections available to the actors in the simulation <strong><%= simulation.getDisplayName() %></strong>.<br>
          (If you would like to work on a different simulation, <a href="select_simulation.jsp">click 
            here</a>.)</p>
	    </blockquote>
      <table width="100%" border="1">
        <tr>
          <td> <table width="100%" border="0">
            <tr> 
              <td width="71%"> <h2> 
                Actor <strong>'<%= actor.getActorName() %>'</strong> 
                in Phase <strong>'<%= spp.getPhaseName() %>' </strong> </h2></td>
                    <td width="29%">
                      <form id="form2" name="form2" method="post" action="set_specific_sim_sections.jsp">
                        <select name="phase_id">
                          <% 
						
						for (ListIterator li = SimPhaseAssignment.getPhasesForSim(afso.schema, simulation.getId()).listIterator(); li.hasNext();) {
							SimulationPhase sp = (SimulationPhase) li.next();
							
							String selected_p = "";
							
							if (sp.getId().intValue() == afso.phase_id.intValue()) {
								selected_p = "selected";
							}
							
							
				%>
                          <option value="<%= sp.getId().toString() %>" <%= selected_p %>><%= sp.getPhaseName() %></option>
                          <% } 	%>
                          </select>
                        <label>
                          <input type="hidden" name="actor_index" value="<%= afso.getMyPSO_SectionMgmt().getCurrentActorIndex()  %>">
                          <input type="hidden" name="actor_being_worked_on_id" value="<%= actor.getId() %>">
                          <input type="submit" name="command" value="Change Phase" />
                          </label>
                        </form>				    </td>
                  </tr>
            </table>
                <p>&nbsp;</p>
                <%
			if (afso.tempSimSecList.size() > 0) {
		%> <table  border="1" cellspacing="2" cellpadding="1">
                  <tr> 
                    <%
		  	int ii = 0;
			// Need set of ids by position to set the exchange going to the right.
			Hashtable idByPos = new Hashtable();
			
			////////////////////////////////////////////////////
			for (ListIterator li = afso.tempSimSecList.listIterator(); li.hasNext();) {
				SimulationSectionAssignment ss = (SimulationSectionAssignment) li.next();
				
				idByPos.put(ii + "", ss.getId());
				
				++ii;
			}
			////////////////////////////////////////////////////
			
			ii = 0;
			int first_ss = 0;
			
			for (ListIterator li = afso.tempSimSecList.listIterator(); li.hasNext();) {
				SimulationSectionAssignment ss = (SimulationSectionAssignment) li.next();
				
				int sec_ss = ss.getId().intValue();
				
				String col_selected = "";
				
				
		  %>
                    <% if (ii > 0) { %>
                    
                    <td><a href="set_specific_sim_sections.jsp?command=move_left&m_index=<%= ii %>">&lt;-</a><!-- /a--></td>
                    <% } %>
                    <td bgcolor="#<%= ss.getTabColor() %>"><a href="show_section_preview.jsp?sec_id=<%= sec_ss %>"><%= ss.getTab_heading() %></a></td>
                    <% if (ii < (afso.tempSimSecList.size() - 1)) { %>
                    <td><a href="set_specific_sim_sections.jsp?command=move_right&m_index=<%= ii %>">-&gt;</a></td>
                    <% } %>
                    <%
				++ii;
			
				first_ss = ss.getId().intValue();
				} // End of loop over simulation sections
			%>
                    <tr> 
                      <%
		  	ii = 0;
			
			for (ListIterator li = afso.tempSimSecList.listIterator(); li.hasNext();) {
				SimulationSectionAssignment ss = (SimulationSectionAssignment) li.next();
		  %>
                      <% if (ii > 0) { %>
                      <td>&nbsp;</td>
                    <% } %>
                      <td>
                        
                        <form name="change_color_ssid_<%= ss.getId() %>" method="post" action="set_specific_sim_sections.jsp">
                          
                          <input type="hidden" name="ss_id" value="<%= ss.getId() %>" />
                          
                          <input type="hidden" name="sending_section" value="change_color" />
                          
                          <select name="new_color">
                            <option value="FFFFFF" <%= USIP_OSP_Util.matchSelected("FFFFFF", ss.getTabColor(), "selected") %> >White</option>
                            <option value="FFCCCC" <%= USIP_OSP_Util.matchSelected("FFCCCC", ss.getTabColor(), "selected") %> >Red</option>
                            <option value="CCFFCC" <%= USIP_OSP_Util.matchSelected("CCFFCC", ss.getTabColor(), "selected") %> >Green</option>
                            <option value="CCCCFF" <%= USIP_OSP_Util.matchSelected("CCCCFF", ss.getTabColor(), "selected") %> >Blue</option>
                            </select>
                          <input type="submit" name="button" id="button" value="Go!" />
                          </form>
                    <% if (ii < (afso.tempSimSecList.size() - 1)) { %>
                        <td>&nbsp;</td>
                    <% } // End of if this is not the last.
			++ii;
				} // End of loop over simulation sections
			%>
                      </tr>
                  <tr> 
                    <%
		  	ii = 0;
			
			for (ListIterator li = afso.tempSimSecList.listIterator(); li.hasNext();) {
				SimulationSectionAssignment ss = (SimulationSectionAssignment) li.next();
		  %>
                    <% if (ii > 0) { %>
                    <td>&nbsp;</td>
                    <% } %>
                    <td><a href="delete_object.jsp?object_type=sim_section&objid=<%= ss.getId().toString() %>&backpage=set_specific_sim_sections.jsp&object_info=<%= ss.getTab_heading() %>">Remove </a></td>
                    <% if (ii < (afso.tempSimSecList.size() - 1)) { %>
                    <td>&nbsp;</td>
                    <% } %>
                    <%
			++ii;
				} // End of loop over simulation sections
			%>
                    </tr>
                  </table>
                <% } else { %> <div align="center">No simulation sections for this 
                  actor in this phase. </div>
                <% } // end of if no sim sections %> 
            <p align="center"><table align="center">
              <tr><td valign="top"><img name="sample_image" id="sample_image" src="../simulation_section_information/images/sample.png" width="300" height="240" /></td>
              <td width="300" valign="top"><strong>Section Description:</strong><br />
              <div style="width:auto; background-color:#DDDDFF; padding:2; border:#000000" id="sec_desc">Section Description</div>
               </td></tr></table>                 
               </p>
                <table width="100%" border="0" cellspacing="2" cellpadding="1">
                  <tr>
                    <td colspan="2" valign="top"> 
                      <table border="1" width="100%">
                        <form id="section_form" name="section_form" method="post" action="set_sim_sections_router.jsp">
                          <tr> 
                            <td width="50%" valign="top"><p>Select a section 
                              below to add to this actor at this phase in the simulation.
                              <blockquote> 
                                <select name="bss_id"  onChange="loadInfo(window.document.section_form.bss_id);">
                                  <%
							
		for (ListIterator li = new BaseSimSection().getAll(afso.schema).listIterator(); li.hasNext();) {
			BaseSimSection bss = (BaseSimSection) li.next();  %>
            <option value="<%= bss.getId() %>"><%= bss.getRec_tab_heading() %></option>
        <% }  // End of loop over base sim sections. %>
        
            <option value="new_section">* Create an Entirely New Section</option>
            
		<% 
		List uc = CustomizeableSection.getAllUncustomized(afso.schema);
								
		if (uc != null) {
 
			for (ListIterator li = uc.listIterator(); li.hasNext();) {
				CustomizeableSection cs = (CustomizeableSection) li.next();
				
				//////////////////////////////////////////////////////////
				// Don't list sections the actor already has at this phase.
				boolean hasItAlready = SimulationSectionAssignment.determineIfActorHasThisSectionAtThisPhase(afso.schema, 
					afso.sim_id, afso.actor_being_worked_on_id, afso.phase_id, cs.getId());
			
				if (!(hasItAlready) ) {
				
					String cs_class = "customized_section";
				
					if (cs.isThisIsACustomizedSection()){
						cs_class = "player_customized_section";
					}
					%>
            		<option value="<%= cs.getId().toString() %>" class="<%= cs_class %>" ><%= cs.getRec_tab_heading() %></option>
            	<% }  // End of if they don't have this section already at this phase         %>
            <% } // End of loop over customizable sections %>
           <% } // End of if list not null %>
                                  </select>
                                </blockquote>                          </td>
                          <td valign="top">Tab Heading: 
                            <input type="text" name="tab_heading" />
                            <input type="hidden" name="tab_pos" value="<%= afso.tempSimSecList.size() + 1 %>">
                            <br /> 
                                <input type="hidden" name="actor_index" value="<%= afso.getMyPSO_SectionMgmt().getCurrentActorIndex() %>">
                                <input type="hidden" name="actor_being_worked_on_id" value="<%= actor.getId() %>">
                                <input type="hidden" name="phase_id" value="<%= afso.phase_id.toString() %>">
                                <br />
                                <input type="submit" name="command" value="Add Section">
                                </td>
                        </tr>
                          </form>
                    </table></td>
                  </tr>
                  </table>
                
            <p align="center">Jump to Actor: <a href="set_universal_sim_sections.jsp?phase_id=<%= spp.getId().toString() %>">Every One </a>	
              
              <%
			
				int aIndex = 1;
				for (ListIterator li = thisSimsActors.listIterator(); li.hasNext();) {
							Actor daActor = (Actor) li.next();
			
			%>
              - <a href="set_specific_sim_sections.jsp?actor_index=<%= aIndex + "" %>&phase_id=<%= spp.getId().toString() %>"><%= daActor.getActorName() %></a>
              <% 
				aIndex += 1;
			} %>
              </p></td>
            </tr>
      </table>
      </blockquote>
      <p align="center"> 
        <% if (afso.getMyPSO_SectionMgmt().getCurrentActorIndex()< thisSimsActors.size()) {

	int nextIndex = afso.getMyPSO_SectionMgmt().getCurrentActorIndex()+ 1;
	
	String nextActorName = "to be determined";
	
	Actor nextActor = (Actor) thisSimsActors.get(nextIndex - 1);
	
%>
        <a href="set_specific_sim_sections.jsp?actor_index=<%= nextIndex %>&phase_id=<%= spp.getId().toString() %>"> 
          Next Step: Customize Sections for the Actor <strong><%= nextActor.getActorName() %></strong> </a> 
        <% } else { %>
        <a href="create_aar_starter_text.jsp"> Next Step: Enter 'After Action Report' Starter Text </a> 
        <% } %>
      </p>
      <% } else { // End of if have set simulation id. %>
      <blockquote> 
        <p> 
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>      <p>&nbsp;</p>			</td>
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