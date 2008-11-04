<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" errorPage="" %>
<%
	String attempting_login = (String) request.getParameter("attempting_login");
	
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	System.out.println("in validate jsp");
	
	if ((attempting_login != null) && (attempting_login.equalsIgnoreCase("true"))){
		
		System.out.println("attemptin validatin");
		
		BaseUser bu = pso.validate(request);
		
		if (bu != null){
		
			System.out.println("bu id " + bu.getId());
			pso.user_id = bu.getId();
			System.out.println("pso id " + pso.user_id);

			if (bu.getAuthorizedSchemas().size() == 0){
				pso.errorMsg = "You are not authorized to enter any schema.";
			} else if (bu.getAuthorizedSchemas().size() == 1){
				// Send them on directly to this schema
				SchemaGhost sg = (SchemaGhost) bu.getAuthorizedSchemas().get(0);
				System.out.println("ghost schema is " + sg.getSchema_name());
				pso.schema = sg.getSchema_name();
				User user = pso.loginToSchema(pso.user_id, sg.getSchema_name(), request);
				
				if ((user.isSim_author()) || (user.isSim_instructor()) ) {
					response.sendRedirect("intro.jsp");
					return;
				} else {
					pso.errorMsg = "Not authorized to author or instruct simulations.";
					response.sendRedirect("index.jsp");
					return;
				}
				
			} else if (bu.getAuthorizedSchemas().size() > 1){
				// Send them on to the page where they can select schema.
				response.sendRedirect("pick_schema.jsp");
				return;
			}
			
		} else {
			pso.errorMsg = "Failed Login Attempt";
			response.sendRedirect("index.jsp");
			return;
		}

	} // End of if login in.
			
%>
<html>
<head>
<title>USIP Online Simulation Platform Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../usip_osp.css" rel="stylesheet" type="text/css">
</head>

<body>
Page should not be seen.
</body>
</html>