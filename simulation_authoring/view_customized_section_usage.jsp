<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*,org.usip.osp.graphs.*" 
	errorPage="/error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
		

    String bss_id = request.getParameter("bss_id");
        

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>USIP Open Simulation Platform Delete Object Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>This Customized Section is used in The Following Actor/Phase Locations</h1>
              <p>&nbsp;</p>
              <blockquote>
                <table width="100%" border="1" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="33%"><strong>Simulation</strong></td>
                    <td width="33%"><strong>Actor</strong></td>
                    <td><strong>Phase</strong></td>
                  </tr>
            <% 
		  
			  List theUsage = SimulationSectionAssignment.getUsage(afso.schema, new Long(bss_id));
		  
			  for (ListIterator li = theUsage.listIterator(); li.hasNext();) {
				SimulationSectionAssignment this_ssa = (SimulationSectionAssignment) li.next();
				
				String this_actor_name = "";
				if (this_ssa.getActor_id().intValue() == 0) {
					this_actor_name = "Universal";
				} else {
					this_actor_name = USIP_OSP_Cache.getActorBaseName(afso.schema, this_ssa.getSim_id(), request, this_ssa.getActor_id());
				}
			
		  %>
                  <tr>
                    <td width="33%"><%= USIP_OSP_Cache.getSimulationNameById(request, afso.schema, this_ssa.getSim_id()) %> </td>
                    <td width="33%">
					
					<%= this_actor_name  %></td>
                    <td><%= USIP_OSP_Cache.getPhaseNameById(request, afso.schema, this_ssa.getPhase_id()) %></td>
                  </tr>      
          <%
		 }
		  %>
          </table>
          </p>
      </blockquote>
      <blockquote>
        <p><a href="<%= afso.backPage %>">Back</a></p>
      </blockquote>			</td>
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
