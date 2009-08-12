<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
		org.usip.osp.communications.*,
		org.usip.osp.persistence.*,
		org.usip.osp.baseobjects.*" 
	errorPage="" 
%>
<%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
		String bodyText = "";
	
	String cs_id = (String) request.getParameter("cs_id");
	
	MultiSchemaHibernateUtil.beginTransaction(pso.schema);

	CustomizeableSection cs = (CustomizeableSection) MultiSchemaHibernateUtil.getSession(pso.schema).get(CustomizeableSection.class, new Long(cs_id));
    
	bodyText = (String) cs.getBigString();
	
	Long sim_conv_id = (Long) cs.getContents().get("sim_conv_id");
	
	MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
	
	Vector this_set_of_actors = ChatController.getActorsForConversation(pso, sim_conv_id.toString(), request);
	
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
    xmlHttp.open("GET","broadcast_chat_server.jsp?conv_id=<%= sim_conv_id %>&actor_id=" + <%= pso.actor_id %> + "&start_index=" + start_index,true);
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

	var dataToSend = "conv_id=<%= sim_conv_id %>&actor_id=" + <%= pso.actor_id %> + "&user_id=" + <%= pso.user_id %> + "&newtext=" + send_text;
	
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
    <TD valign="top" width="25%"><%= bodyText %>
	  </TD>
<TR>
    <TD width="25%"><form name="form1" method="post" action="">
  <p>Text to send: 
          <input name="chattexttosend" type="text" id="chattexttosend" size="40" maxlength="255" />
          <BR>
	<input type="submit" name="Submit" value="Submit" onClick="sendText();return false;">
  </p>
</form></TD>
  </TR>
</table>
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