<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,
	java.util.*,
	org.usip.oscw.networking.*,
	org.usip.oscw.persistence.*,
	org.usip.oscw.baseobjects.*,
	org.usip.oscw.specialfeatures.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String bodyText = "";
	
	String cs_id = (String) request.getParameter("cs_id");
	
	MultiSchemaHibernateUtil.beginTransaction(pso.schema);

	CustomizeableSection cs = (CustomizeableSection) MultiSchemaHibernateUtil.getSession(pso.schema).get(CustomizeableSection.class, new Long(cs_id));
    
	bodyText = (String) cs.getBigString();
	
	Long base_doc_id = (Long) cs.getContents().get("doc_id");
	
	MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
	
	RunningSimulation rs = pso.giveMeRunningSim();
	
	System.out.println("blah: " + pso.schema +  " " + cs.getId() + " " + rs.getId());
	
	PlayerReflection playerReflection = PlayerReflection.getPlayerReflection(pso.schema, cs.getId(), rs.getId(), pso.actor_id);
	
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
<p><%= bodyText %></p>

<form name="form1" method="post" action="player_reflection.jsp">

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
