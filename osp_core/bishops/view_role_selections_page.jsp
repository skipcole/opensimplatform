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
		
	List partyList = new ArrayList();
	
	Long selectedFirst = null;
	Long selectedSecond = null;
	Long selectedThird = null;
	
	if (!(pso.preview_mode)) {	
		partyList = BishopsPartyInfo.getAllForRunningSim(pso.schema, pso.running_sim_id, false);
		BishopsRoleVotes firstChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.running_sim_id, pso.user_id, 1);
		BishopsRoleVotes secondChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.running_sim_id, pso.user_id, 2);
		BishopsRoleVotes thirdChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.running_sim_id, pso.user_id, 3);
	
		if (firstChoice != null){
			selectedFirst = firstChoice.getId();
		}
		
		if (secondChoice != null){
			selectedSecond = secondChoice.getId();
		}
		
		if (thirdChoice != null){
			selectedSecond = thirdChoice.getId();
		}		
	
	}
	
	
	
%>
<html>
<head>
<title>Introduction Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<p>&nbsp;</p>
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
%>
  <tr>
    <td><%= ua.getUser_id() %></td>
    <td>Actor 3</td>
    <td>Actor 1</td>
    <td>Actor 5</td>
  </tr>
<% }

%>
</table>
<hr>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td><strong>Player</strong></td>
    <td><strong>Choice 1</strong></td>
    <td><strong>Choice 2</strong></td>
    <td><strong>Choice 3</strong></td>
  </tr>
  <tr>
    <td>Player 1</td>
    <td>Actor 3</td>
    <td>Actor 1</td>
    <td>Actor 5</td>
  </tr>
  <tr>
    <td>Player 2</td>
    <td>Actor 1</td>
    <td>Actor 3</td>
    <td>Actor 5</td>
  </tr>
  <tr>
    <td>Player 3</td>
    <td>Actor 2</td>
    <td>Actor 3</td>
    <td>Actor 4</td>
  </tr>
  <tr>
    <td>Player 4</td>
    <td>Actor 3</td>
    <td>Actor 4</td>
    <td>Actor 5</td>
  </tr>
  <tr>
    <td>Player 5</td>
    <td>Actor 4</td>
    <td>Actor 5</td>
    <td>Actor 3</td>
  </tr>
</table>
<hr>
<p>A place to decide how it will be</p>
<ul>
  <li>Player 1 --&gt; Actor 3</li>
  <li>Player 2 --&gt; Actor 1</li>
  <li>Player 3 --&gt; Actor 2</li>
  <li>Player 4 --&gt; Actor 4</li>
  <li>Player 5 --&gt; Actor 5</li>
</ul>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
</body>
</html>
