<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	com.seachangesimulations.osp.contests.*,	
	org.hibernate.*" 
	errorPage="/error.jsp" %>
<%

		ContestParticipatingOrganization cpo = ContestParticipatingOrganization.getCPO(request.getSession(true));

	Contest contest = Contest.getContest(request);
	
	cpo.processInitialRegistration(request);
	
	if (cpo.isReadyToMoveToNextStep()){
		response.sendRedirect("register_contest_organization_step2.jsp");
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
.redstar {
	color: #F00;
}
-->
</style>
</head>
<body onLoad="">
<% 
	if (contest.getId() != null) {
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top">&nbsp;</td>
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
              <p>Please enter the information below:</p>
              <p><font color="#FF0000"><%= cpo.getErrorMsg() %></font></p>
              
              <form id="form1" name="form1" method="post" action="../simulation_contest/register_contest_organization.jsp">
              
              <input type="hidden" name="sending_page" value="contest_registration">
              <input type="hidden" name="contest_id" value="<%= contest.getId() %>">
              
              <table width="100%" border="0">
                <tr>
                  <td colspan="3"><strong>You</strong></td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Your First Name <span class="redstar">*</span></td>
                  <td><label for="first_name"></label>
                    <input type="text" name="first_name" id="first_name" value="<%= cpo.getRegistrantFirstName() %>" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Your Last Name <span class="redstar">*</span></td>
                  <td><label for="last_name"></label>
                    <input type="text" name="last_name" id="last_name" value="<%= cpo.getRegistrantLastName() %>" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Your Email Address <span class="redstar">*</span></td>
                  <td><label for="email_address"></label>
                    <input type="text" name="email_address" id="email_address" value="<%= cpo.getRegistrantEmailAddress() %>" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Verify Email Address <span class="redstar">*</span></td>
                  <td><label for="email_address2"></label>
                    <input type="text" name="email_address2" id="email_address2" value="<%= cpo.getRegistrantEmailAddress2() %>" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Your Phone Number</td>
                  <td><label for="phone_number"></label>
                    <input type="text" name="phone_number" id="phone_number" value="<%= cpo.getPhoneNumber() %>" /></td>
                </tr>
                <tr>
                  <td colspan="3"><strong>Your Organization</strong></td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Organization Name <span class="redstar">*</span></td>
                  <td><label for="org_name"></label>
                    <input type="text" name="org_name" id="org_name" value="<%= cpo.getOrganizationName() %>" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Department Name (if applicable)</td>
                  <td><label for="dept_name"></label>
                    <input type="text" name="dept_name" id="dept_name" value="<%= cpo.getDepartmentName() %>" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Division Name (if applicable)</td>
                  <td><label for="div_name"></label>
                    <input type="text" name="div_name" id="div_name" value="<%= cpo.getDivisionName() %>" /></td>
                </tr>
                <tr>
                  <td colspan="3"><strong>Your Address at Your Organization</strong></td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Address Line 1</td>
                  <td><label for="address_line1"></label>
                    <input type="text" name="address_line1" id="address_line1" value="<%= cpo.getAddressLine1() %>" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Address Line 2</td>
                  <td><label for="address_line2"></label>
                    <input type="text" name="address_line2" id="address_line2" value="<%= cpo.getAddressLine2() %>" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>City</td>
                  <td><label for="city"></label>
                    <input type="text" name="city" id="city" value="<%= cpo.getCity() %>" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>State/Province</td>
                  <td><label for="state_province"></label>
                    <input type="text" name="state_province" id="state_province" value="<%= cpo.getState() %>" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Postal Code</td>
                  <td><label for="postal_code"></label>
                    <input type="text" name="postal_code" id="postal_code" value="<%= cpo.getPostalCode() %>" /></td>
                </tr>
                <tr>
                  <td colspan="3">&nbsp;</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Captcha</td>
                  <td>              <input name="captchacode" type="text" size="4" maxlength="4" tabindex="9" />
            
<script>
 function resetCaptcha()
 {
  document.getElementById('imgCaptcha').src = '../simulation_user_admin/captchaimage.jsp?sent_from=contest_reg&' + Math.random();
 }
</script>
<div>
<img src="../simulation_user_admin/captchaimage.jsp?sent_from=contest_reg" id="imgCaptcha">
<img src="../simulation_user_admin/refresh.jpg" border=0 id="imgRefresh" onclick="setTimeout('resetCaptcha()', 300); return false;"></div></td>
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
              <p><span class="redstar">*</span> Indicates a required field.</p>
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
<p>
  <% } else { %>
</p>
<p>No Contest Selected.</p>
<p>
  <% } // end of contest id == null %>
 
 <%

	List contests = Contest.getAll();
	
	if (contests.size() > 0) {   %>

<p>Select a contest from the list below:</p>
<p>&nbsp;</p>

               <%
			
				for (ListIterator li = contests.listIterator(); li.hasNext();) {
					Contest theContest = (Contest) li.next();
				
		%>
        x
                        <%
			} // End of loop over contests.
			
			
	} else { // End of if there are contests.
	
		%>
        No contests found on this system. 
        
        <% } // end of no contests found on system. %>
</p>
</body>
</html>