<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin()) || (!(afso.isAdmin()))) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	List userList = null;
	
	boolean didSearch = false;
	String do_search = (String) request.getParameter("do_search");
	
	if ((do_search != null) && (do_search.equalsIgnoreCase("true"))) {
		String search_string = (String) request.getParameter("search_string");
		userList = BaseUser.searchUserByName(afso.schema, search_string);
		didSearch = true;
	}
	
	
%>
<h3>Search Existing Users</h3>
<form name="form1" method="post" action="user_search.jsp">
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

<% if (didSearch) { %>
<h3>Search Results</h3>
<% if (userList != null) { %>
<blockquote>
<%
    	for (ListIterator li = userList.listIterator(); li.hasNext();) {
			BaseUser bu = (BaseUser) li.next();  %>
		<%= bu.getUsername() %><br />
<%     	}  %>
<% } else { //end of if list is not null %>
No results
<% } // end of no results %>
<% } // end of if did search %>
</blockquote>