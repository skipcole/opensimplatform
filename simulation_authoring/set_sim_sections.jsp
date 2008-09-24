<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	pso.backPage = "set_sim_sections.jsp";
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	
	String _command = (String) request.getAttribute("command");
	String m_index = (String) request.getParameter("m_index");
	
	System.out.println("jsp command is = " + _command);
	System.out.println("m command is = " + m_index);
	
	Simulation simulation = new Simulation();
	Actor actor = new Actor();
	SimulationPhase spp = new SimulationPhase();
	
	if (pso.simulationSelected) {
		simulation = pso.handleSetSimSectionsPage(request);	
	
		if ((pso.actor_id != null) && (pso.actor_id.intValue() != 0 )){
			actor = pso.giveMeActor();
		}
	
		if (pso.currentActorIndex == 0){
			actor = new Actor();
			actor.setId(new Long(0));
			actor.setName("Typical");
		}
	
		if (pso.phase_id != null) {
			spp = pso.giveMePhase();
		}
	
	} // end of if pso.selected
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Control Page</title>
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" -->
<script language="JavaScript" type="text/JavaScript">
<!--
var tab_headings = new Array();
	var sec_descs = new Array();
	var the_ids = new Array();
	var the_sample_images = new Array();
	
	<%	

	for (ListIterator li = new BaseSimSection().getAll(pso.schema).listIterator(); li.hasNext();) {
			BaseSimSection bss = (BaseSimSection) li.next(); %>
	tab_headings["<%= bss.getId() %>"] = "<%= bss.getRec_tab_heading() %>";
	sec_descs["<%= bss.getId() %>"] = "<%= bss.getDescription() %>";
	the_ids["<%= bss.getId() %>"] = "<%= bss.getId() %>";
	the_sample_images["<%= bss.getId() %>"] = "<%= bss.getSample_image() %>";
	<% 

	   } 
	%>	
	
function loadFirstInfo(){

	window.document.section_form.tab_heading.value = tab_headings["1"];
	window.document.section_form.sec_desc.value = sec_descs["1"];
	window.document.section_form.the_id.value = the_ids["1"];
	window.document.getElementById('sample_image').src = "../simulation_section_information/images/" + the_sample_images["1"];
	
}

function loadInfo(dropdownlist){

	var myindex  = dropdownlist.selectedIndex;
    
	var passedvalue = dropdownlist.options[myindex].value;
	
	window.document.section_form.tab_heading.value = tab_headings[passedvalue];
	window.document.section_form.sec_desc.value = sec_descs[passedvalue];
	window.document.section_form.the_id.value = the_ids[passedvalue];
	window.document.getElementById('sample_image').src = "../simulation_section_information/images/" + the_sample_images[passedvalue];
	
	return true;

}

function our_goToURL() { //v3.0
  var i, args=our_goToURL.arguments; 
  
  document.MM_returnValue = false;
  
  // The page_id has 2 parts: the type of page and the id of the section
  
  var page_id = window.document.section_form.create_custom_page.value;
  
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"?page_id="+page_id+"'");
}
//-->
</script>
<!-- TemplateParam name="theBodyInfo" type="text" value="" --><!-- InstanceEndEditable -->
<link href="../usip_oscw.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="loadFirstInfo();" -->
</head>
<body onLoad="loadFirstInfo();">

<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="80%" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Set Simulation Sections</h1>
    <!-- InstanceEndEditable --></td>
    <td width="20%" align="right" valign="top"> 
		<% 
		String canEdit = (String) session.getAttribute("author");
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		<a href="../simulation_planning/index.jsp" target="_top">Think</a><br>
	    <a href="creationwebui.jsp" target="_top">Create</a><br>
		<a href="../simulation_facilitation/facilitateweb.jsp" target="_top">Play</a><br>
        <a href="../simulation_sharing/index.jsp" target="_top">Share</a>
		<% } %>
		</td>
  </tr>
</table>
<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
</tr>
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" -->
      <blockquote> 
        <% 
			if (pso.simulationSelected) {
		%>
        <p>Set the sections available to the actors in the simulation <strong><%= simulation.getDisplayName() %></strong>.<br>
          (If you would like to work on a different simulation, <a href="select_simulation.jsp">click 
          here</a>.)</p>
	  </blockquote>
		  <% if (simulation.getPhases().size() == 0) { %>
		  <blockquote>
		  
        <p>This simulation does not have any phases created. You must have created 
          at least one phase to be able to set the simulation sections that will 
          be displayed in a particular simulation phase. <a href="create_simulation_phases.jsp">Click 
          here</a> to create a simulation phase.</p>
		  </blockquote>
		  <% } else { %>
        
        <table width="100%" border="1">
          <tr>
            <td> <table width="100%" border="0">
                <tr> 
                  <td width="71%"> <h2> 
                      <% if (pso.currentActorIndex == 0) { %>
                    Every Actor 
                    <% } else { %>
                      Actor <strong>'<%= actor.getName() %>'</strong> 
                    <% } %>
					  in Phase <strong>'<%= spp.getName() %>' </strong> </h2></td>
                  <td width="29%">
				  <form id="form2" name="form2" method="post" action="set_sim_sections.jsp">
                      <select name="phase_id">
                        <% 
						
						MultiSchemaHibernateUtil.beginTransaction(pso.schema);
						
						Simulation da_sim = (Simulation) MultiSchemaHibernateUtil.getSession(pso.schema).get(Simulation.class, simulation.getId());
						
						for (ListIterator li = da_sim.getPhases().listIterator(); li.hasNext();) {
							SimulationPhase sp = (SimulationPhase) li.next();
							
							String selected_p = "";
							
							if (sp.getId().intValue() == pso.phase_id.intValue()) {
								selected_p = "selected";
							}
							
							
				%>
                        <option value="<%= sp.getId().toString() %>" <%= selected_p %>><%= sp.getName() %></option>
                        <% } 
						
						MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
						
						
						%>
                      </select>
                      <label>
					  <input type="hidden" name="actor_index" value="<%= pso.currentActorIndex  %>">
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
                  
                <td><!-- a href="set_sim_sections.jsp?exchange=true&first_sec=< % = first_ss %>&sec_sec=< % = sec_ss %>" --><a href="set_sim_sections.jsp?command=move_left&m_index=<%= ii %>">&lt;-</a><!-- /a--></td>
                  <% } %>
                  <td><a href="#"><%= ss.getTab_heading() %></a></td>
                  <% if (ii < (pso.tempSimSecList.size() - 1)) { %>
                  <td><a href="set_sim_sections.jsp?command=move_right&m_index=<%= ii %>">-&gt;</a></td>
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
                  <td><a href="delete_object.jsp?object_type=sim_section&amp;objid=<%= ss.getId().toString() %>&amp;backpage=set_sim_sections.jsp&amp;object_info=<%= ss.getTab_heading() %>">Remove </a></td>
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
                        <td width="50%" valign="top"><p>Select a Standard Section 
                            Below 
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
                              <% } %>
                            </select>
                          </blockquote>
                          </td>
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
                            <input type="hidden" name="actor_index" value="<%= pso.currentActorIndex  %>">
                            <input type="hidden" name="actor_id" value="<%= actor.getId() %>">
                            <input type="hidden" name="phase_id" value="<%= pso.phase_id.toString() %>">
                            <input type="text" name="the_id" id="the_id" value="" disabled="disabled" style="width:0" />
                            <br />
                            <input type="submit" name="command" value="Add Section">
                            </label>
                          </p></td>
                      </tr>
					  </form>
					  <form id="section_form" name="cust_lib_section_form" method="post" action="">
                      <tr>
                        <td valign="top"><p>or Pull from custom library</p>
                          <blockquote> 
                            <% 
								List cust_lib = CustomLibrarySection.getAll(pso.schema);
								
								if (cust_lib != null) {
							%>
                            <select name="select_cust_lib_page" onChange="loadInfo(window.document.section_form.select_cust_lib_page);">
                              <option value="0" selected="selected">Select from 
                              List</option>
                              <%
								//} else {
								
									for (ListIterator li = cust_lib.listIterator(); li.hasNext();) {
										CustomLibrarySection cls = (CustomLibrarySection) li.next();
									%>
                              <option value="<%= cls.getId().toString() %>"><%= cls.getRec_tab_heading() %></option>
                              <% } %>
                            </select>
                            <% } else { %>
                            <p>none</p>
                            <% } %>
                          </blockquote></td>
                        <td valign="top"><label> Tab Heading: 
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
                            <input type="hidden" name="actor_index" value="<%= pso.currentActorIndex  %>">
                            <input type="hidden" name="actor_id" value="<%= actor.getId() %>">
                            <input type="hidden" name="phase_id" value="<%= pso.phase_id.toString() %>">
                            <input type="text" name="the_id" id="the_id" value="" disabled="disabled" style="width:0" />
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
				for (ListIterator li = simulation.getActors().listIterator(); li.hasNext();) {
							Actor daActor = (Actor) li.next();
			
			%>
			- <a href="set_sim_sections.jsp?actor_index=<%= aIndex + "" %>&amp;phase_id=<%= spp.getId().toString() %>"><%= daActor.getName() %></a>
			<% 
				aIndex += 1;
			} %>
			  </p></td>
          </tr>
        </table>
      </blockquote>
      <p align="center"> 
        <% if (pso.currentActorIndex < simulation.getActors().size()) {

	int nextIndex = pso.currentActorIndex + 1;
	
	String nextActorName = "to be determined";
	
	Actor nextActor = (Actor) simulation.getActors().get(nextIndex - 1);
	
%>
        <a href="set_sim_sections.jsp?actor_index=<%= nextIndex %>&amp;phase_id=<%= spp.getId().toString() %>"> 
        Next Step: Customize Sections for the Actor <strong><%= nextActor.getName() %></strong> </a> 
        <% } else { %>
        <a href="create_aar_starter_text.jsp"> Next Step: Enter 'After Action Report' Starter Text </a> 
        <% } %>
      </p>
	  <% } // end of if there are no phases %>
      <% } else { // End of if have set simulation id. %>
      <blockquote> 
        <p> 
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>
      <p>&nbsp;</p>
<!-- InstanceEndEditable --></td>
  </tr>
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
</table>

<p>&nbsp;</p>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td align="left" valign="bottom"> 
	<% 
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
	<a href="intro.jsp" target="_top">Home 
      </a>
	  <% } else { %>
	  <a href="../simulation_facilitation/index.jsp" target="_top">Home 
      </a>
	  <% } %>
	  </td>
    <td align="right" valign="bottom"><a href="../simulation_user_admin/my_profile.jsp">My 
      Profile</a> </td>
  </tr>
  <tr>
    <td align="left" valign="bottom"><a href="logout.jsp" target="_top">Logout</a></td>
    <td align="right" valign="bottom">&nbsp;</td>
  </tr>
</table>
<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP Open Source Software Project</a>. </p>
</body>
<!-- InstanceEnd --></html>
<%
	
%>