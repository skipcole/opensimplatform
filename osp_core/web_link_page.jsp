<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
	
	String topPage = "web_link_page_top.jsp?cs_id=" + cs_id;
	
	String bottomPage = "web_link_page_bottom.jsp?cs_id=" + cs_id;
	
	String wlo_id = (String) request.getParameter("wlo_id");
	
	if ((wlo_id != null) && (!(wlo_id.equalsIgnoreCase(""))) && (!(wlo_id.equalsIgnoreCase("0")))){
		WebLinkObjects wlo = WebLinkObjects.getMe(pso.schema, new Long(wlo_id));
		bottomPage = wlo.getWeblinkURL();
		
		topPage += "&wlo_id=" + wlo_id;
	}
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>USIP OSP Web Link Page</title>
</head>

<frameset rows="58,*">
  <frame src="<%= topPage %>">
  <frame src="<%= bottomPage %>">
</frameset>
<noframes><body>
</body>
</noframes></html>
