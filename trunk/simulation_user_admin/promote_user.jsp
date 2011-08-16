<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin()) || (!(afso.isAdmin()))) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	User userOnScratchPad = afso.handlePromoteUser(request);
	
	/////////////////////////////////////////
	List userList = null;
	String do_search = (String) request.getParameter("do_search");
	
	if ((do_search != null) && (do_search.equalsIgnoreCase("true"))) {
		String search_string = (String) request.getParameter("search_string");
		userList = BaseUser.searchUserByName(search_string);
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
	String is_player = "";
	
	if (userOnScratchPad.isAdmin()){
		is_admin = " \"checked\" ";
	} else if (userOnScratchPad.isSim_author()){
		is_author = " \"checked\" ";
	} else if (userOnScratchPad.isSim_instructor()){
		is_instructor = " \"checked\" ";
	} else {
		is_player = " \"checked\" ";
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
      <h1>Promote User</h1>
      <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" -->

<p><font color="#FF0000"><%= afso.errorMsg %></font></p>
      <p>On this page you  can also promote users to be simulation authors, instructors or administrative 
        users. </p>
<% if (userOnScratchPad.getId() == null) { %>
<p>You must first select a user to promote or demote. You can do this by selecting the name of one of the current users, or by searching for the user.</p>

<p>If you need to create a user, <a href="create_user.jsp">click here</a>.</p>

<% } else { %>
      <form action="promote_user.jsp" method="post" name="form1" id="form1">
        <table width="80%" border="1" cellspacing="0" cellpadding="2">
          <tr> 
            <td valign="top">username/email<a href="helptext/user_name.jsp" target="helpinright">(?)</a>:</td>
            <td valign="top">  <%= userOnScratchPad.getBu_username() %></td>
          </tr>
          <tr>
    <td valign="top">First Name:</td>
    <td valign="top"><%= userOnScratchPad.getBu_first_name() %></td>
  </tr>
    <tr>
    <td valign="top">Middle Name:</td>
    <td valign="top"> <%= userOnScratchPad.getBu_middle_name() %></td>
  </tr>
    <tr>
    <td valign="top">Last Name:</td>
    <td valign="top"> <%= userOnScratchPad.getBu_last_name() %></td>
  </tr>
          <tr> 
            <td valign="top">Permission Level: </td>
            <td valign="top"><label>
              <input name="perm_level" type="radio" value="admin" <%= is_admin %> />
              Admin, Author, Instructor, Player </label><br />
			  <label>
              <input name="perm_level" type="radio" value="author" <%= is_author %> />
              Author, Instructor, Player </label><br />
			  <label>
              <input name="perm_level" type="radio" value="instructor" <%= is_instructor %> />
              Instructor, Player </label>
			  <br />
			  <label>
              <input name="perm_level" type="radio" value="player" <%= is_player %> />
              Player </label>
			  </td>
          </tr>
          <tr> 
            <td valign="top">&nbsp;</td>
            <td valign="top"><input type="hidden" name="sending_page" value="create_admin_user" /> 
                	<input type="hidden" name="u_id" value="<%= userOnScratchPad.getId() %>" />
                	<input type="submit" name="command" tabindex="10" value="Clear" />
                	<input type="submit" name="command" tabindex="11" value="Update" />           </td>
          </tr>
        </table>
</form>
                <%
					} // End of if user_id was found.
				%>   
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
            <td><a href="promote_user.jsp?command=Edit&amp;u_id=<%= user.getId().toString() %>"><%= user.getBu_username() %></a></td>
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
		<a href="promote_user.jsp?loaduser=true&amp;userid=<%= bu.getId() %>"><%= bu.getUsername() %></a> <br />
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