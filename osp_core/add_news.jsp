<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
		
	RunningSimulation rs = pso.giveMeRunningSim();
	
	String sending_page = (String) request.getParameter("sending_page");
	String add_news = (String) request.getParameter("add_news");
	
	if ( (sending_page != null) && (add_news != null) && (sending_page.equalsIgnoreCase("add_news"))){
          
		String new_news = (String) request.getParameter("new_news");
		rs = pso.addNews(new_news, request);
		   
	} // End of if coming from this page and have added simulation.
	
	//pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	

%>
<html>
<head>
<title>News Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>

<body>
<h2>Add News</h2>
<form name="form1" method="post" action="add_news.jsp">
<input type="hidden" name="sending_page" value="add_news" />
  <label>
  <textarea name="new_news" cols="80" rows="3"></textarea>
  </label>
  <p>
    <label>
    <input type="submit" name="add_news" value="Submit">
    </label>
  </p>
</form>
<h2 align="center">
  Current News Displayed Here</h2>
<table width="80%" border="1" align="center">
  <tr>
    <td><%= rs.getRs_news() %></td>
  </tr>
</table>
<div align="center"></div>
<p align="center">&nbsp;</p>

<p>&nbsp; </p>
<p>&nbsp;</p>
</body>
</html>
<%
	
%>
