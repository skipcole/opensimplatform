<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.persistence.*" 
	errorPage="/error.jsp" %>

<%
	
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	String ftab = (String) request.getParameter("ftab");
	
	String tabColorHome = "475DB0";
	String tabColorLibrary = "475DB0";
	String tabColorMySims = "475DB0";
	String tabColorMisc = "475DB0";
	
	if (ftab != null) {
		
		if (ftab.equalsIgnoreCase("home")){
			tabColorHome = "9AABE1";
		}
		if (ftab.equalsIgnoreCase("library")){
			tabColorLibrary = "9AABE1";
		}
		if (ftab.equalsIgnoreCase("my_sims")){
			tabColorMySims = "9AABE1";
		}
		if (ftab.equalsIgnoreCase("launch")){
			tabColorMySims = "9AABE1";
		}
		if (ftab.equalsIgnoreCase("misc")){
			tabColorMisc = "9AABE1";
		}
	
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



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
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
</head>
<body onLoad="">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform</h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
	    <table border="0" cellspacing="1" cellpadding="0">
		<tr>
          <td><div align="center"><a href="facilitateweb.jsp?ftab=home" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
        <tr>
          <td><div align="center"><a href="../simulation_user_admin/my_profile.jsp" target="bodyinleft" class="menu_item"><img src="../Templates/images/my_profile.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
        <tr>
          <td><div align="center"><a href="../logout.jsp" target="_top" class="menu_item"><img src="../Templates/images/logout.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
      </table>	  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0">
		<table border="0" cellpadding="0" cellspacing="0" >
		   <tr>
		   <td bgcolor="#<%= tabColorHome %>"><a href="facilitateweb.jsp?ftab=home" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;HOME&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td bgcolor="#<%= tabColorLibrary %>"><a href="facilitateweb.jsp?ftab=library" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;LIBRARY&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
				<td bgcolor="#<%= tabColorMySims %>"><a href="facilitateweb.jsp?ftab=my_sims" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;MY SIMS&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
				<td bgcolor="#<%= tabColorMisc %>"><a href="facilitateweb.jsp?ftab=misc" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;MISC TOOLS&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td></tr></table>
	
	</td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top"></td>
    <td colspan="1" valign="top"></td>
    <td width="194" align="right" valign="top"></td>
  </tr>
</table>
</body>
</html>