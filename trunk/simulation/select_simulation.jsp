<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%

	PlayerSessionObject.handleInitialEntry(request);

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	if (pso.hasSelectedRunningSim){
		response.sendRedirect("simwebui.jsp?tabposition=1");
		return;
	} 
	
	pso.handleLoadPlayerScenario(request);
	
	if (pso.forward_on) {
		pso.forward_on = false;
		response.sendRedirect("simwebui.jsp?tabposition=1");
		return;
	}
	
	// Don't know schema yet for sure, so get the base user for the name
	BaseUser bu = BaseUser.getByUserId(pso.user_id);
	
	response.setHeader("Cache-Control", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
    response.setHeader("Pragma", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
    response.setHeader("Expires", "-1"); //$NON-NLS-1$ //$NON-NLS-2$
				
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<title>USIP Open Simulation Platform</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(images/page_bg.png);
	background-repeat: repeat-x;
}
.style1 {
	color: #FF0000;
	font-weight: bold;
	font-style: italic;
}
-->
</style>
</head>
<body onLoad="">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;<%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "USIP_OSP_HEADER") %></h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center"><a href="../logout.jsp" target="_top" class="menu_item"><img src="../Templates/images/logout.png" alt="Home" width="90" height="19" border="0" /></a></div>  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"></td>
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
            <h1><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "welcome") %> <%= bu.getFull_name() %>!</h1>
            <p><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "below_are") %></p>
            <table width="80%" border="2" cellspacing="2" cellpadding="2">
              <tr valign="top">
                <td width="30%"><h2><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "organization") %></h2></td>
      <td width="30%"><h2><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "simulation") %></h2></td>
      <td width="35%"><h2><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "session") %></h2></td>
      <td width="15%"><h2><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "your_role") %></h2></td>
      <td width="10%"><h2><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "phase") %></h2></td>
      <td width="10%"><h2><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "play") %></h2></td>
    </tr>
              <%

	// Get list of schema where this user may have a simulation waiting.
	List authSchema = BaseUser.getAuthorizedSchemas(pso.user_id);
	
	for (ListIterator lias = authSchema.listIterator(); lias.hasNext();) {
		SchemaGhost sg = (SchemaGhost) lias.next();
		
		pso.schema = sg.getSchema_name();
  
  		List uaList = UserAssignment.getAllForUser(pso.schema, pso.user_id);
	
		for (ListIterator li = uaList.listIterator(); li.hasNext();) {
			UserAssignment ua = (UserAssignment) li.next();
			
			Simulation sim = Simulation.getMe(pso.schema, ua.getSim_id());
			RunningSimulation rs = RunningSimulation.getMe(pso.schema,ua.getRunning_sim_id());
			Actor act = Actor.getMe(pso.schema, ua.getActor_id());
			
			SimulationPhase sp = SimulationPhase.getMe(pso.schema, rs.getPhase_id());
			
			// Must check to see that running sim has been enabled, and has not been inactivated.
			if ((rs.isReady_to_begin()) && (!(rs.isInactivated()))) {
  %>
              <tr valign="top">
                <td><%= sg.getSchema_organization() %></td>
      <td><%= sim.getDisplayName() %>
        <span class="style1"><%= pso.checkDatesOnSim(sim, rs) %>        </span></td>
      <td><%= rs.getRunningSimulationName() %></td>
      <td><%= act.getActorName(pso.schema, rs.getId(), request) %></td>
      <td><%= sp.getPhaseName() %></td>
      <td> <form action="select_simulation.jsp" method="post" name="form1" id="form1">
      
        <input type="submit" name="Submit" value="<%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "play") %> > " />
        <input type="hidden" name="user_assignment_id" value="<%= ua.getId() %>" />
        <input type="hidden" name="schema" value="<%= sg.getSchema_name() %>" />
        <input type="hidden" name="schema_org" value="<%= sg.getSchema_organization() %>" />
        <input type="hidden" name="sending_page" value="select_simulation" />
        </form></td>
    </tr>
              <%
			  
			  } // End of if running simulation has been enabled.
			  
  		} // End of loop over User Assignments
		
		}
		
  %>
            </table>
            <p>If you think that you should have more simulation assignments than shown above, please contact your instructor.</p>
            <p><hr /></p>
            <p>Below are publically accessible simulations. You may select one of these and commence play immediately. </p>
            <blockquote>
        <table>
          <%
		List simList = Simulation.getAllPublishedAutoRegisterable(pso.schema);
		
		for (ListIterator li = simList.listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
			String nameToSend = java.net.URLEncoder.encode(sim.getSimulationName());
			
		%>
        
          <!-- tr> 
            <td><a href="../simulation_facilitation/sim_blurb_information.jsp?sim_id=<%= sim.getId().toString() %>" target="_top"><%= sim.getSimulationName() %> : <%= sim.getVersion() %></a></td>
          </tr -->
           
<%	} // End of loop over auto-reg sims    %>
        </table>
      </blockquote>
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