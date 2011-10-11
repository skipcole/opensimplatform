<%@ page contentType="text/html; charset=UTF-8" language="java" 
import="java.io.*,java.util.*,
java.text.*,
java.sql.*,
org.usip.osp.baseobjects.*,
org.usip.osp.communications.*,
org.usip.osp.networking.*,
org.usip.osp.persistence.*" 
errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	afso.backPage = "../simulation_facilitation/play_panel.jsp";

	Simulation simulation = new Simulation();
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
	RunningSimulation running_simulation = RunningSimulation.getRSForInstructor(request, afso);
	
	boolean editMode = false;
	
	if (running_simulation.getId() != null) {
		editMode = true;	
	}
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



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
              <h1 align="center">Simulation Launch Checklist </h1>
			  
			  <blockquote>
              <% if (afso.sim_id != null) { %>
				<strong>Working on Simulation: <%= simulation.getDisplayName() %> </strong><br/>
              <% } %>
			  <% if (afso.getRunningSimId() != null) { %>
				<strong>Working on Running Simulation: <%= running_simulation.getRunningSimulationName() %></strong><br/>
              <% } %>
			  
                
                
                <p>&nbsp;</p>
                <table width="100%" border="0" cellspacing="2" cellpadding="1">
        
        <%
				int stepIndex = 1;
		%>
        
        <% if (editMode) { %>
         <tr valign="top"> 
          <td>
			<%= stepIndex %>. </td>
            <td><a href="facilitate_change_running_sim_name.jsp?rs_id=<%= running_simulation.getId() %>">Edit Running Simulation</a> <a href="helptext/create_running_sim_help.jsp" target="helpinright">(?)</a></td>
            </tr>
          <% } // End of if in edit mode %>
         <%
		 	stepIndex += 1;
		 %>
        <tr valign="top">
          <td><%= stepIndex %>.</td>
            <td><a href="facilitate_create_schedule_page.jsp">Create Schedule Page</a> <a href="helptext/create_schedule_help.jsp"  target="helpinright">(?)</a></td>
          </tr>
          
          <%
		  List starterDocs = SharedDocument.getAllStarterBaseDocumentsForSim(afso.schema, afso.sim_id, afso.getRunningSimId());
		  
		  
		  System.out.println("rs id is " + afso.getRunningSimId());
		  
		  int starterDocIndex = 0;
		  
		  for (ListIterator li = starterDocs.listIterator(); li.hasNext();) {
			SharedDocument sd = (SharedDocument) li.next();
			
			stepIndex += 1;
		  %>
        <tr valign="top"> 
          <td><%= stepIndex %>.</td>          
          <td><a href="facilitate_write_starter_document.jsp?sendingDocId=true&doc_id=<%= sd.getId() %>&starterDocIndex=<%= starterDocIndex %>">Edit Starter Doc : <%= sd.getDisplayTitle() %> </a></td>
        </tr>
          <%
		  	++starterDocIndex;
		   } %>
        <%
		 	stepIndex += 1;
		 %>
        <tr valign="top"> 
          <td><%= stepIndex %>.</td>
            <td><a href="facilitate_assign_user_to_simulation.jsp">Assign Players</a> <a href="helptext/assign_players_help.jsp" target="helpinright">(?)</a> </td>
            </tr>
        <%
		 	stepIndex += 1;
		 %>
        <tr valign="top"> 
          <td><%= stepIndex %>.</td>
            <td><a href="facilitate_enable_simulation.jsp">Enable/Disable Simulation</a> <a href="helptext/enable_sim_help.jsp" target="helpinright">(?)</a></td>
            </tr>
        <%
		 	stepIndex += 1;
		 %>
        <tr valign="top">
          <td><%= stepIndex %>.</td>
          <td><a href="facilitate_email_notifications.jsp">Notify Players via Email</a><a href="helptext/email_notify_help.jsp" target="helpinright"> (?) </a></td>
        </tr>
      </table>
</blockquote>
      <p align="center"></p>			</td>
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
