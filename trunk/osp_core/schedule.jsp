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
	
	String displayString = "Instructor generated schedule will go here.";
	
	String change_schedule = request.getParameter("change_schedule");
	
	if (change_schedule != null) {
		String new_schedule = request.getParameter("new_schedule");
		RunningSimulation rs = RunningSimulation.getById(pso.schema, pso.getRunningSimId());
		rs.setSchedule(new_schedule);
		rs.saveMe(pso.schema);
	}
	
	if (!(pso.preview_mode)){
		displayString = pso.giveMeRunningSim().getSchedule();
	}
	
%>
<html>
<head>
<title>Schedule Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<table width="95%" border="0" cellspacing="2" cellpadding="2">
  <tr valign="top"> 
    <td><p><%= displayString %></p>
    </td>
  </tr>
</table>
<p>&nbsp;</p>
<p>
  <% 
	if (pso.isControlCharacter()) {
%>
</p>
<form name="form1" method="post" action="schedule.jsp">
  <label>
  <textarea name="new_schedule" cols="80" rows="5"><%= displayString %></textarea>
  </label>
  <p>
    <label>
	<input type="hidden" name="change_schedule" value="true">
    <input type="submit" name="update" value="Update">
    </label>
  </p>
  <p>(Your editing interface will improve soon.)</p>
</form>
<p>&nbsp;</p>
<p>
  <%
} // End of if is control character
%>
</p>
</body>
</html>
