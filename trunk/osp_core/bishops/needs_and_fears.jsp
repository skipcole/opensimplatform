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
<table width="95%" border="0" cellspacing="2" cellpadding="2">
  <tr valign="top">
  <% if ((partyList != null) && (partyList.size() >= 1)) { 
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(0);
  %>
    <td><p>Party 1: <%= bpi.getName() %> (edit/delete)</p>
      <h2>Need</h2>
      <p><%= bpi.getNeedsDoc() %>      </p>
      <p>&nbsp;</p>
      <h2>Fears</h2>
      <p><%= bpi.getFearsDoc() %>      </p>
      <p>&nbsp;</p>
      </td> 
    <% } %>
    <td><p>Party 2: _____ (edit/delete)</p>
        <p>Needs: ______</p>
      <p>Fears: _______</p>
      <p>&nbsp;</p></td>
    <td><p>Party 3: _____ (edit/delete)</p>
        <p>Needs: ______</p>
      <p>Fears: _______</p>
      <p>&nbsp;</p></td>
  </tr>
  <tr valign="top">
    <td><p>Party 3: _____ (edit/delete)</p>
        <p>Needs: ______</p>
      <p>Fears: _______</p>
      <p>&nbsp;</p></td>
    <td>Conflict info</td>
    <td><p>Party: _____ (edit/delete)</p>
        <p>Needs: ______</p>
      <p>Fears: _______</p>
      <p>&nbsp;</p></td>
  </tr>
  <tr valign="top">
    <td><p>Party: _____ (edit/delete)</p>
        <p>Needs: ______</p>
      <p>Fears: _______</p>
      <p>&nbsp;</p></td>
    <td><p>Party: _____ (edit/delete)</p>
        <p>Needs: ______</p>
      <p>Fears: _______</p>
      <p>&nbsp;</p></td>
    <td><p>Party: _____ (edit/delete)</p>
        <p>Needs: ______</p>
      <p>Fears: _______</p>
      <p>&nbsp;</p></td>
  </tr>
</table>
<p>&nbsp;</p>
<p>x</p>
<p>Below here are shown excess actors</p>
</body>
</html>
