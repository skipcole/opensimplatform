<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		//response.sendRedirect("index.jsp");
		return;
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script type="text/javascript" src="../third_party_libraries/sticky_full/sticky.full.js"></script>
<link rel="stylesheet" href="../third_party_libraries/sticky_full/sticky.full.css" type="text/css" />

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<title>USIP OSP Hidden Page</title>
<script type="text/javascript">

function getSimEvent()
  {

  	$.get('sim_event_server.jsp', {'running_sim_id': "<%= pso.getRunningSimId() %>"}, 
	
		function(myFunction){
		
			var sim_event_type = $("sim_event_type",myFunction).text();
			var sim_event_text = $("sim_event_text",myFunction).text();
			
			if (sim_event_type == "phase_change"){
				alert(sim_event_text);
			 	top.document.location="simwebui.jsp?tabposition=1";
			} else if (sim_event_type == "email"){
				top.topFrame.stickyAlert(sim_event_text);
			} else if (sim_event_type == "event"){
				alert(sim_event_text);
			} else if (sim_event_type == "news"){
				alert(sim_event_text);
			} else if (sim_event_type == "announcement"){
				alert(sim_event_text);
			}  else if (sim_event_type == "memo"){
				alert(sim_event_text);
			} else if (sim_event_type == "multiple"){
				alert(sim_event_text);
			}
			
		
		}, 'xml');

	
  }  // End of getSimEvent method ;

function sendHeartbeat()
{	
	  	$.get('heartbeat_acceptor.jsp')
}


function timedCount()
{
	getSimEvent()
	sendHeartbeat()
	setTimeout("timedCount()", 7000)
}


</script>

<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
</head>
<body onLoad="timedCount();">
</body>
</html>
