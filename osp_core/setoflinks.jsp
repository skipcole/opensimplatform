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
<p><%= sol.getName() %></p>
<ul>
  <li><strong>Librarian's Page: <a href="http://fc.bishops.com/~lucys/html_files/drukerpeace2010.htm" target="_new">http://fc.bishops.com/~lucys/html_files/drukerpeace2010.htm</a></strong></li>
  <li><strong>Scholarly Resources: <a href="http://ezproxy.bishops.com:2048/login/" target="_new">http://ezproxy.bishops.com:2048/login/</a></strong></li>
</ul>
<p><strong>Quick Links</strong></p>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
<%
		List linkList = IndividualLink.getAllForSetOfLinks(pso.schema, sol.getId());
		
		for (ListIterator<IndividualLink> li = linkList.listIterator(); li.hasNext();) {
			IndividualLink this_link = li.next();   %>
		
        <tr>
			<td colspan="2"><strong><%= this_link.getLinkTitle() %></strong></td>
        </tr>
			<td width="17%">&nbsp;</td><td><a href="<%= this_link.getLinkString() %>" target="_new"><%= this_link.getLinkString() %></a></td>
			</tr>
			<td width="17%">&nbsp;</td><td><%= this_link.getDescription() %></td></tr>
       <% 	} %>
</table>
</body>
</html>
