<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
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
<p>On this page control can perform the functions listed below.</p>
<h2>Core Functions </h2>
<table width="95%" border="0" cellspacing="2" cellpadding="2">
  <tr valign="top">
    <td valign="top"><a href="change_phase.jsp">Change Phase </a>    </td> 
    <td valign="top">This page will allow you to change the phase of the simulation.      </td>
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
    <td valign="top"><a href="push_injects.jsp">Push Inject </a></td>
    <td valign="top">Pushes a pre-planned annoucement (in common simulation parlance called an inject) out to the players. </td>
  </tr>
  <!-- tr valign="top">
    <td valign="top"><a href="change_player.jsp">Swap/Remove/Add Players</a> </td>
    <td valign="top">This page allows you to change who is playing. </td>
  </tr
  -->
  <tr valign="top">
    <td valign="top"><a href="similie_review_timeline.jsp">Review Injects Sent</a> </td>
    <td valign="top">Allows you to look at a timeline of the injects you have sent out. </td>
  </tr>
  <tr valign="top">
    <td valign="top"><a href="similie_review_phases_timeline.jsp?timeline_to_show=phases">Review Phase Changes</a></td>
    <td valign="top">Allows you to see a history of all of the phase changes.</td>
  </tr>
  <tr valign="top">
    <td valign="top"><a href="finger.jsp" -->View Last Logins </a></td>
    <td valign="top">This page allows you to see when your players have logged in. (in repair shop)    </td>
  </tr>
  <tr valign="top">
    <td valign="top"><a href="write_aar.jsp">Write AAR</a> </td>
    <td valign="top">This page allows you to work on the 'After Action Report'. </td>
  </tr>
</table>
<p>&nbsp;</p>
<h2>Additional Fuctions</h2>
<table width="95%" border="0" cellpadding="2" cellspacing="2">
  <tr valign="top">
    <td valign="top"><a href="set_onelink_all.jsp">Change a Fixed (One) Link </a></td>
    <td valign="top">Allows the editing of any fixed link pages accessed by the simulation.</td>
  </tr>
  <tr valign="top">
    <td valign="top"><a href="write_document_list_all.jsp">Edit simulation document </a></td>
    <td valign="top">Access and edit any document currently associated with this running simulation. </td>
  </tr>
  <tr valign="top">
    <td valign="top"><a href="email_player.jsp">Email your Players</a> </td>
    <td valign="top">Send an email to players in this simulation. </td>
  </tr>
  <tr valign="top">
    <td width="32%" valign="top"><a href="make_rating_announcement.jsp">Make Rating Announcement </a></td>
    <td width="68%" valign="top">On this  page will be able to send a message including a 'star' rating to tell your students how they are doing. </td>
  </tr>
  <% if (simulation.isUsesGameClock()){ %>
  <tr valign="top">
    <td valign="top"><a href="sim_time_controls.jsp">Simulation Timer Controls</a></td>
    <td valign="top">Allows you to start/stop/pause/reset etc. the game clock.</td>
  </tr>
  <% } %>
</table>
<p>&nbsp; </p>



</body>
</html>
