<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,
	java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%
	String schema_id = (String) request.getParameter("schema_id");
	String uri = (String) request.getParameter("uri");
	String initial_entry = (String) request.getParameter("initial_entry");
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Open Simulation Platform Auto-Registration Page</title>
</head>

<frameset rows="*" cols="75%,25%">
  <frame src="auto_registration_page.jsp?initial_entry=<%= initial_entry %>&schema_id=<%= schema_id %>&uri=<%= uri %>" />
  <frame name="helpinright" src="helptext/user_basichelp.jsp">
</frameset>
<noframes><body>
</body>
</noframes></html>
