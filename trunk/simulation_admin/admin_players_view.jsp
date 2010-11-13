<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	if (!(afso.isAdmin())){
		response.sendRedirect("index.jsp");
		return;
	}
	

	String admin_backdoor = request.getParameter("admin_backdoor");
	if ((admin_backdoor != null) && ( admin_backdoor.equalsIgnoreCase("true") ) ) {
		PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
		pso.user_id = afso.user_id;
		pso.schema = afso.schema;
		pso.setLoggedin(true);
		
		String sim_id = request.getParameter("sim_id");
		pso.sim_id = new Long(sim_id);
		
		String rs_id = request.getParameter("rs_id");
		pso.setRunningSimId( new Long(rs_id) );
		
		
		pso.handleLoadPlayerScenario(request);
	
		if (pso.forward_on) {
			pso.forward_on = false;
			response.sendRedirect("admin_players_view_enter.jsp");
			return;
		}
	
	}
	
%>
<html>
<head>
<title>Finger</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>

<h1>Player's View  </h1>
<p>Below you can enter into the simulation world as any of the players. </p>
<p>&nbsp;</p>

<blockquote>
  <table width="100%" border="0">
  <tr>
    <td><strong>Simulation</strong></td>
    <td><strong>Running Simulation </strong></td>
    <td><strong>Player</strong></td>
    <td><strong>Become</strong></td>
  </tr>
  <% 
  List simList = Simulation.getAll(afso.schema);
  
  for (ListIterator li = simList.listIterator(); li.hasNext();) {
	Simulation sim = (Simulation) li.next();
	
	List rsList = RunningSimulation.getAllForSim(sim.getId() + "", afso.schema);	
	
	for (ListIterator lirs = rsList.listIterator(); lirs.hasNext();) {
		RunningSimulation rs = (RunningSimulation) lirs.next();
		
		List uaList = UserAssignment.getAllForRunningSim(afso.schema, rs.getId());
	
		for (ListIterator liua = uaList.listIterator(); liua.hasNext();) {
		UserAssignment ua = (UserAssignment) liua.next();
  %>
  <tr>
    <td><%= sim.getSimulationName() %></td>
    <td><%= rs.getRunningSimulationName() %></td>
    <td><%= USIP_OSP_Cache.getActorName(afso.schema, sim.getId(), rs.getId(), request, ua.getActor_id()) %></td>
    <td><form name="form1" method="post" action="">
      <label>
        <input type="submit" name="Submit" value="Become ->">
		<input type="hidden" name="admin_backdoor" value="true" />
		<input type="hidden" name="user_assignment_id" value="<%= ua.getId() %>" />
        <input type="hidden" name="schema" value="<%= afso.schema %>" />
		<input type="hidden" name="sim_id" value="<%= sim.getId() %>" />
		<input type="hidden" name="rs_id" value="<%= rs.getId() %>" />
        <input type="hidden" name="schema_org" value="Admin" />
        <input type="hidden" name="sending_page" value="select_simulation" />
        </label>
    </form>
    </td>
  </tr>
  <% 
	} // End of loop over simulations
	} // End of loop over running simulations
	} // End of loop over actors.
%>
</table>

<p>&nbsp;</p>
</blockquote>
<p>&nbsp;</p>
<p></p>
</body>
</html>