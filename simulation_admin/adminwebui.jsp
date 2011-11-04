<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject.handleInitialEntry(request);
	
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<title>USIP OSP Administration Section</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

  <frameset rows="150,90%" border="0">
    <frame src="admin_top.jsp">
    <frame name="bodyinleft" src="simulation_admin.jsp">
  </frameset>
  
<noframes><body>

</body></noframes>
</html>
