<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.specialfeatures.*,
	org.usip.osp.bishops.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
	
	ArrayList tempList = BishopsBrainstormer.getDocs(request, pso);
	SharedDocument bneeds = (SharedDocument) tempList.get(0);
	SharedDocument bsolutions = (SharedDocument) tempList.get(1);
	
		
%>
<html>
<head>
<title>Introduction Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<p align="center">Enter the needs and proposed solutions for each country.</p>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="50%" valign="top">Submitter</td>
    <td width="50%" valign="top"><h1><strong>Brainstorm Needs</strong></h1></td>
    <td width="50%" valign="top"><h1><strong>Brainstorm Solutions</strong></h1></td>
  </tr>
  <tr>
    <td valign="top">Country (select color text) (edit) </td>
    <td valign="top"><%= bneeds.getBigString() %><br>
      <a href="brainstorm_data.jsp?sd_id=<%= bneeds.getId() %>">edit</a></td>
    <td valign="top"><%= bsolutions.getBigString() %><br>
      <a href="brainstorm_data.jsp?sd_id=<%= bsolutions.getId() %>">edit</a></td>
  </tr>
</table>
<form name="form1" method="post" action="">
  Add Country 
    <label>
  <input type="text" name="textfield">
(color)  </label>
  <label>
  <input type="submit" name="Submit" value="Submit">
  </label>
</form>
<p align="center">&nbsp;</p>
</body>
</html>
