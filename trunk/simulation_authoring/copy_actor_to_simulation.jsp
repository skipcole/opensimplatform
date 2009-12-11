<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	String sending_page = (String) request.getParameter("sending_page");
	String addactortosim = (String) request.getParameter("addactortosim");
	String remove = (String) request.getParameter("remove");
	
	String actor_being_worked_on_id = (String) request.getParameter("actor_being_worked_on_id");
	String sim_id = (String) request.getParameter("sim_id");
	
	if ( (sending_page != null) && (addactortosim != null) && (sending_page.equalsIgnoreCase("assign_actor"))){
		afso.addActorToSim(sim_id, actor_being_worked_on_id);
	} // End of if coming from this page and have assigned actor
	
	if ( (remove != null) &&  (remove.equalsIgnoreCase("true"))){
		afso.removeActorFromSim(sim_id, actor_being_worked_on_id);
		     
	} // End of if coming from this page and have removed actor
	

	//////////////////////////////////
	List simList = Simulation.getAll(afso.schema);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Copy 
                Actor to a Simulation</h1>
              <p>On this page you can select an actor from a different simulation and have them copied into this simulation.</p>
              <br />
      <table width="100%" border="0" cellspacing="2" cellpadding="2">
        <tr valign="top">
          <td width="1%">&nbsp;</td>
            <td width="37%"> <h2>Simulation/Version</h2></td>
            <td width="26%"> <h2>Actors</h2></td>
            </tr>
        <%
		
		for (ListIterator li = simList.listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
			
		%>
        <form action="assign_actor_to_simulation.jsp" method="post" name="form1" id="form1">
          <tr valign="top">
            <td>&nbsp;</td>
              <td><%= sim.getName() %> : <%= sim.getVersion() %></td>
              <td><%
			
			for (ListIterator la = Actor.getAllForSim(afso.schema, sim.getId()).listIterator(); la.hasNext();) {
				Actor act = (Actor) la.next();

			%> 
                Copy in:  <%= act.getName() %> <br/>
                <% } // End of loop over Actors %>                </td>
              </tr>
            
            <tr><td colspan="3"><hr /></td>
            </tr>
          </form>
          <%
  	} // End of loop over simulations
  %>
      </table>
      <p>&nbsp;</p>
      <div align="center"></div>
      <a href="assign_actor_to_simulation.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
<%
	
%>