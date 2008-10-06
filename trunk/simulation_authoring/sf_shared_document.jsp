<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*,org.usip.oscw.specialfeatures.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if ((pso.simulation.id == null) || (pso.simulation.id.equalsIgnoreCase(""))){
		pso.errorMsg = "<p><font color=red> You must first select the sim you want to add this special feature to.</font></p>";
		response.sendRedirect("add_special_features.jsp");
		return;
	}
	
	if ((pso.simulation.id == null) || (pso.simulation.id.equalsIgnoreCase(""))){
		pso.errorMsg = "<p><font color=red> You must first select the sim you want to add this special feature to.</font></p>";
		response.sendRedirect("add_special_features.jsp");
		return;
	}
	
	// Determine if setting sim to edit.
	String sending_page = (String) request.getParameter("sending_page");
	String create_new = (String) request.getParameter("create_new");
		
	
	String debug = "";
	
	SharedDocument sd = new SharedDocument();
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("add_shared_doc"))){
	
		sd.sim_id = pso.simulation.id;
		sd.tabheading = (String) request.getParameter("tab_title");
		sd.docTitle = (String) request.getParameter("doc_title");
		sd.docDesc = (String) request.getParameter("doc_desc");
		sd.docStarterText = (String) request.getParameter("doc_starter_text");	
		
		debug = sd.store();
	} // End of if 
	
		//////////////////////////////////
	// Put shared document on scratch pad
	String edit_sd = (String) request.getParameter("edit_sd");
	
	boolean inEditingMode = false;
	
	if ((edit_sd != null) && (edit_sd.equalsIgnoreCase("true"))){
		
		inEditingMode = true;
		
		sd = new SharedDocument();
		
		sd.set_sf_id((String) request.getParameter("sf_id"));
		
		sd.load();
			
	}
	///////////////////////////////////////
	
	Vector sharedDocs = new SharedDocument().getSetForASimulation(pso.simulation.id);
	
	
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
      <h1>Add / Edit Shared Document</h1>
    <!-- InstanceEndEditable --></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<BR />
<table width="90%" bgcolor="#FFFFFF" align="center" border="1" cellspacing="0" cellpadding="0">
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" --> 
<p><%= Debug.getDebug(debug) %></p>
      <blockquote>
        <p>Current Shared Documents for the Simulation <%= pso.simulation.name %>:</p>
        <blockquote>
          <p>
            <% if (sharedDocs.size() == 0) { %>
          </p>
        </blockquote>
        <ul>
          <li>None
            <p>
              <% } %>
              <% for (Enumeration e = sharedDocs.elements(); e.hasMoreElements();){ 
	SharedDocument this_sd = (SharedDocument) e.nextElement();
	%>
            </p>
          </li>
          <li><a href="sf_shared_document.jsp?edit_sd=true&amp;sf_id=<%= this_sd.get_sf_id() %>"><%= this_sd.docTitle %></a>
            <p>
              <% } %>
            </p>
          </li>
        </ul>
        <p>Add a shared document to the simulation </p>
      </blockquote>
      <form name="form2" id="form2" method="post" action="sf_shared_document.jsp">
	<input type="hidden" name="sending_page" value="add_shared_doc">
        <table width="80%" border="0" cellspacing="2" cellpadding="1">
          <tr>
            <td width="32%">&nbsp;</td>
            <td width="32%">Tab Title</td>
            <td width="68%"> <input name="tab_title" type="text" size="80" value="<%= sd.tabheading %>" /> 
            </td>
          </tr>
          <tr>
            <td width="32%">&nbsp;</td>
            <td width="32%">Document Title</td>
            <td width="68%"> <input name="doc_title" type="text" size="80" value="<%= sd.docTitle %>"  /> 
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>Document Description (seen by players)</td>
            <td><textarea name="doc_desc" cols="60" rows="5"><%= sd.docDesc %></textarea></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>Document Starter Text (if any)</td>
            <td><textarea name="doc_starter_text" cols="60" rows="5"><%= sd.docStarterText %></textarea></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td> <% if (inEditingMode) { %>
              update 
              <% } else { %> <input type="submit" name="create_new" value="Submit" /> 
              <% } %></td>
            <td>&nbsp;</td>
          </tr>
        </table>
    </form>
    <p>&nbsp;</p>
    <p align="center"><a href="add_special_features.jsp">Back to Add Special Features</a></p>
    <p>&nbsp;</p>

<!-- InstanceEndEditable -->
    <p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
