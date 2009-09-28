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
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	SimulationRatings sr = pso.handleSimFeedback(request);


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
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h1>Simulation Feedback</h1>
<p>This page is a place for you to enter in your comments on this simulation.</p>
<p>These comments will be seen by simulation authors and instructors looking at this simulation in the library. Your constructive comments can help instructors decide to chose this simulation, or not. And also provide them valuable insight on how to best conduct it for future classes.</p>
<form name="form1" action="sim_feedback.jsp" method="POST">
<input type="hidden" name="sending_page" value="sim_feedback" />
<p>Please enter your comments below.<p>
  <textarea id="sim_feedback_text" name="sim_feedback_text" style="height: 310px; width: 710px;">
		  <%= sr.getUser_comments() %>
		  </textarea>
  <script language="javascript1.2">
  			generate_wysiwyg('sim_feedback_text');
		</script>
  </p>
  		  <p>Optional: Your Name 
  		    <label>
  		    <input type="text" name="users_stated_name" id="textfield" value="<%= sr.getUsers_stated_name() %>" >
  		    </label>
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
