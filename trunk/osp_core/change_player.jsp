<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
	pso.handleAssignUser(request);
	
	List userList = null;
	String do_search = (String) request.getParameter("do_search");
	
	if ((do_search != null) && (do_search.equalsIgnoreCase("true"))) {
		String search_string = (String) request.getParameter("search_string");
		userList = BaseUser.searchUserByName(pso.schema, search_string);
	}
		
%>
<html>
<head>
<title>Create Actor</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.4.1.js"></script>
<script type="text/javascript" src="../third_party_libraries/jquery/jquery.autocomplete.js"></script>
<link rel="stylesheet" href="../third_party_libraries/jquery/jquery.autocomplete.css" type="text/css" />
<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>
</head>

<body onLoad="">
<h1>Change Players </h1>
<p align="center"><span class="style1"><%= pso.errorMsg %></span>
<% pso.errorMsg = ""; %></p>
<blockquote>
<table border="1">
        <tr valign="top"> 
          <td ><strong>Actor</strong></td>
            <td >Details</td>
            <td ><strong>User Currently Assigned </strong></td>
            <td ><strong>Enter User's Email* </strong></td>
            <td ><strong>Assign User</strong></td>
    </tr>
        <%
		
		int ii = 5;
		
			for (ListIterator li = simulation.getActors(pso.schema).listIterator(); li.hasNext();) {
				Actor act = (Actor) li.next();
				User user_assigned = UserAssignment.getUserAssigned(pso.schema, pso.getRunningSimId(), act.getId());
				
				UserAssignment ua = new UserAssignment();
				
				if (user_assigned == null) {
					user_assigned = new User();
					user_assigned.setBu_username("<font color=\"#FF0000\">Not Assigned</font>");
					
					ua = UserAssignment.getUserAssignment (pso.schema, pso.getRunningSimId(), act.getId());
				}

					%>
        <tr valign="top"> 
          <form action="change_player.jsp" method="post" name="form3" id="form3">
            <td><%= act.getActorName() %></td>
              <td><a href="../simulation_facilitation/view_sim_actor_assignment_notes.jsp?comingfrompso=true&act_id=<%= act.getId() %>">details</a></td>
              <td><%= user_assigned.getBu_username() %></td>
              <td>
              <input name="user_to_add_to_simulation" type="text" style="width: 200px;" value="" id="userNameAjax<%= act.getId() %>" class="userNameAjax<%= act.getId() %>" tabindex="<%= ii %>"/>              </td>
              <td> <input type="hidden" name="sending_page" value="assign_user_to_simulation" /> 
                <input type="hidden" name="actor_to_add_to_simulation" value="<%= act.getId() %>" /> 
                <input type="hidden" name="simulation_adding_to" value="<%= simulation.getId() %>" /> 
                <input type="hidden" name="running_simulation_adding_to" value="<%= pso.getRunningSimId() %>" /> 
                <input type="submit" name="command" value="Assign User" tabindex="<%= ii + 1 %>" /></td>
          </form>
    </tr>
        <%
				ii += 2;
		  	}
			// End of loop over results set of Actors
		%>
  </table>
</blockquote>
<p>* Type in the word 'remove' to remove a user assignment. </p>
<p>&nbsp;</p>
<blockquote>
<h2>Search for a User</h2>
<p>Use the form below to find a user's email address if you need it.  </p>
<form name="form1" method="post" action="">
<table width="70%" border="1">
  <tr>
    <td>Part of Their Name: </td>
    <td>
      <label>
        <input type="text" name="search_string">
        </label>    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><label>
	<input type="hidden" name="do_search" value="true">
      <input type="submit" name="Submit" value="Submit">
    </label></td>
  </tr>
</table>
</form>

<% if (userList != null) { %>
<h3>Search Results</h3>
<blockquote>
<%
    	for (ListIterator li = userList.listIterator(); li.hasNext();) {
			BaseUser bu = (BaseUser) li.next();  %>
		<%= bu.getUsername() %> <br />
<%     	}  %>

</blockquote>
</blockquote>

<% } // end if if search results not null %>

<script type="text/javascript">
function findValue(li) {
	if( li == null ) return alert("No match!");

	sValue = li.selectValue;

}

function selectItem(li) {
	findValue(li);
}

function formatItem(row) {
	return row[0];
}

<%
	for (ListIterator li = simulation.getActors(pso.schema).listIterator(); li.hasNext();) {
		Actor act = (Actor) li.next();
		
		/*
function lookupAjax(){
	var oSuggest = $("#userNameAjax< % = act.getId() % > ")[0].autocompleter;
	oSuggest.findValue();
	return false;
}

*/
%>



$("#userNameAjax<%= act.getId() %>").autocomplete(
	"autocomplete.jsp",
	{
delay:2,
minChars:1,
matchSubset:1,
matchContains:1,
cacheLength:10,
onItemSelect:selectItem,
onFindValue:findValue,
formatItem:formatItem,
autoFill:false
	}
);

<% } // End of loop over actor ids %>
</script>
</body>
</html>