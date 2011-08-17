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

	Contest contest = new Contest();
	
	ContestParticipatingOrganization cpo = ContestParticipatingOrganization.processInitialRegistration(request);
	
	if (cpo.readyToMoveToSecondStep()){
		response.sendRedirect("");
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
			  <h1>Contest<a href="../simulation_facilitation/helptext/create_running_sim_help.jsp" target="helpinright"></a> Registration</h1>
			  <br />
            <blockquote>
              <p>Please enter the information below:</p><form id="form1" name="form1" method="post" action="register_contest_organization.jsp">
              <table width="100%" border="0">
                <tr>
                  <td colspan="3"><strong>You</strong></td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Your First Name</td>
                  <td><label for="first_name"></label>
                    <input type="text" name="first_name" id="first_name" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Your Last Name</td>
                  <td><label for="last_name"></label>
                    <input type="text" name="last_name" id="last_name" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Your Email Address</td>
                  <td><label for="email_address"></label>
                    <input type="text" name="email_address" id="email_address" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Verify Email Address</td>
                  <td><label for="email_address2"></label>
                    <input type="text" name="email_address2" id="email_address2" /></td>
                </tr>
                <tr>
                  <td colspan="3"><strong>Your Organization</strong></td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Organization Name</td>
                  <td><label for="org_name"></label>
                    <input type="text" name="org_name" id="org_name" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Department Name (if applicable)</td>
                  <td><label for="dept_name"></label>
                    <input type="text" name="dept_name" id="dept_name" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Division Name (if applicable)</td>
                  <td><label for="div_name"></label>
                    <input type="text" name="div_name" id="div_name" /></td>
                </tr>
                <tr>
                  <td colspan="3"><strong>Your Address at Your Organization</strong></td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Address Line 1</td>
                  <td><label for="address_line1"></label>
                    <input type="text" name="address_line1" id="address_line1" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Address Line 2</td>
                  <td><label for="address_line2"></label>
                    <input type="text" name="address_line2" id="address_line2" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>City</td>
                  <td><label for="city"></label>
                    <input type="text" name="city" id="city" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>State/Province</td>
                  <td><label for="state_province"></label>
                    <input type="text" name="state_province" id="state_province" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Postal Code</td>
                  <td><label for="postal_code"></label>
                    <input type="text" name="postal_code" id="postal_code" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Any special instructions</td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="3">&nbsp;</td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>
                    <input type="submit" name="registration_submit" id="registration_submit" value="Submit" />
                  </td>
                </tr>
              </table>
              </form>
              <p>&nbsp;</p>
              <p>How many Teams will you want to register?</p>
              <p>&nbsp;</p>
              <p>After you have registed and paid the fee for each of your teams, you wlll receive one or more emails (one for each team) to forward out to the students that you want to register on each team.</p>
              <p>After your students have registered and the contest has begun, they will be able to enter into the platform and create simulations.</p>
              <p>(Move to the payment area.)</p>
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
