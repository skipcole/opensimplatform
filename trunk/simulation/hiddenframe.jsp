<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" 
	errorPage="" %>
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
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>USIP OSCW Hidden Page</title>
<script type="text/javascript">

var sendThemRedirect;

function getSimEvent()
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

		var sim_event_text = xmlHttp.responseText;
		
		if (sim_event_text == "phase_change"){
			alert("Simulation Phase has changed. You may now have different capabilities. There may also be new news.");
			 top.document.location="simwebui.jsp?tabposition=1";
			 sendThemRedirect = "true";
			 return;
		} else if (sim_event_text == "news"){
			alert("There is new news. Please check the news page as soon as possible.");
		} else if (sim_event_text == "announcement"){
			alert("There is a new announcement. Please check the announcements page as soon as possible.");
		}
		
        }
      }
    xmlHttp.open("GET","sim_event_server.jsp?running_sim_id=<%= pso.running_sim_id %>",true);
    xmlHttp.send(null);
  }
  
function timedCount()
{
	getSimEvent()
	setTimeout("timedCount()",1000)
}


</script>

</head>

<body  onLoad="timedCount();">
</body>
</html>
<%
	//
%>