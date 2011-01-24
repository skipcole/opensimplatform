<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	RunningSimulation running_simulation = new RunningSimulation();
	
	String rs_id = request.getParameter("rs_id");
	
	afso.setRunningSimId(new Long(rs_id));
	
	running_simulation = RunningSimulation.getById(afso.schema, afso.getRunningSimId());
	//////////////////////////////////////////////////////
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>


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
              <h1>Administrate Users for Running Simulation </h1>
              <br />
      <p>Users for Running Simulation: <strong><%= running_simulation.getRunningSimulationName() %></strong></p>
      <table border="1" width="100%"><tr><td><strong>Actor</strong></td>
      <td><strong>User Email</strong></td>
      <td>Last Login</td>
      </tr>
      <%
	  			for (ListIterator li = UserAssignment.getAllForRunningSim(afso.schema, new Long (rs_id)).listIterator(); li.hasNext();) {
					UserAssignment ua = (UserAssignment) li.next();
					
					User user = new User();
					user.setUser_name(ua.getUsername());
					
					if (ua.getUser_id() != null){
						user = User.getById(afso.schema, ua.getUser_id());
					}
					
					String act_name = USIP_OSP_Cache.getActorName(afso.schema, afso.sim_id, afso.getRunningSimId(), request, ua.getActor_id());
		%>
        	<tr><td> <%= act_name %></td><td> <%= user.getUser_name() %></td>
        	  <td><a href="../simulation_admin/simulation_admin_lastlogin_details.jsp?user_id=<%= user.getId() %>">last date</a></td>
        	</tr>
        <%
			
	  	} // End of loop over user assignments.
	  %>
      </table>
      <p></p>
      <p align="center">
	  <% if (!(running_simulation.isReady_to_begin())) {
	  %>
	  This running simulation has not been enabled. To continue the procces, click below to do so.<br />
	  <a href="create_schedule_page.jsp">Next step: Create Schedule Page</a>
	  <%
	  }
	  %>
	  
	  </p>      <p align="left">&nbsp;</p>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
<%
	
%>
