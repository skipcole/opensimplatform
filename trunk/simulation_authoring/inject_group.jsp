<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.communications.*,
	org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	InjectGroup ig = afso.handleCreateInjectGroup(request);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Create Inject Group Page</title>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
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
			if (afso.sim_id != null) {
		%>
        <form id="form2" name="form2" method="post" action="inject_group.jsp">
            <input type="hidden" name="sending_page" value="create_inject_group" />
            <table width="100%" border="0" cellspacing="0" cellpadding="4">
              <tr>
                <td valign="top">Group Name</td>
              <td valign="top">
                <label>
                  <input type="text" name="inject_group_name" id="inject_group_name" value="<%= ig.getName() %>" />
                  </label>            </td>
            </tr>
              <tr>
                <td valign="top">Group Description</td>
              <td valign="top"><label>
                <textarea name="inject_group_description" id="textarea" cols="45" rows="5"><%= ig.getDescription() %></textarea>
                </label></td>
            </tr>
              <tr>
                <td>&nbsp;</td>
                <td><%
				if (ig.getId() == null) {
				%>
                  <input type="submit" name="command" value="Create" />
                  <%
				} else {
				%>
                  <input type="hidden" name="ig_id" value="<%= ig.getId() %>" />
                  <input type="submit" name="command" value="Clear" tabindex="6" />
                  <input type="submit" name="command" value="Update" />
                  <%
					}
				%></td>
              </tr>
              </table>
          </form>
          
          <p>&nbsp;</p>
          <p>Below are listed all of the Inject Groups in this Simulation</p>
          
          <table width="100%"><tr><td><strong>Select to Edit</strong></td>
          <td><strong>Select to remove</strong></td>
          </tr>
        <%
			for (ListIterator li = InjectGroup.getAllForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
			InjectGroup this_ig = (InjectGroup) li.next();
			
			String removeString = "Group has injects";
			System.out.println("doing check");
			if (!(InjectGroup.checkIfInUse(afso.schema, afso.sim_id, this_ig.getId()))){
				removeString = "Delete";
			}
		%>
        <tr><td><a href="inject_group.jsp?ig_id=<%= this_ig.getId() %>&queueup=true"><%= this_ig.getName() %></a></td>
        <td><%= removeString %></td></tr>
        <% } %>
        </table>
      </blockquote>
      <p align="center">&nbsp;</p>
      <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>      <a href="injects.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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
