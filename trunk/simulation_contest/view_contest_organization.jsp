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

	String cpo_id = (String) request.getParameter("cpo_id");
		
	ContestParticipatingOrganization cpo = ContestParticipatingOrganization.getById(cpo_id);
	
	ContestTeam contestTeam = ContestTeam.handleCreateTeam(request);
	
	String contest_id = (String) request.getParameter("contest_id");
	

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
              
              
              
              <table width="100%" border="0">
                <tr>
                  <td colspan="3"><strong>Registrant</strong></td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td> First Name</td>
                  <td><%= cpo.getRegistrantFirstName() %></td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td> Last Name</td>
                  <td><%= cpo.getRegistrantLastName() %></td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td> Email Address</td>
                  <td><%= cpo.getRegistrantEmailAddress() %></td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Phone Number</td>
                  <td><%= cpo.getPhoneNumber() %></td>
                </tr>
                <tr>
                  <td colspan="3"><strong>Organization</strong></td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Organization Name</td>
                  <td><%= cpo.getOrganizationName() %></td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Department Name</td>
                  <td><%= cpo.getDepartmentName() %></td>
                  </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>Division Name</td>
                  <td><%= cpo.getDivisionName() %></td>
                  </tr>
                <tr>
                  <td colspan="3"><strong> Address</strong></td>
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
                </table>
              <h2><br />
                Notes</h2>
              <table width="100%" border="1">
                <tr>
                  <td><%= cpo.getOrganizationNotes() %></td>
                  </tr>
                </table>
                <p>&nbsp;</p>
              <h2>Teams</h2>
              <ul>
              	<% 
				List contestTeams = ContestTeam.getAllForCPO(cpo.getId());
				
				for (ListIterator li = contestTeams.listIterator(); li.hasNext();) {
					ContestTeam theContestTeam = (ContestTeam) li.next();
					Contest theContest = Contest.getById(theContestTeam.getContestId());
				%>
                <li><a href="view_contest_team.jsp?ct_id=<%= theContestTeam.getId() %>"><%= theContestTeam.getTeamName() %> - Contest: <%= theContest.getContestName() %> : Database: <%= theContestTeam.getTeamSchema() %></a></li>
                <% } %>
              </ul>
              <p>(Click on a team name to see information on it.)</p>
              <p>&nbsp;</p>
              <h2>Create a New Contest Team              </h2>
              <form id="form1" name="form1" method="post" action="view_contest_organization.jsp">
              <input type="hidden" name="sending_page" value="new_contest_team" />
              <input type="hidden" name="cpo_id" value="<%= cpo.getId() %>" /> 
              
              
                <table width="100%" border="1">
                  <tr>
                    <td valign="top">Contest</td>
                    <td valign="top">
                    	<select name="contest_id" id="contest_id">
<%			
	for (ListIterator li = Contest.getAll().listIterator(); li.hasNext();) {
		Contest theContest = (Contest) li.next();		
%>
        <option value="<%= theContest.getId() %>"><%= theContest.getContestName() %></option>
<% } %>
                </select>
                    
                    
                    </td>
                  </tr>
                  <tr>
                    <td valign="top">Team Name:</td>
                    <td valign="top">
                      <label for="textfield"></label>
                      <input type="text" name="team_name" id="team_name" />
                      </td>
                  </tr>
                  <tr>
                    <td valign="top">Max Number of Players per Team:</td>
                    <td valign="top"><label for="textfield2"></label>
                      <input type="text" name="max_players" id="max_players" /></td>
                    </tr>
                  <tr>
                    <td valign="top">Admin Notes:</td>
                    <td valign="top"><textarea name="team_notes" id="team_notes" cols="45" rows="5"></textarea></td>
                  </tr>
                  <tr>
                    <td valign="top">Database:</td>
                    <td valign="top">
                                  <select name="team_schema" tabindex="8">
			  <%
			  	
				List ghostList = SchemaInformationObject.getAll();
			  
			  	for (ListIterator<SchemaInformationObject> li = ghostList.listIterator(); li.hasNext();) {
            		SchemaInformationObject this_sg = (SchemaInformationObject) li.next();
				%>
				<option value="<%= this_sg.getSchema_name() %>"><%= this_sg.getSchema_organization() %></option>
			<% } %>
              </select>
                    
                    </td>
                  </tr>
                  <tr>
                    <td valign="top">&nbsp;</td>
                    <td valign="top"><input type="submit" name="button" id="button" value="Submit" /></td>
                  </tr>
                </table>
            </form>
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