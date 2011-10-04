<%@ page 
	contentType="text/html; charset=utf-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	com.seachangesimulations.osp.gametime.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" 
%>
<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	response.setHeader("Cache-Control", "no-cache");

	boolean hasClock = false;
	
	GameClockPhaseInstructions gcpi = GameClockPhaseInstructions.getByPhaseAndSimId(pso, pso.phase_id, pso.sim_id);
		
	if (gcpi != null){
		hasClock = true;
	}
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">

<style type="text/css" media="screen">
body {
margin:2;
padding:0;
height:100%;
width:100%;
}

.style1 {font-size: small; color:#000000}
</style>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<!--  ----------------------------------------------------------------------------     -->

<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>

<% if (hasClock) { %>
<script type="text/javascript">
		function addMessages(xml) {
			
			if($("status",xml).text() == "0") return;
			
			$("message",xml).each(function(id) {
				text = $("text",xml).get(id);			
				$("#messagewindow").html(text);
			});
			
			
		}

</script>

<script type="text/javascript">
		function updateMsg() {

			$.get("../simulation/sim_phase_server.jsp",
				{ 
				dumbie: Math.random()
				}, 
				function(xml) {
				$("#loading").remove();
				addMessages(xml);
				}
				);

			setTimeout('updateMsg()', 5000);
		
		}
</script>
<% } // end of if has clock %>
<!-------------------------------------------------------------------------------->
</head>

<body 
<% if (hasClock) { %>
onLoad="updateMsg();"
<% } // end of if has clock %>
>

<% if (hasClock) { %>
<p>Time Currently Displayed:<div id="messagewindow"><span id="loading">Loading...</span></div>
</p>

<h2>&nbsp;</h2>
<p>Clock for this Phase: <%= gcpi.getTextSynopsis() %></p>
<h2>Control Actions</h2>

<% if (gcpi.getTimerType() == GameClockPhaseInstructions.GCPI_UNDEFINED) { %>
<p>Timer found, but it is undefined type.</p>
<% } else if (gcpi.getTimerType() == GameClockPhaseInstructions.GCPI_CONST) { %>
<h2>Set to Constant Message<a name="constant_string"></a></h2>
<form name="form1" method="post" action="sim_time_set_time.jsp">
  <p>
    <label for="textfield"></label>
    <input type="text" name="newtime" id="textfield" value="<%= pso.getGameTime(request) %>" />
  </p>
  <p>
    <input type="submit" name="button" id="button" value="Submit">
  </p>
</form>

<% } else if (gcpi.getTimerType() == GameClockPhaseInstructions.GCPI_UP_TIME) { %>
<p>Controls not yet defined.</p>
<% } else if (gcpi.getTimerType() == GameClockPhaseInstructions.GCPI_UP_RUNNING_TIME) { %>
<p>Controls not yet defined.</p>
<% } else if (gcpi.getTimerType() == GameClockPhaseInstructions.GCPI_UP_INTERVAL) { %>
<p>Controls not yet defined.</p>
<ul>
  <li>Pause Clock</li>
  <li>Reset Clock</li>
  <li>Start Clock</li>
</ul>
<% } else if (gcpi.getTimerType() == GameClockPhaseInstructions.GCPI_DOWN_TIME) { %>
<p>Controls not yet defined.</p>
<% } else if (gcpi.getTimerType() == GameClockPhaseInstructions.GCPI_DOWN_RUNNING_TIME) { %>
<p>Controls not yet defined.</p>
<% } else if (gcpi.getTimerType() == GameClockPhaseInstructions.GCPI_DOWN_INTERVAL) { %>
<p>Controls not yet defined.</p>
<% } else { %>
<p>Timer found, its type is set out of bounds.</p>
<% } %>
	
	

<p>&nbsp;</p>

<% } else { %>
<p>No Clock for this Phase. </p>
<% } %>
</body>
</html>