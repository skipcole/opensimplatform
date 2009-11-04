<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*,org.usip.osp.graphs.*" 
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
	
	Chart chart = new Chart();
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("add_sim_var"))){
	
		chart.sim_id = afso.simulation.id;
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

	Vector simCharts = new Chart().getSetForASimulation(afso.simulation.id);
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

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
              <h1>Add / Edit Line Chart</h1>
              <br />
    <p><%= Debug.getDebug(debug) %></p>
    <blockquote>
      <p>Current line charts for the Simulation <%= afso.simulation.name %>:</p>
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
              <a href="../delete_object.jsp?object_type=sf_chart&amp;objid=<%= this_chart.get_sf_id() %>&amp;backpage=sf_sim_linechart.jsp&amp;object_info=&quot;<%= this_chart.name %>&quot;"> 
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
			Vector simVars = new IntegerVariable().getSetForASimulation(afso.simulation.id);
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
    <p align="center"><a href="../incorporate_underlying_model.jsp">Back to Add Special 
      Features</a></p>    <p>&nbsp;</p>			</td>
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
