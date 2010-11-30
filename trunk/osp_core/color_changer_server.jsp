<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.communications.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" 
%>
<%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	String actor_id =  (String) request.getParameter("actor_id");
	String chatlinecolor =  (String) request.getParameter("chatlinecolor");

	pso.changeActorsColor(actor_id, chatlinecolor);
	
	String status_code = "no data to return";
	
	
%>
<?xml version="1.0"?>
<response>
 <status><%= status_code %></status>
</response>