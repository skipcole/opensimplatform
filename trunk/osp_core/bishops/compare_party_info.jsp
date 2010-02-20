<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.bishops.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String parent_id = (String) request.getParameter("parent_id");
	String my_id = (String) request.getParameter("my_id");
	
	BishopsPartyInfo bpi = BishopsPartyInfo.getMe(pso.schema, new Long(my_id));
	BishopsPartyInfo bpi_parent = BishopsPartyInfo.getMe(pso.schema, new Long(parent_id));
	
		
	
%>
<html>
<head>
<title>Compare Party Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h2 align="left">Compare Party Information</h2>

<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="top" width="50%"><p>Previous</p>
      <h2>Party <%= bpi_parent.getPartyIndex() %>: <%= bpi_parent.getBPIName() %><br>
      </h2>
      <h2>Needs</h2>
      <p><%= bpi_parent.getNeedsDoc() %> </p>
      <p>&nbsp;</p>
      <h2>Fears</h2>
    <p><%= bpi_parent.getFearsDoc() %> </p>      <p>&nbsp;</p></td>
    <td valign="top" width="50%"><p>Current</p>
    <h2>Party <%= bpi.getPartyIndex() %>: <%= bpi.getBPIName() %><br>
      </h2>
      <h2>Needs</h2>
      <p><%= bpi.getNeedsDoc() %> </p>
      <p>&nbsp;</p>
      <h2>Fears</h2>
    <p><%= bpi.getFearsDoc() %> </p>      <p>&nbsp;</p></td>
  </tr>
</table>
<p>&nbsp;</p>
<style type="text/css" media="print">
.printbutton {
  visibility: hidden;
  display: none;
}
</style>
<script>
document.write("<input type='button' " +
"onClick='window.print()' " +
"class='printbutton' " +
"value='Print This Page'/>");
</script>
</body>
</html>
