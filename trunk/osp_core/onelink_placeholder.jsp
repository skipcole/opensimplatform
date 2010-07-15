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
		
	OneLinkCustomizer olc = new OneLinkCustomizer();
	
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	olc = new OneLinkCustomizer(request, pso, cs);
	
	String forwardOnString = "";
	
	OneLink ol = OneLink.getById(pso.schema, olc.getOlId());
	
	if (!(pso.preview_mode)) {	
		ol = OneLink.getOneLinkForRunningSim(pso.schema, olc.getOlId(), pso.running_sim_id);
		forwardOnString = ol.generateForwardOnTag();
	}
	
%>
<html>
<head>
<title>One Link Placeholder Page</title>
<%= forwardOnString %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<table width="95%" border="0" cellspacing="2" cellpadding="2">
  <tr valign="top"> 
    <td><p>This page is a placeholder until this site has been set by the simulation facilitator.
    </p>
      </td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>
