<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.coursemanagementinterface.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
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
	String uri_id = (String) request.getParameter("uri");
	String initial_entry = (String) request.getParameter("initial_entry");
	
	
	SchemaInformationObject sio = new SchemaInformationObject();
	UserRegistrationInvite uri = new UserRegistrationInvite();
	
	if ((schema_id != null) && (!(schema_id.equalsIgnoreCase("null")))) {
		sio = SchemaInformationObject.getById(new Long(schema_id));
		
		if ((uri_id != null) && (!(uri_id.equalsIgnoreCase("null")))) {
			uri = UserRegistrationInvite.getById(sio.getSchema_name(), new Long(uri_id));
		
			if (initial_entry != null) {
				userOnScratchPad.setBu_username(uri.getOriginalInviteEmailAddress());
			}
		}
	
	}
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Auto Registration Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(../Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
.style1 {color: #FF0000}
-->
</style>
</head>
<body onLoad="">
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
<BR />
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <p>&nbsp;</p>
              <h1>Self Registration Page </h1>
              <br />
			
      <p>To participate in online simulations using this sytem, you will need to be registered in the system.</p>

      <div align="center"><table width="70%" border="1"><tr><td>
        <p><strong>Please Note:</strong><br/>
Registering on the sytem may not give you immediate access to any simulations. An instructor will need to assign you a role in an ongoing simulation before you can participate in one.</p>
		</td></tr></table></div>

      <p align="center" class="style1"><%= afso.errorMsg %></p>
      <form action="auto_registration_page.jsp" method="post" name="form1" id="form1">
        <table width="80%" border="0" cellspacing="0" cellpadding="0">
          
          <tr>
            <td valign="top">Username/Email <a href="../simulation_user_admin/helptext/user_email.jsp" target="helpinright">(?):</a></td>
              <td valign="top"><input name="email" type="text" tabindex="1" value="<%= userOnScratchPad.getBu_username() %>" size="60"   /></td>
            </tr>
          <tr>
            <td valign="top">Confirm Email <a href="helptext/confirm_email.jsp" target="helpinright">(?)</a>: </td>
            <td valign="top"><input type="text" name="confirm_email" tabindex="2" value="<%= userOnScratchPad.getBu_username() %>" size="60" ></td>
          </tr>
          <tr>
            <td valign="top">Password<a href="../simulation_user_admin/helptext/user_password.jsp" target="helpinright"> (?):</a></td>
              <td valign="top"><input type="text" name="password" tabindex="3"  size="60"   /></td>
            </tr>
          <tr>
            <td valign="top">Confirm Password <a href="helptext/confirm_password.jsp" target="helpinright">(?)</a>: </td>
            <td valign="top"><input type="text" name="confirm_password" tabindex="4" size="60" ></td>
          </tr>
          <tr>
            <td valign="top">First Name <a href="helptext/real_name.jsp" target="helpinright">(?)</a>:</td>
      <td valign="top">
        <label>
          <input type="text" name="first_name" tabindex="5"  id="first_name" value="<%= userOnScratchPad.getBu_first_name() %>"  size="60"   />
          </label></td>
    </tr>
          <tr>
            <td valign="top">Middle Name <a href="helptext/real_name.jsp" target="helpinright">(?)</a>:</td>
      <td valign="top">
        <label>
          <input type="text" name="middle_name" tabindex="6"  id="middle_name" value="<%= userOnScratchPad.getBu_middle_name() %>"    size="60"  />
          </label></td>
    </tr>
          <tr>
            <td valign="top">Last Name <a href="helptext/real_name.jsp" target="helpinright">(?)</a>:</td>
      <td valign="top">
        <label>
          <input type="text" name="last_name" tabindex="7" id="last_name" value="<%= userOnScratchPad.getBu_last_name() %>"  size="60"     />
          </label></td>
    </tr>
    <% if ((schema_id != null) && (!(schema_id.equalsIgnoreCase("null") ) ) ) { %>
	
	     	<tr>
            <td valign="top">Organizational Database: <a href="helptext/org_database.jsp" target="helpinright">(?):</a></td>
              <td valign="top">
              <input type=hidden name="schema_id" value="<%= schema_id %>">
			  <input type=hidden name="uri" value="<%= uri.getId() %>">
			<%= sio.getSchema_organization() %></td>
            </tr>
            
     <% } else { %>
<tr>
            <td valign="top">Organizational Database <a href="helptext/org_database.jsp" target="helpinright">(?)</a>:</td>
              <td valign="top">
              <select name="schema_id" tabindex="8">
			  <%
			  	
				List ghostList = SchemaInformationObject.getAll();
			  
			  	for (ListIterator<SchemaInformationObject> li = ghostList.listIterator(); li.hasNext();) {
            		SchemaInformationObject this_sg = (SchemaInformationObject) li.next();
				%>
				<option value="<%= this_sg.getId() %>"><%= this_sg.getSchema_organization() %></option>
			<% } %>
              </select>              </td>
			</tr>  
     <% }  // end if if schema_id was null %>
          <tr>
            <td valign="top">
Captcha Code <a href="helptext/captcha_code.jsp"  target="helpinright">(?)</a>: </td>
            <td valign="top">
              <input name="captchacode" type="text" size="4" maxlength="4" tabindex="9" />
            
			<script>
 function resetCaptcha()
 {
  document.getElementById('imgCaptcha').src = 'captchaimage.jsp?' + Math.random();
 }
</script>
<div>
<img src="captchaimage.jsp" id="imgCaptcha">
<img src="refresh.jpg" border=0 id="imgRefresh" onclick="setTimeout('resetCaptcha()', 300); return false;"></div></td>
          </tr>
          <tr>
            <td valign="top">&nbsp;</td>
              <td valign="top">
                
                <input type="hidden" name="sending_page" value="create_users" /> 
                
                <input type="submit" name="command" value="Register" tabindex="10"   />			</td>
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
