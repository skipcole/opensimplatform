<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	String sending_page = (String) request.getParameter("sending_page");
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("sim_blurb_information"))) {
		
		afso.handleLoadPlayerAutoAssignedScenario(request);
		response.sendRedirect("../simulation/simwebui.jsp?tabposition=1");
		return;
		
	}
	
	///////////////////////////////////////////////////////////////////////
	String sim_id = (String) request.getParameter("sim_id");
	if (sim_id != null) {
		afso.sim_id = new Long(sim_id);
	}
	Simulation sim = new Simulation();	
	if (afso.sim_id != null){
		sim = afso.giveMeSim();
	}
	

	//////////////////////////////////////////////////////////////////////////
	
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
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Simulation: <%= sim.getSimulationName() %> : <%= sim.getVersion() %>  </h1>
              <br />
      <blockquote>
        <p><%= sim.getBlurb() %></p>
        
      <table>
        <%
			for (ListIterator lia = sim.getActors(afso.schema).listIterator(); lia.hasNext();) {
				Actor act = (Actor) lia.next();
			
		%>
        
        <tr> 
          <td>Enter Simulation as: </td>
              <td><%= act.getActorName() %></td>
              <td>
                <form id="form_<%= sim.getId() %><%= act.getId() %>" name="form_<%= sim.getId() %><%= act.getId() %>" method="post" action="sim_blurb_information.jsp">
                  <input type="hidden" name="sim_id" value="<%= sim.getId() %>" />
                  <input type="hidden" name="actor_id" value="<%= act.getId() %>" />
                  <input type="hidden" name="sending_page" value="sim_blurb_information" /> 
                  <label>
                    <input type="submit" name="button" id="button" value="Enter Sim" />
                    </label>
                  </form>              </td>
            </tr>
        
        <%	} // End of loop over actors    %> 
        </table>
        </blockquote>      <p align="left">&nbsp;</p>			</td>
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
<%
	
%>
