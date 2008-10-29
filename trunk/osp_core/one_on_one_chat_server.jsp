<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%
	String status_code = "2";
	
	String message = "";
	String name = request.getParameter("name");
	String conversation = request.getParameter("conversation");
	String time = request.getParameter("time");
	
		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			String pname = (String) e.nextElement();
			
			if (pname.startsWith("message")){
				message =  (String) request.getParameter(pname);
			}
			if (pname.startsWith("name")){
				name =  (String) request.getParameter(pname);
			}
			if (pname.startsWith("conversation")){
				conversation =  (String) request.getParameter(pname);
			}
		}
							
	if ((message!= null) && (message.trim().length() > 0)){
		status_code = "1";
	}
	
%>
<?xml version="1.0"?>
<response>
 <status><%= status_code %></status>
 <time>1170323512</time>
 <message>
 	<conversation>1</conversation>
   <author><%= name %></author>
   <text><%= message %></text>
 </message>
</response>