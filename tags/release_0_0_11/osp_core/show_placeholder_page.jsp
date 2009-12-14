<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));

	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String page_id = (String) request.getParameter("page_id");
	
	PlaceHolderPage pp = new PlaceHolderPage();
	
	pp.set_sf_id(page_id);
	
	pp.load();
	
%>
<html>
<head>
<title><%= pp.page_title %></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<body>
<h1><%= pp.page_title %> </h1>
<table width="95%" border="0" cellspacing="2" cellpadding="2">
  <tr valign="top"> 
    <td><p><%= pp.description %></p></td>
  </tr>
    <tr valign="top"> 
    <td><div align="center"><img src="images/placeholders/<%= pp.img_file_name %>" ></div></td>
  </tr>
</table>
<p>&nbsp;</p>
<p>&nbsp; </p>
</body>
</html>
<%
	
%>