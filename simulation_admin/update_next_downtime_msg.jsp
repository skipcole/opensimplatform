<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*" 
	errorPage="" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	afso.handleSetNextDowntime(request);

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
}
-->
</style>
</head>
<body onLoad="">

<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3">
        <h1>
          Update Next Planned Downtime Message</h1>

        <blockquote>
          <p>The 'Next Planned Downtime' message can be set in the properties file, or set here.</p>
          <p>Your current 'next downtime message' is: </p>
          </p>
          <form id="form1" name="form1" method="post" action="update_next_downtime_msg.jsp">
          <input type="hidden" name="send_page" value="change_downtime" />
          <table width="100%" border="1" cellspacing="0">
            <tr>
              <td width="19%" valign="top">Message:</td>
              <td width="81%" valign="top">
                <label>
                  <textarea name="new_planned" id="new_planned" cols="45" rows="5"><%= USIP_OSP_Properties.getNextPlannedDowntime() %></textarea>
                </label>              </td>
            </tr>
            <tr>
              <td valign="top">NB:</td>
              <td valign="top">Changes made to the 'next down time' here will disappear if the web server is rebooted. This functionality really only exists to put unplanned down time events, such as 'We are bringing the system down on the night of 01/03/15 (Saturday) for emergency maintenance.'</td>
            </tr>
            <tr>
              <td valign="top">Change</td>
              <td valign="top"><label>
                <input type="submit" name="button" id="button" value="Submit" />
              </label></td>
            </tr>
          </table>
          </form>
      </blockquote>
        <p><a href="simulation_admin.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a></p>
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
<%
	
%>
