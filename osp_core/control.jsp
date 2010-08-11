<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
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
<p>On this page we will </p>

<table width="95%" border="0" cellspacing="2" cellpadding="2">
  <tr valign="top">
    <td valign="top"><p><a href="change_phase.jsp">Change Phase </a></p>    </td> 
    <td valign="top"><p>This page will allow you to </p>      </td>
  </tr>
  <tr valign="top">
    <td valign="top"><a href="become_player.jsp">Change Your Role </a></td>
    <td valign="top">This page allows you to become another player in the current simulation. (You will be able to change back, since the 'control' page will follow you.) </td>
  </tr>
  <tr valign="top">
    <td valign="top"><a href="make_announcement.jsp">Make Announcement</a> </td>
    <td valign="top">Push Annoucements out to your players. </td>
  </tr>
  <tr valign="top">
    <td valign="top"><a href="change_player.jsp">Swap/Remove/Add Players</a> </td>
    <td valign="top">This page allows you to change who is playing. </td>
  </tr>
  <tr valign="top">
    <td valign="top">&nbsp;</td>
    <td valign="top">&nbsp;</td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>
