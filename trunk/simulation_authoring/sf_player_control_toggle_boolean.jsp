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
	
	PlayerControlToggleBoolean pctb = new PlayerControlToggleBoolean();
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("add_player_control"))){
	
		pctb.sim_id = pso.simulation.id;
		pctb.name = (String) request.getParameter("pc_name_bt");
		pctb.title = (String) request.getParameter("pc_title");
		pctb.description = (String) request.getParameter("pc_desc_bt");
		
		pctb.booleanVarSFid = (String) request.getParameter("select_boolean_var");
		
		pctb.setToTrueLabelMessage = (String) request.getParameter("true_state_words");
		pctb.setToFalseLabelMessage = (String) request.getParameter("false_state_words");
		
		pctb.tracked = (String) request.getParameter("mark_aar");
		
		debug = pctb.store();
		
	} // End of if 
	
		//////////////////////////////////
	// Put shared document on scratch pad
	String edit_pc = (String) request.getParameter("edit_pc");
	
	boolean inEditingMode = false;
	
	if ((edit_pc != null) && (edit_pc.equalsIgnoreCase("true"))){
		
		inEditingMode = true;
		
		pctb = new PlayerControlToggleBoolean();
		
		pctb.set_sf_id ((String) request.getParameter("pc_id"));
		
		pctb.load();
					
	}
	///////////////////////////////////////

	Vector simVars = new BooleanVariable().getSetForASimulation(pso.simulation.id);
	
	Vector simPCs = new PlayerControlToggleBoolean().getSetForASimulation(pso.simulation.id);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Control Page</title>
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(../Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">
<% String canEdit = (String) session.getAttribute("author"); %>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="666" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform </h1></td>
    <td align="right" background="../Templates/images/top_fade.png"> 

	  <div align="center">
	    <table border="0" cellspacing="4" cellpadding="4">
	<%  if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
        <tr>
          <td><a href="intro.jsp" target="_top">Home</a></td>
        </tr>
	<% } else { %>
		<tr>
          <td><a href="../simulation_facilitation/index.jsp" target="_top">Home </a></td>
        </tr>
	<% } %>	
        <tr>
          <td><a href="../simulation_user_admin/my_profile.jsp"> My Profile</a></td>
        </tr>
        <tr>
          <td><a href="logout.jsp" target="_top">Logout</a></td>
        </tr>
      </table>	  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"><% 
		
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		
		<table border="0" cellpadding="0" cellspacing="0" >
		   <tr>
		<td><a href="../simulation_planning/index.jsp" target="_top" class="menu_item">THINK</a></td>
		<td>&nbsp;</td>
	    <td><a href="creationwebui.jsp" target="_top" class="menu_item">CREATE</a></td>
		<td>&nbsp;</td>
		<td><a href="../simulation_facilitation/facilitateweb.jsp" target="_top" class="menu_item">PLAY</a></td>
		<td>&nbsp;</td>
        <td><a href="../simulation_sharing/index.jsp" target="_top" class="menu_item">SHARE</a></td>
		   </tr>
		</table>
	<% } %></td>
  </tr>
  <tr>
    <td colspan="2" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Add / Edit Toggle Boolean Player Control</h1>
    <!-- InstanceEndEditable --></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<BR />
<table width="90%" bgcolor="#FFFFFF" align="center" border="1" cellspacing="0" cellpadding="0">
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" --> 
      <%= Debug.getDebug(debug) %>
      <blockquote>
        <p>Current toggle boolean controls for the Simulation <%= pso.simulation.name %>:</p>
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
	PlayerControlToggleBoolean this_pc = (PlayerControlToggleBoolean) e.nextElement();
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
      <form name="form2" id="form2" method="post" action="sf_player_control_toggle_boolean.jsp">
        <input type="hidden" name="sending_page" value = "add_player_control">
        <blockquote> 
          <p>Player Control Name: 
            <input type="text" name="pc_name_bt" />
          </p>

          <table width="100%" border="0" cellspacing="2" cellpadding="1">
            <tr valign="top"> 
              <td>Boolean Variable: </td>
              <td><select name="select_boolean_var">
                  <% for (Enumeration e = simVars.elements(); e.hasMoreElements();){ 
				SimulationVariable this_sv = (SimulationVariable) e.nextElement();
				%>
                  <option value="<%= this_sv.get_sf_id() %>" selected="selected"><%= this_sv.name %></option>
                  <% } %>
                </select></td>
            </tr>
            <tr valign="top">
              <td>Title on Player Control Page</td>
              <td><input type="text" name="pc_title" /></td>
            </tr>
            <tr valign="top"> 
              <td width="32%">Introduction on Player Control Page</td>
              <td width="68%"> <textarea name="pc_desc_bt" cols="40" rows="5"></textarea></td>
            </tr>
            <tr valign="top"> 
              <td>Description of True State</td>
              <td><textarea name="true_state_words"></textarea></td>
            </tr>
            <tr valign="top"> 
              <td>Description of False State</td>
              <td><textarea name="false_state_words"></textarea></td>
            </tr>
            <tr valign="top"> 
              <td>Mark Changes in the AAR</td>
              <td><input type="radio" name="mark_aar" value="true">
                Yes / 
                <input type="radio" name="mark_aar" value="false">
                No</td>
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
      <!-- InstanceEndEditable -->
    <p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
