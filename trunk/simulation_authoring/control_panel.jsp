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
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_oscw.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="80%" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Simulation Author Control Panel</h1>
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
      <table width="100%" border="0" cellspacing="2" cellpadding="1">
        <tr valign="top"> 
          <td colspan="2"><h2>Step</h2></td>
          <td><h2>&nbsp;</h2></td>
          <td><h2>Description</h2></td>
        </tr>
        <tr valign="top"> 
          <td width="3%">1.</td>
          <td width="25%"><a href="create_simulation.jsp" target="bodyinleft">Create 
            New Simulation</a></td>
          <td width="2%">&nbsp;</td>
          <td width="70%">For example, &quot;Dividing Oranges&quot; or &quot;Oil 
            Curse &quot; </td>
        </tr>
        <tr valign="top"> 
          <td>2.</td>
          <td><a href="create_simulation_objectives.jsp">Enter Learning Objectives</a></td>
          <td>&nbsp;</td>
          <td>Record here what you are trying to teach.</td>
        </tr>
        <tr valign="top"> 
          <td>3.</td>
          <td><a href="create_simulation_audience.jsp">Enter Your Audience</a></td>
          <td>&nbsp;</td>
          <td>Record here for whom you think this simulation will be useful.</td>
        </tr>
        <tr valign="top"> 
          <td>4.</td>
          <td><a href="create_simulation_introduction.jsp">Enter Introduction</a></td>
          <td>&nbsp;</td>
          <td>The introductory text that is read by all participants.</td>
        </tr>
        <tr valign="top"> 
          <td>5.</td>
          <td><a href="psp.jsp">Enter Planned Play Ideas </a></td>
          <td>&nbsp;</td>
          <td>Plans for how the simulation may be run. </td>
        </tr>
        <tr valign="top"> 
          <td>6.</td>
          <td><a href="create_simulation_phases.jsp">Create Phases</a></td>
          <td>&nbsp;</td>
          <td>Create the phases that the simulation will go through, for example 
            &quot;in progress&quot; and &quot;done.&quot; Each phase can present 
            a different set of options and views for each of the actors present 
            in it. </td>
        </tr>
        <tr valign="top"> 
          <td>7.</td>
          <td><a href="create_actors.jsp">Create Actors</a></td>
          <td>&nbsp;</td>
          <td>For example, &quot;President&quot; or &quot;Reporter&quot;</td>
        </tr>
        <tr valign="top"> 
          <td>8.</td>
          <td><a href="assign_actor_to_simulation.jsp">Assign Actors</a></td>
          <td>&nbsp;</td>
          <td>For example, assigning &quot;President&quot; to the simulation &quot;Oil 
            Curse &quot; </td>
        </tr>
        <tr valign="top">
          <td>9.</td>
          <td><a href="create_injects.jsp">Create Injects</a></td>
          <td>&nbsp;</td>
          <td>Injects (also called events) are things that are planned to happen during a simulation. For example, at a certain point in time or due to certain conditions present in the simulation, a terrorist attack may occur.</td>
        <tr valign="top"> 
          <td>10.</td>
          <td><a href="add_special_features.jsp">Add Special Features</a></td>
          <td>&nbsp;</td>
          <td>Special features, which continue to be developed, include adding 
            a shared document for the actors, or values and special actions they 
            can make.</td>
        <tr valign="top"> 
          <td>11.</td>
          <td><a href="set_universal_sim_sections.jsp?actor_index=0">Set Universal 
            Sections</a></td>
          <td>&nbsp;</td>
          <td>Select sections that every player will have in each phase of this 
            simulation. Each section provides a place were the player can see 
            and/or do something. One can think of a section as a tab across the 
            top of the player's screen. </td>
        </tr>
        <tr valign="top"> 
          <td>12.</td>
          <td><a href="set_sim_sections.jsp">Assign Specific Sections</a></td>
          <td>&nbsp;</td>
          <td>Actors can each have a unique set of sections. For example in an 
            economic simulation, some players may be able to manipulate variables 
            such as tariff rates, and all players may be able to see their countries 
            basic economic statistics. These abilities will all be handled via 
            a different simulation 'section.' </td>
        </tr>
        <tr valign="top"> 
          <td>13</td>
          <td><a href="create_aar_starter_text.jsp">Enter 'After Action Report' 
            Start Text </a></td>
          <td>&nbsp;</td>
          <td>The 'After Action Report' (AAR) will need to be tailored to each 
            group of players. It is impossible to predict exactly how they will 
            do. But common phrases or thoughts can be stored here to aid the simulation 
            facilitator. </td>
        </tr>
        <tr valign="top"> 
          <td>14.</td>
          <td><a href="review_sim.jsp">Review Simulation</a></td>
          <td>&nbsp;</td>
          <td>A synopsis of your simulation.</td>
        </tr>
        <tr valign="top">
          <td>15.</td>
          <td><a href="publish_sim.jsp">Publish Simulation</a></td>
          <td>&nbsp;</td>
          <td>Here you can mark your simulation as ready to go. This will make 
            it accessible to facilitators to beging live training sessions.</td>
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