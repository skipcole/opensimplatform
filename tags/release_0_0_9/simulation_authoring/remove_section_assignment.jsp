<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
	
<%
	String section_id = (String) request.getParameter("section_id");
	
	Integer newInt = new Integer(section_id);
	
	if (newInt.intValue() > 0) {

		GameSection.deleteSectionAndReOrder(section_id);

		response.sendRedirect("assign_section_to_game_actor.jsp");
	} // end of if section id was a number
	
%>
<html>
<head>
<title>Remove Section Assignment</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<p>Removed <%= section_id %></p>
<p><a href="../ver1/simulation_authoring/assign_section_to_game_actor.jsp">Back to section assignments</a></p>
</body>
</html>
