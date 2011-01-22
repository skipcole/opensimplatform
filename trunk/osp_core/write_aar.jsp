<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}

		
	pso.handleWriteAAR(request);

	
	if (pso.forward_on){
		pso.forward_on = false;
		response.sendRedirect("../simulation/simwebui.jsp?tabposition=1");
		return;
	}
		
	RunningSimulation rs = new RunningSimulation();
	
	if (!(pso.preview_mode)){
		rs = pso. giveMeRunningSim();
	}
	
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
<h2>Write After Action Report (AAR)</h2>
<p> The 'After Action Report' text shown below will be seen </p>

<form name="form1" method="post" action="write_aar.jsp">
 <p>
  <input type="submit" name="command" id="savechanges" value="Save Changes">
</p>
<p>
		  <textarea id="write_aar_end_sim" name="write_aar_end_sim" style="height: 310px; width: 710px;"><%= rs.getAar_text() %></textarea>
		<script language="javascript1.2">
  			generate_wysiwyg('write_aar_end_sim');
		</script>
		  </p>
  
  
  <p>
    <label></label>
  </p>
  <p>
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
