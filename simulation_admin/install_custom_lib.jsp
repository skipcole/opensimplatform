<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if ( (pso.checkDatabaseCreated()) &&  (!(pso.isLoggedin()))) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String error_msg = pso.handleCreateDB(request);
	
	String load = request.getParameter("load");
	
	if ((load != null) && (load.equalsIgnoreCase("true"))){
		String filename = request.getParameter("filename");
		
		System.out.println("loading filename " + filename);
		BaseSimSection.readCustomSectionsFromADir(pso.schema, filename);
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
    <td colspan="3"> <form action="../osp_install/install_db.jsp" method="post" name="form1" id="form1">
        <h1>
          Install Custom Library</h1>

        <blockquote>
          <p>Below are listed the custom libraries found on this system.</p>
  <p> 
	<% for (ListIterator li = FileIO.getListOfCustomLibraries().listIterator(); li.hasNext();) {
			String cust_lib_name = (String) li.next(); %>
    		Load:<a href="install_custom_lib.jsp?load=true&filename=<%= cust_lib_name %>"> <%= cust_lib_name %></a><br />
	<% } %>
  </p>

        </blockquote>
        <p><font color="#FF0000"><%= error_msg %></font></p>
        <p><a href="simulation_admin.jsp">&lt;-- Back</a></p>
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
