<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	com.seachangesimulations.osp.questions.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	
	String sending_page = (String) request.getParameter("sending_page");
	


	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	
%>
<html>
<head>
<title>Team Scores</title>
<script language="JavaScript" type="text/javascript" src="../../wysiwyg_files/wysiwyg.js">
</script>
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>

<p>Team Scores</p>
<table width="100%" border="1" cellspacing="0" cellpadding="2">
  <tr>
    <td>Date</td>
      <% 
	  
	  Enumeration simSets = RunningSimSet.getAllRunningSimsInSameSet(pso.schema, pso.getRunningSimId());
	  
	  int ii = 0;
		for (; simSets.hasMoreElements();) {
			Long key = (Long) simSets.nextElement();
			
			RunningSimulation rsTemp = RunningSimulation.getById(pso.schema, key);
			ii += 1;
			%>
    <td><%= rsTemp.getName() %></td>
	<% } // end of loop over running sims in set. %>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <% 
	simSets = RunningSimSet.getAllRunningSimsInSameSet(pso.schema, pso.getRunningSimId());
	for (; simSets.hasMoreElements();) {
			Long key = (Long) simSets.nextElement();
	
	%>
    <td> rsid: <%= key + "" %> </td>
    <% } %>
  </tr>
</table>
<p>&nbsp;</p>
<p>

  		
  running sim id:  <br />
  
  
  
</p>
<p>&nbsp;</p>
</body>
</html>