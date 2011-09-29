<%@ page 
	contentType="text/html; charset=utf-8" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.sharing.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
		
%>
<html>
<head>
<title>Write Email</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>

<body>
<h2>Reason for Email</h2>

<%
	List roList = RespondableObjectRecipients.getAllForActorInRunningSim(pso.schema, pso.getActorId(), pso.getRunningSimId());
		
%>	
<p>Please indicate below if this email is in response any of the following events below. You can select as many as desired.</p>
<p>&nbsp;</p>	
<form name="form1" method="post" action="">
<table width="100%" border="0">
<%
	int ii = 0;
	for (ListIterator li = roList.listIterator(); li.hasNext();) {
		RespondableObjectRecipients rop = (RespondableObjectRecipients) li.next();
		RespondableObject ro = RespondableObject.getById(pso.schema, rop.getRo_id());
		
		ii++;

%>
  <tr>
    <td width="7%">
      <label>
        <input type="checkbox" name="checkbox<%= ii %>" value="<%= ro.getId() %>">
        </label>    </td>
    <td width="7%">Event:</td>
    <td width="86%"><%= rop.getId() %></td>
  </tr>
<% } %>
</table>

<p>If you desire to put any additional information regarding this email, you may write it below. (It will not be included in the email that gets sent.)</p>
<p>&nbsp;  </p>
<p>&nbsp;</p>
</form>
</body>
</html>

