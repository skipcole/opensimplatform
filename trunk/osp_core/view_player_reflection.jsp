<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.specialfeatures.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	

	List ref_list = new ArrayList();
	
	if (!(pso.preview_mode)) {
		ref_list = PlayerReflection.getReflections(pso.schema, pso.getRunningSimId());
	}
	
%>
<html>
<head>
<title>View Player Reflection</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
			
			SimulationPhase sp = SimulationPhase.getById(pso.schema, pr.getPhase_id());
			
		%>
  <tr>
    <td valign="top"><%= USIP_OSP_Cache.getActorName(pso.schema, pso.sim_id, pso.getRunningSimId(), request, pr.getA_id()) %></td>
    <td valign="top"><%= sp.getPhaseName() %></td>
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