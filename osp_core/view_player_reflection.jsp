<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.specialfeatures.*,
	org.usip.osp.baseobjects.*" 
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
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>

<body>

<h1>Player Reflection </h1>

<blockquote>

<table width="80%" border="1">
<tr>
      <td width=50><strong>Actor</strong></td>
      <td><strong>Phase</strong></td>
      <td><strong>Reflection</strong></td>
</tr>
  <%
  		for (ListIterator li = ref_list.listIterator(); li.hasNext();) {
			PlayerReflection pr = (PlayerReflection) li.next();
			
			SimulationPhase sp = pso.getPhaseNameById(pr.getPhase_id());
			
		%>
  <tr>
    <td valign="top"><%= pso.getActorName(request, pr.getA_id()) %></td>
    <td valign="top"><%= sp.getName() %></td>
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