<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.communications.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" 
%>
<%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	// Get the id for this conversation
	String cs_id = (String) request.getParameter("cs_id");
	
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	SimSectionRSDepOjbectAssignment ssrsdoa = new SimSectionRSDepOjbectAssignment();
	
	Vector this_set_of_actors = new Vector();
	
	String ssrsdoa_id = "0";
	
	Conversation conv = new Conversation();
	
	if (!(pso.preview_mode)){
		ssrsdoa = SimSectionRSDepOjbectAssignment.getOneForRunningSimSection
			(pso.schema, pso.getRunningSimId(), new Long(cs_id), 0);
			
		ssrsdoa_id = ssrsdoa.getObjectId().toString();
		
		conv = Conversation.getById(pso.schema, ssrsdoa.getObjectId());

		this_set_of_actors = pso.getActorsForConversation(ssrsdoa.getObjectId(), request);
	}
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.4.1.js"></script>
<script type="text/javascript">
	<%
		// Keep a set of actors to loop over check on if online.
		Hashtable setOfActors = new Hashtable();
	
		// The print out statement below fills in the hashtable and lists important details regarding the actors.
		
if (conv.getId() != null) {   %>
<%= pso.generateConferenceRoomChatLines(request, setOfActors, conv.getId()) %>
<% } // end of if not null %>

</script>
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

 

function changeActorColor(dropdownlist){

	var myindex  = dropdownlist.selectedIndex;
    
	var passedvalue = dropdownlist.options[myindex].value;
	
	array1 = passedvalue.split("_");
	
	actor_colors[array1[0]] = array1[1];
	
	$.post("color_changer_server.jsp", { actor_id: array1[0], chatlinecolor: array1[1] });
	
	//$("#loading").remove();
	$("#messagewindow<%= conv.getId() %>").replaceWith( "Loading ..." );
	
	// Set the restart to 0 to cause reload
	start_index<%= conv.getId() %> = 0;
	
	return true;

}

function formatString(mTime, message, actorName, msgSender){
	
	var formattedHTML = "";
	
	formattedHTML += ("<table width=100% bgcolor=#" + actor_colors[msgSender] + " ><tr><td><span class=\"style1\">(" + mTime + ") </span>From " + actorName + ": " + message + " </td></tr></table>" );
	
  	return formattedHTML;
	
}

</script>


<script type="text/javascript">
		$(document).ready(function(){
			timestamp = 0;
			fromActor = -1;
			
		<%
			for (Enumeration e = setOfActors.keys(); e.hasMoreElements();){
				String key = (String) e.nextElement();
		%>
			checkOnlineActor<%= key %>();
		<%
			} // End of loop over unique actor ids to add 'user online' function
		%>
			
			<!-- Now we set up the functions for this conversation -->
			
			$("form#chatform<%= conv.getId() %>").submit(function(){
				$.post("one_on_one_chat_inserter.jsp",{
							message: $("#msg<%= conv.getId() %>").val(),
							name: $("#author<%= conv.getId() %>").val(),
							conversation: $("#conversation<%= conv.getId() %>").val(),
							start_index: start_index<%= conv.getId() %>,
							action: "postmsg",
							time: timestamp
						}, function(xml) {
					$("#msg<%= conv.getId() %>").empty();
					addMessages<%= conv.getId() %>(xml);
					$("#msg<%= conv.getId() %>").val("");
				});
				return false;
			});
			
		}); // End of loop over if ready
		
			
		function addMessages<%= conv.getId() %>(xml) {
		
			var coloredMsg = "";
			
			if($("status",xml).text() == "<%= ChatController.NO_NEW_MSG %>") return;
			
			$("message",xml).each(function(id) {
				message = $("message",xml).get(id);
				coloredMsg = formatString($("time",message).text(), $("text",message).text(), $("author",message).text(), $("actor_id",message).text());		
				$("#messagewindow<%= conv.getId() %>").prepend(coloredMsg);
											
				new_start_index<%= conv.getId() %> = $("id",message).text();
				
				if (parseInt(new_start_index<%= conv.getId() %>) > parseInt(start_index<%= conv.getId() %>)){
					start_index<%= conv.getId() %> = new_start_index<%= conv.getId() %>;
				}
				
			});
			
			
		}
		
		function updateMsg() {
			$.post("one_on_one_chat_server.jsp",{ 
				start_index: start_index<%= conv.getId() %>, 
				conversation: $("#conversation<%= conv.getId() %>").val(), 
				time: timestamp }, function(xml) {
				$("#loading").remove();
				addMessages<%= conv.getId() %>(xml);
			});
			//alert("4 secs");
			setTimeout('updateMsg()', 2000);
		
		}
		
		<%
			for (Enumeration e = setOfActors.keys(); e.hasMoreElements();){
				String key = (String) e.nextElement();
		%>
				function checkOnlineActor<%= key %> (){
				
					$.post("actor_online_checker.jsp", { 
						checking_actor: "<%= pso.getActorId() %>", 
						checked_actor: "<%= key %>"
						}, 
						function(xml){
							if ($("status_code",xml).text() == "offline") {
								$("#actorpresent<%= key %>").text("offline");
							} else if ($("status_code",xml).text() == "online") {
								$("#actorpresent<%= key %>").text("online");
    							//alert("Data Loaded for : actorpresent<%= key %>" + xml);
							}
							
							;
  						}
						
						);
					
					
					setTimeout('checkOnlineActor<%= key %>()', 5000);
				
				}
		<%
			} // End of loop over unique actor ids to add 'user online' function
		%>

<!-- 		
//onKeyUp="checkEnter();"
//function checkEnter(){
//	alert("me");
//}
-->


</script>
<style type="text/css" media="screen">
body {
margin:2;
padding:0;
height:100%;
width:100%;
}

#messagewindow<%= conv.getId() %> {
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

.style1 {font-size: small; color:#000000}
</style>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="updateMsg();">
<table width="50%">
<TR>
    <TD valign="top" width="25%"><h1><%= cs.getPageTitle() %></h1>
      <p><%= cs.getBigString() %></p></TD>
<TR>
    <TD width="25%"><form id="chatform<%= conv.getId() %>" >
  <p>Text to send: 
		  	<textarea name="chatmsgbox" cols="40" rows="2" id="msg<%= conv.getId() %>"  ></textarea>
			<input type="hidden" id="author<%= conv.getId() %>" value="You" />
    		<input type="hidden" id="conversation<%= conv.getId() %>" value="<%= conv.getId() %>" />
          <BR>
	<input type="submit" value="Send">
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
        </select> (<I><span id="actorpresent<%= act.getId().toString() %>">Checking status ...</span></I>)</form></LI><% } %>
	</UL>
	</P>
<div id="messagewindow<%= conv.getId() %>"><span id="loading">Loading...</span></div>
</body>
</html>