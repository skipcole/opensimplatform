<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.communications.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	String sending_page = (String) request.getParameter("sending_page");
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("create_inject_group"))){
		pso.handleCreateInjectGroup(request);
		response.sendRedirect("create_injects.jsp");
		return;
	}
	
	InjectGroup ig = new InjectGroup();
	ig.setDescription("none");
	
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Open Simulation Platform Create Inject Group Page</title>


<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Create Inject Group</h1>
              <br />
      <blockquote> 
        <% 
			if (pso.sim_id != null) {
		%>
        <p>Creating Inject Group</p>
          <form id="form2" name="form2" method="post" action="">
            <input type="hidden" name="sending_page" value="create_inject_group" />
            <table width="100%" border="0" cellspacing="0" cellpadding="4">
              <tr>
                <td>Group Name</td>
              <td>
                <label>
                  <input type="text" name="inject_group_name" id="inject_group_name" />
                  </label>            </td>
            </tr>
              <tr>
                <td>Group Description</td>
              <td><label>
                <textarea name="inject_group_description" id="textarea" cols="45" rows="5"></textarea>
                </label></td>
            </tr>
              <tr>
                <td>&nbsp;</td>
              <td><input type="submit" name="button" id="button" value="Create Inject Group" /></td>
            </tr>
              </table>
          </form>
          <p>&nbsp;</p>
          <p> 
            <!-- jsp:include page="snippet.jsp" flush="true" -->
            </p>
          <p>&nbsp;</p>
      </blockquote>
      <p align="center">&nbsp;</p>
      <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>      <a href="create_injects.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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
