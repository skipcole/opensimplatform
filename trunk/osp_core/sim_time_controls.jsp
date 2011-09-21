<%@ page 
	contentType="text/html; charset=utf-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" 
%>
<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	response.setHeader("Cache-Control", "no-cache");
	
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

			$.get("sim_time_server.jsp",
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

<!-------------------------------------------------------------------------------->
</head>
<body onLoad="updateMsg();">
<p>Time Currently Displayed:<div id="messagewindow"><span id="loading">Loading...</span></div>
</p>

<h2>&nbsp;</h2>
<h2>Control Actions</h2>
<ul>
  <li>Pause Clock</li>
  <li>Reset Clock</li>
  <li><a href="#constant_string">Set to Constant Message </a></li>
  <li>Start Clock</li>
</ul>
<p>&nbsp;</p>
<h2>Set to Constant Message<a name="constant_string"></a></h2>
<form name="form1" method="post" action="sim_time_set_time.jsp">
  <p>
    <label for="textfield"></label>
    <input type="text" name="newtime" id="textfield" value="<%= pso.gameTime %>" />
  </p>
  <p>
    <input type="submit" name="button" id="button" value="Submit">
  </p>
</form>
</body>
</html>