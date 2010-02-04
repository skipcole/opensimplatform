<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.bishops.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
	List partyList = new ArrayList();
	
	
	
	
%>
<html>
<head>
<title>Introduction Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<p>A Place to see what the players chose.</p>
<p>&nbsp;</p>

<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td><strong>Player</strong></td>
    <td><strong>Choice 1</strong></td>
    <td><strong>Choice 2</strong></td>
    <td><strong>Choice 3</strong></td>
  </tr>
  <%
  		for (ListIterator li = simulation.getActors(pso.schema).listIterator(); li.hasNext();) {
			Actor aa = (Actor) li.next();
			
			UserAssignment ua = UserAssignment.getUserAssignment (pso.schema, pso.running_sim_id, aa.getId());
			BishopsRoleVotes firstChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.running_sim_id, ua.getUser_id(), 1);
			BishopsRoleVotes secondChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.running_sim_id, ua.getUser_id(), 2);
			BishopsRoleVotes thirdChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.running_sim_id, ua.getUser_id(), 3);
			
			Long bpi_id_1 = null;
			Long bpi_id_2 = null;
			Long bpi_id_3 = null;
			
			if (firstChoice != null) {
				bpi_id_1 = firstChoice.getBishopsPartyInfoId();
			}
			if (secondChoice != null) {
				bpi_id_2 = secondChoice.getBishopsPartyInfoId();
			}
			if (thirdChoice != null) {
				bpi_id_3 = thirdChoice.getBishopsPartyInfoId();
			}
			
			String uName = USIP_OSP_Cache.getUSERName(pso.schema, request, ua.getUser_id());
%>
  <tr>
    <td><%= uName %></td>
    <td><%= BishopsPartyInfo.getBPIName(pso.schema, request, bpi_id_1)  %></td>
    <td><%= BishopsPartyInfo.getBPIName(pso.schema, request, bpi_id_2)  %></td>
    <td><%= BishopsPartyInfo.getBPIName(pso.schema, request, bpi_id_3)  %></td>
  </tr>
<% }

%>
</table>

<hr>
<p>A place to decide how it will be</p>
<ul>
  <li>Player 1 --&gt; Actor 3</li>
</ul>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
</body>
</html>
