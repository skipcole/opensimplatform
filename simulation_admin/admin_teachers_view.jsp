<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	if (!(afso.isAdmin())){
		response.sendRedirect("../login.jsp");
		return;
	}
	

	String admin_backdoor = request.getParameter("admin_backdoor");
	if ((admin_backdoor != null) && ( admin_backdoor.equalsIgnoreCase("true") ) ) {
		PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
		pso.user_id = afso.user_id;
		pso.schema = afso.schema;
		pso.setLoggedin(true);
		
		String sim_id = request.getParameter("sim_id");
		pso.sim_id = new Long(sim_id);
		
		String rs_id = request.getParameter("rs_id");
		pso.setRunningSimId( new Long(rs_id) );
		
		
		pso.handleLoadPlayerScenario(request);
	
		if (pso.forward_on) {
			pso.forward_on = false;
			response.sendRedirect("admin_players_view_enter.jsp");
			return;
		}
	
	}
	
%>
<html>
<head>
<title>Finger</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>

<h1>Teacher's View  </h1>
<p>Below you can enter into the OSP as any of the registered users. </p>
<p>&nbsp;</p>

<p>&nbsp;</p>      <p>Below are listed alphabetically by username all of the current sim creators 
        and administrators.</p>
      <blockquote> 
        <table width="80%" border="1" cellspacing="1" cellpadding="2">
          <tr> 
            <td><strong>Name</strong></td>
            <td><strong>Admin</strong></td>
            <td><strong>Author</strong></td>
            <td><strong>Facilitator</strong></td>
            <td>Become</td>
          </tr>
          <% for (ListIterator li = User.getAllAdminsSCandInstructors(afso.schema).listIterator(); li.hasNext();) {
			User user = (User) li.next(); %>
          <tr> 
            <td><a href="create_admin_user.jsp?command=Edit&u_id=<%= user.getId().toString() %>"><%= user.getBu_username() %></a></td>
            <td><%= user.isAdmin() %></td>
            <td><%= user.isSim_author() %></td>
            <td><%= user.isSim_instructor() %></td>
            <td>(not implemented) </td>
          </tr>
          <% } %>
        </table>
        <p>&nbsp;</p>

<p>&nbsp;</p>
</blockquote>
<p>&nbsp;</p>
<p></p>
</body>
</html>