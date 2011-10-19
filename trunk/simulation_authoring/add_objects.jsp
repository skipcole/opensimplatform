<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = "add_objects.jsp";
	
	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Add Special Features Page</title>


<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><% 
			if (afso.sim_id != null) {
		%>
			  <br />
              <h1>Add Simulation Object</h1>
              <blockquote>
                <h3>Tried and True Objects</h3>
              </blockquote>
              <ul>
                <li><a href="make_create_document_page.jsp">Add Document</a> <a href="helptext/add_document.jsp" target="helpinright">(?)</a> - Add a document to the simulation.</li>
                <li><a href="make_create_document_notifications_page.jsp">Add Document Change Notification</a> <a href="helptext/add_document_change_notification.jsp" target="helpinright">(?)</a> - Create an alert to notify a player that a document has changed.</li>
                <li><a href="injects.jsp">Add Inject</a> <a href="helptext/create_injects_help.jsp" target="helpinright">(?)</a> - Add events that can happen to students during the simulation.</li>
              </ul>
              <p>&nbsp;</p>
              <blockquote>
                <h3>Experimental Objects</h3>
              </blockquote>
              <ul>
                <li><a href="make_create_parameter_page.jsp">Add Parameter</a> (?) - Add a value that can be adjusted during a simulation.</li>
                <li><a href="make_create_set_of_links_page.jsp">Add Set of Links</a> (?) - Add a set of links </li>
                <li><a href="make_create_items_page.jsp">Create Inventory Items</a> (?) - Create and then add items to your player's inventory. </li>
                <li><a href="make_create_game_timer.jsp">Game Time</a> (?) - Controls over a clock shown to players.</li>
                <li><a href="incorporate_underlying_model.jsp">Incorporate Underlying Model</a> (work in progress) (?) - Add automated computation to your simulation. </li>
                <li><a href="timeline_creator.jsp">Timeline Creator</a> (?) - Add a sequence of events to your simulation. </li>
              </ul>
              <p>&nbsp;</p>
              <br />

      <p align="center"><a href="set_universal_sim_sections.jsp?actor_index=0">Next 
        Step: Assign Simulation Sections to Actors</a></p>
      <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>      <a href="assign_actor_to_simulation.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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
<%
	
%>
