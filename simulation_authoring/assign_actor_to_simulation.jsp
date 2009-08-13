<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true), true);

	String sending_page = (String) request.getParameter("sending_page");
	String addactortosim = (String) request.getParameter("addactortosim");
	String remove = (String) request.getParameter("remove");
	
	String actor_being_worked_on_id = (String) request.getParameter("actor_being_worked_on_id");
	String sim_id = (String) request.getParameter("sim_id");
	
	if ( (sending_page != null) && (addactortosim != null) && (sending_page.equalsIgnoreCase("assign_actor"))){
		afso.addActorToSim(sim_id, actor_being_worked_on_id);
	} // End of if coming from this page and have assigned actor
	
	if ( (remove != null) &&  (remove.equalsIgnoreCase("true"))){
		afso.removeActorFromSim(sim_id, actor_being_worked_on_id);
		     
	} // End of if coming from this page and have removed actor
	

	//////////////////////////////////
	List simList = Simulation.getAll(afso.schema);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Open Simulation Platform Control Page</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Assign 
                Actor to a Simulation</h1>
              <br />
      <table width="100%" border="0" cellspacing="2" cellpadding="2">
        <tr valign="top">
          <td>&nbsp;</td>
            <td> <h2>Simulation/Version</h2></td>
            <td> <h2>Current Actors</h2></td>
            <td> <h2>Actor</h2></td>
            <td> <h2>Assign</h2></td>
          </tr>
        <%
		
		for (ListIterator li = simList.listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
		%>
        <form action="assign_actor_to_simulation.jsp" method="post" name="form1" id="form1">
          <tr valign="top">
            <td>&nbsp;</td>
              <td><%= sim.getName() %>:<%= sim.getVersion() %></td>
              <td><%
			
			for (ListIterator la = sim.getActors(afso.schema).listIterator(); la.hasNext();) {
				Actor act = (Actor) la.next();

			%> 
                <A href="assign_actor_to_sim_see_role.jsp?actor_being_worked_on_id=<%= act.getId() %>&sim_id=<%= sim.getId() %>"> <%= act.getName() %> </A>
                <A href="assign_actor_to_simulation.jsp?remove=true&actor_being_worked_on_id=<%= act.getId().toString() %>&sim_id=<%= sim.getId().toString() %>"> (remove) </A><br/>
                <% } // End of loop over Actors %>                </td>
              <td><select name="actor_being_worked_on_id">
                <% 
                for (ListIterator la = sim.getAvailableActors(afso.schema).listIterator(); la.hasNext();) {
					Actor aa = (Actor) la.next();
		%>
                <option value="<%= aa.getId().toString() %>"><%= aa.getName() %></option>
                <% } %>
                </select></td>
              <td> <input type="hidden" name="sending_page" value="assign_actor" /> 
                <input type="hidden" name="sim_id" value="<%= sim.getId().toString() %>" /> 
                <input type="submit" name="addactortosim" value="Submit" /></td>
            </tr>
          </form>
          <%
  	} // End of loop over simulations
  %>
      </table>
      <p>&nbsp;</p>
      <div align="center"><a href="create_injects.jsp">Next Step: Create Injects</a></div>
      <a href="create_actors.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
<%
	
%>