<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.specialfeatures.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
		
	String sim_id = (String) request.getParameter("sim_id");
	String chart_id = (String) request.getParameter("chart_id");
	String sim_values_table = pso.simulation.db_tablename + "_values";

	IntegerVariable sv = new IntegerVariable();
	
	
%>
<html>
<head>
<title>Chart of Variable</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>

<body>
<p><!-- %= debug_string % --></p>
<p align="center"> <img src="../ver1/osp_core/<%= pso.base_servlet_location %>GraphServer?chart_id=<%= chart_id %>&sim_id=<%= sim_id %>&sim_round=<%= pso.simulation_round %>&sim_values_table=<%= sim_values_table %>"  border=2 /> 
</p>
<!-- img src="<%= pso.base_servlet_location %>GraphServer?chart_name=generic&sim_round=< % = pso.simulation_round %>&running_sim_id=< % = pso.running_sim.id %>"  border=2 / -->
</p>

</body>
</html>
<%
	
%>
