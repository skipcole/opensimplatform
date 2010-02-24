<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
%>
<html>
<head>
<title>Introduction Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<form name="form1" method="post" action="web_link_page.jsp">
  <label>
  <select name="select" id="select">
    <option value="99">Google Map 1</option>
    </select>
  </label> 
  <label>
  <input type="submit" name="button" id="button" value="Go!">
  </label>
  Description: 
</form>
<p>&nbsp;</p>
</body>
</html>
