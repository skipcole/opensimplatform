<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.hibernate.*" 
	errorPage="" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = "../simulation_facilitation/create_running_sim.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
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
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
              <tr>
                <td width="120"><img src="../Templates/images/white_block_120.png" /></td>
                <td width="100%"><h1> <br />
                    Inactivate Running Simulation</h1>
                  <br />
                  <blockquote>
                    <%
		
		List simList = Simulation.getAll(afso.schema);
		
		for (ListIterator lis = simList.listIterator(); lis.hasNext();) {
			Simulation sim = (Simulation) lis.next();
		%>
                    <p>Simulation <strong><%= simulation.getDisplayName() %></strong>.<br>
                    </p>
                    <p>Below are the running simulation currently associated with <b> <%= simulation.getSimulationName() %> </b>. <br />
                    </p>
                    <table width="100%" border = "1">
                      <tr>
                        <td><h2>Running Simulation</h2></td>
                        <td><h2>Phase</h2></td>
                        <td><h2>Last Login</h2></td>
                        <td><h2>Inactivate</h2></td>
                      </tr>
                      <%
		  	List rsList = RunningSimulation.getAllForSim(afso.sim_id.toString(), afso.schema);
			
			for (ListIterator li = rsList.listIterator(); li.hasNext();) {
				RunningSimulation rs = (RunningSimulation) li.next();
				
				SimulationPhase sp = new SimulationPhase();
				if (rs.getPhase_id() != null){
					sp = SimulationPhase.getMe(afso.schema, rs.getPhase_id().toString());
				}
		%>
                      <tr>
                        <td><%= rs.getRunningSimulationName() %></td>
                        <td><%= sp.getPhaseName() %></td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                      </tr>
                      <%
			}
		%>
                    </table>
                  </blockquote>
                  <p>&nbsp;</p>
                  
                 <% } // End of loop over sims. %>
                  </td>
              </tr>
            </table></td>
        </tr>
        <tr>
          <td><p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
        </tr>
      </table></td>
  </tr>
</table>
<p>&nbsp;</p>
<p align="center">&nbsp;</p>
</body>
</html>
<%
%>
