<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = "create_simulation_phases.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
	///////////////////////////////////////////////////////////
	String sending_page = (String) request.getParameter("sending_page");
	SimulationPhase spOnScratchPad = afso.handleCreateOrUpdatePhase(simulation, request);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
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
			  <h1>Create Simulation Phase</h1>
			  <br />
            <blockquote> 
              <% 
			if (afso.sim_id != null) {
			
			List phaseList = SimPhaseAssignment.getPhasesForSim(afso.schema, afso.sim_id);

		%>
              <blockquote>
                <p>Create phases for the simulation <strong><%= simulation.getDisplayName() %></strong>. </p>
            <p>The set of actions and views (simulation sections) that are accessible 
              to each actor can be different in every simulation phase.</p>
            <p>Every simulation has at least two phases: one in which it begins, and one in which it ends. The default names of these are, respectively, 'Started' and 'Completed,' but these names can be changed. One can add as many additional phases as desired.</p>
            <form action="create_simulation_phases.jsp" method="post" name="form1" id="form1">
              <table width="80%" border="0" cellspacing="2" cellpadding="2">
                <tr> 
                  <td valign="top">Phase Name:</td>
                  <td valign="top"><input type="text" name="phase_name" value="<%= spOnScratchPad.getPhaseName() %>" /></td>
                </tr>
                <tr>
                  <td valign="top">Phase Notes:</td>
                  <td valign="top"><label>
                    <textarea name="phase_notes" id="textarea" cols="45" rows="5"><%= spOnScratchPad.getNotes() %></textarea>
                    </label></td>
                </tr>
                <tr>
                  <td valign="top">Nominal Order<br />
                    (N.O.) <a href="helptext/phases_nominal_order_help.jsp" target="helpinright">(?)</a>:</td>
                  <td valign="top"><label>
                    <input type="text" name="nominal_order" id="textfield" value="<%= spOnScratchPad.getOrder() + "" %>" />
                    </label></td>
                </tr>
                <tr>
                  <td valign="top">Advanced <a href="helptext/advanced_phase_features.jsp" target="helpinright">(?)</a></td>
                  <td valign="top"><% if (spOnScratchPad.getId() == null) { %>
                  One has to create a phase first before adding advanced features to it.<% } else { %>
                    <a href="advanced_phase_features.jsp?sp_id=<%= spOnScratchPad.getId() %>">Click here</a> to add advanced features to this phase.<% } %>                    </td>
                </tr>
                <tr> 
                  <td valign="top">&nbsp;</td>
                  <td valign="top">
                    <%
				if (spOnScratchPad.getId() == null) {
				%>
                    <input type="submit" name="command" value="Create" />
                    <%
				} else {
				%>
                    <input type="hidden" name="sp_id" value="<%= spOnScratchPad.getId() %>" />
					<table width="100%"><tr>
                <td align="left"><input type="submit"  name="command" value="Update" /></td>
				<td align="right"><input type="submit" name="command2" tabindex="6" value="Clear to Create New Phase" /></td>
				</tr></table>
                    <%
					}
				%>                </td>
                </tr>
                </table>
              <p>&nbsp;</p>
            </form>
            <p>Below are listed all of the current simulation phases for this simulation:</p>
            
            <% 
			boolean showMetaPhases = false;
			if (SimulationMetaPhase.simHasMetaPhases(afso.schema, afso.sim_id)) { 
				showMetaPhases = true;
			}
			%>
            
            <table width="100%" border="1" cellspacing="2" cellpadding="2">
              <tr> 
              	<% if (showMetaPhases) { %>
                	<td width="20%" valign="top"><h2>MetaPhase</h2></td>
                <% } %>
                <td width="20%" valign="top"><h2>Phase Name</h2></td>
                <td width="80%" valign="top"><h2>Phase Notes</h2></td>
                <td width="40" valign="top"><h2>N.O.</h2></td>
                <td width="40" valign="top"><h2>Remove</h2></td>
              </tr>
              <%
		for (ListIterator li = phaseList.listIterator(); li.hasNext();) {
			SimulationPhase sp = (SimulationPhase) li.next();
			
			String flagNotes = "";
			if (sp.isFirstPhase()){
				flagNotes = "<I><small>(First Phase)</small></I>";
			}
			if (sp.isLastPhase()){
				flagNotes = "<I><small>(Last Phase)</small></I>";
			}
			
			// Need a way to look at next phase, and see if it is the same meta-phase, if so, increase the 'col span'
			// and mark it as shown so that the next phase doesn't create a cell to show its meta phase in.
			
		%>
              <tr>
              <% if (showMetaPhases) { %>
                	<td width="20%" valign="top"><%= afso.getMetaPhaseName(request, sp.getMetaPhaseId()) %></td>
                <% } %>
                <td valign="top"><a href="create_simulation_phases.jsp?command=Edit&sp_id=<%= sp.getId().toString() %>"><%= sp.getPhaseName() %></a>  <%= flagNotes %></td>
                <td valign="top"><%= sp.getNotes() %></td> 
                <td valign="top"><%= sp.getOrder() + "" %></td>
                <td valign="top">
                  <% if (flagNotes.length() == 0){ %>
                  <a href="delete_object.jsp?phase_sim_id=<%= afso.sim_id %>&object_type=phase&objid=<%= sp.getId().toString() %>&object_info=<%= sp.getPhaseName() %>">remove</a>
                  <% } %>                  </td>
              </tr>
              <%
	}
%>
              </table>
              <% if (SimulationMetaPhase.simHasMetaPhases(afso.schema, afso.sim_id)) { %>
            <p>hide/display metaphases</p>
            <% } %>
            <p>For a more printable list, <a href="print_simulation_phases.jsp">click here</a>.</p>
            <div align="center"><a href="create_actors.jsp">Next Step: Create Actors 
            </a></div>
          </blockquote>
            </blockquote>
            <% } else { // End of if have set simulation id. %>
            <blockquote>
              <p>
                <%@ include file="select_message.jsp" %></p>
                  </blockquote>
            <% } // End of if have not set simulation for edits. %>            <a href="create_simulation_planned_play_ideas.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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