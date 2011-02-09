<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" 
	errorPage="/error.jsp" %>
<%

	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.logout(request);
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	pso.logout(request);
	
	session.setAttribute("afso", null);
	session.setAttribute("osp_soh", null);
	session.setAttribute("pso", null);
	session.setAttribute("sob", null);
	
	response.sendRedirect("login.jsp");
	return;
%>