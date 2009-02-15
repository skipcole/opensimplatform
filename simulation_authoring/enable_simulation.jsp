<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,
	org.hibernate.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	pso.backPage = "enable_simulation.jsp";
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	

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
<title>Open Simulation Platform Control Page</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
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
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Enable Simulation to Start</h1>
      <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
<p></p>
<blockquote> 
        <% 
			if (pso.sim_id == null) {
		%>
        <p>You must first select the simulation which you will be enabling.<br />

		Please <a href="select_simulation.jsp">click here</a> to select it, or <a href="create_simulation.jsp">create a new one</a>.</p>
		
		<% } else { %>
		<p>Enabling <strong>simulation: <%= simulation.getDisplayName() %></strong>. <br />
		To select a different simulation, <a href="select_simulation.jsp">click here</a>.</p>
        <%
			if (!(pso.runningSimSelected)) {
		%>
        <p>You must select the running simulation for which you will be enabling.<br />

		Please <a href="select_running_simulation.jsp">click here</a> to select it, or <a href="../simulation_facilitation/create_running_sim.jsp">create a new one</a>.</p>
		
		<% } else if (running_sim.isReady_to_begin()) { %>
		<p><strong>Running simulation <%= running_sim.getName() %> </strong> has  been enabled.<br />
		To select a different running simulation to enable, <a href="select_running_simulation.jsp">click here</a>.</p>
		<% } else { %>
		<p>Enabling <strong>running simulation <%= running_sim.getName() %></strong><br />
		To select a different running simulation to enable, <a href="select_running_simulation.jsp">click here</a>.</p>

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
      <!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
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
<!-- InstanceEnd --></html>
<%
	
%>
