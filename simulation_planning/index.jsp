<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.persistence.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../simulation_authoring/index.jsp");
		return;
	}

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">

<html>
<head>
<title>Create Simulation - Think</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

  <frameset rows="150,90%" border="0">
    <frame name="headeruptop" src="think_top.jsp">
    <frame name="bodyinleft" src="general_advice.jsp">
  </frameset>

<noframes><body>

</body></noframes>
</html>
