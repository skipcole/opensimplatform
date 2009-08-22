<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.persistence.*" 
	errorPage="../error.jsp" %>

<%

	AuthorFacilitatorSessionObject.handleInitialEntry(request);

	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	String bottomFrame = "control_panel.jsp";
	String show_intro = (String) request.getParameter("show_intro");
	
	if ((show_intro != null) && (show_intro.equalsIgnoreCase("true") ) ) {
		bottomFrame = "intro_text.jsp";
	}
	
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<title>Create Simulation</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<frameset rows="*" cols="75%,25%">
  <frameset rows="150,90%" border="0">
    <frame name="headeruptop" src="author_top.jsp">
    <frame name="bodyinleft" src="<%= bottomFrame %>">
  </frameset>
  
  <frame name="helpinright" src="helptext/control_basichelp.jsp">
</frameset>
<noframes><body>

</body></noframes>
</html>
