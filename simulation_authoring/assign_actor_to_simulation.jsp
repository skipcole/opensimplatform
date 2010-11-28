<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String sending_page = (String) request.getParameter("sending_page");
	String addactortosim = (String) request.getParameter("addactortosim");
	String remove = (String) request.getParameter("remove");
	
	String actor_being_worked_on_id = (String) request.getParameter("actor_being_worked_on_id");
	String sim_id = (String) request.getParameter("sim_id");
	
	if ( (sending_page != null) && (addactortosim != null) && (sending_page.equalsIgnoreCase("assign_actor"))){
		if (actor_being_worked_on_id != null) {
			afso.addActorToSim(sim_id, actor_being_worked_on_id);
		}
	} // End of if coming from this page and have assigned actor
	
	if ( (remove != null) &&  (remove.equalsIgnoreCase("true"))){
		afso.removeActorFromSim(sim_id, actor_being_worked_on_id);
		     
	} // End of if coming from this page and have removed actor
	
	Simulation sim = new Simulation();
	
	if (afso.sim_id != null) {
		sim = afso.giveMeSim();
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
        <% 
			if (afso.sim_id != null) {
		%>
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
          <td width="25%"> <h2>Simulation</h2></td>
            <td width="39%"> <h2>Actors</h2></td>
            <td width="16%"><h2>Required <a href="helptext/actor_required.jsp" target="helpinright">(?)</a></h2></td>
            <td width="11%"><h2>Priority</h2></td>
            <td width="9%"> <h2>Notes</h2></td>
            </tr>
        <form action="assign_actor_to_simulation.jsp" method="post" name="form1" id="form1">
          <tr valign="top">
            <td colspan="5"><%= sim.getSimulationName() %>:<%= sim.getVersion() %></td>
            </tr>
			<%
			
			List actorAssignments = SimActorAssignment.getActorsAssignmentsForSim(afso.schema, sim.getId());
			
			for (ListIterator la = actorAssignments.listIterator(); la.hasNext();) {
				SimActorAssignment saa = (SimActorAssignment) la.next();
			
				Actor act = Actor.getById(afso.schema, saa.getActorId());

			%>
          <tr valign="top">
            <td></td>
              <td> 
                <A href="assign_actor_to_sim_see_role.jsp?actor_being_worked_on_id=<%= act.getId() %>&sim_id=<%= sim.getId() %>"> <%= act.getActorName() %> </A>
                <A href="assign_actor_to_simulation.jsp?remove=true&actor_being_worked_on_id=<%= act.getId().toString() %>&sim_id=<%= sim.getId().toString() %>"> (remove) </A><br/>
                                </td>
              <td><label></label></td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              </tr>
			  <% } // End of loop over Actors %>
            <tr><td></td><td></td>
              <td colspan="3">&nbsp;</td>
            </tr>
            <tr><td colspan="5"><hr /></td>
            </tr>
          </form>
      </table>
      <p>sss</p>
	  <table>
	    <tr valign="top">
	      <td>Actor</td>
          <td><h2>Required <a href="helptext/actor_required.jsp" target="helpinright">(?)</a></h2></td>
	      <td><h2>Priority()</h2></td>
	      <td><h2>Notes</h2></td>
	      <td><h2>Assign</h2></td>
	      </tr>
	    <tr>
	      <td><select name="actor_being_worked_on_id">
                <% 
                for (ListIterator la = sim.getAvailableActorsForSim(afso.schema).listIterator(); la.hasNext();) {
					Actor act = (Actor) la.next();
		%>
                <option value="<%= act.getId().toString() %>"><%= act.getActorName() %></option>
                <% } %>
                </select></td>
	  
	  <td><label>
                <select name="select" id="select">
                  <option value="required" selected="selected">Required</option>
                  <option value="optional">Optional</option>
                        </select>
              </label></td>
              <td><label>
                <input name="textfield" type="text" size="5" maxlength="5" />
              </label></td>
              <td><form id="form2" name="form2" method="post" action="">
                <label>
                  <textarea name="textarea"></textarea>
                  </label>
              </form>
              </td>
              <td> <input type="hidden" name="sending_page" value="assign_actor" /> 
                <input type="hidden" name="sim_id" value="<%= sim.getId().toString() %>" /> 
                <input type="submit" name="addactortosim" value="Submit" /></td>
	  </tr>
	  </table>
	  <p><a href="copy_actor_to_simulation.jsp">Add actor from other simulation </a><a href="helptext/copy_actor_from_other_sim.jsp" target="helpinright">(?)</a></p>
	  <p></p>
      <div align="center"><a href="add_objects.jsp">Next Step: Add Objects</a></div>
      <a href="create_actors.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
  <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
  <% } %>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>