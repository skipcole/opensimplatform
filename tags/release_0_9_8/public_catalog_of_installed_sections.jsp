<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*" 
	errorPage="../error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	String db_schema = (String) request.getParameter("db_schema");
	
	if ((db_schema != null) && (db_schema.length() > 0)) {
		afso.schema = db_schema;
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>USIP Open Simulation Platform</title>




<link href="usip_osp.css" rel="stylesheet" type="text/css" />
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

<link href="usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Catalog of Sections </h1>
              <br />
    <blockquote> 
      <p>This page shows all of the sections that have been loaded on your system 
        that are availabe to a Simulation Creator to add to a simulation.</p>
    <table border="1" width="100%"><tr><td>
      <table border="0" cellspacing="1" width="100%">
              <tr align="left" valign="top">
          <td colspan="3" align="left" valign="top" height="50"  bgcolor="#CCCCFF" >Sections below here can be added to any simulation.</td>
          </tr>
        <%

		for (ListIterator li = BaseSimSection.getAllBase(afso.schema).listIterator(); li.hasNext();) {
			BaseSimSection bss = (BaseSimSection) li.next();
			
			if (!(bss.isAuthorGeneratedSimulationSection())){ 
			 %>

        <tr align="left" valign="top"> 
          <td align="left" valign="top"><strong><%= bss.getRec_tab_heading() %></strong></td>
        <td align="left" valign="top"><%= bss.getDescription() %></td>
        <td align="left" valign="top"><img src="simulation_section_information/images/<%= bss.getSample_image() %>" width="300" height="240" /></td>
      </tr>
        <% 
			} // End of if not an author generated section
		 }  %>
        
          <tr align="left" valign="top">
          <td colspan="3" align="left" valign="top" height="50"  bgcolor="#CCCCFF" >Sections below here can be added to any simulation, and can be customized to allow them to behave differently depending upon the simulation creator's wishes.</td>
          </tr>
        <%

		for (ListIterator li = new CustomizeableSection().getAllCustomizable(afso.schema).listIterator(); li.hasNext();) {
			CustomizeableSection bss = (CustomizeableSection) li.next(); 
			
			if (!(bss.isAuthorGeneratedSimulationSection()) && (!(bss.isThisIsACustomizedSection()))){ 
			%>

        <tr align="left" valign="top"> 
          <td align="left" valign="top"><strong><%= bss.getRec_tab_heading() %></strong><br />
            <span class="style1">(Customization Possible)</span></td>
        <td align="left" valign="top"><%= bss.getDescription() %></td>
        <td align="left" valign="top"><img src="simulation_section_information/images/<%= bss.getSample_image() %>" width="300" height="240" /></td>
      </tr>
        <% 
			} // End of if not customized
		 }  %>
          <tr align="left" valign="top">
          <td height="50"  bgcolor="#CCCCFF" colspan="3" align="left" valign="top">Additionally, a simulation author can add any web page to their simulation. </td>
          </tr>
       <tr>
       <td align="left" valign="top">Author Generated Name</td>
       <td align="left" valign="top"></td><td align="left" valign="top"><img src="simulation_section_information/images/new_section.png" alt="Any Web Page Anywhere" width="300" height="240" /></td>
       </tr>
        </table>
    </td></tr></table>
    </blockquote>
    <blockquote>
      <p>&nbsp;</p>
      </blockquote>			</td>
		</tr>
		</table>	</td>
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
</html>
<%
	
%>
