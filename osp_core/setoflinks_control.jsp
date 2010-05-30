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
	
	SetOfLinksCustomizer solc = new SetOfLinksCustomizer();
	
	CustomizeableSection cs = CustomizeableSection.getMe(pso.schema, cs_id);
	
	solc = new SetOfLinksCustomizer(request, pso, cs);
	
	SetOfLinks sol = SetOfLinks.getMe(pso.schema, solc.getSolId());
	
	if (!(pso.preview_mode)) {	
		sol = SetOfLinks.getSetOfLinksForRunningSim(pso.schema, solc.getSolId(), pso.running_sim_id);
	}
	
	IndividualLink individualLink = new IndividualLink();
	individualLink.setDescription("");
	individualLink.setLinkString("");
	individualLink.setLinkTitle("");
	
	String queueup = request.getParameter("queueup");
	String il_id = request.getParameter("il_id"); 
	
	if ((queueup != null) && (queueup.equalsIgnoreCase("true")) && (il_id != null) && (il_id.trim().length() > 0)) {		
		individualLink = IndividualLink.getMe(pso.schema, new Long(il_id));
	}
		
	String sending_page = request.getParameter("sending_page"); 
	
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("setoflinks_control"))){
		
		String create_il = request.getParameter("create_il");
		String clear_il = request.getParameter("clear_il");
		String update_il = request.getParameter("update_il");
		
		if (clear_il != null) {
			// Do nothing
		} else {

			if (update_il != null) {
				individualLink = IndividualLink.getMe(pso.schema, new Long(il_id));
			}
			
			if 	((update_il != null)  || (create_il != null) ) {	
				String link_title = request.getParameter("link_title"); 
				String link_string = request.getParameter("link_string");
				String link_desc = request.getParameter("link_desc");
		
				individualLink.setActor_id(pso.actor_id);
				individualLink.setDescription(link_desc);
				individualLink.setLinkString(link_string);
				individualLink.setLinkTitle(link_title);
				individualLink.setRunning_sim_id(pso.running_sim_id);
				individualLink.setSet_of_links_id(sol.getId());
				individualLink.setSim_id(pso.sim_id);
		
				individualLink.saveMe(pso.schema);
		
			}
		} // end of 'else'
	}
	
%>
<html>
<head>
<title>Set of Links Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<p><%= cs.getBigString() %></p>
<p><%= sol.getName() %></p>
<blockquote>
<form name="form1" method="post" action="setoflinks_control.jsp">
<input type="hidden" name="sending_page" value="setoflinks_control">
<input type="hidden" name="cs_id" value="<%= cs_id %>">
<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="13%" valign="top">Title:</td>
    <td width="87%" valign="top"><label>
      <input type="text" name="link_title" id="link_title" value="<%= individualLink.getLinkTitle() %>">
    </label></td>
  </tr>
  <tr>
    <td valign="top">Link:</td>
    <td valign="top"><label>
      <input name="link_string" type="text" id="link_string" size="80" value="<%= individualLink.getLinkString() %>">
    </label></td>
  </tr>
  <tr>
    <td valign="top">Description:</td>
    <td valign="top"><label>
      <textarea name="link_desc" id="link_desc" cols="80" rows="4"><%= individualLink.getDescription() %></textarea>
    </label>    </td>
  </tr>
  <tr>
    <td valign="top">Set ():</td>
    <td valign="top"><p>This link is associated with a set of simulations?</p>
      <p>yes
        <input type="radio" name="link_of_a_set" id="radio2" value="yes">
/ no
<input type="radio" name="link_of_a_set" id="radio" value="no">
      </p>
      <select name="select" id="select">
      <option value="0">None</option>
      <p><%   List rssList = RunningSimSet.getAllForSim(afso.sim_id.toString(), afso.schema);
			
				for (ListIterator li = rssList.listIterator(); li.hasNext();) {
					RunningSimSet rss = (RunningSimSet) li.next();
			  
			  %>
              <option value="<%= rss.getId() %>"><%= rss.getRunningSimSetName() %></option>
              <% } %>
        </select>
      </td>
  </tr>
  <tr>
    <td valign="top">&nbsp;</td>
    <td valign="top"><%
				if (individualLink.getId() == null) {
				%>
      <input type="submit" name="create_il" value="Create" />
      <%
				} else {
				%>
      <input type="submit" name="clear_il" value="Clear" tabindex="6" />
      <input type="hidden" name="il_id" value="<%= individualLink.getId() %>" />
      <input type="submit" name="update_il" value="Update" />
      <%
					}
				%></td>
  </tr>
</table>
</form>
</blockquote>
<p>as a first step, show all of the sets that this running simulation is associated with:</p>
<p><%
	List mySets = RunningSimSetAssignment.getAllForRunningSimulation(pso.schema, pso.running_sim_id);
	
					for (ListIterator li = mySets.listIterator(); li.hasNext();) {
					RunningSimSetAssignment rss = (RunningSimSetAssignment) li.next();
					%>
                    found one with set id of <%= rss.getRs_set_id() %>
<%
					}

%></p>
<p>for each set, display all links in thatt set of links</p>
<p>get set of links that are associated with this running simulation</p>
<p></p>
<h2>Quick Links for 'running simulation set name'.</h2>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <%
		List linkList = IndividualLink.getAllForSetOfLinks(pso.schema, sol.getId());
		
		for (ListIterator<IndividualLink> li = linkList.listIterator(); li.hasNext();) {
			IndividualLink this_link = li.next();   %>
  <tr>
    <td colspan="2"><strong><%= this_link.getLinkTitle() %> (<a href="setoflinks_control.jsp?cs_id=<%= cs_id %>&il_id=<%= this_link.getId() %>&queueup=true">Click here to Edit</a>)</strong></td>
  </tr>
    <td width="17%">&nbsp;</td>
    <td><a href="<%= this_link.getLinkString() %>" target="_new"><%= this_link.getLinkString() %></a></td>
  </tr>
    <td width="17%">&nbsp;</td>
    <td><%= this_link.getDescription() %></td>
  </tr>
  <% 	} %>
</table>
<p></p>
<p>&nbsp;</p>
<h2>Below are the Quick Links for 'running simulation name'.</h2>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
<%
		List linkList = IndividualLink.getAllForSetOfLinks(pso.schema, sol.getId());
		
		for (ListIterator<IndividualLink> li = linkList.listIterator(); li.hasNext();) {
			IndividualLink this_link = li.next();   %>
		
        <tr>
			<td colspan="2"><strong><%= this_link.getLinkTitle() %> (<a href="setoflinks_control.jsp?cs_id=<%= cs_id %>&il_id=<%= this_link.getId() %>&queueup=true">Click here to Edit</a>)</strong></td>
        </tr>
			<td width="17%">&nbsp;</td><td><a href="<%= this_link.getLinkString() %>" target="_new"><%= this_link.getLinkString() %></a></td>
			</tr>
			<td width="17%">&nbsp;</td><td><%= this_link.getDescription() %></td></tr>
       <% 	} %>
</table>
       
<p>&nbsp;</p>
</body>
</html>
