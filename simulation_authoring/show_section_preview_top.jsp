<%@ page contentType="text/html; charset=utf-8" language="java" 
	import="java.sql.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.networking.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	// Make sure that the player session object has been created, and is focused on the same simulation.
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	pso.sim_id = afso.sim_id;
	
	String sec_id = request.getParameter("sec_id");
	
	SimulationSectionAssignment ssa = new SimulationSectionAssignment();
	
	if (sec_id != null) {
		ssa = SimulationSectionAssignment.getById(afso.schema, new Long(sec_id));
	}
	
	String actors_name_string = "fill it in from cache";
			
	if (afso.actor_being_worked_on_id.equals(new Long(0))) {
		actors_name_string = " every actor ";
	} else {
		actors_name_string = USIP_OSP_Cache.getActorName(afso.schema, afso.sim_id, new Long(0), request, afso.actor_being_worked_on_id);
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>USIP OSP Control Page</title>
</head>

<body>
<table>
	<tr>
		<td><table border="1"><tr><td><%= ssa.getTab_heading() %></td></tr></table>
		</td>
		<td>(Change this Tab Heading  for <%= actors_name_string %> in phase <%= USIP_OSP_Cache.getPhaseNameById(request, pso.schema, pso.phase_id) %>)		</td>
	</tr>
</table>
</body>
</html>
