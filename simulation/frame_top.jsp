<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	String tabposition = request.getParameter("tabposition");
	
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}	

%>
<html>
<head>
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script type="text/javascript" src="../third_party_libraries/sticky_full/sticky.full.js"></script>
<link rel="stylesheet" href="../third_party_libraries/sticky_full/sticky.full.css" type="text/css" />

<title>Simulation <%= pso.simulation_name %>, Version , Session <%= pso.run_sim_name %></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
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

var gaveLogoutWarning = false;

function getSimRound()
  {

  	$.get('sim_phase_server.jsp', {
		'running_sim_id': "<%= pso.getRunningSimId() %>", 
		'from_actor': "<%= pso.getActorId() %>", 
		'from_tab': "<%= tabposition %>"}, 
		
		function(myFunction){
		
			var my_status = $("my_status",myFunction).text();
			var sim_time = $("sim_time",myFunction).text();
			var sim_phase = $("sim_phase",myFunction).text();
			
			if (my_status == "logout"){
				if (!(gaveLogoutWarning)) {
					gaveLogoutWarning = true;	// Just give the warning once.
					alert("You have been logged out.");
					top.document.location="../logout.jsp";
				}
			 	
			} else {
				document.getElementById('sim_phase_div').innerHTML = sim_phase;
				document.getElementById('sim_time_div').innerHTML = sim_time;
        	}
			
		} , 'xml');

  } // End of get sim round

  
function timedCount()
{
	getSimRound();
	setTimeout("timedCount()", 3000);
}
</script>
<link href="../usip_osp.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
.style1 {font-size: 9pt}
-->
</style>

<script>
function stickyAlert(alertWords) {
    $.sticky(alertWords);
}
</script> 

</head>
<body bgcolor="#FFFFFF" onLoad="timedCount();">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr  background="images/<%= pso.getSchemaOrgBanner() %>" >
    <td width="23" valign="top"  background="images/<%= pso.getSchemaOrgBanner() %>" ><span class="header"><img src="../Templates/images/resize.gif" width="23" height="22" border="0" usemap="#Map"></span>
            <map name="Map">
              <area shape="rect" coords="1,1,23,11" href="simwebui.jsp?lessten=true" target="_top" alt="resize smaller">
              <area shape="rect" coords="1,12,23,22" href="simwebui.jsp?addten=true" target="_top" alt="resize smaller">
            </map></td>
    <td width="120" valign="top"  background="images/<%= pso.getSchemaOrgBanner() %>" ><a href="about.jsp" target="mainFrame"><img src="images/<%= pso.getSchemaOrgLogo() %>" width="120" height="100" border="0" /></a></td>
    <td width="80%" valign="middle"   background="images/<%= pso.getSchemaOrgBanner() %>" >    
    <table width="100%" border="0" cellspacing="2" cellpadding="2">
        <tr>
          <td colspan="3" valign="top"><span class="header"><%= pso.simulation_org %></span></td>
          <td align="right" valign="top">
          	<div id="sim_time_div"></div>
          </td>
        </tr>
        <tr> 
          <td><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "scenario") %></td>
          <td><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "session") %></td>
          <td><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "your_role") %></td>
          <td><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "phase") %>
		</td>
        </tr>
        <tr> 
          <td><strong><a href="../osp_core/introduction.jsp" target="mainFrame"><%= pso.simulation_name %></a></strong></td>
          <td><strong><a href="../osp_core/schedule.jsp" target="mainFrame"><%= pso.run_sim_name %></a></strong></td>
          <td><strong><%= pso.getActorName() %></strong></td>
          <td><strong><div id="sim_phase_div">Loading...</div></strong></td>
          <!-- td><strong><div id="sim_round_div">Loading...</div></strong></td -->
        </tr>
      </table>    </td>
    <td align="right" width="20%"   background="images/<%= pso.getSchemaOrgBanner() %>" > 

	  <div align="center">
	    <table border="0" cellspacing="1" cellpadding="0">

        <tr>
          <td><div align="center"><table bgcolor="#475DB0" cellspacing="0" cellpadding="2" border="1"><tr><td align="center"><a href="../simulation_user_admin/my_profile.jsp" target="mainFrame" class="menu_item style1"><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "my_profile") %></a></td>
          </tr>
                <tr>
                  <td align="center"><a href="../logout.jsp" target="_top" class="menu_item style1"><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "logout") %></a></td>
                </tr>
          </table>
          </div></td>
        </tr>
      </table>	  
	  </div>    </td>
  </tr>
</table>
</td>
	<td align="right">
    </td>
  </tr>
</table>
<table cellpadding="4" cellspacing="0" border="1">
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
    	<td bgcolor="#<%= ssg.getTabColor() %>"> <a href="simwebui.jsp?tabposition=<%= ii %>&phase=<%= pso.phase_id %>" target="_parent">
	      <div align="center" class="<%= divId %>"><%= ssg.getTabHeading() %></div></a></td>
		<%
			++ii;
			} // End of loop over sections
		%>
  	</tr>
	</table>

</body>
<HEAD>
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
</HEAD>
</html>