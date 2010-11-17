<%@ page contentType="text/html; charset=UTF-8" language="java" 
import="java.io.*,java.util.*,java.text.*,
java.sql.*,
org.usip.osp.networking.*,
org.usip.osp.persistence.*,
org.usip.osp.baseobjects.*" %>
<%
	String errorMsg = "";
	
	String select_language = request.getParameter("select_language");
	
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (select_language != null) {
		pso.languageCode = new Long(select_language).intValue();
	}
	
	String selectedEnglish = " selected=\"selected\" ";
	String selectedSpanish = "";
	
	if (pso.languageCode == 2) {
		selectedSpanish = " selected=\"selected\" ";
		selectedEnglish = "";
	}
	
	BaseUser bu = OSPSessionObjectHelper.handleLoginAttempt(request);
	
	if (bu != null){
		pso.languageCode = bu.getPreferredLanguageCode().intValue();
		response.sendRedirect("select_functionality_and_schema.jsp");
		return;
	}
	
	String attempting_login = (String) request.getParameter("attempting_login");
	// If we got here, the login attempt failed.
	if ((attempting_login != null) && (attempting_login.equalsIgnoreCase("true"))) {
		errorMsg = "Incorrect username/password combination.";
	}	
	
%>
<html>
<head>
<title>USIP Open Simulation Platform Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="usip_osp.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="third_party_libraries/jquery/jquery-1.4.1.js"></script>
<style type="text/css">
<!--
.style1 {font-size: small}
-->
</style>
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
</head>
<body onLoad="">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="Templates/images/top_fade.png"><h1 class="header">&nbsp;<%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "USIP_OSP_HEADER") %></h1></td>
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
<p>&nbsp;</p>
<table width="720" border="0" cellspacing="0" cellpadding="0" align="center" background="Templates/images/page_bg.png">

  <tr> 
    <td colspan="3" background="Templates/images/page_bg.png" ><P>&nbsp;</P>
      <h1 align="center">&nbsp;&nbsp;&nbsp;<%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "USIP_OSP_HEADER") %> <br>
        &nbsp;&nbsp;&nbsp;(Release <%= USIP_OSP_Properties.getRelease() %>)<br> 
        <br>
      </h1>
	  <div align="center">
	  <table border="1" width="59%" bordercolor="#<%= USIP_OSP_Properties.getRawValue("server_color") %>"><tr><td>
      <form name="form1" method="post" action="login.jsp" target="_top">
        <input type="hidden" name="pageLanguage" value=<%= pso.languageCode %>>
        <input type="hidden" name="attempting_login" value="true">

        <table width="58%" cellspacing="0" cellpadding="0" align="center" border="0">
          <tr>
            <td>&nbsp;</td>
            <td><h1><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "login") %></h1></td>
          </tr>
          <tr> 
            <td><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "username") %></td>
            <td> <input type="text" name="username"></td>
          </tr>
          <tr> 
            <td><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "password") %></td>
            <td> <input type="password" name="password"> </td>
          </tr>
          
          <tr> 
            <td>&nbsp;</td>
            <td> <input type="submit" name="Submit" value="Submit" > </td>
          </tr>
          <tr> 
            <td colspan="2"><font color="#FF0000"><%= errorMsg %></font>
									<script>
				var browser = jQuery.uaMatch(navigator.userAgent).browser;
				if (browser != "mozilla"){
					document.write("<b>Please Note: This software has only been tested on Firefox. It may work on other platforms, but you may experience some inconveniences. Our apologies in advance.</b>" );
				}
</script>
			</td>
          </tr>
          <tr>
            <td colspan="2"><div align="right"><span class="style1"><a href="simulation_user_admin/retrieve_password.jsp"><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "forgot_password") %></a></span></div></td>
          </tr>
          <tr>
            <td colspan="2"><div align="right"><a href="simulation_user_admin/auto_registration_form.jsp" class="style1">Register</a></div></td>
          </tr>
        </table>

      </form>
	  <center>
        <table width="50%" border="0" cellspacing="2" cellpadding="1">
           <tr>
             <td align="center">
             <form name="form2" method="post" action="login.jsp">
             <select name="select_language" id="selectlanguage">
               <option value="1" <%= selectedEnglish %>>English</option>
               <option value="2" <%= selectedSpanish %>>epanol</option>
             </select>
               
                 <label>
                   <input type="submit" name="button" id="button" value="Submit">
                 </label>
               </form>
			   
             </td>
           </tr>
           <tr>
            <td>Upcoming Planned Outage:<br /> <%= USIP_OSP_Properties.getNextPlannedDowntime() %></td>
          </tr>
          <tr> 
          <tr> 
            <td><a href="acknowledgements/index.htm">Acknowledgements</a></td>
          </tr>
        </table>
		</td></tr></table>
		</div>
	  </center>
      <p align="center">&nbsp;</p>
    </td>
  </tr>

</table>

<p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p>
</body>
</html>