<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.hibernate.*" 
	errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	pso.backPage = "../simulation_facilitation/create_running_sim.jsp";
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
	pso.handleAddRunningSimulation(request, simulation);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>USIP Open Simulation Platform</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
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
			  <h1>Create Running Simulation</h1>
			  <br />
            <blockquote> 
              <% 
			if (pso.sim_id != null) {
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
				
				SimulationPhase sp = new SimulationPhase();
				if (rs.getPhase_id() != null){
					sp = SimulationPhase.getMe(pso.schema, rs.getPhase_id().toString());
				}
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
              <p>                </p>
            <%@ include file="../simulation_authoring/select_message.jsp" %>
                  </blockquote>
            <p>
              <% } // End of if have not set simulation for edits. %>
                  </p>
            <p>&nbsp;</p>
            <% 
		if (!(pso.isAuthor())) { %>
	  		<a href="instructor_home.jsp" target="_top">&lt;-- Back            </a>
	        <% } %>			</td>
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
