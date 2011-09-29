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
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
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
      <td valign="top"><%= linkName %> Date </td>
      <td colspan="2" valign="top"><input name="wlo_event_date" tabindex="1"  type="text" value="<%= wlo.getWLODateFormattedForWeb() %>" id="datepicker"> 
        (mm/dd/yyyy) </td>
    </tr>
      <tr>
      <td width="25%" valign="top"><%= linkName %> Name</td>
      <td colspan="2" valign="top">
          <input name="wlo_name" type="text" id="wlo_name" tabindex="2" value="<%= wlo.getWeblinkName() %>" size="40" maxlength="60">      </td>
    </tr>
    <tr>
      <td valign="top">Source of <%= linkName %></td>
      <td colspan="2" valign="top"><input type="text" name="wlo_source" tabindex="3"  value="<%= wlo.getWeblinkSource() %>" size="40"></td>
    </tr>
    <tr>
      <td valign="top"><%= linkName %> Description</td>
      <td colspan="2" valign="top"><textarea name="wlo_description" cols="80" rows="2" tabindex="4"><%= wlo.getWeblinkDescription() %></textarea></td>
    </tr>
    <tr>
      <td valign="top"><%= linkName %> URL</td>
      <td colspan="2" valign="top"><textarea name="wlo_url" cols="80" rows="4" tabindex="5"><%= wlo.getWeblinkURL() %></textarea></td>
    </tr>
		<%
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
		sdf.setTimeZone(TimeZone.getDefault());
	%>
        <tr>
          <td valign="top">Submitter</td>
		  <%
		  	String userName = "";
		  	if (wlo.getU_id() == null){
				userName = "";
			} else {
				userName = USIP_OSP_Cache.getUSERName(pso.schema, request, wlo.getU_id());
			}
		  %>
          <td colspan="2" valign="top"><%= userName %></td>
        </tr>
      <tr>
      <td valign="top">Posting Date </td>
      <td colspan="2" valign="top"><%= sdf.format(wlo.getPostingDate()) %>	  </td>
    </tr>
    <tr>
      <td valign="top">&nbsp;</td>
      <td width="38%" valign="top"><%	if (wlo.getId() == null) { %>
          <input type="submit" name="command" value="Create" tabindex="6" />
          <%
				} else {
				%>
          <input type="hidden" name="wlo_id" value="<%= wlo.getId() %>" />
          <input type="submit" name="command" value="Clear" tabindex="7" />
          <input type="submit" name="command" value="Update" tabindex="8" />
        <%
					}
				%>      </td>
      <td width="37%" align="right" valign="top"><%	if (wlo.getId() != null) { %>
        <input type="submit" name="command" value="Delete" tabindex="9"  onClick="return confirm('Are you sure you want to delete this item?');" />
        <% } %></td>
    </tr>
  </table>
  </form>
  <p></p>
</blockquote>
<% } else { %>
<p>&nbsp;</p>
<p>No pages have been loaded.</p>

  <% } // End of if the user can not edit or add an article.%>
  <% 
  	// TODO Should make this customizable to show, not show, or only show if the player is control.
	//if (pso.isControlCharacter()) {
	
	
	List wloList = new ArrayList();
	
	if ((cs_id != null) && (!(cs_id.equalsIgnoreCase("null"))) && (pso.getRunningSimId() != null)) {
		wloList = WebLinkObjects.getAllForRunningSimulationAndSection(pso.schema, pso.getRunningSimId(), new Long(cs_id));	
	}
%>
<HR />
<h2>List of Articles</h2>
	<UL>
 	<% for (ListIterator li = wloList.listIterator(); li.hasNext();) {
			WebLinkObjects wlol = (WebLinkObjects) li.next();
			
			//java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy HH:mm a");
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy");
			
			String dateString = " unknown date ";
			
			if (wlol.getWebObjectDate() != null) {
				dateString = sdf.format(wlol.getWebObjectDate());
			}
			
			dateString += " - ";

		  	String userName = "";
		  	if (wlol.getU_id() == null){
				userName = "";
			} else {
				userName = USIP_OSP_Cache.getUSERName(pso.schema, request, wlol.getU_id());
			}

    %>
    <LI><%= dateString %> <%= wlol.getWeblinkName() %> Posted by <%= userName %> on <%= sdf.format(wlol.getPostingDate()) %></LI>
    <% } // end of loop over list %>
	
	</UL>
 
  <% // } // End of if they are a control character. %>
</body>
</html>
