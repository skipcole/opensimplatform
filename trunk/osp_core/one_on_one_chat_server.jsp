<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.communications.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" 
%>
<%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		return;
	}
	
	ArrayList <String> chat = pso.getChat(request);
	
%>
<?xml version="1.0"?>
<response>
 <status><%= chat.get(0) %></status>
 <%= chat.get(1) %>
</response>