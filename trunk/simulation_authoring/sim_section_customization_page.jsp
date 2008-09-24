<%@ page contentType="text/html; charset=iso-8859-1" language="java" 
import="java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" 
errorPage="" %>
<%
	session.setAttribute("custom_page", request.getParameter("custom_page"));	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Untitled Document</title>
</head>

<frameset rows="*" cols="75%,25%">
  
  <frame src="customize_page.jsp">
  <frame src="helptext/control_basichelp.jsp">
</frameset>
<noframes><body>
</body>
</noframes></html>
