<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
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
<title>OSP Simulation <%= pso.simulation_name %>, Session <%= pso.run_sim_name %></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>
<frameset rows="0,164,*,40" frameborder="yes" border="0" framespacing="0">
  <frame src="hiddenframe.jsp" name="hiddenframe" noresize="noresize">
  <frame src="frame_top.jsp?tabposition=<%= pso.tabposition %>" name="topFrame" noresize="resize" scrolling="no">
  <frame src="<%= pso.bottomFrame %>" name="mainFrame"  noresize="resize">
  <frame src="frame_footer.jsp" name="footerFrame"  noresize="resize">
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>
