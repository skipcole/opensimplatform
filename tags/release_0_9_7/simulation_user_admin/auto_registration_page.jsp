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
	
	SessionObjectBase sob = USIP_OSP_Util.getSessionObjectBase(request);
	
	User userOnScratchPad = sob.handleAutoRegistration(request);
	
	if(sob.forward_on){
		response.sendRedirect("auto_registration_thankyou.jsp");
		sob.forward_on = false;
		return;
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
			
      <p>To participate in online simulations, or to author or instruct simulations using this sytem, you will need to be registered on it.</p>
      <p><% if (false) { %></p>
      <p>if registering for a contest</p>
      <blockquote>
        <p>if email address on link found</p>
        <blockquote>
          <p>look up user and confirm registation for Team X in Contest A</p>
          </blockquote>
        <p>else</p>
        <blockquote>
          <p>if you already are registered on this platform, and would like to get added onto Team X in Contest A, please enter your username and password. </p>
          <p>Otherwise, fill in the registration information below.</p>
        </blockquote>
      </blockquote>
      <p>end of if registering for contest</p>
      <p><% } %></p>
      <p align="center" class="style1"><%= sob.errorMsg %></p>
	  	<%
			sob.errorMsg = "";
	
		%>
      <form action="auto_registration_page.jsp" method="post" name="form1" id="form1">
      <input type="hidden" name="schema" value="<%= schema %>" />
      
        <table width="80%" border="0" cellspacing="0" cellpadding="0">
          
          <tr>
            <td valign="top"><strong>Username/Email <a href="../simulation_user_admin/helptext/user_email.jsp" target="helpinright">(?):</a></strong></td>
              <td valign="top"><input name="email" type="text" tabindex="1" value="<%= userOnScratchPad.getBu_username() %>" size="60"   /></td>
            </tr>
          <tr>
            <td valign="top"><strong>Confirm Email <a href="helptext/confirm_email.jsp" target="helpinright">(?)</a>: </strong></td>
            <td valign="top"><input type="text" name="confirm_email" tabindex="2" value="<%= userOnScratchPad.getBu_username() %>" size="60" ></td>
          </tr>
          <tr>
            <td valign="top"><strong>Password<a href="../simulation_user_admin/helptext/user_password.jsp" target="helpinright"> (?):</a></strong></td>
              <td valign="top"><input type="text" name="password" tabindex="3"  size="60"   /></td>
            </tr>
          <tr>
            <td valign="top"><strong>Confirm Password <a href="helptext/confirm_password.jsp" target="helpinright">(?)</a>: </strong></td>
            <td valign="top"><input type="text" name="confirm_password" tabindex="4" size="60" ></td>
          </tr>
          <tr>
            <td valign="top"><strong>First Name <a href="helptext/first_name.jsp" target="helpinright">(?)</a>:</strong></td>
      <td valign="top">
        <label>
          <input type="text" name="first_name" tabindex="5"  id="first_name" value="<%= userOnScratchPad.getBu_first_name() %>"  size="60"   />
          </label></td>
    </tr>
          <tr>
            <td valign="top"><strong>Middle Name <a href="helptext/middle_name.jsp" target="helpinright">(?)</a>:</strong></td>
      <td valign="top">
        <label>
          <input type="text" name="middle_name" tabindex="6"  id="middle_name" value="<%= userOnScratchPad.getBu_middle_name() %>"    size="60"  />
          </label></td>
    </tr>
          <tr>
            <td valign="top"><strong>Last Name <a href="helptext/last_name.jsp" target="helpinright">(?)</a>:</strong></td>
      <td valign="top">
        <label>
          <input type="text" name="last_name" tabindex="7" id="last_name" value="<%= userOnScratchPad.getBu_last_name() %>"  size="60"     />
          </label></td>
    </tr>
    <% if (sob.sioSet) { %>
		<input type="hidden" name="schema" value="<%= sob.schema %>" />
		
	     	<tr>
            <td valign="top"><strong>Organizational Database: <a href="helptext/org_database.jsp" target="helpinright">(?):</a></strong></td>
              <td valign="top">
              <%= this_sg.getSchema_organization() %>
			</td>
            </tr>
        
     <% } else { %>
<tr>
            <td valign="top"><strong>Organizational Database <a href="helptext/org_database.jsp" target="helpinright">(?)</a>:</strong></td>
              <td valign="top">
              <select name="schema" tabindex="8">
			  <%
			  	
				List ghostList = SchemaInformationObject.getAll();
			  
			  	for (ListIterator<SchemaInformationObject> li = ghostList.listIterator(); li.hasNext();) {
            		SchemaInformationObject this_sg = (SchemaInformationObject) li.next();
				%>
				<option value="<%= this_sg.getSchemaName() %>"><%= this_sg.getSchema_organization() %></option>
			<% } %>
              </select>              </td>
			</tr>  
     <% }  // end if if schema_id was null %>
          <tr>
            <td valign="top"><strong>
Captcha Code <a href="helptext/captcha_code.jsp"  target="helpinright">(?)</a>: </strong></td>
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
                <input type="hidden" name="ua_id" value="<%= sob.uaId %>" />
                <input type="hidden" name="uri_id" value="<%= sob.uriId %>" />
                <input type="hidden" name="ct_id" value="<%= sob.ctId %>" />
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