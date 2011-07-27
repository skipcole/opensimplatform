<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin()) || (!(afso.isAdmin()))) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	(check if user is admin)
	
	User userOnScratchPad = afso.handleCreateAdminUser(request);
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Search Users</h1>
              <br />
			
      <p><font color="#FF0000"><%= afso.errorMsg %></font></p>
      <p>Criteria Section for User</p>
      <p>Pulldown selection for Schema</p>
      <p>.</p>
      <blockquote> 
        <table width="80%" border="1" cellspacing="1" cellpadding="2">
          <tr><td><div align="right"><strong>Schema:</strong></div></td><td colspan="3"><div align="center"><strong>test</strong></div></td>
          </tr>
          <tr> 
            <td><strong>Name</strong></td>
              <td><strong>Admin</strong></td>
              <td><strong>Author</strong></td>
              <td><strong>Facilitator</strong></td>
            </tr>
          <% for (ListIterator li = User.getAllAdminsSCandInstructors(afso.schema).listIterator(); li.hasNext();) {
			User user = (User) li.next(); %>
          <tr> 
            <td><a href="promote_user.jsp?command=Edit&u_id=<%= user.getId().toString() %>"><%= user.getBu_username() %></a></td>
              <td><%= user.isAdmin() %></td>
              <td><%= user.isSim_author() %></td>
              <td><%= user.isSim_instructor() %></td>
            </tr>
          <% } %>
          </table>
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
	afso.errorMsg = "";
%>