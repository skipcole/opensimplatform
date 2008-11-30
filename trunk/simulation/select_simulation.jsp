<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
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
	
	// Don't know schema yet for sure, so get the base user for the name
	BaseUser bu = BaseUser.getByUserId(pso.user_id);
				
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>USIP Open Simulation Platform</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
</head>
<body onLoad="">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform </h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center"></div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0">
</td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top">&nbsp;</td>
    <td colspan="1" valign="top"><br /></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<BR />
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%">
            <h1>Welcome <%= bu.getFull_name() %>!</h1>
            <p>Please select your simulation below:</p>
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
      <td> <form action="load_player_scenario.jsp" method="post" name="form1" id="form1">
        <input type="submit" name="Submit" value="Play" />
        <input type="hidden" name="user_assignment_id" value="<%= ua.getId() %>" />
        <input type="hidden" name="schema" value="<%= sg.getSchema_name() %>" />
        <input type="hidden" name="schema_org" value="<%= sg.getSchema_organization() %>" />
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
            <p>&nbsp;</p>			</td>
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