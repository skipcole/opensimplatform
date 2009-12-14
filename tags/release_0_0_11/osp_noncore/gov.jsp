<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" 
	errorPage="" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
%>
<html>
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<body>
<p align="center"><img src="images/gov.gif" width="822" height="893"></p>

<p>&nbsp; </p>
</body>
</html>
