<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if ((afso.simulation.id == null) || (afso.simulation.id.equalsIgnoreCase(""))){
		afso.errorMsg = "<p><font color=red> You must first select the sim you want to add this special feature to.</font></p>";
		response.sendRedirect("add_special_features.jsp");
		return;
	}
	
	// Determine if setting sim to edit.
	String sending_page = (String) request.getParameter("sending_page");
	String create_new = (String) request.getParameter("create_new");
		
	String debug = "";
	
	SimulationVariable sv = new SimulationVariable();
	
	//String has_chart = (String) request.getParameter("has_chart");
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("add_sim_var"))){
	
		sv.sim_id = afso.simulation.id;
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

	Vector simVars = new SimulationVariable().getSetForASimulation(afso.simulation.id);
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Open Simulation Platform Control Page</title>



<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Add / Edit Simulation Variable</h1>
              <br />
    <p><%= Debug.getDebug(debug) %></p>
    <p>A 'simulation variable' is something that is tracked throughout the simulation 
      can be changed either directly or indirectly due to the players actions. </p>
    <p>Some examples of simulation variables could be the GDP of a country, or the 
      number of rats in a city.</p>
    <p>Current simulation variables for the Simulation <%= afso.simulation.name %>:</p>
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
    <form name="form2" id="form2" method="post" action="sf_sim_variable.jsp">
      <input type="hidden" name="sending_page" value="add_sim_var">
      
      <table width="80%" border="0" cellspacing="2" cellpadding="1">
        <tr valign="top"> 
          <td width="32%">Variable Name</td>
        <td width="68%"> <input name="var_name" type="text" size="80" value="<%= sv.name %>" />          </td>
      </tr>
        <tr valign="top"> 
          <td width="32%">Variable Type</td>
              <td width="68%"> <ul>
                <li> 
                  <input type="radio" name="var_type" value="boolean" />
                  Boolean (coming soon)</li>
                  <li> 
                    <input type="radio" name="var_type" value="budget" />
                    Budget<a href="../helptext/sim_budget_variable.jsp" target="helpinright">(?)</a><br />
                    Budget Units 
                    <input type="text" name="textfield" />
                    (Dollars, Workers, etc.)</li>
                  <li> 
                    <input name="var_type" type="radio" value="integer" <%= varInteger %> />
                    Integer</li>
                </ul></td>
      </tr>
        <tr valign="top"> 
          <td>Starting Value</td>
        <td>
          <input type="text" name="start_value" value="<%= sv.initialValue %>" />
          
          <select name="start_value">
            <option value="true" selected="selected">true</option>
            <option value="false">false</option>
            </select> </td>
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
        <td>
          <select name="prop_type">
            <option value="fibonacci">Fibonacci</option>
            <option value="player_set">Player Set</option>
            </select></td>
      </tr>
        <tr> 
          <td>Create Chart to Track Variable?</td>
        <td><input name="has_chart" type="radio" value="yes" checked="checked"  />
          Yes / 
          <input type="radio" name="has_chart" value="no" />
          No</td>
      </tr>
        <tr> 
          <td>&nbsp;</td>
        <td><table width="100%" border="0" cellspacing="2" cellpadding="1">
          <tr> 
            <td>Chart Type</td>
              <td><select name="chart_type">
                <option value="LineChart" selected="selected">Line Chart</option>
                </select></td>
            </tr>
          <tr> 
            <td>Chart Title</td>
              <td><input type="text" name="chart_title" value="<%= sv.chart.title %>" /></td>
            </tr>
          <tr> 
            <td>X Axis Title</td>
              <td><input type="text" name="chart_xaxis" value="<%= sv.chart.xAxisTitle %>" /></td>
            </tr>
          <tr> 
            <td>Y Axis Title</td>
              <td><input type="text" name="chart_yaxis" value="<%= sv.chart.yAxisTitle %>" /></td>
            </tr>
          <tr> 
            <td>Height</td>
              <td><input type="text" name="chart_height" value="<%= sv.chart.height %>" /></td>
            </tr>
          <tr>
            <td>Width</td>
              <td><input type="text" name="chart_width" value="<%= sv.chart.width %>" /></td>
            </tr>
          </table></td>
      </tr>
        <tr> 
          <td> <% if (inEditingMode) { %>
            <input type="submit" name="edit_sim_var" value="Update" />
            <% } else { %> <input type="submit" name="create_new" value="Submit" /> 
            <% } %></td>
        <td>&nbsp;</td>
      </tr>
        </table>
    </form>
    <p>&nbsp;</p>
    <p align="center"><a href="../incorporate_underlying_model.jsp">Back to Add Special Features</a></p>    <p>&nbsp;</p>			</td>
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
