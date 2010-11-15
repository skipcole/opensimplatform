<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
%>
<html>
<head>
<title>Finger</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>

<h1>Errors</h1>
<ul>
  <li>E: Email sent (Email related errors may not be sucessfully emailed.) </li>
  <li>D: Stored in database (Database related errors may not be sucessfully stored in the database.) </li>
</ul>
<h3>Current Errors </h3>
<table width="100%" border="1">
<tr>
      
      <td width="5%"><strong>Date</strong></td>
      <td width="12%"><strong>Class</strong></td>
      <td width="16%"><strong>Error</strong></td>
      <td width="61%"><strong>Error Conditions </strong></td>
      <td width="2%"><strong>E</strong></td>
      <td width="2%"><strong>D</strong></td>
      <td width="2%">M</td>
</tr>
<tr>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td><form name="form1" method="post" action="">
    <label>
      <input type="submit" name="Submit" value="|">
      </label>
  </form>
  </td>
</tr>
</table>
<h3>&nbsp;</h3>
<h3>Previous Errors </h3>
<table width="100%" border="1">
  <tr>
    <td width="5%"><strong>Date</strong></td>
    <td width="12%"><strong>Class</strong></td>
    <td width="16%"><strong>Error</strong></td>
    <td width="61%"><strong>Error Conditions </strong></td>
    <td width="2%"><strong>E</strong></td>
    <td width="2%"><strong>D</strong></td>
    <td width="2%">M</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td><form name="form1" method="post" action="">
      <label>
        <input type="submit" name="Submit2" value="|">
        </label>
    </form></td>
  </tr>
</table>
<p></p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p></p>
</body>
</html>