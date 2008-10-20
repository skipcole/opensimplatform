<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,
	org.usip.oscw.communications.*,org.usip.oscw.baseobjects.*" 
	errorPage="" 
%>
<%

	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	// Get the id for this conversation
	String conv_id = "";
	Vector this_set_of_actors = new Vector();
	
	if (false) {
		request.getParameter("conversation_id");
		this_set_of_actors = ChatController.getActorsForConversation(pso, conv_id, request);
	}
	
	
%>
<html>
<head>
<script type="text/javascript">

	var start_index = 0
	
	var actor_names = new Array();
	var actor_colors = new Array();
	
	<% 
		for (Enumeration e = this_set_of_actors.elements(); e.hasMoreElements();){
			ActorGhost act = (ActorGhost) e.nextElement();
			String this_a_id = act.getId().toString();
			String this_a_name = act.getName();
	%>
		actor_names["<%= this_a_id %>"] = "<%= this_a_name %>"
		actor_colors["<%= this_a_id %>"] = "ffffff"
	<%	
		}
	%>



function formatString(rawData){
	
	var lineDelimiter = "|||||";
	var innerDelimiter = "_xyxyx_";
	
	var formattedHTML = "";
	
	array0 = rawData.split(lineDelimiter);

	for (count = 0; count < array0.length - 1; count++){
		var this_msg = array0[count];
		
		array1 = this_msg.split(innerDelimiter);
		
		var msgMetaData = array1[0];
		
		array2 = msgMetaData.split("_")
		var msgIndex = array2[0];
		
		// Make it a number
		msgIndex = msgIndex * 1
		
		//////////////////////////////////
		if (msgIndex > start_index) {
			start_index = msgIndex
		}
		
		var msgSender = array2[1];
		
		var msgPayload = array1[1];
		
		//formattedHTML += ("<div class=actor_" + actor_colors[msgSender] + ">From " + actor_names[msgSender] + ": " + msgPayload + " </div>" );
		formattedHTML += ("<table width=100% bgcolor=#" + actor_colors[msgSender] + " ><tr><td>From " + actor_names[msgSender] + ": " + msgPayload + " </td></tr></table>" );
		
	}
		
  	return ( formattedHTML );
}
  
var chat_text = ""

function changeActorColor(dropdownlist){

	var myindex  = dropdownlist.selectedIndex;
    
	var passedvalue = dropdownlist.options[myindex].value;
	
	array1 = passedvalue.split("_");
	
	actor_colors[array1[0]] = array1[1];
	
	chat_text = "";
	// Set the restart to 0 to cause reload
	start_index = 0;
	
	return true;

}

function ajaxFunction()
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

		var returnedText = xmlHttp.responseText;
		
		if (returnedText.length > 10) {
		
			var formattedHTML = formatString(returnedText);
			
			chat_text = chat_text + formattedHTML;
			document.getElementById('foo').innerHTML = chat_text;
			var objDiv = document.getElementById("foo");
			objDiv.scrollTop = objDiv.scrollHeight;
		
		}
		
        }
      }
    xmlHttp.open("GET","broadcast_chat_server.jsp?conv_id=<%= conv_id %>&actor_id=" + <%= pso.actor_id %> + "&start_index=" + start_index,true);
    xmlHttp.send(null);
  }
  

</script>
<script type="text/javascript">
function timedCount()
{
	ajaxFunction()
	setTimeout("timedCount()",1000)
}

function sendText(){
	
  	var send_text
	
	send_text = encodeURI(document.getElementById('chattexttosend').value);
	
	document.getElementById('chattexttosend').value = "";
	
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

	var dataToSend = "conv_id=<%= conv_id %>&actor_id=" + <%= pso.actor_id %> + "&user_id=" + <%= pso.user_id %> + "&newtext=" + send_text;
	
	xmlHttp.open("POST","broadcast_chat_server.jsp",true);
	xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xmlHttp.send(dataToSend);

  }
</script>
<style type="text/css" media="screen">
body {
margin:2;
padding:0;
height:100%;
width:100%;
} 
#foo {
position:absolute;
right:0;
top:0;
padding:0;
width:50%;
height:100%; /* works only if parent container is assigned a height value */
color:#FFFFFF;
background:#FFFFFF;
border:1px solid #333;
overflow:scroll;
}
.actor_ffffff {
	background-color: #FFFFFF;
	background-position: left top;
}
.actor_ddffff {
	background-color: #DDFFFF;
	background-position: left top;
}
.actor_ffddff {
	background-color: #FFDDFF;
	background-position: left top;
}
.actor_ffffdd {
	background-color: #FFFFDD;
	background-position: left top;
}
</style>
</head>
<body onLoad="timedCount();"> 
<table width="50%">
<TR>
    <TD valign="top" width="25%"><h1>Eavesdrop</h1>
      <p>Select the conversation below you want to see. </p>
      <p>&nbsp;</p></TD>
<TR>
    <TD width="25%">
	<form name="form1" method="post" action="">
	Conversation: <select name="select">
	
	<%
		for (ListIterator li = Conversation.getAllForSim(pso.schema, pso.sim_id).listIterator(); li.hasNext();) {
			Conversation conv = (Conversation) li.next();
			%>
	  <option value="<%= conv.getConversation_name() %>"><%= conv.getConversation_name() %></option>
		<%	}  %>
		</select>
    </form>
    </TD>
  </TR>
</table>
Actors present in conversation:
<P>
	<UL>
	<% 
		for (Enumeration e = this_set_of_actors.elements(); e.hasMoreElements();){
			ActorGhost act = (ActorGhost) e.nextElement();
			String this_a_id = act.getId().toString();
			String this_a_name = act.getName();
	%>
		<LI><form><%= this_a_name %> <select name="select<%= this_a_id %>" onChange="changeActorColor(this.form.select<%= this_a_id %>);">
      <option value="<%= this_a_id %>_ffffff">White</option>
	  <option value="<%= this_a_id %>_ffdddd">Red</option>
	  <option value="<%= this_a_id %>_ddffdd">Green</option>
	  <option value="<%= this_a_id %>_ddddff">Blue</option>
	  
    </select> </form></LI>
	<%	
		}
	%>
	</UL>
	</P>
<div id="foo" >Chat Text</div>
</body>
</html>
<%
	
%>