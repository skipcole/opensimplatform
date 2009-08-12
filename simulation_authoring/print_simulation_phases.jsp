<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true), true);
	afso.backPage = "create_simulation_phases.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
	///////////////////////////////////////////////////////////
	String sending_page = (String) request.getParameter("sending_page");
	SimulationPhase spOnScratchPad = afso.handleCreateOrUpdatePhase(simulation, request);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Open Simulation Platform Control Page</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="100%"><br />
			  <h1>Simulation Phases</h1>
			  <br />
            <blockquote> 
              <% 
			if (afso.sim_id != null) {
			
			List phaseList = SimulationPhase.getAllForSim(afso.schema, afso.sim_id);

		%>
              <blockquote>
                <p>Phases for the simulation <strong><%= simulation.getDisplayName() %></strong>. </p>
            <table width="100%" border="1" cellspacing="2" cellpadding="2">
              <tr> 
                <td width="20%" valign="top"><h2>Phase Name</h2></td>
                <td width="80%" valign="top"><h2>Phase Notes</h2></td>
                <td width="40" valign="top"><h2>N.O.</h2></td>
                </tr>
              <%
		for (ListIterator li = phaseList.listIterator(); li.hasNext();) {
			SimulationPhase sp = (SimulationPhase) li.next();
			
			String flagNotes = "";
			if (sp.isFirstPhase()){
				flagNotes = "<I><small>(First Phase)</small></I>";
			}
			if (sp.isLastPhase()){
				flagNotes = "<I><small>(Last Phase)</small></I>";
			}
			
			
		%>
              <tr>
                <td valign="top"><a href="create_simulation_phases.jsp?command=Edit&sp_id=<%= sp.getId().toString() %>"><%= sp.getName() %></a>  <%= flagNotes %></td>
                <td valign="top"><%= sp.getNotes() %></td> 
                <td valign="top"><%= sp.getOrder() + "" %></td>
                </tr>
              <%
	}
%>
              </table>
            <p>&nbsp;</p>
              
          <div align="center"></div>
          </blockquote>
            </blockquote>
            <% } else { // End of if have set simulation id. %>
            <blockquote>
              <p>
                <%@ include file="select_message.jsp" %></p>
                  </blockquote>
            <% } // End of if have not set simulation for edits. %>            <a href="create_simulation_planned_play_ideas.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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