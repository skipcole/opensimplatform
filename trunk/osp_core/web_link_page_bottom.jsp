<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,
	java.text.*,
	java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	boolean canEdit = true;
	
	WebLinkObjects wlo = pso.wloOnScratchPad;
	
	String cs_id = request.getParameter("cs_id");
	
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	String linkName = (String) cs.getContents().get(WebLinksCustomizer.KEY_FOR_LINK_NAME);
	
	String editMode = request.getParameter("editMode");
	
	if ((editMode != null) && (editMode.equalsIgnoreCase("true"))){
		String wlo_id = (String) request.getParameter("wlo_id");
		
		if (((wlo_id != null) && (wlo_id.length() > 0))
			&& (!(wlo_id.equalsIgnoreCase("0"))) ) {
			wlo = WebLinkObjects.getById(pso.schema, new Long(wlo_id));
		}
	}

%>
<html>
<head>
	<link type="text/css" href="../third_party_libraries/jquery/jquery-ui-1.8.4/development-bundle/themes/cupertino/jquery.ui.all.css" rel="stylesheet" />
	<script type="text/javascript" src="../third_party_libraries/jquery/jquery-ui-1.8.4/js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="../third_party_libraries/jquery/jquery-ui-1.8.4/development-bundle/ui/jquery.ui.core.js"></script>
	<script type="text/javascript" src="../third_party_libraries/jquery/jquery-ui-1.8.4/development-bundle/ui/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="../third_party_libraries/jquery/jquery-ui-1.8.4/development-bundle/ui/jquery.ui.datepicker.js"></script>
	<script type="text/javascript">
	$(function() {
		$("#datepicker").datepicker();
	});
	</script>

<title>USIP OSP Web Links Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<% if (canEdit) { %>
<h2>Add <%= linkName %> </h2><%= wlo.getWloError() %>
<blockquote><form name="form1" method="post" action="web_link_page.jsp" target="mainFrame">
	<input type="hidden" name="sending_page" value="web_link_page_bottom">
    <input type="hidden" name="cs_id" value="<%= cs_id %>">
  <table border="0" cellpadding="2" cellspacing="2">
    <tr>
      <td width="19%" valign="top"><%= linkName %> Name</td>
      <td colspan="2" valign="top">
          <input name="wlo_name" type="text" id="wlo_name" tabindex="1" value="<%= wlo.getWeblinkName() %>" size="40">      </td>
    </tr>

	<script type="text/javascript">
	$(function() {
		$("#datepicker").datepicker();
	});
	</script>


    <tr>
      <td valign="top"><%= linkName %> Date </td>
      <td colspan="2" valign="top"><input name="wlo_event_date" type="text" id="datepicker"></td>
    </tr>
    <tr>
      <td valign="top"><%= linkName %> Description</td>
      <td colspan="2" valign="top"><textarea name="wlo_description" cols="80" rows="2" tabindex="2"><%= wlo.getWeblinkDescription() %></textarea></td>
    </tr>
    <tr>
      <td valign="top"><%= linkName %> URL</td>
      <td colspan="2" valign="top"><textarea name="wlo_url" cols="80" rows="4" tabindex="3"><%= wlo.getWeblinkURL() %></textarea></td>
    </tr>
		<%
		SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy HH:mm:ss z");
		sdf.setTimeZone(TimeZone.getDefault());
	%>
    <tr>
      <td valign="top">Posting Date </td>
      <td colspan="2" valign="top"><%= sdf.format(wlo.getPostingDate()) %>
	  </td>
    </tr>
    <tr>
      <td valign="top">&nbsp;</td>
      <td width="46%" valign="top"><%	if (wlo.getId() == null) { %>
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
      <td width="35%" align="right" valign="top"><%	if (wlo.getId() != null) { %>
        <input type="submit" name="command" value="Delete" tabindex="7"  onClick="return confirm('Are you sure you want to delete this item?');" />
        <% } %></td>
    </tr>
	
  </table>
  </form>
  <p></p>
</blockquote>
<% } else { %>
<p>&nbsp;</p>
<p>No pages have been loaded.</p>
<% } %>
</body>
</html>
