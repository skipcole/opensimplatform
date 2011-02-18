<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.persistence.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}

	
	Simulation simulation = new Simulation();
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
	RunningSimulation running_simulation = new RunningSimulation();
	if (afso.getRunningSimId() != null){
		running_simulation = afso.giveMeRunningSim();
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Open Simulation Platform Control Page</title>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
              <tr>
                <td width="120"><img src="../Templates/images/white_block_120.png" /></td>
                <td width="100%"><br />
                  <h1>Welcome!  </h1>
			  <blockquote>    
			  <% if (afso.getRunningSimId() != null) { %>
				<strong>Your last Running Simulation was: <%= running_simulation.getRunningSimulationName() %> </strong> 
				<% if (afso.sim_id != null) { %>(<a href="select_running_simulation.jsp">Select Another for the Simulation <%= simulation.getDisplayName() %> </a>)<br/>
				<% } %>
				<p>Your Dasbhoard</p>
				<table width="100%" border="1">
        <tr>
          <td valign="top"><strong>Student Name </strong></td>
          <td valign="top"><strong>Actor</strong></td>
          <td valign="top"><strong>Username</strong></td>
          <td valign="top"><strong>Status</strong></td>
          <td valign="top"><strong>Send</strong></td>
        </tr>
        <tr>
          <td valign="top">&nbsp;</td>
          <td valign="top">&nbsp;</td>
          <td valign="top">&nbsp;</td>
          <td valign="top">&nbsp;</td>
          <td valign="top">&nbsp;</td>
        </tr>
		</table>
				<% }  // End of if they have worked on a running sim before.%>
			  </blockquote>
                  <blockquote>
                    <p>&nbsp;</p>
                    <ul>
                      <li>Take me to the <a href="library.jsp">online library</a>.</li>
                      <li>Take me to the <a href="play_panel.jsp">simulation facilitation control panel</a>.</li>
                      <li>Take me to a <a href="view_running_sims.jsp">list of my simulations</a>. </li>
                    </ul>
                  </blockquote>
                  <p align="center"></p></td>
              </tr>
            </table></td>
        </tr>
        <tr>
          <td><p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
        </tr>
      </table></td>
  </tr>
</table>
<p>&nbsp;</p>
<p align="center">&nbsp;</p>
</body>
</html>
<%
	
%>
