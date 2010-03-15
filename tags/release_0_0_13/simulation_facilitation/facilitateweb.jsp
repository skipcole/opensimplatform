<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" errorPage="" %>
<%

	AuthorFacilitatorSessionObject.handleInitialEntry(request);
	
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	String loadSim = (String) request.getParameter("loadSim");
	
	String mainPage = "instructor_home.jsp";
	
	if ((loadSim != null) && (loadSim.equalsIgnoreCase("true"))){
		String sim_id = (String) request.getParameter("sim_id");

		System.out.println("sim id is " + sim_id);
		
		afso.sim_id = new Long(sim_id);
		
		 mainPage = "play_panel.jsp";
		
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<title>USIP OSP Facilitate Simulation</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<frameset rows="*" cols="75%,25%" border="1">
  <frameset rows="150,90%" border="0">
    <frame  name="headeruptop" src="facilitate_top.jsp">
    <frame name="bodyinleft" src="<%= mainPage %>">
  </frameset>
  
  <frame name="helpinright" src="helptext/facilitation_basichelp.jsp">
</frameset>
<noframes><body>

</body></noframes>
</html>
