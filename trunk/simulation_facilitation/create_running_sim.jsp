<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*,org.hibernate.*" 
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
	
	MultiSchemaHibernateUtil.beginTransaction(pso.schema);
	
	String sending_page = (String) request.getParameter("sending_page");
	String addRunningSimulation = (String) request.getParameter("addRunningSimulation");
	
	if ( (sending_page != null) && (addRunningSimulation != null) && (sending_page.equalsIgnoreCase("create_running_sim"))){

		String rsn = (String) request.getParameter("running_sim_name");
		simulation = (Simulation) MultiSchemaHibernateUtil.getSession(pso.schema).get(Simulation.class, simulation.getId());
		RunningSimulation rs = simulation.addNewRunningSimulation(rsn, MultiSchemaHibernateUtil.getSession(pso.schema));
		
		pso.running_sim_id = rs.getId();
		pso.runningSimSelected = true;
		
		// Connnection has been closed, so re-open it
		//MultiSchemaHibernateUtil.beginTransaction(pso.schema);
            
	} // End of if coming from this page and have added sim.

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>USIP Online Simulation Platform</title>
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_oscw.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="80%" valign="top"><!-- InstanceBeginEditable name="pageTitle" --><h1>Create Running Simulation</h1><!-- InstanceEndEditable --></td>
    <td width="20%" align="right" valign="top"> 
		<% 
		String canEdit = (String) session.getAttribute("author");
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		<a href="../simulation_planning/index.jsp" target="_top">Think</a><br>
	    <a href="../simulation_authoring/creationwebui.jsp" target="_top">Create</a><br>
		<a href="facilitateweb.jsp" target="_top">Play</a><br>
        <a href="../simulation_sharing/index.jsp" target="_top">Share</a>
		<% } %>
		</td>
  </tr>
</table>
<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
</tr>
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" --> 
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
		  	List rsList = new RunningSimulation().getAllForSim(pso.sim_id.toString(), MultiSchemaHibernateUtil.getSession(pso.schema));
			
			for (ListIterator li = rsList.listIterator(); li.hasNext();) {
				RunningSimulation rs = (RunningSimulation) li.next();
				SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil.getSession(pso.schema).get(SimulationPhase.class, new Long(rs.getPhase_id()));
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
      <p align="center"><a href="bulk_invite.jsp">Next Step: Bulk Invite Users</a></p>
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
		if (!((canEdit != null) && (canEdit.equalsIgnoreCase("true")))) { %>
	  		<a href="../simulation_facilitation/index.jsp" target="_top">&lt;-- Back
      </a>
	        <% } %>
      <!-- InstanceEndEditable --></td>
  </tr>
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
</table>

<p>&nbsp;</p>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td align="left" valign="bottom"> 
	<% 
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
	<a href="../simulation_authoring/intro.jsp" target="_top">Home 
      </a>
	  <% } else { %>
	  <a href="index.jsp" target="_top">Home 
      </a>
	  <% } %>
	  </td>
    <td align="right" valign="bottom"><a href="../simulation_user_admin/my_profile.jsp">My 
      Profile</a> </td>
  </tr>
  <tr>
    <td align="left" valign="bottom"><a href="../simulation_authoring/logout.jsp" target="_top">Logout</a></td>
    <td align="right" valign="bottom">&nbsp;</td>
  </tr>
</table>
<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP Open Source Software Project</a>. </p>
</body>
<!-- InstanceEnd --></html>
<%
		MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
%>
