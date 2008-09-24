<%@ page contentType="text/html; charset=ISO-8859-1" 

language="java" 
import="java.sql.*,java.util.*,
		org.usip.oscw.baseobjects.*,
		org.usip.oscw.networking.*,
		org.usip.oscw.persistence.*" 
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
<title>Online Simulation Platform Control Page</title>
</head>
<body>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td width="80%" valign="top"> <h1>Installation Instructions</h1></td>
    <td width="20%" align="right" valign="top">&nbsp;</td>
  </tr>
</table>
<p>&nbsp;</p>
<p>Follow the instructions below to install the USIP Online Simulation Platform <br>
</p>
<table width="80%" border="1" cellspacing="0" cellpadding="2">
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
    <td>Files may be downloaded from <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">http://code.google.com/p/usiponlinesimulationcreationwizard/</a></td>
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
    <td colspan="2">Restart Tomcat</td>
    <td>&nbsp;</td>
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
    <td>Find this page, the file 'oscw_install/index.jsp,' on the machine you 
      are installing on. The links below will only function correctly if you are 
      looking at this page on the Tomcat server on which you are installing. </td>
  </tr>
  <tr align="left" valign="top"> 
    <td>4.</td>
    <td>&nbsp;</td>
    <td colspan="2">Prepare Database</td>
    <td>&nbsp;</td>
  </tr>
  <tr align="left" valign="top"> 
    <td>&nbsp;</td>
    <td>4a</td>
    <td>&nbsp;</td>
    <td>Create Root Schema</td>
    <td>You must create a schema to contain all of the username and password information 
      that your database will hold. The recommended schema name for the first 
      schema is 'usiposcw'.. </td>
  </tr>
  <tr align="left" valign="top"> 
    <td>&nbsp;</td>
    <td>4b</td>
    <td>&nbsp;</td>
    <td>Update Properties File</td>
    <td> The properties file USIP_OSCW_Properties_en_US.properties needs to be 
      updated to include the connection information for your first schema.<br /></td>
  </tr>
  <tr align="left" valign="top"> 
    <td>&nbsp;</td>
    <td>4c</td>
    <td>&nbsp;</td>
    <td>Create the root database</td>
    <td><a href="install_root_db.jsp">Click here</a> to create the root database.</td>
  </tr>
  <tr align="left" valign="top"> 
    <td>&nbsp;</td>
    <td>4d</td>
    <td>&nbsp;</td>
    <td>Enter user schema info</td>
    <td><a href="install_db.jsp">Click Here</a> to install an organization schema 
      Database.</td>
  </tr>
  <tr align="left" valign="top"> 
    <td>6</td>
    <td>&nbsp;</td>
    <td colspan="2">Verify Installation</td>
    <td>&nbsp;</td>
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
      <form name="form1" id="form1" method="post" action="catalog_of_installed_sections.jsp">
	  
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
    <td><a href="../simulation_authoring/index.jsp">Login to Tool</a></td>
  </tr>
</table>
<p>Any questions? Send an email to <a href="mailto:scole@usip.org">scole@usip.org</a></p>
<p>&nbsp;</p>

</body>
</html>
