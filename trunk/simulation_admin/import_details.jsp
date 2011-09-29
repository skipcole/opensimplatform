<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.sharing.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	// Load up details, if called for, to show user.
	ExperienceExportObject eeo = ExperienceExportObject.importExperienceDetails(request);
	
	// Load in experience
	ExperienceExportObject eeoFinal = ExperienceExportObject.importExperience(request, afso.schema);
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {
	color: #FF0000;
	font-weight: bold;
}
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
              <h1>Import Details</h1>
              <br />
              <form id="form1" name="form1" method="post" action="import_details.jsp">
  <table width="100%">
  <tr><td width="23%">Name:</td>
    <td width="77%"><%= eeo.getFileName() %></td>
    </tr>
  <tr>
    <td>Notes:</td>
    <td><%= eeo.getExportNotes() %></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><label>
      <input type="hidden" name="import" value="true" />
	  <input type="hidden" name="filename" value="<%= eeo.getFileName() %>" />
      <input type="submit" name="button" id="button" value="Continue with Import" />
      </label></td>
  </tr>
    </table>
      </form>
    <p>&nbsp;</p>    <p>&nbsp;</p>			</td>
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