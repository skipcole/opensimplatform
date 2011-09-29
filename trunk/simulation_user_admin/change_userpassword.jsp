<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	
	String error_msg = "";
	
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}

	String user_email = (String) request.getParameter("user_email");
	String forcepasswordchange = request.getParameter("forcepasswordchange");
	
	boolean forcedChange = false;

	if ((forcepasswordchange != null) && (forcepasswordchange.equalsIgnoreCase("true"))){
		forcedChange = true;
	}

	int returnCode = afso.changeUserPassword(request);
	

	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>


<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>
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
<% if (returnCode == SessionObjectBase.PASSWORDS_CHANGED) { %>
			<h1>Password Has Been Changed</h1>
            
                <% if (forcedChange) { %>
                	The user will need to change their password upon login.
                <% } %>
                
<% } else if (returnCode == SessionObjectBase.USER_NOT_FOUND) { %>
			  	<h1 class="style1">User with that password not found</h1>
			  	<% } else if (returnCode == SessionObjectBase.PASSWORDS_MISMATCH) { %>
			  	<h1 class="style1">New Passwords Did Not Match</h1>
			  	<% } else if (returnCode == SessionObjectBase.NO_ACTION){ %>
				<h1>Change User Password </h1>
			  	<% } // End of if password changed, or an error %>
			
              <br />
      <form id="form1" name="form1" method="post" action="change_userpassword.jsp">
  <table border="0" cellspacing="2" cellpadding="1" width="100%">
    <tr>
      <td>User Email Address:</td>
      <td><label for="textfield"></label>
        <input type="text" name="user_email" id="user_email" /></td>
    </tr>
    <tr>
      <td>New Password:</td>
      <td>
        <label>
          <input type="password" name="new_password" />
          </label></td>
    </tr>
    <tr>
      <td>Confirm New Password:</td>
      <td>
        <label>
        <input type="password" name="new_password2" />
        </label></td>
    </tr>
    <tr>
      <td>Send Email:</td>
      <td><input type="radio" name="radio" id="radio" value="radio" />
        <label for="radio">Yes </label>
        <input type="radio" name="radio2" id="radio2" value="radio2" />
        <label for="radio2">No</label></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td><label>
	  <input type="hidden" name="forcepasswordchange" value="<%= forcepasswordchange %>" />
        <input type="hidden" name="sending_page" value="change_userpassword" /> 
        <input type="submit" name="update" id="update" value="Update" />
        </label></td>
    </tr>
  </table>
        </form>    
		<p>&nbsp;</p>			</td>
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
	
%>