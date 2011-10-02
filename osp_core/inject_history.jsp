<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.communications.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
		
	String inject_id = (String) request.getParameter("inject_id");
	
	Inject inject = new Inject();
	
	if (inject_id != null) {
		inject = Inject.getById(pso.schema, new Long(inject_id));
	}
%>
<html>
<head>
<title>Introduction Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h1>Inject Firing History </h1>
<blockquote>
<table width="90%" border="1">
  <tr>
    <td><strong>Inject</strong></td>
    <td><%= inject.getInject_name() %></td>
  </tr>
  <tr>
    <td><strong>Inject Text</strong></td>
    <td><%= inject.getInject_text() %></td>
  </tr>
</table>
</blockquote>
<p>&nbsp;</p>
<h2>Uses this Running Simulation</h2>
<blockquote>
<%
	List useList = InjectFiringHistory.getAllForInjectAndRunningSim(pso.schema, pso.getRunningSimId(), inject.getId());
	
	if ((useList == null) || (useList.size() == 0)) {
%>
	<UL><LI>None</LI></UL>
<% } else { %>
  <table width="90%" border="1">
  
  <%
  		for (ListIterator lii = useList.listIterator(); lii.hasNext();) {
			InjectFiringHistory ifh = (InjectFiringHistory) lii.next();
  %>
    <tr>
      <td>From</td>
      <td><%= ifh.getId() %></td>
    </tr>
    <tr>
      <td>To</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>Firing Date</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>Text (if modified from original) </td>
      <td>&nbsp;</td>
    </tr>
	<% } %>
  </table>
  <% } %>
</blockquote>
<p>&nbsp;</p>
<h2>Uses in Other Running Simulations</h2>
<blockquote>
  <p>Check in Inject Firing history for events for other simulations </p>
  <table width="100%" border="0">
    <tr>
      <td>Running Sim Name</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>Instructor Name</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>Actors From and To </td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>Firing Date</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>Text (if modified from original) </td>
      <td>&nbsp;</td>
    </tr>
  </table>
  <blockquote>
    <p>&nbsp;</p>
  </blockquote>
</blockquote>
<h2>Uses in Imported Experience </h2>
<blockquote>
  <p>Check in Inject Firing history for imported events </p>
  <blockquote>
    <p>&nbsp;</p>
  </blockquote>
</blockquote>
<p>&nbsp;</p>
</body>
</html>
