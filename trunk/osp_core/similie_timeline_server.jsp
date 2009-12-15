<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %><%
	
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String timeline_to_show = (String) request.getParameter("timeline_to_show");
	
	String textToShow = "";
	
	if ((timeline_to_show != null) && (timeline_to_show.equalsIgnoreCase("show_plan")  ) ){
		//textToShow = AuthorFacilitatorSessionObject.getEventsForPhase(pso.schema, pso.sim_id, pso.phase_id);
		// Make this work for more than just one timeline
		textToShow = AuthorFacilitatorSessionObject.getEventsForPhase(pso.schema, pso.sim_id, new Long(1));
	} else {
		textToShow = pso.getSimilieEvents();
	}
	
	response.setContentType("text/xml");
	
%><?xml version="1.0" encoding="utf-8"?>
<data>
<%= textToShow %>
</data>