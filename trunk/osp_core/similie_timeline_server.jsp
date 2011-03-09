<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %><%
	
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	String timeline_to_show = (String) request.getParameter("timeline_to_show");
	System.out.println(timeline_to_show);
	
	String textToShow = "";
	
	if ((timeline_to_show != null) && (timeline_to_show.equalsIgnoreCase("actual"))){
		textToShow = PlayerSessionObject.getInjectFiredForTimeline(pso.schema, pso.getRunningSimId());
	} else if (timeline_to_show != null){
		textToShow = PlayerSessionObject.getEventsForTimeline(pso.schema, new Long(timeline_to_show));
	} 
	
	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");
	
%><?xml version="1.0" encoding="utf-8"?>
<data>
<%= textToShow %>
</data>