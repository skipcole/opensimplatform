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
<p>&nbsp;</p>
</body>
</html>
