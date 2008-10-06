<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*,org.usip.oscw.persistence.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);

	Simulation simulation = new Simulation();
	simulation.setCreator(pso.user_Display_Name);
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
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
          <td><a href="../simulation_authoring/intro.jsp" target="_top">Home</a></td>
        </tr>
	<% } else { %>
		<tr>
          <td><a href="index.jsp" target="_top">Home </a></td>
        </tr>
	<% } %>	
        <tr>
          <td><a href="../simulation_user_admin/my_profile.jsp"> My Profile</a></td>
        </tr>
        <tr>
          <td><a href="../simulation_authoring/logout.jsp" target="_top">Logout</a></td>
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
	    <td><a href="../simulation_authoring/creationwebui.jsp" target="_top" class="menu_item">CREATE</a></td>
		<td>&nbsp;</td>
		<td><a href="facilitateweb.jsp" target="_top" class="menu_item">PLAY</a></td>
		<td>&nbsp;</td>
        <td><a href="../simulation_sharing/index.jsp" target="_top" class="menu_item">SHARE</a></td>
		   </tr>
		</table>
	<% } %></td>
  </tr>
  <tr>
    <td colspan="2" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Online Simulation Library </h1>
    <!-- InstanceEndEditable --></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<BR />
<table width="90%" bgcolor="#FFFFFF" align="center" border="1" cellspacing="0" cellpadding="0">
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" --> 
      <form action="index.jsp" method="post" name="form1" id="form1">
	  	<input type="hidden" name="sending_page" value="create_simulation" />
        <blockquote>
        <blockquote>
          <p>&nbsp;</p>
        </blockquote>
      </form>
      <blockquote>
        
        <p>Below are all of the currently published Simulations for your organization.</p>
        <p>Click on the name of the simulation template to begin preparing a play 
          session.</p>
        <table width="100%" border="1" cellspacing="0" cellpadding="2">
          <tr valign="top"> 
            <td width="15%"><strong>Name / Version</strong></td>
            <td width="16%"><strong>Author</strong></td>
            <td width="16%"><strong>Keywords</strong></td>
            <td width="16%"><strong>Publish Date</strong></td>
            <td width="16%"><strong>Review</strong></td>
            <td width="16%"><strong>User Comments</strong></td>
          </tr>
          <% 
		  for (ListIterator li = Simulation.getAllPublished(pso.schema).listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
			%>
          <tr valign="top"> 
            <td><a href="facilitateweb.jsp?loadSim=true&sim_id=<%= sim.getId() %>"><%= sim.getName() %> : <%= sim.getVersion() %></a></td>
            <td>ETCD</td>
            <td><%= sim.getListingKeyWords() %></td>
            <td>12/2008</td>
            <td><a href="../simulation_authoring/review_sim.jsp?loadSim=true&sim_id=<%= sim.getId() %>">Review</a></td>
            <td>&nbsp;</td>
          </tr>
          <% } %>
        </table>
<br>
      </blockquote>
      <p align="center"></p>

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
