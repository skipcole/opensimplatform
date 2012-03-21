<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.coursemanagementinterface.*,
	org.usip.osp.baseobjects.*,
	com.oreilly.servlet.*" 
	errorPage="/error.jsp" %>
<%
		AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
		
		if (!(afso.isLoggedin())) {
			response.sendRedirect("../blank.jsp");
			return;
		}
		
		List importList = CSVInterpreter.parseCSV(request, afso.schema);


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>


<style type="text/css">
<!--
.style1 {
	color: #FF0000;
	font-weight: bold;
}
-->
</style>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
          

<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Confirm Student Import  Information</h1>
              <p>Below are the students to be imported for this class.</p>
              
              <form id="form1" name="form1" method="post" action="">
              <table width="100%" border="1" cellpadding="2" cellspacing="0">
                <tr>
                  <td valign="top">First Name</td>
                  <td valign="top">Last Name</td>
                  <td valign="top">User Name</td>
                  <td valign="top">Temporary<br />
                    Password</td>
                  <td valign="top">Class</td>
                  <td valign="top">Include</td>
                </tr>
<%
	

	int ii = 0;
			for (ListIterator li = importList.listIterator(); li.hasNext();) {
				
				ii += 1;
				
				User user = (User) li.next();
				
%>
                <tr>
                  <td valign="top">
                    <input type="text" name="user_first_name_<%= ii %>" id="user_first_name_<%= ii %>" value="<%= user.getBu_first_name() %>" /></td>
                  <td valign="top">
                   
                    <input type="text" name="user_last_name_<%= ii %>" id="user_last_name_<%= ii %>" value="<%= user.getBu_last_name() %>" />
                  </td>
                  <td valign="top"><input type="text" name="user_name_<%= ii %>" id="user_name_<%= ii %>" value="<%= user.getUserName() %>" /></td>
                  <td valign="top">&nbsp;</td>
                  <td valign="top">&nbsp;</td>
                  <td valign="top"><input type="checkbox" name="checkbox" id="checkbox" />
                    <label for="checkbox"></label></td>
                </tr>
<%
			} // end of loop over students.
%>

              </table>
              <p>x
                <input type="submit" name="button" id="button" value="Cancel" />
              </p>
              </form></td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>

</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
