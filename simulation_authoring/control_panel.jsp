<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.io.*,
	java.util.*,
	java.text.*,
	java.sql.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	afso.backPage = "control_panel.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();
	
	if (afso.sim_id != null){
		System.out.println("loading sim " + afso.sim_id);
		simulation = afso.giveMeSim();
	}
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />

</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Simulation Author Control Panel</h1>
              <blockquote>
                <p align="left">
                  <% 
			if (afso.sim_id != null) {
			
				String gameNameDisplay = simulation.getSimulationName() + " version " + simulation.getVersion();

		%>
                  You are currently working on the simulation <strong><%= gameNameDisplay %></strong>.</p>
                <blockquote>
                  <p align="left"> If you would like to work on a different simulation, <a href="select_simulation.jsp">click 
                    here</a>.<br />
                    To create a new simulation, <a href="create_simulation.jsp?clear=true">click here</a>.
                        <% } else { // End of if have set simulation id. %>
                  </p>
                </blockquote>
              
                <p align="left"> No simulation currently selected for editing. </p>
                <blockquote>
                  <p align="left">To select a simulation to work on, <a href="select_simulation.jsp">click 
                    here</a>.<br />
                    To create a new simulation, <a href="create_simulation.jsp?clear=true">click here</a>. </p>
                  </blockquote>
              </blockquote>
              <% } // End of if have not set simulation for edits. %>
              </p>
<br />
      <table width="100%" border="0" cellspacing="2" cellpadding="1">
        <tr valign="top"> 
          <td colspan="3"><h2>Step</h2></td>
            </tr>
        <tr valign="top">
          <td width="3%">1.</td>
          <td width="36%"><a href="enter_basic_simulation_information.jsp">Edit Sim Basic Data </a></td>
		  <td width="61%" rowspan="14" valign="top"><p>Following the steps on the left will enable you to create your simulation.</p>
              <p>You can either work through the steps sequentially, or skip around amongst them after having completed the first step. Creating a simulation generally a requires many hours of work, but this can be spread out over as many days as you wish. Any information that you add here can always be modified  later. </p>
              <p>Good luck in your work! </p>
              <p>The USIP OSP Team </p>
              <p>&nbsp; </p></td>
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
            <td><a href="add_objects.jsp">Add Objects</a>   (?)</td>
            </tr>
        <tr valign="top"> 
              <td>10.</td>
            <td><a href="set_universal_sim_sections.jsp?actor_index=0">Set Universal 
              Sections</a><a href="create_aar_starter_text.jsp"> </a><a href="injects.jsp"> </a><a href="create_simulation_objectives.jsp"> </a><a href="helptext/create_univ_sections_help.jsp" target="helpinright">(?)</a></td>
            </tr>
        <tr valign="top"> 
          <td>11.</td>
            <td><a href="set_specific_sim_sections.jsp">Assign Specific Sections </a><a href="helptext/create_individual_sections_help.jsp" target="helpinright">(?)</a></td>
            </tr>
        <tr valign="top"> 
          <td>12</td>
            <td><a href="create_aar_starter_text.jsp">Enter 'After Action Report' 
              Start Text </a><a href="injects.jsp"> </a><a href="create_simulation_objectives.jsp"> </a><a href="helptext/create_aar_help.jsp" target="helpinright">(?)</a></td>
            </tr>
        <tr valign="top"> 
          <td>13.</td>
            <td><a href="review_sim.jsp">Review Simulation</a></td>
            </tr>
        <tr valign="top">
          <td>14.</td>
            <td><a href="publish_sim.jsp">Publish Simulation</a></td>
            </tr>
      </table>
      <p>&nbsp;</p>			</td>
		</tr>
		</table>	</td>
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
</html>
<%
	
%>