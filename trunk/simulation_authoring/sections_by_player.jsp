<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,java.io.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	afso.backPage = "review_sim.jsp";
	
	String loadSim = (String) request.getParameter("loadSim");
	if ((loadSim != null) && (loadSim.equalsIgnoreCase("true"))) {
		afso.sim_id = new Long((String) request.getParameter("sim_id"));
	}
	
	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
	List phaseList = SimPhaseAssignment.getPhasesForSim(afso.schema, afso.sim_id);
	
	// Need to pick player
	String sending_page = (String) request.getParameter("sending_page");
	
	Actor displayActor = new Actor();
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("sections_by_player"))){
          
		String actor_id = (String) request.getParameter("actor_id");
		afso.actor_being_worked_on_id = new Long (actor_id);
		
		displayActor = Actor.getById(afso.schema, afso.actor_being_worked_on_id);
	
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.4.1.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>USIP Open Simulation Platform</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			  <h1>Show Simulation Sections</h1>
			  <br />
			
	        <blockquote>
	          <% 
			if (afso.sim_id != null) {
		%>
        <form id="form1" name="form1" method="post" action="sections_by_player.jsp">
        <input type="hidden" name="sending_page" value="sections_by_player" />
Select Player<select name="actor_id">
	<option value="0">None Selected</option>
	<% for (ListIterator la = simulation.getActors(afso.schema).listIterator(); la.hasNext();) {
		Actor act = (Actor) la.next(); 
		
		String selected = "";
		
		if ((afso.actor_being_worked_on_id != null) && 
		(act.getId().intValue() == afso.actor_being_worked_on_id.intValue()) ){
			selected = " selected ";
		}
		
		%>
  	<option value="<%= act.getId() %>" <%= selected %>><%= act.getActorName() %></option>
  	<% } %>
</select>
        <input type="submit" name="button" id="button" value="Submit" />
        </form>
<br/>    
<%
	if ((afso.actor_being_worked_on_id != null) && (displayActor != null)) {
%>
<h1>Player <%= displayActor.getActorName() %> </h1>  
<% 
		for (ListIterator li = phaseList.listIterator(); li.hasNext();) {
			SimulationPhase sp = (SimulationPhase) li.next();
%>
<blockquote>
<h2><%= sp.getPhaseName() %></h2>
<blockquote>
<%
			// Get full list from database hit
			List<SimulationSectionAssignment> fullList = SimulationSectionAssignment.getBySimAndActorAndPhase(afso.schema,
					afso.sim_id, afso.actor_being_worked_on_id, sp.getId());

			// Copy the needed parts of that list into the ghosts
			for (ListIterator<SimulationSectionAssignment> li_ss = fullList.listIterator(); li_ss.hasNext();) {
				SimulationSectionAssignment ss = li_ss.next();
%>
<%= ss.getTab_heading() %> |
		<% } //End of loop over sections. %>
</blockquote>
</blockquote>
<%
		}  // End of loop over phases.
	
	} // End of if actor_being_worked_on_id != null
%>

See all sections.
<br/>
            <% } // End of if have not set simulation for edits. %>
            <% if (afso.isAuthor()) { %>
            <a href="review_sim.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>
            <% } else { %>
	  	    <a href="../simulation_facilitation/instructor_home.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>
		    <% } %>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>