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

		ContestParticipatingOrganization cpo = ContestParticipatingOrganization.getCPO(request.getSession(true));

	Contest contest = Contest.getContest(request);


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
.redstar {
	color: #F00;
}
-->
</style>
</head>
<body onLoad="">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="<%= contest.getContestLogo() %>" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"><h1 class="header">&nbsp;<%= contest.getContestName() %>    </h1></td>

  </tr>
  <tr>
  	<td width="120" align="right" valign="top">&nbsp;</td>
    <td colspan="1" valign="top"><br /></td>
		</td>
  </tr>
</table>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			  <h1>Contest<a href="../simulation_facilitation/helptext/create_running_sim_help.jsp" target="helpinright"></a> Registration (Continued)</h1>
			  <br />
            <blockquote>
              <p>Thank You for completing the first part of registering for this competition. The information you entered has been sent to the Contest Manager, and you will receive a confirmation email.</p>
              <p>Below is additional information that you will need regarding getting registered for this contest:</p>
              <table width="100%" border="1">
                <tr>
                  <td>&nbsp;</td>
                </tr>
              </table>
              <p>&nbsp;</p>
              
              <p>After all steps in the registration process of been completed,  you wlll receive several emails:</p>
              </blockquote>
            <ul>
              <li>You will receive and administrator login and password that can be used to access the platform and monitor the status of your team.</li>
              <li>You will receive one email per team that you have entered that you can forward on to the members of that team. In this email they will have a link they can use to register themselves on the platform.</li>
            </ul>
            <blockquote>
              <p>After your students have registered and the contest has begun, they will be able to enter into the platform and create simulations.</p>
  <p>&nbsp;</p>
  <p>&nbsp;</p>
              
            </blockquote>
            <p align="center">&nbsp;</p>
<p>&nbsp;</p>
</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">.</p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
<%
%>
