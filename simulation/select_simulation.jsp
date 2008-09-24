<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" 
	errorPage="" %>
<%

	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	System.out.println("user is logged in");
	
	if (pso.hasSelectedRunningSim){
		System.out.println("user selected sim");
		response.sendRedirect("simwebui.jsp?tabposition=1");
		return;
	} else {
		System.out.println("user has not selected sim");
	}
				
%>
<html>
<head>
<title>Online Simulation Platform Simulation</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<p>&nbsp;</p>
<h1>Welcome <%= pso.user_Display_Name %>!</h1>

<p>Select your simulation</p>
<table width="80%" border="2" cellspacing="2" cellpadding="2">
  <tr valign="top">
    <td width="30%"><h2>Organization</h2></td>
    <td width="30%"><h2>Simulation</h2></td>
    <td width="35%"><h2>Session</h2></td>
    <td width="15%"><h2>Your Role</h2></td>
    <td width="10%"><h2>Play</h2></td>
    <td width="10%"><h2>Phase</h2></td>
  </tr>
  <%

	// Get list of schema where this user may have a simulation waiting.
	List authSchema = BaseUser.getAuthorizedSchemas(pso.user_id);
	
	for (ListIterator lias = authSchema.listIterator(); lias.hasNext();) {
		SchemaGhost sg = (SchemaGhost) lias.next();
		
		pso.schema = sg.getSchema_name();
			
  		MultiSchemaHibernateUtil.beginTransaction(pso.schema);
  
  		List uaList = new UserAssignment().getAllForUser(pso.user_id, MultiSchemaHibernateUtil.getSession(pso.schema));
	
	
		for (ListIterator li = uaList.listIterator(); li.hasNext();) {
			UserAssignment ua = (UserAssignment) li.next();
			
			Simulation sim = (Simulation) MultiSchemaHibernateUtil.getSession(pso.schema).get(Simulation.class,ua.getSim_id());
			RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil.getSession(pso.schema).get(RunningSimulation.class,ua.getRunning_sim_id());
			Actor act = (Actor) MultiSchemaHibernateUtil.getSession(pso.schema).get(Actor.class,ua.getActor_id());
			
			SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil.getSession(pso.schema).get(SimulationPhase.class,rs.getPhase_id());
			
  %>
  <tr valign="top">
    <td><%= sg.getSchema_organization() %></td>
    <td><%= sim.getDisplayName() %></td>
    <td><%= rs.getName() %></td>
    <td><%= act.getName() %></td>
    <td> <form name="form1" method="post" action="load_player_scenario.jsp">
        <input type="submit" name="Submit" value="Play">
        <input type="hidden" name="user_assignment_id" value="<%= ua.getId() %>" >
		<input type="hidden" name="schema" value="<%= sg.getSchema_name() %>" >
		<input type="hidden" name="schema_org" value="<%= sg.getSchema_organization() %>" >
      </form></td>
    <td><%= sp.getName() %></td>
  </tr>
  <%
  		} // End of loop over User Assignments
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
		
		}
		
  %>
</table>

<p>If you think that you should have more simulation assignments than shown above, please contact your instructor.</p>

<p>&nbsp;</p>
</body>
</html>
<%
		
%>