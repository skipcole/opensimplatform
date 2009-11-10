<%@ page contentType="text/html; charset=utf-8" language="java" 
	import="java.sql.*,org.usip.osp.networking.*" 
	errorPage="" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	// Make sure that the player session object has been created, and is focused on the same simulation.
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	pso.sim_id = afso.sim_id;
	
	String actors_name_string = "fill it in from cache";
			
	if (afso.actor_being_worked_on_id.equals(new Long(0))) {
		actors_name_string = " every actor ";
	} else {
		System.out.println("actor id : " + afso.actor_being_worked_on_id);
		actors_name_string = " [insert code here] ";
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>USIP OSP Control Page</title>
</head>

<body>
Below is a sample of what this page make look like during a simulation for <%= actors_name_string %> in phase <%= afso.phase_id %> .<br />
TODO: List out all actors and phases that have this simulation section.
</body>
</html>
