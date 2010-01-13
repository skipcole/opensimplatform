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
		
	BishopsPartyInfo bpi = BishopsPartyInfo.handelAdd(request, pso);
	

	
	int numParties = 9;
	
	if (!(pso.preview_mode)) {
		numParties = BishopsPartyInfo.numberOfParties(pso.schema, pso.running_sim_id) + 1;
	}
	
%>
<html>
<head>
<title>Add Party Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h2 align="left">Add/Edit a Party</h2>
<form name="form1" method="post" action="">
<input type="hidden" name="sending_page" value="add_party">
<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="top">Party</td>
    <td valign="top">
      <label>
        <input type="text" name="party_name" id="party_name" value="<%= bpi.getName() %>">
        </label>    </td>
  </tr>
  <tr>
    <td valign="top">Index</td>
    <td valign="top"><label>
      <select name="party_index" id="party_index">
      	<% 
		
		
		boolean foundIndex = false;
		
		for (int ii = 1; ii <= numParties ; ++ii) {
        	String selected_ii = "";
            
            if (ii == bpi.getPartyIndex()) {
            	selected_ii = " selected ";
				foundIndex = true;
            }
			
			if ((ii == numParties) && (!foundIndex)) {
				selected_ii = " selected ";
			}
			
		%>
        
        		<option value="<%= ii %>" <%= selected_ii %> ><%= ii %></option>
        <% } %>
      </select>
    </label></td>
  </tr>
  <tr>
    <td valign="top">Needs</td>
    <td valign="top"><label>
      <textarea name="party_needs" id="party_needs" cols="45" rows="5"><%= bpi.getNeedsDoc() %></textarea>
    </label></td>
  </tr>
  <tr>
    <td valign="top">Fears</td>
    <td valign="top"><label>
      <textarea name="party_fears" id="party_fears" cols="45" rows="5"><%= bpi.getFearsDoc() %></textarea>
    </label></td>
  </tr>
  <tr>
    <td valign="top">Status</td>
    <td valign="top">
    <%
		String checked_inactive = "";
		
		if (bpi.isInActive()) {
			checked_inactive = " checked ";
		}
		
	%>
    <label>
      <input type="checkbox" name="marked_inactive" id="checkbox" <%= checked_inactive %>>
    Mark Inactive</label></td>
  </tr>
  <tr>
    <td valign="top">&nbsp;</td>
    <td valign="top"><%
				if (bpi.getId() == null) {
				%>
      <input type="submit" name="command" value="Create" />
      <%
				} else {
				%>
      <input type="hidden" name="bpi_id" value="<%= bpi.getId() %>" />
      <input type="hidden" name="sim_id" value="<%= pso.sim_id %>" />
      <input type="submit" name="command" value="Clear" tabindex="6" />
      <input type="submit" name="command" value="Update" />
      <%
					}
				%></td>
  </tr>
</table>
</form>
<p align="left">&nbsp;</p>
<p align="left">&nbsp;</p>
<p>&nbsp;</p>
</body>
</html>
