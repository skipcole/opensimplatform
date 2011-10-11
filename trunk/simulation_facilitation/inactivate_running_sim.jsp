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
	
	String sending_page = (String) request.getParameter("sending_page");
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("inactivate_rs"))){
	
		String action = (String) request.getParameter("action");
		String rsid = (String) request.getParameter("rsid");
		
		System.out.println("a/r " + action + "/" + rsid);
		
		if (action != null) {
		
			RunningSimulation rs = RunningSimulation.getById(afso.schema, new Long(rsid));
		
			if (action.equalsIgnoreCase("Inactivate")) {
				rs.setInactivated(true);
			} else if (action.equalsIgnoreCase("Activate")) {
				rs.setInactivated(false);
			}
			
			rs.saveMe(afso.schema);
		}
	
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>USIP Open Simulation Platform</title>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
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
                    Inactivate/Activate Running Simulation</h1>
                  <br />
                  <blockquote>
                    <%
		
		List simList = Simulation.getAll(afso.schema);
		
		for (ListIterator lis = simList.listIterator(); lis.hasNext();) {
			Simulation this_sim = (Simulation) lis.next();
		%>
                    <p>Simulation <strong><%= this_sim.getDisplayName() %></strong>.<br>
                    </p>
                    <p>Below are the running simulation currently associated with <b> <%= this_sim.getSimulationName() %> </b>. <br />
                    </p>
                    <table width="100%" border = "1">
                      <tr>
                        <td><h2>Running Simulation</h2></td>
                        <td><h2>Phase</h2></td>
                        <td><h2>Last Login</h2></td>
                        <td><h2>Inactivate /Activate</h2></td>
                      </tr>
                      <%
		  	List rsList = RunningSimulation.getAllForSim(this_sim, afso.schema);
			
			for (ListIterator li = rsList.listIterator(); li.hasNext();) {
				RunningSimulation rs = (RunningSimulation) li.next();
				
				SimulationPhase sp = new SimulationPhase();
				if (rs.getPhase_id() != null){
					sp = SimulationPhase.getById(afso.schema, rs.getPhase_id().toString());
				}
				
				String actionTitle = "Inactivate";
				if (rs.isInactivated()){
					actionTitle = "Activate";
				}
		%>
                      <tr>
                        <td><%= rs.getRunningSimulationName() %></td>
                        <td><%= sp.getPhaseName() %></td>
                        <td>&nbsp;</td>
                        <td>
                        <form id="form<%= rs.getId() %>" name="form<%= rs.getId() %>" method="post" action="inactivate_running_sim.jsp?rsid=<%= rs.getId() %>&action=<%= actionTitle %>">
                        <input type="hidden" name="sending_page" value="inactivate_rs">  
                            <input type="submit" name="button" id="button" value="<%= actionTitle %>" />
                            
                        </form>
                        </td>
                      </tr>
                      <%
			}
		%>
                    </table>
                  
                  
                 <% } // End of loop over sims. %>
                 
                 </blockquote>
                  <p>&nbsp;</p>
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
