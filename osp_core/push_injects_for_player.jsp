<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.communications.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	pso.backPage = "push_injects_for_player.jsp";
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
	if (pso.handlePushInject(request)){
		String inject_id = (String) request.getParameter("inject_id");
		response.sendRedirect("select_actors.jsp?inject_id=" + inject_id + "&cs_id=" + cs_id);
		return;
	}
	
	CustomizeableSection cs = new CustomizeableSection();

	boolean canEdit = false;
	
	if (cs_id != null) {
		cs = CustomizeableSection.getById(pso.schema, cs_id);
		
		if (cs.getContents() != null) {
			String can_edit = (String) cs.getContents().get(InjectGroup.PLAYER_CAN_EDIT);
			if (can_edit != null) {
				if (can_edit.equalsIgnoreCase("true")){
						canEdit = true;
				}
			}
		}
	}
		
%>
<html>
<head>
<title>Push Inject Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></head>

<body>
<h2>Player Push Injects </h2>
<p>Note: Injects in red have already been fired to all players. Injects in green have been fired to some players. </p>
<table width="100%" border="1" cellspacing="0" cellpadding="4">
		  <%
		  
		  List setOfInjectGroups = new ArrayList();
		  
		  if (cs.getId() != null) {
		  	setOfInjectGroups = InjectGroup.getSetOfInjectGroupsForSection(pso.schema, cs.getId());
		  }
		  
		for (ListIterator li = setOfInjectGroups.listIterator(); li.hasNext();) {
			InjectGroup ig = (InjectGroup) li.next();
		%>

  <tr>
    <td colspan="4" valign="top"><p><strong><%= ig.getName() %></strong></p>
      <ul>
        <li><%= ig.getDescription() %></li>
    </ul></td>
  </tr>

    <% 
	List injectList = Inject.getAllForSimAndGroup(pso.schema, pso.sim_id, ig.getId());

		int iii = 0;
		
		  for (ListIterator lii = injectList.listIterator(); lii.hasNext();) {
			Inject da_inject = (Inject) lii.next();
			iii += 1;
			
%>
<form name="formforinject<%= iii %>" action="push_injects_for_player.jsp" method="post">
<input type="hidden" name="sending_page" value="push_injects">
<input type="hidden" name="cs_id" value="<%= cs.getId() %>">
  <tr>
    <td valign="top">&nbsp;</td>
    <td colspan="2" valign="top"><%= da_inject.getInject_name() %> </td>
    <td width="33%" rowspan="3" valign="top">
    <%
		String selected_all = " checked ";
		String selected_some = "";
		
		if (InjectActorAssignments.getAllForInject(pso.schema, da_inject.getId()).size() > 0 ){
			selected_all = "";
			selected_some = " checked ";
		}
	%>
    <label>
      <input name="player_target" type="radio" value="all" <%= selected_all %>>
      To All Players </label>
  <br>
  <label>
  <input name="player_target" type="radio" value="some"  <%= selected_some %>>
    To Some Players<BR />
    <I>(Select to who after hitting submit.)</I></label> 
  <br>
  <label>
  <input type="submit" name="button" id="button" value="Push Inject">
  </label></td>
  </tr>
  <%
  	String injectLineColor = "#FFCCCC";
  %>
  <tr bgcolor="<%= pso.getInjectColor(request, da_inject.getId()) %>">
    <td valign="top" bordercolor="#000000">&nbsp;</td>
    <td width="4%" valign="top" bordercolor="#000000">&nbsp;</td>
    <td valign="top" bordercolor="#000000">
    	<% 
		if (canEdit) {
		%>
        <label>
          <textarea name="announcement_text" id="inject_<%= da_inject.getId() %>" cols="45" rows="5"><%= da_inject.getInject_text() %></textarea>
		<script language="javascript1.2">
  			generate_wysiwyg('inject_<%= da_inject.getId() %>');
		</script>
          <input type="hidden" name="inject_id" value="<%= da_inject.getId() %>">
        </label>
		<% } else { %>
		<%= da_inject.getInject_text() %>
		<input type="hidden" name="announcement_text" value="<%= da_inject.getInject_text() %>" />
		<% } %>		
		</td>
  </tr>
  <tr>
    <td valign="top" bordercolor="#000000">&nbsp;</td>
    <td colspan="4" valign="top" bordercolor="#000000"><strong>Notes</strong>: <%= da_inject.getInject_Notes() %></td>
  </tr>
  <tr>
    <td valign="top">&nbsp;</td>
    <td colspan="3" valign="top"><hr></td>
  </tr>
  </form>
  <% } // End of loop over injects %>
 
 <% } // end of loop over inject groups %>
</table>
</body>
</html>