<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.specialfeatures.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
	String ol_id = (String) request.getParameter("ol_id");
		
	OneLinkCustomizer olc = new OneLinkCustomizer();
	OneLink baseOL = new OneLink();
	OneLink rsOL = new OneLink();
	String forwardOnString = "";
	Long olId = null;
	
	if (USIP_OSP_Util.stringFieldHasValue(cs_id)){
		CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
		olc = new OneLinkCustomizer(request, pso, cs);
		baseOL = OneLink.getById(pso.schema, olc.getOlId());
		olId = olc.getOlId();
	}
	
	if (USIP_OSP_Util.stringFieldHasValue(ol_id)){
		olId = new Long(ol_id);
		baseOL = OneLink.getById(pso.schema, olId);
	}
	
	if (!(pso.preview_mode)) {	
		rsOL = OneLink.getOneLinkForRunningSim(pso.schema, olId, pso.getRunningSimId());
		
		rsOL = OneLink.checkForRunningSimOneLinkUpdate(request, pso.schema, rsOL);
	} else {
		rsOL = baseOL;
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
<input type="hidden" name="ol_id" value="<%= ol_id %>">
<input type="hidden" name="sending_page" value="set_one_link">
<table width="95%" border="0" cellspacing="2" cellpadding="2">
  <tr valign="top"> 
    <td><p>Current Link:</p></td>
    <td><p><%= rsOL.getStartingValue() %></p></td>
    </tr>
    <tr>
    <td><p>New Link :</p></td>
                <td><input type="text" name="new_value" value="<%= rsOL.getStartingValue() %>" width="80" /></td>
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