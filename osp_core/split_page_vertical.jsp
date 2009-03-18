<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
    	org.usip.osp.networking.*,
        org.usip.osp.persistence.*,
        org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%	
    ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../simulation/index.jsp");
		return;
	}
	
	//SimulationSection ss = (SimulationSection) simSecList.get(tabpos - 1);
	//String leftFrame = ss.generateURLforBottomFrame(running_sim_id, actor_id, user_id);
	
	String leftFrame = "introduction.jsp";
	String rightFrame = "introduction.jsp";	
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
