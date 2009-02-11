<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.communications.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	
	String inject_group_id = (String) request.getParameter("inject_group_id");
	String sending_page = (String) request.getParameter("sending_page");
	
	String edit = (String) request.getParameter("edit");
	String inj_id = (String) request.getParameter("inj_id");
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("create_ind_inject"))){
		System.out.println("doing inject creation");
		pso.handleCreateInject(request);
		response.sendRedirect("create_injects.jsp");
		return;
	}
	
	Inject inj = new Inject();
	boolean in_edit_mode = false;
	
	
	if ( (edit != null) && (edit.equalsIgnoreCase("true"))){
		
		inj = Inject.getMe(pso.schema, new Long(inj_id));
		in_edit_mode = true;
		
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Open Simulation Platform Create Inject Group Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>

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
              <h1>Create Inject</h1>
              <br />
      <blockquote> 
        <% 
			if (pso.simulationSelected) {
		%>
        <p>          </p>
          <form id="form2" name="form2" method="post" action="create_indvidual_inject.jsp">
            <input type="hidden" name="inj_id" value="<%= inj_id %>" />
            <input type="hidden" name="edit" value="<%= edit %>"  />
            <input type="hidden" name="sending_page" value="create_ind_inject" />
            <table width="100%" border="1" cellspacing="0" cellpadding="4">
              <tr>
                <td valign="top">Inject Name:</td>
              <td valign="top">
                <label>
                  <input type="text" name="inject_name" id="inject_name" value="<%= inj.getInject_name() %>"/>
                  </label>            </td>
            </tr>
              <tr>
                <td valign="top">Inject Text:</td>
              <td valign="top">
                <textarea id="inject_text" name="inject_text" cols="45" rows="5"><%= inj.getInject_text() %></textarea>
                <script language="javascript1.2">
  			generate_wysiwyg('inject_text');
		</script>                </td>
            </tr>
              
              <tr>
                <td valign="top">Inject Notes:<br />
                  (Notes direct the simulation facilitator on how to use this inject.)</td>
              <td valign="top"><label>
                <textarea name="inject_notes" id="inject_notes" cols="45" rows="5"><%= inj.getInject_Notes() %></textarea>
                </label></td>
            </tr>
              <tr>
                <td>&nbsp;</td>
              <td>
                <input type="hidden" name="inject_group_id" value="<%= inject_group_id %>" />
                
                <% if (in_edit_mode) { %>
                <input type="submit" name="button" id="button" value="Save Changes" />
                <% } else { %>
                <input type="submit" name="button" id="button" value="Create Inject" />
                <% } %>                </td>
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
