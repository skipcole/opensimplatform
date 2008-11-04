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
<title>Online Simulation Platform Control Page</title>
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
      <h1>Add / Edit Player Control</h1>
    <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
      <p><%= Debug.getDebug(debug) %></p>
      <blockquote>
        <p>A 'player control' allows a player to control a simulation variable, 
          for example the amount of money being spent on police forces, or on 
          trash pick up. Frequently something being tracked by the player, such 
          as the number of rats in the city, is not something that can be directly 
          controlled by them. But they can control other items, such as the budget 
          of the 'rat extermination department.'</p>
        <p>Current player controls for the Simulation <%= pso.simulation.name %>:</p>
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
            </p>
            <br />
          </li>
        </ul>
      </blockquote>
      <form name="form2" id="form2" method="post" action="sf_player_control.jsp">
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
          </p>
          <p>&nbsp;</p>
          <p>Next, select how the player will control it:</p>
          <blockquote>
            <p> 
              <input name="pc_type" type="radio" value="radiobutton" checked="checked" />
              Direct Control</p>
            <p> 
              <input type="radio" name="pc_type" value="radiobutton" disabled />
              Allocates</p>
            <p> 
              <input type="radio" name="pc_type" value="radiobutton" disabled />
              Influences</p>
          </blockquote>
          <p>&nbsp; </p>
          <p>Direct Control</p>
          <p>In this type of control, the player can set a variable any particular 
            value they desire, as long as they keep it inside of the permitted 
            range.</p>
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
          <p>-------</p>
          <p>Allocates (Coming Soon!)</p>
          <p>In this type of control, a player can add to a value as long as they 
            have enough of another variable to cover their allocation. For example, 
            if the player has $10,000 in cash, they can allocate up to the entire 
            amount to pest control.</p>
          <p>-------</p>
          <p>Influences (Coming Soon!)</p>
          <p>In this type of control, the player can influence another variable. 
            For example, they maybe be able to increase it by 10 or 15% by their 
            influence.</p>
          <p>&nbsp; </p>
        </blockquote>
      </form>
      <p>&nbsp;</p>
	  <p align="center"><a href="add_special_features.jsp">Back to Add Special Features</a></p>
	  <p>&nbsp;</p>
      <!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
