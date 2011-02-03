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
	
		response.sendRedirect("create_user_complete.jsp?u_id=" + userOnScratchPad.getId());
		
		return;
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
              <h1>Create User <a href="../simulation_facilitation/helptext/create_user_help.jsp" target="helpinright">(?)</a></h1>
			
              <p><font color="#FF0000"><%= afso.errorMsg %></font></p>
			  <%
			  	afso.errorMsg = "";
			  %>
      <form action="create_user.jsp" method="post" name="form1" id="form1">
        <table width="80%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><strong>username/email<a href="helptext/user_email.jsp" target="helpinright"> (?):</a></strong></td>
              <td><input type="text" name="email" tabindex="1" value="<%= userOnScratchPad.getBu_username() %>"  /></td>
            </tr>
          <tr>
            <td height="29"><strong>password<a href="helptext/user_password.jsp" target="helpinright"> (?):</a></strong></td>
              <td><input type="text" name="password" tabindex="2" /></td>
            </tr>
			          <tr>
            <td><strong>temporary password <a href="helptext/temp_password.jsp" target="helpinright"> (?)</a>: </strong></td>
            <td><input name="radiobutton" type="radio" value="radiobutton" />
              yes / 
              <label>
              <input name="radiobutton" type="radio" value="radiobutton" />
              </label>
              no </td>
          </tr>
			
          <tr>
            <td><strong>First Name <a href="helptext/first_name.jsp" target="helpinright">(?)</a>:</strong></td>
      <td>
        <label>
          <input type="text" name="first_name" tabindex="4"  id="first_name" value="<%= userOnScratchPad.getBu_first_name() %>" />
          </label></td>
    </tr>
          <tr>
            <td><strong>Middle Name<a href="helptext/middle_name.jsp" target="helpinright"> (?)</a>:</strong></td>
      <td>
        <label>
          <input type="text" name="middle_name" tabindex="5"  id="middle_name" value="<%= userOnScratchPad.getBu_middle_name() %>" />
          </label></td>
    </tr>
          <tr>
            <td><strong>Last Name <a href="helptext/last_name.jsp" target="helpinright">(?)</a>:</strong></td>
      <td>
        <label>
          <input type="text" name="last_name" tabindex="6" id="last_name" value="<%= userOnScratchPad.getBu_last_name() %>"  />
          </label></td>
    </tr>
          <tr>
            <td><strong>Phone Number: </strong></td>
            <td><label>
            <input type="text" name="phonenumber" value="<%= user.getPhoneNumber() %>" />
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
              <select name="preferred_language" id="preferred_language">
                <option value="1" <%= checkedEng %>>English</option>
                <option value="2" <%= checkedSpan %>>Spanish</option>
              </select>
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
<iframe src ="../simulation_user_admin/user_search.jsp" width="100%" height="200">
  <p> </p>
</iframe>
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