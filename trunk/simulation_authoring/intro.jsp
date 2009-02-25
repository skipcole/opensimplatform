<%@ page contentType="text/html; charset=iso-8859-1" language="java" 
import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" errorPage="../error.jsp" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>USIP Open Simulation Platform</title>
</head>

<frameset rows="150,90%" border="0">
  <frame src="welcome_top.jsp" />
  <frame src="intro_text.jsp" />
</frameset>
<noframes><body>
</body>
</noframes></html>
