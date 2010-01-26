<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.bishops.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	////////////////////////////
	BishopsRoleVotes.handleSetVotes(request, pso);
	////////////////////////////
		
	List partyList = new ArrayList();
	
	Long selectedFirst = null;
	Long selectedSecond = null;
	Long selectedThird = null;
	
	if (!(pso.preview_mode)) {	
		partyList = BishopsPartyInfo.getAllForRunningSim(pso.schema, pso.running_sim_id, false);
		BishopsRoleVotes firstChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.running_sim_id, pso.user_id, 1);
		BishopsRoleVotes secondChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.running_sim_id, pso.user_id, 2);
		BishopsRoleVotes thirdChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.running_sim_id, pso.user_id, 3);
	
		if (firstChoice != null){
			selectedFirst = firstChoice.getId();
		}
		
		if (secondChoice != null){
			selectedSecond = secondChoice.getId();
		}
		
		if (thirdChoice != null){
			selectedSecond = thirdChoice.getId();
		}		
	
	}
	

	
%>
<html>
<head>
<title>Introduction Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<p>&nbsp;</p>
<p>Select your choices for the role you would like to play, then press submit.</p>
<form name="form1" method="post" action="select_role_page.jsp">
<input type="hidden" name="sending_page" value="select_role_page">
<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="33%" valign="top">First Choice:<br>
    <select name="first_choice" id="select">
    <%
		String selected1None = "";
		if (selectedFirst == null) {
			selected1None = " selected ";
		}
	%>
    <option value="0">None Selected</option>
	<%
	
	for (int ii = 0; ii <  partyList.size(); ++ii) {
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(ii);
		
		String selected = "";
		if (bpi.getId().equals(selectedFirst)){
			selected = "selected";
		}
  	%>
<option value="<%= bpi.getId() %>" <%= selected %>><%= bpi.getName() %></option>
      <% } %>
      </select>
      </td>
    <td valign="top" width="33%">Second Choice:<br>
      <label>
          <select name="second_choice" id="select">
          <option value="0">None Selected</option>
	<%
	
	for (int ii = 0; ii <  partyList.size(); ++ii) {
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(ii);
		
		String selected = "";
		if (bpi.getId().equals(selectedSecond)){
			selected = "selected";
		}
  	%>
<option value="<%= bpi.getId() %>"><%= bpi.getName() %></option>
      <% } %>
      </select>
      </label></td>
    <td width="33%" valign="top">Third Choice:<br>
      <label>
          <select name="third_choice" id="select">
          <option value="0">None Selected</option>
	<%
	
	for (int ii = 0; ii <  partyList.size(); ++ii) {
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(ii);
  	%>
<option value="<%= bpi.getId() %>"><%= bpi.getName() %></option>
      <% } %>
      </select>
      </label></td>
  </tr>
  <tr valign="top">
    <td colspan="3" valign="top"><div align="center">
      
        <label>
          <input type="submit" name="button" id="button" value="Submit">
          </label>
      
      </div></td>
  </tr>
</table>
</form>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
</body>
</html>
