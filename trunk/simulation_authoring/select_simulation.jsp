<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.persistence.*,org.hibernate.*" 
	errorPage="../error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	//////////////////////////////////
	// Get list of all simulations
	List simList = Simulation.getAll(afso.schema);

	afso.handleSelectSimulation(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
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
            select one, or <a href="create_simulation.jsp?clear=true">create a new one</a>, 
            to continue.</p>
          <p>&nbsp;</p>
          <table>
            <%
		
		for (ListIterator li = simList.listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
			String nameToSend = java.net.URLEncoder.encode(sim.getSimulationName());
			
		%>
            <tr> 
              <td><a href="select_simulation.jsp?select_sim=true&backpage=<%= afso.backPage %>&sim_id=<%= sim.getId().toString() %>"><%= sim.getSimulationName() %> version <%= sim.getVersion() %></a></td>
              <td><input type="submit" name="Submit" value="Copy" /></td>
              <td><input type="submit" name="Submit2" value="Rename" /></td>
              <td><input type="submit" name="Submit3" value="Delete" /></td>
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
