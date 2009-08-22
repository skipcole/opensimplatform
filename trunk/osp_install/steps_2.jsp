<%@ page contentType="text/html; charset=ISO-8859-1" 

language="java" 
import="java.sql.*,java.util.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*" 
errorPage=""

 %>
<%
	// Load the defaults in case they are needed
	
	session.setAttribute("db_loc", "jdbc:mysql://localhost:");
	session.setAttribute("db_port", "3306");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Open Simulation Platform Control Page</title>
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
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform Installation</h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"></td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top"></td>
    <td colspan="1" valign="top"></td>
    <td width="194" align="right" valign="top"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td width="80%" valign="top"><br /> <h1>Installation Instructions - Continued. </h1></td>
    <td width="20%" align="right" valign="top">&nbsp;</td>
  </tr>
</table>
<p>&nbsp;</p>
<p>Steps (Continued)<br>
</p>
<table width="100%" border="1" cellspacing="0" cellpadding="2">
  <tr align="left" valign="top"> 
    <td colspan="2"><strong>Step</strong></td>
    <td colspan="2"><strong>Heading</strong></td>
    <td width="667"><strong>Description</strong></td>
  </tr>
  <tr align="left" valign="top"> 
    <td width="15">4.</td>
    <td width="25">&nbsp;</td>
    <td colspan="2">Prepare Database</td>
    <td>&nbsp;</td>
  </tr>
  <tr align="left" valign="top"> 
    <td>&nbsp;</td>
    <td>4a</td>
    <td width="70">&nbsp;</td>
    <td width="116">Populate the root database</td>
    <td><a href="install_root_db.jsp">Click here</a> to create the necessary tables in the root database.</td>
  </tr>
  <tr align="left" valign="top"> 
    <td>&nbsp;</td>
    <td>4b</td>
    <td>&nbsp;</td>
    <td>Enter user schema info</td>
    <td><a href="install_db.jsp">Click Here</a> to install an organization schema 
      Database (populate it with tables.)</td>
  </tr>

  <tr align="left" valign="top"> 
    <td>5</td>
    <td>&nbsp;</td>
    <td colspan="2">Verify Installation</td>
    <td>&nbsp;</td>
  </tr>
  <tr align="left" valign="top">
    <td>&nbsp;</td>
    <td>5a</td>
    <td>&nbsp;</td>
    <td>schema</td>
    <td>Click here</td>
  </tr>
  <tr align="left" valign="top"> 
    <td>&nbsp;</td>
    <td>6a</td>
    <td>&nbsp;</td>
    <td>diagnostics</td>
    <td>Click here to run diagnostics</td>
  </tr>
  <tr align="left" valign="top"> 
    <td>&nbsp;</td>
    <td>6b</td>
    <td>&nbsp;</td>
    <td>sections</td>
    <td>
      <form name="form1" id="form1" method="post" action="../simulation_authoring/catalog_of_installed_sections.jsp">
	  
        <select name="db_schema">
		<% 
		List sioList = new ArrayList();
		
		try {
			sioList = SchemaInformationObject.getAll();
		} catch (Exception ee){
			System.out.println("SchemaInformationObject doesn't seem to exist.");
		}
		
		for (ListIterator li = sioList.listIterator(); li.hasNext();) {
			SchemaInformationObject sio = (SchemaInformationObject) li.next();
		
		%>
          <option value="<%= sio.getSchema_name() %>"><%= sio.getSchema_name() %></option>
		  <% 
		  }
		%>
        </select>
        <input type="submit" name="Submit" value="Submit" />
        to see the sections now available 
      </form></td>
  </tr>
  <tr align="left" valign="top"> 
    <td>&nbsp;</td>
    <td>6c</td>
    <td>&nbsp;</td>
    <td>email</td>
    <td>Click here to send test email from the system</td>
  </tr>
  <tr align="left" valign="top">
    <td>7</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td><a href="../login.jsp">Login to Tool</a></td>
  </tr>
</table>
<p><strong>Any questions? Contact our community at <a href="http://docs.opensimplatform.org">docs.opensimplatform.org</a></strong></p>
<p><a href="mailto:scole@usip.org"></a></p>
<p>&nbsp;</p>

</body>
</html>
