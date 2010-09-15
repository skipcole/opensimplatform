<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
		
	Simulation sim = new Simulation();	
	
	String sending_page = (String) request.getParameter("sending_page");
	String inject_id = (String) request.getParameter("inject_id");
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("select_actors"))){
	
		pso.makeTargettedAnnouncement(request, inject_id);
		response.sendRedirect(pso.backPage);
		return;		
		
	}
	
	if (pso.sim_id != null){
		sim = pso.giveMeSim();
	}

%>
<html>
<head>
<title>Select Actors Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>

<body>
<h2>Select Actors to Receive This Inject </h2>
<form name="form1" method="post" action="select_actors.jsp">
<input type="hidden" name="inject_id" value="<%= inject_id %>">
<input type="hidden" name="sending_page" value="select_actors" />
<%
	List targets = new ArrayList();
	List targetRaw = InjectActorAssignments.getAllForInject(pso.schema, new Long(inject_id));
					
	///
	for (ListIterator liInjId = targetRaw.listIterator(); liInjId.hasNext();) {
		InjectActorAssignments targ = (InjectActorAssignments) liInjId.next();
		targets.add(targ.getActor_id());
		
	}
					
%>
<% for (ListIterator la = sim.getActors(pso.schema).listIterator(); la.hasNext();) {
	Actor act = (Actor) la.next(); 
	
	String thisActorIsTargetted = "";
					
	System.out.println("actor id: " + act.getId());
	if (targets.contains(act.getId())){
		thisActorIsTargetted= "checked=\"checked\"";
	}
	
	%>

	<label><input type="checkbox" name="actor_cb_<%= act.getId().toString() %>" value="true"  <%= thisActorIsTargetted %> /> <%= act.getActorName(pso.schema, pso.getRunningSimId(), request) %></label> <br/>	 
		<% } // End of loop over Actors %>
    <label>
    <input type="submit" name="add_news" value="Submit">
    </label>
  </p>
</form>

<p align="left">Actors selected will receive the following announcement:</p>
<%= pso.alertInQueueText %>
<p>&nbsp; </p>
<p>&nbsp;</p>
</body>
</html>
<%
	
%>
