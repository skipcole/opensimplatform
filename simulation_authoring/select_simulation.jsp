<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.persistence.*,org.hibernate.*" 
	errorPage="../error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = "select_simulation.jsp";

	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	//////////////////////////////////
	// Get list of all simulations
	List simList = Simulation.getAll(afso.schema);

	afso.handleSelectSimulation(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect("control_panel.jsp");
		return;
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



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
              <h1>Select Simulation </h1>
              <br />
			 
      <blockquote>
        <p>On this page you may take the following actions on the simulations listed below. If you need to create a new simulation, <a href="create_simulation.jsp">click here</a>. </p>
          <ol>
            <li>Edit. If you have permission to edit the simulation, and it has not been published and hence locked from editing, you will be able to select the simulation to edit it.</li>
            <li>Review. You will be able to review a simulation here.</li>
            <!-- li>Copy. This will allow you to create a copy of the simulation.</li -->
            <li>Rename. This allows authors who can edit the simulation to change basic  information: name, version and name of creating organization.</li>
            <!-- li>Delete. This will remove the simulation from this platform. If the simulation has running games created on this platform with users assigned to it. It will not be able to delete it. </li -->
          </ol>
          <table width="100%">
		  <tr>
              <td colspan="5"><hr /></td>
            </tr>
            <%
		
		for (ListIterator li = simList.listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
			String nameToSend = java.net.URLEncoder.encode(sim.getSimulationName() + " version " + sim.getVersion());
			
			int canEdit = SimEditAuthorization.checkAuthorizedToEdit(afso.schema, sim.getId(), afso.user_id);
		%>
            
            <tr> 
              <td colspan="5"><strong><%= sim.getSimulationName() %> version <%= sim.getVersion() %></strong></td>
                    </tr>
            <tr>
              <td width="5%" align="right">&nbsp;</td>
                    <td align="right">
						<form action="select_simulation.jsp" method="get">
							<input type="hidden" name="select_sim" value="true" />
							<input type="hidden" name="sim_id" value="<%= sim.getId() %>" />
							<input type="submit" name="edit" value="Edit"  <%= SimEditAuthorization.getDisabledCode(canEdit) %> />
                    	</form>
					</td>
                    <td align="right">
                      <form id="form1" name="form1" method="post" action="review_sim.jsp">
                        <input type="hidden" name="loadSim" value="true" />
                        <input type="hidden" name="sim_id" value="<%= sim.getId() %>" />
                        <input type="submit" name="button" id="button" value="Review" />
                      </form></td>
					<!-- td align="right">
						<form action="copy_simulation.jsp" method="get">
							<input type="hidden" name="sim_id" value="<%= sim.getId() %>" />
							<input type="submit" name="copy" value="Create Copy" />
						</form>
					</td -->
                    <td align="right">
						<form action="rename_simulation.jsp" method="get">
							<input type="hidden" name="sim_id" value="<%= sim.getId() %>" />
							<input type="submit" name="rename" value="Rename" <%= SimEditAuthorization.getDisabledCode(canEdit) %> />
						</form></td>
                    <!-- td align="right">
                      <form action="delete_object.jsp" method="POST">
                        <input type="hidden" name="object_type" value="simulation" />
                        <input type="hidden" name="object_info" value="<%= nameToSend %>" />
                        <input type="hidden" name="objid" value="<%= sim.getId() %>" />
                        <input type="submit" name="delete" value="Delete" />
                        </form></td -->
                  </tr>
            <tr>
              <td colspan="5"><hr /></td>
                    </tr>
            <%
	}
%>
            </table>
      </blockquote>      
      <p align="center">&nbsp;</p>			</td>
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
