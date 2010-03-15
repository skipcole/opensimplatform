<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.communications.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	afso.backPage = "../simulation_facilitation/create_schedule_page.jsp";
	
	Simulation simulation = afso.handleCreateSchedulePage(request);
	
	RunningSimulation rs = afso.giveMeRunningSim();
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>


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
			<td width="100%"><br />
              <h1>Create Schedule Page</h1>
              <br />
                    <% 
			if (afso.sim_id == null) {
		%>
                  <p>You must first select the simulation which you will be enabling.<br />
                      
                  Please <a href="../simulation_authoring/select_simulation.jsp">click here</a> to select it, or <a href="../simulation_authoring/create_simulation.jsp">create a new one</a>.</p>
                  <% } else { 
		
			if ((afso.running_sim_id == null)) {
		%>
                  <p>You must select the running simulation for which you will be creating a schedule.<br />
                      
                  Please <a href="select_running_simulation.jsp">click here</a> to select it, or <a href="create_running_sim.jsp">create a new one</a>.</p>
                  <p>
                    <% } else { %>
                            </p>
                  <blockquote>
                    <p><strong>Simulation</strong>: <%= simulation.getDisplayName() %> (To select a different simulation, <a href="../simulation_authoring/select_simulation.jsp">click here</a>.)</p>
		  <p><strong>Running Simulation</strong>: <%= rs.getRunningSimulationName() %> (To select a different running simulation, <a href="../simulation_authoring/create_running_sim.jsp">click here</a>.)</p>
                    </blockquote>
                  <p> Below are notes from the simulation author on how he or she felt this simulation would be conducted.</p>
                  <p>Below that is a place where you can enter the specific schedule page for your students. This page will give them important information on when they can and should login and any other details. You will be able to give the players announcements during the simulation, but this page will set the initial expectations.</p>
                  <div align="center">
                    <table width="80%" border="1">
                      <caption>
                      Notes from the Simulation Author
                      </caption>
	    <tr>
	      <td><%= simulation.getPlanned_play_ideas() %></td>
	      </tr>
                    </table>
	                  </div>
                  <p>&nbsp;</p>
                  <form action="create_schedule_page.jsp" method="post" name="form2" id="form2">
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
              <input type="submit" name="command" value="Save" />
              </p>
          </blockquote>
                    </form>
                  <% } // end of if running_sim.id has been set. %>
                  <%
		
	}// end of if afso.simulation.id has been set.

%>
                  <blockquote>
                    <p>&nbsp;</p>
                    </blockquote>
                  <p align="center"><a href="bulk_invite.jsp">Next Step: Bulk Invite Users</a><a href="../simulation_authoring/create_simulation_phases.jsp"></a></p>
                  <a href="create_running_sim.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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