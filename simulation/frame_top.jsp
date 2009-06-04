<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<%
	String tabposition = request.getParameter("tabposition");
	
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}	

%>
<html>
<head>
<title>Simulation <%= pso.simulation_name %>, Version , Session <%= pso.run_sim_name %></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style type="text/css">
<!--
.normal {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	font-style: normal;
	line-height: normal;
	color: #666666;
	background-color: #99CCFF;
	background-repeat: no-repeat;
	background-position: center;
}
.highlighted {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 16px;
	font-style: normal;
	line-height: normal;
	color: #FFFFFF;
	background-color: #0033CC;
	font-weight: bold;
}
-->
</style>
<script type="text/javascript">
function getSimRound()
  {
  var xmlHttp;
  try
    {
    // Firefox, Opera 8.0+, Safari
    xmlHttp=new XMLHttpRequest();
    }
  catch (e)
    {
    // Internet Explorer
    try
      {
      xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
      }
    catch (e)
      {
      try
        {
        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
      catch (e)
        {
        alert("Your browser does not support AJAX!");
        return false;
        }
      }
    }
    xmlHttp.onreadystatechange=function()
      {
      if(xmlHttp.readyState==4)
        {

		var sim_round_text = xmlHttp.responseText;
		
		array0 = sim_round_text.split("_");
		
		//document.getElementById('sim_round_div').innerHTML = array0[0];
		document.getElementById('sim_event_div').innerHTML = array0[1];
        }
      }
    xmlHttp.open("GET","sim_round_server.jsp?running_sim_id=<%= pso.running_sim_id %>&from_actor=<%= pso.actor_id %>&from_tab=<%= tabposition %>",true);
    xmlHttp.send(null);
  }
  
function timedCount()
{
	getSimRound()
	setTimeout("timedCount()",1000)
}
</script>
<link href="../usip_osp.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" onLoad="timedCount();">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><a href="about.jsp" target="_top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></a></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header"><%= pso.simulation_org %> </h1>
    
    <table width="80%" border="0" cellspacing="2" cellpadding="2">
        <tr> 
          <td>Scenario</td>
          <td>Session</td>
          <td>Your Role</td>
          <td>Phase</td>
          <!-- td>Round</td -->
        </tr>
        <tr> 
          <td><strong><%= pso.simulation_name %></strong></td>
          <td><strong><%= pso.run_sim_name %></strong></td>
          <td><strong><%= pso.actor_name %></strong></td>
          <td><strong><div id="sim_event_div">Loading...</div></strong></td>
          <!-- td><strong><div id="sim_round_div">Loading...</div></strong></td -->
        </tr>
      </table> 
    
    </td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
	    <table border="0" cellspacing="1" cellpadding="0">

        <tr>
          <td><div align="center"><a href="../simulation_user_admin/my_player_profile.jsp" class="menu_item" target="_top"><img src="../Templates/images/my_profile.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
        <tr>
          <td><div align="center"><a href="logout.jsp" target="_top" class="menu_item"><img src="../Templates/images/logout.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
      </table>	  
	  </div>
    </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"></td>
  </tr>
</table>
         </td>
	<td align="right">
    </td>
  </tr>
</table>
<table cellpadding="4" border="1">
	  <tr> 
	  	<%
			int ii = 1;
			
			List simSecList = pso.getSimSecList(request);
			
			for (ListIterator li = simSecList.listIterator(); li.hasNext();) {
				SimulationSectionGhost ssg = (SimulationSectionGhost) li.next();
				
				String divId = "plaintop";
			
				if (tabposition.equalsIgnoreCase(ii + "")) {
					divId = "highlighted";
				}
		%>
    	<td bgcolor="#<%= ssg.getTabColor() %>"> <a href="simwebui.jsp?tabposition=<%= ii %>" target="_parent">
	      <div align="center" class="<%= divId %>"><%= ssg.getTabHeading() %></div></a></td>
		<%
			++ii;
			} // End of loop over sections
		%>
  	</tr>
	</table>
</body>
</html>
<%
	
%>
