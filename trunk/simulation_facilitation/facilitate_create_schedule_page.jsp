<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.communications.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	Simulation simulation = afso.handleCreateSchedulePage(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.backPage);
		return;
	}
	
	RunningSimulation rs = afso.giveMeRunningSim();
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>


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
			<td width="100%"><br />
              <h1>Create Schedule Page <a href="helptext/create_schedule_help.jsp"  target="helpinright">(?)</a></h1>
              <br />
                    <% 
			if (afso.sim_id == null) {
		%>
                    <% } else { 
		
			if ((afso.getRunningSimId() == null)) {
		%>
                  <p>You must select the running simulation for which you will be creating a schedule.                  </p>
                  <p>
                    <% } else { %>
                  </p>
                  <blockquote>
                    <p><strong>Simulation</strong>: <%= simulation.getDisplayName() %></p>
		  <p><strong>Running Simulation</strong>: <%= rs.getRunningSimulationName() %></p>
                  <p> Below are notes from the simulation author on how he or she felt this simulation should be conducted.                  </p>
                  </blockquote>
                  <div align="center">
                    <table width="80%" border="1">
	                  <tr>
	                    <td><div align="center">
	                      <h2>Notes from the Simulation Author(s) </h2>
	                    </div></td>
                      </tr>
	                  <tr>
	      <td><%= simulation.getPlannedPlayIdeas() %></td>
	      </tr>
                    </table>
                  </div>
                  <blockquote>
                    <p>Below here, you can enter the specific schedule information for your students. This page can give them important information on when they should login, other game play details you want to stress to them, and set initial expectations.</p>
                  </blockquote>
                  <form action="facilitate_create_schedule_page.jsp" method="post" name="form2" id="form2">
                    <blockquote>
                      <p>
                        <textarea id="sim_schedule" name="sim_schedule" style="height: 120px; width: 480px;"><%= rs.getSchedule() %></textarea>
            
        <script language="javascript1.2">
			wysiwygWidth = 480;
			wysiwygHeight = 120;
  			generate_wysiwyg('sim_schedule');
		</script>
                      </p>
            <p> 
              <input type="hidden" name="sending_page" value="create_schedule" />
              
              <table width="100%"><tr>
              <td><input type="submit" name="command_save" value="Save" /></td>
              <td><input type="submit" name="command_save_and_proceed" value="Save and Proceed" /></td>
              </tr></table>
              </p>
          </blockquote>
                  </form>
                  <% } // end of if running_sim.id has been set. %>
                  <%
		
	}// end of if afso.simulation.id has been set.

%>
                  <blockquote>
                    <p><a href="facilitate_panel.jsp">To Simulation Launch Checklist</a> </p>
                    <p>&nbsp;</p>
                </blockquote></td>
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