<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	pso.backPage = "create_simulation_audience.jsp";
	
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

	String sim_audience = (String) request.getParameter("sim_audience");
	String enter_sim_audience = (String) request.getParameter("enter_sim_audience");
	
	if ( (sending_page != null) && (enter_sim_audience != null) && (sending_page.equalsIgnoreCase("enter_sim_audience"))){

		simulation.setAudience(sim_audience);
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
              <h1>Enter Simulation Audience</h1>
              <br />
      <blockquote>
        <% 
			if (pso.sim_id != null) {
		%>
        <p>Enter your learning audience for the simulation <strong><%= simulation.getDisplayName() %></strong>.<br>
          (If you would like to work on a different simulation, <a href="select_simulation.jsp">click 
            here</a>.)</p>
      </blockquote>
      <form action="create_simulation_audience.jsp" method="post" name="form2" id="form2">
        <blockquote>
          <p>
            <textarea id="sim_audience" name="sim_audience" style="height: 710px; width: 710px;"><%= simulation.getAudience() %></textarea>
            
            <script language="javascript1.2">
  			generate_wysiwyg('sim_audience');
		</script>
            </p>
            <p> 
              <input type="hidden" name="sending_page" value="enter_sim_audience" />
              <input type="submit" name="enter_sim_audience" value="Save" />
              </p>
          </blockquote>
      </form>
      <p align="center" class="style1">Please remember to save changes before leaving this page.</p>
      <p align="center"><a href="create_simulation_introduction.jsp">Next Step: Enter Simulation Introduction </a></p>
      <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>      <p><a href="create_simulation_objectives.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a> </p>			</td>
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
