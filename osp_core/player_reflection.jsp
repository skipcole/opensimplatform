<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,
	java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.specialfeatures.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}

	PlayerReflection playerReflection = pso.handlePlayerReflection(request);
	
	String cs_id = (String) request.getParameter("cs_id");
	
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	

%>
<html>
<head>
<title>Player Reflection Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<p><%= cs.getBigString() %></p>

<form name="form1" method="post" action="player_reflection.jsp">
  <p>
    <label>
    <input type="submit" name="update_text" value="Save">
    </label>
  (Feel free to save your reflections frequently as you type. <strong><em>Make sure</em></strong> you save your final reflections.)</p>
<input type="hidden" name="sending_page" value="player_reflection" />
<input type="hidden" name="cs_id" value="<%= cs_id %>" />
  
  		  <p>
		  <textarea id="player_reflection_text" name="player_reflection_text" style="height: 310px; width: 710px;">
		  <%= playerReflection.getBigString() %>
		  </textarea>
		<script language="javascript1.2">
  			generate_wysiwyg('player_reflection_text');
		</script>
		  </p>

</form>

<p>&nbsp;</p>
</body>
</html>
<%
	
%>
