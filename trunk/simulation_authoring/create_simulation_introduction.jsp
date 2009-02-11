<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	pso.backPage = "create_simulation_introduction.jsp";
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
	// Determine if setting sim to edit.
	String sending_page = (String) request.getParameter("sending_page");
	
	String sim_intro = (String) request.getParameter("sim_intro");
	String enter_intro = (String) request.getParameter("enter_intro");
	
	if ( (sending_page != null) && (enter_intro != null) && (sending_page.equalsIgnoreCase("create_sim_intro"))){
		
		simulation.setIntroduction(sim_intro);
		simulation.saveMe(pso.schema);

	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

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
              <h1>Enter Simulation Introduction</h1>
              <br />
    <% 
			if (pso.simulationSelected) {
		%>
	  <p>Enter the introduction for the simulation <strong><%= simulation.getDisplayName() %></strong>.<br>
	    (If you would like to work on a different simulation, <a href="select_simulation.jsp">click 
	      here</a>.)</p>
	  <form action="create_simulation_introduction.jsp" method="post" name="form2" id="form2">
	    <blockquote>
	      <p>
	        <textarea id="sim_intro" name="sim_intro" style="height: 710px; width: 710px;"><%= simulation.getIntroduction() %></textarea>
	        <script language="javascript1.2">
  			generate_wysiwyg('sim_intro');
		</script>
	        </p>
            <p> 
              <input type="hidden" name="sending_page" value="create_sim_intro" />
              <input type="submit" name="enter_intro" value="Submit" />
              </p>
          </blockquote>
      </form>
	  <blockquote>
	    <p>&nbsp;</p>
      </blockquote>
	  <p align="center"><span class="style1">Please remember to save changes before leaving this page.</span></p>
	  <p align="center"><a href="create_simulation_planned_play_ideas.jsp">Next Step: Enter Planned Play Ideas </a></p>
	  <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>
      <a href="create_simulation_audience.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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