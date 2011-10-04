<%@ page
        contentType="text/html; charset=UTF-8"
        language="java"
        import="java.sql.*,java.util.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*,
		org.usip.osp.baseobjects.*"
        errorPage="/error.jsp" %><%
		
		// The purpose of this file is to bring the user's screen cleanly
		// back to the login page in the entire window (and not in just one
		// frame of the window.)
		
		%>
<link href="usip_osp.css" rel="stylesheet" type="text/css">
<script language="Javascript">
        top.location.href = "logout.jsp";
        window.location.href = "logout.jsp";
</script>
<style type="text/css">
<!--
.style1 {color: #FFFFFF}
-->
</style>
</head>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" valign="top"  background="Templates/images/top_fade.png">
      <span class="style1">You have been logged out. If java script was enabled on your machine, you should have been directed to the<a href="login.jsp"> login page</a>. </span></td>
    <td align="right" background="Templates/images/top_fade.png" width="20%">
    </td>
  </tr>
</table>
</body>
</html>