<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*,java.util.*,org.usip.osp.networking.*" errorPage="../error.jsp" %>
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
    <td><table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
        <tr>
          <td width="120"><img src="../Templates/images/white_block_120.png" /></td>
          <td width="828"><br />
            <h1>Restore Database</h1>
            <p>&nbsp;</p>
            <p>User Archives Found</p>
            <p>
              <% for (ListIterator li = FileIO.getListOfUserArchives().listIterator(); li.hasNext();) {
			
				String fileName = (String) li.next(); 
				%>
              	Load:<a href="restore.jsp?loaddetails=true&filename=<%= fileName %>"> <%= fileName %></a><br />
              <% } %>
            </p>
            <p>&nbsp;</p>
            <form id="form1" name="form1" method="post" action="archive_restore.jsp">
              <input type="hidden" name="sending_page" value="archive_restore" />
              <label></label>
              <p>&nbsp; </p>
              <label></label>
              <em><strong>
              <input type="submit" name="button3" id="button3" value="Restore Users" disabled="disabled" />
              </strong></em>
              <p>&nbsp; </p>
              <label></label>
              <p>&nbsp; </p>
            </form>
            <p><br />
            <p>&nbsp;</p></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td><p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
<p>&nbsp;</p>
<p align="center">&nbsp;</p>
</body>
</html>