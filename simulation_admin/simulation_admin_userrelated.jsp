<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*" errorPage="/error.jsp" %>
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
                  <h1>Simulation Software User Admininstration</h1>
                  <br />
                  <table width="100%" border="1" cellspacing="2" cellpadding="1">
                    <tr valign="top">
                      <td width="24%"><a href="../simulation_user_admin/create_admin_user.jsp">Add Administrative User</a></td>
                      <td width="72%">Allows you to add a user who can administrate or create or facilitate simulations.</td>
                    </tr>
                    <tr valign="top">
                      <td><a href="import_student_csv_file.jsp">Import CSV File</a></td>
                      <td>Allows you to import a comma separated file of student information.</td>
                    </tr>
                    <tr valign="top">
                      <td><a href="simulation_admin_lastlogins.jsp">Last Login Times</a></td>
                      <td>Allows one to see when users have logged on the system.</td>
                    </tr>
                    <tr valign="top">
                      <td><a href="admin_players_view.jsp">Player View </a></td>
                      <td>Allows one to enter a simulation as a particular player. </td>
                    </tr>
                    <tr valign="top">
                      <td>Reset Password </td>
                      <td>Allows an admin to reset a player password. </td>
                    </tr>
                    <tr valign="top">
                      <td><a href="admin_teachers_view.jsp">Teacher View</a> </td>
                      <td>Allows one to enter the OSP as an author or facilitator. </td>
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
