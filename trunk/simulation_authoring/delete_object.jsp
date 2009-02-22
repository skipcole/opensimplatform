<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*,org.usip.osp.graphs.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
		
	String objectType = request.getParameter("object_type");
    String objectInfo = request.getParameter("object_info");
    String objid = request.getParameter("objid");
    String cancel_action = request.getParameter("cancel_action");
        
    String debug = "";
	
	boolean doneWithThis = pso.handleDeleteObject(request);
	
	if (doneWithThis) {
		response.sendRedirect(pso.backPage);
		return;
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>USIP Open Simulation Platform Delete Object Page</title>



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
              <h1>Confirm Deletion</h1>
              <br />
      <blockquote>
        <h1>Confirm Deletion</h1>
          <p>Confirm deletion of <%= objectType %> <%= objectInfo %></p>
      </blockquote>
      <form action="delete_object.jsp" method="post" name="form1" id="form1">
        <blockquote>
          <p>
            <input type="hidden" name="object_type" value="<%= objectType %>" />
            <input type="hidden" name="object_info" value="<%= objectInfo %>" />
            <input type="hidden" name="objid" value="<%= objid %>" />
            <input type="submit" name="deletion_confirm" value="Submit" />
            <input type="submit" name="cancel_action" value="Cancel" />
            </p>
          </blockquote>
      </form>
      <blockquote> 
        <p>&nbsp;</p>
          <p><a href="<%= pso.backPage %>">Back</a></p>
      </blockquote>
      <%= Debug.getDebug(debug) %>			</td>
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
