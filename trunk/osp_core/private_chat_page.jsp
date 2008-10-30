<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.communications.*,org.usip.oscw.baseobjects.*" 
	errorPage="" 
%>
<%

	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
%>
<html>
<head>
<script type="text/javascript" src="../jquery-1.2.6.js"></script>
<script type="text/javascript">

	<%  // Loop over the conversations for this Actor
	for (ListIterator<Conversation> li = Conversation.getActorsPrivateChats(pso.schema, pso.sim_id, pso.actor_id).listIterator(); li.hasNext();) {
			Conversation conv = (Conversation) li.next(); %>
			
			var start_index<%= conv.getId() %> = 0;
			var new_start_index<%= conv.getId() %> = 0;
	<% } // End of loop over conversations. %>

</script>
	<script type="text/javascript">
		$(document).ready(function(){
			timestamp = 0;
			
		<%  // Loop over the conversations for this Actor
		for (ListIterator<Conversation> li = Conversation.getActorsPrivateChats(pso.schema, pso.sim_id, pso.actor_id).listIterator(); li.hasNext();) {
			Conversation conv = (Conversation) li.next(); %>
		
			updateMsg<%= conv.getId() %>();
			$("form#chatform<%= conv.getId() %>").submit(function(){
				$.post("one_on_one_chat_server.jsp",{
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
			<% } %>
		});
		
		<%  // Loop over the conversations for this Actor
		for (ListIterator<Conversation> li = Conversation.getActorsPrivateChats(pso.schema, pso.sim_id, pso.actor_id).listIterator(); li.hasNext();) {
			Conversation conv = (Conversation) li.next(); %>
			
		function addMessages<%= conv.getId() %>(xml) {
			if($("status",xml).text() == "<%= ChatController.NO_NEW_MSG %>") return;
			timestamp = $("time",xml).text();
			$("message",xml).each(function(id) {
				message = $("message",xml).get(id);
				$("#messagewindow<%= conv.getId() %>").prepend("<span class=\"style1\">("+$("time",message).text() + ") </span><b>"+$("author",message).text()+
											":</b>  " +$("text",message).text()+
											"<br />");
											
				new_start_index<%= conv.getId() %> = $("id",message).text();
				
				//document.write("its :" + new_start_index<%= conv.getId() %> + " which has a vlaue of : " + parseInt(new_start_index<%= conv.getId() %>));
				
				if (parseInt(new_start_index<%= conv.getId() %>) > parseInt(start_index<%= conv.getId() %>)){
					start_index<%= conv.getId() %> = new_start_index<%= conv.getId() %>;
				}
				
			});
			
			/////////////////////////////////
			
			
		}
		function updateMsg<%= conv.getId() %>() {
			$.post("one_on_one_chat_server.jsp",{ start_index: start_index<%= conv.getId() %>, conversation: $("#conversation<%= conv.getId() %>").val(), time: timestamp }, function(xml) {
				$("#loading").remove();
				addMessages<%= conv.getId() %>(xml);
			});
			setTimeout('updateMsg<%= conv.getId() %>()', 4000);
		}
		<% } %>

</script>
<style type="text/css" media="screen">
body {
margin:2;
padding:0;
height:100%;
width:100%;
} 
<%
	// Loop over the conversations for this Actor
	for (ListIterator<Conversation> li = Conversation.getActorsPrivateChats(pso.schema, pso.sim_id, pso.actor_id).listIterator(); li.hasNext();) {
		Conversation conv = (Conversation) li.next();
%>	
		#messagewindow<%= conv.getId() %> {
			height: 250px;
			border: 1px solid;
			padding: 5px;
			overflow: auto;
		}
<%
	} // end of loop over conversations
%>
		
.style1 {font-size: small}
.style1 {font-size: small}
</style>
</head>
<body onLoad="timedCount();"> 
<table width="50%">

<TR>
    <TD width="25%"></TD>
  </TR>
</table>
<P>

<%
	
	// Loop over the conversations for this Actor
	for (ListIterator<Conversation> li = Conversation.getActorsPrivateChats(pso.schema, pso.sim_id, pso.actor_id).listIterator(); li.hasNext();) {
		Conversation conv = (Conversation) li.next();
	
	
%>
<table width="100%" border="1">
  <% 
  		
		// Loop over the conversation actors (should be 2 of them) for this private chat.
  		for (ListIterator<ConvActorAssignment> liii = conv.getConv_actor_assigns().listIterator(); liii.hasNext();) {
			ConvActorAssignment caa = (ConvActorAssignment) liii.next();
			
			// Don't do the chat with the actor and his or her self.
			if (!(caa.getActor_id().equals(pso.actor_id))) {
			
			String this_a_id = caa.getActor_id().toString();
			String this_a_name = pso.getActorName(request, caa.getActor_id());
	%>
		
  <tr valign="top"> 
    <td width="40%"> Your conversation with <%= this_a_name %><br>
				<form id="chatform<%= conv.getId() %>" >
  <p>Message: <input type="text" id="msg<%= conv.getId() %>" width="40" /> <br />
	<input type="hidden" id="author<%= conv.getId() %>" value="You" />
    <input type="hidden" id="conversation<%= conv.getId() %>" value="<%= conv.getId() %>" />
	<input type="submit" value="Send">
  </p>
</form>
			</td>
			
    <td><p id="messagewindow<%= conv.getId() %>"><span id="loading">Loading...</span></p></td>
		</tr>
        <% 
			} //End of if this is a different actor from this particular player character.
		} // End of loop over conversation actors. %>
	</table>
 <% 
 	}
  %>
	</P>
</body>
</html>
<%
	
%>