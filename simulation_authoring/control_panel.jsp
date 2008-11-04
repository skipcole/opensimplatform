<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.oscw.baseobjects.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*" errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	pso.backPage = "control_panel.jsp";
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();
	
	if (pso.sim_id != null){
		System.out.println("loading sim " + pso.sim_id);
		simulation = pso.giveMeSim();
	}
	

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Control Page</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
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
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Simulation Author Control Panel</h1>
      <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
      <table width="100%" border="0" cellspacing="2" cellpadding="1">
        <tr valign="top"> 
          <td colspan="2"><h2>Step</h2></td>
          </tr>
        <tr valign="top"> 
          <td width="11%">1.</td>
          <td width="89%"><a href="create_simulation.jsp" target="bodyinleft">Create 
            New Simulation </a><a href="helptext/create_simulation_help.jsp" target="helpinright">(?)</a></td>
          </tr>
        <tr valign="top"> 
          <td>2.</td>
          <td><a href="create_simulation_objectives.jsp">Enter Learning Objectives </a><a href="helptext/create_objectives_help.jsp" target="helpinright">(?)</a></td>
          </tr>
        <tr valign="top"> 
          <td>3.</td>
          <td><a href="create_simulation_audience.jsp">Enter Your Audience </a><a href="helptext/create_audience_help.jsp" target="helpinright">(?)</a></td>
          </tr>
        <tr valign="top"> 
          <td>4.</td>
          <td><a href="create_simulation_introduction.jsp">Enter Introduction</a><a href="create_simulation_audience.jsp"> </a><a href="helptext/create_introduction_help.jsp" target="helpinright">(?)</a></td>
          </tr>
        <tr valign="top"> 
          <td>5.</td>
          <td><a href="create_simulation_planned_play_ideas.jsp">Enter Planned Play Ideas </a><a href="create_simulation_audience.jsp"> </a><a href="helptext/enter_plans_help.jsp" target="helpinright">(?)</a></td>
          </tr>
        <tr valign="top"> 
          <td>6.</td>
          <td><a href="create_simulation_phases.jsp">Create Phases</a><a href="create_simulation_objectives.jsp"> </a><a href="helptext/create_phases_help.jsp" target="helpinright">(?)</a></td>
          </tr>
        <tr valign="top"> 
          <td>7.</td>
          <td><a href="create_actors.jsp">Create Actors </a><a href="helptext/create_actors_help.jsp" target="helpinright">(?)</a></td>
          </tr>
        <tr valign="top"> 
          <td>8.</td>
          <td><a href="assign_actor_to_simulation.jsp">Assign Actors </a><a href="helptext/assign_actors_help.jsp" target="helpinright">(?)</a></td>
          </tr>
        <tr valign="top">
          <td>9.</td>
          <td><a href="create_injects.jsp">Create Injects </a><a href="create_simulation_objectives.jsp"> </a><a href="helptext/create_injects_help.jsp" target="helpinright">(?)</a></td>
          <tr valign="top"> 
          <td>10.</td>
          <td><a href="add_special_features.jsp">Add Special Features</a><a href="create_aar_starter_text.jsp"> </a><a href="create_injects.jsp"> </a><a href="create_simulation_objectives.jsp"> </a><a href="helptext/create_special_features_help.jsp" target="helpinright">(?)</a></td>
          <tr valign="top"> 
          <td>11.</td>
          <td><a href="set_universal_sim_sections.jsp?actor_index=0">Set Universal 
            Sections</a><a href="create_aar_starter_text.jsp"> </a><a href="create_injects.jsp"> </a><a href="create_simulation_objectives.jsp"> </a><a href="helptext/create_univ_sections_help.jsp" target="helpinright">(?)</a></td>
          </tr>
        <tr valign="top"> 
          <td>12.</td>
          <td><a href="set_sim_sections.jsp">Assign Specific Sections </a><a href="helptext/create_individual_sections_help.jsp" target="helpinright">(?)</a></td>
          </tr>
        <tr valign="top"> 
          <td>13</td>
          <td><a href="create_aar_starter_text.jsp">Enter 'After Action Report' 
            Start Text </a><a href="create_injects.jsp"> </a><a href="create_simulation_objectives.jsp"> </a><a href="helptext/create_aar_help.jsp" target="helpinright">(?)</a></td>
          </tr>
        <tr valign="top"> 
          <td>14.</td>
          <td><a href="review_sim.jsp">Review Simulation</a></td>
          </tr>
        <tr valign="top">
          <td>15.</td>
          <td><a href="publish_sim.jsp">Publish Simulation</a></td>
          </tr>
      </table>
      <p align="center">
	  <% 
			if (pso.simulationSelected) {
			
				String gameNameDisplay = simulation.getName() + " version " + simulation.getVersion();

		%>
		You are currently working on the simulation <strong><%= gameNameDisplay %></strong>.<br>
		(If you would like to work on a different simulation, <a href="select_simulation.jsp">click 
          here</a>.)
		<% } else { // End of if have set simulation id. %>
      <div align="center">
        <blockquote>&nbsp; </blockquote>
      </div>
      <blockquote><p align="center">
		No simulation currently selected for editing. </p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>
		 
      </p>
      <p>&nbsp;</p>
<!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
<%
	
%>