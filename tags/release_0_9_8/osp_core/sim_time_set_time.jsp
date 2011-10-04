<%@ page 
	contentType="text/html; charset=utf-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" 
%>
<%
	
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	String newtime = (String) request.getParameter("newtime");
	
	if (USIP_OSP_Util.stringFieldHasValue(newtime)){
		pso.gameTime = newtime;
	}
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

</head>
<body>
<p>&nbsp;</p>
<form name="form1" method="post" action="sim_time_set_time.jsp">
  <p>
    <label for="textfield"></label>
    <input type="text" name="newtime" id="textfield" value="<%= pso.gameTime %>" />
  </p>
  <p>
    <input type="submit" name="button" id="button" value="Submit">
  </p>
</form>
</body>
</html>