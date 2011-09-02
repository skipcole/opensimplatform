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

	String cpo_id = (String) request.getParameter("cpo_id");	
	ContestParticipatingOrganization cpo = ContestParticipatingOrganization.getById(cpo_id);
	

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
			  <h1>Contest<a href="../simulation_facilitation/helptext/create_running_sim_help.jsp" target="helpinright"></a> Participating Organization</h1>
			  <br />
            <blockquote>
              <p><font color="#FF0000"><%= cpo.getErrorMsg() %></font></p>
         
              
              <table width="100%" border="0">
                <tr>
                  <td colspan="3"><strong>You</strong></td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Your First Name <span class="redstar">*</span></td>
                  <td><%= cpo.getRegistrantFirstName() %></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Your Last Name <span class="redstar">*</span></td>
                  <td><%= cpo.getRegistrantLastName() %></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Your Email Address <span class="redstar">*</span></td>
                  <td><%= cpo.getRegistrantEmailAddress() %></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Verify Email Address <span class="redstar">*</span></td>
                  <td><%= cpo.getRegistrantEmailAddress2() %></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Your Phone Number</td>
                  <td><%= cpo.getPhoneNumber() %></td>
                </tr>
                <tr>
                  <td colspan="3"><strong>Your Organization</strong></td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Organization Name <span class="redstar">*</span></td>
                  <td><%= cpo.getOrganizationName() %></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Department Name (if applicable)</td>
                  <td><%= cpo.getDepartmentName() %></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Division Name (if applicable)</td>
                  <td><%= cpo.getDivisionName() %></td>
                </tr>
                <tr>
                  <td colspan="3"><strong>Your Address at Your Organization</strong></td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Address Line 1</td>
                  <td><%= cpo.getAddressLine1() %></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Address Line 2</td>
                  <td><%= cpo.getAddressLine2() %></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>City</td>
                  <td><%= cpo.getCity() %></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>State/Province</td>
                  <td><%= cpo.getState() %></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Postal Code</td>
                  <td><%= cpo.getPostalCode() %></td>
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
                  <td>
                  </td>
                </tr>
              </table>
              <p>Notes</p>
              <p>Button to send First Email (to add more admins)</p>
              <p>Button to send Second Email (to add more team members)</p>
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
</p>
</body>
</html>