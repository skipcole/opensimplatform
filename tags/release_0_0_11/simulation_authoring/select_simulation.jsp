<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.persistence.*,org.hibernate.*" 
	errorPage="../error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}

	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
	//////////////////////////////////
	// Get list of all simulations
	List simList = Simulation.getAll(afso.schema);
	
	String sending_page = (String) request.getParameter("sending_page");
	
	///////////////////////////////////
	
	boolean justAdded = false;
	
	String debug_string = "";

	//////////////////////////////////
	// Put sim on scratch pad
	String select_sim = (String) request.getParameter("select_sim");
	
	if ((select_sim != null) && (select_sim.equalsIgnoreCase("true"))){
		
		// Need to move this to method, and make sure all is done clean when switching between simulations.
		afso.sim_id = new Long(   (String) request.getParameter("sim_id")   );
		afso.actor_being_worked_on_id = null;
		
		simulation = afso.giveMeSim();
		
		response.sendRedirect(afso.backPage);
		return;
			
	}
	
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Select Simulation </h1>
              <br />
			 
      <blockquote>
        <p>&nbsp;</p>
          <p>Below are listed alphabetically all of the current Simulations. Please 
            select one, or <a href="create_simulation.jsp">create a new one</a>, 
            to continue.</p>
          <table>
            <%
		
		for (ListIterator li = simList.listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
			String nameToSend = java.net.URLEncoder.encode(sim.getName());
			
		%>
            <tr> 
              <td><a href="select_simulation.jsp?select_sim=true&amp;backpage=<%= afso.backPage %>&amp;sim_id=<%= sim.getId().toString() %>"><%= sim.getName() %> version <%= sim.getVersion() %></a></td>
            </tr>
            <%
	}
%>
            </table>
      </blockquote>      <p align="center">&nbsp;</p>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>