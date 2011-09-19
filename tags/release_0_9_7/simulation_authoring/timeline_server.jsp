<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %><%
	
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	Long timeLineId = null;
	
	String timeline_id = (String) request.getParameter("timeline_id");
	
	if ((timeline_id != null) && (!(timeline_id.equalsIgnoreCase("null")))){
		timeLineId = new Long(timeline_id);
	}

	
	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");
	
%><?xml version="1.0" encoding="utf-8"?>
<data>
<%= AuthorFacilitatorSessionObject.getEventsForTimeline(afso.schema, timeLineId) %>
</data>