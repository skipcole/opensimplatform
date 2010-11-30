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
	
	User userOnScratchPad = afso.handleCreateAdminUser(request);
	
	/////////////////////////////////////////
	List userList = null;
	String do_search = (String) request.getParameter("do_search");
	
	if ((do_search != null) && (do_search.equalsIgnoreCase("true"))) {
		String search_string = (String) request.getParameter("search_string");
		userList = BaseUser.searchUserByName(afso.schema, search_string);
	}
	
	////////////////////////////////////////
	
	
	String loaduser = (String) request.getParameter("loaduser");
	
	if ((loaduser != null) && (loaduser.equalsIgnoreCase("true"))) {
		String userid = (String) request.getParameter("userid");
		
		userOnScratchPad = User.getById(afso.schema, new Long(userid));
	}
	/////////
	String is_admin = "";
	String is_author = "";
	String is_instructor = "";
	
	if (userOnScratchPad.isAdmin()){
		is_admin = " \"checked\" ";
	}
	if (userOnScratchPad.isSim_author()){
		is_author = " \"checked\" ";
	}
	if (userOnScratchPad.isSim_instructor()){
		is_instructor = " \"checked\" ";
	}
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Control Page</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
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
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Create Admin User</h1>
      <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" -->

<p><font color="#FF0000"><%= afso.errorMsg %></font></p>
      <p>On this page you can create simulation authors, instructors and administrative 
        users. </p>
      <p>You can also promote normal users to be simulation authors, instructors or administrative 
        users. </p>
      <form action="create_admin_user.jsp" method="post" name="form1" id="form1">
        <table width="80%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td>username/email<a href="helptext/user_name.jsp" target="helpinright">(?)</a>:</td>
            <td> <input type="text" name="email" tabindex="1" value="<%= userOnScratchPad.getBu_username() %>" /> </td>
          </tr>
          <tr> 
            <td>password<a href="helptext/user_password.jsp" target="helpinright">(?)</a></td>
            <td><input type="text" name="password" tabindex="2" /></td>
          </tr>
    <tr>
    <td>First Name:</td>
    <td>
      <label>
      <input type="text" name="first_name" id="first_name" tabindex="4" value="<%= userOnScratchPad.getBu_first_name() %>" />
      </label></td>
  </tr>
    <tr>
    <td>Middle Name:</td>
    <td>
      <label>
      <input type="text" name="middle_name" id="middle_name" tabindex="5" value="<%= userOnScratchPad.getBu_middle_name() %>" />
      </label></td>
  </tr>
    <tr>
    <td>Last Name:</td>
    <td>
      <label>
      <input type="text" name="last_name" id="last_name" tabindex="6" value="<%= userOnScratchPad.getBu_last_name() %>"  />
      </label></td>
  </tr>
          <tr> 
            <td>administrator</td>
            <td><input name="admin" type="checkbox" tabindex="7" value="true" <%= is_admin %> /></td>
          </tr>
          <tr> 
            <td>simulation author</td>
            <td><input name="author" type="checkbox" value="true" tabindex="8" <%= is_author %> /></td>
          </tr>
          <tr> 
            <td>simulation Instructor</td>
            <td><input name="instructor" type="checkbox" value="true" tabindex="9" <%= is_instructor %> /></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td><input type="hidden" name="sending_page" value="create_admin_user" /> 
                <%
				if (userOnScratchPad.getId() == null) {
				%>
                	<input type="submit" name="command" tabindex="10" value="Create" />
                <%
				} else {
				%>
                	<input type="hidden" name="u_id" value="<%= userOnScratchPad.getId() %>" />
                	<input type="submit" name="command" tabindex="10" value="Clear" />
                	<input type="submit" name="command" tabindex="11" value="Update" />
                <%
					}
				%>              </td>
          </tr>
        </table>
</form>
<p>&nbsp;</p>      <p>Below are listed alphabetically by username all of the current sim creators 
        and administrators.</p>
      <blockquote> 
        <table width="80%" border="1" cellspacing="1" cellpadding="2">
          <tr> 
            <td><strong>Name</strong></td>
            <td><strong>Admin</strong></td>
            <td><strong>Author</strong></td>
            <td><strong>Facilitator</strong></td>
          </tr>
          <% for (ListIterator li = User.getAllAdminsSCandInstructors(afso.schema).listIterator(); li.hasNext();) {
			User user = (User) li.next(); %>
          <tr> 
            <td><a href="create_admin_user.jsp?command=Edit&u_id=<%= user.getId().toString() %>"><%= user.getBu_username() %></a></td>
            <td><%= user.isAdmin() %></td>
            <td><%= user.isSim_author() %></td>
            <td><%= user.isSim_instructor() %></td>
          </tr>
          <% } %>
        </table>
        <p>&nbsp;</p>
<h2>Search for a User</h2>
<p>Use the form below to find a user's email address if you need it.  </p>
<form name="form1" method="post" action="">
<table width="70%" border="1">
  <tr>
    <td>Part of Their Name: </td>
    <td>
      <label>
        <input type="text" name="search_string">
        </label>    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><label>
	<input type="hidden" name="do_search" value="true">
      <input type="submit" name="Submit" value="Submit">
    </label></td>
  </tr>
</table>
</form>

<% if (userList != null) { %>
<h3>Search Results (Click on a User to Queue Load Them.) </h3>
<blockquote>
<%
    	for (ListIterator li = userList.listIterator(); li.hasNext();) {
			BaseUser bu = (BaseUser) li.next();  %>
		<a href="create_admin_user.jsp?loaduser=true&userid=<%= bu.getId() %>"><%= bu.getUsername() %></a> <br />
<%     	}  %>

</blockquote>
</blockquote>

<% } // end if if search results not null %>

      </blockquote>
<!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
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
<!-- InstanceEnd --></html>
<%
	afso.errorMsg = "";
%>