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
		if (tip_id != null){
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
		tip.setParentTipTextId(tc.getTip().getId());

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
peace
<p>
<%
	List tl = Tips.getChildren(tc.getTip().getId(), pso.schema);

	for (ListIterator li = tl.listIterator(); li.hasNext();) {
		Tips this_tip = (Tips) li.next();

%>
<%= this_tip.getId() %>
<% } %>
</p>
<p> <% if (true) { %>
<form name="form1" method="post" action="tips.jsp">
  <label>
  <textarea name="tip_page_text"></textarea>
</label>

  <label>
  <input type="submit" name="Submit" value="Submit">
  </label>
  <input type="hidden" name="cs_id" value="<%= cs_id %>" >
  <input type="hidden" name="sending_page" value="tips" >
</form>
<p>
  <% } // End of %>
  <% } // End of if can leave tip. %>
</body>
</html>