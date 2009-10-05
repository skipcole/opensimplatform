<%@ page contentType="text/html; charset=utf-8" language="java" 
	import="java.sql.*,org.usip.osp.networking.*" 
	errorPage="" %>
<%
AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>USIP OSP Control Page</title>
</head>

<body>
Below is a sample of what this page make look like during a simulation for  actor <%= afso.actor_being_worked_on_id %> in phase <%= afso.phase_id %> . 
URL: <%= afso.phase_id %>
</body>
</html>
