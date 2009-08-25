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

<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3">
        <h1>List of Schema Installed</h1>
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
        <p><font color="#FF0000"></font></p>
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
