<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*,org.usip.osp.networking.*" errorPage="/error.jsp" %>
<%
	String test = " sat ";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Untitled Document</title>
</head>

<body>

<p>echo this: <%= test %></p>
<p>echo from AFSO: <%= AuthorFacilitatorSessionObject.debugStuff %> </p>
</body>
</html>