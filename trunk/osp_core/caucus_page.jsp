<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
		org.usip.osp.communications.*,
		org.usip.osp.persistence.*,
		org.usip.osp.baseobjects.*" 
	errorPage="" 
%>
<%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
		
	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, new Long(cs_id));
	
	Long sim_conv_id = (Long) cs.getContents().get("sim_conv_id");
	
	Conversation conv = Conversation.getById(pso.schema, sim_conv_id)
	
	Vector this_set_of_actors = ChatController.getActorsForConversation(pso, sim_conv_id.toString(), request);
	
%>
<html>
<head>
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.4.1.js"></script>
<script type="text/javascript">

	var start_index = 0
	
	var actor_names = new Array();
	var actor_colors = new Array();
	
	// Here we loop over the actors to set the color of their chat.
	<% 
		for (Enumeration e = this_set_of_actors.elements(); e.hasMoreElements();){
			ActorGhost act = (ActorGhost) e.nextElement();
			String this_a_id = act.getId().toString();
			String this_a_name = act.getActorName();
	%>
		actor_names["<%= this_a_id %>"] = "<%= this_a_name %>"
		actor_colors["<%= this_a_id %>"] = "ffffff"
	<%	
		}
	%>

  
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

//document.getElementById('foo').innerHTML = chat_text;
//var objDiv = document.getElementById("foo");
//objDiv.scrollTop = objDiv.scrollHeight;
//xmlHttp.open("GET","broadcast_chat_server.jsp?conv_id=<%= sim_conv_id %>&actor_id=" + <%= pso.getActorId() %> + "&start_index=" + start_index,true)
  

</script>
<script type="text/javascript">

function sendText(){
	send_text = encodeURI(document.getElementById('chattexttosend').value);
	
	document.getElementById('chattexttosend').value = "";
	var dataToSend = "conv_id=<%= sim_conv_id %>&actor_id=" + <%= pso.getActorId() %> + "&user_id=" + <%= pso.user_id %> + "&newtext=" + send_text;	
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
    <TD valign="top" width="25%"><%= cs.getBigString() %>
	  xxx</TD>
<TR>
    <TD width="25%"><form name="form1" method="post" action="">
  <p>Text to send: 
          <input name="chattexttosend" type="text" id="chattexttosend" size="20" maxlength="255" />
          <BR>
	<input type="submit" name="Submit" value="Submit" onClick="sendText();return false;">
  </p>
</form></TD>
  </TR>
</table>
<P>
	<UL><% 
		for (Enumeration e = this_set_of_actors.elements(); e.hasMoreElements();){
			ActorGhost act = (ActorGhost) e.nextElement();
			String this_a_id = act.getId().toString();
			String this_a_name = act.getActorName(pso.schema, pso.getRunningSimId(), request);
	%><LI class="actor_class"><form><%= this_a_name %> <select name="select<%= this_a_id %>" onChange="changeActorColor(this.form.select<%= this_a_id %>);">
      <option value="<%= this_a_id %>_ffffff">White</option>
	  <option value="<%= this_a_id %>_ffdddd">Red</option>
	  <option value="<%= this_a_id %>_ddffdd">Green</option>
	  <option value="<%= this_a_id %>_ddddff">Blue</option>
      <option value="<%= this_a_id %>_ffff66">Yellow</option>
    </select> (<I><span id="actorpresent<%= act.getActor_id().toString() %>">Checking status ...</span></I>)</form></LI><% } %>
	</UL>
	</P>
<div id="foo" >Chat Text</div>
</body>
</html>