<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), false);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="../jquery-1.2.6.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>USIP OSP Hidden Page</title>
<script type="text/javascript"  >

var sendThemRedirect;
var callnum = 0;


function getSimEvent()
  {
  
  	$.get('sim_event_server.jsp', {'running_sim_id': "<%= pso.running_sim_id %>"}, 
	
		function(myFunction){
		
			var sim_event_text = $("sim_event_text",myFunction).text();
			//sim_event_text = sim_event_text.trim()
			
			if (sim_event_text == "phase_change"){
				alert("Simulation Phase has changed. You may now have different capabilities. There may also be new news.");
			 	top.document.location="simwebui.jsp?tabposition=1";
			 	sendThemRedirect = "true";
			 	return;
			} 
			else if (sim_event_text == "news"){
				alert("There is new news. Please check the news page as soon as possible.");
			} else if (sim_event_text == "announcement"){
				alert("There is a new announcement. Please check the announcements page as soon as possible.");
			} 
		
			//alert("|" + sim_event_text + "|");
		}, 'xml');
	
	
  }  // End of getSimEvent method ;



function timedCount()
{
	callnum += 1;
	getSimEvent();
	setTimeout("timedCount()",4000);
}


</script>
</head>
<body  onLoad="timedCount();">
</body>
</html>
<%
	//
%>
