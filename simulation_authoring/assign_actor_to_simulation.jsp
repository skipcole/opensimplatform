<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" 
	errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);

	String sending_page = (String) request.getParameter("sending_page");
	String addactortosim = (String) request.getParameter("addactortosim");
	String remove = (String) request.getParameter("remove");
	
	String actor_id = (String) request.getParameter("actor_id");
	String sim_id = (String) request.getParameter("sim_id");
	
	if ( (sending_page != null) && (addactortosim != null) && (sending_page.equalsIgnoreCase("assign_actor"))){
		pso.addActorToSim(sim_id, actor_id);
	} // End of if coming from this page and have assigned actor
	
	if ( (remove != null) &&  (remove.equalsIgnoreCase("true"))){
		pso.removeActorFromSim(sim_id, actor_id);
		     
	} // End of if coming from this page and have removed actor
	

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
      <h1>Assign 
        Actor to a Simulation</h1>
    <!-- InstanceEndEditable --></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<BR />
<table width="90%" bgcolor="#FFFFFF" align="center" border="1" cellspacing="0" cellpadding="0">
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" -->
      <table width="100%" border="0" cellspacing="2" cellpadding="2">
        <tr valign="top">
          <td>&nbsp;</td>
          <td> <h2>Simulation/Version</h2></td>
          <td> <h2>Current Actors</h2></td>
          <td> <h2>Actor</h2></td>
          <td> <h2>Assign</h2></td>
        </tr>
          <%
		
		for (ListIterator li = simList.listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
		%>
        <form action="" method="post" name="form1" id="form1">
          <tr valign="top">
            <td>&nbsp;</td>
            <td><%= sim.getName() %></td>
            <td><%
			
			for (ListIterator la = sim.getActors().listIterator(); la.hasNext();) {
				Actor act = (Actor) la.next();

			%> 
			<%= act.getName() %> 
			<A href="assign_actor_to_simulation.jsp?remove=true&actor_id=<%= act.getId().toString() %>&sim_id=<%= sim.getId().toString() %>"> (remove) </A><br/>
		<% } // End of loop over Actors %>
		</td>
            <td><select name="actor_id">
			<% 
                for (ListIterator la = sim.getAvailableActors(pso.schema).listIterator(); la.hasNext();) {
					Actor aa = (Actor) la.next();
		%>
                <option value="<%= aa.getId().toString() %>"><%= aa.getName() %></option>
                <% } %>
              </select></td>
            <td> <input type="hidden" name="sending_page" value="assign_actor" /> 
              <input type="hidden" name="sim_id" value="<%= sim.getId().toString() %>" /> 
              <input type="submit" name="addactortosim" value="Submit" /></td>
          </tr>
        </form>
        <%
  	} // End of loop over simulations
  %>
      </table>

      <p>&nbsp;</p>
      <div align="center"><a href="create_injects.jsp">Next Step: Create Injects</a></div>
		
	  <a href="create_actors.jsp">&lt;- Back</a>      <!-- InstanceEndEditable -->
    <p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
<%
	
%>