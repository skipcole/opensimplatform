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
	
	BudgetVariable sbv = new BudgetVariable();
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("add_sim_var"))){
	
		sbv.sim_id = pso.simulation.id;
		sbv.name = (String) request.getParameter("var_name");
		sbv.value = (String) request.getParameter("initial_value");
		sbv.description = (String) request.getParameter("var_description");
		
		String bud_accumulates = (String) request.getParameter("bud_accumulates");
		
		if ((bud_accumulates != null) && (bud_accumulates.equalsIgnoreCase("yes"))){
			sbv.accumulates = true;
		} else {
			sbv.accumulates = false;
		}
		
		debug = sbv.store();
	} // End of if 
	
		//////////////////////////////////
	// Put shared document on scratch pad
	String edit_sv = (String) request.getParameter("edit_sv");
	
	boolean inEditingMode = false;
	
	
	if ((edit_sv != null) && (edit_sv.equalsIgnoreCase("true"))){
		
		inEditingMode = true;
		
		sbv = new BudgetVariable();
		
		sbv.set_sf_id((String) request.getParameter("sf_id"));
		
		sbv.load();
	}
	///////////////////////////////////////

	Vector simVars = new BudgetVariable().getSetForASimulation(pso.simulation.id);
	
	String trueSelected = "";
	String falseSelected = "selected";
	
	if (sbv.value.equalsIgnoreCase("true")){
		trueSelected = "selected";
		falseSelected = "";
	}
	
	
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
              <h1>Add / Edit Budget Variable</h1>
              <br />
    <p><%= Debug.getDebug(debug) %></p>
    <p>&nbsp;</p>
    <blockquote>
      <p>Current budgets for the Simulation <%= pso.simulation.name %>:</p>
          <blockquote>
            <p>
              <% if (simVars.size() == 0) { %>
              </p>
          </blockquote>
          <ul>
            <li>None
              <p>
                <% } %>
                <% for (Enumeration e = simVars.elements(); e.hasMoreElements();){ 
			BudgetVariable this_sv = (BudgetVariable) e.nextElement();
	%>
                </p>
            </li>
            <li><a href="sf_sim_var_budget.jsp?edit_sv=true&amp;sf_id=<%= this_sv.get_sf_id() %>"><%= this_sv.name %></a>
              <p>
                <% } %>
                </p>
            </li>
          </ul>
          <p>Add a budget</p>
      </blockquote>
    <form name="form2" id="form2" method="post" action="sf_sim_var_budget.jsp">
      <input type="hidden" name="sending_page" value="add_sim_var">
      <table width="80%" border="0" cellspacing="2" cellpadding="1">
        <tr valign="top">
          <td width="1%">&nbsp;</td>
              <td width="42%">Budget Name</td>
              <td width="57%"> <input name="var_name" type="text" size="20" value="<%= sbv.name %>" />                </td>
            </tr>
        <tr valign="top">
          <td>&nbsp;</td>
              <td>Budget Description</td>
              <td><textarea name="var_description" cols="20" rows="2"><%= sbv.description %></textarea></td>
            </tr>
        <tr valign="top">
          <td>&nbsp;</td>
              <td>Starting Value</td>
              <td> <input type="text" name="initial_value" value="<%= sbv.value %>" /></td>
            </tr>
        <tr valign="top">
          <td>&nbsp;</td>
              <td>Budget Accumulates</td>
              <td><input name="bud_accumulates" type="radio" value="yes" <% if (sbv.accumulates) { %> checked <% } %> />
                Yes / 
                <input type="radio" name="bud_accumulates" value="no" <% if (!(sbv.accumulates)) { %> checked <% } %> />
                No</td>
            </tr>
        <tr valign="top">
          <td>&nbsp;</td>
              <td> <% if (inEditingMode) { %> <input type="submit" name="edit_sim_var" value="Update" /> 
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
