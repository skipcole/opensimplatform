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
                  <h1>Share Simulations</h1>
                  <br />
                  <table width="100%" border="1" cellspacing="2" cellpadding="1">
                    <tr valign="top">
                      <td width="36%">Package A Simulation</td>
                      <td width="1%">&nbsp;</td>
                      <td width="61%"><p>Creates a file that contains all of the information in your simulation.</p>
                        <ul>
                          <li><a href="package_simulation.jsp">Package One</a></li>
                        </ul></td>
                    </tr>
                    <tr valign="top">
                      <td>Upload / Download</td>
                      <td>&nbsp;</td>
                      <td><p>Upload or download a packaged simulation from an OSP fileserver.</p>
                        <ul>
                          <li>Upload</li>
                          <li>Download<br />
                          </li>
                        </ul></td>
                    </tr>
                    <tr valign="top">
                      <td>Unpackage A Simulation</td>
                      <td>&nbsp;</td>
                      <td><p>Unpackage a file containing a simulation</p>
                        <ul>
                          <li><a href="unpackage_simulation.jsp">Unpackage Simulation File</a></li>
                        </ul></td>
                    </tr>
                </table></td>
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
