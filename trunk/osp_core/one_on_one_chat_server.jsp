<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.communications.*,org.usip.oscw.baseobjects.*" 
	errorPage="" 
%>
<%

	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	String status_code = "2";
	
	String message = (String) request.getParameter("message");
	String name =  (String) request.getParameter("name");
	String conversation =  (String) request.getParameter("conversation");
	String start_index = (String) request.getParameter("start_index");

	java.util.Date now = new java.util.Date();
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy HH:mm a");
	String time_string = sdf.format(now) + "_" + start_index;
	
	String xml_msgs = "";
							
	if ((message!= null) && (message.trim().length() > 0)){
		
		xml_msgs = ChatController.insertAndGetXMLConversation(pso.user_id, pso.actor_id, 
				start_index, message, conversation, null,
				pso.running_sim_id, pso.schema);
		
				
		status_code = "1";
	}
	

	
%>
<?xml version="1.0"?>
<response>
 <status><%= status_code %></status>
 <message>
 	<conversation><%= conversation %></conversation>
    <time><%= time_string %></time>
    <author><%= name %></author>
    <text><%= message %></text>
 </message>
 <%= xml_msgs %>
</response>