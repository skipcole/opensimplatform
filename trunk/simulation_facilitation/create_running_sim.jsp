<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.hibernate.*" 
	errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	pso.backPage = "../simulation_facilitation/create_running_sim.jsp";
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
		
	String sending_page = (String) request.getParameter("sending_page");
	String addRunningSimulation = (String) request.getParameter("addRunningSimulation");
	
	if ( (sending_page != null) && (addRunningSimulation != null) && (sending_page.equalsIgnoreCase("create_running_sim"))){

		String rsn = (String) request.getParameter("running_sim_name");
		RunningSimulation rs = simulation.addNewRunningSimulation(rsn, pso.schema);
		
		pso.running_sim_id = rs.getId();
		pso.runningSimSelected = true;
            
	} // End of if coming from this page and have added sim.

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>USIP Open Simulation Platform</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(../Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform</h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
	    <table border="0" cellspacing="1" cellpadding="0">
	<%  if (pso.isAuthor()) { %>
        <tr>
          <td><div align="center"><a href="../simulation_authoring/intro.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } else { %>
		<tr>
          <td><div align="center"><a href="instructor_home.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } %>	
        <tr>
          <td><div align="center"><a href="../simulation_user_admin/my_profile.jsp" class="menu_item"><img src="../Templates/images/my_profile.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
        <tr>
          <td><div align="center"><a href="../simulation_authoring/logout.jsp" target="_top" class="menu_item"><img src="../Templates/images/logout.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
      </table>	  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"><% 
		
		String bgColor_think = "#475DB0";
		String bgColor_create = "#475DB0";
		String bgColor_play = "#475DB0";
		String bgColor_share = "#475DB0";
		
		pso.findPageType(request);
		
		if (pso.page_type == 1){
			bgColor_think = "#9AABE1";
		} else if (pso.page_type == 2){
			bgColor_create = "#9AABE1";
		} else if (pso.page_type == 3){
			bgColor_play = "#9AABE1";
		} else if (pso.page_type == 4){
			bgColor_share = "#9AABE1";
		}
		
		if (pso.isAuthor()) { %>
		
		<table border="0" cellpadding="0" cellspacing="0" >
		   <tr>
		<td bgcolor="<%= bgColor_think %>"><a href="../simulation_planning/index.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;THINK&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
	    <td bgcolor="<%= bgColor_create %>"><a href="../simulation_authoring/creationwebui.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;CREATE&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
		<td bgcolor="<%= bgColor_play %>"><a href="facilitateweb.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;PLAY&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
        <td bgcolor="<%= bgColor_share %>"><a href="../simulation_sharing/index.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;SHARE&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		   </tr>
		</table>
	<% } %></td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top"></td>
    <td colspan="1" valign="top"></td>
    <td width="194" align="right" valign="top"></td>
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
			<td width="100%"><br />
			<!-- InstanceBeginEditable name="pageTitle" --><h1>Create Running Simulation</h1><!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
      <blockquote> 
        <% 
			if (pso.simulationSelected) {
		%>
        <p>Create running simulations for the simulation <strong><%= simulation.getDisplayName() %></strong>.<br>
          (If you would like to create running simulations for a different simulation, 
          <a href="../simulation_authoring/select_simulation.jsp">click here</a>.)</p>
      
      Below are the running simulation currently associated with <b> <%= simulation.getName() %> </b>. <br />
        <table width="80%" border = "1">
          <tr> 
            <td><h2>Running Simulation</h2></td>
            <td><h2>Phase</h2></td>
          </tr>
          <%
		  	List rsList = RunningSimulation.getAllForSim(pso.sim_id.toString(), pso.schema);
			
			for (ListIterator li = rsList.listIterator(); li.hasNext();) {
				RunningSimulation rs = (RunningSimulation) li.next();
				
				SimulationPhase sp = SimulationPhase.getMe(pso.schema, rs.getPhase_id().toString());
		%>
          <tr> 
            <td><%= rs.getName() %></td>
            <td><%= sp.getName() %></td>
          </tr>
          <%
			}
		%>
        </table>
	  </blockquote>
      <form action="create_running_sim.jsp" method="post" name="form1" id="form1">
        <input type="hidden" name="sending_page" value="create_running_sim" />
        <table width="80%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td>Enter new Running Simulation Name (for example 'Summer 2007 - 
              1')</td>
            <td><input type="text" name="running_sim_name" /></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td><input type="submit" name="addRunningSimulation" value="Submit" /></td>
          </tr>
        </table>
      </form>
      <p align="center"><a href="create_schedule_page.jsp">Next step: Create Schedule Page</a></p>
      <% } else { // End of if have set simulation id. %>
      <blockquote> 
        <p>
        </p>
          <%@ include file="../simulation_authoring/select_message.jsp" %>
      </blockquote>
      <p>
        <% } // End of if have not set simulation for edits. %>
      </p>
      <p>&nbsp;</p>
      <% 
		if (!(pso.isAuthor())) { %>
	  		<a href="instructor_home.jsp" target="_top">&lt;-- Back
      </a>
	        <% } %>
      <!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
<%
%>
