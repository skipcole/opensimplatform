<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	// Determine if setting sim to edit.
	String sending_page = (String) request.getParameter("sending_page");
	String create_new = (String) request.getParameter("create_new");
		
	String debug = "";
	
	SimulationVariable sv = new SimulationVariable();
	
	//String has_chart = (String) request.getParameter("has_chart");
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("add_sim_var"))){
	
		sv.sim_id = pso.simulation.id;
		sv.name = (String) request.getParameter("var_name");
		sv.setVarType((String) request.getParameter("var_type"));
		sv.propagation_type = (String) request.getParameter("prop_type");
		
		
		String has_chart = (String) request.getParameter("has_chart");
		
		if ((has_chart != null) && (has_chart.equalsIgnoreCase("yes"))){
			sv.hasChart = true;
			sv.chart.title = (String) request.getParameter("chart_title");
			sv.chart.type = (String) request.getParameter("chart_type");
			sv.chart.xAxisTitle = (String) request.getParameter("chart_yaxis");
			sv.chart.yAxisTitle = (String) request.getParameter("chart_xaxis");
			sv.chart.setHeight ((String) request.getParameter("chart_height"));
			sv.chart.setWidth ((String) request.getParameter("chart_width") );
		}
		
		debug = sv.store();
	} // End of if 
	
		//////////////////////////////////
	// Put shared document on scratch pad
	String edit_sv = (String) request.getParameter("edit_sv");
	
	boolean inEditingMode = false;
	
	String varBoolean = "";
	String varDecimal = "";
	String varInteger = "";
	
	
	if ((edit_sv != null) && (edit_sv.equalsIgnoreCase("true"))){
		
		inEditingMode = true;
		
		sv = new SimulationVariable();
		
		sv.set_sf_id((String) request.getParameter("sf_id"));
		
		sv.load();
		
		
		if (sv.getVarType().equalsIgnoreCase("integer")){
			varInteger = "checked";
		}
					
	}
	///////////////////////////////////////

	Vector simVars = new SimulationVariable().getSetForASimulation(pso.simulation.id);
	
	
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

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform</h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
	    <table border="0" cellspacing="1" cellpadding="0">
	<%  if (pso.isAuthor()) { %>
        <tr>
          <td><div align="center"><a href="intro.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } else if (pso.isFacilitator()) { %>
		<tr>
          <td><div align="center"><a href="../simulation_facilitation/instructor_home.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } %>	
        <tr>
          <td><div align="center"><a href="../simulation_user_admin/my_profile.jsp" class="menu_item"><img src="../Templates/images/my_profile.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
        <tr>
          <td><div align="center"><a href="logout.jsp" target="_top" class="menu_item"><img src="../Templates/images/logout.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
      </table>	  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"><% 
		
		String bgColor_think = "#475DB0";
		String bgColor_create = "#475DB0";
		String bgColor_play = "#475DB0";
		String bgColor_share = "#475DB0";
		
		pso.findPageType(request);
		
		if (pso.page_type == 1){
			bgColor_think = "#9AABE1";
		} else if (pso.page_type == 2){
			bgColor_create = "#9AABE1";
		} else if (pso.page_type == 3){
			bgColor_play = "#9AABE1";
		} else if (pso.page_type == 4){
			bgColor_share = "#9AABE1";
		}
		
		if (pso.isAuthor()) { %>
		
		<table border="0" cellpadding="0" cellspacing="0" >
		   <tr>
		<td bgcolor="<%= bgColor_think %>"><a href="../simulation_planning/index.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;THINK&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
	    <td bgcolor="<%= bgColor_create %>"><a href="creationwebui.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;CREATE&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
		<td bgcolor="<%= bgColor_play %>"><a href="../simulation_facilitation/facilitateweb.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;PLAY&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
        <td bgcolor="<%= bgColor_share %>"><a href="../simulation_sharing/index.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;SHARE&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		   </tr>
		</table>
	<% } %></td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top"></td>
    <td colspan="1" valign="top"></td>
    <td width="194" align="right" valign="top"></td>
  </tr>
</table>
<BR />
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Add / Edit Simulation Variable</h1>
    <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
<p><%= Debug.getDebug(debug) %></p>
      <p>&nbsp;</p>
<p>Current simulation variables for the Simulation <%= pso.simulation.name %>:</p>

<ul>
<% if (simVars.size() == 0) { %>
  <li>None</li>
  <% } %>
<% for (Enumeration e = simVars.elements(); e.hasMoreElements();){ 
	SimulationVariable this_sv = (SimulationVariable) e.nextElement();
	%>
	<li><a href="sf_sim_variable.jsp?edit_sv=true&amp;sf_id=<%= this_sv.get_sf_id() %>"><%= this_sv.name %></a></li>
	<% } %>
</ul>

<p>Add a simulation variable</p>
    <form name="form2" id="form2" method="post" action="temp_budget.jsp">
	<input type="hidden" name="sending_page" value="add_sim_var">
      
        <table width="80%" border="0" cellspacing="2" cellpadding="1">
          <tr valign="top"> 
            <td width="32%">Variable Name</td>
            <td width="68%"> <input name="var_name" type="text" size="80" value="<%= sv.name %>" /> 
            </td>
          </tr>
          <tr valign="top"> 
            <td width="32%">Variable Type</td>
            <td width="68%"> <ul>
                <li> 
                  <input type="radio" name="var_type" value="boolean" disabled />
                  Boolean (coming soon)</li>
                <li> 
                  <input type="radio" name="var_type" value="budget" />
                  Budget (A budget is anything that can be pulled from or paid 
                  into.)<br />
                  Budget Units 
                  <input type="text" name="textfield" />
                </li>
                <li> 
                  <input type="radio" name="var_type" value="decimal" disabled />
                  Decimal (coming soon)</li>
                <li> 
                  <input name="var_type" type="radio" value="integer" <%= varInteger %> />
                  Integer</li>
              </ul></td>
          </tr>
          <tr valign="top"> 
            <td>Starting Value</td>
            <td><input type="text" name="start_value" value="<%= sv.initialValue %>" /></td>
          </tr>
          <tr valign="top"> 
            <td>Max Value</td>
            <td><input name="has_max_value" type="checkbox" value="checkbox" checked="checked" />
              None or 
              <input type="text" name="max_value" /></td>
          </tr>
          <tr valign="top"> 
            <td>Min Value</td>
            <td><input name="has_min_value" type="checkbox" value="checkbox" checked="checked" />
              None or 
              <input type="text" name="min_value" /></td>
          </tr>
          <tr valign="top"> 
            <td>Propagation Means</td>
            <td> <select name="prop_type">
                <option value="fibonacci">Fibonacci</option>
                <option value="player_set">Player Set</option>
				<option value="player_set">Sum Debits and Credits</option>
              </select></td>
          </tr>
          <tr> 
            <td> <% if (inEditingMode) { %> <input type="submit" name="edit_sim_var" value="Update" /> 
              <% } else { %> <input type="submit" name="create_new" value="Submit" /> 
              <% } %></td>
            <td>&nbsp;</td>
          </tr>
        </table>
    </form>
    <p>&nbsp;</p>
      <p align="center">&nbsp;</p>
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
