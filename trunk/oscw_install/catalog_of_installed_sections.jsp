<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.oscw.baseobjects.*,
		org.usip.oscw.networking.*,
		org.usip.oscw.persistence.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	String db_schema = (String) request.getParameter("db_schema");
	
	if ((db_schema != null) && (db_schema.length() > 0)) {
		pso.schema = db_schema;
	}


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Online Simulation Platform Control Page</title>
<link href="../usip_oscw.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
</head>
<body onLoad="">
<blockquote> 
  <h1>Catalog of Sections Available to Simulation Developers </h1>
  <p>&nbsp;</p>
  <p>This page shows all of the sections that have been loaded on your system 
    that are availabe to a Simulation Creator to add to a simulation. These sections 
    are divided into two parts: off of the shelf and custom made. The off the 
    shelf sections are those that just come with the latest version of the tool. 
    The custom made have been created by a programmer and placed in a library.</p>
  <ul>
    <li><a href="#offtheshelf">Off the Shelf</a></li>
    <li><a href="#customlibraries">Custom Libraries</a></li>
  </ul>
  <h2>&nbsp;</h2>
  <h2>Off the Shelf Sections<a name="offtheshelf" id="offtheshelf"></a></h2>
  <p>Below are all of the sections that come standard with this too. </p>
  <table border="1" cellspacing="1">
    <tr align="left" valign="top"> 
      <td><h1>Name</h1></td>
      <td><h1>Description</h1></td>
      <td><h1>Image</h1></td>
    </tr>
    <%

		for (ListIterator li = BaseSimSection.getAllBase(pso.schema).listIterator(); li.hasNext();) {
			BaseSimSection bss = (BaseSimSection) li.next(); %>
    <tr align="left" valign="top"> 
      <td><%= bss.getRec_tab_heading() %></td>
      <td><%= bss.getDescription() %></td>
      <td><img src="../simulation_section_information/images/<%= bss.getSample_image() %>" width="300" height="240" /></td>
    </tr>
    <%  }  %>
	
	    <%

		for (ListIterator li = new CustomizeableSection().getAllCustomizable(pso.schema).listIterator(); li.hasNext();) {
			CustomizeableSection bss = (CustomizeableSection) li.next(); %>
    <tr align="left" valign="top"> 
      <td><%= bss.getRec_tab_heading() %></td>
      <td><%= bss.getDescription() %></td>
      <td><img src="../simulation_section_information/images/<%= bss.getSample_image() %>" width="300" height="240" /></td>
    </tr>
    <%  }  %>
  </table>
  <p>&nbsp;</p>
  <h2>Custom Libraries<a name="customlibraries" id="customlibraries"></a></h2>
  <p>Below are all of the custom library sections that have been added to your 
    installation. </p>
  <table border="1" cellspacing="1">
    <tr align="left" valign="top"> 
      <td><h1>Name</h1></td>
      <td><h1>Library</h1></td>
      <td><h1>Description</h1></td>
      <td><h1>Image</h1></td>
    </tr>
    <%

		for (ListIterator li = new CustomLibrarySection().getAll(pso.schema).listIterator(); li.hasNext();) {
			BaseSimSection bss = (BaseSimSection) li.next(); %>
    <tr align="left" valign="top"> 
      <td><%= bss.getRec_tab_heading() %></td>
      <td><%= bss.getCust_lib_name() %></td>
      <td><%= bss.getDescription() %></td>
      <td><img src="../simulation_section_information/images/<%= bss.getSample_image() %>" width="300" height="240" /></td>
    </tr>
    <%  }  %>
  </table>
  <p>&nbsp;</p>
</blockquote>
</body>
</html>
<%
	
%>
