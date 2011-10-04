<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	SimActorAssignment this_saa = afso.addActorToSim(request);
	
	Actor this_act = new Actor();
	
	if (this_saa.getActorId() != null) {
		this_act = Actor.getById(afso.schema, this_saa.getActorId());
	}	
	
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
<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>
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
              <h1>Actor Assignments </h1>
              <p>Here you can leave notes about the actors in the simulation: if they are required or not, details concerning the type of player best fitted for them, notes on which actors to include in unison, etc. For example, some simulations may have a variable number of players, such as from 8 to 18. But you may want to say here that if one includes Actor X, then one should also include Actor Y for balance. </p>
              <% 
						if (this_saa.getId() != null) {
					%>
				  <strong>Update Actor (<a href="assign_actor_to_simulation.jsp?clear_queue=true">click here to clear</a>)
				  <% } else { %>
				  Assign Actor</strong> 
				  <% } %>
				  <form action="assign_actor_to_simulation.jsp" method="post" name="assign_actor" id="assign_actor" >
                <table width="100%" border="1">
                  <tr valign="top">
                    <td width="28%" align="left" valign="top"><strong>Actor</strong></strong></td>
                    <td width="11%" align="left" valign="top"><strong>Required <a href="helptext/actor_required.jsp" target="helpinright">(?)</a></strong></td>
                    <td width="9%" align="left" valign="top"><strong>Priority <a href="helptext/actor_priority.jsp"  target="helpinright">(?)</a></strong></td>
                    <td width="14%" align="left" valign="top"><strong>Role</strong></td>
                    <td width="16%" align="left" valign="top"><strong>Notes</strong></td>
                    <td width="22%" align="left" valign="top"><strong>
					<% 
						if (this_saa.getId() != null) {
					%>
					Update
					<input type="hidden" name="saa_id" value="<%= this_saa.getId() %>" />
					<input type="hidden" name="update_saa" value="true" />
					<% } else { %>
					Assign
					<input type="hidden" name="create_saa" value="true" />
					<% } %>
					
					
					</strong></td>
                    </tr>
                  <tr>
                    <td align="left" valign="top">
					<% 
						if (this_saa.getId() != null) {
					%> <%= this_act.getActorName() %>
					<% } else { %>
					<select name="actor_being_worked_on_id">
                        <% 
                for (ListIterator la = sim.getAvailableActorsForSim(afso.schema).listIterator(); la.hasNext();) {
					Actor act = (Actor) la.next();
		%>
                        <option value="<%= act.getId().toString() %>"><%= act.getActorName() %></option>
                        <% } %>
                    </select>
					
					<% } // end of if no actor queued up %>					</td>
                    <td align="left" valign="top"><label>
					<%
						String selected_req = "";
						String selected_opt = "selected=\"selected\"";
						
						if (this_saa.getAssignmentType() == SimActorAssignment.TYPE_REQUIRED){
							selected_req = "selected=\"selected\"";
							selected_opt = "";
						}
					%>
                      <select name="saa_type" id="saa_type">
                        <option value="required" <%= selected_req %>>Required</option>
                        <option value="optional" <%= selected_opt %>>Optional</option>
                      </select>
                    </label></td>
                    <td align="left" valign="top"><label>
                      <input name="saa_priority" type="text" size="5" maxlength="5" value="<%= this_saa.getAssignmentPriority() %>" />
                    </label></td>
                    <td align="left" valign="top"><input name="saa_role" type="text" value="<%= this_saa.getActors_role() %>" /></td>
                    <td align="left" valign="top"><label>
                      <textarea name="saa_notes"><%= this_saa.getAssignmentNotes() %></textarea>
                      </label>                    </td>
                    <td align="left" valign="top"><input type="hidden" name="sending_page" value="assign_actor" />
                        <input type="hidden" name="sim_id" value="<%= sim.getId().toString() %>" />
                        <input type="submit" name="addactortosim" value="Submit" /></td>
                    </tr>
                </table>
              </form>
              <p><hr /></p>
              <h2>Currently Assigned Actors in <%= sim.getSimulationName() %>:<%= sim.getVersion() %><br />
              </h2>
              <table width="100%" border="1" cellspacing="0" cellpadding="2">
        <tr valign="top">
          <td width="20%"> <strong>Actors*</strong></td>
            <td width="10%"><strong>Required <a href="helptext/actor_required.jsp" target="helpinright">(?)</a></strong></td>
            <td width="10%"><strong>Priority</strong></td>
            <td width="10%"><strong>Role</strong></td>
            <td width="45%"> <strong>Notes</strong></td>
            <td width="5%"><strong>Inactivate</strong></td>
        </tr>
			<%
			
			List actorAssignments = SimActorAssignment.getActiveActorsAssignmentsForSim(afso.schema, sim.getId(), true);
			
			for (ListIterator la = actorAssignments.listIterator(); la.hasNext();) {
				SimActorAssignment saa = (SimActorAssignment) la.next();
			
				Actor act = Actor.getById(afso.schema, saa.getActorId());

			%>
          <tr valign="top">
            <td> 
                <A href="assign_actor_to_simulation.jsp?queue_up=true&saa_id=<%= saa.getId() %>"> <%= act.getActorName() %> </A>                        </td>
              <td><%= saa.getAssignmentTypeDescriptor() %></td>
              <td><%= saa.getAssignmentPriority() %></td>
              <td><%= saa.getActors_role() %></td>
              <td><%= saa.getAssignmentNotes() %></td>
              <td><a href="assign_actor_to_simulation.jsp?inactivate=true&saa_id=<%= saa.getId() %>">inactivate</a></td>
          </tr>
			  <% } // End of loop over Actors %>
      </table>
              * Select actor's name to edit their assignment
              <h2>&nbsp;</h2>
			  <% 
			  List actorNonAssignments = SimActorAssignment.getActiveActorsAssignmentsForSim(afso.schema, sim.getId(), false);
			  
			  if ((actorNonAssignments != null) && (actorNonAssignments.size() > 0)) {
			  %>
              <h2>Currently <span class="style1">Inactivated</span> Assignments in <%= sim.getSimulationName() %>:<%= sim.getVersion() %><br />
                  </h2>
              <table width="100%" border="1" cellspacing="0" cellpadding="2">
                <tr valign="top">
                  <td width="39%"><span class="style1"><strong>Inactive Actors</strong></span></td>
                  <td width="16%"><strong>Required <a href="helptext/actor_required.jsp" target="helpinright">(?)</a></strong></td>
                  <td width="11%"><strong>Priority</strong></td>
                  <td width="11%"><strong>Role</strong></td>
                  <td width="9%"><strong>Notes</strong></td>
                  <td width="9%"><strong>Activate</strong></td>
                </tr>
                <%
			for (ListIterator la = actorNonAssignments.listIterator(); la.hasNext();) {
				SimActorAssignment saa = (SimActorAssignment) la.next();
			
				Actor act = Actor.getById(afso.schema, saa.getActorId());

			%>
                <tr valign="top">
                  <td width="20%"> <%= act.getActorName() %> </td>
                  <td width="10%"><%= saa.getAssignmentTypeDescriptor() %></td>
                  <td width="10%"><%= saa.getAssignmentPriority() %></td>
                  <td width="10%"><%= saa.getActors_role() %></td>
                  <td width="50%"><%= saa.getAssignmentNotes() %></td>
                  <td width="5%"><a href="assign_actor_to_simulation.jsp?activate=true&saa_id=<%= saa.getId() %>">activate</a></td>
                </tr>
                <% } // End of loop over Actors %>
              </table>
		<% } // End of if there are some inactive assignments %>
              <p>&nbsp; </p>
      <p><a href="copy_actor_to_simulation.jsp">Add actor from other simulation </a><a href="helptext/copy_actor_from_other_sim.jsp" target="helpinright">(?)</a></p>
      <p>&nbsp;</p>
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