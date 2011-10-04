<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*" 
	errorPage="/error.jsp" %>
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
	
	BooleanVariable sbv = new BooleanVariable();
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("add_sim_var"))){
	
		sbv.sim_id = afso.simulation.id;
		sbv.name = (String) request.getParameter("var_name");
		sbv.initialValue = (String) request.getParameter("start_value");
		sbv.description = (String) request.getParameter("var_description");
		
		debug = sbv.store();
	} // End of if 
	
		//////////////////////////////////
	// Put shared document on scratch pad
	String edit_sv = (String) request.getParameter("edit_sv");
	
	boolean inEditingMode = false;
	
	
	if ((edit_sv != null) && (edit_sv.equalsIgnoreCase("true"))){
		
		inEditingMode = true;
		
		sbv = new BooleanVariable();
		
		sbv.set_sf_id((String) request.getParameter("sf_id"));
		
		sbv.load();
					
	}
	///////////////////////////////////////

	Vector simVars = new BooleanVariable().getSetForASimulation(afso.simulation.id);
	
	String trueSelected = "";
	String falseSelected = "selected";
	
	if (sbv.value.equalsIgnoreCase("true")){
		trueSelected = "selected";
		falseSelected = "";
	}
	
	String trackYesChecked = "";
	String trackNoChecked = "";
	
	if (sbv.tracked.equalsIgnoreCase("false")){
			trackNoChecked = "checked";
	}
	
	if (sbv.tracked.equalsIgnoreCase("true")){
			trackYesChecked = "checked";
	}
	
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
              <h1>Add / Edit Boolean Variable</h1>
              <br />
    <p><%= Debug.getDebug(debug) %></p>
    <p>&nbsp;</p>
    <blockquote>
      <p>Current boolean variables for the Simulation <%= afso.simulation.name %>:</p>
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
			BooleanVariable this_sv = (BooleanVariable) e.nextElement();
	%>
                </p>
            </li>
            <li><a href="sf_sim_var_boolean.jsp?edit_sv=true&amp;sf_id=<%= this_sv.get_sf_id() %>"><%= this_sv.name %></a>
              <p>
                <% } %>
                </p>
            </li>
          </ul>
          <p>Add a boolean simulation variable</p>
      </blockquote>
    <form name="form2" id="form2" method="post" action="sf_sim_var_boolean.jsp">
      <blockquote>
        <p>
          <input type="hidden" name="sending_page" value="add_sim_var">
          </p>
          </blockquote>
          <table width="80%" border="0" cellspacing="2" cellpadding="1">
            <tr valign="top"> 
              <td width="43%">Variable 
                Name</font><a href="../helptext/sim_boolean_variable.jsp" target="helpinright">(?)</a></td>
              <td width="57%"> <input name="var_name" type="text" size="20" value="<%= sbv.name %>" />                </td>
            </tr>
            <tr valign="top"> 
              <td>Variable 
                Description</font><a href="../helptext/sim_boolean_variable.jsp" target="helpinright">(?)</a></td>
              <td><textarea name="var_description" cols="40" rows="3"></textarea></td>
            </tr>
            <tr valign="top"> 
              <td>Starting 
                Value</font><a href="../helptext/sim_boolean_variable.jsp" target="helpinright">(?)</a></td>
              <td> <select name="start_value">
                <option value="true" >true</option>
                <option value="false" >false</option>
                </select> </td>
            </tr>
            <tr valign="top"> 
              <td> <% if (inEditingMode) { %> <input type="submit" name="edit_sim_var" value="Update" /> 
                <% } else { %> <input type="submit" name="create_new" value="Submit" /> 
                <% } %></td>
              <td>&nbsp;</td>
            </tr>
            </table>
      </form>
    <blockquote>
      <p>&nbsp;</p>
      </blockquote>
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
