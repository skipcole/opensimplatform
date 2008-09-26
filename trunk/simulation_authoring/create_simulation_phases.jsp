<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	pso.backPage = "create_simulation_phases.jsp";
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
	String sending_page = (String) request.getParameter("sending_page");
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("create_phase"))){
	
		pso.handleAddPhase(simulation, request);

	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Control Page</title>
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
    <td width="80%" valign="top"><!-- InstanceBeginEditable name="pageTitle" --><h1>Create Simulation Phase</h1><!-- InstanceEndEditable --></td>
    <td width="20%" align="right" valign="top"> 
		<% 
		String canEdit = (String) session.getAttribute("author");
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		<a href="../simulation_planning/index.jsp" target="_top">Think</a><br>
	    <a href="creationwebui.jsp" target="_top">Create</a><br>
		<a href="../simulation_facilitation/facilitateweb.jsp" target="_top">Play</a><br>
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
			
			List phaseList = new SimulationPhase().getAllForSim(pso.schema, pso.sim_id);

		%>
        <blockquote>
          <p>Create phases for the simulation <strong><%= simulation.getDisplayName() %></strong>. </p>
          <p>The set of actions and views (simulation sections) that are accessible 
            to each actor can be different in every simulation phase.</p>
          <p>Every simulation begins in the 'Started' phase, and every simulation 
            ends in the 'Completed' phase. It is possible, but not necessary, 
            to add aditional phases that the simulation can pass through. </p>
          <form action="create_simulation_phases.jsp" method="post" name="form1" id="form1">
            <input type="hidden" name="sending_page" value="create_phase" />
            <table width="80%" border="0" cellspacing="2" cellpadding="2">
              <tr> 
                <td valign="top">Phase Name:</td>
                <td valign="top"><input type="text" name="phase_name" /></td>
              </tr>
              <tr>
                <td valign="top">Phase Notes:</td>
                <td valign="top"><label>
                  <textarea name="phase_notes" id="textarea" cols="45" rows="5"></textarea>
                </label></td>
              </tr>
              <tr> 
                <td valign="top">&nbsp;</td>
                <td valign="top"><input type="submit" name="createphase" value="Submit" /></td>
              </tr>
            </table>
            <p>&nbsp;</p>
          </form>
          <p>Below are listed all of the current simulation phases for this simulation:</p>
          <table width="100%" border="1" cellspacing="2" cellpadding="2">
            <tr> 
              <td width="34%" valign="top"><h2>Phase Name</h2></td>
              <td width="66%" valign="top"><h2>Phase Notes</h2></td>
            </tr>
       <%
		for (ListIterator li = phaseList.listIterator(); li.hasNext();) {
			SimulationPhase sp = (SimulationPhase) li.next();
			
		%>
            <tr>
              <td valign="top"><%= sp.getName() %></td>
              <td valign="top"><%= sp.getNotes() %></td> 
            </tr>
            <%
	}
%>
          </table>
          <p>&nbsp;</p>
            
          <div align="center"><a href="create_actors.jsp">Next Step: Create Actors 
            </a></div>
        </blockquote>
      </blockquote>
	  <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
		<%@ include file="select_message.jsp" %></p>
      </blockquote>
	  
      <% } // End of if have not set simulation for edits. %>
      <a href="psp.jsp">&lt;- Back</a> <!-- InstanceEndEditable --></td>
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
	<a href="intro.jsp" target="_top">Home 
      </a>
	  <% } else { %>
	  <a href="../simulation_facilitation/index.jsp" target="_top">Home 
      </a>
	  <% } %>
	  </td>
    <td align="right" valign="bottom"><a href="../simulation_user_admin/my_profile.jsp">My 
      Profile</a> </td>
  </tr>
  <tr>
    <td align="left" valign="bottom"><a href="logout.jsp" target="_top">Logout</a></td>
    <td align="right" valign="bottom">&nbsp;</td>
  </tr>
</table>
<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP Open Source Software Project</a>. </p>
</body>
<!-- InstanceEnd --></html>
<%
	
%>