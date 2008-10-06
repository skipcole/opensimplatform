<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" 
	errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);

	String sending_page = (String) request.getParameter("sending_page");
	String createsection = (String) request.getParameter("createsection");
	
	
	if ( (sending_page != null) && (createsection != null) && (sending_page.equalsIgnoreCase("create_section"))){
		
		BaseSimSection bss = new BaseSimSection(pso.schema, request.getParameter("url"), request.getParameter("directory"), 
			request.getParameter("filename"), request.getParameter("rec_tab_heading"), 
			request.getParameter("description"));
			
            
	} // End of if coming from this page and have added simulation section.
	
	String create_defaults = (String) request.getParameter("create_defaults");
	if ((create_defaults != null) && (create_defaults.equalsIgnoreCase("Create Defaults"))){
		BaseSimSection.readBaseSimSectionsFromXMLFiles(pso.schema);
	}
		

	List baseList = BaseSimSection.getAll(pso.schema);
	
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
      <h1>Create Simulation Section</h1>
      <!-- InstanceEndEditable --></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<BR />
<table width="90%" bgcolor="#FFFFFF" align="center" border="1" cellspacing="0" cellpadding="0">
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" -->


<form action="create_simulation_section.jsp" method="post" name="form1" id="form1">
<input type="hidden" name="sending_page" value="create_section" />

        <table width="80%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td>URL:</td>
            <td><input type="text" name="url" tabindex="1" /></td>
          </tr>
          <tr> 
            <td>Directory:</td>
            <td><input type="text" name="directory" tabindex="2" /></td>
          </tr>
          <tr> 
            <td>Filename:</td>
            <td><input type="text" name="filename" tabindex="3" /></td>
          </tr>
          <tr> 
            <td>Recommended Tab Heading:</td>
            <td><input type="text" name="rec_tab_heading" tabindex="4" /></td>
          </tr>
          <tr> 
            <td>Description:</td>
            <td><textarea name="description"></textarea></td>
          </tr>
		  <tr> 
            <td>&nbsp;</td>
            <td><label>
              <input name="checkbox" type="checkbox" value="checkbox" checked="checked" disabled />
            Send Actor Information</label></td>
          </tr>
		      <tr>
            <td>&nbsp;</td>
            <td><input name="checkbox2" type="checkbox" value="checkbox" disabled />
Send User Information</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td><input type="submit" name="createsection" value="Submit" tabindex="7" /> 
              <label> 
              <input type="submit" name="clear_button" value="Clear" tabindex="8" />
              </label></td>
          </tr>

 
        </table>
  <p>&nbsp;</p>
  <p></p>
</form>
<p>&nbsp;</p>

<p>Below are listed alphabetically all of the current Base Simulation Sections.</p>
<table width="80%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td><h2>Recommended Tab Heading</h2></td>
    <td><h2>File Name</h2></td>
  </tr>
  <%
		for (ListIterator li = baseList.listIterator(); li.hasNext();) {
			BaseSimSection bss = (BaseSimSection) li.next();		
		%>
  <tr> 
    <td><%= bss.getRec_tab_heading() %></td>
    <td><%= bss.getPage_file_name() %></td>
  </tr>
  <%
	}
%>
</table>
      <p><a href="<%= pso.backPage %>">&lt;-- Back</a></p>
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
