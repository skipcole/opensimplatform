<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.baseobjects.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	afso.backPage = "../simulation_facilitation/play_panel.jsp";

	Simulation simulation = new Simulation();
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
	RunningSimulation running_simulation = new RunningSimulation();
	if (afso.getRunningSimId() != null){
		running_simulation = afso.giveMeRunningSim();
	}
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.4.1.js"></script>
<!-- script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script -->
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
              <h1 align="center">Simulation Facilitation Miscellaneous Tools</h1>
              <p align="center">&nbsp; </p>
              <h2>Basic Tools </h2>
			  <p>Below are listed activities that instructors may occasionally do. </p>
			  <ul>
			    <li><a href="administrate_running_sim_add_instructor.jsp">Add Instructors</a>  <a href="helptext/add_instructor.jsp" target="helpinright">(?) </a></li>
                <li><a href="facilitate_find_student.jsp">Find Student</a><a href="helptext/find_student.jsp" target="helpinright"> (?) </a></li>
                <li><a href="bulk_invite.jsp">Invite Student(s) to Register </a><a href="helptext/bulk_invite_help.jsp" target="helpinright">(?) </a></li>
                <li><a href="../simulation_user_admin/create_user.jsp">Register Student </a> <a href="helptext/create_user_help.jsp" target="helpinright">(?)</a></li>
                </ul>      
              <p>&nbsp;</p>
              <h2>Advanced Tools </h2>
      <p>Below are listed instructor activities that are either rare, or in development.</p>
      <ul>
        <li><a href="create_set_of_running_sims.jsp">Create Set of Running Simulations</a><a href="helptext/add_instructor.jsp"  target="helpinright"> (?)</a></li>
        <li><a href="facilitate_create_login_message.jsp">Create a Login Message</a><a href="helptext/create_running_sim_help.jsp" target="helpinright"> (?)</a></li>
        </ul>
      <p align="center"></p>			</td>
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
