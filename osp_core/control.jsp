<%@ page 
	contentType="text/html; charset=ISO-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.baseobjects.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.persistence.*" 
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
	
	String advance = (String) request.getParameter("advance");
	
	String reset = (String) request.getParameter("reset");
	
	String endsimulation = (String) request.getParameter("endsimulation");
	
	
	if ( (sending_page != null) && (advance != null) && (sending_page.equalsIgnoreCase("control"))){

		pso.advanceRound(request);
	
	} // End of if conditions met to advance round
	
	
	
	////////////
	String command = (String) request.getParameter("command");
	if ( (sending_page != null) && (command != null) && (command.equalsIgnoreCase("Change Phase"))){

		String phase_id = (String) request.getParameter("phase_id");
		pso.changePhase(phase_id, request);
		
		return;
	
	} // End of if conditions met to give news alert
	
	
	String debug = "";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Simulation Master Control Page</title>
</head>

<body>
<h1>Greetings Controller </h1>

<p><%= debug %> </p>

<p>Change phase to one of the phases listed below.</p>
          <table width="100%" border="1" cellspacing="2" cellpadding="2">
            <tr> 
              <td width="20%" valign="top"><h2>Phase Name</h2></td>
              <td width="80%" valign="top"><h2>Phase Notes</h2></td>
              <td width="40" valign="top"><h2>N.O.</h2></td>
              <td width="40" valign="top"><h2>Change to this Phase</h2></td>
              <td width="40" valign="top"><h2>Email Players</h2></td>
            </tr>
       <%
	   
	   List phaseList = new SimulationPhase().getAllForSim(pso.schema, pso.sim_id);
	   
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
        <form id="form2" name="form2" method="post" action="control.jsp">
<input type="hidden" name="sending_page" value="control" />
            <tr>
              <td valign="top"><%= sp.getName() %>  <%= flagNotes %></td>
              <td valign="top"><%= sp.getNotes() %></td> 
              <td valign="top"><%= sp.getOrder() + "" %></td>
              <td valign="top">
              	<input type="hidden" name="phase_id" value="<%= sp.getId().toString() %>" />
                <input type="submit" name="command" value="Change Phase"  onClick="return confirm('Are you sure you want to change phase?');" />
              </td>
              <td valign="top"><input type="checkbox" name="notify_via_email" id="notify_via_email" value="true"/></td>
            </tr>
            </form>
            <%
	}
%>
          </table>
          <p>&nbsp;</p>

</body>
</html>
<%
	
%>