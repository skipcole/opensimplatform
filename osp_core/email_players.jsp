<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

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
<h1>Email Players </h1>
<p>Select the players below to email.</p>
<blockquote>
<form action="change_player.jsp" method="post" name="form3" id="form3">
<table border="1">
        <tr valign="top"> 
          <td ><strong>Actor</strong></td>
            <td ><strong>User Currently Assigned </strong></td>
            <td ><strong>Email Address to Contact </strong></td>
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
            <td><%= act.getActorName() %></td>
              <td><%= user_assigned.getBu_username() %></td>
              <td>
              <input name="user_to_add_to_simulation" type="text" style="width: 200px;" value="<%= user_assigned.getBu_username() %>"  tabindex="<%= ii %>"/>              </td>
      </tr>
        <%
				ii += 2;
		  	}
			// End of loop over results set of Actors
		%>
  </table>
  <p>
  Email to be sent:  </p>
  <p>
    <label>
    <textarea name="textarea"></textarea>
    </label>
  </p>
  <p>
    <label>
    <input type="submit" name="Submit" value="Send Email">
    </label>
  </p>
</form>
</blockquote>
<p>&nbsp;</p>
<p>&nbsp;</p>
<blockquote>&nbsp;</blockquote>

</body>
</html>