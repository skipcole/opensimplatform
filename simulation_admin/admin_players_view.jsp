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
    <td><%= ua.getActor_id() %></td>
    <td><form name="form1" method="post" action="">
      <label>
        <input type="submit" name="Submit" value="Become ->">
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