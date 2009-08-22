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
	
	BudgetVariable sbv = new BudgetVariable();
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("add_sim_var"))){
	
		sbv.sim_id = afso.simulation.id;
		sbv.name = (String) request.getParameter("var_name");
		//sbv.setValue((String) request.getParameter("initial_value"));
		sbv.description = (String) request.getParameter("var_description");
		
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

	Vector simVars = new BudgetVariable().getSetForASimulation(afso.simulation.id);
	
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
      <p>Budget triggers are a starting point to allow player actions affect their 
        universe.</p>
      <p>Current budget triggersfor the Simulation <%= afso.simulation.name %>:</p>
      <ul>
        <% if (simVars.size() == 0) { %>
        <li>None</li>
          <% } %>
        <% for (Enumeration e = simVars.elements(); e.hasMoreElements();){ 
			BudgetVariable this_sv = (BudgetVariable) e.nextElement();
	%>
        <li><a href="sf_sim_var_budget.jsp?edit_sv=true&amp;sf_id=<%= this_sv.get_sf_id() %>"><%= this_sv.name %></a></li>
          <% } %>
      </ul>
      <p>Add a budget trigger</p>
      <form name="form1" method="post" action="">
        <table width="100%" border="0" cellspacing="2" cellpadding="1">
          <tr> 
            <td>Step</td>
              <td valign="top">&nbsp;</td>
              <td colspan="2" valign="top">&nbsp;</td>
            </tr>
          <tr> 
            <td><strong>1</strong></td>
              <td colspan="3" valign="top"><strong>Select Budget and Trigger Point 
                Quantity</strong></td>
            </tr>
          <tr> 
            <td width="7%">&nbsp;</td>
              <td width="33%" valign="top">Budget 
                <select name="select">
                  </select></td>
              <td colspan="2" valign="top">Quantity</td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">&nbsp;</td>
              <td width="13%" valign="top"> <input type="radio" name="radiobutton" value="radiobutton"> 
                &gt; <br /> <input type="radio" name="radiobutton" value="radiobutton" />
                =<br /> <input type="radio" name="radiobutton" value="radiobutton" /> 
                &lt; </td>
              <td width="21%" valign="top"><input name="textfield" type="text" size="10" maxlength="10" /></td>
            </tr>
          <tr> 
            <td><strong>2</strong></td>
              <td colspan="3" valign="top"><strong>Select Variable Effected</strong></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">Variable 
                <select name="select2">
                  </select></td>
              <td valign="top">&nbsp;</td>
              <td valign="top">&nbsp;</td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">&nbsp;</td>
              <td colspan="2" valign="top">&nbsp;</td>
            </tr>
          <tr> 
            <td><strong>3</strong></td>
              <td colspan="3" valign="top"><strong>Select the Effect on the Variable</strong></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">&nbsp;</td>
              <td colspan="2" valign="top"><input type="radio" name="radiobutton" value="radiobutton" />
                Set it equal to: 
                <input name="textfield2" type="text" size="10" maxlength="10" /> 
                <br /> <input type="radio" name="radiobutton" value="radiobutton" />
                Increase it by: <input name="textfield22" type="text" size="10" maxlength="10" /> 
                <br /> <input type="radio" name="radiobutton" value="radiobutton" />
                Decrease it by: <input name="textfield23" type="text" size="10" maxlength="10" /> 
                <br /> <input type="radio" name="radiobutton" value="radiobutton" />
                Multiply it by: <input name="textfield24" type="text" size="10" maxlength="10" />                </td>
            </tr>
          <tr> 
            <td><strong>4</strong></td>
              <td valign="top"><strong> 
                <input type="submit" name="Submit" value="Submit" />
                </strong></td>
              <td colspan="2" valign="top">&nbsp;</td>
            </tr>
          </table>
	    </form>
      <p>&nbsp;</p>
      <p>&nbsp;</p>
      <p align="center"><a href="../incorporate_underlying_model.jsp">Back to Add Special 
        Features</a></p>      <p>&nbsp;</p>			</td>
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
