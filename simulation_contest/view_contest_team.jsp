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
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	String ct_id = (String) request.getParameter("ct_id");
	
	ContestTeam ct = new ContestTeam();
	Contest contest = new Contest();
	ContestParticipatingOrganization cpo = new ContestParticipatingOrganization();
	
	if ((ct_id != null) && (!( ct_id.equalsIgnoreCase("null") ))){
		ct = ContestTeam.getById(ct_id);
		contest = Contest.getById(ct.getContestId());
		cpo = ContestParticipatingOrganization.getById(ct.getContestParticipatingOrgId());
	}
	
	String additionNotes = ContestTeamMember.handleContestTeamAddition(request);
	
	 ContestTeamMember.handleConfirmUser(request);

	

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

              <table width="100%" border="1">
                <tr>
                  <td><strong>Team</strong></td>
                  <td><strong><%= ct.getTeamName() %></strong></td>
                </tr>
                <tr>
                  <td><strong>Contest</strong></td>
                  <td><strong><%= contest.getContestName() %></strong></td>
                </tr>
                <tr>
                  <td><strong>Contest Participating Organization</strong></td>
                  <td><strong><%= cpo.getOrganizationName() %></strong></td>
                </tr>
                <tr>
                  <td><strong>Instructor Code</strong></td>
                  <td><strong><%= ct.getTeamAdminRegistrationCode() %></strong></td>
                </tr>
                <tr>
                  <td><strong>Student Code</strong></td>
                  <td><strong><%= ct.getTeamStudentRegistrationCode() %></strong></td>
                </tr>
                <tr>
                  <td><strong>Schema</strong></td>
                  <td><strong><%= ct.getTeamSchema() %></strong></td>
                </tr>
              </table>
              <p>&nbsp;</p>
              <h2>Team Members</h2>
              <blockquote>
                <h3>Confirmed Members</h3>
              
                <ol>
                  <% 
				List contestTeamMembersConfirmed = ContestTeamMember.getAllConfirmedTeamMembers(ct.getId(), true);
				
				for (ListIterator li = contestTeamMembersConfirmed.listIterator(); li.hasNext();) {
					ContestTeamMember theContestTeamMember = (ContestTeamMember) li.next();
					BaseUser bu = BaseUser.getByUserId(theContestTeamMember.getUserId());
				%>
                  <li><%= bu.getFull_name() %>, <%= bu.getUsername() %> </li>
                  <% } %>
                </ol>
                <h3>Unconfirmed Members</h3>
              
                <ol>
                  <% 
				List contestTeamMembersUn = ContestTeamMember.getAllConfirmedTeamMembers(ct.getId(), false);
				
				for (ListIterator li = contestTeamMembersUn.listIterator(); li.hasNext();) {
					ContestTeamMember theContestTeamMember = (ContestTeamMember) li.next();
					BaseUser bu = BaseUser.getByUserId(theContestTeamMember.getUserId());
				%><form id="form2" name="form2" method="post" action="view_contest_team.jsp">
              <input type="hidden" name="contest_id" value="<%= contest.getId() %>" />
              <input type="hidden" name="cpo_id" value="<%= cpo.getId() %>" />
              <input type="hidden" name="ct_id" value="<%= ct.getId() %>" />
              <input type="hidden" name="ctm_id" value="<%= theContestTeamMember.getId() %>" />
              <input type="hidden" name="sending_page" value="confirm_contest_member" />
                  <li><%= bu.getFull_name() %>, <%= bu.getUsername() %> 
                  <input type="submit" name="button2" id="button2" value="Confirm" />
                  </li>
                  </form>
                  <% } %>
                </ol>
                </blockquote>
                <p>&nbsp; <%= additionNotes %> </p>
                <h2>Add Team Member</h2>
              <blockquote>
              <form id="form1" name="form1" method="post" action="view_contest_team.jsp">
              <input type="hidden" name="contest_id" value="<%= contest.getId() %>" />
              <input type="hidden" name="cpo_id" value="<%= cpo.getId() %>" />
              <input type="hidden" name="ct_id" value="<%= ct.getId() %>" />
              <input type="hidden" name="sending_page" value="new_contest_member" />
                <table width="100%" border="1">
                  <tr>
                    <td>User Email</td>
                    <td>
                      <input type="text" name="user_email" id="user_email" />
                    </td>
                  </tr>
                  <tr>
                    <td>Team Admin</td>
                    <td>Yes
                      <input type="radio" name="team_admin" id="radio" value="true" />
/ No
<input type="radio" name="team_admin" id="radio2" value="false" /></td>
                  </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <td><input type="submit" name="button" id="button" value="Create" /></td>
                  </tr>
                </table>
                </form>
                <p>&nbsp;</p>
              </blockquote>
              <p>Button to send First Email (to add more admins)</p>
              <blockquote>
                <p>Welcome. To check on the members of your team, go to this web site. To allow another player to register as an Team administrator, got to this web site.</p>
                <p>xxxxx : <%= ct.getTeamSchema() %> </p>
                <p>autoreg?ct_id=x&amp;instuctor_code=xxx&amp;</p>
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
