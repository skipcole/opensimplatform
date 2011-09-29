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
	
	// Move all of the above to a java file AFSO
	String toggle_e = (String) request.getParameter("toggle_e");
	
	
	if ((toggle_e != null) && (toggle_e.equalsIgnoreCase("true") ) ) {
		String e_id = (String) request.getParameter("e_id");
		
		OSPErrors ospError = OSPErrors.getById(new Long (e_id));
		if (ospError.isErrorProcessed()){
			ospError.setErrorProcessed(false);
		} else {
			ospError.setErrorProcessed(true);
		}
		ospError.saveMe();
	}
	
%>
<html>
<head>
<title>View Errors</title>
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
  <td width="5%">id</td>
      
      <td width="5%"><strong>Date</strong></td>
      <td width="12%"><strong>Class</strong></td>
      <td width="16%"><strong>Error</strong></td>
      <td width="61%"><strong>Error Conditions </strong></td>
      <td width="2%"><strong>E</strong></td>
      <td width="2%"><strong>D</strong></td>
      <td width="2%">M</td>
</tr>
<% 
	List unprocessedErrors = OSPErrors.getAllErrors(false);
	
  		for (ListIterator li = unprocessedErrors.listIterator(); li.hasNext();) {	
			OSPErrors upE = (OSPErrors) li.next();
%>
<tr>
  <td valign="top"><%= upE.getId() %></td>
  <td valign="top">&nbsp;</td>
  <td valign="top">&nbsp;</td>
  <td valign="top">&nbsp;</td>
  <td valign="top">&nbsp;</td>
  <td valign="top">&nbsp;</td>
  <td valign="top">&nbsp;</td>
  <td valign="top"><form name="form1" method="post" action="errors.jsp">
  	<input type="hidden" name="toggle_e" value="true" />
	<input type="hidden" name="e_id" value="<%= upE.getId() %> %>" />
    <label>
      <input type="submit" name="Submit" value="|">
      </label>
  </form>  </td>
</tr>
<% } // end of loop over unprccessed errors. %>
</table>
<h3>&nbsp;</h3>
<h3>Previous Errors </h3>
<table width="100%" border="1">
  <tr>
    <td width="5%">id</td>
    <td width="5%"><strong>Date</strong></td>
    <td width="12%"><strong>Class</strong></td>
    <td width="16%"><strong>Error</strong></td>
    <td width="61%"><strong>Error Conditions </strong></td>
    <td width="2%"><strong>E</strong></td>
    <td width="2%"><strong>D</strong></td>
    <td width="2%">M</td>
  </tr>
<% 
	List processedErrors = OSPErrors.getAllErrors(true);
	
  		for (ListIterator li = processedErrors.listIterator(); li.hasNext();) {	
			OSPErrors upE = (OSPErrors) li.next();
%>
  <tr>
    <td valign="top"><%= upE.getId() %></td>
    <td valign="top">&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td valign="top"><form name="form1" method="post" action="errors.jsp">
  	<input type="hidden" name="toggle_e" value="true" />
	<input type="hidden" name="e_id" value="<%= upE.getId() %> %>" />
    <label>
      <input type="submit" name="Submit" value="|">
      </label>
  </form>  </td>
  </tr>
<% } // end of loop over processed errors. %>
</table>
<p></p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p></p>
</body>
</html>