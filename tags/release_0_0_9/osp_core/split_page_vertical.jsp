<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
    	org.usip.osp.networking.*,
        org.usip.osp.persistence.*,
        org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%	
    PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../simulation/index.jsp");
		return;
	}
	
	//SimulationSectionAssignment ss = (SimulationSectionAssignment) simSecList.get(tabpos - 1);
	//String leftFrame = ss.generateURLforBottomFrame(running_sim_id, actor_id, user_id);
	String cs_id = (String) request.getParameter("cs_id");
	System.out.println("cs id was : " + cs_id);
	
	SimulationSectionAssignment sLeft =  SimulationSectionAssignment.getSubSection(pso.schema, new Long (cs_id), 1 , pso.sim_id,
			pso.actor_id, pso.phase_id);
			
		
	SimulationSectionAssignment sRight =  SimulationSectionAssignment.getSubSection(pso.schema, new Long (cs_id), 2 , pso.sim_id,
			pso.actor_id, pso.phase_id);
	
	String leftFrame = "../simulation/frame_bottom.jsp";
	String rightFrame = "../simulation/frame_bottom.jsp";
	
	if (sLeft != null){
		leftFrame = sLeft.generateURLforBottomFrame(pso.running_sim_id, pso.actor_id, pso.user_id);
	}
	if (sRight != null) {
		rightFrame = sRight.generateURLforBottomFrame(pso.running_sim_id, pso.actor_id, pso.user_id);
	}
    %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>USIP OSP Split Page Vertical</title>
</head>

<frameset cols="50%,50%">
  <frame src="<%= leftFrame %>" />
  <frame src="<%= rightFrame %>" />
</frameset>
<noframes><body>
</body>
</noframes></html>
