<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.coursemanagementinterface.*,	
	org.hibernate.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = "../simulation_facilitation/view_running_sim.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>USIP Open Simulation Platform</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			  <h1>View Running Simulations</h1>
			  <br />
            <blockquote> 

              <h2>Simulations Where You are an Instructor </h2>
              <p>Below are the running simulations where you are a designated instructor. Select the name of the running simulation to monitor or edit it. </p>
              <table width="80%" border = "1">
                <tr>
                  <td><h2>Simulation</h2></td> 
                  <td><h2>Running Simulation</h2></td>
              <td><h2>Phase</h2></td>
            </tr>
                <%
				
				for (ListIterator lis = Simulation.getAll(afso.schema).listIterator(); lis.hasNext();) {
				Simulation sim = (Simulation) lis.next();
				afso.sim_id = sim.getId();
				
		  	List rsList = RunningSimulation.getAllForSim(afso.sim_id.toString(), afso.schema);
			
			for (ListIterator li = rsList.listIterator(); li.hasNext();) {
				RunningSimulation rs = (RunningSimulation) li.next();
				
				SimulationPhase sp = new SimulationPhase();
				if (rs.getPhase_id() != null){
					sp = SimulationPhase.getById(afso.schema, rs.getPhase_id().toString());
				}
				
				if (InstructorRunningSimAssignments.checkIsInstructor(afso.user_id, afso.schema, rs.getId())){ 
				
		%>
                <tr>
                  <td><%= sim.getDisplayName() %></td> 
                  <td><a href="administrate_running_simulation.jsp?rs_id=<%= rs.getId() %>"><%= rs.getRunningSimulationName() %></a></td>
              <td><%= sp.getPhaseName() %></td>
            </tr>
                <%
					} // End of if this user is a designated instructor.
				} // End of loop over Running Sims
			} // End of loop over sims
		%>
                </table>
	          <p>&nbsp;</p>
	          <h2>Simulations Where You are a Player </h2>
	          <p>Below are all of the simulations in which you have been assigned as a player. You can log in as a player to access any of these. </p>
	          <table width="80%" border="2" cellspacing="2" cellpadding="2">
              <tr valign="top">
      <td width="30%"><h2><%= USIP_OSP_Cache.getInterfaceText(request, afso.languageCode, "simulation") %></h2></td>
      <td width="35%"><h2>Running Simulation</h2></td>
      <td width="15%"><h2><%= USIP_OSP_Cache.getInterfaceText(request, afso.languageCode, "your_role") %></h2></td>
      <td width="10%"><h2><%= USIP_OSP_Cache.getInterfaceText(request, afso.languageCode, "phase") %></h2></td>
      <!-- td width="10%"><h2><%= USIP_OSP_Cache.getInterfaceText(request, afso.languageCode, "play") %></h2></td  -->
    </tr>
              <%
  
  		List uaList = UserAssignment.getAllForUser(afso.schema, afso.user_id);
	
		for (ListIterator li = uaList.listIterator(); li.hasNext();) {
			UserAssignment ua = (UserAssignment) li.next();
			
			Simulation sim = Simulation.getById(afso.schema, ua.getSim_id());
			RunningSimulation rs = RunningSimulation.getById(afso.schema,ua.getRunning_sim_id());
			Actor act = Actor.getById(afso.schema, ua.getActor_id());
			
			SimulationPhase sp = SimulationPhase.getById(afso.schema, rs.getPhase_id());
			
			// Must check to see that running sim has been enabled, and has not been inactivated.
			if ((rs.isReady_to_begin()) && (!(rs.isInactivated()))) {
			
			if ((act != null) && (sp != null)){
  %>
              <tr valign="top">
      <td><%= sim.getDisplayName() %></td>
      <td><%= rs.getRunningSimulationName() %></td>
      <td><%= act.getActorName(afso.schema, rs.getId(), request) %></td>
      <td><%= sp.getPhaseName() %></td>
      <!-- td> <form action="../simulation/select_simulation_to_play.jsp" method="post" name="form1" id="form1">
      
        <input type="submit" name="Submit" value="<%= USIP_OSP_Cache.getInterfaceText(request, afso.languageCode, "play") %> > " />
        <input type="hidden" name="user_assignment_id" value="<%= ua.getId() %>" />
        <input type="hidden" name="schema" value="<%= afso.schema %>" />
        <input type="hidden" name="schema_org" value="<%= afso.simulation_org %>" />
        <input type="hidden" name="sending_page" value="select_simulation" />
        </form></td  -->
    </tr>
				  <%
			  
			  } // End of if act or sp was null
			  } // End of if running simulation has been enabled.
			  
  		} // End of loop over User Assignments
		
  %>
	</table>
            </blockquote>

            <p>&nbsp;</p>
		</td>
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
