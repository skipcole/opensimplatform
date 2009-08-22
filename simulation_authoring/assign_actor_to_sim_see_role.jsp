<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	String sim_id = (String) request.getParameter("sim_id");
	String actor_being_worked_on_id = (String) request.getParameter("actor_being_worked_on_id");
	
	SimActorAssignment saa = SimActorAssignment.getMe(afso.schema, new Long(sim_id), new Long(actor_being_worked_on_id));
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

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
              <h1>Actor <%= actor_being_worked_on_id %> in  Simulation <%= sim_id %></h1>
              <br />
      <table width="100%" border="0" cellspacing="2" cellpadding="2">
        
        <form action="assign_actor_to_simulation.jsp" method="post" name="form1" id="form1">
          <tr valign="top">
            <td>Role:</td>
              <td><label>
                <textarea name="textarea" id="textarea" cols="45" rows="5"><%= saa.getActors_role() %></textarea>
                </label></td>
            </tr>
          <tr valign="top">
            <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
          </form>
      </table>
      <p>&nbsp;</p>
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