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
      <h1>Assign 
        Actor to a Simulation</h1>
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
		
	  <a href="create_actors.jsp">&lt;- Back</a>      <!-- InstanceEndEditable --></td>
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