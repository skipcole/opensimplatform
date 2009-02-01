<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	pso.backPage = "add_underlying_model.jsp";
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Add Special Features Page</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
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
<%
	String myLogoutPage = pso.getBaseSimURL() + "/simulation/logout.jsp";
	
	if ( (pso.isAuthor())  || (pso.isFacilitator())) {
		myLogoutPage = pso.getBaseSimURL() + "/simulation_authoring/logout.jsp";
	}
%>
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
          <td><div align="center"><a href="<%= myLogoutPage %>" target="_top" class="menu_item"><img src="../Templates/images/logout.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
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
      <h1>Incorporating Equation 'Planet Temperature'</h1>
    <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
      <blockquote> 
        <% 
			if (pso.simulationSelected) {
		%>
        <h2>&nbsp;</h2>
        <h2><a href="#">Planet Temperature</a>: Pt = Funcition (Number Black Daisies, Number White Daises).</h2>
        <h2>&nbsp;</h2>
        <h2>Establish Mapping of Internal Variables to Equation Variables</h2>
        <table width="90%" border="1" cellspacing="0">
          <tr>
            <td><strong>Equation Variable</strong></td>
            <td><strong>Internal Variable</strong></td>
            <td><strong>Change</strong></td>
          </tr>
          <tr>
            <td valign="top">Planet Temperature</td>
            <td valign="top"><label>
              <select name="select" id="select">
                <option value="PlanetTemp" selected="selected">PlanetTemp</option>
                <option value="NUMb">NUMb</option>
                <option value="NUMw">NUMw</option>
                <option value="NEW">Create New Variable For This</option>
                                                        </select>
              <br />
            </label></td>
            <td valign="top"><label>
              <input type="submit" name="button2" id="button2" value="Submit" />
            </label></td>
          </tr>
          <tr>
            <td valign="top">Number of Black Daisies</td>
            <td valign="top"><label>
              <select name="select" id="select">
                <option value="PlanetTemp">PlanetTemp</option>
                <option value="NUMb" selected="selected">NUMb</option>
                <option value="NUMw">NUMw</option>
                <option value="NEW">Create New Variable For This</option>
                          </select>
              <br />
            </label></td>
            <td valign="top"><label>
              <input type="submit" name="button2" id="button2" value="Submit" />
            </label></td>
          </tr>
                    <tr>
            <td valign="top">Number of White Daisies</td>
            <td valign="top"><label>
              <select name="select" id="select">
                <option value="PlanetTemp">PlanetTemp</option>
                <option value="NUMb">NUMb</option>
                <option value="NUMw" selected="selected">NUMw</option>
                <option value="NEW">Create New Variable For This</option>
                          </select>
              <br />
            </label></td>
            <td valign="top"><label>
              <input type="submit" name="button2" id="button2" value="Submit" />
            </label></td>
          </tr>
        </table>
        <p>&nbsp;</p>
        <h2>Indicate when the Equation Will Be Called</h2>
        <table width="90%" border="1" cellspacing="0">
          <tr>
            <td width="5%"><label>
              <input type="checkbox" name="checkbox" id="checkbox" />
            </label></td>
            <td width="34%">On Round Change</td>
            <td width="20%">&nbsp;</td>
            <td><input type="checkbox" name="checkbox3" id="checkbox3" /></td>
            <td>When Simulation Terminates</td>
          </tr>
          <tr>
            <td><input type="checkbox" name="checkbox2" id="checkbox2" /></td>
            <td>On Phase Change</td>
            <td>&nbsp;</td>
            <td><input type="checkbox" name="checkbox3" id="checkbox3" /></td>
            <td>When Called by Control</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td><label>
              <div align="center">
                <input type="submit" name="button" id="button" value="Submit" />
                </div>
            </label></td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
        </table>
        <p>&nbsp;</p>
      </blockquote>
      <p align="center"><a href="set_universal_sim_sections.jsp?actor_index=0">Next 
        Step: Assign Simulation Sections to Actors</a></p>
		
	  <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
		<%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>
      <a href="incorporate_underlying_model_equation.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>      <!-- InstanceEndEditable -->
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
<%
	
%>
