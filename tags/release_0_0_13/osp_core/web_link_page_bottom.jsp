<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	boolean canEdit = true;
	
	WebLinkObjects wlo = new WebLinkObjects();
	
	
	String sending_page = request.getParameter("sending_page");
	String cs_id = request.getParameter("cs_id");
	
	if ((cs_id != null) && (sending_page != null) && (sending_page.equalsIgnoreCase("add_wlo"))){
	
		Long csId = new Long (cs_id);
		
		String wlo_name = request.getParameter("wlo_name");
		String wlo_description = request.getParameter("wlo_description");
		String wlo_url = request.getParameter("wlo_url");
		
		wlo = new WebLinkObjects(pso.schema, wlo_name, wlo_description, wlo_url, pso.running_sim_id, csId);
	}
	// If adding, add it to the list, and then put them back to where they were
	if (false) {
		response.sendRedirect("simwebui.jsp?tabposition=" + pso.tabposition);
		return;
	}
%>
<html>
<head>
<title>Introduction Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<% if (canEdit) { %>
<h2>Add Web Link</h2>
<blockquote><form name="form1" method="post" action="web_link_page_bottom.jsp">
	<input type="hidden" name="sending_page" value="add_wlo">
    <input type="hidden" name="cs_id" value="<%= cs_id %>">
  <table width="80%" border="0" cellpadding="2" cellspacing="2">
    <tr>
      <td valign="top">Web Link Name</td>
      <td valign="top">
          <input name="wlo_name" type="text" id="wlo_name" tabindex="1" value="<%= wlo.getWeblinkName() %>" size="40">      </td>
    </tr>
    <tr>
      <td valign="top">Web Link Description</td>
      <td valign="top"><textarea name="wlo_description" cols="80" rows="2" tabindex="2"><%= wlo.getWeblinkDescription() %></textarea></td>
    </tr>
    <tr>
      <td valign="top">Web Link URL</td>
      <td valign="top"><textarea name="wlo_url" cols="80" rows="4" tabindex="3"><%= wlo.getWeblinkURL() %></textarea></td>
    </tr>
    <tr>
      <td valign="top">&nbsp;</td>
      <td valign="top"><%
				if (wlo.getId() == null) {
				%>
          <input type="submit" name="command" value="Create" tabindex="4" />
          <%
				} else {
				%>
          <input type="hidden" name="wlo_id" value="<%= wlo.getId() %>" />
          <input type="submit" name="command" value="Clear" tabindex="5" />
          <input type="submit" name="command" value="Update" tabindex="6" />
          <%
					}
				%>      </td>
    </tr>
  </table>
  </form>
  <p>&nbsp;</p>
</blockquote>
<% } else { %>
<p>&nbsp;</p>
<p>No pages have been loaded.</p>
<% } %>
</body>
</html>
