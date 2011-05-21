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
	InstructorApplication iaObject = InstructorApplication.sendEmailAndSave(request, sob);
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Instructor Application</title>

<link href="usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
.style1 {color: #FF0000}
-->
</style>
</head>
<body onLoad="">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="Templates/images/top_fade.png"><h1 class="header">&nbsp;USIP Open Simulation Platform </h1></td>
    <td align="right" background="Templates/images/top_fade.png" width="20%"> 

	  <div align="center"></div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="Templates/images/logo_bot.png" width="120" height="20" /></td>
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
			<td width="120"><img src="Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			<% if (iaObject.isEmailSent() == true) {  %>
			  <h1>Application Submitted!</h1>
			  <p>Your application email has been sent to the adminstrator. You should also receive a copy.</p>
              <p>Thank You!</p>
              <p>&nbsp;</p>
              <p>&nbsp;</p>
              <% } %>
              <h1>Instructor Application  </h1>
              <br />
			
      <p>Please complete the form below to send an email to the adminster of this system indicating that you would like to be registered on this system and made an instructor. You will also be cc'd on this email.</p>
      <p>If your application is accepted and your made an instructor on this system, you will then be able to enroll your students in simulations hosted here and conduct games. It is highly recommended that before completing this application that you have completed the <a href="simulation_tutorials/index.jsp">instructor/facilitator tutorial</a>. </p>
     <p align="center" class="style1"><%= sob.errorMsg %></p>
	  	<%
			sob.errorMsg = "";
			sob.errorCode = SessionObjectBase.ALL_GOOD;
	
		%>
      <p align="center" class="style1"></p>
      <form action="instructor_application.jsp" method="post" name="form1" id="form1">
<table width="80%" border="1" cellspacing="0" cellpadding="0">
          
	<tr>
            <td valign="top"><strong>Name?</strong></td>
      <td valign="top">
        <label>
          <input type="text" name="applicant_name" tabindex="1"  id="applicant_name" value="<%= iaObject.getApplicantName() %>"  size="60"   />
          </label></td>
    </tr>
          <tr>
            <td valign="top"><strong>Email?</strong></td>
              <td valign="top"><input name="applicant_email" type="text" tabindex="2" value="<%= iaObject.getApplicantEmailAddress() %>" size="60"   /></td>
            </tr>
     <tr>
       <td valign="top"><strong>A little about yourself?</strong></td>
       <td valign="top"><label>
         <textarea tabindex="3" name="applicant_background"><%= iaObject.getApplicantBackground() %></textarea>
       </label></td>
     </tr>
     <tr>
       <td valign="top"><strong>Where do you see yourself running the simulation? </strong></td>
       <td valign="top"><textarea tabindex="4" name="applicant_desires"><%= iaObject.getApplicantDesiredUse() %></textarea></td>
     </tr>  
          <tr>
            <td valign="top"><strong>
Captcha Code?</strong></td>
            <td valign="top">
              <input name="captchacode" type="text" size="4" maxlength="4" tabindex="5" />
            
			<script>
 function resetCaptcha()
 {
  document.getElementById('imgCaptcha').src = 'simulation_user_admin/captchaimage.jsp?' + Math.random();
 }
</script>
<div>
<img src="simulation_user_admin/captchaimage.jsp" id="imgCaptcha">
<img src="simulation_user_admin/refresh.jpg" border=0 id="imgRefresh" onclick="setTimeout('resetCaptcha()', 300); return false;"></div></td>
          </tr>
          <tr>
            <td valign="top">&nbsp;</td>
              <td valign="top">
                
                <input type="hidden" name="sending_page" value="instructor_application" /> 
                <input type="submit" name="command" value="Apply" tabindex="6"   />			</td>
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