<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.hibernate.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = "../simulation_facilitation/create_running_sim.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
	afso.handleAddRunningSimulation(request, simulation);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>USIP Open Simulation Platform</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>
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
			  <h1>Create Running Simulation<a href="helptext/create_running_sim_help.jsp" target="helpinright">(?)</a></h1>
			  <br />
            <blockquote> 
			  <span class="style1"><%= afso.errorMsg %></span>
			<% afso.errorMsg = ""; %>
            <% if (afso.sim_id != null) {  %>
			
              <p>Create running simulations for the simulation <strong><%= simulation.getDisplayName() %></strong>.
              </p>
              <p><% if (afso.getRunningSimId() != null) { %>
              You are currently working on running simulation <%= afso.run_sim_name %>.<% } %>
              </p>
              Below are the running simulation currently associated with the simulation <b><%= simulation.getSimulationName() %> </b> where you are a designated instructor. <br />
              <table width="80%" border = "1">
                <tr> 
                  <td><h2>Running Simulation</h2></td>
                  <td><h2>Enabled</h2></td>
                  <td><h2>Phase</h2></td>
            </tr>
                <%
		  	List rsList = RunningSimulation.getAllForSim(afso.sim_id.toString(), afso.schema);
			
			for (ListIterator li = rsList.listIterator(); li.hasNext();) {
				RunningSimulation rs = (RunningSimulation) li.next();
				
				SimulationPhase sp = new SimulationPhase();
				if (rs.getPhase_id() != null){
					sp = SimulationPhase.getById(afso.schema, rs.getPhase_id().toString());
				}
		%>
                <tr> 
                  <td><a href="administrate_running_simulation.jsp?rs_id=<%= rs.getId() %>"><%= rs.getRunningSimulationName() %></a></td>
                  <td>
				  <% if (rs.isReady_to_begin()){ %> true <% } else { %>false<% } %>
				  </td>
                  <td><%= sp.getPhaseName() %></td>
            </tr>
                <%
			}
		%>
                </table>
	          </blockquote>
            <form action="../simulation_authoring_play/create_running_sim.jsp" method="post" name="form1" id="form1">
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
            <p align="center"><a href="../simulation_authoring_play/create_schedule_page.jsp">Next step: Create Schedule Page</a></p>
            <% } else { // End of if have set simulation id. %>
            <blockquote> 
              <p>You must first select the simulation you want to work on from the library.                 </p>
                  </blockquote>
            <p>
              <% } // End of if have not set simulation for edits. %>
                  </p>
            <p>&nbsp;</p>
</td>
		</tr>
		</table>	</td>
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
</html>
<%
%>
