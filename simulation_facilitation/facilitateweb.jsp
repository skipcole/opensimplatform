<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	String loadSim = (String) request.getParameter("loadSim");
	
	if ((loadSim != null) && (loadSim.equalsIgnoreCase("true"))){
		String sim_id = (String) request.getParameter("sim_id");

		System.out.println("sim id is " + sim_id);
		
		pso.sim_id = new Long(sim_id);
		
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<title>USIP OSP Facilitate Simulation</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<frameset rows="*" cols="75%,25%" border="1">
  <frameset rows="150,90%" border="0">
    <frame  name="headeruptop" src="facilitate_top.jsp">
    <frame name="bodyinleft" src="instructor_home.jsp">
  </frameset>
  
  <frame name="helpinright" src="helptext/facilitation_basichelp.jsp">
</frameset>
<noframes><body>

</body></noframes>
</html>
