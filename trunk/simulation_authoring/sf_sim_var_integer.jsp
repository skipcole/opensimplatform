<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*,org.usip.oscw.specialfeatures.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	// Determine if setting sim to edit.
	String sending_page = (String) request.getParameter("sending_page");
	String create_new = (String) request.getParameter("create_new");
		
	String debug = "";
	
	IntVariable sv = new IntVariable();
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("add_sim_var"))){
		
		sv.setName(request.getParameter("var_name"));
		sv.setDescription(request.getParameter("description"));
		sv.setPropagation_type(request.getParameter("prop_type"));
		sv.setInitialValue(request.getParameter("start_value"));
		pso.hibernate_session.saveOrUpdate(sv);
		pso.simulation.getVar_int().add(sv);
		pso.hibernate_session.saveOrUpdate(pso.simulation);
		
	} // End of if 
	
		//////////////////////////////////
	// Put shared document on scratch pad
	String edit_sv = (String) request.getParameter("edit_sv");
	
	boolean inEditingMode = false;
	
	String varBoolean = "";
	String varDecimal = "";
	String varInteger = "";
	
	
	if ((edit_sv != null) && (edit_sv.equalsIgnoreCase("true"))){
		/*
		inEditingMode = true;
		
		sv = new IntegerVariable();
		
		sv.set_sf_id((String) request.getParameter("sf_id"));
		
		sv.load();
		*/
					
	}
	///////////////////////////////////////

	List simInts = pso.simulation.getVar_int();
	
	
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
      <h1>Add / Edit Integer Variable</h1>
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
<p><%= Debug.getDebug(debug) %></p>
      <blockquote>
        <p>Current integer variables for the Simulation <%= pso.simulation.getDisplayName() %>:</p>
        <blockquote>
          <p>
            <% if (simInts.size() == 0) { %>
          </p>
        </blockquote>
        <ul>
          <li>None
            <p>
              <% } %>
              <% for (ListIterator li = simInts.listIterator(); li.hasNext();) {
					IntVariable iv = (IntVariable) li.next();
	%>
            </p>
          </li>
          <li><a href="sf_sim_var_integer.jsp?edit_sv=true&amp;sf_id=<%= iv.getid() %>"><%= iv.getName() %></a>
		  <a href="delete_object.jsp?object_type=sf_var_int&amp;objid=<%= iv.getid() %>&amp;backpage=sf_sim_var_integer.jsp&amp;object_info=&quot;<%= iv.getname() %>&quot;"> 
              (Remove) <%= iv.getName() %> </a>
            <p>
              <% } %>
            </p>
          </li>
        </ul>
        <p>Add an integer simulation variable</p>
      </blockquote>
      <form name="form2" id="form2" method="post" action="sf_sim_var_integer.jsp">
        <input type="hidden" name="sending_page" value="add_sim_var">
        <table width="80%" border="0" cellspacing="2" cellpadding="1">
          <tr valign="top"> 
            <td width="32%">&nbsp;</td>
            <td width="32%">Variable Name</td>
            <td width="68%" colspan="2"> <input name="var_name" type="text" size="80" value="<%= sv.getName() %>" />            </td>
          </tr>
          <tr valign="top">
            <td>&nbsp;</td>
            <td>Variable Description</td>
            <td colspan="2"><textarea name="description" cols="30" rows="2"><%= sv.getDescription() %></textarea></td>
          </tr>
          <tr valign="top"> 
            <td>&nbsp;</td>
            <td>Starting Value</td>
            <td colspan="2"> <input type="text" name="start_value" value="<%= sv.getInitial_value() %>" />            </td>
          </tr>
          <tr valign="top"> 
            <td>&nbsp;</td>
            <td>Propagation Type</td>
            <td> <select name="prop_type">
                <option value="constant">Constant</option>
                <option value="fibonacci">Fibonacci Growth</option>
              </select></td>
            <td><ul>
                <li>Constant</font><a href="helptext/sim_var_constant.jsp" target="helpinright">(?)</a></li>
                <li>Fibonacci 
                  Growth</font><a href="helptext/sim_var_constant.jsp" target="helpinright">(?)</a></li>
              </ul></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td> <% if (inEditingMode) { %> <input type="submit" name="edit_sim_var" value="Update" /> 
              <% } else { %> <input type="submit" name="create_new" value="Submit" /> 
              <% } %></td>
            <td colspan="2">&nbsp;</td>
          </tr>
        </table>
      </form>
      <p>&nbsp;</p>
      <p align="center"><a href="add_special_features.jsp">Back to Add Special 
        Features</a></p>
      <p>&nbsp;</p>
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
