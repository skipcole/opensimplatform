<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.bishops.*,
	org.usip.osp.communications.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String doc_1 = (String) request.getParameter("doc_1");
	String doc_2 = (String) request.getParameter("doc_2");
	
	SharedDocument doc1 = SharedDocument.getMe(pso.schema, new Long(doc_1));
	SharedDocument doc2 = SharedDocument.getMe(pso.schema, new Long(doc_2));
	
	
%>
<html>
<head>
<title>Compare Document Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h2 align="left">Compare Documents</h2>

<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="top" width="50%"><p>Previous</p>
      <h2>&nbsp;</h2>
    <%= doc1.getBigString() %></td>
    <td valign="top" width="50%"><p>Current</p>
    <h2>&nbsp;</h2>
    <%= doc2.getBigString() %></td>
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
