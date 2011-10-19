<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.persistence.*" 
	errorPage="/error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = "../simulation_authoring/create_simulation.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
    Simulation simulation = afso.handleEditBasicSimParameters(request); 
	
	int canEdit = SimEditAuthorization.checkAuthorizedToEdit(afso.schema, afso.sim_id, afso.user_id);
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>


<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
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
              <h1>Enter Basic  Simulation Information </h1>
              <br />
      <form action="enter_basic_simulation_information.jsp" method="post" name="form1" id="form1">
        <input type="hidden" name="sending_page" value="create_simulation" />
        <blockquote>
        
        <table width="80%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">Simulation Name:</td>
              <td valign="top"><%= simulation.getSimulationName() %></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">Simulation Version:</td>
              <td valign="top"><%= simulation.getVersion() %></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">Simulation Creating Organization <a href="helptext/sim_banner.jsp" target="helpinright">(?)</a>:</td>
              <td valign="top"><%= simulation.getCreation_org() %></td>
            </tr>
            <td>&nbsp;</td>
            <td valign="top">Simulation Editors </td>
            <td valign="top">
				<table border="1" cellspacing="0">
					<tr>
						<td valign="top"><input name="editing_users" type="radio" value="specific_users"
						 <%= USIP_OSP_Util.matchSelected(simulation.getSimEditingRestrictions(), Simulation.CAN_BE_EDITED_BY_SPECIFIC_USERS , "checked=\"checked\"") %>
						 <%= SimEditAuthorization.getDisabledCode(canEdit) %>
						 /></td>
						<td width="100%" valign="top">
			  				<%
			  				List userList = SimEditors.getAuthorizedUsers(afso.schema, afso.sim_id);
							
							for (ListIterator li = userList.listIterator(); li.hasNext();) {
								User user = (User) li.next();
			
			  				%>
			  				<%= user.getUserName() %><br />
							<%  } // end of loop over users %>
			  				<a href="add_editor.jsp">Add/Remove Editors</a></td>
			  		</tr>
					<tr>
						<td><input name="editing_users" type="radio" value="everyone" 
						<%= USIP_OSP_Util.matchSelected(simulation.getSimEditingRestrictions(), Simulation.CAN_BE_EDITED_BY_EVERYONE , "checked=\"checked\"") %>
						 <%= SimEditAuthorization.getDisabledCode(canEdit) %>
						/></td>
						<td>Everyone</td>
					</tr>
				</table>          </tr>
          <tr>
            <td>&nbsp;</td>
              <td valign="top">Simulation Copyright String <a href="helptext/sim_copyright.jsp" target="helpinright">(?)</a>:</td>
              <td valign="top"> <textarea name="simcopyright" tabindex="4" <%= SimEditAuthorization.getDisabledCode(canEdit) %>><%= simulation.getCopyright_string() %></textarea></td>
            </tr>
          <tr>
            <td>&nbsp;</td>
              <td valign="top">Simulation Blurb <a href="helptext/sim_blurb.jsp" target="helpinright">(?)</a>:</td>
              <td valign="top"><textarea name="simblurb" tabindex="4" <%= SimEditAuthorization.getDisabledCode(canEdit) %>><%= simulation.getBlurb() %></textarea></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">&nbsp;</td>
              <td valign="top">
                <%
				if (afso.sim_id == null) {
				%>
                <%
				} else {
				%>
                <input type="hidden" name="sim_id" value="<%= simulation.getId() %>" />
				<table width="100%"><tr>
                <td align="left"><input type="submit"  name="command" value="Update" <%= SimEditAuthorization.getDisabledCode(canEdit) %> /></td>
				<td align="right">&nbsp;</td>
				</tr></table>
                <%
					}
				%>              </td>
            </tr>
          </table>
		  
		  <p>&nbsp;		        </p>
      </form>
		  <p>
		    <%
				if (afso.sim_id != null) {
			%>
            <p align="center">
              <div align="center">
                <form action="create_simulation_objectives.jsp" method="post" name="form1" id="form1">
              <label><input type="submit" name="Submit" value="Proceed to Next Step" /></label>
			  
			  </form>
              </div>
            </p>
		  <% } %>
		  </p>  
      <p align="center"></p>			</td>
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