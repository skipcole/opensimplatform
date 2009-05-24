<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.persistence.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}

	String sending_page = (String) request.getParameter("sending_page");
	
	String select_running_sim = (String) request.getParameter("select_running_sim");
	
	if ((select_running_sim != null) && (select_running_sim.equalsIgnoreCase("true"))){
		
		Long r_sim_id = new Long(   (String) request.getParameter("r_sim_id")   );
		
		pso.running_sim_id = r_sim_id;
		
		RunningSimulation rs = pso.giveMeRunningSim();
		
		pso.run_sim_name = rs.getName();
		response.sendRedirect(pso.backPage);
		return;
			
	}
	
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Open Simulation Platform Control Page</title>



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
              <h1>Select Running Simulation</h1>
              <br />
			 
      <blockquote>
        <p>&nbsp;</p>
          <p>Below are listed alphabetically all of the current running simulations for the simulation 
            <%= simulation.getDisplayName() %>. </p>
          <p>Please 
            select one, or <a href="../simulation_facilitation/create_running_sim.jsp">create a new one</a>, 
            to continue.</p>
          <table>
            <%
		
		MultiSchemaHibernateUtil.beginTransaction(pso.schema);
		simulation = (Simulation) MultiSchemaHibernateUtil.getSession(pso.schema).get(Simulation.class, simulation.getId());
		for (ListIterator li = simulation.getRunning_sims(pso.schema).listIterator(); li.hasNext();) {
			RunningSimulation rs = (RunningSimulation) li.next();
			
			if (rs != null) {
		%>
            <tr> 
              <td><a href="select_running_simulation.jsp?select_running_sim=true&backpage=<%= pso.backPage %>&r_sim_id=<%= rs.getId() %>"><%= rs.getName() %></a></td>
            </tr>
            <%
	}
	}
	
		MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);

%>
            </table>
      </blockquote>      <p align="center">&nbsp;</p>			</td>
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
