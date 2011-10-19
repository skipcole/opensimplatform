<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.persistence.*" 
	errorPage="/error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	
    Simulation simulation = afso.handleCopySim(request); 
	
	if (afso.forward_on) {
		afso.forward_on = false;
		response.sendRedirect("create_simulation_done.jsp?comingfrom=Copied");
		return;
	}
	
	String sim_id = (String) request.getParameter("sim_id");
	
	Simulation oldSim = Simulation.getById(afso.schema, new Long(sim_id));

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>


<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
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
              <h1>Copy Simulation </h1>
              <p>On this page you can create a copy of an existing simulation.</p>
              <br />
      <form action="copy_simulation.jsp" method="post" name="form1" id="form1">
        <input type="hidden" name="sending_page" value="copy" />
        <blockquote>
        
        <table width="80%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">Simulation Name <a href="helptext/sim_name.jsp" target="helpinright">(?)</a>:</td>
              <td valign="top">
  <input type="text" name="simulation_name" value="<%= oldSim.getSimulationName() %>" tabindex="1" /></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">Simulation Version <a href="helptext/sim_version.jsp" target="helpinright">(?)</a>:</td>
              <td valign="top">
  <input type="text" name="simulation_version" value="<%= oldSim.getVersion() %>" tabindex="2" /></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">Simulation Creating Organization <a href="helptext/sim_banner.jsp" target="helpinright">(?)</a>:</td>
              <td valign="top">
  <input type="text" name="creation_org" value="<%= oldSim.getCreation_org() %>" tabindex="3" /></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">&nbsp;</td>
              <td valign="top">
                <%
				if (afso.sim_id != null) {
				%>
				<input type="submit" name="command" value="Copy" />
                <input type="hidden" name="sim_id" value="<%= sim_id %>" />
                <%
					}
				%>              </td>
            </tr>
          </table>
		  
		  <p>Back to the <a href="control_panel.jsp">Control Panel </a></p>
		  <p>&nbsp;		        </p>
      </form>

      <p align="center"></p>			</td>
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