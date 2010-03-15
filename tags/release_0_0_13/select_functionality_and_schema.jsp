<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" errorPage="../error.jsp" %>
<%

	String attempting_select = (String) request.getParameter("attempting_select");
	
	OSPSessionObjectHelper osp_soh = OSPSessionObjectHelper.getOSP_SOH(request.getSession(true));

	if (osp_soh.getUserid() == null){
		response.sendRedirect("login.jsp");
		return;
	}
	
	BaseUser bu = BaseUser.getByUserId(osp_soh.getUserid());
	
	List ghostList = BaseUser.getAuthorizedSchemas(osp_soh.getUserid());
	
	// Check to see if this is just one player logged in to one schema. 
	// If this is the case, forward them on.
	if ((ghostList != null) && (ghostList.size() == 1)){
	
		SchemaGhost this_sg = (SchemaGhost) ghostList.get(0);
		User user_in_this_schema = User.getMe(this_sg.getSchema_name() , osp_soh.getUserid());
	
		if (user_in_this_schema.isJustPlayer()){
			response.sendRedirect("simulation/select_simulation.jsp?initial_entry=true&schema_id=" + this_sg.getId());
			return;
		}
	}
	
	response.setHeader("Cache-Control", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
    response.setHeader("Pragma", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
    response.setHeader("Expires", "-1"); //$NON-NLS-1$ //$NON-NLS-2$
		
%>
<html>
<head>
<title>USIP Open Simulation Platform Login</title>
<link href="usip_osp.css" rel="stylesheet" type="text/css">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<style type="text/css">
<!--
.style1 {font-size: small}
-->
</style>
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
</head>
<body onLoad="">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform -<br>
    &nbsp;Select Program Section
    </h1></td>
    <td align="right" background="Templates/images/top_fade.png" width="20%"> 

	  <div align="center"></div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"></td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top">&nbsp;</td>
    <td colspan="1" valign="top"><br /></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<table width="720" border="0" cellspacing="0" cellpadding="0" align="center" bgcolor="#DDDDFF">
<tr> 
    <td width="24" height="24" >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3">
      <blockquote> 
          <h2>Select from the Following</h2>
            <%
			  	for (ListIterator<SchemaGhost> li = ghostList.listIterator(); li.hasNext();) {
            		SchemaGhost this_sg = (SchemaGhost) li.next();
					User user_in_this_schema = User.getMe(this_sg.getSchema_name() , osp_soh.getUserid());
		  %>
        <table align="center"><tr><td><h3>Database: <%= this_sg.getSchema_organization() %></h3></td></tr>
          <tr><td>
          <table align="center" border="1" cellspacing="2" cellpadding="1">
            <tr>
            
              <% if(user_in_this_schema.isAdmin()) { %>
                <td width="25%" valign="top"><p align="center"><a href="simulation_admin/adminwebui.jsp?initial_entry=true&schema_id=<%= this_sg.getId() %>"><img src="images/admins.png" alt="Admin Login" width="140" height="120"></a></p>
                  <p>Use this section to administrate this OSP installation.</p></td>
              <% } // end of if is administratgor %>
                
              <% if(user_in_this_schema.isSim_author()) { %>
              	<td width="25%" valign="top"><p align="center"><a href="simulation_authoring/creationwebui.jsp?initial_entry=true&schema_id=<%= this_sg.getId() %>&show_intro=true"><img src="images/authors.png" alt="Author Login" width="140" height="120"></a></p>
              	  Use this section to author simulations.</td>
                <% } // end of if is author. %>
                
              <% if(user_in_this_schema.isSim_instructor()) {  %>  
              <td width="25%" valign="top"><p align="center"><a href="simulation_facilitation/facilitateweb.jsp?initial_entry=true&schema_id=<%= this_sg.getId() %>"><img src="images/instructors.png" alt="Instructor Login" width="140" height="120"></a></p>
                <p>Use this section to create running simulations, invite students and perform other common instructor tasks.</p></td>
              <% } // end of if is instructor. %>
                
              <td width="25%" valign="top"><p align="center"><a href="simulation/select_simulation.jsp?initial_entry=true&schema_id=<%= this_sg.getId() %>"><img src="images/players.png" alt="Player Login" width="140" height="120"></a></p>
                <p>Use this section to enter into a simulated world either as a student or a control character.</p></td>
            </tr>
          </table>
          </td></tr>
          </table>
          <%
		  		} // End of loop over schemas
		  %>
          <p>&nbsp;</p>
      </blockquote>

</td>
  </tr>
  <tr> 
    <td width="24" height="24" >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
  </tr>
</table>

<p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p>
</body>
</html>