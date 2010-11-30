<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.bishops.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

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
		partyList = BishopsPartyInfo.getAllForRunningSim(pso.schema, pso.getRunningSimId(), false);
		BishopsRoleVotes firstChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.getRunningSimId(), pso.user_id, 1);
		BishopsRoleVotes secondChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.getRunningSimId(), pso.user_id, 2);
		BishopsRoleVotes thirdChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.getRunningSimId(), pso.user_id, 3);
	
		if (firstChoice != null){
			selectedFirst = firstChoice.getBishopsPartyInfoId();
			System.out.println("first was: " + selectedFirst);
			
		}
		
		if (secondChoice != null){
			selectedSecond = secondChoice.getBishopsPartyInfoId();
			System.out.println("2 was: " + selectedSecond);
		}
		
		if (thirdChoice != null){
			selectedThird = thirdChoice.getBishopsPartyInfoId();
			System.out.println("3 was: " + selectedThird);
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
    <option value="0" <%= selected1None %>>None Selected</option>
	<%
	
	for (int ii = 0; ii <  partyList.size(); ++ii) {
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(ii);
		
		String selected = "";
		if (bpi.getId().equals(selectedFirst)){
			selected = "selected";
		}
  	%>
<option value="<%= bpi.getId() %>" <%= selected %>><%= bpi.getBPIName() %></option>
      <% } %>
      </select>
      </td>
    <td valign="top" width="33%">Second Choice:<br>
      <label>
          <select name="second_choice" id="select">
          <%
			String selected2None = "";
			if (selectedSecond == null) {
				selected2None = " selected ";
			}
		%>
    <option value="0" <%= selected2None %>>None Selected</option>
	<%
	
	for (int ii = 0; ii <  partyList.size(); ++ii) {
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(ii);
		
		String selected = "";
		if (bpi.getId().equals(selectedSecond)){
			selected = "selected";
		}
  	%>
<option value="<%= bpi.getId() %>" <%= selected %>><%= bpi.getBPIName() %></option>
      <% } %>
      </select>
      </label></td>
    <td width="33%" valign="top">Third Choice:<br>
      <label>
          <select name="third_choice" id="select">
          <%
			String selected3None = "";
			if (selectedThird == null) {
				selected3None = " selected ";
			}
		%>
    <option value="0" <%= selected3None %>>None Selected</option>
	<%
	
	for (int ii = 0; ii <  partyList.size(); ++ii) {
  		BishopsPartyInfo bpi = (BishopsPartyInfo) partyList.get(ii);
		
		
		String selected = "";
		if (bpi.getId().equals(selectedThird)){
			selected = "selected";
		}
  	%>
<option value="<%= bpi.getId() %>" <%= selected %>><%= bpi.getBPIName() %></option>
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
