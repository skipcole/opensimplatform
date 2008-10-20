<%@ page 
	contentType="text/html; charset=ISO-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.baseobjects.*,
	org.usip.oscw.networking.*,
	org.usip.oscw.communications.*,
	org.usip.oscw.persistence.*" 
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
	String newsalert = (String) request.getParameter("newsalert");
	if ( (sending_page != null) && (newsalert != null) && (newsalert.equalsIgnoreCase("News Alert"))){

		// This really does not belong here. Specific news feeds should create a news alert.
		//pso.setNewsAlert(Alert.TYPE_ANNOUNCEMENT, request);
	
	} // End of if conditions met to give news alert
	////////////
	
	
	////////////
	String command = (String) request.getParameter("command");
	if ( (sending_page != null) && (command != null) && (command.equalsIgnoreCase("Change Phase"))){

		String phase_id = (String) request.getParameter("phase_id");
		
		System.out.println("changing phase to " + phase_id);
		
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

<p><%= debug %>Below are the following actions available to you. </p>
  
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="173" valign="top"><form id="form1" name="form1" method="post" action="control.jsp">
        <input type="submit" name="newsalert" value="News Alert" />
        <input type="hidden" name="sending_page" value="control" />
      </form></td>
    <td width="80%" valign="top"><p>&nbsp;</p>
      <p>&nbsp;</p></td>
  </tr>
  <tr>
    <td width="173" valign="top"><form id="form1" name="form1" method="post" action="control.jsp">
        <input type="submit" name="advance" value="Advance Round" />
        <input type="hidden" name="sending_page" value="control" />
      </form></td>
    <td width="80%" valign="top"><p>&nbsp;</p>
      <p>&nbsp;</p></td>
  </tr>

  <tr>
    <td valign="top"><strong>Change Phase </strong></td>
    <td valign="top">
	
	
<form id="form2" name="form2" method="post" action="control.jsp">
<input type="hidden" name="sending_page" value="control" />
                      <select name="phase_id">
                        <% for (ListIterator li = simulation.getPhases().listIterator(); li.hasNext();) {
							SimulationPhase sp = (SimulationPhase) li.next();
							
							String selected_p = "";
							
							if (sp.getId().intValue() == pso.phase_id.intValue()) {
								selected_p = "selected";
							}
							
							
				%>
                        <option value="<%= sp.getId().toString() %>" <%= selected_p %>><%= sp.getName() %></option>
                        <% } %>
                      </select>
                      <label>
                      <input type="submit" name="command" value="Change Phase" />
                      </label>
                      <label>
                      <input type="checkbox" name="notify_via_email" id="notify_via_email" value="true"/>
                      NotifyPlayersViaEmail</label>
</form>	</td>
  </tr>
</table>
  <p>
    
  </p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
</body>
</html>
<%
	
%>