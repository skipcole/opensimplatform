<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.bishops.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	List partyList = new ArrayList();
	
	if (!(pso.preview_mode)) {	
		partyList = BishopsPartyInfo.getAllForRunningSim(pso.schema, pso.running_sim_id);
	}
%>
<html>
<head>
<title>Introduction Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<p align="center"><a href="add_party.jsp">Add a Party</a></p>
<table width="100%" border="1" cellspacing="0" cellpadding="2">
  <tr valign="top">
  <% if ((partyList != null) && (partyList.size() >= 1)) { 
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(0);
  %>
    <td width="33%"><h2>Party 1: <%= bpi.getName() %> (<a href="add_party.jsp?queueu_up=true&bpi_id=<%= bpi.getId() %>">edit/delete</a>)<br>
    </h2>
      <h2>Needs</h2>
      <p><%= bpi.getNeedsDoc() %>      </p>
      <p>&nbsp;</p>
      <h2>Fears</h2>
      <p><%= bpi.getFearsDoc() %>      </p>
      <p>&nbsp;</p>
      </td> 
    <% } %>
<% if ((partyList != null) && (partyList.size() >= 2)) { 
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(1);
  %>
    <td width="33%"><h2>Party 2: <%= bpi.getName() %> (<a href="add_party.jsp?queueu_up=true&bpi_id=<%= bpi.getId() %>">edit/delete</a>)<br>
    </h2>
      <h2>Needs</h2>
      <p><%= bpi.getNeedsDoc() %>      </p>
      <p>&nbsp;</p>
      <h2>Fears</h2>
      <p><%= bpi.getFearsDoc() %>      </p>
      <p>&nbsp;</p>
    </td> 
    <% } %>
    
    <% if ((partyList != null) && (partyList.size() >= 3)) { 
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(2);
  %>
    <td width="33%"><h2>Party 3: <%= bpi.getName() %> (<a href="add_party.jsp?queueu_up=true&bpi_id=<%= bpi.getId() %>">edit/delete</a>)<br>
    </h2>
      <h2>Needs</h2>
      <p><%= bpi.getNeedsDoc() %>      </p>
      <p>&nbsp;</p>
      <h2>Fears</h2>
      <p><%= bpi.getFearsDoc() %>      </p>
      <p>&nbsp;</p>
    </td> 
    <% } %>
  </tr>
  <tr valign="top">
<% if ((partyList != null) && (partyList.size() >= 4)) { 
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(3);
  %>
    <td><h2>Party 4: <%= bpi.getName() %> (<a href="add_party.jsp?queueu_up=true&bpi_id=<%= bpi.getId() %>">edit/delete</a>)<br>
    </h2>
      <h2>Needs</h2>
      <p><%= bpi.getNeedsDoc() %>      </p>
      <p>&nbsp;</p>
      <h2>Fears</h2>
      <p><%= bpi.getFearsDoc() %>      </p>
      <p>&nbsp;</p>
    </td> 
    <% } %>
    <td width="33%" bgcolor="#FFEEEE"><h2>Conflict info</h2></td>
<% if ((partyList != null) && (partyList.size() >= 5)) { 
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(4);
  %>
    <td><h2>Party 5: <%= bpi.getName() %> (<a href="add_party.jsp?queueu_up=true&bpi_id=<%= bpi.getId() %>">edit/delete</a>)<br>
    </h2>
      <h2>Needs</h2>
      <p><%= bpi.getNeedsDoc() %>      </p>
      <p>&nbsp;</p>
      <h2>Fears</h2>
      <p><%= bpi.getFearsDoc() %>      </p>
      <p>&nbsp;</p>
    </td> 
    <% } %>
  </tr>
  <tr valign="top">
<% if ((partyList != null) && (partyList.size() >= 6)) { 
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(5);
  %>
    <td><h2>Party 6: <%= bpi.getName() %> (<a href="add_party.jsp?queueu_up=true&bpi_id=<%= bpi.getId() %>">edit/delete</a>)<br>
    </h2>
      <h2>Needs</h2>
      <p><%= bpi.getNeedsDoc() %>      </p>
      <p>&nbsp;</p>
      <h2>Fears</h2>
      <p><%= bpi.getFearsDoc() %>      </p>
      <p>&nbsp;</p>
    </td> 
    <% } %>
    <% if ((partyList != null) && (partyList.size() >= 7)) { 
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(6);
  %>
    <td><h2>Party 7: <%= bpi.getName() %> (<a href="add_party.jsp?queueu_up=true&bpi_id=<%= bpi.getId() %>">edit/delete</a>)<br>
    </h2>
      <h2>Needs</h2>
      <p><%= bpi.getNeedsDoc() %>      </p>
      <p>&nbsp;</p>
      <h2>Fears</h2>
      <p><%= bpi.getFearsDoc() %>      </p>
      <p>&nbsp;</p>
    </td> 
    <% } %>
<% if ((partyList != null) && (partyList.size() >= 8)) { 
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(7);
  %>
    <td><h2>Party 8: <%= bpi.getName() %> (<a href="add_party.jsp?queueu_up=true&bpi_id=<%= bpi.getId() %>">edit/delete</a>)<br>
    </h2>
      <h2>Needs</h2>
      <p><%= bpi.getNeedsDoc() %>      </p>
      <p>&nbsp;</p>
      <h2>Fears</h2>
      <p><%= bpi.getFearsDoc() %>      </p>
      <p>&nbsp;</p>
    </td> 
    <% } %>
  </tr>
</table>
<p>&nbsp;</p>
<p>Additional Actors</p>
<p>&nbsp;</p>
<p>Inactive Actors</p>
<p>&nbsp;</p>
</body>
</html>
