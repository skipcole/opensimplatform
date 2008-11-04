<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
		
	RunningSimulation rs = pso.giveMeRunningSim();
	
	Hashtable grabBag = rs.getGrabBag();
	
	String sending_page = (String) request.getParameter("sending_page");
	String player_choice = (String) request.getParameter("player_choice");
	
	String selectedCop = "none";
	
	if ( (sending_page != null) && (player_choice != null) && (sending_page.equalsIgnoreCase("rol_lever"))){
		grabBag.put("rol_choice", player_choice);
		
		pso.hibernate_session.saveOrUpdate(rs);
		
	} // End of if coming from this page and have added simulation.
	
	selectedCop = (String) grabBag.get("rol_choice");
	
	String selectedA = "";
	String selectedB = "";
	String selectedC = "";
	
	if (selectedCop != null) {
		if (selectedCop.equalsIgnoreCase("A")){
			selectedA = "checked";
		} else if (selectedCop.equalsIgnoreCase("B")){
			selectedB = "checked";
		} else if (selectedCop.equalsIgnoreCase("C")){
			selectedC = "checked";
		}
	}
	
	
	
%>
<html>
<head>
<title>Rule of Law Lever</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<h2>Selection of 'Top Cop'</h2>
<p>Select your candidate from the three listed below.</p>
<p>Select very, very carefully. Since changing your choice once it has been made 
  <strong>may have consequences</strong>.</p>
<form name="form1" method="post" action="rol_lever.jsp">
<input type="hidden" name="sending_page" value="rol_lever" />
  <p>
    <input name="player_choice" type="radio" value="A" <%= selectedA %> >
    Person A</p>
  <p> 
    <input type="radio" name="player_choice" value="B" <%= selectedB %> >
    Person B</p>
  <p> 
    <input type="radio" name="player_choice" value="C" <%= selectedC %> >
    Person C</p>
  <p>
    <input type="submit" name="Submit" value="Submit">
  </p>
</form>
<p>&nbsp;</p>
</body>
</html>
<%
	
%>
