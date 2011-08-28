<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	afso.backPage = "add_editor.jsp";
	
	afso.addEditor(request);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>


<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.4.1.js"></script>
<script type="text/javascript" src="../third_party_libraries/jquery/jquery.autocomplete.js"></script>
<link rel="stylesheet" href="../third_party_libraries/jquery/jquery.autocomplete.css" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Add  Editors  <a href="../simulation_facilitation/helptext/assign_players_help.jsp" target="helpinright"></a></h1>
              <br />
      <p>Select an authorized simulation author from the list below to allow them to edit this simulation. </p>
      <table>
	  <% 
	
		// Get hashtable of current editors
		Hashtable currentSetOfEditors = SimEditors.getCurrentEditors(afso.schema, afso.sim_id);
		
		// create list of people who one can add to edit
	  	ArrayList usersList = new ArrayList();
		
		// Loop over all users, and add the non-assigned editors to the possible list of people to edit.
		 for (ListIterator li = User.getAll(afso.schema, true).listIterator(); li.hasNext();) {
			User user = (User) li.next();
			if (currentSetOfEditors.get(user.getId()) == null) {	
				usersList.add(user);
			}
		}
		
		// Sort the eligible editors found
		Collections.sort(usersList);
		
		for (ListIterator li = usersList.listIterator(); li.hasNext();) {
			
			User user = (User) li.next();
		%>
<tr>
<td><%= user.getBu_full_name() %> | </td>
<td>
		<form action="add_editor.jsp" method="post" name="form3" id="form3">
		  <input type="hidden" name="sending_page" value="add_editor" />
          <input type="hidden" name="user_id" value="<%= user.getId() %>" />
          <input type="hidden" name="sim_id" value="afso.sim_id" />
		  <%= user.getBu_username() %>
		  <input type="submit" name="command2" value="Add Editor" />
		</form></td></tr>
		<% } %>
        </table>
      </blockquote>
      <h1>&nbsp;</h1>
      <h1>Remove Editors <a href="../simulation_facilitation/helptext/assign_players_help.jsp" target="helpinright"></a></h1>
      <br />
      <p>Select a currently authorized editor below to remove them from this list.. </p>
      <table>
	<%
			List currentList = SimEditors.getAuthorizedUsers(afso.schema, afso.sim_id);
			Collections.sort(currentList);
			
			ArrayList listWithoutCurrentUser = new ArrayList();					
			for (ListIterator li = currentList.listIterator(); li.hasNext();) {
				User user = (User) li.next();
				
				if (!((user.getId().intValue() == afso.sim_id.intValue()))){
					listWithoutCurrentUser.add(user);
				}
			}
			
			for (ListIterator li = listWithoutCurrentUser.listIterator(); li.hasNext();) {
				User user = (User) li.next();
			
			%>
        <tr>
          <td><%= user.getBu_full_name() %></td>
          <td><form action="add_editor.jsp" method="post" name="form3" id="form3">
              <input type="hidden" name="sending_page" value="remove_editor" />
              <input type="hidden" name="user_id" value="<%= user.getId() %>" />
             | <%= user.getBu_username() %>
              <input type="submit" name="command" value="Remove this Editor" />
          </form></td>
        </tr>
        <% } %>
      </table>
      <p>&nbsp;</p>
      <p align="left"><a href="enter_basic_simulation_information.jsp">&lt;-- 
        Back</a></p>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>


</body>
</html>