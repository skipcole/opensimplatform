<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.communications.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}

	ActorCategory actorCategory = afso.handleCreateActorCategory(request);
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Create Actor Category Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {
	color: #FF0000;
	font-weight: bold;
}
-->
</style>
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" width="100%" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Create Actor Category</h1>
              <br />
      <blockquote> 
        <p>
          <% 
			if (afso.sim_id != null) {
		%>
          An 'Actor Category' gives you the ability to set simulations sections for some actors to be exactly the same. One first picks the actor that is going to be the 'exemplar' and then sets the actors that will have the same sections.</p>
        <p><span class="style1">Note!</span>: When you apply the sections of the exemplar to the actors in the category, it will remove all previous sections added to that actor.</p>
        <p>          </p>
          <form id="form2" name="form2" method="post" action="create_actor_category.jsp">
            <input type="hidden" name="inj_id" value="<%= actorCategory.getId() %>" />
            <input type="hidden" name="sending_page" value="create_actor_category" />
            <table width="100%" border="1" cellspacing="0" cellpadding="4">
              <tr>
                <td colspan="2" valign="top">Category Name:</td>
              <td width="65%" valign="top">
                <label>
                  <input type="text" name="ac_name" id="ac_name" value="<%= actorCategory.getCategoryName() %>"/>
                  </label>            </td>
            </tr>
              <tr>
                <td colspan="2" valign="top">Category Exemplar:</td>
                <td valign="top">
                <select name="exemplar_id">
                <option value="0">None Selected</option>
                <% 

                for (ListIterator la = SimActorAssignment.getActorsForSim(afso.schema, afso.sim_id).listIterator(); la.hasNext();) {
					Actor act = (Actor) la.next();
		%>
                <option value="<%= act.getId().toString() %>"><%= act.getActorName() %></option>
                <% } %>
                </select></td>
              </tr>

              <% 

                for (ListIterator la = SimActorAssignment.getActorsForSim(afso.schema, afso.sim_id).listIterator(); la.hasNext();) {
					Actor act = (Actor) la.next(); %>
                                  <tr>
                <td width="6%">&nbsp;</td>
                <td width="29%">Include Actor:</td>
                <td>
					<label><input type="checkbox" name="actor_<%= act.getId().toString() %>" id="checkbox" /><%= act.getActorName() %></label>				     </td>
              </tr>
				<% } %>
              <tr>
                <td colspan="2">&nbsp;</td>
              <td>              
                <%
				if (actorCategory.getId() == null) {
				%>
                <input type="submit" name="create_ac" value="Create" />
                <%
				} else {
				%>
                <input type="submit" name="command" value="Clear" tabindex="6" />
                <input type="hidden" name="ac_id" value="<%= actorCategory.getId() %>" />
                <input type="submit" name="update_ac" value="Update" />
                <%
					}
				%></td>
            </tr>
              </table>
          </form>
          <p>Below are listed all of the actor categories currently associated with this simulation. </p>
          <table border="1" width="100%">
            <tr>
              <td><strong>Actor Category</strong></td>
              <td><strong>Remove*</strong></td>
              <td>Apply</td>
            </tr>
            <%
					for (ListIterator li = ActorCategory.getAllForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
						ActorCategory this_ac = (ActorCategory) li.next();
		%>
        <form id="form1" name="form1" method="post" action="">
            <tr>
              <td><a href="create_actor_category.jsp?ac_id=<%= this_ac.getId() %>&queueup=true"><%= this_ac.getCategoryName() %></a></td>
              <td>&nbsp;</td>
              <td>
                <label>
                  <input type="submit" name="button" id="button" value="Submit" />
                  </label>
              
              </td>
            </tr>
            </form>
            <% } %>
          </table>
          <p>* Feature not yet implemented</p>
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
