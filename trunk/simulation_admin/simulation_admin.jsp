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
                      <td><a href="simulation_admin_backuprelated.jsp">Backup/Restore Tasks</a></td>
                      <td>Allows you to archive and restore database components  (users, simulation sections, running simulations. simulations, etc.) for archival or transport purposes.</td>
                    </tr>
                    <tr valign="top">
                      <td><a href="simulation_admin_extensionrelated.jsp">Extension Tasks</a></td>
                      <td>Allows you to add new simulation sections or models to your USIP OSP installation</td>
                    </tr>
                    <tr valign="top">
                      <td><a href="simulation_maintenancerelated.jsp">Maintenance Tasks</a></td>
                      <td><p>Allows one to see all of the installed databases and the last time someone has logged into them. Also allows one to edit or install a new database. <br />
                      Also here one can send test the email system and set the next planned down time.</p>                      </td>
                    </tr>
                    <tr valign="top">
                      <td><a href="software_development_section.jsp">Software Development Tasks</a></td>
                      <td>Section useful to developers only.</td>
                    </tr>
                    
                    <tr valign="top">
                      <td width="24%"><a href="simulation_admin_userrelated.jsp">User Tasks</a></td>
                      <td width="72%">Allows you to add administrative users and see last login times.</td>
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
