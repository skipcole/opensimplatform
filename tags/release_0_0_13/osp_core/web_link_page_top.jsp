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
	
	String cs_id = (String) request.getParameter("cs_id");
	String wlo_id = (String) request.getParameter("wlo_id");
	
	List wloList = new ArrayList();
	
	if (cs_id != null) {
		wloList = WebLinkObjects.getAllForRunningSimulationAndSection(pso.schema, pso.running_sim_id, new Long(cs_id));	
	}
	
	Long nowShowingId = new Long(0);
	WebLinkObjects nowShowingWLO = new WebLinkObjects();
	
	int nowShowingInt = 0;
	
	if (wlo_id != null) {
		nowShowingId = new Long(wlo_id);
		nowShowingInt = nowShowingId.intValue();
		nowShowingWLO = WebLinkObjects.getMe(pso.schema, nowShowingId);
	}
	
	// TODO: We should make this customizable, so it can indicate the kind of URL's being added.
	String noneSelectedString = "Add New Page";
		
%>
<html>
<head>
<title>Introduction Page</title>
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
<input type="hidden" name="cs_id" value="<%= cs_id %>">
  <select name="wlo_id" id="select">
  <%
  	String selected = "";
  	if(nowShowingWLO.getId() == null) {
		selected = " selected ";
	}
  %>
  <option value="0" <%= selected %>><%= noneSelectedString %></option>
  	<% for (ListIterator li = wloList.listIterator(); li.hasNext();) {
			WebLinkObjects wlo = (WebLinkObjects) li.next();
			
			selected = "";
			if (wlo.getId().intValue() == nowShowingInt){
				selected = " selected ";
			}
    %>
    <option value="<%= wlo.getId() %>" <%= selected %>><%= wlo.getWeblinkName() %></option>
    <% } %>
    </select>
  <input type="submit" name="button" id="button" value="Go!">
</form>
</td><td valign="top" width="50%"><div id="wlo_desc"><%= nowShowingWLO.getWeblinkDescription() %></div></td></tr>
</table>
<p>&nbsp;</p>
</body>
</html>
