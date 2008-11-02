<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.communications.*,org.usip.oscw.baseobjects.*" 
	errorPage="" 
%>
<%

	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	String status_code = ChatController.NO_NEW_MSG + "";
	
	String conversation =  (String) request.getParameter("conversation");
	String start_index = (String) request.getParameter("start_index");

	java.util.Date now = new java.util.Date();
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy HH:mm a");
	String time_string = sdf.format(now) + "_" + start_index;
	
	String xml_msgs = "";
							
	xml_msgs = ChatController.getXMLConversation(pso.user_id, pso.actor_id, 
				start_index, conversation, pso, request);
			
	if ((xml_msgs != null) && (xml_msgs.trim().length() > 0)){
		status_code = ChatController.NEW_MSG + "";
	}
	
%>
<?xml version="1.0"?>
<response>
 <status><%= status_code %></status>
 <%= xml_msgs %>
</response>