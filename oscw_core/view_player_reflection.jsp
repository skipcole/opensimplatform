<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,
	org.usip.oscw.persistence.*,
	org.usip.oscw.specialfeatures.*,
	org.usip.oscw.baseobjects.*" 
	errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	

	List ref_list = PlayerReflection.getReflections(pso.schema, pso.running_sim_id);
	
%>
<html>
<head>
<title>View Player Reflection</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"></head>

<body>

<h1>Player Reflection </h1>

<blockquote>

<table width="80%" border="1">
<tr>
      <td width=50><strong>Actor</strong></td>
      <td>CS_ID</td>
      <td><strong>Reflection</strong></td>
</tr>
  <%
  		for (ListIterator li = ref_list.listIterator(); li.hasNext();) {
			PlayerReflection pr = (PlayerReflection) li.next();
			
		%>
  <tr>
    <td valign="top"><%= pso.getActorName(request, pr.getA_id()) %></td>
    <td valign="top">&nbsp;</td>
    <td valign="top"><%= pr.getBigString() %></td>
  </tr>

  <%
	}
%>
</table>
</blockquote>
<p>&nbsp;</p>
<p></p>
</body>
</html>
<%
	
%>