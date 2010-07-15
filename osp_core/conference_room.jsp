<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.communications.*,org.usip.osp.baseobjects.*" 
	errorPage="" 
%>
<%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	// Get the id for this conversation
	String cs_id = (String) request.getParameter("cs_id");
	
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	SimSectionRSDepOjbectAssignment ssrsdoa = new SimSectionRSDepOjbectAssignment();
	
	Vector this_set_of_actors = new Vector();
	
	String ssrsdoa_id = "0";
	
	if (!(pso.preview_mode)){
		ssrsdoa = SimSectionRSDepOjbectAssignment.getOneForRunningSimSection
			(pso.schema, pso.running_sim_id, new Long(cs_id), 0);
			
		ssrsdoa_id = ssrsdoa.getObjectId().toString();
	
		this_set_of_actors = pso.getActorsForConversation(ssrsdoa.getObjectId(), request);
	}
	
	
%>
<html>
<head>
<script type="text/javascript" src="../jquery-1.2.6.js"></script>
<script type="text/javascript">

	var start_index = 0
	
	var actor_names = new Array();
	var actor_colors = new Array();
	
	<% 
		for (Enumeration e = this_set_of_actors.elements(); e.hasMoreElements();){
			ActorGhost act = (ActorGhost) e.nextElement();
			String this_a_id = act.getId().toString();
			String this_a_name = act.getActorName();
	%>
		actor_names["<%= this_a_id %>"] = "<%= this_a_name %>"
		actor_colors["<%= this_a_id %>"] = "<%= act.getDefaultColorChatBubble() %>"
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
	
	$.post("color_changer_server.jsp", { actor_id: array1[0], chatlinecolor: array1[1] });
	
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
			
			chat_text =  formattedHTML + chat_text;
			document.getElementById('foo').innerHTML = chat_text;
			var objDiv = document.getElementById("foo");
			//objDiv.scrollTop = objDiv.scrollHeight;
		
		}
		
        }
      }
    xmlHttp.open("GET","broadcast_chat_server.jsp?conv_id=<%= ssrsdoa_id %>&actor_id=" + <%= pso.actor_id %> + "&start_index=" + start_index,true);
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

	var dataToSend = "conv_id=<%= ssrsdoa_id %>&actor_id=" + <%= pso.actor_id %> + "&user_id=" + <%= pso.user_id %> + "&newtext=" + send_text;
	
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
overflow:auto;

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
.actor_class {
	
}
</style>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="timedCount();"> 
<table width="50%">
<TR>
    <TD valign="top" width="25%"><h1><%= cs.getPageTitle() %></h1>
      <p><%= cs.getBigString() %></p></TD>
<TR>
    <TD width="25%"><form name="form1" method="post" action="">
  <p>Text to send: 
          <textarea name="chattexttosend" cols="40" rows="2" id="chattexttosend" type="text" ></textarea>
          <BR>
	<input type="submit" name="Submit" value="Submit" onClick="sendText();return false;">
  </p>
</form></TD>
  </TR>
</table>
Actors in this conversation:
<P>
	<UL><% 
		for (Enumeration e = this_set_of_actors.elements(); e.hasMoreElements();){
			ActorGhost act = (ActorGhost) e.nextElement();
			String this_a_id = act.getId().toString();
			String this_a_name = act.getActorName();
	%><LI class="actor_class"><form><%= this_a_name %> <select name="select<%= this_a_id %>" onChange="changeActorColor(this.form.select<%= this_a_id %>);">
      <option value="<%= this_a_id %>_ffffff" <%= USIP_OSP_Util.matchSelected("ffffff", act.getDefaultColorChatBubble(), " selected ") %>>White</option>
	  <option value="<%= this_a_id %>_ffdddd" <%= USIP_OSP_Util.matchSelected("ffdddd", act.getDefaultColorChatBubble(), " selected ") %>>Red</option>
	  <option value="<%= this_a_id %>_ddffdd" <%= USIP_OSP_Util.matchSelected("ddffdd", act.getDefaultColorChatBubble(), " selected ") %>>Green</option>
	  <option value="<%= this_a_id %>_ddddff" <%= USIP_OSP_Util.matchSelected("ddddff", act.getDefaultColorChatBubble(), " selected ") %>>Blue</option>
	  <option value="<%= this_a_id %>_ffff66" <%= USIP_OSP_Util.matchSelected("ffff66", act.getDefaultColorChatBubble(), " selected ") %>>Yellow</option>
        </select> </form></LI><% } %>
	</UL>
	</P>
<div id="foo" >Chat Text</div>
</body>
</html>
<%
	
%>
