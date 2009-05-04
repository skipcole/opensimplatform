<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<%

	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	pso.handleEndSim(request);
	
	if (pso.forward_on){
		pso.forward_on = false;
		response.sendRedirect(pso.backPage);
		pso.backPage = "index.jsp";
		return;
	}
	
	if (  (!(pso.isLoggedin()))){
		response.sendRedirect("index.jsp");
		pso.forward_on = false;
		return;
	}
	
	pso.handleSimWeb(request);
	
%>
<html>
<head>
<title>OSP Simulation<%= pso.simulation_name %>, Session<%= pso.run_sim_name %></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<frameset rows="0,164,*,40" frameborder="yes" border="0" framespacing="0">
  <frame src="hiddenframe.jsp" name="hiddenframe" noresize="noresize" >
  <frame src="frame_top.jsp?tabposition=<%= pso.tabposition %>" name="topFrame" noresize="resize" scrolling="no">
  <frame src="<%= pso.bottomFrame %>" name="mainFrame"  noresize="resize">
  <frame src="frame_footer.jsp" name="footerFrame"  noresize="resize">
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>
<%
	//
%>
