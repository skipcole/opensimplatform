<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*" 
	errorPage="/error.jsp" %>

<%

		InstallationObject installObject = InstallationObject.getInstallationObject(request.getSession(true));
	
	int checkInstall = InstallationObject.checkInstall(request);
	
	if (checkInstall != 0) {
		response.sendRedirect("index.jsp?reason_failed=" + checkInstall);
		return;
	}
	
	String error_msg = installObject.handleCreateRootDB(request);
	
	if (installObject.forward_on){
		installObject.forward_on = false;
		response.sendRedirect(installObject.backPage);
		return;
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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
.style2 {color: #000000}
.style3 {color: #FF0000}
-->
</style>
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform Installation</h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"></td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top"></td>
    <td colspan="1" valign="top"></td>
    <td width="194" align="right" valign="top"></td>
  </tr>
</table>
<br />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3"> <form action="install_root_db.jsp" method="post" name="form1" id="form1">
        <h1>
          <input type="hidden" name="sending_page" value="install_root_db" />
          Install Root Database </h1>
        <blockquote>&nbsp;</blockquote>
        <table width="80%" border="0" cellspacing="2" cellpadding="2">
          <tr>
            <td valign="top"><p>Wipe Database Key</p>
            <p class="style2">(The key you enter must match the corresponding key located in the properties file. This is to prevent accidental database eradication.)</p></td>
            <td valign="top"><label>
              <input type="text" name="wipe_database_key" id="textfield" />
            </label></td>
          </tr>
          <tr> 
            <td valign="top">Warning. Hitting submit will purge the root database.</td>
            <td><input type="submit" name="installrootdb" value="Submit" /></td>
          </tr>
        </table>
        
        <p align="center" class="style3"><%= error_msg %> &nbsp;</p>
      </form>
      <p>&nbsp;</p>
      <p>&nbsp;</p></td>
  </tr>
  <tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
</table>
<p>&nbsp;</p>

<p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP 
  Open Source Software Project</a>. </p>
</body>
</html>