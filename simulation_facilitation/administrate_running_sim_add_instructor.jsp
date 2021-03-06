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
	
	if (afso.getRunningSimId() != null) {
		running_simulation = RunningSimulation.getById(afso.schema, afso.getRunningSimId());
	}
	//////////////////////////////////////////////////////
	
	String sending_page = request.getParameter("sending_page");
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("add_instructor")) && (afso.getRunningSimId() != null)) {
		String instructor_name = request.getParameter("instructor_name");
		
		User user = User.getByUsername(afso.schema, instructor_name);
		
		if (user != null){
			InstructorRunningSimAssignments irsa = 
				new InstructorRunningSimAssignments(afso.schema, afso.getRunningSimId(), user.getId());
		}
		
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>


<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
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
			  
	<% if (afso.getRunningSimId() != null) { %> 
      <h2 align="left">Instructors for Running Simulation: <strong><%= running_simulation.getRunningSimulationName() %></strong></h2>
	  <ol>
	  <% 
	  	List iList = InstructorRunningSimAssignments.getInstructorsForSim(running_simulation.getId(), afso.schema);
		
		if ((iList == null) || (iList.size() == 0)) { %>
			<li>None</li>
		<% } // End of if there are no instructors.
		
		for (ListIterator li = iList.listIterator(); li.hasNext();) {
			User user = (User) li.next();
			
	  %>
	  <li><%= user.getBu_full_name() %> | <%= user.getBu_username() %>  | <% if (iList.size() > 1) { %> 
	  (remove) 
	    <% } %></li>
	  <% } // end of loop over instructors %>	  
	  </ol>
	  
	  <p>Add Another (type username below and hit submit): </p>
	  
	  
	  
	  <form id="form1" name="form1" method="post" action="administrate_running_sim_add_instructor.jsp">
	  <input type="hidden" name="sending_page" value="add_instructor" />
	    <label>
	      <input type="text" name="instructor_name" />
	      </label>
	    <label>
	    <input type="submit" name="Submit" value="Submit" />
	    </label>
	  </form>
	  
	  
	  <% } // end of if not rs_id != null %>
	  </td>
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
