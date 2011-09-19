<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.bishops.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

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
			
			String uName = "user name";
			String c_1 = "tbd";
			String c_2 = "tbd";
			String c_3 = "tbd";
			
			if (pso.getRunningSimId() != null){
			
				UserAssignment ua = UserAssignment.getUserAssignment (pso.schema, pso.getRunningSimId(), aa.getId());
			
				if ((ua != null) && (ua.getUser_id() != null)) {
					BishopsRoleVotes firstChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.getRunningSimId(), ua.getUser_id(), 1);
					BishopsRoleVotes secondChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.getRunningSimId(), ua.getUser_id(), 2);
					BishopsRoleVotes thirdChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.getRunningSimId(), ua.getUser_id(), 3);
			
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
			
					uName = USIP_OSP_Cache.getUSERName(pso.schema, request, ua.getUser_id());
					c_1 = BishopsPartyInfo.getBPIName(pso.schema, request, bpi_id_1);
					c_2 = BishopsPartyInfo.getBPIName(pso.schema, request, bpi_id_2);
					c_3 = BishopsPartyInfo.getBPIName(pso.schema, request, bpi_id_3);
			
			
			}  // End of ua id not null
			}  // End of if running sim not null
			
%>
  <tr>
    <td><%= uName %></td>
    <td><%= c_1 %></td>
    <td><%= c_2 %></td>
    <td><%= c_3 %></td>
  </tr>
<% }

%>
</table>

<hr>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
</body>
</html>
