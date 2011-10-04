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
<title>Share OSP Simulations</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<frameset rows="*" cols="75%,25%" border="1">
  <frameset rows="150,90%" border="0">
    <frame src="share_top.jsp">
    <frame name="bodyinleft" src="left.jsp">
  </frameset>
  
  <frame name="helpinright" src="../simulation_authoring/helptext/control_basichelp.jsp">
</frameset>
<noframes><body>

</body></noframes>
</html>
