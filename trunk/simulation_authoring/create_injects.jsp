<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true), true);
	afso.backPage = "create_injects.jsp";
	
	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Open Simulation Platform Add Special Features Page</title>


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
			<td width="100%">
            <blockquote> 
              <h1>Create Injects</h1>
        <% 
			if (afso.sim_id != null) {
		%>
          <table width="100%" border="0" cellspacing="0" cellpadding="4">
            <tr>
              <td colspan="4">Creating injects for the simulation <strong><%= simulation.getDisplayName() %></strong>.<br />
(If you would like to work on a different simulation, <a href="select_simulation.jsp">click 
            here</a>.)</td>
            </tr>
            <tr>
              <td colspan="4">Injects are arranged into groups. You must first <a href="create_inject_group.jsp">create an inject group</a> before creating any injects. </td>
            </tr>
            <tr>
              <td colspan="4"><strong><u>Current Inject Groups and Injects</u></strong></td>
              </tr>
            <%
			for (ListIterator li = InjectGroup.getAllForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
			InjectGroup ig = (InjectGroup) li.next();
		%>
            <tr>
              <td width="25%" valign="top"><strong><%= ig.getName() %></strong></td>
              <td width="13%" valign="top">&nbsp;</td>
              <td width="27%" valign="top">&nbsp;</td>
              <td width="35%" valign="top"><a href="create_indvidual_inject.jsp?inject_group_id=<%= ig.getId() %>">Create Inject in Group <%= ig.getName() %>
                </a></td>
            </tr>
            <tr>
              <td valign="top">&nbsp;</td>
              <td colspan="3" valign="top"><strong>Group Description:</strong> <%= ig.getDescription() %></td>
            </tr>
            <% 
		  List injectList = Inject.getAllForSimAndGroup(afso.schema, afso.sim_id, ig.getId());
		  
		   if (injectList.size() > 0) { 
		  	for (ListIterator lii = injectList.listIterator(); lii.hasNext();) {
			Inject da_inject = (Inject) lii.next();
		  
		  %>
            <tr>
              <td valign="top">&nbsp;</td>
              <td valign="top">inject name:</td>
              <td colspan="2" valign="top"><%= da_inject.getInject_name() %></td>
            </tr>
            <tr>
              <td valign="top">&nbsp;</td>
              <td valign="top">inject text:</td>
              <td colspan="2" valign="top"><%= da_inject.getInject_text() %></td>
            </tr>
            <tr>
              <td valign="top">&nbsp;</td>
              <td valign="top">inject notes:</td>
              <td colspan="2" valign="top"><%= da_inject.getInject_Notes() %></td>
            </tr>
            <tr>
              <td valign="top">&nbsp;</td>
              <td valign="top">&nbsp;</td>
              <td colspan="2" valign="top"><a href="create_indvidual_inject.jsp?edit=true&inj_id=<%= da_inject.getId() %>">update</a> / 
                <a href="delete_object.jsp?object_type=inject&objid=<%= da_inject.getId() %>&object_info=<%= da_inject.getInject_name() %>"> 
                  delete</a></td>
            </tr>
            <% }  // End of loop.  
		  } else { %>
            <tr>
              <td valign="top">&nbsp;</td>
              <td valign="top">&nbsp;</td>
              <td colspan="2" valign="top">No injects in this group yet. </td>
            </tr>
            <% } %>
            
            <p>&nbsp;</p>
          <% } // end of loop over inject groups %>
            </table>
          <p> 
            <!-- jsp:include page="snippet.jsp" flush="true" -->
            </p>
          <p>&nbsp;</p>
      </blockquote>
      <p align="center"><a href="add_objects.jsp">Next Step: Add Objects</a><a href="set_universal_sim_sections.jsp?actor_index=0"></a></p>
      <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>
      
            <a href="assign_actor_to_simulation.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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
