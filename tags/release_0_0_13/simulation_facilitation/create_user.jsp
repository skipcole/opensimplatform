<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%
	
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	User userOnScratchPad = afso.handleCreateUser(request);


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
              <h1>Create User</h1>
			
      <p><font color="#FF0000"><%= afso.errorMsg %></font></p>
      <form action="create_user.jsp" method="post" name="form1" id="form1">
        <table width="80%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>username/email<a href="../simulation_user_admin/helptext/user_email.jsp" target="helpinright">(?)</a></td>
              <td><input type="text" name="email" tabindex="1" value="<%= userOnScratchPad.getBu_username() %>"  /></td>
            </tr>
          <tr>
            <td>password<a href="../simulation_user_admin/helptext/user_password.jsp" target="helpinright"> (?)</a></td>
              <td><input type="text" name="password" tabindex="2"  value="<%= userOnScratchPad.getBu_password() %>"/></td>
            </tr>
          <tr>
            <td>First Name:</td>
      <td>
        <label>
          <input type="text" name="first_name" tabindex="4"  id="first_name" value="<%= userOnScratchPad.getBu_first_name() %>" />
          </label></td>
    </tr>
          <tr>
            <td>Middle Name:</td>
      <td>
        <label>
          <input type="text" name="middle_name" tabindex="5"  id="middle_name" value="<%= userOnScratchPad.getBu_middle_name() %>" />
          </label></td>
    </tr>
          <tr>
            <td>Last Name:</td>
      <td>
        <label>
          <input type="text" name="last_name" tabindex="6" id="last_name" value="<%= userOnScratchPad.getBu_last_name() %>"  />
          </label></td>
    </tr>
          <tr>
            <td>&nbsp;</td>
              <td><input type="hidden" name="sending_page" value="create_users" />
                <input type="submit" name="command" value="Save" tabindex="6" />
                <%
			  	// Put in switch here to allow the ediiting of users
			  %>
                <label>
                  <input type="submit" name="command" value="Clear" />
                  </label></td>
            </tr>
          </table>
        </form>
      <p>&nbsp;</p>
      <p>Below are listed alphabetically by username all of the current players 
        listed for your organization.</p>      <blockquote>
        <p>
          <%
		  
		  List userList = User.getAllForSchemaAndLoadDetails(afso.schema);
		
		for (ListIterator uli = userList.listIterator(); uli.hasNext();) {
			User bu = (User) uli.next();  %>
          
          <%= bu.getBu_username() %> <br />
          <% } %>
          </p>
          <p align="center"><a href="assign_user_to_simulation.jsp">Next 
            Step: Assign User</a></p>
          <p align="left"><a href="bulk_invite.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a></p>
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