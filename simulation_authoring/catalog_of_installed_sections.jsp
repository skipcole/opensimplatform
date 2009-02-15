<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	String db_schema = (String) request.getParameter("db_schema");
	
	if ((db_schema != null) && (db_schema.length() > 0)) {
		pso.schema = db_schema;
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>USIP Open Simulation Platform</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->


<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<style type="text/css">
<!--
.style1 {font-size: small}
-->
</style>
<!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Catalog of Sections </h1>
    <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" -->
<blockquote> 
  <p>This page shows all of the sections that have been loaded on your system 
    that are availabe to a Simulation Creator to add to a simulation. These sections 
    are divided into two parts: off of the shelf and custom made. The off the 
    shelf sections are those that just come with the latest version of the tool. 
    The custom made have been created by a programmer and placed in a library.</p>
  <table border="1"><tr><td>
  <table border="0" cellspacing="1">
    <%

		for (ListIterator li = BaseSimSection.getAllBase(pso.schema).listIterator(); li.hasNext();) {
			BaseSimSection bss = (BaseSimSection) li.next(); %>
    <tr align="left" valign="top"> 
      <td><strong><%= bss.getRec_tab_heading() %></strong></td>
      <td><%= bss.getDescription() %></td>
      <td><img src="../simulation_section_information/images/<%= bss.getSample_image() %>" width="300" height="240" /></td>
    </tr>
    <%  }  %>
	
	    <%

		for (ListIterator li = new CustomizeableSection().getAllCustomizable(pso.schema).listIterator(); li.hasNext();) {
			CustomizeableSection bss = (CustomizeableSection) li.next(); %>
    <tr align="left" valign="top"> 
      <td><strong><%= bss.getRec_tab_heading() %></strong><br />
        <span class="style1">(Customization Required)</span></td>
      <td><%= bss.getDescription() %></td>
      <td><img src="../simulation_section_information/images/<%= bss.getSample_image() %>" width="300" height="240" /></td>
    </tr>
    <%  }  %>
  </table>
  </td></tr></table>
  <p>&nbsp;</p>
  <h2>&nbsp;</h2>
  </blockquote>

    <blockquote>
      <p>&nbsp;</p>
</blockquote>
			<!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
<%
	
%>
