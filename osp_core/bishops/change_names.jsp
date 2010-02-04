<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	Actor act = new Actor();
	
	if (!(pso.preview_mode)) {
		act = Actor.getMe(pso.schema, pso.actor_id);
	}

	String sending_page = (String) request.getParameter("sending_page");
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("change_names"))){
          
		String new_name = (String) request.getParameter("new_name");

		act.setAssumedIdentity(true);
		   
	} // End of if coming from this page and have added simulation.
	
	

	
%>
<html>
<head>
<title>Introduction Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<p>Assume Identity</p>
<form name="form1" method="post" action="change_names.jsp">

<table width="80%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td>Old Name</td>
    <td><%= act.getName() %></td>
  </tr>
  <tr>
    <td>New Name</td>
    <td>
      <label>
        <input type="text" name="new_name" id="new_name" value="">
        </label>
    
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><label>
      <input type="submit" name="button" id="button" value="Submit">
    </label></td>
  </tr>
</table>
</form>
<p></p>
<table width="95%" border="0" cellspacing="2" cellpadding="2">
  <tr valign="top"> 
    <td><p>&nbsp;</p>
      <p>This page is designed to allow the control player to change the identities of the actors in the simulation. </p>
      <p>&nbsp;</p>
    <p>&nbsp;</p></td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>
