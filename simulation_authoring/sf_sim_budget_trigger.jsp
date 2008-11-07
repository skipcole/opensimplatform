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

	Vector simVars = new BudgetVariable().getSetForASimulation(pso.simulation.id);
	
	String trueSelected = "";
	String falseSelected = "selected";
	
	if (sbv.value.equalsIgnoreCase("true")){
		trueSelected = "selected";
		falseSelected = "";
	}
	
	
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
<% String canEdit = (String) session.getAttribute("author"); %>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform </h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
	    <table border="0" cellspacing="1" cellpadding="0">
	<%  if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
        <tr>
          <td><div align="center"><a href="intro.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } else { %>
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
		
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		
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
  	<td width="120" align="right" valign="top">&nbsp;</td>
    <td colspan="1" valign="top"><br /></td>
    <td width="194" align="right" valign="top">		</td>
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
      <h1>Add / Edit Budget Variable</h1>
    <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
      <p><%= Debug.getDebug(debug) %></p>
      <p>Budget triggers are a starting point to allow player actions affect their 
        universe.</p>
      <p>Current budget triggersfor the Simulation <%= pso.simulation.name %>:</p>
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
              Multiply it by: <input name="textfield24" type="text" size="10" maxlength="10" /> 
            </td>
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
      <p align="center"><a href="add_special_features.jsp">Back to Add Special 
        Features</a></p>
      <p>&nbsp;</p>
      <!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
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
<!-- InstanceEnd --></html>
