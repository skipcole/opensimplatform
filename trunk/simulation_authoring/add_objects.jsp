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
              <ul>
                <li><a href="make_create_conversation_page.jsp">Add Conversation</a> - Add a place for players to communicate. Note, for them to talk, you will need to add a chat room as a simulation section.</li>
                <li><a href="make_create_document_page.jsp">Add Document</a> - Add a document to the simulation that the players and instructors will be able to read and/or modify.</li>
                <li><a href="make_create_document_notifications_page.jsp">Add Document Change Notification</a> - Create an alert to notify a player that a document has been changed and may contain new information.</li>
                <li><a href="injects.jsp">Add Inject</a> <a href="helptext/create_injects_help.jsp" target="helpinright">(?)</a> - Add events that can happen to students during the simulation.</li>
                <li><a href="make_create_onelink_page.jsp">Add One Link</a> - Add a 'one link' to a web address that can altered during the simulation.</li>
                <li><a href="make_create_parameter_page.jsp">Add Parameter</a> - Add a value that can be adjusted during a simulation.</li>
                <li>(<span class="style1">Coming Someday</span>) <a href="incorporate_underlying_model.jsp">Incorporate Underlying Model</a> </li>
                <li><a href="timeline_editor.jsp">Timeline Creator</a></li>
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
