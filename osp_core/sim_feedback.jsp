<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,
	java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.specialfeatures.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String sending_page = (String) request.getParameter("sending_page");
	String update_text = (String) request.getParameter("update_text");
	
	if ( (sending_page != null) && (update_text != null) && (sending_page.equalsIgnoreCase("player_reflection"))){
		String player_reflection_text = (String) request.getParameter("player_reflection_text");
		
		playerReflection.setBigString(player_reflection_text);
		playerReflection.save(pso.schema);
		
		   
	} // End of if coming from this page and have added text
	

%>
<html>
<head>
<title>Player Reflection Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>

<body>
<h1>Simulation Feedback</h1>
<p>This page is a place for you to enter in your comments on this simulation.</p>
<p>These comments will be seen by simulation authors and instructors looking at this simulation in the library. Your constructive comments can help instructors decide to chose this simulation, or not. And also provide them valuable insight on how to best conduct it for future classes.</p>
<form name="form1" method="post" action="sim_feedback.jsp">

<input type="hidden" name="sending_page" value="player_reflection" />
<input type="hidden" name="cs_id" value="<%= cs_id %>" />
  
  		  <p>
		  <textarea id="player_reflection_text" name="player_ratings_text" style="height: 310px; width: 710px;">
		  <%= playerReflection.getBigString() %>
		  </textarea>
		<script language="javascript1.2">
  			generate_wysiwyg('player_reflection_text');
		</script>
		  </p>
  <p>
    <label>
    <input type="submit" name="update_text" value="Submit">
    </label>
  </p>
</form>

<p>&nbsp;</p>
</body>
</html>
<%
	
%>
