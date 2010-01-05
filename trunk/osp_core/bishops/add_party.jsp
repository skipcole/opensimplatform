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
		
	BishopsPartyInfo bpi = new BishopsPartyInfo();
	
	String sending_page = (String) request.getParameter("sending_page");
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("add_party"))){
	
		String command = (String) request.getParameter("command");
		
		if (command.equalsIgnoreCase("Clear")){
			
		}
		
		if (command.equalsIgnoreCase("Update")){
			String bpi_id = (String) request.getParameter("bpi_id");
			bpi = BishopsPartyInfo.getMe(pso.schema, new Long(bpi_id));
		}
		
		if ((command.equalsIgnoreCase("Update")) || (command.equalsIgnoreCase("Update"))){
			String party_name = (String) request.getParameter("party_name");
			String party_needs = (String) request.getParameter("party_needs");
			String party_fears = (String) request.getParameter("party_fears");
			
			bpi.setName(party_name);
			bpi.setNeedsDoc(party_needs);
			bpi.setFearsDoc(party_fears);
			bpi.setRunning_sim_id(pso.running_sim_id);
			bpi.setSim_id(pso.sim_id);
			bpi.saveMe(pso.schema);	
		}		
		
	}
	
	String queueu_up = (String) request.getParameter("queueu_up");
	
	if ((queueu_up != null) && (queueu_up.equalsIgnoreCase("true"))) {
		
		String bpi_id = (String) request.getParameter("bpi_id");
		
		bpi = BishopsPartyInfo.getMe(pso.schema, new Long(bpi_id));
	
	}
%>
<html>
<head>
<title>Add Party Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h2 align="left">Add a Party</h2>
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
        <option value="1">1</option>
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
    <td valign="top"><label>
      <input type="checkbox" name="checkbox" id="checkbox">
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
