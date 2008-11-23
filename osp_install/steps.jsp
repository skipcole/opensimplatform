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
<style type="text/css">
<!--
.style1 {
	color: #FF0000;
	font-weight: bold;
}
-->
</style>
</head>
<body>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td width="80%" valign="top"> <h1>Installation Instructions</h1></td>
    <td width="20%" align="right" valign="top">&nbsp;</td>
  </tr>
</table>
<p>&nbsp;</p>
<p>Follow the instructions below to install the USIP Open Simulation Platform <br>
</p>
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
        <li>Java 5.0</li>
        <li>Tomcat</li>
        <li>MySQL Database<br />
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
    <td colspan="2">Prepare Properties Files</td>
    <td>&nbsp;</td>
  </tr>
    <tr align="left" valign="top">
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>Update Properties File</td>
    <td> The properties file <span class="style1">USIP_OSP_Properties_en_US.properties</span> needs to be edited to configure it to your system.<br /></td>
  </tr>
    <tr align="left" valign="top">
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>NB:</td>
      <td>The principal schema (a mysql schema) MUST exist. By default it is usiposp.</td>
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
    <td>Locate this install page</td>
    <td>Find this page, the file 'osp_install/index.jsp,' on the machine you 
      are installing on. The link below will only function correctly if you are 
      looking at this page on the Tomcat server on which you are installing. </td>
  </tr>

  <tr align="left" valign="top"> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>Check DB</td>
    <td>Verify that the following schema exists:<span class="style1"> <%= USIP_OSP_Properties.getValue("principalschema") %></span></td>
  </tr>
</table>
<p>After you have edited your properties file, click on the link below to go to the next step.</p>
<p>Do Not Click to move foward unless you have a mysql schema named as it is done in the properties file mentioned in Step 3!</p>
<p>ON to the last set of <a href="steps_2.jsp">steps</a>.</p>
<hr />
<p>Any questions? Contact our community at <a href="http://www.opensimplatform.org">opensimplatform.org</a></p>
<p>&nbsp;</p>

</body>
</html>
