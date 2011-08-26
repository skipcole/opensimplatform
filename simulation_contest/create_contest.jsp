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
	
	Contest contest = Contest.handleCreateContest(request);
	
	boolean editMode = false;
	
	String contest_id = (String) request.getParameter("contest_id");
	if (USIP_OSP_Util.stringFieldHasValue(contest_id)) {
		editMode = true;	
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
			  <h1>Create/Edit Contest<a href="../simulation_facilitation/helptext/create_running_sim_help.jsp" target="helpinright"></a></h1>
			  <br />
            <blockquote>
              <p>Here you can create a contest to have multiple teams creating simulations. </p>
              <p>It is NOT recommended to make any changes to a contest once it has begun.</p>
              <p>&nbsp;</p>
              <form action="create_contest.jsp" method="post" name="form1" id="form1">
 
			<% if (editMode) { %>             
              <input type="hidden" name="sending_page" value="edit_contest" /> 
			  <% } else { %>
              <input type="hidden" name="sending_page" value="create_contest" />
			<% } %>
              <input type="hidden" name="contest_id" value="<%= contest.getId() %>" />
              
              <table width="80%" border="1" cellspacing="2" cellpadding="2">
                <tr> 
                  <td valign="top">Contest Name</td>
              <td valign="top"><input type="text" name="contest_name" value="<%= contest.getContestName() %>" /></td>
            </tr>
                <tr>
                  <td valign="top">Contest Logo</td>
                  <td valign="top"><input type="hidden" name="MAX_FILE_SIZE" value="100000" /><input name="uploadedfile" type="file" tabindex="5" /></td>
                </tr>
                <tr>
                  <td valign="top">Contest Short Description</td>
                  <td valign="top"><label for="short_description"></label>
                    <textarea name="short_description" id="short_description" cols="45" rows="5">
                    <%= contest.getContestShortDescription() %> </textarea></td>
                </tr>
                <tr>
                  <td valign="top">Contest Description</td>
                  <td valign="top"><label for="description"></label>
                    <textarea name="description" id="description" cols="45" rows="5">
                    <%= contest.getContestDescription() %> </textarea></td>
                </tr>
                <tr>
                  <td valign="top">Contest Period</td>
                  <td valign="top">&nbsp;</td>
                </tr>
                <tr>
                  <td valign="top">Max Players</td>
                  <td valign="top">&nbsp;</td>
                </tr>
                <tr>
                  <td valign="top">Conest Second Page Info</td>
                  <td valign="top"><label for="second_page"></label>
                    <textarea name="second_page" id="second_page" cols="45" rows="5"></textarea></td>
                </tr>
                <tr> 
                  <td valign="top">&nbsp;</td>
                  <td valign="top"><input type="submit" name="addRunningSimulation" value="Submit" /></td>
                </tr>
                </table>
          </form>
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
