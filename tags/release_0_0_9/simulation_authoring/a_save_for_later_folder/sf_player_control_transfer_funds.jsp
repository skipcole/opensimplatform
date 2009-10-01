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
	
	PlayerControlBudgetTransfer pc = new PlayerControlBudgetTransfer();
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("add_player_control"))){
	
		pc.sim_id = afso.simulation.id;
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

	Vector simVars = new BudgetVariable().getSetForASimulation(afso.simulation.id);
	
	Vector simPCs = new PlayerControlBudgetTransfer().getSetForASimulation(afso.simulation.id);
	
	
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
              <h1>Add / Edit Transfer Funds Player Control</h1>
              <br />
      <%= Debug.getDebug(debug) %>
      <blockquote>
        <p>Current transfer fund controls for the Simulation <%= afso.simulation.name %>:</p>
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
      <p align="center"><a href="../incorporate_underlying_model.jsp">Back to Add Special Features</a></p>      <p>&nbsp;</p>			</td>
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
