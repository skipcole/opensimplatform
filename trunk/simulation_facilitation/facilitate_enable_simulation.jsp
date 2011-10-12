<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,
	org.hibernate.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	

	////////////////////////////////////////////////////////
	Simulation simulation = new Simulation();	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
	/////////////////////////////////////////////////////
	RunningSimulation running_sim = new RunningSimulation();
	if (afso.getRunningSimId() != null){
	
		afso.handleEnableSim(request);
		
		running_sim = (RunningSimulation) afso.giveMeRunningSim();
	}
	//////////////////////////////////////////////////////
	
	String enable_string = "Enable";
	if (running_sim.isReady_to_begin()) {
		enable_string = "Disable";
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {
	color: #FF0000;
	font-weight: bold;
}
-->
</style>
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
              <h1><%= enable_string %> Simulation <a href="helptext/enable_sim_help.jsp" target="helpinright">(?)</a></h1>
              <blockquote>
                <%  if (afso.sim_id == null) { %>
                <% } else { %>
        		<% if (afso.getRunningSimId() == null) { %>
        <% } else { %>
        <p><%= enable_string %> running simulation <strong><%= running_sim.getRunningSimulationName() %></strong></p>
  
		<% if (running_sim.isReady_to_begin()) { %>
		Please note, disabling a simulation will prevent everyone from logging back on to it. <br/>
		(It will not cause people currently logged on to be logged off.)<br /><br />
		<% } else { %>
		Enabling a simulation will allow the players that you have assigned to log on to it.
		<% } %>
    <form action="facilitate_enable_simulation.jsp" method="post" name="form1" id="form1">
      <input type="hidden" name="sending_page" value="enable_game" />
	  <input type="hidden" name="enable_string" value="<%= enable_string %>" />
	  
      <table width="100%" border="1" cellspacing="0" cellpadding="2">
        <tr valign="top"> 
          <td width="34%"><%= enable_string %> the simulation:</td>
                <td width="66%"> <input type="submit" name="command" value="<%= enable_string %> Simulation" /></td>
              </tr>
        </table>
    </form>
    <p>&nbsp;</p>
    <p align="center">When you are done, <a href="facilitate_email_notifications.jsp">click here to move to the next step and notify your players.</a></p>
    <p align="left"></p>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
              </blockquote>
      <% } // end of if running_sim.id has been set. %>
        <%
		
	}// end of if afso.simulation.id has been set.

%>        <blockquote>
          <div align="center">
            <p align="left"><a href="facilitate_panel.jsp">To Simulation Launch Checklist</a></p>
            <p align="left"><a href="facilitate_assign_user_to_simulation.jsp"></a></p>
          </div>
          </blockquote>			</td>
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
