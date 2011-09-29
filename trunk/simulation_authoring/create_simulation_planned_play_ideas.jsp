<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}

	Simulation simulation = afso.handleWizardPage(request, afso.SIM_PLANNED_PLAY_IDEAS);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.nextPage);
		return;
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>


<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>

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
              <h1>Enter Planned Play Ideas</h1>
              <br />
	  <% 
			if (afso.sim_id != null) {
		%>
	    <p>Enter the planned play ideas <a href="helptext/planned_play_ideas.jsp" target="helpinright">(?)</a> for the simulation <strong><%= simulation.getDisplayName() %></strong>.<br>
	      (If you would like to work on a different simulation, <a href="select_simulation.jsp">click 
	        here</a>.)</p>
	<form action="create_simulation_planned_play_ideas.jsp" method="post" name="form2" id="form2">
	  <blockquote>
	    <p>
	      <textarea id="sim_text" name="sim_text" style="height: 710px; width: 710px;"><%= simulation.getPlannedPlayIdeas() %></textarea>
	      
	      <script language="javascript1.2">
  			generate_wysiwyg('sim_text');
		</script>
	      </p>
	    <p>Frequently multiple game sessions can be play concurrently, one can have multiple simulation universes in which the players play, but for each game session, please place in the data below. </p>
		
		<%
			PlannedPlaySessionParameters ppsp = simulation.getPPSP(afso.schema);
		%>
	    <table width="90%" border="1">
          <tr>
            <td>Minimum Number of Players</td>
            <td><label>
              <input type="text" name="min_num_players" value="<%= ppsp.getMinNumPlayers() %>" />
            </label></td>
          </tr>
          <tr>
            <td>Maximum Number of Players </td>
            <td><input type="text" name="max_num_players" value="<%= ppsp.getMaxNumPlayers() %>" /></td>
          </tr>
          <tr>
            <td>Minimum Amount of Play Time </td>
            <td><input type="text" name="min_play_time" value="<%= ppsp.getMinPlayTime() %>" /></td>
          </tr>
          <tr>
            <td>Recommended Play Time </td>
            <td><input type="text" name="rec_play_time" value="<%= ppsp.getRecommendedPlayTime() %>" /></td>
          </tr>
        </table>
	    <p>&nbsp;</p>
	    <p> 
              <input type="hidden" name="sending_page" value="authoring_wizard_page" />
              <a href="browse_planned_play_ideas.jsp">Browse Planned Play Ideas from Other Simulations</a>
            <table width="100%" border="0">
              <tr>
                <td align="center"><input type="submit" name="save" value="Save" /></td>
                <td align="center"><input type="submit" name="cancel" value="Cancel"   onClick="return confirm('Are you sure you want to cancel? All changes will be lost.');"  /></td>
                <td align="center"><label>
                  <input type="submit" name="save_and_proceed" value="Save and Proceed" />
                </label></td>
              </tr>
            </table>
              </p>
          </blockquote>
      </form>
	<blockquote>
	  <p>&nbsp;</p>
      </blockquote>
	<% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>
      <a href="create_simulation_introduction.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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