<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.persistence.*" 
	errorPage="" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = "../simulation_authoring/create_simulation.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
    Simulation simulation = afso.handleCreateOrUpdateNewSim(request);  
	
	List simList = Simulation.getAll(afso.schema);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>


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
              <h1>Create New Simulation </h1>
              <br />
      <form action="create_simulation.jsp" method="post" name="form1" id="form1">
        <input type="hidden" name="sending_page" value="create_simulation" />
        <blockquote>
        
        <table width="80%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">Simulation Name <a href="helptext/sim_name.jsp" target="helpinright">(?)</a>:</td>
              <td valign="top">
  <input type="text" name="simulation_name" value="<%= simulation.getName() %>" tabindex="1" /></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">Simulation Version <a href="helptext/sim_version.jsp" target="helpinright">(?)</a>:</td>
              <td valign="top">
  <input type="text" name="simulation_version" value="<%= simulation.getVersion() %>" tabindex="2" /></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">Simulation Creating Organization <a href="helptext/sim_banner.jsp" target="helpinright">(?)</a>:</td>
              <td valign="top">
  <input type="text" name="creation_org" value="<%= simulation.getCreation_org() %>" tabindex="3" /></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">Simulation Creator <a href="helptext/sim_banner.jsp" target="helpinright">(?)</a>:</td>
              <td valign="top">
  <input type="hidden" name="simcreator" value="<%= afso.user_Display_Name %>"> 
                <%= simulation.getCreator() %></td>
            </tr>
          <tr>
            <td>&nbsp;</td>
              <td valign="top">Simulation Copyright String (?):</td>
              <td valign="top"> <textarea name="simcopyright" tabindex="4"><%= simulation.getCopyright_string() %></textarea></td>
            </tr>
          <tr>
            <td>&nbsp;</td>
              <td valign="top">Simulation Blurb (?):</td>
              <td valign="top"><textarea name="simblurb" tabindex="4"><%= simulation.getBlurb() %></textarea></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">&nbsp;</td>
              <td valign="top">
                <%
				if (simulation.getId() == null) {
				%>
                <input type="submit" name="command" value="Create" />
                <%
				} else {
				%>
                <input type="hidden" name="sim_id" value="<%= simulation.getId() %>" />
                <input type="submit" name="command" value="Clear" tabindex="6" />
                <input type="submit" name="command" value="Update" />
                <%
					}
				%>              </td>
            </tr>
          </table>
          <blockquote>
            <p>&nbsp;</p>
          </blockquote>
      </form>
      <blockquote>
        <p>&nbsp;</p>
          <p>Below are listed alphabetically all simulations in your database.</p>
          <table>
            <%
		
		for (ListIterator li = simList.listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
			String nameToSend = java.net.URLEncoder.encode(sim.getName());
			
		%>
            <tr> 
              <td><a href="create_simulation.jsp?command=Edit&sim_id=<%= sim.getId().toString() %>"><%= sim.getName() %> : <%= sim.getVersion() %></a></td>
              <td>&nbsp;</td>
              <td><!-- a href="delete_object.jsp?object_type=simulation&objid=<%= sim.getId().toString() %>&object_info=<%= nameToSend %>"> 
                (Remove) <%= sim.getName() %> : <%= sim.getVersion() %> </a --></td>
            </tr>
            <%
	}
%>
            </table>
      </blockquote>      <p align="center"><a href="create_simulation_objectives.jsp">Next Step: 
        Simulation Objectives</a></p>			</td>
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
