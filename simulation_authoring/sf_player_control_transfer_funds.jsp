<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*,org.usip.oscw.specialfeatures.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if ((pso.simulation.id == null) || (pso.simulation.id.equalsIgnoreCase(""))){
		pso.errorMsg = "<p><font color=red> You must first select the sim you want to add this special feature to.</font></p>";
		response.sendRedirect("add_special_features.jsp");
		return;
	}
	
	// Determine if setting sim to edit.
	String sending_page = (String) request.getParameter("sending_page");
	String create_new = (String) request.getParameter("create_new");
		
	String debug = "";
	
	PlayerControlBudgetTransfer pc = new PlayerControlBudgetTransfer();
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("add_player_control"))){
	
		pc.sim_id = pso.simulation.id;
		pc.name = (String) request.getParameter("pc_name_bt");
		pc.description = (String) request.getParameter("pc_desc_bt");
		
		pc.fromAcctString = (String) request.getParameter("select_from_acct");
		pc.toAcctString = (String) request.getParameter("select_to_acct");
		
		debug = pc.store();
	} // End of if 
	
		//////////////////////////////////
	// Put shared document on scratch pad
	String edit_pc = (String) request.getParameter("edit_pc");
	
	boolean inEditingMode = false;
	
	if ((edit_pc != null) && (edit_pc.equalsIgnoreCase("true"))){
		
		inEditingMode = true;
		
		pc = new PlayerControlBudgetTransfer();
		
		pc.set_sf_id ((String) request.getParameter("pc_id"));
		
		pc.load();
					
	}
	///////////////////////////////////////

	Vector simVars = new BudgetVariable().getSetForASimulation(pso.simulation.id);
	
	Vector simPCs = new PlayerControlBudgetTransfer().getSetForASimulation(pso.simulation.id);
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Control Page</title>
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_oscw.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="80%" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Add / Edit Transfer Funds Player Control</h1>
    <!-- InstanceEndEditable --></td>
    <td width="20%" align="right" valign="top"> 
		<% 
		String canEdit = (String) session.getAttribute("author");
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		<a href="../simulation_planning/index.jsp" target="_top">Think</a><br>
	    <a href="creationwebui.jsp" target="_top">Create</a><br>
		<a href="../simulation_facilitation/facilitateweb.jsp" target="_top">Play</a><br>
        <a href="../simulation_sharing/index.jsp" target="_top">Share</a>
		<% } %>
		</td>
  </tr>
</table>
<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
</tr>
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" --> 
      <%= Debug.getDebug(debug) %>
      <blockquote>
        <p>Current transfer fund controls for the Simulation <%= pso.simulation.name %>:</p>
        <blockquote>
          <p>
            <% if (simPCs.size() == 0) { %>
          </p>
        </blockquote>
        <ul>
          <li>None 
            <p> 
              <% } %>
              <% for (Enumeration e = simPCs.elements(); e.hasMoreElements();){ 
	PlayerControlBudgetTransfer this_pc = (PlayerControlBudgetTransfer) e.nextElement();
	%>
            </p>
          </li>
          <li><a href="sf_player_control_transfer_funds.jsp?edit_sv=true&amp;sf_id=<%= this_pc.get_sf_id() %>"><%= this_pc.name %></a> 
            <p> 
              <% } %>
            </p>
            
          </li>
        </ul>
      </blockquote>
      <form name="form2" id="form2" method="post" action="sf_player_control_transfer_funds.jsp">
        <input type="hidden" name="sending_page" value = "add_player_control">
        <blockquote> Player Control Name: 
          <input type="text" name="pc_name_bt" />
          <p>First select the variable which will be controlled or modified.</p>
          <table width="80%" border="0" cellspacing="2" cellpadding="1">
            <tr>
              <td>Transfer From Account</td>
              <td>Transfer To Account</td>
            </tr>
            <tr>
              <td><select name="select_from_acct">
                  <% for (Enumeration e = simVars.elements(); e.hasMoreElements();){ 
				SimulationVariable this_sv = (SimulationVariable) e.nextElement();
				%>
                  <option value="<%= this_sv.get_sf_id() %>" selected="selected"><%= this_sv.name %></option>
                  <% } %>
                </select></td>
              <td><select name="select_to_acct">
                  <% for (Enumeration e = simVars.elements(); e.hasMoreElements();){ 
				SimulationVariable this_sv = (SimulationVariable) e.nextElement();
				%>
                  <option value="<%= this_sv.get_sf_id() %>" selected="selected"><%= this_sv.name %></option>
                  <% } %>
                </select></td>
            </tr>
          </table>
          <br />
          <table width="100%" border="0" cellspacing="2" cellpadding="1">
            <tr valign="top"> 
              <td width="32%">Introduction on Player Control Page</td>
              <td width="68%"> <textarea name="pc_desc_bt" cols="40" rows="5"></textarea></td>
            </tr>
          </table>
          <p>
            <input type="submit" name="Submit2" value="Submit" />
          </p>
          <p>&nbsp; </p>
        </blockquote>
      </form>
      <p align="center"><a href="add_special_features.jsp">Back to Add Special Features</a></p>
	  <p>&nbsp;</p>
      <!-- InstanceEndEditable --></td>
  </tr>
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
</table>

<p>&nbsp;</p>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td align="left" valign="bottom"> 
	<% 
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
	<a href="intro.jsp" target="_top">Home 
      </a>
	  <% } else { %>
	  <a href="../simulation_facilitation/index.jsp" target="_top">Home 
      </a>
	  <% } %>
	  </td>
    <td align="right" valign="bottom"><a href="../simulation_user_admin/my_profile.jsp">My 
      Profile</a> </td>
  </tr>
  <tr>
    <td align="left" valign="bottom"><a href="logout.jsp" target="_top">Logout</a></td>
    <td align="right" valign="bottom">&nbsp;</td>
  </tr>
</table>
<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP Open Source Software Project</a>. </p>
</body>
<!-- InstanceEnd --></html>
