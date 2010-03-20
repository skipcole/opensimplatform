<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.specialfeatures.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
		
	SetOfLinksCustomizer solc = new SetOfLinksCustomizer();
	
	CustomizeableSection cs = CustomizeableSection.getMe(pso.schema, cs_id);
	solc = new SetOfLinksCustomizer(request, pso, cs);
	
	SetOfLinks sol = SetOfLinks.getMe(pso.schema, solc.getSolId());
	
	if (!(pso.preview_mode)) {	
		sol = SetOfLinks.getSetOfLinksForRunningSim(pso.schema, solc.getSolId(), pso.running_sim_id);
	}
	
%>
<html>
<head>
<title>Set of Links Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<p><%= cs.getBigString() %></p>
<p>Set of Links name = <%= sol.getName() %></p>
<p>Add Link</p>
<blockquote>
<form name="form1" method="post" action="setoflinks_control.jsp">
<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="13%">Title:</td>
    <td width="87%"><label>
      <input type="text" name="link_title" id="link_title">
    </label></td>
  </tr>
  <tr>
    <td>Link:</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>Description:</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>
        <input type="submit" name="button" id="button" value="Submit">
    </td>
  </tr>
</table>
</form>
</blockquote>
<p>&nbsp;</p>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
<%
		List linkList = IndividualLink.getAllForSetOfLinks("test", new Long(1));
		
		for (ListIterator<IndividualLink> li = linkList.listIterator(); li.hasNext();) {
			IndividualLink this_link = li.next();   %>
		
        <tr>
			<td colspan="2"><%= this_link.getLinkTitle() %></td></tr>
			<td width="17%">&nbsp;</td><td><%= this_link.getLinkString() %></td></tr>
			<td width="17%">&nbsp;</td><td><%= this_link.getDescription() %></td></tr>
       <% 	} %>
</table>
       
<p>&nbsp;</p>
</body>
</html>
