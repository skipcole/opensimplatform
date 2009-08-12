<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.hibernate.*" 
	errorPage="" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getPSO(request.getSession(true), true);
	afso.backPage = "../simulation_facilitation/view_running_sim.jsp";
	
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
			  <h1>View Running Simulations</h1>
			  <br />
            <blockquote> 
              <% 
			if (afso.sim_id != null) {
		%>

              Below are the running simulation created by [Insert Creator's Name] </b>. <br />
              <table width="80%" border = "1">
                <tr> 
                  <td><h2>Running Simulation</h2></td>
              <td><h2>Phase</h2></td>
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
                  <td><a href="administrate_users.jsp"><%= rs.getName() %></a></td>
              <td><%= sp.getName() %></td>
            </tr>
                <%
			}
		%>
                </table>
	          </blockquote>

            <p align="center">&nbsp;</p>
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
		if (!(afso.isAuthor())) { %>
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
