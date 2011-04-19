<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	
	String error_msg = "";
	
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if ((pso == null) || (!(pso.isLoggedin()))) {
		response.sendRedirect("../blank.jsp");
		return;
	}

	String forcepasswordchange = request.getParameter("forcepasswordchange");
	
	boolean forcedChange = false;
	
	/*
	if ((forcepasswordchange != null) && (forcepasswordchange.equalsIgnoreCase("true"))){
		forcedChange = true;
	}

	int returnCode = pso.changePassword(request);
	
	System.out.println("returnCode was: " + returnCode);
	*/
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

<%
	if ((returnCode == SessionObjectBase.FORCED_PASSWORD_CHANGED) || (forcedChange) ){

%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;USIP Open Simulation Platform </h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center"></div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"></td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top">&nbsp;</td>
    <td colspan="1" valign="top"><br /></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>


<% } // End of if this is a forced password changed %>

<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			<% if (returnCode == SessionObjectBase.FORCED_PASSWORD_CHANGED) { %>
			<h1>Username Changed</h1>
			You should now contact the user to let them know.
			<% } else if (forcedChange) { %>
			<% } else { %>
			  	<% if (returnCode == SessionObjectBase.PASSWORDS_CHANGED) { %>
			  	<h1>Username Has Been Changed</h1>
			  	<% } else if ((returnCode == SessionObjectBase.WRONG_OLD_PASSWORD) || (returnCode == SessionObjectBase.INSUFFICIENT_INFORMATION)) { %>
			  	<h1 class="style1">Incorrect Previous Username </h1>
			  	<% } else if (returnCode == SessionObjectBase.PASSWORDS_MISMATCH) { %>
			  	<h1 class="style1">New Usernames Did Not Match</h1>
			  	<% } else { %>
				<h1>Change Username - NOT IMPLEMENTED </h1>
			  	<p>
			  	  <% } // End of if password changed, or an error %>
			  	  
		  	        <% } // End of if forced change. %>
		  	        <br />
                    <% if (returnCode != SessionObjectBase.FORCED_PASSWORD_CHANGED) { %>
			  	  </p>
			  	<p>&nbsp;</p>
			  	<h2>NOTE!!!!!</h2>
			  	<p>If the previous username has been assigned, we should go in and changes all simulations where they have been added.     </p>
			  	<form id="form1" name="form1" method="post" action="change_username.jsp">
  <table border="0" cellspacing="2" cellpadding="1" width="100%">
    <tr>
      <td>Old Username: </td>
      <td>
        <label>
        <input type="password" name="old_password" />
        </label></td>
    </tr>
    <tr>
      <td>New Username: </td>
      <td>
        <label>
        <input type="password" name="new_password" />
        </label></td>
    </tr>
    <tr>
      <td>Confirm New Username: </td>
      <td>
        <label>
        <input type="password" name="new_password2" />
        </label></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td><label>
	  <input type="hidden" name="forcepasswordchange" value="<%= forcepasswordchange %>" />
        <input type="hidden" name="sending_page" value="change_password" /> 
        <input type="submit" name="update" id="update" value="Update" disabled="disabled" />
        </label></td>
    </tr>
  </table>
        </form>      
<% } // end of if not %>
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