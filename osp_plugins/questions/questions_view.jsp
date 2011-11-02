<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	com.seachangesimulations.osp.questions.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}

	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	Hashtable contents = cs.getContents();
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
%>
<html>
<head>
<title>Questions View Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h1><%=  QuestionCustomizer.getPageStringValue(cs, QuestionCustomizer.KEY_FOR_PAGETITLE) %></h1>
<p><%= cs.getBigString() %></p>

<%

	List<QuestionAndResponse> qAndRList = QuestionAndResponse.getAllForSim(pso.schema, pso.sim_id); 

			// Loop over all actors in the simulation
			for (ListIterator li = simulation.getActors(pso.schema).listIterator(); li.hasNext();) {
				Actor act = (Actor) li.next();
				
				// For each actor, get all of their user assignments
				List theUsersAssigned = UserAssignment.getUsersAssigned(pso.schema, pso.getRunningSimId(), act.getId());
					
				// Loop over all of the user assignments
				for (ListIterator liua = theUsersAssigned.listIterator(); liua.hasNext();) {
					UserAssignment ua = (UserAssignment) liua.next();
					
					if (ua.getUser_id() != null) {
						
						//loop over questions
						for (ListIterator lq = qAndRList.listIterator(); lq.hasNext();) {
							QuestionAndResponse this_qar = (QuestionAndResponse) lq.next();
					
							PlayerAnswer pa = 
								PlayerAnswer.getByQuestionRunningSimAndUserIds
								(pso.schema, this_qar.getId(), pso.getRunningSimId(),  ua.getUser_id());
					%>
                    Actor <%= act.getId() %>, User <%= ua.getUser_id() %>, <%= this_qar.getQuestionIdentifier() %>, <%= pa.getPlayerAnswer() %><br />
                    <%
						} // end of loop over questions
					} // end of if user id != null
					
				} // end of loop over user assignments
				
			} // End of loop over actors
			
%>
                    
<p>Get all questions for cs section and running sim, ordered by index, grouped by userid</p>
<p>loop over them, and show the players answers</p>
<p>&nbsp;</p>
</body>
</html>