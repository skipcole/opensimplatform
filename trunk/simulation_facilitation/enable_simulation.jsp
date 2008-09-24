<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,
	org.hibernate.*,
	org.usip.oscw.baseobjects.*" 
	errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	pso.backPage = "enable_simulation.jsp";

	////////////////////////////////////////////////////////
	Simulation simulation = new Simulation();	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
	/////////////////////////////////////////////////////
	RunningSimulation running_sim = new RunningSimulation();
	if (pso.running_sim_id != null){
		pso.handleEnableSim(request);
		running_sim = (RunningSimulation) pso.giveMeRunningSim();
	}
	//////////////////////////////////////////////////////
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
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
    <td width="80%" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Enable Simulation to Start</h1>
      <!-- InstanceEndEditable --></td>
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
<p></p>
<blockquote> 
        <% 
			if (!(pso.simulationSelected)) {
		%>
        <p>You must first select the simulation which you will be enabling.<br />

		Please <a href="../simulation_authoring/select_simulation.jsp">click here</a> to select it, or <a href="../simulation_authoring/create_simulation.jsp">create a new one</a>.</p>
		
		<% } else { %>
		<p>Enabling <strong>simulation: <%= simulation.getDisplayName() %></strong>. <br />
		To select a different simulation, <a href="../simulation_authoring/select_simulation.jsp">click here</a>.</p>
        <%
			if (!(pso.runningSimSelected)) {
		%>
        <p>You must select the running simulation for which you will be enabling.<br />

		Please <a href="../simulation_authoring/select_running_simulation.jsp">click here</a> to select it, or <a href="create_running_sim.jsp">create a new one</a>.</p>
		
		<% } else if (running_sim.isReady_to_begin()) { %>
		<p><strong>Running simulation <%= running_sim.getName() %> </strong> has  been enabled.<br />
		To select a different running simulation to enable, <a href="../simulation_authoring/select_running_simulation.jsp">click here</a>.</p>
		<% } else { %>
		<p>Enabling <strong>running simulation <%= running_sim.getName() %></strong><br />
		To select a different running simulation to enable, <a href="../simulation_authoring/select_running_simulation.jsp">click here</a>.</p>

  <p>&nbsp;</p>
  <form action="enable_simulation.jsp" method="post" name="form1" id="form1">
    <input type="hidden" name="sending_page" value="enable_game" />
          <table width="100%" border="0" cellspacing="2" cellpadding="2">
            <tr valign="top"> 
              <td width="34%">Notify players:</td>
              <td width="66%"> <input name="email_users" type="checkbox" value="true" checked="checked" />
                yes </td>
            </tr>
            <tr valign="top">
              <td>List of Players:</td>
              <td>Put in list of players to notify</td>
            </tr>
            <tr valign="top"> 
              <td width="34%">Email text:<br /> <br /> </td>
              <td width="66%"> Dear &lt;Players Name&gt;,<br /> <p> 
                  <textarea name="email_text" cols="60" rows="5">
     You are invited to enter a simulation. 
     Please go to the website [web_site_location] to enter. 
     Your username is [username], and your password is [password].
Enjoy!
</textarea>
                </p>
                <p><font color="#CC9900">Note:</font> You should not need to replace 
                  the text inside of brackets []. If your system is configured 
                  correctly, these will automatically be replaced with the correct 
                  information in the emails sent out.</p></td>
            </tr>
            <tr valign="top"> 
              <td>Enable the simulation to start:</td>
              <td> <input type="submit" name="command" value="Start Simulation" /></td>
            </tr>
          </table>
  </form>
  <p>&nbsp;</p>
</blockquote>
	         <% } // end of if running_sim.id has been set. %>
        <%
		
	}// end of if pso.simulation.id has been set.

%>
<blockquote>
      <div align="center"><a href="../simulation/index.jsp" target="_top">Next 
        Step: Begin Play</a></div>
</blockquote>
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
	
%>
