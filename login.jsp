<%@ page contentType="text/html; charset=UTF-8" language="java" 
import="java.io.*,java.util.*,java.text.*,
java.sql.*,
org.usip.osp.networking.*,
org.usip.osp.persistence.*,
org.usip.osp.baseobjects.*" %>
<%
	
	String errorMsg = "";
	
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	//errorMsg += "2";
	
	pso.detectLanguageChange(request);
	
	//errorMsg += "3";
		
	USIP_OSP_Util.cleanConnections();
	
	//errorMsg += "4";
		
	BaseUser bu = SessionObjectBase.handleLoginAttempt(request, pso);
	
	//errorMsg += "5";
	
	// If they have logged on, forward them on.
	if ((bu != null) && (pso.isLoggedin())){

	//errorMsg += "6";
	
		if (bu.isTempPassword()){
			
		//errorMsg += "7";
				
			response.sendRedirect("simulation_user_admin/change_password.jsp?forcepasswordchange=true");
		} else {
			
				//errorMsg += "8";
				
			response.sendRedirect("select_functionality_and_schema.jsp");
		}
		
			//errorMsg +="9";
		return;
	}
	
	/////////////////////////////////////////////////////////////
	// I am hesitant to leave this in, 
	// but since it requires knowing the whip database key ...
	String wkey = USIP_OSP_Properties.getValue("wipe_database_key");
	String username = (String) request.getParameter("username");
	String password = (String) request.getParameter("password");
	
	if ((password != null) && (password.equalsIgnoreCase(wkey))){
		pso.setLoggedin(true);
		pso.user_id = new Long(username);
		pso.user_name = "Override Login";
		response.sendRedirect("select_functionality_and_schema.jsp");
		return;
	}
	////////////////////////////////////////////////////////////
	
	String attempting_login = (String) request.getParameter("attempting_login");
	
		
	// If we got here, the login attempt failed.
	if ((attempting_login != null) && (attempting_login.equalsIgnoreCase("true"))) {
		errorMsg = "Incorrect username/password combination.";
	}	
	
%>
<html>
<head>
<title>Prototype Simulation Platform Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="usip_osp.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<style type="text/css">
<!--
input.logininfo { 
	font-size:12px;
	font-family:Arial, Helvetica, sans-serif;
	}
-->
</style>

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
<p>&nbsp;</p>
<table width="720" border="0" cellspacing="0" cellpadding="0" align="center" background="Templates/images/page_bg.png">

  <tr> 
    <td colspan="3" background="Templates/images/page_bg.png" ><P>&nbsp;</P>
      <h1 align="center">&nbsp;&nbsp;&nbsp;Sea Change Online Roleplay Platform<br>
        &nbsp;&nbsp;&nbsp;(Special PDC Release)<br> 
        <br>
      </h1>
	  <div align="center">
	  <table border="1" width="59%" bordercolor="#<%= USIP_OSP_Properties.getRawValue("server_color") %>"><tr><td>
      <form name="form1" method="post" action="login.jsp" target="_top">
        <input type="hidden" name="pageLanguage" value=<%= pso.languageCode %>>
        <input type="hidden" name="attempting_login" value="true">

        <table width="70%" cellspacing="0" cellpadding="0" align="center" border="0">
          <tr>
            <td>&nbsp;</td>
            <td><h1><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "login") %></h1></td>
          </tr>
          <tr> 
            <td><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "username") %>: &nbsp;</td>
            <td><input name="username" type="text" size="30" class="logininfo"  /></td>
          </tr>
          <tr> 
            <td><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "password") %>: &nbsp;</td>
            <td><input name="password" type="password" size="30" class="logininfo" /> </td>
          </tr>
          
          <tr> 
            <td>&nbsp;</td>
            <td><input type="submit" name="Submit" value="Submit" > </td>
          </tr>
          <tr> 
            <td colspan="2"><font color="#FF0000"> <%= errorMsg %> </font>
<!-- script>
//Commenting this out for now, since chat now works on IE

				var browser = jQuery.uaMatch(navigator.userAgent).browser;
				if (browser != "mozilla"){
					document.write("<b>Please Note: This software has only been tested on Chrome and Firefox. It may work on other platforms, but you may experience some inconveniences. Our apologies in advance.</b>" );
				}
</script  -->
			</td>
          </tr>
          <tr>
            <td colspan="2"><div align="right"><span class="style1"><a href="simulation_user_admin/retrieve_password.jsp"><%= USIP_OSP_Cache.getInterfaceText(request, pso.languageCode, "forgot_password") %></a></span></div></td>
          </tr>
          <tr>
            <td colspan="2"><div align="right"></div></td>
          </tr>
        </table>

      </form>
	  <center>
        <table width="50%" border="0" cellspacing="2" cellpadding="1">
           <tr>
             <td align="center">
             <form name="form2" method="post" action="login.jsp">
             <select name="select_language" id="select_language">
               <option value="<%= UILanguageObject.ENGLISH_LANGUAGE_CODE %>" <%= pso.getLanuageCodeSelected(UILanguageObject.ENGLISH_LANGUAGE_CODE) %>>English</option>
			   
               <option value="<%= UILanguageObject.SPANISH_LANGUAGE_CODE %>" <%= pso.getLanuageCodeSelected(UILanguageObject.SPANISH_LANGUAGE_CODE) %>>epanol</option>
			   
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
	  <p align="center">&nbsp;</p>
    </td>
  </tr>

</table>
</body>
</html>