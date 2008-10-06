<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*,org.usip.oscw.persistence.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);

	pso.backPage = "../simulation_authoring/create_simulation.jsp";
	
	Simulation simulation = new Simulation();
	simulation.setCreator(pso.user_Display_Name);
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	} else {
		pso.simulationSelected = false;
	}
	
	String sending_page = (String) request.getParameter("sending_page");
	String addsimulation = (String) request.getParameter("addsimulation");
	
	///////////////////////////////////
	
	boolean justAdded = false;
	
	String debug_string = "";
	
	if ( (sending_page != null) && (addsimulation != null) && (sending_page.equalsIgnoreCase("create_simulation"))){
          pso.createNewSim(request);  
	} // End of if coming from this page and have added simulation.

	//////////////////////////////////
	// Put sim on scratch pad
	String edit_simulation = (String) request.getParameter("edit_simulation");
	
	
	if ((edit_simulation != null) && (edit_simulation.equalsIgnoreCase("true"))){
		
		pso.sim_id = new Long(   (String) request.getParameter("sim_id")   );
		simulation = pso.giveMeSim();
		
		pso.simulationSelected = true;
			
	}
	
	
	//////////////////////////////////
	// Clear sim from scratch pad
	String clear_simulation = (String) request.getParameter("clear_button");
	
	if ((clear_simulation != null) && (clear_simulation.equalsIgnoreCase("Clear"))){
		
		simulation = new Simulation();
		simulation.setCreator(pso.user_Display_Name);
		pso.simulationSelected = false;
			
	}
	
	//////////////////////////////////
	List simList = Simulation.getAll(pso.schema);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Control Page</title>
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" -->
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
    <td width="666" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform </h1></td>
    <td align="right" background="../Templates/images/top_fade.png"> 

	  <div align="center">
	    <table border="0" cellspacing="4" cellpadding="4">
	<%  if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
        <tr>
          <td><a href="intro.jsp" target="_top">Home</a></td>
        </tr>
	<% } else { %>
		<tr>
          <td><a href="../simulation_facilitation/index.jsp" target="_top">Home </a></td>
        </tr>
	<% } %>	
        <tr>
          <td><a href="../simulation_user_admin/my_profile.jsp"> My Profile</a></td>
        </tr>
        <tr>
          <td><a href="logout.jsp" target="_top">Logout</a></td>
        </tr>
      </table>	  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"><% 
		
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		
		<table border="0" cellpadding="0" cellspacing="0" >
		   <tr>
		<td><a href="../simulation_planning/index.jsp" target="_top" class="menu_item">THINK</a></td>
		<td>&nbsp;</td>
	    <td><a href="creationwebui.jsp" target="_top" class="menu_item">CREATE</a></td>
		<td>&nbsp;</td>
		<td><a href="../simulation_facilitation/facilitateweb.jsp" target="_top" class="menu_item">PLAY</a></td>
		<td>&nbsp;</td>
        <td><a href="../simulation_sharing/index.jsp" target="_top" class="menu_item">SHARE</a></td>
		   </tr>
		</table>
	<% } %></td>
  </tr>
  <tr>
    <td colspan="2" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Create New Simulation </h1>
    <!-- InstanceEndEditable --></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<BR />
<table width="90%" bgcolor="#FFFFFF" align="center" border="1" cellspacing="0" cellpadding="0">
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" --> 
      <form action="create_simulation.jsp" method="post" name="form1" id="form1">
	  	<input type="hidden" name="sending_page" value="create_simulation" />
        <blockquote>

        <table width="80%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td>&nbsp;</td>
            <td valign="top">Simulation Name <a href="helptext/sim_name.jsp" target="helpinright">(?)</a>:</td>
            <td valign="top">
<input type="text" name="simulation_name" value="<%= simulation.getName() %>" tabindex="1" /></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td valign="top">Simulation Version <a href="helptext/sim_version.jsp" target="helpinright">(?)</a>:</td>
            <td valign="top">
<input type="text" name="simulation_version" value="<%= simulation.getVersion() %>" tabindex="2" /></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td valign="top">Simulation Creating Organization <a href="helptext/sim_banner.jsp" target="helpinright">(?)</a>:</td>
            <td valign="top">
<input type="text" name="creation_org" value="<%= simulation.getCreation_org() %>" tabindex="3" /></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td valign="top">Simulation Creator <a href="helptext/sim_banner.jsp" target="helpinright">(?)</a>:</td>
            <td valign="top">
<input type="hidden" name="simcreator" value="<%= pso.user_Display_Name %>"> 
              <%= simulation.getCreator() %></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td valign="top">Simulation Copyright String</td>
            <td valign="top"> <textarea name="simcopyright" tabindex="4"><%= simulation.getCopyright_string() %></textarea></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td valign="top">&nbsp;</td>
            <td valign="top">
<input type="submit" name="addsimulation" value="Save" tabindex="5" /> 
              <input type="submit" name="clear_button" value="Clear" tabindex="6" /></td>
          </tr>
        </table>
        <blockquote>
          <p>&nbsp;</p>
        </blockquote>
      </form>
      <blockquote>
        <p>&nbsp;</p>
        <p>Below are listed alphabetically all of the current Simulations.</p>
        <table>
          <%
		
		for (ListIterator li = simList.listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
			String nameToSend = java.net.URLEncoder.encode(sim.getName());
			
		%>
          <tr> 
            <td><a href="create_simulation.jsp?edit_simulation=true&amp;sim_id=<%= sim.getId().toString() %>"><%= sim.getName() %> : <%= sim.getVersion() %></a></td>
            <td>&nbsp;</td>
            <td><a href="delete_object.jsp?object_type=simulation&objid=<%= sim.getId().toString() %>&object_info=<%= nameToSend %>"> 
              (Remove) <%= sim.getName() %> : <%= sim.getVersion() %> </a></td>
          </tr>
          <%
	}
%>

        </table>
      </blockquote>
      <p align="center"><a href="create_simulation_objectives.jsp">Next Step: 
        Simulation Objectives</a></p>

      <!-- InstanceEndEditable -->
    <p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
<%
	
%>
