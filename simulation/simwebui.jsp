<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%

	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())){
		response.sendRedirect("index.jsp");
		return;
	}
	
	String tabposition = (String) request.getParameter("tabposition");
	
	String bottomFrame = "frame_bottom.jsp";
	
	int tabpos = 1;
	
	try {
		tabpos = new Integer(tabposition).intValue();
	} catch (Exception e){
		e.printStackTrace();
	}
	
	
	try {
	List simSecList = new SimulationSection().getBySimAndActorAndPhase(pso.schema, pso.sim_id, pso.actor_id, pso.phase_id);
	
	if (tabpos <= simSecList.size()) {
		SimulationSection ss = (SimulationSection) simSecList.get(tabpos - 1);
		bottomFrame = ss.generateURLforBottomFrame(pso.actor_id);
	}
	
		} catch (Exception e){
		e.printStackTrace();
		response.sendRedirect("index.jsp");
		return;
	}
	
	
%>
<html>
<head>
<title>OSP Simulation <%= pso.simulation_name %>, Session <%= pso.run_sim_name %></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<frameset rows="0,164,*,40" frameborder="yes" border="0" framespacing="0">
  <frame src="hiddenframe.jsp" name="hiddenframe" noresize="noresize" >
  <frame src="frame_top.jsp?tabposition=<%= tabposition %>" name="topFrame" noresize="resize" >
  <frame src="<%= bottomFrame %>" name="mainFrame"  noresize="resize">
  <frame src="frame_footer.jsp" name="footerFrame"  noresize="resize">
</frameset>
<noframes><body>

</body></noframes>
</html>
<%
	//
%>