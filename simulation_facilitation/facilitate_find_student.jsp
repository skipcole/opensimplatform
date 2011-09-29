<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.coursemanagementinterface.*,	
	org.hibernate.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	List userList = null;

	boolean didSearch = false;
	String do_search = (String) request.getParameter("do_search");
	String search_string = (String) request.getParameter("search_string");
	
	if ((do_search != null) && (do_search.equalsIgnoreCase("true"))) {
		
		if ((search_string != null) && (search_string.trim().length() > 0)) {
			userList = BaseUser.searchByNameOrUsername(search_string);
			didSearch = true;
		}
	}
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>USIP Open Simulation Platform</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>
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
			  <h1>Find Student<a href="helptext/create_running_sim_help.jsp" target="helpinright"></a></h1>
			  <br />
            <blockquote>
              <h3>Search Existing Users</h3>
              <form action="facilitate_find_student.jsp" method="post" name="form1" id="form1">
                <table width="70%" border="1">
                  <tr>
                    <td>Part of Their Name: </td>
                    <td><label>
                      <input type="text" name="search_string" />
                    </label></td>
                  </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <td><label>
                      <input type="hidden" name="do_search" value="true" />
                      <input type="submit" name="Submit" value="Submit" />
                    </label></td>
                  </tr>
                </table>
              </form>
              <% if (didSearch) { %>
              <h3>&nbsp;</h3>
              <h3>Search Results</h3>
              
              <blockquote>
              <% if ((userList != null) && (userList.size() > 0)) { %>
              
                <%
    	for (ListIterator li = userList.listIterator(); li.hasNext();) {
			BaseUser bu = (BaseUser) li.next();  %>
                <%= bu.getFull_name() %> <%= bu.getUsername() %><br />
                <%     	}  %>
                <% } else { //end of if list is not null %>
                
                No results
                
  	<% } // end of no results %>
  <% } // end of if did search %>
              </blockquote>
              <p>&nbsp;</p>
	                      </blockquote>
<p align="left"><a href="misc_tools.jsp">&lt;-- Back</a></p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
<%
%>
