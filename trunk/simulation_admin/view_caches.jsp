<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*,java.util.*,org.usip.osp.networking.*" errorPage="../error.jsp" %>
<%

	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if ((!(afso.isLoggedin())) || (!(afso.isAdmin()))) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>View of Simulation Caches</h1>
              <h2>Autocomplete Username Cache</h2>
              <% 
			  	for (Enumeration e = USIP_OSP_Cache.getAutocompleteUserNames(afso.schema, request).keys(); e.hasMoreElements();) {
					String key = (String) e.nextElement();
			  %>
              <%= key %> <br />
              <% } %>
              <h2>&nbsp;</h2>
              <h2>&nbsp;</h2>
              <h2>User Id Cache</h2>
              <p>Id  for <%= afso.userDisplayName %> is: <%= USIP_OSP_Cache.getUserIdByName(afso.schema, request, afso.user_name) %></p>
              <form id="form1" name="form1" method="post" action="view_cache.jsp">
                <label>
                <input type="hidden" name="sending_page" value="reset" />
                  </label>
              </form>
              <p>              
              <p>Some caches are kept on keyed on schema and running simulations              
              <p><br />
                
              <p>&nbsp;</p></td>
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

<p align="center">&nbsp;</p>
</body>
</html>
