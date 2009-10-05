<%@ page contentType="text/html; charset=ISO-8859-1" 

language="java" 
import="java.sql.*,java.util.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*" 
errorPage=""

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
.style3 {color: #FF0000}
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
    <td width="80%" valign="top"><br /><h1>Installation Instructions</h1></td>
    <td width="20%" align="right" valign="top">&nbsp;</td>
  </tr>
</table>
<h3>Important Notes:</h3>
<ul>
  <li>Complete Instructions (including up to date system requirements and faqs) are maintained at: <a href="http://demo.opensimplatform.org/docs/index.php/Step_by_Step_Installation">http://demo.opensimplatform.org/docs/index.php/Step_by_Step_Installation</a></li>
  <li>The installation process will walk you through creating one simulation database. Additional databases may be created later by entering the administration section of the OSP and clicking on the &quot;View / Edit / Install Databases&quot; link.</li>
</ul>
<h3>Installation Instructions<br>
</h3>
<table width="100%" border="1" cellspacing="0" cellpadding="2">
  <tr align="left" valign="top"> 
    <td colspan="2"><strong>Step</strong></td>
    <td colspan="2"><strong>Heading</strong></td>
    <td><strong>Description</strong></td>
  </tr>
  <tr align="left" valign="top"> 
    <td width=15>1.</td>
    <td width="25">&nbsp;</td>
    <td colspan="2">Check Requirements</td>
    <td width="667"> <ul>
        <li><strong>MySQL Database</strong></li>
        <li>(Java 5.0 / Tomcat) To be seeing this page on your server you must have already installed these.<br />
        </li>
    </ul></td>
  </tr>
  <tr align="left" valign="top"> 
    <td>2.</td>
    <td>&nbsp;</td>
    <td colspan="2">Get and Place Files </td>
    <td>&nbsp;</td>
  </tr>
  <tr align="left" valign="top"> 
    <td>&nbsp;</td>
    <td>2a</td>
    <td width="70">&nbsp;</td>
    <td width="116">Get Files</td>
    <td>Files may be downloaded from <a href="http://code.google.com/p/opensimplatform/">http://code.google.com/p/opensimplatform/</a></td>
  </tr>
  <tr align="left" valign="top"> 
    <td>&nbsp;</td>
    <td>2b</td>
    <td>&nbsp;</td>
    <td>Place Files</td>
    <td> Place downloaded files into your tomcat\webapps directory <br /> </td>
  </tr>
  <tr align="left" valign="top"> 
    <td>3.</td>
    <td>&nbsp;</td>
    <td colspan="2">Prepare Databases</td>
    <td>You will need to create two MySQL databases to house the information needed to run the OSP.</td>
  </tr>
    <tr align="left" valign="top">
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>Create root schema</td>
    <td>  The first database  is normally named <strong>usiposp</strong>. The name you give it much match what is in the properties file (editing the properties file is done in the next step.)<br /></td>
  </tr>  
     <tr align="left" valign="top">
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>Create first simulation schema</td>
    <td> The second database schema will be used to house simulation information. It can have any name you desire.<br /></td>
  </tr> 
  <tr align="left" valign="top"> 
    <td>4.</td>
    <td>&nbsp;</td>
    <td colspan="2">Create Properties Files</td>
    <td>&nbsp;</td>
  </tr>
    <tr align="left" valign="top">
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>Open Tempate file</td>
      <td>Open the file <strong>USIP_OSP_Properties_en_US.properties.template</strong> and do a 'Save As' filename <strong>USIP_OSP_Properties_en_US.properties</strong></td>
    </tr>
    <tr align="left" valign="top">
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>Edit Properties File</td>
    <td> Edit the file <strong>USIP_OSP_Properties_en_US.properties</strong>  to configure it to your system.<br /></td>
  </tr>
  <tr align="left" valign="top"> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>Restart</td>
    <td>Restart your Tomcat server.</td>
  </tr>
  <tr align="left" valign="top"> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>Return to this page</td>
    <td>The link below will only function correctly if you are 
      looking at this page on the Tomcat server on which you are installing. </td>
  </tr>
  <tr align="left" valign="top">
    <td>5.</td>
    <td>&nbsp;</td>
    <td colspan="2"><a href="install_root_db.jsp">Click</a> to Move Foward</td>
    <td>The next set of <a href="install_root_db.jsp">steps</a> will prepare for you your root database and first simulation database.</td>
  </tr>
</table>
<blockquote>
<% String reason_failed = request.getParameter("reason_failed");

if (reason_failed != null) {

%>
<% if (!(reason_failed.equalsIgnoreCase("0"))) { %>
<p class="style3">Your attempt to move forward to the next set of steps has failed.</p>
  <ul>
	<% if (reason_failed.equalsIgnoreCase("1")) { %>
		
		  <li>It appears that your properties file could not be found. </li>

	<% } %>
    
    <% if (reason_failed.equalsIgnoreCase("2")) { %>
		<li>It appears that the values listed in your properties file did not lead to a successful database connection. </li>
	<% } %>
	
    </ul>
<% }  // End of reason failed not being equal to 0. %>
<% } // End of reason failed not being null  %>
</blockquote>
<hr />
<p>Any questions? Contact our community at <a href="http://docs.opensimplatform.org">docs.opensimplatform.org</a></p>
<p>&nbsp;</p>

</body>
</html>
