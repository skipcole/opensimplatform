<p>This page is never actually seen by the player.</p>
<p>It just resets their player session object - erasing all information in it 
  - and sends them back to the login page.</p>
<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" 
	errorPage="" %>
<%

	//HttpSession session = request.getSession(true);
	session.setAttribute("pso", null);
	
	response.sendRedirect("index.jsp");
	return;
%><%
	
%>