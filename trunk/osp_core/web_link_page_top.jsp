<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
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
	
	String cs_id = (String) request.getParameter("cs_id");
	String wlo_id = (String) request.getParameter("wlo_id");
	
	List wloList = new ArrayList();
	
	if ((cs_id != null) && (!(cs_id.equalsIgnoreCase("null"))) && (pso.getRunningSimId() != null)) {
		wloList = WebLinkObjects.getAllForRunningSimulationAndSection(pso.schema, pso.getRunningSimId(), new Long(cs_id));	
	}
	
	Long nowShowingId = new Long(0);
	WebLinkObjects nowShowingWLO = new WebLinkObjects();
	
	int nowShowingInt = 0;
	
	if (wlo_id != null) {
		nowShowingId = new Long(wlo_id);
		nowShowingInt = nowShowingId.intValue();
		nowShowingWLO = WebLinkObjects.getById(pso.schema, nowShowingId);
	}
	
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	String linkName = (String) cs.getContents().get(WebLinksCustomizer.KEY_FOR_LINK_NAME);
	
	// TODO: We should make this customizable, so it can indicate the kind of URL's being added.
	String noneSelectedString = "Add " + linkName;
		
%>
<html>
<head>
<title>Web Link Page Top</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
<!--

//-->
</script>
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<table width="100%">
  <tr><td valign="top">
<form name="wlo_form" method="post" action="web_link_page.jsp" target="mainFrame">
<input type="hidden" name="sending_page" value="web_link_page_top">
<input type="hidden" name="cs_id" value="<%= cs_id %>">
  <select name="wlo_id" id="select">
  <%
  	String selected = "";
	
	boolean showNewWindowLink = true;
	
  	if(nowShowingWLO.getId() == null) {
		selected = " selected ";
		showNewWindowLink = false;
	}
  %>
  <option value="0" <%= selected %>><%= noneSelectedString %></option>
  	<% for (ListIterator li = wloList.listIterator(); li.hasNext();) {
			WebLinkObjects wlo = (WebLinkObjects) li.next();
			
			selected = "";
			if (wlo.getId().intValue() == nowShowingInt){
				selected = " selected ";
			}
			
			//java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy HH:mm a");
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("mm/dd/yy");
			
			String dateString = " unknown date ";
			
			if (wlo.getWebObjectDate() != null) {
				dateString = sdf.format(wlo.getWebObjectDate());
			}
			
			dateString += " - ";
    %>
    <option value="<%= wlo.getId() %>" <%= selected %>><%= dateString %><%= wlo.getWeblinkName() %></option>
    <% } %>
    </select>
  <input type="submit" name="command" id="go_button" value="Go!">
  <label>
  <input type="submit" name="command" id="edit_button" value="Edit">
  </label>
</form>
<br /> <a href="similie_on_the_fly_timeline.jsp" target="wlo_bottom" >TimeLine (in development)</a></td>
  <td valign="top" ><div id="wlo_desc"><%= nowShowingWLO.getWeblinkDescription() %>
<% if (showNewWindowLink) { %>
<BR><a href="<%= nowShowingWLO.getWeblinkURL() %>" target="_new"> Open in new Window </a>
<% } %>
</div></td></tr>
</table>
<p>&nbsp;</p>
</body>
</html>
