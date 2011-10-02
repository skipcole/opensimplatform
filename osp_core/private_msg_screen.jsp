<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" 
	errorPage="/error.jsp" 
%>
<%
	// The conversation is pulled out of the context
	Hashtable conversation_actors  = (Hashtable) getServletContext().getAttribute("conversation_actors");

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	Vector this_set_of_actors = (Vector) conversation_actors.get(pso.simulation.id + "_" + pso.actor.id);
	
	if (this_set_of_actors == null) {
		this_set_of_actors = new Vector();
		
		try {
			
			String selectInfo = "select a.actor_id, a.actor_name from actors a, user_actors ua where " +
				"a.actor_id = ua.actor_id and ua.sim_id = " + pso.simulation.id;
			
			Connection connection = MysqlDatabase.getConnection();		
			Statement stmt = connection.createStatement();
			ResultSet rst = stmt.executeQuery(selectInfo);
			
			while (rst.next()){
				Vector recordV = new Vector();
				recordV.add(rst.getString(1));
				recordV.add(rst.getString(2));
				
				this_set_of_actors.add(recordV);
			}
			
			connection.close();
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		conversation_actors.put(this_set_of_actors, pso.simulation.id + "_" + pso.actor.id);
	}
%>
<html>
<head>
<script type="text/javascript">

	var start_index = 0
	
	var actor_names = new Array()
	
	<% 
		for (Enumeration e = this_set_of_actors.elements(); e.hasMoreElements();){
			Vector v = (Vector) e.nextElement();
			String this_a_id = (String) v.get(0);
			String this_a_name = (String) v.get(1);
	%>
		actor_names["<%= this_a_id %>"] = "<%= this_a_name %>"
	<%	
		}
	%>
	
	var actor_colors = new Array();


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
		
		formattedHTML += ("<font color=black>From " + actor_names[msgSender] + "</font>: <font color=" + actor_colors[msgSender] + ">" + msgPayload + "</font> <BR>" );
		
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
    xmlHttp.open("GET","../../pmpg_core/broadcast_chat_server.jsp?conversation_id=" + <%= pso.conversationid %> + "&actor_id=" + <%= pso.actor.id %> + "&start_index=" + start_index,true);
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

	var dataToSend = "conversation_id=" + <%= pso.conversationid %> + "&actor_id=" + <%= pso.actor.id %> + "&newtext=" + send_text;
	
	xmlHttp.open("POST","../../pmpg_core/broadcast_chat_server.jsp",true);
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
color:#333;
background:#eaeaea;
border:1px solid #333;
overflow:scroll;
}
</style>
</head>
<body onLoad="timedCount();"> 
<table width="50%">
<TR>
    <TD valign="top" width="25%"><h1>Private Messages</h1>
      <p>Messages sent on this page will go only to the player you select.</p>
      <p>&nbsp;</p></TD>
<TR>
    <TD width="25%"><form name="form1" method="post" action="">
        <p>Text to send: 
          <input name="chattexttosend" type="text" id="chattexttosend" size="40" maxlength="255" />
        </p>
        <p>Actor to send it to:<br>
          <BR>
          <input type="submit" name="Submit" value="Submit" onClick="sendText();return false;">
        </p>
</form></TD>
  </TR>
</table>
Actors visible to you:
<P>
	<UL>
	<% 
		for (Enumeration e = this_set_of_actors.elements(); e.hasMoreElements();){
			Vector v = (Vector) e.nextElement();
			String this_a_id = (String) v.get(0);
			String this_a_name = (String) v.get(1);
	%>
		<LI><form><%= this_a_name %> <select name="select<%= this_a_id %>" onChange="changeActorColor(this.form.select<%= this_a_id %>);">
      <option value="<%= this_a_id %>_#0033CC"><font color="#0033CC">Blue</font></option>
	  <option value="<%= this_a_id %>_#009933"><font color="#009933">Green</font></option>
	  <option value="<%= this_a_id %>_#990000"><font color="#990000">Red</font></option>
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