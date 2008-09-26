<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.oscw.communications.*,
	org.usip.oscw.networking.*,
	org.usip.oscw.persistence.*,
	org.usip.oscw.baseobjects.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	
	String inject_group_id = (String) request.getParameter("inject_group_id");
	String sending_page = (String) request.getParameter("sending_page");
	
	String edit = (String) request.getParameter("edit");
	String inj_id = (String) request.getParameter("inj_id");
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("create_ind_inject"))){
		pso.handleCreateInject(request);
		response.sendRedirect("create_injects.jsp");
		return;
	}
	
	Inject inj = new Inject();
	boolean in_edit_mode = false;
	
	
	if ( (edit != null) && (edit.equalsIgnoreCase("true"))){
		
		inj = Inject.getMe(pso.schema, new Long(inj_id));
		in_edit_mode = true;
		
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Create Inject Group Page</title>
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" -->
<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>
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
      <h1>Create Inject</h1>
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
      <blockquote> 
        <% 
			if (pso.simulationSelected) {
		%>
        <p>Create Inject</p>
        <form id="form2" name="form2" method="post" action="">
        <input type="hidden" name="inj_id" value="<%= inj_id %>" />
        <input type="hidden" name="edit" value="<%= edit %>"  />
        <input type="hidden" name="sending_page" value="create_ind_inject" />
        <table width="100%" border="0" cellspacing="0" cellpadding="4">
          <tr>
            <td valign="top">Inject Name:</td>
            <td valign="top">
              <label>
                <input type="text" name="inject_name" id="inject_name" value="<%= inj.getInject_name() %>"/>
                </label>            </td>
          </tr>
          <tr>
            <td valign="top">Inject Text:</td>
            <td valign="top"><label>
              <textarea name="inject_text" id="inject_text" cols="45" rows="5"><%= inj.getInject_text() %></textarea>
            </label></td>
          </tr>

          <tr>
            <td valign="top">Inject Notes:<br />
              (Notes direct the simulation facilitator on how to use this inject.)</td>
            <td valign="top"><label>
              <textarea name="inject_notes" id="inject_notes" cols="45" rows="5"><%= inj.getInject_Notes() %></textarea>
            </label></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>
            <input type="hidden" name="inject_group_id" value="<%= inject_group_id %>" />
            
            <% if (in_edit_mode) { %>
            	<input type="submit" name="button" id="button" value="Save Changes" />
            <% } else { %>
            	<input type="submit" name="button" id="button" value="Create Inject" />
            <% } %>
            </td>
          </tr>
        </table>
        </form>
        <p>&nbsp;</p>
        <p> 
          <!-- jsp:include page="snippet.jsp" flush="true" -->
        </p>
        <p>&nbsp;</p>
      </blockquote>
      <p align="center">&nbsp;</p>
		
	  <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
		<%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>
      <a href="create_injects.jsp">&lt;- Back</a>      <!-- InstanceEndEditable --></td>
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
