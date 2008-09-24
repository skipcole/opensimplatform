<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*,org.usip.oscw.specialfeatures.*,org.usip.oscw.graphs.*" 
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
	
	Chart chart = new Chart();
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("add_sim_var"))){
	
		chart.sim_id = pso.simulation.id;
		chart.name = (String) request.getParameter("chart_name");

		chart.variableSFID = (String) request.getParameter("var_sf_id");
		chart.tab_heading = "chart of x";
		
		chart.title = (String) request.getParameter("chart_title");
		chart.type = (String) request.getParameter("chart_type");
		chart.xAxisTitle = (String) request.getParameter("chart_yaxis");
		chart.yAxisTitle = (String) request.getParameter("chart_xaxis");
		chart.setHeight ((String) request.getParameter("chart_height"));
		chart.setWidth ((String) request.getParameter("chart_width") );
		
		debug = chart.store();
	} // End of if 
	
		//////////////////////////////////
	// Put shared document on scratch pad
	String edit_sv = (String) request.getParameter("edit_sv");
	
	boolean inEditingMode = false;
	
	/*
	if ((edit_sv != null) && (edit_sv.equalsIgnoreCase("true"))){
		
		inEditingMode = true;
		
		sv = new IntegerVariable();
		
		sv.set_sf_id((String) request.getParameter("sf_id"));
		
		sv.load();
		
		
		if (sv.getVarType().equalsIgnoreCase("integer")){
			varInteger = "checked";
		}
					
	}
	*/
	///////////////////////////////////////

	Vector simCharts = new Chart().getSetForASimulation(pso.simulation.id);
	
	
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
      <h1>Add / Edit Line Chart</h1>
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
<p><%= Debug.getDebug(debug) %></p>
      <blockquote>
        <p>Current line charts for the Simulation <%= pso.simulation.name %>:</p>
        <blockquote>
          <p>
            <% if (simCharts.size() == 0) { %>
          </p>
        </blockquote>
        <ul>
          <li>None
            <p>
              <% } %>
              <% for (Enumeration e = simCharts.elements(); e.hasMoreElements();){ 
	Chart this_chart = (Chart) e.nextElement();
	%>
            </p>
          </li>
          <li><a href="sf_sim_linechart.jsp?edit_sv=true&amp;sf_id=<%= this_chart.get_sf_id() %>"><%= this_chart.name %></a>
		  <a href="delete_object.jsp?object_type=sf_chart&amp;objid=<%= this_chart.get_sf_id() %>&amp;backpage=sf_sim_linechart.jsp&amp;object_info=&quot;<%= this_chart.name %>&quot;"> 
              (Remove) <%= this_chart.name %> </a>
            <p>
              <% } %>
            </p>
          </li>
        </ul>
        <p>Add a Line Chart</p>
      </blockquote>
      <form name="form2" id="form2" method="post" action="sf_sim_linechart.jsp">
        <input type="hidden" name="sending_page" value="add_sim_var">
        <table width="80%" border="0" cellspacing="2" cellpadding="1">
          <tr valign="top"> 
            <td width="1%">&nbsp;</td>
            <td width="31%">Variable</td>
            <td width="68%"> <%
			Vector simVars = new IntegerVariable().getSetForASimulation(pso.simulation.id);
			%> <select name="var_sf_id">
                <% for (Enumeration e = simVars.elements(); e.hasMoreElements();){ 
					IntegerVariable this_int = (IntegerVariable) e.nextElement();  %>
                <option value="<%= this_int.get_sf_id() %>"><%= this_int.name %></option>
                <% } %>
              </select> </td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>Chart Name</td>
            <td><input name="chart_name" type="text" size="30" value="<%= chart.name %>" /></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>Chart Type</td>
            <td><select name="chart_type">
                <option value="LineChart" selected="selected">Line Chart</option>
              </select></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>Chart Title</td>
            <td><input type="text" name="chart_title" value="<%= chart.title %>" /></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>X Axis Title</td>
            <td><input type="text" name="chart_xaxis" value="<%= chart.xAxisTitle %>" /></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>Y Axis Title</td>
            <td><input type="text" name="chart_yaxis" value="<%= chart.yAxisTitle %>" /></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>Height</td>
            <td><input type="text" name="chart_height" value="<%= chart.height %>" /></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>Width</td>
            <td><input type="text" name="chart_width" value="<%= chart.width %>" /></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td> <% if (inEditingMode) { %>
              <% } else { %> <input type="submit" name="create_new" value="Submit" /> 
              <% } %></td>
            <td>&nbsp;</td>
          </tr>
        </table>
      </form>
      <p>&nbsp;</p>
      <p align="center"><a href="add_special_features.jsp">Back to Add Special 
        Features</a></p>
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
