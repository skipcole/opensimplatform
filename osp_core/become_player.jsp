<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
		
	Actor this_actor = new Actor();
	
	if (!(pso.preview_mode)) {
	
		this_actor = pso.giveMeActor();
    
	}

	
%>
<html>
<head>
<title>Create Actor</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>

<body>
<h1>Change into a Different Player </h1>

<p>You are currently: <%= this_actor.getActorName() %></p>
<blockquote>
  <table width="90%" border="1">
<tr>
  <td width=50>&nbsp;</td>
<td width=50><strong>Picture/Name</strong></td>
</tr>
  <%
  		for (ListIterator li = simulation.getActors(pso.schema).listIterator(); li.hasNext();) {
			Actor act = (Actor) li.next();
			
			if (!(act.getId().equals(pso.getActorId()))) {

		%>
  <tr>
    <td valign="top" width="200"><form name="form1" method="post" action="../simulation/simwebui.jsp" target="_top">
      <label>
        <input type="submit" name="Submit" value="Become -->">
        </label>
		<input type="hidden" name="tabposition" value="1" />
		<input type="hidden" name="sending_page" value="become_player" />
		<input type="hidden" name="actor_id" value="<%= act.getId() %>" />
		<input type="hidden" name="actor_name" value="<%= act.getActorName() %>" />
      <%= act.getActorName(pso.schema, pso.getRunningSimId(), request) %>
    </form>    </td>
    <td valign="top" width="200"><img src="images/actors/<%= act.getImageFilename() %>" width="200"  ><br></td>
    </tr>

  <%
	} // End of don't show the same actor as this player is playing.
	
	} // End of loop over actors
%>
</table>
</blockquote>
<p>&nbsp;</p>
<p></p>
</body>
</html>