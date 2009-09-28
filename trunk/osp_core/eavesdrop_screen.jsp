<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.communications.*,org.usip.osp.baseobjects.*" 
	errorPage="" 
%>
<%

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	

	// handle Eavesdrop
	String sending_page = (String) request.getParameter("sending_page");
	
	String conversationText = "";
	
	String conv_id = "0";
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("eavesdrop"))) {
	
		conv_id = (String) request.getParameter("conv_id");
		conversationText = ChatController.getHTMLConv(request, pso, conv_id);
	}
	
%>
<html>
<head>
<style type="text/css" media="screen">
body {
margin:2;
padding:0;
height:100%;
width:100%;
} 
</style>
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body onLoad="timedCount();"> 
<table width="100%" border="1">
<TR>
    <TD valign="top" width="50%"><h1>Eavesdrop</h1>
      <p>Select the conversation below you want to see. </p>
      
      	<form name="form1" method="post" action="eavesdrop_screen.jsp">
        <input type="hidden" name="sending_page" value="eavesdrop" />
	Conversation: <select name="conv_id">
	
	<%
		for (ListIterator li = Conversation.getAllForRunningSim(pso.schema, pso.sim_id, pso.running_sim_id).listIterator(); li.hasNext();) {
			Conversation conv = (Conversation) li.next();
			
			String selected = "";
			
			if (conv.getId().equals(new Long(conv_id))){
				selected = " selected ";
			}
			
			%>
	  <option value="<%= conv.getId() %>" <%= selected %>><%= conv.getListOfActors(pso.schema, pso, request) %></option>
		<%	}  %>
		</select>
    <label>
    <input type="submit" name="button" id="button" value="Submit">
    </label>
      	</form>
    
    </TD>
    <TD width="50%" valign="top"><%= conversationText %></TD>
</table>
</body>
</html>