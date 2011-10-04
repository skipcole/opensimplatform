<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.coursemanagementinterface.*,
	org.usip.osp.persistence.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	// Get the running sim first, so if the last sim they edited was a different simulation, 
	// We can set the simulation to be the one that the running sim is from.
	RunningSimulation running_simulation = new RunningSimulation();
	if (afso.getRunningSimId() != null){
		running_simulation = afso.giveMeRunningSim();
		afso.sim_id = running_simulation.getSim_id();
		// TODO - Open questions do we change the 'last sim edit field' ?
	}
	
	Simulation simulation = new Simulation();
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Open Simulation Platform Control Page</title>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
              <tr>
                <td width="120"><img src="../Templates/images/white_block_120.png" /></td>
                <td width="100%"><br />
                  <h1>Welcome!  </h1>
   
			  <% if (afso.getRunningSimId() != null) { %>
				<strong>Your Dasbhoard showing participants in Running Simulation <a href="facilitate_panel.jsp"><%= running_simulation.getRunningSimulationName() %></a> </strong> <br/><br />
			<blockquote>				
				<table width="100%" border="1">
        <tr>
          <td valign="top"><strong>Student Name </strong></td>
          <td valign="top"><strong>Actor</strong></td>
          <td valign="top"><strong>Username</strong></td>
          <td valign="top"><strong>Status</strong></td>
          </tr>
		<% 
		
			List dashboardLines = StudentDashboardLine.getDashboardLines(afso.sim_id, afso.schema, afso.runningSimId);
			
			// Loop over all actors in the simulation
			for (ListIterator li = dashboardLines.listIterator(); li.hasNext();) {
				StudentDashboardLine sdl = (StudentDashboardLine) li.next();

					%>
        <tr>
          <td valign="top">
		  <% if (sdl.getStudentId() != null) { %>
		  		<a href="../simulation_user_admin/view_player_profile.jsp?student_id=<%= sdl.getStudentId() %>">
		  		<%= sdl.getStudentName() %>		  		</a>
		  <% } else { %>
		  	<%= sdl.getStudentName() %>
		  <% } %>		  </td>
          <td valign="top"><%= sdl.getStudentRole() %></td>
          <td valign="top"><%= sdl.getStudentEmail() %></td>
          <td valign="top" bgcolor="#<%= sdl.getStudentStatusColor() %>"><%= sdl.getStudentStatus() %></td>
          </tr>
		<%
				} // End of loop over StudentDashboardLine
		%>
		</table>
				<% } else {  // End of if they have worked on a running sim before.%>
                <p>No simulation found to display on dashboard.</p>
                <% } %>
			  </blockquote>
			  <br />
			  	<% if (afso.getRunningSimId() != null) { %>
					<a href="select_running_sim_for_dashboard.jsp">Select a different Running Simulation </a><br/>
					<br/>
				<% } %>
                  <p align="center"></p></td>
              </tr>
            </table></td>
        </tr>
        <tr>
          <td><p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
        </tr>
      </table></td>
  </tr>
</table>
<p>&nbsp;</p>
<p align="center">&nbsp;</p>
</body>
</html>
<%
	
%>
