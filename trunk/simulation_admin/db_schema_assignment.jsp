<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*" 
	errorPage="" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if ( (afso.checkDatabaseCreated()) &&  (!(afso.isLoggedin()))) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String error_msg = afso.handleCreateDB(request);
	
	String db_schema = afso.getClean(request, "db_schema");
    String db_user = afso.getClean(request, "db_user");
    String db_pass = afso.getClean(request, "db_pass");
	String db_loc = afso.getClean(request, "db_loc");
	String db_port = afso.getClean(request, "db_port");
	
	String admin_first = afso.getClean(request, "admin_first");
	String admin_middle = afso.getClean(request, "admin_middle");
	String admin_last = afso.getClean(request, "admin_last");
	
	String admin_full = afso.getClean(request, "admin_full");
	
	String admin_username = afso.getClean(request, "admin_username");
	
	String root_realname = afso.getClean(request, "root_realname");
	String rootpass1 = afso.getClean(request, "rootpass1");
    String rootpass2 = afso.getClean(request, "rootpass2");
    String rootemail1 = afso.getClean(request, "rootemail1");
    String rootemail2 = afso.getClean(request, "rootemail2");
        
    String email_smtp = afso.getClean(request, "email_smtp");
    String email_user = afso.getClean(request, "email_user");
    String email_pass = afso.getClean(request, "email_pass");
    String email_user_address = afso.getClean(request, "email_user_address");
        

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Open Simulation Platform Control Page</title>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(../Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform Installation</h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"></td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top"></td>
    <td colspan="1" valign="top"></td>
    <td width="194" align="right" valign="top"></td>
  </tr>
</table>
<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3"> <form action="../osp_install/install_db.jsp" method="post" name="form1" id="form1">
        <h1>
          <input type="hidden" name="sending_page" value="clean_db" />
          List of Schema Installed</h1>
		  <blockquote>
          <%
		  	List ghostList = SchemaInformationObject.getAll();
		  %>
          <table width="80%" border="0" cellspacing="2" cellpadding="1">
            <tr> 
              <td><strong>id</strong></td>
              <td><strong>Schema Name</strong></td>
              <td><strong>Organization</strong></td>
              <td><strong>Last Login</strong></td>
            </tr>
            <%
			  	for (ListIterator<SchemaInformationObject> li = ghostList.listIterator(); li.hasNext();) {
            		SchemaInformationObject this_sg = (SchemaInformationObject) li.next();
				%>
            <tr> 
              <td><%= this_sg.getId() %></td>
              <td><%= this_sg.getSchema_name() %></td>
              <td><%= this_sg.getSchema_organization() %></td>
              <td><%= this_sg.getLastLogin() %></td>
            </tr>
            <%
		  		} // End of loop over schemas
		  %>
          </table>
          <p>&nbsp;</p>
        </blockquote>
        <p><font color="#FF0000"><%= error_msg %></font></p>
      </form>
      <p>&nbsp;</p>
      <p>&nbsp;</p></td>
  </tr>
  <tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
</table>
<p>&nbsp;</p>

<p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP 
  Open Source Software Project</a>. </p>
</body>
</html>
<%
	
%>
