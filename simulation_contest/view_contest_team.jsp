<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.coursemanagementinterface.*,	
	org.hibernate.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>USIP Open Simulation Platform</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			  <h1>View Contest<a href="../simulation_facilitation/helptext/create_running_sim_help.jsp" target="helpinright"></a> Team</h1>
			  <br />
            <blockquote>
              <p>Here you can create a contest to have multiple teams creating simulations. </p>
              <p>Contest:</p>
              <p>Contest Participating Organization:</p>
              <p>Team X - Instructor Code</p>
              <p>Team X - Student Code</p>
              <p>Button to send First Email (to add more admins)</p>
              <blockquote>
                <p>Welcome. To check on the members of your team, go to this web site. To allow another player to register as an Team administrator, got to this web site.</p>
              </blockquote>
              <p>Button to send Second Email (to add more team members)</p>
              <blockquote>
                <p>Welcome. Forward the email below to your students.</p>
                <p>Welcome Contestant.<br />
                  To participate in this contest you must be registered on this USIP OSP platform. If you don't already have an account, go to this web site. If you do, log into the platform, go to your profile page and enter in the contest code xxx. </p>
              </blockquote>
              <p></p>
            </blockquote>
            <p align="center">&nbsp;</p>
<p>&nbsp;</p>
</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
<%
%>
