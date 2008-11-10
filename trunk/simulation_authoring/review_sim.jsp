<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,java.io.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	pso.backPage = "review_sim.jsp";
	
	String loadSim = (String) request.getParameter("loadSim");
	if ((loadSim != null) && (loadSim.equalsIgnoreCase("true"))) {
		pso.sim_id = new Long((String) request.getParameter("sim_id"));
		pso.simulationSelected = true;
	}
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>USIP Open Simulation Platform</title>
<SCRIPT language=JavaScript1.2>
<!--
function showHide(name){
  if (document.all[name].style.display == "block") {
   document.all[name].style.display = "none";
  } else {
   document.all[name].style.display = "block";
  }
  return true;
}
-->
</SCRIPT>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
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
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">
<% String canEdit = (String) session.getAttribute("author"); %>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform </h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
	    <table border="0" cellspacing="1" cellpadding="0">
	<%  if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
        <tr>
          <td><div align="center"><a href="intro.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } else { %>
		<tr>
          <td><div align="center"><a href="../simulation_facilitation/instructor_home.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } %>	
        <tr>
          <td><div align="center"><a href="../simulation_user_admin/my_profile.jsp" class="menu_item"><img src="../Templates/images/my_profile.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
        <tr>
          <td><div align="center"><a href="logout.jsp" target="_top" class="menu_item"><img src="../Templates/images/logout.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
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
		
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		
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
  	<td width="120" align="right" valign="top">&nbsp;</td>
    <td colspan="1" valign="top"><br /></td>
    <td width="194" align="right" valign="top">		</td>
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
			<!-- InstanceBeginEditable name="pageTitle" --><h1>Review Simulation</h1><!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" -->
	  <% 
	  			
	boolean showEdit = false;
	
	if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) {
		showEdit = true;
	}
	  %>
	  
	  <blockquote>
        <% 
			if (pso.simulationSelected) {
		%>
        <p>Below is the review for the simulation <strong><%= simulation.getDisplayName() %></strong>.<br>
          (If you would like to look at a different simulation, <a href="select_simulation.jsp">click 
          here</a>.)</p>
      </blockquote>
	  
	<blockquote>
		<h2>1. Simulation Name: </h2>
			<blockquote>
			  <p><%= simulation.getDisplayName() %></p>
			  <p><% if (showEdit) { %>(<a href="create_simulation.jsp">edit</a>)<% } %></p>
			</blockquote>
			<blockquote><hr /></blockquote>
		<h2>2. Simulation Learning Objectives</h2>
			<blockquote>
			  <p><%= simulation.getLearning_objvs() %></p>
			  <p><% if (showEdit) { %>(<a href="create_simulation_objectives.jsp">edit</a>)<% } %></p>
			</blockquote>
			<blockquote><hr /></blockquote>
		<h2>3. Simulation Audience</h2>
			<blockquote>
			  <p><%= simulation.getAudience() %></p>
			  <p><% if (showEdit) { %>(<a href="create_simulation_audience.jsp">edit</a>)<% } %></p>
			</blockquote>
			<blockquote><hr /></blockquote>
		
		<h2>4. Simulation Introduction</h2>
			<blockquote>
			  <p><%= simulation.getIntroduction() %></p>
			  <p><% if (showEdit) { %>(<a href="create_simulation_introduction.jsp">edit</a>)<% } %></p>
			</blockquote>
			<blockquote><hr /></blockquote>
			
		<h2>5. Simulation Planned Play Ideas </h2>
			<blockquote>
			  <p><%= simulation.getPlanned_play_ideas() %></p>
			  <p><% if (showEdit) { %>(<a href="create_simulation_planned_play_ideas.jsp">edit</a>)<% } %></p>
			</blockquote>
			<blockquote><hr /></blockquote>
		
		<h2>6. Simulation Phases </h2>
			<blockquote>
			  <p>
			  
	<%
		for (ListIterator li = simulation.getPhases().listIterator(); li.hasNext();) {
			SimulationPhase sp = (SimulationPhase) li.next();
			
		%>
            
            <%= sp.getName() %><br />
            <%
	}
%>
			  
			  </p>
			  <p><% if (showEdit) { %>(<a href="create_simulation_phases.jsp">edit</a>)<% } %></p>
			</blockquote>
			<blockquote><hr /></blockquote>
		
		
		<h2>7. &amp; 8. Simulation Actors </h2>
		<blockquote>
			  <% for (ListIterator la = simulation.getActors().listIterator(); la.hasNext();) {
					Actor act = (Actor) la.next();
			  %><a href="javascript://" onClick="showHide('actor_desc_<%= act.getId() %>');"><%= act.getName() %> </a>
            <div id="actor_desc_<%= act.getId() %>"  style="display:none; padding:5px;">
          <blockquote>
            <strong>Public:</strong><br />
			<%= act.getPublic_description() %><hr />
			<strong>Semi-Public:</strong><br />
			<%= act.getSemi_public_description() %><hr />
			<strong>Private:</strong><br />
			<%= act.getPrivate_description() %>
          </blockquote>
          </div>
          <br />
		<% } // End of loop over Actors %>
        <br />
		  <% if (showEdit) { %>(<a href="create_actors.jsp">create another actor </a>)(<a href="assign_actor_to_simulation.jsp">assign another actor </a>)<% } %>
			  <hr />
        </blockquote>
        <h2>9. Simulation Special Features </h2>
		    <blockquote>
		      <p>&nbsp;</p>
		      <p><% if (showEdit) { %>(<a href="add_special_features.jsp">edit</a>)<% } %></p>
		      <hr />
        </blockquote>
		    <h2>10. Simulation Default Sections (for each phase) </h2>
		    <blockquote>
		<table width="80%" align="center">
       <%  int iIndex = 0;
	   
	   		for (ListIterator li = simulation.getPhases().listIterator(); li.hasNext();) {
				SimulationPhase sp = (SimulationPhase) li.next();
		%>
			<% if (iIndex == 0) { %>
				<TR valign="top">
			<% } %>
	            <TD width="30%">

				<table>
				<tr valign="top"><td valign="top" bgcolor="#44AAFF"> <%= sp.getName() %></td></tr>
				
				<%
					List secsList = SimulationSection.getBySimAndActorAndPhase(pso.schema, pso.sim_id, new Long(0), sp.getId());
					for (ListIterator secsli = secsList.listIterator(); secsli.hasNext();) {
						SimulationSection thisSecs = (SimulationSection) secsli.next();
				%>
				<tr valign="top"><td width="200">&nbsp;&nbsp;<%= thisSecs.getTab_heading() %> </td></tr>
				
				<%
					} // End of loop over sections
				%>
				</table>
				
				</TD>
				
			<% if (iIndex == 2) { %>
				</TR>
			<% } %>
			
			<% if (iIndex == 0) {
					iIndex = 1; 
				} else if (iIndex == 1){
					iIndex = 2;
				} else {
				iIndex = 0;
				}
			%>
		    <% } // End of loop over phases %>
			<% if (iIndex != 0) { 
				PrintWriter mywriter = response.getWriter();
				mywriter.println("</TR>");
			%>
				
			<% } %>
		</table>
		
		      <p><% if (showEdit) { %>(<a href="set_specific_sim_sections.jsp?actor_index=0">edit</a>)<% } %></p>
		      <hr />
          </blockquote>
		    <h2>11. Simulation Specific Sections </h2>
		    <blockquote>
		      <p>
               <% for (ListIterator bss = SimulationSection.getDistinctSectionsForSim(pso.schema, pso.sim_id).listIterator(); bss.hasNext();) {
					BaseSimSection this_base = (BaseSimSection) bss.next();
					
			  	if (this_base instanceof CustomizeableSection) { 
					CustomizeableSection css = (CustomizeableSection) this_base;
					%>
                    <a href="javascript://" onClick="showHide('custom_sim_desc_<%= css.getId() %>')"><%= css.getRec_tab_heading() %></a><br />
                    <div id="custom_sim_desc_<%= css.getId() %>"  style="display:none; padding:5px;">
                    <blockquote>
			  		<%= css.getBigString() %>
                    </blockquote>
                    </div>
                    <% } // end of if CustomizeableSection %>
			  
			  <%
			  	} // End of loop over sim sections.
				%>
              </p>
		      <p><% if (showEdit) { %>(<a href="create_simulation_objectives.jsp">edit</a>)<% } %></p>
		      <hr />
          </blockquote>
		    <h2>12. Simulation 'After Action Report' Starter Text </h2>
		    <blockquote>
		      <p><%= simulation.getAar_starter_text() %></p>
		      <p><% if (showEdit) { %>(<a href="create_aar_starter_text.jsp">edit</a>)<% } %></p>
		      <hr />
	      </blockquote>
		    <p>&nbsp;</p>
		    <blockquote>
			<% if (showEdit) { %>
		  <p align="center"><a href="../simulation_facilitation/facilitateweb.jsp" target="_top">On 
            To Play Test the Simulation</a></p>
          <p align="center"><a href="publish_sim.jsp">On To Publish the Simulation</a></p>
		  <% } // end of if show edit. %>
		</blockquote>
	</blockquote>
		  <% } else { // End of if have set simulation id. %>
      <blockquote>
		<%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>
      <% if (showEdit) { %><a href="create_aar_starter_text.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a><% } else { %>
	  	<a href="../simulation_facilitation/instructor_home.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>
		<% } %>
	  
	  <!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
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
<!-- InstanceEnd --></html>
<%
	
%>