<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	RunningSimulation rs = pso. giveMeRunningSim();
	
%>
<html>
<head>
<title>Write AAR and End Sim Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h2>End Simulation</h2>
<p> Your current 'After Action Report' text is shown below. </p>
<form name="form1" method="post" action="../simulation/simwebui.jsp" target="_top">
 
<p>
<%= rs.getAar_text() %>
		  </p>
  
  
  <p>
    <label></label>
  </p>
  <p>
    <input type="submit" name="command" value="End Simulation" />
    <input type="hidden" name="sending_page" value="end_sim" />
  </p>
</form>
<p align="center">&nbsp;</p>

<p>&nbsp; </p>
<p>&nbsp;</p>
</body>
</html>
<%
	
%>
