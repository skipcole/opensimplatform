<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.hibernate.*" 
	errorPage="/error.jsp" %>
<%

	String actor_id = (String) request.getParameter("actor_id");
	
	String comingfrompso = (String) request.getParameter("comingfrompso");
	
	SessionObjectBase sob =  USIP_OSP_Util.getSessionObjectBaseIfFound(request);
	
	if (!(sob.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();	
	
	if (sob.sim_id != null){
		simulation = sob.giveMeSim();
	}
	
	
	SimActorAssignment saa = SimActorAssignment.getBySimIdAndActorId(sob.schema, sob.sim_id, new Long(actor_id));
	Actor act = Actor.getById(sob.schema, saa.getActorId());

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>USIP Open Simulation Platform</title>

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
			  <h1>View Simulation Actor Assignment Notes </h1>
			  <h2></h2>
			  <table width="100%" border="1" cellspacing="0" cellpadding="2">
                <tr valign="top">
                  <td width="20%"><strong>Actor</strong></td>
                  <td width="80%"><%= act.getActorName() %></td>
                  </tr>
                <tr valign="top">
                  <td><strong>Required</strong></td>
                  <td><%= saa.getAssignmentTypeDescriptor() %></td>
                  </tr>
                <tr valign="top">
                  <td><strong>Priority</strong></td>
                  <td><%= saa.getAssignmentPriority() %></td>
                  </tr>
                <tr valign="top">
                  <td><strong>Role</strong></td>
                  <td><%= saa.getActors_role() %></td>
                  </tr>
                <tr valign="top">
                  <td><strong>Notes</strong></td>
                  <td><%= saa.getAssignmentNotes() %></td>
                  </tr>
              </table>
			  <br />
                  
            <p>&nbsp;</p>
			</td>
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
