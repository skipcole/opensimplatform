<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
%>
<html>
<head>
<title>Create Actor</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>

<h1>Users Logged in to this Simulation Run</h1>
<p>Simulation: </p>
<p>Running Simulation</p>

<p>&nbsp;</p>
<blockquote>

<table width="80%" border="1">
<tr>
      <td width=50><strong>User </strong></td>
<td width="25%"><strong>Name</strong></td>
<td><strong>Description</strong></td>
</tr>
  <%
  		for (ListIterator li = simulation.getActors().listIterator(); li.hasNext();) {
			Actor aa = (Actor) li.next();
			
			if (aa.isShown() {
			
		%>
  <tr>
    <td valign="top"><img src="images/actors/<%= aa.getImageFilename() %>"  ></td>
    <td valign="top"><%= aa.getName() %></td><td valign="top"><%= aa.getPublic_description() %></td>
  </tr>

  <%
  		} // of if this character isShown.
	}
%>
</table>
</blockquote>
<p>&nbsp;</p>
<p></p>
</body>
</html>
<%
	
%>