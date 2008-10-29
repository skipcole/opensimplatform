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
	
	// Get the id for this conversation
	String conv_id = request.getParameter("conversation_id");
	
	//Vector this_set_of_actors = ChatController.getActorsForConversation(pso, conv_id, request);
	
%>
<html>
<head>
<script type="text/javascript">

	var start_index = 0

</script>
<script type="text/javascript">
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
				<form name="form1" method="post" action="">
  <p>Text to send: 
          <input name="chattexttosend" type="text" id="chattexttosend" size="40" maxlength="255" />
          <BR>
	<input type="submit" name="Submit" value="Submit" onClick="sendText();return false;">
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