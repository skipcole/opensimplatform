<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.specialfeatures.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
		
	OneLinkCustomizer olc = new OneLinkCustomizer();
	
	CustomizeableSection cs = CustomizeableSection.getMe(pso.schema, cs_id);
	olc = new OneLinkCustomizer(request, pso, cs);
	
	String forwardOnString = "";
	
	OneLink ol = OneLink.getMe(pso.schema, olc.getOlId());
	
	if (!(pso.preview_mode)) {	
		ol = OneLink.getOneLinkForRunningSim(pso.schema, olc.getOlId(), pso.running_sim_id);
		
		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null) && (  sending_page.equalsIgnoreCase("set_one_link")  ) ) {
			String newValue = (String) request.getParameter("new_value");
		
			if (newValue != null) {
				ol.setStartingValue(newValue);
				ol.saveMe(pso.schema);
			}
		}
	}
	
%>
<html>
<head>
<title>One Link Set Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
On this page you can set the web site that the 'One Link' page for another player is pointing to.
<form name="form1" method="post" action="onelink_set_page.jsp">
<input type="hidden" name="cs_id" value="<%= cs_id %>">
<input type="hidden" name="sending_page" value="set_one_link">
<table width="95%" border="0" cellspacing="2" cellpadding="2">
  <tr valign="top"> 
    <td><p>Current Link:</p></td>
    <td><p><%= ol.getStartingValue() %></p></td>
    </tr>
    <tr>
    <td>New Link :</td>
                <td><input type="text" name="new_value" value="<%= ol.getStartingValue() %>" /></td>
  </tr>
    <tr>
      <td>&nbsp;</td>
      <td>
        <label>
          <input type="submit" name="button" id="button" value="Submit">
        </label>
   
      </td>
    </tr>
</table>   
</form>
<p>&nbsp;</p>
</body>
</html>
