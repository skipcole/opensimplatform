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
<link href="../usip_oscw.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="80%" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Create New Simulation </h1>
    <!-- InstanceEndEditable --></td>
    <td width="20%" align="right" valign="top"> 
		<% 
		String canEdit = (String) session.getAttribute("author");
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		<a href="../simulation_planning/index.jsp" target="_top">Think</a><br>
	    <a href="creationwebui.jsp" target="_top">Create</a><br>
		<a href="../simulation_facilitation/facilitateweb.jsp" target="_top">Play</a><br>
        <a href="../simulation_sharing/index.jsp" target="_top">Share</a>
		<% } %>
		</td>
  </tr>
</table>
<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
</tr>
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

      <!-- InstanceEndEditable --></td>
  </tr>
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
</table>

<p>&nbsp;</p>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td align="left" valign="bottom"> 
	<% 
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
	<a href="intro.jsp" target="_top">Home 
      </a>
	  <% } else { %>
	  <a href="../simulation_facilitation/index.jsp" target="_top">Home 
      </a>
	  <% } %>
	  </td>
    <td align="right" valign="bottom"><a href="../simulation_user_admin/my_profile.jsp">My 
      Profile</a> </td>
  </tr>
  <tr>
    <td align="left" valign="bottom"><a href="logout.jsp" target="_top">Logout</a></td>
    <td align="right" valign="bottom">&nbsp;</td>
  </tr>
</table>
<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP Open Source Software Project</a>. </p>
</body>
<!-- InstanceEnd --></html>
<%
	
%>
