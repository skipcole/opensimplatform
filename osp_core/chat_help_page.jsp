<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.communications.*,
	org.usip.osp.networking.*,
	org.usip.osp.baseobjects.*" 
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
	ChatHelpCustomizer chc = new ChatHelpCustomizer(request, pso, cs);
	
	ArrayList constantPeople = chc.getConstantActors();
	
	Actor this_actor = new Actor();
	
	if (!(pso.preview_mode)) {
	
		this_actor = pso.giveMeActor();
    
	}
	
	// Keep a set of actors to loop over check on if online.
	Hashtable setOfActors = new Hashtable();

	ArrayList theseConversations = new ArrayList();
	
	if (!(pso.preview_mode)) {
	// Loop over the conversations for this Actor
	for (ListIterator<Long> li = constantPeople.listIterator(); li.hasNext();) {
		Long constantActorId = (Long) li.next();
		
		if (constantActorId.intValue() != this_actor.getId().intValue()) {
			setOfActors.put(constantActorId.toString(), "set");
			Conversation conv = Conversation.getChatHelpConversation(pso.schema, pso.sim_id, cs.getId(),
				pso.getRunningSimId(), this_actor.getId(), constantActorId);
			
			if (conv != null){
				theseConversations.add(conv);
			}
		} else {
			List convs = Conversation.getAllChatHelpConversationsForHelper(pso.schema, pso.sim_id, cs.getId(),
				pso.getRunningSimId(), this_actor.getId());
				
			for (ListIterator<Conversation> lic = convs.listIterator(); lic.hasNext();) {
				Conversation conv = lic.next();	
				
				if (conv != null){
					theseConversations.add(conv);
				}
			}
		}

	}
	} // end of not in preview mode.
	
%>
<html>
<head>
<title>Chat Help Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script type="text/javascript">
	<%		// The print out statement below fills in the hashtable and lists important details regarding the actors.  %>
	<%= pso.generateChatHelpLines(request, theseConversations, new Long(cs_id)) %>

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
			
		<%  // Loop over the conversations for this Actor
		for (ListIterator<Conversation> li = theseConversations.listIterator(); li.hasNext();) {
			Conversation conv = (Conversation) li.next(); %>
		
			updateMsg<%= conv.getId() %>();
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
			<% } // End of loop over conversations. %>
			
		}); // End of loop over if ready
		
		<%  // Loop over the conversations for this Actor
		for (ListIterator<Conversation> li = theseConversations.listIterator(); li.hasNext();) {
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
			$.post("one_on_one_chat_server.jsp",{ 
				start_index: start_index<%= conv.getId() %>, 
				conversation: $("#conversation<%= conv.getId() %>").val(), 
				time: timestamp }, function(xml) {
				$("#loading").remove();
				addMessages<%= conv.getId() %>(xml);
			});
			setTimeout('updateMsg<%= conv.getId() %>()', 2000);
		}
		<% } // End of loop over conversations. %>
		
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
    							//alert("Data Loaded for : actorpresent<%= key %>" + xml);
							} else if ($("status_code",xml).text() == "online") {
								$("#actorpresent<%= key %>").text("online");
    							//alert("Data Loaded for : actorpresent<%= key %>" + xml);
							}
							
							;
  						}
						
						);
					
					
					setTimeout('checkOnlineActor<%= key %>()', 1000);
				
				}
		<%
			} // End of loop over unique actor ids to add 'user online' function
		%>

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
	for (ListIterator<Conversation> li = theseConversations.listIterator(); li.hasNext();) {
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

		<%
			for (Enumeration e = setOfActors.keys(); e.hasMoreElements();){
				String key = (String) e.nextElement();
		%>
		#actorpresent<%= key %>{
			text-decoration:none
		}	
		<%
			}
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
<table width="100%" border="1">
  <% 
  
  	// Loop over the conversations for this Actor
	for (ListIterator<Conversation> li = theseConversations.listIterator(); li.hasNext();) {
		Conversation conv = (Conversation) li.next();
  		
		// Loop over the conversation actors (should be 2 of them) for this private chat.
  		for (ListIterator<ConvActorAssignment> liii = conv.getConv_actor_assigns(pso.schema).listIterator(); liii.hasNext();) {
			ConvActorAssignment caa = (ConvActorAssignment) liii.next();
			
			// Don't do the chat with the actor and his or her self.
			if (!(caa.getActor_id().equals(pso.getActorId()))) {
			
			String this_a_id = caa.getActor_id().toString();
			String this_a_name = USIP_OSP_Cache.getActorName(pso.schema, pso.sim_id, pso.getRunningSimId(), request, caa.getActor_id());
			
			String this_a_thumb = "images/actors/" + pso.getActorThumbImage(request, caa.getActor_id());
			
	%>
		
  <tr valign="top"> 
    <td width="40%"> Your conversation with <%= this_a_name %>
    (<I><span id="actorpresent<%= caa.getActor_id().toString() %>">Checking status ...</span></I>)<br>
				<form id="chatform<%= conv.getId() %>" >
  <p>&nbsp;&nbsp;<textarea cols="40" rows="5" id="msg<%= conv.getId() %>" ></textarea>
	<input type="hidden" id="author<%= conv.getId() %>" value="You" />
	<input type="hidden" id="cs_id" name="cs_id" value="<%= cs_id %>" />
    <input type="hidden" id="conversation<%= conv.getId() %>" value="<%= conv.getId() %>" /><br />
	<input type="submit" value="Send"  onClick="sendText();return false;">
  </p>
</form>			</td>
			
    <td><p id="messagewindow<%= conv.getId() %>"><span id="loading" >Loading...</span></p></td>
		</tr>
        <% 
			} //End of if this is a different actor from this particular player character.
		} // End of loop over conversation actors.
		
		} // End of loop over conversation  %>
	</table>
	</P>
</body>
</html>