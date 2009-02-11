<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*" 
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
	
	PlayerControl pc = new PlayerControl();
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("add_player_control"))){
	
		pc.sim_id = pso.simulation.id;
		pc.name = (String) request.getParameter("pc_name");
		pc.description = (String) request.getParameter("pc_desc");
		String x = (String) request.getParameter("select_var");
		pc.intVar.set_sf_id(x);
		
		String dc_type = (String) request.getParameter("dc_type");
		if ((dc_type != null) && (dc_type.equalsIgnoreCase("set_variable"))){
			AllowableResponse ar1 = new AllowableResponse();
			ar1.response_type = AllowableResponse.RT_SET;
			ar1.controlText = (String) request.getParameter("dc_control_text");
			pc.allowableResponses = new Vector();
			pc.allowableResponses.add(ar1);
		}
		
		debug = pc.store();
	} // End of if 
	
		//////////////////////////////////
	// Put shared document on scratch pad
	String edit_pc = (String) request.getParameter("edit_pc");
	
	boolean inEditingMode = false;
	
	
	if ((edit_pc != null) && (edit_pc.equalsIgnoreCase("true"))){
		
		inEditingMode = true;
		
		pc = new PlayerControl();
		
		pc.set_sf_id ((String) request.getParameter("pc_id"));
		
		pc.load();
					
	}
	///////////////////////////////////////

	Vector simVars = new IntegerVariable().getSetForASimulation(pso.simulation.id);
	
	Vector simPCs = new PlayerControl().getSetForASimulation(pso.simulation.id);
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Control Page</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
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
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Add / Edit Player Direct Control</h1>
    <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
      <%= Debug.getDebug(debug) %>
      <blockquote>
        <p>This control allows a player direct access to change a variable.</p>
        <p>Current direct player controls for the Simulation <%= pso.simulation.name %>:</p>
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
	PlayerControl this_pc = (PlayerControl) e.nextElement();
	%>
            </p>
          </li>
          <li><a href="sf_player_control.jsp?edit_sv=true&amp;sf_id=<%= this_pc.get_sf_id() %>"><%= this_pc.name %></a> 
            <p> 
              <% } %>
              <br />
            </p>
          </li>
        </ul>
      </blockquote>
      <form name="form2" id="form2" method="post" action="sf_player_direct_control.jsp">
	  <input type="hidden" name="sending_page" value = "add_player_control">
        <blockquote> Player Control Name: 
          <input type="text" name="pc_name" />
          <p>First select the variable which will be controlled or modified.</p>
            <select name="select_var">
		  		<% for (Enumeration e = simVars.elements(); e.hasMoreElements();){ 
				SimulationVariable this_sv = (SimulationVariable) e.nextElement();
				%>
              <option value="<%= this_sv.get_sf_id() %>" selected="selected"><%= this_sv.name %></option>
			  <% } %>
            </select>
          <p>&nbsp;</p>
          <table width="100%" border="0" cellspacing="2" cellpadding="1">
            <tr valign="top"> 
              <td width="32%"> 
                <input type="radio" name="dc_type" value="radiobutton" disabled />
                Specific Values</td>
              <td width="68%">&nbsp;</td>
            </tr>
            <tr valign="top"> 
              <td> 
                <input name="dc_type" type="radio" value="set_variable" checked="checked" />
                Any Value</td>
              <td>Label seen by user: 
                <input type="text" name="dc_control_text" /></td>
            </tr>
            <tr valign="top"> 
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
            <tr valign="top"> 
              <td>Introduction on Player Control Page</td>
              <td> 
                <textarea name="pc_desc" cols="40" rows="5"></textarea></td>
            </tr>
          </table>
          <p> 
            <input type="submit" name="Submit2" value="Submit" />
          </p>
          <p>&nbsp; </p>
        </blockquote>
      </form>
      <p>&nbsp;</p>
	  <p align="center"><a href="incorporate_underlying_model.jsp">Back to Add Special Features</a></p>
	  <p>&nbsp;</p>
      <!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
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
<!-- InstanceEnd --></html>
