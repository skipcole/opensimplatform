<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));

	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	pso.actor_being_worked_on_id = pso.getActorId();
	TipsCustomizer tc = new TipsCustomizer(request, pso, cs);
	
	String sending_page = (String) request.getParameter("sending_page");
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("tips"))){
	
		System.out.println("adding tip");
		
		Tips tip = new Tips();
	
		String tip_id = (String) request.getParameter("tip_id");
		if ((tip_id != null) && (!(tip_id.equalsIgnoreCase("null")))   ){
			tip.setId(new Long(tip_id));
		}
		String tip_page_text = request.getParameter("tip_page_text");
		tip.setTipText(tip_page_text);
		tip.setActorId(pso.getActorId());
		tip.setPhaseId(pso.phase_id);
		tip.setCsId(cs.getId());
		tip.setSimId(pso.sim_id);
		tip.setTipLastEditDate(new java.util.Date());
		tip.setBaseTip(false);
		tip.setParentTipId(tc.getTip().getId());
		tip.setUserId(pso.user_id);
		tip.setUserName(pso.userDisplayName);
		tip.setUserEmail(pso.user_name);

		tip.saveMe(pso.schema);

	}
	
%>
<html>
<head>
<title>USIP OSP Text Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body><%= tc.getTip().getTipText() %>

<% if (tc.getCanLeaveTip() ) { %>
<hr>
<p>Tips left by Others</p>

<%

	List tl = new ArrayList();
	
	if ((tc.getTip() != null) && (tc.getTip().getId() != null) ) {
		tl = Tips.getChildren(tc.getTip().getId(), pso.schema);
	}
	
    Tips this_users_tip = new Tips();
	
	for (ListIterator li = tl.listIterator(); li.hasNext();) {
		Tips this_tip = (Tips) li.next();

%><p>	<% if ( (pso.user_id != null) && (pso.user_id.equals(this_tip.getUserId())) ) { 
	this_users_tip = this_tip;
%>
		<% } else { %>
		<p><%= this_tip.getTipText() %><br /></p>
		<% } %>
</p>
<% } // End of loop over other people's tips.  %>

You can leave your tip here.

<form name="form1" method="post" action="tips.jsp">
  <label>
  <textarea name="tip_page_text"><%= this_users_tip.getTipText()%></textarea>
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
