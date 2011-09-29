<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));

	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	pso.actor_being_worked_on_id = pso.getActorId();
	TipsCustomizer tc = new TipsCustomizer(request, pso, cs);
	
	pso.addPlayerTip(cs, tc, request);
	
%>
<html>
<head>
<title>USIP OSP Text Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h1>Simulation Author's Tip(s)</h1>
<p><%= tc.getTip().getTipText() %>
  
  <% if (tc.getCanLeaveTip() ) { %>
</p>
<hr />
<h1><strong>Tips left by Others</strong></h1>
<%

	List tl = new ArrayList();
	
	if ((tc.getTip() != null) && (tc.getTip().getId() != null) ) {
		tl = Tips.getChildren(tc.getTip().getId(), pso.schema);
	}
	
    Tips this_users_tip = new Tips();
	
	for (ListIterator li = tl.listIterator(); li.hasNext();) {
		Tips this_tip = (Tips) li.next();

%><p>	<% if ( (pso.user_name != null) && (pso.user_name.equalsIgnoreCase(this_tip.getUserEmail())) ) { 
	this_users_tip = this_tip;
%>
		<% } else { %>
		<p><strong>Tip left by <%= this_tip.getUserEmail() %></strong><br />
<%= this_tip.getTipText() %><br />
		</p>
<% } %>
</p>
<% } // End of loop over other people's tips.  %>

<strong>Your Tip.</strong> (<%= pso.user_name %>) 
<form name="form1" method="post" action="tips.jsp">
  <label>
  <textarea cols="80" name="tip_page_text"><%= this_users_tip.getTipText()%></textarea>
</label>

  <label>
  <input type="submit" name="Submit" value="Submit">
  </label>
  <input type="hidden" name="cs_id" value="<%= cs_id %>" >
  <input type="hidden" name="tip_id" value="<%= this_users_tip.getId() %>" >
  <input type="hidden" name="sending_page" value="tips" >
</form>
  <% } // End of if can leave tip. %>
</body>
</html>
