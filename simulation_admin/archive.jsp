<%@ page contentType="text/html; charset=UTF-8" language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*" errorPage="" %>
<%

	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String resultsOfUserSave = "";
	String resultsOfUserAndSimSave = "";
	String resultsOfSimSave = "";
	
	String sending_page = (String) request.getParameter("sending_page");
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("archive_users"))){
		resultsOfUserSave = afso.handlePackageUsers();
	}
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("archive_all"))){
		resultsOfUserAndSimSave = afso.handlePackageUsersAndSimulations();
	}
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("archive_sims"))){
		resultsOfSimSave = afso.handlePackageSimulations();
	}

	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">

<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="828"><br />
              <h1>Archive Database</h1>
              <p>This top button is all you should need to select to save all of the users and simulations.</p>
              <table><tr><td valign="top">
              <form id="form1" name="form1" method="post" action="archive.jsp">
                <input type="hidden" name="sending_page" value="archive_all" />
                <input type="submit" name="button" id="button" value="Archive Users and Simulations" />
                </form>
                </td><td valign="top"><em><strong><%= resultsOfUserAndSimSave %></strong></em></td></tr></table>
                <p>&nbsp;</p>
                <p>&nbsp;</p>
                <p>&nbsp;                </p>
                
                <table><tr><td valign="top">
                <form id="form2" name="form2" method="post" action="archive.jsp">
                <input type="hidden" name="sending_page" value="archive_users" />
                                <label>
                                <input type="submit" name="button2" id="button2" value="Archive Users" />
                </label>
                                
                </form>
                </td>
                <td valign="top"><em><strong><%= resultsOfUserSave %></strong></em></td></tr>
                </table>
                <p>&nbsp;                </p>
                <label></label>
  
  				<table><tr><td valign="top">
                <form id="form3" name="form3" method="post" action="archive.jsp">
                <input type="hidden" name="sending_page" value="archive_sims" />
                <input type="submit" name="button3" id="button3" value="Archive Simulations" />
                </form>
                </td>
                <td valign="top"><em><strong><%= resultsOfSimSave %></strong></em></td></tr>
                </table>
                         
                <p>&nbsp;                </p>
                                <label>
                <input type="submit" name="button" id="button" value="Archive Running Simulations" disabled="disabled" />
                </label>
                <em><strong>functionality not 
                  yet implemented
                </p>
                </strong></em>
                <p>&nbsp;                </p>
              </form>
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

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
