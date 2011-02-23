<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" errorPage="/error.jsp" %>
<%

	AuthorFacilitatorSessionObject.handleInitialEntry(request);
	
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	String loadSim = (String) request.getParameter("loadSim");
	
	String ftab = (String) request.getParameter("ftab");
	
	String topPage = "facilitate_top.jsp?ftab=" + ftab;
	String mainPage = "instructor_home.jsp?ftab=" + ftab;
	
	if (ftab != null) {
		
		if (ftab.equalsIgnoreCase("home")){
			mainPage = "instructor_home.jsp?ftab=home";
		}
		if (ftab.equalsIgnoreCase("library")){
			mainPage = "facilitate_library.jsp?ftab=library";
		}
		if (ftab.equalsIgnoreCase("my_sims")){
			mainPage = "view_running_sims.jsp?ftab=my_sims";
		}
		if (ftab.equalsIgnoreCase("misc")){
			mainPage = "misc_tools.jsp?ftab=misc";
		}
	
	}
	
	if ((loadSim != null) && (loadSim.equalsIgnoreCase("true"))){
		String sim_id = (String) request.getParameter("sim_id");
		
		afso.sim_id = new Long(sim_id);
		
		 mainPage = "facilitate_panel.jsp";
		
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
    <frame  name="headeruptop" src="<%= topPage %>">
    <frame name="bodyinleft" src="<%= mainPage %>">
  </frameset>
  
  <frame name="helpinright" src="helptext/facilitation_basichelp.jsp">
</frameset>
<noframes><body>

</body></noframes>
</html>
