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
			
	String sending_page = (String) request.getParameter("sending_page");
		
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("end_sim"))){
		
		pso.handleWriteAARandEndSim(request);
		
		if (pso.forward_on){
			pso.forward_on = false;
			response.sendRedirect("../simulation/simwebui.jsp?tabposition=1");
			return;
		}

	} // End of if conditions met to end simulation
	
	RunningSimulation rs = pso. giveMeRunningSim();
	
%>
<html>
<head>
<title>Write AAR and End Sim Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>

<body>
<h2>End Simulation</h2>
<p> Your current 'After Action Report' text is shown below. </p>
<form name="form1" method="post" action="write_aar_end_sim.jsp">
 
<p>
<%= rs.getAar_text() %>
		  </p>
  
  
  <p>
    <label></label>
  </p>
  <p>
    <input type="submit" name="command" value="End Simulation" />
    <input type="hidden" name="sending_page" value="end_sim" />
  </p>
</form>
<p align="center">&nbsp;</p>

<p>&nbsp; </p>
<p>&nbsp;</p>
</body>
</html>
<%
	
%>
