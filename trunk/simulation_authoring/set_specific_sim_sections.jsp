<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	pso.backPage = "set_specific_sim_sections.jsp";
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();
	Actor actor = new Actor();
	SimulationPhase spp = new SimulationPhase();
	List thisSimsActors = new ArrayList();
	
	if (pso.simulationSelected) {
		simulation = pso.handleSetSimSectionsPage(request);	
		
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxx");
		System.out.println("actor id is " + pso.actor_being_worked_on_id);
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxx");
	
		if ((pso.actor_being_worked_on_id != null) && (pso.actor_being_worked_on_id.intValue() != 0 )){
			actor = pso.giveMeActor(pso.actor_being_worked_on_id);
		}
	
		if (pso.phase_id != null) {
			spp = pso.giveMePhase();
		}
		
		thisSimsActors = simulation.getActors(pso.schema);
	
	} // end of if pso.selected
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Control Page</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->
<script language="JavaScript" type="text/JavaScript">
<!--
var tab_headings = new Array();
	var sec_descs = new Array();
	var the_sample_images = new Array();
	
	<%	

	for (ListIterator li = new BaseSimSection().getAllAndChildren(pso.schema).listIterator(); li.hasNext();) {
			BaseSimSection bss = (BaseSimSection) li.next(); %>
	tab_headings["<%= bss.getId() %>"] = "<%= bss.getRec_tab_heading() %>";
	sec_descs["<%= bss.getId() %>"] = "<%= bss.getDescription() %>";
	the_sample_images["<%= bss.getId() %>"] = "<%= bss.getSample_image() %>";
	<% 

	   } 
	%>	
	
function loadFirstInfo(){

	window.document.section_form.tab_heading.value = tab_headings["1"];
	window.document.section_form.sec_desc.value = sec_descs["1"];
	window.document.getElementById('sample_image').src = "../simulation_section_information/images/" + the_sample_images["1"];
	
}

function loadInfo(dropdownlist){

	var myindex  = dropdownlist.selectedIndex;
    
	var passedvalue = dropdownlist.options[myindex].value;
	
	window.document.section_form.tab_heading.value = tab_headings[passedvalue];
	window.document.section_form.sec_desc.value = sec_descs[passedvalue];
	window.document.getElementById('sample_image').src = "../simulation_section_information/images/" + the_sample_images[passedvalue];
	
	return true;

}

//-->
</script>
<!-- TemplateParam name="theBodyInfo" type="text" value="" --><!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(../Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="loadFirstInfo();" -->
</head>
<body onLoad="loadFirstInfo();">
<%
	String myLogoutPage = "../simulation/logout.jsp";
	if ( (pso.isAuthor())  || (pso.isFacilitator())) {
		myLogoutPage = "../simulation_authoring/logout.jsp";
	}
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform</h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
	    <table border="0" cellspacing="1" cellpadding="0">
	<%  if (pso.isAuthor()) { %>
        <tr>
          <td><div align="center"><a href="intro.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } else if (pso.isFacilitator()) { %>
		<tr>
          <td><div align="center"><a href="../simulation_facilitation/instructor_home.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } %>	
        <tr>
          <td><div align="center"><a href="../simulation_user_admin/my_profile.jsp" class="menu_item"><img src="../Templates/images/my_profile.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
        <tr>
          <td><div align="center"><a href="<%= myLogoutPage %>" target="_top" class="menu_item"><img src="../Templates/images/logout.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
      </table>	  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"><% 
		
		String bgColor_think = "#475DB0";
		String bgColor_create = "#475DB0";
		String bgColor_play = "#475DB0";
		String bgColor_share = "#475DB0";
		
		pso.findPageType(request);
		
		if (pso.page_type == 1){
			bgColor_think = "#9AABE1";
		} else if (pso.page_type == 2){
			bgColor_create = "#9AABE1";
		} else if (pso.page_type == 3){
			bgColor_play = "#9AABE1";
		} else if (pso.page_type == 4){
			bgColor_share = "#9AABE1";
		}
		
		if (pso.isAuthor()) { %>
		
		<table border="0" cellpadding="0" cellspacing="0" >
		   <tr>
		<td bgcolor="<%= bgColor_think %>"><a href="../simulation_planning/index.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;THINK&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
	    <td bgcolor="<%= bgColor_create %>"><a href="creationwebui.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;CREATE&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
		<td bgcolor="<%= bgColor_play %>"><a href="../simulation_facilitation/facilitateweb.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;PLAY&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
        <td bgcolor="<%= bgColor_share %>"><a href="../simulation_sharing/index.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;SHARE&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		   </tr>
		</table>
	<% } %></td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top"></td>
    <td colspan="1" valign="top"></td>
    <td width="194" align="right" valign="top"></td>
  </tr>
</table>
<BR />
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Set Specific Simulation Sections</h1>
    <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" -->
      <blockquote> 
        <% 
			if (pso.simulationSelected) {
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
                      Actor <strong>'<%= actor.getName() %>'</strong> 
					  in Phase <strong>'<%= spp.getName() %>' </strong> </h2></td>
                  <td width="29%">
				  <form id="form2" name="form2" method="post" action="set_specific_sim_sections.jsp">
                      <select name="phase_id">
                        <% 
						
						for (ListIterator li = SimPhaseAssignment.getPhasesForSim(pso.schema, simulation.getId()).listIterator(); li.hasNext();) {
							SimulationPhase sp = (SimulationPhase) li.next();
							
							String selected_p = "";
							
							if (sp.getId().intValue() == pso.phase_id.intValue()) {
								selected_p = "selected";
							}
							
							
				%>
                        <option value="<%= sp.getId().toString() %>" <%= selected_p %>><%= sp.getName() %></option>
                        <% } 	%>
                      </select>
                      <label>
					  <input type="hidden" name="actor_index" value="<%= pso.getMyPSO_SectionMgmt().getCurrentActorIndex()  %>">
					  <input type="hidden" name="actor_id" value="<%= actor.getId() %>">
                      <input type="submit" name="command" value="Change Phase" />
                      </label>
                  </form>
				  </td>
                </tr>
              </table>
              <p>&nbsp;</p>
              <%
			if (pso.tempSimSecList.size() > 0) {
		%> <table  border="1" cellspacing="2" cellpadding="1">
                <tr> 
                  <%
		  	int ii = 0;
			// Need set of ids by position to set the exchange going to the right.
			Hashtable idByPos = new Hashtable();
			
			////////////////////////////////////////////////////
			for (ListIterator li = pso.tempSimSecList.listIterator(); li.hasNext();) {
				SimulationSection ss = (SimulationSection) li.next();
				
				idByPos.put(ii + "", ss.getId());
				
				++ii;
			}
			////////////////////////////////////////////////////
			
			ii = 0;
			int first_ss = 0;
			
			for (ListIterator li = pso.tempSimSecList.listIterator(); li.hasNext();) {
				SimulationSection ss = (SimulationSection) li.next();
				
				int sec_ss = ss.getId().intValue();
				
		  %>
                  <% if (ii > 0) { %>
                  
                <td><!-- a href="set_specific_sim_sections.jsp?exchange=true&first_sec=< % = first_ss %>&sec_sec=< % = sec_ss %>" --><a href="set_specific_sim_sections.jsp?command=move_left&amp;m_index=<%= ii %>">&lt;-</a><!-- /a--></td>
                  <% } %>
                  <td><a href="#"><%= ss.getTab_heading() %></a></td>
                  <% if (ii < (pso.tempSimSecList.size() - 1)) { %>
                  <td><a href="set_specific_sim_sections.jsp?command=move_right&amp;m_index=<%= ii %>">-&gt;</a></td>
                  <% } %>
                  <%
				++ii;
			
				first_ss = ss.getId().intValue();
				} // End of loop over simulation sections
			%>
                <tr> 
                  <%
		  	ii = 0;
			
			for (ListIterator li = pso.tempSimSecList.listIterator(); li.hasNext();) {
				SimulationSection ss = (SimulationSection) li.next();
		  %>
                  <% if (ii > 0) { %>
                  <td>&nbsp;</td>
                  <% } %>
                  <td><a href="delete_object.jsp?object_type=sim_section&amp;objid=<%= ss.getId().toString() %>&backpage=set_specific_sim_sections.jsp&object_info=<%= ss.getTab_heading() %>">Remove </a></td>
                  <% if (ii < (pso.tempSimSecList.size() - 1)) { %>
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
			  <p align="center">
			  <img name="sample_image" id="sample_image" src="../simulation_section_information/images/sample.png" width="300" height="240" />
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
							
		for (ListIterator li = new BaseSimSection().getAll(pso.schema).listIterator(); li.hasNext();) {
			BaseSimSection bss = (BaseSimSection) li.next();
			%>
                              <option value="<%= bss.getId() %>"><%= bss.getRec_tab_heading() %></option>
                              <% } %>
                              <option value="new_section">* Create an Entirely 
                              New Section</option>
                              <% 
								List uc = CustomizeableSection.getAllUncustomized(pso.schema);
								
								if (uc != null) {
							%>
                              <% 
			for (ListIterator li = uc.listIterator(); li.hasNext();) {
				CustomizeableSection cs = (CustomizeableSection) li.next();
			%>
                              <option value="<%= cs.getId().toString() %>"><%= cs.getRec_tab_heading() %></option>
                              <% } %>
                              <% } // End of loop over customizable sections	
							  
							  List cust_lib = CustomLibrarySection.getAll(pso.schema);
							  
							  if (cust_lib != null) {  
							  for (ListIterator li = cust_lib.listIterator(); li.hasNext();) {
										CustomLibrarySection cls = (CustomLibrarySection) li.next();
									%>
                              <option value="<%= cls.getId().toString() %>"><%= cls.getRec_tab_heading() %></option>
                              <% } // End of loop over custom library sections
							  	} // End of if cust_lib != null
							  %>
                            </select>
                          </blockquote>                          </td>
                        <td valign="top"> <label> Tab Heading: 
                          <input type="text" name="tab_heading" />
                          <br />
                          Tab Position: 
                          <select name="tab_pos">
                            <% for (int tp = 1; tp <= pso.tempSimSecList.size() + 1; ++tp) { %>
                            <option value="<%= tp %>"><%= tp %></option>
                            <% } %>
                          </select>
                          </label> <p> 
                            <label> 
                            <textarea name="sec_desc" id="sec_desc" cols="40" rows="4" disabled="disabled">Section Description</textarea>
                            <input type="hidden" name="actor_index" value="<%= pso.getMyPSO_SectionMgmt().getCurrentActorIndex() %>">
                            <input type="hidden" name="actor_id" value="<%= actor.getId() %>">
                            <input type="hidden" name="phase_id" value="<%= pso.phase_id.toString() %>">
                            <br />
                            <input type="submit" name="command" value="Add Section">
                            </label>
                          </p></td>
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
			- <a href="set_specific_sim_sections.jsp?actor_index=<%= aIndex + "" %>&phase_id=<%= spp.getId().toString() %>"><%= daActor.getName() %></a>
			<% 
				aIndex += 1;
			} %>
			  </p></td>
          </tr>
        </table>
      </blockquote>
      <p align="center"> 
        <% if (pso.getMyPSO_SectionMgmt().getCurrentActorIndex()< thisSimsActors.size()) {

	int nextIndex = pso.getMyPSO_SectionMgmt().getCurrentActorIndex()+ 1;
	
	String nextActorName = "to be determined";
	
	Actor nextActor = (Actor) thisSimsActors.get(nextIndex - 1);
	
%>
        <a href="set_specific_sim_sections.jsp?actor_index=<%= nextIndex %>&phase_id=<%= spp.getId().toString() %>"> 
        Next Step: Customize Sections for the Actor <strong><%= nextActor.getName() %></strong> </a> 
        <% } else { %>
        <a href="create_aar_starter_text.jsp"> Next Step: Enter 'After Action Report' Starter Text </a> 
        <% } %>
      </p>
      <% } else { // End of if have set simulation id. %>
      <blockquote> 
        <p> 
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>
      <p>&nbsp;</p>
<!-- InstanceEndEditable -->
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