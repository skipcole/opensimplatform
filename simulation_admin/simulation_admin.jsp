<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" errorPage="" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Open Simulation Platform Control Page</title>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
              <tr>
                <td width="120"><img src="../Templates/images/white_block_120.png" /></td>
                <td width="100%"><br />
                  <h1>Simulation Software Admininstration</h1>
                  <br />
                  <table width="100%" border="1" cellspacing="2" cellpadding="1">
                    <tr valign="top">
                      <td width="24%"><a href="../simulation_user_admin/create_admin_user.jsp">Add Administrative User</a></td>
                      <td width="72%">Allows you to add a user who can administrate or create or facilitate simulations.</td>
                    </tr>
                    <tr valign="top">
                      <td><a href="install_edit_db.jsp">View / Edit / Install Databases</a></td>
                      <td>Allows one to see all of the installed databases and the last time someone has logged into them. Also allows one to edit or install a new database. </td>
                    </tr>
                    <tr valign="top">
                      <td><a href="install_simulation_sections.jsp">Install Simulation Sections</a></td>
                      <td>Install sections that can be added to simulations.</td>
                    </tr>
                    <tr valign="top">
                      <td><a href="install_models.jsp">Install Model</a></td>
                      <td>Allows you to install a model whose definition file has been found on this system.</td>
                    </tr>
                    <tr valign="top">
                      <td><a href="update_next_downtime_msg.jsp">Set Next Planned Downtime</a></td>
                      <td>Allows you to update the 'next scheduled downtime notice' on the login pages.</td>
                    </tr>
                    <tr valign="top">
                      <td><a href="software_development_section.jsp">Software Development</a></td>
                      <td>Section useful to developers only.</td>
                    </tr>
                    <tr valign="top">
                      <td><a href="email_test.jsp">Test Email</a></td>
                      <td>Allows you to test the functionality of the email system.</td>
                    </tr>
                    <tr valign="top">
                      <td><a href="archive.jsp">Archive  Database</a></td>
                      <td>Allows you to archive an entire database or compenents of it (users, simulation sections, running simulations. simulations, etc.) to prepare to move to a new database.</td>
                    </tr>
                    <tr valign="top">
                      <td><a href="restore.jsp">Restore Database</a></td>
                      <td>Allows you to import an archived database or components of it.</td>
                    </tr>
                  </table>
                  <p>&nbsp;</p>
                  <p>&nbsp;</p></td>
              </tr>
            </table></td>
        </tr>
        <tr>
          <td><p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
        </tr>
      </table></td>
  </tr>
</table>
<p>&nbsp;</p>
<p align="center">&nbsp;</p>
</body>
</html>
