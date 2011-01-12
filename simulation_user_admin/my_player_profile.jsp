<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	String error_msg = "";
	
	SessionObjectBase sob = USIP_OSP_Util.getSessionObjectBaseIfFound(request);
	
	if ((sob == null) || (!(sob.isLoggedin()))) {
		response.sendRedirect("../simulation/index.jsp");
		return;
	}
	
	User user = sob.giveMeUser();
	
	String sending_page = request.getParameter("sending_page");
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("my_profile_language"))) {
		String language_id = request.getParameter("language_id");
		sob.languageCode = new Long(language_id).intValue();
		
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform My Player Profile Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
</head>
<body onLoad="">
<BR />
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>My Profile </h1>
              <br />
  <table width="80%" border="0" cellspacing="2" cellpadding="1">
    <tr>
      <td valign="top">Full Name:</td>
      <td valign="top">
<%= user.getBu_full_name() %>       </td>
    </tr>
    <tr>
      <td valign="top">First Name:</td>
      <td valign="top">

          <%= user.getBu_first_name() %></td>
    </tr>
    <tr>
      <td valign="top">Middle Name:</td>
      <td valign="top">
          <%= user.getBu_middle_name() %></td>
    </tr>
    <tr>
      <td valign="top">Last Name:</td>
      <td valign="top">
<%= user.getBu_last_name() %></td>
    </tr>

    <tr>
      <td valign="top">Email Address:</td>
      <td valign="top"><%= sob.user_name %></td>
    </tr>
    <tr>
      <td valign="top">Password</td>
      <td valign="top"><a href="change_password.jsp">Change Password </a></td>
    </tr>
    <tr>
      <td valign="top">Language:</td>
      <td valign="top">
      <%
		
		String checkedEnglish = "";
		String checkedSpanish = "";
		
		if (sob.languageCode == UILanguageObject.ENGLISH_LANGUAGE_CODE){
			System.out.println("its in english");
			checkedEnglish = " selected=\"selected\" ";
		} else if (sob.languageCode == UILanguageObject.SPANISH_LANGUAGE_CODE) {
			checkedSpanish = " selected=\"selected\" ";
			System.out.println("its in spanish");
		} else {
			System.out.println("its in unknown");
		}
	  %>
      <form id="form1" name="form1" method="post" action="my_player_profile.jsp">
      <input type="hidden" name="sending_page" value="my_profile_language" />
        <label>
          <select name="language_id" id="select">
            <option value="1" <%= checkedEnglish %>>English</option>
            <option value="2" <%= checkedSpanish %>>Spanish</option>
          </select>
          </label>
        <label>
        <input type="submit" name="button" id="button" value="Change Language" />
        </label>
      </form>      </td>
    </tr>
  </table>
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