<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%
	
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	User userOnScratchPad = afso.handleAutoRegistration(request);
	
	if(afso.forward_on){
	
		response.sendRedirect("auto_registration_thankyou.jsp");
		afso.forward_on = false;
		return;
	}
	
	// Get the schema id that has been sent in. If there is none, then allow user to select organizational database.
	String schema_id = (String) request.getParameter("schema_id");
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Open Simulation Platform Auto Registration Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(../Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
</head>
<body onLoad="">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform </h1></td>
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
<BR />
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Registration Page</h1>
              <br />
			
      <p>To participate in online simulations using this sytem, you will need to be registered in the system.</p>
      <p>Please Note:</p>
      <ol>
        <li>Your email address will be your username</li>
    <li>In case you lose your password, you can have it emailed to you.</li>
    <li>Do not use a password that you typically use. This is a low security system, so pick a simple new password.</li>
    <li>Registering on the sytem will not give you immediate access to any simulations. An instructor will need to assign you a role in an ongoing simulation before you can participate in one.</li>
    <li>After registering you will receive an email at the address you give for your email address. If you do not see this email shortly after registering, please look for it in your junk email box (and then white list the sender if you know how to.)</li>
        </ol>
      <p>Please enter your information below. </p>
      <form action="auto_registration_page.jsp" method="post" name="form1" id="form1">
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
    <% if (schema_id == null) { %>
          <tr>
            <td>Organizational Database: (?)</td>
              <td>
              <select name="selected_schema">
			  <%
			  	
				List ghostList = SchemaInformationObject.getAll();
			  
			  	for (ListIterator<SchemaInformationObject> li = ghostList.listIterator(); li.hasNext();) {
            		SchemaInformationObject this_sg = (SchemaInformationObject) li.next();
				%>
				<option value="<%= this_sg.getSchema_name() %>"><%= this_sg.getSchema_organization() %></option>
			<% } %>
              </select>
              
              </td>
            </tr>
     <% } else { %>
     	<tr>
            <td>Organizational Database: (?)</td>
              <td>
              <input type=hidden name="selected_schema" value="<%= schema_id %>">
				look up schema name	
              
              </td>
            </tr>
     <% }  // end if if schema_id was null %>
          <tr>
            <td>&nbsp;</td>
              <td>
                
                <input type="hidden" name="sending_page" value="create_users" /> 
                
                <input type="submit" name="command" value="Register" tabindex="6" />			</td>
            </tr>
          </table>
        </form>
      <p>&nbsp;</p>
      <p>&nbsp;</p>      <blockquote>&nbsp;</blockquote>			</td>
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
