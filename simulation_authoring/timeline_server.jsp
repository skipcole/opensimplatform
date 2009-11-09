<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %><%
	
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	response.setContentType("text/xml");
	
%><?xml version="1.0" encoding="utf-8"?>
<data>
<%= AuthorFacilitatorSessionObject.getEventsForPhase(afso.schema, afso.sim_id, afso.phase_id) %>
</data>