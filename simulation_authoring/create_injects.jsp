<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,
	org.usip.oscw.communications.*,
	org.usip.oscw.persistence.*,
	org.usip.oscw.baseobjects.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	pso.backPage = "create_injects.jsp";
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Add Special Features Page</title>
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
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
      <h1>Create Injects</h1>
    <!-- InstanceEndEditable --></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<BR />
<table width="90%" bgcolor="#FFFFFF" align="center" border="1" cellspacing="0" cellpadding="0">
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" --> 
      <blockquote> 
        <% 
			if (pso.simulationSelected) {
		%>
        <p>Creating injects for the simulation <strong><%= simulation.getDisplayName() %></strong>.<br>
          (If you would like to work on a different simulation, <a href="select_simulation.jsp">click 
          here</a>.)</p>
        <p>Injects are arranged into groups. You must first create an inject group before creating any injects.</p>
        <form id="form1" name="form1" method="post" action="create_inject_group.jsp">
          <label>
          <input type="submit" name="button" id="button" value="Create Inject Group" />
          </label>
        </form>
        <table width="100%" border="0" cellspacing="0" cellpadding="4">
          <tr>
            <td colspan="4"><strong><u>Current Inject Groups and Injects</u></strong></td>
            </tr>
		  <%
			for (ListIterator li = InjectGroup.getAllForSim(pso.schema, pso.sim_id).listIterator(); li.hasNext();) {
			InjectGroup ig = (InjectGroup) li.next();
		%>
          <tr>
            <td width="25%" valign="top"><strong><%= ig.getName() %></strong></td>
            <td width="13%" valign="top">&nbsp;</td>
            <td width="27%" valign="top">&nbsp;</td>
            <td width="35%" valign="top"><a href="create_indvidual_inject.jsp?inject_group_id=<%= ig.getId() %>">Create Inject in Group <%= ig.getName() %>
            </a></td>
          </tr>
          <tr>
            <td valign="top">&nbsp;</td>
            <td colspan="3" valign="top"><strong>Group Description:</strong> <%= ig.getDescription() %></td>
          </tr>
          <% 
		  List injectList = Inject.getAllForSimAndGroup(pso.schema, pso.sim_id, ig.getId());
		  
		   if (injectList.size() > 0) { 
		  	for (ListIterator lii = injectList.listIterator(); lii.hasNext();) {
			Inject da_inject = (Inject) lii.next();
		  
		  %>
          <tr>
            <td valign="top">&nbsp;</td>
            <td valign="top">inject name:</td>
            <td colspan="2" valign="top"><%= da_inject.getInject_name() %></td>
          </tr>
          <tr>
            <td valign="top">&nbsp;</td>
            <td valign="top">inject text:</td>
            <td colspan="2" valign="top"><%= da_inject.getInject_text() %></td>
          </tr>
          <tr>
            <td valign="top">&nbsp;</td>
            <td valign="top">inject notes:</td>
            <td colspan="2" valign="top"><%= da_inject.getInject_Notes() %></td>
          </tr>
          <tr>
            <td valign="top">&nbsp;</td>
            <td valign="top">&nbsp;</td>
            <td colspan="2" valign="top"><a href="create_indvidual_inject.jsp?edit=true&inj_id=<%= da_inject.getId() %>">update</a> / 
            <a href="delete_object.jsp?object_type=inject&objid=<%= da_inject.getId() %>&object_info=<%= da_inject.getInject_name() %>"> 
            delete</a></td>
          </tr>
          <% }  // End of loop.  
		  } else { %>
          <tr>
            <td valign="top">&nbsp;</td>
            <td valign="top">&nbsp;</td>
            <td colspan="2" valign="top">No injects in this group yet. </td>
          </tr>
          <% } %>
        
        <p>&nbsp;</p>
        <% } // end of loop over inject groups %>
        </table>
        <p> 
          <!-- jsp:include page="snippet.jsp" flush="true" -->
        </p>
        <p>&nbsp;</p>
      </blockquote>
      <p align="center"><a href="add_special_features.jsp">Next Step: Add Special 
      Feature</a><a href="set_universal_sim_sections.jsp?actor_index=0"></a></p>
		
	  <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
		<%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>
      <a href="assign_actor_to_simulation.jsp">&lt;- Back</a>      <!-- InstanceEndEditable -->
    <p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
<%
	
%>
