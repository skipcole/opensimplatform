<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.coursemanagementinterface.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
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
              <h1>Add/Remove Instructors  </h1>
              <p>When an user is designated as an instructor for a simulation, he or she will see that running simulation in their list of 'My Sims' and will be able to access the student dashboard for that simulation.</p>
              <p>Note: making someone an 'Instructor' in a simulation does not automatically add them as a player in the simulation. Someone can act as an instructor (assigning players, checking on players, etc.) and never actually enter into the game world. </p>
              <p align="center">&nbsp;</p>      
      <h2 align="left">Instructors for Running Simulation: <strong><%= running_simulation.getRunningSimulationName() %></strong></h2>
      <p align="left">&nbsp;</p>
	  <% 
	  	List iList = InstructorRunningSimAssignments.getInstructorsForSim(running_simulation.getId(), afso.schema);
		
		for (ListIterator li = iList.listIterator(); li.hasNext();) {
			User user = (User) li.next();
	  %>
	  <%= user.getId() %> <br />
	  <% } // end of loop over instructors %>	  </td>
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
