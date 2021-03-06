<%@ page 
	contentType="text/html; charset=utf-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.communications.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" 
%>
<%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	// Get the id for this conversation
	String cs_id = (String) request.getParameter("cs_id");
	
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	SimSectionRSDepOjbectAssignment ssrsdoa = new SimSectionRSDepOjbectAssignment();
	
	Vector this_set_of_actors = new Vector();
	
	Conversation conv = new Conversation();
	
	String conversation_type = "normal";
	
	if (!(pso.preview_mode)){
		ssrsdoa = SimSectionRSDepOjbectAssignment.getOneForRunningSimSection
			(pso.schema, pso.getRunningSimId(), new Long(cs_id), 0);
		
		conv = Conversation.getById(pso.schema, ssrsdoa.getObjectId());
		if (conv.getConversationType() == Conversation.TYPE_STUDENT_CHAT) {
			conversation_type = "student";	
		}

		this_set_of_actors = pso.getActorsForConversation(ssrsdoa.getObjectId(), request);
	}
	
	
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

#messagewindow {
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
<!--  ----------------------------------------------------------------------------     -->

<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>

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
</script>
 
<script type="text/javascript">
function changeActorColor(dropdownlist){

	var myindex  = dropdownlist.selectedIndex;
    
	var passedvalue = dropdownlist.options[myindex].value;
	
	var array1 = passedvalue.split("_");
	
	actor_colors[array1[0]] = array1[1];
	
	$.post("color_changer_server.jsp", { actor_id: array1[0], chatlinecolor: array1[1] });
	
	//$("#loading").remove();
	$("#messagewindow").empty();
	
	// Set the restart to 0 to cause reload
	start_index = 0;
	
	return true;

}
</script>

<script type="text/javascript">
function formatString(mTime, message, actorName, msgSender){
	
	var formattedHTML =  ('<table width=100% bgcolor=#' + actor_colors[msgSender] + ' ><tr><td><span class=\"style1\">(' + mTime + ') </span>From ' + actorName + ': ' + message + ' </td></tr></table>' );
	
  	return formattedHTML;
	
}
</script>


<script type="text/javascript">
		$(document).ready(function(){
		
			timestamp = 0;
			
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
							start_index: start_index,
							action: "postmsg",
							conversation_type: "<%= conversation_type %>",
							time: timestamp
						}, function(xml) {
					$("#msg<%= conv.getId() %>").empty();
					addMessages(xml);
					$("#msg<%= conv.getId() %>").val("");
				});
				return false;
			});
			
		}); // End of loop over if ready
		
</script>
<script type="text/javascript">
		function addMessages(xml) {
			
			xml = parseXml(xml);
			
			var coloredMsg = "";
			
			if($("status",xml).text() == "<%= ChatController.NO_NEW_MSG %>") return;
			
			$("message",xml).each(function(id) {
				message = $("message",xml).get(id);
				coloredMsg = formatString($("time",message).text(), $("text",message).text(), $("author",message).text(), $("actor_id",message).text());			
				$("#messagewindow").prepend(coloredMsg);
											
				var new_start_index = $("id",message).text();
				
				if (parseInt(new_start_index) > parseInt(start_index)){
					start_index = new_start_index;
				}
				
			});
			
			
		}

</script>
<script type="text/javascript">
function parseXml(xml)
{	
	if (jQuery.browser.msie)
	{

		//var xmlDoc = new ActiveXObject("Microsoft.XMLDOM"); 
		
		//xmlDoc.loadXML(xml);
		
		//xml = xmlDoc;
		
	}
	
	return xml;
}
</script>
<script type="text/javascript">
		function updateMsg() {

			$.get("one_on_one_chat_server.jsp",
				{ 
				start_index: start_index, 
				conversation: $("#conversation<%= conv.getId() %>").val(), 
				time: timestamp,
				dumbie: Math.random()
				}, 
				function(xml) {
				$("#loading").remove();
				addMessages(xml);
				}
				);

			setTimeout('updateMsg()', 1000);
		
		}
</script>
<script type="text/javascript">
		
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
</script>
<!-- 		
//onKeyUp="checkEnter();"
//function checkEnter(){
//	alert("me");
//}
-->

<script type="text/javascript">
    $(function()
    {
       var  testTextBox = $('#msg<%= conv.getId() %>');
        var code =null;
        testTextBox.keypress(function(e)
        {
            code= (e.keyCode ? e.keyCode : e.which);
            if (code == 13){
				$('#chatform<%= conv.getId() %>').submit();
			}
            //e.preventDefault();
        });

    });

</script> 
<!-------------------------------------------------------------------------------->
</head>
<body onLoad="updateMsg();">
<table width="50%">
<TR>
    <TD valign="top" width="25%"><h1><%= cs.getPageTitle() %></h1><a href="conference_room_archive.jsp?conv_id=<%= conv.getId() %>&cs_id=<%= cs_id %>">Conversation Archive</a>
      <p><%= cs.getBigString() %></p></TD>
<TR>
    <TD width="25%"><form id="chatform<%= conv.getId() %>" >
	<table>
    <tr><td valign="top">Your Text:</td>
  <td valign="top"> <textarea name="chatmsgbox" cols="32" rows="4" id="msg<%= conv.getId() %>"   ></textarea></td></tr></table>
	      <input type="hidden" id="author<%= conv.getId() %>" value="You" />
    		<input type="hidden" id="conversation<%= conv.getId() %>" value="<%= conv.getId() %>" />
          <BR>
</form></TD>
  </TR>
</table>
<p>Actors in this conversation:</p>
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
<div id="messagewindow"><span id="loading">Loading...</span></div>
<p><a href="conference_room_archive.jsp?conv_id=<%= conv.getId() %>&cs_id=<%= cs_id %>">Conversation Archive</a></p>
</body>
</html>