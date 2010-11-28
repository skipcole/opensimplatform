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
		response.sendRedirect("../login.jsp");
		return;
	}
	
	
	String cs_id = (String) request.getParameter("cs_id");
	
	SetOfLinksCustomizer solc = new SetOfLinksCustomizer();
	
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	solc = new SetOfLinksCustomizer(request, pso, cs);
	
	SetOfLinks sol = SetOfLinks.getById(pso.schema, solc.getSolId());

	//IndividualLink individualLink = new IndividualLink();
	IndividualLink individualLink = new IndividualLink();
	
	if (!(pso.preview_mode)) {	
		sol = SetOfLinks.getSetOfLinksForRunningSim(pso.schema, solc.getSolId(), pso.getRunningSimId());
		individualLink = IndividualLink.handleEdit (request, pso, sol.getId());
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
    <td valign="top">Set of Links Page</td>
    <td valign="top"><p>Here we must loop over the sets of links pages created for this simulation.</p>
      <p>We allow them to select the 'cs_id' essentially.</p>
      <p>They should have created a set of links page before creating this control page.</p></td>
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
      <p><%   List rssList = RunningSimSet.getAllForSim(pso.sim_id.toString(), pso.schema);
			
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

<p><%
	List mySets = RunningSimSetAssignment.getAllForRunningSimulation(pso.schema, pso.getRunningSimId());
	
					for (ListIterator lim = mySets.listIterator(); lim.hasNext();) {
					RunningSimSetAssignment rssa = (RunningSimSetAssignment) lim.next();
					
					RunningSimSet rss = RunningSimSet.getById(pso.schema, rssa.getRs_set_id());
					%>
                    
<h2>Quick Links for <%= rss.getRunningSimSetName() %></h2>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <%
		List linkList = IndividualLink.getAllForSetOfRunningSims(pso.schema, rss.getId(), new Long(cs_id));
		
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
  <% 	} // End of links in this set %>
</table>
<% } // end of loop over sets %>
<p>&nbsp;</p>






<h2>Below are the Quick Links for running simulation:<%= pso.run_sim_name %>.</h2>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
<%
		List thisRSLinkList = IndividualLink.getAllForSetOfLinks(pso.schema, sol.getId());
		
		for (ListIterator<IndividualLink> li = thisRSLinkList.listIterator(); li.hasNext();) {
			IndividualLink this_link = li.next();   %>
		
        <tr>
			<td colspan="2"><strong><%= this_link.getLinkTitle() %> (<a href="setoflinks_control.jsp?cs_id=<%= cs_id %>&il_id=<%= this_link.getId() %>&queueup=true">Click here to Edit</a>)</strong></td>
        </tr>
			<td width="17%">&nbsp;</td><td><a href="<%= this_link.getLinkString() %>" target="_new"><%= this_link.getLinkString() %></a></td>
			</tr>
			<td width="17%">&nbsp;</td><td><%= this_link.getDescription() %></td></tr>
       <% 	} %>
</table>
</blockquote>


<p>&nbsp;</p>
</body>
</html>
