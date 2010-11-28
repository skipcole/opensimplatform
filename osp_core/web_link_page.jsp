<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	WebLinkObjects.handleEdit(request, pso);
	
	String wlo_event_date = request.getParameter("wlo_event_date");

	System.out.println("event date: " + wlo_event_date);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>USIP OSP Web Link Page</title>
</head>

<frameset rows="58,*">
  <frame src="<%= pso.wloOnScratchPad.getWloTopPage() %>" name="wlo_top">
  <frame src="<%= pso.wloOnScratchPad.getWloBottomPage() %>" name="wlo_bottom">
</frameset>
<noframes><body>
</body>
</noframes></html>
