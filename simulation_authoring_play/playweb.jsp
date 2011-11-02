<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	String loadSim = (String) request.getParameter("loadSim");
	
	if ((loadSim != null) && (loadSim.equalsIgnoreCase("true"))){
		String sim_id = (String) request.getParameter("sim_id");

		System.out.println("sim id is " + sim_id);
		
		afso.sim_id = new Long(sim_id);
		
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<title>Simulation Play Testing</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
  <frameset rows="150,90%" border="0">
    <frame src="play_top.jsp">
    <frame name="bodyinleft" src="play_panel.jsp">
  </frameset>
<noframes><body>

</body></noframes>
</html>
