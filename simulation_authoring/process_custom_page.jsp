<%@ page contentType="text/html; charset=iso-8859-1" language="java" 
import="java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
errorPage="../error.jsp" %>
<%
	
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
		
	pso.handleProcessCustomPage(request);
	
	response.sendRedirect(pso.backPage);
	return;
	
%>
