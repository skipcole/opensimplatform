<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	User userOnScratchPad = afso.handleCreateUser(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
	
		response.sendRedirect("email_user_temp_password.jsp?u_id=" + userOnScratchPad.getId());
		
		return;
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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" width="100%" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Create/Register User <a href="../simulation_facilitation/helptext/create_user_help.jsp" target="helpinright">(?)</a></h1>
			
              <p><font color="#FF0000"><%= afso.errorMsg %></font></p>
			  <%
			  	afso.errorMsg = "";
			  %>
      <form action="create_user.jsp" method="post" name="form1" id="form1">
        <table width="80%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><strong>username/email<a href="helptext/user_email.jsp" target="helpinright"> (?):</a></strong></td>
              <td><input name="email" type="text" tabindex="1" value="<%= userOnScratchPad.getBu_username() %>" size="60"  /></td>
            </tr>
          <tr>
            <td height="29"><strong>temporary password<a href="helptext/temp_password.jsp" target="helpinright"> (?):</a></strong></td>
              <td><input name="password" type="text" tabindex="2" size="20" /></td>
            </tr>
			
          <tr>
            <td><strong>First Name <a href="helptext/first_name.jsp" target="helpinright">(?)</a>:</strong></td>
      <td>
        <label>
          <input name="first_name" type="text"  id="first_name" tabindex="4" value="<%= userOnScratchPad.getBu_first_name() %>" size="60" />
          </label></td>
    </tr>
          <tr>
            <td><strong>Middle Name<a href="helptext/middle_name.jsp" target="helpinright"> (?)</a>:</strong></td>
      <td>
        <label>
          <input name="middle_name" type="text"  id="middle_name" tabindex="5" value="<%= userOnScratchPad.getBu_middle_name() %>" size="60" />
          </label></td>
    </tr>
          <tr>
            <td><strong>Last Name <a href="helptext/last_name.jsp" target="helpinright">(?)</a>:</strong></td>
      <td>
        <label>
          <input name="last_name" type="text" id="last_name" tabindex="6" value="<%= userOnScratchPad.getBu_last_name() %>" size="60"  />
          </label></td>
    </tr>
          <tr> 
            <td valign="top"><strong>Permission Level: </strong></td>
            <td valign="top">
            
            <% if (afso.isAdmin()) { %>
              <label>
              <input name="perm_level" type="radio" value="admin" <%= is_admin %> />
              Admin, Author, Instructor, Player </label><br />
            <% } %>
            <% if (afso.isAuthor()) { %>
			  <label>
              <input name="perm_level" type="radio" value="author" <%= is_author %> />
              Author, Instructor, Player </label><br />
            <% } %>
            <% if (afso.isFacilitator()) { %>
			  <label>
              <input name="perm_level" type="radio" value="instructor" <%= is_instructor %> />
              Instructor, Player </label><br />
            <% } %>
			  <label>
              <input name="perm_level" type="radio" value="player" <%= is_player %> />
              Player </label>
			  </td>
          </tr>
          <tr>
            <td><strong>Phone Number: </strong></td>
            <td><label>
            <input name="phonenumber" type="text" tabindex="7" value="<%= userOnScratchPad.getPhoneNumber() %>" size="60" />
          </label></td>
          </tr>
          <tr>
            <td><strong>Language:</strong></td>
            <td>
            <%
				String checkedEng = " selected=\"selected\" ";
				String checkedSpan = "";
				
				if (userOnScratchPad.getBu_language().intValue() == 2) {
					checkedEng = "";
					checkedSpan = " selected=\"selected\" ";
				}
			%>
            
            <label for="select">
              <select name="preferred_language" id="preferred_language" tabindex="8">
                <option value="1" <%= checkedEng %>>English</option>
                <option value="2" <%= checkedSpan %>>Spanish</option>
              </select>
            </label></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
              <td><input type="hidden" name="sending_page" value="create_users" />
                <input type="submit" name="command" value="Save" tabindex="9" />
                <%
			  	// Put in switch here to allow the ediiting of users
			  %>
                <label>
                  <input type="submit" name="command" value="Clear" tabindex="10" />
                  </label></td>
            </tr>
          </table>
        </form>
      <p>&nbsp;</p>
            </td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>